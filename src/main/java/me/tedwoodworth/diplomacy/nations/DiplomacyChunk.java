package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import org.bukkit.Chunk;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class DiplomacyChunk {

    private final Chunk chunk;

    public DiplomacyChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Nullable
    public Nation getNation() {
        for (var nation : Nations.getInstance().getNations()) {
            if (nation.hasChunk(this)) {
                return nation;
            }
        }
        return null;
    }

    @Nullable
    public DiplomacyGroup getGroup() {
        for (var group : DiplomacyGroups.getInstance().getGroups()) {
            if (group.hasChunk(this)) {
                return group;
            }
        }
        return null;
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
}
