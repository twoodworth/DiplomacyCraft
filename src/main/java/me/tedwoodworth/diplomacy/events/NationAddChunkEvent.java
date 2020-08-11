package me.tedwoodworth.diplomacy.events;

import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.Nation;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NationAddChunkEvent extends Event {

    private final Nation nation;
    private final DiplomacyChunk diplomacyChunk;
    private static final HandlerList HANDLERS = new HandlerList();

    public NationAddChunkEvent(Nation nation, DiplomacyChunk diplomacyChunk) {
        this.nation = nation;
        this.diplomacyChunk = diplomacyChunk;
    }

    public Nation getNation() {
        return nation;
    }

    public DiplomacyChunk getDiplomacyChunk() {
        return diplomacyChunk;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
