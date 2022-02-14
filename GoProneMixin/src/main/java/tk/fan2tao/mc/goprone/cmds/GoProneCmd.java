package tk.fan2tao.mc.goprone.cmds;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.fan2tao.mc.goprone.GoProne;
import tk.fan2tao.mc.goprone.utils.AbstractCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class GoProneCmd implements CommandExecutor, TabCompleter, Listener {
    private final Plugin plugin;
    private final PluginCommand pluginCmd;

    public GoProneCmd(Plugin plugin, String label, String[] aliases, String description) {
        this.plugin = plugin;

        Constructor<PluginCommand> commandConst;
        PluginCommand pluginCmd = null;
        try {
            commandConst = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            commandConst.setAccessible(true);

            pluginCmd = commandConst.newInstance(label, plugin);
            pluginCmd.setDescription(description);
            pluginCmd.setUsage("/" + label);
            pluginCmd.setAliases(Lists.newArrayList(aliases));
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        this.pluginCmd = pluginCmd;
    }

    public GoProneCmd register() {
        pluginCmd.setExecutor(this);
        pluginCmd.setTabCompleter(this);
        CraftServer server = (CraftServer) Bukkit.getServer();
        server.getCommandMap().register(plugin.getName(), pluginCmd);
        server.syncCommands();
        return this;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            UUID uuid = player.getUniqueId();
            GoProne.entityProneStates.putIfAbsent(uuid, !GoProne.entityProneStates.getOrDefault(uuid, false));
            return true;
        }
        commandSender.sendMessage("Plz run as a player");
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
