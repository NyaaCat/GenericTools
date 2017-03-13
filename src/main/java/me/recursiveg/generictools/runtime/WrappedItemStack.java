package me.recursiveg.generictools.runtime;

import de_tr7zw_itemnbtapi.NBTCompound;
import me.recursiveg.generictools.config.ItemTemplate;
import org.bukkit.inventory.ItemStack;

/**
 * Adapted from tr7zw's ItemNBT API plugin
 * https://github.com/tr7zw/Item-NBT-API
 * 2017 March 13
 */
public class WrappedItemStack extends NBTCompound {
    // The actually associated item
    private final ItemStack wrappedItemStack;
    // A cloned item where all modification happens
    private ItemStack shadowItemStack;
    public final ItemTemplate template;

    public WrappedItemStack(ItemStack item, ItemTemplate template) {
        super(null, null);
        wrappedItemStack = item;
        shadowItemStack = item.clone();
        this.template = template;
    }

    @Override
    public ItemStack getItem() {
        return shadowItemStack;
    }

    @Override
    protected void setItem(ItemStack item) {
        shadowItemStack = item;
    }

    /**
     * Copy all metadata into the original ItemStack
     * since all modifications are done on the cloned item
     * issuing this call after all changes is necessary.
     */
    public void commit() {
        wrappedItemStack.setItemMeta(shadowItemStack.getItemMeta());
    }
}
