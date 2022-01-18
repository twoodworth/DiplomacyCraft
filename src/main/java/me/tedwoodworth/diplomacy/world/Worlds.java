package me.tedwoodworth.diplomacy.world;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.players.PlayerUtil;
import me.tedwoodworth.diplomacy.time.TimeManager;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static me.tedwoodworth.diplomacy.time.TimeManager.OPEN_HOUR;
import static me.tedwoodworth.diplomacy.time.TimeManager.OPEN_MINUTE;

public class Worlds {

    private final World hub;
    private final World world;
    private final World clone;
    private static Worlds instance = null;


    public static Worlds getInstance() {
        if (instance == null) {
            instance = new Worlds();
        }
        return instance;
    }

    private Worlds() {
        var temp = Bukkit.getWorld("world");
        if (temp == null) {
            world = Bukkit.createWorld(
                    new WorldCreator("world")
                            .type(WorldType.NORMAL)
                            .generateStructures(false)
            );
            var border = world.getWorldBorder();
            border.setCenter(0, 0);
            border.setSize(3000);
            world.setDifficulty(Difficulty.NORMAL);
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        } else {
            world = temp;
        }
        temp = Bukkit.getWorld("hub");
        if (temp == null) {
            hub = Bukkit.createWorld(
                    new WorldCreator("hub")
                            .type(WorldType.FLAT)
                            .generateStructures(false)
            );
            var border = hub.getWorldBorder();
            border.setCenter(0, 0);
            hub.setSpawnLocation(0, 4, 0);
            border.setSize(100);
            hub.setDifficulty(Difficulty.PEACEFUL);
            hub.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            hub.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            hub.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            hub.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            hub.setGameRule(GameRule.DO_WEATHER_CYCLE, false);

        } else {
            hub = temp;
        }
        temp = Bukkit.getWorld("clone");
        if (temp == null) {
            clone = Bukkit.createWorld(new WorldCreator("clone").copy(world));
        } else {
            clone = temp;
        }
    }

    public World getHub() {
        return hub;
    }

    public World getWorld() {
        return world;
    }

    public World getClone() {
        return clone;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Worlds.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onPlayerJoin(PlayerJoinEvent e) {
            var player = e.getPlayer();
            if (!player.hasPlayedBefore()) {
                e.setJoinMessage(ChatColor.LIGHT_PURPLE + player.getName() + " has joined for the first time.");
                Location location = Worlds.getInstance().hub.getSpawnLocation();
                player.teleport(location);
            }
        }

        //todo drop money on player death

        @EventHandler
        public void onRespawn(PlayerRespawnEvent e) {
            var player = e.getPlayer();

            //todo set respawn point
            // 1) Personal Spawn
            // 2) Town Spawn
            // 3) Nation Spawn
            // 4) World Spawn
            e.setRespawnLocation(hub.getSpawnLocation()); //todo change to world "world"
        }

        @EventHandler
        private void onPlayerPortal(PlayerPortalEvent e) {
            var p = e.getPlayer();
            var world = p.getWorld();
            if (!world.equals(hub)) {
                e.setCancelled(true);
                return;
            } else {
                if (TimeManager.isOpen) {
                    //todo teleport to last location
                    // set health
                    // set hunger
                    // set experience
                    PlayerUtil.restoreInventory(p);
                    e.setTo(Worlds.getInstance().world.getSpawnLocation());
                } else {
                    e.setCancelled(true);
                    p.teleport(hub.getSpawnLocation());

                    var now = ZonedDateTime.now(ZoneId.of("UTC"));
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
    }
}
