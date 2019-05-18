package me.recursiveg.generictools;

import cat.nyaa.nyaacore.CommandReceiver;
import cat.nyaa.nyaacore.LanguageRepository;
import cat.nyaa.nyaacore.Pair;
import me.recursiveg.generictools.config.ItemTemplate;
import me.recursiveg.generictools.runtime.GenericToolInstance;
import me.recursiveg.generictools.triggers.RightClickAirTrigger;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHandler extends CommandReceiver {
    private final GenericTools plugin;

    public CommandHandler(GenericTools plugin, LanguageRepository i18n) {
        super(plugin, i18n);
        this.plugin = plugin;
    }

    private static List<String> getEventChainDiagram(ItemTemplate template) {
        return Collections.singletonList("[[Not Implemented]]");
    }

    @Override
    public String getHelpPrefix() {
        return "";
    }

    @SubCommand("debug")
    public void debugCommand(CommandSender sender, Arguments args) {
        ItemStack item = new ItemStack(Material.STICK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("The Generic Crowbar");
        item.setItemMeta(meta);

        ItemTemplate template = new ItemTemplate();
        template.item = item;
        template.actions = Collections.singletonList(Pair.of(new RightClickAirTrigger(), "command(ctx, \"say hello\")"));
        plugin.cfg.items.itemMap.put("test", template);
        plugin.cfg.items.save();

        GenericToolInstance gti = GenericToolInstance.fromTemplate("test");
        asPlayer(sender).getLocation().getWorld().dropItem(asPlayer(sender).getEyeLocation(), gti.getItemStack());
    }

    public void printSelf(int i) {
        plugin.getLogger().info("i=" + String.valueOf(i) + " self=" + this.toString());
        throw new RuntimeException("???");
    }

    @SubCommand(value = "create", permission = "gt.command")
    public void createItemTemplate(CommandSender sender, Arguments args) {
        String name = args.next();
        if (name == null) throw new BadCommandException("user.missing_template_name");

        ItemStack item; // cloned item
        try {
            item = getItemInHand(sender).clone();
        } catch (RuntimeException ex) {
            msg(sender, "user.create.default_template");
            item = new ItemStack(Material.STICK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("The Generic Crowbar");
            item.setItemMeta(meta);
        }
        ItemTemplate template = new ItemTemplate();
        template.item = item;
        plugin.cfg.items.itemMap.put(name, template);
        plugin.cfg.items.save();
    }

    private ItemTemplate nextItemTemplate(Arguments args) {
        String str = args.next();
        if (str == null) throw new BadCommandException("user.missing_template_name");
        ItemTemplate ret = plugin.cfg.items.itemMap.get(str);
        if (ret == null) throw new BadCommandException("user.invalid_template_name", str);
        return ret;
    }

    /**
     * Take in a list of integers.
     * If the `end` term is specified, it will also be consumed.
     * If null specified for end, it will consume until the end of the argument.
     *
     * @param args commandline arguments
     * @param end  end term, can be null
     * @return a list of integers.
     */
    private List<Integer> nextIntegerList(Arguments args, String end) {
        List<Integer> list = new ArrayList<>();
        if (end == null) {
            while (args.top() != null) list.add(args.nextInt());
        } else {
            while (args.top() != null && !end.equals(args.top())) list.add(args.nextInt());
            if (args.top() != null) args.nextAssert(end);
        }
        return list;
    }
}
