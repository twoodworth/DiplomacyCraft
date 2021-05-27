package me.tedwoodworth.diplomacy.dynmap.blocks;

public class NationChunk {
    private final int x;
    private final int z;

    public NationChunk(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }
}
