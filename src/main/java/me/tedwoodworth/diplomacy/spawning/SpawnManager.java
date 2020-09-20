package me.tedwoodworth.diplomacy.spawning;

import com.google.common.collect.ImmutableMap;
import io.papermc.lib.features.bedspawnlocation.BedSpawnLocation;
import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.data.type.Bed;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SpawnManager {

    private static SpawnManager instance = null;
    private final List<Biome> badBiomes;
    private final List<Biome> oceanBiomes;
    private final List<Biome> endBiomes;
    private final List<Biome> netherBiomes;

    private SpawnManager() {
        this.badBiomes = new ArrayList<>();
        badBiomes.add(Biome.BASALT_DELTAS);
        badBiomes.add(Biome.COLD_OCEAN);
        badBiomes.add(Biome.CRIMSON_FOREST);
        badBiomes.add(Biome.DEEP_COLD_OCEAN);
        badBiomes.add(Biome.DEEP_FROZEN_OCEAN);
        badBiomes.add(Biome.DEEP_LUKEWARM_OCEAN);
        badBiomes.add(Biome.DEEP_OCEAN);
        badBiomes.add(Biome.DEEP_WARM_OCEAN);
        badBiomes.add(Biome.BADLANDS);
        badBiomes.add(Biome.BADLANDS_PLATEAU);
        badBiomes.add(Biome.DESERT);
        badBiomes.add(Biome.DESERT_HILLS);
        badBiomes.add(Biome.DESERT_LAKES);
        badBiomes.add(Biome.END_BARRENS);
        badBiomes.add(Biome.END_HIGHLANDS);
        badBiomes.add(Biome.END_MIDLANDS);
        badBiomes.add(Biome.ERODED_BADLANDS);
        badBiomes.add(Biome.FROZEN_OCEAN);
        badBiomes.add(Biome.FROZEN_RIVER);
        badBiomes.add(Biome.ICE_SPIKES);
        badBiomes.add(Biome.LUKEWARM_OCEAN);
        badBiomes.add(Biome.MODIFIED_BADLANDS_PLATEAU);
        badBiomes.add(Biome.MUSHROOM_FIELD_SHORE);
        badBiomes.add(Biome.MUSHROOM_FIELDS);
        badBiomes.add(Biome.NETHER_WASTES);
        badBiomes.add(Biome.OCEAN);
        badBiomes.add(Biome.RIVER);
        badBiomes.add(Biome.SMALL_END_ISLANDS);
        badBiomes.add(Biome.SNOWY_BEACH);
        badBiomes.add(Biome.SNOWY_TUNDRA);
        badBiomes.add(Biome.SOUL_SAND_VALLEY);
        badBiomes.add(Biome.THE_END);
        badBiomes.add(Biome.THE_VOID);
        badBiomes.add(Biome.WARM_OCEAN);
        badBiomes.add(Biome.WARPED_FOREST);

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

        this.endBiomes = new ArrayList<>();
        endBiomes.add(Biome.END_BARRENS);
        endBiomes.add(Biome.END_HIGHLANDS);
        endBiomes.add(Biome.END_MIDLANDS);
        endBiomes.add(Biome.SMALL_END_ISLANDS);
        endBiomes.add(Biome.THE_END);

        this.netherBiomes = new ArrayList<>();
        netherBiomes.add(Biome.BASALT_DELTAS);
        netherBiomes.add(Biome.CRIMSON_FOREST);
        netherBiomes.add(Biome.NETHER_WASTES);
        netherBiomes.add(Biome.SOUL_SAND_VALLEY);
        netherBiomes.add(Biome.WARPED_FOREST);
    }

    public @Nullable Location getRespawnLocation(Location deathLocation, boolean death) {
        var world = deathLocation.getWorld();
        if (world.getEnvironment().equals(World.Environment.THE_END)) {
            death = false;
            world = Bukkit.getWorld("world");
            deathLocation = world.getSpawnLocation();
        }
        var chunk = deathLocation.getChunk();
        if (world != null) {
            var border = world.getWorldBorder();
            var center = border.getCenter();
            var size = border.getSize();
            var maxX = center.getX() + size / 2.0 - 8;
            var minX = center.getX() - size / 2.0 + 8;
            var maxZ = center.getZ() + size / 2.0 - 8;
            var minZ = center.getZ() - size / 2.0 + 8;

            if (death) {
                maxX = Math.min(maxX, deathLocation.getX() + 1000);
                minX = Math.max(minX, deathLocation.getX() - 1000);
                maxZ = Math.min(maxZ, deathLocation.getZ() + 1000);
                minZ = Math.max(minZ, deathLocation.getZ() - 1000);
            }

            var deathBiome = deathLocation.getBlock().getBiome();

            var x = Math.random() * (Math.abs(maxX) + Math.abs(minX)) - Math.abs(minX);
            double y;
            var z = Math.random() * (Math.abs(maxZ) + Math.abs(minZ)) - Math.abs(minZ);

            Location location;
            if (deathLocation.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                y = Math.random() * 89 + 32;
                location = new Location(world, x, y, z);
                var block = location.getBlock();

                if (block.isLiquid() || block.getType().equals(Material.FIRE) || block.getType().equals(Material.MAGMA_BLOCK)) {
                    return getRespawnLocation(deathLocation, death);
                } else if (block.getType().equals(Material.AIR) || block.isPassable()) {
                    var testLocation = decreaseYLocation(location);
                    var testBlock = testLocation.getBlock();
                    if (testBlock.isLiquid() || testBlock.getType().equals(Material.FIRE) || block.getType().equals(Material.MAGMA_BLOCK)) {
                        return getRespawnLocation(deathLocation, death);
                    } else {
                        return testLocation;
                    }
                }
            } else if (oceanBiomes.contains(deathBiome)) {
                y = world.getHighestBlockYAt((int) x, (int) z);
                location = new Location(world, x, y + 1, z);
                var block = location.getBlock();
                if ((block.getType().equals(Material.LAVA) || block.getType().equals(Material.FIRE))) {
                    return getRespawnLocation(deathLocation, death);
                } else if (block.getType().equals(Material.AIR) || block.isPassable()) {
                    var testLocation = decreaseYLocation(location);
                    var testBlock = testLocation.getBlock();
                    if (!testBlock.getType().equals(Material.LAVA) && !testBlock.getType().equals(Material.FIRE)) {
                        return location;
                    }
                }
                return getRespawnLocation(deathLocation, death);
            } else if (badBiomes.contains(deathBiome) && !oceanBiomes.contains(deathBiome) && world.getEnvironment().equals(World.Environment.NORMAL)) {
                    y = world.getHighestBlockYAt((int) x, (int) z);
                location = new Location(world, x, y + 1, z);
                var block = location.getBlock();
                if ((block.isLiquid() || block.getType().equals(Material.FIRE))) {
                    return getRespawnLocation(deathLocation, death);
                } else if (block.getType().equals(Material.AIR) || block.isPassable()) {
                    var testLocation = decreaseYLocation(location);
                    var testBlock = testLocation.getBlock();
                    if (!testBlock.isLiquid() && !testBlock.getType().equals(Material.FIRE)) {
                        return location;
                    }
                    return getRespawnLocation(deathLocation, death);
                }
            } else {
                    y = world.getHighestBlockYAt((int) x, (int) z);
                location = new Location(world, x, y + 1, z);
                var block = location.getBlock();
                if (block.isLiquid() || block.getType().equals(Material.FIRE) || badBiomes.contains(block.getBiome())) {
                    return getRespawnLocation(deathLocation, death);
                } else if (block.getType().equals(Material.AIR) || block.isPassable()) {
                    var testLocation = decreaseYLocation(location);
                    var testBlock = testLocation.getBlock();
                    if (!testBlock.isLiquid() && !testBlock.getType().equals(Material.FIRE)) {
                        return location;
                    }
                    return getRespawnLocation(deathLocation, death);
                }
            }
        }
        return getRespawnLocation(deathLocation, death);
    }

    public Location decreaseYLocation(Location location) {
        var nLocation = new Location(location.getWorld(), location.getX(), location.getY() - 1, location.getZ());
        var block = nLocation.getBlock();
        if (block.getType().equals(Material.AIR) || block.isPassable()) {
            var nLocation2 = new Location(nLocation.getWorld(), nLocation.getX(), nLocation.getY() - 1, nLocation.getZ());
            var nBlock = nLocation2.getBlock();
            if (nBlock.getType().equals(Material.AIR) || nBlock.isPassable()) {
                nLocation = decreaseYLocation(nLocation);
            }
        }
        return nLocation;
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

        @EventHandler(priority = EventPriority.HIGH)
        private void onPlayerRespawn(PlayerRespawnEvent event) {
            var player = (Player) event.getPlayer();
            player.sendMessage("Respawning...");
            var deathLocation = player.getLocation();
            var bedSpawnLocation = player.getBedSpawnLocation();
            if (bedSpawnLocation == null
                    || (deathLocation.getWorld().getEnvironment().equals(World.Environment.NETHER) && !Objects.equals(deathLocation.getWorld(), bedSpawnLocation.getWorld())
                    || (bedSpawnLocation.getWorld().getEnvironment().equals(World.Environment.NETHER)) && !player.getWorld().getEnvironment().equals(World.Environment.NETHER))) {
                var location = getRespawnLocation(player.getLocation(), true);
                if (location == null) {
                    return;
                }
                event.setRespawnLocation(location);
            } else {
                event.setRespawnLocation(player.getBedSpawnLocation());
            }
        }

        @EventHandler
        private void onPlayerJoin(PlayerJoinEvent event) {
            var player = (Player) event.getPlayer();
            if (!player.hasPlayedBefore()) {
                var world = Bukkit.getWorld("world");
                if (world == null) {
                    return;
                }
                var location = getRespawnLocation(world.getSpawnLocation(), false);
                if (location == null) {
                    return;
                }
                player.teleport(location);
            }
        }

        @EventHandler(priority = EventPriority.HIGH)
        private void onPlayerBedEnter(PlayerBedEnterEvent event) {
            var player = event.getPlayer();
            var bedSpawnLocation = player.getBedSpawnLocation();
            player.setBedSpawnLocation(event.getBed().getLocation());
            if (!event.getBed().getType().equals(Material.RESPAWN_ANCHOR)) {
                player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                player.sendMessage("Your sleep timer has been reset");
            }
            event.setCancelled(true);
        }
    }
}
