package me.tedwoodworth.diplomacy.events;

import me.tedwoodworth.diplomacy.nations.Nation;
import org.bukkit.Chunk;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NationRemoveChunkEvent extends Event {

    private final Nation nation;
    private final Chunk chunk;
    private static final HandlerList HANDLERS = new HandlerList();

    public NationRemoveChunkEvent(Nation nation, Chunk chunk) {
        this.nation = nation;
        this.chunk = chunk;
    }

    public Nation getNation() {
        return nation;
    }

    public Chunk getChunk() {
        return chunk;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
