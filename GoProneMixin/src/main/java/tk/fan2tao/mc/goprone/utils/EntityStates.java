package tk.fan2tao.mc.goprone.utils;

import com.google.common.collect.Maps;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import tk.fan2tao.mc.goprone.GoProne;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public final class EntityStates {
    private static final Map<UUID, Boolean> entityProneStates = Maps.newConcurrentMap();
    private static final Map<UUID, Boolean> entityFlightBeforeStates = Maps.newConcurrentMap();

    public static boolean isProne(UUID uuid) {
        return entityProneStates.getOrDefault(uuid, false);
    }
    public static void setProne(UUID uuid, boolean isProne) {
        entityProneStates.put(uuid, isProne);
    }
    public static boolean isFlightBefore(UUID uuid) {
        return entityFlightBeforeStates.getOrDefault(uuid, false);
    }
    public static void setFlightBefore(UUID uuid, boolean isProne) {
        entityFlightBeforeStates.put(uuid, isProne);
    }

    public static void toggleProne(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        UUID uuid = player.getUniqueId();
        if (!isProne(uuid)) {
            if (!player.isFlying() && !serverPlayer.isFallFlying()) {
                setProne(uuid, true);
                setFlightBefore(uuid, serverPlayer.getAbilities().mayfly);
                player.setAllowFlight(false);
            }
        } else {
            setProne(uuid, false);
            player.setAllowFlight(isFlightBefore(uuid));
        }
        EntityStates.updateClientProneState(uuid);
    }
    
    public static void updateClientProneState(UUID uuid) {
        Player player = Objects.requireNonNull(Bukkit.getPlayer(uuid));
        player.sendPluginMessage(GoProne.getInstance(), GoProne.CHANNEL_STATES, new byte[]{(byte)(1), (byte)(isProne(uuid) ? 0x01 : 0x00)});
        // 为了兼容forge傻逼， 第一个byte会被吞并且只能是1
    }

}
