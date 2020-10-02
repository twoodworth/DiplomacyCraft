package me.tedwoodworth.diplomacy.events;

import me.tedwoodworth.diplomacy.nations.Nation;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NationToggleBorderEvent extends Event {

    private final Nation nation;
    private final boolean oldIsOpen;
    private final boolean newIsOpen;
    private static final HandlerList HANDLERS = new HandlerList();

    public NationToggleBorderEvent(Nation nation, boolean oldIsOpen, boolean newIsOpen) {
        this.nation = nation;
        this.oldIsOpen = oldIsOpen;
        this.newIsOpen = newIsOpen;
    }

    public Nation getNation() {
        return nation;
    }

    public boolean getOldIsOpen() { return oldIsOpen; }

    public boolean getNewIsOpen() { return newIsOpen; }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
