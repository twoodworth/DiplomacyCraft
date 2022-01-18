package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Guis.NationGuiFactory;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MenuCommand implements CommandExecutor, TabCompleter {

    /*
        Constants used by this class to be sent to the user to show proper command usage.
     */
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String menuUsage = "/menu";

    /**
     * Registers MenuCommand to the plugin
     *
     * @param pluginCommand: command to register
     */
    public static void register(PluginCommand pluginCommand) {
        var menuCommand = new MenuCommand();

        pluginCommand.setExecutor(menuCommand);
        pluginCommand.setTabCompleter(menuCommand);
    }


    /**
     * Code to be executed on usage of any command.
     * <p>
     * Used for checking if a menu command is being called, and what functions to call
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
            menu(sender);
        } else {
            sender.sendMessage(incorrectUsage + menuUsage);
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
        list.add("menu");
        return list;
    }

    /**
     * Used by a player to view the menu GUI
     *
     * @param sender: Sender of command
     */
    private void menu(CommandSender sender) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        // generate menu and show it to player
        var gui = NationGuiFactory.createMenu((Player) sender);
        gui.show((Player) sender);
    }
}
