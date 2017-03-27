package me.recursiveg.generictools.function;

import cat.nyaa.utils.CommandReceiver;
import me.recursiveg.generictools.runtime.WrappedEvent;
import org.bukkit.Bukkit;

@IFunction.Function(FuncCommand.NAME)
public class FuncCommand implements IFunction {
    public static final String NAME = "command";

    @Serializable(name = "command")
    public String commandString;

    @Override
    public void parseCommandLine(CommandReceiver.Arguments args) {
        commandString = args.nextString();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public WrappedEvent<?> accept(WrappedEvent<?> we) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), commandString);
        return we;
    }
}