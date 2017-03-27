package me.recursiveg.generictools.runtime;

import me.recursiveg.generictools.config.ItemTemplate;
import org.bukkit.event.Event;

public class WrappedEvent<T extends Event> {
    public T event;
    public final WrappedItemStack wis;
    public final ItemTemplate itemTemplate;
    public boolean cancelled = false;

    public WrappedEvent(T event, WrappedItemStack wis, ItemTemplate template) {
        this.event = event;
        this.wis = wis;
        this.itemTemplate = template;
    }

    // get a clone for each chain fork
    protected WrappedEvent<T> getChainCopy() {
        WrappedEvent<T> ret = new WrappedEvent<T>(event, wis, itemTemplate);
        ret.cancelled = cancelled;
        return ret;
    }
}
