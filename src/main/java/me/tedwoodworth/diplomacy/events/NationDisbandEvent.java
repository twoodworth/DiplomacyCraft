package me.tedwoodworth.diplomacy.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NationDisbandEvent extends Event {

    private String nationID;
    private static final HandlerList HANDLERS = new HandlerList();

    public NationDisbandEvent(String nationID) {
        this.nationID = nationID;
    }

    public String getNationID() {
        return nationID;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
