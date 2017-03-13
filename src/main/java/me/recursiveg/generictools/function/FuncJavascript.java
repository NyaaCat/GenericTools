package me.recursiveg.generictools.function;

import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.runtime.WrappedItemStack;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * Probably the most powerful Function
 * bukkitServer and the Event will be passed to javascript interpreter
 * Use with caution
 * <p>
 * Standard main() method looks like this:
 * <pre>
 *     function main(event, scriptConfigString, wrappedItemStack, bukkitServer) {
 *         ...
 *         return event;
 *         // or return null to terminate the Function chain.
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
    public Event accept(Event e, WrappedItemStack wis) {
        try {
            return (Event) GenericTools.instance.script.getScript(scriptName).invokeFunction(MAIN_FUNC, e, scriptConfig, wis, Bukkit.getServer());
        } catch (Exception ex) {
            ex.printStackTrace();
            GenericTools.instance.i18n.severe("admin.script.rte", scriptName, e.toString());
            throw new RuntimeException(ex);
        }
    }
}
