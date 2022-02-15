package tk.fan2tao.mc.goprone.mixin;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.SimplePluginManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.io.File;
import java.util.List;

@Mixin(SimplePluginManager.class)
public abstract class SPMMixin implements PluginManager {
    @ModifyVariable(method = "loadPlugins(Ljava/io/File;Ljava/util/List;)[Lorg/bukkit/plugin/Plugin;", index = 2, at = @At("HEAD"), argsOnly = true, remap = false)
    public List<File> injectExtraPluginJars(@NotNull List<File> extraPluginJars) {
        extraPluginJars.add(new File("/Volumes/PiExDisk/Code/Minecraft/Server/GoProneMixin/GoPronePaper/build/libs/GoPronePaper-1.0-SNAPSHOT-all.jar"));
        return extraPluginJars;
    }
}
