package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.DiplomacyConfig;
import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.events.NationAddChunksEvent;
import me.tedwoodworth.diplomacy.events.NationRemoveChunksEvent;
import me.tedwoodworth.diplomacy.geology.WorldManager;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AdminCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.RED + "Incorrect usage, try: ";
    private static final String adminUsage = "/admin <command>";
    private static final String giveUsage = "/admin give <player> <item> <amount>";
    private static final String setChunkNationUsage = "/admin setChunkNation <nation>";
    private static final String removeChunkNationUsage = "/admin removeChunkNation";
    private static final String fireArrowUsage = "/admin fireArrow <speed> <spread>";
    private static final String generateChunkUsage = "/admin generateChunk";
    private static final String censorUsage = "/admin censor <word>";
    private static final String uncensorUsage = "/admin uncensor <word>";
    private static final String superCensorUsage = "/admin superCensor <word>";
    private static final String listCensoredUsage = "/admin listCensored";


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
            } else {
                if (args[0].equalsIgnoreCase("give")) {
                    if (args.length == 3) {
                        give(sender, args[1], args[2], "64");
                    } else if (args.length == 4) {
                        give(sender, args[1], args[2],  args[3]);
                    } else {
                        sender.sendMessage(incorrectUsage + giveUsage);
                    }
                } else if (args[0].equalsIgnoreCase("removeChunkNation")) {
                    if (args.length == 1) {
                        removeChunkNation(sender);
                    } else {
                        sender.sendMessage(incorrectUsage + removeChunkNationUsage);
                    }
                } else if (args[0].equalsIgnoreCase("setChunkNation")) {
                    if (args.length == 2) {
                        setChunkNation(sender, args[1]);
                    } else {
                        sender.sendMessage(incorrectUsage + setChunkNationUsage);
                    }
                } else if (args[0].equalsIgnoreCase("fireArrow")) {
                    if (args.length == 3) {
                        fireArrow(sender, args[1], args[2]);
                    } else {
                        sender.sendMessage(incorrectUsage + fireArrowUsage);
                    }
                } else if (args[0].equalsIgnoreCase("generateChunk")) {
                    if (args.length == 1) {
                        generateChunk(sender);
                    } else {
                        sender.sendMessage(incorrectUsage + generateChunkUsage);
                    }
                } else if (args[0].equalsIgnoreCase("censor")) {
                    if (args.length == 2) {
                        addCensoredWord(sender, args[1]);
                    } else {
                        sender.sendMessage(incorrectUsage + censorUsage);
                    }
                } else if (args[0].equalsIgnoreCase("superCensor")) {
                    if (args.length == 2) {
                        addSuperCensoredWord(sender, args[1]);
                    } else {
                        sender.sendMessage(incorrectUsage + superCensorUsage);
                    }
                } else if (args[0].equalsIgnoreCase("uncensor")) {
                    if (args.length == 2) {
                        removeCensoredWord(sender, args[1]);
                    } else {
                        sender.sendMessage(incorrectUsage + uncensorUsage);
                    }
                } else if (args[0].equalsIgnoreCase("listCensored")) {
                    if (args.length == 1) {
                        listCensoredWords(sender);
                    } else {
                        sender.sendMessage(incorrectUsage + listCensoredUsage);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.isOp()) {
            return null;
        } else {
            if (args.length > 0) {
                if (args.length == 1) {
                    return Arrays.asList(
                            "give",
                            "setChunkNation",
                            "removeChunkNation",
                            "fireArrow",
                            "generateChunk",
                            "censor",
                            "uncensor",
                            "superCensor",
                            "listCensored"
                    );
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("give")) {
                        var online = Bukkit.getOnlinePlayers();
                        var list = new ArrayList<String>();
                        for (var player : online) {
                            list.add(player.getName());
                        }
                        return list;
                    } else if (args[0].equalsIgnoreCase("setChunkNation")) {
                        var list = new ArrayList<String>();
                        for (var nation : Nations.getInstance().getNations()) {
                            list.add(nation.getName());
                        }
                        return list;
                    }
                } else if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("give")) {
                        return Arrays.asList(
                                "apple_of_life",
                                "guard_crystal",
                                "grenade",
                                "magical_dust"

                        );
                    }
                }
            }
        }
        return null;
    }

    private void generateChunk(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be a player.");
            return;
        }
        var loc = ((Player) sender).getLocation();
        var chunk = loc.getChunk();
        var x = chunk.getX();
        var z = chunk.getZ();
        WorldManager.getInstance().adjustChunks(x, z);

    }

    private void addCensoredWord(CommandSender sender, String word) {
        var added = DiplomacyConfig.getInstance().addCensoredWord(word);
        if (!added) {
            sender.sendMessage(ChatColor.RED + "This word was already censored.");
        } else {
            sender.sendMessage(ChatColor.RED + "The word '" + word + "' has been added to the censor list.");
        }
    }

    private void removeCensoredWord(CommandSender sender, String word) {
        var removed = DiplomacyConfig.getInstance().removeCensoredWord(word);
        if (!removed) {
            sender.sendMessage(ChatColor.RED + "This word is not censored.");
        } else {
            sender.sendMessage(ChatColor.RED + "The word '" + word + "' has been removed from the censor/supercensor list.");
        }
    }

    private void addSuperCensoredWord(CommandSender sender, String word) {
        var added = DiplomacyConfig.getInstance().addSuperCensoredWord(word);
        if (!added) {
            sender.sendMessage(ChatColor.RED + "This word was already super censored.");
        } else {
            sender.sendMessage(ChatColor.RED + "The word '" + word + "' has been added to the super censor list.");
        }
    }

    private void listCensoredWords(CommandSender sender) {
        var censored = DiplomacyConfig.getInstance().getCensoredWords();
        var superCensored = DiplomacyConfig.getInstance().getSuperCensoredWords();

        var censoredMessage = new StringBuilder();
        censoredMessage.append(ChatColor.RED + "Censored words: ");
        for (var word : censored) {
            censoredMessage.append(word + ", ");
        }

        var superCensoredMessage = new StringBuilder();
        superCensoredMessage.append(ChatColor.RED + "Super Censored words: ");
        for (var word : superCensored) {
            superCensoredMessage.append(word + ", ");
        }

        sender.sendMessage(censoredMessage.toString());
        sender.sendMessage(superCensoredMessage.toString());
    }

    private void fireArrow(CommandSender sender, String strSpeed, String strSpread) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: Must be a player to use this command.");
            return;
        }

        float speed;
        float spread;
        try {
            speed = Float.parseFloat(strSpeed);
            spread = Float.parseFloat(strSpread);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Error: Velocity must be a number");
            return;
        }

        var world = ((Player) sender).getWorld();
        var arrow = world.spawnArrow(((Player) sender).getEyeLocation(), ((Player) sender).getEyeLocation().getDirection(), speed, spread);
    }

    private void removeChunkNation(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: Must be a player to use this command.");
            return;
        }

        var chunk = ((Player) sender).getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        var nation = diplomacyChunk.getNation();

        if (nation == null) {
            sender.sendMessage(ChatColor.RED + "Chunk does not belong to any nation.");
            return;
        }


        nation.removeChunk(diplomacyChunk);
        var set = new HashSet<DiplomacyChunk>();
        set.add(diplomacyChunk);
        Bukkit.getPluginManager().callEvent(new NationRemoveChunksEvent(nation, set));

        if (diplomacyChunk.getGroup() != null) {
            diplomacyChunk.getGroup().removeChunk(diplomacyChunk);
        }

        sender.sendMessage(ChatColor.AQUA + "Plot abandoned.");

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var playerChunk = testPlayer.getLocation().getChunk();
            if (diplomacyChunk.getChunk().equals(playerChunk)) {
                testPlayer.sendTitle(ChatColor.GRAY + "Wilderness", null, 5, 40, 10);
            }
        }


    }


    private void setChunkNation(CommandSender sender, String strNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: Must be a player to use this command.");
            return;
        }

        var chunk = ((Player) sender).getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        var nation = diplomacyChunk.getNation();

        var nNation = Nations.getInstance().get(strNation);
        if (nNation == null) {
            sender.sendMessage(ChatColor.RED + "Unknown nation");
            return;
        }

        if (Objects.equals(nation, nNation)) {
            sender.sendMessage(ChatColor.RED + "Chunk already belongs to that nation.");
            return;
        }

        if (nation != null) {
            nation.removeChunk(diplomacyChunk);
        }
        nNation.addChunk(diplomacyChunk);
        var set = new HashSet<DiplomacyChunk>();
        set.add(diplomacyChunk);
        Bukkit.getPluginManager().callEvent(new NationAddChunksEvent(nNation, set));

        if (nation != null) {
            Bukkit.getPluginManager().callEvent(new NationRemoveChunksEvent(nation, set));
        }

        if (nation != null) {
            if (nation.getGroups() != null) {
                for (var group : nation.getGroups()) {
                    if (group.getChunks().contains(diplomacyChunk)) {
                        group.removeChunk(diplomacyChunk);
                    }
                }
            }
        }

        sender.sendMessage(ChatColor.AQUA + "Plot given to '" + nNation.getName() + "'.");

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var playerChunk = testPlayer.getLocation().getChunk();
            if (chunk.equals(playerChunk)) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
                Nation testNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testNation == null) {
                    testPlayer.sendTitle(ChatColor.BLUE + nNation.getName(), null, 5, 40, 10);
                } else if (nNation.getEnemyNationIDs().contains(testNation.getNationID())) {
                    testPlayer.sendTitle(ChatColor.RED + nNation.getName(), null, 5, 40, 10);
                } else if (nNation.getAllyNationIDs().contains(testNation.getNationID()) || nNation.equals(nation)) {
                    testPlayer.sendTitle(ChatColor.GREEN + nNation.getName(), null, 5, 40, 10);
                } else {
                    testPlayer.sendTitle(ChatColor.BLUE + nNation.getName(), null, 5, 40, 10);
                }
            }
        }

    }

    private void give(CommandSender sender, String strPlayer, String strItem, String strAmount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: Must be a player to use this command.");
            return;
        }

        var player = Bukkit.getPlayer(strPlayer);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Unknown player.");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(strAmount);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Amount must be an integer.");
            return;
        }

        if (amount < 1) {
            sender.sendMessage(ChatColor.RED + "Amount must be at least 1.");
            return;
        }

        if (amount > 64) {
            sender.sendMessage(ChatColor.RED + "Amount cannot exceed 64.");
            return;
        }

        ItemStack item;
        if (strItem.equalsIgnoreCase("apple_of_life")) {
            item = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.APPLE_OF_LIFE, amount);
        } else if (strItem.equalsIgnoreCase("guard_crystal")) {
            item = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GUARD_CRYSTAL, amount);
        } else if (strItem.equalsIgnoreCase("grenade")) {
            item = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GRENADE, amount);
        } else if (strItem.equalsIgnoreCase("magical_dust")) {
            item = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, amount);
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown item.");
            return;
        }

        player.sendMessage(ChatColor.GREEN + "You have been given " + ChatColor.RED + amount + ChatColor.GREEN + " of " + ChatColor.RED +  strItem + ChatColor.GREEN + " by an admin.");
        var inv = player.getInventory();
        var extra = inv.addItem(item);
        if (extra.size() > 0) {
            player.getWorld().dropItem(((Player) sender).getLocation(), extra.get(0));
        }
    }
}
