package me.recursiveg.generictools.runtime;

import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.Utils;
import me.recursiveg.generictools.triggers.ITrigger;
import me.recursiveg.generictools.triggers.RightClickAirTrigger;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

import static me.recursiveg.generictools.Utils.Triple;

public class TriggersListener {
    private static final List<Utils.Triple<String, Class<? extends ITrigger>, Class<? extends Listener>>> TRIGGERS;

    static {
        TRIGGERS = new ArrayList<>();
        TRIGGERS.add(Triple.of("right_click_air", RightClickAirTrigger.class, RightClickAirTrigger.L.class));
    }

    public TriggersListener(GenericTools plugin) {
        for (Triple<String, Class<? extends ITrigger>, Class<? extends Listener>> t : TRIGGERS) {
            try {
                Bukkit.getPluginManager().registerEvents(t.r.newInstance(), plugin);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
