package me.tedwoodworth.diplomacy.survival;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.database.DBManager;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.PortalCreateEvent;

public class SurvivalWorld {
    private final World world;
    private final World clone;
    private static SurvivalWorld instance = null;


    public static SurvivalWorld getInstance() {
        if (instance == null) {
            instance = new SurvivalWorld();
        }
        return instance;
    }

    private SurvivalWorld() {
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

        temp = Bukkit.getWorld("clone");
        if (temp == null) {
            clone = Bukkit.createWorld(new WorldCreator("clone").copy(world));
        } else {
            clone = temp;
        }
    }

    public World getWorld() {
        return world;
    }

    public World getClone() {
        return clone;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new SurvivalWorld.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onPlayerJoin(PlayerJoinEvent e) {
            var player = e.getPlayer();
            if (!DBManager.hasJoined(player)) {
                e.setJoinMessage(ChatColor.LIGHT_PURPLE + player.getName() + " has joined for the first time.");
                DBManager.insertPlayer(player);
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
        }
        @EventHandler
        private void onPlayerPortal(PlayerPortalEvent e) {
            e.setCancelled(true);
        }

        @EventHandler
        private void onPlayerPortal(PortalCreateEvent e) {
            e.setCancelled(true);
        }
    }
}
