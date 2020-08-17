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
import java.util.List;
import java.util.Objects;

public class DiplomacyConfig {

    private static DiplomacyConfig instance = null;
    private final File diplomacyConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "config.yml");
    private YamlConfiguration diplomacyConfig;

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

    public String getDiscordLink() {
        var discordLink = diplomacyConfig.getString("discord-link");
        if (discordLink == null) {
            Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Config.yml")));
            diplomacyConfig = YamlConfiguration.loadConfiguration(reader);
            return getDiscordLink();
        }
        return discordLink;
    }

    public List<String> getMessages() {
        if (diplomacyConfig.getStringList("messages").size() == 0) {
            Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Config.yml")));
            diplomacyConfig = YamlConfiguration.loadConfiguration(reader);
        }
        return diplomacyConfig.getStringList("messages");
    }

    public long getMessageInterval() {
        var interval = diplomacyConfig.getString("message-interval");
        if (interval == null) {
            Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Config.yml")));
            diplomacyConfig = YamlConfiguration.loadConfiguration(reader);
            interval = "120";
        }
        return Long.parseLong(interval);
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
