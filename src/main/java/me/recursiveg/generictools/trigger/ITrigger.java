package me.recursiveg.generictools.trigger;

import me.recursiveg.generictools.function.IFunction;
import me.recursiveg.generictools.runtime.WrappedEvent;
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

    // Triggers simply pass down the events by default, i.e. pass to whatever function linked to it.
    @Override
    default WrappedEvent<?> accept(WrappedEvent<?> we) {
        return we;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Trigger {
        String value(); // trigger name
    }
}
