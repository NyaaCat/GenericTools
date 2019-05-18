package me.recursiveg.generictools.triggers;

import cat.nyaa.nyaacore.Pair;
import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.runtime.GenericToolInstance;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;

public class RightClickAirTrigger implements ITrigger {
    public static class L implements Listener {
        @EventHandler
        public void onRightClickAir(PlayerInteractEvent ev) {
            if (ev.getAction() == Action.RIGHT_CLICK_AIR) {
                GenericToolInstance gti = GenericToolInstance.fromItemStack(ev.getItem());
                if (gti == null) return;
                for (Pair<ITrigger, String> p : gti.getTemplate().actions) {
                    if (!(p.getKey() instanceof RightClickAirTrigger)) continue;
                    RightClickAirTrigger triggerInstance = (RightClickAirTrigger) p.getKey();
                    Map<String, Object> eventVariables = new HashMap<>();
                    eventVariables.put("__event", ev);
                    GenericTools.instance.executor.execute(gti, eventVariables, p.getValue());
                }
            }
        }
    }
}