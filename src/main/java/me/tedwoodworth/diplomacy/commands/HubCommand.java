package me.tedwoodworth.diplomacy.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.bungee.BCUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HubCommand implements CommandExecutor, TabCompleter {

    public static void register(PluginCommand pluginCommand) {
        var hubCommand = new HubCommand();
        pluginCommand.setExecutor(hubCommand);
        pluginCommand.setTabCompleter(hubCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("hub")) {
            hub(sender);
            return true;
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }

    private void hub(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        BCUtil.setServer(((Player) sender), "hub");
    }
}
