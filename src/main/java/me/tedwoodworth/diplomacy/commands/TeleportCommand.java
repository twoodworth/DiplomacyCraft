package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.geology.WorldManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.*;

public class TeleportCommand implements CommandExecutor, TabCompleter {
    private static final String incorrectUsage = ChatColor.DARK_RED + "Incorrect usage, try: ";
    private static final String ottUsage = "/ott <player>";
    private static final String ottConfirmUsage = "/ottConfirm <player>";
    private static final String ottCancelUsage = "/ottCancel <player>";
    private static final String ottAcceptUsage = "/ottAccept <player>";
    private static final String ottDeclineUsage = "/ottDecline <player>";
    private static final String spawnUsage = "/spawn";

    private static Map<RequestKey, Long> requests;

    private void setRequests(Map<RequestKey, Long> requests) {
        TeleportCommand.requests = requests;
    }

    private Map<RequestKey, Long> getRequests() {
        if (requests == null) {
            requests = new HashMap<>();
        }
        return requests;
    }

    private void addRequest(RequestKey key, Long timestamp) {
        var requests = this.getRequests();
        requests.put(key, timestamp);
        setRequests(requests);
    }

    private void removeRequest(RequestKey key) {
        var requests = this.getRequests();
        requests.remove(key);
        setRequests(requests);
    }

    private int requestTaskID = -1;

    private void onRequestTask() {
        for (var requestKey : this.getRequestKeys()) {
            if (Instant.now().getEpochSecond() - this.getRequests().get(requestKey) > 60) {
                if (requestKey.teleportingPlayer.isOnline()) {
                    requestKey.teleportingPlayer.sendMessage(ChatColor.RED + "Teleport confirm request has expired.");
                }
                removeRequest(requestKey);
            }
        }
        if (this.getRequests().size() == 0) {
            cancelTask();
        }
    }

    private ArrayList<RequestKey> getRequestKeys() {
        return new ArrayList<>(this.getRequests().keySet());
    }

    private void cancelTask() {
        Bukkit.getScheduler().cancelTask(requestTaskID);
        requestTaskID = -1;
    }


    public static void register(PluginCommand pluginCommand) {
        var teleportCommand = new TeleportCommand();

        pluginCommand.setExecutor(teleportCommand);
        pluginCommand.setTabCompleter(teleportCommand);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("ott")) {
            if (args.length == 1) {
                ott(sender, args[0]);
            } else {
                sender.sendMessage(incorrectUsage + ottUsage);
            }
        } else if (command.getName().equalsIgnoreCase("ottConfirm")) {
            if (args.length == 1) {
                ottConfirm(sender, args[0]);
            } else {
                sender.sendMessage(incorrectUsage + ottConfirmUsage);
            }
        } else if (command.getName().equalsIgnoreCase("ottCancel")) {
            if (args.length == 1) {
                ottCancel(sender, args[0]);
            } else {
                sender.sendMessage(incorrectUsage + ottCancelUsage);
            }
        } else if (command.getName().equalsIgnoreCase("ottAccept")) {
            if (args.length == 1) {
                ottAccept(sender, args[0]);
            } else {
                sender.sendMessage(incorrectUsage + ottAcceptUsage);
            }
        } else if (command.getName().equalsIgnoreCase("ottDecline")) {
            if (args.length == 1) {
                ottDecline(sender, args[0]);
            } else {
                sender.sendMessage(incorrectUsage + ottDeclineUsage);
            }
        } else if (command.getName().equalsIgnoreCase("spawn")) {
            if (args.length == 0) {
                spawn(sender);
            } else {
                sender.sendMessage(incorrectUsage + spawnUsage);
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("ott")) {
            if (args.length == 1) {
                List<String> players = new ArrayList<>();
                for (var player : Bukkit.getOnlinePlayers())
                    players.add(player.getName());
                return players;
            }
        }
        return null;
    }

    private void spawn(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Must be a player to use this command.");
            return;
        }
        var player = ((Player) sender);

        if (DiplomacyPlayers.getInstance().combatLogged.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "You cannot teleport to spawn while in combat.");
            return;
        }

        var map = DiplomacyPlayers.getInstance().teleportMap;
        if (map.containsKey(player)) {
            sender.sendMessage(ChatColor.RED + "Already teleporting.");
            return;
        }
        var key = (int) (Math.random() * Integer.MAX_VALUE);
        DiplomacyPlayers.getInstance().teleportMap.put(player, key);
        player.sendMessage(ChatColor.GOLD + "Teleporting to spawn in 3 seconds, do not move.");
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> teleportSpawn(player, key), 60L);
    }

    private void teleportSpawn(Player player, int key) {
        var map = DiplomacyPlayers.getInstance().teleportMap;
        if (map.containsKey(player) && map.get(player) == key) {
            var point = WorldManager.getInstance().getSpawn().getSpawnLocation();
            point.setX(point.getX() + 0.5);
            point.setZ(point.getZ() + 0.5);
            player.sendMessage(ChatColor.GOLD + "Teleporting to spawn...");
            player.teleport(point, PlayerTeleportEvent.TeleportCause.COMMAND);
            DiplomacyPlayers.getInstance().teleportMap.remove(player);
        }

    }

    private void ott(CommandSender sender, String strOtherPlayer) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var player = (Player) sender;
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var canTeleport = diplomacyPlayer.getCanTeleport();

        if (!canTeleport) {
            sender.sendMessage(ChatColor.DARK_RED + "You have already used your one-time teleport or it has expired.");
            return;
        }

        if (diplomacyPlayer.getLives() == 0) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot teleport, you have 0 lives left.");
            return;
        }

        if (DiplomacyPlayers.getInstance().combatLogged.containsKey(player)) {
            player.sendMessage(ChatColor.RED + "You cannot use OTT while in combat.");
            return;
        }

        var otherPlayer = Bukkit.getPlayer(strOtherPlayer);

        if (otherPlayer == null) {
            sender.sendMessage(ChatColor.DARK_RED + "Unknown player.");
            return;
        }

        if (Objects.equals(player, otherPlayer)) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot teleport to yourself.");
            return;
        }


        var requestKey = new RequestKey(player, "ConfirmTeleport", otherPlayer);

        for (var request : this.getRequestKeys()) {
            if (Objects.equals(requestKey.teleportingPlayer, request.teleportingPlayer)) {
                sender.sendMessage(ChatColor.DARK_RED + "Pending one-time teleport requests must be cancelled before you can try using it again.");
                return;
            }
        }

        addRequest(requestKey, Instant.now().getEpochSecond());

        if (requestTaskID == -1) {
            requestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onRequestTask, 0L, 20L);
        }

        var acceptHoverText = new ComponentBuilder()
                .append("Click to confirm")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .create();

        var declineHoverText = new ComponentBuilder()
                .append("Click to cancel")
                .color(net.md_5.bungee.api.ChatColor.RED)
                .create();

        var message = new ComponentBuilder()
                .append("Warning: You can only teleport once ")
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .append("[Confirm]")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ottConfirm " + otherPlayer.getName()))
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(acceptHoverText)))
                .bold(true)
                .append(" ")
                .append("[Cancel]")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ottCancel " + otherPlayer.getName()))
                .color(net.md_5.bungee.api.ChatColor.RED)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(declineHoverText)))
                .bold(true)
                .create();
        player.spigot().sendMessage(message);

    }

    private void ottConfirm(CommandSender sender, String strNonTeleporter) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var teleporter = (Player) sender;
        var teleporterDiplomacyPlayer = DiplomacyPlayers.getInstance().get(teleporter.getUniqueId());
        var canTeleport = teleporterDiplomacyPlayer.getCanTeleport();

        if (!canTeleport) {
            sender.sendMessage(ChatColor.DARK_RED + "You have already used your one-time teleport or it has expired.");
            return;
        }

        if (teleporterDiplomacyPlayer.getLives() == 0) {
            sender.sendMessage(ChatColor.DARK_RED + "You cannot teleport, you have 0 lives left.");
            return;
        }

        if (DiplomacyPlayers.getInstance().combatLogged.containsKey(teleporter)) {
            teleporter.sendMessage(ChatColor.RED + "You cannot use OTT while in combat.");
            return;
        }


        var nonTeleporter = Bukkit.getPlayer(strNonTeleporter);

        if (nonTeleporter == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player you are attempting to teleport to is no longer online.");
            return;
        }

        var requestKey = new RequestKey(teleporter, "ConfirmTeleport", nonTeleporter);


        var missing = true;
        for (var request : this.getRequestKeys()) {
            if (Objects.equals(request, requestKey)) {
                missing = false;
                break;
            }
        }

        if (missing) {
            sender.sendMessage(ChatColor.DARK_RED + "Teleport confirmation request not found, it may have expired.");
            return;
        }
        removeRequest(requestKey);

        var nRequestKey = new RequestKey(teleporter, "RequestTeleport", nonTeleporter);

        addRequest(nRequestKey, Instant.now().getEpochSecond());

        if (requestTaskID == -1) {
            requestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onRequestTask, 0L, 20L);
        }

        sender.sendMessage(ChatColor.AQUA + "Teleport request has been sent.");

        var acceptHoverText = new ComponentBuilder()
                .append("Click to accept")
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .create();

        var declineHoverText = new ComponentBuilder()
                .append("Click to decline")
                .color(net.md_5.bungee.api.ChatColor.RED)
                .create();

        var message = new ComponentBuilder()
                .append(teleporter.getName() + " has requested to teleport to you ")
                .color(net.md_5.bungee.api.ChatColor.AQUA)
                .append("[Accept]")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ottAccept " + teleporter.getName()))
                .color(net.md_5.bungee.api.ChatColor.GREEN)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(acceptHoverText)))
                .bold(true)
                .append(" ")
                .append("[Decline]")
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ottDecline " + teleporter.getName()))
                .color(net.md_5.bungee.api.ChatColor.RED)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(declineHoverText)))
                .bold(true)
                .create();
        nonTeleporter.spigot().sendMessage(message);

    }

    private void ottCancel(CommandSender sender, String strNonTeleporter) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var teleporter = (Player) sender;
        var teleporterDiplomacyPlayer = DiplomacyPlayers.getInstance().get(teleporter.getUniqueId());
        var canTeleport = teleporterDiplomacyPlayer.getCanTeleport();

        if (!canTeleport) {
            sender.sendMessage(ChatColor.DARK_RED + "You have already used your one-time teleport.");
            return;
        }

        var nonTeleporter = Bukkit.getPlayer(strNonTeleporter);

        if (nonTeleporter == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player you attempted to teleport to is no longer online.");
            return;
        }

        var requestKey = new RequestKey(teleporter, "ConfirmTeleport", nonTeleporter);

        if (!getRequestKeys().contains(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Teleport confirmation request not found, it may have expired.");
            return;
        }

        removeRequest(requestKey);

        sender.sendMessage(ChatColor.AQUA + "Teleport cancelled.");
    }

    private void ottAccept(CommandSender sender, String strTeleporter) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var teleporter = Bukkit.getPlayer(strTeleporter);

        if (teleporter == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player attempting to teleport to you is no longer online.");
            return;
        }
        var teleporterDiplomacyPlayer = DiplomacyPlayers.getInstance().get(teleporter.getUniqueId());
        var canTeleport = teleporterDiplomacyPlayer.getCanTeleport();

        if (!canTeleport) {
            sender.sendMessage(ChatColor.DARK_RED + "That player has already used their one-time teleport.");
            return;
        }

        if (teleporterDiplomacyPlayer.getLives() == 0) {
            sender.sendMessage(ChatColor.DARK_RED + "That player has 0 lives and cannot teleport.");
            return;
        }

        if (DiplomacyPlayers.getInstance().combatLogged.containsKey(teleporter)) {
            sender.sendMessage(ChatColor.RED + "The player attempting to teleport to you is in combat and cannot use their OTT.");
            return;
        }

        var nonTeleporter = (Player) sender;

        var requestKey = new RequestKey(teleporter, "RequestTeleport", nonTeleporter);

        if (!getRequestKeys().contains(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Teleport confirmation request not found, it may have expired.");
            return;
        }

        removeRequest(requestKey);

        sender.sendMessage(ChatColor.AQUA + "Teleport request has been accepted.");
        teleporter.sendMessage(ChatColor.AQUA + "Teleport request has been accepted.");

        teleporter.teleport(nonTeleporter, PlayerTeleportEvent.TeleportCause.COMMAND);
        teleporterDiplomacyPlayer.setCanTeleport(false);

    }

    private void ottDecline(CommandSender sender, String strTeleporter) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.DARK_RED + "You must be a player to use this command.");
            return;
        }

        var teleporter = Bukkit.getPlayer(strTeleporter);

        if (teleporter == null) {
            sender.sendMessage(ChatColor.DARK_RED + "The player attempting to teleport to is no longer online.");
            return;
        }

        var teleporterDiplomacyPlayer = DiplomacyPlayers.getInstance().get(teleporter.getUniqueId());
        var canTeleport = teleporterDiplomacyPlayer.getCanTeleport();

        if (!canTeleport) {
            sender.sendMessage(ChatColor.DARK_RED + "That player has already used their one-time teleport.");
            return;
        }

        var nonTeleporter = (Player) sender;

        var requestKey = new RequestKey(teleporter, "RequestTeleport", nonTeleporter);

        if (!getRequestKeys().contains(requestKey)) {
            sender.sendMessage(ChatColor.DARK_RED + "Teleport confirmation request not found, it may have expired.");
            return;
        }

        removeRequest(requestKey);

        sender.sendMessage(ChatColor.AQUA + "Teleport request has been declined.");
        teleporter.sendMessage(ChatColor.AQUA + "Teleport request has been declined.");
    }

    private static class RequestKey {

        private final Player teleportingPlayer;
        private final String keyType;
        private final Player teleportToPlayer;

        public RequestKey(Player teleportingPlayer, String keyType, Player teleportToPlayer) {
            this.teleportingPlayer = teleportingPlayer;
            this.keyType = keyType;
            this.teleportToPlayer = teleportToPlayer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RequestKey that = (RequestKey) o;
            return Objects.equals(teleportingPlayer, that.teleportingPlayer) &&
                    Objects.equals(keyType, that.keyType) &&
                    Objects.equals(teleportToPlayer, that.teleportToPlayer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(teleportingPlayer, keyType, teleportToPlayer);
        }
    }
}
