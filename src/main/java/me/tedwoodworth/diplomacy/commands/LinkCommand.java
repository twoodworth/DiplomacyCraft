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

public class LinkCommand implements CommandExecutor, TabCompleter {

    /*
        Constants used by this class to be sent to the user to show proper command usage.
     */
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String mapUsage = "/map";
    private static final String discordUsage = "/map";

    /**
     * Registers LinkCommand to the plugin
     *
     * @param pluginCommand: command to register
     */
    public static void register(PluginCommand pluginCommand) {
        var mapCommand = new LinkCommand();

        pluginCommand.setExecutor(mapCommand);
        pluginCommand.setTabCompleter(mapCommand);
    }


    /**
     * Code to be executed on usage of any command.
     * <p>
     * Used for checking if a link command is being called, and what functions to call
     * according to the command parameters.
     *
     * @param sender:  Sender of the command
     * @param command: Command being sent
     * @param label:   Command alias, if used
     * @param args:    Arguments of command
     * @return true always
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("map")) {
            if (args.length == 0) {
                map(sender);
            } else {
                sender.sendMessage(incorrectUsage + mapUsage);
            }
        } else if (command.getName().equals("discord")) {
            if (args.length == 0) {
                discord(sender);
            } else {
                sender.sendMessage(incorrectUsage + discordUsage);
            }
        }
        return true;
    }

    /**
     * Provides a list of argument recommendations based on what the user
     * has typed into the command bar so far.
     *
     * @param sender:  Sender of command
     * @param command: Command being sent
     * @param alias:   Alias of command used
     * @param args:    Arguments of command
     * @return list of arguments, or null if none should be sent.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    /**
     * Determines the cardinal direction a given player is currently facing.
     *
     * @param player: Player to check
     * @return String name of cardinal direction
     */
    public static String getCardinalDirection(Player player) {
        // get player angle relative to world, converted to 360-degree system
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }

        // return direction corresponding to the given angle
        if ((0.0D <= rotation) && (rotation < 22.5D)) {
            return "N";
        }
        if ((22.5D <= rotation) && (rotation < 67.5D)) {
            return "NE";
        }
        if ((67.5D <= rotation) && (rotation < 112.5D)) {
            return "E";
        }
        if ((112.5D <= rotation) && (rotation < 157.5D)) {
            return "SE";
        }
        if ((157.5D <= rotation) && (rotation < 202.5D)) {
            return "S";
        }
        if ((202.5D <= rotation) && (rotation < 247.5D)) {
            return "SW";
        }
        if ((247.5D <= rotation) && (rotation < 292.5D)) {
            return "W";
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
            return "NW";
        }
        return "N";
    }

    /**
     * Sends the player a hyperlink in chat to the server's live map
     * @param sender
     */
    private void map(CommandSender sender) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        // gets the map link from the config file
        var mapLink = DiplomacyConfig.getInstance().getMapLink();

        // create link component
        var hoverComponent = new ComponentBuilder()
                .append(mapLink)
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .create();

        // create message
        var message = new ComponentBuilder()
                .append("[Click me to open the map]")
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .bold(true)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, mapLink))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverComponent)))
                .create();

        // send message
        sender.spigot().sendMessage(message);
    }

    /**
     * Sends the player a link to the Discord server
     * @param sender
     */
    private void discord(CommandSender sender) {
        // cancel if sender is not a player
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        // get discord link from the config file
        var discordLink = DiplomacyConfig.getInstance().getDiscordLink();

        // create hover component
        var hoverComponent = new ComponentBuilder()
                .append(discordLink)
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .create();

        // create message
        var message = new ComponentBuilder()
                .append("[Click me to open discord]")
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .bold(true)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, discordLink))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverComponent)))
                .create();

        // send message
        sender.spigot().sendMessage(message);
    }
}
