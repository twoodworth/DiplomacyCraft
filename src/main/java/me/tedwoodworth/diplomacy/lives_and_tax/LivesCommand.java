package me.tedwoodworth.diplomacy.lives_and_tax;

import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LivesCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String livesUsage = "/lives";

    public static void register(PluginCommand pluginCommand) {
        var livesCommand = new LivesCommand();

        pluginCommand.setExecutor(livesCommand);
        pluginCommand.setTabCompleter(livesCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("lives")) {
            if (args.length == 0) {
                lives(sender);
            } else if (args.length > 1 && args[0].equalsIgnoreCase("giveApple")) {
                giveApple(sender, args);
            } else {
                sender.sendMessage(incorrectUsage + livesUsage);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("giveLives") && args.length == 1) {
            List<String> players = new ArrayList<>();
            for (var player : DiplomacyPlayers.getInstance().getPlayers()) {
                players.add(player.getOfflinePlayer().getName());
            }
            return players;
        } else if (command.getName().equalsIgnoreCase("addLife") && args.length == 1) {
            List<String> players = new ArrayList<>();
            for (var player : DiplomacyPlayers.getInstance().getPlayers()) {
                players.add(player.getOfflinePlayer().getName());
            }
            return players;
        }
        return null;
    }

    // lives giveApple <player> <reason>
    private void giveApple(CommandSender sender, String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "Insufficient permission.");
            return;
        }

        var player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Unknown player.");
            return;
        }

        StringBuilder message = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            message.append(" ").append(args[i]);
        }
        LivesManager.getInstance().giveApple(player, message.toString());
    }

    private void lives(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var lives = diplomacyPlayer.getLives();
        var label = " lives.";
        if (lives == 1) {
            label = " life.";
        }
        sender.sendMessage(ChatColor.AQUA + "You have " + lives + label);
    }
}
