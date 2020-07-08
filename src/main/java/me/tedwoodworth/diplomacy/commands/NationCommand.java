package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;

import java.util.Arrays;
import java.util.List;

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
        NationCommand nationCommand = new NationCommand();

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
        DiplomacyPlayer player = DiplomacyPlayers.getInstance().get(((OfflinePlayer) sender).getUniqueId());
        Nation nation = Nations.getInstance().get(player);
        if (nation == null) {
            Nations.getInstance().createNation(name, (OfflinePlayer) sender);
            sender.sendMessage(ChatColor.GREEN + "The nation of " + name + " has been founded.");
        } else {
            sender.sendMessage(ChatColor.RED + "You must leave your current nation before you can establish a new nation.");
        }
    }

    private void nationRename(CommandSender sender, String name) {

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
