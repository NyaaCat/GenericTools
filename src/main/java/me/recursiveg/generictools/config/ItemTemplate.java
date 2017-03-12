package me.recursiveg.generictools.config;

import cat.nyaa.utils.ISerializable;
import me.recursiveg.generictools.function.IFunction;
import me.recursiveg.generictools.trigger.ITrigger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTemplate implements ISerializable {
    @Serializable
    public ItemStack item = new ItemStack(Material.AIR);
    @Serializable(manualSerialization = true)
    public Map<Integer, ITrigger> triggers = new HashMap<>();
    @Serializable(manualSerialization = true)
    public Map<Integer, IFunction> functions = new HashMap<>();
    @Serializable(manualSerialization = true)
    public Map<Integer, List<Integer>> triggerToFunctionPath = new HashMap<>();
    @Serializable(manualSerialization = true)
    public Map<Integer, List<Integer>> functionToFunctionPath = new HashMap<>();

    @Override
    public void deserialize(ConfigurationSection config) {
        item = config.getItemStack("item");
        ConfigurationSection sec;
        sec = config.getConfigurationSection("triggers");
        for (String key : sec.getKeys(false)) {
            int idx = Integer.parseInt(key);

        }
        ISerializable.deserialize(config, this);
    }

    @Override
    public void serialize(ConfigurationSection config) {
        //ISerializable.serialize(config, this);
        config.set("item", item);
        ConfigurationSection sec = config.createSection("triggers");
        for (Map.Entry<Integer, ITrigger> e : triggers.entrySet()) {
            e.getValue().serialize(sec.createSection(Integer.toString(e.getKey())));
        }
        sec = config.createSection("functions");
        for (Map.Entry<Integer, IFunction> e : functions.entrySet()) {
            e.getValue().serialize(sec.createSection(Integer.toString(e.getKey())));
        }
        sec = config.createSection("trig->func");
        for (Map.Entry<Integer, List<Integer>> e : triggerToFunctionPath.entrySet()) {
            sec.set(Integer.toString(e.getKey()), e.getValue());
        }
        sec = config.createSection("func->func");
        for (Map.Entry<Integer, List<Integer>> e : functionToFunctionPath.entrySet()) {
            sec.set(Integer.toString(e.getKey()), e.getValue());
        }
    }
}
