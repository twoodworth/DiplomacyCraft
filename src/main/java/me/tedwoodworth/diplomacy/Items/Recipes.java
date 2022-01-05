//package me.tedwoodworth.diplomacy.Items;
//
//import me.tedwoodworth.diplomacy.Diplomacy;
//import org.bukkit.Material;
//import org.bukkit.NamespacedKey;
//import org.bukkit.inventory.FurnaceRecipe;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.ShapedRecipe;
//import org.bukkit.inventory.ShapelessRecipe;
//
//import java.util.ArrayList;
//
//import static org.bukkit.Material.*;
//
//public class Recipes {
//    private static Recipes instance = null;
//    public ArrayList<Material> choices = new ArrayList<>();
//
//    public static Recipes getInstance() {
//        if (instance == null) {
//            instance = new Recipes();
//        }
//        return instance;
//    }
//
//    public void loadRecipes() {
//        // Chain armor
//        var chainHelm = new ItemStack(CHAINMAIL_HELMET);
//        var chainHelmR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_helm"), chainHelm);
//        chainHelmR.shape("NNN", "N N");
//        chainHelmR.setIngredient('N', IRON_NUGGET);
//        Diplomacy.getInstance().getServer().addRecipe(chainHelmR);
//
//        var chainChest = new ItemStack(CHAINMAIL_CHESTPLATE);
//        var chainChestR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_chest"), chainChest);
//        chainChestR.shape("N N", "NNN", "NNN");
//        chainChestR.setIngredient('N', IRON_NUGGET);
//        Diplomacy.getInstance().getServer().addRecipe(chainChestR);
//
//        var chainLegs = new ItemStack(CHAINMAIL_LEGGINGS);
//        var chainLegsR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_legs"), chainLegs);
//        chainLegsR.shape("NNN", "N N", "N N");
//        chainLegsR.setIngredient('N', IRON_NUGGET);
//        Diplomacy.getInstance().getServer().addRecipe(chainLegsR);
//
//        var chainBoots = new ItemStack(CHAINMAIL_BOOTS);
//        var chainBootsR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "chain_boots"), chainBoots);
//        chainBootsR.shape("N N", "N N");
//        chainBootsR.setIngredient('N', IRON_NUGGET);
//        Diplomacy.getInstance().getServer().addRecipe(chainBootsR);
//
//        var nameTag = new ItemStack(NAME_TAG);
//        var nameTagR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "nametag"), nameTag);
//        nameTagR.shape("LLI");
//        nameTagR.setIngredient('L', LEATHER);
//        nameTagR.setIngredient('I', IRON_INGOT);
//        Diplomacy.getInstance().getServer().addRecipe(nameTagR);
//
//        var saddle = new ItemStack(SADDLE);
//        var saddleR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "saddle"), saddle);
//        saddleR.shape("LLL", "L L", "I I");
//        saddleR.setIngredient('L', LEATHER);
//        saddleR.setIngredient('I', IRON_INGOT);
//        Diplomacy.getInstance().getServer().addRecipe(saddleR);
//
//        var ironHorse = new ItemStack(IRON_HORSE_ARMOR);
//        var ironHorseR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "iron_horse_armor"), ironHorse);
//        ironHorseR.shape("  I", "ISI", "I I");
//        ironHorseR.setIngredient('S', SADDLE);
//        ironHorseR.setIngredient('I', IRON_INGOT);
//        Diplomacy.getInstance().getServer().addRecipe(ironHorseR);
//
//        var goldHorse = new ItemStack(GOLDEN_HORSE_ARMOR);
//        var goldHorseR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "gold_horse_armor"), goldHorse);
//        goldHorseR.shape("  G", "GSG", "G G");
//        goldHorseR.setIngredient('S', SADDLE);
//        goldHorseR.setIngredient('G', GOLD_INGOT);
//        Diplomacy.getInstance().getServer().addRecipe(goldHorseR);
//
//        var diamondHorse = new ItemStack(DIAMOND_HORSE_ARMOR);
//        var diamondHorseR = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "diamond_horse_armor"), diamondHorse);
//        diamondHorseR.shape("  D", "DSD", "D D");
//        diamondHorseR.setIngredient('S', SADDLE);
//        diamondHorseR.setIngredient('D', DIAMOND);
//        Diplomacy.getInstance().getServer().addRecipe(diamondHorseR);
//
//        // grenade
//        var grenade = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GRENADE, 2);
//
//        var grenadeRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "grenade"), grenade);
//        grenadeRecipe.shape("RII", "IFI", "III");
//        grenadeRecipe.setIngredient('I', IRON_NUGGET);
//        grenadeRecipe.setIngredient('R', REDSTONE);
//        grenadeRecipe.setIngredient('F', FIRE_CHARGE);
//        Diplomacy.getInstance().getServer().addRecipe(grenadeRecipe);
//
//        // apple of life
//        var apple = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.APPLE_OF_LIFE, 1);
//        var appleRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "apple_of_life"), apple);
//        appleRecipe.shape("DDD", "DAD", "DDD");
//        appleRecipe.setIngredient('D', GLOWSTONE_DUST);
//        appleRecipe.setIngredient('A', APPLE);
//        Diplomacy.getInstance().getServer().addRecipe(appleRecipe);
//
//        // magical dust
//        var dust = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, 8);
//        var dustRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "magical_dust_crafting"), dust);
//        dustRecipe.addIngredient(GOLDEN_APPLE);
//        Diplomacy.getInstance().getServer().addRecipe(dustRecipe);
//
//        // magical dust (furnace)
//        var dust2 = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, 1);
//        var dustRecipe2 = new FurnaceRecipe(new NamespacedKey(Diplomacy.getInstance(), "magical_dust_smelting"), dust2, ENCHANTED_BOOK, 5.25F, 200);
//        Diplomacy.getInstance().getServer().addRecipe(dustRecipe2);
//
//        // bottle of xp
//        var xp = new ItemStack(EXPERIENCE_BOTTLE);
//        var xpRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "xp_bottle"), xp);
//        xpRecipe.addIngredient(4, GLOWSTONE_DUST);
//        xpRecipe.addIngredient(GLASS_BOTTLE);
//        Diplomacy.getInstance().getServer().addRecipe(xpRecipe);
//
//        // guard crystal
//        var guard = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GUARD_CRYSTAL, 1);
//        var guardRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "guard_crystal"), guard);
//        guardRecipe.shape("GGG", "GAG", "GDG");
//        guardRecipe.setIngredient('G', GLASS);
//        guardRecipe.setIngredient('A', GOLDEN_APPLE);
//        guardRecipe.setIngredient('D', DIAMOND);
//        Diplomacy.getInstance().getServer().addRecipe(guardRecipe);
//
//    }
//}
