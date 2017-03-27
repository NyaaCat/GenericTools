package me.recursiveg.generictools.function;

import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.runtime.WrappedEvent;
import org.bukkit.Bukkit;

/**
 * Probably the most powerful Function
 * bukkitServer and the Event will be passed to javascript interpreter
 * Use with caution
 * <p>
 * Standard main() method looks like this:
 * <pre>
 *     function main(wrappedEvent, scriptConfigString, bukkitServer) {
 *         ...
 *         return wrappedEvent;
 *         // never return null
 *     }
 * </pre>
 */
@IFunction.Function(FuncJavascript.NAME)
public class FuncJavascript implements IFunction {
    public static final String NAME = "javascript";
    public static final String MAIN_FUNC = "main";


    @Serializable(name = "script_name")
    String scriptName;
    @Serializable(name = "script_config")
    String scriptConfig;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public WrappedEvent<?> accept(WrappedEvent<?> we) {
        try {
            return (WrappedEvent<?>) GenericTools.instance.script.getScript(scriptName).invokeFunction(MAIN_FUNC, we, scriptConfig, Bukkit.getServer());
        } catch (Exception ex) {
            ex.printStackTrace();
            GenericTools.instance.i18n.severe("admin.script.rte", scriptName, we.event.toString());
            throw new RuntimeException(ex);
        }
    }
}
