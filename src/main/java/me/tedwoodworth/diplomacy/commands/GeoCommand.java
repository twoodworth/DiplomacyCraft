//package me.tedwoodworth.diplomacy.commands;
//
//import me.tedwoodworth.diplomacy.geology.GeoData;
//import me.tedwoodworth.diplomacy.geology.SubChunk;
//import me.tedwoodworth.diplomacy.Guis.NationGuiFactory;
//import org.bukkit.ChatColor;
//import org.bukkit.command.*;
//import org.bukkit.entity.Player;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GeoCommand implements CommandExecutor, TabCompleter {
//
//    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
//    private static final String tempUsage = "/geo temp";
//    private static final String tickSpeedUsage = "/geo tickspeed <speed>";
//    private static final String geoUsage = "/geo <...>";
//
//    public static void register(PluginCommand pluginCommand) {
//        var geoCommand = new GeoCommand();
//
//        pluginCommand.setExecutor(geoCommand);
//        pluginCommand.setTabCompleter(geoCommand);
//    }
//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        if (args.length == 1) {
//            if (args[0].equalsIgnoreCase("temp")) {
//                temp(sender);
//            } else if (args[0].equalsIgnoreCase("time")) {
//                time(sender);
//            } else if (args[0].equalsIgnoreCase("tickspeed")) {
//                sender.sendMessage(incorrectUsage + tickSpeedUsage);
//            } else {
//                sender.sendMessage(incorrectUsage + geoUsage);
//            }
//        } else if (args.length == 2) {
//            if (args[0].equals("tickspeed")) {
//                tickSpeed(sender, args[1]);
//            } else {
//                sender.sendMessage(incorrectUsage + geoUsage);
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
//        return new ArrayList<>();
//    }
//
//    private void temp(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        var player = (Player) sender;
//
//        if (!player.isOp()) {
//            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
//            return;
//        }
//
//        var loc = player.getLocation();
//        var y = loc.getY();
//        if (y > 255 || y < 0) {
//            sender.sendMessage(ChatColor.DARK_RED + "Error: Your y height must be within 0-255");
//            return;
//        }
//        var subchunk = new SubChunk(loc);
//
//        var k = GeoData.getInstance().getSubchunkTemperature(subchunk);
//        var c = k - 273;
//        var f = (int) (c * 1.8) + 32;
//
//
//        player.sendMessage(ChatColor.GREEN + "Temperature:");
//        player.sendMessage(ChatColor.GREEN + String.valueOf(f) + "\u00B0" + "F");
//        player.sendMessage(ChatColor.GREEN + String.valueOf(c) + "\u00B0" + "C");
//        player.sendMessage(ChatColor.GREEN + String.valueOf(k) + " K");
//    }
//
//    private void time(CommandSender sender) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        var player = (Player) sender;
//
//        if (!player.isOp()) {
//            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
//            return;
//        }
//
//        var world = GeoData.getInstance().WORLD;
//        var time = world.getTime();
//        var fullTime = world.getFullTime();
//        var days = (int) (fullTime / 24000L) + 1;
//        var dayOfYear = days % 2016;
//        sender.sendMessage(ChatColor.GREEN + "Time of day: " + time + "/24000 ticks");
//        sender.sendMessage(ChatColor.GREEN + "Time of year: " + dayOfYear + "/2016 days");
//        sender.sendMessage(ChatColor.GREEN + "Year: " + days / 2016);
//    }
//
//    private void tickSpeed(CommandSender sender, String strSpeed) {
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        var player = (Player) sender;
//
//        if (!player.isOp()) {
//            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
//            return;
//        }
//
//        int speed;
//        try {
//            speed = Integer.parseInt(strSpeed);
//        } catch (NumberFormatException e) {
//            sender.sendMessage(ChatColor.DARK_RED + "Speed must be a number.");
//            return;
//        }
//
//        if (speed < 0) {
//            sender.sendMessage(ChatColor.DARK_RED + "Speed cannot be less than 0.");
//            return;
//        }
//
//        GeoData.getInstance().setTickSpeed(speed);
//        sender.sendMessage(ChatColor.GREEN + "Random tick speed set to " + speed + " times normal speed.");
//    }
//}
