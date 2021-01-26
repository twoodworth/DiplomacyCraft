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

    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
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

    private void map(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var colors = new HashMap<Nation, Color>();
        var chunk = player.getLocation().getChunk();
        var chunkX = chunk.getX() - 6;
        var chunkZ = chunk.getZ() + 6;
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

        var direction = getCardinalDirection(player);
        var directionText = new ComponentBuilder();
        directionText
                .append("Facing: ")
                .color(net.md_5.bungee.api.ChatColor.BLUE)
                .bold(true);
        switch (direction) {
            default -> directionText.append("North");
            case "NE" -> directionText.append("Northeast");
            case "E" -> directionText.append("East");
            case "SE" -> directionText.append("Southeast");
            case "S" -> directionText.append("South");
            case "SW" -> directionText.append("Southwest");
            case "W" -> directionText.append("West");
            case "NW" -> directionText.append("Northwest");
        }
        directionText.color(net.md_5.bungee.api.ChatColor.GOLD)
                .bold(true);
        var directionHover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(directionText.create()));

        for (int i = 0; i < 13; i++) {
            mapMessage.append("  ");
            for (int j = 0; j < 13; j++) {
                var mapComponent = new ComponentBuilder();
                var hoverComponent = new ComponentBuilder();

                String symbol;
                var cChunk = world.getChunkAt(chunkX, chunkZ);
                var dChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(cChunk);
                var nation = dChunk.getNation();
                var current = false;
                if (i == 6 && j == 6) {
                    symbol = "\u2014";
                    current = true;
                } else {
                    if (nation == null) {
                        symbol = "\u2014";
                    } else {
                        symbol = "\u2588";
                    }
                }
                if (nation == null) {
                    mapComponent
                            .append(symbol)
                            .bold(true);
                    if (current) {
                        mapComponent
                                .color(net.md_5.bungee.api.ChatColor.GOLD);
                    } else {
                        mapComponent
                                .color(net.md_5.bungee.api.ChatColor.DARK_GRAY);
                    }
                    hoverComponent
                            .append("Wilderness")
                            .color(net.md_5.bungee.api.ChatColor.DARK_GRAY)
                            .bold(true);
                } else {
                    net.md_5.bungee.api.ChatColor color;
                    if (colors.containsKey(nation)) {
                        color = net.md_5.bungee.api.ChatColor.of(colors.get(nation));
                    } else {
                        var nColor = nation.getColor();
                        color = net.md_5.bungee.api.ChatColor.of(nColor);
                        colors.put(nation, nColor);
                    }
                    mapComponent
                            .append(symbol)
                            .bold(true);
                    if (current) {
                        mapComponent
                                .color(net.md_5.bungee.api.ChatColor.GOLD);
                    } else {
                        mapComponent
                                .color(color);
                    }
                    hoverComponent
                            .append(nation.getName())
                            .color(color)
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

                if (current) {
                    hoverComponent
                            .append("\nCurrent Location")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .bold(true);
                }

                if (contestChunks.containsKey(dChunk) && !(i == 6 && j == 6)) {
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
                chunkZ--;
            }
            if (i == 4) {
                mapMessage
                        .append("  Facing:")
                        .color(net.md_5.bungee.api.ChatColor.GOLD)
                        .bold(true)
                        .event(directionHover);
            } else if (i == 6) {
                mapMessage
                        .append("    ")
                        .event(directionHover);
                if (direction.equals("NW")) {
                    mapMessage
                            .append("\\")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .bold(true)
                            .event(directionHover);
                } else {
                    mapMessage
                            .append(" ")
                            .event(directionHover);
                }

                if (direction.equals("N")) {
                    mapMessage
                            .append("N")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .bold(true)
                            .event(directionHover);
                } else {
                    mapMessage
                            .append("N")
                            .color(net.md_5.bungee.api.ChatColor.WHITE)
                            .bold(true)
                            .event(directionHover);
                }

                if (direction.equals("NE")) {
                    mapMessage
                            .append("/")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .bold(true)
                            .event(directionHover);
                } else {
                    mapMessage
                            .append(" ")
                            .event(directionHover);
                }
            } else if (i == 7) {
                mapMessage
                        .append("    ")
                        .event(directionHover);
                if (direction.equals("W")) {
                    mapMessage
                            .append("W")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .bold(true)
                            .event(directionHover);
                } else {
                    mapMessage
                            .append("W")
                            .color(net.md_5.bungee.api.ChatColor.WHITE)
                            .bold(true)
                            .event(directionHover);
                }
                mapMessage.append("+")
                        .bold(false)
                        .color(net.md_5.bungee.api.ChatColor.GRAY)
                        .event(directionHover);
                if (direction.equals("E")) {
                    mapMessage
                            .append("E")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .bold(true)
                            .event(directionHover);
                } else {
                    mapMessage
                            .append("E")
                            .color(net.md_5.bungee.api.ChatColor.WHITE)
                            .bold(true)
                            .event(directionHover);
                }
            } else if (i == 8) {
                mapMessage
                        .append("    ")
                        .event(directionHover);
                if (direction.equals("SW")) {
                    mapMessage
                            .append("/")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .bold(true)
                            .event(directionHover);
                } else {
                    mapMessage
                            .append(" ")
                            .event(directionHover);
                }

                if (direction.equals("S")) {
                    mapMessage
                            .append("S")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .bold(true)
                            .event(directionHover);
                } else {
                    mapMessage
                            .append("S")
                            .color(net.md_5.bungee.api.ChatColor.WHITE)
                            .bold(true)
                            .event(directionHover);
                }

                if (direction.equals("SE")) {
                    mapMessage
                            .append("\\")
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .bold(true)
                            .event(directionHover);
                } else {
                    mapMessage
                            .append(" ")
                            .event(directionHover);
                }
            }

            if (i < 12)
                mapMessage.append("\n");
            chunkX++;
            chunkZ += 13;
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
