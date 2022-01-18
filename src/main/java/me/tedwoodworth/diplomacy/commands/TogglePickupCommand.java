package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Items.Items;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TogglePickupCommand implements CommandExecutor, TabCompleter {

    /*
        Constants used by this class to be sent to the user to show proper command usage.
     */
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String toggleUsage = "/ta";

    /**
     * Registers TogglePickup to the plugin
     *
     * @param pluginCommand: command to register
     */
    public static void register(PluginCommand pluginCommand) {
        var togglePickupCommand = new TogglePickupCommand();

        pluginCommand.setExecutor(togglePickupCommand);
        pluginCommand.setTabCompleter(togglePickupCommand);
    }


    /**
     * Code to be executed on usage of any command.
     * <p>
     * Used for checking if a togglePickup command is being called, and what functions to call
     * according to the command parameters.
     *
     * @param sender:  Sender of the command
     * @param command: Command being sent
     * @param label:   Command alias, if used
     * @param args:    Arguments of command
     * @return true always
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            toggleAutopickup(sender);
        } else {
            sender.sendMessage(incorrectUsage + toggleUsage);
        }
        return true;
    }

    /**
     * Provides a list of argument recommendations based on what the user
     * has typed into the command bar so far.
     *
     * @param sender:  Sender of command
     * @param command: Command being sent
     * @param alias:   Alias of command used
     * @param args:    Arguments of command
     * @return list of arguments, or null if none should be sent.
     */
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
