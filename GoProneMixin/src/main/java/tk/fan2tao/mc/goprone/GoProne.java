package tk.fan2tao.mc.goprone;

import cn.apisium.papershelled.annotation.Mixin;
import cn.apisium.papershelled.plugin.PaperShelledPlugin;
import cn.apisium.papershelled.plugin.PaperShelledPluginDescription;
import cn.apisium.papershelled.plugin.PaperShelledPluginLoader;
import com.google.common.collect.Maps;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.fan2tao.mc.goprone.cmds.GoProneCmd;
import tk.fan2tao.mc.goprone.mixin.PlayerMixin;
import tk.fan2tao.mc.goprone.mixin.SPMMixin;

import java.io.File;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("unused")
@Plugin(name = "GoProne", version = "@@RELEASE_VERSION@@")
@Description("A simple PaperShelled plugin used to make player go prone")
@Author("Dreamail")
@ApiVersion(ApiVersion.Target.v1_18)
@Mixin({
        PlayerMixin.class,
        SPMMixin.class
})
public class GoProne extends PaperShelledPlugin {
    public static Map<UUID, Boolean> entityProneStates = Maps.newConcurrentMap();

    public GoProne(PaperShelledPluginLoader loader, PaperShelledPluginDescription paperShelledDescription, PluginDescriptionFile description, File file) {
        super(loader, paperShelledDescription, description, file);
    }
    @Override
    public void onEnable() {
        saveDefaultConfig();
        new GoProneCmd(this, "goprone", new String[] {"gp"}, "make player go prone").register();
    }

    @Override
    public @Nullable BiomeProvider getDefaultBiomeProvider(@NotNull String s, @Nullable String s1) {
        return null;
    }
}
