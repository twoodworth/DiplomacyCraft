package me.tedwoodworth.diplomacy.enchanting;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import java.util.List;
import java.util.Objects;

public class EnchantingTable {
    private final int id;
    private final ConfigurationSection configSection;

    public EnchantingTable(String id, ConfigurationSection config) {
        this.id = Integer.parseInt(id);
        this.configSection = config;
    }

    public int getID() {
        return id;
    }

    public ConfigurationSection getConfigSection() {
        return configSection;
    }

    public Location getLocation() {
        var x = Double.parseDouble(Objects.requireNonNull(configSection.getString("Location.x")));
        var y = Double.parseDouble(Objects.requireNonNull(configSection.getString("Location.y")));
        var z = Double.parseDouble(Objects.requireNonNull(configSection.getString("Location.z")));
        var worldName = configSection.getString("Location.World");
        assert worldName != null;
        var world = Bukkit.getWorld(worldName);
        return new Location(world, x, y, z);
    }

    public void addEnchantment(Enchantment enchantment) {
        var enchantments = configSection.getStringList("Enchantments");
        enchantments.add(enchantment.getKey().getKey());
        configSection.set("Enchantments", enchantments);
    }

    public List<String> getEnchantmentKeys() {
        return configSection.getStringList("Enchantments");
    }

    public boolean hasEnchantment(Enchantment enchantment) {
        return getEnchantmentKeys().contains(enchantment.getKey().getKey());
    }
}
