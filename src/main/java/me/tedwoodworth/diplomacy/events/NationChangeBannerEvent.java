//package me.tedwoodworth.diplomacy.events;
//
//import me.tedwoodworth.diplomacy.nations.Nation;
//import org.bukkit.event.Event;
//import org.bukkit.event.HandlerList;
//import org.bukkit.inventory.ItemStack;
//import org.jetbrains.annotations.NotNull;
//
//public class NationChangeBannerEvent extends Event {
//
//    private final Nation nation;
//    private final ItemStack oldBanner;
//    private final ItemStack newBanner;
//    private static final HandlerList HANDLERS = new HandlerList();
//
//    public NationChangeBannerEvent(Nation nation, ItemStack oldBanner, ItemStack newBanner) {
//        this.nation = nation;
//        this.oldBanner = oldBanner;
//        this.newBanner = newBanner;
//    }
//
//    public Nation getNation() {
//        return nation;
//    }
//
//    public ItemStack getOldBanner() { return oldBanner; }
//
//    public ItemStack getNewBanner() { return newBanner; }
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
