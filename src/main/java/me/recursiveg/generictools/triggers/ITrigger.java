package me.recursiveg.generictools.triggers;

import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Every trigger has an inner class "Listener"
 * which listen on bukkit events and search for
 */
public interface ITrigger extends ISerializable {
    @Override
    default void serialize(ConfigurationSection config) {
        ISerializable.serialize(config, this);
        config.set("__class__", this.getClass().getCanonicalName());
    }
}
