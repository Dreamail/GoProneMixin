package tk.fan2tao.mc.goprone.utils;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.TabCompleter;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * 命令管理器
 *
 * @author MrXiaoM
 */
public class CmdManager implements CommandExecutor, TabCompleter, Listener {
    private final List<AbstractCommand> commands = new ArrayList<>();
    private final Plugin plugin;
    private final String packageName;

    /**
     * 命令管理器，可动态注册命令
     * (目前只管注册不管卸载)
     *
     * @param plugin      插件主类实例
     * @param packageName 所有命令所在包
     * @author MrXiaoM
     */
    public CmdManager(Plugin plugin, String packageName) {
        this.plugin = plugin;
        this.packageName = packageName;
    }

    /**
     * 寻找包内所有命令并注册
     *
     * @author MrXiaoM
     */
    public CmdManager register() {
        List<Command> list = new ArrayList<>();
        try {
            String jar = URLDecoder.decode(this.plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.UTF_8);
            ZipFile zip = new ZipFile(jar);
            // 遍历插件压缩包內所有内容
            Enumeration<? extends ZipEntry> files = zip.entries();
            while (files.hasMoreElements()) {
                try {
                    String url = files.nextElement().getName();
                            // 只认 .class 文件
                    if (url.toLowerCase().endsWith(".class")) {
                        // 去除 .class 后缀并替换分隔符为.
                        url = url.substring(0, url.toLowerCase().lastIndexOf(".class"))
                                .replace('/', '.').replace('\\', '.');
                        // 条件: 在commands包内，不是子类 (应是内部类 fix by Dreamail
                        if (url.startsWith(packageName + ".") && !url.contains("$")) {
                            Class<?> c = Class.forName(url);
                            Constructor<?> constAbstractCmd = c.getDeclaredConstructor(plugin.getClass());
                            AbstractCommand cmd = (AbstractCommand) constAbstractCmd.newInstance(plugin);
                            PluginCommand plugCmd = cmd.toPluginCommand();
                            plugCmd.setExecutor(this);
                            plugCmd.setTabCompleter(this);
                            list.add(plugCmd);
                            this.commands.add(cmd);
                        }
                    }
                } catch (Throwable t1) {
                    t1.printStackTrace();
                }
            }
            zip.close();
            this.pushCommands(list);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return this;
    }

    /**
     * 将已注册的命令手动推送到服务器命令列表
     *
     * @param list 命令列表
     * @author MrXiaoM
     */
    public void pushCommands(List<Command> list) {
        try {
            CraftServer server = (CraftServer) Bukkit.getServer();
            //Class<?> craftServerClass = Class.forName("org.bukkit.craftbukkit." + Bukkit.getServer().getClass().getPackage().getName().substring(23) + ".CraftServer");
            /*Class<?> craftServerClass = CraftServer.class;
            SimpleCommandMap commandMap = (SimpleCommandMap) craftServerClass.getDeclaredMethod("getCommandMap").invoke(Bukkit.getServer()); //it is a public method! fuck! by Dreamail */
            SimpleCommandMap commandMap = server.getCommandMap();
            commandMap.registerAll(plugin.getDescription().getName(), list);
            /*Method syncCommands = craftServerClass.getDeclaredMethod("syncCommands"); //it is a public method to! by Dreamail
            syncCommands.setAccessible(true);
            syncCommands.invoke(Bukkit.getServer());*/
            server.syncCommands();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 获取命令实例，在命令不在列表时返回null
     *
     * @param <T>   命令类型
     * @param clazz 命令类
     *              [url=home.php?mod=space&uid=491268]@Return[/url] 命令实例
     * @author MrXiaoM
     */
    @Nullable
    public <T> T getCommandInstance(Class<T> clazz) {
        if (clazz == null) return null;
        for (AbstractCommand cmd : this.commands) {
            try {
                return clazz.cast(cmd);
            } catch (Throwable t) {
                // 收声
            }
        }
        return null;
    }

    /**
     * 获取所有已载入并注册的命令
     *
     * @return 命令列表
     * @author MrXiaoM
     */
    public List<AbstractCommand> getLoadedCommands() {
        return this.commands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdHead = label.contains(":") ? label.substring(label.indexOf(":") + 1) : label;
        for (AbstractCommand command : this.commands) {
            if (command.isCommand(cmdHead)) {
                // CommandProcessEvent event = new CommandProcessEvent(sender, cmdHead, args);
                // Bukkit.getServer().getPluginManager().callEvent(event);
                // if (!event.isCancelled()) {
                return command.onCommand(sender, cmdHead, args, sender instanceof Player);
                // }
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdHead = label.contains(":") ? label.substring(label.indexOf(":") + 1) : label;
        for (AbstractCommand command : commands) {
            if (command.isCommand(cmdHead)) {
                return command.onTabComplete(sender, cmdHead, args, sender instanceof Player);
            }
        }
        return new ArrayList<>();
    }
}
