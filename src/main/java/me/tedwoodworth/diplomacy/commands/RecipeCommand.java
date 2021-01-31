package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.nations.Guis;
import me.tedwoodworth.diplomacy.nations.NationGuiFactory;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RecipeCommand implements CommandExecutor, TabCompleter {

    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String recipesUsage = "/recipe";

    public static void register(PluginCommand pluginCommand) {
        var recipeCommand = new RecipeCommand();

        pluginCommand.setExecutor(recipeCommand);
        pluginCommand.setTabCompleter(recipeCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            recipes(sender);
        } else {
            sender.sendMessage(incorrectUsage + recipesUsage);
        }
        return true;
    }

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

        var gui = Guis.getInstance().getRecipeMenu(Guis.getInstance().RECIPES_KEY);
        gui.show((Player) sender);
    }
}
