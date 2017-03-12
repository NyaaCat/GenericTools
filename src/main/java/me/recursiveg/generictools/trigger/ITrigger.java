package me.recursiveg.generictools.trigger;

import me.recursiveg.generictools.function.IFunction;
import org.bukkit.event.Listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Trigger serve two purposes:
 * 1. Work as EventListener for "dummy" triggers which are not associated with any ItemTemplate
 * 2. Work as Function, deciding if the associated item should actually accept this Event
 */
public interface ITrigger extends Listener, IFunction {

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Trigger {
        String value(); // trigger name

        boolean updateItem() default false;
    }
}
