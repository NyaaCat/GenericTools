package me.recursiveg.generictools;

import cat.nyaa.utils.CommandReceiver;
import cat.nyaa.utils.Internationalization;
import org.bukkit.command.CommandSender;

public class CommandHandler extends CommandReceiver<GenericTools> {
    private final GenericTools plugin;

    public CommandHandler(GenericTools plugin, Internationalization i18n) {
        super(plugin, i18n);
        this.plugin = plugin;
    }

    @Override
    public String getHelpPrefix() {
        return "";
    }

    @SubCommand("debug")
    public void debugCommand(CommandSender sender, Arguments args) {

    }
}
