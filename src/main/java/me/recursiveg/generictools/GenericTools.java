package me.recursiveg.generictools;

import me.recursiveg.generictools.runtime.Executor;
import me.recursiveg.generictools.runtime.GenericToolInstance;
import me.recursiveg.generictools.runtime.TriggersListener;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class GenericTools extends JavaPlugin {
    public static final String PLUGIN_COMMAND_NAME = "generictools";
    public static GenericTools instance;

    public I18n i18n;
    public Configuration cfg;
    public CommandHandler cmd;
    public Executor executor;
    public TriggersListener triggers;

    @Override
    public void onEnable() {
        instance = this;
        cfg = new Configuration(this);
        cfg.load();
        i18n = new I18n(this, cfg.language);
        cmd = new CommandHandler(this, i18n);
        getCommand(PLUGIN_COMMAND_NAME).setExecutor(cmd);
        getCommand(PLUGIN_COMMAND_NAME).setTabCompleter((TabCompleter) cmd);
        executor = new Executor(this);
        GenericToolInstance.initNamespacedKeys(this);
        triggers = new TriggersListener(this);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getCommand(PLUGIN_COMMAND_NAME).setExecutor(null);
        getCommand(PLUGIN_COMMAND_NAME).setTabCompleter(null);
        HandlerList.unregisterAll(this);
        cfg.save();
    }
}
