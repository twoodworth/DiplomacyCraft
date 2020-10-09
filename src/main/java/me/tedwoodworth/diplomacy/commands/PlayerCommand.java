package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.nations.Guis;
import me.tedwoodworth.diplomacy.nations.NationGuiFactory;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String playerUsage = "/player";
    private static final String playerListUsage = "/player list";
    private static final String playerInfoUsage = "/player info <player>";

    public static void register(PluginCommand pluginCommand) {
        var playerCommand = new PlayerCommand();

        pluginCommand.setExecutor(playerCommand);
        pluginCommand.setTabCompleter(playerCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            player(sender);
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 1) {
                playerList(sender);
            } else {
                sender.sendMessage(incorrectUsage + playerListUsage);
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 2) {
                playerInfo(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + playerInfoUsage);
            }
        } else {
            sender.sendMessage(incorrectUsage + playerUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info")) {
                var players = new ArrayList<String>();
                for (var player : DiplomacyPlayers.getInstance().getPlayers()) {
                    players.add(player.getPlayer().getName());
                }
                return players;
            }
        } else if (args.length == 0) {
            return Arrays.asList("info", "list");
        }
        return null;
    }

    private void player(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        sender.sendMessage(ChatColor.YELLOW + "----" + ChatColor.GOLD + " Players " + ChatColor.YELLOW + "--" + ChatColor.GOLD + " Page " + ChatColor.RED + "1" + ChatColor.GOLD + "/" + ChatColor.RED + "1" + ChatColor.YELLOW + " ----");
        sender.sendMessage(ChatColor.GOLD + "/player list" + ChatColor.WHITE + " Get a list of all nations");
        sender.sendMessage(ChatColor.GOLD + "/player info" + ChatColor.WHITE + " Get info about a nation");
    }

    private void playerInfo(CommandSender sender, String strPlayer) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = DiplomacyPlayers.getInstance().get(strPlayer);

        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }


        var gui = NationGuiFactory.createPlayer(player.getPlayer());
        gui.show((Player) sender);
    }

    private void playerList(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        sender.sendMessage(ChatColor.GOLD + "Loading player list...");
        var nGui = Guis.getInstance().getPlayers("alphabetically");
        nGui.show((Player) sender);
    }
}
