//package me.tedwoodworth.diplomacy.world;
//
//import me.tedwoodworth.diplomacy.Diplomacy;
//import me.tedwoodworth.diplomacy.Guis.Guis;
//import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
//import org.bukkit.Bukkit;
//import org.bukkit.block.Block;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerInteractEvent;
//
//public class PlotManager {
//    private static PlotManager instance = null;
//
//
//    public static PlotManager getInstance() {
//        if (instance == null) {
//            instance = new PlotManager();
//        }
//        return instance;
//    }
//
//    public void registerEvents() {
//        Bukkit.getPluginManager().registerEvents(new PlotManager.EventListener(), Diplomacy.getInstance());
//    }
//
//    private class EventListener implements Listener {
//
//        @EventHandler
//        private void onPlayerInteract(PlayerInteractEvent e) {
//            Block b = e.getClickedBlock();
//
//        }
//    }
//    }
