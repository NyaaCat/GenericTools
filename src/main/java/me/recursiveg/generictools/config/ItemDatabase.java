package me.recursiveg.generictools.config;

import cat.nyaa.utils.FileConfigure;
import me.recursiveg.generictools.GenericTools;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ItemDatabase extends FileConfigure {
    private final GenericTools plugin;

    public ItemDatabase(GenericTools plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getFileName() {
        return "items.yml";
    }

    @Override
    protected JavaPlugin getPlugin() {
        return plugin;
    }

    // Map<TemplateName, Template>: name is case sensitive
    private Map<String, ItemTemplate> itemMap = new HashMap<>();

    @Override
    public void deserialize(ConfigurationSection config) {
        itemMap = new HashMap<>();
        for (String itemKey : config.getKeys(false)) {
            ItemTemplate template = new ItemTemplate();
            template.deserialize(config.getConfigurationSection(itemKey));
            itemMap.put(itemKey, template);
        }
    }

    @Override
    public void serialize(ConfigurationSection config) {
        for (String itemKey : itemMap.keySet()) {
            itemMap.get(itemKey).serialize(config.createSection(itemKey));
        }
    }

    /**
     * Look up the database for associated ItemTemplate for given ItemStack
     * Return null if not found
     *
     * @param stack the item
     * @return found template or null
     */
    public ItemTemplate findTemplate(ItemStack stack) {
        return null;
    }

}