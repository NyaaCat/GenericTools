package me.recursiveg.generictools.runtime;

import me.recursiveg.generictools.GenericTools;
import me.recursiveg.generictools.config.ItemTemplate;
import me.recursiveg.generictools.function.IFunction;
import me.recursiveg.generictools.trigger.ITrigger;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public final class EventDispatcher {

    /**
     * Recursively pass given Event into the item
     * if itemToCheck is associated with any ItemTemplate
     *
     * @param ev           the event
     * @param itemToCheck  potential related item
     * @param triggerClass
     */
    public static void dispatch(Event ev, ItemStack itemToCheck, Class<? extends ITrigger> triggerClass) {
        ItemTemplate template = GenericTools.instance.cfg.items.findTemplate(itemToCheck);
        if (template != null) {
            WrappedItemStack wis = new WrappedItemStack(itemToCheck);
            for (Map.Entry<Integer, ITrigger> e : template.triggers.entrySet()) {
                if (e.getValue().getClass() == triggerClass) {
                    passEvent(ev, wis, template, e.getValue(), template.triggerToFunctionPath.get(e.getKey()));
                }
            }
        }
    }

    private static void passEvent(Event ev, WrappedItemStack wis,
                                  ItemTemplate template, IFunction function, List<Integer> chainedFunctions) {
        try {
            Event ret = function.accept(ev, template, wis);
            if (ret != null && chainedFunctions != null && chainedFunctions.size() > 0) {
                for (Integer funcIdx : chainedFunctions) {
                    passEvent(ret, wis, template, template.functions.get(funcIdx), template.functionToFunctionPath.get(funcIdx));
                }
            }
        } catch (Exception ex) {

        }
    }
}
