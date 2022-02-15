package tk.fan2tao.mc.goprone.eventhandler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import tk.fan2tao.mc.goprone.GoProne;

public class LoginEventHandler implements Listener {
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        /*try {
        Class s2cCustomClass = Class.forName("net.minecraft.network.protocol.game.PacketPlayInCustomPayload");
        Class fBBClass = Class.forName("net.minecraft.network.PacketDataSerializer");
        Class mCKeyClass = Class.forName("net.minecraft.resources.MinecraftKey");

        Constructor s2cCustomConstructor = s2cCustomClass.getConstructor(mCKeyClass, fBBClass);
        Constructor fBBConstructor = fBBClass.getConstructor(ByteBuf.class);
        Constructor mCKeyConstructor = mCKeyClass.getConstructor(String.class, String.class);
            Object fBB = fBBConstructor.newInstance(Unpooled.buffer());
            Object mCKey = mCKeyConstructor.newInstance("goprone", "enable");
            Object s2cCustom = s2cCustomConstructor.newInstance(mCKey, fBB);

        PacketContainer enablePacket = new PacketContainer(PacketType.Play.Server.CUSTOM_PAYLOAD, s2cCustom);
        //enablePacket.getMinecraftKeys().write(0, new MinecraftKey("goprone", "enable"));
        GoPronePaper.getInstance().protocolManager.sendServerPacket(event.getPlayer(), enablePacket);
        } catch (InvocationTargetException | ClassNotFoundException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }*/
        event.getPlayer().sendPluginMessage(GoProne.getInstance(), "goprone:enable", new byte[]{1});
    }
}
