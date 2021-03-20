package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyConfig;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.io.File;
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
        var list = new ArrayList<String>();
        list.add("resetWorld");
        return list;
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

        if (diameter < 16) {
            sender.sendMessage(ChatColor.DARK_RED + "Diameter too small. Cannot be smaller than 16");
            return;
        }

        // must be multiple of 16
        var remainder = diameter % 16;
        if (remainder != 0) {
            diameter = diameter - remainder;
            sender.sendMessage(ChatColor.GREEN + "Diameter rounded to " + diameter + " (must be a multiple of 16)");
        }

        WorldCreator wc1 = new WorldCreator("tempWorld");
        wc1.environment(World.Environment.NORMAL);
        wc1.type(WorldType.NORMAL);
        wc1.createWorld();

        var tempWorld = Bukkit.getWorld("tempWorld");

        for (var p : Bukkit.getOnlinePlayers()) {
            p.teleport(new Location(tempWorld, 0, 63, 0));
            p.kickPlayer("World being rest");
        }

        Bukkit.unloadWorld("world", true);
        var container = Bukkit.getWorldContainer();
        var path = container.getAbsolutePath();
        var index = path.indexOf(".");
        var builder = new StringBuilder(path);
        builder.deleteCharAt(index);
        path = builder.toString();
        System.out.println("Path: " + path + "world");
        var worldFolder = new File(path + "world");
        recursiveDelete(worldFolder);

        Bukkit.unloadWorld("tempWorld", false);
        worldFolder = new File(path + "tempWorld");
        recursiveDelete(worldFolder);


        WorldCreator wc = new WorldCreator("world");
        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.FLAT);
        wc.generatorSettings("minecraft:air;minecraft:the_void");
        wc.createWorld();

        var world = Bukkit.getWorld("world");
        var border = world.getWorldBorder();
        border.setCenter(0, 0);
        border.setSize(diameter);

        var chunks = (int) (border.getSize() / 16.0);
        fillWorld(world, chunks / 2, chunks / 2 * (-1), chunks / 2 * (-1), 0, Math.pow(chunks, 2));

    }

    private void fillWorld(World world, int radius, int currentX, int currentZ, int count, double total) {
        var percent = (100 * count) / total;
        System.out.println("Generating new world: " + String.format("%.0f%%", percent));
        var chunk = world.getChunkAt(currentX, currentZ);
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    var block = chunk.getBlock(x, y, z);
                    block.setType(Material.LAVA);
                    block.setBiome(Biome.THE_VOID);
                }
            }
        }
        currentZ++;
        if (currentZ == radius) {
            currentZ = radius * (-1);
            currentX++;
        }
        if (currentX < radius) {
            int finalCurrentX = currentX;
            int finalCurrentZ = currentZ;
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> fillWorld(world, radius, finalCurrentX, finalCurrentZ, count + 1, total), 1L);

        } else {
            Bukkit.getServer().reload();
        }

    }

    private void recursiveDelete(File file) {
        if (file.isDirectory()) {
            var contents = file.listFiles();
            for (var content : contents) {
                recursiveDelete(content);
            }
        }
        if (file.delete() == false) {
            System.out.println("a file failed to delete");
        }
    }
}
