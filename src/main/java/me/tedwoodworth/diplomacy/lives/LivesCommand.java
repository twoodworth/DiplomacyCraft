package me.tedwoodworth.diplomacy.lives;

import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class LivesCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String livesUsage = "/lives";
    private static final String giveLivesUsage = "/giveLives <player> <amount>";

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
            } else {
                sender.sendMessage(incorrectUsage + livesUsage);
            }
        } else if (command.getName().equalsIgnoreCase("giveLives")) {
            if (args.length == 2) {
                giveLives(sender, args[0], args[1]);
            } else {
                sender.sendMessage(incorrectUsage + giveLivesUsage);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("giveLives") && args.length == 1) {
            List<String> players = new ArrayList<>();
            for (var player : DiplomacyPlayers.getInstance().getPlayers()) {
                players.add(player.getPlayer().getName());
            }
            return players;
        }
        return null;
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
        player.sendMessage(ChatColor.AQUA + "The next day starts in " + LivesManager.getInstance().getStringTimeUntil() + ".");
    }

    private void giveLives(CommandSender sender, String strPlayer, String strAmount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }


        var recipient = DiplomacyPlayers.getInstance().get(strPlayer);
        if (recipient == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        var amount = 0;
        try {
            amount = Integer.parseInt(strAmount);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Amount must be a number.");
            return;
        }

        if (amount < 1) {
            sender.sendMessage(ChatColor.DARK_RED + "Amount must be greater than 0.");
            return;
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var lives = diplomacyPlayer.getLives();

        if (lives - amount == 0) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot give another player your last life.");
            return;
        }

        if (amount > lives) {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have enough lives.");
            return;
        }

        diplomacyPlayer.setLives(lives - amount);
        recipient.setLives(recipient.getLives() + amount);

        var label = " lives.";
        if (amount == 1) {
            label = " life.";
        }

        sender.sendMessage(ChatColor.AQUA + "You have given " + recipient.getPlayer().getName() + " " + amount + label);

        if (recipient.getPlayer().isOnline()) {
            recipient.getPlayer().getPlayer().sendMessage(ChatColor.AQUA + player.getName() + " has given you " + amount + label);
        }
    }
}