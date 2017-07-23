package me.recursiveg.generictools.runtime;

import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.function.FuncCommand;
import me.recursiveg.generictools.function.FuncConsume;
import me.recursiveg.generictools.function.FuncJavascript;
import me.recursiveg.generictools.function.IFunction;
import me.recursiveg.generictools.trigger.ITrigger;
import me.recursiveg.generictools.trigger.TrigRightClickAir;

import java.util.HashMap;
import java.util.Map;

/**
 * Although Functions & Triggers are treated differently
 * They should not share the same name
 */
public class FunctionManager {
    public static final Class BUILTIN_FUNCTIONS[] = {
            FuncJavascript.class,
            FuncCommand.class,
            FuncConsume.class
    };

    public static final Class BUILTIN_TRIGGERS[] = {
            TrigRightClickAir.class
    };


    private final GenericTools plugin;
    public final Map<String, Class<? extends IFunction>> functions = new HashMap<>();
    public final Map<String, Class<? extends ITrigger>> triggers = new HashMap<>();

    public FunctionManager(GenericTools plugin) {
        this.plugin = plugin;
        for (Class c : BUILTIN_FUNCTIONS) loadFunction(c);
        for (Class c : BUILTIN_TRIGGERS) loadFunction(c);
    }

    public void loadFunction(Class<?> funcCls) {
        if (IFunction.class.isAssignableFrom(funcCls) && funcCls.isAnnotationPresent(IFunction.Function.class)) {
            String name = funcCls.getAnnotation(IFunction.Function.class).value();
            if (functions.containsKey(name) || triggers.containsKey(name)) throw new RuntimeException("Duplicated function name: " + name);
            functions.put(name, (Class<? extends IFunction>) funcCls);
            return;
        }
        if (ITrigger.class.isAssignableFrom(funcCls) && funcCls.isAnnotationPresent(ITrigger.Trigger.class)) {
            String name = funcCls.getAnnotation(ITrigger.Trigger.class).value();
            if (functions.containsKey(name) || triggers.containsKey(name)) throw new RuntimeException("Duplicated trigger name: " + name);
            triggers.put(name, (Class<? extends ITrigger>) funcCls);
            return;
        }
        throw new RuntimeException("Illegal function class: " + funcCls.getName());
    }

    /**
     * NOTE: this function will also search in trigger map
     */
    public IFunction getFunctionInstance(String name) {
        Class<? extends IFunction> cls = functions.get(name);
        if (cls == null) cls = triggers.get(name);
        if (cls == null) return null;
        try {
            return cls.newInstance();
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

    public ITrigger getTriggerInstance(String name) {
        Class<? extends ITrigger> cls = triggers.get(name);
        if (cls == null) return null;
        try {
            return cls.newInstance();
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }

    public void registerTriggerListeners() {
        for (String name : triggers.keySet()) {
            plugin.getServer().getPluginManager().registerEvents(getTriggerInstance(name), plugin);
        }
    }
}
