package me.recursiveg.generictools.function;

import cat.nyaa.utils.ISerializable;
import me.recursiveg.generictools.runtime.WrappedItemStack;
import org.bukkit.event.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public interface IFunction extends ISerializable {

    String name();

    /**
     * Dynamically decide which accept() method to call
     * Or override this method to accept everything
     *
     * @return the event to be passed to next Function. Or `null` to terminate the chain.
     */
    default Event accept(Event e, WrappedItemStack wis) {
        if (e == null) return null;
        Class<? extends Event> cls = e.getClass();
        List<Method> candidates = new ArrayList<>();
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Acceptor.class)) {
                // Event foo(? extends Event ev, WrappedItemStack wis)
                if (m.getParameterCount() == 2 && m.getReturnType() == Event.class && m.getParameterTypes()[1] == WrappedItemStack.class) {
                    Class<?> c = m.getParameterTypes()[0];
                    if (c.isAssignableFrom(cls) && Event.class.isAssignableFrom(c)) {
                        candidates.add(m);
                    }
                }
            }
        }
        if (candidates.isEmpty()) return null;
        Method topm = candidates.get(0);
        if (candidates.size() > 1) {
            Class<?> top = topm.getParameterTypes()[0];
            for (int i = 1; i < candidates.size(); i++) {
                Class tmp = candidates.get(i).getParameterTypes()[0];
                if (top.isAssignableFrom(tmp)) {
                    top = tmp;
                    topm = candidates.get(i);
                }
            }
        }

        try {
            Object ret = topm.invoke(this, e, wis);
            return (Event) ret;
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

    /**
     * Every Acceptor method should have following signature:
     * Event foo(? extends Event ev, WrappedItemStack wis)
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Acceptor {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Function {
        String value(); // function name
    }
}
