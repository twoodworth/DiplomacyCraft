package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EconomyCommand implements CommandExecutor, TabCompleter {
    private static final String payUsage = "/pay <user> <amount>";
    private static final String walletUsage = "/wallet";
    private static final String depositUsage = "/deposit <amount>";
    private static final String withdrawUsage = "/withdraw <amount>";

    public static void register(PluginCommand pluginCommand) {
        var economyCommand = new EconomyCommand();

        pluginCommand.setExecutor(economyCommand);
        pluginCommand.setTabCompleter(economyCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("pay")) {
            if (args.length == 2) {
                pay(sender, args[0], args[1]);
            } else {
                sender.sendMessage(payUsage);
            }
        } else if (command.getName().equalsIgnoreCase("wallet")) {
            if (args.length == 0) {
                wallet(sender);
            } else {
                sender.sendMessage(walletUsage);
            }
        } else if (command.getName().equalsIgnoreCase("deposit")) {
            if (args.length == 1) {
                deposit(sender, args[0]);
            } else {
                sender.sendMessage(depositUsage);
            }
        } else if (command.getName().equalsIgnoreCase("withdraw")) {
            if (args.length == 1) {
                withdraw(sender, args[0]);
            } else {
                sender.sendMessage(withdrawUsage);
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("pay")) {
            if (args.length == 1) {
                List<String> players = new ArrayList<>();
                for (var player : Bukkit.getOnlinePlayers())
                    players.add(player.getName());
                return players;
            } else {
                return null;
            }
        } else if (command.getName().equalsIgnoreCase("wallet")) {
            return null;
        } else if (command.getName().equalsIgnoreCase("deposit")) {
            return null;
        } else if (command.getName().equalsIgnoreCase("withdraw")) {
            return null;
        } else {
            return null;
        }
    }

    private void pay(CommandSender sender, String recipientName, String strAmount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return;
        }
        var senderPlayer = (OfflinePlayer) sender;
        var recipient = (OfflinePlayer) Bukkit.getPlayer(recipientName);

        if (recipient == null) {
            sender.sendMessage(ChatColor.RED + "The player '" + recipientName + "' does not exist.");
            return;
        }

        if (!Bukkit.getOnlinePlayers().contains(recipient)) {
            sender.sendMessage(ChatColor.RED + recipientName + " is not online.");
            return;
        }//TODO remove if I decide players will always be "online" with a dummy replacing them when they log off.

        var senderLocation = ((Player) senderPlayer).getLocation();
        var recipientLocation = ((Player) recipient).getLocation();
        var isNear = senderLocation.distanceSquared(recipientLocation) < 625;

        if (!isNear) {
            sender.sendMessage(ChatColor.RED + recipientName + " is too far away.");
            return;
        }

        var amount = 0.0;

        try {

            amount = Double.parseDouble(strAmount);

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Amount must be a number.");
            return;
        }

        var symbol = '\u00A4';

        if (amount < 0.01) {
            sender.sendMessage(ChatColor.RED + "A minimum payment of " + symbol + "0.01 is required.");
            return;
        }

        var tooManyDecimals = BigDecimal.valueOf(amount).scale() > 2;

        if (tooManyDecimals) {
            sender.sendMessage(ChatColor.RED + "Too many decimal places.");
            return;
        }

        var recipientBalance = Diplomacy.getEconomy().getBalance(recipient);
        var newRecipientBalance = recipientBalance + amount;
        var senderBalance = Diplomacy.getEconomy().getBalance(senderPlayer);
        var newSenderBalance = senderBalance - amount;

        if (newSenderBalance < 0.0) {
            sender.sendMessage(ChatColor.RED + "You only have " + symbol + String.valueOf(senderBalance) + ".");
            return;
        }


        if (newRecipientBalance > 10000000000000.0) {
            sender.sendMessage(ChatColor.RED + recipient.getName() + " does not have enough room in their bank account.");
            return;
        }

        Diplomacy.getEconomy().depositPlayer(recipient, amount);
        Diplomacy.getEconomy().withdrawPlayer(senderPlayer, amount);

        sender.sendMessage(ChatColor.GREEN + "You have sent " + symbol + amount + " to " + recipient.getName() + ".");
        ((Player) recipient).sendMessage(ChatColor.GREEN + "You have been sent " + symbol + amount + " from " + sender.getName() + ".");

    }

    private void wallet(CommandSender sender) {

    }

    private void deposit(CommandSender sender, String amount) {

    }

    private void withdraw(CommandSender sender, String amount) {

    }


}
