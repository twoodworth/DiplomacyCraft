package me.tedwoodworth.diplomacy.commands;

import com.google.gson.JsonObject;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyConfig;
import me.tedwoodworth.diplomacy.FileUtils;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ResetWorldCommand implements CommandExecutor, TabCompleter {

    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String newWorldUsage = "/resetworld <password> <new world diameter>";

    public static void register(PluginCommand pluginCommand) {
        var resetWorldCommand = new ResetWorldCommand();

        pluginCommand.setExecutor(resetWorldCommand);
        pluginCommand.setTabCompleter(resetWorldCommand);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 2) {
            resetWorld(sender, args[0], args[1]);
        } else {
            sender.sendMessage(incorrectUsage + newWorldUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }

    private void resetWorld(CommandSender sender, String password, String strDiameter) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        var player = (Player) sender;
        if (!player.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
            return;
        }
        var cPassword = DiplomacyConfig.getInstance().getResetWorldPassword();
        if (!password.equals(cPassword)) {
            sender.sendMessage(ChatColor.DARK_RED + "Incorrect password");
            return;
        }
        int diameter;
        try {
            diameter = Integer.parseInt(strDiameter);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Diameter must be a number.");
            return;
        }

        if (diameter > 100000) {
            sender.sendMessage(ChatColor.DARK_RED + "Diameter too large. Cannot be greater than 100000");
            return;
        }

        if (diameter < 64) {
            sender.sendMessage(ChatColor.DARK_RED + "Diameter too small. Cannot be smaller than 64");
            return;
        }

        // must be multiple of 32
        var remainder = diameter % 32;
        if (remainder != 0) {
            diameter = diameter - remainder;
            sender.sendMessage(ChatColor.GREEN + "Diameter rounded to " + diameter + " (must be a multiple of 32)");
        }

        Bukkit.broadcastMessage(ChatColor.GREEN + "World is being reset. All nations will be removed, lives set back to 20, and a fresh planet generated.");
        System.out.println("Preparing new world...");

        // overworld
        var container = Bukkit.getWorldContainer();
        var nWorldDir = new File(container.getAbsolutePath() + "newWorld");
        nWorldDir.mkdir();
        var resource = getClass().getClassLoader().getResource("Default/.newWorld");
        FileUtils.copyResourcesRecursively(resource, nWorldDir);

        DiplomacyConfig.getInstance().setWorldSize(diameter);

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            testPlayer.kickPlayer("World being reset");
        }

        // remove all nations
        System.out.println("Removing nations...");
        var nations = Nations.getInstance().getNations();
        for (var nation : new ArrayList<>(nations)) {
            Nations.getInstance().removeNation(nation);
        }
        var offlinePlayers = Bukkit.getOfflinePlayers();

        // reset all lives
        System.out.println("Resetting Lives...");

        var eco = Diplomacy.getEconomy();
        for (var offlinePlayer : offlinePlayers) {
            eco.withdrawPlayer(offlinePlayer, eco.getBalance(offlinePlayer));

            var uuid = offlinePlayer.getUniqueId();
                var dp = DiplomacyPlayers.getInstance().get(uuid);
            if (dp != null) {
                dp.setLives(20);
            }
        }

        System.out.println();
        System.out.println();
        System.out.println("!!! -------------------------------> BEFORE STARTING UP THE SERVER, DELETE THE FOLDER \"world\"");
        System.out.println("!!! -------------------------------> AND RENAME THE FOLDER \".newWorld\" TO WORLD.");
        System.out.println();
        System.out.println();
        Bukkit.getServer().shutdown();
    }
}
