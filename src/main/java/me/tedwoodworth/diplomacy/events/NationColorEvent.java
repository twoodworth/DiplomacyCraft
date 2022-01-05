//package me.tedwoodworth.diplomacy.events;
//
//import me.tedwoodworth.diplomacy.nations.Nation;
//import org.bukkit.event.Event;
//import org.bukkit.event.HandlerList;
//import org.jetbrains.annotations.NotNull;
//
//import java.awt.*;
//
//public class NationColorEvent extends Event {
//
//    private Nation nation;
//    private Color color;
//    private static final HandlerList HANDLERS = new HandlerList();
//
//    public NationColorEvent(Nation nation, Color color) {
//        this.nation = nation;
//        this.color = color;
//    }
//
//    public Nation getNation() {
//        return nation;
//    }
//
//    public Color getColor() {
//        return color;
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
