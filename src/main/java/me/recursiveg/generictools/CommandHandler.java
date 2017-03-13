package me.recursiveg.generictools;

import cat.nyaa.utils.CommandReceiver;
import cat.nyaa.utils.Internationalization;
import me.recursiveg.generictools.runtime.WrappedItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

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
        ItemStack item = getItemInHand(sender);
        WrappedItemStack wis = new WrappedItemStack(item);
        if ("write".equals(args.next())) {
            wis.setString("test.key", args.next());
            wis.commit();
            sender.sendMessage("Done");
        } else if (wis.hasKey("test.key")){
            sender.sendMessage("Key is: " + wis.getString("test.key"));
        } else {
            sender.sendMessage("Key not found.");
        }
    }
}
