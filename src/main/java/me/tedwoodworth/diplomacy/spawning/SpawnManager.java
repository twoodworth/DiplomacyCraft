package me.tedwoodworth.diplomacy.spawning;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.geology.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnManager {

    private static SpawnManager instance = null;
    private final List<Biome> oceanBiomes;
    private final Location[] respawnLocations = new Location[20];

    private SpawnManager() {
        this.oceanBiomes = new ArrayList<>();
        oceanBiomes.add(Biome.COLD_OCEAN);
        oceanBiomes.add(Biome.DEEP_COLD_OCEAN);
        oceanBiomes.add(Biome.DEEP_FROZEN_OCEAN);
        oceanBiomes.add(Biome.DEEP_LUKEWARM_OCEAN);
        oceanBiomes.add(Biome.DEEP_OCEAN);
        oceanBiomes.add(Biome.DEEP_WARM_OCEAN);
        oceanBiomes.add(Biome.LUKEWARM_OCEAN);
        oceanBiomes.add(Biome.OCEAN);
        oceanBiomes.add(Biome.WARM_OCEAN);

        for (int i = 0; i < respawnLocations.length; i++) {
            var location = getNewLocation();
            while (location == null) {
                location = getNewLocation();
            }
            respawnLocations[i] = location;
        }
        System.out.println("[Diplomacy] Initialized random teleportation");
    }

    private Location getNewLocation() {
        var x = (int) (Math.random() * 16384);
        var z = (int) (Math.random() * 16384);
        var loc = new Location(WorldManager.getInstance().getOverworld(), x, 255, z);
        var y = getMaxYHeight(loc);
        if (y == -1 || y > 100) {
            return null;
        } else {
            return new Location(WorldManager.getInstance().getOverworld(), x, y, z);
        }

    }

    private void replaceLocation(final int i) {
        var loc = getNewLocation();
        if (loc == null) {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> replaceLocation(i), 10L);
        } else {
            respawnLocations[i] = loc;
        }
    }

    public int getMaxYHeight(Location location) {
        for (int i = WorldManager.getInstance().getOverworld().getMaxHeight(); i > 33; i--) {
            var nLoc = new Location(location.getWorld(), location.getX(), i, location.getZ());
            var block = nLoc.getBlock();
            var type = block.getType();
            if (block.isLiquid() || type == Material.FIRE) return -1;
            if (type.isSolid()) {
                if (type == Material.CACTUS || type == Material.MAGMA_BLOCK) {
                    return -1;
                } else {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    public static SpawnManager getInstance() {
        if (instance == null) {
            instance = new SpawnManager();
        }
        return instance;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onPlayerPortal(PlayerPortalEvent event) {
            var from = event.getFrom();
            if (Objects.equals(from.getWorld(), WorldManager.getInstance().getSpawn())) {
                var i = (int) (Math.random() * respawnLocations.length);
                var loc = respawnLocations[i];
                event.setTo(loc);
                Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> replaceLocation(i));
            }
        }

    }
}
