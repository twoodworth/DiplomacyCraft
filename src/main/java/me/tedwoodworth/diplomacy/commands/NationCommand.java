package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Guis.Guis;
import me.tedwoodworth.diplomacy.events.NationAddChunksEvent;
import me.tedwoodworth.diplomacy.events.NationJoinEvent;
import me.tedwoodworth.diplomacy.events.NationLeaveEvent;
import me.tedwoodworth.diplomacy.events.NationRemoveChunksEvent;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.ScoreboardManager;
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

import java.awt.*;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.List;
import java.util.*;

public class NationCommand implements CommandExecutor, TabCompleter {

    /*
        Constants used by this class to be sent to the user to show proper command usage.
     */
    private static final String incorrectUsage = ChatColor.RED + "Incorrect usage, try: ";
    private static final String nationUsage = "/nation";
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
    private static final String nationListUsage = "/nation list";
    private static final String nationMembersUsage = "/nation members <nation> <page>";
    private static final String nationOutlawsUsage = "/nation outlaws <nation> <page>";
    private static final String nationEnemiesUsage = "/nation enemies <nation> <page>";
    private static final String nationAlliesUsage = "/nation allies <nation> <page>";
    private static final String nationGroupsUsage = "/nation groups <nation> <page>";
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
    private static final String nationOutlawUsage = "/nation outlaw (add/remove) <player>";
    private static final String nationOutlawRemoveUsage = "/nation outlaw remove <player>";
    private static final String setClassUsage = "/nation setClass <player> <class>";
    private static final String nationColorUsage = "/nation color <0-255 (Red)> <0-255 (Green)> <0-255 (Blue)>";

    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");
    private Map<String, Long> requests = new HashMap<>();
    private int requestTaskID = -1;

    /**
     * Registers NationCommand to the plugin
     *
     * @param pluginCommand: command to register
     */
    public static void register(PluginCommand pluginCommand) {
        var nationCommand = new NationCommand();

        pluginCommand.setExecutor(nationCommand);
        pluginCommand.setTabCompleter(nationCommand);
    }

    /**
     * Removes request keys that are more than 60 seconds old from the requests set.
     */
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

    /**
     * Cancels the request task
     */
    private void cancelTask() {
        Bukkit.getScheduler().cancelTask(requestTaskID);
        requestTaskID = -1;
    }

    /**
     * Code to be executed on usage of any command.
     * <p>
     * Used for checking if a nation command is being called, and what functions to call
     * according to the command parameters.
     *
     * @param sender:  Sender of the command
     * @param command: Command being sent
     * @param label:   Command alias, if used
     * @param args:    Arguments of command
     * @return true always
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            nation(sender);
        } else if (args[0].equalsIgnoreCase("1")) {
            if (args.length == 1) {
                nation(sender);
            } else {
                sender.sendMessage(incorrectUsage + nationUsage);
            }
        } else if (args[0].equalsIgnoreCase("2")) {
            if (args.length == 1) {
                nation2(sender);
            } else {
                sender.sendMessage(incorrectUsage + nationUsage);
            }
        } else if (args[0].equalsIgnoreCase("3")) {
            if (args.length == 1) {
                nation3(sender);
            } else {
                sender.sendMessage(incorrectUsage + nationUsage);
            }
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                nationCreate(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationCreateUsage);
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 2) {
                nationInfo(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationInfoUsage);
            }
        } else if (args[0].equalsIgnoreCase("rename")) {
            if (args.length == 2) {
                nationRename(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationRenameUsage);
            }
        } else if (args[0].equalsIgnoreCase("color")) {
            if (args.length == 4) {
                nationColor(sender, args[1], args[2], args[3]);
            } else {
                sender.sendMessage(incorrectUsage + nationColorUsage);
            }
        } else if (args[0].equalsIgnoreCase("surrender")) {
            if (args.length == 2) {
                nationSurrender(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationSurrenderUsage);
            }
        } else if (args[0].equalsIgnoreCase("disband")) {
            if (args.length == 1) {
                nationDisband(sender);
            } else {
                sender.sendMessage(incorrectUsage + nationDisbandUsage);
            }
        } else if (args[0].equalsIgnoreCase("setClass")) {
            if (args.length == 3) {
                setClass(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + setClassUsage);
            }
        } else if (args[0].equalsIgnoreCase("ally")) {
            if (args.length == 2) {
                nationAlly(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationAllyUsage);
            }
        } else if (args[0].equalsIgnoreCase("accept")) {
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("ally")) {
                    nationAcceptAlly(sender, args[2]);
                } else if (args[1].equalsIgnoreCase("neutral")) {
                    nationAcceptNeutral(sender, args[2]);
                }
            } else {
                sender.sendMessage(incorrectUsage + nationAcceptUsage);
            }
        } else if (args[0].equalsIgnoreCase("decline")) {
            if (args.length == 3) {
                if (args[1].equalsIgnoreCase("ally")) {
                    nationDeclineAlly(sender, args[2]);
                } else if (args[1].equalsIgnoreCase("neutral")) {
                    nationDeclineNeutral(sender, args[2]);
                }
            } else {
                sender.sendMessage(incorrectUsage + nationDeclineUsage);
            }
        } else if (args[0].equalsIgnoreCase("neutral")) {
            if (args.length == 2) {
                nationNeutral(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationNeutralUsage);
            }
        } else if (args[0].equalsIgnoreCase("enemy")) {
            if (args.length == 2) {
                nationEnemy(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationEnemyUsage);
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 1) {
                nationList(sender);
            } else if (args.length == 2) {
                nationListPage(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationListUsage);
            }
        } else if (args[0].equalsIgnoreCase("members")) {
            if (args.length == 2) {
                nationMembers(sender, args[1]);
            } else if (args.length == 3) {
                nationMembersPage(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + nationMembersUsage);
            }
        } else if (args[0].equalsIgnoreCase("outlaws")) {
            if (args.length == 2) {
                nationOutlaws(sender, args[1]);
            } else if (args.length == 3) {
                nationOutlawsPage(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + nationOutlawsUsage);
            }
        } else if (args[0].equalsIgnoreCase("allies")) {
            if (args.length == 2) {
                nationAllyEnemy(sender, args[1], true);
            } else if (args.length == 3) {
                nationAllyEnemyPage(sender, args[1], true, args[2]);
            } else {
                sender.sendMessage(incorrectUsage + nationAlliesUsage);
            }
        } else if (args[0].equalsIgnoreCase("enemies")) {
            if (args.length == 2) {
                nationAllyEnemy(sender, args[1], false);
            } else if (args.length == 3) {
                nationAllyEnemyPage(sender, args[1], false, args[2]);
            } else {
                sender.sendMessage(incorrectUsage + nationEnemiesUsage);
            }
        } else if (args[0].equalsIgnoreCase("groups")) {
            if (args.length == 2) {
                nationGroups(sender, args[1]);
            } else if (args.length == 3) {
                nationGroupsPage(sender, args[1], args[2]);
            } else {
                sender.sendMessage(incorrectUsage + nationGroupsUsage);
            }
        } else if (args[0].equalsIgnoreCase("invite")) {
            if (args.length == 2) {
                nationInvite(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationInviteUsage);
            }
        } else if (args[0].equalsIgnoreCase("join")) {
            if (args.length == 2) {
                nationJoin(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationJoinUsage);
            }
        } else if (args[0].equalsIgnoreCase("declinejoin")) {
            if (args.length == 2) {
                nationDeclineJoin(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationDeclineJoinUsage);
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (args.length == 1) {
                nationLeave(sender);
            } else if (args[1].equalsIgnoreCase("confirm")) {
                if (args.length == 2) {
                    nationLeaveConfirm(sender);
                } else {
                    sender.sendMessage(incorrectUsage + nationLeaveConfirmUsage);
                }
            } else {
                sender.sendMessage(incorrectUsage + nationLeaveUsage);
            }
        } else if (args[0].equalsIgnoreCase("kick")) {
            if (args.length == 2) {
                nationKick(sender, args[1]);
            } else {
                sender.sendMessage(incorrectUsage + nationKickUsage);
            }
        } else if (args[0].equalsIgnoreCase("open")) {
            if (args.length == 1) {
                nationOpen(sender);
            } else {
                sender.sendMessage(incorrectUsage + nationOpenUsage);
            }
        } else if (args[0].equalsIgnoreCase("close")) {
            if (args.length == 1) {
                nationClose(sender);
            } else {
                sender.sendMessage(incorrectUsage + nationCloseUsage);
            }
        } else if (args[0].equalsIgnoreCase("banner")) {
            if (args.length == 1) {
                nationBanner(sender);
            } else {
                sender.sendMessage(incorrectUsage + nationBannerUsage);
            }
        } else if (args[0].equalsIgnoreCase("outlaw")) {
            if (args[1].equalsIgnoreCase("add")) {
                if (args.length == 3) {
                    nationOutlawAdd(sender, args[2]);
                } else {
                    sender.sendMessage(incorrectUsage + nationOutlawAddUsage);
                }
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (args.length == 3) {
                    nationOutlawRemove(sender, args[2]);
                } else {
                    sender.sendMessage(incorrectUsage + nationOutlawRemoveUsage);
                }
            } else {
                sender.sendMessage(incorrectUsage + nationOutlawUsage);
            }
        } else {
            sender.sendMessage(incorrectUsage + nationUsage);
        }
        return true;
    }

    /**
     * Provides a list of argument recommendations based on what the user
     * has typed into the command bar so far.
     *
     * @param sender:  Sender of command
     * @param command: Command being sent
     * @param alias:   Alias of command used
     * @param args:    Arguments of command
     * @return list of arguments, or null if none should be sent.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 0) {
            if (args.length == 1) {
                var list = Arrays.asList(
                        "create",
                        "color",
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
                        "outlaw",
                        "groups",
                        "allies",
                        "members",
                        "setClass",
                        "outlaws",
                        "enemies");
                var list1 = new ArrayList<String>();
                for (var val : list) {
                    if (val.toLowerCase().contains(args[0].toLowerCase()))
                        list1.add(val);
                }
                return list1;
            } else if (args[0].equalsIgnoreCase("create")) {
                return null;
            } else if (args[0].equalsIgnoreCase("color")) {
                return null;
            } else if (args[0].equalsIgnoreCase("info")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations()) {
                    if (nation.getName().toLowerCase().contains(args[1].toLowerCase()))
                        nations.add(nation.getName());
                }
                return nations;
            } else if (args[0].equalsIgnoreCase("rename")) {
                return null;
            } else if (args[0].equalsIgnoreCase("banner")) {
                return null;
            } else if (args[0].equalsIgnoreCase("surrender")) {
                List<String> nations = new ArrayList<>();
                for (var nation : Nations.getInstance().getNations())
                    if (nation.getName().toLowerCase().contains(args[1].toLowerCase()))
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
                        if (nation.getName().toLowerCase().contains(args[2].toLowerCase()))
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
                        if (nation.getName().toLowerCase().contains(args[2].toLowerCase()))
                            nations.add(nation.getName());
                    return nations;
                }
            } else if (args[0].equalsIgnoreCase("neutral")) {
                if (args.length == 2) {
                    List<String> nations = new ArrayList<>();
                    for (var nation : Nations.getInstance().getNations())
                        if (nation.getName().toLowerCase().contains(args[1].toLowerCase()))
                            nations.add(nation.getName());
                    return nations;
                }
            } else if (args[0].equalsIgnoreCase("enemy")) {
                if (args.length == 2) {
                    List<String> nations = new ArrayList<>();
                    for (var nation : Nations.getInstance().getNations())
                        if (nation.getName().toLowerCase().contains(args[1].toLowerCase()))
                            nations.add(nation.getName());
                    return nations;
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                return null;
            } else if (args[0].equalsIgnoreCase("allies")
                    || args[0].equalsIgnoreCase("enemies")
                    || args[0].equalsIgnoreCase("groups")
                    || args[0].equalsIgnoreCase("members")
                    || args[0].equalsIgnoreCase("outlaws")) {
                if (args.length == 2) {
                    List<String> nations = new ArrayList<>();
                    for (var nation : Nations.getInstance().getNations())
                        if (nation.getName().toLowerCase().contains(args[1].toLowerCase()))
                            nations.add(nation.getName());
                    return nations;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("invite")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                        var nation = Nations.getInstance().get(diplomacyPlayer);
                        if (nation == null) {
                            if (player.getName().toLowerCase().contains(args[1].toLowerCase()))
                                players.add(player.getName());
                        }
                    }
                    return players;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("setClass")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        if (player.getName().toLowerCase().contains(args[1].toLowerCase()))
                            players.add(player.getName());
                    }
                    return players;
                } else if (args.length == 3) {
                    var nation = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId()));
                    if (nation == null) return null;
                    List<String> classes = new ArrayList<>();
                    for (var nationClass : nation.getClasses()) {
                        if (nationClass.getName().toLowerCase().contains(args[2].toLowerCase()))
                            classes.add(nationClass.getName());
                    }
                    return classes;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("join")) {
                if (args.length == 2) {
                    List<String> nations = new ArrayList<>();
                    for (var nation : Nations.getInstance().getNations())
                        if (nation.getName().toLowerCase().contains(args[1].toLowerCase()))
                            nations.add(nation.getName());
                    return nations;
                }
            } else if (args[0].equalsIgnoreCase("declineJoin")) {
                if (args.length == 2) {
                    List<String> nations = new ArrayList<>();
                    for (var nation : Nations.getInstance().getNations())
                        if (nation.getName().toLowerCase().contains(args[1].toLowerCase()))
                            nations.add(nation.getName());
                    return nations;
                }
            } else if (args[0].equalsIgnoreCase("leave")) {
                return null;
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    if (nation != null) {
                        for (var player : nation.getMembers()) {
                            var permissions = nation.getMemberClass(player).getPermissions();
                            if (permissions.get("CanBeKicked")) {
                                if (player.getOfflinePlayer().getName().toLowerCase().contains(args[1].toLowerCase()))
                                    players.add(player.getOfflinePlayer().getName());
                            }
                        }
                    } else {
                        return null;
                    }
                    return players;
                }
            } else if (args[0].equalsIgnoreCase("open")) {
                return null;
            } else if (args[0].equalsIgnoreCase("close")) {
                return null;
            } else if (args[0].equalsIgnoreCase("outlaw")) {
                if (args.length == 2) {
                    return Arrays.asList("add", "remove");
                } else if (args[1].equalsIgnoreCase("add") && args.length == 3) {
                    var players = new ArrayList<String>();
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    if (nation != null) {
                        for (var player : Bukkit.getOnlinePlayers()) {
                            if (player.getName().toLowerCase().contains(args[2].toLowerCase()))
                                players.add(player.getName());
                        }
                        return players;
                    }
                    return null;
                } else if (args[1].equalsIgnoreCase("remove") && args.length == 3) {
                    var players = new ArrayList<String>();
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    if (nation != null) {
                        for (var player : Bukkit.getOnlinePlayers()) {
                            if (player.getName().toLowerCase().contains(args[2].toLowerCase()))
                                players.add(player.getName());
                        }
                        return players;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Used by a player to view page 1 of the list of nation commands.
     *
     * @param sender: Sender of command.
     */
    private void nation(CommandSender sender) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        // send list of commands
        sender.sendMessage(ChatColor.YELLOW + "----" + ChatColor.GOLD + " Nations " + ChatColor.YELLOW + "--" + ChatColor.GOLD + " Page " + ChatColor.RED + "1" + ChatColor.GOLD + "/" + ChatColor.RED + "3" + ChatColor.YELLOW + " ----");
        sender.sendMessage(ChatColor.GOLD + "/nation list" + ChatColor.WHITE + " Get a list of all nations");
        sender.sendMessage(ChatColor.GOLD + "/nation info" + ChatColor.WHITE + " Get info about a nation");
        sender.sendMessage(ChatColor.GOLD + "/nation create" + ChatColor.WHITE + " Create a nation");
        sender.sendMessage(ChatColor.GOLD + "/nation rename" + ChatColor.WHITE + " Rename a nation");
        sender.sendMessage(ChatColor.GOLD + "/nation surrender" + ChatColor.WHITE + " Surrender your nation");
        sender.sendMessage(ChatColor.GOLD + "/nation members" + ChatColor.WHITE + " View nation members");
        sender.sendMessage(ChatColor.GOLD + "/nation invite" + ChatColor.WHITE + " Invite a player to your nation");
        sender.sendMessage(ChatColor.GOLD + "/nation join" + ChatColor.WHITE + " Join a nation");
        sender.sendMessage(ChatColor.GOLD + "/nation leave" + ChatColor.WHITE + " Leave your nation");
        sender.sendMessage(ChatColor.GOLD + "/nation setClass" + ChatColor.WHITE + " Set a player's class");
        sender.sendMessage(ChatColor.GOLD + "Type " + ChatColor.RED + "/nation 2 " + ChatColor.GOLD + "to read the next page.");
    }


    /**
     * Used by a player to view page 2 of the list of nation commands.
     *
     * @param sender: Sender of command.
     */
    private void nation2(CommandSender sender) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        // send list of commands
        sender.sendMessage(ChatColor.YELLOW + "----" + ChatColor.GOLD + " Nations " + ChatColor.YELLOW + "--" + ChatColor.GOLD + " Page " + ChatColor.RED + "2" + ChatColor.GOLD + "/" + ChatColor.RED + "3" + ChatColor.YELLOW + " ----");
        sender.sendMessage(ChatColor.GOLD + "/nation kick" + ChatColor.WHITE + " Kick a player from your nation");
        sender.sendMessage(ChatColor.GOLD + "/nation disband" + ChatColor.WHITE + " Disband your nation");
        sender.sendMessage(ChatColor.GOLD + "/nation deposit" + ChatColor.WHITE + " Deposit into your nation's balance");
        sender.sendMessage(ChatColor.GOLD + "/nation withdraw" + ChatColor.WHITE + " Withdraw from your nation's balance");
        sender.sendMessage(ChatColor.GOLD + "/nation ally" + ChatColor.WHITE + " Become allies with another nation");
        sender.sendMessage(ChatColor.GOLD + "/nation neutral" + ChatColor.WHITE + " Become neutral with another nation");
        sender.sendMessage(ChatColor.GOLD + "/nation enemy" + ChatColor.WHITE + " Become enemies with another nation");
        sender.sendMessage(ChatColor.GOLD + "/nation open" + ChatColor.WHITE + " Open your nation's borders");
        sender.sendMessage(ChatColor.GOLD + "/nation close" + ChatColor.WHITE + " Close your nation's borders");
        sender.sendMessage(ChatColor.GOLD + "Type " + ChatColor.RED + "/nation 3 " + ChatColor.GOLD + "to read the next page.");
    }


    /**
     * Used by a player to view page 3 of the list of nation commands.
     *
     * @param sender: Sender of command.
     */
    private void nation3(CommandSender sender) {
        // cancel if not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        // send list of commands
        sender.sendMessage(ChatColor.YELLOW + "----" + ChatColor.GOLD + " Nations " + ChatColor.YELLOW + "--" + ChatColor.GOLD + " Page " + ChatColor.RED + "3" + ChatColor.GOLD + "/" + ChatColor.RED + "3" + ChatColor.YELLOW + " ----");
        sender.sendMessage(ChatColor.GOLD + "/nation outlaw" + ChatColor.WHITE + " Add/remove outlaws");
        sender.sendMessage(ChatColor.GOLD + "/nation outlaws" + ChatColor.WHITE + " View nation outlaws");
        sender.sendMessage(ChatColor.GOLD + "/nation color" + ChatColor.WHITE + " Set your nation's map color");
        sender.sendMessage(ChatColor.GOLD + "/nation allies" + ChatColor.WHITE + " List a nation's allies");
        sender.sendMessage(ChatColor.GOLD + "/nation enemies" + ChatColor.WHITE + " List a nation's enemies");
        sender.sendMessage(ChatColor.GOLD + "/nation banner" + ChatColor.WHITE + " Set your nation's banner");
    }

    /**
     * Used by a player to view the info GUI of a specific nation
     *
     * @param sender:    Sender of command
     * @param strNation: Nation name
     */
    private void nationInfo(CommandSender sender, String strNation) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var nation = Nations.getInstance().get(strNation);

        // cancel if nation does not exist
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        // show nation GUI to player
        var gui = Guis.getInstance().getNationMenu(nation);
        gui.show(player);
    }

    /**
     * Used by a player to set the map color of their nation
     *
     * @param sender:   Sender of command
     * @param strRed:   Red RGB value
     * @param strGreen: Green RGB value
     * @param strBlue:  Blue RGB value
     */
    private void nationColor(CommandSender sender, String strRed, String strGreen, String strBlue) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        // cancel if player is not in a nation
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        var nationClass = nation.getMemberClass(diplomacyPlayer);
        boolean canSetNationColor = nationClass.getPermissions().get("CanSetNationColor");

        // cancel if insufficient permission
        if (!canSetNationColor) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to set the nation's color.");
            return;
        }

        var red = 0;
        var green = 0;
        var blue = 0;

        // parse int RGB values. Cancel if improperly formatted
        try {
            red = Integer.parseInt(strRed);
            green = Integer.parseInt(strGreen);
            blue = Integer.parseInt(strBlue);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Values must be numbers.");
            return;
        }

        // cancel if values are out of range
        if (red < 0 || green < 0 || blue < 0 || red > 255 || green > 255 || blue > 255) {
            sender.sendMessage(ChatColor.DARK_RED + "Numbers cannot be below 0 or above 255.");
            return;
        }

        // cancel if color is already set to the provided color
        if (nation.getColor().getRed() == red && nation.getColor().getGreen() == green && nation.getColor().getBlue() == blue) {
            sender.sendMessage(ChatColor.DARK_RED + "Your nation's color is already set to that.");
            return;
        }

        // Update nation color
        nation.setColor(red, green, blue);
        var color = new Color(red, green, blue);
        var message = new ComponentBuilder()
                .append("Your nation's map color has been set to this color: ")
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .append("\u2588\u2588\u2588")
                .color(net.md_5.bungee.api.ChatColor.BOLD)
                .color(net.md_5.bungee.api.ChatColor.of(color))
                .create();

        // update scoreboard to reflect new color
        ScoreboardManager.getInstance().updateScoreboards();

        // send notifications
        for (var member : nation.getMembers()) {
            if (member.getOfflinePlayer().isOnline()) {
                member.getOfflinePlayer().getPlayer().spigot().sendMessage(message);
            }
        }

    }

    /**
     * Used by a player to create a new nation
     *
     * @param sender: Sender of command
     * @param name:   Name of new nation
     */
    private void nationCreate(CommandSender sender, String name) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var leader = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(leader);
        var sameName = Nations.getInstance().get(name) != null;

        // cancel if sender is already part of a nation
        if (nation != null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must leave your current nation before you can establish a new nation.");
            return;
        }

        // cancel if nation name is already taken
        if (sameName) {
            sender.sendMessage(ChatColor.DARK_RED + "The name " + name + " is taken, choose another name.");
            return;
        }

        // cancel if name is too long
        if (name.length() > 16) {
            sender.sendMessage(ChatColor.DARK_RED + "The name " + name + " is too long, choose another name.");
        }

        // create nation
        Nations.getInstance().createNation(name, leader);
        sender.sendMessage(ChatColor.AQUA + "The nation of " + ChatColor.GREEN + name + ChatColor.AQUA + " has been founded.");

        // send notifications
        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!Objects.equals(uuid, onlinePlayer.getUniqueId())) {
                onlinePlayer.sendMessage(ChatColor.AQUA + "The nation of " + ChatColor.BLUE + name + ChatColor.AQUA + " has been founded.");
            }
        }
    }

    /**
     * Used by a player to rename a nation
     *
     * @param sender: Sender of command
     * @param name:   New name of nation
     */
    private void nationRename(CommandSender sender, String name) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(((Player) sender).getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        // cancel if sender is not in a nation
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You are not in a nation.");
            return;
        }

        // cancel if insufficient permission
        var nationClass = nation.getMemberClass(diplomacyPlayer);
        boolean canRenameNation = nationClass.getPermissions().get("CanRenameNation");
        if (!canRenameNation) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to rename the nation.");
            return;
        }

        // cancel if the old name is the same as the new name
        var oldName = nation.getName();
        if (oldName.equals(name)) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation is already named " + name + ".");
            return;
        }

        // cancel if the new name is already taken
        var nameTaken = Nations.getInstance().get(name) != null;
        if (nameTaken) {
            sender.sendMessage(ChatColor.DARK_RED + "The name " + name + " is taken, choose another name.");
            return;
        }

        // cancel if the new name is too long
        if (name.length() > 16) {
            sender.sendMessage(ChatColor.DARK_RED + "The name " + name + " is too long, choose another name.");
            return;
        }

        // rename nation
        Nations.getInstance().rename(nation, name);

        // send notifications
        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            var color = ChatColor.BLUE;
            if (testNation != null) {
                if (testNation.getEnemyNationIDs().contains(nation.getNationID())) {
                    color = ChatColor.RED;
                } else if (testNation.getAllyNationIDs().contains(nation.getNationID()) || Objects.equals(nation, testNation)) {
                    color = ChatColor.GREEN;
                }
            }
            onlinePlayer.sendMessage(ChatColor.AQUA + "The nation of " + color + oldName + ChatColor.AQUA + " has been renamed to " + color + name + ChatColor.AQUA + ".");
        }
    }

    /**
     * Used by a player to surrender their nation to another nation
     *
     * @param sender:          Sender of command
     * @param otherNationName: Name of nation to surrender to
     */
    private void nationSurrender(CommandSender sender, String otherNationName) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        Nation nation = Nations.getInstance().get(diplomacyPlayer);

        // cancel if sender is not in a nation
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to surrender a nation.");
            return;
        }


        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canSurrender = permissions.get("CanSurrenderNation");

        // cancel if insufficient permission
        if (!canSurrender) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to surrender your nation.");
            return;
        }

        // cancel if otherNation does not exist
        Nation otherNation = Nations.getInstance().get(otherNationName);
        if (otherNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The nation of " + ChatColor.BLUE + otherNationName + ChatColor.DARK_RED + " does not exist.");
            return;
        }

        // cancel if nation and otherNation are the same
        if (Objects.equals(nation, otherNation)) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot surrender your nation to itself.");
            return;
        }

        // send notifications
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

        // transfer members if otherNation has open borders
        var members = nation.getMembers();
        for (var member : members) {
            if (otherNation.getIsOpen()) {
                otherNation.addMember(member);
            }
            nation.removeMember(member);
        }

        // call events
        if (otherNation.getIsOpen())
            Bukkit.getPluginManager().callEvent(new NationJoinEvent(members, otherNation));
        Bukkit.getPluginManager().callEvent(new NationLeaveEvent(members, nation));


        // send pop-ups to players located in effected chunks
        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(testPlayer.getLocation().getChunk());
            var testNation = testDiplomacyChunk.getNation();
            if (Objects.equals(testNation, nation)) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
                Nation testPlayerNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testPlayerNation == null) {
                    testPlayer.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getEnemyNationIDs().contains(testPlayerNation.getNationID())) {
                    testPlayer.sendTitle(ChatColor.RED + otherNation.getName(), null, 5, 40, 10);
                } else if (otherNation.getAllyNationIDs().contains(testPlayerNation.getNationID()) || otherNation.equals(testPlayerNation)) {
                    testPlayer.sendTitle(ChatColor.GREEN + otherNation.getName(), null, 5, 40, 10);
                } else {
                    testPlayer.sendTitle(ChatColor.BLUE + otherNation.getName(), null, 5, 40, 10);
                }
            }
        }

        // transfer groups
        var groups = nation.getGroups();
        for (var group : groups) {
            group.setNation(otherNation);
        }

        // cancel contests in effected chunks
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

        //tranfer chunks
        var diplomacyChunks = nation.getChunks();
        for (var diplomacyChunk : diplomacyChunks) {
            nation.removeChunk(diplomacyChunk);
            otherNation.addChunk(diplomacyChunk);
        }

        // send chunk change events
        Bukkit.getPluginManager().callEvent(new NationAddChunksEvent(otherNation, diplomacyChunks));
        Bukkit.getPluginManager().callEvent(new NationRemoveChunksEvent(nation, diplomacyChunks));

        // remove data related to the surrendurring nation
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

        // remove nation
        Nations.getInstance().removeNation(nation);

    }

    /**
     * Used by a player to disband their nation
     *
     * @param sender: Sender of command
     */
    private void nationDisband(CommandSender sender) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        Player player = (Player) sender;
        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        Nation nation = Nations.getInstance().get(diplomacyPlayer);

        // cancel if sender is not in a nation
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to disband a nation.");
            return;
        }


        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canSurrender = permissions.get("CanSurrenderNation");

        // cancel if insufficient permission
        if (!canSurrender) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to disband your nation.");
            return;
        }

        // send pop-up to players in effected chunks
        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(testPlayer.getLocation().getChunk());
            var testNation = testDiplomacyChunk.getNation();
            if (Objects.equals(testNation, nation)) {
                testPlayer.sendTitle(ChatColor.WHITE + "Wilderness", null, 5, 40, 10);
            }
        }

        // disband groups belonging to nation
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

        // cancel contests in effected chunks
        var contests = ContestManager.getInstance().getContests();
        for (var contest : contests) {
            var attackingNation = contest.getAttackingNation();
            if (attackingNation.equals(nation)) {
                ContestManager.getInstance().endContest(contest);
            } else if (Objects.equals(contest.getDiplomacyChunk().getNation(), nation)) {
                contest.setIsWilderness(true);
            }
        }

        // remove chunks from nation
        var diplomacyChunks = nation.getChunks();
        for (var diplomacyChunk : diplomacyChunks) {
            nation.removeChunk(diplomacyChunk);
        }
        Bukkit.getPluginManager().callEvent(new NationRemoveChunksEvent(nation, diplomacyChunks));

        // send notifications
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

        // remove data of nation
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

        // remove nation
        Nations.getInstance().removeNation(nation);


    }

    /**
     * Used by a player to form an alliance between their nation and another nation
     * @param sender: Sender of command
     * @param strAllyNation: Name of other nation
     */
    private void nationAlly(CommandSender sender, String strAllyNation) {
        // cancel if sender is not a player
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
            sender.sendMessage(ChatColor.DARK_RED + "Your nation cannot become allies with itself.");
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
            if (testPlayerNation == null) continue;
            var testMemberClass = testPlayerNation.getMemberClass(testDiplomacyPlayer);
            if (testMemberClass == null) continue;
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
                testPlayer.sendMessage(ChatColor.AQUA + "An alliance request has been sent to " + color + receiverNation.getName() + ChatColor.AQUA + " (Expires in 60 seconds)");
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

        if (senderNation.getEnemyNationIDs().contains(receiverNation.getNationID())) {
            senderNation.removeEnemyNation(receiverNation);
            receiverNation.removeEnemyNation(senderNation);
        }

        var contests = ContestManager.getInstance().getContests();
        for (var contest : contests) {
            var attackingNation = contest.getAttackingNation();
            if (attackingNation.equals(senderNation)) {
                ContestManager.getInstance().endContest(contest);
            } else if (Objects.equals(contest.getDiplomacyChunk().getNation(), senderNation)) {
                if (attackingNation.equals(receiverNation)) {
                    ContestManager.getInstance().winContest(contest);
                } else if (attackingNation.getAllyNationIDs().contains(senderNation.getNationID())) {
                    ContestManager.getInstance().endContest(contest);
                }
            }
        }

        senderNation.addAllyNation(receiverNation);
        receiverNation.addAllyNation(senderNation);
        ScoreboardManager.getInstance().updateScoreboards();


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
                if (testPlayerNation == null) continue;
                var testMemberClass = testPlayerNation.getMemberClass(testDiplomacyPlayer);
                if (testMemberClass == null) continue;
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
                    testPlayer.sendMessage(ChatColor.AQUA + "A neutrality request has been sent to " + ChatColor.RED + otherNation.getName() + ChatColor.AQUA + " (Expires in 60 seconds)");
                }
            }
        } else if (senderNation.getAllyNationIDs().contains(otherNation.getNationID())) {

            senderNation.removeAllyNation(otherNation);
            otherNation.removeAllyNation(senderNation);
            ScoreboardManager.getInstance().updateScoreboards();

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
        ScoreboardManager.getInstance().updateScoreboards();

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
        ScoreboardManager.getInstance().updateScoreboards();

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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }


        Nations.getInstance().listNations((Player) sender, 1);
    }

    private void nationListPage(CommandSender sender, String strPage) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        int page;
        try {
            page = Integer.parseInt(strPage);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be an integer.");
            return;
        }

        if (page < 1) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be greater than 0.");
            return;
        }

        Nations.getInstance().listNations((Player) sender, page);
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
                    onlinePlayer.sendMessage(ChatColor.GREEN + player.getName() + " has joined the nation.");
                }
            }

            nation.addMember(diplomacyPlayer);
            var set = new HashSet<DiplomacyPlayer>();
            set.add(diplomacyPlayer);
            Bukkit.getPluginManager().callEvent(new NationJoinEvent(set, nation));
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
        var set = new HashSet<DiplomacyPlayer>();
        set.add(diplomacyPlayer);
        Bukkit.getPluginManager().callEvent(new NationJoinEvent(set, nation));
        sender.sendMessage(ChatColor.AQUA + "You have joined " + ChatColor.GREEN + nation.getName() + ChatColor.AQUA + ".");


    }

    private void nationAllyEnemy(CommandSender sender, String strNation, boolean ally) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        if (ally) {
            Nations.getInstance().listAllyNations((Player) sender, nation, 1);
        } else {
            Nations.getInstance().listEnemyNations((Player) sender, nation, 1);
        }
    }

    private void nationAllyEnemyPage(CommandSender sender, String strNation, boolean ally, String strPage) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        int page;
        try {
            page = Integer.parseInt(strPage);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be an integer.");
            return;
        }

        if (page < 1) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be greater than 0.");
            return;
        }

        if (ally) {
            Nations.getInstance().listAllyNations((Player) sender, nation, page);
        } else {
            Nations.getInstance().listEnemyNations((Player) sender, nation, page);
        }
    }

    private void nationGroups(CommandSender sender, String strNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        Nations.getInstance().listGroups(nation, (Player) sender, 1);
    }

    private void nationGroupsPage(CommandSender sender, String strNation, String strPage) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        int page;
        try {
            page = Integer.parseInt(strPage);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be an integer.");
            return;
        }

        if (page < 1) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be greater than 0.");
            return;
        }

        Nations.getInstance().listGroups(nation, (Player) sender, page);
    }

    private void nationMembers(CommandSender sender, String strNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        Nations.getInstance().listMembers((Player) sender, nation, 1);
    }

    private void nationMembersPage(CommandSender sender, String strNation, String strPage) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        int page;
        try {
            page = Integer.parseInt(strPage);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be an integer.");
            return;
        }

        if (page < 1) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be greater than 0.");
            return;
        }

        Nations.getInstance().listMembers((Player) sender, nation, page);
    }

    private void nationOutlaws(CommandSender sender, String strNation) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        Nations.getInstance().listOutlaws((Player) sender, nation, 1);
    }

    private void nationOutlawsPage(CommandSender sender, String strNation, String strPage) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var nation = Nations.getInstance().get(strNation);
        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Nation not found.");
            return;
        }

        int page;
        try {
            page = Integer.parseInt(strPage);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be an integer.");
            return;
        }

        if (page < 1) {
            sender.sendMessage(ChatColor.DARK_RED + "Page must be greater than 0.");
            return;
        }

        Nations.getInstance().listOutlaws((Player) sender, nation, page);
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

    private void setClass(CommandSender sender, String strOtherPlayer, String strClass) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not belong to a nation.");
            return;
        }

        var otherPlayer = DiplomacyPlayers.getInstance().get(strOtherPlayer);
        if (otherPlayer == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Player not found.");
            return;
        }

        var otherNation = Nations.getInstance().get(otherPlayer);
        if (otherNation == null) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getOfflinePlayer().getName() + " does not belong to a nation.");
            return;
        }

        if (!Objects.equals(otherNation, nation)) {
            sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getOfflinePlayer().getName() + " does not belong to your nation.");
            return;
        }

        var nClass = nation.getNationClass(strClass);
        if (nClass == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown class.");
            return;
        }

        var otherClass = nation.getMemberClass(otherPlayer);
        var otherID = Integer.parseInt(otherClass.getClassID());
        var pClass = nation.getMemberClass(diplomacyPlayer);

        var perms = pClass.getPermissions();
        if (!perms.get("CanRemoveFromClass" + (otherID + 1))) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to remove a player from the class of \"" + otherClass.getName() + "\".");
            return;
        }

        var nID = Integer.parseInt(nClass.getClassID());
        if (!perms.get("CanSetClassTo" + (nID + 1))) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to set a player's class to \"" + nClass.getName() + "\".");
            return;
        }

        var members = nation.getMembers();
        if (otherID == 8) {
            var leaders = 0;
            for (var member : members) {
                if (nation.getMemberClass(member).getClassID().equals("8")) leaders++;
                if (leaders > 1) break;
            }
            if (leaders == 1) {
                sender.sendMessage(ChatColor.DARK_RED + otherPlayer.getOfflinePlayer().getName() + " could not be demoted because there must be at least one member with the class of \"" + otherClass.getName() + "\"");
                return;
            }
        }

        nation.setMemberClass(otherPlayer, nClass);
        for (var member : members) {
            var testPlayer = member.getOfflinePlayer();
            if (testPlayer.getPlayer() != null) {
                testPlayer.getPlayer().sendMessage(ChatColor.AQUA + otherPlayer.getOfflinePlayer().getName() + "'s class has been set to " + nClass.getName());
            }
        }
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
            if (nation.getMemberClass(member).getClassID().equals("8")) {
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
        var set = new HashSet<DiplomacyPlayer>();
        set.add(diplomacyPlayer);
        Bukkit.getPluginManager().callEvent(new NationLeaveEvent(set, nation));

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
                if (nation.getMemberClass(member).getClassID().equals("8")) {
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
            var set = new HashSet<DiplomacyPlayer>();
            set.add(diplomacyPlayer);
            Bukkit.getPluginManager().callEvent(new NationLeaveEvent(set, nation));
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

        Bukkit.getPluginManager().callEvent(new NationRemoveChunksEvent(nation, diplomacyChunks));

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
                onlinePlayer.sendMessage(ChatColor.AQUA + "The nation " + ChatColor.BLUE + nation.getName() + ChatColor.AQUA + " has disbanded.");
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

        if (senderNation == null) {
            sender.sendMessage(ChatColor.RED + "You must be in a nation to kick players.");
            return;
        }
        var senderClass = senderNation.getMemberClass(senderDiplomacyPlayer);
        var senderPermissions = senderClass.getPermissions();
        var canKickPlayers = senderPermissions.get("CanKickPlayers");
        if (!canKickPlayers) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to kick players.");
            return;
        }

        var otherDiplomacyPlayer = DiplomacyPlayers.getInstance().get(strPlayer);

        if (otherDiplomacyPlayer == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        var otherNation = Nations.getInstance().get(otherDiplomacyPlayer);
        var otherPlayer = otherDiplomacyPlayer.getOfflinePlayer();

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
                onlinePlayer.sendMessage(ChatColor.BLUE + otherPlayer.getName() + ChatColor.AQUA + " has been kicked from the nation.");
            }
        }

        if (otherPlayer.isOnline()) {
            otherPlayer.getPlayer().sendMessage(ChatColor.AQUA + "You have been kicked from " + ChatColor.BLUE + senderNation.getName() + ChatColor.AQUA + ".");
        }

        senderNation.removeMember(otherDiplomacyPlayer);
        var set = new HashSet<DiplomacyPlayer>();
        set.add(otherDiplomacyPlayer);
        Bukkit.getPluginManager().callEvent(new NationLeaveEvent(set, senderNation));
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

        var diplomacyOutlaw = DiplomacyPlayers.getInstance().get(strOutlaw);

        if (diplomacyOutlaw == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown Player.");
            return;
        }

        var outlaw = diplomacyOutlaw.getOfflinePlayer();

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

        if (outlaw.isOnline()) {
            outlaw.getPlayer().sendMessage(ChatColor.AQUA + "You have been outlawed by " + color + nation.getName());
        }

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(nation, testNation) && !Objects.equals(onlinePlayer, outlaw)) {
                sender.sendMessage(color + outlaw.getName() + ChatColor.AQUA + " has been outlawed.");
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

        var diplomacyOutlaw = DiplomacyPlayers.getInstance().get(strOutlaw);

        if (diplomacyOutlaw == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown Player.");
            return;
        }

        var outlaw = diplomacyOutlaw.getOfflinePlayer();

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

        if (outlaw.isOnline()) {
            outlaw.getPlayer().sendMessage(ChatColor.AQUA + "You are no longer outlawed by " + color + nation.getName());
        }

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (Objects.equals(nation, testNation) && !Objects.equals(onlinePlayer, outlaw)) {
                sender.sendMessage(color + outlaw.getName() + ChatColor.AQUA + " is no longer outlawed.");
            }
        }

    }

    private void nationOutlawList(CommandSender sender, String page) {

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
