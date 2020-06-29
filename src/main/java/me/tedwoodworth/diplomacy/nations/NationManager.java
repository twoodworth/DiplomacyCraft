package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NationManager {
    private static final File nationConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "nations.yml");

    private static List<Nation> nations = new ArrayList<>();

    private static YamlConfiguration nationConfig;

    public static List<Nation> getNations() {
        return nations;
    }

    public static Nation createNation(String name, OfflinePlayer leader) {
        String nextNationID = nationConfig.getString("NextNationID");
        if (nextNationID == null) {
            nationConfig.set("NextNationID", "0");
            nextNationID = "0";
        }

        String nationID = nextNationID;
        nextNationID = String.valueOf(Integer.parseInt(nextNationID) + 1);

        Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Nation.yml")));
        ConfigurationSection nationSection = YamlConfiguration.loadConfiguration(reader);

        nationConfig.set("Nations." + nationID, nationSection);
        nationConfig.set("NextNationID", nextNationID);
        Nation.initializeNation(nationSection, leader);
        return new Nation(nationID, nationSection);

    }

    public static void load() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
        nationConfig = YamlConfiguration.loadConfiguration(nationConfigFile);
        ConfigurationSection nationsSection = nationConfig.getConfigurationSection("Nations");
        if (nationsSection == null) {
            nationsSection = nationConfig.createSection("Nations");
        }
        for (String nationID : nationsSection.getKeys(false)) {
            ConfigurationSection nationSection = nationConfig.getConfigurationSection("Nations." + nationID);
            assert nationSection != null;


            Nation nation = new Nation(nationID, nationSection);
            nations.add(nation);
        }
    }

    public static void save() {
        try {
            nationConfig.save(nationConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }
    }
}
