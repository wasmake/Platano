package me.makecode.platano.core.dependency;

import lombok.SneakyThrows;
import org.apache.maven.model.Model;
import org.apache.maven.model.Repository;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class LibraryLoader {
    private Method ADD_URL_METHOD;
    private ClassLoader mainClassLoader;
    private Logger logger;
    private java.util.logging.Logger javaLogger;
    private File dataFolder;
    private List<org.apache.maven.model.Dependency> dependencyList;
    private List<Repository> repositories;
    
    public LibraryLoader(ClassLoader classLoader, Object logger, File dataFolder, String groupId, String artifactId){
        this.mainClassLoader = classLoader;
        if(java.util.logging.Logger.class.isAssignableFrom(logger.getClass())){
            this.javaLogger = (java.util.logging.Logger) logger;
        } else {
            this.logger = (Logger) logger;
        }
        this.dataFolder = dataFolder;
        loadPomDependencies(groupId, artifactId);
        try {
            ADD_URL_METHOD = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            ADD_URL_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void info(String message){
        if(logger != null){
            info(message);
            return;
        }
        javaLogger.info(message);
    }

    public void error(String message){
        if(logger != null){
            error(message);
            return;
        }
        javaLogger.severe(message);
    }

    @SneakyThrows
    public void loadPomDependencies(String groupId, String artifactId){
        String s = getClass().getResource("").getPath().split("!")[0];
        System.out.println(s);
        String[] parts =  s.split(Pattern.quote("/"));
        String plgName = parts[parts.length-1];
        System.out.println(plgName);
        File file = new File("plugins/" + plgName);

        JarFile jarFile = new JarFile(file.getAbsolutePath());

        final Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            final JarEntry entry = entries.nextElement();
            if (entry.getName().contains(".")) {
                if(entry.getName().equalsIgnoreCase("META-INF/maven/" + groupId + "/" + artifactId + "/pom.xml")){
                    System.out.println("File : " + entry.getName());
                    JarEntry fileEntry = jarFile.getJarEntry(entry.getName());
                    InputStream input = jarFile.getInputStream(fileEntry);
                    readMaven(input);
                }
            }
        }
    }

    public void readMaven(InputStream inputStream){
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            Model model = reader.read(inputStream);
            repositories = model.getRepositories();
            dependencyList = model.getDependencies();
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resolves all {@link MavenLibrary} annotations on the given object.
     *
     * @param object the object to load libraries for.
     */
    public void loadAll(Object object) {
        loadAll(object.getClass());
    }

    public org.apache.maven.model.Dependency getPomDependency(String artifactId){
        for(org.apache.maven.model.Dependency dependency : dependencyList){
            if(dependency.getArtifactId().equalsIgnoreCase(artifactId)) return dependency;
        }
        return null;
    }

    /**
     * Resolves all {@link MavenLibrary} annotations on the given class.
     *
     * @param clazz the class to load libraries for.
     */
    public void loadAll(Class<?> clazz) {
        MavenLibrary[] libs = clazz.getDeclaredAnnotationsByType(MavenLibrary.class);
        if (libs == null) {
            return;
        }

        for (MavenLibrary lib : libs) {
            org.apache.maven.model.Dependency dependency = getPomDependency(lib.artifactId());
            if(dependency == null){
                error("Dependency not found on the pom.xml!");
                return;
            }
            tryToLoad(dependency, lib);
        }
    }

    public void tryToLoad(org.apache.maven.model.Dependency dependency, MavenLibrary lib) {
        boolean loaded = false;
        info(String.format("Trying to load dependency %s:%s:%s", dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion()));
        if(lib.repo().url() == null || lib.repo().url() == ""){
            for(Repository repository : repositories) {
                loaded = load(dependency.getGroupId(), lib.artifactId(), dependency.getVersion(), repository.getUrl());
            }
        }
        else {
            loaded = load(dependency.getGroupId(), lib.artifactId(), dependency.getVersion(), lib.repo().url());
        }
        if(!loaded) error("Unable to download dependency " + dependency.getArtifactId() + " from any repository!");
    }

    public boolean load(String groupId, String artifactId, String version, String repoUrl) {
        return load(new Dependency(groupId, artifactId, version, repoUrl));
    }

    public boolean load(Dependency d) {
        info(String.format("Fetching dependency %s:%s:%s from %s", d.getGroupId(), d.getArtifactId(), d.getVersion(), d.getRepoUrl()));
        String name = d.getArtifactId() + "-" + d.getVersion();

        File saveLocation = new File(getLibFolder(), name + ".jar");
        if (!saveLocation.exists()) {

            try {
                 info("Dependency '" + name + "' is not already in the libraries folder. Attempting to download...");
                URL url = d.getUrl();

                try (InputStream is = url.openStream()) {
                    Files.copy(is, saveLocation.toPath());
                }

            } catch (Exception e) {
                return false;
            }

             info("Dependency '" + name + "' successfully downloaded.");
            return true;
        }

        if (!saveLocation.exists()) {
            return false;
        }

        URLClassLoader classLoader = (URLClassLoader) mainClassLoader;
        try {
            ADD_URL_METHOD.invoke(classLoader, saveLocation.toURI().toURL());
        } catch (Exception e) {
            return false;
        }

        info("Loaded dependency '" + name + "' successfully.");
        return true;
    }

    private File getLibFolder() {
        File pluginDataFolder = dataFolder;

        File libs = new File(pluginDataFolder,"libraries");
        libs.mkdirs();
        return libs;
    }

    public final class Dependency {
        private final String groupId;
        private final String artifactId;
        private final String version;
        private final String repoUrl;

        public Dependency(String groupId, String artifactId, String version, String repoUrl) {
            this.groupId = Objects.requireNonNull(groupId, "groupId");
            this.artifactId = Objects.requireNonNull(artifactId, "artifactId");
            this.version = Objects.requireNonNull(version, "version");
            this.repoUrl = Objects.requireNonNull(repoUrl, "repoUrl");
        }

        public String getGroupId() {
            return this.groupId;
        }

        public String getArtifactId() {
            return this.artifactId;
        }

        public String getVersion() {
            return this.version;
        }

        public String getRepoUrl() {
            return this.repoUrl;
        }

        public URL getUrl() throws MalformedURLException {
            String repo = this.repoUrl;
            if (!repo.endsWith("/")) {
                repo += "/";
            }
            repo += "%s/%s/%s/%s-%s.jar";

            String url = String.format(repo, this.groupId.replace(".", "/"), this.artifactId, this.version, this.artifactId, this.version);
            return new URL(url);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Dependency)) return false;
            final Dependency other = (Dependency) o;
            return this.getGroupId().equals(other.getGroupId()) &&
                    this.getArtifactId().equals(other.getArtifactId()) &&
                    this.getVersion().equals(other.getVersion()) &&
                    this.getRepoUrl().equals(other.getRepoUrl());
        }

        @Override
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + this.getGroupId().hashCode();
            result = result * PRIME + this.getArtifactId().hashCode();
            result = result * PRIME + this.getVersion().hashCode();
            result = result * PRIME + this.getRepoUrl().hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "LibraryLoader.Dependency(" +
                    "groupId=" + this.getGroupId() + ", " +
                    "artifactId=" + this.getArtifactId() + ", " +
                    "version=" + this.getVersion() + ", " +
                    "repoUrl=" + this.getRepoUrl() + ")";
        }
    }


}
