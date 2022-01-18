package me.tedwoodworth.diplomacy.geology;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;

import java.util.ArrayList;
import java.util.Arrays;

public class RandomTicker implements Runnable {
    private final Diplomacy plugin = Diplomacy.getInstance();

    @Override
    public void run() {
        var overworld = WorldManager.getInstance().getOverworld();
        var loaded = new ArrayList<>(Arrays.asList(overworld.getLoadedChunks()));
        var online = Bukkit.getOnlinePlayers();
        for (var chunk : overworld.getForceLoadedChunks()) {
            loaded.remove(chunk);
        }

        for (int j = 0; j < 3; j++) {
            for (var ignored : online) {
                var r = (int) (Math.random() * loaded.size());
                var chunk = loaded.remove(r);
                if (DiplomacyChunks.getInstance().getDiplomacyChunk(chunk).getNation() != null) continue;
                for (int i = 0; i < 50; i++) {
                    var x = (int) (Math.random() * 16);
                    var y = (int) (Math.random() * overworld.getMaxHeight());
                    var z = (int) (Math.random() * 16);

                    var block = chunk.getBlock(x, y, z);
                    task(block);
                }
            }
        }
        Bukkit.getScheduler().runTaskLater(plugin, this, 2L);
    }

    private void task(Block block) {
        var world = block.getWorld();
        if (world.equals(WorldManager.getInstance().getSpawn())) return;
        var type = block.getType();
        switch (type) {
            case COBBLESTONE -> {
                var d = block.getRelative(BlockFace.DOWN);
                if ((d.isLiquid() || d.isPassable())) {
                    var state = block.getState();
                    block.setType(Material.AIR, true);
                    var loc = block.getLocation();
                    loc.setX(loc.getX() + 0.5);
                    loc.setZ(loc.getZ() + 0.5);
                    block.getWorld().spawnFallingBlock(loc, state.getBlockData());
                }
            }
            case WATER -> {
                var loc = block.getLocation();
                var levelled = ((Levelled) block.getBlockData());
                var u = block.getRelative(BlockFace.UP);
                if (loc.getY() > 33 && levelled.getLevel() == 0 && u.isPassable()) {
                    levelled.setLevel(1);
                    block.setBlockData(levelled, true);
                }
            }
            case LAVA -> {
                var loc = block.getLocation();
                var levelled = ((Levelled) block.getBlockData());
                if (loc.getY() > 33 && levelled.getLevel() == 0) {
                    block.setType(Material.BLACKSTONE, true);
                }
            }
            case GRASS_BLOCK -> {
                var u = block.getRelative(BlockFace.UP);
                if (u.getType().isAir()) {
                    u.setType(Material.GRASS, true);
                }
            }
            case GRASS -> {
                var u = block.getRelative(BlockFace.UP);
                if (u.getType().isAir()) {
                    block.setType(Material.TALL_GRASS, true);
                }
            }
        }
    }
}
