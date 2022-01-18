package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Diplomacy;
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

/**
 * Adds functionality for each of the admin commands.
 * Admin commands are commands that can only used by server administrators.
 */
public class AdminCommand implements CommandExecutor, TabCompleter {

    /*
        Constants used by this class to be sent to the user to show proper command usage.
     */
    private static final String incorrectUsage = ChatColor.RED + "Incorrect usage, try: ";
    private static final String adminUsage = "/admin <command>";
    private static final String giveUsage = "/admin give <player> <item> <amount>";
    private static final String setChunkNationUsage = "/admin setChunkNation <nation>";
    private static final String removeChunkNationUsage = "/admin removeChunkNation";
    private static final String fireArrowUsage = "/admin fireArrow <speed> <spread>";
    private static final String generateChunkUsage = "/admin generateChunk";
    private static final String generateAllChunksUsage = "/admin generateAllChunks";
    private static final String censorUsage = "/admin censor <word>";
    private static final String uncensorUsage = "/admin uncensor <word>";
    private static final String superCensorUsage = "/admin superCensor <word>";
    private static final String listCensoredUsage = "/admin listCensored";


    /**
     * Registers AdminCommand to the plugin
     *
     * @param pluginCommand command to register
     */
    public static void register(PluginCommand pluginCommand) {
        var adminCommand = new AdminCommand();

        pluginCommand.setExecutor(adminCommand);
        pluginCommand.setTabCompleter(adminCommand);

    }

    /**
     * Code to be executed on usage of any command.
     * <p>
     * Used for checking if an admin command is being called, and what functions to call
     * according to the command parameters.
     *
     * @param sender:  Sender of the command
     * @param command: Command being sent
     * @param s:       Command alias, if used
     * @param args:    Arguments of command
     * @return true always
     */
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
                } else if (args[0].equalsIgnoreCase("generateAllChunks")) {
                    if (args.length == 1) {
                        generateAllChunks(sender);
                    } else {
                        sender.sendMessage(incorrectUsage + generateAllChunksUsage);
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

    /**
     * Provides a list of argument recommendations based on what the user
     * has typed into the command bar so far.
     *
     * @param sender:  Sender of command
     * @param command: Command being sent
     * @param s:       Alias of command used
     * @param args:    Arguments of command
     * @return list of arguments, or null if none should be sent.
     */
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
                            "generateAllChunks",
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

    /**
     * Used for generating / syncing a chunk between the overworld, subworld, and nether.
     * <p>
     * Will generate / syncing the chunks at the XZ location of the sender.
     *
     * @param sender: Sender of command
     */
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

    /**
     * Generates all chunks in the entire world.
     *
     * @param sender: Sender of command
     */
    private void generateAllChunks(CommandSender sender) {
        // return if not used by player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be a player.");
            return;
        }

        // call task
        generateAllChunksTask(0, 0);
    }

    /**
     * Generates all chunks in the entire world from the provided
     * location and on.
     *
     * @param sender: Sender of command
     * @param x:      x starting coordinate
     * @param z:      z starting coordinate
     */
    private void generateAllChunks(CommandSender sender, int x, int z) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be a player.");
            return;
        }
        generateAllChunksTask(x, z);
    }

    /**
     * Task which recursively generates all chunks in the entire world.
     *
     * @param curX: x coordinate of current chunk
     * @param curZ: z coordinate of current chunk
     */
    private void generateAllChunksTask(int curX, int curZ) {
        // call adjustChunks for current chunk
        WorldManager.getInstance().adjustChunks(curX, curZ);

        // track progress
        System.out.println("Generated " + ((1026 * (curZ + 1)) + (curX + 1)) + " / 1052676 chunks.\n");
        if (curX % 128 == 0) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bcast Nether is " + String.format("%.2f", (((1026 * (curZ + 1)) + (curX + 1)) / 1052676.0) * 100) + "% generated.");
        }

        // call task for next chunk.
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
            // increment x
            var x = curX;
            x++;
            var z = curZ;

            // if at max x, loop x back to 0 and increment z
            if (x == 1025) {
                x = 0;
                z++;
            }

            // base case, where final z is met. Finish generation
            if (z == 1025) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "bcast World is fully generated!");
                System.out.println("Finished generating world.");
                return;
            }
            generateAllChunksTask(x, z);

        }, 5L);

    }

    /**
     * Adds a word to the list of censored words
     *
     * @param sender: Sender of command
     * @param word:   word to censor
     */
    private void addCensoredWord(CommandSender sender, String word) {
        var added = DiplomacyConfig.getInstance().addCensoredWord(word);
        if (!added) {
            sender.sendMessage(ChatColor.RED + "This word was already censored.");
        } else {
            sender.sendMessage(ChatColor.RED + "The word '" + word + "' has been added to the censor list.");
        }
    }

    /**
     * Removes a word from the list of censored words
     *
     * @param sender: Sender of command
     * @param word:   Word to uncensor
     */
    private void removeCensoredWord(CommandSender sender, String word) {
        var removed = DiplomacyConfig.getInstance().removeCensoredWord(word);
        if (!removed) {
            sender.sendMessage(ChatColor.RED + "This word is not censored.");
        } else {
            sender.sendMessage(ChatColor.RED + "The word '" + word + "' has been removed from the censor/supercensor list.");
        }
    }

    /**
     * Adds a word to the list of super censored words
     *
     * @param sender: Sender of command
     * @param word:   Word to send
     */
    private void addSuperCensoredWord(CommandSender sender, String word) {
        var added = DiplomacyConfig.getInstance().addSuperCensoredWord(word);
        if (!added) {
            sender.sendMessage(ChatColor.RED + "This word was already super censored.");
        } else {
            sender.sendMessage(ChatColor.RED + "The word '" + word + "' has been added to the super censor list.");
        }
    }

    /**
     * Sends a list of censored words to the command sender
     *
     * @param sender: Sender of command
     */
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

    /**
     * Spawns a flaming arrow at the given speed and radius of variation
     *
     * @param sender:    Sender of command
     * @param strSpeed:  Speed of arrow
     * @param strSpread: Spread (radius of variation) of arrow
     */
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

    /**
     * Takes the chunk that the sender is currently located in, and
     * forces it to be unclaimed, resetting it to wilderness.
     *
     * @param sender: Sender of command
     */
    private void removeChunkNation(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: Must be a player to use this command.");
            return;
        }

        var chunk = ((Player) sender).getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        var nation = diplomacyChunk.getNation();

        // check if any nation owns the chunk
        if (nation == null) {
            sender.sendMessage(ChatColor.RED + "Chunk does not belong to any nation.");
            return;
        }


        // remove the chunk from nation
        nation.removeChunk(diplomacyChunk);
        var set = new HashSet<DiplomacyChunk>();
        set.add(diplomacyChunk);
        Bukkit.getPluginManager().callEvent(new NationRemoveChunksEvent(nation, set));

        if (diplomacyChunk.getGroup() != null) {
            diplomacyChunk.getGroup().removeChunk(diplomacyChunk);
        }

        // send notification
        sender.sendMessage(ChatColor.AQUA + "Plot abandoned.");

        // send "Wilderness" pop-up to all players within the chunk
        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var playerChunk = testPlayer.getLocation().getChunk();
            if (diplomacyChunk.getChunk().equals(playerChunk)) {
                testPlayer.sendTitle(ChatColor.GRAY + "Wilderness", null, 5, 40, 10);
            }
        }


    }


    /**
     * Gives ownership of the chunk that the sender is located in to the specified nation
     *
     * @param sender:    Sender of command
     * @param strNation: Nation to give chunk to
     */
    private void setChunkNation(CommandSender sender, String strNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: Must be a player to use this command.");
            return;
        }

        // get chunk
        var chunk = ((Player) sender).getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        var nation = diplomacyChunk.getNation();

        // get nation. Return if it does not exist
        var nNation = Nations.getInstance().get(strNation);
        if (nNation == null) {
            sender.sendMessage(ChatColor.RED + "Unknown nation");
            return;
        }

        // return if nation already owns the chunk
        if (Objects.equals(nation, nNation)) {
            sender.sendMessage(ChatColor.RED + "Chunk already belongs to that nation.");
            return;
        }

        // transfer ownership of chunk and its corresponding group to new nation
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

        // send notification
        sender.sendMessage(ChatColor.AQUA + "Plot given to '" + nNation.getName() + "'.");

        // send pop-up to all players located in the chunk
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

    /**
     * Used for spawning a custom item of the DiplomacyCraft plugin and giving it to a player
     *
     * @param sender:    Sender of command
     * @param strPlayer: Player to send item to
     * @param strItem:   Item to spawn
     * @param strAmount: Amount to spawn
     */
    private void give(CommandSender sender, String strPlayer, String strItem, String strAmount) {
        // check if sender is player
        if (!(sender instanceof Player)) {
            sender.sendMessage("Error: Must be a player to use this command.");
            return;
        }

        // find specified player, or return if not found
        var player = Bukkit.getPlayer(strPlayer);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Unknown player.");
            return;
        }

        // parse amount
        int amount;
        try {
            amount = Integer.parseInt(strAmount);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Amount must be an integer.");
            return;
        }

        // check if valid value
        if (amount < 1) {
            sender.sendMessage(ChatColor.RED + "Amount must be at least 1.");
            return;
        }

        if (amount > 64) {
            sender.sendMessage(ChatColor.RED + "Amount cannot exceed 64.");
            return;
        }

        // generate custom item, or return if it does not exist.
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

        // send notification
        player.sendMessage(ChatColor.GREEN + "You have been given " + ChatColor.RED + amount + ChatColor.GREEN + " of " + ChatColor.RED +  strItem + ChatColor.GREEN + " by an admin.");

        // send item
        var inv = player.getInventory();
        var extra = inv.addItem(item);
        if (extra.size() > 0) {
            player.getWorld().dropItem(((Player) sender).getLocation(), extra.get(0));
        }
    }
}
