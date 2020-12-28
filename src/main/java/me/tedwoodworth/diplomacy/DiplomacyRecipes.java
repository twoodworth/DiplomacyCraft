package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.enchanting.Tools;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.Arrays;

import static org.bukkit.Material.*;

public class DiplomacyRecipes {
    private static DiplomacyRecipes instance = null;
    public String CHISEL_LORE = ChatColor.BLUE + "Chisel";
    public String KNIFE_LORE = ChatColor.BLUE + "Hunting Knife";
    public String SAW_LORE = ChatColor.BLUE + "Saw";
    public String IRON_DUST_LORE = ChatColor.BLUE + "Iron Dust";
    public String GOLD_DUST_LORE = ChatColor.BLUE + "Gold Dust";
    public String NETHERITE_DUST_LORE = ChatColor.BLUE + "Ancient Dust";
    public String SIFTER_LORE = ChatColor.BLUE + "Sifter";
    public String MAGNET_LORE = ChatColor.BLUE + "Magnet";
    public String SLURRIED_IRON_LORE = ChatColor.BLUE + "Slurried Iron Ingot";
    public String SLURRIED_GOLD_LORE = ChatColor.BLUE + "Slurried Gold Ingot";
    public String SLURRIED_NETHERITE_LORE = ChatColor.BLUE + "Slurried Netherite Ingot";
    public String IRON_SLURRY_LORE = ChatColor.BLUE + "Iron Slurry";
    public String GOLD_SLURRY_LORE = ChatColor.BLUE + "Gold Slurry";
    public String NETHERITE_SLURRY_LORE = ChatColor.BLUE + "Netherite Slurry";
    public String NEW_LAYER_LORE = "new layer";
    public String GRENADE_LORE = ChatColor.YELLOW + "Right Click: " + ChatColor.GRAY + "Overhand throw";
    public String GRENADE_LORE_2 = ChatColor.YELLOW + "Left Click: " + ChatColor.GRAY + "Underhand throw";
    public String GRENADE_LORE_3 = ChatColor.YELLOW + "Hold Shift: " + ChatColor.GRAY + "Unpin and wait until releasing shift to throw";

    public String COARSE_PAPER_LORE = ChatColor.BLUE + "Coarse Sandpaper";
    public String FINE_PAPER_LORE = ChatColor.BLUE + "Fine Sandpaper";
    public String WHETSTONE_LORE = ChatColor.BLUE + "Whetstone";
    public String WATERSTONE_LORE = ChatColor.BLUE + "Waterstone";
    public String COARSE_BLADE_LORE = ChatColor.BLUE + "Coarse Sharpening Blades";
    public String FINE_BLADE_LORE = ChatColor.BLUE + "Fine Sharpening Blades";
    public String IRON_ROD_LORE = ChatColor.BLUE + "Iron Honing Rod";
    public String NETHERITE_ROD_LORE = ChatColor.BLUE + "Netherite Honing Rod";
    public String REMAINING_USES_LORE = ChatColor.GRAY + "Remaining Uses:";

    public static DiplomacyRecipes getInstance() {
        if (instance == null) {
            instance = new DiplomacyRecipes();
        }
        return instance;
    }

    public void loadRecipes() {
        // Enchanting Table
        Diplomacy.getInstance().getServer().removeRecipe(NamespacedKey.minecraft("enchanting_table"));
        var table = new ItemStack(ENCHANTING_TABLE);
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
        enchantingTable.setIngredient('D', DIAMOND);
        enchantingTable.setIngredient('O', OBSIDIAN);
        enchantingTable.setIngredient('B', BOOK);
        Diplomacy.getInstance().getServer().addRecipe(enchantingTable);


        // Chisels
        var planks = new RecipeChoice.MaterialChoice(Tools.getInstance().planks);
        var stones = new RecipeChoice.MaterialChoice(Arrays.asList(COBBLESTONE, BLACKSTONE));

        var chiselLore = new ArrayList<String>();
        chiselLore.add(CHISEL_LORE);

        var woodChisel = new ItemStack(WOODEN_HOE);
        meta = woodChisel.getItemMeta();
        meta.setLore(chiselLore);
        meta.setLocalizedName("Wooden Chisel");
        meta.setDisplayName(ChatColor.RESET + "Wooden Chisel");
        woodChisel.setItemMeta(meta);
        ShapedRecipe woodChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "wooden_chisel"), woodChisel);
        woodChiselR.shape("SMM");
        woodChiselR.setIngredient('S', STICK);
        woodChiselR.setIngredient('M', planks);
        Diplomacy.getInstance().getServer().addRecipe(woodChiselR);

        var stoneChisel = new ItemStack(STONE_HOE);
        meta = stoneChisel.getItemMeta();
        meta.setLore(chiselLore);
        meta.setDisplayName(ChatColor.RESET + "Stone Chisel");
        meta.setLocalizedName("Stone Chisel");
        stoneChisel.setItemMeta(meta);
        ShapedRecipe stoneChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "stone_chisel"), stoneChisel);
        stoneChiselR.shape("SMM");
        stoneChiselR.setIngredient('S', STICK);
        stoneChiselR.setIngredient('M', stones);
        Diplomacy.getInstance().getServer().addRecipe(stoneChiselR);

        var goldChisel = new ItemStack(GOLDEN_HOE);
        meta = goldChisel.getItemMeta();
        meta.setLore(chiselLore);
        goldChisel.setItemMeta(meta);
        meta.setDisplayName(ChatColor.RESET + "Golden Chisel");
        meta.setLocalizedName("Golden Chisel");
        ShapedRecipe goldChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_chisel"), goldChisel);
        goldChiselR.shape("SMM");
        goldChiselR.setIngredient('S', STICK);
        goldChiselR.setIngredient('M', GOLD_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(goldChiselR);

        var ironChisel = new ItemStack(IRON_HOE);
        meta = ironChisel.getItemMeta();
        meta.setLore(chiselLore);
        meta.setDisplayName(ChatColor.RESET + "Iron Chisel");
        meta.setLocalizedName("Iron Chisel");
        ironChisel.setItemMeta(meta);
        ShapedRecipe ironChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_chisel"), ironChisel);
        ironChiselR.shape("SMM");
        ironChiselR.setIngredient('S', STICK);
        ironChiselR.setIngredient('M', IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(ironChiselR);

        var diamondChisel = new ItemStack(DIAMOND_HOE);
        meta = diamondChisel.getItemMeta();
        meta.setLore(chiselLore);
        meta.setDisplayName(ChatColor.RESET + "Diamond Chisel");
        meta.setLocalizedName("Diamond Chisel");
        diamondChisel.setItemMeta(meta);
        ShapedRecipe diamondChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "diamond_chisel"), diamondChisel);
        diamondChiselR.shape("SMM");
        diamondChiselR.setIngredient('S', STICK);
        diamondChiselR.setIngredient('M', DIAMOND);
        Diplomacy.getInstance().getServer().addRecipe(diamondChiselR);


        // Knives

        var knifeLore = new ArrayList<String>();
        knifeLore.add(KNIFE_LORE);

        var woodenKnife = new ItemStack(WOODEN_SWORD);
        meta = woodenKnife.getItemMeta();
        meta.setLore(knifeLore);
        meta.setLocalizedName("Wooden Hunting Knife");
        meta.setDisplayName(ChatColor.RESET + "Wooden Hunting Knife");
        woodenKnife.setItemMeta(meta);
        ShapedRecipe woodenKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "wooden_knife"), woodenKnife);
        woodenKnifeR.shape("M", "S");
        woodenKnifeR.setIngredient('S', STICK);
        woodenKnifeR.setIngredient('M', planks);
        Diplomacy.getInstance().getServer().addRecipe(woodenKnifeR);

        var stoneKnife = new ItemStack(STONE_SWORD);
        meta = stoneKnife.getItemMeta();
        meta.setLore(knifeLore);
        meta.setDisplayName(ChatColor.RESET + "Stone Hunting Knife");
        meta.setLocalizedName("Stone Hunting Knife");
        stoneKnife.setItemMeta(meta);
        ShapedRecipe stoneKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "stone_knife"), stoneKnife);
        stoneKnifeR.shape("M", "S");
        stoneKnifeR.setIngredient('S', STICK);
        stoneKnifeR.setIngredient('M', stones);
        Diplomacy.getInstance().getServer().addRecipe(stoneKnifeR);

        var goldKnife = new ItemStack(GOLDEN_SWORD);
        meta = goldKnife.getItemMeta();
        meta.setLore(knifeLore);
        goldKnife.setItemMeta(meta);
        meta.setDisplayName(ChatColor.RESET + "Golden Hunting Knife");
        meta.setLocalizedName("Golden Hunting Knife");
        ShapedRecipe goldKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_knife"), goldKnife);
        goldKnifeR.shape("M", "S");
        goldKnifeR.setIngredient('S', STICK);
        goldKnifeR.setIngredient('M', GOLD_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(goldKnifeR);

        var ironKnife = new ItemStack(IRON_SWORD);
        meta = ironKnife.getItemMeta();
        meta.setLore(knifeLore);
        meta.setDisplayName(ChatColor.RESET + "Iron Hunting Knife");
        meta.setLocalizedName("Iron Hunting Knife");
        ironKnife.setItemMeta(meta);
        ShapedRecipe ironKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_knife"), ironKnife);
        ironKnifeR.shape("M", "S");
        ironKnifeR.setIngredient('S', STICK);
        ironKnifeR.setIngredient('M', IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(ironKnifeR);

        var diamondKnife = new ItemStack(DIAMOND_SWORD);
        meta = diamondKnife.getItemMeta();
        meta.setLore(knifeLore);
        meta.setDisplayName(ChatColor.RESET + "Diamond Hunting Knife");
        meta.setLocalizedName("Diamond Hunting Knife");
        diamondKnife.setItemMeta(meta);
        ShapedRecipe diamondKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "diamond_knife"), diamondKnife);
        diamondKnifeR.shape("M", "S");
        diamondKnifeR.setIngredient('S', STICK);
        diamondKnifeR.setIngredient('M', DIAMOND);
        Diplomacy.getInstance().getServer().addRecipe(diamondKnifeR);


        // Saws

        var sawLore = new ArrayList<String>();
        sawLore.add(SAW_LORE);

        var woodenSaw = new ItemStack(WOODEN_HOE);
        meta = woodenSaw.getItemMeta();
        meta.setLore(sawLore);
        meta.setLocalizedName("Wooden Saw");
        meta.setDisplayName(ChatColor.RESET + "Wooden Saw");
        woodenSaw.setItemMeta(meta);
        woodenSaw.addEnchantment(Enchantment.SILK_TOUCH, 1);
        ShapedRecipe woodenSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "wooden_saw"), woodenSaw);
        woodenSawR.shape(" S ", "S S", "MMM");
        woodenSawR.setIngredient('S', STICK);
        woodenSawR.setIngredient('M', planks);
        Diplomacy.getInstance().getServer().addRecipe(woodenSawR);

        var stoneSaw = new ItemStack(STONE_HOE);
        meta = stoneSaw.getItemMeta();
        meta.setLore(sawLore);
        meta.setDisplayName(ChatColor.RESET + "Stone Saw");
        meta.setLocalizedName("Stone Saw");
        stoneSaw.setItemMeta(meta);
        stoneSaw.addEnchantment(Enchantment.SILK_TOUCH, 1);
        ShapedRecipe stoneSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "stone_saw"), stoneSaw);
        stoneSawR.shape(" S ", "S S", "MMM");
        stoneSawR.setIngredient('S', STICK);
        stoneSawR.setIngredient('M', stones);
        Diplomacy.getInstance().getServer().addRecipe(stoneSawR);

        var goldSaw = new ItemStack(GOLDEN_HOE);
        meta = goldSaw.getItemMeta();
        meta.setLore(sawLore);
        meta.setDisplayName(ChatColor.RESET + "Golden Saw");
        meta.setLocalizedName("Golden Saw");
        goldSaw.setItemMeta(meta);
        goldSaw.addEnchantment(Enchantment.SILK_TOUCH, 1);
        ShapedRecipe goldenSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_saw"), goldSaw);
        goldenSawR.shape(" S ", "S S", "MMM");
        goldenSawR.setIngredient('S', STICK);
        goldenSawR.setIngredient('M', GOLD_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(goldenSawR);

        var ironSaw = new ItemStack(IRON_HOE);
        meta = ironSaw.getItemMeta();
        meta.setLore(sawLore);
        meta.setDisplayName(ChatColor.RESET + "Iron Saw");
        meta.setLocalizedName("Iron Saw");
        ironSaw.setItemMeta(meta);
        ironSaw.addEnchantment(Enchantment.SILK_TOUCH, 1);
        ShapedRecipe ironSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_saw"), ironSaw);
        ironSawR.shape(" S ", "S S", "MMM");
        ironSawR.setIngredient('S', STICK);
        ironSawR.setIngredient('M', IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(ironSawR);

        var diamondSaw = new ItemStack(DIAMOND_HOE);
        meta = diamondSaw.getItemMeta();
        meta.setLore(sawLore);
        meta.setDisplayName(ChatColor.RESET + "Diamond Saw");
        meta.setLocalizedName("Diamond Saw");
        diamondSaw.setItemMeta(meta);
        diamondSaw.addEnchantment(Enchantment.SILK_TOUCH, 1);
        ShapedRecipe diamondSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "diamond_saw"), diamondSaw);
        diamondSawR.shape(" S ", "S S", "MMM");
        diamondSawR.setIngredient('S', STICK);
        diamondSawR.setIngredient('M', DIAMOND);
        Diplomacy.getInstance().getServer().addRecipe(diamondSawR);

        // Chain armor

        var chainHelm = new ItemStack(CHAINMAIL_HELMET);
        var chainHelmR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_helm"), chainHelm);
        chainHelmR.shape("NNN", "N N");
        chainHelmR.setIngredient('N', IRON_NUGGET);
        Diplomacy.getInstance().getServer().addRecipe(chainHelmR);

        var chainChest = new ItemStack(CHAINMAIL_CHESTPLATE);
        var chainChestR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_chest"), chainChest);
        chainChestR.shape("N N", "NNN", "NNN");
        chainChestR.setIngredient('N', IRON_NUGGET);
        Diplomacy.getInstance().getServer().addRecipe(chainChestR);

        var chainLegs = new ItemStack(CHAINMAIL_LEGGINGS);
        var chainLegsR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_legs"), chainLegs);
        chainLegsR.shape("NNN", "N N", "N N");
        chainLegsR.setIngredient('N', IRON_NUGGET);
        Diplomacy.getInstance().getServer().addRecipe(chainLegsR);

        var chainBoots = new ItemStack(CHAINMAIL_BOOTS);
        var chainBootsR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_boots"), chainBoots);
        chainBootsR.shape("N N", "N N");
        chainBootsR.setIngredient('N', IRON_NUGGET);
        Diplomacy.getInstance().getServer().addRecipe(chainBootsR);


        // Metal Dust
        FurnaceRecipe ironNugget = new FurnaceRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_nugget"),
                new ItemStack(IRON_NUGGET),
                SUGAR, 0, 200);
        Diplomacy.getInstance().getServer().addRecipe(ironNugget);

        FurnaceRecipe goldNugget = new FurnaceRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_nugget"),
                new ItemStack(GOLD_NUGGET),
                GLOWSTONE_DUST, 0, 200);
        Diplomacy.getInstance().getServer().addRecipe(goldNugget);

        FurnaceRecipe ancientNuggetR = new FurnaceRecipe(new NamespacedKey(Diplomacy.getInstance(), "netherite_Scrap"),
                new ItemStack(NETHERITE_SCRAP),
                REDSTONE, 0, 200);
        Diplomacy.getInstance().getServer().addRecipe(ancientNuggetR);

        // Metal Dust
        var ironNuggetB = new BlastingRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_nuggetB"),
                new ItemStack(IRON_NUGGET),
                SUGAR, 0, 100);
        Diplomacy.getInstance().getServer().addRecipe(ironNuggetB);

        var goldNuggetB = new BlastingRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_nuggetB"),
                new ItemStack(GOLD_NUGGET),
                GLOWSTONE_DUST, 0, 100);
        Diplomacy.getInstance().getServer().addRecipe(goldNuggetB);

        var ancientNuggetRB = new BlastingRecipe(new NamespacedKey(Diplomacy.getInstance(), "netherite_ScrapB"),
                new ItemStack(NETHERITE_SCRAP),
                REDSTONE, 0, 100);
        Diplomacy.getInstance().getServer().addRecipe(ancientNuggetRB);


        // Sifter
        var sifterLore = new ArrayList<String>();
        sifterLore.add(SIFTER_LORE);
        var sifter = new ItemStack(CHAINMAIL_HELMET);
        meta = sifter.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Wooden Sifter");
        meta.setLocalizedName("Wooden Sifter");
        meta.setLore(sifterLore);
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);

        var woodenSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "wooden_sifter"), sifter);
        woodenSifter.shape("NNN", "NNN", "NNN");
        woodenSifter.setIngredient('N', STICK);
        Diplomacy.getInstance().getServer().addRecipe(woodenSifter);

        meta.setDisplayName(ChatColor.RESET + "String Sifter");
        meta.setLocalizedName("String Sifter");
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
        var stringSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "string_sifter"), sifter);
        stringSifter.shape("NSN", "NSN", "NSN");
        stringSifter.setIngredient('N', STICK);
        stringSifter.setIngredient('S', STRING);
        Diplomacy.getInstance().getServer().addRecipe(stringSifter);

        meta.setDisplayName(ChatColor.RESET + "Chain Sifter");
        meta.setLocalizedName("Chain Sifter");
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
        sifter.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        var chainSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_sifter"), sifter);
        chainSifter.shape("NNN", "NSN", "NNN");
        chainSifter.setIngredient('N', IRON_NUGGET);
        chainSifter.setIngredient('S', IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(chainSifter);

        meta.setDisplayName(ChatColor.RESET + "Redstone Sifter");
        meta.setLocalizedName("Redstone Sifter");
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 4);
        sifter.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        var redstoneSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "redstone_sifter"), sifter);
        redstoneSifter.shape(" R ", "RSR", " R ");
        redstoneSifter.setIngredient('R', REDSTONE);
        redstoneSifter.setIngredient('S', CHAINMAIL_HELMET);
        Diplomacy.getInstance().getServer().addRecipe(redstoneSifter);


        meta.setDisplayName(ChatColor.RESET + "Netherite Sifter");
        meta.setLocalizedName("Netherite Sifter");
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 7);
        sifter.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        var netheriteSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "netherite_sifter"), sifter);
        netheriteSifter.shape("NNN", "NSN", "NNN");
        netheriteSifter.setIngredient('N', IRON_NUGGET);
        netheriteSifter.setIngredient('S', NETHERITE_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(netheriteSifter);

        lore = new ArrayList<>();
        lore.add(IRON_SLURRY_LORE);
        var ironSlurry = new ItemStack(POTION, 3);
        meta = ironSlurry.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Iron Slurry");
        meta.setLocalizedName("Iron Slurry");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ((PotionMeta) meta).setColor(Color.fromRGB(130, 107, 92));
        ironSlurry.setItemMeta(meta);
        var ironSlurryRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_slurry"), ironSlurry);
        ironSlurryRecipe.addIngredient(1, CLAY_BALL);
        ironSlurryRecipe.addIngredient(1, CHARCOAL);
        ironSlurryRecipe.addIngredient(1, SUGAR);
        ironSlurryRecipe.addIngredient(1, BONE_MEAL);
        ironSlurryRecipe.addIngredient(1, GRAVEL);
        ironSlurryRecipe.addIngredient(1, SAND);
        ironSlurryRecipe.addIngredient(3, POTION);
        Diplomacy.getInstance().getServer().addRecipe(ironSlurryRecipe);

        lore = new ArrayList<>();
        lore.add(GOLD_SLURRY_LORE);
        var goldSlurry = new ItemStack(POTION, 3);
        meta = goldSlurry.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Gold Slurry");
        meta.setLocalizedName("Gold Slurry");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ((PotionMeta) meta).setColor(Color.fromRGB(199, 160, 62));
        goldSlurry.setItemMeta(meta);
        var goldSlurryRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_slurry"), goldSlurry);
        goldSlurryRecipe.addIngredient(1, CLAY_BALL);
        goldSlurryRecipe.addIngredient(1, CHARCOAL);
        goldSlurryRecipe.addIngredient(1, GLOWSTONE_DUST);
        goldSlurryRecipe.addIngredient(1, BONE_MEAL);
        goldSlurryRecipe.addIngredient(1, GRAVEL);
        goldSlurryRecipe.addIngredient(1, SAND);
        goldSlurryRecipe.addIngredient(3, POTION);
        Diplomacy.getInstance().getServer().addRecipe(goldSlurryRecipe);

        lore = new ArrayList<>();
        lore.add(NETHERITE_SLURRY_LORE);
        var netheriteSlurry = new ItemStack(POTION, 3);
        meta = netheriteSlurry.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Netherite Slurry");
        meta.setLocalizedName("Netherite Slurry");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ((PotionMeta) meta).setColor(Color.fromRGB(50, 0, 10));
        netheriteSlurry.setItemMeta(meta);
        var netheriteSlurryRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "netherite_slurry"), netheriteSlurry);
        netheriteSlurryRecipe.addIngredient(1, CLAY_BALL);
        netheriteSlurryRecipe.addIngredient(1, CHARCOAL);
        netheriteSlurryRecipe.addIngredient(1, REDSTONE);
        netheriteSlurryRecipe.addIngredient(1, BONE_MEAL);
        netheriteSlurryRecipe.addIngredient(1, GRAVEL);
        netheriteSlurryRecipe.addIngredient(1, SAND);
        netheriteSlurryRecipe.addIngredient(3, POTION);
        Diplomacy.getInstance().getServer().addRecipe(netheriteSlurryRecipe);

        lore = new ArrayList<>();
        lore.add(SLURRIED_IRON_LORE);
        var slurriedIron = new ItemStack(BRICK);
        meta = slurriedIron.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Slurried Iron Ingot");
        meta.setLocalizedName("Slurried Iron Ingot");
        meta.setLore(lore);
        slurriedIron.setItemMeta(meta);
        var slurriedIronIngot = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "slurried_iron"), slurriedIron);
        slurriedIronIngot.addIngredient(1, IRON_INGOT);
        slurriedIronIngot.addIngredient(1, POTION);
        Diplomacy.getInstance().getServer().addRecipe(slurriedIronIngot);

        lore = new ArrayList<>();
        lore.add(SLURRIED_GOLD_LORE);
        var slurriedGold = new ItemStack(BRICK);
        meta = slurriedGold.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Slurried Gold Ingot");
        meta.setLocalizedName("Slurried Gold Ingot");
        meta.setLore(lore);
        slurriedGold.setItemMeta(meta);
        var slurriedGoldIngot = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "slurried_gold"), slurriedGold);
        slurriedGoldIngot.addIngredient(1, GOLD_INGOT);
        slurriedGoldIngot.addIngredient(1, POTION);
        Diplomacy.getInstance().getServer().addRecipe(slurriedGoldIngot);

        lore = new ArrayList<>();
        lore.add(SLURRIED_NETHERITE_LORE);
        var slurriedNetherite = new ItemStack(BRICK);
        meta = slurriedNetherite.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Slurried Netherite Ingot");
        meta.setLocalizedName("Slurried Netherite Ingot");
        meta.setLore(lore);
        slurriedNetherite.setItemMeta(meta);
        var slurriedNetheriteIngot = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "slurried_netherite"), slurriedNetherite);
        slurriedNetheriteIngot.addIngredient(1, NETHERITE_INGOT);
        slurriedNetheriteIngot.addIngredient(1, POTION);
        Diplomacy.getInstance().getServer().addRecipe(slurriedNetheriteIngot);


        var layers = new RecipeChoice.MaterialChoice(Tools.getInstance().layers);
        lore = new ArrayList<>();
        lore.add(NEW_LAYER_LORE);
        var layerItem = new ItemStack(STICK);
        var layerMeta = layerItem.getItemMeta();
        layerMeta.setLore(lore);
        layerItem.setItemMeta(layerMeta);

        var helmetLayer = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "helmet_layer"), layerItem);
        helmetLayer.shape("III", "IHI");
        helmetLayer.setIngredient('I', layers);
        helmetLayer.setIngredient('H', new RecipeChoice.MaterialChoice(Tools.getInstance().helmets));
        Diplomacy.getInstance().getServer().addRecipe(helmetLayer);

        var chestLayer = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chest_layer"), layerItem);
        chestLayer.shape("ICI", "III", "III");
        chestLayer.setIngredient('I', layers);
        chestLayer.setIngredient('C', new RecipeChoice.MaterialChoice(Tools.getInstance().chestplates));
        Diplomacy.getInstance().getServer().addRecipe(chestLayer);

        var leggingLayer = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "legging_layer"), layerItem);
        leggingLayer.shape("III", "ILI", "I I");
        leggingLayer.setIngredient('I', layers);
        leggingLayer.setIngredient('L', new RecipeChoice.MaterialChoice(Tools.getInstance().leggings));
        Diplomacy.getInstance().getServer().addRecipe(leggingLayer);

        var bootLayer = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "boot_layer"), layerItem);
        bootLayer.shape("IBI", "I I");
        bootLayer.setIngredient('I', layers);
        bootLayer.setIngredient('B', new RecipeChoice.MaterialChoice(Tools.getInstance().boots));
        Diplomacy.getInstance().getServer().addRecipe(bootLayer);

        var grenade = new ItemStack(FIREWORK_STAR);
        grenade.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        var grenadeMeta = grenade.getItemMeta();
        grenadeMeta.setDisplayName(ChatColor.RESET + "Grenade");
        grenadeMeta.setLocalizedName("Grenade");
        lore = new ArrayList<>();
        lore.add(GRENADE_LORE);
        lore.add(GRENADE_LORE_2);
        lore.add(GRENADE_LORE_3);
        lore.add("");
        lore.add(ChatColor.RED + "Grenade will explode 4-6 seconds after thrown.");
        grenadeMeta.setLore(lore);
        grenadeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        grenade.setItemMeta(grenadeMeta);

        var grenadeRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "grenade"), grenade);
        grenadeRecipe.shape("RII", "IFI", "III");
        grenadeRecipe.setIngredient('I', IRON_NUGGET);
        grenadeRecipe.setIngredient('R', REDSTONE);
        grenadeRecipe.setIngredient('F', FIRE_CHARGE);
        Diplomacy.getInstance().getServer().addRecipe(grenadeRecipe);

        var coarseSandPaper = new ItemStack(PAPER, 3);
        coarseSandPaper.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        var paperMeta = coarseSandPaper.getItemMeta();
        paperMeta.setDisplayName(ChatColor.RESET + "Coarse Sandpaper");
        paperMeta.setLocalizedName("Coarse Sandpaper");
        lore = new ArrayList<>();
        lore.add(COARSE_PAPER_LORE);
        paperMeta.setLore(lore);
        paperMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        coarseSandPaper.setItemMeta(paperMeta);

        var coarsePaperRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "coarse_paper"), coarseSandPaper);
        coarsePaperRecipe.shape("QQQ", "QSQ", "PPP");
        coarsePaperRecipe.setIngredient('Q', QUARTZ);
        coarsePaperRecipe.setIngredient('S', SLIME_BALL);
        coarsePaperRecipe.setIngredient('P', PAPER);
        Diplomacy.getInstance().getServer().addRecipe(coarsePaperRecipe);

        var fineSandPaper = new ItemStack(PAPER, 3);
        fineSandPaper.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        var paperMeta2 = fineSandPaper.getItemMeta();
        paperMeta2.setDisplayName(ChatColor.RESET + "Fine Sandpaper");
        paperMeta2.setLocalizedName("Coarse Sand Paper");
        lore = new ArrayList<>();
        lore.add(FINE_PAPER_LORE);
        paperMeta2.setLore(lore);
        paperMeta2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        fineSandPaper.setItemMeta(paperMeta2);

        var finePaperRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "fine_paper"), fineSandPaper);
        finePaperRecipe.shape(" D ", "DSD", "PPP");
        finePaperRecipe.setIngredient('D', SUGAR);
        finePaperRecipe.setIngredient('S', SLIME_BALL);
        finePaperRecipe.setIngredient('P', PAPER);
        Diplomacy.getInstance().getServer().addRecipe(finePaperRecipe);

        var sharp3 = new ItemStack(STICK);
        sharp3.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 3);
        var sharp3recipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "sharp3"), sharp3);

        var sharpTools = new ArrayList<>(Tools.getInstance().tools);
        sharpTools.add(ARROW);
        sharpTools.add(TIPPED_ARROW);
        sharpTools.add(SPECTRAL_ARROW);
        var toolChoice = new RecipeChoice.MaterialChoice(sharpTools);
        sharp3recipe.addIngredient(toolChoice);
        sharp3recipe.addIngredient(PAPER);
        Diplomacy.getInstance().getServer().addRecipe(sharp3recipe);

        var whetstone = new ItemStack(BRICK, 1);
        whetstone.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        var whetstoneMeta = whetstone.getItemMeta();
        whetstoneMeta.setDisplayName(ChatColor.RESET + "Whetstone");
        whetstoneMeta.setLocalizedName("Whetstone");
        lore = new ArrayList<>();
        lore.add(WHETSTONE_LORE);
        lore.add("");
        lore.add(REMAINING_USES_LORE);
        lore.add("200");
        whetstoneMeta.setLore(lore);
        whetstoneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        whetstone.setItemMeta(whetstoneMeta);

        var whetstoneRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "whetstone"), whetstone);
        whetstoneRecipe.shape("DDD", "III", "OOO");
        whetstoneRecipe.setIngredient('D', DIAMOND);
        whetstoneRecipe.setIngredient('I', IRON_INGOT);
        whetstoneRecipe.setIngredient('O', OBSIDIAN);
        Diplomacy.getInstance().getServer().addRecipe(whetstoneRecipe);

        var sharp5 = new ItemStack(STICK);
        sharp5.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
        var sharp5recipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "sharp5"), sharp5);

        sharp5recipe.addIngredient(toolChoice);
        sharp5recipe.addIngredient(BRICK);
        Diplomacy.getInstance().getServer().addRecipe(sharp5recipe);

        var waterstone = new ItemStack(NETHER_BRICK, 1);
        waterstone.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        var waterstoneMeta = waterstone.getItemMeta();
        waterstoneMeta.setDisplayName(ChatColor.RESET + "Waterstone");
        waterstoneMeta.setLocalizedName("Waterstone");
        lore = new ArrayList<>();
        lore.add(WATERSTONE_LORE);
        lore.add("");
        lore.add(REMAINING_USES_LORE);
        lore.add("250");
        waterstoneMeta.setLore(lore);
        waterstoneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        waterstone.setItemMeta(waterstoneMeta);

        var waterstoneRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "waterstone"), waterstone);
        waterstoneRecipe.shape("AGA", "GBG", "AGA");
        waterstoneRecipe.setIngredient('A', REDSTONE);
        waterstoneRecipe.setIngredient('G', GLOWSTONE_DUST);
        waterstoneRecipe.setIngredient('B', BRICK);
        Diplomacy.getInstance().getServer().addRecipe(waterstoneRecipe);

        var sharp6 = new ItemStack(STICK);
        sharp6.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
        var sharp6recipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "sharp6"), sharp6);

        sharp6recipe.addIngredient(toolChoice);
        sharp6recipe.addIngredient(NETHER_BRICK);
        sharp6recipe.addIngredient(POTION);
        Diplomacy.getInstance().getServer().addRecipe(sharp6recipe);


        var coarseBlade = new ItemStack(SMOOTH_QUARTZ_STAIRS, 1);
        coarseBlade.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        var coarseBladeMeta = coarseBlade.getItemMeta();
        coarseBladeMeta.setDisplayName(ChatColor.RESET + "Coarse Sharpening Blades");
        coarseBladeMeta.setLocalizedName("Coarse Sharpening Blades");
        lore = new ArrayList<>();
        lore.add(COARSE_BLADE_LORE);
        lore.add("");
        lore.add(REMAINING_USES_LORE);
        lore.add("400");
        coarseBladeMeta.setLore(lore);
        coarseBladeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        coarseBlade.setItemMeta(coarseBladeMeta);

        var coarseBladeRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "coarse_blade"), coarseBlade);
        coarseBladeRecipe.shape("B B", "OOO");
        coarseBladeRecipe.setIngredient('O', OBSIDIAN);
        coarseBladeRecipe.setIngredient('B', IRON_SWORD);
        Diplomacy.getInstance().getServer().addRecipe(coarseBladeRecipe);

        var sharp7 = new ItemStack(STICK);
        sharp7.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
        var sharp7recipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "sharp7"), sharp7);

        sharp7recipe.addIngredient(toolChoice);
        sharp7recipe.addIngredient(SMOOTH_QUARTZ_STAIRS);
        Diplomacy.getInstance().getServer().addRecipe(sharp7recipe);


        var fineBlade = new ItemStack(POLISHED_BLACKSTONE_STAIRS, 1);
        fineBlade.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        var fineBladeMeta = fineBlade.getItemMeta();
        fineBladeMeta.setDisplayName(ChatColor.RESET + "Fine Sharpening Blades");
        fineBladeMeta.setLocalizedName("Fine Sharpening Blades");
        lore = new ArrayList<>();
        lore.add(FINE_BLADE_LORE);
        lore.add("");
        lore.add(REMAINING_USES_LORE);
        lore.add("500");
        fineBladeMeta.setLore(lore);
        fineBladeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        fineBlade.setItemMeta(fineBladeMeta);

        var fineBladeRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "fine_blade"), fineBlade);
        fineBladeRecipe.shape("B B", "OOO");
        fineBladeRecipe.setIngredient('O', OBSIDIAN);
        fineBladeRecipe.setIngredient('B', NETHERITE_SWORD);
        Diplomacy.getInstance().getServer().addRecipe(fineBladeRecipe);

        var sharp8 = new ItemStack(STICK);
        sharp8.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 8);
        var sharp8recipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "sharp8"), sharp8);

        sharp8recipe.addIngredient(toolChoice);
        sharp8recipe.addIngredient(POLISHED_BLACKSTONE_STAIRS);
        Diplomacy.getInstance().getServer().addRecipe(sharp8recipe);


        var ironRod = new ItemStack(END_ROD, 1);
        var ironRodMeta = ironRod.getItemMeta();
        ironRodMeta.setDisplayName(ChatColor.RESET + "Iron Honing Rod");
        ironRodMeta.setLocalizedName("Iron Honing Rod");
        lore = new ArrayList<>();
        lore.add(IRON_ROD_LORE);
        lore.add("");
        lore.add(REMAINING_USES_LORE);
        lore.add("1000");
        ironRodMeta.setLore(lore);
        ironRodMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ironRod.setItemMeta(ironRodMeta);

        var ironRodRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_rod"), ironRod);
        ironRodRecipe.shape(" I", "HI", " I");
        var choice = new RecipeChoice.MaterialChoice(WOODEN_HOE, STONE_HOE, IRON_HOE, GOLDEN_HOE, DIAMOND_HOE, NETHERITE_HOE);
        ironRodRecipe.setIngredient('H', choice);
        ironRodRecipe.setIngredient('I', IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(ironRodRecipe);

        var sharp9 = new ItemStack(STICK);
        sharp9.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 9);
        var sharp9recipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "sharp9"), sharp9);

        sharp9recipe.addIngredient(toolChoice);
        sharp9recipe.addIngredient(END_ROD);
        Diplomacy.getInstance().getServer().addRecipe(sharp9recipe);

        var netheriteRod = new ItemStack(END_ROD, 1);
        netheriteRod.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 1);
        var netheriteRodMeta = netheriteRod.getItemMeta();
        netheriteRodMeta.setDisplayName(ChatColor.RESET + "Netherite Honing Rod");
        netheriteRodMeta.setLocalizedName("Netherite Honing Rod");
        lore = new ArrayList<>();
        lore.add(NETHERITE_ROD_LORE);
        lore.add("");
        lore.add(REMAINING_USES_LORE);
        lore.add("2500");
        netheriteRodMeta.setLore(lore);
        netheriteRodMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        netheriteRod.setItemMeta(netheriteRodMeta);

        var netheriteRodRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "netherite_rod"), netheriteRod);
        netheriteRodRecipe.shape(" I", "HI", " I");
        netheriteRodRecipe.setIngredient('H', choice);
        netheriteRodRecipe.setIngredient('I', NETHERITE_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(netheriteRodRecipe);

        var heavyArrow = new ItemStack(ARROW, 8);
        heavyArrow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);

        var heavyArrowRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "heavyArrow"), heavyArrow);
        heavyArrowRecipe.addIngredient(IRON_INGOT);
        heavyArrowRecipe.addIngredient(FEATHER);
        heavyArrowRecipe.addIngredient(STICK);
        Diplomacy.getInstance().getServer().addRecipe(heavyArrowRecipe);

        var blazedArrow = new ItemStack(ARROW, 8);
        blazedArrow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);

        var blazedArrowRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "blazedArrow"), blazedArrow);
        var arrowChoice = new RecipeChoice.MaterialChoice(ARROW, SPECTRAL_ARROW, TIPPED_ARROW);
        blazedArrowRecipe.shape("AAA", "ABA", "AAA");
        blazedArrowRecipe.setIngredient('A', arrowChoice);
        blazedArrowRecipe.setIngredient('B', BLAZE_POWDER);
        Diplomacy.getInstance().getServer().addRecipe(blazedArrowRecipe);

        var depthStrider1 = new ItemStack(CHAINMAIL_BOOTS);
        depthStrider1.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 1);

        var depthStrider1R = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "depthStrider1"), depthStrider1);
        var bootChoices = new RecipeChoice.MaterialChoice(LEATHER_BOOTS, CHAINMAIL_BOOTS, GOLDEN_BOOTS, IRON_BOOTS, DIAMOND_BOOTS, NETHERITE_BOOTS);
        depthStrider1R.shape("LLL", "LBL", "LLL");
        depthStrider1R.setIngredient('L', LEATHER);
        depthStrider1R.setIngredient('B', bootChoices);
        Diplomacy.getInstance().getServer().addRecipe(depthStrider1R);

        var depthStrider2 = new ItemStack(CHAINMAIL_BOOTS);
        depthStrider2.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 2);

        var depthStrider2R = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "depthStrider2"), depthStrider2);
        depthStrider2R.shape("LLL", "LBL", "LLL");
        depthStrider2R.setIngredient('L', SLIME_BALL);
        depthStrider2R.setIngredient('B', bootChoices);
        Diplomacy.getInstance().getServer().addRecipe(depthStrider2R);

        var depthStrider3 = new ItemStack(CHAINMAIL_BOOTS);
        depthStrider3.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 3);

        var depthStrider3R = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "depthStrider3"), depthStrider3);
        depthStrider3R.shape("LLL", "LBL", "LLL");
        depthStrider3R.setIngredient('L', PHANTOM_MEMBRANE);
        depthStrider3R.setIngredient('B', bootChoices);
        Diplomacy.getInstance().getServer().addRecipe(depthStrider3R);

        var thorns = new ItemStack(CHAINMAIL_CHESTPLATE);
        thorns.addUnsafeEnchantment(Enchantment.THORNS, 1);

        var thornsR = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "thorns"), thorns);
        thornsR.addIngredient(PRISMARINE_SHARD);
        thornsR.addIngredient(new RecipeChoice.MaterialChoice(LEATHER_CHESTPLATE, CHAINMAIL_CHESTPLATE, GOLDEN_CHESTPLATE,
                IRON_CHESTPLATE, DIAMOND_CHESTPLATE, NETHERITE_CHESTPLATE));
        Diplomacy.getInstance().getServer().addRecipe(thornsR);

        var quickCharge1 = new ItemStack(CROSSBOW);
        quickCharge1.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 1);

        var quickCharge1R = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "quickcharge1"), quickCharge1);
        quickCharge1R.shape("III", "IBI", "III");
        quickCharge1R.setIngredient('I', IRON_NUGGET);
        quickCharge1R.setIngredient('B', CROSSBOW);
        Diplomacy.getInstance().getServer().addRecipe(quickCharge1R);

        var quickCharge2 = new ItemStack(CROSSBOW);
        quickCharge2.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 2);

        var quickCharge2R = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "quickcharge2"), quickCharge2);
        quickCharge2R.addIngredient(CROSSBOW);
        quickCharge2R.addIngredient(SLIME_BLOCK);
        Diplomacy.getInstance().getServer().addRecipe(quickCharge2R);

        var quickCharge3 = new ItemStack(CROSSBOW);
        quickCharge3.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 3);

        var quickCharge3R = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "quickcharge3"), quickCharge3);
        quickCharge3R.shape(" D ", "DBD", " D ");
        quickCharge3R.setIngredient('D', REDSTONE);
        quickCharge3R.setIngredient('B', CROSSBOW);
        Diplomacy.getInstance().getServer().addRecipe(quickCharge3R);

        var quickCharge4 = new ItemStack(CROSSBOW);
        quickCharge4.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 4);

        var quickCharge4R = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "quickcharge4"), quickCharge4);
        quickCharge4R.shape("RMR", "MBM", "RMR");
        quickCharge4R.setIngredient('R', REDSTONE);
        quickCharge4R.setIngredient('B', CROSSBOW);
        quickCharge4R.setIngredient('M', IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(quickCharge4R);

        var quickCharge5 = new ItemStack(CROSSBOW);
        quickCharge5.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 5);

        var quickCharge5R = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "quickcharge5"), quickCharge5);
        quickCharge5R.addIngredient(NETHERITE_INGOT);
        quickCharge5R.addIngredient(NETHERITE_INGOT);
        quickCharge5R.addIngredient(CROSSBOW);
        Diplomacy.getInstance().getServer().addRecipe(quickCharge5R);

        var fishingRod = new ItemStack(FISHING_ROD);
        fishingRod.addUnsafeEnchantment(Enchantment.LURE, 3);

        var lure = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "lure"), fishingRod);
        lure.addIngredient(FISHING_ROD);
        var foodChoice = new RecipeChoice.MaterialChoice(
                ROTTEN_FLESH, MUTTON, COOKED_MUTTON,
                COOKED_PORKCHOP, COOKED_SALMON, COOKED_BEEF,
                COOKED_CHICKEN, COOKED_COD, COOKED_RABBIT, BEEF,
                CHICKEN, MUTTON, PORKCHOP, RABBIT, RABBIT_FOOT,
                PUFFERFISH, COD, SALMON, TROPICAL_FISH, ENCHANTED_GOLDEN_APPLE,
                GOLDEN_APPLE, GOLDEN_CARROT, POTATO, BEETROOT, BREAD, CARROT,
                APPLE, DRIED_KELP, MELON, POISONOUS_POTATO, POTATO, PUMPKIN_PIE,
                CAKE, COOKIE, SWEET_BERRIES, SPIDER_EYE
        );
        lure.addIngredient(foodChoice);
        Diplomacy.getInstance().getServer().addRecipe(lure);

        var fireAspect = new ItemStack(STONE_SWORD, 2);
        fireAspect.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 1);

        var fireAspectR = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "fireAspect"), fireAspect);
        fireAspectR.addIngredient(BLAZE_POWDER);
        fireAspectR.addIngredient(new RecipeChoice.MaterialChoice(WOODEN_SWORD, STONE_SWORD, IRON_SWORD,
                GOLDEN_SWORD, DIAMOND_SWORD, NETHERITE_SWORD));
        Diplomacy.getInstance().getServer().addRecipe(fireAspectR);

        var rename = new ItemStack(STONE_SWORD, 3);

        var renameR = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "rename"), rename);
        var choices = new ArrayList<>(Tools.getInstance().tools);
        choices.addAll(Tools.getInstance().tools);
        choices.addAll(Tools.getInstance().helmets);
        choices.addAll(Tools.getInstance().chestplates);
        choices.addAll(Tools.getInstance().leggings);
        choices.addAll(Tools.getInstance().boots);
        choices.add(SHEARS);
        choices.add(Material.BOW);
        choices.add(Material.CROSSBOW);
        choices.add(Material.FISHING_ROD);
        choices.add(Material.SHIELD);
        var renameChoice = new RecipeChoice.MaterialChoice(choices);
        renameR.addIngredient(NAME_TAG);
        renameR.addIngredient(renameChoice);
        Diplomacy.getInstance().getServer().addRecipe(renameR);


    }
}
