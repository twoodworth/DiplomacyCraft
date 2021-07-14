package me.tedwoodworth.diplomacy.Items;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.geology.WorldManager;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.bukkit.Material.*;

public class Items {
    private static Items instance = null;
    private final ItemStack air = new ItemStack(Material.AIR);

    public final List<Material> helmets = new ArrayList<>();
    public final List<Material> chestplates = new ArrayList<>();
    public final List<Material> leggings = new ArrayList<>();
    public final List<Material> boots = new ArrayList<>();
    public final Set<Material> wooden = new HashSet<>();
    public final Set<Material> stone = new HashSet<>();
    public final Set<Material> iron = new HashSet<>();
    public final Set<Material> gold = new HashSet<>();
    public final Set<Material> diamond = new HashSet<>();
    public final List<Material> planks = new ArrayList<>();
    public final List<Material> wool = new ArrayList<>();
    public final List<Material> tools = new ArrayList<>();

    public static Items getInstance() {
        if (instance == null) {
            instance = new Items();
        }
        return instance;
    }

    public boolean isClassItem(ItemStack itemStack) {
        if (itemStack == null) return false;

        var meta = itemStack.getItemMeta();
        return meta != null
                && meta.getLore() != null
                && meta.getLore().get(0).contains("Prefix:");
    }

    public Items() {
        wooden.add(Material.WOODEN_SWORD);
        wooden.add(Material.WOODEN_AXE);
        wooden.add(Material.WOODEN_HOE);
        wooden.add(Material.WOODEN_PICKAXE);
        wooden.add(Material.WOODEN_SHOVEL);

        stone.add(Material.STONE_SWORD);
        stone.add(STONE_AXE);
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
        diamond.add(DIAMOND_AXE);
        diamond.add(Material.DIAMOND_HOE);
        diamond.add(Material.DIAMOND_PICKAXE);
        diamond.add(Material.DIAMOND_SHOVEL);
        diamond.add(Material.DIAMOND_HELMET);
        diamond.add(Material.DIAMOND_CHESTPLATE);
        diamond.add(Material.DIAMOND_LEGGINGS);
        diamond.add(Material.DIAMOND_BOOTS);

        tools.add(Material.WOODEN_AXE);
        tools.add(STONE_AXE);
        tools.add(Material.IRON_AXE);
        tools.add(GOLDEN_AXE);
        tools.add(DIAMOND_AXE);
        tools.add(Material.NETHERITE_AXE);
        tools.add(Material.WOODEN_SWORD);
        tools.add(Material.STONE_SWORD);
        tools.add(Material.IRON_SWORD);
        tools.add(Material.GOLDEN_SWORD);
        tools.add(Material.DIAMOND_SWORD);
        tools.add(Material.NETHERITE_SWORD);
        tools.add(Material.WOODEN_PICKAXE);
        tools.add(Material.STONE_PICKAXE);
        tools.add(Material.IRON_PICKAXE);
        tools.add(Material.GOLDEN_PICKAXE);
        tools.add(Material.DIAMOND_PICKAXE);
        tools.add(Material.NETHERITE_PICKAXE);
        tools.add(Material.WOODEN_SHOVEL);
        tools.add(Material.STONE_SHOVEL);
        tools.add(Material.IRON_SHOVEL);
        tools.add(Material.GOLDEN_SHOVEL);
        tools.add(Material.DIAMOND_SHOVEL);
        tools.add(Material.NETHERITE_SHOVEL);
        tools.add(Material.TRIDENT);
        tools.add(Material.WOODEN_HOE);
        tools.add(Material.STONE_HOE);
        tools.add(Material.IRON_HOE);
        tools.add(Material.GOLDEN_HOE);
        tools.add(Material.DIAMOND_HOE);
        tools.add(Material.NETHERITE_HOE);

        gold.add(Material.GOLDEN_SWORD);
        gold.add(GOLDEN_AXE);
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

        wool.add(Material.WHITE_WOOL);
        wool.add(Material.BLACK_WOOL);
        wool.add(Material.BLUE_WOOL);
        wool.add(Material.BROWN_WOOL);
        wool.add(Material.CYAN_WOOL);
        wool.add(Material.GRAY_WOOL);
        wool.add(Material.GREEN_WOOL);
        wool.add(Material.LIGHT_BLUE_WOOL);
        wool.add(Material.LIGHT_GRAY_WOOL);
        wool.add(Material.LIME_WOOL);
        wool.add(Material.MAGENTA_WOOL);
        wool.add(Material.ORANGE_WOOL);
        wool.add(Material.PINK_WOOL);
        wool.add(Material.PURPLE_WOOL);
        wool.add(Material.RED_WOOL);
        wool.add(Material.YELLOW_WOOL);

        helmets.add(Material.LEATHER_HELMET);
        helmets.add(Material.CHAINMAIL_HELMET);
        helmets.add(Material.IRON_HELMET);
        helmets.add(Material.GOLDEN_HELMET);
        helmets.add(Material.DIAMOND_HELMET);
        helmets.add(Material.NETHERITE_HELMET);
        helmets.add(Material.TURTLE_HELMET);

        chestplates.add(Material.LEATHER_CHESTPLATE);
        chestplates.add(Material.CHAINMAIL_CHESTPLATE);
        chestplates.add(Material.IRON_CHESTPLATE);
        chestplates.add(Material.GOLDEN_CHESTPLATE);
        chestplates.add(Material.DIAMOND_CHESTPLATE);
        chestplates.add(Material.NETHERITE_CHESTPLATE);

        leggings.add(Material.LEATHER_LEGGINGS);
        leggings.add(Material.CHAINMAIL_LEGGINGS);
        leggings.add(Material.IRON_LEGGINGS);
        leggings.add(Material.GOLDEN_LEGGINGS);
        leggings.add(Material.DIAMOND_LEGGINGS);
        leggings.add(Material.NETHERITE_LEGGINGS);

        boots.add(Material.LEATHER_BOOTS);
        boots.add(Material.CHAINMAIL_BOOTS);
        boots.add(Material.IRON_BOOTS);
        boots.add(Material.GOLDEN_BOOTS);
        boots.add(Material.DIAMOND_BOOTS);
        boots.add(Material.NETHERITE_BOOTS);
    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Items.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onPrepareAnvil(PrepareAnvilEvent event) {
            var inventory = event.getInventory();
            inventory.setMaximumRepairCost(9999);
        }

        @EventHandler
        private void onPrepareItemCraft(PrepareItemCraftEvent event) {
            var inventory = event.getInventory();

            var result = inventory.getResult();
            if (result == null) return;

            // suspicious stew
            if (result.getType() == Material.SUSPICIOUS_STEW) {
                inventory.setResult(air);
            }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        private void onEntityDamage(EntityDamageEvent event) {
            var cause = event.getCause();
            var entity = event.getEntity();
            var damage = event.getDamage();
            var reduce = 0.0;
            Entity damager;
            if (event instanceof EntityDamageByEntityEvent)
                damager = ((EntityDamageByEntityEvent) event).getDamager();
            else damager = null;
            if (damager instanceof Player) {
                var player = ((Player) damager);
                var type = player.getEquipment().getItemInMainHand().getType();
                if (type == Material.WOODEN_AXE || type == Material.STONE_AXE || type == Material.IRON_AXE || type == Material.DIAMOND_AXE || type == Material.NETHERITE_AXE) {
                    for (var item : player.getInventory().getContents()) {
                        if (item == null) continue;
                        if (item.getType() == Material.SHIELD) continue;
                        player.setCooldown(item.getType(), 17);
                    }
                }
            }
        }

        @EventHandler
        private void onItemUse(PlayerInteractEvent event) {
            var item = event.getItem();
            if (item == null) return;
            var action = event.getAction();

            var player = event.getPlayer();
            // Grenade
            if (player.getWorld().equals(WorldManager.getInstance().getSpawn()) && !item.getType().isEdible()) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot use that here.");
                return;
            }

        }

        @EventHandler
        private void onBrew(BrewEvent event) {
            var ingredient = event.getContents().getIngredient();
            var contents = event.getContents().getContents();
            for (var content : contents) {
                // fire potion
                if (content.getItemMeta() instanceof PotionMeta) {
                    var potionType = ((PotionMeta) content.getItemMeta()).getBasePotionData().getType();
                    if (potionType == PotionType.FIRE_RESISTANCE) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        @EventHandler
        private void onInventoryPickupItem(InventoryPickupItemEvent event) {
            var item = event.getItem();
            if (item.getItemStack().getType() == BLAZE_POWDER && GuardManager.getInstance().isGuardProjectile(item)) {
                event.setCancelled(true);
                return;
            }
        }

        @EventHandler
        private void onItemMerge(ItemMergeEvent event) {
            var item = event.getEntity();
            if (item.getItemStack().getType() == BLAZE_POWDER && GuardManager.getInstance().isGuardProjectile(item)) {
                event.setCancelled(true);
                return;
            }
        }

        @EventHandler
        private void onEntityPickupItem(EntityPickupItemEvent event) {
            var drop = event.getItem();
            var item = drop.getItemStack();
            if (item == null) return;
            if (item.getType() == BLAZE_POWDER && GuardManager.getInstance().isGuardProjectile(drop)) {
                event.setCancelled(true);
                return;
            }
            var entity = event.getEntity();
            if (entity instanceof Player) {

            } else {
                entity.setRemoveWhenFarAway(false);
            }
        }

        @EventHandler
        private void onConsume(PlayerItemConsumeEvent event) {
            var item = event.getItem();
            var player = event.getPlayer();
            if (item.getType() == GOLDEN_APPLE && CustomItemGenerator.getInstance().isCustomItem(item)) {
                var amount = item.getAmount();
                if (amount == 1) {
                    player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
                } else {
                    item.setAmount(amount - 1);
                    player.getEquipment().setItemInMainHand(item);
                }
                event.setCancelled(true);
                var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                var lives = dp.getLives() + 1;
                dp.setLives(lives);
                player.sendMessage(ChatColor.GREEN + "You now have " + lives + " lives");
            }
        }
    }
}
