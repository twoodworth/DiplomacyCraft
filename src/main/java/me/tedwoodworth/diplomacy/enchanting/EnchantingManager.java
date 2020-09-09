package me.tedwoodworth.diplomacy.enchanting;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareSmithingEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class EnchantingManager {
    private static EnchantingManager instance = null;
    final String HAMMER_LORE = ChatColor.BLUE + "Hammer";

    private List<Material> netheriteTools;
    private List<Material> diamondTools;
    private List<Material> ironTools;
    private List<Material> goldenTools;
    private List<Material> chainArmor;
    private List<Material> stoneTools;
    private List<Material> woodenTools;
    private List<Material> planks;
    private List<Material> swords;
    private List<Material> axes;
    private List<Material> pickShovelHoes;
    private List<Material> helmets;
    private List<Material> chestplates;
    private List<Material> leggings;
    private List<Material> boots;
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
    private final String FISHING_ROD = "Fishing_Rod";
    private final String OTHER = "Other";
    private final String SWORD = "Sword";
    private final String AXE = "Axe";
    private final String PICK_SHOVEL_HOE = "Pick_shovel_hoe";
    private final String BOW = "bow";
    private final String TRIDENT = "trident";
    private final String CROSSBOW = "crossbow";
    private final String HELMET = "helmet";
    private final String CHESTPLATE = "Chestplate";
    private final String LEGGINGS = "Leggings";
    private final String BOOTS = "Boots";
    private final String BOOK = "Book";
    private final String HAMMER = "Hammer";

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
            this.woodenTools = woodenTools;
        }
        return woodenTools;
    }

    private List<Material> getSwords() {
        if (Objects.equals(this.swords, null)) {
            var swords = new ArrayList<Material>();
            swords.add(Material.WOODEN_SWORD);
            swords.add(Material.STONE_SWORD);
            swords.add(Material.IRON_SWORD);
            swords.add(Material.GOLDEN_SWORD);
            swords.add(Material.DIAMOND_SWORD);
            swords.add(Material.NETHERITE_SWORD);
            this.swords = swords;
        }
        return swords;
    }

    private List<Material> getAxes() {
        if (Objects.equals(this.axes, null)) {
            var axes = new ArrayList<Material>();
            axes.add(Material.WOODEN_AXE);
            axes.add(Material.STONE_AXE);
            axes.add(Material.IRON_AXE);
            axes.add(Material.GOLDEN_AXE);
            axes.add(Material.DIAMOND_AXE);
            axes.add(Material.NETHERITE_AXE);
            this.axes = axes;
        }
        return axes;
    }

    private List<Material> getPickShovelHoes() {
        if (Objects.equals(this.pickShovelHoes, null)) {
            var pickShovelHoes = new ArrayList<Material>();
            pickShovelHoes.add(Material.WOODEN_PICKAXE);
            pickShovelHoes.add(Material.STONE_PICKAXE);
            pickShovelHoes.add(Material.IRON_PICKAXE);
            pickShovelHoes.add(Material.GOLDEN_PICKAXE);
            pickShovelHoes.add(Material.DIAMOND_PICKAXE);
            pickShovelHoes.add(Material.NETHERITE_PICKAXE);
            pickShovelHoes.add(Material.WOODEN_SHOVEL);
            pickShovelHoes.add(Material.STONE_SHOVEL);
            pickShovelHoes.add(Material.IRON_SHOVEL);
            pickShovelHoes.add(Material.GOLDEN_SHOVEL);
            pickShovelHoes.add(Material.DIAMOND_SHOVEL);
            pickShovelHoes.add(Material.NETHERITE_SHOVEL);
            pickShovelHoes.add(Material.WOODEN_HOE);
            pickShovelHoes.add(Material.STONE_HOE);
            pickShovelHoes.add(Material.IRON_HOE);
            pickShovelHoes.add(Material.GOLDEN_HOE);
            pickShovelHoes.add(Material.DIAMOND_HOE);
            pickShovelHoes.add(Material.NETHERITE_HOE);
            this.pickShovelHoes = pickShovelHoes;
        }
        return pickShovelHoes;
    }

    private List<Material> getHelmets() {
        if (Objects.equals(this.helmets, null)) {
            var helmets = new ArrayList<Material>();
            helmets.add(Material.TURTLE_HELMET);
            helmets.add(Material.LEATHER_HELMET);
            helmets.add(Material.CHAINMAIL_HELMET);
            helmets.add(Material.IRON_HELMET);
            helmets.add(Material.GOLDEN_HELMET);
            helmets.add(Material.DIAMOND_HELMET);
            helmets.add(Material.NETHERITE_HELMET);
            this.helmets = helmets;
        }
        return helmets;
    }

    private List<Material> getChestplates() {
        if (Objects.equals(this.chestplates, null)) {
            var chestplates = new ArrayList<Material>();
            chestplates.add(Material.LEATHER_CHESTPLATE);
            chestplates.add(Material.CHAINMAIL_CHESTPLATE);
            chestplates.add(Material.IRON_CHESTPLATE);
            chestplates.add(Material.GOLDEN_CHESTPLATE);
            chestplates.add(Material.DIAMOND_CHESTPLATE);
            chestplates.add(Material.NETHERITE_CHESTPLATE);
            this.chestplates = chestplates;
        }
        return chestplates;
    }

    private List<Material> getLeggings() {
        if (Objects.equals(this.leggings, null)) {
            var leggings = new ArrayList<Material>();
            leggings.add(Material.LEATHER_LEGGINGS);
            leggings.add(Material.CHAINMAIL_LEGGINGS);
            leggings.add(Material.IRON_LEGGINGS);
            leggings.add(Material.GOLDEN_LEGGINGS);
            leggings.add(Material.DIAMOND_LEGGINGS);
            leggings.add(Material.NETHERITE_LEGGINGS);
            this.leggings = leggings;
        }
        return leggings;
    }

    private List<Material> getBoots() {
        if (Objects.equals(this.boots, null)) {
            var boots = new ArrayList<Material>();
            boots.add(Material.LEATHER_BOOTS);
            boots.add(Material.CHAINMAIL_BOOTS);
            boots.add(Material.IRON_BOOTS);
            boots.add(Material.GOLDEN_BOOTS);
            boots.add(Material.DIAMOND_BOOTS);
            boots.add(Material.NETHERITE_BOOTS);
            this.boots = boots;
        }
        return boots;
    }

    public static EnchantingManager getInstance() {
        if (instance == null) {
            instance = new EnchantingManager();
        }
        return instance;
    }

    public String getToolType(Material item) {
        if (getSwords().contains(item)) {
            return SWORD;
        } else if (getAxes().contains(item)) {
            return AXE;
        } else if (getPickShovelHoes().contains(item)) {
            return PICK_SHOVEL_HOE;
        } else if (item.equals(Material.BOW)) {
            return BOW;
        } else if (item.equals(Material.FISHING_ROD)) {
            return FISHING_ROD;
        } else if (item.equals(Material.TRIDENT)) {
            return TRIDENT;
        } else if (item.equals(Material.CROSSBOW)) {
            return CROSSBOW;
        } else if (getHelmets().contains(item)) {
            return HELMET;
        } else if (getChestplates().contains(item)) {
            return CHESTPLATE;
        } else if (getLeggings().contains(item)) {
            return LEGGINGS;
        } else if (getBoots().contains(item)) {
            return BOOTS;
        } else if (item.equals(Material.BOOK)) {
            return BOOK;
        } else {
            return OTHER;
        }
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
        } else if (item.equals(Material.FISHING_ROD)) {
            return FISHING_ROD;
        } else if (item.equals(Material.BOOK)) {
            return BOOK;
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
            case OTHER, BOOK -> {
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
            int newDamage;
            if (!getToolMaterial(item2.getType()).equals(OTHER)) {
                var newDurability = 2 * maxDurability - ((Damageable) item1ItemMeta).getDamage() - ((Damageable) item2.getItemMeta()).getDamage();
                newDamage = maxDurability - newDurability;
            } else {
                var decrease = maxDurability * .25;
                newDamage = (int) (((Damageable) item1ItemMeta).getDamage() - decrease * item2.getAmount());
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
                if (isHammer(resultItem) || enchant.canEnchantItem(new ItemStack(resultItem.getType()))) {
                    resultItem.addUnsafeEnchantment(enchant, resultEnchantments.get(enchant));
                }
            }
        }

        // Return resultItem
        return resultItem;
    }

    private @Nullable Map<Enchantment, Integer> getEnchantment(String toolType, int tier) {
        // Hammer
        if (toolType.equals(HAMMER)) {

            // Hammer Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(11)) {
                    case 0 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 1);
                    case 2 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 3 -> ImmutableMap.of(Enchantment.IMPALING, 1);
                    case 4 -> ImmutableMap.of(Enchantment.LURE, 1);
                    case 5 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 6 -> ImmutableMap.of(Enchantment.PIERCING, 1);
                    case 7 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 1);
                    case 8 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 1);
                    case 9 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 1);
                    case 10 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 1);
                    default -> null;
                };

                // Hammer tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(20)) {
                    case 0 -> ImmutableMap.of(Enchantment.WATER_WORKER, 1);
                    case 1 -> ImmutableMap.of(Enchantment.CHANNELING, 1);
                    case 2 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 3 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 1);
                    case 4 -> ImmutableMap.of(Enchantment.KNOCKBACK, 1);
                    case 5 -> ImmutableMap.of(Enchantment.LUCK, 1);
                    case 6 -> ImmutableMap.of(Enchantment.LURE, 1);
                    case 7 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 1);
                    case 8 -> ImmutableMap.of(Enchantment.ARROW_KNOCKBACK, 1);
                    case 9 -> ImmutableMap.of(Enchantment.QUICK_CHARGE, 1);
                    case 10 -> ImmutableMap.of(Enchantment.OXYGEN, 1);
                    case 11 -> ImmutableMap.of(Enchantment.THORNS, 1);
                    case 12 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 2);
                    case 13 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 2);
                    case 14 -> ImmutableMap.of(Enchantment.IMPALING, 2);
                    case 15 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 16 -> ImmutableMap.of(Enchantment.PIERCING, 2);
                    case 17 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 2);
                    case 18 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 2);
                    case 19 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 2);
                    default -> null;
                };

                // Hammer tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(28)) {
                    case 0 -> ImmutableMap.of(Enchantment.DEPTH_STRIDER, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 2 -> ImmutableMap.of(Enchantment.PROTECTION_FALL, 1);
                    case 3 -> ImmutableMap.of(Enchantment.FIRE_ASPECT, 1);
                    case 4 -> ImmutableMap.of(Enchantment.ARROW_FIRE, 1);
                    case 5 -> ImmutableMap.of(Enchantment.LOYALTY, 1);
                    case 6 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 1);
                    case 7 -> ImmutableMap.of(Enchantment.RIPTIDE, 1);
                    case 8 -> ImmutableMap.of(Enchantment.SILK_TOUCH, 1);
                    case 9 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 10 -> ImmutableMap.of(Enchantment.WATER_WORKER, 1);
                    case 11 -> ImmutableMap.of(Enchantment.CHANNELING, 2);
                    case 12 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 2);
                    case 13 -> ImmutableMap.of(Enchantment.KNOCKBACK, 2);
                    case 14 -> ImmutableMap.of(Enchantment.LUCK, 2);
                    case 15 -> ImmutableMap.of(Enchantment.LURE, 2);
                    case 16 -> ImmutableMap.of(Enchantment.ARROW_KNOCKBACK, 2);
                    case 17 -> ImmutableMap.of(Enchantment.QUICK_CHARGE, 2);
                    case 18 -> ImmutableMap.of(Enchantment.OXYGEN, 2);
                    case 19 -> ImmutableMap.of(Enchantment.THORNS, 2);
                    case 20 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 3);
                    case 21 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 3);
                    case 22 -> ImmutableMap.of(Enchantment.IMPALING, 3);
                    case 23 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 24 -> ImmutableMap.of(Enchantment.PIERCING, 3);
                    case 25 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 3);
                    case 26 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 3);
                    case 27 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 3);
                    default -> null;
                };

                // Hammer tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(33)) {
                    case 0 -> ImmutableMap.of(Enchantment.LOOT_BONUS_BLOCKS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.ARROW_INFINITE, 1);
                    case 2 -> ImmutableMap.of(Enchantment.LOOT_BONUS_MOBS, 1);
                    case 3 -> ImmutableMap.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    case 4 -> ImmutableMap.of(Enchantment.DAMAGE_ALL, 1);
                    case 5 -> ImmutableMap.of(Enchantment.DEPTH_STRIDER, 2);
                    case 6 -> ImmutableMap.of(Enchantment.DIG_SPEED, 2);
                    case 7 -> ImmutableMap.of(Enchantment.PROTECTION_FALL, 2);
                    case 8 -> ImmutableMap.of(Enchantment.FIRE_ASPECT, 2);
                    case 9 -> ImmutableMap.of(Enchantment.ARROW_FIRE, 1);
                    case 10 -> ImmutableMap.of(Enchantment.LOYALTY, 2);
                    case 11 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 2);
                    case 12 -> ImmutableMap.of(Enchantment.RIPTIDE, 2);
                    case 13 -> ImmutableMap.of(Enchantment.SILK_TOUCH, 1);
                    case 14 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 15 -> ImmutableMap.of(Enchantment.WATER_WORKER, 1);
                    case 16 -> ImmutableMap.of(Enchantment.CHANNELING, 3);
                    case 17 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 3);
                    case 18 -> ImmutableMap.of(Enchantment.KNOCKBACK, 3);
                    case 19 -> ImmutableMap.of(Enchantment.LUCK, 3);
                    case 20 -> ImmutableMap.of(Enchantment.LURE, 3);
                    case 21 -> ImmutableMap.of(Enchantment.ARROW_KNOCKBACK, 3);
                    case 22 -> ImmutableMap.of(Enchantment.QUICK_CHARGE, 3);
                    case 23 -> ImmutableMap.of(Enchantment.OXYGEN, 3);
                    case 24 -> ImmutableMap.of(Enchantment.THORNS, 3);
                    case 25 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 4);
                    case 26 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 4);
                    case 27 -> ImmutableMap.of(Enchantment.IMPALING, 4);
                    case 28 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 29 -> ImmutableMap.of(Enchantment.PIERCING, 4);
                    case 30 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 4);
                    case 31 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 4);
                    case 32 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 4);
                    default -> null;
                };
            }
            //Book
        } else if (toolType.equals(BOOK)) {
            // Book Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(22)) {
                    case 0, 1 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 1);
                    case 2, 3, 4, 5 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 1);
                    case 6, 7, 8, 9 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 10 -> ImmutableMap.of(Enchantment.IMPALING, 1);
                    case 11 -> ImmutableMap.of(Enchantment.LURE, 1);
                    case 12 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 13 -> ImmutableMap.of(Enchantment.PIERCING, 1);
                    case 14 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 1);
                    case 15, 16, 17, 18 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 1);
                    case 19, 20 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 1);
                    case 21 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 1);
                    default -> null;
                };

                // Book tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(36)) {
                    case 0 -> ImmutableMap.of(Enchantment.WATER_WORKER, 1);
                    case 1 -> ImmutableMap.of(Enchantment.CHANNELING, 1);
                    case 2, 3, 4, 5 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 6, 7, 8, 9 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 1);
                    case 10 -> ImmutableMap.of(Enchantment.KNOCKBACK, 1);
                    case 11 -> ImmutableMap.of(Enchantment.LUCK, 1);
                    case 12 -> ImmutableMap.of(Enchantment.LURE, 1);
                    case 13 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 1);
                    case 14 -> ImmutableMap.of(Enchantment.ARROW_KNOCKBACK, 1);
                    case 15 -> ImmutableMap.of(Enchantment.QUICK_CHARGE, 1);
                    case 16 -> ImmutableMap.of(Enchantment.OXYGEN, 1);
                    case 17, 18, 19, 20 -> ImmutableMap.of(Enchantment.THORNS, 1);
                    case 21, 22 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 2);
                    case 23, 24, 25, 26 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 2);
                    case 27 -> ImmutableMap.of(Enchantment.IMPALING, 2);
                    case 28 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 29 -> ImmutableMap.of(Enchantment.PIERCING, 2);
                    case 30, 31, 32, 33 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 2);
                    case 34, 35 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 2);
                    case 36 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 2);
                    default -> null;
                };

                // Book tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(61)) {
                    case 0 -> ImmutableMap.of(Enchantment.DEPTH_STRIDER, 1);
                    case 1, 2, 3, 4 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 5 -> ImmutableMap.of(Enchantment.PROTECTION_FALL, 1);
                    case 6 -> ImmutableMap.of(Enchantment.FIRE_ASPECT, 1);
                    case 7 -> ImmutableMap.of(Enchantment.ARROW_FIRE, 1);
                    case 8 -> ImmutableMap.of(Enchantment.LOYALTY, 1);
                    case 9 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 1);
                    case 10 -> ImmutableMap.of(Enchantment.RIPTIDE, 1);
                    case 11, 12, 13, 14 -> ImmutableMap.of(Enchantment.SILK_TOUCH, 1);
                    case 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 29 -> ImmutableMap.of(Enchantment.WATER_WORKER, 1);
                    case 30 -> ImmutableMap.of(Enchantment.CHANNELING, 2);
                    case 31, 32, 33, 34 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 2);
                    case 35 -> ImmutableMap.of(Enchantment.KNOCKBACK, 2);
                    case 36 -> ImmutableMap.of(Enchantment.LUCK, 2);
                    case 37 -> ImmutableMap.of(Enchantment.LURE, 2);
                    case 38 -> ImmutableMap.of(Enchantment.ARROW_KNOCKBACK, 2);
                    case 39 -> ImmutableMap.of(Enchantment.QUICK_CHARGE, 2);
                    case 40 -> ImmutableMap.of(Enchantment.OXYGEN, 2);
                    case 41, 42, 43, 44 -> ImmutableMap.of(Enchantment.THORNS, 2);
                    case 45, 46 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 3);
                    case 47, 48, 49, 50 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 3);
                    case 51 -> ImmutableMap.of(Enchantment.IMPALING, 3);
                    case 52 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 53 -> ImmutableMap.of(Enchantment.PIERCING, 3);
                    case 54, 55, 56, 57 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 3);
                    case 58, 59 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 3);
                    case 60 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 3);
                    default -> null;
                };

                // Book tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(73)) {
                    case 0, 1, 2, 3 -> ImmutableMap.of(Enchantment.LOOT_BONUS_BLOCKS, 1);
                    case 4 -> ImmutableMap.of(Enchantment.ARROW_INFINITE, 1);
                    case 5 -> ImmutableMap.of(Enchantment.LOOT_BONUS_MOBS, 1);
                    case 6, 7, 8, 9 -> ImmutableMap.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    case 10, 11 -> ImmutableMap.of(Enchantment.DAMAGE_ALL, 1);
                    case 12 -> ImmutableMap.of(Enchantment.DEPTH_STRIDER, 2);
                    case 13, 14, 15, 16 -> ImmutableMap.of(Enchantment.DIG_SPEED, 2);
                    case 17 -> ImmutableMap.of(Enchantment.PROTECTION_FALL, 2);
                    case 18 -> ImmutableMap.of(Enchantment.FIRE_ASPECT, 2);
                    case 19 -> ImmutableMap.of(Enchantment.ARROW_FIRE, 1);
                    case 20 -> ImmutableMap.of(Enchantment.LOYALTY, 2);
                    case 21 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 2);
                    case 22 -> ImmutableMap.of(Enchantment.RIPTIDE, 2);
                    case 23, 24, 25, 26 -> ImmutableMap.of(Enchantment.SILK_TOUCH, 1);
                    case 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 41 -> ImmutableMap.of(Enchantment.WATER_WORKER, 1);
                    case 42 -> ImmutableMap.of(Enchantment.CHANNELING, 3);
                    case 43, 44, 45, 46 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 3);
                    case 47 -> ImmutableMap.of(Enchantment.KNOCKBACK, 3);
                    case 48 -> ImmutableMap.of(Enchantment.LUCK, 3);
                    case 49 -> ImmutableMap.of(Enchantment.LURE, 3);
                    case 50 -> ImmutableMap.of(Enchantment.ARROW_KNOCKBACK, 3);
                    case 51 -> ImmutableMap.of(Enchantment.QUICK_CHARGE, 3);
                    case 52 -> ImmutableMap.of(Enchantment.OXYGEN, 3);
                    case 53, 54, 55, 56 -> ImmutableMap.of(Enchantment.THORNS, 3);
                    case 57, 58 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 4);
                    case 59, 60, 61, 62 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 4);
                    case 63 -> ImmutableMap.of(Enchantment.IMPALING, 4);
                    case 64 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 65 -> ImmutableMap.of(Enchantment.PIERCING, 4);
                    case 66, 67, 68, 69 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 4);
                    case 70, 71 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 4);
                    case 72 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 4);
                    default -> null;
                };
            }
            // Sword
        } else if (toolType.equals(SWORD)) {

            // Sword Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(3)) {
                    case 0 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 1);
                    case 2 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 1);
                    default -> null;
                };

                // Sword Tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(20)) {
                    case 0 -> ImmutableMap.of(Enchantment.KNOCKBACK, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 2);
                    case 2 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 2);
                    case 3 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 2);
                    default -> null;
                };

                // Sword Tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(6)) {
                    case 0 -> ImmutableMap.of(Enchantment.FIRE_ASPECT, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 2 -> ImmutableMap.of(Enchantment.KNOCKBACK, 2);
                    case 3 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 3);
                    case 4 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 3);
                    case 5 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 3);
                    default -> null;
                };

                // Sword Tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(8)) {
                    case 0 -> ImmutableMap.of(Enchantment.LOOT_BONUS_MOBS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DAMAGE_ALL, 1);
                    case 2 -> ImmutableMap.of(Enchantment.FIRE_ASPECT, 2);
                    case 3 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 4 -> ImmutableMap.of(Enchantment.KNOCKBACK, 3);
                    case 5 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 4);
                    case 6 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 4);
                    case 7 -> ImmutableMap.of(Enchantment.SWEEPING_EDGE, 4);
                    default -> null;
                };
            }
        } else if (toolType.equals(AXE)) {

            // Axe Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(3)) {
                    case 0 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 2 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 1);
                    default -> null;
                };

                // Axe tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(3)) {
                    case 0 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 2);
                    case 2 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 2);
                    default -> null;
                };

                // Hammer tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(5)) {
                    case 0 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 1 -> ImmutableMap.of(Enchantment.SILK_TOUCH, 1);
                    case 2 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 3 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 3);
                    case 4 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 3);
                    default -> null;
                };

                // Hammer tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(7)) {
                    case 0 -> ImmutableMap.of(Enchantment.LOOT_BONUS_BLOCKS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DAMAGE_ALL, 1);
                    case 2 -> ImmutableMap.of(Enchantment.DIG_SPEED, 2);
                    case 3 -> ImmutableMap.of(Enchantment.SILK_TOUCH, 1);
                    case 4 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 5 -> ImmutableMap.of(Enchantment.DAMAGE_ARTHROPODS, 4);
                    case 6 -> ImmutableMap.of(Enchantment.DAMAGE_UNDEAD, 4);
                    default -> null;
                };
            }
        } else if (toolType.equals(PICK_SHOVEL_HOE)) {

            // Pick/Shovel/Hoe Tier 0 & 1
            if (tier == 0 || tier == 1) {
                return ImmutableMap.of(Enchantment.DIG_SPEED, 1);

                // Pick/Shovel/Hoe tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(3)) {
                    case 0 -> ImmutableMap.of(Enchantment.DIG_SPEED, 1);
                    case 1 -> ImmutableMap.of(Enchantment.SILK_TOUCH, 1);
                    case 2 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    default -> null;
                };

                // Pick/Shovel/Hoe tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(33)) {
                    case 0 -> ImmutableMap.of(Enchantment.LOOT_BONUS_BLOCKS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DIG_SPEED, 2);
                    case 2 -> ImmutableMap.of(Enchantment.SILK_TOUCH, 1);
                    case 3 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    default -> null;
                };
            }
            //Book
        } else if (toolType.equals(BOW)) {

            // Bow Tier 0
            if (tier == 0) {
                return ImmutableMap.of(Enchantment.ARROW_DAMAGE, 1);
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(2)) {
                    case 0 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 1);
                    case 1 -> ImmutableMap.of(Enchantment.ARROW_KNOCKBACK, 1);
                    default -> null;
                };

                // Bow tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(4)) {
                    case 0 -> ImmutableMap.of(Enchantment.ARROW_FIRE, 1);
                    case 1 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 1);
                    case 2 -> ImmutableMap.of(Enchantment.ARROW_KNOCKBACK, 2);
                    case 3 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    default -> null;
                };

                // Bow tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(5)) {
                    case 0 -> ImmutableMap.of(Enchantment.ARROW_INFINITE, 1);
                    case 1 -> ImmutableMap.of(Enchantment.ARROW_FIRE, 1);
                    case 2 -> ImmutableMap.of(Enchantment.ARROW_DAMAGE, 2);
                    case 3 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 4 -> ImmutableMap.of(Enchantment.ARROW_KNOCKBACK, 3);
                    default -> null;
                };
            }
        } else if (toolType.equals(FISHING_ROD)) {

            // Rod Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(0)) {
                    case 0 -> ImmutableMap.of(Enchantment.LURE, 1);
                    default -> null;
                };

                // Rod tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(2)) {
                    case 0 -> ImmutableMap.of(Enchantment.LUCK, 1);
                    case 1 -> ImmutableMap.of(Enchantment.LURE, 1);
                    default -> null;
                };

                // Rod tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(3)) {
                    case 0 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 1 -> ImmutableMap.of(Enchantment.LUCK, 2);
                    case 2 -> ImmutableMap.of(Enchantment.LURE, 2);
                    default -> null;
                };

                // Rod tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(3)) {
                    case 0 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 1 -> ImmutableMap.of(Enchantment.LUCK, 3);
                    case 2 -> ImmutableMap.of(Enchantment.LURE, 3);
                    default -> null;
                };
            }
        } else if (toolType.equals(TRIDENT)) {

            // Trident Tier 0
            if (tier == 0) {
                return ImmutableMap.of(Enchantment.IMPALING, 1);

                // Trident tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(2)) {
                    case 0 -> ImmutableMap.of(Enchantment.CHANNELING, 1);
                    case 1 -> ImmutableMap.of(Enchantment.IMPALING, 2);
                    default -> null;
                };

                // Trident tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(5)) {
                    case 0 -> ImmutableMap.of(Enchantment.LOYALTY, 1);
                    case 1 -> ImmutableMap.of(Enchantment.RIPTIDE, 1);
                    case 2 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 3 -> ImmutableMap.of(Enchantment.CHANNELING, 2);
                    case 4 -> ImmutableMap.of(Enchantment.IMPALING, 3);
                    default -> null;
                };

                // Hammer tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(5)) {
                    case 0 -> ImmutableMap.of(Enchantment.LOYALTY, 2);
                    case 1 -> ImmutableMap.of(Enchantment.RIPTIDE, 2);
                    case 2 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 3 -> ImmutableMap.of(Enchantment.CHANNELING, 3);
                    case 4 -> ImmutableMap.of(Enchantment.IMPALING, 4);
                    default -> null;
                };
            }
            //Book
        } else if (toolType.equals(CROSSBOW)) {

            // Crossbow Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(2)) {
                    case 0 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PIERCING, 1);
                    default -> null;
                };

                // Crossbow tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(3)) {
                    case 0 -> ImmutableMap.of(Enchantment.QUICK_CHARGE, 1);
                    case 1 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 2 -> ImmutableMap.of(Enchantment.PIERCING, 2);
                    default -> null;
                };

                // Hammer tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(4)) {
                    case 0 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 1 -> ImmutableMap.of(Enchantment.QUICK_CHARGE, 2);
                    case 2 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 3 -> ImmutableMap.of(Enchantment.PIERCING, 3);
                    default -> null;
                };

                // Hammer tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(33)) {
                    case 0 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 1 -> ImmutableMap.of(Enchantment.QUICK_CHARGE, 3);
                    case 2 -> ImmutableMap.of(Enchantment.MULTISHOT, 1);
                    case 3 -> ImmutableMap.of(Enchantment.PIERCING, 4);
                    default -> null;
                };
            }
            // Helmet
        } else if (toolType.equals(HELMET)) {

            // Helmet Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(2)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 1);
                    default -> null;
                };

                // Helmet tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(5)) {
                    case 0 -> ImmutableMap.of(Enchantment.WATER_WORKER, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 1);
                    case 2 -> ImmutableMap.of(Enchantment.OXYGEN, 1);
                    case 3 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 2);
                    case 4 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 2);
                    default -> null;
                };

                // Hammer tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(6)) {
                    case 0 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 1 -> ImmutableMap.of(Enchantment.WATER_WORKER, 1);
                    case 2 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 2);
                    case 3 -> ImmutableMap.of(Enchantment.OXYGEN, 2);
                    case 4 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 3);
                    case 5 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 3);
                    default -> null;
                };

                // Hammer tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(7)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 2 -> ImmutableMap.of(Enchantment.WATER_WORKER, 1);
                    case 3 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 3);
                    case 4 -> ImmutableMap.of(Enchantment.OXYGEN, 3);
                    case 5 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 4);
                    case 6 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 4);
                    default -> null;
                };
            }
            //Book
        } else if (toolType.equals(CHESTPLATE)) {

            // Chestplate Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(2)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 1);
                    default -> null;
                };

                // Chestplate tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(4)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 1);
                    case 1 -> ImmutableMap.of(Enchantment.THORNS, 1);
                    case 2 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 2);
                    case 3 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 2);
                    default -> null;
                };

                // Chestplate tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(5)) {
                    case 0 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 2);
                    case 2 -> ImmutableMap.of(Enchantment.THORNS, 2);
                    case 3 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 3);
                    case 4 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 3);
                    default -> null;
                };

                // Hammer tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(6)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 2 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 3);
                    case 3 -> ImmutableMap.of(Enchantment.THORNS, 3);
                    case 4 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 4);
                    case 5 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 4);
                    default -> null;
                };
            }
            //Book
        } else if (toolType.equals(LEGGINGS)) {

            // Leggings Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(2)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 1);
                    default -> null;
                };

                // Leggings tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(3)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 2);
                    case 2 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 2);
                    default -> null;
                };

                // Leggings tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(4)) {
                    case 0 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 2);
                    case 2 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 3);
                    case 3 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 3);
                    default -> null;
                };

                // Leggings tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(5)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 2 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 3);
                    case 3 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 4);
                    case 4 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 4);
                    default -> null;
                };
            }
            //Book
        } else if (toolType.equals(BOOTS)) {

            // Boots Tier 0
            if (tier == 0) {
                var random = new Random();
                return switch (random.nextInt(2)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 1);
                    default -> null;
                };

                // Boots tier 1
            } else if (tier == 1) {
                var random = new Random();
                return switch (random.nextInt(3)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 2);
                    case 2 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 2);
                    default -> null;
                };

                // Boots tier 2
            } else if (tier == 2) {
                var random = new Random();
                return switch (random.nextInt(6)) {
                    case 0 -> ImmutableMap.of(Enchantment.DEPTH_STRIDER, 1);
                    case 1 -> ImmutableMap.of(Enchantment.PROTECTION_FALL, 1);
                    case 2 -> ImmutableMap.of(Enchantment.DURABILITY, 1);
                    case 3 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 2);
                    case 4 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 3);
                    case 5 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 3);
                    default -> null;
                };

                // Boots tier 3
            } else if (tier == 3) {
                var random = new Random();
                return switch (random.nextInt(7)) {
                    case 0 -> ImmutableMap.of(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
                    case 1 -> ImmutableMap.of(Enchantment.DEPTH_STRIDER, 2);
                    case 2 -> ImmutableMap.of(Enchantment.PROTECTION_FALL, 2);
                    case 3 -> ImmutableMap.of(Enchantment.DURABILITY, 2);
                    case 4 -> ImmutableMap.of(Enchantment.PROTECTION_FIRE, 3);
                    case 5 -> ImmutableMap.of(Enchantment.PROTECTION_EXPLOSIONS, 4);
                    case 6 -> ImmutableMap.of(Enchantment.PROTECTION_PROJECTILE, 4);
                    default -> null;
                };
            }
        }
        return null;
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
        Bukkit.getPluginManager().registerEvents(new EnchantingManager.EventListener(), Diplomacy.getInstance());
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
                lore.add(EnchantingManager.getInstance().HAMMER_LORE);
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
            } else if (isHammer(event.getItem()) && event.getAction() == Action.RIGHT_CLICK_BLOCK && (type.equals(Material.DIRT)
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

        public void onPrepareItemEnchant(PrepareItemEnchantEvent event) {
            try {
                var offers = event.getOffers();
                for (var offer : offers) {
                    offer.setEnchantment(Enchantment.ARROW_FIRE);
                    offer.setEnchantmentLevel(1);
                }
            } catch (NullPointerException ignored) {

            }
        }

        @EventHandler
        private void onEnchantItem(EnchantItemEvent event) {
            var item = event.getItem();
            if (item.getEnchantments().size() > 0) {
                event.setCancelled(true);
            }
            var toolMaterial = getToolMaterial(item.getType());
            if (toolMaterial.equals(ELYTRA) || (toolMaterial.equals(OTHER))) {
                event.setCancelled(true);
            }
            var toolType = getToolType(item.getType());
            if (toolType.equals(OTHER)) {
                event.setCancelled(true);
            }

            var location = event.getEnchantBlock().getLocation();
            var table = EnchantingTables.getInstance().getTable(location);
            var tier = event.whichButton();
            var random = Math.random();

            switch (toolMaterial) {
                case WOODEN, FISHING_ROD, NETHERITE -> {
                    if (random < .3) {
                        tier++;
                    }
                }
                case STONE -> {
                    if (random < .1) {
                        tier++;
                    }
                }
                case CHAIN -> {
                    if (random < .24) {
                        tier++;
                    }
                }
                case IRON, TURTLE -> {
                    if (random < .18) {
                        tier++;
                    }
                }
                case GOLDEN -> {
                    if (random < .5) {
                        tier++;
                    }
                }
                case DIAMOND -> {
                    if (random < .2) {
                        tier++;
                    }
                }
                case BOOK -> {
                    if (random < .15) {
                        tier++;
                    }
                }
            }

            // Check if hammer
            if (isHammer(item)) {
                toolType = HAMMER;
            }

            // Get enchantment
            var enchantmentMap = getEnchantment(toolType, tier);
            if (enchantmentMap == null) {
                event.setCancelled(true);
                return;
            }

            for (var testEnchantment : new HashSet<>(event.getEnchantsToAdd().keySet())) {
                event.getEnchantsToAdd().remove(testEnchantment);
            }

            for (var testEnchantment : enchantmentMap.keySet()) {
                boolean isArmor = toolType.equals(HELMET) || toolType.equals(CHESTPLATE) || toolType.equals(LEGGINGS) || toolType.equals(BOOTS);
                if (table.hasEnchantment(testEnchantment)) {
                    var level = enchantmentMap.get(testEnchantment);
                    event.getEnchantsToAdd().put(testEnchantment, level);
                    if (Math.random() > table.getEnchantmentKeys().size() / 33.0) {
                        if (isArmor) {
                            if (Math.random() > .5) {
                                event.getEnchantsToAdd().put(Enchantment.BINDING_CURSE, 1);
                            } else {
                                event.getEnchantsToAdd().put(Enchantment.VANISHING_CURSE, 1);
                            }
                        } else {
                            event.getEnchantsToAdd().put(Enchantment.VANISHING_CURSE, 1);
                        }
                    }
                } else {
                    if (isArmor) {
                        if (Math.random() > .5) {
                            event.getEnchantsToAdd().put(Enchantment.BINDING_CURSE, 1);
                        } else {
                            event.getEnchantsToAdd().put(Enchantment.VANISHING_CURSE, 1);
                        }
                    } else {
                        event.getEnchantsToAdd().put(Enchantment.VANISHING_CURSE, 1);
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
                var enchantments = result.getEnchantments();
                if (result.getType().equals(Material.ENCHANTED_BOOK)) {
                    var meta = result.getItemMeta();
                    var enchantMeta = (EnchantmentStorageMeta) meta;
                    if (enchantMeta != null) {
                        enchantments = enchantMeta.getStoredEnchants();
                    }
                }
                for (var enchant : enchantments.keySet()) {
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
