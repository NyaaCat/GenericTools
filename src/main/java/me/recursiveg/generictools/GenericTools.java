package me.recursiveg.generictools;

import me.recursiveg.generictools.runtime.FunctionManager;
import me.recursiveg.generictools.runtime.ScriptsManager;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class GenericTools extends JavaPlugin {
    public static final String PLUGIN_COMMAND_NAME = "generictools";
    public static GenericTools instance;

    public I18n i18n;
    public Configuration cfg;
    public CommandHandler cmd;
    public ScriptsManager script;
    public FunctionManager funcMgr;

    //private final List functions = new ArrayList();
    //private final Map<String, Listener> listeners = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        funcMgr = new FunctionManager(this);
        cfg = new Configuration(this);
        cfg.load();
        i18n = new I18n(this, cfg.language);
        cmd = new CommandHandler(this, i18n);
        script = new ScriptsManager(this);
        getCommand(PLUGIN_COMMAND_NAME).setExecutor(cmd);
        getCommand(PLUGIN_COMMAND_NAME).setTabCompleter((TabCompleter) cmd);
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        getCommand(PLUGIN_COMMAND_NAME).setExecutor(null);
        getCommand(PLUGIN_COMMAND_NAME).setTabCompleter(null);
        HandlerList.unregisterAll(this);
        cfg.save();
        i18n.reset();
    }
}
