package me.tedwoodworth.diplomacy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

import java.util.ArrayList;

public class DiplomacyRecipes {
    private static DiplomacyRecipes instance = null;

    public static DiplomacyRecipes getInstance() {
        if (instance == null) {
            instance = new DiplomacyRecipes();
        }
        return instance;
    }

    public void loadRecipes() {
        enchantingTable();
    }

    private void enchantingTable() {
        Diplomacy.getInstance().getServer().removeRecipe(NamespacedKey.minecraft("enchanting_table"));

        var table = new ItemStack(Material.ENCHANTING_TABLE);
        var meta = table.getItemMeta();
        var lore = new ArrayList<String>();
        lore.add(ChatColor.RED + "Warning: Enchanting tables are disabled,");
        lore.add(ChatColor.RED + "the only use of enchanting tables is for");
        lore.add(ChatColor.RED + "decoration. To add enchantments onto your items");
        lore.add(ChatColor.RED + "view #enchanting-guide in the discord (/discord)");

        meta.setLore(lore);
        table.setItemMeta(meta);
        ShapedRecipe enchantingTable = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "enchanting_table"), table);

        enchantingTable.shape(" B ", "DOD", "OOO");

        enchantingTable.setIngredient('D', Material.DIAMOND);
        enchantingTable.setIngredient('O', Material.OBSIDIAN);
        enchantingTable.setIngredient('B', Material.BOOK);

        Diplomacy.getInstance().getServer().addRecipe(enchantingTable);
    }

}
