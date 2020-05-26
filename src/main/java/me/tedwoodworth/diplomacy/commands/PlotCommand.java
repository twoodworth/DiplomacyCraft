package me.tedwoodworth.diplomacy.commands;

import org.bukkit.command.*;

import java.util.Arrays;
import java.util.List;

public class PlotCommand implements CommandExecutor, TabCompleter {
    private static final String plotUsage = "/plot ...";
    private static final String plotContestUsage = "/plot contest";
    private static final String plotSurrenderUsage = "/plot surrender <nation>";
    private static final String plotGroupUsage = "/plot group ...";
    private static final String plotGroupSetUsage = "/plot group set <group>";
    private static final String plotGroupRemoveUsage = "/plot group remove";
    private static final String plotClearUsage = "/plot clear";


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
        } else if (args[0].equalsIgnoreCase("group")) {
            if (args.length == 1) {
                plotGroup(sender);
            } else if (args[1].equalsIgnoreCase("set")) {
                if (args.length == 3) {
                    plotGroupSet(sender, args[2]);
                } else {
                    sender.sendMessage(plotGroupSetUsage);
                }
            } else if (args[1].equalsIgnoreCase("remove")) {
                if (args.length == 2) {
                    plotGroupRemove(sender);
                } else {
                    sender.sendMessage(plotGroupRemoveUsage);
                }
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
                return Arrays.asList("contest", "surrender", "group", "clear");
            } else if (args[0].equalsIgnoreCase("contest")) {
                return null;
            } else if (args[0].equalsIgnoreCase("surrender")) {
                return null; // TODO list nations
            } else if (args[0].equalsIgnoreCase("group")) {
                if (args.length == 2) {
                    return Arrays.asList("set", "remove");
                } else {
                    return null; // TODO List groups
                }
            } else if (args[0].equalsIgnoreCase("clear")) {
                return null;
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

    private void plotGroup(CommandSender sender) {

    }

    private void plotGroupSet(CommandSender sender, String group) {

    }

    private void plotGroupRemove(CommandSender sender) {

    }

    private void plotClear(CommandSender sender) {

    }
}
