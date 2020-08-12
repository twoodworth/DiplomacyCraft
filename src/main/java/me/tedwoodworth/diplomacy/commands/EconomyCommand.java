package me.tedwoodworth.diplomacy.commands;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.ShulkerBox;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EconomyCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String payUsage = "/pay <user> <amount>";
    private static final String walletUsage = "/wallet";
    private static final String depositUsage = "/deposit <amount>";
    private static final String withdrawUsage = "/withdraw <amount>";
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

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
                sender.sendMessage(incorrectUsage + payUsage);
            }
        } else if (command.getName().equalsIgnoreCase("wallet")) {
            if (args.length == 0) {
                wallet(sender);
            } else {
                sender.sendMessage(incorrectUsage + walletUsage);
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
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        var senderPlayer = (OfflinePlayer) sender;
        var recipient = (OfflinePlayer) Bukkit.getPlayer(recipientName);

        if (recipient == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player \"" + recipientName + "\" does not exist.");
            return;
        }

        if (Objects.equals(recipient, senderPlayer)) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot pay yourself.");
            return;
        }

        if (!Bukkit.getOnlinePlayers().contains(recipient)) {
            sender.sendMessage(ChatColor.DARK_RED + recipientName + " is not online.");
            return;
        }//TODO remove if I decide players will always be "online" with a dummy replacing them when they log off.

        var senderLocation = ((Player) senderPlayer).getLocation();
        var recipientLocation = ((Player) recipient).getLocation();
        var isNear = senderLocation.distanceSquared(recipientLocation) < 625;

        if (!isNear) {
            sender.sendMessage(ChatColor.DARK_RED + recipientName + " is too far away.");
            return;
        }

        var amount = 0.0;

        try {

            amount = Double.parseDouble(strAmount);

        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Amount must be a number.");
            return;
        }

        if (amount < 0.01) {
            sender.sendMessage(ChatColor.DARK_RED + "A minimum payment of \u00A40.01 is required.");
            return;
        }

        var tooManyDecimals = BigDecimal.valueOf(amount).scale() > 2;

        if (tooManyDecimals) {
            sender.sendMessage(ChatColor.DARK_RED + "Too many decimal places.");
            return;
        }

        var recipientBalance = Diplomacy.getEconomy().getBalance(recipient);
        var newRecipientBalance = recipientBalance + amount;
        var senderBalance = Diplomacy.getEconomy().getBalance(senderPlayer);
        var newSenderBalance = senderBalance - amount;


        if (newSenderBalance < 0.0) {
            sender.sendMessage(ChatColor.DARK_RED + "You only have \u00A4" + formatter.format(senderBalance) + ".");
            return;
        }


        if (newRecipientBalance > 10000000000000.0) {
            sender.sendMessage(ChatColor.DARK_RED + recipient.getName() + "'s balance cannot exceed \u00A410,000,000,000,000.00.");
            return;
        }

        Diplomacy.getEconomy().depositPlayer(recipient, amount);
        Diplomacy.getEconomy().withdrawPlayer(senderPlayer, amount);

        if (amount >= 1) {
            sender.sendMessage(ChatColor.GREEN + "You have sent \u00A4" + formatter.format(amount) + " to " + recipient.getName() + ".");
            ((Player) recipient).sendMessage(ChatColor.GREEN + "You have been sent \u00A4" + formatter.format(amount) + " from " + sender.getName() + ".");
        } else {
            sender.sendMessage(ChatColor.GREEN + "You have sent \u00A40" + formatter.format(amount) + " to " + recipient.getName() + ".");
            ((Player) recipient).sendMessage(ChatColor.GREEN + "You have been sent \u00A40" + formatter.format(amount) + " from " + sender.getName() + ".");
        }

    }

    private void wallet(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var inventory = ((Player) sender).getInventory();
        double amount = getDiamonds(inventory, false, false, false, ((Player) sender));
        sender.sendMessage(ChatColor.GREEN + "You currently have \u00A4" + formatter.format(amount) + " in your pockets.");
    }

    private double getDiamonds(Inventory inventory, boolean isShulkerBox, boolean isEnderChest, boolean checkedEnderchest, Player player) {
        double amount = 0.0;
        for (int i = 0; i < inventory.getSize(); i++) {
            var itemStack = inventory.getItem(i);
            var type = Material.AIR;
            try {
                type = itemStack.getType();
            } catch (NullPointerException e) {
                continue;
            }
            if (type == Material.DIAMOND) {
                amount += 1000.0 * itemStack.getAmount();
            } else if (type == Material.DIAMOND_BLOCK) {
                amount += 9000.0 * itemStack.getAmount();
            } else if (!isShulkerBox && itemStack.getItemMeta() instanceof BlockStateMeta) {
                var itemMeta = (BlockStateMeta) itemStack.getItemMeta();
                if (itemMeta.getBlockState() instanceof ShulkerBox) {
                    var shulker = (ShulkerBox) itemMeta.getBlockState();
                    var inv = Bukkit.createInventory(null, 27, "Shulker Box");
                    inv.setContents(shulker.getInventory().getContents());
                    amount += getDiamonds(inv, true, isEnderChest, checkedEnderchest, player);
                }
            }
            if (type == Material.ENDER_CHEST && !checkedEnderchest) {
                Inventory inv = player.getEnderChest();
                amount += getDiamonds(inv, false, true, true, player);
                checkedEnderchest = true;
            }
        }
        return amount;
    }

    private void deposit(CommandSender sender, String strAmount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        var amount = 0.0;
        try {
            amount = Double.parseDouble(strAmount);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Amount must be a number.");
            return;
        }

        if (amount < 1000.0) {
            sender.sendMessage(ChatColor.DARK_RED + "A minimum deposit of \u00A41000.00 is required.");
            return;
        }

        if (amount % 1000 != 0) {
            sender.sendMessage(ChatColor.DARK_RED + "You can only deposit in multiples of \u00A41,000.00.");
            return;
        }

        var inventory = ((Player) sender).getInventory();
        var maxAmount = getDiamonds(inventory, false, false, false, ((Player) sender));

        if (amount > maxAmount) {
            if (maxAmount >= 1) {
                sender.sendMessage(ChatColor.DARK_RED + "You only have \u00A4" + formatter.format(amount) + " in your wallet.");
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "You only have \u00A40" + formatter.format(amount) + " in your wallet.");
            }
            return;
        }

        if (Diplomacy.getEconomy().getBalance((OfflinePlayer) sender) + amount > 10000000000000.0) {
            sender.sendMessage(ChatColor.DARK_RED + "Your balance cannot exceed \u00A410,000,000,000,000.00.");
        }
        var nMap = depositDiamonds(amount, inventory, false, false, false, (Player) sender);
        var remainingAmount = (Double) nMap.get("amount");
        var playerInventory = (Inventory) nMap.get("inventory");

        ((Player) sender).getInventory().setContents(playerInventory.getContents());

        Diplomacy.getEconomy().depositPlayer((Player) sender, amount);

        sender.sendMessage(ChatColor.GREEN + "You have deposited \u00A4" + formatter.format(amount));
    }

    private Map<String, ?> depositDiamonds(double amount, Inventory inventory, boolean isShulkerBox, boolean isEnderChest, boolean checkedEnderchest, Player player) {

        for (int i = 0; i < inventory.getSize(); i++) {
            var itemStack = inventory.getItem(i);
            var type = Material.AIR;
            try {
                type = itemStack.getType();
            } catch (NullPointerException e) {
                continue;
            }
            if (type == Material.DIAMOND) {
                var stackSize = itemStack.getAmount();
                for (int j = 0; j < stackSize; j++) {
                    Objects.requireNonNull(inventory.getItem(i)).setAmount(itemStack.getAmount() - 1);
                    amount -= 1000.0;
                    if (amount == 0.0) {
                        return ImmutableMap.of(
                                "inventory", inventory,
                                "amount", amount
                        );
                    }
                }
            }
        }
        for (int i = 0; i < inventory.getSize(); i++) {
            var itemStack = inventory.getItem(i);
            var type = Material.AIR;
            try {
                type = itemStack.getType();
            } catch (NullPointerException e) {
                continue;
            }
            if (type == Material.DIAMOND_BLOCK) {
                var stackSize = itemStack.getAmount();
                for (int j = 0; j < stackSize; j++) {
                    if (amount < 9000.0 && amount != 0.0) {
                        Objects.requireNonNull(inventory.getItem(i)).setAmount(itemStack.getAmount() - 1);
                        var remainder = 9 - (((int) (amount)) / 1000);
                        var firstEmpty = inventory.firstEmpty();
                        var newItemStack = new ItemStack(Material.DIAMOND, remainder);
                        if (firstEmpty == -1) {
                            player.getWorld().dropItem(player.getLocation(), newItemStack);
                        } else {
                            inventory.addItem(newItemStack);
                        }
                        amount = 0.0;
                        return ImmutableMap.of(
                                "inventory", inventory,
                                "amount", amount
                        );
                    }
                    Objects.requireNonNull(inventory.getItem(i)).setAmount(itemStack.getAmount() - 1);
                    amount -= 9000.0;
                    if (amount == 0.0) {
                        return ImmutableMap.of(
                                "inventory", inventory,
                                "amount", amount
                        );
                    }
                }
            }
        }
        if (!isShulkerBox) {
            for (int i = 0; i < inventory.getSize(); i++) {
                var itemStack = inventory.getItem(i);
                var type = Material.AIR;
                try {
                    type = itemStack.getType();
                } catch (NullPointerException e) {
                    continue;
                }
                if (itemStack.getItemMeta() instanceof BlockStateMeta) {
                    var itemMeta = (BlockStateMeta) itemStack.getItemMeta();
                    if (itemMeta.getBlockState() instanceof ShulkerBox) {
                        var shulker = (ShulkerBox) itemMeta.getBlockState();
                        var inv = Bukkit.createInventory(null, 27);
                        inv.setContents(shulker.getInventory().getContents());
                        var nMap = depositDiamonds(amount, inv, true, isEnderChest, checkedEnderchest, player);
                        amount = (Double) nMap.get("amount");
                        var nInventory = (Inventory) nMap.get("inventory");
                        var contents = nInventory.getContents();


                        ItemStack nItemStack = new ItemStack(type);
                        BlockStateMeta bsm = (BlockStateMeta) nItemStack.getItemMeta();
                        ShulkerBox box = (ShulkerBox) Objects.requireNonNull(bsm).getBlockState();
                        box.getInventory().setContents(contents);
                        bsm.setBlockState(box);
                        nItemStack.setItemMeta(bsm);
                        inventory.setItem(i, nItemStack);

                        if (amount == 0.0) {
                            return ImmutableMap.of(
                                    "inventory", inventory,
                                    "amount", amount
                            );
                        }
                    }
                }
            }
        }
        if (!checkedEnderchest && !isEnderChest) {
            for (int i = 0; i < inventory.getSize(); i++) {
                var itemStack = inventory.getItem(i);
                var type = Material.AIR;
                try {
                    type = itemStack.getType();
                } catch (NullPointerException e) {
                    continue;
                }
                if (type == Material.ENDER_CHEST) {
                    Inventory inv = player.getEnderChest();
                    var nMap = depositDiamonds(amount, inv, false, true, true, player);
                    amount = (Double) nMap.get("amount");
                    var ecInventory = (Inventory) nMap.get("inventory");

                    player.getEnderChest().setContents(ecInventory.getContents());
                    return ImmutableMap.of(
                            "inventory", inventory,
                            "amount", amount
                    );
                }
            }
        }
        return ImmutableMap.of(
                "inventory", inventory,
                "amount", amount
        );
    }


    private void withdraw(CommandSender sender, String strAmount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        var amount = 0.0;
        try {
            amount = Double.parseDouble(strAmount);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Amount must be a number.");
            return;
        }

        if (amount < 1000.0) {
            sender.sendMessage(ChatColor.DARK_RED + "A minimum withdrawal of \u00A41000.0 is required.");
            return;
        }

        if (amount % 1000 != 0) {
            sender.sendMessage(ChatColor.DARK_RED + "You can only deposit in multiples of \u00A41,000.00.");
            return;
        }

        var maxAmount = Diplomacy.getEconomy().getBalance((OfflinePlayer) sender);

        if (amount > maxAmount) {
            sender.sendMessage(ChatColor.DARK_RED + "You only have \u00A4" + formatter.format(amount) + " in the bank.");
            return;
        }

        var player = (Player) sender;
        Diplomacy.getEconomy().withdrawPlayer((OfflinePlayer) sender, amount);

        var diamondBlocks = (((int) amount) / 1000) / 9;
        var diamondBlockStacks = diamondBlocks / 64;
        var remainingBlocks = diamondBlocks % 64;

        var diamonds = (((int) amount) / 1000) % 9;


        var diamondBlockStack = new ItemStack(Material.DIAMOND_BLOCK, 64);
        var diamondBlock = new ItemStack(Material.DIAMOND_BLOCK, remainingBlocks);
        var diamond = new ItemStack(Material.DIAMOND, diamonds);

        if (diamondBlockStacks != 0) {
            for (int i = 0; i < diamondBlockStacks; i++) {
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItem(player.getLocation(), diamondBlockStack);
                } else {
                    player.getInventory().addItem(diamondBlockStack);
                }
            }
        }
        if (remainingBlocks != 0) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItem(player.getLocation(), diamondBlock);
            } else {
                player.getInventory().addItem(diamondBlock);
            }
        }
        if (diamonds != 0) {
            if (player.getInventory().firstEmpty() == -1) {
                player.getWorld().dropItem(player.getLocation(), diamond);
            } else {
                player.getInventory().addItem(diamond);
            }
        }

        sender.sendMessage(ChatColor.GREEN + "You have withdrawn \u00A4" + formatter.format(amount) + " from the bank.");
    }
}
