package me.tedwoodworth.diplomacy.database;


import me.tedwoodworth.diplomacy.Diplomacy;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;


public class ConnectionManager {
    private static Connection connection = null;

    private ConnectionManager() {
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // SQL creation stuff, You can leave the blow stuff untouched.
    static Connection getSQLConnection() {
        var plugin = Diplomacy.getInstance();
        var dbname = plugin.getConfig().getString("SQLite.Filename", "diplomacy");
        var folder = plugin.getDataFolder();
        if (!folder.exists()) {
            folder.mkdir();
        }
        File dataFolder = new File(plugin.getDataFolder(), dbname + ".db");
        if (!dataFolder.exists()) {
            try {
                if (!dataFolder.createNewFile()) {
                    throw new IOException();
                }
            } catch (IOException e) {
                System.out.println("Error"); //todo remove
                plugin.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }
}