package me.makecode.platano.core.placeholder;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class PlaceholderRegistrar {

    private final Pattern PLACEHOLDER_PATTERN = Pattern.compile("(?<escape>\\\\)?\\%(?<name>[^{}]+)\\%");
    private Map<String, Method> properties;

    public PlaceholderRegistrar(){
        properties = new HashMap<>();
    }

    public void hook(Object object) {
        hook(object.getClass());
    }

    public void hook(Class<?> clazz) {
        for(Method method : clazz.getMethods()){
            if(method.isAnnotationPresent(Placeholder.class)){
                Placeholder placeholder = method.getAnnotation(Placeholder.class);
                if(method.getReturnType() == String.class){
                    registerPlaceholder(placeholder.placeholder(), method);
                }
            }
        }
    }

    public String substitute(String str, Object object) {
        Matcher matcher = PLACEHOLDER_PATTERN.matcher(str);

        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String replacement = matcher.group("escape") != null ?
                    matcher.group().substring(1) :
                    properties.containsKey(matcher.group("name")) ?
                            substitute(getValue(matcher.group("name"), object), object) :
                            matcher.group();
            matcher.appendReplacement(buffer, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(buffer);

        return buffer.toString();

    }

    private String getValue(String placeholder, Object object){
        try {
            return (String) properties.get(placeholder).invoke(this, object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void registerPlaceholder(String placeholder, Method method){
        properties.put(placeholder, method);
    }

}
