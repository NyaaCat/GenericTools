package me.recursiveg.generictools.function;

import cat.nyaa.utils.ISerializable;
import me.recursiveg.generictools.config.ItemTemplate;
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
    /**
     * Dynamically decide which accept() method to call
     * Or override this method to accept everything
     *
     * @param e
     * @param template
     * @param itemStack
     * @return
     */
    default Event accept(Event e, ItemTemplate template, WrappedItemStack itemStack) {
        if (e == null) return null;
        Class<? extends Event> cls = e.getClass();
        List<Method> candidates = new ArrayList<>();
        for (Method m : this.getClass().getDeclaredMethods()) {
            if (m.isAnnotationPresent(Acceptor.class)) {
                if (m.getParameterCount() == 1 && m.getReturnType() == Event.class) {
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
            Object ret = topm.invoke(this, e);
            return (Event) ret;
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

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
