package me.tedwoodworth.diplomacy.Items;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CustomItemRecipes {
    private static CustomItemRecipes instance = null;

    public static CustomItemRecipes getInstance() {
        if (instance == null) {
            instance = new CustomItemRecipes();
        }
        return instance;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new CustomItemRecipes.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onBrew(BrewEvent event) {
            var ingredient = event.getContents().getIngredient();
            if (CustomItemGenerator.getInstance().isCustomItem(ingredient)) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onPrepareCraft(PrepareItemCraftEvent event) {
            var inv = event.getInventory();
            var matrix = inv.getMatrix();
            var result = inv.getResult();
            if (result == null) {
                return;
            } else if (CustomItemGenerator.getInstance().isCustomItem(result)) {
                int id = CustomItemGenerator.getInstance().getCustomID(result);
                CustomItems.CustomID cid = CustomItems.getInstance().getEnum(id);
                switch (cid) {
                    case APPLE_OF_LIFE -> {
                        int count = 0;
                        for (var item : matrix) {
                            if (item != null && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                count++;
                            }
                        }
                        if (count != 6) {
                            inv.setResult(new ItemStack(Material.AIR));
                            return;
                        }
                    }
                    case MAGICAL_DUST, GUARD_CRYSTAL -> {
                        for (var item : matrix) {
                            if (item != null && item.getType() == Material.GOLDEN_APPLE && !CustomItemGenerator.getInstance().isCustomItem(item)) {
                                inv.setResult(new ItemStack(Material.AIR));
                                return;
                            }
                        }
                    }
                    default -> {
                        for (var item : matrix) {
                            if (item != null && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                inv.setResult(new ItemStack(Material.AIR));
                                return;
                            }
                        }
                    }
                }
            } else {
                switch (result.getType()) {
                    case EXPERIENCE_BOTTLE -> {
                        for (var item : matrix) {
                            if (item != null && item.getType() == Material.GLOWSTONE_DUST && !CustomItemGenerator.getInstance().isCustomItem(item)) {
                                inv.setResult(new ItemStack(Material.AIR));
                                return;
                            }
                        }
                    }
                    default -> {
                        for (var item : matrix) {
                            if (item != null && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                inv.setResult(new ItemStack(Material.AIR));
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
}
