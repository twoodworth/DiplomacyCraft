package me.tedwoodworth.diplomacy.commands;

import org.bukkit.command.*;

import java.util.List;

public class GroupCommand implements CommandExecutor, TabCompleter {

    public static void register(PluginCommand pluginCommand) {
        var groupCommand = new GroupCommand();

        pluginCommand.setExecutor(groupCommand);
        pluginCommand.setTabCompleter(groupCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
