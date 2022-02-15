package tk.fan2tao.mc.goprone.eventhandler;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import tk.fan2tao.mc.goprone.utils.EntityStates;

public class GoPronePacketHandler implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] bytes) {
        if (channel.equals("goprone:toggle")) {
            EntityStates.toggleProne(player);
        }
    }
}
