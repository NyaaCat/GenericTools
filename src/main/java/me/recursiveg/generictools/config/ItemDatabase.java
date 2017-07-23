package me.recursiveg.generictools.config;

import cat.nyaa.nyaacore.configuration.FileConfigure;
import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.runtime.WrappedItemStack;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Instantiated item NBT definition:
 * <pre>
 * giSection:
 *   templateName: {String, can be null}
 *   attachedTemplates: {List<String>, can be null or empty list} // TODO: attachable template hasn't implemented
 *   data: {Map<String, Object>, for Function-Specific storage, such as cooldown time or remaining durability}
 *     key1: data1
 *     key2: data2
 * </pre>
 * ...
 */
public class ItemDatabase extends FileConfigure {
    private final GenericTools plugin;

    public ItemDatabase(GenericTools plugin) {
        this.plugin = plugin;
    }

    @Override
    protected String getFileName() {
        return "items.yml";
    }

    @Override
    protected JavaPlugin getPlugin() {
        return plugin;
    }

    // Map<TemplateName, Template>: name is case sensitive
    @Serializable
    public Map<String, ItemTemplate> itemMap = new HashMap<>();

    /**
     * Look up the database for associated ItemTemplate for given ItemStack
     * Return null if not found
     *
     * @param stack the item
     * @return found template or null
     */
    public ItemTemplate findTemplate(ItemStack stack) {
        WrappedItemStack wis = new WrappedItemStack(stack);
        String name = wis.getString("giSection.templateName");
        if (name == null) return null;
        return itemMap.get(name);
    }

    public ItemStack instantiate(String name) {
        ItemTemplate template = itemMap.get(name);
        if (template == null) return null;
        ItemStack item = template.item.clone();
        WrappedItemStack wis = new WrappedItemStack(item);
        wis.setString("giSection.templateName", name);
        wis.commit();
        return item;
    }

    /**
     * Read the name field from item NBT.
     * Existence of template is not guaranteed.
     */
    static public String getTemplateName(ItemStack stack) {
        return new WrappedItemStack(stack).getString("giSection.templateName");
    }
}