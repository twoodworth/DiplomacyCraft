//package me.tedwoodworth.diplomacy.events;
//
//import me.tedwoodworth.diplomacy.nations.Nation;
//import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
//import org.bukkit.event.Event;
//import org.bukkit.event.HandlerList;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Set;
//
//public class NationLeaveEvent extends Event {
//
//    private Set<DiplomacyPlayer> diplomacyPlayers;
//    private Nation nation;
//    private static final HandlerList HANDLERS = new HandlerList();
//
//    public NationLeaveEvent(Set<DiplomacyPlayer> diplomacyPlayers, Nation nation) {
//        this.nation = nation;
//        this.diplomacyPlayers = diplomacyPlayers;
//    }
//
//    public Set<DiplomacyPlayer> getDiplomacyPlayer() {
//        return diplomacyPlayers;
//    }
//
//    public Nation getNation() {
//        return nation;
//    }
//
//    @Override
//    public @NotNull HandlerList getHandlers() {
//        return HANDLERS;
//    }
//
//    public static HandlerList getHandlerList() {
//        return HANDLERS;
//    }
//}
