package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.DiplomacyConfig;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.contest.ContestManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GuardCommand implements CommandExecutor, TabCompleter {

    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String guardUsage = "/guard";
    private static final String guardRenameUsage = "/guard rename <new name>";

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
            }
        }
        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Collections.singletonList("rename");
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
}
