package me.tedwoodworth.diplomacy.commands;

import org.bukkit.command.*;

import java.util.List;

public class PlotCommand implements CommandExecutor, TabCompleter {
    public static void register(PluginCommand pluginCommand){
        PlotCommand plotCommand = new PlotCommand();

        pluginCommand.setExecutor(plotCommand);
        pluginCommand.setTabCompleter(plotCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
       if(args.length == 0) {
           plot(sender);
       }
       else if(args[0].equalsIgnoreCase("claim")){
           plotClaim(sender);
       }
       else if(args[0].equalsIgnoreCase("transfer")) {
           plotTransfer(sender, args[1]);
       }
       else if(args[0].equalsIgnoreCase("forsale")) {
           plotForsale(sender, args[1]);
       }
       else if(args[0].equalsIgnoreCase("notforsale")) {
           plotNotforsale(sender);
       }
       else if(args[0].equalsIgnoreCase("buy")) {
           plotBuy(sender);
       }
       else if(args[0].equalsIgnoreCase("evict")) {
           plotEvict(sender);
       }
       else if(args[0].equalsIgnoreCase("access")) {
           plotAccess(sender, args[1], args[2]);
       }
       else {

       }

        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    private void plot(CommandSender sender){

    }

    private void plotClaim(CommandSender sender){
        
    }

    private void plotTransfer(CommandSender sender, String nation){

    }

    private void plotForsale(CommandSender sender, String price){

    }

    private void plotNotforsale(CommandSender sender){

    }

    private void plotBuy(CommandSender sender){

    }

    private void plotEvict(CommandSender sender){

    }

    private void plotAccess(CommandSender sender, String group, String access){

    }
}
