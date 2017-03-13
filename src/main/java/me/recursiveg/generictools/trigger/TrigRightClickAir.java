package me.recursiveg.generictools.trigger;

import me.recursiveg.generictools.runtime.EventDispatcher;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@ITrigger.Trigger(TrigRightClickAir.NAME)
public class TrigRightClickAir implements ITrigger {
    public static final String NAME = "right_click_air";

    @Override
    public String name() {
        return NAME;
    }

    @EventHandler
    public void onRightClickAir(PlayerInteractEvent ev) {
        if (ev.getAction() == Action.RIGHT_CLICK_AIR) {
            EventDispatcher.dispatch(ev, ev.getItem(), this.getClass());
        }
    }
}
