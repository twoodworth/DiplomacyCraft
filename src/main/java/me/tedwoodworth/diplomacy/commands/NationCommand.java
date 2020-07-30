package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyException;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.NationGuiFactory;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BannerMeta;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.*;

public class NationCommand implements CommandExecutor, TabCompleter {
    private static final String nationCreateUsage = "/nation create <name>";
    private static final String nationInfoUsage = "/nation info <nation>";
    private static final String nationRenameUsage = "/nation rename <name>";
    private static final String nationSurrenderUsage = "/nation surrender <nation>";
    private static final String nationDisbandUsage = "/nation disband";
    private static final String nationAllyUsage = "/nation ally <nation>";
    private static final String nationAcceptUsage = "/nation accept [ally/neutral] <nation>";
    private static final String nationDeclineUsage = "/nation decline [ally/neutral] <nation>";
    private static final String nationNeutralUsage = "/nation neutral <nation>";
    private static final String nationEnemyUsage = "/nation enemy <nation>";
    private static final String nationRelationshipUsage = "/nation (ally/neutral/enemy) <nation>";
    private static final String nationListUsage = "/nation list";
    private static final String nationInviteUsage = "/nation invite <player>";
    private static final String nationJoinUsage = "/nation join <nation>";
    private static final String nationDeclineJoinUsage = "/nation DeclineJoin <nation>";
    private static final String nationLeaveUsage = "/nation leave";
    private static final String nationLeaveConfirmUsage = "/nation leave confirm";
    private static final String nationKickUsage = "/nation kick <player>";
    private static final String nationOpenUsage = "/nation open";
    private static final String nationCloseUsage = "/nation close";
    private static final String nationBorderUsage = "/nation (open/close)";
    private static final String nationBannerUsage = "/nation banner";
    private static final String nationOutlawUsage = "/nation outlaw (add/remove) <player>";
    private static final String nationOutlawAddUsage = "/nation outlaw add <player>";
    private static final String nationOutlawRemoveUsage = "/nation outlaw remove <player>";
    private static final String nationOutlawListUsage = "/nation outlaw list <page>";
    private static final String nationDepositUsage = "/nation deposit <amount>";
    private static final String nationWithdrawUsage = "/nation withdraw <amount>";
    private static final String nationDepositWithdrawUsage = "/nation (deposit/withdraw) <amount>";

    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");
    private Map<String, Long> requests = new HashMap<>();
    private int requestTaskID = -1;

    public static void register(PluginCommand pluginCommand) {
        var nationCommand = new NationCommand();

        pluginCommand.setExecutor(nationCommand);
        pluginCommand.setTabCompleter(nationCommand);
    }

    private void onRequestTask() {
        for (var requestKey : new ArrayList<>(requests.keySet())) {
            if (Instant.now().getEpochSecond() - requests.get(requestKey) > 60) {
                requests.remove(requestKey);
            }
        }
        if (requests.size() == 0) {
            cancelTask();
        }
    }

    private void cancelTask() {
        Bukkit.getScheduler().cancelTask(requestTaskID);
        requestTaskID = -1;
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
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 2) {
                nationInfo(sender, args[1]);
            } else {
                sender.sendMessage(nationInfoUsage);
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
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("ally")) {
                    nationAcceptAlly(sender, args[2]);
                } else if (args[1].equalsIgnoreCase("neutral")) {
                    nationAcceptNeutral(sender, args[2]);
                }
            } else {
                sender.sendMessage(nationAcceptUsage);
            }
        } else if (args[0].equalsIgnoreCase("decline")) {
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("ally")) {
                    nationDeclineAlly(sender, args[2]);
                } else if (args[1].equalsIgnoreCase("neutral")) {
                    nationDeclineNeutral(sender, args[2]);
                }
            } else {
                sender.sendMessage(nationDeclineUsage);
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
        } else if (args[0].equalsIgnoreCase("declinejoin")) {
            if (args.length == 2) {
                nationDeclineJoin(sender, args[1]);
            } else {
                sender.sendMessage(nationDeclineJoinUsage);
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (args.length == 1) {
                nationLeave(sender);
            } else if (args[1].equalsIgnoreCase("confirm")) {
                if (args.length == 2) {
                    nationLeaveConfirm(sender);
                } else {
                    sender.sendMessage(nationLeaveConfirmUsage);
                }
            } else {
                sender.sendMessage(nationLeaveUsage);
            }
        } else if (args[0].equalsIgnoreCase("kick")) {
            if (args.length == 2) {
                nationKick(sender, args[1]);
            } else {
                sender.sendMessage(nationKickUsage);
            }
        } else if (args[0].equalsIgnoreCase("open")) {
            if (args.length == 1) {
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
            if (args.length == 1) {
                nationBanner(sender);
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
        } else if (args[0].equalsIgnoreCase("deposit")) {
            if (args.length == 2) {
                nationDeposit(sender, args[1]);
            } else {
                sender.sendMessage(nationDepositUsage);
            }
        } else if (args[0].equalsIgnoreCase("withdraw")) {
            if (args.length == 2) {
                nationWithdraw(sender, args[1]);
            } else {
                sender.sendMessage(nationWithdrawUsage);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 0) {
            if (args.length == 1) {
                return Arrays.asList(
                        "create",
                        "banner",
                        "rename",
                        "surrender",
                        "disband",
                        "ally",
                        "accept",
                        "decline",
                        "neutral",
                        "enemy",
                        "list",
                        "info",
                        "invite",
                        "join",
                        "declineJoin",
                        "leave",
                        "kick",
                        "open",
                        "close",
                        "outlaw");
            } else if (args[0].equalsIgnoreCase("create")) {
                return null;
            } else if (args[0].equalsIgnoreCase("info")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("rename")) {
                return null;
            } else if (args[0].equalsIgnoreCase("banner")) {
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
                if (args.length == 2) {
                    return Arrays.asList(
                            "neutral",
                            "ally");
                } else if (args.length == 3) {
                    List<String> nations = new ArrayList<>();
                    for (var nation : Nations.getInstance().getNations())
                        nations.add(nation.getName());
                    return nations;
                }
            } else if (args[0].equalsIgnoreCase("decline")) {
                if (args.length == 2) {
                    return Arrays.asList(
                            "neutral",
                            "ally");
                } else if (args.length == 3) {
                    List<String> nations = new ArrayList<>();
                    for (var nation : Nations.getInstance().getNations())
                        nations.add(nation.getName());
                    return nations;
                }
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
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                        var nation = Nations.getInstance().get(diplomacyPlayer);
                        if (nation == null) {
                            players.add(player.getName());
                        }
                    }
                    return players;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("join")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("declineJoin")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("leave")) {
                return null;
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
            }
        }
        return null;
    }

    private void nation(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Manage Nations:");
        sender.sendMessage(ChatColor.AQUA + nationListUsage + ChatColor.GRAY + " Get a list of all nations");
        sender.sendMessage(ChatColor.AQUA + nationInfoUsage + ChatColor.GRAY + " Get info about a nation");
        sender.sendMessage(ChatColor.AQUA + nationCreateUsage + ChatColor.GRAY + " Create a nation");
        sender.sendMessage(ChatColor.AQUA + nationRenameUsage + ChatColor.GRAY + " Rename a nation");
        sender.sendMessage(ChatColor.AQUA + nationSurrenderUsage + ChatColor.GRAY + " Surrender your nation");
        sender.sendMessage(ChatColor.AQUA + nationDisbandUsage + ChatColor.GRAY + " Disband your nation");
        sender.sendMessage(ChatColor.AQUA + nationInviteUsage + ChatColor.GRAY + " Invite a player to your nation");
        sender.sendMessage(ChatColor.AQUA + nationJoinUsage + ChatColor.GRAY + " Join a nation");
        sender.sendMessage(ChatColor.AQUA + nationKickUsage + ChatColor.GRAY + " Kick a player from your nation");
        sender.sendMessage(ChatColor.AQUA + nationLeaveUsage + ChatColor.GRAY + " Leave your nation");
        sender.sendMessage(ChatColor.AQUA + nationDepositWithdrawUsage + ChatColor.GRAY + " Deposit/withdraw from your nation's balance");
        sender.sendMessage(ChatColor.AQUA + nationRelationshipUsage + ChatColor.GRAY + " Become allies/neutral/enemies with another nation");
        sender.sendMessage(ChatColor.AQUA + nationBorderUsage + ChatColor.GRAY + " Open/close your nation's borders");
        sender.sendMessage(ChatColor.AQUA + nationBannerUsage + ChatColor.GRAY + " Set your nation's banner");
        sender.sendMessage(ChatColor.AQUA + nationOutlawUsage + ChatColor.GRAY + " Add/remove outlaws");
    }

    private void nationInfo(CommandSender sender, String strNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var nation = Nations.getInstance().get(strNation);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        var gui = new NationGuiFactory().create(nation, player);
        gui.show(player);
    }

    private void nationCreate(CommandSender sender, String name) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var leader = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(leader);
        var sameName = Nations.getInstance().get(name);
        if (nation == null) {
            if (sameName == null) {
                Nations.getInstance().createNation(name, leader);
                sender.sendMessage(ChatColor.AQUA + "The nation of " + ChatColor.GREEN + name + ChatColor.AQUA + " has been founded.");
                for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
                    if (!Objects.equals(uuid, onlinePlayer.getUniqueId())) {
                        onlinePlayer.sendMessage(ChatColor.AQUA + "The nation of " + ChatColor.BLUE + name + ChatColor.AQUA + " has been founded.");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.DARK_RED + "The name " + ChatColor.BLUE + name + ChatColor.DARK_RED + " is taken, choose another name.");
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "You must leave your current nation before you can establish a new nation.");
        }
    }

    private void nationRename(CommandSender sender, String name) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        try {
            Nations.getInstance().rename(nation, name);
        } catch (DiplomacyException e) {
            e.printStackTrace();
        }
    }

    private void nationSurrender(CommandSender sender, String otherNationName) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        Nation nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to surrender a nation.");
            return;
        }


        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canSurrender = permissions.get("CanSurrenderNation");

        if (!canSurrender) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to surrender your nation.");
            return;
        }

        Nation otherNation = Nations.getInstance().get(otherNationName);
        if (otherNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation of " + ChatColor.BLUE + otherNationName + ChatColor.DARK_RED + " does not exist.");
            return;
        }

        if (Objects.equals(nation, otherNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot surrender your nation to itself.");
            return;
        }

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            if (Nations.getInstance().get(testDiplomacyPlayer) != null) {
                var testNation = Nations.getInstance().get(testDiplomacyPlayer);
                var color1 = ChatColor.BLUE;
                var color2 = ChatColor.BLUE;
                if (Objects.requireNonNull(testNation).getEnemyNationIDs().contains(otherNation.getNationID())) {
                    color2 = ChatColor.RED;
                } else if (testNation.getAllyNationIDs().contains(otherNation.getNationID()) || Objects.equals(testNation, otherNation)) {
                    color2 = ChatColor.GREEN;
                }
                if (testNation.getEnemyNationIDs().contains(nation.getNationID())) {
                    color1 = ChatColor.RED;
                } else if (testNation.getAllyNationIDs().contains(nation.getNationID()) || Objects.equals(testNation, nation)) {
                    color1 = ChatColor.GREEN;
                }
                onlinePlayer.sendMessage(ChatColor.AQUA + "The nation " + color1 + nation.getName() + ChatColor.AQUA + " has surrendered to " + color2 + otherNation.getName() + ChatColor.AQUA + ".");
            } else {
                onlinePlayer.sendMessage(ChatColor.AQUA + "The nation " + ChatColor.BLUE + nation.getName() + ChatColor.AQUA + " has surrendered to " + ChatColor.BLUE + otherNation.getName() + ChatColor.AQUA + ".");
            }
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
                sender.sendMessage(ChatColor.GREEN + "\u00A4" + formatter.format(balance) + " has been transferred from " + nation.getName() + " to your bank account.");

        }
        nation.setBalance(0.0);

        for (var testNationID : nation.getAllyNationIDs()) {
            var testNation = Nations.getInstance().getFromID(testNationID);
            Objects.requireNonNull(testNation).removeAllyNation(nation);
            nation.removeAllyNation(testNation);
        }

        for (var testNationID : nation.getEnemyNationIDs()) {
            var testNation = Nations.getInstance().getFromID(testNationID);
            Objects.requireNonNull(testNation).removeEnemyNation(nation);
            nation.removeEnemyNation(testNation);
        }

        Nations.getInstance().removeNation(nation);

    }

    private void nationDisband(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        Nation nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to disband a nation.");
            return;
        }


        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canSurrender = permissions.get("CanSurrenderNation");

        if (!canSurrender) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to disband your nation.");
            return;
        }

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(testPlayer.getLocation().getChunk());
            var testNation = testDiplomacyChunk.getNation();
            if (Objects.equals(testNation, nation)) {
                testPlayer.sendTitle(ChatColor.GRAY + "Wilderness", null, 5, 40, 10);
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
                sender.sendMessage(ChatColor.GREEN + "\u00A4" + formatter.format(balance) + " has been transferred from " + nation.getName() + " to your bank account.");
        }
        nation.setBalance(0.0);

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            var color = ChatColor.BLUE;
            if (testNation != null) {
                if (testNation.getEnemyNationIDs().contains(nation.getNationID())) {
                    color = ChatColor.RED;
                } else if (testNation.getAllyNationIDs().contains(nation.getNationID())) {
                    color = ChatColor.GREEN;
                }
            }
            onlinePlayer.sendMessage(ChatColor.AQUA + "The nation " + color + nation.getName() + ChatColor.AQUA + " has disbanded.");
        }

        for (var testNationID : nation.getAllyNationIDs()) {
            var testNation = Nations.getInstance().getFromID(testNationID);
            Objects.requireNonNull(testNation).removeAllyNation(nation);
            nation.removeAllyNation(testNation);
        }

        for (var testNationID : nation.getEnemyNationIDs()) {
            var testNation = Nations.getInstance().getFromID(testNationID);
            Objects.requireNonNull(testNation).removeEnemyNation(nation);
            nation.removeEnemyNation(testNation);
        }

        Nations.getInstance().removeNation(nation);


    }

    private void nationAlly(CommandSender sender, String strAllyNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var senderNation = Nations.getInstance().get(diplomacyPlayer);

        if (senderNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to become allies with another nation.");
            return;
        }

        var memberClass = senderNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canAllyNations = permissions.get("CanAllyNations");
        if (!canAllyNations) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to become allies with other nations.");
            return;
        }

        var receiverNation = Nations.getInstance().get(strAllyNation);

        if (receiverNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation " + ChatColor.BLUE + strAllyNation + ChatColor.DARK_RED + " does not exist.");
            return;
        }

        if (Objects.equals(senderNation, receiverNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation cannot become enemies with itself.");
            return;
        }

        if (senderNation.getAllyNationIDs().contains(receiverNation.getNationID())) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation is already allies with " + ChatColor.GREEN + receiverNation.getName() + ChatColor.DARK_RED + ".");
            return;
        }

        var requestKey = senderNation.getNationID() + "_AllyRequest_" + receiverNation.getNationID();
        if (requests.containsKey(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Request already sent.");
            return;
        }

        requests.put(requestKey, Instant.now().getEpochSecond());

        if (requestTaskID == -1) {
            requestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onRequestTask, 0L, 20L);
        }

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
            var testMemberClass = testPlayerNation.getMemberClass(testDiplomacyPlayer);
            var testPermissions = testMemberClass.getPermissions();
            boolean testCanAllyNations = testPermissions.get("CanAllyNations");
            if (Objects.equals(testPlayerNation, receiverNation) && testCanAllyNations) {
                var acceptHoverText = new ComponentBuilder()
                        .append("Click to accept")
                        .color(net.md_5.bungee.api.ChatColor.GREEN)
                        .create();

                var declineHoverText = new ComponentBuilder()
                        .append("Click to decline")
                        .color(net.md_5.bungee.api.ChatColor.RED)
                        .create();

                var color = net.md_5.bungee.api.ChatColor.BLUE;

                if (receiverNation.getEnemyNationIDs().contains(senderNation.getNationID())) {
                    color = net.md_5.bungee.api.ChatColor.RED;
                }

                var senderNationComponent = new ComponentBuilder()
                        .append(senderNation.getName())
                        .color(color)
                        .create();

                var message = new ComponentBuilder()
                        .append(senderNationComponent)
                        .append(" has requested an alliance. ")
                        .color(net.md_5.bungee.api.ChatColor.AQUA)
                        .append("[Accept]")
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation accept ally " + senderNation.getName()))
                        .color(net.md_5.bungee.api.ChatColor.GREEN)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(acceptHoverText)))
                        .bold(true)
                        .append(" ")
                        .append("[Decline]")
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation decline ally " + senderNation.getName()))
                        .color(net.md_5.bungee.api.ChatColor.RED)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(declineHoverText)))
                        .bold(true)
                        .create();
                testPlayer.spigot().sendMessage(message);
            } else if (Objects.equals(testPlayerNation, senderNation) && testCanAllyNations) {
                var color = ChatColor.BLUE;

                if (receiverNation.getEnemyNationIDs().contains(senderNation.getNationID())) {
                    color = ChatColor.RED;
                }
                testPlayer.sendMessage(ChatColor.AQUA + "An alliance request has been sent to " + color + senderNation.getName() + ChatColor.AQUA + " (Expires in 60 seconds)");
            }
        }
    }

    private void nationAcceptAlly(CommandSender sender, String strSenderNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var receiverNation = Nations.getInstance().get(diplomacyPlayer);

        if (receiverNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to become allies with another nation.");
            return;
        }

        var memberClass = receiverNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canAllyNations = permissions.get("CanAllyNations");
        if (!canAllyNations) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to accept ally requests.");
            return;
        }

        var senderNation = Nations.getInstance().get(strSenderNation);

        if (senderNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation " + ChatColor.BLUE + strSenderNation + ChatColor.DARK_RED + " does not exist.");
            return;
        }

        if (Objects.equals(senderNation, receiverNation)) {
            sender.sendMessage(ChatColor.RED + "Your nation cannot become allies with itself.");
            return;
        }

        if (senderNation.getAllyNationIDs().contains(receiverNation.getNationID())) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation is already allies with " + ChatColor.GREEN + receiverNation.getName() + ChatColor.DARK_RED + ".");
            return;
        }


        var requestKey = senderNation.getNationID() + "_AllyRequest_" + receiverNation.getNationID();
        if (!requests.containsKey(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Request cannot be found, it may have expired.");
            return;
        }

        requests.remove(requestKey);
        if (requests.size() == 0) {
            cancelTask();
        }

        senderNation.addAllyNation(receiverNation);
        receiverNation.addAllyNation(senderNation);

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(testPlayerNation, receiverNation)) {
                testPlayer.sendMessage(ChatColor.AQUA + "Your nation is now allied to " + ChatColor.GREEN + senderNation.getName() + ".");
            } else if (Objects.equals(testPlayerNation, senderNation)) {
                testPlayer.sendMessage(ChatColor.AQUA + "Your nation is now allied to " + ChatColor.GREEN + receiverNation.getName() + ".");
            }
        }
    }

    private void nationDeclineAlly(CommandSender sender, String strSenderNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var receiverNation = Nations.getInstance().get(diplomacyPlayer);

        if (receiverNation == null) {
            sender.sendMessage(ChatColor.RED + "You must be in a nation to do this.");
            return;
        }

        var memberClass = receiverNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canNeutralNations = permissions.get("CanNeutralNations");
        if (!canNeutralNations) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to decline alliance requests.");
            return;
        }

        var senderNation = Nations.getInstance().get(strSenderNation);

        if (senderNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation " + ChatColor.BLUE + strSenderNation + ChatColor.BLUE + " does not exist.");
            return;
        }

        if (senderNation.getAllyNationIDs().contains(receiverNation.getNationID())) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation is already allied to " + ChatColor.GREEN + receiverNation.getName() + ChatColor.DARK_RED + ".");
            return;
        }


        var requestKey = senderNation.getNationID() + "_AllyRequest_" + receiverNation.getNationID();
        if (!requests.containsKey(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Request cannot be found, it may have expired.");
            return;
        }

        requests.remove(requestKey);
        if (requests.size() == 0) {
            cancelTask();
        }

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
            var color = ChatColor.BLUE;
            if (receiverNation.getEnemyNationIDs().contains(senderNation.getNationID())) {
                color = ChatColor.RED;
            }
            if (Objects.equals(testPlayerNation, receiverNation)) {
                testPlayer.sendMessage(ChatColor.AQUA + "The alliance request from " + color + senderNation.getName() + ChatColor.AQUA + " has been declined.");
            } else if (Objects.equals(testPlayerNation, senderNation)) {
                testPlayer.sendMessage(color + receiverNation.getName() + ChatColor.AQUA + " has declined your nation's alliance request.");
            }
        }
    }

    private void nationNeutral(CommandSender sender, String strOtherNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var senderNation = Nations.getInstance().get(diplomacyPlayer);

        if (senderNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to become neutral with another nation.");
            return;
        }

        var memberClass = senderNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canNeutralNations = permissions.get("CanNeutralNations");
        if (!canNeutralNations) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to become neutral with other nations.");
            return;
        }

        var otherNation = Nations.getInstance().get(strOtherNation);

        if (otherNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation " + ChatColor.BLUE + strOtherNation + ChatColor.DARK_RED + " does not exist.");
            return;
        }

        if (Objects.equals(senderNation, otherNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation cannot become neutral with itself.");
            return;
        }

        if (senderNation.getEnemyNationIDs().contains(otherNation.getNationID())) {
            var requestKey = senderNation.getNationID() + "_NeutralRequest_" + otherNation.getNationID();
            if (requests.containsKey(requestKey)) {
                sender.sendMessage(ChatColor.DARK_RED + "Request already sent.");
                return;
            }

            requests.put(requestKey, Instant.now().getEpochSecond());

            if (requestTaskID == -1) {
                requestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onRequestTask, 0L, 20L);
            }

            for (var testPlayer : Bukkit.getOnlinePlayers()) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
                var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
                var testMemberClass = testPlayerNation.getMemberClass(testDiplomacyPlayer);
                var testPermissions = testMemberClass.getPermissions();
                boolean testCanNeutralNations = testPermissions.get("CanNeutralNations");
                if (Objects.equals(testPlayerNation, otherNation) && testCanNeutralNations) {
                    var acceptHoverText = new ComponentBuilder()
                            .append("Click to accept")
                            .color(net.md_5.bungee.api.ChatColor.GREEN)
                            .create();

                    var declineHoverText = new ComponentBuilder()
                            .append("Click to decline")
                            .color(net.md_5.bungee.api.ChatColor.RED)
                            .create();

                    var senderNationComponent = new ComponentBuilder()
                            .append(senderNation.getName())
                            .color(net.md_5.bungee.api.ChatColor.RED)
                            .create();

                    var message = new ComponentBuilder()
                            .append(senderNationComponent)
                            .append(" has requested neutrality. ")
                            .color(net.md_5.bungee.api.ChatColor.AQUA)
                            .append("[Accept]")
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation accept neutral " + senderNation.getName()))
                            .color(net.md_5.bungee.api.ChatColor.GREEN)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(acceptHoverText)))
                            .bold(true)
                            .append(" ")
                            .append("[Decline]")
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation decline neutral " + senderNation.getName()))
                            .color(net.md_5.bungee.api.ChatColor.RED)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(declineHoverText)))
                            .bold(true)
                            .create();
                    testPlayer.spigot().sendMessage(message);
                } else if (Objects.equals(testPlayerNation, senderNation) && testCanNeutralNations) {
                    testPlayer.sendMessage(ChatColor.AQUA + "A neutrality request has been sent to " + ChatColor.RED + senderNation.getName() + ChatColor.AQUA + " (Expires in 60 seconds)");
                }
            }
        } else if (senderNation.getAllyNationIDs().contains(otherNation.getNationID())) {

            senderNation.removeAllyNation(otherNation);
            otherNation.removeAllyNation(senderNation);

            for (var testPlayer : Bukkit.getOnlinePlayers()) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
                var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (Objects.equals(testPlayerNation, otherNation)) {
                    testPlayer.sendMessage(ChatColor.AQUA + "Your nation is now neutral to " + ChatColor.BLUE + senderNation.getName() + ChatColor.AQUA + ".");
                } else if (Objects.equals(testPlayerNation, senderNation)) {
                    testPlayer.sendMessage(ChatColor.AQUA + "Your nation is now neutral to " + ChatColor.BLUE + otherNation.getName() + ChatColor.AQUA + ".");
                }
            }
        } else if (!senderNation.getAllyNationIDs().contains(otherNation.getNationID()) && !senderNation.getEnemyNationIDs().contains(otherNation.getNationID())) {
            sender.sendMessage("" + ChatColor.DARK_RED + "Your nation is already neutral with " + ChatColor.BLUE + otherNation.getName() + ChatColor.DARK_RED + ".");
        }
    }

    private void nationAcceptNeutral(CommandSender sender, String strSenderNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var receiverNation = Nations.getInstance().get(diplomacyPlayer);

        if (receiverNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to become neutral with another nation.");
            return;
        }

        var memberClass = receiverNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canNeutralNations = permissions.get("CanNeutralNations");
        if (!canNeutralNations) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to accept neutrality requests.");
            return;
        }

        var senderNation = Nations.getInstance().get(strSenderNation);

        if (senderNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation " + ChatColor.BLUE + strSenderNation + ChatColor.DARK_RED + " does not exist.");
            return;
        }

        if (Objects.equals(senderNation, receiverNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation cannot become neutral with itself.");
            return;
        }

        if (senderNation.getAllyNationIDs().contains(receiverNation.getNationID())) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation is already neutral with " + ChatColor.BLUE + receiverNation.getName() + ChatColor.DARK_RED + ".");
            return;
        }


        var requestKey = senderNation.getNationID() + "_NeutralRequest_" + receiverNation.getNationID();
        if (!requests.containsKey(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Request cannot be found, it may have expired.");
            return;
        }

        requests.remove(requestKey);
        if (requests.size() == 0) {
            cancelTask();
        }

        senderNation.removeEnemyNation(receiverNation);
        receiverNation.removeEnemyNation(senderNation);

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(testPlayerNation, receiverNation)) {
                testPlayer.sendMessage(ChatColor.AQUA + "Your nation is now neutral to " + ChatColor.BLUE + senderNation.getName() + ChatColor.AQUA + ".");
            } else if (Objects.equals(testPlayerNation, senderNation)) {
                testPlayer.sendMessage(ChatColor.AQUA + "Your nation is now neutral to " + ChatColor.BLUE + receiverNation.getName() + ChatColor.AQUA + ".");
            }
        }
    }

    private void nationDeclineNeutral(CommandSender sender, String strSenderNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var receiverNation = Nations.getInstance().get(diplomacyPlayer);

        if (receiverNation == null) {
            sender.sendMessage(ChatColor.RED + "You must be in a nation to do this.");
            return;
        }

        var memberClass = receiverNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canNeutralNations = permissions.get("CanNeutralNations");
        if (!canNeutralNations) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to decline neutrality requests.");
            return;
        }

        var senderNation = Nations.getInstance().get(strSenderNation);

        if (senderNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation " + ChatColor.BLUE + strSenderNation + ChatColor.BLUE + " does not exist.");
            return;
        }

        if (senderNation.getAllyNationIDs().contains(receiverNation.getNationID())) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation is already neutral with " + ChatColor.BLUE + receiverNation.getName() + ChatColor.DARK_RED + ".");
            return;
        }


        var requestKey = senderNation.getNationID() + "_NeutralRequest_" + receiverNation.getNationID();
        if (!requests.containsKey(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Request cannot be found, it may have expired.");
            return;
        }

        requests.remove(requestKey);
        if (requests.size() == 0) {
            cancelTask();
        }

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(testPlayerNation, receiverNation)) {
                testPlayer.sendMessage(ChatColor.AQUA + "The neutrality request from " + ChatColor.RED + senderNation.getName() + ChatColor.AQUA + " has been declined.");
            } else if (Objects.equals(testPlayerNation, senderNation)) {
                testPlayer.sendMessage(ChatColor.RED + receiverNation.getName() + ChatColor.AQUA + " has declined your nation's neutrality request.");
            }
        }
    }

    private void nationEnemy(CommandSender sender, String strEnemyNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var senderNation = Nations.getInstance().get(diplomacyPlayer);

        if (senderNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to become enemies with another nation.");
            return;
        }

        var memberClass = senderNation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canEnemyNations = permissions.get("CanEnemyNations");
        if (!canEnemyNations) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to become enemies with other nations.");
            return;
        }

        var enemyNation = Nations.getInstance().get(strEnemyNation);

        if (enemyNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation " + ChatColor.BLUE + strEnemyNation + ChatColor.DARK_RED + " does not exist.");
            return;
        }

        if (Objects.equals(senderNation, enemyNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation cannot become enemies with itself.");
            return;
        }

        if (senderNation.getEnemyNationIDs().contains(enemyNation.getNationID())) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation is already enemies with " + ChatColor.RED + enemyNation.getName() + ChatColor.DARK_RED + ".");
            return;
        }

        if (senderNation.getAllyNationIDs().contains(enemyNation.getNationID())) {
            senderNation.removeAllyNation(enemyNation);
            enemyNation.removeAllyNation(senderNation);
        }
        senderNation.addEnemyNation(enemyNation);
        enemyNation.addEnemyNation(senderNation);

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(testPlayerNation, enemyNation)) {
                testPlayer.sendMessage(ChatColor.AQUA + "Your nation is now enemies with " + ChatColor.RED + senderNation.getName() + ChatColor.AQUA + ".");
            } else if (Objects.equals(testPlayerNation, senderNation)) {
                testPlayer.sendMessage(ChatColor.AQUA + "Your nation is now enemies with " + ChatColor.RED + enemyNation.getName() + ChatColor.AQUA + ".");
            }
        }
    }

    private void nationList(CommandSender sender) {

    }

    private void nationInvite(CommandSender sender, String strInvited) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to invite others.");
            return;
        }

        if (nation.getIsOpen()) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation is open, player's don't need an invite to join.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canInvitePlayers = permissions.get("CanInvitePlayers");
        if (!canInvitePlayers) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to invite players to your nation.");
            return;
        }

        var invitedPlayer = Bukkit.getPlayer(strInvited);

        if (invitedPlayer == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }
        var invitedDiplomacyPlayer = DiplomacyPlayers.getInstance().get(invitedPlayer.getUniqueId());
        var invitedPlayerNation = Nations.getInstance().get(invitedDiplomacyPlayer);
        var alreadyInNation = Nations.getInstance().get(invitedDiplomacyPlayer) != null;

        if (Objects.equals(nation, invitedPlayerNation)) {
            sender.sendMessage(ChatColor.RED + invitedPlayer.getName() + " is already in your nation.");
            return;
        }

        if (alreadyInNation) {
            sender.sendMessage(ChatColor.DARK_RED + invitedPlayer.getName() + " is already in another nation.");
            return;
        }

        var requestKey = nation.getNationID() + "_JoinRequest_" + invitedPlayer.getUniqueId();

        if (requests.containsKey(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Invite already sent.");
            return;
        }

        requests.put(requestKey, Instant.now().getEpochSecond());

        if (requestTaskID == -1) {
            requestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onRequestTask, 0L, 20L);
        }

        var acceptHoverText = new ComponentBuilder()
                .append("Click to join " + nation.getName())
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .create();

        var declineHoverText = new ComponentBuilder()
                .append("Click to decline invite.")
                .color(net.md_5.bungee.api.ChatColor.RED)
                .create();

        var nationComponent = new ComponentBuilder()
                .append(nation.getName())
                .color(net.md_5.bungee.api.ChatColor.BLUE)
                .create();

        var message = new ComponentBuilder()
                .append(nationComponent)
                .append(" has invited you to their nation. ")
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .append("[Accept]")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation join " + nation.getName()))
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(acceptHoverText)))
                .bold(true)
                .append(" ")
                .append("[Decline]")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation DeclineJoin " + nation.getName()))
                .color(net.md_5.bungee.api.ChatColor.RED)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(declineHoverText)))
                .bold(true)
                .create();
        invitedPlayer.spigot().sendMessage(message);

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(testNation, nation)) {
                player.sendMessage(ChatColor.AQUA + "An invite has been sent to " + ChatColor.BLUE + invitedPlayer.getName() + ChatColor.AQUA + " (Expires in 60 seconds)");
            }
        }
    }

    private void nationJoin(CommandSender sender, String strNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var invitedNation = Nations.getInstance().get(diplomacyPlayer);

        if (invitedNation != null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are already in a nation.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown Nation.");
            return;
        }

        if (nation.getIsOpen()) {
            for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
                var testNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (Objects.equals(testNation, nation)) {
                    onlinePlayer.sendMessage(ChatColor.AQUA + player.getName() + " has joined the nation.");
                }
            }

            nation.addMember(diplomacyPlayer);
            sender.sendMessage(ChatColor.AQUA + "You have joined " + ChatColor.GREEN + nation.getName() + ChatColor.AQUA + ".");
            return;
        }

        var requestKey = nation.getNationID() + "_JoinRequest_" + uuid.toString();
        if (!requests.containsKey(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Invite cannot be found, it may have expired.");
            return;
        }

        requests.remove(requestKey);
        if (requests.size() == 0) {
            cancelTask();
        }

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(testNation, nation)) {
                onlinePlayer.sendMessage(ChatColor.AQUA + player.getName() + " has joined the nation.");
            }
        }

        nation.addMember(diplomacyPlayer);
        sender.sendMessage(ChatColor.AQUA + "You have joined " + ChatColor.GREEN + nation.getName() + ChatColor.AQUA + ".");


    }

    private void nationDeclineJoin(CommandSender sender, String strNation) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var invitedNation = Nations.getInstance().get(diplomacyPlayer);

        if (invitedNation != null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are already in a nation.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown Nation.");
            return;
        }

        var requestKey = nation.getNationID() + "_JoinRequest_" + uuid.toString();
        if (!requests.containsKey(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Invite cannot be found, it may have expired.");
            return;
        }

        requests.remove(requestKey);
        if (requests.size() == 0) {
            cancelTask();
        }

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(testNation, nation)) {
                onlinePlayer.sendMessage(ChatColor.AQUA + player.getName() + " has declined their invite.");
            }
        }

        sender.sendMessage(ChatColor.AQUA + "You have declined the invite from " + ChatColor.BLUE + nation.getName() + ChatColor.AQUA + ".");
    }

    private void nationLeave(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You aren't in a nation.");
            return;
        }

        var members = nation.getMembers();

        if (members.size() == 1) {

            var requestKey = nation.getNationID() + "_LeaveConfirmation_" + ((Player) sender).getUniqueId().toString();

            requests.remove(requestKey);
            requests.put(requestKey, Instant.now().getEpochSecond());

            if (requestTaskID == -1) {
                requestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onRequestTask, 0L, 20L);
            }


            var confirmHoverText = new ComponentBuilder()
                    .append("Click to confirm")
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .create();


            var message = new ComponentBuilder()
                    .append("This will disband your nation, are you sure? ")
                    .color(net.md_5.bungee.api.ChatColor.AQUA)
                    .append("[Confirm]")
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation leave confirm"))
                    .color(net.md_5.bungee.api.ChatColor.GREEN)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(confirmHoverText)))
                    .bold(true)
                    .create();
            sender.spigot().sendMessage(message);
            return;
        }

        var leaderCount = 0;
        for (var member : members) {
            var diplomacyMember = DiplomacyPlayers.getInstance().get(UUID.fromString(member));
            if (nation.getMemberClass(diplomacyMember).getClassID().equals("8")) {
                leaderCount++;
            }
        }

        if (leaderCount == 1 && nation.getMemberClass(diplomacyPlayer).getClassID().equals("8")) {
            String className = Objects.requireNonNull(nation.getClassFromID("8")).getName();
            sender.sendMessage(ChatColor.DARK_RED + "You must promote another player to " + className + " before you can leave.");
            return;
        }
        sender.sendMessage(ChatColor.AQUA + "You have left " + ChatColor.BLUE + nation.getName() + ".");
        nation.removeMember(diplomacyPlayer);

    }

    private void nationLeaveConfirm(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You aren't in a nation.");
            return;
        }

        var members = nation.getMembers();
        var requestKey = nation.getNationID() + "_LeaveConfirmation_" + ((Player) sender).getUniqueId().toString();
        if (!requests.containsKey(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Command cannot be found, it may have expired.");
            return;
        }
        requests.remove(requestKey);
        if (requests.size() == 0) {
            cancelTask();
        }

        if (members.size() > 1) {

            var leaderCount = 0;
            for (var member : members) {
                var diplomacyMember = DiplomacyPlayers.getInstance().get(UUID.fromString(member));
                if (nation.getMemberClass(diplomacyMember).getClassID().equals("8")) {
                    leaderCount++;
                }
            }

            if (leaderCount == 1) {
                String className = Objects.requireNonNull(nation.getClassFromID("8")).getName();
                sender.sendMessage(ChatColor.DARK_RED + "You are no longer the only player in your nation.");
                sender.sendMessage(ChatColor.DARK_RED + "You must promote another player to " + className + " before you can leave.");
                return;
            }

            sender.sendMessage(ChatColor.AQUA + "You have left " + ChatColor.BLUE + nation.getName() + ".");
            sender.sendMessage(ChatColor.AQUA + "Since another player has joined your nation, it will not disband.");
            nation.removeMember(diplomacyPlayer);
        }

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(testPlayer.getLocation().getChunk());
            var testNation = testDiplomacyChunk.getNation();
            if (Objects.equals(testNation, nation)) {
                testPlayer.sendTitle(ChatColor.GRAY + "Wilderness", null, 5, 40, 10);
            }
        }

        var groups = nation.getGroups();
        for (var group : groups) {
            var leaders = DiplomacyPlayers.getInstance().getLeaders(group);
            var groupMembers = DiplomacyPlayers.getInstance().getMembers(group);
            for (var leader : leaders) {
                leader.removeGroupLed(group);
            }

            for (var member : groupMembers) {
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
            sender.sendMessage(ChatColor.GREEN + "\u00A4" + formatter.format(balance) + " has been transferred from " + nation.getName() + " to your bank account.");
        }
        nation.setBalance(0.0);

        sender.sendMessage(ChatColor.AQUA + "You have left " + ChatColor.BLUE + nation.getName() + ".");

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            var color = ChatColor.BLUE;
            if (Nations.getInstance().get(testDiplomacyPlayer) != null) {
                if (Objects.requireNonNull(testNation).getEnemyNationIDs().contains(nation.getNationID())) {
                    color = ChatColor.RED;
                } else if (testNation.getAllyNationIDs().contains(nation.getNationID())) {
                    color = ChatColor.GREEN;
                }
                onlinePlayer.sendMessage(ChatColor.AQUA + "The nation " + color + nation.getName() + ChatColor.AQUA + " has disbanded.");
            } else {
                onlinePlayer.sendMessage(ChatColor.AQUA + "The nation " + ChatColor.BLUE + nation.getName() + ChatColor.AQUA + "has disbanded.");
            }
        }


        for (var testNationID : nation.getAllyNationIDs()) {
            var testNation = Nations.getInstance().getFromID(testNationID);
            Objects.requireNonNull(testNation).removeAllyNation(nation);
            nation.removeAllyNation(testNation);
        }

        for (var testNationID : nation.getEnemyNationIDs()) {
            var testNation = Nations.getInstance().getFromID(testNationID);
            Objects.requireNonNull(testNation).removeEnemyNation(nation);
            nation.removeEnemyNation(testNation);
        }

        Nations.getInstance().removeNation(nation);

    }

    private void nationKick(CommandSender sender, String strPlayer) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var senderDiplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var senderNation = Nations.getInstance().get(senderDiplomacyPlayer);

        var senderClass = senderNation.getMemberClass(senderDiplomacyPlayer);
        var senderPermissions = senderClass.getPermissions();
        var canKickPlayers = senderPermissions.get("CanKickPlayers");
        if (!canKickPlayers) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to kick players.");
            return;
        }

        var otherPlayer = Bukkit.getPlayer(strPlayer);

        if (otherPlayer == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;//TODO make it so you can kick offline players
        }

        var otherDiplomacyPlayer = DiplomacyPlayers.getInstance().get(otherPlayer.getUniqueId());
        var otherNation = Nations.getInstance().get(otherDiplomacyPlayer);

        if (!Objects.equals(otherNation, senderNation)) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " is not a member of your nation.");
            return;
        }

        var otherClass = otherNation.getMemberClass(otherDiplomacyPlayer);
        var otherPermissions = otherClass.getPermissions();
        var canBeKicked = otherPermissions.get("CanBeKicked");
        if (!canBeKicked) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getName() + " cannot be kicked.");
            return;
        }

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(testNation, senderNation)) {
                onlinePlayer.sendMessage(ChatColor.AQUA + otherPlayer.getName() + " been kicked from the nation.");
            }
        }

        otherPlayer.sendMessage(ChatColor.AQUA + "You have been kicked from " + ChatColor.BLUE + senderNation.getName() + ChatColor.AQUA + ".");
        senderNation.removeMember(otherDiplomacyPlayer);


    }


    private void nationOpen(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canToggleBorder = permissions.get("CanToggleBorder");
        if (!canToggleBorder) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to open the borders.");
            return;
        }

        if (nation.getIsOpen()) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation already has open borders.");
            return;
        }


        nation.setIsOpen(true);
    }

    private void nationClose(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canToggleBorder = permissions.get("CanToggleBorder");
        if (!canToggleBorder) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to close the borders.");
            return;
        }

        if (!nation.getIsOpen()) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation already has closed borders.");
            return;
        }

        nation.setIsOpen(false);
    }

    private void nationOutlawAdd(CommandSender sender, String strOutlaw) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canManageOutlaws = permissions.get("CanManageOutlaws");
        if (!canManageOutlaws) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to outlaw players.");
            return;
        }

        var outlaw = Bukkit.getPlayer(strOutlaw);

        if (outlaw == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown Player.");
        }

        if (nation.getOutlaws().contains(outlaw.getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_RED + outlaw.getName() + " is already outlawed.");
            return;
        }

        var outlawDiplomacyPlayer = DiplomacyPlayers.getInstance().get(Objects.requireNonNull(outlaw).getUniqueId());
        var outlawNation = Nations.getInstance().get(outlawDiplomacyPlayer);

        var color = ChatColor.BLUE;
        if (outlawNation != null) {
            if (Objects.equals(outlawNation, nation) || nation.getAllyNationIDs().contains(outlawNation.getNationID())) {
                color = ChatColor.GREEN;
            } else if (nation.getEnemyNationIDs().contains(outlawNation.getNationID())) {
                color = ChatColor.RED;
            }
        }

        nation.addOutlaw(outlaw);

        outlaw.sendMessage(ChatColor.AQUA + "You have been outlawed by " + color + nation.getName());

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(nation, testNation) && !Objects.equals(onlinePlayer, outlaw)) {
                sender.sendMessage(ChatColor.DARK_RED + outlaw.getName() + ChatColor.AQUA + " has been outlawed.");
            }
        }

    }

    private void nationOutlawRemove(CommandSender sender, String strOutlaw) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canManageOutlaws = permissions.get("CanManageOutlaws");
        if (!canManageOutlaws) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to un-outlaw a player.");
            return;
        }

        var outlaw = Bukkit.getPlayer(strOutlaw);

        if (outlaw == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown Player.");
        }

        if (!nation.getOutlaws().contains(Objects.requireNonNull(outlaw).getUniqueId())) {
            sender.sendMessage(ChatColor.DARK_RED + outlaw.getName() + " is not outlawed.");
            return;
        }

        var outlawDiplomacyPlayer = DiplomacyPlayers.getInstance().get(Objects.requireNonNull(outlaw).getUniqueId());
        var outlawNation = Nations.getInstance().get(outlawDiplomacyPlayer);

        var color = ChatColor.BLUE;
        if (outlawNation != null) {
            if (Objects.equals(outlawNation, nation) || nation.getAllyNationIDs().contains(outlawNation.getNationID())) {
                color = ChatColor.GREEN;
            } else if (nation.getEnemyNationIDs().contains(outlawNation.getNationID())) {
                color = ChatColor.RED;
            }
        }

        nation.removeOutlaw(outlaw);

        outlaw.sendMessage(ChatColor.AQUA + "You are no longer outlawed by " + color + nation.getName());

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(nation, testNation) && !Objects.equals(onlinePlayer, outlaw)) {
                sender.sendMessage(ChatColor.DARK_RED + outlaw.getName() + ChatColor.AQUA + " is no longer outlawed.");
            }
        }

    }

    private void nationOutlawList(CommandSender sender, String page) {

    }

    private void nationDeposit(CommandSender sender, String strAmount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canDeposit = permissions.get("CanDeposit");
        if (!canDeposit) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to deposit currency into your nation.");
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

        var nationBalance = nation.getBalance();
        var newNationBalance = nationBalance + amount;
        var playerBalance = Diplomacy.getEconomy().getBalance(player);
        var newPlayerBalance = playerBalance - amount;


        if (newPlayerBalance < 0.0) {
            sender.sendMessage(ChatColor.DARK_RED + "You only have \u00A4" + formatter.format(playerBalance) + ".");
            return;
        }


        if (newNationBalance > 10000000000000.0) {
            sender.sendMessage("Your nation's balance cannot exceed \u00A410,000,000,000,000.00.");
            return;
        }

        nation.setBalance(newNationBalance);
        Diplomacy.getEconomy().withdrawPlayer(player, amount);

        System.out.println(nation.getBalance()); //TODO Remove

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);

            if (Objects.equals(testNation, nation)) {
                if (Objects.equals(testPlayer, player)) {
                    testPlayer.sendMessage(ChatColor.GREEN + "You have deposited \u00A4" + formatter.format(amount) + " into your nation's balance.");
                }
            } else {
                sender.sendMessage(ChatColor.GREEN + sender.getName() + " has deposited \u00A4" + formatter.format(amount) + " into your nation's balance.");
            }
        }
    }


    private void nationWithdraw(CommandSender sender, String strAmount) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canWithdraw = permissions.get("CanWithdraw");
        if (!canWithdraw) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to withdraw currency from your nation.");
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
            sender.sendMessage(ChatColor.DARK_RED + "A minimum withdrawal of \u00A40.01 is required.");
            return;
        }

        var tooManyDecimals = BigDecimal.valueOf(amount).scale() > 2;

        if (tooManyDecimals) {
            sender.sendMessage(ChatColor.DARK_RED + "Too many decimal places.");
            return;
        }

        var nationBalance = nation.getBalance();
        var newNationBalance = nationBalance - amount;
        var playerBalance = Diplomacy.getEconomy().getBalance(player);
        var newPlayerBalance = playerBalance + amount;


        if (newNationBalance < 0.0) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation only has a balance of \u00A4" + formatter.format(nationBalance) + ".");
            return;
        }


        if (newPlayerBalance > 10000000000000.0) {
            sender.sendMessage(ChatColor.DARK_RED + "Your balance cannot exceed \u00A410,000,000,000,000.00.");
            return;
        }

        nation.setBalance(newNationBalance);
        Diplomacy.getEconomy().depositPlayer(player, amount);
        System.out.println(nation.getBalance());//TODO Remove

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);

            if (Objects.equals(testNation, nation)) {
                if (Objects.equals(testPlayer, player)) {
                    if (amount >= 1) {
                        testPlayer.sendMessage(ChatColor.GREEN + "You have withdrawn \u00A4" + formatter.format(amount) + " from your nation's balance.");
                    }
                } else {
                    if (amount >= 1) {
                        sender.sendMessage(ChatColor.GREEN + sender.getName() + " has withdrawn \u00A4" + formatter.format(amount) + " from your nation's balance.");
                    }
                }
            }
        }
    }

    private void nationBanner(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        var canChangeBanner = permissions.get("CanChangeBanner");
        if (!canChangeBanner) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to change your nation's banner.");
            return;
        }

        var heldItem = player.getInventory().getItemInMainHand();
        if (!(heldItem.getItemMeta() instanceof BannerMeta)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be holding a banner.");
            return;
        }

        nation.setBanner(heldItem);

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            var testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (testPlayerNation != null) {
                if (Objects.equals(testPlayerNation, nation)) {
                    testPlayer.sendMessage(ChatColor.AQUA + "Your nation's banner has been updated.");
                } else if (nation.getEnemyNationIDs().contains(testPlayerNation.getNationID())) {
                    testPlayer.sendMessage(ChatColor.RED + nation.getName() + ChatColor.AQUA + " has updated their banner.");
                } else if (nation.getAllyNationIDs().contains(testPlayerNation.getNationID())) {
                    testPlayer.sendMessage(ChatColor.GREEN + nation.getName() + ChatColor.AQUA + " has updated their banner.");
                } else {
                    testPlayer.sendMessage(ChatColor.BLUE + nation.getName() + ChatColor.AQUA + " has updated their banner.");
                }
            }
        }
    }
}
