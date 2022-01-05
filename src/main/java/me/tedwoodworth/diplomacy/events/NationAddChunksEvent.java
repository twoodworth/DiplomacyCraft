//package me.tedwoodworth.diplomacy.events;
//
//import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
//import me.tedwoodworth.diplomacy.nations.Nation;
//import org.bukkit.event.Event;
//import org.bukkit.event.HandlerList;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Set;
//
//public class NationAddChunksEvent extends Event {
//
//    private final Nation nation;
//    private final Set<DiplomacyChunk> diplomacyChunks;
//    private static final HandlerList HANDLERS = new HandlerList();
//
//    public NationAddChunksEvent(Nation nation, Set<DiplomacyChunk> diplomacyChunk) {
//        this.nation = nation;
//        this.diplomacyChunks = diplomacyChunk;
//    }
//
//    public Nation getNation() {
//        return nation;
//    }
//
//    public Set<DiplomacyChunk> getDiplomacyChunks() {
//        return diplomacyChunks;
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
