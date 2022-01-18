package me.tedwoodworth.diplomacy.events;

import me.tedwoodworth.diplomacy.nations.Nation;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NationCreateEvent extends Event {

    private final Nation nation;
    private static final HandlerList HANDLERS = new HandlerList();

    public NationCreateEvent(Nation nation) {
        this.nation = nation;
    }

    public Nation getNation() {
        return nation;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
