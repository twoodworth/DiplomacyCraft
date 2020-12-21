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

public class DiplomacyRecipes {
    private static DiplomacyRecipes instance = null;
    public String NEW_LORE = "New";
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
    public String GRENADE_LORE_3 = ChatColor.YELLOW + "Hold Shift: " + ChatColor.GRAY + "Wait until letting go to throw";

    public static DiplomacyRecipes getInstance() {
        if (instance == null) {
            instance = new DiplomacyRecipes();
        }
        return instance;
    }

    public void loadRecipes() {
        // Enchanting Table
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


        // Chisels
        var planks = new RecipeChoice.MaterialChoice(Tools.getInstance().planks);
        var stones = new RecipeChoice.MaterialChoice(Arrays.asList(Material.COBBLESTONE, Material.BLACKSTONE));

        var chiselLore = new ArrayList<String>();
        chiselLore.add(NEW_LORE);
        chiselLore.add(CHISEL_LORE);

        var woodChisel = new ItemStack(Material.WOODEN_HOE);
        meta = woodChisel.getItemMeta();
        meta.setLore(chiselLore);
        meta.setLocalizedName("Wooden Chisel");
        meta.setDisplayName(ChatColor.RESET + "Wooden Chisel");
        woodChisel.setItemMeta(meta);
        ShapedRecipe woodChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "wooden_chisel"), woodChisel);
        woodChiselR.shape("SMM");
        woodChiselR.setIngredient('S', Material.STICK);
        woodChiselR.setIngredient('M', planks);
        Diplomacy.getInstance().getServer().addRecipe(woodChiselR);

        var stoneChisel = new ItemStack(Material.STONE_HOE);
        meta = stoneChisel.getItemMeta();
        meta.setLore(chiselLore);
        meta.setDisplayName(ChatColor.RESET + "Stone Chisel");
        meta.setLocalizedName("Stone Chisel");
        stoneChisel.setItemMeta(meta);
        ShapedRecipe stoneChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "stone_chisel"), stoneChisel);
        stoneChiselR.shape("SMM");
        stoneChiselR.setIngredient('S', Material.STICK);
        stoneChiselR.setIngredient('M', stones);
        Diplomacy.getInstance().getServer().addRecipe(stoneChiselR);

        var goldChisel = new ItemStack(Material.GOLDEN_HOE);
        meta = goldChisel.getItemMeta();
        meta.setLore(chiselLore);
        goldChisel.setItemMeta(meta);
        meta.setDisplayName(ChatColor.RESET + "Golden Chisel");
        meta.setLocalizedName("Golden Chisel");
        ShapedRecipe goldChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_chisel"), goldChisel);
        goldChiselR.shape("SMM");
        goldChiselR.setIngredient('S', Material.STICK);
        goldChiselR.setIngredient('M', Material.GOLD_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(goldChiselR);

        var ironChisel = new ItemStack(Material.IRON_HOE);
        meta = ironChisel.getItemMeta();
        meta.setLore(chiselLore);
        meta.setDisplayName(ChatColor.RESET + "Iron Chisel");
        meta.setLocalizedName("Iron Chisel");
        ironChisel.setItemMeta(meta);
        ShapedRecipe ironChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_chisel"), ironChisel);
        ironChiselR.shape("SMM");
        ironChiselR.setIngredient('S', Material.STICK);
        ironChiselR.setIngredient('M', Material.IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(ironChiselR);

        var diamondChisel = new ItemStack(Material.DIAMOND_HOE);
        meta = diamondChisel.getItemMeta();
        meta.setLore(chiselLore);
        meta.setDisplayName(ChatColor.RESET + "Diamond Chisel");
        meta.setLocalizedName("Diamond Chisel");
        diamondChisel.setItemMeta(meta);
        ShapedRecipe diamondChiselR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "diamond_chisel"), diamondChisel);
        diamondChiselR.shape("SMM");
        diamondChiselR.setIngredient('S', Material.STICK);
        diamondChiselR.setIngredient('M', Material.DIAMOND);
        Diplomacy.getInstance().getServer().addRecipe(diamondChiselR);


        // Knives

        var knifeLore = new ArrayList<String>();
        knifeLore.add(NEW_LORE);
        knifeLore.add(KNIFE_LORE);

        var woodenKnife = new ItemStack(Material.WOODEN_SWORD);
        meta = woodenKnife.getItemMeta();
        meta.setLore(knifeLore);
        meta.setLocalizedName("Wooden Hunting Knife");
        meta.setDisplayName(ChatColor.RESET + "Wooden Hunting Knife");
        woodenKnife.setItemMeta(meta);
        ShapedRecipe woodenKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "wooden_knife"), woodenKnife);
        woodenKnifeR.shape("M", "S");
        woodenKnifeR.setIngredient('S', Material.STICK);
        woodenKnifeR.setIngredient('M', planks);
        Diplomacy.getInstance().getServer().addRecipe(woodenKnifeR);

        var stoneKnife = new ItemStack(Material.STONE_SWORD);
        meta = stoneKnife.getItemMeta();
        meta.setLore(knifeLore);
        meta.setDisplayName(ChatColor.RESET + "Stone Hunting Knife");
        meta.setLocalizedName("Stone Hunting Knife");
        stoneKnife.setItemMeta(meta);
        ShapedRecipe stoneKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "stone_knife"), stoneKnife);
        stoneKnifeR.shape("M", "S");
        stoneKnifeR.setIngredient('S', Material.STICK);
        stoneKnifeR.setIngredient('M', stones);
        Diplomacy.getInstance().getServer().addRecipe(stoneKnifeR);

        var goldKnife = new ItemStack(Material.GOLDEN_SWORD);
        meta = goldKnife.getItemMeta();
        meta.setLore(knifeLore);
        goldKnife.setItemMeta(meta);
        meta.setDisplayName(ChatColor.RESET + "Golden Hunting Knife");
        meta.setLocalizedName("Golden Hunting Knife");
        ShapedRecipe goldKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_knife"), goldKnife);
        goldKnifeR.shape("M", "S");
        goldKnifeR.setIngredient('S', Material.STICK);
        goldKnifeR.setIngredient('M', Material.GOLD_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(goldKnifeR);

        var ironKnife = new ItemStack(Material.IRON_SWORD);
        meta = ironKnife.getItemMeta();
        meta.setLore(knifeLore);
        meta.setDisplayName(ChatColor.RESET + "Iron Hunting Knife");
        meta.setLocalizedName("Iron Hunting Knife");
        ironKnife.setItemMeta(meta);
        ShapedRecipe ironKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_knife"), ironKnife);
        ironKnifeR.shape("M", "S");
        ironKnifeR.setIngredient('S', Material.STICK);
        ironKnifeR.setIngredient('M', Material.IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(ironKnifeR);

        var diamondKnife = new ItemStack(Material.DIAMOND_SWORD);
        meta = diamondKnife.getItemMeta();
        meta.setLore(knifeLore);
        meta.setDisplayName(ChatColor.RESET + "Diamond Hunting Knife");
        meta.setLocalizedName("Diamond Hunting Knife");
        diamondKnife.setItemMeta(meta);
        ShapedRecipe diamondKnifeR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "diamond_knife"), diamondKnife);
        diamondKnifeR.shape("M", "S");
        diamondKnifeR.setIngredient('S', Material.STICK);
        diamondKnifeR.setIngredient('M', Material.DIAMOND);
        Diplomacy.getInstance().getServer().addRecipe(diamondKnifeR);


        // Saws

        var sawLore = new ArrayList<String>();
        sawLore.add(NEW_LORE);
        sawLore.add(SAW_LORE);

        var woodenSaw = new ItemStack(Material.WOODEN_AXE);
        meta = woodenSaw.getItemMeta();
        meta.setLore(sawLore);
        meta.setLocalizedName("Wooden Saw");
        meta.setDisplayName(ChatColor.RESET + "Wooden Saw");
        woodenSaw.setItemMeta(meta);
        ShapedRecipe woodenSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "wooden_saw"), woodenSaw);
        woodenSawR.shape(" S ", "S S", "MMM");
        woodenSawR.setIngredient('S', Material.STICK);
        woodenSawR.setIngredient('M', planks);
        Diplomacy.getInstance().getServer().addRecipe(woodenSawR);

        var stoneSaw = new ItemStack(Material.STONE_AXE);
        meta = stoneSaw.getItemMeta();
        meta.setLore(sawLore);
        meta.setDisplayName(ChatColor.RESET + "Stone Saw");
        meta.setLocalizedName("Stone Saw");
        stoneSaw.setItemMeta(meta);
        ShapedRecipe stoneSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "stone_saw"), stoneSaw);
        stoneSawR.shape(" S ", "S S", "MMM");
        stoneSawR.setIngredient('S', Material.STICK);
        stoneSawR.setIngredient('M', stones);
        Diplomacy.getInstance().getServer().addRecipe(stoneSawR);

        var goldSaw = new ItemStack(Material.GOLDEN_AXE);
        meta = goldSaw.getItemMeta();
        meta.setLore(sawLore);
        goldSaw.setItemMeta(meta);
        meta.setDisplayName(ChatColor.RESET + "Golden Saw");
        meta.setLocalizedName("Golden Saw");
        ShapedRecipe goldenSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_saw"), goldSaw);
        goldenSawR.shape(" S ", "S S", "MMM");
        goldenSawR.setIngredient('S', Material.STICK);
        goldenSawR.setIngredient('M', Material.GOLD_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(goldenSawR);

        var ironSaw = new ItemStack(Material.IRON_AXE);
        meta = ironSaw.getItemMeta();
        meta.setLore(sawLore);
        meta.setDisplayName(ChatColor.RESET + "Iron Saw");
        meta.setLocalizedName("Iron Saw");
        ironSaw.setItemMeta(meta);
        ShapedRecipe ironSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_saw"), ironSaw);
        ironSawR.shape(" S ", "S S", "MMM");
        ironSawR.setIngredient('S', Material.STICK);
        ironSawR.setIngredient('M', Material.IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(ironSawR);

        var diamondSaw = new ItemStack(Material.DIAMOND_AXE);
        meta = diamondSaw.getItemMeta();
        meta.setLore(sawLore);
        meta.setDisplayName(ChatColor.RESET + "Diamond Saw");
        meta.setLocalizedName("Diamond Saw");
        diamondSaw.setItemMeta(meta);
        ShapedRecipe diamondSawR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "diamond_saw"), diamondSaw);
        diamondSawR.shape(" S ", "S S", "MMM");
        diamondSawR.setIngredient('S', Material.STICK);
        diamondSawR.setIngredient('M', Material.DIAMOND);
        Diplomacy.getInstance().getServer().addRecipe(diamondSawR);

        // Chain armor

        var chainHelm = new ItemStack(Material.CHAINMAIL_HELMET);
        var chainHelmR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_helm"), chainHelm);
        chainHelmR.shape("NNN", "N N");
        chainHelmR.setIngredient('N', Material.IRON_NUGGET);
        Diplomacy.getInstance().getServer().addRecipe(chainHelmR);

        var chainChest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        var chainChestR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_chest"), chainChest);
        chainChestR.shape("N N", "NNN", "NNN");
        chainChestR.setIngredient('N', Material.IRON_NUGGET);
        Diplomacy.getInstance().getServer().addRecipe(chainChestR);

        var chainLegs = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        var chainLegsR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_legs"), chainLegs);
        chainLegsR.shape("NNN", "N N", "N N");
        chainLegsR.setIngredient('N', Material.IRON_NUGGET);
        Diplomacy.getInstance().getServer().addRecipe(chainLegsR);

        var chainBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
        var chainBootsR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_boots"), chainBoots);
        chainBootsR.shape("N N", "N N");
        chainBootsR.setIngredient('N', Material.IRON_NUGGET);
        Diplomacy.getInstance().getServer().addRecipe(chainBootsR);


        // Metal Dust
        FurnaceRecipe ironNugget = new FurnaceRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_nugget"),
                new ItemStack(Material.IRON_NUGGET),
                Material.SUGAR, 0, 200);
        Diplomacy.getInstance().getServer().addRecipe(ironNugget);

        FurnaceRecipe goldNugget = new FurnaceRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_nugget"),
                new ItemStack(Material.GOLD_NUGGET),
                Material.GLOWSTONE_DUST, 0, 200);
        Diplomacy.getInstance().getServer().addRecipe(goldNugget);

        FurnaceRecipe ancientNuggetR = new FurnaceRecipe(new NamespacedKey(Diplomacy.getInstance(), "netherite_Scrap"),
                new ItemStack(Material.NETHERITE_SCRAP),
                Material.REDSTONE, 0, 200);
        Diplomacy.getInstance().getServer().addRecipe(ancientNuggetR);

        // Sifter
        var sifterLore = new ArrayList<String>();
        sifterLore.add(SIFTER_LORE);
        var sifter = new ItemStack(Material.CHAINMAIL_HELMET);
        meta = sifter.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Wooden Sifter");
        meta.setLocalizedName("Wooden Sifter");
        meta.setLore(sifterLore);
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);

        var woodenSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "wooden_sifter"), sifter);
        woodenSifter.shape("NNN", "NNN", "NNN");
        woodenSifter.setIngredient('N', Material.STICK);
        Diplomacy.getInstance().getServer().addRecipe(woodenSifter);

        meta.setDisplayName(ChatColor.RESET + "String Sifter");
        meta.setLocalizedName("String Sifter");
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
        var stringSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "string_sifter"), sifter);
        stringSifter.shape("NSN", "NSN", "NSN");
        stringSifter.setIngredient('N', Material.STICK);
        stringSifter.setIngredient('S', Material.STRING);
        Diplomacy.getInstance().getServer().addRecipe(stringSifter);

        meta.setDisplayName(ChatColor.RESET + "Chain Sifter");
        meta.setLocalizedName("Chain Sifter");
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
        sifter.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        var chainSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_sifter"), sifter);
        chainSifter.shape("NNN", "NSN", "NNN");
        chainSifter.setIngredient('N', Material.IRON_NUGGET);
        chainSifter.setIngredient('S', Material.IRON_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(chainSifter);

        meta.setDisplayName(ChatColor.RESET + "Redstone Sifter");
        meta.setLocalizedName("Redstone Sifter");
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 4);
        sifter.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        var redstoneSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "redstone_sifter"), sifter);
        redstoneSifter.shape(" R ", "RSR", " R ");
        redstoneSifter.setIngredient('R', Material.REDSTONE);
        redstoneSifter.setIngredient('S', Material.CHAINMAIL_HELMET);
        Diplomacy.getInstance().getServer().addRecipe(redstoneSifter);


        meta.setDisplayName(ChatColor.RESET + "Netherite Sifter");
        meta.setLocalizedName("Netherite Sifter");
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 7);
        sifter.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        var netheriteSifter = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "netherite_sifter"), sifter);
        netheriteSifter.shape("NNN", "NSN", "NNN");
        netheriteSifter.setIngredient('N', Material.IRON_NUGGET);
        netheriteSifter.setIngredient('S', Material.NETHERITE_INGOT);
        Diplomacy.getInstance().getServer().addRecipe(netheriteSifter);

        lore = new ArrayList<>();
        lore.add(IRON_SLURRY_LORE);
        var ironSlurry = new ItemStack(Material.POTION, 3);
        meta = ironSlurry.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Iron Slurry");
        meta.setLocalizedName("Iron Slurry");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ((PotionMeta) meta).setColor(Color.fromRGB(130, 107, 92));
        ironSlurry.setItemMeta(meta);
        var ironSlurryRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_slurry"), ironSlurry);
        ironSlurryRecipe.addIngredient(1, Material.CLAY_BALL);
        ironSlurryRecipe.addIngredient(1, Material.CHARCOAL);
        ironSlurryRecipe.addIngredient(1, Material.SUGAR);
        ironSlurryRecipe.addIngredient(1, Material.BONE_MEAL);
        ironSlurryRecipe.addIngredient(1, Material.GRAVEL);
        ironSlurryRecipe.addIngredient(1, Material.SAND);
        ironSlurryRecipe.addIngredient(3, Material.POTION);
        Diplomacy.getInstance().getServer().addRecipe(ironSlurryRecipe);

        lore = new ArrayList<>();
        lore.add(GOLD_SLURRY_LORE);
        var goldSlurry = new ItemStack(Material.POTION, 3);
        meta = goldSlurry.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Gold Slurry");
        meta.setLocalizedName("Gold Slurry");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ((PotionMeta) meta).setColor(Color.fromRGB(199, 160, 62));
        goldSlurry.setItemMeta(meta);
        var goldSlurryRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_slurry"), goldSlurry);
        goldSlurryRecipe.addIngredient(1, Material.CLAY_BALL);
        goldSlurryRecipe.addIngredient(1, Material.CHARCOAL);
        goldSlurryRecipe.addIngredient(1, Material.GLOWSTONE_DUST);
        goldSlurryRecipe.addIngredient(1, Material.BONE_MEAL);
        goldSlurryRecipe.addIngredient(1, Material.GRAVEL);
        goldSlurryRecipe.addIngredient(1, Material.SAND);
        goldSlurryRecipe.addIngredient(3, Material.POTION);
        Diplomacy.getInstance().getServer().addRecipe(goldSlurryRecipe);

        lore = new ArrayList<>();
        lore.add(NETHERITE_SLURRY_LORE);
        var netheriteSlurry = new ItemStack(Material.POTION, 3);
        meta = netheriteSlurry.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Netherite Slurry");
        meta.setLocalizedName("Netherite Slurry");
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ((PotionMeta) meta).setColor(Color.fromRGB(50, 0, 10));
        netheriteSlurry.setItemMeta(meta);
        var netheriteSlurryRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "netherite_slurry"), netheriteSlurry);
        netheriteSlurryRecipe.addIngredient(1, Material.CLAY_BALL);
        netheriteSlurryRecipe.addIngredient(1, Material.CHARCOAL);
        netheriteSlurryRecipe.addIngredient(1, Material.REDSTONE);
        netheriteSlurryRecipe.addIngredient(1, Material.BONE_MEAL);
        netheriteSlurryRecipe.addIngredient(1, Material.GRAVEL);
        netheriteSlurryRecipe.addIngredient(1, Material.SAND);
        netheriteSlurryRecipe.addIngredient(3, Material.POTION);
        Diplomacy.getInstance().getServer().addRecipe(netheriteSlurryRecipe);

        lore = new ArrayList<>();
        lore.add(SLURRIED_IRON_LORE);
        var slurriedIron = new ItemStack(Material.BRICK);
        meta = slurriedIron.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Slurried Iron Ingot");
        meta.setLocalizedName("Slurried Iron Ingot");
        meta.setLore(lore);
        slurriedIron.setItemMeta(meta);
        var slurriedIronIngot = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "slurried_iron"), slurriedIron);
        slurriedIronIngot.addIngredient(1, Material.IRON_INGOT);
        slurriedIronIngot.addIngredient(1, Material.POTION);
        Diplomacy.getInstance().getServer().addRecipe(slurriedIronIngot);

        lore = new ArrayList<>();
        lore.add(SLURRIED_GOLD_LORE);
        var slurriedGold = new ItemStack(Material.BRICK);
        meta = slurriedGold.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Slurried Gold Ingot");
        meta.setLocalizedName("Slurried Gold Ingot");
        meta.setLore(lore);
        slurriedGold.setItemMeta(meta);
        var slurriedGoldIngot = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "slurried_gold"), slurriedGold);
        slurriedGoldIngot.addIngredient(1, Material.GOLD_INGOT);
        slurriedGoldIngot.addIngredient(1, Material.POTION);
        Diplomacy.getInstance().getServer().addRecipe(slurriedGoldIngot);

        lore = new ArrayList<>();
        lore.add(SLURRIED_NETHERITE_LORE);
        var slurriedNetherite = new ItemStack(Material.BRICK);
        meta = slurriedNetherite.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "Slurried Netherite Ingot");
        meta.setLocalizedName("Slurried Netherite Ingot");
        meta.setLore(lore);
        slurriedNetherite.setItemMeta(meta);
        var slurriedNetheriteIngot = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "slurried_netherite"), slurriedNetherite);
        slurriedNetheriteIngot.addIngredient(1, Material.NETHERITE_INGOT);
        slurriedNetheriteIngot.addIngredient(1, Material.POTION);
        Diplomacy.getInstance().getServer().addRecipe(slurriedNetheriteIngot);


        var layers = new RecipeChoice.MaterialChoice(Tools.getInstance().layers);
        lore = new ArrayList<>();
        lore.add(NEW_LAYER_LORE);
        var layerItem = new ItemStack(Material.STICK);
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

        var grenade = new ItemStack(Material.FIREWORK_STAR);
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
        grenadeRecipe.setIngredient('I', Material.IRON_NUGGET);
        grenadeRecipe.setIngredient('R', Material.REDSTONE);
        grenadeRecipe.setIngredient('F', Material.FIRE_CHARGE);
        Diplomacy.getInstance().getServer().addRecipe(grenadeRecipe);


    }
}
