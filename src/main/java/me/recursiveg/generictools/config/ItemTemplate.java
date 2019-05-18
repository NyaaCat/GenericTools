package me.recursiveg.generictools.config;

import cat.nyaa.nyaacore.Pair;
import cat.nyaa.nyaacore.configuration.ISerializable;
import me.recursiveg.generictools.triggers.ITrigger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A template is a complete description about one particular tool/weapon.
 * Including material, damageValue, lores, enchants, Function chains, etc.
 */
public class ItemTemplate implements ISerializable {
    @Serializable
    public ItemStack item = new ItemStack(Material.AIR); // basic item stack
    @Serializable(manualSerialization = true)
    public List<Pair<ITrigger, String>> actions = new ArrayList<>(); // [(trigger,script)]

    @Override
    public void deserialize(ConfigurationSection config) {
        ISerializable.deserialize(config, this);
        ConfigurationSection sec = config.getConfigurationSection("actions");
        if (sec != null) {
            for (String key : sec.getKeys(false)) {
                try {
                    ITrigger trigger = (ITrigger) ISerializable.directDeserialize(sec.getConfigurationSection(key + ".trigger"));
                    String script = sec.getString(key + ".script");
                    actions.add(Pair.of(trigger, script));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void serialize(ConfigurationSection config) {
        ISerializable.serialize(config, this);
        ConfigurationSection sec = config.createSection("actions");
        for (int i = 0; i < actions.size(); i++) {
            sec.set(i + ".script", actions.get(i).getValue());
            actions.get(i).getKey().serialize(sec.createSection(i + ".trigger"));
        }
    }


}
