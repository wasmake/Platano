package me.makecode.platano.core.dependency;

import java.lang.annotation.*;

/**
 * Annotation to indicate a required library for a class.
 */
@Documented
@Repeatable(MavenLibraries.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MavenLibrary {
    /**
     * The artifact id of the library
     *
     * @return the artifact id of the library
     */
    String artifactId();

    /**
     * The repo where the library can be obtained from
     *
     * @return the repo where the library can be obtained from
     */
    Repository repo() default @Repository(url = "https://repo1.maven.org/maven2");

}