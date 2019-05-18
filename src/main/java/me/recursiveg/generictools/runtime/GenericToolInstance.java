package me.recursiveg.generictools.runtime;

import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.config.ItemTemplate;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class GenericToolInstance {
    private static GenericTools plugin;
    private static NamespacedKey TEMPLATE_NAME;

    private ItemStack it;
    private String templateName;
    private ItemTemplate template;


    public static void initNamespacedKeys(GenericTools plugin) {
        GenericToolInstance.plugin = plugin;
        TEMPLATE_NAME = new NamespacedKey(plugin, "template_name");
    }

    public static GenericToolInstance fromItemStack(ItemStack it) {
        if (it == null) return null;
        if (!it.hasItemMeta()) return null;
        ItemMeta m = it.getItemMeta();
        if (m == null) return null;
        if (!m.getPersistentDataContainer().has(TEMPLATE_NAME, PersistentDataType.STRING)) return null;
        String templateName = m.getPersistentDataContainer().get(TEMPLATE_NAME, PersistentDataType.STRING);
        ItemTemplate template = plugin.cfg.items.itemMap.get(templateName);
        if (template == null) return null;

        GenericToolInstance ret = new GenericToolInstance();
        ret.it = it;
        ret.templateName = templateName;
        ret.template = template;
        return ret;
    }

    public static GenericToolInstance fromTemplate(String templateName) {
        ItemTemplate template = plugin.cfg.items.itemMap.get(templateName);
        if (template == null) return null;

        ItemStack it = template.item.clone();
        ItemMeta m = it.getItemMeta();
        m.getPersistentDataContainer().set(TEMPLATE_NAME, PersistentDataType.STRING, templateName);
        it.setItemMeta(m);

        GenericToolInstance ret = new GenericToolInstance();
        ret.it = it;
        ret.templateName = templateName;
        ret.template = template;
        return ret;
    }

    public String getTemplateName() {
        return templateName;
    }

    public ItemTemplate getTemplate() {
        return template;
    }

    public ItemStack getItemStack() {
        return it;
    }

    public <T, Z> void setPersistentData(String key, PersistentDataType<T, Z> dataTypeAdapter, Z value) {
        ItemMeta m = it.getItemMeta();
        m.getPersistentDataContainer().set(new NamespacedKey(plugin, key), dataTypeAdapter, value);
        it.setItemMeta(m);
    }

    public <T, Z> Z getPersistentData(String key, PersistentDataType<T, Z> dataTypeAdapter) {
        return it.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key), dataTypeAdapter);
    }
}
