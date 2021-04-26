package me.tedwoodworth.diplomacy.geology;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.HashSet;

public class RandomBlockTicker {
    private static RandomBlockTicker instance = null;
    private final GeoData geoData = GeoData.getInstance();
    private final int BLOCKS_PER_TICK = (int) (.0000076 * Math.pow(geoData.WORLD.getWorldBorder().getSize(), 2) * 256 * 16) + 1;
    private final BukkitScheduler SCHEDULER = Bukkit.getScheduler();
    private final Diplomacy DIPLOMACY = Diplomacy.getInstance();


    public static RandomBlockTicker getInstance() {
        if (instance == null) {
            instance = new RandomBlockTicker();
        }
        return instance;
    }

    private RandomBlockTicker() {
        onRandomBlockTick();
    }


    //todo plate shifting
    // each block will have a angle (0 - 2pi) and speed (0-1) associated with it
    // On a random tick, solid blocks have a 1 / (338688000 * speed * 100) chance of shifting.
    // upon shifting, the block will have its x and z components of its angle determined, each with a max value of 1.
    // for each component, if Math.random() is less than the absolute value of the component, the block will move in that direction.
    // if Math.random() is greater for both directions, then nothing will happen.
    // The same thing will then happen 1 tick later for the block that will be in the location that the current block is taking.
    // this will keep happening until the next block belongs to a different plate or the next block is air/water/lava.
    // if the next block is a different plate, the current and next block will be compared. If the next block is more dense, it (and
    // all the blocks below it) will get pushed down. If less dense, it will get pushed up. If equal, it is a 50/50 chance
    private void onRandomBlockTick() {
        for (int i = 0; i < BLOCKS_PER_TICK * geoData.tickSpeed; i++) {
            var block = getRandomBlock();
            var type = block.getType();
            switch (type) {
                case WHEAT -> {
                    // wheat grows
                }
                case BEETROOTS -> {
                    // beetroots grow
                }
                case CARROTS -> {
                    // carrots grow
                }
                case POTATO -> {
                    // potatoes grow
                }
                case MELON_STEM -> {
                    // melon stem grows
                }
                case PUMPKIN_STEM -> {
                    // pumpkin stem grows
                }
                case FARMLAND -> {
                    // farmland becomes damp or dries out
                }
                case BAMBOO -> {
                    // bamboo grows
                }
                case COCOA_BEANS -> {
                    // Cocoa beans grow
                }
                case SUGAR_CANE -> {
                    // sugar cane grows
                }
                case SWEET_BERRIES -> {
                    // sweet berries grow
                }
                case CACTUS -> {
                    // cactus grows
                }
                case RED_MUSHROOM -> {
                    // red mushroom spreads or grows
                }
                case BROWN_MUSHROOM -> {
                    // brown mushroom spreads or grows
                }
                case KELP -> {
                    // kelp grows if on top
                }
                case SEA_PICKLE -> {
                    // sea pickle spreads
                }
                case NETHER_WART_BLOCK -> {
                    // nether wart grows
                }
                case CHORUS_FLOWER -> {
                    // chorus flower grows
                }
                case CRIMSON_FUNGUS -> {
                    // crimson fungi grows
                }
                case WARPED_FUNGUS -> {
                    // warped fungi grows
                }
                case VINE -> {

                }
                case FIRE -> {

                }
                case ICE -> {

                }
                case BLUE_ICE -> {

                }
                case PACKED_ICE -> {

                }
                case SNOW -> {

                }
                case SNOW_BLOCK -> {

                }
                case ACACIA_LEAVES -> {

                }
                case BIRCH_LEAVES -> {

                }
                case DARK_OAK_LEAVES -> {

                }
                case JUNGLE_LEAVES -> {

                }
                case OAK_LEAVES -> {

                }
                case SPRUCE_LEAVES -> {

                }
                case GRASS_BLOCK -> {

                }
                case MYCELIUM -> {

                }
                case ACACIA_SAPLING -> {

                }
                case BAMBOO_SAPLING -> {

                }
                case BIRCH_SAPLING -> {

                }
                case DARK_OAK_SAPLING -> {

                }
                case JUNGLE_SAPLING -> {

                }
                case OAK_SAPLING -> {

                }
                case LAVA -> {
                    //todo lava ignites something
                    var temp = geoData.getSubchunkTemperature(new SubChunk(block));
                    if (temp < 470) {
                        if (temp < 280) {
                            block.setType(Material.OBSIDIAN);
                            break;
                        }
                        var up = geoData.getRelativeBlock(BlockFace.UP, block);
                        var down = geoData.getRelativeBlock(BlockFace.DOWN, block);
                        var north = geoData.getRelativeBlock(BlockFace.NORTH, block);
                        var south = geoData.getRelativeBlock(BlockFace.SOUTH, block);
                        var east = geoData.getRelativeBlock(BlockFace.EAST, block);
                        var west = geoData.getRelativeBlock(BlockFace.WEST, block);

                        int count = 0;
                        if (up != null && up.getType() != Material.LAVA) count++;
                        if (down != null && down.getType() != Material.LAVA) count++;
                        if (north.getType() != Material.LAVA) count++;
                        if (south.getType() != Material.LAVA) count++;
                        if (east.getType() != Material.LAVA) count++;
                        if (west.getType() != Material.LAVA) count++;

                        if (count > 1) {
                            block.setType(Material.MAGMA_BLOCK);
                        } else if (count == 1) {
                            if (Math.random() < Math.pow(1 - (temp / 473.0), 2)) block.setType(Material.MAGMA_BLOCK);
                        }
                    }

                }
                case MAGMA_BLOCK -> {
                    var temp = geoData.getSubchunkTemperature(new SubChunk(block));
                    if (temp > 490) {
                        block.setType(Material.LAVA);
                    } else if (temp < 460) {
                        var up = geoData.getRelativeBlock(BlockFace.UP, block);
                        var down = geoData.getRelativeBlock(BlockFace.DOWN, block);
                        var north = geoData.getRelativeBlock(BlockFace.NORTH, block);
                        var south = geoData.getRelativeBlock(BlockFace.SOUTH, block);
                        var east = geoData.getRelativeBlock(BlockFace.EAST, block);
                        var west = geoData.getRelativeBlock(BlockFace.WEST, block);

                        var list = new ArrayList<>();
                        if (up == null) list.add(null);
                        else list.add(up.getType());
                        if (down == null) list.add(null);
                        else list.add(down.getType());
                        list.add(north.getType());
                        list.add(south.getType());
                        list.add(east.getType());
                        list.add(west.getType());


                        if (list.contains(Material.LAVA)) break;

                        var magmacount = 0;
                        for (var item : list) if (item == Material.MAGMA_BLOCK) magmacount++;

                        if (list.contains(Material.AIR)) {
                            if (magmacount >= 3) {
                                block.setType(Material.BLACKSTONE);
                            } else {
                                block.setType(Material.ANDESITE);
                            }
                        } else if (list.contains(Material.BLACKSTONE) || list.contains(Material.OBSIDIAN)) {
                            if (Math.random() < 0.3) block.setType(Material.BLACKSTONE);
                            else block.setType(Material.ANDESITE);
                        } else if (list.contains(Material.ANDESITE) && Math.random() < 0.43) {
                            block.setType(Material.ANDESITE);
                        } else {
                            block.setType(Material.DIORITE);
                        }
                    }
                }
                case OBSIDIAN, BLACKSTONE, ANDESITE, DIORITE -> {
                    if (geoData.getSubchunkTemperature(new SubChunk(block)) > 480) {
                        block.setType(Material.MAGMA_BLOCK);
                    }
                }
                case REDSTONE_ORE -> {
                    // turns off
                }
                case TURTLE_EGG -> {
                    // crack or hatch
                }
                case CAMPFIRE -> {
                    // smoke appears
                }
                case SOUL_CAMPFIRE -> {
                    // smoke appears
                }

            }
        }
        SCHEDULER.runTaskLater(DIPLOMACY, this::onRandomBlockTick, 1L);
    }

    private Block getRandomBlock() {
        return geoData.WORLD.getBlockAt(
                (int) (Math.random() * geoData.WORLD_SIZE) + geoData.MIN_XZ,
                (int) (Math.random() * geoData.MAX_Y),
                (int) (Math.random() * geoData.WORLD_SIZE) + geoData.MIN_XZ
        );
    }
}
