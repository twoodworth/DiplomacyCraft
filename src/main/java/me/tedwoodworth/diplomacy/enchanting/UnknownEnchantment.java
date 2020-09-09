package me.tedwoodworth.diplomacy.enchanting;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UnknownEnchantment extends Enchantment {
    private static UnknownEnchantment instance = null;
    private static final NamespacedKey key = new NamespacedKey(Diplomacy.getInstance(), "unknown_enchant");

    public UnknownEnchantment(@NotNull NamespacedKey key) {
        super(key);
    }

    public static UnknownEnchantment getInstance() {
        if (instance == null) {
            instance = new UnknownEnchantment(key);
        }
        return instance;
    }

    @Override
    public @NotNull String getName() {
        return "Unknown";
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public @NotNull EnchantmentTarget getItemTarget() {
        return EnchantmentTarget.BREAKABLE;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean conflictsWith(@NotNull Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(@NotNull ItemStack item) {
        return false;
    }
}
