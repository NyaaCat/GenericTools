package me.recursiveg.generictools;

import cat.nyaa.nyaacore.configuration.PluginConfigure;
import me.recursiveg.generictools.config.ItemDatabase;
import org.bukkit.plugin.java.JavaPlugin;

public class Configuration extends PluginConfigure {
    private final GenericTools plugin;

    public Configuration(GenericTools plugin) {
        this.plugin = plugin;
        items = new ItemDatabase(plugin);
    }

    @Override
    protected JavaPlugin getPlugin() {
        return plugin;
    }

    @Serializable
    public String language = "zh_CN";

    @StandaloneConfig
    public ItemDatabase items;
}
