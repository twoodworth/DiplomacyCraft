package me.tedwoodworth.diplomacy.nations;

import org.bukkit.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DiplomacyChunk {

    private Chunk chunk;

    public DiplomacyChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiplomacyChunk that = (DiplomacyChunk) o;
        return Objects.equals(chunk, that.chunk);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chunk);
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

    @Nullable
    public NationGroup getGroup() {
        Nation nation = this.getNation();
        if (nation != null) {
            if (nation.getGroups() != null) {
                for (var group : nation.getGroups()) {
                    if (group.getChunks().contains(this)) {
                        return group;
                    }
                }
            }
        }
        return null;
    }

}
