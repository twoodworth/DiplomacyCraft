package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.DiplomacyConfig;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.List;

public class MapCommand implements CommandExecutor, TabCompleter {

    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String mapUsage = "/map";

    public static void register(PluginCommand pluginCommand) {
        var mapCommand = new MapCommand();

        pluginCommand.setExecutor(mapCommand);
        pluginCommand.setTabCompleter(mapCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            map(sender);
        } else {
            sender.sendMessage(incorrectUsage + mapUsage);
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    private void map(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var mapLink = DiplomacyConfig.getInstance().getMapLink();


        var hoverComponent = new ComponentBuilder()
                .append(mapLink)
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .create();

        var message = new ComponentBuilder()
                .append("[Click me to open the map]")
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .bold(true)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, mapLink))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverComponent)))
                .create();

        sender.spigot().sendMessage(message);
    }
}