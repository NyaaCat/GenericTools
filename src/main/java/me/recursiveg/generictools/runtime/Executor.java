package me.recursiveg.generictools.runtime;

import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.functions.FuncCommand;
import me.recursiveg.generictools.functions.IFunction;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Executor {
    private static final List<Class<? extends IFunction>> FUNCTIONS;

    static {
        FUNCTIONS = new ArrayList<>();
        FUNCTIONS.add(FuncCommand.class);
    }

    private final GenericTools plugin;
    private ScriptEngine engine;

    public Executor(GenericTools plugin) {
        this.plugin = plugin;
        engine = new ScriptEngineManager().getEngineByName("nashorn");
        for (Class<? extends IFunction> cls : FUNCTIONS) {
            for (Field f : cls.getDeclaredFields()) {
                IFunction.CallableFunction anno = f.getAnnotation(IFunction.CallableFunction.class);
                if (anno != null) {
                    try {
                        IFunction.GtFunctionInterface iface = (IFunction.GtFunctionInterface) f.get(null);
                        engine.put(anno.value(), iface);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public void execute(GenericToolInstance gti, Map<String, ?> initVariables, String script) {
        ExecutionContext ctx = new ExecutionContext(gti, initVariables);
        engine.put("ctx", ctx);
        try {
            engine.eval(script);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
