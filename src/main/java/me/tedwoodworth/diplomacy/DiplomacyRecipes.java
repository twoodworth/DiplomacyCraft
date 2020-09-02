package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.enchanting.enchantingManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

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
        woodenHammer();
        stoneHammer();
        goldenHammer();
        ironHammer();
        diamondHammer();
        enchantingTable();
    }

    private void woodenHammer() {
        var hammer = new ItemStack(Material.WOODEN_HOE);
        var itemMeta = hammer.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        var lore = new ArrayList<String>();
        lore.add(enchantingManager.getInstance().getHammerLore());
        itemMeta.setLore(lore);
        itemMeta.setLocalizedName(ChatColor.WHITE + "Wooden Hammer");
        itemMeta.setDisplayName(ChatColor.WHITE + "Wooden Hammer");
        hammer.setItemMeta(itemMeta);

        ShapedRecipe woodenHammer = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "WoodenHammer"), hammer);

        woodenHammer.shape("MMM","MSM", " S ");

        var planks = new ArrayList<Material>();
        planks.add(Material.ACACIA_PLANKS);
        planks.add(Material.BIRCH_PLANKS);
        planks.add(Material.CRIMSON_PLANKS);
        planks.add(Material.DARK_OAK_PLANKS);
        planks.add(Material.JUNGLE_PLANKS);
        planks.add(Material.OAK_PLANKS);
        planks.add(Material.SPRUCE_PLANKS);
        planks.add(Material.WARPED_PLANKS);
        woodenHammer.setIngredient('M', new RecipeChoice.MaterialChoice(planks));
        woodenHammer.setIngredient('S', Material.STICK);
        Diplomacy.getInstance().getServer().addRecipe(woodenHammer);
    }

    private void stoneHammer() {
        var hammer = new ItemStack(Material.STONE_HOE);
        var itemMeta = hammer.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        var lore = new ArrayList<String>();
        lore.add(enchantingManager.getInstance().getHammerLore());
        itemMeta.setLore(lore);
        itemMeta.setLocalizedName(ChatColor.WHITE + "Stone Hammer");
        itemMeta.setDisplayName(ChatColor.WHITE + "Stone Hammer");
        hammer.setItemMeta(itemMeta);

        ShapedRecipe stoneHammer = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "StoneHammer"), hammer);

        stoneHammer.shape("MMM","MSM", " S ");

        var stones = new ArrayList<Material>();
        stones.add(Material.COBBLESTONE);
        stones.add(Material.BLACKSTONE);

        stoneHammer.setIngredient('M', new RecipeChoice.MaterialChoice(stones));
        stoneHammer.setIngredient('S', Material.STICK);
        Diplomacy.getInstance().getServer().addRecipe(stoneHammer);
    }

    private void goldenHammer() {
        var hammer = new ItemStack(Material.GOLDEN_HOE);
        var itemMeta = hammer.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        var lore = new ArrayList<String>();
        lore.add(enchantingManager.getInstance().getHammerLore());
        itemMeta.setLore(lore);
        itemMeta.setLocalizedName(ChatColor.WHITE + "Golden Hammer");
        itemMeta.setDisplayName(ChatColor.WHITE + "Golden Hammer");
        hammer.setItemMeta(itemMeta);

        ShapedRecipe goldenHammer = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "GoldenHammer"), hammer);

        goldenHammer.shape("MMM","MSM", " S ");

        goldenHammer.setIngredient('M', Material.GOLD_INGOT);
        goldenHammer.setIngredient('S', Material.STICK);
        Diplomacy.getInstance().getServer().addRecipe(goldenHammer);
    }

    private void ironHammer() {
        var hammer = new ItemStack(Material.IRON_HOE);
        var itemMeta = hammer.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        var lore = new ArrayList<String>();
        lore.add(enchantingManager.getInstance().getHammerLore());
        itemMeta.setLore(lore);
        itemMeta.setLocalizedName(ChatColor.WHITE + "Iron Hammer");
        itemMeta.setDisplayName(ChatColor.WHITE + "Iron Hammer");
        hammer.setItemMeta(itemMeta);

        ShapedRecipe ironHammer = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "IronHammer"), hammer);

        ironHammer.shape("MMM","MSM", " S ");

        ironHammer.setIngredient('M', Material.IRON_INGOT);
        ironHammer.setIngredient('S', Material.STICK);
        Diplomacy.getInstance().getServer().addRecipe(ironHammer);
    }

    private void diamondHammer() {
        var hammer = new ItemStack(Material.DIAMOND_HOE);
        var itemMeta = hammer.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        var lore = new ArrayList<String>();
        lore.add(enchantingManager.getInstance().getHammerLore());
        itemMeta.setLore(lore);
        itemMeta.setLocalizedName(ChatColor.WHITE + "Diamond Hammer");
        itemMeta.setDisplayName(ChatColor.WHITE + "Diamond Hammer");
        hammer.setItemMeta(itemMeta);

        ShapedRecipe diamondHammer = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "DiamondHammer"), hammer);

        diamondHammer.shape("MMM","MSM", " S ");

        diamondHammer.setIngredient('M', Material.DIAMOND);
        diamondHammer.setIngredient('S', Material.STICK);
        Diplomacy.getInstance().getServer().addRecipe(diamondHammer);
    }

    private void enchantingTable() {
        Diplomacy.getInstance().getServer().removeRecipe(NamespacedKey.minecraft("enchanting_table"));

        ShapedRecipe enchantingTable = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "enchanting_table"), new ItemStack(Material.ENCHANTING_TABLE));

        enchantingTable.shape(" B ", "DOD", "OOO");

        enchantingTable.setIngredient('D', Material.DIAMOND);
        enchantingTable.setIngredient('O', Material.OBSIDIAN);
        enchantingTable.setIngredient('B', Material.KNOWLEDGE_BOOK);

        Diplomacy.getInstance().getServer().addRecipe(enchantingTable);
    }

}
