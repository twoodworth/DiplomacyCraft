package me.tedwoodworth.diplomacy.geology;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Nullable;

public class SubChunk {
    private final World world = Bukkit.getWorld("world");
    private final Chunk chunk;
    private final int subSection;

    public SubChunk(Chunk chunk, int subSection) {
        this.chunk = chunk;
        this.subSection = subSection;
    }

    public SubChunk(Block block) {
        this.chunk = block.getChunk();
        this.subSection = block.getY() / 16;
    }

    public SubChunk(Location location) {
        this.chunk = location.getChunk();
        this.subSection = (int) location.getY() / 16;
    }

    public SubChunk(int x, int y, int z) {
        this.chunk = world.getChunkAt(x, z);
        this.subSection = y;
    }

    public Block getBlock(int x, int y, int z) throws IndexOutOfBoundsException {
        if (x >= 16 || y >= 16 || z >= 16) throw new IndexOutOfBoundsException("x, y, and z must be 16 or less");
        return chunk.getBlock(x, y + subSection * 16, z);
    }

    public Chunk getChunk() {
        return chunk;
    }

    public int getSubSection() {
        return subSection;
    }

    @Nullable
    public SubChunk getRelative(BlockFace relative) {
        switch (relative) {
            case UP -> {
                if (subSection == 15) {
                    return null;
                } else {
                    return new SubChunk(chunk, subSection + 1);
                }
            }
            case DOWN -> {
                if (subSection == 0) {
                    return null;
                } else {
                    return new SubChunk(chunk, subSection - 1);
                }
            }
            case NORTH -> {
                if (chunk.getZ() == -GeoData.getInstance().MAX_XZ / 16) {
                    var block = chunk.getBlock(0, 0, 0);
                    var nChunk = GeoData.getInstance().getRelativeBlock(BlockFace.NORTH, block).getChunk();
                    return new SubChunk(nChunk, subSection);
                } else {
                    var loc = new Location(chunk.getWorld(), chunk.getX() * 16, 0, chunk.getZ() * 16 -16);
                    return new SubChunk(loc.getChunk(), subSection);
                }
            }
            case SOUTH -> {
                if (chunk.getZ() == GeoData.getInstance().MAX_XZ / 16) {
                    var block = chunk.getBlock(0, 0, 15);
                    var nChunk = GeoData.getInstance().getRelativeBlock(BlockFace.SOUTH, block).getChunk();
                    return new SubChunk(nChunk, subSection);
                } else {
                    var loc = new Location(chunk.getWorld(), chunk.getX() * 16, 0, chunk.getZ() * 16 + 16);
                    return new SubChunk(loc.getChunk(), subSection);
                }
            }
            case EAST -> {
                if (chunk.getX() == GeoData.getInstance().MAX_XZ / 16) {
                    var block = chunk.getBlock(15, 0, 0);
                    var nChunk = GeoData.getInstance().getRelativeBlock(BlockFace.EAST, block).getChunk();
                    return new SubChunk(nChunk, subSection);
                } else {
                    var loc = new Location(chunk.getWorld(), chunk.getX() * 16 + 16, 0, chunk.getZ() * 16);
                    return new SubChunk(loc.getChunk(), subSection);
                }
            }
            case WEST -> {
                if (chunk.getX() == -GeoData.getInstance().MAX_XZ / 16) {
                    var block = chunk.getBlock(0, 0, 0);
                    var nChunk = GeoData.getInstance().getRelativeBlock(BlockFace.WEST, block).getChunk();
                    return new SubChunk(nChunk, subSection);
                } else {
                    var loc = new Location(chunk.getWorld(), chunk.getX() * 16 - 16, 0, chunk.getZ() * 16);
                    return new SubChunk(loc.getChunk(), subSection);
                }
            }
        }
        return null;
    }

}
