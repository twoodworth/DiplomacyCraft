package me.tedwoodworth.diplomacy.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CombatLogCommand implements CommandExecutor, TabCompleter {

    public static void register(PluginCommand pluginCommand) {
        var combatLogCommand = new CombatLogCommand();

        pluginCommand.setExecutor(combatLogCommand);
        pluginCommand.setTabCompleter(combatLogCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("tag")) {
            if (args.length == 1) {
                tag(sender, args[0]);
            }
        } else if (command.getName().equalsIgnoreCase("untag")) {
            if (args.length == 1) {
                untag(sender, args[0]);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }

    private void tag(CommandSender sender, String strPlayer) {
        if ((sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "Players cannot use this command.");
            return;
        }

        var player = Bukkit.getPlayer(strPlayer);
        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        player.sendMessage(ChatColor.RED + "You are now in combat.");
    }

    private void untag(CommandSender sender, String strPlayer) {
        if ((sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "Players cannot use this command.");
            return;
        }

        var player = Bukkit.getPlayer(strPlayer);
        if (player == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        player.sendMessage(ChatColor.RED + "You are no longer in combat.");
    }
}
