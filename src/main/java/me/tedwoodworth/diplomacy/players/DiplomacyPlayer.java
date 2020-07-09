package me.tedwoodworth.diplomacy.players;

import com.google.common.base.Objects;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.UUID;

public class DiplomacyPlayer {
    private UUID uuid;
    private ConfigurationSection configSection;


    DiplomacyPlayer(UUID uuid, ConfigurationSection configSection) {
        this.uuid = uuid;
        this.configSection = configSection;
    }

    public UUID getUUID() {
        return uuid;
    }


    public List<String> getGroups() {
        return configSection.getStringList("Groups");
    }

    public List<String> getGroupsLed() {
        return configSection.getStringList("GroupsLed");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiplomacyPlayer that = (DiplomacyPlayer) o;
        return Objects.equal(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}

