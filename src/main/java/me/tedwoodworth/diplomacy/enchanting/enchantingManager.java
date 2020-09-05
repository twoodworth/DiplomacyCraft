package me.tedwoodworth.diplomacy.enchanting;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class enchantingManager {
    private static enchantingManager instance = null;
    final String HAMMER_LORE = ChatColor.BLUE + "Hammer";

    private List<Material> netheriteTools;
    private List<Material> diamondTools;
    private List<Material> ironTools;
    private List<Material> goldenTools;
    private List<Material> chainArmor;
    private List<Material> stoneTools;
    private List<Material> woodenTools;
    private List<Material> planks;
    private ItemStack knowledgeBook;
    private final String WOODEN = "Wooden";
    private final String STONE = "Stone";
    private final String CHAIN = "Chain";
    private final String GOLDEN = "Golden";
    private final String IRON = "Iron";
    private final String DIAMOND = "Diamond";
    private final String NETHERITE = "Netherite";
    private final String ENCHANTED_BOOK = "Enchanted_Book";
    private final String TURTLE = "Turtle";
    private final String ELYTRA = "Elytra";
    private final String OTHER = "Other";

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

    public List<Material> getPlanks() {
        if (Objects.equals(this.planks, null)) {
            var planks = new ArrayList<Material>();
            planks.add(Material.ACACIA_PLANKS);
            planks.add(Material.BIRCH_PLANKS);
            planks.add(Material.CRIMSON_PLANKS);
            planks.add(Material.DARK_OAK_PLANKS);
            planks.add(Material.JUNGLE_PLANKS);
            planks.add(Material.OAK_PLANKS);
            planks.add(Material.SPRUCE_PLANKS);
            planks.add(Material.WARPED_PLANKS);
            this.planks = planks;
        }
        return planks;
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

    private List<Material> getChainArmor() {
        if (Objects.equals(this.chainArmor, null)) {
            var chainArmor = new ArrayList<Material>();
            chainArmor.add(Material.CHAINMAIL_HELMET);
            chainArmor.add(Material.CHAINMAIL_CHESTPLATE);
            chainArmor.add(Material.CHAINMAIL_LEGGINGS);
            chainArmor.add(Material.CHAINMAIL_BOOTS);
            this.chainArmor = chainArmor;
        }
        return chainArmor;
    }

    private List<Material> getStoneTools() {
        if (Objects.equals(this.chainArmor, null)) {
            var stoneTools = new ArrayList<Material>();
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

    public String getToolMaterial(Material item) {
        if (getWoodenTools().contains(item)) {
            return WOODEN;
        } else if (getStoneTools().contains(item)) {
            return STONE;
        } else if (getChainArmor().contains(item)) {
            return CHAIN;
        } else if (getGoldenTools().contains(item)) {
            return GOLDEN;
        } else if (getIronTools().contains(item)) {
            return IRON;
        } else if (getDiamondTools().contains(item)) {
            return DIAMOND;
        } else if (getNetheriteTools().contains(item)) {
            return NETHERITE;
        } else if (item.equals(Material.ENCHANTED_BOOK)) {
            return ENCHANTED_BOOK;
        } else if (item.equals(Material.TURTLE_HELMET)) {
            return TURTLE;
        } else if (item.equals(Material.ELYTRA)) {
            return ELYTRA;
        } else {
            return OTHER;
        }
    }


    private @Nullable ItemStack getResult(ItemStack item1, ItemStack item2, ItemStack hammer, @Nullable String resultName) {
        // Get item1 Meta
        var item1ItemMeta = item1.getItemMeta();
        if (item1ItemMeta == null) {
            return null;
        }

        // Create resultItem
        var resultItem = new ItemStack(item1);
        var resultItemMeta = resultItem.getItemMeta();
        if (resultItemMeta == null) {
            return null;
        }

        // Update result name
        resultItemMeta.setDisplayName(resultName);
        resultItem.setItemMeta(resultItemMeta);

        // Calculations finished if there is no item2.
        if (item2 == null || item2.getItemMeta() == null) {
            return resultItem;
        }

        // Check if the combinations of item1 and item2 are valid.
        switch (getToolMaterial(item1.getType())) {
            case WOODEN -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK) || this.getPlanks().contains(item2.getType()) || Objects.equals(item1.getType(), item2.getType()))) {
                    return null;
                }
            }
            case STONE -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK) || item2.getType().equals(Material.COBBLESTONE) || item2.getType().equals(Material.BLACKSTONE) || Objects.equals(item1.getType(), item2.getType()))) {
                    return null;
                }
            }
            case CHAIN -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK) || Objects.equals(item1.getType(), item2.getType()))) {
                    return null;
                }
            }
            case IRON -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK) || item2.getType().equals(Material.IRON_INGOT) || Objects.equals(item1.getType(), item2.getType()))) {
                    return null;
                }
            }
            case GOLDEN -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK) || item2.getType().equals(Material.GOLD_INGOT) || Objects.equals(item1.getType(), item2.getType()))) {
                    return null;
                }
            }
            case DIAMOND -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK) || item2.getType().equals(Material.DIAMOND) || Objects.equals(item1.getType(), item2.getType()))) {
                    return null;
                }
            }
            case NETHERITE -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK) || item2.getType().equals(Material.NETHERITE_INGOT))) {
                    return null;
                }
            }
            case TURTLE -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK) || item2.getType().equals(Material.SCUTE) || Objects.equals(item1.getType(), item2.getType()))) {
                    return null;
                }
            }
            case ELYTRA -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK) || item2.getType().equals(Material.PHANTOM_MEMBRANE) || Objects.equals(item1.getType(), item2.getType()))) {
                    return null;
                }
            }
            case OTHER -> {
                if (!(item2.getType().equals(Material.ENCHANTED_BOOK))) {
                    return null;
                }
            }
        }

        // Prevent combining hammers with hoes.
        if (isHammer(item1) != isHammer(item2) && item1.getType().equals(item2.getType())) {
            return null;
        }

        // Calculate resultItem durability
        if (!getToolMaterial(resultItem.getType()).equals(OTHER) && !item2.getType().equals(Material.ENCHANTED_BOOK)) {
            int maxDurability = item1.getType().getMaxDurability();
            for (var player : Bukkit.getOnlinePlayers()) {
            }
            int newDamage;
            if (!getToolMaterial(item2.getType()).equals(OTHER)) {
                var newDurability = 2 * maxDurability - ((Damageable) item1ItemMeta).getDamage() - ((Damageable) item2.getItemMeta()).getDamage();
                for (var player : Bukkit.getOnlinePlayers()) {
                }
                newDamage = maxDurability - newDurability;
            } else {
                var decrease = maxDurability * .25;
                for (var player : Bukkit.getOnlinePlayers()) {
                }
                newDamage = (int) (((Damageable) item1ItemMeta).getDamage() - decrease * item2.getAmount());

                for (var player : Bukkit.getOnlinePlayers()) {
                }
            }

            // Set resultItem damage
            ((Damageable) resultItemMeta).setDamage(Math.max(0, newDamage));
            resultItem.setItemMeta(resultItemMeta);
        }

        // Add enchantments to Maps
        Map<Enchantment, Integer> enchantments2;
        Map<Enchantment, Integer> enchantments1;
        if (item2.getItemMeta() instanceof EnchantmentStorageMeta) {
            var enchantMeta = (EnchantmentStorageMeta) item2.getItemMeta();
            enchantments2 = enchantMeta.getStoredEnchants();
        } else {
            enchantments2 = item2.getEnchantments();
        }
        if (resultItem.getItemMeta() instanceof EnchantmentStorageMeta) {
            var enchantMeta = (EnchantmentStorageMeta) resultItem.getItemMeta();
            enchantments1 = enchantMeta.getStoredEnchants();
        } else {
            enchantments1 = resultItem.getEnchantments();
        }

        // Combine enchantments into 1 Map
        Map<Enchantment, Integer> resultEnchantments = new HashMap<>();
        for (var enchantment : enchantments1.keySet()) {
            resultEnchantments.put(enchantment, enchantments1.get(enchantment));
        }
        for (var enchantment : enchantments2.keySet()) {
            var level2 = enchantments2.get(enchantment);
            if (enchantments1.containsKey(enchantment)) {
                var level1 = enchantments1.get(enchantment);
                if (!enchantment.equals(Enchantment.BINDING_CURSE) && !enchantment.equals(Enchantment.VANISHING_CURSE)) {
                    resultEnchantments.remove(enchantment);
                    if (level1.equals(level2)) {
                        resultEnchantments.put(enchantment, 1 + level2);
                    } else {
                        resultEnchantments.put(enchantment, Math.max(level2, level1));
                    }
                }
            } else {
                resultEnchantments.put(enchantment, level2);
            }
        }

        // Return null if items are stacked
        if (resultItem.getAmount() > 1) return null;

        // Remove enchantments that the hammer cannot handle
        resultEnchantments = applyHammer(hammer, resultItem, resultEnchantments);
        if (resultEnchantments == null) {
            return null;
        }

        // Set to null if there is a pair of incompatible enchantments
        // Bane/Smite/Sharpness TODO Cleaving
        var count = 0;
        if (resultEnchantments.containsKey(Enchantment.DAMAGE_ARTHROPODS)) count++;
        if (resultEnchantments.containsKey(Enchantment.DAMAGE_UNDEAD)) count++;
        if (resultEnchantments.containsKey(Enchantment.DAMAGE_ALL)) count++;
        if (count > 1) return null;

        // Protection
        count = 0;
        if (resultEnchantments.containsKey(Enchantment.PROTECTION_ENVIRONMENTAL)) count++;
        if (resultEnchantments.containsKey(Enchantment.PROTECTION_EXPLOSIONS)) count++;
        if (resultEnchantments.containsKey(Enchantment.PROTECTION_PROJECTILE)) count++;
        if (resultEnchantments.containsKey(Enchantment.PROTECTION_FIRE)) count++;
        if (count > 1) return null;

        // Channeling/Riptide
        count = 0;
        if (resultEnchantments.containsKey(Enchantment.CHANNELING)) count++;
        if (resultEnchantments.containsKey(Enchantment.RIPTIDE)) count++;
        if (count > 1) return null;

        // Depth Strider/Frost Walker
        count = 0;
        if (resultEnchantments.containsKey(Enchantment.DEPTH_STRIDER)) count++;
        if (resultEnchantments.containsKey(Enchantment.FROST_WALKER)) count++;
        if (count > 1) return null;

        // Fortune/Silk Touch
        count = 0;
        if (resultEnchantments.containsKey(Enchantment.SILK_TOUCH)) count++;
        if (resultEnchantments.containsKey(Enchantment.LOOT_BONUS_BLOCKS)) count++;
        if (count > 1) return null;

        // Infinity/Mending
        count = 0;
        if (resultEnchantments.containsKey(Enchantment.MENDING)) count++;
        if (resultEnchantments.containsKey(Enchantment.ARROW_INFINITE)) count++;
        if (count > 1) return null;

        // Loyalty/Riptide
        count = 0;
        if (resultEnchantments.containsKey(Enchantment.LOYALTY)) count++;
        if (resultEnchantments.containsKey(Enchantment.RIPTIDE)) count++;
        if (count > 1) return null;

        // Multishot/Piercing
        count = 0;
        if (resultEnchantments.containsKey(Enchantment.PIERCING)) count++;
        if (resultEnchantments.containsKey(Enchantment.MULTISHOT)) count++;
        if (count > 1) return null;

        // Add enchantments to resultItem
        if (resultItem.getItemMeta() instanceof EnchantmentStorageMeta) {
            var enchantMeta = (EnchantmentStorageMeta) resultItem.getItemMeta();
            for (var enchant : new HashSet<>(enchantMeta.getStoredEnchants().keySet())) {
                enchantMeta.removeStoredEnchant(enchant);
            }
            for (var enchant : resultEnchantments.keySet()) {
                enchantMeta.addStoredEnchant(enchant, resultEnchantments.get(enchant), true);
            }
            resultItem.setItemMeta(enchantMeta);
        } else {
            for (var enchant : new HashSet<>(resultItem.getEnchantments().keySet())) {
                resultItem.removeEnchantment(enchant);
            }
            for (var enchant : resultEnchantments.keySet()) {
                if(isHammer(resultItem) || enchant.canEnchantItem(new ItemStack(resultItem.getType()))) {
                    resultItem.addUnsafeEnchantment(enchant, resultEnchantments.get(enchant));
                }
            }
        }

        // Return resultItem
        return resultItem;
    }

    private @Nullable Map<Enchantment, Integer> applyHammer(ItemStack hammer, ItemStack result, Map<Enchantment, Integer> enchantments) {
        if (result == null) {
            return null;
        }
        var hammerType = getToolMaterial(hammer.getType());
        var resultType = getToolMaterial(result.getType());
        int hammerLevel;
        int resultLevel;

        switch (hammerType) {
            case WOODEN -> hammerLevel = 1;
            case STONE, GOLDEN -> hammerLevel = 2;
            case IRON -> hammerLevel = 3;
            case DIAMOND -> hammerLevel = 4;
            case NETHERITE -> hammerLevel = 5;
            default -> {
                return null;
            }
        }

        switch (resultType) {
            case CHAIN, STONE -> resultLevel = 1;
            case GOLDEN, IRON -> resultLevel = 2;
            case DIAMOND -> resultLevel = 3;
            case NETHERITE -> resultLevel = 4;
            default -> resultLevel = 0;

        }

        if (resultLevel >= hammerLevel) {
            return null;
        }

        for (var enchantment : new HashSet<>(enchantments.keySet())) {
            if (enchantments.get(enchantment) == 1) continue;
            if (!hammer.getEnchantments().containsKey(enchantment)) {
                enchantments.remove(enchantment);
                enchantments.put(enchantment, 1);
                continue;
            }
            var level = enchantments.get(enchantment);
            if (level > 1 + hammer.getEnchantmentLevel(enchantment)) {
                enchantments.remove(enchantment);
                enchantments.put(enchantment, 1 + hammer.getEnchantmentLevel(enchantment));
            }
        }
        return enchantments;
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
                var hasHammer = isHammer(mainHand);
                if (!hasHammer) {
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
            if (event.getView().getTopInventory() instanceof AnvilInventory) {
                if (event.getSlotType().equals(InventoryType.SlotType.QUICKBAR)) {
                    if (event.getWhoClicked().getInventory().getHeldItemSlot() == event.getSlot()) {
                        event.getWhoClicked().sendMessage(ChatColor.RED + "You cannot let go of your hammer right now.");
                        event.setCancelled(true);
                    }
                } else if (event.getSlotType().equals(InventoryType.SlotType.RESULT)
                && event.getInventory().getItem(event.getSlot()) != null
                && ((AnvilInventory) event.getInventory()).getRepairCost() > 0) {
                    var player = event.getWhoClicked();
                    var item = player.getInventory().getItemInMainHand();
                    var itemMeta = item.getItemMeta();
                    if (itemMeta == null) return;
                    if (!(itemMeta instanceof Damageable)) return;
                    var damage = ((Damageable) itemMeta).getDamage();
                    damage += 5;
                    if (damage > item.getType().getMaxDurability()) {
                        player.getInventory().setItemInMainHand(null);
                        player.playEffect(EntityEffect.BREAK_EQUIPMENT_MAIN_HAND);
                        ((Player) player).playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                    } else {
                        ((Damageable) itemMeta).setDamage(damage);
                        item.setItemMeta(itemMeta);
                        player.getInventory().setItemInMainHand(item);
                    }
                }

            }
        }

        @EventHandler
        public void onVillagerAcquireTrade(VillagerAcquireTradeEvent event) {
            if (event.getRecipe().getResult().equals(new ItemStack(Material.NAME_TAG))) {
                var recipes = new ArrayList<>(event.getEntity().getRecipes());
                var knowledgeBookRecipe = new MerchantRecipe(getKnowledgeBook(), 1);
                knowledgeBookRecipe.addIngredient(new ItemStack(Material.EMERALD, (int) (Math.random() * 17 + 48)));
                recipes.add(knowledgeBookRecipe);
                event.getEntity().setRecipes(recipes);
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onPrepareAnvil(PrepareAnvilEvent event) {
            var anvilInv = event.getInventory();
            var item1 = anvilInv.getItem(0);
            var item2 = anvilInv.getItem(1);

            var holder = event.getView().getBottomInventory().getHolder();
            if (holder == null) return;
            if (!(holder instanceof Player)) return;

            var player = (Player) holder;
            var hammer = player.getInventory().getItemInMainHand();

            if (!isHammer(hammer)) return;

            // Return if there is no item1.
            if (item1 == null) return;

            var item1Meta = item1.getItemMeta();
            if (item1Meta == null) return;

            var result = getResult(item1, item2, hammer, event.getInventory().getRenameText());

            // Turn result to null if it is identical to item1
            if (item1.equals(result)) result = null;

            // Set the result
            event.setResult(result);

            // Calculate the cost
            if (result != null) {
                var cost = 0;
                for (var enchant : result.getEnchantments().keySet()) {
                    if (!enchant.equals(Enchantment.BINDING_CURSE) && !enchant.equals(Enchantment.VANISHING_CURSE)) {
                        cost += Math.pow(2, result.getEnchantmentLevel(enchant) - 1);
                    }
                }
                var finalCost = 1 + cost;

                // Set the cost
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> event.getInventory().setMaximumRepairCost(finalCost + 1), 1);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> event.getInventory().setRepairCost(finalCost), 1);
            }
        }
    }
}
