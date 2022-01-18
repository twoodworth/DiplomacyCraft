package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.chat.ChatManager;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Adds functionality to each of the chat commands
 */
public class ChatCommand implements CommandExecutor, TabCompleter {

    /*
        Constants used by this class to be sent to the user to show proper command usage.
     */
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String gcUsage = "/gc";
    private static final String acUsage = "/ac";
    private static final String ncUsage = "/nc";
    private static final String lcUsage = "/lc";

    /**
     * Registers ChatCommand to the plugin
     *
     * @param pluginCommand: command to register
     */
    public static void register(PluginCommand pluginCommand) {
        var chatCommand = new ChatCommand();

        pluginCommand.setExecutor(chatCommand);
        pluginCommand.setTabCompleter(chatCommand);
    }

    /**
     * Code to be executed on usage of any command.
     * <p>
     * Used for checking if a chat command is being called, and what functions to call
     * according to the command parameters.
     *
     * @param sender:  Sender of the command
     * @param command: Command being sent
     * @param label:   Command alias, if used
     * @param args:    Arguments of command
     * @return true always
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("gc")) {
            if (args.length == 0) {
                gc(sender);
            } else {
                sender.sendMessage(incorrectUsage + gcUsage);
            }
        } else if (command.getName().equalsIgnoreCase("ac")) {
            if (args.length == 0) {
                ac(sender);
            } else {
                sender.sendMessage(incorrectUsage + acUsage);
            }
        } else if (command.getName().equalsIgnoreCase("nc")) {
            if (args.length == 0) {
                nc(sender);
            } else {
                sender.sendMessage(incorrectUsage + ncUsage);
            }
        } else if (command.getName().equalsIgnoreCase("lc")) {
            if (args.length == 0) {
                lc(sender);
            } else {
                sender.sendMessage(incorrectUsage + lcUsage);
            }
        }
        return true;
    }


    /**
     * Provides a list of argument recommendations based on what the user
     * has typed into the command bar so far.
     * <p>
     * Because none of the chat commands have any arguments, this will always return null.
     *
     * @param sender:  Sender of command
     * @param command: Command being sent
     * @param alias:   Alias of command used
     * @param args:    Arguments of command
     * @return list of arguments, or null if none should be sent.
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

    /**
     * Switches a player to the global chat mode
     *
     * @param sender: Sender of command
     */
    private void gc(CommandSender sender) {
        // return if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;

        // sets mode to "0", corresponding with global, if player is not currently in a chat modef.
        if (ChatManager.getInstance().getChatMode(player) == null) {
            ChatManager.getInstance().setChatMode(player, "0");
        }


        // if player is already in global chat, sends notification accordingly and returns.
        if (ChatManager.getInstance().getChatMode(player).equals("0")) {
            sender.sendMessage(ChatColor.DARK_RED + "You are already in global chat.");
            return;
        }

        // if player is not in global chat, sends notification accordingly and switches to global chat.
        ChatManager.getInstance().setChatMode(player, "0");
        player.sendMessage(ChatColor.AQUA + "You have joined global chat.");
    }

    /**
     * Switches a player to the Ally Chat mode.
     *
     * @param sender: Sender of command
     */
    private void ac(CommandSender sender) {
        // returns if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;

        // checks if player belongs to a nation.
        var nation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(player.getUniqueId()));
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to enter ally chat.");
            return;
        }

        // Sets chat to global chat if player does not belong to any chat mode
        if (ChatManager.getInstance().getChatMode(player) == null) {
            ChatManager.getInstance().setChatMode(player, "0");
        }

        // if player is already in ally chat, returns
        if (ChatManager.getInstance().getChatMode(player).equals("2")) {
            sender.sendMessage(ChatColor.DARK_RED + "You are already in ally chat.");
            return;
        }

        // sets player's mode to Ally chat
        ChatManager.getInstance().setChatMode(player, "2");
        player.sendMessage(ChatColor.AQUA + "You have joined ally chat.");
    }

    /**
     * Sets the player's chat mode to nation chat
     *
     * @param sender: Sender of command
     */
    private void nc(CommandSender sender) {
        // return if not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;

        // check if player belongs to nation.
        var nation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(player.getUniqueId()));
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to enter nation chat.");
            return;
        }

        // set mode to global chat if player does not have a chat mode
        if (ChatManager.getInstance().getChatMode(player) == null) {
            ChatManager.getInstance().setChatMode(player, "0");
        }

        // Checks if already in nation chat
        if (ChatManager.getInstance().getChatMode(player).equals("3")) {
            sender.sendMessage(ChatColor.DARK_RED + "You are already in nation chat.");
            return;
        }

        // Sets mode to nation chat
        ChatManager.getInstance().setChatMode(player, "3");
        player.sendMessage(ChatColor.AQUA + "You have joined nation chat.");
    }

    /**
     * Sets a player's chat mode to local chat
     *
     * @param sender: Sender of command
     */
    private void lc(CommandSender sender) {
        // check if sender is a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        var player = (Player) sender;

        // set chat mode to global if player does not have a chat mode
        if (ChatManager.getInstance().getChatMode(player) == null) {
            ChatManager.getInstance().setChatMode(player, "0");
        }

        // checks if already in local chat
        if (ChatManager.getInstance().getChatMode(player).equals("1")) {
            sender.sendMessage(ChatColor.DARK_RED + "You are already in local chat.");
            return;
        }

        // sets chat mode to local
        ChatManager.getInstance().setChatMode(player, "1");
        player.sendMessage(ChatColor.AQUA + "You have joined local chat.");
    }
}
