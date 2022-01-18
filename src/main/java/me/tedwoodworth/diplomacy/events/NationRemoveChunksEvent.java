package me.tedwoodworth.diplomacy.events;

import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.Nation;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class NationRemoveChunksEvent extends Event {

    private final Nation nation;
    private final Set<DiplomacyChunk> chunks;
    private static final HandlerList HANDLERS = new HandlerList();

    public NationRemoveChunksEvent(Nation nation, Set<DiplomacyChunk> chunks) {
        this.nation = nation;
        this.chunks = chunks;
    }

    public Nation getNation() {
        return nation;
    }

    public Set<DiplomacyChunk> getChunks() {
        return chunks;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
