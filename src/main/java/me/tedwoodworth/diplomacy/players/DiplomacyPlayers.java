package me.tedwoodworth.diplomacy.players;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DiplomacyPlayers {

    private static DiplomacyPlayers instance = null;
    private File diplomacyPlayerConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "diplomacyPlayers.yml");
    private Map<UUID, DiplomacyPlayer> diplomacyPlayers = new WeakHashMap<>();
    private YamlConfiguration config;

    public static DiplomacyPlayers getInstance() {
        if (instance == null) {
            instance = new DiplomacyPlayers();
        }
        return instance;
    }

    private DiplomacyPlayers() {
        config = YamlConfiguration.loadConfiguration(diplomacyPlayerConfigFile);
        save();
    }

    public DiplomacyPlayer get(UUID uuid) {
        List<String> groups = new ArrayList<>(1);
        List<String> groupsLed = new ArrayList<>(1);
        DiplomacyPlayer player = diplomacyPlayers.get(uuid);
        if (player == null) {
            Map<String, Object> playersMap = ImmutableMap.of(
                    "Groups", groups,
                    "GroupsLed", groupsLed);

            config.createSection(uuid.toString(), playersMap);
            player = new DiplomacyPlayer(uuid, config.getConfigurationSection(uuid.toString()));
            diplomacyPlayers.put(uuid, player);
        }
        return player;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    public void save() {
        try {
            config.save(diplomacyPlayerConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }
    }
}
