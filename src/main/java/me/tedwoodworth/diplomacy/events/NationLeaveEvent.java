package me.tedwoodworth.diplomacy.events;

import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NationLeaveEvent extends Event {

    private DiplomacyPlayer diplomacyPlayer;
    private Nation nation;
    private static final HandlerList HANDLERS = new HandlerList();

    public NationLeaveEvent(DiplomacyPlayer diplomacyPlayer, Nation nation) {
        this.nation = nation;
        this.diplomacyPlayer = diplomacyPlayer;
    }

    public DiplomacyPlayer getDiplomacyPlayer() {
        return diplomacyPlayer;
    }

    public Nation getNation() {
        return nation;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}
