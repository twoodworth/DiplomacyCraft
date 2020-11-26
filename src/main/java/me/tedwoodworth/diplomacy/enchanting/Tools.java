package me.tedwoodworth.diplomacy.enchanting;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyRecipes;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class Tools {
    private static Tools instance = null;
    private final NamespacedKey purityKey = new NamespacedKey(Diplomacy.getInstance(), "purity");
    private final String purityLore = ChatColor.GRAY + "Purity:";
    private final String refinedIronLore = ChatColor.BLUE + "Refined Iron Ingot";
    private final String refinedNetheriteLore = ChatColor.BLUE + "Refined Netherite Ingot";
    private final String refinedGoldLore = ChatColor.BLUE + "Refined Gold Ingot";

    public final Set<Material> wooden = new HashSet<>();
    public final Set<Material> stone = new HashSet<>();
    public final Set<Material> iron = new HashSet<>();
    public final Set<Material> gold = new HashSet<>();
    public final Set<Material> diamond = new HashSet<>();
    public final List<Material> planks = new ArrayList<>();
    public final String p0 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "||||||||||";
    public final String p1 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "|||||||||" + ChatColor.DARK_RED + ChatColor.BOLD + "|";
    public final String p2 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "||||||||" + ChatColor.RED + ChatColor.BOLD + "||";
    public final String p3 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "|||||||" + ChatColor.GOLD + ChatColor.BOLD + "|||";
    public final String p4 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "||||||" + ChatColor.GOLD + ChatColor.BOLD + "||||";
    public final String p5 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "|||||" + ChatColor.YELLOW + ChatColor.BOLD + "|||||";
    public final String p6 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "||||" + ChatColor.YELLOW + ChatColor.BOLD + "||||||";
    public final String p7 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "|||" + ChatColor.GREEN + ChatColor.BOLD + "|||||||";
    public final String p8 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "||" + ChatColor.GREEN + ChatColor.BOLD + "||||||||";
    public final String p9 = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "|" + ChatColor.AQUA + ChatColor.BOLD + "|||||||||";
    public final String p10 = "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "||||||||||";
    final String HAMMER_LORE = ChatColor.BLUE + "Hammer";

    public static Tools getInstance() {
        if (instance == null) {
            instance = new Tools();
        }
        return instance;
    }

    public ItemStack generatePurity(ItemStack item, double max) {
        var nItem = new ItemStack(item);
        nItem.setAmount(1);
        var impure = Math.random() * max;
        var meta = nItem.getItemMeta();
        meta.getPersistentDataContainer().set(purityKey, PersistentDataType.DOUBLE, impure);
        var lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        lore.add(purityLore);
        if (impure < 0.00000043) lore.add(p10);
        else if (impure < 0.00000187) lore.add(p9);
        else if (impure < 0.00000811) lore.add(p8);
        else if (impure < 0.00003511) lore.add(p7);
        else if (impure < 0.00015199) lore.add(p6);
        else if (impure < 0.00065793) lore.add(p5);
        else if (impure < 0.00284804) lore.add(p4);
        else if (impure < 0.01232847) lore.add(p3);
        else if (impure < 0.05336699) lore.add(p2);
        else if (impure < 0.23101297) lore.add(p1);
        else lore.add(p0);

        meta.setLore(lore);
        nItem.setItemMeta(meta);
        return nItem;
    }

    public Tools() {
        wooden.add(Material.WOODEN_SWORD);
        wooden.add(Material.WOODEN_AXE);
        wooden.add(Material.WOODEN_HOE);
        wooden.add(Material.WOODEN_PICKAXE);
        wooden.add(Material.WOODEN_SHOVEL);

        stone.add(Material.STONE_SWORD);
        stone.add(Material.STONE_AXE);
        stone.add(Material.STONE_HOE);
        stone.add(Material.STONE_PICKAXE);
        stone.add(Material.STONE_SHOVEL);

        iron.add(Material.IRON_SWORD);
        iron.add(Material.IRON_AXE);
        iron.add(Material.IRON_HOE);
        iron.add(Material.IRON_PICKAXE);
        iron.add(Material.IRON_SHOVEL);
        iron.add(Material.IRON_HELMET);
        iron.add(Material.IRON_CHESTPLATE);
        iron.add(Material.IRON_LEGGINGS);
        iron.add(Material.IRON_BOOTS);

        diamond.add(Material.DIAMOND_SWORD);
        diamond.add(Material.DIAMOND_AXE);
        diamond.add(Material.DIAMOND_HOE);
        diamond.add(Material.DIAMOND_PICKAXE);
        diamond.add(Material.DIAMOND_SHOVEL);
        diamond.add(Material.DIAMOND_HELMET);
        diamond.add(Material.DIAMOND_CHESTPLATE);
        diamond.add(Material.DIAMOND_LEGGINGS);
        diamond.add(Material.DIAMOND_BOOTS);

        gold.add(Material.GOLDEN_SWORD);
        gold.add(Material.GOLDEN_AXE);
        gold.add(Material.GOLDEN_HOE);
        gold.add(Material.GOLDEN_PICKAXE);
        gold.add(Material.GOLDEN_SHOVEL);
        gold.add(Material.GOLDEN_HELMET);
        gold.add(Material.GOLDEN_CHESTPLATE);
        gold.add(Material.GOLDEN_LEGGINGS);
        gold.add(Material.GOLDEN_BOOTS);

        planks.add(Material.WARPED_PLANKS);
        planks.add(Material.SPRUCE_PLANKS);
        planks.add(Material.OAK_PLANKS);
        planks.add(Material.JUNGLE_PLANKS);
        planks.add(Material.DARK_OAK_PLANKS);
        planks.add(Material.CRIMSON_PLANKS);
        planks.add(Material.BIRCH_PLANKS);
        planks.add(Material.ACACIA_PLANKS);
    }



    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Tools.EventListener(), Diplomacy.getInstance());
    }

    private boolean isNew(ItemStack item) {
        var meta = item.getItemMeta();
        return (!meta.hasLore() || meta.getLore().get(0).equals(DiplomacyRecipes.NEW_LORE)) && !meta.hasEnchants() &&
                ((Damageable) meta).getDamage() == 0;
    }

    private boolean isRefined(ItemStack item) {
        var meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null) {
            var lore = meta.getLore().get(0);
            return lore.equals(refinedNetheriteLore) || lore.equals(refinedGoldLore) || lore.equals(refinedIronLore);
        }
        return false;
    }

    private boolean hasPurity(ItemStack item) {
        if (isRefined(item)) return true;
        var meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getLore() != null && meta.getLore().get(0).equals(purityLore);
    }

    private double getPurity(ItemStack item) {
        if (!hasPurity(item)) {
            throw new NullPointerException("Item has no purity.");
        }

        var container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
        return Objects.requireNonNull(container.get(purityKey, PersistentDataType.DOUBLE));
    }

    private boolean isHammer(ItemStack item) {
        if (item == null) return false;

        var itemMeta = item.getItemMeta();
        if (itemMeta == null) return false;

        var lore = itemMeta.getLore();
        if (lore == null) return false;

        return lore.get(0).equals(HAMMER_LORE);
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onPrepareItemCraft(PrepareItemCraftEvent event) {
            var inventory = event.getInventory();

            var result = inventory.getResult();
            if (result == null) return;

            // Unbreaking
            if (wooden.contains(result.getType()) && isNew(result)) {
                for (var item : inventory.getMatrix()) {
                    if (item != null &&
                            !item.getType().equals(Material.AIR) &&
                            !item.getType().equals(Material.ACACIA_PLANKS) &&
                            !item.getType().equals(Material.STICK)) {
                        return;
                    }
                }
                result.addEnchantment(Enchantment.DURABILITY, 1);
            } else if (stone.contains(result.getType()) && isNew(result)) {
                for (var item : inventory.getMatrix()) {
                    if (item != null &&
                            !item.getType().equals(Material.AIR) &&
                            !item.getType().equals(Material.BLACKSTONE) &&
                            !item.getType().equals(Material.STICK)) {
                        return;
                    }
                }
                result.addEnchantment(Enchantment.DURABILITY, 2);
            } else if (diamond.contains(result.getType()) && isNew(result)) {
                result.addEnchantment(Enchantment.DURABILITY, 2);
            } else if ((iron.contains(result.getType()) || gold.contains(result.getType())) && isNew(result)) {
                var unbreaking = 10;
                for (var item : inventory.getMatrix()) {
                    if (item != null && (item.getType().equals(Material.IRON_INGOT) || item.getType().equals(Material.GOLD_INGOT))) {
                        if (item.getItemMeta() != null && item.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
                            unbreaking = Math.min(unbreaking, item.getEnchantmentLevel(Enchantment.DURABILITY));
                        } else {
                            unbreaking = 0;
                            break;
                        }
                    }
                }
                if (unbreaking > 0) result.addUnsafeEnchantment(Enchantment.DURABILITY, unbreaking);
            }

            if (isNew(result)) {
                var meta = result.getItemMeta();
                if (meta != null && meta.getLore() != null) {
                    var lore = meta.getLore();
                    lore.remove(0);
                    meta.setLore(lore);
                    result.setItemMeta(meta);
                }
                return;
            }
        }

        @EventHandler
        private void onItemUse(PlayerInteractEvent event) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                var item = event.getItem();
                if (item == null) return;
                if (item.getType() == Material.IRON_INGOT || item.getType() == Material.GOLD_INGOT || item.getType() == Material.NETHERITE_INGOT
                || item.getType() == Material.IRON_NUGGET || item.getType() == Material.GOLD_NUGGET || item.getType() == Material.NETHERITE_SCRAP) {
                    if (!hasPurity(item)) {
                        var nItem = generatePurity(item, 1.0);

                        var player = event.getPlayer();
                        var inventory = player.getInventory();

                        if (item.getAmount() == 1) inventory.setItemInMainHand(nItem);
                        else {
                            item.setAmount(item.getAmount() - 1);

                            var full = inventory.firstEmpty() == -1;

                            if (full) player.getWorld().dropItem(player.getLocation(), nItem);
                            else inventory.addItem(nItem);
                        }
                    }
                }
            }
        }

        @EventHandler
        private void onPrepareSmithing(PrepareSmithingEvent event) {
            var inventory = event.getInventory().getContents();
            var result = event.getResult();
            if (result != null) {
                result.removeEnchantment(Enchantment.DURABILITY);
                var ingredient = inventory[1];
                if (ingredient.getItemMeta() != null && ingredient.getItemMeta().hasEnchants()) {
                    result.addEnchantment(Enchantment.DURABILITY, ingredient.getEnchantmentLevel(Enchantment.DURABILITY));
                }
             event.setResult(result);
            }
        }

        @EventHandler
        private void onPlayerInteract(PlayerInteractEvent event) {
            var player = event.getPlayer();
            var item = event.getPlayer().getInventory().getItemInMainHand();
            if (isHammer(item)) {
                event.getPlayer().sendMessage(ChatColor.RED + "Hammers are a removed feature and can no longer be used. " +
                        "Your hammer has been reverted into the ingredients used to craft it.");
                player.playEffect(EntityEffect.BREAK_EQUIPMENT_MAIN_HAND);
                switch (item.getType()) {
                    case WOODEN_HOE -> player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.OAK_PLANKS, 5));
                    case STONE_HOE -> player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.COBBLESTONE, 5));
                    case IRON_HOE -> player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.IRON_INGOT, 5));
                    case GOLDEN_HOE -> player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.GOLD_INGOT, 5));
                    case DIAMOND_HOE -> player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.DIAMOND, 5));
                    case NETHERITE_HOE -> {
                        player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.DIAMOND, 5));
                        player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.NETHERITE_INGOT));
                    }
                }
                player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.STICK, 2));
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                event.setCancelled(true);
                return;
            }
        }
    }
}
