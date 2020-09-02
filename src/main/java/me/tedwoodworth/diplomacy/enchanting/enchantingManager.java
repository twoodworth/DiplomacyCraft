package me.tedwoodworth.diplomacy.enchanting;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class enchantingManager {
    private static enchantingManager instance = null;
    final String HAMMER_LORE = ChatColor.BLUE + "Hammer";

    private List<Material> netheriteTools;
    private List<Material> diamondTools;
    private List<Material> ironTools;
    private List<Material> goldenTools;
    private List<Material> stoneTools;
    private List<Material> woodenTools;
    private ItemStack knowledgeBook;

    public String getHammerLore() {
        return HAMMER_LORE;
    }

    public ItemStack getKnowledgeBook() {
        if (Objects.equals(this.knowledgeBook, null)) {
            var item = new ItemStack(Material.KNOWLEDGE_BOOK);
            var itemMeta = item.getItemMeta();
            var lore = new ArrayList<String>();
            lore.add("Used for crafting enchanting tables.");
            if (itemMeta != null) {
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);
            }
            this.knowledgeBook = item;
        }
        return this.knowledgeBook;
    }

    private List<Material> getNetheriteTools() {
        if (Objects.equals(this.netheriteTools, null)) {
            var netheriteTools = new ArrayList<Material>();
            netheriteTools.add(Material.NETHERITE_HELMET);
            netheriteTools.add(Material.NETHERITE_CHESTPLATE);
            netheriteTools.add(Material.NETHERITE_LEGGINGS);
            netheriteTools.add(Material.NETHERITE_BOOTS);
            netheriteTools.add(Material.NETHERITE_SWORD);
            netheriteTools.add(Material.NETHERITE_PICKAXE);
            netheriteTools.add(Material.NETHERITE_AXE);
            netheriteTools.add(Material.NETHERITE_SHOVEL);
            netheriteTools.add(Material.NETHERITE_HOE);
            this.netheriteTools = netheriteTools;
        }
        return netheriteTools;
    }

    private List<Material> getDiamondTools() {
        if (Objects.equals(this.diamondTools, null)) {
            var diamondTools = new ArrayList<Material>();
            diamondTools.add(Material.DIAMOND_HELMET);
            diamondTools.add(Material.DIAMOND_CHESTPLATE);
            diamondTools.add(Material.DIAMOND_LEGGINGS);
            diamondTools.add(Material.DIAMOND_BOOTS);
            diamondTools.add(Material.DIAMOND_SWORD);
            diamondTools.add(Material.DIAMOND_PICKAXE);
            diamondTools.add(Material.DIAMOND_AXE);
            diamondTools.add(Material.DIAMOND_SHOVEL);
            diamondTools.add(Material.DIAMOND_HOE);
            this.diamondTools = diamondTools;
        }
        return diamondTools;
    }

    private List<Material> getGoldenTools() {
        if (Objects.equals(this.goldenTools, null)) {
            var goldenTools = new ArrayList<Material>();
            goldenTools.add(Material.GOLDEN_HELMET);
            goldenTools.add(Material.GOLDEN_CHESTPLATE);
            goldenTools.add(Material.GOLDEN_LEGGINGS);
            goldenTools.add(Material.GOLDEN_BOOTS);
            goldenTools.add(Material.GOLDEN_SWORD);
            goldenTools.add(Material.GOLDEN_PICKAXE);
            goldenTools.add(Material.GOLDEN_AXE);
            goldenTools.add(Material.GOLDEN_SHOVEL);
            goldenTools.add(Material.GOLDEN_HOE);
            this.goldenTools = goldenTools;
        }
        return goldenTools;
    }

    private List<Material> getIronTools() {
        if (Objects.equals(this.ironTools, null)) {
            var ironTools = new ArrayList<Material>();
            ironTools.add(Material.IRON_HELMET);
            ironTools.add(Material.IRON_CHESTPLATE);
            ironTools.add(Material.IRON_LEGGINGS);
            ironTools.add(Material.IRON_BOOTS);
            ironTools.add(Material.IRON_SWORD);
            ironTools.add(Material.IRON_PICKAXE);
            ironTools.add(Material.IRON_AXE);
            ironTools.add(Material.IRON_SHOVEL);
            ironTools.add(Material.IRON_HOE);
            this.ironTools = ironTools;
        }
        return ironTools;
    }

    private List<Material> getStoneTools() {
        if (Objects.equals(this.stoneTools, null)) {
            var stoneTools = new ArrayList<Material>();
            stoneTools.add(Material.CHAINMAIL_HELMET);
            stoneTools.add(Material.CHAINMAIL_CHESTPLATE);
            stoneTools.add(Material.CHAINMAIL_LEGGINGS);
            stoneTools.add(Material.CHAINMAIL_BOOTS);
            stoneTools.add(Material.STONE_SWORD);
            stoneTools.add(Material.STONE_PICKAXE);
            stoneTools.add(Material.STONE_AXE);
            stoneTools.add(Material.STONE_SHOVEL);
            stoneTools.add(Material.STONE_HOE);
            this.stoneTools = stoneTools;
        }
        return stoneTools;
    }

    private List<Material> getWoodenTools() {
        if (Objects.equals(this.woodenTools, null)) {
            var woodenTools = new ArrayList<Material>();
            woodenTools.add(Material.LEATHER_HELMET);
            woodenTools.add(Material.LEATHER_CHESTPLATE);
            woodenTools.add(Material.LEATHER_LEGGINGS);
            woodenTools.add(Material.LEATHER_BOOTS);
            woodenTools.add(Material.WOODEN_SWORD);
            woodenTools.add(Material.WOODEN_PICKAXE);
            woodenTools.add(Material.WOODEN_AXE);
            woodenTools.add(Material.WOODEN_SHOVEL);
            woodenTools.add(Material.TURTLE_HELMET);
            woodenTools.add(Material.ELYTRA);
            this.woodenTools = woodenTools;
        }
        return woodenTools;
    }

    public static enchantingManager getInstance() {
        if (instance == null) {
            instance = new enchantingManager();
        }
        return instance;
    }



    private @Nullable ItemStack getResult(ItemStack hammer1, ItemStack hammer2, ItemStack result) {
        var hammer1Type = hammer1.getType();
        var hammer2Type = hammer2.getType();
        var resultType = result.getType();
        var ignoreHammer1 = false;
        var ignoreHammer2 = false;
        int hammer1level;
        int hammer2level;
        int resultLevel;

        switch (hammer1Type) {
            case WOODEN_HOE -> hammer1level = 1;
            case STONE_HOE, GOLDEN_HOE -> hammer1level = 2;
            case IRON_HOE -> hammer1level = 3;
            case DIAMOND_HOE -> hammer1level = 4;
            case NETHERITE_HOE -> hammer1level = 5;
            default -> hammer1level = 0;
        }

        switch (hammer2Type) {
            case WOODEN_HOE -> hammer2level = 1;
            case STONE_HOE, GOLDEN_HOE -> hammer2level = 2;
            case IRON_HOE -> hammer2level = 3;
            case DIAMOND_HOE -> hammer2level = 4;
            case NETHERITE_HOE -> hammer2level = 5;
            default -> hammer2level = 0;
        }

        if (getStoneTools().contains(resultType)) {
            resultLevel = 1;
        } else if (getGoldenTools().contains(resultType) || getIronTools().contains(resultType)) {
            resultLevel = 2;
        } else if (getDiamondTools().contains(resultType)) {
            resultLevel = 3;
        } else if (getNetheriteTools().contains(resultType)) {
            resultLevel = 4;
        } else {
            resultLevel = 0;
        }

        if (resultLevel >= hammer1level) {
            ignoreHammer1 = true;
        }
        if (resultLevel >= hammer2level) {
            ignoreHammer2 = true;
        }
        if (ignoreHammer1 && ignoreHammer2) {
            return null;
        }

        var enchantments = result.getEnchantments().keySet();

        for (var enchantment : new HashSet<>(enchantments)) {
            int hammer1enchantment = 0;
            if (!ignoreHammer1 && hammer1.getEnchantments().containsKey(enchantment)) {
                hammer1enchantment = hammer1.getEnchantmentLevel(enchantment);
            }

            int hammer2enchantment = 0;
            if (!ignoreHammer2 && hammer2.getEnchantments().containsKey(enchantment)) {
                hammer2enchantment = hammer2.getEnchantmentLevel(enchantment);
            }

            var maxLevel = Math.max(hammer1enchantment, hammer2enchantment);
            if (hammer1enchantment == hammer2enchantment && hammer1enchantment > 0) {
                maxLevel++;
            }
            var level = Math.min(result.getEnchantmentLevel(enchantment), maxLevel);
            result.removeEnchantment(enchantment);
            result.addUnsafeEnchantment(enchantment, level);
        }
        return result;
    }

    private boolean isHammer(ItemStack item) {

        if (item == null) {
            return false;
        }

        var itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            return false;
        }

        var lore = itemMeta.getLore();
        if (lore == null) {
            return false;
        }

        return lore.get(0).equals(HAMMER_LORE);
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new enchantingManager.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler(ignoreCancelled = true)
        public void onPrepareSmithing(PrepareSmithingEvent event) {
            var result = event.getResult();
            if (result == null) {
                return;
            }
            var input = event.getInventory().getItem(0);
            if (isHammer(input) && input.getType().equals(Material.DIAMOND_HOE)) {
                var itemMeta = result.getItemMeta();
                if (itemMeta == null) {
                    return;
                }
                var lore = new ArrayList<String>();
                lore.add(enchantingManager.getInstance().HAMMER_LORE);
                itemMeta.setLore(lore);
                itemMeta.setLocalizedName(ChatColor.WHITE + "Netherite Hammer");
                itemMeta.setDisplayName(ChatColor.WHITE + "Netherite Hammer");
                result.setItemMeta(itemMeta);
                event.setResult(result);
                event.getInventory().setItem(2, result);
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlayerInteract(PlayerInteractEvent event) {
            var block = event.getClickedBlock();
            if (block == null) {
                return;
            }
            var type = block.getType();
            if (type.equals(Material.ANVIL) || type.equals(Material.CHIPPED_ANVIL) || type.equals(Material.DAMAGED_ANVIL)) {
                var mainHand = event.getPlayer().getInventory().getItemInMainHand();
                var offHand = event.getPlayer().getInventory().getItemInOffHand();
                var mainHasHammer = isHammer(mainHand);
                var offHasHammer = isHammer(offHand);
                if (!(mainHasHammer || offHasHammer)) {
                    event.getPlayer().sendMessage(ChatColor.RED + "You must be holding a hammer.");
                    event.setCancelled(true);
                }
            } else if (isHammer(event.getItem()) && (type.equals(Material.DIRT)
                    || type.equals(Material.GRASS_BLOCK)
                    || type.equals(Material.GRASS_PATH)
                    || type.equals(Material.COARSE_DIRT))) {
                event.setCancelled(true);
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onInventoryClick(InventoryClickEvent event) {
            // Rest of my code which isn't being reached
            if (event.getView().getTopInventory() instanceof AnvilInventory) {
                if (event.getWhoClicked().getInventory().getHeldItemSlot() == event.getSlot()) {
                        event.getWhoClicked().sendMessage(ChatColor.RED + "You cannot swap out the item you're holding right now.");
                        event.setCancelled(true);
                }
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
            if (event.getPlayer().getOpenInventory().getTopInventory() instanceof AnvilInventory) {
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot swap out the item you're holding right now.");
                event.setCancelled(true);
            }
        }

        @EventHandler
        public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
            if (event.getRecipe().getResult().equals(new ItemStack(Material.NAME_TAG))) {
                var recipes = new ArrayList<>(event.getEntity().getRecipes());
                var knowledgeBookRecipe = new MerchantRecipe(getKnowledgeBook(), 1);
                knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int)(Math.random() * 17 + 48)));
                recipes.add(knowledgeBookRecipe);
                event.getEntity().setRecipes(recipes);
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onPrepareAnvil(PrepareAnvilEvent event) {
            if (event.getResult() == null) {
                return;
            }
            var anvilInv = event.getInventory();
            var item1 = anvilInv.getItem(0);
            var item2 = anvilInv.getItem(1);

            var item1IsHammer = isHammer(item1);
            var item2IsHammer = isHammer(item2);

            Material item1type = null;
            Material item2type = null;
            if (item1 != null) {
                item1type = item1.getType();
            }
            if (item2 != null) {
                item2type = item2.getType();
            }

            if ((Objects.equals(item1type, item2type) && !(item1IsHammer == item2IsHammer))) {
                event.getInventory().setItem(2, null);
                event.setResult(null);
                return;
            }

            var holder = event.getView().getBottomInventory().getHolder();
            if (holder == null) {
                return;
            }
            if (!(holder instanceof Player)) {
                return;
            }

            var player = (Player) holder;
            var result = event.getResult();
            var hammer1 = player.getInventory().getItemInMainHand();
            var hammer2 = player.getInventory().getItemInOffHand();

            if (!isHammer(hammer1)) {
                hammer1 = new ItemStack(Material.AIR);
            }
            if (!isHammer(hammer2)) {
                hammer2 = new ItemStack(Material.AIR);
            }
            result = getResult(hammer1, hammer2, result);
            event.getInventory().setItem(2, result);
            event.setResult(result);
        }
    }
}
