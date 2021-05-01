package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Guis.NationGuiFactory;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MenuCommand implements CommandExecutor, TabCompleter {

    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String menuUsage = "/menu";

    public static void register(PluginCommand pluginCommand) {
        var menuCommand = new MenuCommand();

        pluginCommand.setExecutor(menuCommand);
        pluginCommand.setTabCompleter(menuCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            menu(sender);
        } else {
            sender.sendMessage(incorrectUsage + menuUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        var list = new ArrayList<String>();
        list.add("menu");
        return list;
    }

    private void menu(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var gui = NationGuiFactory.createMenu((Player) sender);
        gui.show((Player) sender);
    }
}
