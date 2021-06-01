package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.guards.GuardManager;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.util.Arrays;
import java.util.List;

public class GuardCommand implements CommandExecutor, TabCompleter {

    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String guardUsage = "/guard";
    private static final String guardRenameUsage = "/guard rename <new name>";
    private static final String guardStatsUsage = "/guard stats <type> <level>";

    public static void register(PluginCommand pluginCommand) {
        var mapCommand = new GuardCommand();

        pluginCommand.setExecutor(mapCommand);
        pluginCommand.setTabCompleter(mapCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("guard")) {
            if (args.length == 0) {
                sender.sendMessage(incorrectUsage + guardUsage);
            } else if (args[0].equalsIgnoreCase("rename")) {
                if (args.length == 2) {
                    renameGuard(sender, args[1]);
                } else {
                    sender.sendMessage(incorrectUsage + guardRenameUsage);
                }
            } else if (args[0].equalsIgnoreCase("stats")) {
                if (args.length == 3) {
                    guardStats(sender, args[1], args[2]);
                } else {
                    sender.sendMessage(incorrectUsage + guardStatsUsage);
                }
            }
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("rename", "stats");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("stats")) {
            return Arrays.asList(
                    "sniper",
                    "gunner",
                    "flamethrower",
                    "healer",
                    "tank",
                    "teleporter",
                    "generator",
                    "snowmaker"
            );
        }
        return null;
    }


    private Entity getEntityLookedAt(Player player) {
        var location = player.getEyeLocation().clone();
        var direction = location.getDirection();
        for (int i = 0; i <= 20; i++) {
            var block = location.getBlock();
            if (!block.getType().equals(Material.AIR) && !block.getType().equals(Material.WATER) && !block.isPassable()) {
                return null;
            }
            var nearby = player.getNearbyEntities(i, i, i);
            for (var entity : nearby) {
                var box = entity.getBoundingBox().clone();
                if (box.contains(location.toVector())) {
                    if (entity instanceof Item || entity instanceof Projectile) continue;
                    return entity;
                }
            }
            location.setX(location.getX() + direction.getX() * 0.25);
            location.setY(location.getY() + direction.getY() * 0.25);
            location.setZ(location.getZ() + direction.getZ() * 0.25);
        }
        return null;
    }

    private void renameGuard(CommandSender sender, String newName) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }
        if (newName.length() > 20) {
            sender.sendMessage(ChatColor.RED + "Error: Name cannot exceed 20 characters");
            return;
        }

        var player = ((Player) sender);

        var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(dp);
        if (nation == null) {
            sender.sendMessage(ChatColor.RED + "Error: You must belong to a nation to use this command.");
            return;
        }

        var permissions = nation.getMemberClass(dp).getPermissions();
        var canManageGuards = permissions.get("CanManageGuards");
        if (!canManageGuards) {
            sender.sendMessage(ChatColor.RED + "Error: You do not have permission to use this command.");
            return;
        }

        var lookedAt = getEntityLookedAt(player);
        if (lookedAt == null) {
            player.sendMessage(ChatColor.RED + "Error: You are not looking at a guard, or guard is too far away.");
            return;
        }

        if (!GuardManager.getInstance().isGuard(lookedAt)) {
            player.sendMessage(ChatColor.RED + "Error: You are not looking at a guard.");
            return;
        }

        var gNation = GuardManager.getInstance().getNation(lookedAt);
        if (!nation.equals(gNation)) {
            player.sendMessage(ChatColor.RED + "Error: This guard crystal does not belong to your nation.");
            return;
        }
        GuardManager.getInstance().setName(lookedAt, newName);
        sender.sendMessage(ChatColor.GREEN + "Guard name set to '" + newName + "'.");
    }

    private void guardStats(CommandSender sender, String type, String strLevel) {

        int level;
        try {
            level = Integer.parseInt(strLevel);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Level must be a number.");
            return;
        }

        if (level < 1 || level > 100) {
            sender.sendMessage(ChatColor.RED + "Level must be from 1 - 100.");
            return;
        }

        final var lower = type.toLowerCase();
        var instance = GuardManager.getInstance();
        switch (lower) {
//            String.format("%.2f", health);
            case "sniper" -> {
                var message = ChatColor.DARK_GREEN + "Level " + level + " sniper stats:\n";
                message += ChatColor.DARK_GREEN + "  Cost: " + instance.sniperCost[level - 1] + " magic dust\n";
                message += ChatColor.DARK_GREEN + "  Max Health: " + String.format("%.2f", (instance.sniperMaxHealth[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Power: " + String.format("%.2f", (instance.sniperPower[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Precision: " + String.format("%.2f", (instance.sniperPrecision[level - 1])) + "\u00B0\n";
                message += ChatColor.DARK_GREEN + "  Radius: " + String.format("%.2f", (instance.sniperRadius[level - 1])) + " blocks\n";
                message += ChatColor.DARK_GREEN + "  Resistance: " + String.format("%.2f", (100 * instance.sniperResistance[level - 1])) + "%\n";
                message += ChatColor.DARK_GREEN + "  Velocity: " + String.format("%.2f", (20.0 * instance.sniperVelocity[level - 1])) + " blocks/s";
                sender.sendMessage(message);
            }
            case "gunner" -> {
                var message = ChatColor.DARK_GREEN + "Level " + level + " gunner stats:\n";
                message += ChatColor.DARK_GREEN + "  Cost: " + instance.gunnerCost[level - 1] + " magic dust\n";
                message += ChatColor.DARK_GREEN + "  Fire Rate: " + String.format("%.2f", (20.0 / instance.gunnerDelay[level - 1])) + " arrows/s\n";
                message += ChatColor.DARK_GREEN + "  Max Health: " + String.format("%.2f", (instance.gunnerMaxHealth[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Power: " + String.format("%.2f", (instance.gunnerPower[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Precision: " + String.format("%.2f", (instance.gunnerPrecision[level - 1])) + "\u00B0\n";
                message += ChatColor.DARK_GREEN + "  Radius: " + String.format("%.2f", (instance.gunnerRadius[level - 1])) + " blocks\n";
                message += ChatColor.DARK_GREEN + "  Resistance: " + String.format("%.2f", (100 * instance.gunnerResistance[level - 1])) + "%\n";
                message += ChatColor.DARK_GREEN + "  Velocity: " + String.format("%.2f", (20.0 * instance.gunnerVelocity[level - 1])) + " blocks/s";
                sender.sendMessage(message);
            }
            case "flamethrower" -> {
                var message = ChatColor.DARK_GREEN + "Level " + level + " flamethrower stats:\n";
                message += ChatColor.DARK_GREEN + "  Cost: " + instance.flamethrowerCost[level - 1] + " magic dust\n";
                message += ChatColor.DARK_GREEN + "  Fire Rate: " + String.format("%.2f", (20.0 / instance.flamethrowerDelay[level - 1] * instance.flamesPerTick[level - 1])) + " flames/s\n";
                message += ChatColor.DARK_GREEN + "  Max Health: " + String.format("%.2f", (instance.flamethrowerMaxHealth[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Flame Damage: " + String.format("%.2f", (instance.flamethrowerPower[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Burn length: " + String.format("%.2f", (instance.flamethrowerBurnTime[level - 1] / 20.0)) + "s\n";
                message += ChatColor.DARK_GREEN + "  Radius: 12.00 blocks\n";
                message += ChatColor.DARK_GREEN + "  Resistance: " + String.format("%.2f", (100 * instance.flamethrowerResistance[level - 1])) + "%";
                sender.sendMessage(message);
            }
            case "healer" -> {
                var message = ChatColor.DARK_GREEN + "Level " + level + " healer stats:\n";
                message += ChatColor.DARK_GREEN + "  Cost: " + instance.healerCost[level - 1] + " magic dust\n";
                message += ChatColor.DARK_GREEN + "  Fire Rate: 1 heal every 2 seconds\n";
                message += ChatColor.DARK_GREEN + "  Max Health: " + String.format("%.2f", (instance.healerMaxHealth[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Power: " + String.format("%.2f", (60.0 * instance.healerPower[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Radius: " + String.format("%.2f", (instance.healerRadius[level - 1])) + " blocks\n";
                message += ChatColor.DARK_GREEN + "  Resistance: " + String.format("%.2f", (100 * instance.healerResistance[level - 1])) + "%";
                sender.sendMessage(message);
            }
            case "tank" -> {
                var message = ChatColor.DARK_GREEN + "Level " + level + " tank stats:\n";
                message += ChatColor.DARK_GREEN + "  Blast Radius: " + String.format("%.2f", (instance.tankPower[level - 1])) + " blocks\n";
                message += ChatColor.DARK_GREEN + "  Cost: " + instance.tankCost[level - 1] + " magic dust\n";
                message += ChatColor.DARK_GREEN + "  Fire Rate: 1 RPG every 8 seconds\n";
                message += ChatColor.DARK_GREEN + "  Max Health: " + String.format("%.2f", (instance.tankMaxHealth[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Radius: 50.0 blocks\n";
                message += ChatColor.DARK_GREEN + "  Resistance: " + String.format("%.2f", (100 * instance.tankResistance[level - 1])) + "%";
                sender.sendMessage(message);
            }
            case "teleporter" -> {
                var message = ChatColor.DARK_GREEN + "Level " + level + " teleporter stats:\n";
                message += ChatColor.DARK_GREEN + "  Cost: " + instance.teleporterCost[level - 1] + " magic dust\n";
                message += ChatColor.DARK_GREEN + "  Load Rate: " + String.format("%.2f", (1.0 / (instance.teleporterLoadRate[level - 1] / 12.0))) + " blocks/s\n";
                message += ChatColor.DARK_GREEN + "  Max Health: " + String.format("%.2f", (instance.teleporterMaxHealth[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Radius: " + String.format("%.2f", (instance.teleporterRadius[level - 1])) + " blocks\n";
                message += ChatColor.DARK_GREEN + "  Resistance: " + String.format("%.2f", (100 * instance.tankResistance[level - 1])) + "%";
                sender.sendMessage(message);
            }
            case "generator" -> {
                var message = ChatColor.DARK_GREEN + "Level " + level + " teleporter stats:\n";
                message += ChatColor.DARK_GREEN + "  Cost: " + instance.generatorCost[level - 1] + " magic dust\n";
                message += ChatColor.DARK_GREEN + "  Max Health: " + String.format("%.2f", (instance.generatorMaxHealth[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Resistance: " + String.format("%.2f", (100 * instance.generatorResistance[level - 1])) + "%\n";
                message += ChatColor.DARK_GREEN + "  Average Drop Wait Times:\n";
                var rate = instance.generatorRate[level - 1];
                // iron nugget = (12800 / rate) seconds per drop
                // gold nugget = (12800 / rate) seconds per drop
                // gunpowder = (172800 / rate) seconds per drop
                // glowstone = (172800 / rate) seconds per drop
                // lapis = (172800 / rate) seconds per drop
                // quartz = (172800 / rate) seconds per drop
                // redstone = (172800 / rate) seconds per drop
                // magic dust = (345600 / rate) seconds per drop
                // emerald = (691200 / rate) seconds per drop
                // diamond = (691200 / rate) seconds per drop
                // netherite scrap = (3686400 / rate) seconds per drop

                // iron
                var itemRate = (int) (12800 / rate);
                var years = itemRate / 31557600;
                var remainder = itemRate - (years * 31557600);
                var days = remainder / 86400;
                remainder -= (days * 86400);
                var hours = remainder / 3600;
                remainder -= (hours * 3600);
                var minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Iron Nugget: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // gold
                itemRate = (int) (12800 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Gold Nugget: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // gunpowder
                itemRate = (int) (172800 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Gunpowder: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // glowstone
                itemRate = (int) (172800 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Glowstone: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // lapis
                itemRate = (int) (172800 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Lapis Lazuli: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // quartz
                itemRate = (int) (172800 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Quartz: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // redstone
                itemRate = (int) (172800 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Redstone: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // magic dust
                itemRate = (int) (345600 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Magic Dust: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // diamond
                itemRate = (int) (691200 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Diamond: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // emerald
                itemRate = (int) (691200 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Emerald: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m\n";

                // netherite scrap
                itemRate = (int) (3686400 / rate);
                years = itemRate / 31557600;
                remainder = itemRate - (years * 31557600);
                days = remainder / 86400;
                remainder -= (days * 86400);
                hours = remainder / 3600;
                remainder -= (hours * 3600);
                minutes = remainder / 60;
                message += ChatColor.DARK_GREEN + "    Netherite Scrap: ";
                if (years > 0) message += years + "y ";
                if (days > 0) message += days + "d ";
                if (hours > 0) message += hours + "h ";
                message += minutes + "m";
                sender.sendMessage(message);
            }
            case "snowmaker" -> {
                var message = ChatColor.DARK_GREEN + "Level " + level + " snowmaker stats:\n";
                message += ChatColor.DARK_GREEN + "  Cost: " + instance.snowmakerCost[level - 1] + " magic dust\n";
                message += ChatColor.DARK_GREEN + "  Fire Rate: " + String.format("%.2f", (20.0 / instance.snowmakerDelay[level - 1] * instance.snowmakerBallsPerTick[level - 1])) + " snowballs/s\n";
                message += ChatColor.DARK_GREEN + "  Max Health: " + String.format("%.2f", (instance.snowmakerMaxHealth[level - 1])) + " HP\n";
                message += ChatColor.DARK_GREEN + "  Ball Slowness: Slowness " + (1 + instance.snowmakerPower[level - 1]) + "\n";
                message += ChatColor.DARK_GREEN + "  Slowness length: " + String.format("%.2f", (instance.snowmakerSlowTime[level - 1] / 20.0)) + "s\n";
                message += ChatColor.DARK_GREEN + "  Radius: 24.00 blocks\n";
                message += ChatColor.DARK_GREEN + "  Resistance: " + String.format("%.2f", (100 * instance.snowmakerResistance[level - 1])) + "%";
                sender.sendMessage(message);
            }
            default -> {
                sender.sendMessage(ChatColor.RED + "Invalid guard type.");
                return;
            }
        }
    }
}
