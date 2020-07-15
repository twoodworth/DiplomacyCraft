package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.nations.MemberInfo;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class NationCommand implements CommandExecutor, TabCompleter {
    private static final String nationCreateUsage = "/nation create <nation>";
    private static final String nationRenameUsage = "/nation rename <nation>";
    private static final String nationSurrenderUsage = "/nation surrender <nation>";
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
        } else if (args.length == 1) {
            nationNation(sender, args[0]);
        } else if (args[0].equalsIgnoreCase("list")) {
            if (args.length == 1) {
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
                        "kick",
                        "open",
                        "close",
                        "outlaw");
            } else if (args[0].equalsIgnoreCase("create")) {
                return null;
            } else if (args[0].equalsIgnoreCase("rename")) {
                return null;
            } else if (args[0].equalsIgnoreCase("surrender")) {
                return null; //TODO List Nations
            } else if (args[0].equalsIgnoreCase("ally")) {
                return null; //TODO List Nations
            } else if (args[0].equalsIgnoreCase("accept")) {
                return null; //TODO List Nations with Pending Ally Requests
            } else if (args[0].equalsIgnoreCase("neutral")) {
                return null; //TODO List Nations
            } else if (args[0].equalsIgnoreCase("enemy")) {
                return null; //TODO List Nations
            } else if (args[0].equalsIgnoreCase("list")) {
                return null;
            } else if (args[0].equalsIgnoreCase("invite")) {
                return null; //TODO List Nation-less players
            } else if (args[0].equalsIgnoreCase("join")) {
                return null; //TODO List Open Nations and nations that have sent an invite
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
        var player = DiplomacyPlayers.getInstance().get(uuid);
        var nation = Nations.getInstance().get(player);
        MemberInfo playerInfo = null;
        if (nation != null) {
            var memberInfos = nation.getMemberInfos();
            for (var memberInfo : memberInfos) {
                if (player.equals(memberInfo.getMember())) {
                    playerInfo = memberInfo;
                }
            }
        }

        if (playerInfo != null) {
            var nationClass = playerInfo.getMemberClassID();
            boolean canRenameNation = Objects.requireNonNull(nation.getNationClass(nationClass)).getPermissions().get("CanRenameNation");
            var sameName = Nations.getInstance().get(name);
            if (canRenameNation) {
                var oldName = nation.getName();
                if (!oldName.equals(name)) {
                    if (sameName == null) {
                        Nations.getInstance().rename(name, nation);
                        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "The nation of \'" + oldName + "\' has been renamed to \'" + name + "\'.");
                    } else {
                        sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "The name \'" + name + "\' is taken, choose another name.");
                    }
                } else {
                    sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "The nation is already named '" + name + "'.");
                }
            } else {
                sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You do not have permission to rename the nation.");
            }
        } else {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You are not in a nation.");
        }


    }

    private void nationSurrender(CommandSender sender, String nation) {

    }

    private void nationAlly(CommandSender sender, String nation) {

    }

    private void nationAccept(CommandSender sender, String nation) {

    }

    private void nationNeutral(CommandSender sender, String nation) {

    }

    private void nationEnemy(CommandSender sender, String nation) {
        var player = (Player) sender;
        var uuid = (player).getUniqueId();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
        var senderNation = Nations.getInstance().get(diplomacyPlayer);

        if (senderNation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You must be in a nation to become enemies with another nation.");
            return;
        }

        var enemyNation = Nations.getInstance().get(nation);

        if (enemyNation == null) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "The nation of " + nation + " does not exist.");
            return;
        }

        if (Objects.equals(senderNation, enemyNation)) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Your nation cannot become enemies with itself.");
            return;
        }

        if (senderNation.getEnemyNationIDs().contains(enemyNation.getNationID())) {
            sender.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Your nation is already enemies with " + enemyNation.getName() + ".");
            return;
        }

        senderNation.addEnemyNation(enemyNation);
        enemyNation.addEnemyNation(senderNation);
        sender.sendMessage("" + ChatColor.GREEN + ChatColor.BOLD + "Your nation is now enemies with " + enemyNation.getName() + ".");

    }

    private void nationNation(CommandSender sender, String nation) {

    }

    private void nationList(CommandSender sender) {

    }

    private void nationInvite(CommandSender sender, String player) {

    }

    private void nationJoin(CommandSender sender, String nation) {

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
}
