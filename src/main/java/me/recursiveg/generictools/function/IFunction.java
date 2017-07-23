package me.recursiveg.generictools.function;

import cat.nyaa.nyaacore.CommandReceiver;
import cat.nyaa.nyaacore.configuration.ISerializable;
import me.recursiveg.generictools.I18n;
import me.recursiveg.generictools.runtime.WrappedEvent;
import me.recursiveg.generictools.trigger.ITrigger;
import org.bukkit.event.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

public interface IFunction extends ISerializable {

    String name();

    /**
     * parse the arguments and fill in local fields
     * Doing nothing by default
     *
     * @param args commandLine arguments
     */
    default void parseCommandLine(CommandReceiver.Arguments args) {
    }

    /**
     * Dynamically decide which accept() method to call
     * Or override this method to accept everything
     *
     * @return the event to be passed to next Function. Never null
     */
    default WrappedEvent<?> accept(WrappedEvent<?> we) {
        if (we == null) return null;
        Class<? extends Event> cls = we.event.getClass();
        Method acceptor = null;
        Class<? extends Event> acceptedType = null;
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Acceptor.class)) {
                Class<? extends Event> acceptableEventType = m.getAnnotation(Acceptor.class).value();
                if (m.getParameterCount() == 1 &&
                        m.getReturnType() == WrappedEvent.class &&
                        m.getParameterTypes()[0] == WrappedEvent.class &&
                        acceptableEventType.isAssignableFrom(cls)) {
                    // current method accepts this event
                    if (acceptor == null) {
                        acceptor = m;
                        acceptedType = acceptableEventType;
                        continue;
                    }
                    if (acceptedType.isAssignableFrom(acceptableEventType)) {
                        // current eventType is higher in inheritance
                        acceptor = m;
                        acceptedType = acceptableEventType;
                    }
                }
            }
        }
        if (acceptor == null) {
            we.cancelled = true;
            return we;
        }

        try {
            Object ret = acceptor.invoke(this, we);
            return (WrappedEvent<?>) ret;
        } catch (ReflectiveOperationException ex) {
            we.cancelled = true;
            return we;
        }
    }

    /**
     * Every Acceptor method should have following signature:
     * WrappedEvent<?> foo(WrappedEvent<?> ev)
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Acceptor {
        Class<? extends Event> value() default Event.class;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Function {
        String value(); // function name
    }

    default String getInfoString() {
        return I18n.format((this instanceof ITrigger? "trigger" : "function") + "_help." + name() + ".info_string");
    }
}
