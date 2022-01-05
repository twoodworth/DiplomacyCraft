//package me.tedwoodworth.diplomacy.commands;
//
//import me.tedwoodworth.diplomacy.DiplomacyConfig;
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.md_5.bungee.api.chat.hover.content.Text;
//import org.bukkit.ChatColor;
//import org.bukkit.command.*;
//import org.bukkit.entity.Player;
//
//import java.util.List;
//
//public class LinkCommand implements CommandExecutor, TabCompleter {
//
//    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
//    private static final String mapUsage = "/map";
//    private static final String discordUsage = "/map";
//
//    public static void register(PluginCommand pluginCommand) {
//        var mapCommand = new LinkCommand();
//
//        pluginCommand.setExecutor(mapCommand);
//        pluginCommand.setTabCompleter(mapCommand);
//    }
//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (command.getName().equals("map")) {
//            if (args.length == 0) {
//                map(sender);
//            } else {
//                sender.sendMessage(incorrectUsage + mapUsage);
//            }
//        } else if (command.getName().equals("discord")) {
//            if (args.length == 0) {
//                discord(sender);
//            } else {
//                sender.sendMessage(incorrectUsage + discordUsage);
//            }
//        }
//        return true;
//    }
//
//
//    @Override
//    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
//        return null;
//    }
//
//    public static String getCardinalDirection(Player player) {
//        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
//        if (rotation < 0.0D) {
//            rotation += 360.0D;
//        }
//        if ((0.0D <= rotation) && (rotation < 22.5D)) {
//            return "N";
//        }
//        if ((22.5D <= rotation) && (rotation < 67.5D)) {
//            return "NE";
//        }
//        if ((67.5D <= rotation) && (rotation < 112.5D)) {
//            return "E";
//        }
//        if ((112.5D <= rotation) && (rotation < 157.5D)) {
//            return "SE";
//        }
//        if ((157.5D <= rotation) && (rotation < 202.5D)) {
//            return "S";
//        }
//        if ((202.5D <= rotation) && (rotation < 247.5D)) {
//            return "SW";
//        }
//        if ((247.5D <= rotation) && (rotation < 292.5D)) {
//            return "W";
//        }
//        if ((292.5D <= rotation) && (rotation < 337.5D)) {
//            return "NW";
//        }
//        return "N";
//    }
//
//    private void map(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        var mapLink = DiplomacyConfig.getInstance().getMapLink();
//
//
//        var hoverComponent = new ComponentBuilder()
//                .append(mapLink)
//                .color(net.md_5.bungee.api.ChatColor.AQUA)
//                .create();
//
//        var message = new ComponentBuilder()
//                .append("[Click me to open the map]")
//                .color(net.md_5.bungee.api.ChatColor.AQUA)
//                .bold(true)
//                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, mapLink))
//                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverComponent)))
//                .create();
//
//        sender.spigot().sendMessage(message);
//    }
//
//    private void discord(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        var discordLink = DiplomacyConfig.getInstance().getDiscordLink();
//
//
//        var hoverComponent = new ComponentBuilder()
//                .append(discordLink)
//                .color(net.md_5.bungee.api.ChatColor.AQUA)
//                .create();
//
//        var message = new ComponentBuilder()
//                .append("[Click me to open discord]")
//                .color(net.md_5.bungee.api.ChatColor.AQUA)
//                .bold(true)
//                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, discordLink))
//                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverComponent)))
//                .create();
//
//        sender.spigot().sendMessage(message);
//    }
//}
