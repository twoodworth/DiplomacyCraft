package me.tedwoodworth.diplomacy.geology;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.scheduler.BukkitScheduler;

public class RandomSubchunkTicker {
    private final int SUBCHUNKS_PER_TICK = (int) (.000045 * Math.pow(Bukkit.getWorld("world").getWorldBorder().getSize() / 16, 2)) * 64 + 1;
    private static RandomSubchunkTicker instance = null;
    private final GeoData geoData = GeoData.getInstance();
    private final BukkitScheduler SCHEDULER = Bukkit.getScheduler();
    private final Diplomacy DIPLOMACY = Diplomacy.getInstance();


    public static RandomSubchunkTicker getInstance() {
        if (instance == null) {
            instance = new RandomSubchunkTicker();
        }
        return instance;
    }

    private RandomSubchunkTicker() {
        onRandomSubchunkTick();
    }


    private void onRandomSubchunkTick() {
        for (int i = 0; i < SUBCHUNKS_PER_TICK * geoData.tickSpeed; i++) {
            var subchunk = getRandomSubchunk();
            var r = (int) (Math.random() * 6);
            switch (r) {
                case 0 -> {
                    var rel = subchunk.getRelative(BlockFace.UP);
                    if (rel != null) {
                        var temp1 = geoData.getSubchunkTemperature(rel);
                        var temp2 = geoData.getSubchunkTemperature(subchunk);
                        if (Math.random() < 0.05) temp2--;
                        geoData.setSubchunkTemperature(rel, (temp1 * 7 + temp2) / 8);
                        if (geoData.receivesSunlight(subchunk)) {
                            var temp3 = geoData.getSunTemperature(subchunk);
                            if (temp3 > temp2 && temp3 > temp1) {
                                geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2 + temp3) / 9);
                            } else {
                                geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                            }
                        } else {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                        }
                    } else {
                        geoData.setSubchunkTemperature(subchunk, (geoData.getSubchunkTemperature(subchunk) * 3 + 200) / 4);
                    }
                }
                case 1 -> {
                    var rel = subchunk.getRelative(BlockFace.DOWN);
                    if (rel != null) {
                        var temp1 = geoData.getSubchunkTemperature(rel);
                        var temp2 = geoData.getSubchunkTemperature(subchunk);
                        if (Math.random() < 0.05) temp2--;
                        geoData.setSubchunkTemperature(rel, (temp1 * 7 + temp2) / 8);
                        if (geoData.receivesSunlight(subchunk)) {
                            var temp3 = geoData.getSunTemperature(subchunk);
                            if (temp3 > temp2 && temp3 > temp1) {
                                geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2 + temp3) / 9);
                            } else {
                                geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                            }
                        } else {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                        }
                    } else {
//                        geoData.setSubchunkTemperature(subchunk, (geoData.getSubchunkTemperature(subchunk) * 3 + 500) / 4);
                        // todo add sub-world in which this will be applied
                    }
                }
                case 2 -> {
                    var rel = subchunk.getRelative(BlockFace.NORTH);
                    var temp1 = geoData.getSubchunkTemperature(rel);
                    var temp2 = geoData.getSubchunkTemperature(subchunk);
                    if (Math.random() < 0.05) temp2--;
                    geoData.setSubchunkTemperature(rel, (temp1 * 7 + temp2) / 8);
                    if (geoData.receivesSunlight(subchunk)) {
                        var temp3 = geoData.getSunTemperature(subchunk);
                        if (temp3 > temp2 && temp3 > temp1) {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2 + temp3) / 9);
                        } else {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                        }
                    } else {
                        geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                    }
                }
                case 3 -> {
                    var rel = subchunk.getRelative(BlockFace.SOUTH);
                    var temp1 = geoData.getSubchunkTemperature(rel);
                    var temp2 = geoData.getSubchunkTemperature(subchunk);
                    if (Math.random() < 0.05) temp2--;
                    geoData.setSubchunkTemperature(rel, (temp1 * 7 + temp2) / 8);
                    if (geoData.receivesSunlight(subchunk)) {
                        var temp3 = geoData.getSunTemperature(subchunk);
                        if (temp3 > temp2 && temp3 > temp1) {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2 + temp3) / 9);
                        } else {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                        }
                    } else {
                        geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                    }
                }
                case 4 -> {
                    var rel = subchunk.getRelative(BlockFace.EAST);
                    var temp1 = geoData.getSubchunkTemperature(rel);
                    var temp2 = geoData.getSubchunkTemperature(subchunk);
                    if (Math.random() < 0.05) temp2--;
                    geoData.setSubchunkTemperature(rel, (temp1 * 7 + temp2) / 8);
                    if (geoData.receivesSunlight(subchunk)) {
                        var temp3 = geoData.getSunTemperature(subchunk);
                        if (temp3 > temp2 && temp3 > temp1) {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2 + temp3) / 9);
                        } else {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                        }
                    } else {
                        geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                    }
                }
                case 5 -> {
                    var rel = subchunk.getRelative(BlockFace.WEST);
                    var temp1 = geoData.getSubchunkTemperature(rel);
                    var temp2 = geoData.getSubchunkTemperature(subchunk);
                    if (Math.random() < 0.05) temp2--;
                    geoData.setSubchunkTemperature(rel, (temp1 * 7 + temp2) / 8);
                    if (geoData.receivesSunlight(subchunk)) {
                        var temp3 = geoData.getSunTemperature(subchunk);
                        if (temp3 > temp2 && temp3 > temp1) {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2 + temp3) / 9);
                        } else {
                            geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                        }
                    } else {
                        geoData.setSubchunkTemperature(subchunk, (temp1 * 7 + temp2) / 8);
                    }
                }
            }
        }
        SCHEDULER.runTaskLater(DIPLOMACY, this::onRandomSubchunkTick, 1L);
    }

    private SubChunk getRandomSubchunk() {
        return new SubChunk(
                geoData.WORLD.getBlockAt(
                        (int) (Math.random() * geoData.WORLD_SIZE) + geoData.MIN_XZ,
                        (int) (Math.random() * geoData.MAX_Y),
                        (int) (Math.random() * geoData.WORLD_SIZE) + geoData.MIN_XZ
                )
        );
    }

}
