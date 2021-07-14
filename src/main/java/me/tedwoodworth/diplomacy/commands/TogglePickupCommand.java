package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Items.Items;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TogglePickupCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String toggleUsage = "/ta";

    public static void register(PluginCommand pluginCommand) {
        var togglePickupCommand = new TogglePickupCommand();

        pluginCommand.setExecutor(togglePickupCommand);
        pluginCommand.setTabCompleter(togglePickupCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            toggleAutopickup(sender);
        } else {
            sender.sendMessage(incorrectUsage + toggleUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        var list = new ArrayList<String>();
        list.add("toggleAutopickup");
        return list;
    }

    private void toggleAutopickup(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var container = player.getPersistentDataContainer();
        var pickup = Items.getInstance().getAutoPickup(player);
        if (pickup) {
            player.sendMessage(ChatColor.RED + "Automatic pickup disabled");
        } else {
            player.sendMessage(ChatColor.GREEN + "Automatic pickup enabled.");
        }
        Items.getInstance().toggleAutoPickup(player);
    }
}
