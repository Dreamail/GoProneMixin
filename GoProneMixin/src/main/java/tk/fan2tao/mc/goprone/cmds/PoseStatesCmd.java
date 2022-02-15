package tk.fan2tao.mc.goprone.cmds;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PoseStatesCmd implements CommandExecutor, TabCompleter, Listener {
    private final Plugin plugin;
    private final PluginCommand pluginCmd;

    public PoseStatesCmd(Plugin plugin, String label, String[] aliases, String description) {
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

    public PoseStatesCmd register() {
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
            player.sendMessage(((CraftPlayer) player).getHandle().getPose().name());
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
