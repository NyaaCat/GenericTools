package me.recursiveg.generictools;

import cat.nyaa.nyaacore.LanguageRepository;
import org.bukkit.plugin.java.JavaPlugin;

public class I18n extends LanguageRepository {
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
        plugin.getLogger().severe(format(key, args));
    }

    public void warn(String key, Object... args) {
        plugin.getLogger().warning(format(key, args));
    }

    public void info(String key, Object... args) {
        plugin.getLogger().info(format(key, args));
    }

    public static String format(String key, Object... args) {
        return instance.getFormatted(key, args);
    }
}

