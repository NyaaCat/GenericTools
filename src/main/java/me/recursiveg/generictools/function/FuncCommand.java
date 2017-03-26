package me.recursiveg.generictools.function;

import me.recursiveg.generictools.runtime.WrappedItemStack;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;

@IFunction.Function(FuncCommand.NAME)
public class FuncCommand implements IFunction {
    public static final String NAME = "command";

    @Serializable(name = "command")
    public String commandString;

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public Event accept(Event e, WrappedItemStack wis) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), commandString);
        return e;
    }
}