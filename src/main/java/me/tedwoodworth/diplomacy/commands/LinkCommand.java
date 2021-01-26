package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.DiplomacyConfig;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class LinkCommand implements CommandExecutor, TabCompleter {

    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String mapUsage = "/map";
    private static final String discordUsage = "/map";

    public static void register(PluginCommand pluginCommand) {
        var mapCommand = new LinkCommand();

        pluginCommand.setExecutor(mapCommand);
        pluginCommand.setTabCompleter(mapCommand);
    }

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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    private void map(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var colors = new HashMap<Nation, Color>();
        var chunk = player.getLocation().getChunk();
        var chunkX = chunk.getX() - 6;
        var chunkZ = chunk.getZ() - 6;
        var world = chunk.getWorld();

        var contestChunks = new HashMap<DiplomacyChunk, Nation>();
        var contests = ContestManager.getInstance().getContests();
        for (var contest : contests) {
            contestChunks.put(contest.getDiplomacyChunk(), contest.getAttackingNation());
        }

        var mapMessage = new ComponentBuilder();
        mapMessage.append("***********  Map  ***********")
                .color(net.md_5.bungee.api.ChatColor.DARK_GREEN)
                .bold(true)
                .append("\n")
                .append("  Hover over map with\n  mouse for more info")
                .color(net.md_5.bungee.api.ChatColor.GRAY)
                .bold(true)
                .append("\n")
                .append("\n");

        mapMessage.append("            North           ")
                .color(net.md_5.bungee.api.ChatColor.RED)
                .append("\n");
        for (int i = 0; i < 13; i++) {
            mapMessage.append("  ");
            for (int j = 0; j < 13; j++) {
                var mapComponent = new ComponentBuilder();
                var hoverComponent = new ComponentBuilder();

                String symbol;
                var cChunk = world.getChunkAt(chunkX, chunkZ);
                var dChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(cChunk);
                var nation = dChunk.getNation();

                if (nation == null) {
                    symbol = "\u2014";
                    mapComponent
                            .append(symbol)
                            .color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                            .bold(true);
                    hoverComponent
                            .append("Wilderness")
                            .color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                            .bold(true);
                } else {
                    Color color;
                    if (colors.containsKey(nation)) {
                        color = colors.get(nation);
                    } else {
                        color = nation.getColor();
                        colors.put(nation, color);
                    }
                    mapComponent
                            .append("\u2588")
                            .color(net.md_5.bungee.api.ChatColor.of(color))
                            .bold(true);
                    hoverComponent
                            .append(nation.getName())
                            .color(net.md_5.bungee.api.ChatColor.of(color))
                            .bold(true);

                    if (dChunk.getGroup() != null) {
                        hoverComponent.append("\nGroup: ")
                                .color(net.md_5.bungee.api.ChatColor.BLUE)
                                .append(dChunk.getGroup().getName())
                                .color(net.md_5.bungee.api.ChatColor.GRAY)
                                .bold(true);
                    }
                }


                hoverComponent
                        .append("\n")
                        .append("X: ")
                        .color(net.md_5.bungee.api.ChatColor.BLUE)
                        .bold(true)
                        .append(String.valueOf(chunkX * 16 + 8))
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .bold(true)
                        .append("\n")
                        .append("Z: ")
                        .color(net.md_5.bungee.api.ChatColor.BLUE)
                        .bold(true)
                        .append(String.valueOf(chunkZ * 16 + 8))
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .bold(true);

                if (contestChunks.containsKey(dChunk)) {
                    var attacker = contestChunks.get(dChunk);
                    mapComponent.obfuscated(true);
                    hoverComponent
                            .append("\n\n")
                            .append("Contested by: ")
                            .color(net.md_5.bungee.api.ChatColor.RED)
                            .bold(true)
                            .append(attacker.getName())
                            .color(net.md_5.bungee.api.ChatColor.GRAY)
                            .bold(true);
                } else {
                    mapComponent.obfuscated(false);
                }

                mapComponent.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverComponent.create())));
                mapMessage.append(mapComponent.create());
                chunkZ++;
            }
            if (i < 12)
                mapMessage.append("\n");
            chunkX++;
            chunkZ -= 13;
        }

        var nations = new ArrayList<>(colors.keySet());
        nations.sort(Comparator.comparing(Nation::getName));

        sender.spigot().sendMessage(mapMessage.create());
    }

    private void discord(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var discordLink = DiplomacyConfig.getInstance().getDiscordLink();


        var hoverComponent = new ComponentBuilder()
                .append(discordLink)
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .create();

        var message = new ComponentBuilder()
                .append("[Click me to open discord]")
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .bold(true)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, discordLink))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverComponent)))
                .create();

        sender.spigot().sendMessage(message);
    }
}
