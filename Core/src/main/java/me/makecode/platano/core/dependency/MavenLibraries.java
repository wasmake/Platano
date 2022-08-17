package me.makecode.platano.core.dependency;

import java.lang.annotation.*;

/**
 * Annotation to indicate the required libraries for a class.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MavenLibraries {

    MavenLibrary[] value() default {};

}
