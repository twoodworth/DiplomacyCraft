package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.nations.NationGuiFactory;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PlayerCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String playerUsage = "/player";

    public static void register(PluginCommand pluginCommand) {
        var playerCommand = new PlayerCommand();

        pluginCommand.setExecutor(playerCommand);
        pluginCommand.setTabCompleter(playerCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) {
            player(sender, args[0]);
        } else {
            sender.sendMessage(incorrectUsage + playerUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            var players = new ArrayList<String>();
            for (var player : DiplomacyPlayers.getInstance().getPlayers()) {
                players.add(player.getPlayer().getName());
            }
            return players;
        }
        return null;
    }

    private void player(CommandSender sender, String strPlayer) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = DiplomacyPlayers.getInstance().get(strPlayer);

        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }


        var gui = NationGuiFactory.createPlayer((Player) sender, player.getPlayer());
        gui.show((Player) sender);
    }
}
