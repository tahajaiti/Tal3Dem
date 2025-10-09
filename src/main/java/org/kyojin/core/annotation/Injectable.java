package org.kyojin.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Injectable {
    enum Scope {
        SINGLETON,
        FACTORY
    }

    Scope scope() default Scope.SINGLETON;
}
