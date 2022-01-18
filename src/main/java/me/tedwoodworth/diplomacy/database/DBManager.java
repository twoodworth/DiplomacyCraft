package me.tedwoodworth.diplomacy.database;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.Plugin;

import java.sql.SQLException;
import java.util.logging.Level;

public class DBManager {

    public static void initialize() {
        try {
            var conn = ConnectionManager.getSQLConnection();
            if (conn == null) {
                Diplomacy.getInstance().getLogger().log(Level.SEVERE, "Unable to initialize database");
                Bukkit.getPluginManager().disablePlugin(Diplomacy.getInstance());
                return;
            }

            var s = conn.prepareStatement(Statements.createChunkTable);
            s.execute();
            s.close();

            s = conn.prepareStatement(Statements.createPlayerTable);
            s.execute();
            s.close();

            s = conn.prepareStatement(Statements.createChunkResidents);
            s.execute();
            s.close();

            s = conn.prepareStatement(Statements.createCoordinateTable);
            s.execute();
            s.close();

            s = conn.prepareStatement(Statements.createPlayerLocations);
            s.execute();
            s.close();

            conn.commit();
        } catch (SQLException e) {
            Diplomacy.getInstance().getLogger().log(Level.SEVERE, "Unable to initialize database");
            Bukkit.getPluginManager().disablePlugin(Diplomacy.getInstance());
        }
    }



    public static void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    private static class EventListener implements Listener {


    }
}
