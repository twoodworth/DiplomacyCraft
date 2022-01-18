package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Guis.Guis;
import me.tedwoodworth.diplomacy.Guis.RecipeGuis;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RecipeCommand implements CommandExecutor, TabCompleter {

    /*
        Constants used by this class to be sent to the user to show proper command usage.
     */
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String recipesUsage = "/recipe";

    /**
     * Registers RecipeCommand to the plugin
     *
     * @param pluginCommand: command to register
     */
    public static void register(PluginCommand pluginCommand) {
        var recipeCommand = new RecipeCommand();

        pluginCommand.setExecutor(recipeCommand);
        pluginCommand.setTabCompleter(recipeCommand);
    }


    /**
     * Code to be executed on usage of any command.
     * <p>
     * Used for checking if a recipe command is being called, and what functions to call
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
            recipes(sender);
        } else {
            sender.sendMessage(incorrectUsage + recipesUsage);
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
        list.add("recipes");
        return list;
    }

    private void recipes(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var gui = RecipeGuis.getInstance().getRecipeMenu(RecipeGuis.getInstance().RECIPES_KEY);
        gui.show((Player) sender);
    }
}
