package me.tedwoodworth.diplomacy.geology;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.data.FloatArrayPersistentDataType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GeoData {
    private static GeoData instance = null;
    public final NamespacedKey tempKey = new NamespacedKey(Diplomacy.getInstance(), "temp");
    public final World WORLD = Bukkit.getWorld("world");
    public final int MAX_XZ = (int) (WORLD.getWorldBorder().getSize()) / 2;
    public final int MAX_Y = WORLD.getMaxHeight();
    public int tickSpeed = 1;

    public static GeoData getInstance() {
        if (instance == null) {
            instance = new GeoData();
        }
        return instance;
    }

    private GeoData() {
    }

    public void setTickSpeed(int speed) {
        tickSpeed = speed;
    }

    public int getSunTemperature(SubChunk subchunk) {
        var time = WORLD.getFullTime();
        var days = (int) (time / 24000L) + 1;
        var dayOfYear = days % 2016;
        var sunAngleYear = Math.sin(2.0 * Math.PI / 2016 * dayOfYear) * 0.436332313;
        var sunAngleDay = Math.max(0, Math.sin(2.0 * Math.PI / 24000 * WORLD.getTime()) * 0.436332313);
        var sunAngle = Math.min(sunAngleYear, sunAngleDay);
        var length = MAX_XZ * 2;
        var chunkLat = subchunk.getBlock(0, 0, 0).getZ();
        var percentSun = Math.max(0, Math.cos(Math.PI * chunkLat / length + sunAngle));
        return (int) (213 + 105 * percentSun);
    }

    public boolean receivesSunlight(Block block) {
        var up = getRelativeBlock(BlockFace.UP, block);
        return up == null || receivesSunlight(up);
    }

    public boolean receivesSunlight(SubChunk subChunk) {
        var block = subChunk.getBlock((int) (Math.random() * 16), 15, (int) (Math.random() * 16));
        var up = getRelativeBlock(BlockFace.UP, block);
        return up == null || receivesSunlight(up);
    }

    @Nullable
    public Block getRelativeBlock(BlockFace relative, Block block) {
        switch (relative) {
            case UP -> {
                if (block.getLocation().getY() == MAX_Y)
                    return null;
            }
            case DOWN -> {
                if (block.getLocation().getY() == 0)
                    return null;
            }
            case NORTH -> {
                if (block.getLocation().getZ() == -MAX_XZ) {
                    return (new Location(WORLD, block.getX(), block.getY(), MAX_XZ)).getBlock();
                }
            }
            case SOUTH -> {
                if (block.getLocation().getZ() == MAX_XZ) {
                    return (new Location(WORLD, block.getX(), block.getY(), -MAX_XZ)).getBlock();
                }
            }
            case EAST -> {
                if (block.getLocation().getX() == MAX_XZ) {
                    return (new Location(WORLD, -MAX_XZ, block.getY(), block.getZ())).getBlock();
                }
            }
            case WEST -> {
                if (block.getLocation().getX() == -MAX_XZ) {
                    return (new Location(WORLD, MAX_XZ, block.getY(), block.getZ())).getBlock();
                }
            }
        }
        return block.getRelative(relative);
    }

    public int getSubchunkTemperature(SubChunk subChunk) {
        var data = ((PersistentDataHolder) subChunk.getChunk()).getPersistentDataContainer();
        var temperatures = data.get(tempKey, PersistentDataType.INTEGER_ARRAY);
        if (temperatures == null) {
            temperatures = new int[16];
            Arrays.fill(temperatures, 500);
        }
        return temperatures[subChunk.getSubSection()];
    }

    public void setSubchunkTemperature(SubChunk subChunk, int temperature) {
        var data = ((PersistentDataHolder) subChunk.getChunk()).getPersistentDataContainer();
        var temperatures = data.get(tempKey, PersistentDataType.INTEGER_ARRAY);
        if (temperatures == null) {
            temperatures = new int[16];
            Arrays.fill(temperatures, 500);
        }
        temperatures[subChunk.getSubSection()] = temperature;
        data.set(tempKey, PersistentDataType.INTEGER_ARRAY, temperatures);
    }
}
