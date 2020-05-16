package me.tedwoodworth.diplomacy.commands;

import org.bukkit.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlotCommand implements CommandExecutor, TabCompleter {
    private static final String plotUsage = "/plot ...";
    private static final String plotContestUsage = "/plot contest";
    private static final String plotSurrenderUsage = "/plot surrender <nation>";
    private static final String plotForsaleUsage = "/plot forsale <price>";
    private static final String plotNotforsaleUsage = "/plot notforsale";
    private static final String plotBuyUsage = "/plot buy";
    private static final String plotEvictUsage = "/plot evict";
    private static final String plotSetUsage = "/plot set (public|private)";


    public static void register(PluginCommand pluginCommand) {
        PlotCommand plotCommand = new PlotCommand();

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
        } else if (args[0].equalsIgnoreCase("forsale")) {
            if (args.length == 2) {
                plotForsale(sender, args[1]);
            } else {
                sender.sendMessage(plotForsaleUsage);
            }
        } else if (args[0].equalsIgnoreCase("notforsale")) {
            if (args.length == 1) {
                plotNotforsale(sender);
            } else {
                sender.sendMessage(plotNotforsaleUsage);
            }
        } else if (args[0].equalsIgnoreCase("buy")) {
            if (args.length == 1) {
                plotBuy(sender);
            } else {
                sender.sendMessage(plotBuyUsage);
            }
        } else if (args[0].equalsIgnoreCase("evict")) {
            if (args.length == 1) {
                plotEvict(sender);
            } else {
                sender.sendMessage(plotEvictUsage);
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (args.length > 1) {
                if(args[1].equalsIgnoreCase("public")) {
                    plotSetPrivate(sender);
                } else if(args[1].equalsIgnoreCase("private")) {
                    plotSetPrivate(sender);
                } else {
                    sender.sendMessage(plotSetUsage);
                }
            } else {
                sender.sendMessage(plotEvictUsage);
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
                return Arrays.asList("contest", "surrender", "forsale", "notforsale", "buy", "evict", "permission");
            } else if(args[0].equalsIgnoreCase("contest")) {
                return null;
            } else if(args[0].equalsIgnoreCase("surrender")) {
                return null; // TODO list nations
            } else if(args[0].equalsIgnoreCase("forsale")) {
                return null;
            } else if(args[0].equalsIgnoreCase("notforsale")) {
                return null;
            } else if(args[0].equalsIgnoreCase("buy")) {
                return null;
            } else if(args[0].equalsIgnoreCase("evict")) {
                return null;
            }else if(args[0].equalsIgnoreCase("set")) {
                if(args.length == 2) {
                    return Arrays.asList("private", "public");
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    private void plot(CommandSender sender) {

    }

    private void plotContest(CommandSender sender) {

    }

    private void plotSurrender(CommandSender sender, String nation) {

    }

    private void plotForsale(CommandSender sender, String price) {

    }

    private void plotNotforsale(CommandSender sender) {

    }

    private void plotBuy(CommandSender sender) {

    }

    private void plotEvict(CommandSender sender) {

    }

    private void plotSetPrivate(CommandSender sender) {

    }

    private void plotSetPublic(CommandSender sender) {

    }
}
