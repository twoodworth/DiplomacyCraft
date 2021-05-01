package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;

public class GuideCommand implements CommandExecutor, TabCompleter {

    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String guideUsage = "/guide";

    public static void register(PluginCommand pluginCommand) {
        var guideCommand = new GuideCommand();

        pluginCommand.setExecutor(guideCommand);
        pluginCommand.setTabCompleter(guideCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            guide(sender);
        } else {
            sender.sendMessage(incorrectUsage + guideUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    private void guide(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        ((Player) sender).getInventory().addItem(DiplomacyPlayers.getInstance().getGuideBook());
    }
}
