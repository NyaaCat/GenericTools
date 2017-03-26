package me.recursiveg.generictools;

import cat.nyaa.utils.CommandReceiver;
import cat.nyaa.utils.Internationalization;
import me.recursiveg.generictools.config.ItemTemplate;
import me.recursiveg.generictools.function.FuncCommand;
import me.recursiveg.generictools.trigger.ITrigger;
import me.recursiveg.generictools.trigger.TrigRightClickAir;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

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
        asPlayer(sender);
        ITrigger trig = new TrigRightClickAir();
        FuncCommand cmd = new FuncCommand();
        cmd.commandString = "say hi";
        ItemStack item = new ItemStack(Material.STICK);
        ItemTemplate temp = new ItemTemplate();
        temp.item = item;
        temp.functions.put(0, cmd);
        temp.triggers.put(0, trig);
        temp.triggerToFunctionPath.put(0, Collections.singletonList(0));
        plugin.cfg.items.itemMap.put("test", temp);
        asPlayer(sender).getLocation().getWorld().dropItem(asPlayer(sender).getLocation(), plugin.cfg.items.instantiate("test"));
    }
}
