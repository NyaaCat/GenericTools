package me.recursiveg.generictools.config;

import cat.nyaa.nyaacore.configuration.FileConfigure;
import me.recursiveg.generictools.GenericTools;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ItemDatabase extends FileConfigure {
    private final GenericTools plugin;
    // Map<TemplateName, Template>: name is case sensitive
    @Serializable
    public Map<String, ItemTemplate> itemMap = new HashMap<>();

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
}