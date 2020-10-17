package me.tedwoodworth.diplomacy.players;

import org.bukkit.configuration.ConfigurationSection;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class Account {
    private final ConfigurationSection configSection;
    private final String id;

    public Account(ConfigurationSection configSection, String id) {
        this.configSection = configSection;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ConfigurationSection getConfigSection() {
        return configSection;
    }

    public UUID getMain() {
        var strUUID = configSection.getString("Main");
        assert strUUID != null;
        return UUID.fromString(strUUID);
    }

    public void setMain(UUID uuid) {
        configSection.set("Main", uuid.toString());
    }

    public int getLives() {
        return configSection.getInt("Lives");
    }

    public void setLives(int lives) {
        configSection.set("Lives", lives);
    }

    public Set<UUID> getPlayerIDs() {
        var strUUIDs = configSection.getStringList("UUIDs");
        var UUIDs = new HashSet<UUID>();
        for (var strUUID : strUUIDs) {
            var uuid = UUID.fromString(strUUID);
            UUIDs.add(uuid);
        }
        return UUIDs;
    }

    public Set<InetAddress> getAddresses() {
        var addresses = new HashSet<InetAddress>();
            var strAddresses = configSection.getStringList("Addresses");
            for (var strAddress : strAddresses) {
                try {
                var address = InetAddress.getByName(strAddress);
                addresses.add(address);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        return addresses;
    }

    public void addAddress(InetAddress address) {
        var strAddresses = configSection.getStringList("Addresses");
        strAddresses.add(address.getHostAddress());
        configSection.set("Addresses", strAddresses);
    }

    public void addPlayerID(UUID id) {
        var strUUIDs = configSection.getStringList("UUIDs");
        strUUIDs.add(id.toString());
        configSection.set("UUIDs", strUUIDs);
    }

    public void removePlayerID(UUID id) {
        var strUUIDs = configSection.getStringList("UUIDs");
        strUUIDs.remove(id.toString());
        configSection.set("UUIDs", strUUIDs);
    }

    public void removeAddress(InetAddress address) {
        var strAddresses = configSection.getStringList("Addresses");
        strAddresses.remove(address.getHostAddress());
        configSection.set("Addresses", strAddresses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(configSection, account.configSection) &&
                Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configSection, id);
    }
}


