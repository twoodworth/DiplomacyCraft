package me.tedwoodworth.diplomacy.nations;

import org.bukkit.Chunk;
import org.bukkit.World;

public class DiplomacyChunk {

    private Chunk chunk;

    public DiplomacyChunk(World world, int x, int z) {
        this.chunk = world.getChunkAt(x, z);
    }

    public DiplomacyChunk(Chunk chunk, Nation nation) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public Nation getNation() {
        for (Nation nation : Nations.getInstance().getNations()) {
            if (nation.getChunks().contains(this)) {
                return nation;
            }
        }
        return null;
    }
}
