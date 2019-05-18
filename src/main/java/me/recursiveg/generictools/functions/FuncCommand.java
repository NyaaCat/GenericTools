package me.recursiveg.generictools.functions;

import me.recursiveg.generictools.runtime.ExecutionContext;
import org.bukkit.Bukkit;

@IFunction.Name(FuncCommand.NAME)
public abstract class FuncCommand implements IFunction {
    public static final String NAME = "command";

    @CallableFunction(NAME)
    public static final GtFunctionInterface EXECUTOR = (ctx, args) -> FuncCommand.execute(ctx, (String) args[0]);

    public static ExecutionContext execute(ExecutionContext ctx, String cmd) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
        return ctx;
    }

}
