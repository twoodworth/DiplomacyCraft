package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.events.NationAddChunksEvent;
import me.tedwoodworth.diplomacy.events.NationRemoveChunksEvent;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PlotCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.RED + "Incorrect usage, try: ";
    private static final String plotUsage = "/plot";
    private static final String plotContestUsage = "/plot contest";
    private static final String plotSurrenderUsage = "/plot surrender <nation>";
    private static final String plotAbandonUsage = "/plot abandon";
    private static final String plotGroupUsage = "/plot group";
    private static final String plotUnlockUsage = "/plot unlock";


    public static void register(PluginCommand pluginCommand) {
        var plotCommand = new PlotCommand();

        pluginCommand.setExecutor(plotCommand);
        pluginCommand.setTabCompleter(plotCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            plot(sender);
        } else if (args[0].equalsIgnoreCase("contest")) {
            if (args.length == 1) {
                plotContest(sender);
            } else {
                sender.sendMessage(incorrectUsage + plotContestUsage);
            }
        } else if (args[0].equalsIgnoreCase("surrender")) {
            if (args.length == 2) {
                plotSurrender(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + plotSurrenderUsage);
            }
        } else if (args[0].equalsIgnoreCase("abandon")) {
            if (args.length == 1) {
                plotAbandon(sender);
            } else {
                sender.sendMessage(incorrectUsage + plotAbandonUsage);
            }
        } else if (args[0].equalsIgnoreCase("group")) {
            if (args.length == 1) {
                plotGroup(sender);
            } else {
                sender.sendMessage(incorrectUsage + plotGroupUsage);
            }
        } else if (args[0].equalsIgnoreCase("unlock")) {
            if (args.length == 1) {
                plotUnlock(sender);
            } else {
                sender.sendMessage(incorrectUsage + plotUnlockUsage);
            }
        } else {
            sender.sendMessage(incorrectUsage + plotUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 0) {
            return null;
        } else {
            if (args.length == 1) {
                return Arrays.asList("contest", "surrender", "group", "unlock", "abandon");
            } else if (args[0].equalsIgnoreCase("contest")) {
                return null;
            } else if (args[0].equalsIgnoreCase("surrender")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("unlock")) {
                return null;
            } else {
                return null;
            }
        }
    }

    private void plot(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "----" + ChatColor.GOLD + " Plots " + ChatColor.YELLOW + "--" + ChatColor.GOLD + " Page " + ChatColor.RED + "1" + ChatColor.GOLD + "/" + ChatColor.RED + "1" + ChatColor.YELLOW + " ----");
        sender.sendMessage(ChatColor.GOLD + "/plot contest" + ChatColor.WHITE + " Contest a plot");
        sender.sendMessage(ChatColor.GOLD + "/plot surrender" + ChatColor.WHITE + " Surrender a plot to another nation");
        sender.sendMessage(ChatColor.GOLD + "/plot abandon" + ChatColor.WHITE + " Abandon a plot");
        sender.sendMessage(ChatColor.GOLD + "/plot group" + ChatColor.WHITE + " Get the group that controls a plot");
        sender.sendMessage(ChatColor.GOLD + "/plot unlock" + ChatColor.WHITE + " Destroy all signs in a plot");
    }

    private void plotUnlock(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);
        var chunk = player.getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to unlock this plot.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canUnlock = permissions.get("CanUnlock");

        if (!canUnlock) {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to unlock this plot.");
            return;
        }

        var chunkNation = diplomacyChunk.getNation();

        if (!Objects.equals(nation, chunkNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to unlock this plot.");
            return;
        }

        var unlocked = unlockChunk(chunk);

        if (unlocked) {
            sender.sendMessage(ChatColor.AQUA + "Plot unlocked.");
        } else {
            sender.sendMessage((ChatColor.RED + "This plot does not contain any signs."));
        }

    }

    private boolean unlockChunk(Chunk chunk) {

        var unlocked = false;

        final var minX = chunk.getX() * 16;
        final var minZ = chunk.getZ() * 16;
        final var maxY = chunk.getWorld().getMaxHeight();

        var signs = new ArrayList<Material>();
        signs.add(Material.SPRUCE_SIGN);
        signs.add(Material.SPRUCE_WALL_SIGN);
        signs.add(Material.ACACIA_SIGN);
        signs.add(Material.ACACIA_WALL_SIGN);
        signs.add(Material.BIRCH_SIGN);
        signs.add(Material.BIRCH_WALL_SIGN);
        signs.add(Material.CRIMSON_SIGN);
        signs.add(Material.CRIMSON_WALL_SIGN);
        signs.add(Material.DARK_OAK_SIGN);
        signs.add(Material.DARK_OAK_WALL_SIGN);
        signs.add(Material.JUNGLE_SIGN);
        signs.add(Material.JUNGLE_WALL_SIGN);
        signs.add(Material.OAK_SIGN);
        signs.add(Material.OAK_WALL_SIGN);
        signs.add(Material.WARPED_SIGN);
        signs.add(Material.WARPED_WALL_SIGN);

        for (int x = 0; x <= 15; ++x) {
            for (int y = 0; y <= 255; ++y) {
                for (int z = 0; z <= 15; ++z) {
                    var block = chunk.getBlock(x, y, z);
                    if (signs.contains(block.getType())) {
                        unlocked = true;
                        var drops = block.getDrops();
                        block.setType(Material.AIR);
                        for (var drop : drops) {
                            block.getWorld().dropItem(block.getLocation(), drop);
                        }
                    }
                }
            }
        }
        return unlocked;
    }


    private void plotContest(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var attackingNation = Nations.getInstance().get(diplomacyPlayer);
        var chunk = player.getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        var isWilderness = diplomacyChunk.getNation() == null;

        if (attackingNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to contest territory.");
            return;
        }

        var memberClass = attackingNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canContest = permissions.get("CanContest");


        if (!canContest) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to contest territory.");
            return;
        }

        if (Objects.equals(diplomacyChunk.getNation(), attackingNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot contest your own territory.");
            return;
        }

        if (ContestManager.getInstance().isBeingContested(diplomacyChunk)) {
            sender.sendMessage(ChatColor.DARK_RED + "This plot is already being contested.");
            return;
        }

        if (!isWilderness && attackingNation.getAllyNationIDs().contains(diplomacyChunk.getNation().getNationID())) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot contest an ally's territory.");
            return;
        }

        ContestManager.getInstance().startContest(attackingNation, diplomacyChunk, isWilderness);

        sender.sendMessage(ChatColor.AQUA + "Contest started.");

    }

    private void plotSurrender(CommandSender sender, String strOtherNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);
        var chunk = player.getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to surrender territory.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canSurrender = permissions.get("CanSurrenderPlot");

        if (!canSurrender) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to surrender territory.");
            return;
        }

        if (!Objects.equals(diplomacyChunk.getNation(), nation)) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot surrender territory that is not yours.");
            return;
        }

        var otherNation = Nations.getInstance().get(strOtherNation);

        if (otherNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation of '" + ChatColor.BLUE + strOtherNation + ChatColor.DARK_RED + "' does not exist.");
            return;
        }
        if (Objects.equals(nation, otherNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot surrender territory to your own nation.");
            return;
        }

        nation.removeChunk(diplomacyChunk);
        otherNation.addChunk(diplomacyChunk);
        var set = new HashSet<DiplomacyChunk>();
        set.add(diplomacyChunk);
        Bukkit.getPluginManager().callEvent(new NationAddChunksEvent(otherNation, set));
        Bukkit.getPluginManager().callEvent(new NationRemoveChunksEvent(nation, set));

        if (nation.getGroups() != null) {
            for (var group : nation.getGroups()) {
                if (group.getChunks().contains(diplomacyChunk)) {
                    group.removeChunk(diplomacyChunk);
                }
            }
        }
        var color = ChatColor.BLUE;
        if (otherNation.getAllyNationIDs().contains(nation.getNationID())) {
            color = ChatColor.GREEN;
        } else if (otherNation.getEnemyNationIDs().contains(nation.getNationID())) {
            color = ChatColor.RED;
        }

        sender.sendMessage(ChatColor.AQUA + "Plot surrendered to '" + color + otherNation.getName() + ChatColor.AQUA + "'.");

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var playerChunk = testPlayer.getLocation().getChunk();
            if (chunk.equals(playerChunk)) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
                Nation testNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testNation == null) {
                    testPlayer.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getEnemyNationIDs().contains(testNation.getNationID())) {
                    testPlayer.sendTitle(ChatColor.RED + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getAllyNationIDs().contains(testNation.getNationID()) || otherNation.equals(nation)) {
                    testPlayer.sendTitle(ChatColor.GREEN + otherNation.getName(), null, 5, 40, 10);
                } else {
                    testPlayer.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                }
            }
        }
    }

    private void plotAbandon(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);
        var chunk = player.getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to abandon territory.");
            return;
        }


        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canSurrender = permissions.get("CanSurrenderPlot");

        if (!canSurrender) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to abandon territory.");
            return;
        }

        if (!Objects.equals(diplomacyChunk.getNation(), nation)) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot abandon territory that is not yours.");
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
            var playerChunk = testPlayer.getLocation().getChunk();//TODO check
            if (diplomacyChunk.getChunk().equals(playerChunk)) {
                testPlayer.sendTitle(ChatColor.GRAY + "Wilderness", null, 5, 40, 10);
            }
        }
    }

    private void plotGroup(CommandSender sender) {
        Player player = (Player) sender;
        Chunk chunk = player.getLocation().getChunk();
        DiplomacyChunk diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        DiplomacyGroup group = diplomacyChunk.getGroup();
        if (group != null) {
            sender.sendMessage(ChatColor.AQUA + "This plot belongs to the group '" + group.getName() + "'.");
        } else {
            sender.sendMessage(ChatColor.AQUA + "This plot does not belong to any groups.");
        }
    }

    private void plotClear(CommandSender sender) {
        //TODO add functionality
    }
}
