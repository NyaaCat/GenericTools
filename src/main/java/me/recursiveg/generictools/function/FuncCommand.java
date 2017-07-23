package me.recursiveg.generictools.function;

import cat.nyaa.nyaacore.CommandReceiver;
import me.recursiveg.generictools.I18n;
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

    @Override
    public String getInfoString() {
        return I18n.format("function_help.command.info_string", commandString);
    }
}