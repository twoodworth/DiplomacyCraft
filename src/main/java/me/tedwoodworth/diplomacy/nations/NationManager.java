package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NationManager {
    private static final File nationConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "nations.yml");

    private static final List<Nation> nations = new ArrayList<>();

    public static List<Nation> getNations() {
        return nations;
    }

    public static void load() {
        YamlConfiguration nationConfig = YamlConfiguration.loadConfiguration(nationConfigFile);
        for (String nationID : nationConfig.getKeys(false)) {
            ConfigurationSection nationSection = nationConfig.getConfigurationSection(nationID);
            assert nationSection != null;


            Nation nation = new Nation(nationID, nationSection);
            nations.add(nation);
        }
    }
}
