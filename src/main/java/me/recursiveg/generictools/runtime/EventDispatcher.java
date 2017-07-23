package me.recursiveg.generictools.runtime;

import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.config.ItemTemplate;
import me.recursiveg.generictools.function.IFunction;
import me.recursiveg.generictools.trigger.ITrigger;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class EventDispatcher {

    /**
     * Recursively pass given Event into the item
     * if itemToCheck is associated with any ItemTemplate
     *
     * Note: all functions on the same event chain will share the same wis and event.
     *
     * @param ev           the event
     * @param itemToCheck  potential related item
     * @param triggerClass the class of the trigger of this event
     */
    public static void dispatch(Event ev, ItemStack itemToCheck, Class<? extends ITrigger> triggerClass) {
        ItemTemplate template = GenericTools.instance.cfg.items.findTemplate(itemToCheck);
        if (template != null) {
            WrappedItemStack wis = new WrappedItemStack(itemToCheck);
            WrappedEvent<?> we = new WrappedEvent<>(ev, wis, template, new HashMap<>());
            for (Map.Entry<Integer, ITrigger> e : template.triggers.entrySet()) {
                // In case there are multiple triggers of same type
                if (e.getValue().getClass() == triggerClass) {
                    passEvent(we, e.getValue(), template.triggerToFunctionPath.get(e.getKey()));
                }
            }
            updateLoreDisplay(template, we.loreDisplayParameters, wis);
            wis.commit();
        }
    }

    private static void passEvent(WrappedEvent<?> we, IFunction function, List<Integer> chainedFunctions) {
        try {
            WrappedEvent<?> ret = function.accept(we);
            if (ret == null) return;
            if (ret.cancelled) return;
            if (chainedFunctions == null) return;
            if (chainedFunctions.size() <= 0) return;
            for (Integer funcIdx : chainedFunctions) {
                passEvent(ret.getChainCopy(), ret.itemTemplate.functions.get(funcIdx), ret.itemTemplate.functionToFunctionPath.get(funcIdx));
            }
        } catch (Exception ex) {
            // TODO
        }
    }

    /**
     * Update the item lore of the WrappedItemStack according to the template & param map.
     *
     * @param template GT template
     * @param params lore params
     * @param wis target wis
     */
    private static void updateLoreDisplay(ItemTemplate template, Map<String, String> params, WrappedItemStack wis) {
        if (!template.updateLores) return;
        if (!template.item.getItemMeta().hasLore()) wis.getItem().getItemMeta().setLore(new ArrayList<>());
        List<String> newLore = template.item.getItemMeta().getLore().stream().map(line -> {
            if (!line.contains("{")) return line;
            for (String key : params.keySet()) {
                line = line.replace("{" + key + "}", params.get(key));
            }
            return line;
        }).collect(Collectors.toList());
        wis.getItem().getItemMeta().setLore(newLore);
    }
}
