package me.tedwoodworth.diplomacy.enchanting;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyRecipes;
import me.tedwoodworth.diplomacy.data.FloatArrayPersistentDataType;
import org.bukkit.*;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.Furnace;
import org.bukkit.block.data.Levelled;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.bukkit.potion.PotionEffectType.*;

public class Tools {
    private static Tools instance = null;
    public final NamespacedKey purityKey = new NamespacedKey(Diplomacy.getInstance(), "purity");
    private final String purityLore = ChatColor.GRAY + "Purity:";
    private final String foldsLore = ChatColor.GRAY + "Folds:";
    private final String plateLore = ChatColor.BLUE + "Refined Plate";
    private final String refinedLore = ChatColor.BLUE + "Refined";
    private final ItemStack air = new ItemStack(Material.AIR);

    private final String NETHERITE_LAYER_LORE = ChatColor.GREEN + "Netherite Fortified";
    private final String CRYING_OBSIDIAN_LAYER_LORE = ChatColor.GREEN + "Crying Obsidian Fortified";
    private final String OBSIDIAN_LAYER_LORE = ChatColor.GREEN + "Obsidian Fortified";
    private final String ENDSTONE_LAYER_LORE = ChatColor.GREEN + "Endstone Fortified";

    private final String WITHER_LAYER_LORE = ChatColor.RED + "Wither Insulated";
    private final String MAGMA_CREAM_LAYER_LORE = ChatColor.RED + "Magma Cream Insulated";
    private final String CHORUS_LAYER_LORE = ChatColor.RED + "Chorus Insulated";
    private final String SOUL_LAYER_LORE = ChatColor.RED + "Soul Insulated";

    private final String SHULKER_LAYER_LORE = ChatColor.YELLOW + "Shulker  Embedded";
    private final String HONEY_LAYER_LORE = ChatColor.YELLOW + "Honey Embedded";
    private final String SLIME_LAYER_LORE = ChatColor.YELLOW + "Slime Embedded";
    private final String WOOD_LAYER_LORE = ChatColor.YELLOW + "Wood Embedded";

    private final String NAUTILUS_LAYER_LORE = ChatColor.BLUE + "Nautilus Padded";
    private final String SCUTE_LAYER_LORE = ChatColor.BLUE + "Scute Padded";
    private final String MEMBRANE_LAYER_LORE = ChatColor.BLUE + "Membrane Padded";
    private final String WOOL_LAYER_LORE = ChatColor.BLUE + "Wool Padded";


    private final Map<Material, String> layerLores = new HashMap<>();


    public final List<Material> layers = new ArrayList<>();
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

    public boolean isClassItem(ItemStack itemStack) {
        if (itemStack == null) return false;

        var meta = itemStack.getItemMeta();
        return meta != null
                && meta.getLore() != null
                && meta.getLore().get(0).contains("Prefix:");
    }

    public List<String> getLayers(ItemStack itemStack) {
        int layers = 0;
        var layerList = new ArrayList<String>();
        var meta = itemStack.getItemMeta();
        var lore = meta.getLore();
        if (lore != null) {
            for (var line : lore) {
                if (layerLores.containsValue(line)) {
                    layerList.add(line);
                    layers++;
                }
                if (layers == 4) break;
            }
        }
        return layerList;
    }

    public boolean hasFolds(ItemStack item) {
        return (item != null && isMetal(item) && item.getItemMeta() != null &&
                item.getItemMeta().getLore() != null && item.getItemMeta().getLore().contains(foldsLore));
    }

    public boolean isIngot(ItemStack itemStack) {
        var type = itemStack.getType();
        return type == Material.IRON_INGOT || type == Material.GOLD_INGOT || type == Material.NETHERITE_INGOT;
    }

    public boolean isPlate(ItemStack itemStack) {
        if (itemStack == null) return false;
        var meta = itemStack.getItemMeta();
        if (meta == null) return false;
        var lore = meta.getLore();
        if (lore == null) return false;
        return lore.contains(plateLore);
    }

    public boolean isMetal(ItemStack itemStack) {
        if (itemStack == null) return false;

        var type = itemStack.getType();
        return isPlate(itemStack) ||
                type == Material.GOLD_NUGGET ||
                (type == Material.GOLD_BLOCK && !isClassItem(itemStack)) ||
                type == Material.GOLD_INGOT ||
                (type == Material.IRON_INGOT && !isMagnet(itemStack)) ||
                type == Material.IRON_NUGGET ||
                (type == Material.IRON_BLOCK && !isClassItem(itemStack)) ||
                type == Material.NETHERITE_SCRAP ||
                (type == Material.NETHERITE_INGOT && !isMagnet(itemStack)) ||
                (type == Material.NETHERITE_BLOCK && !isClassItem(itemStack));

    }

    public ItemStack[] dragSplitStackPurities(ItemStack itemStack, int slots) {
        var isPlate = isPlate(itemStack);
        var isRefined = isRefined(itemStack);
        var meta = itemStack.getItemMeta();
        var amount = itemStack.getAmount();
        var splitSize = amount / slots;
        var leftover = amount % slots;
        var purities = getPurity(itemStack);

        var stacks = new ItemStack[slots + 1];
        int h = 0;
        for (int i = 0; i < slots; i++) {
            var stack = new ItemStack(itemStack.getType(), splitSize);
            var lore = new ArrayList<String>();
            if (isPlate)
                lore.add(plateLore);
            var sMeta = stack.getItemMeta();
            sMeta.setLore(lore);
            stack.setItemMeta(sMeta);
            var stackPurities = new float[splitSize];

            if (splitSize >= 0) System.arraycopy(purities, h, stackPurities, 0, splitSize);
            h += splitSize;

            setPurity(stack, stackPurities);
            sMeta = stack.getItemMeta();
            sMeta.setDisplayName(meta.getDisplayName());
            sMeta.setLocalizedName(meta.getLocalizedName());
            sMeta.setLore(generatePurityLure(stackPurities));
            var sLore = sMeta.getLore();
            if (isRefined) {
                sLore.add("");
                sLore.add(refinedLore);
                sMeta.setLore(sLore);
            }
            if (isPlate) {
                sLore.add("");
                sLore.add(plateLore);
                sMeta.setLore(sLore);
                sMeta.setDisplayName(ChatColor.RESET + itemStack.getItemMeta().getDisplayName());
            }
            stack.setItemMeta(sMeta);
            stacks[i] = stack;
        }

        if (leftover == 0) {
            stacks[slots] = air;
        } else {
            var stack = new ItemStack(itemStack.getType(), leftover);
            var lore = new ArrayList<String>();
            var sMeta = stack.getItemMeta();
            if (isPlate) {
                lore.add("");
                lore.add(plateLore);
                sMeta.setLore(lore);
                stack.setItemMeta(sMeta);
            }
            var stackPurities = new float[leftover];
            if (purities.length - h >= 0) System.arraycopy(purities, h, stackPurities, 0, purities.length - h);
            setPurity(stack, stackPurities);
            var stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName(meta.getDisplayName());
            stackMeta.setLocalizedName(meta.getLocalizedName());
            stackMeta.setLore(generatePurityLure(stackPurities));
            var nLore = stackMeta.getLore();
            if (isRefined) {
                nLore.add("");
                nLore.add(refinedLore);
                stackMeta.setLore(nLore);
            }

            if (isPlate(itemStack)) {
                nLore.add("");
                nLore.add(plateLore);
                stackMeta.setLore(nLore);
                stackMeta.setDisplayName(ChatColor.RESET + meta.getDisplayName());
            }

            stack.setItemMeta(stackMeta);
            stacks[slots] = stack;
        }
        return stacks;
    }

    public @Nullable ItemStack getMetalPlate(ItemStack itemStack) {
        String name;
        Material material;
        switch (itemStack.getType()) {
            case IRON_INGOT -> {
                material = Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
                name = "Refined Iron Plate";
            }
            case GOLD_INGOT -> {
                material = Material.LIGHT_WEIGHTED_PRESSURE_PLATE;
                name = "Refined Gold Plate";
            }
            default -> {
                return null;
            }
        }
        ;

        var plate = new ItemStack(material);
        var meta = plate.getItemMeta();
        var lore = new ArrayList<String>();
        lore.add(plateLore);
        meta.setLore(lore);
        plate.setItemMeta(meta);
        setPurity(plate, getPurity(itemStack));
        meta = plate.getItemMeta();
        var pLore = meta.getLore();
        pLore.add("");
        pLore.add(plateLore);
        meta.setLore(pLore);
        meta.setDisplayName(ChatColor.RESET + name);
        meta.setLocalizedName(name);
        plate.setItemMeta(meta);
        return plate;
    }

    public ItemStack[] splitStackPurities(ItemStack itemStack) {
        var meta = itemStack.getItemMeta();
        var container = meta.getPersistentDataContainer();
        var purities = container.get(purityKey, FloatArrayPersistentDataType.instance);

        var newPurities = new float[purities.length / 2];
        var otherPurities = new float[purities.length / 2 + purities.length % 2];

        System.arraycopy(purities, 0, otherPurities, 0, otherPurities.length);
        if (purities.length - otherPurities.length >= 0)
            System.arraycopy(purities, otherPurities.length, newPurities, 0, purities.length - otherPurities.length);


        var newStack = new ItemStack(itemStack.getType(), newPurities.length);
        var newMeta = newStack.getItemMeta();
        newMeta.setLore(generatePurityLure(newPurities));
        var isRefined = isRefined(itemStack);
        var lore = newMeta.getLore();
        if (isRefined) {
            lore.add("");
            lore.add(refinedLore);
            newMeta.setLore(lore);
        }
        if (isPlate(itemStack)) {
            lore.add("");
            lore.add(plateLore);
            newMeta.setLore(lore);
        }
        newMeta.setDisplayName(itemStack.getItemMeta().getDisplayName());
        newMeta.setLocalizedName(itemStack.getItemMeta().getLocalizedName());
        newMeta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, newPurities);
        newStack.setItemMeta(newMeta);

        var otherStack = new ItemStack(itemStack.getType(), otherPurities.length);
        var otherMeta = otherStack.getItemMeta();
        otherMeta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, otherPurities);
        otherMeta.setLore(generatePurityLure(otherPurities));
        lore = otherMeta.getLore();
        if (isRefined) {
            lore.add("");
            lore.add(refinedLore);
            otherMeta.setLore(lore);
        }
        if (isPlate(itemStack)) {
            lore.add("");
            lore.add(plateLore);
            otherMeta.setLore(lore);
            otherMeta.setDisplayName(ChatColor.RESET + itemStack.getItemMeta().getDisplayName());
        }
        otherStack.setItemMeta(otherMeta);

        var stacks = new ItemStack[2];
        stacks[0] = newStack;
        stacks[1] = otherStack;
        return stacks;
    }

    public ItemStack[] dropItemFromPurity(ItemStack stack) {
        var meta = stack.getItemMeta();
        var purities = getPurity(stack);

        var isRefined = isRefined(stack);
        var itemPurity = new float[1];
        itemPurity[0] = purities[0];

        var newPurities = new float[purities.length - 1];
        System.arraycopy(purities, 1, newPurities, 0, newPurities.length);

        ItemStack newStack = new ItemStack(stack.getType(), newPurities.length);
        var newMeta = newStack.getItemMeta();
        newMeta.setLore(generatePurityLure(newPurities));
        var lore = newMeta.getLore();
        if (isRefined) {
            lore.add("");
            lore.add(refinedLore);
            newMeta.setLore(lore);
        }
        if (isPlate(stack)) {
            lore.add("");
            lore.add(plateLore);
            newMeta.setLore(lore);
            newMeta.setDisplayName(ChatColor.RESET + stack.getItemMeta().getDisplayName());
        } else {
            newMeta.setDisplayName(stack.getItemMeta().getDisplayName());
        }
        newMeta.setLocalizedName(stack.getItemMeta().getLocalizedName());
        newMeta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, newPurities);
        newStack.setItemMeta(newMeta);

        var newStack2 = new ItemStack(stack.getType(), 1);
        var newMeta2 = newStack2.getItemMeta();
        newMeta2.setLore(generatePurityLure(itemPurity));
        lore = newMeta2.getLore();
        if (isRefined) {
            lore.add("");
            lore.add(refinedLore);
            newMeta2.setLore(lore);
        }
        if (isPlate(stack)) {
            lore.add("");
            lore.add(plateLore);
            newMeta2.setLore(lore);
            newMeta2.setDisplayName(ChatColor.RESET + stack.getItemMeta().getDisplayName());
        } else {
            newMeta2.setDisplayName(stack.getItemMeta().getDisplayName());
        }
        newMeta2.setLocalizedName(stack.getItemMeta().getLocalizedName());
        newMeta2.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, itemPurity);
        newStack2.setItemMeta(newMeta2);

        var newStacks = new ItemStack[2];
        newStacks[0] = newStack;
        newStacks[1] = newStack2;
        return newStacks;
    }

    public ItemStack[] getCombinedPurity(ItemStack stack1, ItemStack stack2) {
        var purities1 = stack1.getItemMeta().getPersistentDataContainer().get(purityKey, FloatArrayPersistentDataType.instance);
        var purities2 = stack2.getItemMeta().getPersistentDataContainer().get(purityKey, FloatArrayPersistentDataType.instance);
        var isRefined = isRefined(stack1);
        if (purities1.length + purities2.length <= 64) {
            var newPurities = new float[purities1.length + purities2.length];
            System.arraycopy(purities1, 0, newPurities, 0, purities1.length);
            System.arraycopy(purities2, 0, newPurities, purities1.length, purities2.length);
            Arrays.sort(newPurities);

            ItemStack newStack = new ItemStack(stack1.getType(), newPurities.length);
            var meta = newStack.getItemMeta();
            meta.setLore(generatePurityLure(newPurities));
            var lore = meta.getLore();
            if (isRefined) {
                lore.add("");
                lore.add(refinedLore);
                meta.setLore(lore);
            }
            if (isPlate(stack1)) {
                lore.add("");
                lore.add(plateLore);
                meta.setLore(lore);
                meta.setDisplayName(ChatColor.RESET + stack1.getItemMeta().getDisplayName());
            } else {
                meta.setDisplayName(stack1.getItemMeta().getDisplayName());
            }
            meta.setLocalizedName(stack1.getItemMeta().getLocalizedName());
            meta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, newPurities);
            newStack.setItemMeta(meta);

            var stackArray = new ItemStack[1];
            stackArray[0] = newStack;
            return stackArray;

        } else {

            var newPurities1 = new float[64];
            var newPurities2 = new float[purities1.length + purities2.length - 64];

            System.arraycopy(purities1, 0, newPurities1, 0, purities1.length);
            for (int i = purities1.length; i < purities1.length + purities2.length; i++) {
                if (i < 64) newPurities1[i] = purities2[i - purities1.length];
                else newPurities2[i - 64] = purities2[i - purities1.length];
            }

            Arrays.sort(newPurities1);
            Arrays.sort(newPurities2);

            ItemStack newStack = new ItemStack(stack1.getType(), 64);
            var meta = newStack.getItemMeta();
            meta.setLore(generatePurityLure(newPurities1));
            var lore = meta.getLore();
            if (isRefined) {
                lore.add("");
                lore.add(refinedLore);
                meta.setLore(lore);
            }
            if (isPlate(stack1)) {
                lore.add("");
                lore.add(plateLore);
                meta.setLore(lore);
                meta.setDisplayName(ChatColor.RESET + stack1.getItemMeta().getDisplayName());
            } else {
                meta.setDisplayName(stack1.getItemMeta().getDisplayName());
            }
            meta.setLocalizedName(stack1.getItemMeta().getLocalizedName());
            meta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, newPurities1);
            newStack.setItemMeta(meta);

            ItemStack newStack2 = new ItemStack(stack2.getType(), newPurities2.length);
            var meta2 = newStack2.getItemMeta();
            meta2.setLore(generatePurityLure(newPurities2));
            lore = meta2.getLore();
            if (isRefined) {
                lore.add("");
                lore.add(refinedLore);
                meta2.setLore(lore);
            }
            if (isPlate(stack1)) {
                lore.add("");
                lore.add(plateLore);
                meta2.setLore(lore);
                meta2.setDisplayName(ChatColor.RESET + stack1.getItemMeta().getDisplayName());
            } else {
                meta2.setDisplayName(stack2.getItemMeta().getDisplayName());
            }
            meta2.setLocalizedName(stack2.getItemMeta().getLocalizedName());
            meta2.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, newPurities2);
            newStack2.setItemMeta(meta2);

            var stackArray = new ItemStack[2];
            stackArray[0] = newStack;
            stackArray[1] = newStack2;
            return stackArray;

        }
    }

    public void refine(ItemStack item) {
        var meta = item.getItemMeta();
        var lore = meta.getLore();
        lore.add("");
        lore.add(refinedLore);
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    public List<String> generatePurityLure(float[] purities) {
        var lore = new ArrayList<String>();
        lore.add(purityLore);
        StringBuilder loreBuilder = new StringBuilder();

        for (int i = 0; i < purities.length; i++) {
            if (i != 0 && i % 8 == 0) {
                lore.add(loreBuilder.toString());
                loreBuilder = new StringBuilder();
            } else if (i != 0) loreBuilder.append(" ");

            if (purities[i] < 0.000001f) loreBuilder.append(p10);
            else if (purities[i] < 0.00000398f) loreBuilder.append(p9);
            else if (purities[i] < 0.00001585f) loreBuilder.append(p8);
            else if (purities[i] < 0.00006310f) loreBuilder.append(p7);
            else if (purities[i] < 0.00025119f) loreBuilder.append(p6);
            else if (purities[i] < 0.00100000f) loreBuilder.append(p5);
            else if (purities[i] < 0.00398107f) loreBuilder.append(p4);
            else if (purities[i] < 0.01584893f) loreBuilder.append(p3);
            else if (purities[i] < 0.06309573f) loreBuilder.append(p2);
            else if (purities[i] < 0.25118864f) loreBuilder.append(p1);
            else loreBuilder.append(p0);

            if (i == purities.length - 1) lore.add(loreBuilder.toString());
        }
        return lore;
    }

    public ItemStack generatePurity(ItemStack item, double max) {
        var purities = new float[item.getAmount()];
        for (int i = 0; i < purities.length; i++)
            purities[i] = (float) (Math.random() * max);
        Arrays.sort(purities);
        var meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, purities);

        meta.setLore(generatePurityLure(purities));
        item.setItemMeta(meta);
        return item;
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

        layers.add(Material.NETHERITE_INGOT);
        layers.add(Material.CRYING_OBSIDIAN);
        layers.add(Material.OBSIDIAN);
        layers.add(Material.END_STONE);
        layers.add(Material.WITHER_SKELETON_SKULL);
        layers.add(Material.MAGMA_CREAM);
        layers.add(Material.POPPED_CHORUS_FRUIT);
        layers.add(Material.SOUL_SAND);
        layers.add(Material.SHULKER_SHELL);
        layers.add(Material.HONEY_BOTTLE);
        layers.add(Material.SLIME_BALL);
        layers.addAll(planks);
        layers.add(Material.NAUTILUS_SHELL);
        layers.add(Material.SCUTE);
        layers.add(Material.PHANTOM_MEMBRANE);
        layers.add(Material.WHITE_WOOL);
        layers.add(Material.BLACK_WOOL);
        layers.add(Material.BLUE_WOOL);
        layers.add(Material.BROWN_WOOL);
        layers.add(Material.CYAN_WOOL);
        layers.add(Material.GRAY_WOOL);
        layers.add(Material.GREEN_WOOL);
        layers.add(Material.LIGHT_BLUE_WOOL);
        layers.add(Material.LIGHT_GRAY_WOOL);
        layers.add(Material.LIME_WOOL);
        layers.add(Material.MAGENTA_WOOL);
        layers.add(Material.ORANGE_WOOL);
        layers.add(Material.PINK_WOOL);
        layers.add(Material.PURPLE_WOOL);
        layers.add(Material.RED_WOOL);
        layers.add(Material.YELLOW_WOOL);

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

        layerLores.put(Material.NETHERITE_INGOT, NETHERITE_LAYER_LORE);
        layerLores.put(Material.CRYING_OBSIDIAN, CRYING_OBSIDIAN_LAYER_LORE);
        layerLores.put(Material.OBSIDIAN, OBSIDIAN_LAYER_LORE);
        layerLores.put(Material.END_STONE, ENDSTONE_LAYER_LORE);
        layerLores.put(Material.WITHER_SKELETON_SKULL, WITHER_LAYER_LORE);
        layerLores.put(Material.MAGMA_CREAM, MAGMA_CREAM_LAYER_LORE);
        layerLores.put(Material.POPPED_CHORUS_FRUIT, CHORUS_LAYER_LORE);
        layerLores.put(Material.SOUL_SAND, SOUL_LAYER_LORE);
        layerLores.put(Material.SHULKER_SHELL, SHULKER_LAYER_LORE);
        layerLores.put(Material.HONEY_BOTTLE, HONEY_LAYER_LORE);
        layerLores.put(Material.SLIME_BALL, SLIME_LAYER_LORE);
        layerLores.put(Material.OAK_PLANKS, WOOD_LAYER_LORE);
        layerLores.put(Material.NAUTILUS_SHELL, NAUTILUS_LAYER_LORE);
        layerLores.put(Material.SCUTE, SCUTE_LAYER_LORE);
        layerLores.put(Material.PHANTOM_MEMBRANE, MEMBRANE_LAYER_LORE);
        layerLores.put(Material.WHITE_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.BLACK_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.BLUE_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.BROWN_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.CYAN_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.GRAY_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.GREEN_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.LIGHT_BLUE_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.LIGHT_GRAY_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.LIME_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.MAGENTA_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.ORANGE_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.PINK_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.PURPLE_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.RED_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.YELLOW_WOOL, WOOL_LAYER_LORE);
        layerLores.put(Material.ACACIA_PLANKS, WOOD_LAYER_LORE);
        layerLores.put(Material.BIRCH_PLANKS, WOOD_LAYER_LORE);
        layerLores.put(Material.CRIMSON_PLANKS, WOOD_LAYER_LORE);
        layerLores.put(Material.DARK_OAK_PLANKS, WOOD_LAYER_LORE);
        layerLores.put(Material.JUNGLE_PLANKS, WOOD_LAYER_LORE);
        layerLores.put(Material.OAK_PLANKS, WOOD_LAYER_LORE);
        layerLores.put(Material.WARPED_PLANKS, WOOD_LAYER_LORE);
        layerLores.put(Material.SPRUCE_PLANKS, WOOD_LAYER_LORE);


    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Tools.EventListener(), Diplomacy.getInstance());
    }

    private boolean isNew(ItemStack item) {
        var meta = item.getItemMeta();
        return (meta != null && meta.getLore() != null && meta.getLore().contains(DiplomacyRecipes.getInstance().NEW_LORE)) ||
                ((meta != null && !meta.hasLore() && !meta.hasEnchants() &&
                        ((Damageable) meta).getDamage() == 0));
    }

    public boolean isNewLayer(ItemStack item) {
        var meta = item.getItemMeta();
        return (meta != null && meta.getLore() != null && meta.getLore().contains(DiplomacyRecipes.getInstance().NEW_LAYER_LORE));
    }

    private boolean isRefined(ItemStack item) {
        if (item == null) return false;
        var meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null) {
            var lore = meta.getLore();
            return lore.contains(refinedLore);
        }
        return false;
    }

    private boolean isSlurried(ItemStack item) {
        var meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null) {
            var lore = meta.getLore();
            return lore.contains(DiplomacyRecipes.getInstance().SLURRY_IRON_LORE) ||
                    lore.contains(DiplomacyRecipes.getInstance().SLURRY_GOLD_LORE) ||
                    lore.contains(DiplomacyRecipes.getInstance().SLURRY_NETHERITE_LORE);
        }
        return false;
    }

    private boolean isSifter(ItemStack item) {
        var meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null) {
            var lore = meta.getLore().get(0);
            return lore.equals(DiplomacyRecipes.getInstance().SIFTER_LORE);
        }
        return false;
    }

    private boolean isMagnet(ItemStack item) {
        var meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null) {
            var lore = meta.getLore().get(0);
            return lore.equals(DiplomacyRecipes.getInstance().MAGNET_LORE);
        }
        return false;
    }

    private boolean isDust(ItemStack item) {
        var meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null) {
            var lore = meta.getLore().get(0);
            return lore.equals(DiplomacyRecipes.getInstance().GOLD_DUST_LORE) ||
                    lore.equals(DiplomacyRecipes.getInstance().IRON_DUST_LORE) ||
                    lore.equals(DiplomacyRecipes.getInstance().NETHERITE_DUST_LORE);
        }
        return false;
    }

    private boolean hasPurity(ItemStack item) {
        if (isRefined(item)) return true;
        var meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getLore() != null && meta.getLore().get(0).equals(purityLore);
    }

    private void setPurity(ItemStack item, float[] purities) {
        if (!(isMetal(item))) throw new IllegalArgumentException("Item is not a metal.");
        var meta = item.getItemMeta();
        var container = Objects.requireNonNull(meta).getPersistentDataContainer();
        container.set(purityKey, FloatArrayPersistentDataType.instance, purities);
        meta.setLore(generatePurityLure(purities));
        item.setItemMeta(meta);
    }

    private float[] getPurity(ItemStack item) {
        if (!(isMetal(item)) && !isSlurried(item)) {
            throw new IllegalArgumentException("Item is not a metal.");
        }
        if (!hasPurity(item)) generatePurity(item, 1.0);

        var container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
        return Objects.requireNonNull(container.get(purityKey, FloatArrayPersistentDataType.instance));
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
        private void onPrepareAnvil(PrepareAnvilEvent event) {
            var inventory = event.getInventory();
            inventory.setRepairCost(0);
            var item = inventory.getItem(0);
            var item2 = inventory.getItem(1);
            if (item != null && isMetal(item) && isIngot(item) && !isRefined(item) && item2 != null && item2.getType() == Material.BLAZE_POWDER) {
                var nItem = dropItemFromPurity(item);
                var plate = getMetalPlate(nItem[1]);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> inventory.setItem(2, plate), 1L);
            } else if (item != null && isSlurried(item) && item2 != null && item2.getType() == Material.BLAZE_POWDER) {
                ItemStack nItem;
                var meta = item.getItemMeta();
                var lore = meta.getLore();
                if (lore.contains(DiplomacyRecipes.getInstance().SLURRY_IRON_LORE)) {
                    nItem = new ItemStack(Material.IRON_INGOT);
                    var i = lore.indexOf(DiplomacyRecipes.getInstance().SLURRY_IRON_LORE);
                    lore.remove(i);
                    lore.remove(i - 1);
                    meta.setDisplayName(ChatColor.RESET + "Iron Ingot");
                    meta.setLocalizedName("Iron Ingot");
                } else if (lore.contains(DiplomacyRecipes.getInstance().SLURRY_GOLD_LORE)) {
                    nItem = new ItemStack(Material.GOLD_INGOT);
                    var i = lore.indexOf(DiplomacyRecipes.getInstance().SLURRY_GOLD_LORE);
                    lore.remove(i);
                    lore.remove(i - 1);
                    meta.setDisplayName(ChatColor.RESET + "Gold Ingot");
                    meta.setLocalizedName("Gold Ingot");
                } else {
                    nItem = new ItemStack(Material.NETHERITE_INGOT);
                    var i = lore.indexOf(DiplomacyRecipes.getInstance().SLURRY_NETHERITE_LORE);
                    lore.remove(i);
                    lore.remove(i - 1);
                    meta.setDisplayName(ChatColor.RESET + "Netherite Ingot");
                    meta.setLocalizedName("Netherite Ingot");
                }
                nItem.setItemMeta(item.getItemMeta());
                int folds;
                if (lore.contains(foldsLore)) {
                    var i = lore.indexOf(foldsLore);
                    folds = Integer.parseInt(lore.get(i + 1)) + 1;
                    lore.set(i + 1, String.valueOf(folds));
                } else {
                    lore.add("");
                    lore.add(foldsLore);
                    lore.add("1");
                    folds = 1;
                }
                lore.add("");
                lore.add(refinedLore);
                meta.setLore(lore);
                nItem.setItemMeta(meta);
                var purity = getPurity(nItem)[0];
                var unbreaking = (int) (((-5.0 * Math.log(purity)) / (3 * Math.log(10.0))) * (1.0 - 1.0 / folds));
                if (unbreaking > 0) {
                    nItem.addUnsafeEnchantment(Enchantment.DURABILITY, unbreaking);
                }
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> inventory.setItem(2, nItem), 1L);
            } else {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> inventory.setItem(2, air), 1L);
            }
        }

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
            var containsDust = false;
            for (var item : inventory.getMatrix()) {
                if (item != null && isDust(item)) {
                    containsDust = true;
                    break;
                }
            }

            if (!containsDust && isNew(result) && !isMetal(result)) {
                var meta = result.getItemMeta();
                if (meta != null && meta.getLore() != null) {
                    var lore = meta.getLore();
                    lore.remove(0);
                    meta.setLore(lore);
                    result.setItemMeta(meta);
                }
                return;
            }

            // Sifter
            if (isSifter(result)) {
                for (var item : inventory.getMatrix()) {
                    if (item != null && item.getType().equals(Material.IRON_INGOT)) {
                        if (isMagnet(item)) {
                            var meta = result.getItemMeta();
                            meta.setDisplayName(ChatColor.RESET + "Magnetic Sifter");
                            meta.setLocalizedName("Magnetic Sifter");
                            result.setItemMeta(meta);
                            result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 6);
                            result.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                        }
                        return;
                    }
                    if (item != null && item.getType().equals(Material.NETHERITE_INGOT)) {
                        if (!isMagnet(item)) {
                            event.getInventory().setResult(air);
                            return;
                        }
                    }
                    if (item != null && item.getType().equals(Material.REDSTONE) && isDust(item)) {
                        event.getInventory().setResult(air);
                        return;
                    }
                    if (item != null && item.getType().equals(Material.CHAINMAIL_HELMET)) {
                        if (result != null && result.getType() == Material.CHAINMAIL_HELMET) {
                            var meta = result.getItemMeta();
                            ((Damageable) meta).setDamage(((Damageable) item.getItemMeta()).getDamage());
                            result.setItemMeta(meta);
                        }
                    }
                }
            }

            // Armor layers
            if (isNewLayer(result)) {
                Material layerType = Material.AIR;
                ItemStack armor = air;
                for (var item : inventory.getMatrix()) {
                    if (item == null || item.getType() == Material.AIR) continue;
                    var type = item.getType();
                    if (helmets.contains(type) || chestplates.contains(type) || leggings.contains(type) || boots.contains(type)) {
                        if (isSifter(item)) {
                            inventory.setResult(air);
                            return;
                        }
                        armor = item;
                    } else {
                        if (layerType == Material.AIR) layerType = type;
                        else if (layerType != type && !(wool.contains(item.getType()) && wool.contains(layerType) && !(planks.contains(item.getType()) && planks.contains(layerType)))) {
                            event.getInventory().setResult(air);
                            return;
                        }
                    }
                }
                if (layerType == Material.HONEY_BOTTLE) {
                    var count = 0;
                    for (var item : inventory.getMatrix()) {
                        if (item.getType() == Material.HONEY_BOTTLE) count++;
                    }
                    var player = event.getView().getPlayer();
                    if (player.getInventory().firstEmpty() == -1)
                        player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.GLASS_BOTTLE, count));
                    else
                        player.getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE, count));
                }
                if (armor == air) {
                    event.getInventory().setResult(air);
                    return;
                }
                var layers = getLayers(armor);
                if (layers.size() == 4) {
                    event.getInventory().setResult(air);
                    return;
                }
                var nLayer = layerLores.get(layerType);
                if (layers.contains(nLayer)) {
                    event.getInventory().setResult(air);
                    return;
                }
                result.setType(armor.getType());
                result.setItemMeta(armor.getItemMeta());
                result.setData(armor.getData());

                var fire = 0;
                var proj = 0;
                var blast = 0;
                var melee = 0;

                var meta = result.getItemMeta();
                if (meta.hasEnchant(Enchantment.PROTECTION_FIRE)) {
                    fire = meta.getEnchantLevel(Enchantment.PROTECTION_FIRE);
                    meta.removeEnchant(Enchantment.PROTECTION_FIRE);
                }
                if (meta.hasEnchant(Enchantment.PROTECTION_PROJECTILE)) {
                    proj = meta.getEnchantLevel(Enchantment.PROTECTION_PROJECTILE);
                    meta.removeEnchant(Enchantment.PROTECTION_PROJECTILE);
                }
                if (meta.hasEnchant(Enchantment.PROTECTION_EXPLOSIONS)) {
                    blast = meta.getEnchantLevel(Enchantment.PROTECTION_EXPLOSIONS);
                    meta.removeEnchant(Enchantment.PROTECTION_EXPLOSIONS);
                }
                if (meta.hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                    melee = meta.getEnchantLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    meta.removeEnchant(Enchantment.PROTECTION_ENVIRONMENTAL);
                }

                if (nLayer.equals(NETHERITE_LAYER_LORE)) blast += 4;
                else if (nLayer.equals(CRYING_OBSIDIAN_LAYER_LORE)) blast += 3;
                else if (nLayer.equals(OBSIDIAN_LAYER_LORE)) blast += 2;
                else if (nLayer.equals(ENDSTONE_LAYER_LORE)) blast += 1;
                else if (nLayer.equals(WITHER_LAYER_LORE)) fire += 4;
                else if (nLayer.equals(MAGMA_CREAM_LAYER_LORE)) fire += 3;
                else if (nLayer.equals(CHORUS_LAYER_LORE)) fire += 2;
                else if (nLayer.equals(SOUL_LAYER_LORE)) fire += 1;
                else if (nLayer.equals(SHULKER_LAYER_LORE)) proj += 4;
                else if (nLayer.equals(HONEY_LAYER_LORE)) proj += 3;
                else if (nLayer.equals(SLIME_LAYER_LORE)) proj += 2;
                else if (nLayer.equals(WOOD_LAYER_LORE)) proj += 1;
                else if (nLayer.equals(NAUTILUS_LAYER_LORE)) melee += 4;
                else if (nLayer.equals(SCUTE_LAYER_LORE)) melee += 3;
                else if (nLayer.equals(MEMBRANE_LAYER_LORE)) melee += 2;
                else if (nLayer.equals(WOOL_LAYER_LORE)) melee += 1;

                var rMeta = result.getItemMeta();
                var lore = rMeta.getLore();
                if (lore == null) {
                    lore = new ArrayList<>();
                    lore.add("");
                }
                lore.add(nLayer);
                rMeta.setLore(lore);
                result.setItemMeta(rMeta);

                if (fire > 0) result.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, fire);
                if (proj > 0) result.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, proj);
                if (blast > 0) result.addUnsafeEnchantment(Enchantment.PROTECTION_EXPLOSIONS, blast);
                if (melee > 0) result.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, melee);

                inventory.setResult(result);
            }


            // Metal / purities
            if (isMetal(result)) {
                var isRefined = false;
                var total = 0.0f;
                var count = 0;

                for (var item : inventory.getMatrix()) {
                    if (isMetal(item)) {
                        count++;
                        total += getPurity(item)[0];
                    }
                    if (!isRefined && isRefined(item)) {
                        isRefined = true;
                    }
                }

                var purity = total / count;

                var purities = new float[result.getAmount()];
                for (int i = 0; i < purities.length; i++) {
                    purities[i] = purity;
                }
                setPurity(result, purities);
                if (isRefined) refine(result);
                event.getInventory().setResult(result);
            }

            if (isSlurried(result)) {
                for (var content : inventory.getMatrix()) {
                    if (content.getType() == Material.POTION) {
                        var meta = (PotionMeta) content.getItemMeta();
                        if (meta.getBasePotionData().getType() != PotionType.MUNDANE) {
                            inventory.setResult(air);
                            return;
                        }
                    } else if (content.getType() == Material.SUGAR || content.getType() == Material.GLOWSTONE_DUST || content.getType() == Material.REDSTONE) {
                        if (!isDust(content)) {
                            inventory.setResult(air);
                            return;
                        }
                    } else if (isMetal(content)) {
                        if (content.getAmount() == 1)
                            result.setItemMeta(content.getItemMeta());
                        else {
                            var item = dropItemFromPurity(content)[1];
                            result.setItemMeta(item.getItemMeta());
                        }
                        var meta = result.getItemMeta();
                        var lore = meta.getLore();
                        lore.add("");
                        String name;
                        switch (content.getType()) {
                            default -> {
                                name = "Slurried Iron Ingot";
                                lore.add(DiplomacyRecipes.getInstance().SLURRY_IRON_LORE);
                            }
                            case GOLD_INGOT -> {
                                name = "Slurried Gold Ingot";
                                lore.add(DiplomacyRecipes.getInstance().SLURRY_GOLD_LORE);
                            }
                            case NETHERITE_INGOT -> {
                                name = "Slurried Netherite Ingot";
                                lore.add(DiplomacyRecipes.getInstance().SLURRY_NETHERITE_LORE);
                            }
                        }
                        if (lore.contains(refinedLore)) {
                            var i = lore.indexOf(refinedLore);
                            lore.remove(i);
                            lore.remove(i - 1);
                        }
                        meta.setLore(lore);
                        meta.setDisplayName(ChatColor.RESET + name);
                        meta.setLocalizedName(name);
                        if (meta.hasEnchants()) {
                            for (var enchant : meta.getEnchants().keySet()) {
                                meta.removeEnchant(enchant);
                            }
                        }
                        result.setItemMeta(meta);

                    }
                }
                inventory.setResult(result);
            }

            // Cancelling crafts for special items
            for (var content : inventory.getMatrix()) {
                if (content == null) continue;
                if ((isMagnet(content) && !isSifter(result))
                        || isSlurried(content)
                        || (isDust(content) && !isSlurried(result))) {
                    inventory.setResult(air);
                    return;
                }
            }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        private void onBlockBreak(BlockBreakEvent event) {
            var item = event.getPlayer().getInventory().getItemInMainHand();

            var location = event.getBlock().getLocation();
            location.setY(location.getY() + 1.0);

            // Nether gold ore
            if (event.getBlock().getType() == Material.NETHER_GOLD_ORE) {
                var amount = (int) (Math.random() * 5) + 2;
                var drop = new ItemStack(Material.GOLD_NUGGET, amount);
                generatePurity(drop, 0.5);
                event.setDropItems(false);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                        event.getBlock().getWorld().dropItem(location, drop), 1L);
            }

            // Gravel
            if (event.getBlock().getType() == Material.GRAVEL) {
                event.setDropItems(false);
                if (item.getType() == Material.WOODEN_SHOVEL || item.getType() == Material.STONE_SHOVEL || item.getType() == Material.IRON_SHOVEL
                        || item.getType() == Material.GOLDEN_SHOVEL || item.getType() == Material.DIAMOND_SHOVEL || item.getType() == Material.NETHERITE_SHOVEL) {
                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                            event.getBlock().getWorld().dropItem(location, new ItemStack(Material.GRAVEL)), 1L);
                } else {
                    if (isSifter(item)) {
                        event.getPlayer().getWorld().playSound(event.getBlock().getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1, (float) (Math.random() * 0.5));
                        var meta = item.getItemMeta();
                        var damage = ((Damageable) meta).getDamage();
                        if (Math.random() < 1.0 / (1.0 + meta.getEnchantLevel(Enchantment.DURABILITY))) {
                            var player = event.getPlayer();
                            if (damage == 164) {
                                player.getInventory().setItemInMainHand(air);
                                player.playEffect(EntityEffect.BREAK_EQUIPMENT_MAIN_HAND);
                                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                            } else {
                                ((Damageable) meta).setDamage(damage + 1);
                                item.setItemMeta(meta);
                            }
                        }
                    }
                    var level = 0;
                    if (isSifter(item)) level = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

                    // flints
                    int flints;
                    var r = Math.random();
                    if (r < 0.3875) flints = 0;
                    else if (r < 0.6125) flints = 1;
                    else if (r < 0.8043) flints = 2;
                    else if (r < 0.9234) flints = 3;
                    else if (r < 0.9772) flints = 4;
                    else if (r < 0.9949) flints = 5;
                    else if (r < 0.9991) flints = 6;
                    else if (r < 0.9999) flints = 7;
                    else flints = 8;

                    if (flints > 0) {
                        var chance = 1.0;
                        var drops = 0;
                        if (level == 0) chance = 0.012;
                        else if (level == 1) chance = 0.053;
                        else if (level == 2) chance = 0.231;

                        for (int i = 0; i < flints; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }
                        if (drops > 0) {
                            int finalDrops = drops;
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, new ItemStack(Material.FLINT, finalDrops)), 1L);
                        }

                    }
                    // Coal
                    if (level < 2) return;

                    int coal;
                    r = Math.random();
                    if (r < 0.4867) coal = 0;
                    else if (r < 0.7367) coal = 1;
                    else if (r < 0.9032) coal = 2;
                    else if (r < 0.9754) coal = 3;
                    else if (r < 0.9958) coal = 4;
                    else if (r < 0.9995) coal = 5;
                    else coal = 6;

                    if (coal > 0) {
                        double chance;
                        if (level == 2) chance = 0.012;
                        else chance = 0.053;

                        var drops = 0;

                        for (int i = 0; i < coal; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            var coalItem = new ItemStack(Material.COAL, drops);
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, coalItem), 1L);
                        }
                    }

                    // Gold dust
                    if (level < 4) return;

                    int goldDust;
                    r = Math.random();
                    if (r < 0.4867) goldDust = 0;
                    else if (r < 0.7367) goldDust = 1;
                    else if (r < 0.9032) goldDust = 2;
                    else if (r < 0.9754) goldDust = 3;
                    else if (r < 0.9958) goldDust = 4;
                    else if (r < 0.9995) goldDust = 5;
                    else goldDust = 6;

                    if (goldDust > 0) {
                        double chance;
                        if (level == 4) chance = 0.012;
                        else if (level == 5 || level == 6) chance = 0.053;
                        else if (level == 7) chance = 0.231;
                        else chance = 0.0;

                        var drops = 0;

                        for (int i = 0; i < goldDust; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            var goldDustItem = new ItemStack(Material.GLOWSTONE_DUST);
                            var meta = goldDustItem.getItemMeta();
                            meta.setDisplayName(ChatColor.RESET + "Gold Dust");
                            meta.setLocalizedName("Gold Dust");
                            var lore = new ArrayList<String>();
                            lore.add(DiplomacyRecipes.getInstance().GOLD_DUST_LORE);
                            meta.setLore(lore);
                            goldDustItem.setItemMeta(meta);
                            goldDustItem.setAmount(drops);
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, goldDustItem), 1L);
                        }
                    }

                    // Iron dust
                    if (level < 5) return;

                    int ironDust;
                    r = Math.random();
                    if (r < 0.4681) ironDust = 0;
                    else if (r < 0.6255) ironDust = 1;
                    else if (r < 0.7642) ironDust = 2;
                    else if (r < 0.8686) ironDust = 3;
                    else if (r < 0.9357) ironDust = 4;
                    else if (r < 0.9726) ironDust = 5;
                    else if (r < 0.9898) ironDust = 6;
                    else if (r < 0.9967) ironDust = 7;
                    else if (r < 0.9990) ironDust = 8;
                    else if (r < 0.9997) ironDust = 9;
                    else ironDust = 10;

                    if (ironDust > 0) {
                        double chance;
                        if (level == 5) chance = 0.012;
                        else if (level == 6) chance = 0.053;
                        else if (level == 7) chance = 0.231;
                        else chance = 0.0;

                        var drops = 0;

                        for (int i = 0; i < ironDust; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            var ironDustItem = new ItemStack(Material.SUGAR);
                            var meta = ironDustItem.getItemMeta();
                            meta.setDisplayName(ChatColor.RESET + "Iron Dust");
                            meta.setLocalizedName("Iron Dust");
                            var lore = new ArrayList<String>();
                            lore.add(DiplomacyRecipes.getInstance().IRON_DUST_LORE);
                            meta.setLore(lore);
                            ironDustItem.setItemMeta(meta);
                            ironDustItem.setAmount(drops);
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, ironDustItem), 1L);
                        }
                    }

                    // Redstone dust
                    int redstoneDust;
                    r = Math.random();
                    if (r < 0.5478) redstoneDust = 0;
                    else if (r < 0.8212) redstoneDust = 1;
                    else if (r < 0.9573) redstoneDust = 2;
                    else if (r < 0.9941) redstoneDust = 3;
                    else if (r < 0.9995) redstoneDust = 4;
                    else redstoneDust = 5;

                    if (redstoneDust > 0) {
                        double chance;
                        if (level == 5) chance = 0.012;
                        else if (level == 6) chance = 0.053;
                        else chance = 0.231;

                        var drops = 0;

                        for (int i = 0; i < redstoneDust; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            int finalDrops = drops;
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, new ItemStack(Material.REDSTONE, finalDrops)), 1L);
                        }
                    }
                }
            } else if (event.getBlock().getType() == Material.SOUL_SAND) {
                event.setDropItems(false);
                if (item.getType() == Material.WOODEN_SHOVEL || item.getType() == Material.STONE_SHOVEL || item.getType() == Material.IRON_SHOVEL
                        || item.getType() == Material.GOLDEN_SHOVEL || item.getType() == Material.DIAMOND_SHOVEL || item.getType() == Material.NETHERITE_SHOVEL) {
                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                            event.getBlock().getWorld().dropItem(location, new ItemStack(Material.SOUL_SAND)), 1L);
                } else {
                    if (isSifter(item)) {
                        event.getPlayer().getWorld().playSound(event.getBlock().getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1, (float) (Math.random() * 0.5));
                        var meta = item.getItemMeta();
                        var damage = ((Damageable) meta).getDamage();
                        if (Math.random() < 1.0 / (1.0 + meta.getEnchantLevel(Enchantment.DURABILITY))) {
                            var player = event.getPlayer();
                            if (damage == 164) {
                                player.getInventory().setItemInMainHand(air);
                                player.playEffect(EntityEffect.BREAK_EQUIPMENT_MAIN_HAND);
                                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                            } else {
                                ((Damageable) meta).setDamage(damage + 1);
                                item.setItemMeta(meta);
                            }
                        }
                    }
                    var level = 0;
                    if (isSifter(item)) level = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

                    // Soul Soil
                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                            event.getBlock().getWorld().dropItem(location, new ItemStack(Material.SOUL_SOIL)), 1L);


                    var r = Math.random();
                    if (r < 0.05)
                        event.getPlayer().playSound(event.getBlock().getLocation(), Sound.AMBIENT_SOUL_SAND_VALLEY_MOOD, 1, (float) Math.random());

                    // Status effect for messing with the dead
                    int l;
                    int t;
                    r = Math.random();
                    if (r < 0.9416) l = -1;
                    else if (r < 0.9805) l = 0;
                    else if (r < 0.9956) l = 1;
                    else if (r < 0.9994) l = 2;
                    else if (r < 0.9999) l = 3;
                    else l = 4;

                    r = Math.random();
                    if (r < 0.9416) t = 1 + (int) (Math.random() * 20 * 10);
                    else if (r < 0.9805) t = 1 + (int) (Math.random() * 20 * 60);
                    else if (r < 0.9956) t = 1 + (int) (Math.random() * 20 * 60 * 5);
                    else if (r < 0.9994) t = 1 + (int) (Math.random() * 20 * 60 * 30);
                    else if (r < 0.9999) t = 1 + (int) (Math.random() * 20 * 60 * 60 * 6);
                    else t = 1 + (int) (Math.random() * 20 * 60 * 60 * 24 * 2);


                    r = (Math.random() * 6);
                    PotionEffectType type = switch (((int) r)) {
                        case 0 -> SLOW;
                        case 1 -> SLOW_DIGGING;
                        case 2 -> WEAKNESS;
                        case 3 -> CONFUSION;
                        case 4 -> BLINDNESS;
                        case 5 -> HUNGER;
                        default -> null;
                    };
                    if (l > -1 && type != null)
                        event.getPlayer().addPotionEffect(new PotionEffect(type, t, l, true, false, false));

                    // Charcoal
                    if (level < 1) return;

                    int coal;
                    r = Math.random();
                    if (r < 0.4867) coal = 0;
                    else if (r < 0.7367) coal = 1;
                    else if (r < 0.9032) coal = 2;
                    else if (r < 0.9754) coal = 3;
                    else if (r < 0.9958) coal = 4;
                    else if (r < 0.9995) coal = 5;
                    else coal = 6;

                    if (coal > 0) {
                        double chance;
                        if (level == 1) chance = 0.012;
                        else if (level == 2) chance = 0.053;
                        else chance = 0.231;

                        var drops = 0;

                        for (int i = 0; i < coal; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            var coalItem = new ItemStack(Material.CHARCOAL, drops);
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, coalItem), 1L);
                        }
                    }

                    // Gold nugget
                    if (level < 3) return;

                    int goldNugget;
                    r = Math.random();
                    if (r < 0.4867) goldNugget = 0;
                    else if (r < 0.7367) goldNugget = 1;
                    else if (r < 0.9032) goldNugget = 2;
                    else if (r < 0.9754) goldNugget = 3;
                    else if (r < 0.9958) goldNugget = 4;
                    else if (r < 0.9995) goldNugget = 5;
                    else goldNugget = 6;

                    if (goldNugget > 0) {
                        double chance;
                        if (level == 3) chance = 0.012;
                        else chance = 0.053;

                        var drops = 0;

                        for (int i = 0; i < goldNugget; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            int finalDrops = drops;
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, generatePurity(new ItemStack(Material.GOLD_NUGGET, finalDrops), 0.436)), 1L);
                        }
                    }

                    // Gold dust
                    if (level < 4) return;

                    int goldDust;
                    r = Math.random();
                    if (r < 0.4867) goldDust = 0;
                    else if (r < 0.7367) goldDust = 1;
                    else if (r < 0.9032) goldDust = 2;
                    else if (r < 0.9754) goldDust = 3;
                    else if (r < 0.9958) goldDust = 4;
                    else if (r < 0.9995) goldDust = 5;
                    else goldDust = 6;

                    if (goldDust > 0) {
                        double chance;
                        if (level == 4) chance = 0.012;
                        else if (level == 5 || level == 6) chance = 0.053;
                        else if (level == 7) chance = 0.231;
                        else chance = 0.0;

                        var drops = 0;

                        for (int i = 0; i < goldDust; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            var goldDustItem = new ItemStack(Material.GLOWSTONE_DUST);
                            var meta = goldDustItem.getItemMeta();
                            meta.setDisplayName(ChatColor.RESET + "Gold Dust");
                            meta.setLocalizedName("Gold Dust");
                            var lore = new ArrayList<String>();
                            lore.add(DiplomacyRecipes.getInstance().GOLD_DUST_LORE);
                            meta.setLore(lore);
                            goldDustItem.setItemMeta(meta);
                            goldDustItem.setAmount(drops);
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, goldDustItem), 1L);
                        }
                    }

                    // Glowstone dust
                    if (level < 5) return;

                    int glowstoneDust;
                    r = Math.random();
                    if (r < 0.4681) glowstoneDust = 0;
                    else if (r < 0.6255) glowstoneDust = 1;
                    else if (r < 0.7642) glowstoneDust = 2;
                    else if (r < 0.8686) glowstoneDust = 3;
                    else if (r < 0.9357) glowstoneDust = 4;
                    else if (r < 0.9726) glowstoneDust = 5;
                    else if (r < 0.9898) glowstoneDust = 6;
                    else if (r < 0.9967) glowstoneDust = 7;
                    else if (r < 0.9990) glowstoneDust = 8;
                    else if (r < 0.9997) glowstoneDust = 9;
                    else glowstoneDust = 10;

                    if (glowstoneDust > 0) {
                        double chance;
                        if (level == 5) chance = 0.012;
                        else if (level == 6) chance = 0.053;
                        else if (level == 7) chance = 0.231;
                        else chance = 0.0;

                        var drops = 0;

                        for (int i = 0; i < glowstoneDust; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            int finalDrops = drops;
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, new ItemStack(Material.GLOWSTONE_DUST, finalDrops)), 1L);
                        }
                    }

                    //  Gunpowder
                    if (level < 6) return;

                    int gunPowder;
                    r = Math.random();
                    if (r < 0.4681) gunPowder = 0;
                    else if (r < 0.6255) gunPowder = 1;
                    else if (r < 0.7642) gunPowder = 2;
                    else if (r < 0.8686) gunPowder = 3;
                    else if (r < 0.9357) gunPowder = 4;
                    else if (r < 0.9726) gunPowder = 5;
                    else if (r < 0.9898) gunPowder = 6;
                    else if (r < 0.9967) gunPowder = 7;
                    else if (r < 0.9990) gunPowder = 8;
                    else if (r < 0.9997) gunPowder = 9;
                    else gunPowder = 10;

                    if (gunPowder > 0) {
                        double chance;
                        if (level == 6) chance = 0.012;
                        else chance = 0.123;

                        var drops = 0;

                        for (int i = 0; i < gunPowder; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            int finalDrops = drops;
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, new ItemStack(Material.GUNPOWDER, finalDrops)), 1L);
                        }
                    }

                    // Ancient dust
                    int ancientDust;
                    r = Math.random();
                    if (r < 0.5478) ancientDust = 0;
                    else if (r < 0.8212) ancientDust = 1;
                    else if (r < 0.9573) ancientDust = 2;
                    else if (r < 0.9941) ancientDust = 3;
                    else if (r < 0.9995) ancientDust = 4;
                    else ancientDust = 5;

                    if (ancientDust > 0) {
                        double chance;
                        if (level == 6) chance = 0.012;
                        else chance = 0.053;

                        var drops = 0;

                        for (int i = 0; i < ancientDust; i++) {
                            if (Math.random() < chance) {
                                drops++;
                            }
                        }

                        if (drops > 0) {
                            var ancientDustItem = new ItemStack(Material.REDSTONE);
                            var meta = ancientDustItem.getItemMeta();
                            meta.setDisplayName(ChatColor.RESET + "Ancient Dust");
                            meta.setLocalizedName("Ancient Dust");
                            var lore = new ArrayList<String>();
                            lore.add(DiplomacyRecipes.getInstance().NETHERITE_DUST_LORE);
                            meta.setLore(lore);
                            ancientDustItem.setItemMeta(meta);
                            ancientDustItem.setAmount(drops);
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () ->
                                    event.getBlock().getWorld().dropItem(location, ancientDustItem), 1L);
                        }
                    }
                }
            }
        }

        @EventHandler
        private void onPlayerDropItem(PlayerDropItemEvent event) {
            var item = event.getItemDrop();
            var inventory = event.getPlayer().getInventory();
            var currentItem = inventory.getItemInMainHand();
            if (currentItem != null && isMetal(currentItem) && item.getItemStack().getAmount() == 1) {
                var dropItem = dropItemFromPurity(currentItem);
                inventory.setItemInMainHand(dropItem[0]);
                item.setItemStack(dropItem[1]);
            }
        }

        @EventHandler
        private void onEntityDamage(EntityDamageEvent event) {
            var cause = event.getCause();
            var entity = event.getEntity();
            var damage = event.getDamage();
            var reduce = 0.0;

            if (!(entity instanceof LivingEntity)) return;
            var equip = ((LivingEntity) entity).getEquipment();
            var helmet = air;
            var chest = air;
            var legs = air;
            var boots = air;

            if (equip != null) {
                helmet = equip.getHelmet();
                chest = equip.getChestplate();
                legs = equip.getLeggings();
                boots = equip.getBoots();
            }

            if (helmet == null) helmet = air;
            if (chest == null) chest = air;
            if (legs == null) legs = air;
            if (boots == null) boots = air;

            switch (helmet.getType()) {
                case LEATHER_HELMET -> reduce += 0.02;
                case GOLDEN_HELMET, CHAINMAIL_HELMET -> reduce += 0.03;
                case IRON_HELMET -> reduce += 0.04;
                case TURTLE_HELMET, DIAMOND_HELMET -> reduce += 0.05;
                case NETHERITE_HELMET -> reduce += 0.06;
            }

            switch (chest.getType()) {
                case LEATHER_CHESTPLATE -> reduce += 0.06;
                case GOLDEN_CHESTPLATE, CHAINMAIL_CHESTPLATE -> reduce += 0.10;
                case IRON_CHESTPLATE -> reduce += 0.12;
                case DIAMOND_CHESTPLATE -> reduce += 0.15;
                case NETHERITE_CHESTPLATE -> reduce += 0.16;
            }

            switch (legs.getType()) {
                case LEATHER_LEGGINGS -> reduce += 0.04;
                case GOLDEN_LEGGINGS -> reduce += 0.06;
                case CHAINMAIL_LEGGINGS -> reduce += 0.08;
                case IRON_LEGGINGS -> reduce += 0.10;
                case DIAMOND_LEGGINGS -> reduce += 0.11;
                case NETHERITE_LEGGINGS -> reduce += 0.12;
            }

            switch (boots.getType()) {
                case LEATHER_BOOTS -> reduce += 0.01;
                case GOLDEN_BOOTS -> reduce += 0.02;
                case CHAINMAIL_BOOTS -> reduce += 0.03;
                case IRON_BOOTS -> reduce += 0.04;
                case DIAMOND_BOOTS -> reduce += 0.05;
                case NETHERITE_BOOTS -> reduce += 0.06;
            }

            reduce /= 4.0;
            reduce *= 6.0;

            switch (cause) {
                case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> {
                    damage *= 4;
                    if (helmet.getItemMeta() != null && helmet.getItemMeta().hasEnchant(Enchantment.PROTECTION_EXPLOSIONS))
                        reduce += 0.01 * helmet.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                    if (chest.getItemMeta() != null && chest.getItemMeta().hasEnchant(Enchantment.PROTECTION_EXPLOSIONS))
                        reduce += 0.01 * chest.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_EXPLOSIONS))
                        reduce += 0.01 * legs.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_EXPLOSIONS))
                        reduce += 0.01 * legs.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                    ((LivingEntity) entity).setHealth(Math.max(0.1, ((LivingEntity) entity).getHealth() - damage * (1.0 - reduce)));
                    event.setDamage(0.1);
                    if (reduce == 1) event.setCancelled(true);
                }
                case CONTACT, ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> {
                    damage *= 1.5;
                    if (helmet.getItemMeta() != null && helmet.getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
                        reduce += 0.01 * helmet.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    if (chest.getItemMeta() != null && chest.getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
                        reduce += 0.01 * chest.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
                        reduce += 0.01 * legs.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
                        reduce += 0.01 * legs.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    if (reduce == 1) event.setCancelled(true);
                    else {
                        ((LivingEntity) entity).setHealth(Math.max(0.1, ((LivingEntity) entity).getHealth() - damage * (1.0 - reduce)));
                        event.setDamage(0.1);
                    }

                }
                case DRAGON_BREATH, FIRE, FIRE_TICK, HOT_FLOOR, LAVA -> {
                    damage *= 2;
                    if (helmet.getItemMeta() != null && helmet.getItemMeta().hasEnchant(Enchantment.PROTECTION_FIRE))
                        reduce += 0.01 * helmet.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
                    if (chest.getItemMeta() != null && chest.getItemMeta().hasEnchant(Enchantment.PROTECTION_FIRE))
                        reduce += 0.01 * chest.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_FIRE))
                        reduce += 0.01 * legs.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_FIRE))
                        reduce += 0.01 * legs.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
                    if (reduce == 1) event.setCancelled(true);
                    else {
                        ((LivingEntity) entity).setHealth(Math.max(0.1, ((LivingEntity) entity).getHealth() - damage * (1.0 - reduce)));
                        event.setDamage(0.1);
                    }
                }
                case PROJECTILE -> {
                    damage *= 2;
                    if (helmet.getItemMeta() != null && helmet.getItemMeta().hasEnchant(Enchantment.PROTECTION_PROJECTILE))
                        reduce += 0.01 * helmet.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                    if (chest.getItemMeta() != null && chest.getItemMeta().hasEnchant(Enchantment.PROTECTION_PROJECTILE))
                        reduce += 0.01 * chest.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_PROJECTILE))
                        reduce += 0.01 * legs.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_PROJECTILE))
                        reduce += 0.01 * legs.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                    if (reduce == 1) {
                        event.setCancelled(true);
                    }
                    else {
                        ((LivingEntity) entity).setHealth(Math.max(0.1, ((LivingEntity) entity).getHealth() - damage * (1.0 - reduce)));
                        event.setDamage(0.1);
                    }
                }
            }
        }

        @EventHandler
        private void onItemUse(PlayerInteractEvent event) {
            var item = event.getItem();
            if (item == null) return;

            // Ancient Dust
            if (item.getType() == Material.REDSTONE && isDust(item)) {
                event.setCancelled(true);
                return;
            }

            // Metal Plates
            if (isPlate(item) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                var block = event.getClickedBlock();
                if (block != null && block.getType() == Material.CAULDRON) {
                    var cauldron = (Levelled) block.getBlockData();
                    if (cauldron.getLevel() != 0) {
                        cauldron.setLevel(cauldron.getLevel() - 1);
                        block.setBlockData(cauldron);
                        var hand = event.getHand();
                        Material material;
                        switch (item.getType()) {
                            case HEAVY_WEIGHTED_PRESSURE_PLATE -> material = Material.IRON_NUGGET;
                            case LIGHT_WEIGHTED_PRESSURE_PLATE -> material = Material.GOLD_NUGGET;
                            default -> {
                                event.setCancelled(true);
                                return;
                            }
                        }

                        var nuggets = new ItemStack(material, 9);
                        generatePurity(nuggets, getPurity(item)[0]);
                        var meta = nuggets.getItemMeta();
                        var lore = meta.getLore();
                        lore.add("");
                        lore.add(refinedLore);
                        meta.setLore(lore);
                        nuggets.setItemMeta(meta);

                        if (item.getAmount() > 1)
                            event.getPlayer().getEquipment().setItem(hand, dropItemFromPurity(item)[0]);
                        else
                            event.getPlayer().getEquipment().setItem(hand, air);

                        block.getWorld().dropItem(block.getLocation(), nuggets);
                        block.getWorld().playSound(block.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                        block.getWorld().playSound(block.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 1, 1);
                    }
                }
                event.setCancelled(true);
                return;
            }

            // Lodestone sifter & not equipping
            if (isSifter(item)) {
                var meta = item.getItemMeta();
                if (meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) == 4) {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.getClickedBlock().getType().equals(Material.LODESTONE)) {
                        meta.setDisplayName(ChatColor.RESET + "Lodestone Sifter");
                        meta.setLocalizedName("Lodestone Sifter");
                        item.setItemMeta(meta);
                        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);
                        item.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
                        event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ITEM_LODESTONE_COMPASS_LOCK, 1, 1);
                    }
                }
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
                    event.setCancelled(true);
                return;
            }

            // magnet
            if (item.getAmount() == 1 && hasPurity(item)) {
                if (getPurity(item)[0] <= 0.00006310f) {
                    if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.getClickedBlock().getType().equals(Material.LODESTONE)) {
                        if (item.getType().equals(Material.IRON_INGOT) || item.getType().equals(Material.NETHERITE_INGOT)) {
                            var meta = item.getItemMeta();
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                            if (item.getType().equals(Material.IRON_INGOT)) {
                                meta.setDisplayName(ChatColor.RESET + "Iron Magnet");
                                meta.setLocalizedName("Iron Magnet");
                            } else {
                                meta.setDisplayName(ChatColor.RESET + "Netherite Magnet");
                                meta.setLocalizedName("Netherite Magnet");
                            }

                            var lore = new ArrayList<String>();
                            lore.add(DiplomacyRecipes.getInstance().MAGNET_LORE);
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            item.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 1);
                            event.getPlayer().getWorld().playSound(event.getPlayer().getLocation(), Sound.ITEM_LODESTONE_COMPASS_LOCK, 1, 1);
                        }
                    }
                }
            }

            // generate purity
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (isMetal(item)) {
                    if (!hasPurity(item)) {
                        generatePurity(item, 1.0);
                    }
                }
            }
        }

        @EventHandler
        private void onInventoryDrag(InventoryDragEvent event) {
            var temp = event.getOldCursor();
            if (hasFolds(temp)) return;

            var view = event.getView();
            for (var slot : event.getRawSlots()) {
                var inv = view.getInventory(slot);
                if (inv != null && inv.getType() == InventoryType.BREWING) {
                    if (isDust(temp)) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            var type = event.getType();
            if (type == DragType.EVEN) {
                var slots = event.getRawSlots();
                var items = event.getOldCursor();
                if (!isMetal(items)) return;
                var player = event.getWhoClicked();
                event.setCancelled(true);
                var newStacks = dragSplitStackPurities(items, slots.size());
                var cursor = newStacks[slots.size()];
                int i = 0;
                for (var num : slots) {
                    var slotItem = view.getItem(num);

                    if (slotItem != null && slotItem.getType() != Material.AIR) {
                        var combined = getCombinedPurity(slotItem, newStacks[i]);
                        view.setItem(num, combined[0]);

                        if (combined.length > 1) {
                            if (cursor.getType() == Material.AIR) {
                                cursor = combined[1];
                            } else {
                                cursor = getCombinedPurity(combined[1], cursor)[0];
                            }
                        }
                    } else {
                        view.setItem(num, newStacks[i]);
                    }
                    i++;
                }
                var item = new ItemStack(cursor);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> player.setItemOnCursor(item), 1L);
            } else if (type == DragType.SINGLE) {
                var cursorItem = event.getOldCursor();
                if (!isMetal(cursorItem)) return;
                var slots = event.getRawSlots();
                var player = event.getWhoClicked();
                event.setCancelled(true);
                for (var num : slots) {
                    var item = view.getItem(num);
                    if (!(item != null && item.getAmount() == 64)) {
                        ItemStack[] dropped;
                        if (cursorItem.getAmount() > 1) {
                            dropped = dropItemFromPurity(cursorItem);
                        } else {
                            dropped = new ItemStack[2];
                            dropped[0] = air;
                            dropped[1] = new ItemStack(cursorItem);
                        }
                        if (item == null || item.getType() == Material.AIR) {
                            view.setItem(num, dropped[1]);
                        } else {
                            view.setItem(num, getCombinedPurity(item, dropped[1])[0]);
                        }
                        cursorItem = dropped[0];
                    }
                }
                var item = new ItemStack(cursorItem);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> player.setItemOnCursor(item), 1L);
            }
        }

        @EventHandler
        private void onItemMerge(ItemMergeEvent event) {
            var entity = event.getEntity();
            var target = event.getTarget();

            var entityStack = entity.getItemStack();
            var targetStack = target.getItemStack();


            if (hasFolds(entityStack) || hasFolds(targetStack)) return;

            var isRefined = isRefined(entityStack);
            if (entityStack.getType() == targetStack.getType() && isMetal(entityStack) && isMetal(targetStack) && isRefined == isRefined(targetStack)) {
                event.setCancelled(true);
                var combined = getCombinedPurity(entityStack, targetStack);
                target.setItemStack(combined[0]);
                entity.remove();
                event.setCancelled(true);

            }
        }

        @EventHandler
        private void onBrew(BrewEvent event) {
            var ingredient = event.getContents().getIngredient();
            if (ingredient != null && isDust(ingredient)) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onInventoryClick(InventoryClickEvent event) {
            var currentItem = event.getCurrentItem();
            if (currentItem != null && isMetal(currentItem) && !hasPurity(currentItem))
                generatePurity(currentItem, 1.0);

            var cursorItem = event.getCursor();
            if (cursorItem != null && isMetal(cursorItem) && !hasPurity(cursorItem))
                generatePurity(cursorItem, 1.0);

            if (event.getRawSlot() == -1) return;

            var view = event.getView();
            var temp = view.getInventory(event.getRawSlot());
            if (temp != null && temp.getType() == InventoryType.BREWING) {
                if (temp != null && temp.getType() == InventoryType.BREWING) {
                    if (cursorItem != null && isDust(cursorItem)) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
            if ((currentItem != null && isSifter(currentItem)) && view.getType() == InventoryType.CRAFTING && event.isShiftClick() && (view.getItem(5) == null || view.getItem(5).getType() == Material.AIR)) {
                event.setCancelled(true);
                return;
            }
            if ((cursorItem != null && isSifter(cursorItem)) && view.getSlotType(event.getRawSlot()) == InventoryType.SlotType.ARMOR) {
                event.setCancelled(true);
                return;
            }

            if (view.getSlotType(event.getRawSlot()) == InventoryType.SlotType.RESULT &&
                    view.getTopInventory().getType() == InventoryType.ANVIL && view.getItem(event.getRawSlot()) != null && view.getItem(event.getRawSlot()).getType() != Material.AIR) {

                if (!event.isShiftClick()) {
                    if (cursorItem != null && cursorItem.getType() == currentItem.getType() && isMetal(cursorItem) && isMetal(currentItem)
                            && isRefined(cursorItem) == isRefined(currentItem) && !hasFolds(currentItem) && !hasFolds(cursorItem)) {
                        event.getView().setCursor(getCombinedPurity(cursorItem, currentItem)[0]);
                    } else if (cursorItem == null || cursorItem.getType() == Material.AIR)
                        event.getView().setCursor(view.getItem(event.getRawSlot()));
                    else {
                        event.setCancelled(true);
                        return;
                    }
                }

                var item0 = view.getItem(0);
                if (item0 != null && item0.getAmount() > 1)
                    if (isMetal(item0)) {
                        view.setItem(0, dropItemFromPurity(item0)[0]);
                    } else {
                        item0.setAmount(item0.getAmount() - 1);
                    }
                else
                    view.setItem(0, air);

                var item1 = view.getItem(1);
                if (item1 != null && item1.getAmount() > 1)
                    if (isMetal(item1)) {
                        view.setItem(1, dropItemFromPurity(item1)[0]);
                    } else {
                        item1.setAmount(item1.getAmount() - 1);
                    }
                else
                    view.setItem(1, air);

                view.setItem(2, air);

                var player = event.getWhoClicked();
                var block = player.getTargetBlock(null, 5);
                var loc = block.getLocation();

                block.getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 0.5f, 1);

                if (Math.random() < 0.02) {
                    if (block.getType() == Material.ANVIL) {
                        block.setType(Material.CHIPPED_ANVIL);
                    } else if (block.getType() == Material.CHIPPED_ANVIL) {
                        block.setType(Material.DAMAGED_ANVIL);
                    } else {
                        block.setType(Material.AIR);
                        block.getWorld().playSound(loc, Sound.BLOCK_ANVIL_DESTROY, 1, 1);
                    }
                }
            }

            if (view.getSlotType(event.getRawSlot()) == InventoryType.SlotType.RESULT &&
                    view.getItem(0) != null &&
                    view.getItem(0).getType() != Material.AIR &&
                    (view.getTopInventory().getType() == InventoryType.WORKBENCH ||
                            view.getTopInventory().getType() == InventoryType.CRAFTING)) {
                var usesMetal = false;
                var slots = view.countSlots() - 5;
                for (int i = 0; i < slots; i++) {
                    var item = view.getItem(i);
                    if (view.getSlotType(i) == InventoryType.SlotType.CRAFTING
                            && item != null && isMetal(item)) {
                        usesMetal = true;
                        break;
                    }
                }
                if (!usesMetal) return;
                if (cursorItem != null && cursorItem.getType() != Material.AIR && !event.isShiftClick()) {
                    event.setCancelled(true);
                    return;
                }

                for (int i = 0; i < slots; i++) {
                    if (view.getSlotType(i) == InventoryType.SlotType.CRAFTING) {
                        var item = view.getItem(i);
                        if (item == null || item.getType() == Material.AIR) continue;
                        if (isMetal(item)) {
                            if (view.getBottomInventory().firstEmpty() == -1
                                    && event.isShiftClick()) {
                                event.setCancelled(true);
                                return;
                            }
                            if (item.getAmount() > 1) {
                                int num = i;
                                if (item.getType() != Material.AIR)
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                                            () -> view.setItem(num, dropItemFromPurity(item)[0]), 1L);
                            } else {
                                int num = i;
                                if (item.getType() != Material.AIR)
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                                            () -> view.setItem(num, air), 1L);
                            }
                        } else {
                            if (item.getAmount() > 1) {
                                int num = i;
                                item.setAmount(item.getAmount() - 1);
                                if (item.getType() != Material.AIR)
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                                            () -> view.setItem(num, item), 1L);
                            } else {
                                int num = i;
                                if (item.getType() != Material.AIR)
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                                            () -> view.setItem(num, air), 1L);
                            }
                        }
                    }
                }
                if (currentItem != null && isSlurried(currentItem)) {
                    var inv = event.getWhoClicked().getInventory();
                    if (inv.firstEmpty() == -1) {
                        event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), new ItemStack(Material.GLASS_BOTTLE));
                    } else
                        inv.addItem(new ItemStack(Material.GLASS_BOTTLE));
                }
            }

            var click = event.getClick();
            switch (click) {
                case DOUBLE_CLICK -> {
                    if (cursorItem != null && isMetal(cursorItem)) {
                        var isRefined = isRefined(cursorItem);
                        var slots = view.countSlots() - 5;
                        for (int i = 0; i < slots; i++) {
                            if (view.getSlotType(i).equals(InventoryType.SlotType.RESULT)) continue;
                            var item = view.getItem(i);
                            if (hasFolds(currentItem) || hasFolds(item)) continue;
                            if (item != null && item.getType() == cursorItem.getType() && isMetal(item) && isRefined == isRefined(item)) {
                                var nStack = getCombinedPurity(cursorItem, item);
                                cursorItem.setAmount(nStack[0].getAmount());
                                cursorItem.setItemMeta(nStack[0].getItemMeta());

                                if (nStack.length > 1) {
                                    view.setItem(i, nStack[1]);
                                    break;
                                } else {
                                    view.setItem(i, air);
                                }
                            }
                        }
                        event.setCancelled(true);
                        return;
                    }
                }
                case DROP -> {
                    if (currentItem != null && isMetal(currentItem)) {
                        var dropItem = dropItemFromPurity(currentItem);
                        event.setCurrentItem(dropItem[0]);
                        var item = event.getWhoClicked().getWorld().dropItemNaturally(event.getWhoClicked().getLocation(), dropItem[1]);
                        item.setPickupDelay(60);
                        item.setVelocity(event.getWhoClicked().getLocation().getDirection().multiply(0.25));
                        event.setCancelled(true);
                        return;
                    }
                }
                case LEFT -> {
                    if (cursorItem != null && currentItem != null
                            && cursorItem.getType() == currentItem.getType()
                            && isMetal(cursorItem) && isMetal(currentItem) && isRefined(cursorItem) == isRefined(currentItem)) {
                        if (hasFolds(currentItem) || hasFolds(cursorItem)) return;
                        var combined = getCombinedPurity(currentItem, cursorItem);
                        currentItem.setAmount(combined[0].getAmount());
                        currentItem.setItemMeta(combined[0].getItemMeta());

                        if (combined.length > 1) {
                            cursorItem.setAmount(combined[1].getAmount());
                            cursorItem.setItemMeta(combined[1].getItemMeta());
                        } else {
                            event.getWhoClicked().setItemOnCursor(air);
                        }
                        event.setCancelled(true);
                        return;
                    }
                }
                case RIGHT -> {
                    if (event.getSlot() == -999) {
                        if (cursorItem != null && isMetal(cursorItem)) {
                            var dropItem = dropItemFromPurity(cursorItem);
                            event.getView().setCursor(dropItem[0]);
                            var item = event.getWhoClicked().getWorld().dropItemNaturally(event.getWhoClicked().getLocation(), dropItem[1]);
                            item.setPickupDelay(60);
                            item.setVelocity(event.getWhoClicked().getLocation().getDirection().multiply(0.25));
                            event.setCancelled(true);
                            return;
                        }
                    }
                    if ((cursorItem != null && cursorItem.getType() != Material.AIR) && (currentItem == null || currentItem.getType() == Material.AIR) && event.getSlotType() != InventoryType.SlotType.ARMOR) {
                        if (isMetal(cursorItem)) {
                            var dropped = dropItemFromPurity(cursorItem);
                            event.getWhoClicked().setItemOnCursor(dropped[0]);
                            event.setCurrentItem(dropped[1]);
                            event.setCancelled(true);
                            return;
                        }
                    } else if (cursorItem != null && cursorItem.getType() != Material.AIR &&
                            currentItem != null && currentItem.getType() != Material.AIR &&
                            event.getSlotType() != InventoryType.SlotType.ARMOR) {
                        if (hasFolds(cursorItem) || hasFolds(currentItem)) return;
                        if (isMetal(currentItem) && isMetal(cursorItem) && currentItem.getType() == cursorItem.getType() && currentItem.getAmount() < 64 && isRefined(currentItem) == isRefined(cursorItem)) {
                            var dropped = dropItemFromPurity(cursorItem);
                            event.getWhoClicked().setItemOnCursor(dropped[0]);
                            var combined = getCombinedPurity(dropped[1], currentItem);
                            event.setCurrentItem(combined[0]);
                            event.setCancelled(true);
                            return;
                        }
                    } else if ((cursorItem == null || cursorItem.getType() == Material.AIR)
                            && currentItem != null && currentItem.getType() != Material.AIR) {
                        if (isMetal(currentItem)) {
                            var results = splitStackPurities(currentItem);
                            event.setCurrentItem(results[0]);
                            event.getWhoClicked().setItemOnCursor(results[1]);
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
                case SHIFT_LEFT, SHIFT_RIGHT -> {
                    if (currentItem != null && isMetal(currentItem)) {
                        var currentSlot = event.getRawSlot();
                        var nCurrentItem = new ItemStack(currentItem);
                        event.setCurrentItem(air);
                        var size = view.countSlots() - 5;
                        var currentInventory = view.getInventory(event.getRawSlot());

                        // If regular inventory, furnace, or horse/mule inventory
                        if (view.getTopInventory().getType() == InventoryType.CRAFTING ||
                                view.getTopInventory().getType() == InventoryType.FURNACE ||
                                view.getTopInventory().getType() == InventoryType.BLAST_FURNACE ||
                                view.getTopInventory().getType() == InventoryType.SMOKER ||
                                view.getTopInventory().getType() == InventoryType.CARTOGRAPHY ||
                                (view.getTopInventory().getHolder() instanceof Entity &&
                                        (((Entity) view.getTopInventory().getHolder()).getType() == EntityType.DONKEY
                                                || ((Entity) view.getTopInventory().getHolder()).getType() == EntityType.MULE
                                                || ((Entity) view.getTopInventory().getHolder()).getType() == EntityType.HORSE
                                                || ((Entity) view.getTopInventory().getHolder()).getType() == EntityType.ZOMBIE_HORSE
                                                || ((Entity) view.getTopInventory().getHolder()).getType() == EntityType.SKELETON_HORSE))) {
                            int inv;

                            if (view.getTopInventory().getType() != InventoryType.CRAFTING) {
                                if (view.getTopInventory().getHolder() instanceof Entity)
                                    inv = 2;
                                else inv = 3;
                            } else {
                                inv = 9;
                            }

                            if (currentSlot >= inv + 27 ||
                                    view.getSlotType(currentSlot) == InventoryType.SlotType.CRAFTING) {
                                for (int i = inv; i < (inv + 27); i++) {
                                    var slotItem = view.getItem(i);
                                    if (hasFolds(slotItem) || hasFolds(currentItem)) continue;
                                    if (slotItem != null && slotItem.getType() == nCurrentItem.getType() && isMetal(slotItem) && isRefined(slotItem) == isRefined(nCurrentItem)) {
                                        var combined = getCombinedPurity(slotItem, nCurrentItem);
                                        view.setItem(i, combined[0]);
                                        if (combined.length > 1) {
                                            nCurrentItem = combined[1];
                                        } else {
                                            event.setCancelled(true);
                                            return;
                                        }
                                    }
                                }

                                for (int i = inv; i < inv + 27; i++) {
                                    var slotItem = view.getItem(i);
                                    if (slotItem == null || slotItem.getType() == Material.AIR) {
                                        view.setItem(i, nCurrentItem);
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                            } else {
                                var k = false;
                                for (int i = inv + 27; i < inv + 36; i++) {
                                    int j = i;
                                    if (view.getSlotType(currentSlot) == InventoryType.SlotType.RESULT &&
                                            (view.getTopInventory().getType() == InventoryType.WORKBENCH ||
                                                    view.getTopInventory().getType() == InventoryType.CRAFTING)) {
                                        if (!k) {
                                            k = true;
                                            i -= 27;
                                        }
                                        j = inv + 45 - i - 1;
                                    }
                                    var slotItem = view.getItem(j);
                                    if (hasFolds(slotItem) || hasFolds(currentItem)) continue;
                                    if (slotItem != null && slotItem.getType() == nCurrentItem.getType() && isMetal(slotItem) && isRefined(slotItem) == isRefined(nCurrentItem)) {
                                        var combined = getCombinedPurity(slotItem, nCurrentItem);
                                        view.setItem(j, combined[0]);
                                        if (combined.length > 1) {
                                            nCurrentItem = combined[1];
                                        } else {
                                            event.setCancelled(true);
                                            return;
                                        }
                                    }
                                }
                                k = false;
                                for (int i = inv + 27; i < inv + 36; i++) {
                                    int j = i;
                                    if (view.getSlotType(currentSlot) == InventoryType.SlotType.RESULT &&
                                            (view.getTopInventory().getType() == InventoryType.WORKBENCH ||
                                                    view.getTopInventory().getType() == InventoryType.CRAFTING)) {
                                        if (!k) {
                                            k = true;
                                            i -= 27;
                                        }
                                        j = inv + 45 - i - 1;
                                    }

                                    var slotItem = view.getItem(j);
                                    if (slotItem == null || slotItem.getType() == Material.AIR) {
                                        view.setItem(j, nCurrentItem);
                                        event.setCancelled(true);
                                        return;
                                    }
                                }
                            }
                            currentInventory.setItem(currentSlot, nCurrentItem);
                            event.setCancelled(true);
                            return;
                        }

                        // Not regular inventory or horse/mule/donkey
                        for (int i = 0; i < size; i++) {
                            int j = i;
                            if (Objects.equals(view.getInventory(currentSlot), view.getTopInventory())) {
                                switch (view.getTopInventory().getType()) {
                                    case HOPPER, DROPPER, ANVIL, DISPENSER, CHEST, ENDER_CHEST, SHULKER_BOX -> j = size - i - 1;
                                }
                            }
                            if (Objects.equals(view.getInventory(j), currentInventory)) continue;
                            switch (view.getSlotType(j)) {
                                case ARMOR, FUEL, OUTSIDE, RESULT:
                                    continue;
                            }
                            if (j < 3 && view.getType() == InventoryType.CARTOGRAPHY) continue;
                            if (j < 2 && (view.getInventory(j).getHolder() instanceof ChestedHorse)) continue;


                            var slotItem = view.getItem(j);
                            if (hasFolds(slotItem) || hasFolds(currentItem)) continue;
                            if (slotItem != null && slotItem.getType() == nCurrentItem.getType() && isMetal(slotItem) && isRefined(slotItem) == isRefined(nCurrentItem)) {
                                var combined = getCombinedPurity(slotItem, nCurrentItem);
                                view.setItem(j, combined[0]);
                                if (combined.length > 1) {
                                    nCurrentItem = combined[1];
                                } else {
                                    event.setCancelled(true);
                                    return;
                                }
                            }
                        }

                        for (int i = 0; i < size; i++) {
                            int j = i;
                            if (Objects.equals(view.getInventory(currentSlot), view.getTopInventory())) {
                                switch (view.getTopInventory().getType()) {
                                    case HOPPER, DROPPER, ANVIL, DISPENSER, CHEST, ENDER_CHEST, SHULKER_BOX -> j = size - i - 1;
                                }
                            }
                            if (Objects.equals(view.getInventory(j), currentInventory)) continue;
                            switch (view.getSlotType(j)) {
                                case ARMOR, FUEL, OUTSIDE, RESULT:
                                    continue;
                            }
                            if (j < 3 && view.getType() == InventoryType.CARTOGRAPHY) continue;

                            var slotItem = view.getItem(j);
                            if (slotItem == null || slotItem.getType() == Material.AIR) {
                                view.setItem(j, nCurrentItem);
                                event.setCancelled(true);
                                return;
                            }
                        }
                        view.setItem(currentSlot, nCurrentItem);
                        event.setCancelled(true);
                        return;
                    }
                }
            }

            var inventory = event.getInventory();
            if (inventory instanceof FurnaceInventory) {
                var cursor = event.getCursor();
                if (event.getSlotType() == InventoryType.SlotType.CRAFTING && cursor != null && (cursor.getType() == Material.SUGAR || cursor.getType() == Material.REDSTONE || cursor.getType() == Material.GLOWSTONE_DUST)) {
                    if (!isDust(cursor)) event.setCancelled(true);
                    return;
                }
                var current = event.getCurrentItem();
                if ((event.getSlotType() == InventoryType.SlotType.CRAFTING || event.isShiftClick()) && current != null && (current.getType() == Material.SUGAR || current.getType() == Material.REDSTONE || current.getType() == Material.GLOWSTONE_DUST)) {
                    if (!isDust(current)) event.setCancelled(true);
                    return;
                }
            }


            var item = event.getCurrentItem();
            if (item == null) return;
            if (!isMetal(item)) return;
            if (!hasPurity(item)) {
                var nItem = generatePurity(item, 1.0);
                event.setCurrentItem(nItem);
            }
        }

        @EventHandler
        private void onInventoryPickupItem(InventoryPickupItemEvent event) {
            var item = event.getItem().getItemStack();

            if (hasFolds(item)) return;

            if (isMetal(item)) {
                var inv = event.getInventory();
                for (var content : inv.getContents()) {
                    if (hasFolds(content)) continue;
                    if (isMetal(content) && content.getType() == item.getType() && isRefined(content) == isRefined(item)) {
                        var combined = getCombinedPurity(item, content);
                        content.setItemMeta(combined[0].getItemMeta());
                        content.setAmount(combined[0].getAmount());
                        if (combined.length > 1) {
                            item.setItemMeta(combined[1].getItemMeta());
                            item.setAmount(combined[1].getAmount());
                        } else {
                            event.getItem().remove();
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        @EventHandler
        private void onBlockDispense(BlockDispenseEvent event) {
            var item = event.getItem();
            if (!isMetal(item)) return;

            var block = event.getBlock();
            Inventory inv;
            if (block.getType() == Material.DROPPER) {
                inv = ((Dropper) block.getState()).getInventory();
            } else {
                inv = ((Dispenser) block.getState()).getInventory();
            }

            int slot = -1;
            for (int i = 0; i < inv.getSize(); i++) {
                var stack = inv.getItem(i);
                if (isMetal(stack) && Arrays.equals(getPurity(stack), getPurity(item))) {
                    slot = i;
                    break;
                }
            }

            var dropItem = dropItemFromPurity(item);
            event.setItem(dropItem[1]);
            if (dropItem[0] != null && slot > -1) {
                int finalSlot = slot;
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> inv.setItem(finalSlot, dropItem[0]), 0L);
            }
        }

        @EventHandler
        private void onInventoryMoveItem(InventoryMoveItemEvent event) {
            var item = event.getItem();
            var slot = event.getSource().first(item);
            if (isMetal(item)) {
                var inv = event.getDestination();
                switch (inv.getType()) {
                    case BARREL, CHEST, DISPENSER, SHULKER_BOX:
                        break;
                    default:
                        return;
                }
                if (inv.firstEmpty() == -1) return;
                var transfer = item;
                var drop = dropItemFromPurity(item);
                transfer = drop[1];
                event.getSource().setItem(slot, drop[0]);
                for (int i = 0; i < inv.getSize(); i++) {
                    var content = inv.getItem(i);
                    if (content != null && isMetal(content) && content.getItemMeta() != null &&
                            content.getItemMeta().getLore() != null && content.getItemMeta().getLore().contains(foldsLore))
                        continue;
                    if (content == null || content.getType() == Material.AIR) {
                        inv.setItem(i, transfer);
                        event.setCancelled(true);
                        return;
                    } else if (isMetal(content) && content.getType() == item.getType() && content.getAmount() < 64 && isRefined(content) == isRefined(item)) {
                        inv.setItem(i, getCombinedPurity(transfer, content)[0]);
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

        @EventHandler
        private void onEntityPickupItem(EntityPickupItemEvent event) {
            var item = event.getItem().getItemStack();
            if (item == null) return;
            if (isMetal(item) && event.getEntity() instanceof Player) {
                var isRefined = isRefined(item);
                var inv = ((Player) event.getEntity()).getInventory();
                for (var content : inv.getContents()) {
                    if (isMetal(content) && content.getType() == item.getType() && isRefined == isRefined(content)) {
                        var combined = getCombinedPurity(item, content);
                        content.setItemMeta(combined[0].getItemMeta());
                        content.setAmount(combined[0].getAmount());
                        if (combined.length > 1) {
                            item.setItemMeta(combined[1].getItemMeta());
                            item.setAmount(combined[1].getAmount());
                        } else {
                            event.getItem().remove();
                            event.setCancelled(true);
                            var player = ((Player) event.getEntity());
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.2f, (float) (Math.random() * 8));
                            return;
                        }
                    }
                }
            }
        }

        @EventHandler
        private void onFurnaceSmelt(FurnaceSmeltEvent event) {
            var furnace = (Furnace) event.getBlock().getState();
            if (furnace.getInventory().getSmelting() == null) return;
            var inventory = furnace.getInventory();
            var smelting = inventory.getSmelting();
            var type = smelting.getType();
            if (type == Material.SUGAR || type == Material.REDSTONE || type == Material.GLOWSTONE_DUST) {
                if (!isDust(smelting)) {
                    event.setCancelled(true);
                    return;
                }
                var result = inventory.getResult();

                Material material = switch (type) {
                    case SUGAR -> Material.IRON_NUGGET;
                    case REDSTONE -> Material.NETHERITE_SCRAP;
                    case GLOWSTONE_DUST -> Material.GOLD_NUGGET;
                    default -> null;
                };
                var item = new ItemStack(material);

                var newResult = generatePurity(item, 0.01);

                if (result != null) {
                    inventory.setResult(getCombinedPurity(newResult, result)[0]);
                } else {
                    inventory.setResult(newResult);
                }

                if (smelting.getAmount() == 1) {
                    inventory.setSmelting(air);
                } else {
                    smelting.setAmount(smelting.getAmount() - 1);
                }
                event.setCancelled(true);
            }
            if (iron.contains(type) || gold.contains(type) || type == Material.CHAINMAIL_HELMET || type == Material.CHAINMAIL_CHESTPLATE || type == Material.CHAINMAIL_LEGGINGS || type == Material.CHAINMAIL_BOOTS ||
                    type == Material.IRON_ORE || type == Material.GOLD_ORE || type == Material.ANCIENT_DEBRIS || type == Material.NETHER_GOLD_ORE) {
                var result = inventory.getResult();

                Material material = switch (type) {
                    case IRON_ORE -> Material.IRON_INGOT;
                    case GOLD_ORE, NETHER_GOLD_ORE -> Material.GOLD_INGOT;
                    case ANCIENT_DEBRIS -> Material.NETHERITE_SCRAP;
                    case CHAINMAIL_HELMET, CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS -> Material.IRON_NUGGET;
                    default -> Material.AIR;
                };
                if (iron.contains(type)) material = Material.IRON_NUGGET;
                else if (gold.contains(type)) material = Material.GOLD_NUGGET;
                if (isSifter(smelting)) material = Material.AIR;

                var item = new ItemStack(material);

                ItemStack newResult;
                if (item.getType() != Material.AIR)
                    newResult = generatePurity(item, 1.0);
                else
                    newResult = item;

                if (newResult.getType() != Material.AIR)
                    if (result != null) {
                        inventory.setResult(getCombinedPurity(newResult, result)[0]);
                    } else {
                        inventory.setResult(newResult);
                    }

                if (smelting.getAmount() == 1) {
                    inventory.setSmelting(air);
                } else {
                    smelting.setAmount(smelting.getAmount() - 1);
                }
                event.setCancelled(true);
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
                    result.addUnsafeEnchantment(Enchantment.DURABILITY, ingredient.getEnchantmentLevel(Enchantment.DURABILITY));
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
                player.getInventory().setItemInMainHand(air);
                event.setCancelled(true);
                return;
            }
        }
    }
}
