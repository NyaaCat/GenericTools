package me.recursiveg.generictools.runtime;

import de_tr7zw_itemnbtapi.NBTCompound;
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

    public WrappedItemStack(ItemStack item) {
        super(null, null);
        wrappedItemStack = item;
        shadowItemStack = item.clone();
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
        int amount = shadowItemStack.getAmount();
        if (amount < 0) amount = 0;
        wrappedItemStack.setAmount(amount);
        wrappedItemStack.setItemMeta(shadowItemStack.getItemMeta());
    }

    public ItemStack getRealItemStack() {
        return wrappedItemStack;
    }
}
