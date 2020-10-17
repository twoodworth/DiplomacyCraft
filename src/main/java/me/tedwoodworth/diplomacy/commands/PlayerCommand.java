package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.nations.Guis;
import me.tedwoodworth.diplomacy.nations.NationGuiFactory;
import me.tedwoodworth.diplomacy.nations.ScoreboardManager;
import me.tedwoodworth.diplomacy.players.AccountManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
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
    private static final String playerAccountsUsage = "/player accounts <player>";
    private static final String playerSetMainUsage = "/player setMain <player>";

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
        } else if (args[0].equalsIgnoreCase("accounts")) {
            if (args.length == 2) {
                playerAccounts(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + playerAccountsUsage);
            }
        } else if (args[0].equalsIgnoreCase("setMain")) {
            if (args.length == 2) {
                playerSetMain(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + playerSetMainUsage);
            }
        } else {
            sender.sendMessage(incorrectUsage + playerUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("info")) {
                var players = new ArrayList<String>();
                for (var player : DiplomacyPlayers.getInstance().getPlayers()) {
                    players.add(player.getPlayer().getName());
                }
                return players;
            } else if (args[0].equalsIgnoreCase("accounts")) {
                var players = new ArrayList<String>();
                for (var player : DiplomacyPlayers.getInstance().getPlayers()) {
                    players.add(player.getPlayer().getName());
                }
                return players;
            } else if (args[0].equalsIgnoreCase("setMain")) {
                var players = new ArrayList<String>();
                for (var player : DiplomacyPlayers.getInstance().getPlayers()) {
                    players.add(player.getPlayer().getName());
                }
                return players;
            }
        } else if (args.length == 1) {
            return Arrays.asList("info", "list", "setMain", "accounts");
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
        sender.sendMessage(ChatColor.GOLD + "/player accounts" + ChatColor.WHITE + " View a player's alts");
        sender.sendMessage(ChatColor.GOLD + "/player setMain" + ChatColor.WHITE + " Set your main account");
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
        sender.sendMessage(ChatColor.RED + "Command is currently disabled for maintenance."); //TODO fix
//        if (!(sender instanceof Player)) {
//            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
//            return;
//        }
//
//        sender.sendMessage(ChatColor.GOLD + "Loading player list...");
//
//        var nGui = Guis.getInstance().getPlayers("alphabetically");
//        nGui.show((Player) sender);
    }

    private void playerAccounts(CommandSender sender, String strPlayer) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = DiplomacyPlayers.getInstance().get(strPlayer);

        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        var account = AccountManager.getInstance().getAccount(player.getUUID());

        if (account == null) {
            sender.sendMessage(ChatColor.RED + "Error: Account is null. Please report this bug to an admin if you see this.");
            return;
        }

        var mainUUID = account.getMain();
        var main = Bukkit.getOfflinePlayer(mainUUID).getName();

        sender.sendMessage(ChatColor.DARK_GREEN + "Alts:");
        for (var uuid : account.getPlayerIDs()) {
            if (uuid.equals(mainUUID)) continue;
            sender.sendMessage(ChatColor.GREEN + Bukkit.getOfflinePlayer(uuid).getName());
        }
        sender.sendMessage(ChatColor.DARK_GREEN + "Main:\n" + ChatColor.GREEN + main);
    }

    private void playerSetMain(CommandSender sender, String strPlayer) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = DiplomacyPlayers.getInstance().get(strPlayer);

        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        var account = AccountManager.getInstance().getAccount(player.getUUID());

        if (account == null) {
            sender.sendMessage(ChatColor.RED + "Error: Account is null. Please report this bug to an admin if you see this.");
            return;
        }

        var main = account.getMain();
        var newMain = player.getUUID();

        if (main.equals(newMain)) {
            sender.sendMessage(ChatColor.RED + "That account is already your main account.");
            return;
        }

        if (!account.getPlayerIDs().contains(newMain)) {
            sender.sendMessage(ChatColor.RED + "That player is not linked to your account.");
            return;
        }

        account.setMain(newMain);
        for (var testUUID : account.getPlayerIDs()) {
            var testPlayer = Bukkit.getPlayer(testUUID);
            if (testPlayer == null) continue;
            testPlayer.sendMessage(ChatColor.DARK_GREEN + "Main account has been set to " + Bukkit.getOfflinePlayer(newMain).getName());
        }
        ScoreboardManager.getInstance().updateScoreboards();
    }
}
