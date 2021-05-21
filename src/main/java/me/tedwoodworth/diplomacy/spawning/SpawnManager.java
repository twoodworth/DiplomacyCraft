package me.tedwoodworth.diplomacy.spawning;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.geology.WorldManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SpawnManager {

    private static SpawnManager instance = null;
    private final List<Biome> badBiomes;
    private final Location[] respawnLocations = new Location[40];

    private SpawnManager() {
        this.badBiomes = new ArrayList<>();
        badBiomes.add(Biome.COLD_OCEAN);
        badBiomes.add(Biome.DEEP_COLD_OCEAN);
        badBiomes.add(Biome.DEEP_FROZEN_OCEAN);
        badBiomes.add(Biome.DEEP_LUKEWARM_OCEAN);
        badBiomes.add(Biome.DEEP_OCEAN);
        badBiomes.add(Biome.DEEP_WARM_OCEAN);
        badBiomes.add(Biome.LUKEWARM_OCEAN);
        badBiomes.add(Biome.OCEAN);
        badBiomes.add(Biome.BEACH);
        badBiomes.add(Biome.WARM_OCEAN);
        badBiomes.add(Biome.DESERT);
        badBiomes.add(Biome.DESERT_LAKES);
        badBiomes.add(Biome.DESERT_HILLS);
        badBiomes.add(Biome.RIVER);
        badBiomes.add(Biome.FROZEN_RIVER);
        badBiomes.add(Biome.BADLANDS_PLATEAU);
        badBiomes.add(Biome.BADLANDS);
        badBiomes.add(Biome.MODIFIED_BADLANDS_PLATEAU);
        badBiomes.add(Biome.MODIFIED_WOODED_BADLANDS_PLATEAU);
        badBiomes.add(Biome.WOODED_BADLANDS_PLATEAU);

        System.out.println("[Diplomacy] 0/40 Random locations initialized");
        for (int i = 0; i < respawnLocations.length; i++) {
            var location = getNewLocation();
            while (location == null) {
                location = getNewLocation();
            }
            respawnLocations[i] = location;
            System.out.println("[Diplomacy] " + (i + 1) + "/40 Random locations initialized");
        }
        System.out.println("[Diplomacy] Initialized random teleportation");
    }

    private Location getNewLocation() {
        var x = (int) (Math.random() * 16384);
        var z = (int) (Math.random() * 16384);
        var loc = new Location(WorldManager.getInstance().getOverworld(), x, 255, z);
        var y = getMaxYHeight(loc);
        if (y == -1 || y > 65 || badBiomes.contains(loc.getBlock().getBiome())) {
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
        private void onSpawn(CreatureSpawnEvent event) {
            var entity = event.getEntity();
            if (entity instanceof Villager || entity instanceof ZombieVillager) {
                event.setCancelled(true);
            } else if (Objects.equals(event.getLocation().getWorld(), WorldManager.getInstance().getSpawn())) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void foodLevelChange(FoodLevelChangeEvent event) {
            var entity = event.getEntity();
            if (entity.getWorld().equals(WorldManager.getInstance().getSpawn())) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onPlayerPortal(PlayerPortalEvent event) {
            var player = event.getPlayer();
            var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            var lives = dp.getLives();
            if (lives == 0) {
                player.sendMessage(ChatColor.RED + "You are out of lives and cannot enter. Vote or wait until tomorrow begins to get more lives.");
                event.setTo(WorldManager.getInstance().getSpawn().getSpawnLocation());
                return;
            }
            var from = event.getFrom();
            if (Objects.equals(from.getWorld(), WorldManager.getInstance().getSpawn())) {
                var last = dp.getLastLocation();
                if (last != null) {
                    event.setTo(last);
                    player.sendMessage(ChatColor.GOLD + "Teleporting to your last location...");
                    return;
                }
                var bed = player.getBedSpawnLocation();
                if (bed != null) {
                    event.setTo(bed);
                    player.sendMessage(ChatColor.GOLD + "Teleporting to your bed/anchor location...");
                    return;
                }
                var i = (int) (Math.random() * respawnLocations.length);
                var loc = respawnLocations[i];
                event.setTo(loc);
                player.sendMessage(ChatColor.GOLD + "No bed/anchor found. Teleporting to a random location...");
                Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> replaceLocation(i));
            }
        }

        @EventHandler
        private void onEntityDamage(EntityDamageEvent event) {
            var entity = event.getEntity();
            if (entity.getWorld().equals(WorldManager.getInstance().getSpawn())) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onPlayerMove(PlayerMoveEvent event) {
            var player = event.getPlayer();
            if (player.getWorld().equals(WorldManager.getInstance().getSpawn()) && player.getLocation().getY() < -20) {
                player.teleport(WorldManager.getInstance().getSpawn().getSpawnLocation());
            }
        }

        @EventHandler
        private void onPlayerDropItem(PlayerDropItemEvent event) {
            var player = event.getPlayer();
            if (player.getWorld().equals(WorldManager.getInstance().getSpawn()) && player.getGameMode() != GameMode.CREATIVE) {
                player.sendMessage(ChatColor.RED + "You cannot do that here.");
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onPlayerInteract(PlayerInteractEvent event) {
            var player = event.getPlayer();
            var block = event.getClickedBlock();
            var item = event.getItem();

            if (player.getWorld().equals(WorldManager.getInstance().getSpawn()) && player.getGameMode() != GameMode.CREATIVE) {
                if (block != null) {
                    var blockType = block.getType();
                    if (blockType == Material.REPEATER || blockType == Material.ITEM_FRAME || blockType == Material.BEACON || blockType == Material.SPRUCE_DOOR) {
                        event.setCancelled(true);
                        player.sendMessage(ChatColor.RED + "You cannot use this.");
                        return;
                    }
                }
                if (item != null) {
                    if (!item.getType().isEdible()) {
                        player.sendMessage(ChatColor.RED + "You cannot use this here.");
                        event.setCancelled(true);
                    }
                }
            }
        }

        @EventHandler
        private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
            var player = event.getPlayer();
            if (player.getWorld().equals(WorldManager.getInstance().getSpawn()) && player.getGameMode() != GameMode.CREATIVE) {
                player.sendMessage(ChatColor.RED + "You cannot do that here.");
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
            var player = event.getPlayer();
            if (player.getWorld().equals(WorldManager.getInstance().getSpawn()) && player.getGameMode() != GameMode.CREATIVE) {
                player.sendMessage(ChatColor.RED + "You cannot use that here.");
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onPlayerBucketFill(PlayerBucketFillEvent event) {
            var player = event.getPlayer();
            if (player.getWorld().equals(WorldManager.getInstance().getSpawn()) && player.getGameMode() != GameMode.CREATIVE) {
                player.sendMessage(ChatColor.RED + "You cannot use that here.");
                event.setCancelled(true);
            }
        }

    }
}
