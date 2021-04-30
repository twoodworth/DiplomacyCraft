package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;

import java.util.ArrayList;

import static org.bukkit.Material.*;

public class DiplomacyRecipes {
    private static DiplomacyRecipes instance = null;
    public ArrayList<Material> choices = new ArrayList<>();

    public static DiplomacyRecipes getInstance() {
        if (instance == null) {
            instance = new DiplomacyRecipes();
        }
        return instance;
    }

    public void loadRecipes() {
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

        // grenade
        var grenade = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GRENADE, 2);

        var grenadeRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "grenade"), grenade);
        grenadeRecipe.shape("RII", "IFI", "III");
        grenadeRecipe.setIngredient('I', IRON_NUGGET);
        grenadeRecipe.setIngredient('R', REDSTONE);
        grenadeRecipe.setIngredient('F', FIRE_CHARGE);
        Diplomacy.getInstance().getServer().addRecipe(grenadeRecipe);

        // apple of life
        var apple = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.APPLE_OF_LIFE, 1);
        var appleRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "apple_of_life"), apple);
        appleRecipe.addIngredient(6, GLOWSTONE_DUST);
        appleRecipe.addIngredient(APPLE);
        Diplomacy.getInstance().getServer().addRecipe(appleRecipe);

        // magical dust
        var dust = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, 6);
        var dustRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "magical_dust"), dust);
        dustRecipe.addIngredient(GOLDEN_APPLE);
        Diplomacy.getInstance().getServer().addRecipe(dustRecipe);

        // bottle of xp
        var xp = new ItemStack(EXPERIENCE_BOTTLE);
        var xpRecipe = new ShapelessRecipe(new NamespacedKey(Diplomacy.getInstance(), "xp_bottle"), xp);
        xpRecipe.addIngredient(4, GLOWSTONE_DUST);
        xpRecipe.addIngredient(GLASS_BOTTLE);
        Diplomacy.getInstance().getServer().addRecipe(xpRecipe);

        // guard crystal
        var guard = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GUARD_CRYSTAL, 1);
        var guardRecipe = new ShapedRecipe(new NamespacedKey(Diplomacy.getInstance(), "guard_crystal"), guard);
        guardRecipe.shape("GGG", "GAG", "GDG");
        guardRecipe.setIngredient('G', GLASS_PANE);
        guardRecipe.setIngredient('A', GOLDEN_APPLE);
        guardRecipe.setIngredient('D', DIAMOND);
        Diplomacy.getInstance().getServer().addRecipe(guardRecipe);

    }
}
