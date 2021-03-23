package me.tedwoodworth.diplomacy.geology;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitScheduler;

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
                case OAK_SAPLING ->  {

                }
                case LAVA -> {
                    //todo lava ignites something
                    var temp = geoData.getSubchunkTemperature(new SubChunk(block));
                    if (temp < 473) {
                        var up = geoData.getRelativeBlock(BlockFace.UP, block);
                        var down = geoData.getRelativeBlock(BlockFace.DOWN, block);
                        var north = geoData.getRelativeBlock(BlockFace.NORTH, block);
                        var south = geoData.getRelativeBlock(BlockFace.SOUTH, block);
                        var east = geoData.getRelativeBlock(BlockFace.EAST, block);
                        var west = geoData.getRelativeBlock(BlockFace.WEST, block);

                        int count = 1;
                        if (up != null && up.getType() != Material.LAVA) count += 2000;
                        if (down != null && down.getType() != Material.LAVA) count += 2000;
                        if (north.getType() != Material.LAVA) count += 2000;
                        if (south.getType() != Material.LAVA) count += 2000;
                        if (east.getType() != Material.LAVA) count += 2000;
                        if (west.getType() != Material.LAVA) count += 2000;
                        if (Math.random() < count / 6000.0) block.setType(Material.MAGMA_BLOCK);
                    }

                }
                case MAGMA_BLOCK -> {
                    var temp = geoData.getSubchunkTemperature(new SubChunk(block));
                    if (temp > 473) {
                        block.setType(Material.LAVA);
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
                (int) (Math.random() * geoData.MAX_XZ * 2) - geoData.MAX_XZ,
                (int) (Math.random() * geoData.MAX_Y),
                (int) (Math.random() * geoData.MAX_XZ * 2) - geoData.MAX_XZ
        );
    }
}
