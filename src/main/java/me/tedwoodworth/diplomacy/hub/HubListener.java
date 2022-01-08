package me.tedwoodworth.diplomacy.hub;

import me.tedwoodworth.diplomacy.bungee.BCUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static me.tedwoodworth.diplomacy.hub.HubTimer.OPEN_HOUR;
import static me.tedwoodworth.diplomacy.hub.HubTimer.OPEN_MINUTE;

public class HubListener implements Listener {

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent e) {
        if (Objects.requireNonNull(e.getTo()).getBlock().getType().equals(Material.END_PORTAL)) {
            if (HubTimer.isOpen) {
                BCUtil.setServer(e.getPlayer(), "survival");
            } else {
                var p = e.getPlayer();
                p.teleport(HubWorld.getInstance().getHub().getSpawnLocation());

                var now = ZonedDateTime.now(ZoneId.of("America/New_York"));
                var nextRun = now.withHour(OPEN_HOUR).withMinute(OPEN_MINUTE).withSecond(0);
                if (now.compareTo(nextRun) > 0) nextRun = nextRun.plusDays(1);
                Duration duration = Duration.between(now, nextRun);
                var diff = duration.getSeconds();
                var h = diff / 3600;
                diff = diff % 3600;
                var m = diff / 60;
                var s = diff % 60;

                p.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Server will open in " + h + "h " + m + "m " + s + "s.");
            }
        }
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        event.getPlayer().teleport(HubWorld.getInstance().getHub().getSpawnLocation());
        event.setJoinMessage(null);
    }

    @EventHandler
    private void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }
}
