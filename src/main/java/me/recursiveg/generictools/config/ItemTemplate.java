package me.recursiveg.generictools.config;

import cat.nyaa.utils.ISerializable;
import me.recursiveg.generictools.GenericTools;
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
            ConfigurationSection s = sec.getConfigurationSection(key);
            ITrigger trig = GenericTools.instance.funcMgr.getTriggerInstance(s.getString("_name"));
            trig.deserialize(s);
            triggers.put(idx, trig);
        }
        sec = config.getConfigurationSection("functions");
        for (String key : sec.getKeys(false)) {
            int idx = Integer.parseInt(key);
            ConfigurationSection s = sec.getConfigurationSection(key);
            IFunction func = GenericTools.instance.funcMgr.getFunctionInstance(s.getString("_name"));
            func.deserialize(s);
            functions.put(idx, func);
        }
        sec = config.getConfigurationSection("trig->func");
        for (String key : sec.getKeys(false)) {
            int idx = Integer.parseInt(key);
            triggerToFunctionPath.put(idx, sec.getIntegerList(key));
        }
        sec = config.getConfigurationSection("func->func");
        for (String key : sec.getKeys(false)) {
            int idx = Integer.parseInt(key);
            functionToFunctionPath.put(idx, sec.getIntegerList(key));
        }
    }

    @Override
    public void serialize(ConfigurationSection config) {
        config.set("item", item);
        ConfigurationSection sec = config.createSection("triggers");
        for (Map.Entry<Integer, ITrigger> e : triggers.entrySet()) {
            ConfigurationSection s = sec.createSection(Integer.toString(e.getKey()));
            s.set("_name", e.getValue().name());
            e.getValue().serialize(s);
        }
        sec = config.createSection("functions");
        for (Map.Entry<Integer, IFunction> e : functions.entrySet()) {
            ConfigurationSection s = sec.createSection(Integer.toString(e.getKey()));
            s.set("_name", e.getValue().name());
            e.getValue().serialize(s);
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
