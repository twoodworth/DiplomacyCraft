package me.tedwoodworth.diplomacy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class DiplomacyConfig {

    private static DiplomacyConfig instance = null;
    private final File diplomacyConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "config.yml");
    private YamlConfiguration diplomacyConfig;

    private String mapLink;

    public static DiplomacyConfig getInstance() {
        if (instance == null) {
            instance = new DiplomacyConfig();
        }
        return instance;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new DiplomacyConfig.EventListener(), Diplomacy.getInstance());
    }

    private DiplomacyConfig() {
        diplomacyConfig = YamlConfiguration.loadConfiguration(diplomacyConfigFile);
    }

    public String getMapLink() {
        var mapLink = diplomacyConfig.getString("map-link");
        if (mapLink == null) {
            Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Config.yml")));
            diplomacyConfig = YamlConfiguration.loadConfiguration(reader);
            return getMapLink();
        }
        return mapLink;
    }

    public void save() {
        try {
            diplomacyConfig.save(diplomacyConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }

        @EventHandler
        void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin().equals(Diplomacy.getInstance())) {
                save();
            }
        }
    }




}
