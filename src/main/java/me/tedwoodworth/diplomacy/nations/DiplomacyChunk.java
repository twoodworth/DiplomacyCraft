package me.tedwoodworth.diplomacy.nations;

import org.bukkit.Chunk;
import org.jetbrains.annotations.Nullable;

public class DiplomacyChunk {

    private Chunk chunk;

    public DiplomacyChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Nullable
    public Nation getNation() {
        for (var nation : Nations.getInstance().getNations()) {
            if (nation.getChunks().anyMatch(this::equals)) {
                return nation;
            }
        }
        return null;
    }
}
