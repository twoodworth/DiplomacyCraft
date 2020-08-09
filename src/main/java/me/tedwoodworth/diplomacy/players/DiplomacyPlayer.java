package me.tedwoodworth.diplomacy.players;

import com.google.common.base.Objects;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.text.SimpleDateFormat;
import java.time.Instant;
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

    public long getAge() {
        return Instant.now().getEpochSecond() - Bukkit.getOfflinePlayer(this.getUUID()).getFirstPlayed();
    }

    public String getDateJoined() {
        var joined = Bukkit.getOfflinePlayer(this.getUUID()).getFirstPlayed();
        var time = new java.util.Date(joined);
        SimpleDateFormat jdf = new SimpleDateFormat("MM/dd/yyyy");
        return jdf.format(time);
    }

    public List<String> getGroups() {
        return configSection.getStringList("Groups");
    }

    public List<String> getGroupsLed() {
        return configSection.getStringList("GroupsLed");
    }

    public void addGroup(DiplomacyGroup group) {
        var list = this.getGroups();
        list.add(group.getGroupID());
        configSection.set("Groups", list);
    }

    public void removeGroup(DiplomacyGroup group) {
        var list = this.getGroups();
        list.remove(group.getGroupID());
        configSection.set("Groups", list);
    }

    public void addGroupLed(DiplomacyGroup group) {
        var list = this.getGroupsLed();
        list.add(group.getGroupID());
        configSection.set("GroupsLed", list);
    }

    public void removeGroupLed(DiplomacyGroup group) {
        var list = this.getGroupsLed();
        list.remove(group.getGroupID());
        configSection.set("GroupsLed", list);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (DiplomacyPlayer) o;
        return Objects.equal(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    public OfflinePlayer getPlayer() {
        var player = Bukkit.getOfflinePlayer(uuid);
        Validate.notNull(player);
        return player;
    }
}

