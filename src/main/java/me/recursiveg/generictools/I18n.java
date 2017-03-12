package me.recursiveg.generictools;

import cat.nyaa.utils.Internationalization;
import org.bukkit.plugin.java.JavaPlugin;

public class I18n extends Internationalization {
    public static I18n instance = null;
    private String lang = null;
    private final GenericTools plugin;

    @Override
    protected JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    protected String getLanguage() {
        return lang;
    }

    public I18n(GenericTools plugin, String lang) {
        instance = this;
        this.plugin = plugin;
        this.lang = lang;
        load();
    }

    public void severe(String key, Object... args) {
        plugin.getLogger().severe(_(key, args));
    }

    public void warn(String key, Object... args) {
        plugin.getLogger().warning(_(key, args));
    }

    public void info(String key, Object... args) {
        plugin.getLogger().info(_(key, args));
    }

    public static String _(String key, Object... args) {
        return instance.get(key, args);
    }
}

