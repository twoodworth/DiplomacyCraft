package me.tedwoodworth.diplomacy.groups;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.Nation;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DiplomacyGroups {
    private static DiplomacyGroups instance = null;
    private final File groupConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "diplomacyGroups.yml");
    private List<DiplomacyGroup> groups = new ArrayList<>();
    private YamlConfiguration groupConfig;

    public List<DiplomacyGroup> getGroups() {
        return groups;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new DiplomacyGroups.EventListener(), Diplomacy.getInstance());
    }

    public DiplomacyGroup createGroup(String name, Nation nation) {
        var nextGroupID = groupConfig.getString("NextGroupID");
        if (nextGroupID == null) {
            groupConfig.set("NextGroupID", "0");
            nextGroupID = "0";
        }

        var groupID = nextGroupID;
        nextGroupID = String.valueOf(Integer.parseInt(nextGroupID) + 1);

        Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Group.yml")));
        ConfigurationSection groupSection = YamlConfiguration.loadConfiguration(reader);

        groupConfig.set("Groups." + groupID, groupSection);
        groupConfig.set("NextNationID", nextGroupID);
        var initializedGroupSection = DiplomacyGroup.initializeGroup(groupSection, nation, name);
        var group = new DiplomacyGroup(groupID, initializedGroupSection);
        groups.add(group);
        return group;
    }

    public static DiplomacyGroups getInstance() {
        if (instance == null) {
            instance = new DiplomacyGroups();
        }
        return instance;
    }

    private DiplomacyGroups() {
        groupConfig = YamlConfiguration.loadConfiguration(groupConfigFile);
        var groupsSection = groupConfig.getConfigurationSection("Groups");
        if (groupsSection == null) {
            groupsSection = groupConfig.createSection("Groups");
        }
        for (var groupID : groupsSection.getKeys(false)) {
            var groupSection = groupConfig.getConfigurationSection("Groups." + groupID);
            Validate.notNull(groupSection);


            var group = new DiplomacyGroup(groupID, groupSection);
            groups.add(group);
        }
    }

    @Nullable
    public DiplomacyGroup get(String name) {
        for (var group : groups) {
            if (name.equalsIgnoreCase(group.getName())) {
                return group;
            }
        }
        return null;
    }

    @Nullable
    public DiplomacyGroup get(Chunk chunk) {
        for (var group : groups) {
            if (group.getChunks().contains(chunk)) {
                return group;
            }
        }
        return null;
    }

    public void removeGroup(DiplomacyGroup group) {
        groupConfig.set("Groups." + group.getGroupID(), null);
        groups.remove(group);
    }

    public void save() {
        try {
            groupConfig.save(groupConfigFile);
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
