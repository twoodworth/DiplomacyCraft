package me.tedwoodworth.diplomacy.commands;

import org.bukkit.command.*;

import java.util.List;

public class PlotCommand implements CommandExecutor, TabCompleter {
    private static final String plotUsage = "/plot ...";
    private static final String plotClaimUsage = "/plot claim";
    private static final String plotTransferUsage = "/plot transfer <nation>";
    private static final String plotForsaleUsage = "/plot forsale <price>";
    private static final String plotNotforsaleUsage = "/plot notforsale";
    private static final String plotBuyUsage = "/plot buy";
    private static final String plotEvictUsage = "/plot evict";
    private static final String plotPermissionUsage = "/plot permission (citizen|ally|outsider) (allow|deny)";

    public static void register(PluginCommand pluginCommand) {
        PlotCommand plotCommand = new PlotCommand();

        pluginCommand.setExecutor(plotCommand);
        pluginCommand.setTabCompleter(plotCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            plot(sender);
        } else if (args[0].equalsIgnoreCase("claim")) {
            if (args.length == 1) {
                plotClaim(sender);
            } else {
                sender.sendMessage(plotClaimUsage);
            }
        } else if (args[0].equalsIgnoreCase("transfer")) {
            if (args.length == 2) {
                plotTransfer(sender, args[1]);
            } else {
                sender.sendMessage(plotTransferUsage);
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
        } else if (args[0].equalsIgnoreCase("permission")) {
            if (args.length == 3) {
                plotPermission(sender, args[1], args[2]);
            } else {
                sender.sendMessage(plotPermissionUsage);
            }
        } else {
            sender.sendMessage(plotUsage);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    private void plot(CommandSender sender) {

    }

    private void plotClaim(CommandSender sender) {

    }

    private void plotTransfer(CommandSender sender, String nation) {

    }

    private void plotForsale(CommandSender sender, String price) {

    }

    private void plotNotforsale(CommandSender sender) {

    }

    private void plotBuy(CommandSender sender) {

    }

    private void plotEvict(CommandSender sender) {

    }

    private void plotPermission(CommandSender sender, String group, String access) {

    }
}
