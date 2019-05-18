package me.recursiveg.generictools.functions;

import me.recursiveg.generictools.runtime.ExecutionContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface IFunction {
    @FunctionalInterface
    interface GtFunctionInterface {
        ExecutionContext accept(ExecutionContext ctx, Object... args);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Name {
        String value();
    }

    /**
     * this field must have type {@link GtFunctionInterface}
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface CallableFunction {
        String value();
    }
}
