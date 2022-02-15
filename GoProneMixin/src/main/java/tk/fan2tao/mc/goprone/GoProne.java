package tk.fan2tao.mc.goprone;

import cn.apisium.papershelled.annotation.Mixin;
import cn.apisium.papershelled.plugin.PaperShelledPlugin;
import cn.apisium.papershelled.plugin.PaperShelledPluginDescription;
import cn.apisium.papershelled.plugin.PaperShelledPluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.fan2tao.mc.goprone.cmds.PoseStatesCmd;
import tk.fan2tao.mc.goprone.eventhandler.GoPronePacketHandler;
import tk.fan2tao.mc.goprone.eventhandler.LoginEventHandler;
import tk.fan2tao.mc.goprone.mixin.PlayerMixin;

import java.io.File;

@SuppressWarnings("unused")
@Plugin(name = "GoProne", version = "@@RELEASE_VERSION@@")
@Description("A simple PaperShelled plugin used to make player go prone")
@Author("Dreamail")
@ApiVersion(ApiVersion.Target.v1_18)
@Mixin({
        PlayerMixin.class,
        //SPMMixin.class
})
public class GoProne extends PaperShelledPlugin {
    private static GoProne instance;
    public static boolean isJumpingAllowed = false;
    public static boolean isFlyingAllowed = false;

    public static final String CHANNEL_ENABLED = "goprone:enable";
    public static final String CHANNEL_STATES = "goprone:states";
    public static final String CHANNEL_TOGGLE = "goprone:toggle";

    public GoProne(PaperShelledPluginLoader loader, PaperShelledPluginDescription paperShelledDescription, PluginDescriptionFile description, File file) {
        super(loader, paperShelledDescription, description, file);
        instance = this;
    }

    public static GoProne getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        new PoseStatesCmd(this, "pose", new String[] {}, "show player pose").register();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, CHANNEL_ENABLED);
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, CHANNEL_STATES);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, CHANNEL_TOGGLE, new GoPronePacketHandler());
        Bukkit.getServer().getPluginManager().registerEvents(new LoginEventHandler(), this);
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull String s, @Nullable String s1) {
        return null;
    }
}
