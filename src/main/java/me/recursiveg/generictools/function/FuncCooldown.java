package me.recursiveg.generictools.function;

import cat.nyaa.nyaacore.CommandReceiver;
import me.recursiveg.generictools.I18n;
import me.recursiveg.generictools.runtime.WrappedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerEvent;

import java.util.UUID;

@IFunction.Function(FuncCooldown.NAME)
public class FuncCooldown implements IFunction {
    public static final String NAME = "cooldown";

    @Serializable(name = "cooldown_seconds")
    public int cooldown;
    @Serializable
    public String cooldownId;

    @Override
    public void parseCommandLine(CommandReceiver.Arguments args) {
        cooldown = args.nextInt();
        cooldownId = UUID.randomUUID().toString();
    }

    @Override
    public String name() {
        return NAME;
    }

    @Override
    public WrappedEvent<?> accept(WrappedEvent<?> we) {
        long cdEndTime = 0;
        if (we.wis.hasKey("giSection.cooldown-" + cooldownId))
            cdEndTime = Long.parseLong(we.wis.getString("giSection.cooldown-" + cooldownId));
        long sysTime = System.currentTimeMillis();
        long delta = sysTime - cdEndTime;
        if (delta < 0) {
            if (we.event instanceof PlayerEvent) {
                ((PlayerEvent) we.event).getPlayer().sendMessage(I18n.format("function_help.cooldown.cd_not_end", -delta/1000.0));
            }
            we.cancelled = true;
        } else {
            we.wis.setString("giSection.cooldown-" + cooldownId, Long.toString(sysTime + cooldown * 1000));
        }
        return we;
    }

    @Override
    public String getInfoString() {
        return I18n.format("function_help.cooldown.info_string", cooldown, cooldownId);
    }
}