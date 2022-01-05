//package me.tedwoodworth.diplomacy.events;
//
//import me.tedwoodworth.diplomacy.nations.Nation;
//import org.bukkit.event.Event;
//import org.bukkit.event.HandlerList;
//import org.bukkit.inventory.ItemStack;
//import org.jetbrains.annotations.NotNull;
//
//public class NationChangeBalanceEvent extends Event {
//
//    private final Nation nation;
//    private final double oldBal;
//    private final double newBal;
//    private static final HandlerList HANDLERS = new HandlerList();
//
//    public NationChangeBalanceEvent(Nation nation, double newBal, double oldBal) {
//        this.nation = nation;
//        this.newBal = newBal;
//        this.oldBal = oldBal;
//    }
//
//    public Nation getNation() {
//        return nation;
//    }
//
//    public double getOldBal() { return oldBal; }
//
//    public double getNewBal() { return newBal; }
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
