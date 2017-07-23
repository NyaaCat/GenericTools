package me.recursiveg.generictools;

import cat.nyaa.nyaacore.CommandReceiver;
import cat.nyaa.nyaacore.LanguageRepository;
import cat.nyaa.nyaacore.Message;
import me.recursiveg.generictools.config.ItemDatabase;
import me.recursiveg.generictools.config.ItemTemplate;
import me.recursiveg.generictools.function.FuncCommand;
import me.recursiveg.generictools.function.IFunction;
import me.recursiveg.generictools.runtime.WrappedItemStack;
import me.recursiveg.generictools.trigger.ITrigger;
import me.recursiveg.generictools.trigger.TrigRightClickAir;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommandHandler extends CommandReceiver {
    private final GenericTools plugin;

    public CommandHandler(GenericTools plugin, LanguageRepository i18n) {
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

    @SubCommand(value = "attach", permission = "gt.command")
    public void attachFunction(CommandSender sender, Arguments args) {
        ItemTemplate template = nextItemTemplate(args);
        IFunction func = getFunction(args.next(), args.next());
        func.parseCommandLine(args);
        template.attachFunction(func);
        plugin.cfg.items.save();
    }

    @SubCommand(value = "link", permission = "gt.command")
    public void linkFunctions(CommandSender sender, Arguments args) {
        ItemTemplate template = nextItemTemplate(args);
        String type = args.next();
        List<Integer> srcIdx = nextIntegerList(args, "to");
        args.next();
        List<Integer> dstIdx = nextIntegerList(args, null);
        if ("function".startsWith(type)) {
            for (Integer idx : srcIdx)
                if (!template.functions.containsKey(idx))
                    throw new BadCommandException("user.missing_function_idx", idx);
            for (Integer idx : dstIdx)
                if (!template.functions.containsKey(idx))
                    throw new BadCommandException("user.missing_function_idx", idx);
            for (Integer idx : srcIdx) {
                if (!template.functionToFunctionPath.containsKey(idx))
                    template.functionToFunctionPath.put(idx, new ArrayList<>());
                template.functionToFunctionPath.get(idx).addAll(dstIdx);
            }
            plugin.cfg.items.save();
        } else if ("trigger".startsWith(type)) {
            for (Integer idx : srcIdx)
                if (!template.triggers.containsKey(idx))
                    throw new BadCommandException("user.missing_function_idx", idx);
            for (Integer idx : dstIdx)
                if (!template.functions.containsKey(idx))
                    throw new BadCommandException("user.missing_function_idx", idx);
            for (Integer idx : srcIdx) {
                if (!template.triggerToFunctionPath.containsKey(idx))
                    template.triggerToFunctionPath.put(idx, new ArrayList<>());
                template.triggerToFunctionPath.get(idx).addAll(dstIdx);
            }
            plugin.cfg.items.save();
        } else {
            throw new BadCommandException("user.invalid_function_type", type);
        }
    }

    @SubCommand(value = "give", permission = "gt.command")
    public void giveToolToPlayer(CommandSender sender, Arguments args) {
        String name = args.top();
        ItemStack item = plugin.cfg.items.instantiate(args.next());
        if (item == null) throw new BadCommandException("user.invalid_template_name", name);

        if (args.top() == null) {
            Player p = asPlayer(sender);
            p.getLocation().getWorld().dropItem(p.getLocation(), item);
            return;
        }
        if ("to".equalsIgnoreCase(args.top())) {
            args.next();
            Player p = args.nextPlayer();
            p.getLocation().getWorld().dropItem(p.getLocation(), item);
            return;
        }
        int amount = args.nextInt();
        if ("to".equalsIgnoreCase(args.top())) {
            args.next();
            Player p = args.nextPlayer();
            for (int i = 0; i < amount; i++) {
                p.getLocation().getWorld().dropItem(p.getLocation(), item.clone());
            }
        } else {
            Player p = asPlayer(sender);
            for (int i = 0; i < amount; i++) {
                p.getLocation().getWorld().dropItem(p.getLocation(), item.clone());
            }
        }
    }

    @SubCommand(value = "inspect", permission = "gt.command")
    public void inspectItems(CommandSender sender, Arguments args) {
        if (args.top() == null) { // check item in hand
            ItemStack item = getItemInHand(sender);
            String name = ItemDatabase.getTemplateName(item);
            if (name == null) throw new BadCommandException("user.inspect.not_gt_item");
            ItemTemplate template = plugin.cfg.items.itemMap.get(name);
            if (template == null) throw new BadCommandException("user.inspect.not_gt_item");
            printInspect(template, name, sender);
            WrappedItemStack wis = new WrappedItemStack(item);
            printInspect(wis, sender);
        } else {
            String name = args.top();
            ItemTemplate template = nextItemTemplate(args);
            printInspect(template, name, sender);
        }
    }

    private void printInspect(ItemTemplate template, String templateName, CommandSender sender) {
        msg(sender, "user.inspect.msg_tmpl_header");
        msg(sender, "user.inspect.msg_tmpl_name", templateName);
        msg(sender, "user.inspect.msg_tmpl_desc", "Not Implemented");
        new Message("").append(I18n.format("user.inspect.msg_tmpl_tmpl"), template.item.clone());
        for (Integer idx : template.functions.keySet()) {
            msg(sender, "user.inspect.msg_tmpl_func", idx, template.functions.get(idx).name());
        }
        for (Integer idx : template.triggers.keySet()) {
            msg(sender, "user.inspect.msg_tmpl_trig", idx, template.triggers.get(idx).name());
        }
    }

    private void printInspect(WrappedItemStack wis, CommandSender sender) {
        msg(sender, "user.inspect.msg_item_header");
        for (String key : wis.getCompound("giSection").getKeys()) {
            msg(sender, "user.inspect.msg_item_nbt", "giSection." + key);
        }
        sender.sendMessage("WIS inspect WIP");
    }

    @SubCommand(value = "list", permission = "gt.command")
    public void listCommand(CommandSender sender, Arguments args) {
        String type = args.nextString();
        switch (type) {
            case "templates":
            case "tmpl":
                msg(sender, "user.list.tmpl_header");
                plugin.cfg.items.itemMap.keySet().stream().sorted().forEach(name -> {
                    ItemStack template = plugin.cfg.items.itemMap.get(name).item.clone();
                    new Message(I18n.format("user.list.tmpl_item", name)).append(template).send(sender);
                });
                break;
            case "functions":
            case "func":
                msg(sender, "user.list.func_header");
                plugin.funcMgr.functions.keySet().stream().sorted().forEach(name -> {
                    msg(sender, "user.list.func_item", name, I18n.format("function_help." + name + ".description"));
                });
                break;
            case "triggers":
            case "trig":
                msg(sender, "user.list.trig_header");
                plugin.funcMgr.triggers.keySet().stream().sorted().forEach(name -> {
                    msg(sender, "user.list.trig_item", name, I18n.format("trigger_help." + name + ".description"));
                });
                break;
            default:
                throw new BadCommandException("user.list.invalid_arg", type);
        }
    }

    private ItemTemplate nextItemTemplate(Arguments args) {
        String str = args.next();
        if (str == null) throw new BadCommandException("user.missing_template_name");
        ItemTemplate ret = plugin.cfg.items.itemMap.get(str);
        if (ret == null) throw new BadCommandException("user.invalid_template_name", str);
        return ret;
    }

    /**
     * @param type can only be "function" or "trigger"
     * @param name name of that function
     * @return the function
     * @throws BadCommandException if no function can be found
     */
    private IFunction getFunction(String type, String name) {
        if (type == null || name == null) throw new BadCommandException("user.missing_function");
        IFunction ret;
        if ("function".startsWith(type)) {
            ret = plugin.funcMgr.getFunctionInstance(name);
        } else if ("trigger".startsWith(type)) {
            ret = plugin.funcMgr.getTriggerInstance(name);
        } else {
            throw new BadCommandException("user.invalid_function_type", type);
        }
        if (ret == null) throw new BadCommandException("user.missing_function");
        return ret;
    }

    private List<Integer> nextIntegerList(Arguments args, String end) {
        List<Integer> list = new ArrayList<>();
        if (end == null) {
            while (args.top() != null) list.add(args.nextInt());
        } else {
            while (args.top() != null && !end.equals(args.top())) list.add(args.nextInt());
        }
        return list;
    }
}
