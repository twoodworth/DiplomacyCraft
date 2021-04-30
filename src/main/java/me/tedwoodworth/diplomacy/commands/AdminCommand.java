package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdminCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.RED + "Incorrect usage, try: ";
    private static final String adminUsage = "/admin <command>";
    private static final String getItemUsage = "/admin getItem <ID>";


    public static void register(PluginCommand pluginCommand) {
        var adminCommand = new AdminCommand();

        pluginCommand.setExecutor(adminCommand);
        pluginCommand.setTabCompleter(adminCommand);

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.DARK_RED + "Insufficient permissions.");
        } else {
            if (args.length == 0) {
                sender.sendMessage(incorrectUsage + adminUsage);
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("getItem")) {
                    sender.sendMessage(incorrectUsage + getItemUsage);
                }
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("getItem")) {
                    getItem(sender, args[1]);
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

    private void getItem(CommandSender sender, String strID) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: Must be a player to use this command.");
            return;
        }
        int id;
        try {
            id = Integer.parseInt(strID);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "ID must be an integer.");
            return;
        }
        var length = CustomItems.CustomID.values().length;

        if (id >= length) {
            sender.sendMessage(ChatColor.RED + "Invalid ID. Must be from 0 to " + (length - 1));
            return;
        }

        var item = CustomItemGenerator.getInstance().getCustomItem(id, 1);
        var inv = ((Player) sender).getInventory();
        var extra = inv.addItem(item);
        if (extra.size() > 0) {
            ((Player) sender).getWorld().dropItem(((Player) sender).getLocation(), extra.get(0));
        }
    }
}
