package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;

public class NationCommand implements CommandExecutor, TabCompleter {
    private static final String nationCreateUsage = "/nation create <nation>";
    private static final String nationRenameUsage = "/nation rename <nation>";
    private static final String nationSurrenderUsage = "/nation surrender <nation>";
    private static final String nationDisbandUsage = "/nation surrender";
    private static final String nationAllyUsage = "/nation ally <nation>";
    private static final String nationAcceptUsage = "/nation accept <nation>";
    private static final String nationNeutralUsage = "/nation neutral <nation>";
    private static final String nationEnemyUsage = "/nation enemy <nation>";
    private static final String nationListUsage = "/nation list";
    private static final String nationInviteUsage = "/nation invite <player>";
    private static final String nationJoinUsage = "/nation join <nation>";
    private static final String nationKickUsage = "/nation kick <player>";
    private static final String nationOpenUsage = "/nation open";
    private static final String nationCloseUsage = "/nation close";
    private static final String nationBannerUsage = "/nation banner";
    private static final String nationOutlawAddUsage = "/nation outlaw add <player>";
    private static final String nationOutlawRemoveUsage = "/nation outlaw remove <player>";
    private static final String nationOutlawListUsage = "/nation outlaw list <page>";
    private static final DecimalFormat formatter = new DecimalFormat("#,###.00");


    public static void register(PluginCommand pluginCommand) {
        var nationCommand = new NationCommand();

        pluginCommand.setExecutor(nationCommand);
        pluginCommand.setTabCompleter(nationCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            nation(sender);
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                nationCreate(sender, args[1]);
            } else {
                sender.sendMessage(nationCreateUsage);
            }
        } else if (args[0].equalsIgnoreCase("rename")) {
            if (args.length == 2) {
                nationRename(sender, args[1]);
            } else {
                sender.sendMessage(nationRenameUsage);
            }
        } else if (args[0].equalsIgnoreCase("surrender")) {
            if (args.length == 2) {
                nationSurrender(sender, args[1]);
            } else {
                sender.sendMessage(nationSurrenderUsage);
            }
        } else if (args[0].equalsIgnoreCase("disband")) {
            if (args.length == 1) {
                nationDisband(sender);
            } else {
                sender.sendMessage(nationDisbandUsage);
            }
        } else if (args[0].equalsIgnoreCase("ally")) {
            if (args.length == 2) {
                nationAlly(sender, args[1]);
            } else {
                sender.sendMessage(nationAllyUsage);
            }
        } else if (args[0].equalsIgnoreCase("accept")) {
            if (args.length == 2) {
                nationAccept(sender, args[1]);
            } else {
                sender.sendMessage(nationAcceptUsage);
            }
        } else if (args[0].equalsIgnoreCase("neutral")) {
            if (args.length == 2) {
                nationNeutral(sender, args[1]);
            } else {
                sender.sendMessage(nationNeutralUsage);
            }
        } else if (args[0].equalsIgnoreCase("enemy")) {
            if (args.length == 2) {
                nationEnemy(sender, args[1]);
            } else {
                sender.sendMessage(nationEnemyUsage);
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 2) {
                nationList(sender);
            } else {
                sender.sendMessage(nationListUsage);
            }
        } else if (args[0].equalsIgnoreCase("invite")) {
            if (args.length == 2) {
                nationInvite(sender, args[1]);
            } else {
                sender.sendMessage(nationInviteUsage);
            }
        } else if (args[0].equalsIgnoreCase("join")) {
            if (args.length == 2) {
                nationJoin(sender, args[1]);
            } else {
                sender.sendMessage(nationJoinUsage);
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (args.length == 1) {
                nationLeave(sender);
            } else {
                sender.sendMessage(nationJoinUsage);
            }
        } else if (args[0].equalsIgnoreCase("kick")) {
            if (args.length == 2) {
                nationKick(sender, args[1]);
            } else {
                sender.sendMessage(nationKickUsage);
            }
        } else if (args[0].equalsIgnoreCase("open")) {
            if (args.length == 2) {
                nationOpen(sender);
            } else {
                sender.sendMessage(nationOpenUsage);
            }
        } else if (args[0].equalsIgnoreCase("close")) {
            if (args.length == 1) {
                nationClose(sender);
            } else {
                sender.sendMessage(nationCloseUsage);
            }
        } else if (args[0].equalsIgnoreCase("banner")) {
            if (args.length == 2) {
                nationBanner(sender, args[1]);
            } else {
                sender.sendMessage(nationBannerUsage);
            }
        } else if (args[0].equalsIgnoreCase("outlaw")) {
            if (args[1].equalsIgnoreCase("add")) {
                if (args.length == 3) {
                    nationOutlawAdd(sender, args[2]);
                } else {
                    sender.sendMessage(nationOutlawAddUsage);
                }
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (args.length == 3) {
                    nationOutlawRemove(sender, args[2]);
                } else {
                    sender.sendMessage(nationOutlawRemoveUsage);
                }
            } else if (args[1].equalsIgnoreCase("list")) {
                if (args.length == 2) {
                    nationOutlawList(sender, "1");
                } else if (args.length == 3) {
                    nationOutlawList(sender, args[2]);
                }
            } else {
                sender.sendMessage(nationOutlawListUsage);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 0) {
            return null;
        } else {
            if (args.length == 1) {
                return Arrays.asList(
                        "create",
                        "rename",
                        "surrender",
                        "ally",
                        "accept",
                        "neutral",
                        "enemy",
                        "list",
                        "invite",
                        "join",
                        "leave",
                        "kick",
                        "open",
                        "close",
                        "outlaw");
            } else if (args[0].equalsIgnoreCase("create")) {
                return null;
            } else if (args[0].equalsIgnoreCase("rename")) {
                return null;
            } else if (args[0].equalsIgnoreCase("surrender")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("disband")) {
                return null;
            } else if (args[0].equalsIgnoreCase("ally")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("accept")) {
                return null; //TODO List Nations with Pending Ally Requests
            } else if (args[0].equalsIgnoreCase("neutral")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("enemy")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("list")) {
                return null;
            } else if (args[0].equalsIgnoreCase("invite")) {
                return null; //TODO List Nation-less players
            } else if (args[0].equalsIgnoreCase("join")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("kick")) {
                return null; //TODO List Players in nation that can be kicked
            } else if (args[0].equalsIgnoreCase("open")) {
                return null;
            } else if (args[0].equalsIgnoreCase("close")) {
                return null;
            } else if (args[0].equalsIgnoreCase("outlaw")) {
                if (args.length == 2) {
                    return Arrays.asList("add", "remove", "list");
                } else if (args[1].equalsIgnoreCase("add")) {
                    return null; //TODO List Players
                } else if (args[1].equalsIgnoreCase("remove")) {
                    return null; //TODO List Outlawed players
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private void nation(CommandSender sender) {

    }

    private void nationCreate(CommandSender sender, String name) {
        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var leader = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(leader);
        var sameName = Nations.getInstance().get(name);
        if (nation == null) {
            if (sameName == null) {
                Nations.getInstance().createNation(name, leader);
                sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "The nation of '" + name + "' has been founded.");
            } else {
                sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "The name '" + name + "' is taken, choose another name.");
            }
        } else {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must leave your current nation before you can establish a new nation.");
        }
    }

    private void nationRename(CommandSender sender, String name) {
        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You are not in a nation.");
            return;
        }

        var nationClass = nation.getMemberClass(diplomacyPlayer);
        boolean canRenameNation = nationClass.getPermissions().get("CanRenameNation");
        if (!canRenameNation) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You do not have permission to rename the nation.");
            return;
        }

        var oldName = nation.getName();
        if (oldName.equals(name)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "The nation is already named '" + name + "'.");
            return;
        }

        var nameTaken = Nations.getInstance().get(name) != null;
        if (nameTaken) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "The name \'" + name + "\' is taken, choose another name.");
            return;
        }

        Nations.getInstance().rename(name, nation);

        for (var player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "The nation of '" + oldName + "' has been renamed to '" + name + "'.");
        }
    }

    private void nationSurrender(CommandSender sender, String otherNationName) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        Nation nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be in a nation to surrender a nation.");
            return;
        }


        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canSurrender = permissions.get("CanSurrenderNation");

        if (!canSurrender) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You do not have permission to surrender your nation.");
            return;
        }

        Nation otherNation = Nations.getInstance().get(otherNationName);
        if (otherNation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "The nation of '" + otherNationName + "' does not exist.");
            return;
        }

        if (Objects.equals(nation, otherNation)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You cannot surrender your nation to itself.");
            return;
        }

        var members = nation.getMembers();
        for (var member : members) {
            var diplomacyMember = DiplomacyPlayers.getInstance().get(UUID.fromString(member));
            if (otherNation.getIsOpen()) {
                otherNation.addMember(diplomacyMember);
            }
            nation.removeMember(diplomacyMember);
        }


        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(player.getLocation().getChunk());
            var testNation = testDiplomacyChunk.getNation();
            if (Objects.equals(testNation, nation)) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
                Nation testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testPlayerNation == null) {
                    player.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getEnemyNationIDs().contains(testPlayerNation.getNationID())) {
                    player.sendTitle(ChatColor.RED + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getAllyNationIDs().contains(testPlayerNation.getNationID()) || otherNation.equals(testPlayerNation)) {
                    player.sendTitle(ChatColor.GREEN + otherNation.getName(), null, 5, 40, 10);
                } else {
                    player.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                }
            }
        }

        var groups = nation.getGroups();
        for (var group : groups) {
            group.setNation(otherNation);
        }

        var contests = ContestManager.getInstance().getContests();
        for (var contest : contests) {
            var attackingNation = contest.getAttackingNation();
            if (attackingNation.equals(nation)) {
                ContestManager.getInstance().endContest(contest);
            } else if (Objects.equals(contest.getDiplomacyChunk().getNation(), nation)) {
                if (attackingNation.equals(otherNation)) {
                    ContestManager.getInstance().winContest(contest);
                } else if (attackingNation.getAllyNationIDs().contains(nation.getNationID())) {
                    ContestManager.getInstance().endContest(contest);
                }
            }
        }

        var diplomacyChunks = nation.getChunks();
        for (var diplomacyChunk : diplomacyChunks) {
            nation.removeChunk(diplomacyChunk);
            otherNation.addChunk(diplomacyChunk);
        }

        var balance = nation.getBalance();
        if (nation.getBalance() >= 0.01) {
            Diplomacy.getEconomy().depositPlayer(player, balance);
            if (balance >= 1.0) {
                sender.sendMessage(ChatColor.GREEN + "\u00A4" + formatter.format(balance) + "' has been transferred from " + nation.getName() + " to your bank account.");
            } else {
                sender.sendMessage(ChatColor.GREEN + "\u00A40" + formatter.format(balance) + "' has been transferred from " + nation.getName() + " to your bank account.");
            }
        }
        nation.setBalance(0.0);


        Nations.getInstance().removeNation(nation);

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "The nation '" + nation.getName() + "' has surrendered to '" + otherNation.getName() + "'.");
        }

    }

    private void nationDisband(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        Nation nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be in a nation to disband a nation.");
            return;
        }


        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canSurrender = permissions.get("CanSurrenderNation");

        if (!canSurrender) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You do not have permission to disband your nation.");
            return;
        }

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(player.getLocation().getChunk());
            var testNation = testDiplomacyChunk.getNation();
            if (Objects.equals(testNation, nation)) {
                player.sendTitle(ChatColor.GRAY + "Wilderness", null, 5, 40, 10);
            }
        }

        var groups = nation.getGroups();
        for (var group : groups) {
            var leaders = DiplomacyPlayers.getInstance().getLeaders(group);
            var members = DiplomacyPlayers.getInstance().getMembers(group);
            for (var leader : leaders) {
                leader.removeGroupLed(group);
            }

            for (var member : members) {
                member.removeGroup(group);
            }

            DiplomacyGroups.getInstance().removeGroup(group);
        }

        var contests = ContestManager.getInstance().getContests();
        for (var contest : contests) {
            var attackingNation = contest.getAttackingNation();
            if (attackingNation.equals(nation)) {
                ContestManager.getInstance().endContest(contest);
            } else if (Objects.equals(contest.getDiplomacyChunk().getNation(), nation)) {
                contest.setIsWilderness(true);
            }
        }

        var diplomacyChunks = nation.getChunks();
        for (var diplomacyChunk : diplomacyChunks) {
            nation.removeChunk(diplomacyChunk);

        }

        var balance = nation.getBalance();
        if (nation.getBalance() >= 0.01) {
            Diplomacy.getEconomy().depositPlayer(player, balance);
            if (balance >= 1.0) {
                sender.sendMessage(ChatColor.GREEN + "\u00A4" + formatter.format(balance) + "' has been transferred from " + nation.getName() + " to your bank account.");
            } else {
                sender.sendMessage(ChatColor.GREEN + "\u00A40" + formatter.format(balance) + "' has been transferred from " + nation.getName() + " to your bank account.");
            }
        }
        nation.setBalance(0.0);


        Nations.getInstance().removeNation(nation);

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "The nation '" + nation.getName() + "' has disbanded.");
        }


    }

    private void nationAlly(CommandSender sender, String nation) {

    }

    private void nationAccept(CommandSender sender, String nation) {

    }

    private void nationNeutral(CommandSender sender, String nation) {

    }

    private void nationEnemy(CommandSender sender, String strEnemyNation) {
        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var senderNation = Nations.getInstance().get(diplomacyPlayer);

        if (senderNation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be in a nation to become enemies with another nation.");
            return;
        }

        var memberClass = senderNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canEnemyNations = permissions.get("CanEnemyNations");
        if (!canEnemyNations) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You do not have permission to become enemies with other nations.");
            return;
        }

        var enemyNation = Nations.getInstance().get(strEnemyNation);

        if (enemyNation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "The nation '" + strEnemyNation + "' does not exist.");
            return;
        }

        if (Objects.equals(senderNation, enemyNation)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Your nation cannot become enemies with itself.");
            return;
        }

        if (senderNation.getEnemyNationIDs().contains(enemyNation.getNationID())) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Your nation is already enemies with '" + enemyNation.getName() + "'.");
            return;
        }

        senderNation.addEnemyNation(enemyNation);
        enemyNation.addEnemyNation(senderNation);

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(testPlayerNation, enemyNation)) {
                sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Your nation is now enemies with '" + senderNation.getName() + "'.");
            } else if (Objects.equals(testPlayerNation, senderNation)) {
                sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Your nation is now enemies with '" + enemyNation.getName() + "'.");
            }
        }
        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Your nation is now enemies with '" + enemyNation.getName() + "'.");
    }

    private void nationList(CommandSender sender) {

    }

    private void nationInvite(CommandSender sender, String player) {

    }

    private void nationJoin(CommandSender sender, String nation) {

    }

    private void nationLeave(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You are already not in a nation.");
            return;
        }

        var members = nation.getMembers();

        if (members.size() == 1) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You cannot use /leave because you are the only member left in your nation.");
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "To leave, you must either surrender your nation to another nation or disband it.");
            return;
        }

        var leaderCount = 0;
        for (var member : members) {
            var diplomacyMember = DiplomacyPlayers.getInstance().get(UUID.fromString(member));
            if (nation.getMemberClass(diplomacyMember).equals("8")) {
                leaderCount++;
            }
        }

        if (leaderCount == 1) {
            String className = Objects.requireNonNull(nation.getClassFromID("8")).getName();
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must grant another player the class '" + className + "' before you can leave.");
        }

        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "You have left '" + nation.getName() + "'.");
        nation.removeMember(diplomacyPlayer);


    }

    private void nationKick(CommandSender sender, String player) {

    }

    private void nationOpen(CommandSender sender) {

    }

    private void nationClose(CommandSender sender) {

    }

    private void nationBanner(CommandSender sender, String banner) {

    }

    private void nationOutlawAdd(CommandSender sender, String player) {

    }

    private void nationOutlawRemove(CommandSender sender, String player) {

    }

    private void nationOutlawList(CommandSender sender, String page) {

    }

    private void nationDeposit(CommandSender sender, String strAmount) {

    }

    private void nationWithdraw(CommandSender sender, String strAmount) {

    }
}
