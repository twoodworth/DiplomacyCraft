//package me.tedwoodworth.diplomacy.events;
//
//import me.tedwoodworth.diplomacy.nations.Nation;
//import org.bukkit.event.Event;
//import org.bukkit.event.HandlerList;
//import org.jetbrains.annotations.NotNull;
//
//public class NationRenameEvent extends Event {
//
//    private Nation nation;
//    private String oldName;
//    private String newName;
//    private static final HandlerList HANDLERS = new HandlerList();
//
//    public NationRenameEvent(Nation nation, String oldName, String newName) {
//        this.nation = nation;
//        this.oldName = oldName;
//        this.newName = newName;
//    }
//
//    public Nation getNation() {
//        return nation;
//    }
//
//    public String getOldName() {
//        return oldName;
//    }
//
//    public String getNewName() {
//        return newName;
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
