package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
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

import java.text.DecimalFormat;
import java.time.Instant;
import java.util.*;

public class NationCommand implements CommandExecutor, TabCompleter {
    private static final String nationCreateUsage = "/nation create <nation>";
    private static final String nationRenameUsage = "/nation rename <nation>";
    private static final String nationSurrenderUsage = "/nation surrender <nation>";
    private static final String nationDisbandUsage = "/nation surrender";
    private static final String nationAllyUsage = "/nation ally <nation>";
    private static final String nationAcceptUsage = "/nation accept [ally/neutral] <nation>";
    private static final String nationRejectUsage = "/nation decline [ally/neutral] <nation>";
    private static final String nationNeutralUsage = "/nation neutral <nation>";
    private static final String nationEnemyUsage = "/nation enemy <nation>";
    private static final String nationListUsage = "/nation list";
    private static final String nationInviteUsage = "/nation invite <player>";
    private static final String nationJoinUsage = "/nation join <nation>";
    private static final String nationDeclineJoinUsage = "/nation DeclineJoin <nation>";
    private static final String nationLeaveUsage = "/nation leave";
    private static final String nationLeaveConfirmUsage = "/nation leave confirm";
    private static final String nationKickUsage = "/nation kick <player>";
    private static final String nationOpenUsage = "/nation open";
    private static final String nationCloseUsage = "/nation close";
    private static final String nationBannerUsage = "/nation banner";
    private static final String nationOutlawAddUsage = "/nation outlaw add <player>";
    private static final String nationOutlawRemoveUsage = "/nation outlaw remove <player>";
    private static final String nationOutlawListUsage = "/nation outlaw list <page>";

    private static final DecimalFormat formatter = new DecimalFormat("#,###.00");
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
                    nationRejectAlly(sender, args[2]);
                } else if (args[1].equalsIgnoreCase("neutral")) {
                    nationRejectNeutral(sender, args[2]);
                }
            } else {
                sender.sendMessage(nationRejectUsage);
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
        } else if (args[0].

                equalsIgnoreCase("kick")) {
            if (args.length == 2) {
                nationKick(sender, args[1]);
            } else {
                sender.sendMessage(nationKickUsage);
            }
        } else if (args[0].

                equalsIgnoreCase("open")) {
            if (args.length == 2) {
                nationOpen(sender);
            } else {
                sender.sendMessage(nationOpenUsage);
            }
        } else if (args[0].

                equalsIgnoreCase("close")) {
            if (args.length == 1) {
                nationClose(sender);
            } else {
                sender.sendMessage(nationCloseUsage);
            }
        } else if (args[0].

                equalsIgnoreCase("banner")) {
            if (args.length == 2) {
                nationBanner(sender, args[1]);
            } else {
                sender.sendMessage(nationBannerUsage);
            }
        } else if (args[0].

                equalsIgnoreCase("outlaw")) {
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
                return null; //TODO List Nation-less players
            } else if (args[0].equalsIgnoreCase("join")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    nations.add(nation.getName());
                return nations;
            } else if (args[0].equalsIgnoreCase("declinejoin")) {
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
            return null;
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
        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        var nationClass = nation.getMemberClass(diplomacyPlayer);
        boolean canRenameNation = nationClass.getPermissions().get("CanRenameNation");
        if (!canRenameNation) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to rename the nation.");
            return;
        }

        var oldName = nation.getName();
        if (oldName.equals(name)) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation is already named " + ChatColor.GREEN + name + ChatColor.DARK_RED + ".");
            return;
        }

        var nameTaken = Nations.getInstance().get(name) != null;
        if (nameTaken) {
            sender.sendMessage(ChatColor.DARK_RED + "The name " + ChatColor.BLUE + name + ChatColor.DARK_RED + " is taken, choose another name.");
            return;
        }

        Nations.getInstance().rename(name, nation);

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            var color = ChatColor.BLUE;
            if (testNation.getEnemyNationIDs().contains(nation.getNationID())) {
                color = ChatColor.RED;
            } else if (testNation.getAllyNationIDs().contains(nation.getNationID())) {
                color = ChatColor.GREEN;
            }
            onlinePlayer.sendMessage(ChatColor.AQUA + "The nation of " + color + oldName + ChatColor.AQUA + " has been renamed to " + color + name + ChatColor.AQUA + ".");
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
            if (balance >= 1.0) {
                sender.sendMessage(ChatColor.GREEN + "\u00A4" + formatter.format(balance) + " has been transferred from " + nation.getName() + " to your bank account.");
            } else {
                sender.sendMessage(ChatColor.GREEN + "\u00A40" + formatter.format(balance) + " has been transferred from " + nation.getName() + " to your bank account.");
            }
        }
        nation.setBalance(0.0);


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
            if (balance >= 1.0) {
                sender.sendMessage(ChatColor.GREEN + "\u00A4" + formatter.format(balance) + " has been transferred from " + nation.getName() + " to your bank account.");
            } else {
                sender.sendMessage(ChatColor.GREEN + "\u00A40" + formatter.format(balance) + " has been transferred from " + nation.getName() + " to your bank account.");
            }
        }
        nation.setBalance(0.0);

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            var color = ChatColor.BLUE;
            if (testNation.getEnemyNationIDs().contains(nation.getNationID())) {
                color = ChatColor.RED;
            } else if (testNation.getAllyNationIDs().contains(nation.getNationID())) {
                color = ChatColor.GREEN;
            }
            onlinePlayer.sendMessage(ChatColor.AQUA + "The nation " + color + nation.getName() + ChatColor.AQUA + " has disbanded.");
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
                        .append("[Reject]")
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

    private void nationRejectAlly(CommandSender sender, String strSenderNation) {
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
                            .append("[Reject]")
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

    private void nationRejectNeutral(CommandSender sender, String strSenderNation) {
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
            if (balance >= 1.0) {
                sender.sendMessage(ChatColor.GREEN + "\u00A4" + formatter.format(balance) + " has been transferred from " + nation.getName() + " to your bank account.");
            } else {
                sender.sendMessage(ChatColor.GREEN + "\u00A40" + formatter.format(balance) + " has been transferred from " + nation.getName() + " to your bank account.");
            }
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

        Nations.getInstance().removeNation(nation);

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
