package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NationManager {
    private static final File nationConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "nations.yml");

    public static void load() {
        YamlConfiguration nationConfig = YamlConfiguration.loadConfiguration(nationConfigFile);
        List<Nation> nations = new ArrayList<>();
        for (String nationID : nationConfig.getKeys(false)) {
            ConfigurationSection nationSection = nationConfig.getConfigurationSection(nationID);
            String name = nationSection.getString("name");
            List<UUID> leaders = nationSection.getString("name");
            String name = nationSection.getString("name");
            String name = nationSection.getString("name");
            String name = nationSection.getString("name");
            String name = nationSection.getString("name");
            String name = nationSection.getString("name");
            String name = nationSection.getString("name");

        }
    }


}
