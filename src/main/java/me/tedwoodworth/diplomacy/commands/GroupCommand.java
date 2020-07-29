package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class GroupCommand implements CommandExecutor, TabCompleter {
    private static final String groupCreateUsage = "/group create <name>";
    private static final String groupInfoUsage = "/group info <group>";
    private static final String groupRenameUsage = "/group rename <group> <name>";
    private static final String groupSurrenderUsage = "/group surrender <group> <nation>";
    private static final String groupDisbandUsage = "/group disband <group>";
    private static final String groupListUsage = "/group list";
    private static final String groupAddUsage = "/group add <player> <group>";
    private static final String groupLeaveUsage = "/group leave <group>";
    private static final String groupKickUsage = "/group kick <player> <group>";
    private static final String groupBannerUsage = "/group banner <group>";
    private static final String groupClaimUsage = "/group claim <group>";
    private static final String groupUnclaimUsage = "/group unclaim";
    private static final String groupPromoteUsage = "/group promote <player> <group>";
    private static final String groupDemoteUsage = "/group demote <player> <group>";

    public static void register(PluginCommand pluginCommand) {
        var groupCommand = new GroupCommand();

        pluginCommand.setExecutor(groupCommand);
        pluginCommand.setTabCompleter(groupCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            group(sender);
        } else if (args[0].equalsIgnoreCase("create")) {
            if (args.length == 2) {
                groupCreate(sender, args[1]);
            } else {
                sender.sendMessage(groupCreateUsage);
            }
        } else if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 2) {
                groupInfo(sender, args[1]);
            } else {
                sender.sendMessage(groupInfoUsage);
            }
        } else if (args[0].equalsIgnoreCase("rename")) {
            if (args.length == 3) {
                groupRename(sender, args[1], args[2]);
            } else {
                sender.sendMessage(groupRenameUsage);
            }
        } else if (args[0].equalsIgnoreCase("surrender")) {
            if (args.length == 3) {
                groupSurrender(sender, args[1], args[2]);
            } else {
                sender.sendMessage(groupSurrenderUsage);
            }
        } else if (args[0].equalsIgnoreCase("disband")) {
            if (args.length == 2) {
                groupDisband(sender, args[1]);
            } else {
                sender.sendMessage(groupDisbandUsage);
            }
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 2) {
                groupList(sender);
            } else {
                sender.sendMessage(groupListUsage);
            }
        } else if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 3) {
                groupAdd(sender, args[1], args[2]);
            } else {
                sender.sendMessage(groupAddUsage);
            }
        } else if (args[0].equalsIgnoreCase("leave")) {
            if (args.length == 2) {
                groupLeave(sender, args[1]);
            } else {
                sender.sendMessage(groupLeaveUsage);
            }
        } else if (args[0].equalsIgnoreCase("kick")) {
            if (args.length == 3) {
                groupKick(sender, args[1], args[2]);
            } else {
                sender.sendMessage(groupKickUsage);
            }
        } else if (args[0].equalsIgnoreCase("banner")) {
            if (args.length == 2) {
                groupBanner(sender, args[1]);
            } else {
                sender.sendMessage(groupBannerUsage);
            }
        } else if (args[0].equalsIgnoreCase("claim")) {
            if (args.length == 2) {
                groupClaim(sender, args[1]);
            } else {
                sender.sendMessage(groupClaimUsage);
            }
        } else if (args[0].equalsIgnoreCase("unclaim")) {
            if (args.length == 1) {
                groupUnclaim(sender);
            } else {
                sender.sendMessage(groupUnclaimUsage);
            }
        } else if (args[0].equalsIgnoreCase("promote")) {
            if (args.length == 3) {
                groupPromote(sender, args[1], args[2]);
            } else {
                sender.sendMessage(groupPromoteUsage);
            }
        } else if (args[0].equalsIgnoreCase("demote")) {
            if (args.length == 3) {
                groupDemote(sender, args[1], args[2]);
            } else {
                sender.sendMessage(groupDemoteUsage);
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
                        "info",
                        "rename",
                        "surrender",
                        "disband",
                        "list",
                        "add",
                        "leave",
                        "kick",
                        "banner",
                        "claim",
                        "unclaim",
                        "promote",
                        "demote");
            } else if (args[0].equalsIgnoreCase("create")) {
                return null;
            } else if (args[0].equalsIgnoreCase("info")) {
                List<String> groups = new ArrayList<>();
                for (var group : DiplomacyGroups.getInstance().getGroups()) {
                    groups.add(group.getName());
                }
                return groups;
            } else if (args[0].equalsIgnoreCase("rename")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("banner")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("surrender")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else if (args.length == 3) {
                    List<String> nations = new ArrayList<>();
                    for (var nation : Nations.getInstance().getNations())
                        nations.add(nation.getName());
                    return nations;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("disband")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("list")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    return players;
                } else if (args.length == 3) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                }
                return null;
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("kick")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    return players;
                } else if (args.length == 3) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("claim")) {
                if (args.length == 2) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("unclaim")) {
                return null;
            } else if (args[0].equalsIgnoreCase("promote")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    return players;
                } else if (args.length == 3) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            } else if (args[0].equalsIgnoreCase("demote")) {
                if (args.length == 2) {
                    List<String> players = new ArrayList<>();
                    for (var player : Bukkit.getOnlinePlayers()) {
                        players.add(player.getName());
                    }
                    return players;
                } else if (args.length == 3) {
                    List<String> groups = new ArrayList<>();
                    for (var group : DiplomacyGroups.getInstance().getGroups()) {
                        groups.add(group.getName());
                    }
                    return groups;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    private void groupInfo(CommandSender sender, String strGroup) {//TODO add
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
    }

    private void groupCreate(CommandSender sender, String name) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canCreateGroups = permissions.get("CanCreateGroups");

        if (!canCreateGroups) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to create groups.");
            return;
        }

        var sameName = DiplomacyGroups.getInstance().get(name);
        if (sameName != null) {
            sender.sendMessage(ChatColor.DARK_RED + "That name is already taken.");
            return;
        }

        DiplomacyGroups.getInstance().createGroup(name, nation);
        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testPlayer);
            if (Objects.equals(nation, testNation)) {
                onlinePlayer.sendMessage(ChatColor.AQUA + "The group " + ChatColor.BLUE + name + ChatColor.AQUA + " has been founded.");
            }
        }
    }

    private void groupRename(CommandSender sender, String strGroup, String name) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var uuid = ((OfflinePlayer) sender).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(diplomacyPlayer);

        if (nation == null) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be in a nation to use this command.");
            return;
        }

        var group = DiplomacyGroups.getInstance().get(strGroup);
        if (group == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown group.");
            return;
        }

        var isGroupLeader = diplomacyPlayer.getGroupsLed().contains(group.getGroupID());

        var memberClass = nation.getMemberClass(diplomacyPlayer);
        var permissions = memberClass.getPermissions();
        boolean canLeadAllGroups = permissions.get("CanLeadAllGroups");

        if (!(isGroupLeader || canLeadAllGroups)) {
            sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to rename this group.");
            return;
        }

        var sameName = DiplomacyGroups.getInstance().get(name);
        if (sameName != null) {
            sender.sendMessage(ChatColor.DARK_RED + "That name is already taken.");
            return;
        }


        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testPlayer);
            if (Objects.equals(nation, testNation) || testPlayer.getGroups().contains(group.getGroupID())) {
                onlinePlayer.sendMessage(ChatColor.AQUA + "The group " + ChatColor.BLUE + group.getName() + ChatColor.AQUA + " has been renamed to " + ChatColor.BLUE + name + ChatColor.AQUA + ".");
            }
        }
        group.setName(name);

    }

}
