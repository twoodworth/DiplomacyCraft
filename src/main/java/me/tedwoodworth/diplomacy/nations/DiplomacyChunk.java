package me.tedwoodworth.diplomacy.nations;

import org.bukkit.Chunk;

public class DiplomacyChunk {

    private Chunk chunk;

    public DiplomacyChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public Nation getNation() {
        for (Nation nation : Nations.getInstance().getNations()) {
            if (nation.getChunks().anyMatch(this::equals)) {
                return nation;
            }
        }
        return null;
    }
}
