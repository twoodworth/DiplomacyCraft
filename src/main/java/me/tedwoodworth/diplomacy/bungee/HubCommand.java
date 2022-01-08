//package me.tedwoodworth.diplomacy.bungee;
//
//import net.md_5.bungee.api.ChatColor;
//import net.md_5.bungee.api.CommandSender;
//import net.md_5.bungee.api.ProxyServer;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.config.ServerInfo;
//import net.md_5.bungee.api.connection.ProxiedPlayer;
//import net.md_5.bungee.api.plugin.Command;
//import org.bukkit.entity.Player;
//
//public class HubCommand extends Command {
//
//    public HubCommand() {
//        super("hub");
//    }
//
//    @Override
//    public void execute(CommandSender sender, String[] args) {
//        if (!(sender instanceof ProxiedPlayer)) {
//            sender.sendMessage(new ComponentBuilder("This command can only be run by a player!").color(ChatColor.RED).create());
//            return;
//        }
//        BCUtil.setServer((Player) sender, "hub");
//    }
//}