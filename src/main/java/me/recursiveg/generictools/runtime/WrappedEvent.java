package me.recursiveg.generictools.runtime;

import me.recursiveg.generictools.config.ItemTemplate;
import org.bukkit.event.Event;

import java.util.Map;

public class WrappedEvent<T extends Event> {
    public T event;
    public final WrappedItemStack wis;
    public final ItemTemplate itemTemplate;
    public final Map<String, String> loreDisplayParameters;
    /** The cancelled variable here doesn't control the event.
     *  It only prevents the wrappedEvent from passing down the event chain
     */
    public boolean cancelled = false;
    /* The limit on how many layers a chain could extend */
    public int ttl = 16;

    public WrappedEvent(T event, WrappedItemStack wis, ItemTemplate template, Map<String, String> ldp) {
        this.event = event;
        this.wis = wis;
        this.itemTemplate = template;
        this.loreDisplayParameters = ldp;
    }

    // get a clone for each chain fork
    protected WrappedEvent<T> getChainCopy() {
        if (this.ttl <= 0) throw new RuntimeException("chain fork TTL exceeded");
        WrappedEvent<T> ret = new WrappedEvent<T>(event, wis, itemTemplate, loreDisplayParameters);
        ret.cancelled = cancelled;
        ret.ttl = this.ttl - 1;
        return ret;
    }
}
