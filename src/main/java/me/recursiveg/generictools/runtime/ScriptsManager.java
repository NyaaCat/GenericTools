package me.recursiveg.generictools.runtime;

import me.recursiveg.generictools.GenericTools;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Scripts used by FuncJavascript
 * TODO: reload command needed
 */
public class ScriptsManager {
    private final GenericTools plugin;
    private final Map<String, Invocable> scripts = new HashMap<>();

    public ScriptsManager(GenericTools plugin) {
        this.plugin = plugin;
        loadScripts();
    }

    public Invocable getScript(String name) {
        return scripts.get(name);
    }

    public void loadScripts() {
        try {
            File dir = new File(plugin.getDataFolder(), "scripts");
            dir.mkdirs();
            for (File f : dir.listFiles()) {
                try {
                    if (f.isFile() && f.getName().endsWith(".js")) {
                        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
                        engine.eval(new FileReader(f));
                        scripts.put(f.getName().substring(0, f.getName().length() - 3), (Invocable) engine);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    plugin.i18n.severe("admin.script.cannot_load_single", f.getName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            plugin.i18n.severe("admin.script.cannot_load");
            scripts.clear();
        }
    }
}
