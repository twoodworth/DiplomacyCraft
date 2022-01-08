package me.tedwoodworth.diplomacy.database;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.sql.SQLException;
import java.util.logging.Level;

public class DBManager {

    public static void initialize() {
        try {
            var conn = DBConnectionManager.getSQLConnection();
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

            conn.commit();
        } catch (SQLException e) {
            Diplomacy.getInstance().getLogger().log(Level.SEVERE, "Unable to initialize database");
            Bukkit.getPluginManager().disablePlugin(Diplomacy.getInstance());
        }
    }

    public static boolean hasJoined(Player player) {
        var conn = DBConnectionManager.getSQLConnection();
        try {
            var s = conn.prepareStatement(Statements.selectPlayer);
            s.setString(1, player.getUniqueId().toString());
            var rs = s.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }

    }

    public static double selectBalance(Player player) {
        var conn = DBConnectionManager.getSQLConnection();
        try {
            var s = conn.prepareStatement(Statements.selectBalance);
            s.setString(1, player.getUniqueId().toString());
            var rs = s.executeQuery();
            double d;
            if (rs.next()) {
                d = rs.getDouble("balance");
            } else {
                d = 0.0;
            }
            rs.close();
            s.close();
            return d;
        } catch (SQLException e) {
            return 0.0;
        }
    }

    public static void insertPlayer(Player player) {
        var conn = DBConnectionManager.getSQLConnection();
        try {
            var s = conn.prepareStatement(Statements.insertPlayer);
            s.setString(1, player.getUniqueId().toString());
            s.setDouble(2, 1000.00);
            s.execute();
            s.close();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    private static class EventListener implements Listener {


    }
}
