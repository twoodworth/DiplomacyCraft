package me.tedwoodworth.diplomacy.spawning;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnManager {

    private static SpawnManager instance = null;
    private File diplomacyPlayerConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "spawnChunks.yml");
    private YamlConfiguration config;
    private List<Biome> badBiomes = new ArrayList<>(0);

    private SpawnManager() {
        config = YamlConfiguration.loadConfiguration(diplomacyPlayerConfigFile);
        save();
    }

    public Chunk getSpawnChunk() {
        var chunks = getSpawnChunks();
        var index = (int) (Math.random() * chunks.size());
        return chunks.get(index);
    }

    public Chunk getRespawnChunk(Location location) {
        var validChunks = new ArrayList<Chunk>();
        for (var chunk : getSpawnChunks()) {
            var chunkLocation = new Location(chunk.getWorld(), chunk.getX(), location.getY(), chunk.getZ());
            if (Objects.equals(location.getWorld(), chunkLocation.getWorld()) && location.distanceSquared(chunkLocation) < 4000000) {
                validChunks.add(chunk);
            }
        }

        var index = (int) (Math.random() * validChunks.size());
        return validChunks.get(index);
    }

    public List<Chunk> getSpawnChunks() {
        List<Chunk> chunks = new ArrayList<>(0);
        var worlds = config.getKeys(false);
        for (var worldKey : worlds) {
            var world = Bukkit.getWorld(worldKey);
            var chunkMaps = config.getMapList(worldKey);
            for (var chunkMap : chunkMaps) {
                var x = Integer.parseInt(chunkMap.get("x").toString());
                var z = Integer.parseInt(chunkMap.get("z").toString());
                chunks.add(world.getChunkAt(x, z));
            }
        }
        return chunks;
    }

    public void addSpawnChunk(Chunk chunk) {
        var world = chunk.getWorld().getName();
        var x = chunk.getX();
        var z = chunk.getZ();

        var chunkMap = ImmutableMap.of(
                "x", x,
                "z", z
        );

        var worldChunks = config.getMapList(world);
        worldChunks.add(chunkMap);
        config.set(world, worldChunks);
    }

    public List<Biome> getBadBiomes() {
        if (badBiomes.size() == 0) {
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
        }
        return badBiomes;
    }

    public List<Biome> getOceanBiomes() {
        var oceanBiomes = new ArrayList<Biome>(0);
        oceanBiomes.add(Biome.COLD_OCEAN);
        oceanBiomes.add(Biome.DEEP_COLD_OCEAN);
        oceanBiomes.add(Biome.DEEP_FROZEN_OCEAN);
        oceanBiomes.add(Biome.DEEP_LUKEWARM_OCEAN);
        oceanBiomes.add(Biome.DEEP_OCEAN);
        oceanBiomes.add(Biome.DEEP_WARM_OCEAN);
        oceanBiomes.add(Biome.LUKEWARM_OCEAN);
        oceanBiomes.add(Biome.OCEAN);
        oceanBiomes.add(Biome.WARM_OCEAN);
        return oceanBiomes;
    }

    public List<Biome> getEndBiomes() {
        var endBiomes = new ArrayList<Biome>(0);
        endBiomes.add(Biome.END_BARRENS);
        endBiomes.add(Biome.END_HIGHLANDS);
        endBiomes.add(Biome.END_MIDLANDS);
        endBiomes.add(Biome.SMALL_END_ISLANDS);
        endBiomes.add(Biome.THE_END);
        return endBiomes;
    }

    public List<Biome> getNetherBiomes() {
        var netherBiomes = new ArrayList<Biome>(0);
        netherBiomes.add(Biome.BASALT_DELTAS);
        netherBiomes.add(Biome.CRIMSON_FOREST);
        netherBiomes.add(Biome.NETHER_WASTES);
        netherBiomes.add(Biome.SOUL_SAND_VALLEY);
        netherBiomes.add(Biome.WARPED_FOREST);
        return netherBiomes;
    }

    public Location getSpawnLocation() {

        var chunks = getSpawnChunks();
        if (chunks.size() == 0) {
            return Bukkit.getWorld("world").getSpawnLocation();
        }

        var x = Math.random() * 16;
        var z = Math.random() * 16;
        var chunk = getSpawnChunk();
        var location = new Location(chunk.getWorld(), chunk.getX() * 16 + x, 63.0, chunk.getZ() * 16 + z);
        var block = location.getBlock();
        var biome = block.getBiome();
        var isBadBiome = false;
        for (var badBiome : this.getBadBiomes()) {
            if (Objects.equals(biome, badBiome)) {
                isBadBiome = true;
                break;
            }
        }

        if (block.isLiquid() || block.getType().equals(Material.FIRE) || isBadBiome) {
            location = getSpawnLocation();
        } else if (block.getType().equals(Material.AIR) || block.isPassable()) {
            var testLocation = decreaseYLocation(location);
            var testBlock = testLocation.getBlock();
            if (testBlock.isLiquid() || testBlock.getType().equals(Material.FIRE)) {
                location = getSpawnLocation();
            } else {
                location = testLocation;
            }
        } else {
            location = increaseYLocation(location);
        }
        return location;
    }

    public Location getRespawnLocation(Location deathLocation) {
        var deathBiome = deathLocation.getBlock().getBiome();
        if (getEndBiomes().contains(deathBiome)) {
            return getSpawnLocation();
        }

        var chunk = getRespawnChunk(deathLocation);
        var x = Math.random() * 16;
        var z = Math.random() * 16;
        Location location;

        if (getNetherBiomes().contains(deathBiome)) {
            var y = Math.random() * 89 + 32;
            location = new Location(chunk.getWorld(), chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z);
            var block = location.getBlock();

            if (block.isLiquid() || block.getType().equals(Material.FIRE) || block.getType().equals(Material.MAGMA_BLOCK)) {
                location = getRespawnLocation(deathLocation);
            } else if (block.getType().equals(Material.AIR) || block.isPassable()) {
                var testLocation = decreaseYLocation(location);
                var testBlock = testLocation.getBlock();
                if (testBlock.isLiquid() || testBlock.getType().equals(Material.FIRE) || block.getType().equals(Material.MAGMA_BLOCK)) {
                    location = getRespawnLocation(deathLocation);
                } else {
                    location = testLocation;
                }
            }
        } else if (getOceanBiomes().contains(deathBiome)) {
            location = new Location(chunk.getWorld(), chunk.getX() * 16 + x, 63, chunk.getZ() * 16 + z);
            var block = location.getBlock();
            if ((block.getType().equals(Material.LAVA) || block.getType().equals(Material.FIRE))) {
                location = getRespawnLocation(deathLocation);
            } else if (block.getType().equals(Material.AIR) || block.isPassable()) {
                var testLocation = decreaseYLocation(location);
                var testBlock = testLocation.getBlock();
                if (testBlock.getType().equals(Material.LAVA) || testBlock.getType().equals(Material.FIRE)) {
                    location = getSpawnLocation();
                } else {
                    location = testLocation;
                }
            } else {
                location = increaseYLocation(location);
            }
        } else if (getBadBiomes().contains(deathBiome) && !getOceanBiomes().contains(deathBiome) && !getEndBiomes().contains(deathBiome) && !getNetherBiomes().contains(deathBiome)) {
            location = new Location(chunk.getWorld(), chunk.getX() * 16 + x, 63, chunk.getZ() * 16 + z);
            var block = location.getBlock();
            if ((block.isLiquid() || block.getType().equals(Material.FIRE))) {
                location = getRespawnLocation(deathLocation);
            } else if (block.getType().equals(Material.AIR) || block.isPassable()) {
                var testLocation = decreaseYLocation(location);
                var testBlock = testLocation.getBlock();
                if (testBlock.isLiquid() || testBlock.getType().equals(Material.FIRE)) {
                    location = getSpawnLocation();
                } else {
                    location = testLocation;
                }
            } else {
                location = increaseYLocation(location);
                return location;
            }
        } else {
            location = new Location(chunk.getWorld(), chunk.getX() * 16 + x, 63, chunk.getZ() * 16 + z);
            var block = location.getBlock();
            if (block.isLiquid() || block.getType().equals(Material.FIRE) || getBadBiomes().contains(block.getBiome())) {
                location = getRespawnLocation(deathLocation);
            } else if (block.getType().equals(Material.AIR) || block.isPassable()) {
                var testLocation = decreaseYLocation(location);
                var testBlock = testLocation.getBlock();
                if (testBlock.isLiquid() || testBlock.getType().equals(Material.FIRE)) {
                    location = getRespawnLocation(deathLocation);
                } else {
                    location = testLocation;
                }
            } else {
                location = increaseYLocation(location);
            }
        }
        return location;
    }

    public Location increaseYLocation(Location location) {
        var nLocation = new Location(location.getWorld(), location.getX(), location.getY() + 1, location.getZ());
        var block = nLocation.getBlock();
        if (block.getType().equals(Material.AIR) || block.isPassable()) {
            return nLocation;
        } else {
            return increaseYLocation(nLocation);
        }
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

    public void save() {
        try {
            config.save(diplomacyPlayerConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SpawnManager getInstance() {
        if (instance == null) {
            instance = new SpawnManager();
        }
        return instance;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new SpawnManager.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        private void onPlayerMove(PlayerMoveEvent event) {
            var chunk = event.getTo().getChunk();

            if (!getSpawnChunks().contains(chunk)) {
                addSpawnChunk(chunk);
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        private void onPlayerRespawn(PlayerRespawnEvent event) {
            var player = (Player) event.getPlayer();
            if (player.getBedSpawnLocation() == null
                    || (getNetherBiomes().contains(player.getLocation().getBlock().getBiome()) && !Objects.equals(player.getWorld(), player.getBedSpawnLocation().getWorld()))
                    || (getNetherBiomes().contains(player.getBedSpawnLocation().getBlock().getBiome()) && !getNetherBiomes().contains(player.getLocation().getBlock().getBiome()))) {
                event.setRespawnLocation(getRespawnLocation(player.getLocation()));
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        private void onPlayerLogin(PlayerLoginEvent event) {
            var player = event.getPlayer();
            if (!player.hasPlayedBefore()) {
                player.teleport(getSpawnLocation());
            }
        }
    }
}
