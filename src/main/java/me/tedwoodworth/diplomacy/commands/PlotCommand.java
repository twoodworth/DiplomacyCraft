package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlotCommand implements CommandExecutor, TabCompleter {
    private static final String plotUsage = "/plot ...";
    private static final String plotContestUsage = "/plot contest";
    private static final String plotSurrenderUsage = "/plot surrender <nation>";
    private static final String plotAbandonUsage = "/plot abandon";
    private static final String plotGroupUsage = "/plot group";
    private static final String plotClearUsage = "/plot clear";


    public static void register(PluginCommand pluginCommand) {
        var plotCommand = new PlotCommand();

        pluginCommand.setExecutor(plotCommand);
        pluginCommand.setTabCompleter(plotCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            plot(sender);
        } else if (args[0].equalsIgnoreCase("contest")) {
            if (args.length == 1) {
                plotContest(sender);
            } else {
                sender.sendMessage(plotContestUsage);
            }
        } else if (args[0].equalsIgnoreCase("surrender")) {
            if (args.length == 2) {
                plotSurrender(sender, args[1]);
            } else {
                sender.sendMessage(plotSurrenderUsage);
            }
        } else if (args[0].equalsIgnoreCase("abandon")) {
            if (args.length == 1) {
                plotAbandon(sender);
            } else {
                sender.sendMessage(plotAbandonUsage);
            }
        } else if (args[0].equalsIgnoreCase("group")) {
            if (args.length == 1) {
                plotGroup(sender);
            } else {
                sender.sendMessage(plotGroupUsage);
            }
        } else if (args[0].equalsIgnoreCase("clear")) {
            if (args.length == 1) {
                plotClear(sender);
            } else {
                sender.sendMessage(plotClearUsage);
            }
        } else {
            sender.sendMessage(plotUsage);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return null;
        } else {
            if (args.length == 1) {
                return Arrays.asList("contest", "surrender", "group", "clear", "abandon");
            } else if (args[0].equalsIgnoreCase("contest")) {
                return null;
            } else if (args[0].equalsIgnoreCase("surrender")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("clear")) {
                return null;
            } else {
                return null;
            }
        }
    }

    private void plot(CommandSender sender) {
        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Manage plots:");
        sender.sendMessage(ChatColor.AQUA + plotContestUsage + ChatColor.GRAY + " Contest a plot");
        sender.sendMessage(ChatColor.AQUA + plotSurrenderUsage + ChatColor.GRAY + " Surrender a plot to another nation");
        sender.sendMessage(ChatColor.AQUA + plotAbandonUsage + ChatColor.GRAY + " Abandon a plot");
        sender.sendMessage(ChatColor.AQUA + plotGroupUsage + ChatColor.GRAY + " Get the group that controls a plot");
        sender.sendMessage(ChatColor.AQUA + plotClearUsage + ChatColor.GRAY + " Remove [private] signs from a plot");
    }

    private void plotContest(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be a player to use this command.");
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
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be in a nation to contest territory.");
            return;
        }

        var memberClass = attackingNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canContest = permissions.get("CanContest");


        if (!canContest) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You do not have permission to contest territory.");
            return;
        }

        if (Objects.equals(diplomacyChunk.getNation(), attackingNation)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You cannot contest your own territory.");
            return;
        }

        if (ContestManager.getInstance().isBeingContested(diplomacyChunk)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "This plot is already being contested.");
            return;
        }

        if (!isWilderness && attackingNation.getAllyNationIDs().contains(diplomacyChunk.getNation().getNationID())) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You cannot contest an ally's territory.");
            return;
        }

        ContestManager.getInstance().startContest(attackingNation, diplomacyChunk, isWilderness);

        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Contest started.");

    }

    private void plotSurrender(CommandSender sender, String strOtherNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be a player to use this command.");
            return;
        }
        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);
        var chunk = player.getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);

        if (nation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be in a nation to surrender territory.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canSurrender = permissions.get("CanSurrenderPlot");

        if (!canSurrender) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You do not have permission to surrender territory.");
            return;
        }

        if (!Objects.equals(diplomacyChunk.getNation(), nation)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You cannot surrender territory that is not yours.");
            return;
        }

        var otherNation = Nations.getInstance().get(strOtherNation);

        if (otherNation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "The nation of '" + strOtherNation + "' does not exist.");
            return;
        }
        if (Objects.equals(nation, otherNation)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You cannot surrender territory to your own nation.");
            return;
        }

        nation.removeChunk(diplomacyChunk);
        otherNation.addChunk(diplomacyChunk);

        if (nation.getGroups() != null) {
            for (var group : nation.getGroups()) {
                if (group.getChunks().contains(diplomacyChunk)) {
                    group.removeChunk(diplomacyChunk);
                }
            }
        }
        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Plot surrendered to '" + otherNation.getName() + "'.");

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var playerChunk = player.getLocation().getChunk();
            if (chunk.equals(playerChunk)) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
                Nation testNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testNation == null) {
                    player.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getEnemyNationIDs().contains(testNation.getNationID())) {
                    player.sendTitle(ChatColor.RED + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getAllyNationIDs().contains(testNation.getNationID()) || otherNation.equals(nation)) {
                    player.sendTitle(ChatColor.GREEN + otherNation.getName(), null, 5, 40, 10);
                } else {
                    player.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                }
            }
        }
    }

    private void plotAbandon(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be a player to use this command.");
            return;
        }
        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);
        var chunk = player.getLocation().getChunk();
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);

        if (nation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be in a nation to abandon territory.");
            return;
        }


        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canSurrender = permissions.get("CanSurrenderPlot");

        if (!canSurrender) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You do not have permission to abandon territory.");
            return;
        }

        if (!Objects.equals(diplomacyChunk.getNation(), nation)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You cannot abandon territory that is not yours.");
            return;
        }

        nation.removeChunk(diplomacyChunk);

        if (diplomacyChunk.getGroup() != null) {
            diplomacyChunk.getGroup().removeChunk(diplomacyChunk);
        }

        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Plot abandoned.");

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var playerChunk = player.getLocation().getChunk();
            if (chunk.equals(playerChunk)) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
                Nation testNation = Nations.getInstance().get(testDiplomacyPlayer);
                player.sendTitle(ChatColor.GRAY + "Wilderness", null, 5, 40, 10);
            }
        }
    }

    private void plotGroup(CommandSender sender) {
        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        Chunk chunk = player.getLocation().getChunk();
        DiplomacyChunk diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        DiplomacyGroup group = diplomacyChunk.getGroup();
        if (group != null) {
            sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "This plot belongs to the group '" + group.getName() + "'.");
        } else {
            sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "This plot does not belong to any groups.");
        }
    }

    private void plotClear(CommandSender sender) {
        //TODO add functionality
    }
}
