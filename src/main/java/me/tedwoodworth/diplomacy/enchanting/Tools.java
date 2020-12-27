package me.tedwoodworth.diplomacy.enchanting;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyRecipes;
import me.tedwoodworth.diplomacy.data.BooleanPersistentDataType;
import me.tedwoodworth.diplomacy.data.FloatArrayPersistentDataType;
import me.tedwoodworth.diplomacy.entities.Entities;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.*;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.type.TurtleEgg;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static org.bukkit.potion.PotionEffectType.*;

public class Tools {
    private static Tools instance = null;
    public final NamespacedKey purityKey = new NamespacedKey(Diplomacy.getInstance(), "purity");
    public final NamespacedKey purity3DKey = new NamespacedKey(Diplomacy.getInstance(), "purity3D");
    public final NamespacedKey pickupKey = new NamespacedKey(Diplomacy.getInstance(), "pickup");
    private final String purityLore = ChatColor.GRAY + "Purity:";
    private final String plateLore = ChatColor.BLUE + "Refined Plate";
    private final String refinedLore = ChatColor.BLUE + "Refined";
    private final ItemStack air = new ItemStack(Material.AIR);
    private final Set<Player> grenadeDropList = new HashSet<>();
    private final Set<Item> itemPickupList = new HashSet<>();

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
    public final List<Material> tools = new ArrayList<>();

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
    public final String p11 = "" + ChatColor.WHITE + ChatColor.BOLD + "||||||||||";
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

    public boolean isGrenade(ItemStack itemStack) {
        if (itemStack == null) return false;
        var meta = itemStack.getItemMeta();
        return meta != null &&
                meta.getLore() != null
                && meta.getLore().contains(DiplomacyRecipes.getInstance().GRENADE_LORE);
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

    public ItemStack[] dropItemsFromPurity(ItemStack stack, int dropAmount) {
        var meta = stack.getItemMeta();
        var purities = getPurity(stack);

        var isRefined = isRefined(stack);
        var dropItemPurities = new float[dropAmount];
        if (dropAmount >= 0) System.arraycopy(purities, 0, dropItemPurities, 0, dropAmount);

        var keepItemPurities = new float[purities.length - dropAmount];
        System.arraycopy(purities, dropAmount, keepItemPurities, 0, keepItemPurities.length);

        ItemStack keepStack = new ItemStack(stack.getType(), keepItemPurities.length);
        var keepMeta = keepStack.getItemMeta();
        keepMeta.setLore(generatePurityLure(keepItemPurities));
        var lore = keepMeta.getLore();
        if (isRefined) {
            lore.add("");
            lore.add(refinedLore);
            keepMeta.setLore(lore);
        }
        if (isPlate(stack)) {
            lore.add("");
            lore.add(plateLore);
            keepMeta.setLore(lore);
            keepMeta.setDisplayName(ChatColor.RESET + stack.getItemMeta().getDisplayName());
        } else {
            keepMeta.setDisplayName(stack.getItemMeta().getDisplayName());
        }
        keepMeta.setLocalizedName(stack.getItemMeta().getLocalizedName());
        keepStack.setItemMeta(keepMeta);
        setPurity(keepStack, keepItemPurities);

        var dropStack = new ItemStack(stack.getType(), dropAmount);
        var dropMeta = dropStack.getItemMeta();
        dropMeta.setLore(generatePurityLure(dropItemPurities));
        lore = dropMeta.getLore();
        if (isRefined) {
            lore.add("");
            lore.add(refinedLore);
            dropMeta.setLore(lore);
        }
        if (isPlate(stack)) {
            lore.add("");
            lore.add(plateLore);
            dropMeta.setLore(lore);
            dropMeta.setDisplayName(ChatColor.RESET + stack.getItemMeta().getDisplayName());
        } else {
            dropMeta.setDisplayName(stack.getItemMeta().getDisplayName());
        }
        dropMeta.setLocalizedName(stack.getItemMeta().getLocalizedName());
        dropStack.setItemMeta(dropMeta);
        setPurity(dropStack, dropItemPurities);

        var newStacks = new ItemStack[2];
        newStacks[0] = keepStack;
        newStacks[1] = dropStack;
        return newStacks;
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
        if (!canCombine(stack1, stack2)) throw new IllegalArgumentException("Items cannot be combined");
        var purities1 = getPurity(stack1);
        var purities2 = getPurity(stack2);
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

            if (purities[i] == 0.0f) loreBuilder.append(p11);
            else if (purities[i] < 0.000001f) loreBuilder.append(p10);
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

        tools.add(Material.WOODEN_AXE);
        tools.add(Material.STONE_AXE);
        tools.add(Material.IRON_AXE);
        tools.add(Material.GOLDEN_AXE);
        tools.add(Material.DIAMOND_AXE);
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

    private boolean canCombine(ItemStack item1, ItemStack item2) {
        return item1 != null && item1.getItemMeta() != null
                && item2 != null && item2.getItemMeta() != null
                && item1.getType() == item2.getType()
                && isMetal(item1) == isMetal(item2)
                && !(item1.getItemMeta().hasEnchants() && !isGrenade(item1))
                && !(item2.getItemMeta().hasEnchants() && !isGrenade(item2))
                && isRefined(item1) == isRefined(item2)
                && isPlate(item1) == isPlate(item2)
                && !isSlurried(item1)
                && !isSlurried(item2);
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
            return lore.contains(DiplomacyRecipes.getInstance().SLURRIED_IRON_LORE) ||
                    lore.contains(DiplomacyRecipes.getInstance().SLURRIED_GOLD_LORE) ||
                    lore.contains(DiplomacyRecipes.getInstance().SLURRIED_NETHERITE_LORE);
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
        if (isSlurried(item)) return true;
        if (isPlate(item)) return true;
        var meta = item.getItemMeta();
        if (meta == null) return false;
        return meta.getLore() != null && meta.getLore().contains(purityLore);
    }

    private void setPurity(ItemStack item, float[] purities) {
        if (!(isMetal(item)) && !isSlurried(item)) throw new IllegalArgumentException("Item is not a metal.");
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

    private void setBlockPurity(Block block, float purity) {
        var type = block.getType();
        if (type != Material.GOLD_BLOCK && type != Material.IRON_BLOCK && type != Material.NETHERITE_BLOCK && purity != -1F)
            throw new IllegalArgumentException("Block is not a metal.");

        var location = block.getLocation();
        var chunk = location.getChunk();
        var x = location.getBlockX() - chunk.getX() * 16;
        var y = location.getBlockY();
        var z = location.getBlockZ() - chunk.getZ() * 16;

        var num = (x << 12) | (y << 4) | (z);

        var data = ((PersistentDataHolder) chunk).getPersistentDataContainer();
        var purities = data.get(purity3DKey, FloatArrayPersistentDataType.instance);
        if (purities == null) {
            purities = new float[16 * 16 * block.getWorld().getMaxHeight()];
            Arrays.fill(purities, -1F);
        }
        purities[num] = purity;
        data.set(purity3DKey, FloatArrayPersistentDataType.instance, purities);
    }

    private float getBlockPurity(Block block) {
        var type = block.getType();
        if (type != Material.GOLD_BLOCK && type != Material.IRON_BLOCK && type != Material.NETHERITE_BLOCK)
            throw new IllegalArgumentException("Block is not a metal.");

        var location = block.getLocation();
        var chunk = location.getChunk();
        var x = location.getBlockX() - chunk.getX() * 16;
        var y = location.getBlockY();
        var z = location.getBlockZ() - chunk.getZ() * 16;

        var num = (x << 12) | (y << 4) | (z);


        var data = ((PersistentDataHolder) chunk).getPersistentDataContainer();
        var purities = data.get(purity3DKey, FloatArrayPersistentDataType.instance);
        if (purities == null) {
            purities = new float[16 * 16 * block.getWorld().getMaxHeight()];
            Arrays.fill(purities, -2F);
            purities[num] = (float) (Math.random());
        }
        var purity = purities[num];
        if (purity <= -1F) {
            purity = (float) (Math.random());
            purities[num] = purity;
        }
        return purity;
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
                if (lore.contains(DiplomacyRecipes.getInstance().SLURRIED_IRON_LORE)) {
                    nItem = new ItemStack(Material.IRON_INGOT);
                    var i = lore.indexOf(DiplomacyRecipes.getInstance().SLURRIED_IRON_LORE);
                    lore.remove(i);
                    lore.remove(i - 1);
                    meta.setDisplayName(ChatColor.RESET + "Iron Ingot");
                    meta.setLocalizedName("Iron Ingot");
                } else if (lore.contains(DiplomacyRecipes.getInstance().SLURRIED_GOLD_LORE)) {
                    nItem = new ItemStack(Material.GOLD_INGOT);
                    var i = lore.indexOf(DiplomacyRecipes.getInstance().SLURRIED_GOLD_LORE);
                    lore.remove(i);
                    lore.remove(i - 1);
                    meta.setDisplayName(ChatColor.RESET + "Gold Ingot");
                    meta.setLocalizedName("Gold Ingot");
                } else {
                    nItem = new ItemStack(Material.NETHERITE_INGOT);
                    var i = lore.indexOf(DiplomacyRecipes.getInstance().SLURRIED_NETHERITE_LORE);
                    lore.remove(i);
                    lore.remove(i - 1);
                    meta.setDisplayName(ChatColor.RESET + "Netherite Ingot");
                    meta.setLocalizedName("Netherite Ingot");
                }
                nItem.setItemMeta(item.getItemMeta());
                lore.add("" + ChatColor.BLACK + ChatColor.MAGIC + (int) (Math.random() * Integer.MAX_VALUE));
                lore.add(refinedLore);
                meta.setLore(lore);
                nItem.setItemMeta(meta);
                var purity = getPurity(nItem)[0];
                int unbreaking;
                if (purity == 0.0f) unbreaking = 12;
                else {
                    unbreaking = (int) (((-5.0 * Math.log(purity)) / (3 * Math.log(10.0))));
                    unbreaking = Math.min(10, unbreaking);
                }
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

            // blazed arrows
            if (result.getType() == Material.ARROW && result.getAmount() == 8) {
                var blaze = false;
                var arrows = 0;
                var punch = 2;
                var power = 10;
                ItemStack firstArrow = null;
                for (var item : inventory.getMatrix()) {
                    if (item.getType() == Material.BLAZE_POWDER) {
                        blaze = true;
                    } else if (firstArrow == null) {
                        firstArrow = item;
                    } else if (item.getType() == Material.TIPPED_ARROW) {
                        var potionMeta = ((PotionMeta) firstArrow.getItemMeta());
                        var potionMeta2 = ((PotionMeta) item.getItemMeta());
                        if (potionMeta == null || potionMeta2 == null) {
                            inventory.setResult(air);
                            return;
                        }
                        if (potionMeta.getBasePotionData().getType() != potionMeta2.getBasePotionData().getType()) {
                            inventory.setResult(air);
                            return;
                        }
                    } else {
                        if (item.getType() != firstArrow.getType()) {
                            inventory.setResult(air);
                            return;
                        }
                    }
                    if (item != null && item.getType() != Material.BLAZE_POWDER) {
                        arrows++;
                        var enchants = item.getEnchantments();
                        if (punch > 0 && (enchants == null || !enchants.containsKey(Enchantment.ARROW_KNOCKBACK)))
                            punch = 0;
                        if (power > 0 && enchants != null && enchants.containsKey(Enchantment.ARROW_DAMAGE)) {
                            power = Math.min(power, enchants.get(Enchantment.ARROW_DAMAGE));
                        } else {
                            power = 0;
                        }
                    }
                }
                if (arrows < 8) return;
                if (!blaze) return;
                if (firstArrow == null) return;
                result = new ItemStack(firstArrow);
                result.setAmount(8);
                var enchants = result.getEnchantments();
                for (var enchant : enchants.keySet()) {
                    result.removeEnchantment(enchant);
                }

                if (punch > 0) result.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, punch);
                if (power > 0) result.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, power);
                result.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
                inventory.setResult(result);
                return;
            }

            // heavy tipped arrows and heavy spectral arrows
            if (result.getType() == Material.SPECTRAL_ARROW && result.getAmount() == 2) {
                for (var item : inventory.getMatrix()) {
                    if (item != null && item.getType() != Material.AIR && item.getType() != Material.ARROW && item.getType() != Material.GLOWSTONE_DUST)
                        return;
                    if (item != null && item.getType() == Material.ARROW) {
                        var enchantments = item.getEnchantments();
                        for (var enchantment : enchantments.keySet()) {
                            result.addUnsafeEnchantment(enchantment, enchantments.get(enchantment));
                        }
                    }
                }
                inventory.setResult(result);
            }

            if (result.getType() == Material.TIPPED_ARROW) {
                var arrows = 0;
                var punch = 2;
                var power = 10;
                var flame = 1;
                for (var item : inventory.getMatrix()) {
                    if (item != null && item.getType() == Material.ARROW) {
                        arrows++;
                        var enchants = item.getEnchantments();
                        if (punch > 0 && (enchants == null || !enchants.containsKey(Enchantment.ARROW_KNOCKBACK)))
                            punch = 0;
                        if (flame > 0 && (enchants == null || !enchants.containsKey(Enchantment.ARROW_FIRE))) flame = 0;
                        if (power > 0 && enchants != null && enchants.containsKey(Enchantment.ARROW_DAMAGE)) {
                            power = Math.min(power, enchants.get(Enchantment.ARROW_DAMAGE));
                        } else {
                            power = 0;
                        }
                    }
                }
                if (arrows < 8) return;
                if (punch > 0) result.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, punch);
                if (power > 0) result.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, power);
                if (flame > 0) result.addUnsafeEnchantment(Enchantment.ARROW_FIRE, flame);
                inventory.setResult(result);
            }


            // removing cooldown
            if (tools.contains(result.getType()) && result.getItemMeta() != null) {
                var meta = result.getItemMeta();
                meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier("generic.attackSpeed", 20, AttributeModifier.Operation.ADD_NUMBER));
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                result.setItemMeta(meta);
                inventory.setResult(result);
            }

            // Cancelling sharpening tools
            for (var item : inventory.getMatrix()) {
                if (isWhetstone(item)) {
                    if (result.getType() == Material.FLOWER_POT || result.getType() == Material.BRICKS) {
                        inventory.setResult(air);
                        return;
                    }
                }
                if (isWaterstone(item)) {
                    if (result.getType() == Material.NETHER_BRICK_FENCE || result.getType() == Material.NETHER_BRICK || result.getType() == Material.RED_NETHER_BRICKS) {
                        inventory.setResult(air);
                        return;
                    }
                }
                if (isCoarseSandPaper(item) || isFineSandPaper(item)) {
                    if (result.getType() == Material.CREEPER_BANNER_PATTERN
                            || result.getType() == Material.FLOWER_BANNER_PATTERN
                            || result.getType() == Material.SKULL_BANNER_PATTERN
                            || result.getType() == Material.MOJANG_BANNER_PATTERN
                            || result.getType() == Material.BOOK
                            || result.getType() == Material.CARTOGRAPHY_TABLE
                            || result.getType() == Material.MAP
                            || result.getType() == Material.FIREWORK_ROCKET
                    ) {
                        inventory.setResult(air);
                        return;
                    }
                }
            }

            // Fine sand paper
            if (isFineSandPaper(result)) {
                for (var item : inventory.getMatrix()) {
                    if (item == null || item.getType() == Material.AIR) continue;
                    if (item.getType() == Material.SUGAR && !isDust(item)) {
                        inventory.setResult(air);
                        return;
                    } else if (isFineSandPaper(item) || isCoarseSandPaper(item)) {
                        inventory.setResult(air);
                        return;
                    }
                }
            }

            // Whetstone
            if (isWhetstone(result)) {
                var unbreaking = 0;
                for (var item : inventory.getMatrix()) {
                    if (item.getType() == Material.IRON_INGOT) {
                        var meta = item.getItemMeta();
                        if (meta == null || !meta.hasEnchant(Enchantment.DURABILITY) || meta.getEnchantLevel(Enchantment.DURABILITY) < 2) {
                            inventory.setResult(air);
                            return;
                        }
                        unbreaking += meta.getEnchantLevel(Enchantment.DURABILITY);
                    }
                }
                var uses = 25 * unbreaking;
                var meta = result.getItemMeta();
                var lore = meta.getLore();
                var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                lore.set(line + 1, String.valueOf(uses));
                meta.setLore(lore);
                result.setItemMeta(meta);
                inventory.setResult(result);
                return;
            }

            // Waterstone
            if (isWaterstone(result)) {
                var uses = 0;
                for (var item : inventory.getMatrix()) {
                    if (item == null || item.getType() == Material.AIR) {
                        inventory.setResult(air);
                        return;
                    }
                    if (item.getType() == Material.BRICK) {
                        if (!isWhetstone(item)) {
                            inventory.setResult(air);
                            return;
                        }
                        var line = item.getItemMeta().getLore().indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                        uses = Integer.parseInt(item.getItemMeta().getLore().get(line + 1));
                    }
                    if (item.getType() == Material.GLOWSTONE_DUST) {
                        if (!isDust(item)) {
                            inventory.setResult(air);
                            return;
                        }
                    }
                    if (item.getType() == Material.REDSTONE) {
                        if (!isDust(item)) {
                            inventory.setResult(air);
                            return;
                        }
                    }
                }
                uses = (int) (uses + Math.pow(0.5 * uses, uses / (uses + 35.0)));
                var meta = result.getItemMeta();
                var lore = meta.getLore();
                var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                lore.set(line + 1, String.valueOf(uses));
                meta.setLore(lore);
                result.setItemMeta(meta);
                inventory.setResult(result);
                return;
            }

            // Coarse Sharpening Blade
            if (isCoarseBlade(result)) {
                var uses = -1;
                var knives = false;
                for (var item : inventory.getMatrix()) {
                    if (item == null || item.getType() == Material.AIR) continue;
                    if (item.getType() == Material.IRON_SWORD) {
                        if (isHuntingKnife(item)) {
                            inventory.setResult(air);
                            return;
                        } else {
                            var unbreaking = 1;
                            if (item.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
                                unbreaking = item.getEnchantmentLevel(Enchantment.DURABILITY);
                                if (unbreaking < 5) {
                                    inventory.setResult(air);
                                    return;
                                } else {
                                    unbreaking++;
                                }
                            } else {
                                inventory.setResult(air);
                                return;
                            }
                            if (!item.getItemMeta().hasEnchant(Enchantment.DAMAGE_ALL) || item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) < 6) {
                                inventory.setResult(air);
                                return;
                            }
                            if (uses == -1)
                                uses = (item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage()) * unbreaking;
                            else {
                                knives = true;
                                uses = Math.min(uses, (item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage()) * unbreaking);
                            }

                        }
                    }
                }
                if (!knives) {
                    inventory.setResult(air);
                    return;
                }
                uses = (int) Math.pow(uses, 6.0 / 7.0);
                var meta = result.getItemMeta();
                var lore = meta.getLore();
                var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                lore.set(line + 1, String.valueOf(uses));
                meta.setLore(lore);
                result.setItemMeta(meta);
                inventory.setResult(result);
                return;
            }

            // Fine Sharpening Blade
            if (isFineBlade(result)) {
                var uses = -1;
                var knives = false;
                for (var item : inventory.getMatrix()) {
                    if (item == null || item.getType() == Material.AIR) continue;
                    if (item.getType() == Material.NETHERITE_SWORD) {
                        if (isHuntingKnife(item)) {
                            inventory.setResult(air);
                            return;
                        } else {
                            var unbreaking = 1;
                            if (item.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
                                unbreaking = item.getEnchantmentLevel(Enchantment.DURABILITY);
                                if (unbreaking < 5) {
                                    inventory.setResult(air);
                                    return;
                                } else {
                                    unbreaking++;
                                }
                            } else {
                                inventory.setResult(air);
                                return;
                            }
                            if (!item.getItemMeta().hasEnchant(Enchantment.DAMAGE_ALL) || item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) < 7) {
                                inventory.setResult(air);
                                return;
                            }
                            if (uses == -1)
                                uses = (item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage()) * unbreaking;
                            else {
                                knives = true;
                                uses = Math.min(uses, (item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage()) * unbreaking);
                            }

                        }
                    }
                }
                if (!knives) {
                    inventory.setResult(air);
                    return;
                }
                uses = (int) (Math.pow(uses, 9.0 / 10.0) / 7.0);
                var meta = result.getItemMeta();
                var lore = meta.getLore();
                var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                lore.set(line + 1, String.valueOf(uses));
                meta.setLore(lore);
                result.setItemMeta(meta);
                inventory.setResult(result);
                return;
            }

            // Iron Honing Rod
            if (isIronRod(result)) {
                for (var item : inventory.getMatrix()) {
                    if (item == null || item.getType() == Material.AIR) continue;
                    if (tools.contains(item.getType())) {
                        if (!isChisel(item)) {
                            inventory.setResult(air);
                            return;
                        } else {
                            var durability = item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage();
                            var unbreaking = 1;
                            if (item.getItemMeta().hasEnchant(Enchantment.DURABILITY))
                                unbreaking += item.getEnchantmentLevel(Enchantment.DURABILITY);
                            if (durability * unbreaking < 1000) {
                                inventory.setResult(air);
                                return;
                            }
                        }
                    } else {
                        if (item.getItemMeta() == null || !item.getItemMeta().hasEnchant(Enchantment.DURABILITY) || item.getEnchantmentLevel(Enchantment.DURABILITY) != 10) {
                            inventory.setResult(air);
                            return;
                        }
                    }
                }
                inventory.setResult(result);
                return;
            }

            // Netherite Honing Rod
            if (isNetheriteRod(result)) {
                for (var item : inventory.getMatrix()) {
                    if (item == null || item.getType() == Material.AIR) continue;
                    if (tools.contains(item.getType())) {
                        if (!isChisel(item)) {
                            inventory.setResult(air);
                            return;
                        } else {
                            var durability = item.getType().getMaxDurability() - ((Damageable) item.getItemMeta()).getDamage();
                            var unbreaking = 1;
                            if (item.getItemMeta().hasEnchant(Enchantment.DURABILITY))
                                unbreaking += item.getEnchantmentLevel(Enchantment.DURABILITY);
                            if (durability * unbreaking < 2500) {
                                inventory.setResult(air);
                                return;
                            }
                        }
                    } else {
                        if (item.getItemMeta() == null || !item.getItemMeta().hasEnchant(Enchantment.DURABILITY) || item.getEnchantmentLevel(Enchantment.DURABILITY) != 10) {
                            inventory.setResult(air);
                            return;
                        }
                    }
                }
                inventory.setResult(result);
                return;
            }

            // Sharpness
            if (result.getType() == Material.STICK && result.getItemMeta() != null && result.getItemMeta().hasEnchant(Enchantment.DAMAGE_ALL)) {
                var meta = result.getItemMeta();

                int sharpness = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);
                ItemStack nResult;
                if (sharpness == 3) {
                    ItemStack tool = null;
                    ItemStack paper = null;
                    for (var item : inventory.getMatrix()) {
                        if (item == null || item.getType() == Material.AIR) continue;
                        var type = item.getType();
                        if (tools.contains(type)) tool = item;
                        if (type == Material.PAPER) paper = item;
                    }
                    if (tool == null || !(isCoarseSandPaper(paper) || isFineSandPaper(paper))) {
                        inventory.setResult(air);
                        return;
                    }
                    if (isFineSandPaper(paper)) sharpness = 4;

                    nResult = new ItemStack(tool);
                } else if (sharpness == 5) {
                    ItemStack tool = null;
                    ItemStack whetstone = null;
                    for (var item : inventory.getMatrix()) {
                        if (item == null || item.getType() == Material.AIR) continue;
                        var type = item.getType();
                        if (tools.contains(type)) tool = item;
                        if (type == Material.BRICK) whetstone = item;
                    }
                    if (tool == null || !isWhetstone(whetstone)) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult = new ItemStack(tool);
                } else if (sharpness == 6) {
                    ItemStack tool = null;
                    ItemStack waterstone = null;
                    var water = false;
                    for (var item : inventory.getMatrix()) {
                        if (item == null || item.getType() == Material.AIR) continue;
                        var type = item.getType();
                        if (tools.contains(type)) tool = item;
                        if (type == Material.NETHER_BRICK) waterstone = item;
                        if (item.getItemMeta() instanceof PotionMeta) {
                            var potionType = ((PotionMeta) item.getItemMeta()).getBasePotionData().getType();
                            if (potionType != PotionType.WATER) {
                                inventory.setResult(air);
                                return;
                            } else {
                                water = true;
                            }
                        }
                    }
                    if (tool == null || !isWaterstone(waterstone) || !water) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult = new ItemStack(tool);
                } else if (sharpness == 7) {
                    ItemStack tool = null;
                    ItemStack coarseBlades = null;
                    for (var item : inventory.getMatrix()) {
                        if (item == null || item.getType() == Material.AIR) continue;
                        var type = item.getType();
                        if (tools.contains(type)) tool = item;
                        if (type == Material.SMOOTH_QUARTZ_STAIRS) coarseBlades = item;
                    }
                    if (tool == null || !isCoarseBlade(coarseBlades)) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult = new ItemStack(tool);
                } else if (sharpness == 8) {
                    ItemStack tool = null;
                    ItemStack fineBlades = null;
                    for (var item : inventory.getMatrix()) {
                        if (item == null || item.getType() == Material.AIR) continue;
                        var type = item.getType();
                        if (tools.contains(type)) tool = item;
                        if (type == Material.POLISHED_BLACKSTONE_STAIRS) fineBlades = item;
                    }
                    if (tool == null || !isFineBlade(fineBlades)) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult = new ItemStack(tool);
                } else if (sharpness == 9) {
                    ItemStack tool = null;
                    ItemStack rod = null;
                    for (var item : inventory.getMatrix()) {
                        if (item == null || item.getType() == Material.AIR) continue;
                        var type = item.getType();
                        if (tools.contains(type)) tool = item;
                        if (type == Material.END_ROD) rod = item;
                        if (isNetheriteRod(item)) sharpness = 10;
                    }
                    if (tool == null || !(isIronRod(rod) || isNetheriteRod(rod))) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult = new ItemStack(tool);
                } else {
                    inventory.setResult(air);
                    return;
                }

                var type = nResult.getType();
                var nMeta = nResult.getItemMeta();
                if ((type == Material.WOODEN_HOE || type == Material.STONE_HOE || type == Material.IRON_HOE || type == Material.GOLDEN_HOE || type == Material.DIAMOND_HOE || type == Material.NETHERITE_HOE)
                        && !(isChisel(nResult) || isSaw(nResult))) {
                    inventory.setResult(air);
                    return;
                }

                if (type == Material.WOODEN_AXE
                        || type == Material.STONE_AXE
                        || type == Material.IRON_AXE
                        || type == Material.GOLDEN_AXE
                        || type == Material.DIAMOND_AXE
                        || type == Material.NETHERITE_AXE
                ) {
                    if (nMeta == null || !nMeta.hasEnchant(Enchantment.DAMAGE_ALL) || nMeta.getEnchantLevel(Enchantment.DAMAGE_ALL) != sharpness - 1) {
                        inventory.setResult(air);
                        return;
                    }

                    nResult.removeEnchantment(Enchantment.DAMAGE_ALL);
                    nResult.removeEnchantment(Enchantment.DIG_SPEED);
                    nResult.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                    nResult.addUnsafeEnchantment(Enchantment.DIG_SPEED, sharpness);
                    if (nResult.getItemMeta() instanceof Damageable) {
                        nMeta = nResult.getItemMeta();
                        var damageable = (Damageable) nMeta;
                        var max = nResult.getType().getMaxDurability();
                        var nDurability = (int) ((max - damageable.getDamage()) * (1 - (0.15 * Math.pow(0.5, sharpness - 1))));
                        if (nDurability < 1) {
                            inventory.setResult(air);
                            return;
                        }
                        damageable.setDamage(max - nDurability);
                        nResult.setItemMeta(nMeta);
                        inventory.setResult(nResult);
                    }
                    return;
                }
                if (isChisel(nResult)) {
                    if (nMeta == null || !nMeta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) || nMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) != sharpness - 1) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
                    nResult.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, sharpness);
                    if (nResult.getItemMeta() instanceof Damageable) {
                        nMeta = nResult.getItemMeta();
                        var damageable = (Damageable) nMeta;
                        var max = nResult.getType().getMaxDurability();
                        var nDurability = (int) ((max - damageable.getDamage()) * (1 - (0.15 * Math.pow(0.5, sharpness - 1))));
                        if (nDurability < 1) {
                            inventory.setResult(air);
                            return;
                        }
                        damageable.setDamage(max - nDurability);
                        nResult.setItemMeta(nMeta);
                        inventory.setResult(nResult);
                    }
                    return;
                }
                if (nResult.getType() == Material.ARROW || nResult.getType() == Material.TIPPED_ARROW || nResult.getType() == Material.SPECTRAL_ARROW) {
                    if (nMeta == null || !nMeta.hasEnchant(Enchantment.ARROW_DAMAGE) || nMeta.getEnchantLevel(Enchantment.ARROW_DAMAGE) != sharpness - 1) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult.removeEnchantment(Enchantment.ARROW_DAMAGE);
                    nResult.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, sharpness);
                    inventory.setResult(nResult);
                    return;
                }
                if (isHuntingKnife(nResult)) {
                    if (nMeta == null || !nMeta.hasEnchant(Enchantment.LOOT_BONUS_MOBS) || nMeta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS) != sharpness - 1) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
                    nResult.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, sharpness);
                    if (nResult.getItemMeta() instanceof Damageable) {
                        nMeta = nResult.getItemMeta();
                        var damageable = (Damageable) nMeta;
                        var max = nResult.getType().getMaxDurability();
                        var nDurability = (int) ((max - damageable.getDamage()) * (1 - (0.15 * Math.pow(0.5, sharpness - 1))));
                        if (nDurability < 1) {
                            inventory.setResult(air);
                            return;
                        }
                        damageable.setDamage(max - nDurability);
                        nResult.setItemMeta(nMeta);
                        inventory.setResult(nResult);
                    }
                    return;
                }
                if (type == Material.WOODEN_PICKAXE
                        || type == Material.STONE_PICKAXE
                        || type == Material.IRON_PICKAXE
                        || type == Material.GOLDEN_PICKAXE
                        || type == Material.DIAMOND_PICKAXE
                        || type == Material.NETHERITE_PICKAXE
                ) {
                    if (nMeta == null || !nMeta.hasEnchant(Enchantment.DIG_SPEED) || nMeta.getEnchantLevel(Enchantment.DIG_SPEED) != sharpness - 1) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult.removeEnchantment(Enchantment.DIG_SPEED);
                    nResult.addUnsafeEnchantment(Enchantment.DIG_SPEED, sharpness);
                    if (nResult.getItemMeta() instanceof Damageable) {
                        nMeta = nResult.getItemMeta();
                        var damageable = (Damageable) nMeta;
                        var max = nResult.getType().getMaxDurability();
                        var nDurability = (int) ((max - damageable.getDamage()) * (1 - (0.15 * Math.pow(0.5, sharpness - 1))));
                        if (nDurability < 1) {
                            inventory.setResult(air);
                            return;
                        }
                        damageable.setDamage(max - nDurability);
                        nResult.setItemMeta(nMeta);
                        inventory.setResult(nResult);
                    }
                    return;
                }
                if (isSaw(nResult)) {
                    if (nMeta == null || !nMeta.hasEnchant(Enchantment.DIG_SPEED) || nMeta.getEnchantLevel(Enchantment.DIG_SPEED) != sharpness - 1) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult.removeEnchantment(Enchantment.DIG_SPEED);
                    nResult.addUnsafeEnchantment(Enchantment.DIG_SPEED, sharpness);
                    if (nResult.getItemMeta() instanceof Damageable) {
                        nMeta = nResult.getItemMeta();
                        var damageable = (Damageable) nMeta;
                        var max = nResult.getType().getMaxDurability();
                        var nDurability = (int) ((max - damageable.getDamage()) * (1 - (0.15 * Math.pow(0.5, sharpness - 1))));
                        if (nDurability < 1) {
                            inventory.setResult(air);
                            return;
                        }
                        damageable.setDamage(max - nDurability);
                        nResult.setItemMeta(nMeta);
                        inventory.setResult(nResult);
                    }
                    return;
                }
                if (type == Material.WOODEN_SHOVEL
                        || type == Material.STONE_SHOVEL
                        || type == Material.IRON_SHOVEL
                        || type == Material.GOLDEN_SHOVEL
                        || type == Material.DIAMOND_SHOVEL
                        || type == Material.NETHERITE_SHOVEL
                ) {
                    if (nMeta == null || !nMeta.hasEnchant(Enchantment.DIG_SPEED) || nMeta.getEnchantLevel(Enchantment.DIG_SPEED) != sharpness - 1) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult.removeEnchantment(Enchantment.DIG_SPEED);
                    nResult.addUnsafeEnchantment(Enchantment.DIG_SPEED, sharpness);
                    if (nResult.getItemMeta() instanceof Damageable) {
                        nMeta = nResult.getItemMeta();
                        var damageable = (Damageable) nMeta;
                        var max = nResult.getType().getMaxDurability();
                        var nDurability = (int) ((max - damageable.getDamage()) * (1 - (0.15 * Math.pow(0.5, sharpness - 1))));
                        if (nDurability < 1) {
                            inventory.setResult(air);
                            return;
                        }
                        damageable.setDamage(max - nDurability);
                        nResult.setItemMeta(nMeta);
                        inventory.setResult(nResult);
                    }
                    return;
                }
                if (type == Material.WOODEN_SWORD
                        || type == Material.STONE_SWORD
                        || type == Material.IRON_SWORD
                        || type == Material.GOLDEN_SWORD
                        || type == Material.DIAMOND_SWORD
                        || type == Material.NETHERITE_SWORD
                ) {
                    if (nMeta == null || !nMeta.hasEnchant(Enchantment.DAMAGE_ALL) || nMeta.getEnchantLevel(Enchantment.DAMAGE_ALL) != sharpness - 1) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult.removeEnchantment(Enchantment.DAMAGE_ALL);
                    nResult.removeEnchantment(Enchantment.SWEEPING_EDGE);
                    nResult.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, sharpness);
                    nResult.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, sharpness / 2);
                    if (nResult.getItemMeta() instanceof Damageable) {
                        nMeta = nResult.getItemMeta();
                        var damageable = (Damageable) nMeta;
                        var max = nResult.getType().getMaxDurability();
                        var nDurability = (int) ((max - damageable.getDamage()) * (1 - (0.15 * Math.pow(0.5, sharpness - 1))));
                        if (nDurability < 1) {
                            inventory.setResult(air);
                            return;
                        }
                        damageable.setDamage(max - nDurability);
                        nResult.setItemMeta(nMeta);
                        inventory.setResult(nResult);
                    }
                    return;
                }
                if (type == Material.TRIDENT) {
                    if (nMeta == null || !nMeta.hasEnchant(Enchantment.IMPALING) || nMeta.getEnchantLevel(Enchantment.IMPALING) != sharpness - 1) {
                        inventory.setResult(air);
                        return;
                    }
                    nResult.removeEnchantment(Enchantment.IMPALING);
                    nResult.addUnsafeEnchantment(Enchantment.IMPALING, sharpness);
                    if (nResult.getItemMeta() instanceof Damageable) {
                        nMeta = nResult.getItemMeta();
                        var damageable = (Damageable) nMeta;
                        var max = nResult.getType().getMaxDurability();
                        var nDurability = (int) ((max - damageable.getDamage()) * (1 - (0.15 * Math.pow(0.5, sharpness - 1))));
                        if (nDurability < 1) {
                            inventory.setResult(air);
                            return;
                        }
                        damageable.setDamage(max - nDurability);
                        nResult.setItemMeta(nMeta);
                        inventory.setResult(nResult);
                    }
                    return;
                }
            }

            // No repairing
            if (result.getItemMeta() instanceof Damageable) {
                ItemStack tool = null;
                for (var item : inventory.getMatrix()) {
                    if (item == null || item.getType() == Material.AIR) continue;
                    var type = item.getType();
                    if (!(tools.contains(type)
                            || type == Material.SHEARS
                            || type == Material.FISHING_ROD
                            || type == Material.CARROT_ON_A_STICK
                            || type == Material.FLINT_AND_STEEL
                            || type == Material.BOW
                            || type == Material.ELYTRA
                            || type == Material.SHIELD
                            || type == Material.CROSSBOW
                            || type == Material.WARPED_FUNGUS_ON_A_STICK)) break;
                    else {
                        if (tool == null) tool = item;
                        else {
                            if (tool.getType() == item.getType()) {
                                inventory.setResult(air);
                                return;
                            }
                        }
                    }
                }
            }

            // Unbreaking
            if (wooden.contains(result.getType())) {
                for (var item : inventory.getMatrix()) {
                    if (item != null &&
                            !item.getType().equals(Material.AIR) &&
                            !item.getType().equals(Material.ACACIA_PLANKS) &&
                            !item.getType().equals(Material.STICK)) {
                        return;
                    }
                }
                result.addEnchantment(Enchantment.DURABILITY, 1);
                inventory.setResult(result);
                return;
            } else if (stone.contains(result.getType())) {
                for (var item : inventory.getMatrix()) {
                    if (item != null &&
                            !item.getType().equals(Material.AIR) &&
                            !item.getType().equals(Material.BLACKSTONE) &&
                            !item.getType().equals(Material.STICK)) {
                        return;
                    }
                }
                result.addEnchantment(Enchantment.DURABILITY, 2);
                inventory.setResult(result);
                return;
            } else if (diamond.contains(result.getType())) {
                result.addEnchantment(Enchantment.DURABILITY, 2);
                inventory.setResult(result);
                return;
            } else if ((iron.contains(result.getType()) || gold.contains(result.getType()))) {
                var unbreaking = 12;
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
                inventory.setResult(result);
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
            if (result.getType() == Material.POTION && result.getItemMeta() != null && result.getItemMeta().getLore() != null) {
                var lore = result.getItemMeta().getLore();
                for (var content : inventory.getMatrix()) {
                    if (!(content.getItemMeta() instanceof PotionMeta)) continue;
                    var potionType = ((PotionMeta) content.getItemMeta()).getBasePotionData().getType();
                    if (potionType != PotionType.WATER) {
                        inventory.setResult(air);
                        return;
                    }

                    if (isDust(content) &&
                            !(lore.contains(DiplomacyRecipes.getInstance().IRON_SLURRY_LORE) && content.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().IRON_DUST_LORE))
                            && !(lore.contains(DiplomacyRecipes.getInstance().GOLD_SLURRY_LORE) && content.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().GOLD_DUST_LORE))
                            && !(lore.contains(DiplomacyRecipes.getInstance().NETHERITE_SLURRY_LORE) && content.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().NETHERITE_DUST_LORE))
                    ) {
                        inventory.setResult(air);
                        return;

                    }
                }
            }

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
                Arrays.fill(purities, purity);
                setPurity(result, purities);
                if (isRefined) refine(result);
                event.getInventory().setResult(result);
            }

            if (isSlurried(result)) {
                var rMeta = result.getItemMeta();
                if (rMeta == null || rMeta.getLore() == null) {
                    inventory.setResult(air);
                    return;
                }
                var rLore = rMeta.getLore();
                for (var content : inventory.getMatrix()) {
                    if (content == null || content.getType() == Material.AIR) continue;
                    if (content.getType() == Material.POTION) {
                        var potionMeta = ((PotionMeta) content.getItemMeta());
                        if (potionMeta == null) {
                            inventory.setResult(air);
                            return;
                        }
                        var meta = content.getItemMeta();
                        if (meta == null || meta.getLore() == null) {
                            inventory.setResult(air);
                            return;
                        }
                        var lore = meta.getLore();
                        if (!(lore.contains(DiplomacyRecipes.getInstance().IRON_SLURRY_LORE) && rLore.contains(DiplomacyRecipes.getInstance().SLURRIED_IRON_LORE))
                                && !(lore.contains(DiplomacyRecipes.getInstance().GOLD_SLURRY_LORE) && rLore.contains(DiplomacyRecipes.getInstance().SLURRIED_GOLD_LORE))
                                && !(lore.contains(DiplomacyRecipes.getInstance().NETHERITE_SLURRY_LORE) && rLore.contains(DiplomacyRecipes.getInstance().SLURRIED_NETHERITE_LORE))
                        ) {
                            inventory.setResult(air);
                            return;
                        }
                    } else if (isMetal(content)) {
                        if (content.getEnchantments().keySet().size() > 0) {
                            inventory.setResult(air);
                            return;
                        }
                        if (content.getAmount() == 1)
                            result.setItemMeta(content.getItemMeta());
                        else {
                            var item = dropItemFromPurity(content)[1];
                            result.setItemMeta(item.getItemMeta());
                        }
                        var meta = result.getItemMeta();
                        var lore = meta.getLore();
                        lore.add("" + ChatColor.BLACK + ChatColor.MAGIC + (int) (Math.random() * Integer.MAX_VALUE));
                        String name;
                        switch (content.getType()) {
                            default -> {
                                name = "Slurried Iron Ingot";
                                lore.add(DiplomacyRecipes.getInstance().SLURRIED_IRON_LORE);
                            }
                            case GOLD_INGOT -> {
                                name = "Slurried Gold Ingot";
                                lore.add(DiplomacyRecipes.getInstance().SLURRIED_GOLD_LORE);
                            }
                            case NETHERITE_INGOT -> {
                                name = "Slurried Netherite Ingot";
                                lore.add(DiplomacyRecipes.getInstance().SLURRIED_NETHERITE_LORE);
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
                        || (isDust(content) && !(result.getType().equals(Material.POTION) || isFineSandPaper(result)))
                        || isGrenade(content)) {
                    inventory.setResult(air);
                    return;
                }
            }
        }

        @EventHandler
        private void onEntitySpawn(EntitySpawnEvent event) {
            var entity = event.getEntity();
            if (!(entity instanceof LivingEntity) || entity instanceof Player) return;
            if (entity instanceof Piglin //todo piglin brute
                    || entity instanceof Evoker
                    || entity instanceof Pillager
                    || entity instanceof Skeleton
                    || entity instanceof Stray
                    || entity instanceof Vindicator
                    || entity instanceof WitherSkeleton
                    || entity instanceof Zombie
                    || entity instanceof ZombieVillager)
                ((LivingEntity) entity).setCanPickupItems(true);

            var equipment = ((LivingEntity) entity).getEquipment();
            if (equipment == null) return;

            // Piglin / Zombie piglin / Brute
            if (entity.getType() == EntityType.PIGLIN || entity.getType() == EntityType.ZOMBIFIED_PIGLIN) { //todo piglin brute
                ItemStack weapon;
                var r = Math.random();
                if (r < 0.5 || (entity.getType() == EntityType.ZOMBIFIED_PIGLIN))
                    weapon = new ItemStack(Material.GOLDEN_SWORD);
                else weapon = new ItemStack(Material.CROSSBOW);

                if (weapon.getType() == Material.GOLDEN_SWORD) {
                    var max = weapon.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = weapon.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
                    else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DAMAGE_ALL, 9, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.DAMAGE_ALL, 8, true);
                    else if (r < 0.000457247371) meta.addEnchant(Enchantment.DAMAGE_ALL, 7, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
                    else if (r < 0.00411522634) meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
                    else if (r < 0.037037037) meta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                    else if (r < 0.333333333) meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                    else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                    else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                    else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                    else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    weapon.setItemMeta(meta);
                    equipment.setItemInMainHand(weapon);
                } else if (weapon.getType() == Material.CROSSBOW) {
                    var max = weapon.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = weapon.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.QUICK_CHARGE, 5, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.QUICK_CHARGE, 4, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.QUICK_CHARGE, 3, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.QUICK_CHARGE, 2, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.QUICK_CHARGE, 1, true);

                    r = Math.random();
                    if (r < 0.012345679) meta.addEnchant(Enchantment.MULTISHOT, 1, true);

                    weapon.setItemMeta(meta);
                    equipment.setItemInMainHand(weapon);
                } else {
                    equipment.setItemInMainHand(weapon);
                }

                r = Math.random();
                if (r < 0.333333333) {
                    var helmet = new ItemStack(Material.GOLDEN_HELMET);
                    var max = helmet.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = helmet.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                    else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                    else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                    else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                    else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);

                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var fire = 0;
                    var blast = 0;
                    var proj = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.CRIMSON_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        lore.add(layerLores.get(Material.SOUL_SAND));
                        fire++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634) {
                        lore.add(layerLores.get(Material.MAGMA_CREAM));
                        fire += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4) {
                        lore.add(layerLores.get(Material.WITHER_SKELETON_SKULL));
                        fire += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4) {
                        lore.add(layerLores.get(Material.NETHERITE_INGOT));
                        blast += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (fire > 0) meta.addEnchant(Enchantment.PROTECTION_FIRE, fire, true);
                    }
                    helmet.setItemMeta(meta);
                    equipment.setHelmet(helmet);

                } else {
                    equipment.setHelmet(air);
                }

                r = Math.random();
                if (r < 0.333333333) {
                    var chest = new ItemStack(Material.GOLDEN_CHESTPLATE);
                    var max = chest.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = chest.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                    else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                    else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                    else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                    else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);

                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var fire = 0;
                    var blast = 0;
                    var proj = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.CRIMSON_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        lore.add(layerLores.get(Material.SOUL_SAND));
                        fire++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634) {
                        lore.add(layerLores.get(Material.MAGMA_CREAM));
                        fire += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4) {
                        lore.add(layerLores.get(Material.WITHER_SKELETON_SKULL));
                        fire += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4) {
                        lore.add(layerLores.get(Material.NETHERITE_INGOT));
                        blast += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (fire > 0) meta.addEnchant(Enchantment.PROTECTION_FIRE, fire, true);
                    }
                    chest.setItemMeta(meta);
                    equipment.setChestplate(chest);

                } else {
                    equipment.setChestplate(air);
                }

                r = Math.random();
                if (r < 0.333333333) {
                    var leggings = new ItemStack(Material.GOLDEN_LEGGINGS);
                    var max = leggings.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = leggings.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                    else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                    else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                    else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                    else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);

                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var fire = 0;
                    var blast = 0;
                    var proj = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.CRIMSON_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        lore.add(layerLores.get(Material.SOUL_SAND));
                        fire++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634) {
                        lore.add(layerLores.get(Material.MAGMA_CREAM));
                        fire += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4) {
                        lore.add(layerLores.get(Material.WITHER_SKELETON_SKULL));
                        fire += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4) {
                        lore.add(layerLores.get(Material.NETHERITE_INGOT));
                        blast += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (fire > 0) meta.addEnchant(Enchantment.PROTECTION_FIRE, fire, true);
                    }
                    leggings.setItemMeta(meta);
                    equipment.setLeggings(leggings);

                } else {
                    equipment.setLeggings(air);
                }

                r = Math.random();
                if (r < 0.333333333) {
                    var boots = new ItemStack(Material.GOLDEN_BOOTS);
                    var max = boots.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = boots.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                    else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                    else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                    else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                    else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);

                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var fire = 0;
                    var blast = 0;
                    var proj = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.CRIMSON_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        lore.add(layerLores.get(Material.SOUL_SAND));
                        fire++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634) {
                        lore.add(layerLores.get(Material.MAGMA_CREAM));
                        fire += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4) {
                        lore.add(layerLores.get(Material.WITHER_SKELETON_SKULL));
                        fire += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4) {
                        lore.add(layerLores.get(Material.NETHERITE_INGOT));
                        blast += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (fire > 0) meta.addEnchant(Enchantment.PROTECTION_FIRE, fire, true);
                    }
                    boots.setItemMeta(meta);
                    equipment.setBoots(boots);

                } else {
                    equipment.setBoots(air);
                }

                // drowned, zombie, husk, and zombie villager
            } else if (entity.getType() == EntityType.DROWNED || entity.getType() == EntityType.HUSK || entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.ZOMBIE_VILLAGER) { //todo piglin brute
                ItemStack weapon = air;
                Enchantment enchantType = Enchantment.DURABILITY;
                var r = Math.random();
                if (entity.getType() == EntityType.DROWNED) {
                    if (r < 0.002) {
                        weapon = new ItemStack(Material.TRIDENT);
                        enchantType = Enchantment.IMPALING;
                    } else if (r < 0.05)
                        weapon = new ItemStack(Material.FISHING_ROD);
                    else weapon = air;
                } else {
                    r = Math.random();
                    if (r < 0.333333333) {
                        r = Math.random();
                        switch ((int) (r * 22)) {
                            case 0, 1, 2, 3, 4 -> {
                                r = Math.random();
                                if (r < 1.69350878e-5) weapon = new ItemStack(Material.DIAMOND_AXE);
                                else if (r < 0.00015241579) weapon = new ItemStack(Material.IRON_AXE);
                                else if (r < 0.00137174211) weapon = new ItemStack(Material.GOLDEN_AXE);
                                else if (r < 0.037037037) weapon = new ItemStack(Material.STONE_AXE);
                                else weapon = new ItemStack(Material.WOODEN_AXE);
                                enchantType = Enchantment.DAMAGE_ALL;
                            }
                            case 5 -> {
                                var chiselLore = new ArrayList<String>();
                                chiselLore.add(DiplomacyRecipes.getInstance().CHISEL_LORE);
                                ItemMeta meta;
                                r = Math.random();
                                if (r < 1.69350878e-5) {
                                    weapon = new ItemStack(Material.DIAMOND_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Diamond Chisel");
                                    meta.setDisplayName(ChatColor.RESET + "Diamond Chisel");
                                } else if (r < 0.00015241579) {
                                    weapon = new ItemStack(Material.IRON_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Iron Chisel");
                                    meta.setDisplayName(ChatColor.RESET + "Iron Chisel");
                                } else if (r < 0.00137174211) {
                                    weapon = new ItemStack(Material.GOLDEN_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Golden Chisel");
                                    meta.setDisplayName(ChatColor.RESET + "Golden Chisel");
                                } else if (r < 0.037037037) {
                                    weapon = new ItemStack(Material.STONE_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Stone Chisel");
                                    meta.setDisplayName(ChatColor.RESET + "Stone Chisel");
                                } else {
                                    weapon = new ItemStack(Material.WOODEN_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Wooden Chisel");
                                    meta.setDisplayName(ChatColor.RESET + "Wooden Chisel");
                                }
                                meta.setLore(chiselLore);
                                weapon.setItemMeta(meta);
                                enchantType = Enchantment.LOOT_BONUS_BLOCKS;
                            }
                            case 6 -> {
                                r = Math.random();
                                if (r < 1.69350878e-5) {
                                    weapon = new ItemStack(Material.DIAMOND_HOE);
                                } else if (r < 0.00015241579) {
                                    weapon = new ItemStack(Material.IRON_HOE);
                                } else if (r < 0.00137174211) {
                                    weapon = new ItemStack(Material.GOLDEN_HOE);
                                } else if (r < 0.037037037) {
                                    weapon = new ItemStack(Material.STONE_HOE);
                                } else {
                                    weapon = new ItemStack(Material.WOODEN_HOE);
                                }
                                enchantType = Enchantment.DURABILITY;
                            }
                            case 7, 8 -> {
                                var knifeLore = new ArrayList<String>();
                                knifeLore.add(DiplomacyRecipes.getInstance().KNIFE_LORE);
                                ItemMeta meta;
                                r = Math.random();
                                if (r < 1.69350878e-5) {
                                    weapon = new ItemStack(Material.DIAMOND_SWORD);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Diamond Hunting Knife");
                                    meta.setDisplayName(ChatColor.RESET + "Diamond Hunting Knife");
                                } else if (r < 0.00015241579) {
                                    weapon = new ItemStack(Material.IRON_SWORD);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Iron Hunting Knife");
                                    meta.setDisplayName(ChatColor.RESET + "Iron Hunting Knife");
                                } else if (r < 0.00137174211) {
                                    weapon = new ItemStack(Material.GOLDEN_SWORD);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Golden Hunting Knife");
                                    meta.setDisplayName(ChatColor.RESET + "Golden Hunting Knife");
                                } else if (r < 0.037037037) {
                                    weapon = new ItemStack(Material.STONE_SWORD);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Stone Hunting Knife");
                                    meta.setDisplayName(ChatColor.RESET + "Stone Hunting Knife");
                                } else {
                                    weapon = new ItemStack(Material.WOODEN_SWORD);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Wooden Hunting Knife");
                                    meta.setDisplayName(ChatColor.RESET + "Wooden Hunting Knife");
                                }
                                meta.setLore(knifeLore);
                                weapon.setItemMeta(meta);
                                enchantType = Enchantment.LOOT_BONUS_MOBS;
                            }
                            case 9, 10 -> {

                                r = Math.random();
                                if (r < 1.69350878e-5) weapon = new ItemStack(Material.DIAMOND_PICKAXE);
                                else if (r < 0.00015241579) weapon = new ItemStack(Material.IRON_PICKAXE);
                                else if (r < 0.00137174211) weapon = new ItemStack(Material.GOLDEN_PICKAXE);
                                else if (r < 0.037037037) weapon = new ItemStack(Material.STONE_PICKAXE);
                                else weapon = new ItemStack(Material.WOODEN_PICKAXE);
                                enchantType = Enchantment.DIG_SPEED;
                            }
                            case 11 -> {

                                var sawLore = new ArrayList<String>();
                                sawLore.add(DiplomacyRecipes.getInstance().SAW_LORE);
                                ItemMeta meta;
                                r = Math.random();
                                if (r < 1.69350878e-5) {
                                    weapon = new ItemStack(Material.DIAMOND_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Diamond Saw");
                                    meta.setDisplayName(ChatColor.RESET + "Diamond Saw");
                                } else if (r < 0.00015241579) {
                                    weapon = new ItemStack(Material.IRON_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Iron Saw");
                                    meta.setDisplayName(ChatColor.RESET + "Iron Saw");
                                } else if (r < 0.00137174211) {
                                    weapon = new ItemStack(Material.GOLDEN_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Golden Saw");
                                    meta.setDisplayName(ChatColor.RESET + "Golden Saw");
                                } else if (r < 0.037037037) {
                                    weapon = new ItemStack(Material.STONE_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Stone Saw");
                                    meta.setDisplayName(ChatColor.RESET + "Stone Saw");
                                } else {
                                    weapon = new ItemStack(Material.WOODEN_HOE);
                                    meta = weapon.getItemMeta();
                                    meta.setLocalizedName("Wooden Saw");
                                    meta.setDisplayName(ChatColor.RESET + "Wooden Saw");
                                }
                                meta.setLore(sawLore);
                                meta.addEnchant(Enchantment.SILK_TOUCH, 1, true);
                                weapon.setItemMeta(meta);
                                enchantType = Enchantment.DIG_SPEED;
                            }
                            case 12, 13 -> {

                                r = Math.random();
                                if (r < 1.69350878e-5) weapon = new ItemStack(Material.DIAMOND_SHOVEL);
                                else if (r < 0.00015241579) weapon = new ItemStack(Material.IRON_SHOVEL);
                                else if (r < 0.00137174211) weapon = new ItemStack(Material.GOLDEN_SHOVEL);
                                else if (r < 0.037037037) weapon = new ItemStack(Material.STONE_SHOVEL);
                                else weapon = new ItemStack(Material.WOODEN_SHOVEL);
                                enchantType = Enchantment.DIG_SPEED;
                            }
                            case 14, 15, 16, 17, 18, 19, 20, 21, 22 -> {

                                r = Math.random();
                                if (r < 1.69350878e-5) weapon = new ItemStack(Material.DIAMOND_SWORD);
                                else if (r < 0.00015241579) weapon = new ItemStack(Material.IRON_SWORD);
                                else if (r < 0.00137174211) weapon = new ItemStack(Material.GOLDEN_SWORD);
                                else if (r < 0.037037037) weapon = new ItemStack(Material.STONE_SWORD);
                                else weapon = new ItemStack(Material.WOODEN_SWORD);
                                enchantType = Enchantment.DAMAGE_ALL;
                            }
                            default -> {
                                weapon = air;
                                enchantType = Enchantment.DURABILITY;
                            }
                        }
                    }
                }

                if (weapon.getType() == Material.TRIDENT) {
                    var max = weapon.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = weapon.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.IMPALING, 10, true);
                    else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.IMPALING, 9, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.IMPALING, 8, true);
                    else if (r < 0.000457247371) meta.addEnchant(Enchantment.IMPALING, 7, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.IMPALING, 6, true);
                    else if (r < 0.00411522634) meta.addEnchant(Enchantment.IMPALING, 5, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.IMPALING, 4, true);
                    else if (r < 0.037037037) meta.addEnchant(Enchantment.IMPALING, 3, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.IMPALING, 2, true);
                    else if (r < 0.333333333) meta.addEnchant(Enchantment.IMPALING, 1, true);

                    meta.addEnchant(Enchantment.DURABILITY, 3, true);
                    weapon.setItemMeta(meta);
                    equipment.setItemInMainHand(weapon);
                } else if (weapon.getType() == Material.FISHING_ROD) {
                    var max = weapon.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = weapon.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    r = Math.random();
                    if (r < 0.00137174211) meta.addEnchant(Enchantment.LURE, 3, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.LURE, 2, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.LURE, 1, true);

                    weapon.setItemMeta(meta);
                    equipment.setItemInMainHand(weapon);
                } else if ((weapon.getType() == Material.WOODEN_HOE
                        || weapon.getType() == Material.STONE_HOE
                        || weapon.getType() == Material.IRON_HOE
                        || weapon.getType() == Material.GOLDEN_HOE
                        || weapon.getType() == Material.DIAMOND_HOE) && !isChisel(weapon) && !isSaw(weapon)) {

                    var max = weapon.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = weapon.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    weapon.setItemMeta(meta);
                    equipment.setItemInMainHand(weapon);
                } else if (weapon.getType() != Material.AIR) {
                    var max = weapon.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = weapon.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    if (enchantType != Enchantment.DURABILITY) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(enchantType, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(enchantType, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(enchantType, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(enchantType, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(enchantType, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(enchantType, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(enchantType, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(enchantType, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(enchantType, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(enchantType, 1, true);

                        if (r < 0.333333333) {
                            if (weapon.getType() == Material.DIAMOND_AXE
                                    || weapon.getType() == Material.IRON_AXE
                                    || weapon.getType() == Material.GOLDEN_AXE
                                    || weapon.getType() == Material.STONE_AXE
                                    || weapon.getType() == Material.WOODEN_AXE
                            ) {
                                meta.addEnchant(Enchantment.DIG_SPEED, meta.getEnchantLevel(Enchantment.DAMAGE_ALL), true);
                            } else if (
                                    (weapon.getType() == Material.DIAMOND_SWORD
                                            || weapon.getType() == Material.IRON_SWORD
                                            || weapon.getType() == Material.GOLDEN_SWORD
                                            || weapon.getType() == Material.STONE_SWORD
                                            || weapon.getType() == Material.WOODEN_SWORD) && !isHuntingKnife(weapon)
                            ) {
                                if (meta.getEnchantLevel(Enchantment.DAMAGE_ALL) > 1)
                                    meta.addEnchant(Enchantment.SWEEPING_EDGE, meta.getEnchantLevel(Enchantment.DAMAGE_ALL) / 2, true);
                            }
                        }
                    }
                    weapon.setItemMeta(meta);
                    equipment.setItemInMainHand(weapon);
                } else {
                    equipment.setItemInMainHand(weapon);
                }

                var type = weapon.getType();
                if (
                        type == Material.DIAMOND_HOE ||
                                type == Material.DIAMOND_SWORD ||
                                type == Material.DIAMOND_AXE ||
                                type == Material.DIAMOND_PICKAXE ||
                                type == Material.DIAMOND_SHOVEL
                ) {
                    weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
                    equipment.setItemInMainHand(weapon);
                } else if (
                        type == Material.IRON_HOE ||
                                type == Material.IRON_SWORD ||
                                type == Material.IRON_AXE ||
                                type == Material.IRON_PICKAXE ||
                                type == Material.IRON_SHOVEL ||
                                type == Material.GOLDEN_HOE ||
                                type == Material.GOLDEN_SWORD ||
                                type == Material.GOLDEN_AXE ||
                                type == Material.GOLDEN_PICKAXE ||
                                type == Material.GOLDEN_SHOVEL
                ) {
                    r = Math.random();
                    if (r < 1.69350878e-5) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 10);
                    else if (r < 5.08052634e-5) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 9);
                    else if (r < 0.00015241579) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 8);
                    else if (r < 0.000457247371) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 7);
                    else if (r < 0.00137174211) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 6);
                    else if (r < 0.00411522634) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                    else if (r < 0.012345679) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
                    else if (r < 0.037037037) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                    else if (r < 0.111111111) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
                    else if (r < 0.333333333) weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                    equipment.setItemInMainHand(weapon);
                } else if (type == Material.WOODEN_HOE ||
                        type == Material.WOODEN_SWORD ||
                        type == Material.WOODEN_AXE ||
                        type == Material.WOODEN_PICKAXE ||
                        type == Material.WOODEN_SHOVEL) {
                    var biome = event.getLocation().getBlock().getBiome();
                    if (biome == Biome.SAVANNA || biome == Biome.SAVANNA_PLATEAU || biome == Biome.SHATTERED_SAVANNA || biome == Biome.SHATTERED_SAVANNA_PLATEAU) {
                        weapon.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                        equipment.setItemInMainHand(weapon);
                    }
                }


                if (entity.getType() == EntityType.DROWNED) {
                    r = Math.random();
                    if (r < 0.02) equipment.setItemInOffHand(new ItemStack(Material.NAUTILUS_SHELL));
                    else equipment.setItemInOffHand(air);
                }

                r = Math.random();
                if (r < 0.111111111) {
                    ItemStack helmet;

                    if (r < 1.69350878e-5) helmet = new ItemStack(Material.DIAMOND_HELMET);
                    else if (r < 0.00015241579) helmet = new ItemStack(Material.IRON_HELMET);
                    else if (r < 0.00137174211) helmet = new ItemStack(Material.GOLDEN_HELMET);
                    else if (r < 0.037037037) helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                    else helmet = new ItemStack(Material.LEATHER_HELMET);
                    var max = helmet.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = helmet.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    if (helmet.getType() == Material.DIAMOND_HELMET) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (helmet.getType() == Material.IRON_HELMET || helmet.getType() == Material.GOLDEN_HELMET) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    }

                    r = Math.random();
                    if (r < 0.012345679) meta.addEnchant(Enchantment.WATER_WORKER, 1, true);

                    r = Math.random();
                    if (r < 0.00137174211) meta.addEnchant(Enchantment.OXYGEN, 3, true);
                    else if (r < 0.00411522634) meta.addEnchant(Enchantment.OXYGEN, 2, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.OXYGEN, 1, true);


                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var blast = 0;
                    var proj = 0;
                    var melee = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.OAK_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        lore.add(layerLores.get(Material.WHITE_WOOL));
                        melee++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.SLIME_BALL));
                        proj += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.PHANTOM_MEMBRANE));
                        melee += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634 && layers < 4) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.HONEY_BOTTLE));
                        proj += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4) {
                        lore.add(layerLores.get(Material.SCUTE));
                        melee += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4) {
                        lore.add(layerLores.get(Material.NAUTILUS_SHELL));
                        melee += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (melee > 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, melee, true);
                    }
                    helmet.setItemMeta(meta);
                    equipment.setHelmet(helmet);

                } else {
                    equipment.setHelmet(air);
                }

                r = Math.random();
                if (r < 0.111111111) {
                    ItemStack chestplate;

                    if (r < 1.69350878e-5) chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                    else if (r < 0.00015241579) chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                    else if (r < 0.00137174211) chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
                    else if (r < 0.037037037) chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                    else chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
                    var max = chestplate.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = chestplate.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    if (chestplate.getType() == Material.DIAMOND_CHESTPLATE)
                        meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (chestplate.getType() == Material.IRON_CHESTPLATE || chestplate.getType() == Material.GOLDEN_CHESTPLATE) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.THORNS, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.THORNS, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.THORNS, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.THORNS, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.THORNS, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.THORNS, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.THORNS, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.THORNS, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.THORNS, 2, true);
                        else meta.addEnchant(Enchantment.THORNS, 1, true);
                    }


                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var blast = 0;
                    var proj = 0;
                    var melee = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.OAK_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        lore.add(layerLores.get(Material.WHITE_WOOL));
                        melee++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.SLIME_BALL));
                        proj += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.PHANTOM_MEMBRANE));
                        melee += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634 && layers < 4) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.HONEY_BOTTLE));
                        proj += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4) {
                        lore.add(layerLores.get(Material.SCUTE));
                        melee += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4) {
                        lore.add(layerLores.get(Material.NAUTILUS_SHELL));
                        melee += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (melee > 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, melee, true);
                    }
                    chestplate.setItemMeta(meta);
                    equipment.setChestplate(chestplate);

                } else {
                    equipment.setChestplate(air);
                }

                r = Math.random();
                if (r < 0.111111111) {
                    ItemStack leggings;

                    if (r < 1.69350878e-5) leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                    else if (r < 0.00015241579) leggings = new ItemStack(Material.IRON_LEGGINGS);
                    else if (r < 0.00137174211) leggings = new ItemStack(Material.GOLDEN_LEGGINGS);
                    else if (r < 0.037037037) leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                    else leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                    var max = leggings.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = leggings.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    if (leggings.getType() == Material.DIAMOND_LEGGINGS)
                        meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (leggings.getType() == Material.IRON_LEGGINGS || leggings.getType() == Material.GOLDEN_LEGGINGS) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    }

                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var blast = 0;
                    var proj = 0;
                    var melee = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.OAK_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        lore.add(layerLores.get(Material.WHITE_WOOL));
                        melee++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.SLIME_BALL));
                        proj += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.PHANTOM_MEMBRANE));
                        melee += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634 && layers < 4) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.HONEY_BOTTLE));
                        proj += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4) {
                        lore.add(layerLores.get(Material.SCUTE));
                        melee += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4) {
                        lore.add(layerLores.get(Material.NAUTILUS_SHELL));
                        melee += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (melee > 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, melee, true);
                    }
                    leggings.setItemMeta(meta);
                    equipment.setLeggings(leggings);

                } else {
                    equipment.setLeggings(air);
                }

                r = Math.random();
                if (r < 0.111111111) {
                    ItemStack boots;

                    if (r < 1.69350878e-5) boots = new ItemStack(Material.DIAMOND_BOOTS);
                    else if (r < 0.00015241579) boots = new ItemStack(Material.IRON_BOOTS);
                    else if (r < 0.00137174211) boots = new ItemStack(Material.GOLDEN_BOOTS);
                    else if (r < 0.037037037) boots = new ItemStack(Material.CHAINMAIL_BOOTS);
                    else boots = new ItemStack(Material.LEATHER_BOOTS);
                    var max = boots.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = boots.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    if (boots.getType() == Material.DIAMOND_BOOTS) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (boots.getType() == Material.IRON_BOOTS || boots.getType() == Material.GOLDEN_BOOTS) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.PROTECTION_FALL, 7, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.PROTECTION_FALL, 6, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.PROTECTION_FALL, 5, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.PROTECTION_FALL, 4, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.PROTECTION_FALL, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.PROTECTION_FALL, 2, true);
                        else meta.addEnchant(Enchantment.PROTECTION_FALL, 1, true);
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        r = Math.random();
                        if (r < 0.000457247371) meta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);
                        else meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, true);
                    }

                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var blast = 0;
                    var proj = 0;
                    var melee = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.OAK_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        lore.add(layerLores.get(Material.WHITE_WOOL));
                        melee++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.SLIME_BALL));
                        proj += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679) {
                        lore.add(layerLores.get(Material.PHANTOM_MEMBRANE));
                        melee += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634 && layers < 4) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.HONEY_BOTTLE));
                        proj += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4) {
                        lore.add(layerLores.get(Material.SCUTE));
                        melee += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4) {
                        lore.add(layerLores.get(Material.NAUTILUS_SHELL));
                        melee += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (melee > 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, melee, true);
                    }
                    boots.setItemMeta(meta);
                    equipment.setBoots(boots);

                } else {
                    equipment.setBoots(air);
                }

                // Skeleton, wither skeleton, stray
            } else if (entity.getType() == EntityType.SKELETON || entity.getType() == EntityType.WITHER_SKELETON || entity.getType() == EntityType.STRAY) {
                var environment = event.getLocation().getWorld().getEnvironment();
                var r = Math.random();
                var weapon = air;
                if (entity.getType() == EntityType.WITHER_SKELETON) {
                    if (r < 1.69350878e-5) weapon = new ItemStack(Material.DIAMOND_SWORD);
                    else if (r < 0.00015241579) weapon = new ItemStack(Material.IRON_SWORD);
                    else if (r < 0.00137174211) weapon = new ItemStack(Material.GOLDEN_SWORD);
                    else if (r < 0.8) weapon = new ItemStack(Material.STONE_SWORD);
                    else weapon = new ItemStack(Material.BOW);
                } else {
                    if (r < 1.69350878e-5 && environment != World.Environment.NETHER)
                        weapon = new ItemStack(Material.DIAMOND_SWORD);
                    else if (r < 0.00015241579) weapon = new ItemStack(Material.IRON_SWORD);
                    else if (r < 0.00137174211) weapon = new ItemStack(Material.GOLDEN_SWORD);
                    else if (r < 0.037037037) weapon = new ItemStack(Material.STONE_SWORD);
                    else if (r < 0.2) {
                        if (environment == World.Environment.NETHER) weapon = new ItemStack(Material.STONE_SWORD);
                        else weapon = new ItemStack(Material.WOODEN_SWORD);
                    } else weapon = new ItemStack(Material.BOW);
                }

                if (weapon.getType() == Material.BOW) {
                    var max = weapon.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = weapon.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);
                    weapon.setItemMeta(meta);
                    equipment.setItemInMainHand(weapon);
                } else {
                    var max = weapon.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = weapon.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
                    else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DAMAGE_ALL, 9, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.DAMAGE_ALL, 8, true);
                    else if (r < 0.000457247371) meta.addEnchant(Enchantment.DAMAGE_ALL, 7, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
                    else if (r < 0.00411522634) meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
                    else if (r < 0.037037037) meta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                    else if (r < 0.333333333) meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);

                    if (meta.getEnchantLevel(Enchantment.DAMAGE_ALL) > 1)
                        meta.addEnchant(Enchantment.SWEEPING_EDGE, meta.getEnchantLevel(Enchantment.DAMAGE_ALL) / 2, true);

                    if (weapon.getType() == Material.IRON_SWORD || weapon.getType() == Material.GOLDEN_SWORD) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    } else if (weapon.getType() == Material.DIAMOND_SWORD)
                        meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (weapon.getType() == Material.STONE_SWORD && environment == World.Environment.NETHER)
                        meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (weapon.getType() == Material.WOODEN_SWORD) {
                        var biome = event.getLocation().getBlock().getBiome();
                        if (biome == Biome.SAVANNA || biome == Biome.SAVANNA_PLATEAU || biome == Biome.SHATTERED_SAVANNA || biome == Biome.SHATTERED_SAVANNA_PLATEAU) {
                            meta.addEnchant(Enchantment.DURABILITY, 1, true);
                        }
                    }
                    weapon.setItemMeta(meta);
                    equipment.setItemInMainHand(weapon);
                }


                r = Math.random();
                if (r < 0.111111111) {
                    ItemStack helmet;

                    if (r < 1.69350878e-5) helmet = new ItemStack(Material.DIAMOND_HELMET);
                    else if (r < 0.00015241579) helmet = new ItemStack(Material.IRON_HELMET);
                    else if (r < 0.00137174211) helmet = new ItemStack(Material.GOLDEN_HELMET);
                    else if (r < 0.037037037 || environment == World.Environment.NETHER)
                        helmet = new ItemStack(Material.CHAINMAIL_HELMET);
                    else helmet = new ItemStack(Material.LEATHER_HELMET);
                    var max = helmet.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = helmet.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    if (helmet.getType() == Material.DIAMOND_HELMET) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (helmet.getType() == Material.IRON_HELMET || helmet.getType() == Material.GOLDEN_HELMET) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    }

                    if (environment == World.Environment.NORMAL) {
                        r = Math.random();
                        if (r < 0.012345679) meta.addEnchant(Enchantment.WATER_WORKER, 1, true);

                        r = Math.random();
                        if (r < 0.00137174211) meta.addEnchant(Enchantment.OXYGEN, 3, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.OXYGEN, 2, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.OXYGEN, 1, true);
                    }

                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var blast = 0;
                    var proj = 0;
                    var melee = 0;
                    var fire = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.OAK_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.333333333 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.SOUL_SAND));
                        fire++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.WHITE_WOOL));
                        melee++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679 && environment == World.Environment.NORMAL && layers < 4) {
                        lore.add(layerLores.get(Material.SLIME_BALL));
                        proj += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679 && environment == World.Environment.NORMAL && layers < 4) {
                        lore.add(layerLores.get(Material.PHANTOM_MEMBRANE));
                        melee += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634 && layers < 4) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.HONEY_BOTTLE));
                        proj += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.SCUTE));
                        melee += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.MAGMA_CREAM));
                        fire += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.NAUTILUS_SHELL));
                        melee += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.NETHERITE_INGOT));
                        blast += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.WITHER_SKELETON_SKULL));
                        fire += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (melee > 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, melee, true);
                        if (fire > 0) meta.addEnchant(Enchantment.PROTECTION_FIRE, fire, true);
                    }
                    helmet.setItemMeta(meta);
                    equipment.setHelmet(helmet);

                } else {
                    equipment.setHelmet(air);
                }

                r = Math.random();
                if (r < 0.111111111) {
                    ItemStack chestplate;

                    if (r < 1.69350878e-5) chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE);
                    else if (r < 0.00015241579) chestplate = new ItemStack(Material.IRON_CHESTPLATE);
                    else if (r < 0.00137174211) chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
                    else if (r < 0.037037037 || environment == World.Environment.NETHER)
                        chestplate = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                    else chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
                    var max = chestplate.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = chestplate.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    if (chestplate.getType() == Material.DIAMOND_CHESTPLATE)
                        meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (chestplate.getType() == Material.IRON_CHESTPLATE || chestplate.getType() == Material.GOLDEN_CHESTPLATE) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    }

                    if (environment == World.Environment.NORMAL) {
                        r = Math.random();
                        if (r < 0.111111111) {
                            if (r < 1.69350878e-5) meta.addEnchant(Enchantment.THORNS, 10, true);
                            else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.THORNS, 9, true);
                            else if (r < 0.00015241579) meta.addEnchant(Enchantment.THORNS, 8, true);
                            else if (r < 0.000457247371) meta.addEnchant(Enchantment.THORNS, 7, true);
                            else if (r < 0.00137174211) meta.addEnchant(Enchantment.THORNS, 6, true);
                            else if (r < 0.00411522634) meta.addEnchant(Enchantment.THORNS, 5, true);
                            else if (r < 0.012345679) meta.addEnchant(Enchantment.THORNS, 4, true);
                            else if (r < 0.037037037) meta.addEnchant(Enchantment.THORNS, 3, true);
                            else if (r < 0.111111111) meta.addEnchant(Enchantment.THORNS, 2, true);
                            else meta.addEnchant(Enchantment.THORNS, 1, true);
                        }
                    }


                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var blast = 0;
                    var proj = 0;
                    var melee = 0;
                    var fire = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.OAK_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.333333333 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.SOUL_SAND));
                        fire++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.WHITE_WOOL));
                        melee++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679 && environment == World.Environment.NORMAL && layers < 4) {
                        lore.add(layerLores.get(Material.SLIME_BALL));
                        proj += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679 && environment == World.Environment.NORMAL && layers < 4) {
                        lore.add(layerLores.get(Material.PHANTOM_MEMBRANE));
                        melee += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634 && layers < 4) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.HONEY_BOTTLE));
                        proj += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.SCUTE));
                        melee += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.MAGMA_CREAM));
                        fire += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.NAUTILUS_SHELL));
                        melee += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.NETHERITE_INGOT));
                        blast += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.WITHER_SKELETON_SKULL));
                        fire += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (melee > 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, melee, true);
                        if (fire > 0) meta.addEnchant(Enchantment.PROTECTION_FIRE, fire, true);
                    }
                    chestplate.setItemMeta(meta);
                    equipment.setChestplate(chestplate);

                } else {
                    equipment.setChestplate(air);
                }

                r = Math.random();
                if (r < 0.111111111) {
                    ItemStack leggings;

                    if (r < 1.69350878e-5) leggings = new ItemStack(Material.DIAMOND_LEGGINGS);
                    else if (r < 0.00015241579) leggings = new ItemStack(Material.IRON_LEGGINGS);
                    else if (r < 0.00137174211) leggings = new ItemStack(Material.GOLDEN_LEGGINGS);
                    else if (r < 0.037037037 || environment == World.Environment.NETHER)
                        leggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
                    else leggings = new ItemStack(Material.LEATHER_LEGGINGS);
                    var max = leggings.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = leggings.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    if (leggings.getType() == Material.DIAMOND_LEGGINGS)
                        meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (leggings.getType() == Material.IRON_LEGGINGS || leggings.getType() == Material.GOLDEN_LEGGINGS) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    }

                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var blast = 0;
                    var proj = 0;
                    var melee = 0;
                    var fire = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.OAK_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.333333333 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.SOUL_SAND));
                        fire++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.WHITE_WOOL));
                        melee++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679 && environment == World.Environment.NORMAL && layers < 4) {
                        lore.add(layerLores.get(Material.SLIME_BALL));
                        proj += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679 && environment == World.Environment.NORMAL && layers < 4) {
                        lore.add(layerLores.get(Material.PHANTOM_MEMBRANE));
                        melee += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634 && layers < 4) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.HONEY_BOTTLE));
                        proj += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.SCUTE));
                        melee += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.MAGMA_CREAM));
                        fire += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.NAUTILUS_SHELL));
                        melee += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.NETHERITE_INGOT));
                        blast += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.WITHER_SKELETON_SKULL));
                        fire += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (melee > 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, melee, true);
                        if (fire > 0) meta.addEnchant(Enchantment.PROTECTION_FIRE, fire, true);
                    }
                    leggings.setItemMeta(meta);
                    equipment.setLeggings(leggings);

                } else {
                    equipment.setLeggings(air);
                }

                r = Math.random();
                if (r < 0.111111111) {
                    ItemStack boots;

                    if (r < 1.69350878e-5) boots = new ItemStack(Material.DIAMOND_BOOTS);
                    else if (r < 0.00015241579) boots = new ItemStack(Material.IRON_BOOTS);
                    else if (r < 0.00137174211) boots = new ItemStack(Material.GOLDEN_BOOTS);
                    else if (r < 0.037037037 || environment == World.Environment.NETHER)
                        boots = new ItemStack(Material.CHAINMAIL_BOOTS);
                    else boots = new ItemStack(Material.LEATHER_BOOTS);
                    var max = boots.getType().getMaxDurability();
                    var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                    var meta = boots.getItemMeta();
                    ((Damageable) meta).setDamage(max - durability);

                    if (boots.getType() == Material.DIAMOND_BOOTS) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                    else if (boots.getType() == Material.IRON_BOOTS || boots.getType() == Material.GOLDEN_BOOTS) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                        else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                        else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                        else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                        else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    }

                    r = Math.random();
                    if (r < 0.111111111) {
                        r = Math.random();
                        if (r < 1.69350878e-5) meta.addEnchant(Enchantment.PROTECTION_FALL, 7, true);
                        else if (r < 0.00015241579) meta.addEnchant(Enchantment.PROTECTION_FALL, 6, true);
                        else if (r < 0.000457247371) meta.addEnchant(Enchantment.PROTECTION_FALL, 5, true);
                        else if (r < 0.00411522634) meta.addEnchant(Enchantment.PROTECTION_FALL, 4, true);
                        else if (r < 0.012345679) meta.addEnchant(Enchantment.PROTECTION_FALL, 3, true);
                        else if (r < 0.111111111) meta.addEnchant(Enchantment.PROTECTION_FALL, 2, true);
                        else meta.addEnchant(Enchantment.PROTECTION_FALL, 1, true);
                    }

                    if (environment == World.Environment.NORMAL) {
                        r = Math.random();
                        if (r < 0.012345679) {
                            r = Math.random();
                            if (r < 0.000457247371) meta.addEnchant(Enchantment.DEPTH_STRIDER, 3, true);
                            else if (r < 0.012345679) meta.addEnchant(Enchantment.DEPTH_STRIDER, 2, true);
                            else meta.addEnchant(Enchantment.DEPTH_STRIDER, 1, true);
                        }
                    }

                    var lore = new ArrayList<String>();
                    lore.add("");
                    var layers = 0;
                    var blast = 0;
                    var proj = 0;
                    var melee = 0;
                    var fire = 0;

                    r = Math.random();
                    if (r < 0.333333333) {
                        lore.add(layerLores.get(Material.OAK_PLANKS));
                        proj++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.333333333 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.SOUL_SAND));
                        fire++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.111111111 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.WHITE_WOOL));
                        melee++;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679 && environment == World.Environment.NORMAL && layers < 4) {
                        lore.add(layerLores.get(Material.SLIME_BALL));
                        proj += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.012345679 && environment == World.Environment.NORMAL && layers < 4) {
                        lore.add(layerLores.get(Material.PHANTOM_MEMBRANE));
                        melee += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00411522634 && layers < 4) {
                        lore.add(layerLores.get(Material.OBSIDIAN));
                        blast += 2;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4) {
                        lore.add(layerLores.get(Material.CRYING_OBSIDIAN));
                        blast += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.000457247371 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.HONEY_BOTTLE));
                        proj += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.SCUTE));
                        melee += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 0.00015241579 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.MAGMA_CREAM));
                        fire += 3;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NORMAL) {
                        lore.add(layerLores.get(Material.NAUTILUS_SHELL));
                        melee += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.NETHERITE_INGOT));
                        blast += 4;
                        layers++;
                    }

                    r = Math.random();
                    if (r < 1.69350878e-5 && layers < 4 && environment == World.Environment.NETHER) {
                        lore.add(layerLores.get(Material.WITHER_SKELETON_SKULL));
                        fire += 4;
                        layers++;
                    }

                    if (layers > 0) {
                        meta.setLore(lore);
                        if (blast > 0) meta.addEnchant(Enchantment.PROTECTION_EXPLOSIONS, blast, true);
                        if (proj > 0) meta.addEnchant(Enchantment.PROTECTION_PROJECTILE, proj, true);
                        if (melee > 0) meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, melee, true);
                        if (fire > 0) meta.addEnchant(Enchantment.PROTECTION_FIRE, fire, true);
                    }
                    boots.setItemMeta(meta);
                    equipment.setBoots(boots);

                } else {
                    equipment.setBoots(air);
                }

            } else if (entity.getType() == EntityType.VINDICATOR) {
                var r = Math.random();
                var weapon = equipment.getItemInMainHand();

                var max = weapon.getType().getMaxDurability();
                var durability = (int) (Math.pow(Math.random(), 5) * max) + 1;
                var meta = weapon.getItemMeta();
                ((Damageable) meta).setDamage(max - durability);


                if (weapon.getType() == Material.CROSSBOW) {

                    r = Math.random();
                    if (r < 1.69350878e-5) meta.addEnchant(Enchantment.QUICK_CHARGE, 5, true);
                    else if (r < 0.00015241579) meta.addEnchant(Enchantment.QUICK_CHARGE, 4, true);
                    else if (r < 0.00137174211) meta.addEnchant(Enchantment.QUICK_CHARGE, 3, true);
                    else if (r < 0.012345679) meta.addEnchant(Enchantment.QUICK_CHARGE, 2, true);
                    else if (r < 0.111111111) meta.addEnchant(Enchantment.QUICK_CHARGE, 1, true);

                    r = Math.random();
                    if (r < 0.012345679) meta.addEnchant(Enchantment.MULTISHOT, 1, true);

                    weapon.setItemMeta(meta);
                } else if (weapon.getType() != Material.AIR) {
                    r = Math.random();
                    if (r < 1.69350878e-5) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 10, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 10, true);
                    } else if (r < 5.08052634e-5) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 9, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 9, true);
                    } else if (r < 0.00015241579) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 8, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 8, true);
                    } else if (r < 0.000457247371) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 7, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 7, true);
                    } else if (r < 0.00137174211) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 6, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 6, true);
                    } else if (r < 0.00411522634) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 5, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 5, true);
                    } else if (r < 0.012345679) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 4, true);
                    } else if (r < 0.037037037) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 3, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 3, true);
                    } else if (r < 0.111111111) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 2, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 2, true);
                    } else if (r < 0.333333333) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
                        meta.addEnchant(Enchantment.DIG_SPEED, 1, true);
                    }
                    weapon.setItemMeta(meta);
                }

                r = Math.random();
                if (r < 1.69350878e-5) meta.addEnchant(Enchantment.DURABILITY, 10, true);
                else if (r < 5.08052634e-5) meta.addEnchant(Enchantment.DURABILITY, 9, true);
                else if (r < 0.00015241579) meta.addEnchant(Enchantment.DURABILITY, 8, true);
                else if (r < 0.000457247371) meta.addEnchant(Enchantment.DURABILITY, 7, true);
                else if (r < 0.00137174211) meta.addEnchant(Enchantment.DURABILITY, 6, true);
                else if (r < 0.00411522634) meta.addEnchant(Enchantment.DURABILITY, 5, true);
                else if (r < 0.012345679) meta.addEnchant(Enchantment.DURABILITY, 4, true);
                else if (r < 0.037037037) meta.addEnchant(Enchantment.DURABILITY, 3, true);
                else if (r < 0.111111111) meta.addEnchant(Enchantment.DURABILITY, 2, true);
                else if (r < 0.333333333) meta.addEnchant(Enchantment.DURABILITY, 1, true);

                weapon.setItemMeta(meta);
                equipment.setItemInMainHand(weapon);
            }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onEntityDeath(EntityDeathEvent event) {
            if (event.getEntity().getType() == EntityType.ENDER_DRAGON || event.getEntity().getType() == EntityType.WITHER)
                return;
            var drops = event.getDrops();
            var nDrops = new ArrayList<>(drops);
            drops.clear();

            var entity = event.getEntity();
            var last = entity.getLastDamageCause();

            if (last instanceof EntityDamageByEntityEvent) {
                var killer = ((EntityDamageByEntityEvent) entity.getLastDamageCause()).getDamager();
                if (killer == null || !(killer instanceof Player || (killer instanceof Tameable && ((Tameable) killer).isTamed()) || killer instanceof Projectile))
                    return;
                if (killer instanceof Projectile) {
                    var projectile = ((Projectile) killer);
                    var shooter = projectile.getShooter();
                    if (shooter instanceof Player) killer = ((Player) shooter);
                    else return;
                }
            } //todo allow all deaths to drop in ecology update


            if (entity instanceof InventoryHolder) {
                var holder = (InventoryHolder) entity;
                for (var item : holder.getInventory()) {
                    drops.add(item);
                }
            } else {
                var equipment = entity.getEquipment();
                if (equipment != null) {
                    if (equipment.getBoots() != null && equipment.getBoots().getType() != Material.AIR)
                        drops.add(equipment.getBoots());
                    if (equipment.getChestplate() != null && equipment.getChestplate().getType() != Material.AIR)
                        drops.add(equipment.getChestplate());
                    if (equipment.getHelmet() != null && equipment.getBoots().getType() != Material.AIR)
                        drops.add(equipment.getHelmet());
                    if (equipment.getItemInMainHand() != null && entity.getType() != EntityType.WITCH && equipment.getItemInMainHand().getType() != Material.AIR)
                        drops.add(equipment.getItemInMainHand());
                    if (equipment.getItemInOffHand() != null && equipment.getItemInOffHand().getType() != Material.AIR)
                        drops.add(equipment.getItemInOffHand());
                    if (equipment.getLeggings() != null && equipment.getLeggings().getType() != Material.AIR)
                        drops.add(equipment.getLeggings());
                }
            }

            if ((!(entity instanceof Ageable) || ((Ageable) entity).isAdult()) && !(entity instanceof Player)) {
                var lootDamage = Entities.getInstance().getLootDamage(entity);
                switch (entity.getType()) {
                    case BAT -> {
                        if (Math.random() < lootDamage) drops.add(new ItemStack(Material.RABBIT_HIDE));
                    }
                    case CAT, OCELOT -> {
                        if (Math.random() < lootDamage) drops.add(new ItemStack(Material.PORKCHOP));
                    }
                    case CHICKEN -> {
                        if (Math.pow(Math.random(), 7.0) < lootDamage) drops.add(new ItemStack(Material.CHICKEN));
                        var squared = Math.pow(lootDamage, 2.25);
                        for (int i = 0; i < 8; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.FEATHER));
                        }
                    }
                    case COD -> {
                        if (Math.pow(Math.random(), 5.0) < lootDamage) drops.add(new ItemStack(Material.COD));
                    }
                    case DONKEY -> {
                        var squared = Math.pow(lootDamage, 2);
                        for (int i = 0; i < 4; i++)
                            if (Math.random() < squared)
                                drops.add(new ItemStack(Material.MUTTON));
                        for (int i = 0; i < 7; i++)
                            if (Math.random() < squared)
                                drops.add(new ItemStack(Material.LEATHER));
                        for (int i = 0; i < 3; i++)
                            if (Math.random() < squared)
                                drops.add(new ItemStack(Material.BONE));
                    }
                    case FOX -> {
                        var squared = Math.pow(lootDamage, 2);
                        for (int i = 0; i < 2; i++)
                            if (Math.random() < squared)
                                drops.add(new ItemStack(Material.MUTTON));
                        if (Math.random() < squared)
                            drops.add(new ItemStack(Material.BONE));
                    }
                    case PARROT -> {
                        var squared = Math.pow(lootDamage, 1.5);
                        for (int i = 0; i < 3; i++)
                            if (Math.random() < squared)
                                drops.add(new ItemStack(Material.FEATHER));
                    }
                    case HORSE -> {
                        for (int i = 0; i < 6; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.MUTTON));
                        for (int i = 0; i < 11; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.LEATHER));
                        for (int i = 0; i < 5; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.BONE));
                    }
                    case MUSHROOM_COW -> {
                        var squared = Math.pow(lootDamage, 1.5);
                        for (int i = 0; i < 4; i++)
                            if (Math.random() < squared)
                                drops.add(new ItemStack(Material.BEEF));

                        ItemStack type;
                        if (((MushroomCow) entity).getVariant() == MushroomCow.Variant.BROWN)
                            type = new ItemStack(Material.BROWN_MUSHROOM);
                        else type = new ItemStack(Material.RED_MUSHROOM);
                        for (int i = 0; i < 3; i++)
                            if (Math.random() < squared)
                                drops.add(type);
                        for (int i = 0; i < 6; i++)
                            if (Math.random() < squared)
                                drops.add(new ItemStack(Material.LEATHER));
                        for (int i = 0; i < 4; i++)
                            if (Math.random() < squared)
                                drops.add(new ItemStack(Material.BONE));
                    }
                    case MULE -> {
                        for (int i = 0; i < 5; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.MUTTON));
                        for (int i = 0; i < 9; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.LEATHER));
                        for (int i = 0; i < 3; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.BONE));
                    }
                    case PIG -> {
                        for (int i = 0; i < 8; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.PORKCHOP));
                        for (int i = 0; i < 4; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.BONE));
                    }
                    case PIGLIN -> {
                        for (int i = 0; i < 6; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.PORKCHOP));
                        for (int i = 0; i < 2; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.BONE));
                    }
                    case RABBIT -> {
                        if (Math.pow(Math.random(), 4) < lootDamage)
                            drops.add(new ItemStack(Material.RABBIT));
                        for (int i = 0; i < 4; i++) {
                            if (Math.random() < Math.pow(lootDamage, 9)) drops.add(new ItemStack(Material.RABBIT_FOOT));
                        }
                        for (int i = 0; i < 3; i++)
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.RABBIT_HIDE));
                    }
                    case SALMON -> {
                        if (Math.pow(Math.random(), 5.0) < lootDamage) drops.add(new ItemStack(Material.SALMON));
                    }
                    case SHEEP -> {
                        var squared = Math.pow(lootDamage, 1.2);
                        for (int i = 0; i < 6; i++)
                            if (Math.random() < squared)
                                drops.add(new ItemStack(Material.MUTTON));
                        if (!((Sheep) entity).isSheared()) {
                            var color = ((Sheep) entity).getColor();
                            if (color != null) {
                                Material wool = switch (color) {
                                    case WHITE -> Material.WHITE_WOOL;
                                    case ORANGE -> Material.ORANGE_WOOL;
                                    case MAGENTA -> Material.MAGENTA_WOOL;
                                    case LIGHT_BLUE -> Material.LIGHT_BLUE_WOOL;
                                    case YELLOW -> Material.YELLOW_WOOL;
                                    case LIME -> Material.LIME_WOOL;
                                    case PINK -> Material.PINK_WOOL;
                                    case GRAY -> Material.GRAY_WOOL;
                                    case LIGHT_GRAY -> Material.LIGHT_GRAY_WOOL;
                                    case CYAN -> Material.CYAN_WOOL;
                                    case PURPLE -> Material.PURPLE_WOOL;
                                    case BLUE -> Material.BLUE_WOOL;
                                    case BROWN -> Material.BROWN_WOOL;
                                    case GREEN -> Material.GREEN_WOOL;
                                    case RED -> Material.RED_WOOL;
                                    case BLACK -> Material.BLACK_WOOL;
                                };
                                for (int i = 0; i < 3; i++) {
                                    if (Math.random() < squared) drops.add(new ItemStack(wool));
                                }
                            }
                        }
                    }
                    case SKELETON_HORSE -> {
                        for (int i = 0; i < 12; i++)
                            if (Math.random() < Math.pow(lootDamage, 2))
                                drops.add(new ItemStack(Material.BONE));
                    }
                    case SQUID -> {
                        if (Math.random() < lootDamage)
                            drops.add(new ItemStack(Material.INK_SAC));
                        for (int i = 0; i < 2; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.MUTTON));
                        }
                    }
                    case STRIDER -> {
                        var squared = Math.pow(lootDamage, 2);
                        for (int i = 0; i < 2; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.LEATHER));
                        }
                        for (int i = 0; i < 8; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.STRING));
                        }
                    }
                    case TROPICAL_FISH -> {
                        if (Math.pow(Math.random(), 5.0) < lootDamage) drops.add(new ItemStack(Material.TROPICAL_FISH));
                    }
                    case TURTLE -> {
                        if (Math.random() < Math.pow(lootDamage, 4.0)) drops.add(new ItemStack(Material.SCUTE));
                        for (int i = 0; i < 2; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.MUTTON));
                        }
                        for (int i = 0; i < 2; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.SEAGRASS));
                        }
                    }
                    case POLAR_BEAR -> {
                        var squared = Math.pow(lootDamage, 1.5);
                        for (int i = 0; i < 9; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.BEEF));
                        }
                        for (int i = 0; i < 5; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.BONE));
                        }
                        for (int i = 0; i < 11; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.LEATHER));
                        }
                    }
                    case CAVE_SPIDER, SPIDER -> {
                        var squared = Math.pow(lootDamage, 2);
                        for (int i = 0; i < 8; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.SPIDER_EYE));
                        }
                        for (int i = 0; i < 10; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.STRING));
                        }
                    }
                    case DOLPHIN -> {
                        var squared = Math.pow(lootDamage, 2);
                        for (int i = 0; i < 4; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.LEATHER));
                        }
                        for (int i = 0; i < 4; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.PORKCHOP));
                        }
                        for (int i = 0; i < 3; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.BONE));
                        }
                    }
                    case IRON_GOLEM -> {
                        for (int i = 0; i < 12; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.IRON_INGOT));
                        }
                        if (Math.random() < lootDamage) drops.add(new ItemStack(Material.POPPY));
                    }
                    case LLAMA, TRADER_LLAMA -> {
                        for (int i = 0; i < 5; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.LEATHER));
                        }
                        for (int i = 0; i < 4; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.MUTTON));
                        }
                        for (int i = 0; i < 3; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.BONE));
                        }
                    }
                    case PANDA -> {
                        for (int i = 0; i < 9; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.LEATHER));
                        }
                        for (int i = 0; i < 9; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.PORKCHOP));
                        }
                        for (int i = 0; i < 5; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.BONE));
                        }
                    }
                    case PUFFERFISH -> {
                        if (Math.pow(Math.random(), 4.0) < lootDamage) drops.add(new ItemStack(Material.PUFFERFISH));
                    }
                    case WOLF -> {
                        for (int i = 0; i < 2; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.MUTTON));
                        }
                        for (int i = 0; i < 4; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.LEATHER));
                        }
                    }
                    case ZOMBIFIED_PIGLIN -> {
                        for (int i = 0; i < 6; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.ROTTEN_FLESH));
                        }
                        for (int i = 0; i < 2; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.BONE));
                        }
                    }
                    case BLAZE -> {
                        var squared = Math.pow(lootDamage, 2);
                        for (int i = 0; i < 12; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.BLAZE_ROD));
                        }
                    }
                    case CREEPER -> {
                        for (int i = 0; i < 7; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.GUNPOWDER));
                        }
                        if (Math.random() < Math.pow(lootDamage, 8)) drops.add(new ItemStack(Material.CREEPER_HEAD));
                    }
                    case DROWNED, ZOMBIE, ZOMBIE_VILLAGER, HUSK -> {
                        for (int i = 0; i < 12; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.ROTTEN_FLESH));
                        }
                        for (int i = 0; i < 4; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.BONE));
                        }
                        if (entity.getType() == EntityType.ZOMBIE) {
                            if (Math.random() < Math.pow(lootDamage, 8)) drops.add(new ItemStack(Material.ZOMBIE_HEAD));
                        }
                    }
                    case ELDER_GUARDIAN -> {
                        if (Math.random() < lootDamage)
                            drops.add(new ItemStack(Material.HEART_OF_THE_SEA));
                        if (Math.pow(Math.random(), 5) < lootDamage) drops.add(new ItemStack(Material.WET_SPONGE));

                        for (int i = 0; i < 5; i++) {
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.PRISMARINE_SHARD));
                        }
                        for (int i = 0; i < 5; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.PRISMARINE_CRYSTALS));
                        }
                    }
                    case GUARDIAN -> {
                        if (Math.random() < Math.pow(lootDamage, 3)) drops.add(new ItemStack(Material.WET_SPONGE));

                        for (int i = 0; i < 2; i++) {
                            if (Math.random() < lootDamage)
                                drops.add(new ItemStack(Material.PRISMARINE_SHARD));
                        }
                        for (int i = 0; i < 3; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.PRISMARINE_CRYSTALS));
                        }
                    }
                    case GHAST -> {
                        var squared = Math.pow(lootDamage, 2);
                        for (int i = 0; i < 8; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.GUNPOWDER));
                        }
                        for (int i = 0; i < 4; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.GHAST_TEAR));
                        }
                    }
                    case HOGLIN -> {
                        var squared = Math.pow(lootDamage, 1.5);
                        for (int i = 0; i < 14; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.PORKCHOP));
                        }
                        for (int i = 0; i < 9; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.LEATHER));
                        }
                        for (int i = 0; i < 6; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.BONE));
                        }
                    }
                    case MAGMA_CUBE -> {
                        if (Math.random() < Math.pow(lootDamage, 1.5)) drops.add(new ItemStack(Material.MAGMA_CREAM));
                    }
                    case SLIME -> {
                        if (Math.random() < Math.pow(lootDamage, 1.5)) drops.add(new ItemStack(Material.SLIME_BALL));
                    }
                    case RAVAGER -> {
                        drops.add(new ItemStack(Material.SADDLE));
                        for (int i = 0; i < 12; i++)
                            if (Math.pow(Math.random(), 1.2) < lootDamage)
                                drops.add(new ItemStack(Material.IRON_INGOT));

                        if (Math.random() < lootDamage) drops.add(new ItemStack(Material.CHAIN));
                    }
                    case SHULKER -> {
                        for (int i = 0; i < 2; i++) {
                            if (Math.pow(Math.random(), 2.5) < lootDamage)
                                drops.add(new ItemStack(Material.SHULKER_SHELL));
                        }
                    }
                    case SKELETON, STRAY, WITHER_SKELETON -> {
                        var squared = Math.pow(lootDamage, 1.5);
                        if (entity.getEquipment() != null && entity.getEquipment().getItemInMainHand().getType() == Material.BOW) {
                            for (int i = 0; i < 9; i++) {
                                if (Math.random() < squared) {
                                    ItemStack arrow;

                                    if (entity.getType() == EntityType.STRAY && Math.random() < 0.2) {
                                        arrow = new ItemStack(Material.TIPPED_ARROW);
                                        var meta = arrow.getItemMeta();
                                        ((PotionMeta) meta).setBasePotionData(new PotionData(PotionType.WEAKNESS));
                                        arrow.setItemMeta(meta);
                                    } else arrow = new ItemStack(Material.ARROW);
                                    var r = Math.random();
                                    if (r < 1.69350878e-5) arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 10);
                                    else if (r < 5.08052634e-5)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 9);
                                    else if (r < 0.00015241579)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 8);
                                    else if (r < 0.000457247371)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 7);
                                    else if (r < 0.00137174211)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 6);
                                    else if (r < 0.00411522634)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 5);
                                    else if (r < 0.012345679)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 4);
                                    else if (r < 0.037037037)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 3);
                                    else if (r < 0.111111111)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
                                    else if (r < 0.333333333)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);

                                    if (Math.random() < 0.03)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
                                    if (entity.getType() == EntityType.WITHER_SKELETON && Math.random() < 0.07)
                                        arrow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, 1);
                                    drops.add(arrow);
                                }
                            }
                        }
                        if (entity.getType() == EntityType.WITHER_SKELETON) {
                            for (int i = 0; i < 4; i++) {
                                if (Math.random() < squared) drops.add(new ItemStack(Material.COAL));
                            }
                            if (Math.random() < Math.pow(lootDamage, 8))
                                drops.add(new ItemStack(Material.WITHER_SKELETON_SKULL));
                        }
                        for (int i = 0; i < 12; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.BONE));
                        }
                        if (entity.getType() == EntityType.SKELETON) {
                            if (Math.random() < Math.pow(lootDamage, 8))
                                drops.add(new ItemStack(Material.SKELETON_SKULL));
                        }
                    }
                    case WITCH -> {
                        for (int i = 0; i < 3; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.NETHER_WART));
                        }
                        for (int i = 0; i < 3; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.BONE));
                        }
                        var cubed = Math.pow(lootDamage, 3);
                        for (int i = 0; i < 2; i++) {
                            if (Math.random() < cubed) drops.add(new ItemStack(Material.SUGAR));
                            if (Math.random() < cubed) drops.add(new ItemStack(Material.REDSTONE));
                            if (Math.random() < cubed) drops.add(new ItemStack(Material.GLOWSTONE_DUST));
                            if (Math.random() < cubed) drops.add(new ItemStack(Material.GLASS_BOTTLE));
                        }
                    }
                    case ZOGLIN -> {
                        var squared = Math.pow(lootDamage, 2);
                        for (int i = 0; i < 14; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.ROTTEN_FLESH));
                        }
                        for (int i = 0; i < 8; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.BONE));
                        }
                    }
                    case VILLAGER, WANDERING_TRADER, VINDICATOR, EVOKER -> {
                        for (int i = 0; i < 3; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.BONE));
                        }
                    }
                    case PHANTOM -> {
                        for (int i = 0; i < 5; i++) {
                            if (Math.random() < lootDamage) drops.add(new ItemStack(Material.PHANTOM_MEMBRANE));
                        }
                    }
                    case ENDERMAN -> {
                        var squared = Math.pow(lootDamage, 2);
                        for (int i = 0; i < 8; i++) {
                            if (Math.random() < squared) drops.add(new ItemStack(Material.ENDER_PEARL));
                        }
                    }

                }
                for (var drop : nDrops) {
                    var isSimilar = false;
                    if (drop.getType() == Material.ARROW
                            || drop.getType() == Material.TIPPED_ARROW
                            || drop.getType() == Material.CARROT
                            || drop.getType() == Material.POTATO
                            || drop.getType() == Material.COOKED_CHICKEN
                            || drop.getType() == Material.COOKED_PORKCHOP
                            || drop.getType() == Material.COOKED_BEEF
                            || drop.getType() == Material.COOKED_MUTTON
                            || drop.getType() == Material.COOKED_RABBIT
                            || drop.getType() == Material.CHICKEN
                            || drop.getType() == Material.PORKCHOP
                            || drop.getType() == Material.BEEF
                            || drop.getType() == Material.MUTTON
                            || drop.getType() == Material.FEATHER
                            || drop.getType() == Material.RABBIT
                            || drop.getType() == Material.RABBIT_HIDE
                            || drop.getType() == Material.RABBIT_FOOT
                            || drop.getType() == Material.STRING
                            || drop.getType() == Material.LEATHER
                            || drop.getType() == Material.BLACK_WOOL
                            || drop.getType() == Material.BLUE_WOOL
                            || drop.getType() == Material.BROWN_WOOL
                            || drop.getType() == Material.CYAN_WOOL
                            || drop.getType() == Material.GRAY_WOOL
                            || drop.getType() == Material.GREEN_WOOL
                            || drop.getType() == Material.LIGHT_BLUE_WOOL
                            || drop.getType() == Material.LIGHT_GRAY_WOOL
                            || drop.getType() == Material.LIME_WOOL
                            || drop.getType() == Material.MAGENTA_WOOL
                            || drop.getType() == Material.ORANGE_WOOL
                            || drop.getType() == Material.PINK_WOOL
                            || drop.getType() == Material.PURPLE_WOOL
                            || drop.getType() == Material.RED_WOOL
                            || drop.getType() == Material.WHITE_WOOL
                            || drop.getType() == Material.YELLOW_WOOL
                            || drop.getType() == Material.INK_SAC
                            || drop.getType() == Material.COD
                            || drop.getType() == Material.COOKED_COD
                            || drop.getType() == Material.SALMON
                            || drop.getType() == Material.COOKED_SALMON
                            || drop.getType() == Material.TROPICAL_FISH
                            || drop.getType() == Material.PUFFERFISH
                            || drop.getType() == Material.SEAGRASS
                            || drop.getType() == Material.POPPY
                            || drop.getType() == Material.IRON_INGOT
                            || drop.getType() == Material.BAMBOO
                            || drop.getType() == Material.GOLD_NUGGET
                            || drop.getType() == Material.ROTTEN_FLESH
                            || drop.getType() == Material.BONE
                            || drop.getType() == Material.PRISMARINE_SHARD
                            || drop.getType() == Material.PRISMARINE_CRYSTALS
                            || drop.getType() == Material.WET_SPONGE
                            || drop.getType() == Material.GUNPOWDER
                            || drop.getType() == Material.GHAST_TEAR
                            || drop.getType() == Material.MAGMA_CREAM
                            || drop.getType() == Material.SLIME_BALL
                            || drop.getType() == Material.SHULKER_SHELL
                            || drop.getType() == Material.REDSTONE
                            || drop.getType() == Material.GLOWSTONE_DUST
                            || drop.getType() == Material.SUGAR
                            || drop.getType() == Material.POTION
                            || drop.getType() == Material.EMERALD
                            || drop.getType() == Material.PHANTOM_MEMBRANE
                            || drop.getType() == Material.ENDER_PEARL
                    ) continue;
                    for (var item : new ArrayList<>(drops)) {
                        if (drop.isSimilar(item)) isSimilar = true;
                        break;
                    }
                    if (!isSimilar) drops.add(drop);
                }
            }

            for (var drop : new ArrayList<>(drops)) {
                if (isMetal(drop)) {
                    if (!hasPurity(drop))
                        generatePurity(drop, 1.0);
                }
                drops.remove(drop);
                if (drop == null || drop.getType() == Material.AIR) continue;
                event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), drop);
            }

            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                if (entity != null) entity.remove();
            }, 40L);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockExplode(BlockExplodeEvent event) {
            var blocks = event.blockList();
            var yield = event.getYield();
            for (var block : new ArrayList<>(blocks)) {
                if (block.getType() == Material.IRON_BLOCK || block.getType() == Material.GOLD_BLOCK || block.getType() == Material.NETHERITE_BLOCK) {
                    blocks.remove(block);
                    var purity = getBlockPurity(block);
                    setBlockPurity(block, -2F);
                    if (Math.random() < yield) {
                        var isRefined = purity < 0;
                        if (isRefined) purity *= -1;
                        var item = new ItemStack(block.getType());
                        var purityArray = new float[1];
                        purityArray[0] = purity;
                        setPurity(item, purityArray);
                        if (isRefined) refine(item);
                        ItemStack finalItem = item;
                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                                () -> block.getWorld().dropItem(block.getLocation(), finalItem), 1L);
                    }
                    block.setType(Material.AIR);
                }

                // Containers
                var state = block.getState();
                if (state instanceof BlockInventoryHolder) {
                    blocks.remove(block);
                    var location = block.getLocation();
                    var inventory = ((BlockInventoryHolder) block.getState()).getInventory();
                    for (var content : inventory.getContents()) {
                        if (content != null && content.getType() != Material.AIR) {
                            location.getWorld().dropItemNaturally(location, content);
                        }
                    }
                    inventory.clear();
                    location.getWorld().dropItemNaturally(location, new ItemStack(block.getType()));
                    block.setType(Material.AIR);
                }
            }
        }

        @EventHandler
        private void onEntityAttack(EntityDamageByEntityEvent event) {
            var damager = event.getDamager();
            var damaged = event.getEntity();
            if (!(damager instanceof LivingEntity)) return;
            var equipment = ((LivingEntity) damager).getEquipment();
            if (equipment == null) return;
            var hand = equipment.getItemInMainHand();
            var meta = hand.getItemMeta();
            if (meta == null || !tools.contains(hand.getType())) return;
            var r = Math.random();
            var unbreaking = 0;
            if (meta.hasEnchant(Enchantment.DURABILITY)) unbreaking += meta.getEnchantLevel(Enchantment.DURABILITY);
            var durability = hand.getType().getMaxDurability();

            if (meta.hasEnchant(Enchantment.DAMAGE_ALL)) {
                var level = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);
                if (r < Math.pow(durability + 1, Math.pow(level, 0.75) / 10.0 - 1) / (unbreaking + 1)) {
                    meta.removeEnchant(Enchantment.DAMAGE_ALL);
                    if (level > 1) {
                        meta.addEnchant(Enchantment.DAMAGE_ALL, level - 1, true);
                    }
                    hand.setItemMeta(meta);
                }
            }
            if (meta.hasEnchant(Enchantment.DIG_SPEED)) {
                var level = meta.getEnchantLevel(Enchantment.DIG_SPEED);
                if (r < Math.pow(durability + 1, Math.pow(level, 0.75) / 10.0 - 1) / (unbreaking + 1)) {
                    meta.removeEnchant(Enchantment.DIG_SPEED);
                    if (level > 1) {
                        meta.addEnchant(Enchantment.DIG_SPEED, level - 1, true);
                    }
                    hand.setItemMeta(meta);
                }
            }
            if (meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS)) {
                var level = meta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS);
                if (r < Math.pow(durability + 1, Math.pow(level, 0.75) / 10.0 - 1) / (unbreaking + 1)) {
                    meta.removeEnchant(Enchantment.LOOT_BONUS_MOBS);
                    if (level > 1) {
                        meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, level - 1, true);
                    }
                    hand.setItemMeta(meta);
                }
            }
            if (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                var level = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
                if (r < Math.pow(durability + 1, Math.pow(level, 0.75) / 10.0 - 1) / (unbreaking + 1)) {
                    meta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
                    if (level > 1) {
                        meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, level - 1, true);
                    }
                    hand.setItemMeta(meta);
                }
            }
            if (meta.hasEnchant(Enchantment.IMPALING)) {
                var level = meta.getEnchantLevel(Enchantment.IMPALING);
                if (r < Math.pow(durability + 1, Math.pow(level, 0.75) / 10.0 - 1) / (unbreaking + 1)) {
                    meta.removeEnchant(Enchantment.IMPALING);
                    if (level > 1) {
                        meta.addEnchant(Enchantment.IMPALING, level - 1, true);
                    }
                    hand.setItemMeta(meta);
                }
            }
            equipment.setItemInMainHand(hand);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onEntityExplode(EntityExplodeEvent event) {
            var blocks = event.blockList();
            var yield = event.getYield();
            for (var block : new ArrayList<>(blocks)) {
                if (block.getType() == Material.IRON_BLOCK || block.getType() == Material.GOLD_BLOCK || block.getType() == Material.NETHERITE_BLOCK) {
                    blocks.remove(block);
                    var purity = getBlockPurity(block);
                    setBlockPurity(block, -2F);
                    if (event.getEntity() instanceof TNTPrimed || Math.random() < yield) {
                        var item = new ItemStack(block.getType());
                        var isRefined = purity < 0;
                        if (isRefined) purity *= -1;
                        var purityArray = new float[1];
                        purityArray[0] = purity;
                        setPurity(item, purityArray);
                        ItemStack finalItem = item;
                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                                () -> block.getWorld().dropItem(block.getLocation(), finalItem), 1L);
                    }
                    block.setType(Material.AIR);
                }

                // Containers
                var state = block.getState();
                if (state instanceof BlockInventoryHolder) {
                    blocks.remove(block);
                    var location = block.getLocation();
                    var inventory = ((BlockInventoryHolder) block.getState()).getInventory();
                    for (var content : inventory.getContents()) {
                        if (content != null && content.getType() != Material.AIR) {
                            location.getWorld().dropItemNaturally(location, content);
                        }
                    }
                    inventory.clear();
                    location.getWorld().dropItemNaturally(location, new ItemStack(block.getType()));
                    block.setType(Material.AIR);
                }
            }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        private void onBlockBreak(BlockBreakEvent event) {
            var item = event.getPlayer().getInventory().getItemInMainHand();

            var location = event.getBlock().getLocation();
            location.setY(location.getY() + 0.5);

            // Containers
            var block = event.getBlock();
            var state = block.getState();
            if (state instanceof BlockInventoryHolder) {
                event.setDropItems(false);
                var inventory = ((BlockInventoryHolder) block.getState()).getInventory();
                for (var content : inventory.getContents()) {
                    if (content != null && content.getType() != Material.AIR) {
                        location.getWorld().dropItemNaturally(location, content);
                    }
                }
                inventory.clear();
                location.getWorld().dropItemNaturally(location, new ItemStack(block.getType()));
            }


            // Metal block
            var tool = event.getPlayer().getEquipment().getItemInMainHand().getType();
            if (block.getType() == Material.IRON_BLOCK || block.getType() == Material.GOLD_BLOCK || block.getType() == Material.NETHERITE_BLOCK) {
                var purity = getBlockPurity(block);
                setBlockPurity(block, -2F);

                if ((block.getType() == Material.IRON_BLOCK && (tool == Material.STONE_PICKAXE || tool == Material.IRON_PICKAXE || tool == Material.DIAMOND_PICKAXE || tool == Material.NETHERITE_PICKAXE))
                        || (block.getType() == Material.GOLD_BLOCK && (tool == Material.IRON_PICKAXE || tool == Material.DIAMOND_PICKAXE || tool == Material.NETHERITE_PICKAXE))
                        || (block.getType() == Material.NETHERITE_BLOCK && (tool == Material.DIAMOND_PICKAXE) || (tool == Material.NETHERITE_PICKAXE))) {
                    item = new ItemStack(block.getType());
                    var purityArray = new float[1];
                    purityArray[0] = purity;
                    setPurity(item, purityArray);
                    event.setDropItems(false);
                    ItemStack finalItem = item;
                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                            () -> location.getWorld().dropItem(location, finalItem), 1L);
                }
            }


            // Nether gold ore
            if (event.getBlock().getType() == Material.NETHER_GOLD_ORE
                    && (tool == Material.WOODEN_PICKAXE || tool == Material.STONE_PICKAXE || tool == Material.GOLDEN_PICKAXE || tool == Material.IRON_PICKAXE || tool == Material.DIAMOND_PICKAXE || tool == Material.NETHERITE_PICKAXE)) {
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
                            goldDustItem.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
                            var meta = goldDustItem.getItemMeta();
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
                        if (level == 4) chance = 0.0109;
                        else if (level == 5) chance = 0.021;
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
                            ironDustItem.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
                            var meta = ironDustItem.getItemMeta();
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
                            goldDustItem.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
                            var meta = goldDustItem.getItemMeta();
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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
                            ancientDustItem.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
                            var meta = ancientDustItem.getItemMeta();
                            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
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


            // Blade Dulling
            if (tools.contains(item.getType())) {
                var player = event.getPlayer();
                var equipment = player.getEquipment();
                if (equipment == null) return;
                var hand = equipment.getItemInMainHand();
                var meta = hand.getItemMeta();
                if (meta == null || !tools.contains(hand.getType())) return;
                var r = Math.random();
                var unbreaking = 0;
                if (meta.hasEnchant(Enchantment.DURABILITY))
                    unbreaking += meta.getEnchantLevel(Enchantment.DURABILITY);
                var durability = hand.getType().getMaxDurability();

                if (meta.hasEnchant(Enchantment.DAMAGE_ALL)) {
                    var level = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);
                    if (r < Math.pow(durability + 1, Math.pow(level, 0.9) / 10.0 - 1) / (unbreaking + 1)) {
                        meta.removeEnchant(Enchantment.DAMAGE_ALL);
                        if (level > 1) {
                            meta.addEnchant(Enchantment.DAMAGE_ALL, level - 1, true);
                        }
                        hand.setItemMeta(meta);
                    }
                }
                if (meta.hasEnchant(Enchantment.DIG_SPEED)) {
                    var level = meta.getEnchantLevel(Enchantment.DIG_SPEED);
                    if (r < Math.pow(durability + 1, Math.pow(level, 0.9) / 10.0 - 1) / (unbreaking + 1)) {
                        meta.removeEnchant(Enchantment.DIG_SPEED);
                        if (level > 1) {
                            meta.addEnchant(Enchantment.DIG_SPEED, level - 1, true);
                        }
                        hand.setItemMeta(meta);
                    }
                }
                if (meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS)) {
                    var level = meta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS);
                    if (r < Math.pow(durability + 1, Math.pow(level, 0.9) / 10.0 - 1) / (unbreaking + 1)) {
                        meta.removeEnchant(Enchantment.LOOT_BONUS_MOBS);
                        if (level > 1) {
                            meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, level - 1, true);
                        }
                        hand.setItemMeta(meta);
                    }
                }
                if (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                    var level = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
                    if (r < Math.pow(durability + 1, Math.pow(level, 0.9) / 10.0 - 1) / (unbreaking + 1)) {
                        meta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
                        if (level > 1) {
                            meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, level - 1, true);
                        }
                        hand.setItemMeta(meta);
                    }
                }
                if (meta.hasEnchant(Enchantment.IMPALING)) {
                    var level = meta.getEnchantLevel(Enchantment.IMPALING);
                    if (r < Math.pow(durability + 1, Math.pow(level, 0.9) / 10.0 - 1) / (unbreaking + 1)) {
                        meta.removeEnchant(Enchantment.IMPALING);
                        if (level > 1) {
                            meta.addEnchant(Enchantment.IMPALING, level - 1, true);
                        }
                        hand.setItemMeta(meta);
                    }
                }
                equipment.setItemInMainHand(hand);
            }

            // Metal drops
            if (event.isDropItems()) {
                var drops = block.getDrops();
                for (var drop : drops) {
                    if (isMetal(drop) && !hasPurity(drop)) {
                        generatePurity(drop, 1.0);
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


        private void throwGrenade(Player player, boolean isOverhand, long explodeTime) {
            if (player.isSneaking() && explodeTime != 0) {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                        () -> throwGrenade(player, isOverhand, explodeTime - 1), 1L);
                return;
            }
            if (isOverhand)
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1, 1);
            else
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 0.5f, 2.0f);
            var grenade = new ItemStack(Material.TNT);
            var meta = grenade.getItemMeta();
            var lore = new ArrayList<String>();
            lore.add(DiplomacyRecipes.getInstance().GRENADE_LORE);
            meta.setLore(lore);
            grenade.setItemMeta(meta);
            var velocity = player.getEyeLocation().getDirection();
            if (isOverhand) velocity.multiply(2.0);
            else velocity.multiply(0.6);
            var loc = player.getEyeLocation();
            if (isOverhand)
                loc.setY(loc.getY() + 0.33);
            else
                loc.setY(loc.getY() - 0.6);

            velocity.add(player.getVelocity());
            velocity.setY(velocity.getY() + (Math.random() - 0.5) * 0.025);
            velocity.setX(velocity.getX() + (Math.random() - 0.5) * 0.025);
            velocity.setZ(velocity.getZ() + (Math.random() - 0.5) * 0.025);

            var drop = player.getWorld().dropItem(loc, grenade);
            drop.setVelocity(velocity);
            drop.setPickupDelay(1000);
            grenadeTick(drop, 0, explodeTime, player);
        }

        private Location getNextLocation(Item item) {
            var location = item.getLocation();
            var velocity = item.getVelocity();
            return new Location(location.getWorld(), location.getX() + velocity.getX(), location.getY() + velocity.getY(), location.getZ() + velocity.getZ());
        }

        private boolean isColliding(Location location, Location nextLocation) {
            var nextBlock = nextLocation.getBlock();
            var nextType = nextBlock.getType();
            return !nextBlock.isLiquid() && !nextBlock.isPassable()
                    && nextType != location.getBlock().getType();
        }

        private boolean entityCollision(Item item, Location location, Location nextLocation, Player player,
                                        long curTime) {
            var xDist = Math.abs(location.getX() - nextLocation.getX());
            var yDist = Math.abs(location.getY() - nextLocation.getY());
            var zDist = Math.abs(location.getZ() - nextLocation.getZ());

            var nearby = item.getNearbyEntities(xDist, yDist, zDist);
            if (nearby.size() == 0) return false;
            else {
                var x = location.getX();
                var y = location.getY();
                var z = location.getZ();
                var v = item.getVelocity();
                var vX = v.getX();
                var vY = v.getY();
                var vZ = v.getZ();
                for (int i = 1; i <= 10; i++) {
                    x += vX / 10.0;
                    y += vY / 10.0;
                    z += vZ / 10.0;
                    for (var entity : nearby) {
                        if (curTime < 10L && entity.equals(player)) continue;
                        var box = entity.getBoundingBox();
                        if (box.contains(x, y, z)) {
                            var bX = Math.min(
                                    Math.abs(x - box.getMinX()),
                                    Math.abs(x - box.getMaxX())
                            );
                            var bY = Math.min(
                                    Math.abs(y - box.getMinY()),
                                    Math.abs(y - box.getMaxY())
                            );
                            var bZ = Math.min(
                                    Math.abs(z - box.getMinZ()),
                                    Math.abs(z - box.getMaxZ())
                            );
                            var b = new double[3];
                            b[0] = bX;
                            b[1] = bY;
                            b[2] = bZ;

                            var vLength = item.getVelocity().length();
                            Arrays.sort(b);
                            if (b[0] == bX) v.setX(-1 * v.getX());
                            else if (b[0] == bY) v.setY(-1 * v.getY());
                            else v.setZ(-1 * v.getZ());


                            if ((entity instanceof LivingEntity && !(entity instanceof Bee))
                                    || entity instanceof EnderCrystal
                                    || entity instanceof ArmorStand) {
                                v.add(entity.getVelocity());
                                v.setX(0.15 * v.getX());
                                v.setY(0.15 * v.getY());
                                v.setZ(0.15 * v.getZ());
                            } else if (
                                    entity instanceof Item
                                            || entity instanceof Explosive
                                            || entity instanceof Projectile
                                            || entity instanceof Bee
                                            || entity instanceof FallingBlock) {
                                var half = v.clone();
                                half.setX(v.getX() / 2);
                                half.setY(v.getY() / 2);
                                half.setZ(v.getZ() / 2);

                                var eV = entity.getVelocity();
                                var half2 = eV.clone();
                                half2.setX(eV.getX() / 2);
                                half2.setY(eV.getY() / 2);
                                half2.setZ(eV.getZ() / 2);
                                v.add(half2);
                                eV.add(half);
                                entity.setVelocity(eV);

                            } else if (
                                    entity instanceof Minecart
                                            || entity instanceof Boat) {
                                v.add(entity.getVelocity());
                            }

                            item.setVelocity(v);

                            if (vLength > 0.05) {
                                item.getWorld().playSound(item.getLocation(), Sound.BLOCK_CHAIN_STEP, 1, 1);
                            }

                            if (entity instanceof LivingEntity && vLength > 0.25) {
                                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                                ((LivingEntity) entity).damage(1);
                            }
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private void grenadeTick(Item item, long curTime, long explodeTime, Player player) {
            if (curTime == explodeTime) {
                var diplomacyChunk = new DiplomacyChunk(item.getLocation().getChunk());
                boolean breakBlocks;
                breakBlocks = diplomacyChunk.getNation() == null;
                item.getWorld().createExplosion(item.getLocation(), 3.85F, false, false, item);
                item.getWorld().createExplosion(item.getLocation(), 0.75F, false, breakBlocks, item);
                item.remove();
                return;
            }
            var location = item.getLocation();
            var location2 = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            var velocity = item.getVelocity();
            location2.setY(item.getLocation().getY() + 0.25);
            if (curTime % 2 == 0 && curTime > 2) {
                item.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location2, 1, 0.05, 0.05, 0.05, 0);
            }
            var nextLocation = getNextLocation(item);
            if (location.getBlock().isLiquid()) {
                velocity.setX(velocity.getX() * 0.5);
                velocity.setY(velocity.getY() * 0.5);
                velocity.setY(velocity.getY() * 0.5);
            }

            if (!entityCollision(item, location, nextLocation, player, curTime)) {
                var vLength = velocity.length();

                // X-axis check
                var magnitude = Math.abs(nextLocation.getBlockX() - location.getBlockX());
                if (velocity.getX() > 0
                        && nextLocation.getBlockX() - location.getBlockX() >= 1) {
                    var colliding = true;
                    for (int i = 1; i <= magnitude; i++) {
                        if (item.getWorld().getBlockAt(location.getBlockX() + i, location.getBlockY(), location.getBlockZ()).isPassable()) {
                            colliding = false;
                            break;
                        }
                    }
                    if (colliding)
                        velocity.setX(-0.5 * velocity.getX());
                } else if (velocity.getX() < 0
                        && nextLocation.getBlockX() - location.getBlockX() <= -1) {
                    var colliding = false;
                    for (int i = 1; i <= magnitude; i++) {
                        if (!item.getWorld().getBlockAt(location.getBlockX() - i, location.getBlockY(), location.getBlockZ()).isPassable()) {
                            colliding = true;
                            break;
                        }
                    }
                    if (colliding)
                        velocity.setX(-0.5 * velocity.getX());
                }
                var overallColliding = false;

                // Y-axis check
                magnitude = Math.abs(nextLocation.getBlockY() - location.getBlockY());
                if (velocity.getY() > 0
                        && nextLocation.getBlockY() - location.getBlockY() >= 1) {
                    var colliding = true;
                    for (int i = 1; i <= magnitude; i++) {
                        if (item.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + i, location.getBlockZ()).isPassable()) {
                            colliding = false;
                            break;
                        }
                    }
                    if (colliding) {
                        velocity.setY(-0.5 * velocity.getY());
                        overallColliding = true;
                    }

                } else if (velocity.getY() < 0
                        && nextLocation.getBlockY() - location.getBlockY() <= -1) {
                    var colliding = false;
                    for (int i = 1; i <= magnitude; i++) {
                        if (!item.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - i, location.getBlockZ()).isPassable()) {
                            colliding = true;
                            break;
                        }
                    }
                    if (colliding) {
                        velocity.setY(-0.5 * velocity.getY());
                        overallColliding = true;
                    }
                }

                // Z-axis check
                magnitude = Math.abs(nextLocation.getBlockZ() - location.getBlockZ());
                if (velocity.getZ() > 0
                        && nextLocation.getBlockZ() - location.getBlockZ() >= 1) {
                    var colliding = true;
                    for (int i = 1; i <= magnitude; i++) {
                        if (item.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() + i).isPassable()) {
                            colliding = false;
                            break;
                        }
                    }
                    if (colliding) {
                        velocity.setZ(-0.5 * velocity.getZ());
                        overallColliding = true;
                    }
                } else if (velocity.getZ() < 0
                        && nextLocation.getBlockZ() - location.getBlockZ() <= -1) {
                    var colliding = false;
                    for (int i = 1; i <= magnitude; i++) {
                        if (!item.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() - i).isPassable()) {
                            colliding = true;
                            break;
                        }
                    }
                    if (colliding) {
                        overallColliding = true;
                        velocity.setZ(-0.5 * velocity.getZ());
                    }
                }
                if (overallColliding) {
                    if (vLength > 0.05)
                        item.getWorld().playSound(item.getLocation(), Sound.BLOCK_CHAIN_STEP, 1, 1);
                    item.setVelocity(velocity);
                }
            }
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> grenadeTick(item, curTime + 1L, explodeTime, player), 1L);
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

            if (damager instanceof LivingEntity) {
                var equipment = ((LivingEntity) damager).getEquipment();
                if (equipment != null) {
                    var hand = equipment.getItemInMainHand();
                    if (isHuntingKnife(hand)) {
                        damage = Math.max(0, damage - 3);
                    }
                }
            }

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

            if (equip.getHelmet() == null) {
                var armor = ((LivingEntity) entity).getAttribute(Attribute.GENERIC_ARMOR).getValue();
                reduce += armor * .02;
            }
            reduce /= 4.0;
            reduce *= 6.0;
            reduce = Math.min(1, reduce);

            switch (cause) {
                case BLOCK_EXPLOSION, ENTITY_EXPLOSION -> {
                    damage *= 3;
                    if (chest.getItemMeta() instanceof Damageable) {
                        var meta = chest.getItemMeta();
                        var unbreaking = 0;
                        if (meta.hasEnchant(Enchantment.DURABILITY))
                            unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY);
                        for (int i = 0; i < 5; i++)
                            if (Math.random() < 0.45 / (1.0 + unbreaking))
                                ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                        chest.setItemMeta(meta);
                    }
                    if (legs.getItemMeta() instanceof Damageable) {
                        var meta = legs.getItemMeta();
                        var unbreaking = 0;
                        if (meta.hasEnchant(Enchantment.DURABILITY))
                            unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY);
                        for (int i = 0; i < 5; i++)
                            if (Math.random() < 0.45 / (1.0 + unbreaking))
                                ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                        legs.setItemMeta(meta);
                    }
                    if (boots.getItemMeta() instanceof Damageable) {
                        var meta = boots.getItemMeta();
                        var unbreaking = 0;
                        if (meta.hasEnchant(Enchantment.DURABILITY))
                            unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY);
                        for (int i = 0; i < 5; i++)
                            if (Math.random() < 0.45 / (1.0 + unbreaking))
                                ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                        boots.setItemMeta(meta);
                    }

                    if (helmet.getItemMeta() != null && helmet.getItemMeta().hasEnchant(Enchantment.PROTECTION_EXPLOSIONS))
                        reduce += 0.0083333333 * helmet.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                    if (chest.getItemMeta() != null && chest.getItemMeta().hasEnchant(Enchantment.PROTECTION_EXPLOSIONS))
                        reduce += 0.0133333334 * chest.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_EXPLOSIONS))
                        reduce += 0.0116666667 * legs.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_EXPLOSIONS))
                        reduce += 0.0066666666 * legs.getEnchantmentLevel(Enchantment.PROTECTION_EXPLOSIONS);
                    if (reduce == 1) event.setCancelled(true);
                    else {
                        if (!(entity instanceof Player)) {
                            var instance = Entities.getInstance();
                            var percentHealth = instance.getPercentHealth((LivingEntity) entity);
                            var percentDamage = instance.getPercentOfMaxHealth((LivingEntity) entity, damage * (1.0 - reduce));
                            var currentLootDamage = instance.getLootDamage((LivingEntity) entity);
                            var change = Math.min(percentDamage, percentHealth) * (0.85);
                            instance.setLootDamage((LivingEntity) entity, Math.max(0, currentLootDamage - change) * .99);
                        }
                        event.getEntity().setLastDamageCause(event);
                        event.setDamage(Math.min(((LivingEntity) entity).getHealth(), damage * (1.0 - reduce)));
                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                            if (entity != null && ((LivingEntity) entity).getHealth() < 0.1)
                                ((LivingEntity) entity).setHealth(0.0);
                        }, 0L);
                    }
                }
                case ENTITY_ATTACK, ENTITY_SWEEP_ATTACK -> {
                    if (entity instanceof Player && ((Player) entity).isBlocking()) break;
                    if (helmet.getItemMeta() != null && helmet.getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
                        reduce += 0.0083333333 * helmet.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    if (chest.getItemMeta() != null && chest.getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
                        reduce += 0.0133333334 * chest.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
                        reduce += 0.0116666667 * legs.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_ENVIRONMENTAL))
                        reduce += 0.0066666666 * legs.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                    if (reduce == 1) event.setCancelled(true);
                    else {

                        if (!(entity instanceof Player)) {
                            var instance = Entities.getInstance();
                            var percentHealth = instance.getPercentHealth((LivingEntity) entity);
                            var percentDamage = instance.getPercentOfMaxHealth((LivingEntity) entity, damage * (1.0 - reduce));
                            var currentLootDamage = instance.getLootDamage((LivingEntity) entity);

                            var looting = 0;
                            if (damager != null && damager instanceof LivingEntity) {
                                var equipment = ((LivingEntity) damager).getEquipment();
                                if (equipment != null) {
                                    var item = equipment.getItemInMainHand();
                                    if (isHuntingKnife(item)) {
                                        looting += 1;
                                        if (item.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_MOBS))
                                            looting += item.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
                                    }

                                }
                            }
                            var change = Math.min(percentDamage, percentHealth) * (0.8 - 0.0625 * looting);
                            instance.setLootDamage((LivingEntity) entity, Math.max(0, (currentLootDamage - change) * 0.99));
                        }
                        event.setDamage(Math.min(((LivingEntity) entity).getHealth(), damage * (1.0 - reduce)));
                        event.getEntity().setLastDamageCause(event);
                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                            if (entity != null && ((LivingEntity) entity).getHealth() < 0.1)
                                ((LivingEntity) entity).setHealth(0.0);
                        }, 0L);
                    }

                }
                case FIRE, FIRE_TICK, HOT_FLOOR, LAVA -> {
                    damage *= 2;
                    if (helmet.getItemMeta() instanceof Damageable) {
                        var meta = helmet.getItemMeta();
                        var unbreaking = 0;
                        if (meta.hasEnchant(Enchantment.DURABILITY))
                            unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY);

                        if (Math.random() < 0.125 / (1.0 + unbreaking))
                            ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                        helmet.setItemMeta(meta);
                    }
                    if (chest.getItemMeta() instanceof Damageable) {
                        var meta = chest.getItemMeta();
                        var unbreaking = 0;
                        if (meta.hasEnchant(Enchantment.DURABILITY))
                            unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY);

                        if (Math.random() < 0.125 / (1.0 + unbreaking))
                            ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                        chest.setItemMeta(meta);
                    }
                    if (legs.getItemMeta() instanceof Damageable) {
                        var meta = legs.getItemMeta();
                        var unbreaking = 0;
                        if (meta.hasEnchant(Enchantment.DURABILITY))
                            unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY);

                        if (Math.random() < 0.125 / (1.0 + unbreaking))
                            ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                        legs.setItemMeta(meta);
                    }
                    if (boots.getItemMeta() instanceof Damageable) {
                        var meta = boots.getItemMeta();
                        var unbreaking = 0;
                        if (meta.hasEnchant(Enchantment.DURABILITY))
                            unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY);

                        if (Math.random() < 0.125 / (1.0 + unbreaking))
                            ((Damageable) meta).setDamage(((Damageable) meta).getDamage() + 1);
                        boots.setItemMeta(meta);
                    }
                    if (helmet.getItemMeta() != null && helmet.getItemMeta().hasEnchant(Enchantment.PROTECTION_FIRE))
                        reduce += 0.0083333333 * helmet.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
                    if (chest.getItemMeta() != null && chest.getItemMeta().hasEnchant(Enchantment.PROTECTION_FIRE))
                        reduce += 0.0133333334 * chest.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_FIRE))
                        reduce += 0.0116666667 * legs.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_FIRE))
                        reduce += 0.0066666666 * legs.getEnchantmentLevel(Enchantment.PROTECTION_FIRE);
                    if (reduce == 1) event.setCancelled(true);
                    else {
                        if (!(entity instanceof Player)) {
                            var instance = Entities.getInstance();
                            var percentHealth = instance.getPercentHealth((LivingEntity) entity);
                            var percentDamage = instance.getPercentOfMaxHealth((LivingEntity) entity, damage * (1.0 - reduce));
                            var currentLootDamage = instance.getLootDamage((LivingEntity) entity);
                            var change = Math.min(percentDamage, percentHealth) * (0.925);
                            instance.setLootDamage((LivingEntity) entity, Math.max(0, currentLootDamage - change) * .99);
                        }
                        event.getEntity().setLastDamageCause(event);
                        event.setDamage(Math.min(((LivingEntity) entity).getHealth(), damage * (1.0 - reduce)));
                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                            if (entity != null && ((LivingEntity) entity).getHealth() < 0.1)
                                ((LivingEntity) entity).setHealth(0.0);
                        }, 0L);
                    }
                }
                case PROJECTILE -> {
                    if (entity instanceof Player && ((Player) entity).isBlocking()) break;
                    if (helmet.getItemMeta() != null && helmet.getItemMeta().hasEnchant(Enchantment.PROTECTION_PROJECTILE))
                        reduce += 0.0083333333 * helmet.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                    if (chest.getItemMeta() != null && chest.getItemMeta().hasEnchant(Enchantment.PROTECTION_PROJECTILE))
                        reduce += 0.0133333334 * chest.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_PROJECTILE))
                        reduce += 0.0116666667 * legs.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                    if (legs.getItemMeta() != null && legs.getItemMeta().hasEnchant(Enchantment.PROTECTION_PROJECTILE))
                        reduce += 0.0066666666 * legs.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE);
                    if (!(entity instanceof Player)) {
                        var instance = Entities.getInstance();
                        var percentHealth = instance.getPercentHealth((LivingEntity) entity);
                        var percentDamage = instance.getPercentOfMaxHealth((LivingEntity) entity, damage * (1.0 - reduce));
                        var currentLootDamage = instance.getLootDamage((LivingEntity) entity);
                        var change = Math.min(percentDamage, percentHealth) * (0.65);
                        instance.setLootDamage((LivingEntity) entity, Math.max(0, currentLootDamage - change) * 0.99);
                    }
                    event.getEntity().setLastDamageCause(event);
                    event.setDamage(Math.min(((LivingEntity) entity).getHealth(), damage * (1.0 - reduce)));
                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                        if (entity != null && ((LivingEntity) entity).getHealth() < 0.1)
                            ((LivingEntity) entity).setHealth(0.0);
                    }, 0L);
                }
                case MAGIC, DRAGON_BREATH, SUFFOCATION, FALL, MELTING, DROWNING, VOID, LIGHTNING, STARVATION, POISON, WITHER, FALLING_BLOCK, FLY_INTO_WALL -> {
                    if (!(entity instanceof Player)) {
                        var instance = Entities.getInstance();
                        var percentHealth = instance.getPercentHealth((LivingEntity) entity);
                        var percentDamage = instance.getPercentOfMaxHealth((LivingEntity) entity, damage * (1.0 - reduce));
                        var currentLootDamage = instance.getLootDamage((LivingEntity) entity);
                        var change = Math.min(percentDamage, percentHealth) * (0.985);
                        instance.setLootDamage((LivingEntity) entity, Math.max(0, currentLootDamage - change) * .99);
                    }
                }
                default -> {
                    if (!(entity instanceof Player)) {
                        var instance = Entities.getInstance();
                        var percentHealth = instance.getPercentHealth((LivingEntity) entity);
                        var percentDamage = instance.getPercentOfMaxHealth((LivingEntity) entity, damage * (1.0 - reduce));
                        var currentLootDamage = instance.getLootDamage((LivingEntity) entity);
                        var change = Math.min(percentDamage, percentHealth) * (0.9);
                        instance.setLootDamage((LivingEntity) entity, Math.max(0, currentLootDamage - change * .99));
                    }
                }
            }
            if (entity.getType() == EntityType.HORSE || entity.getType() == EntityType.MULE || entity.getType() == EntityType.DONKEY || entity.getType() == EntityType.LLAMA || entity.getType() == EntityType.TRADER_LLAMA) {
                if (Entities.getInstance().getPercentHealth((LivingEntity) entity) == 1.0) {
                    ((LivingEntity) entity).setHealth(((LivingEntity) entity).getHealth() - event.getDamage());
                    event.setDamage(0.0);
                }
            }
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        private void onBlockPlace(BlockPlaceEvent event) {
            var block = event.getBlock();
            if (block.getType() == Material.IRON_BLOCK || block.getType() == Material.GOLD_BLOCK || block.getType() == Material.NETHERITE_BLOCK) {
                var item = event.getItemInHand();
                var purity = getPurity(item);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                        () -> setBlockPurity(block, purity[0]), 1L);

                var hand = event.getHand();
                var nItem = dropItemFromPurity(item)[0];

                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                        () -> event.getPlayer().getEquipment().setItem(hand, nItem), 1L);

            }
        }

        @EventHandler
        private void grenadeDrop(PlayerDropItemEvent event) {
            var drop = event.getItemDrop();
            var item = drop.getItemStack();
            if (isGrenade(item) && item.getType() == Material.FIREWORK_STAR) {
                var player = event.getPlayer();
                grenadeDropList.add(player);
                Bukkit.getScheduler().runTaskLater(
                        Diplomacy.getInstance(),
                        () -> grenadeDropList.remove(player),
                        2L);
            }
        }

        @EventHandler
        private void grenadeDrop(InventoryClickEvent event) {
            var item = event.getCurrentItem();
            var player = event.getView().getPlayer();
            var equip = player.getEquipment();
            var item2 = air;
            if (equip != null) item2 = equip.getItemInMainHand();
            if ((isGrenade(item) && item != null && item.getType() == Material.FIREWORK_STAR) || (isGrenade(item2) && item2.getType() == Material.FIREWORK_STAR)) {
                grenadeDropList.add((Player) player);
                Bukkit.getScheduler().runTaskLater(
                        Diplomacy.getInstance(),
                        () -> grenadeDropList.remove(player),
                        2L);
            }
        }

        private void grenadeUse(ItemStack item, Action action, Player player, EquipmentSlot hand) {
            boolean isOverhand = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK);

            if (!isOverhand && grenadeDropList.contains(player)) {
                grenadeDropList.remove(player);
                return;
            }

            var amount = item.getAmount();

            if (amount > 1) item.setAmount(amount - 1);
            else player.getEquipment().setItem(hand, air);


            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHAIN_STEP, 1, 1);
            throwGrenade(player, isOverhand, (long) (Math.random() * 40L) + 70L);
        }

        @EventHandler
        private void onEntityShootBow(EntityShootBowEvent event) {
            var bow = event.getBow();
            var projectile = event.getProjectile();
            var enchantments = bow.getEnchantments();
            if (bow != null && bow.getType() == Material.CROSSBOW && (projectile instanceof AbstractArrow)) {
                var arrow = ((AbstractArrow) projectile);
                if (enchantments.containsKey(Enchantment.PIERCING)) {
                    var piercing = enchantments.get(Enchantment.PIERCING);
                    arrow.setPierceLevel(piercing);
                    arrow.setCustomName(String.valueOf(piercing));
                    arrow.setCustomNameVisible(false);
                }
                if (enchantments.containsKey(Enchantment.ARROW_KNOCKBACK)) {
                    arrow.setKnockbackStrength(2);
                }
            }
            if (enchantments.containsKey(Enchantment.PIERCING)) {
                bow.removeEnchantment(Enchantment.PIERCING);
            }
            if (enchantments.containsKey(Enchantment.ARROW_KNOCKBACK)) {
                bow.removeEnchantment(Enchantment.ARROW_KNOCKBACK);
            }
        }

        @EventHandler
        private void onProjectileLaunch(ProjectileLaunchEvent event) {
            if (!(event.getEntity() instanceof AbstractArrow)) return;
            var arrow = ((AbstractArrow) event.getEntity());
            var shooter = arrow.getShooter();
            if (shooter instanceof Skeleton) {
                var r = Math.random();
                var damage = arrow.getDamage();
                if (r < 0.0173415299) arrow.setDamage(damage * 3.75);
                else if (r < 0.0260122949) arrow.setDamage(damage * 3.50);
                else if (r < 0.0390184423) arrow.setDamage(damage * 3.25);
                else if (r < 0.0585276635) arrow.setDamage(damage * 3.00);
                else if (r < 0.0877914952) arrow.setDamage(damage * 2.75);
                else if (r < 0.131687243) arrow.setDamage(damage * 2.50);
                else if (r < 0.197530864) arrow.setDamage(damage * 2.25);
                else if (r < 0.296296296) arrow.setDamage(damage * 2.00);
                else if (r < 0.444444444) arrow.setDamage(damage * 1.75);
                else if (r < 0.666666667) arrow.setDamage(damage * 1.50);

                if (arrow.getWorld().getEnvironment() == World.Environment.NETHER && Math.random() < 0.66) {
                    arrow.setFireTicks(500);
                }

                if (Math.random() < 0.3) arrow.setKnockbackStrength(2);

            } else if (arrow.isShotFromCrossbow() && !(shooter instanceof Player)) {

                var r = Math.random();
                if (r < 0.0173415299) arrow.setPierceLevel(10);
                else if (r < 0.0260122949) arrow.setPierceLevel(9);
                else if (r < 0.0390184423) arrow.setPierceLevel(8);
                else if (r < 0.0585276635) arrow.setPierceLevel(7);
                else if (r < 0.0877914952) arrow.setPierceLevel(6);
                else if (r < 0.131687243) arrow.setPierceLevel(5);
                else if (r < 0.197530864) arrow.setPierceLevel(4);
                else if (r < 0.296296296) arrow.setPierceLevel(3);
                else if (r < 0.444444444) arrow.setPierceLevel(2);
                else if (r < 0.666666667) arrow.setPierceLevel(1);

                if (Math.random() < 0.3) arrow.setKnockbackStrength(2);
            } else if (shooter instanceof Player && !arrow.isShotFromCrossbow()) {
                var player = ((Player) shooter);
                var equipment = player.getEquipment();
                if (equipment == null) return;
                var offHand = equipment.getItemInOffHand();
                ItemStack itemArrow = null;
                if (offHand != null && (offHand.getType() == Material.ARROW || offHand.getType() == Material.TIPPED_ARROW || offHand.getType() == Material.SPECTRAL_ARROW)) {
                    itemArrow = offHand;
                } else {
                    var contents = player.getInventory().getContents();
                    for (var content : contents) {
                        if (contents != null && (content.getType() == Material.ARROW || content.getType() == Material.TIPPED_ARROW || content.getType() == Material.SPECTRAL_ARROW)) {
                            itemArrow = content;
                            break;
                        }
                    }
                    if (itemArrow == null) return;
                }
                var enchants = itemArrow.getEnchantments();
                if (enchants.containsKey(Enchantment.ARROW_DAMAGE)) {
                    var damage = arrow.getDamage();
                    var power = enchants.get(Enchantment.ARROW_DAMAGE);
                    switch (power) {
                        case 1 -> arrow.setDamage(damage * 1.50);
                        case 2 -> arrow.setDamage(damage * 1.75);
                        case 3 -> arrow.setDamage(damage * 2);
                        case 4 -> arrow.setDamage(damage * 2.25);
                        case 5 -> arrow.setDamage(damage * 2.5);
                        case 6 -> arrow.setDamage(damage * 2.75);
                        case 7 -> arrow.setDamage(damage * 3);
                        case 8 -> arrow.setDamage(damage * 3.25);
                        case 9 -> arrow.setDamage(damage * 3.50);
                        case 10 -> arrow.setDamage(damage * 3.75);
                    }

                    arrow.setCustomName(String.valueOf(power));
                    arrow.setCustomNameVisible(false);
                }
                if (enchants.containsKey(Enchantment.ARROW_FIRE)) {
                    arrow.setFireTicks(500);
                }
                if (enchants.containsKey(Enchantment.ARROW_KNOCKBACK)) {
                    arrow.setKnockbackStrength(2);
                }

            }
        }

        @EventHandler
        private void arrowPickup(PlayerPickupArrowEvent event) {
            var arrow = event.getArrow();
            var item = event.getItem();
            var stack = item.getItemStack();
            if (arrow.getCustomName() != null) {
                var power = Integer.parseInt(arrow.getCustomName());
                if (Math.random() < Math.pow(0.9, Math.pow(power, 1.5))) {
                    power--;
                }
                if (power > 0) {
                    stack.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, power);
                }
            }
            if (arrow.getKnockbackStrength() == 2) {
                stack.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);

                item.setItemStack(stack);
            }
        }

        @EventHandler
        private void onItemUse(PlayerInteractEvent event) {
            var item = event.getItem();
            var player = event.getPlayer();
            if (item == null) return;
            var action = event.getAction();
            // charging crossbow
            if (item.getType() == Material.CROSSBOW) {
                var equipment = player.getEquipment();
                if (equipment == null) return;
                var offHand = equipment.getItemInOffHand();
                ItemStack arrow = null;
                if (offHand != null && (offHand.getType() == Material.SPECTRAL_ARROW || offHand.getType() == Material.TIPPED_ARROW || offHand.getType() == Material.ARROW))
                    arrow = offHand;
                else {
                    var contents = player.getInventory().getContents();
                    for (var content : contents) {
                        if (content.getType() == Material.SPECTRAL_ARROW || content.getType() == Material.TIPPED_ARROW || content.getType() == Material.ARROW) {
                            arrow = content;
                            break;
                        }
                    }
                }
                if (arrow == null) return;
                var arrowEnchants = arrow.getEnchantments();
                if (arrowEnchants.containsKey(Enchantment.ARROW_DAMAGE)) {
                    item.addUnsafeEnchantment(Enchantment.PIERCING, arrowEnchants.get(Enchantment.ARROW_DAMAGE));
                } else {
                    if (item.containsEnchantment(Enchantment.PIERCING)) item.removeEnchantment(Enchantment.PIERCING);
                }
                if (arrowEnchants.containsKey(Enchantment.ARROW_KNOCKBACK)) {
                    item.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
                } else {
                    if (item.containsEnchantment(Enchantment.ARROW_KNOCKBACK))
                        item.removeEnchantment(Enchantment.ARROW_KNOCKBACK);
                }
            }

            // using chisel
            if (isChisel(item)) {
                if (player.getCooldown(item.getType()) > 0.0) {
                    event.setCancelled(true);
                    return;
                }

                var block = event.getClickedBlock();
                if (block == null) return;
                var material = block.getType();

                if (action == Action.LEFT_CLICK_BLOCK) {
                    player.sendMessage(ChatColor.RED + "Right click to use a chisel");
                    event.setCancelled(true);
                    return;
                }
                if (!(action == Action.RIGHT_CLICK_BLOCK)) return;
                var fortune = 1;
                if (item.getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_BLOCKS))
                    fortune += item.getItemMeta().getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);

                switch (material) {
                    case GLOWSTONE -> {
                        if (Math.random() < .75) {
                            var amount = 1;
                            for (int i = 0; i < 3; i++) {
                                if (Math.pow(Math.random(), fortune) < .2) amount++;
                            }
                            block.setType(Material.AIR);
                            block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.GLOWSTONE_DUST, amount));
                        }
                    }
                    case MELON -> {
                        var amount = 1;
                        for (int i = 0; i < 8; i++) {
                            if (Math.pow(Math.random(), fortune) < .2) amount++;
                        }
                        block.setType(Material.AIR);
                        block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.MELON_SLICE, amount));
                    }
                    case SEA_LANTERN -> {
                        if (Math.random() < .75) {
                            var amount1 = 1;
                            var amount2 = 0;
                            for (int i = 0; i < 4; i++) {
                                if (Math.pow(Math.random(), fortune) < .2) amount1++;
                            }
                            for (int i = 0; i < 4; i++) {
                                if (Math.pow(Math.random(), fortune) < .05) amount2++;
                            }
                            block.setType(Material.AIR);
                            block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.PRISMARINE_CRYSTALS, amount1));
                            if (amount2 > 0)
                                block.getWorld().dropItem(block.getLocation(), new ItemStack(Material.PRISMARINE_SHARD, amount2));
                        }
                    }
                    case COAL_ORE -> {
                        if (Math.random() < 0.2 - 0.0136363636 * fortune) {
                            block.setType(Material.AIR);
                            player.getWorld().dropItem(block.getLocation(), new ItemStack(Material.COAL));
                        } else if (Math.random() < 0.02 * fortune) {
                            var l1 = player.getEyeLocation().clone();
                            var l2 = block.getLocation();
                            l1.setX((l1.getX() + l2.getX()) / 2.0);
                            l1.setY((l1.getY() + l2.getY()) / 2.0);
                            l1.setZ((l1.getZ() + l2.getZ()) / 2.0);
                            player.getWorld().dropItem(l1, new ItemStack(Material.COAL));
                        }
                    }
                    case DIAMOND_ORE -> {
                        var type = item.getType();
                        if ((type == Material.IRON_HOE || type == Material.DIAMOND_HOE || type == Material.NETHERITE_HOE) && Math.random() < .1 - 0.00606060606 * fortune) {
                            block.setType(Material.AIR);
                            player.getWorld().dropItem(block.getLocation(), new ItemStack(Material.DIAMOND));
                        } else if (Math.random() < 0.0095 * fortune) {
                            var l1 = player.getEyeLocation().clone();
                            var l2 = block.getLocation();
                            l1.setX((l1.getX() + l2.getX()) / 2.0);
                            l1.setY((l1.getY() + l2.getY()) / 2.0);
                            l1.setZ((l1.getZ() + l2.getZ()) / 2.0);
                            player.getWorld().dropItem(l1, new ItemStack(Material.DIAMOND));
                        }
                    }
                    case EMERALD_ORE -> {
                        var type = item.getType();
                        if ((type == Material.IRON_HOE || type == Material.DIAMOND_HOE || type == Material.NETHERITE_HOE) && Math.random() < .2 - 0.0121212121 * fortune) {
                            block.setType(Material.AIR);
                            player.getWorld().dropItem(block.getLocation(), new ItemStack(Material.EMERALD));
                        } else if (Math.random() < 0.023 * fortune) {
                            var l1 = player.getEyeLocation().clone();
                            var l2 = block.getLocation();
                            l1.setX((l1.getX() + l2.getX()) / 2.0);
                            l1.setY((l1.getY() + l2.getY()) / 2.0);
                            l1.setZ((l1.getZ() + l2.getZ()) / 2.0);
                            player.getWorld().dropItem(l1, new ItemStack(Material.EMERALD));
                        }
                    }
                    case NETHER_GOLD_ORE -> {
                        if (Math.random() < .3 - 0.0151515152 * fortune) {
                            block.setType(Material.AIR);
                            var nugget = new ItemStack(Material.GOLD_NUGGET);
                            generatePurity(nugget, 0.3);
                            player.getWorld().dropItem(block.getLocation(), nugget);
                        } else if (Math.random() < 0.085 * fortune) {
                            var amount = (int) (Math.random() * 3) + 1;
                            var nugget = new ItemStack(Material.GOLD_NUGGET, amount);
                            generatePurity(nugget, 0.3);
                            var l1 = player.getEyeLocation().clone();
                            var l2 = block.getLocation();
                            l1.setX((l1.getX() + l2.getX()) / 2.0);
                            l1.setY((l1.getY() + l2.getY()) / 2.0);
                            l1.setZ((l1.getZ() + l2.getZ()) / 2.0);
                            player.getWorld().dropItem(l1, nugget);
                        }
                    }
                    case LAPIS_ORE -> {
                        var type = item.getType();
                        if ((type == Material.STONE_HOE || type == Material.IRON_HOE || type == Material.DIAMOND_HOE || type == Material.NETHERITE_HOE) && Math.random() < .21 - 0.0151515152 * fortune) {
                            block.setType(Material.AIR);
                            var amount = (int) ((Math.random()) * 3) + 1;
                            player.getWorld().dropItem(block.getLocation(), new ItemStack(Material.LAPIS_LAZULI, amount));

                        } else if (Math.random() < 0.075 * fortune) {
                            var l1 = player.getEyeLocation().clone();
                            var l2 = block.getLocation();
                            l1.setX((l1.getX() + l2.getX()) / 2.0);
                            l1.setY((l1.getY() + l2.getY()) / 2.0);
                            l1.setZ((l1.getZ() + l2.getZ()) / 2.0);
                            player.getWorld().dropItem(l1, new ItemStack(Material.LAPIS_LAZULI, (int) ((Math.random()) * 3) + 1));
                        }
                    }
                    case REDSTONE_ORE -> {
                        var type = item.getType();
                        if ((type == Material.IRON_HOE || type == Material.DIAMOND_HOE || type == Material.NETHERITE_HOE) && Math.random() < .205 - 0.0151515152 * fortune) {
                            block.setType(Material.AIR);
                            var amount = (int) ((Math.random()) * 3) + 1;
                            player.getWorld().dropItem(block.getLocation(), new ItemStack(Material.REDSTONE, amount));
                        } else {
                            var l1 = player.getEyeLocation().clone();
                            var l2 = block.getLocation();
                            l1.setX((l1.getX() + l2.getX()) / 2.0);
                            l1.setY((l1.getY() + l2.getY()) / 2.0);
                            l1.setZ((l1.getZ() + l2.getZ()) / 2.0);
                            if (Math.random() < 0.03 * fortune) {
                                player.getWorld().dropItem(l1, new ItemStack(Material.REDSTONE, (int) ((Math.random()) * 2) + 1));
                            }
                        }
                    }
                    case NETHER_QUARTZ_ORE -> {
                        if (Math.random() < .3 - 0.0151515152 * fortune) {
                            block.setType(Material.AIR);
                            player.getWorld().dropItem(block.getLocation(), new ItemStack(Material.QUARTZ));
                        } else if (Math.random() < 0.07 * fortune) {
                            var l1 = player.getEyeLocation().clone();
                            var l2 = block.getLocation();
                            l1.setX((l1.getX() + l2.getX()) / 2.0);
                            l1.setY((l1.getY() + l2.getY()) / 2.0);
                            l1.setZ((l1.getZ() + l2.getZ()) / 2.0);
                            player.getWorld().dropItem(l1, new ItemStack(Material.QUARTZ));
                        }
                    }
                    default -> {
                        player.sendMessage(ChatColor.RED + "You cannot chisel this block.");
                        event.setCancelled(true);
                        return;
                    }
                }

                (player).swingMainHand();
                var delay = 20;
                switch (item.getType()) {
                    case WOODEN_HOE -> delay = 45;
                    case STONE_HOE -> delay = 30;
                    case IRON_HOE -> delay = 20;
                    case DIAMOND_HOE -> delay = 13;
                    case NETHERITE_HOE -> delay = 8;
                    case GOLDEN_HOE -> delay = 4;
                }
                player.setCooldown(Material.WOODEN_HOE, delay);
                player.setCooldown(Material.STONE_HOE, delay);
                player.setCooldown(Material.IRON_HOE, delay);
                player.setCooldown(Material.DIAMOND_HOE, delay);
                player.setCooldown(Material.NETHERITE_HOE, delay);
                player.setCooldown(Material.GOLDEN_HOE, delay);

                if (material != Material.MELON)
                    player.getWorld().playSound(block.getLocation(), Sound.BLOCK_CHAIN_STEP, 1, (long) (Math.random() * 0.5 + 0.5));
                else
                    player.getWorld().playSound(block.getLocation(), Sound.BLOCK_PUMPKIN_CARVE, 1, (long) (Math.random() * 0.5 + 0.5));

                //todo dull & durability decrease

                var meta = item.getItemMeta();
                var unbreaking = 0;
                if (meta.hasEnchant(Enchantment.DURABILITY)) unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY);
                var durability = item.getType().getMaxDurability();

                if (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS)) {
                    var level = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);
                    if (Math.random() < Math.pow(durability + 1, Math.pow(level, 0.75) / 10.0 - 1) / (unbreaking + 1)) {
                        meta.removeEnchant(Enchantment.LOOT_BONUS_BLOCKS);
                        if (level > 1) {
                            meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, level - 1, true);
                        }
                        item.setItemMeta(meta);
                    }
                }

                if (Math.random() < 1.0 / (1.0 + unbreaking)) {
                    var damageable = (Damageable) meta;
                    var max = item.getType().getMaxDurability();
                    var damage = damageable.getDamage() + 1;
                    if (damage == max) {
                        item = air;
                    } else {
                        damageable.setDamage(damage);
                        item.setItemMeta(meta);
                    }
                }
                player.getEquipment().setItem(event.getHand(), item);

                event.setCancelled(true);
                return;
            }

            // using saw
            if (isSaw(item)) {
                if (player.getCooldown(item.getType()) > 0.0) {
                    event.setCancelled(true);
                    return;
                }

                var block = event.getClickedBlock();
                if (block == null) return;
                var material = block.getType();


                if (action == Action.LEFT_CLICK_BLOCK && material != Material.BEE_NEST) {
                    player.sendMessage(ChatColor.RED + "Right click to use a saw");
                    event.setCancelled(true);
                    return;
                }
                if (!(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK)) return;
                var efficiency = 1;
                if (item.getItemMeta().hasEnchant(Enchantment.DIG_SPEED))
                    efficiency += item.getItemMeta().getEnchantLevel(Enchantment.DIG_SPEED);

                switch (material) {
                    case BEE_NEST -> {
                        player.sendMessage(ChatColor.RED + "Use left click with saw to obtain a bee nest");
                        return;
                    }
                    case ACACIA_LEAVES, BIRCH_LEAVES, BRAIN_CORAL, BRAIN_CORAL_FAN, BRAIN_CORAL_WALL_FAN, DEAD_BRAIN_CORAL_FAN, DEAD_BRAIN_CORAL_WALL_FAN, BROWN_MUSHROOM_BLOCK,
                            BUBBLE_CORAL, DEAD_BUBBLE_CORAL_FAN, BUBBLE_CORAL_WALL_FAN, DEAD_BUBBLE_CORAL_WALL_FAN, DARK_OAK_LEAVES, DEAD_BUSH, FERN, FIRE_CORAL,
                            FIRE_CORAL_FAN, FIRE_CORAL_WALL_FAN, DEAD_FIRE_CORAL_FAN, DEAD_FIRE_CORAL_WALL_FAN, GRASS, HORN_CORAL, HORN_CORAL_FAN, HORN_CORAL_WALL_FAN, DEAD_HORN_CORAL_FAN, DEAD_HORN_CORAL_WALL_FAN, JUNGLE_LEAVES, NETHER_SPROUTS, OAK_LEAVES,
                            RED_MUSHROOM_BLOCK, SEAGRASS, SNOW, SNOW_BLOCK, SPRUCE_LEAVES, TUBE_CORAL, TUBE_CORAL_FAN, TUBE_CORAL_WALL_FAN, DEAD_TUBE_CORAL_FAN, DEAD_TUBE_CORAL_WALL_FAN, TURTLE_EGG,
                            TWISTING_VINES, VINE, WEEPING_VINES -> {
                        if (Math.pow(Math.random(), efficiency) < 0.8) {
                            if (material == Material.BRAIN_CORAL_WALL_FAN) material = Material.BRAIN_CORAL_FAN;
                            else if (material == Material.BUBBLE_CORAL_WALL_FAN) material = Material.BUBBLE_CORAL_FAN;
                            else if (material == Material.FIRE_CORAL_WALL_FAN) material = Material.FIRE_CORAL_FAN;
                            else if (material == Material.HORN_CORAL_WALL_FAN) material = Material.HORN_CORAL_FAN;
                            else if (material == Material.TUBE_CORAL_WALL_FAN) material = Material.TUBE_CORAL_FAN;
                            else if (material == Material.DEAD_BRAIN_CORAL_WALL_FAN)
                                material = Material.DEAD_BRAIN_CORAL_FAN;
                            else if (material == Material.DEAD_BUBBLE_CORAL_WALL_FAN)
                                material = Material.DEAD_BUBBLE_CORAL_FAN;
                            else if (material == Material.DEAD_FIRE_CORAL_WALL_FAN)
                                material = Material.DEAD_FIRE_CORAL_FAN;
                            else if (material == Material.DEAD_HORN_CORAL_WALL_FAN)
                                material = Material.DEAD_HORN_CORAL_FAN;
                            else if (material == Material.DEAD_TUBE_CORAL_WALL_FAN)
                                material = Material.DEAD_TUBE_CORAL_FAN;

                            if (material == Material.TURTLE_EGG) {
                                var amount = ((TurtleEgg) block.getBlockData()).getEggs();
                                block.getWorld().dropItem(block.getLocation(), new ItemStack(material, amount));
                                block.setType(Material.AIR);
                            } else {
                                block.getWorld().dropItem(block.getLocation(), new ItemStack(material));
                                block.setType(Material.AIR);
                            }
                        }
                    }

                    case BLACK_STAINED_GLASS, BLACK_STAINED_GLASS_PANE, BLUE_ICE, BLUE_STAINED_GLASS,
                            BLUE_STAINED_GLASS_PANE, BOOKSHELF, BROWN_STAINED_GLASS,
                            BROWN_STAINED_GLASS_PANE, CLAY, COBWEB,
                            CYAN_STAINED_GLASS, CYAN_STAINED_GLASS_PANE, GLASS, GLASS_PANE, GLOWSTONE,
                            GRASS_BLOCK, GRASS_PATH, GRAY_STAINED_GLASS, GRAY_STAINED_GLASS_PANE, GREEN_STAINED_GLASS,
                            GREEN_STAINED_GLASS_PANE, HORN_CORAL_BLOCK, ICE, LIGHT_BLUE_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS_PANE,
                            LIGHT_GRAY_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS_PANE, LIME_STAINED_GLASS, LIME_STAINED_GLASS_PANE,
                            MAGENTA_STAINED_GLASS, MAGENTA_STAINED_GLASS_PANE, MELON, MYCELIUM, ORANGE_STAINED_GLASS,
                            ORANGE_STAINED_GLASS_PANE, PACKED_ICE, PINK_STAINED_GLASS, PINK_STAINED_GLASS_PANE, PURPLE_STAINED_GLASS,
                            PURPLE_STAINED_GLASS_PANE, TUBE_CORAL_BLOCK, WHITE_STAINED_GLASS, WHITE_STAINED_GLASS_PANE, YELLOW_STAINED_GLASS,
                            YELLOW_STAINED_GLASS_PANE -> {//todo add all blocks

                        if (Math.pow(Math.random(), efficiency) < 0.4) {
                            block.getWorld().dropItem(block.getLocation(), new ItemStack(material));
                            block.setType(Material.AIR);
                        }
                    }

                    case BRAIN_CORAL_BLOCK, BUBBLE_CORAL_BLOCK, COAL_ORE, FIRE_CORAL_BLOCK, GILDED_BLACKSTONE, NETHER_GOLD_ORE,
                            NETHER_QUARTZ_ORE, CRIMSON_NYLIUM, WARPED_NYLIUM, STONE -> {
                        if (Math.pow(Math.random(), efficiency) < 0.1) {
                            block.getWorld().dropItem(block.getLocation(), new ItemStack(material));
                            block.setType(Material.AIR);
                        }
                    }


                    case LAPIS_ORE -> {
                        if (item.getType() != Material.WOODEN_HOE && Math.pow(Math.random(), efficiency) < 0.08) {
                            block.getWorld().dropItem(block.getLocation(), new ItemStack(material));
                            block.setType(Material.AIR);
                        }
                    }

                    case EMERALD_ORE, REDSTONE_ORE -> {
                        if (item.getType() != Material.WOODEN_HOE && item.getType() != Material.STONE_HOE && Math.pow(Math.random(), efficiency) < 0.04) {
                            block.getWorld().dropItem(block.getLocation(), new ItemStack(material));
                            block.setType(Material.AIR);
                        }
                    }

                    case DIAMOND_ORE -> {
                        if (item.getType() != Material.WOODEN_HOE && item.getType() != Material.STONE_HOE && Math.pow(Math.random(), efficiency) < 0.001) {
                            block.getWorld().dropItem(block.getLocation(), new ItemStack(material));
                            block.setType(Material.AIR);
                        }
                    }

                    case DIRT -> {
                        player.sendMessage(ChatColor.RED + "You cannot saw this item.");
                        event.setCancelled(true);
                        return;
                    }
                    default -> {
                        player.sendMessage(ChatColor.RED + "You cannot saw this item.");
                        return;
                    }
                }

                (player).swingMainHand();
                var delay = 70;
                switch (item.getType()) {
                    case WOODEN_HOE -> delay = 60;
                    case STONE_HOE -> delay = 45;
                    case IRON_HOE -> delay = 35;
                    case DIAMOND_HOE -> delay = 28;
                    case NETHERITE_HOE -> delay = 23;
                    case GOLDEN_HOE -> delay = 19;
                }
                player.setCooldown(Material.WOODEN_HOE, delay);
                player.setCooldown(Material.STONE_HOE, delay);
                player.setCooldown(Material.IRON_HOE, delay);
                player.setCooldown(Material.DIAMOND_HOE, delay);
                player.setCooldown(Material.NETHERITE_HOE, delay);
                player.setCooldown(Material.GOLDEN_HOE, delay);

                player.getWorld().playSound(block.getLocation(), Sound.UI_STONECUTTER_TAKE_RESULT, 1, 1);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> player.getWorld().playSound(block.getLocation(), Sound.UI_STONECUTTER_TAKE_RESULT, 1, 4), (long) (delay / 3.0 * 2.0));

                //todo dull & durability decrease

                var meta = item.getItemMeta();
                var unbreaking = 0;
                if (meta.hasEnchant(Enchantment.DURABILITY)) unbreaking = meta.getEnchantLevel(Enchantment.DURABILITY);
                var durability = item.getType().getMaxDurability();

                if (meta.hasEnchant(Enchantment.DIG_SPEED)) {
                    var level = meta.getEnchantLevel(Enchantment.DIG_SPEED);
                    if (Math.random() < Math.pow(durability + 1, Math.pow(level, 0.75) / 10.0 - 1) / (unbreaking + 1)) {
                        meta.removeEnchant(Enchantment.DIG_SPEED);
                        if (level > 1) {
                            meta.addEnchant(Enchantment.DIG_SPEED, level - 1, true);
                        }
                        item.setItemMeta(meta);
                    }
                }

                if (Math.random() < 1.0 / (1.0 + unbreaking)) {
                    var damageable = (Damageable) meta;
                    var max = item.getType().getMaxDurability();
                    var damage = damageable.getDamage() + 1;
                    if (damage == max) {
                        item = air;
                    } else {
                        damageable.setDamage(damage);
                        item.setItemMeta(meta);
                    }
                }
                player.getEquipment().setItem(event.getHand(), item);

                event.setCancelled(true);
                return;
            }

            // prevent tilling with chisel / saw
            if ((

                    isChisel(item) ||

                            isSaw(item)) && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                var block = event.getClickedBlock();
                if (block == null) return;
                var type = block.getType();
                if (type != null && (type == Material.DIRT || type == Material.GRASS_BLOCK || type == Material.GRASS_PATH)) {
                    event.setCancelled(true);
                    return;
                }
            }

            // sharpening tools
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (

                    isNetheriteRod(item) ||

                            isCoarseBlade(item) ||

                            isFineBlade(item) ||

                            isIronRod(item))) {
                event.setCancelled(true);
                return;
            }

            // Grenade
            if (isGrenade(item) && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
                ItemStack finalItem = item;
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> grenadeUse(finalItem, action, event.getPlayer(), event.getHand()), 2L);
                event.setCancelled(true);
                return;
            }

            // Ancient Dust
            if (item.getType() == Material.REDSTONE &&

                    isDust(item)) {
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
            if (

                    isSifter(item)) {
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
            if (item.getAmount() == 1 &&

                    hasPurity(item)) {
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
                if (!isMetal(items) || items.getEnchantments().keySet().size() > 0) return;
                var player = event.getWhoClicked();
                event.setCancelled(true);
                var newStacks = dragSplitStackPurities(items, slots.size());
                var cursor = newStacks[slots.size()];
                int i = 0;
                for (var num : slots) {
                    var slotItem = view.getItem(num);

                    if (slotItem != null && slotItem.getType() != Material.AIR && canCombine(slotItem, newStacks[i])) {
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
                if (!isMetal(cursorItem) || cursorItem.getEnchantments().keySet().size() > 0) return;
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


            if ((isGrenade(entityStack) && entityStack.getType() == Material.TNT) || isGrenade(targetStack) && targetStack.getType() == Material.TNT) {
                event.setCancelled(true);
                return;
            }

            if (itemPickupList.contains(entity) || itemPickupList.contains(target)) {
                itemPickupList.remove(entity);
                itemPickupList.remove(target);
                itemPickupList.add(target);
                itemPickupList.add(entity);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                    itemPickupList.remove(entity);
                    itemPickupList.remove(target);
                }, 2L);
            }

            if (isMetal(entityStack) && canCombine(entityStack, targetStack)) {
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
            var contents = event.getContents().getContents();
            for (var content : contents) {
                var meta = content.getItemMeta();
                if (meta == null) continue;
                var lore = meta.getLore();
                if (lore == null) continue;
                if (lore.contains(DiplomacyRecipes.getInstance().IRON_SLURRY_LORE)
                        || lore.contains(DiplomacyRecipes.getInstance().GOLD_SLURRY_LORE)
                        || lore.contains(DiplomacyRecipes.getInstance().NETHERITE_SLURRY_LORE))
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


            // Cancel brewing dust
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

            // Sharpening tools
            if (event.getSlotType() == InventoryType.SlotType.RESULT) {
                if (currentItem != null && currentItem.getItemMeta() != null) {

                    // Using whetstone
                    var meta = currentItem.getItemMeta();
                    if ((meta.hasEnchant(Enchantment.DAMAGE_ALL) && currentItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 5)
                            || (meta.hasEnchant(Enchantment.DIG_SPEED) && currentItem.getEnchantmentLevel(Enchantment.DIG_SPEED) == 5)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) == 5)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) == 5 && !isSifter(currentItem))
                            || (meta.hasEnchant(Enchantment.IMPALING) && currentItem.getEnchantmentLevel(Enchantment.IMPALING) == 5)
                    ) {
                        ItemStack whetstone = null;
                        for (int i = 0; i < view.countSlots(); i++) {
                            if (view.getSlotType(i) != InventoryType.SlotType.CRAFTING) continue;
                            if (isWhetstone(view.getItem(i))) {
                                whetstone = new ItemStack(view.getItem(i));
                                break;
                            }
                        }
                        if (whetstone == null) {
                            event.setCancelled(true);
                            return;
                        }
                        var stoneMeta = whetstone.getItemMeta();
                        var lore = stoneMeta.getLore();
                        var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                        var uses = Integer.parseInt(lore.get(line + 1));
                        if (uses > 1) {
                            lore.set(line + 1, String.valueOf(uses - 1));
                            stoneMeta.setLore(lore);
                            whetstone.setItemMeta(stoneMeta);
                            whetstone.setAmount(1);
                            var first = event.getWhoClicked().getInventory().firstEmpty();
                            if (first == -1) {
                                event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), whetstone);
                            } else {
                                event.getWhoClicked().getInventory().addItem(whetstone);
                            }
                            return;
                        }
                    }

                    // Using waterstone
                    meta = currentItem.getItemMeta();
                    if ((meta.hasEnchant(Enchantment.DAMAGE_ALL) && currentItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 6)
                            || (meta.hasEnchant(Enchantment.DIG_SPEED) && currentItem.getEnchantmentLevel(Enchantment.DIG_SPEED) == 6)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) == 6)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) == 6 && !isSifter(currentItem))
                            || (meta.hasEnchant(Enchantment.IMPALING) && currentItem.getEnchantmentLevel(Enchantment.IMPALING) == 6)
                    ) {
                        ItemStack waterstone = null;
                        for (int i = 0; i < view.countSlots(); i++) {
                            if (view.getSlotType(i) != InventoryType.SlotType.CRAFTING) continue;
                            if (isWaterstone(view.getItem(i))) {
                                waterstone = new ItemStack(view.getItem(i));
                                break;
                            }
                        }
                        if (waterstone == null) {
                            event.setCancelled(true);
                            return;
                        }
                        var stoneMeta = waterstone.getItemMeta();
                        var lore = stoneMeta.getLore();
                        var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                        var uses = Integer.parseInt(lore.get(line + 1));
                        if (uses > 1) {
                            lore.set(line + 1, String.valueOf(uses - 1));
                            stoneMeta.setLore(lore);
                            waterstone.setItemMeta(stoneMeta);
                            waterstone.setAmount(1);
                            var first = event.getWhoClicked().getInventory().firstEmpty();
                            if (first == -1) {
                                event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), waterstone);
                            } else {
                                event.getWhoClicked().getInventory().addItem(waterstone);
                            }
                            first = event.getWhoClicked().getInventory().firstEmpty();
                            if (first == -1) {
                                event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), new ItemStack(Material.GLASS_BOTTLE, 1));
                            } else {
                                event.getWhoClicked().getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE, 1));
                            }
                            return;
                        }
                    }

                    // Using coarse blades
                    meta = currentItem.getItemMeta();
                    if ((meta.hasEnchant(Enchantment.DAMAGE_ALL) && currentItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 7)
                            || (meta.hasEnchant(Enchantment.DIG_SPEED) && currentItem.getEnchantmentLevel(Enchantment.DIG_SPEED) == 7)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) == 7)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) == 7 && !isSifter(currentItem))
                            || (meta.hasEnchant(Enchantment.IMPALING) && currentItem.getEnchantmentLevel(Enchantment.IMPALING) == 7)
                    ) {
                        ItemStack coarseBlades = null;
                        for (int i = 0; i < view.countSlots(); i++) {
                            if (view.getSlotType(i) != InventoryType.SlotType.CRAFTING) continue;
                            if (isCoarseBlade(view.getItem(i))) {
                                coarseBlades = new ItemStack(view.getItem(i));
                                break;
                            }
                        }
                        if (coarseBlades == null) {
                            event.setCancelled(true);
                            return;
                        }
                        var bladeMeta = coarseBlades.getItemMeta();
                        var lore = bladeMeta.getLore();
                        var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                        var uses = Integer.parseInt(lore.get(line + 1));
                        if (uses > 1) {
                            lore.set(line + 1, String.valueOf(uses - 1));
                            bladeMeta.setLore(lore);
                            coarseBlades.setItemMeta(bladeMeta);
                            coarseBlades.setAmount(1);
                            var first = event.getWhoClicked().getInventory().firstEmpty();
                            if (first == -1) {
                                event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), coarseBlades);
                            } else {
                                event.getWhoClicked().getInventory().addItem(coarseBlades);
                            }
                            return;
                        }
                    }

                    // using fine blades
                    meta = currentItem.getItemMeta();
                    if ((meta.hasEnchant(Enchantment.DAMAGE_ALL) && currentItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 8)
                            || (meta.hasEnchant(Enchantment.DIG_SPEED) && currentItem.getEnchantmentLevel(Enchantment.DIG_SPEED) == 8)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) == 8)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) == 8 && !isSifter(currentItem))
                            || (meta.hasEnchant(Enchantment.IMPALING) && currentItem.getEnchantmentLevel(Enchantment.IMPALING) == 8)
                    ) {
                        ItemStack fineBlades = null;
                        for (int i = 0; i < view.countSlots(); i++) {
                            if (view.getSlotType(i) != InventoryType.SlotType.CRAFTING) continue;
                            if (isFineBlade(view.getItem(i))) {
                                fineBlades = new ItemStack(view.getItem(i));
                                break;
                            }
                        }
                        if (fineBlades == null) {
                            event.setCancelled(true);
                            return;
                        }
                        var bladeMeta = fineBlades.getItemMeta();
                        var lore = bladeMeta.getLore();
                        var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                        var uses = Integer.parseInt(lore.get(line + 1));
                        if (uses > 1) {
                            lore.set(line + 1, String.valueOf(uses - 1));
                            bladeMeta.setLore(lore);
                            fineBlades.setItemMeta(bladeMeta);
                            fineBlades.setAmount(1);
                            var first = event.getWhoClicked().getInventory().firstEmpty();
                            if (first == -1) {
                                event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), fineBlades);
                            } else {
                                event.getWhoClicked().getInventory().addItem(fineBlades);
                            }
                            return;
                        }
                    }


                    // Using iron rod
                    meta = currentItem.getItemMeta();
                    if ((meta.hasEnchant(Enchantment.DAMAGE_ALL) && currentItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 9)
                            || (meta.hasEnchant(Enchantment.DIG_SPEED) && currentItem.getEnchantmentLevel(Enchantment.DIG_SPEED) == 9)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) == 9)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) == 9 && !isSifter(currentItem))
                            || (meta.hasEnchant(Enchantment.IMPALING) && currentItem.getEnchantmentLevel(Enchantment.IMPALING) == 9)
                    ) {
                        ItemStack ironRod = null;
                        for (int i = 0; i < view.countSlots(); i++) {
                            if (view.getSlotType(i) != InventoryType.SlotType.CRAFTING) continue;
                            if (isIronRod(view.getItem(i))) {
                                ironRod = new ItemStack(view.getItem(i));
                                break;
                            }
                        }
                        if (ironRod == null) {
                            event.setCancelled(true);
                            return;
                        }
                        var rodMeta = ironRod.getItemMeta();
                        var lore = rodMeta.getLore();
                        var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                        var uses = Integer.parseInt(lore.get(line + 1));
                        if (uses > 1) {
                            lore.set(line + 1, String.valueOf(uses - 1));
                            rodMeta.setLore(lore);
                            ironRod.setItemMeta(rodMeta);
                            ironRod.setAmount(1);
                            var first = event.getWhoClicked().getInventory().firstEmpty();
                            if (first == -1) {
                                event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), ironRod);
                            } else {
                                event.getWhoClicked().getInventory().addItem(ironRod);
                            }
                            return;
                        }
                    }


                    // Using netherite rod
                    meta = currentItem.getItemMeta();
                    if ((meta.hasEnchant(Enchantment.DAMAGE_ALL) && currentItem.getEnchantmentLevel(Enchantment.DAMAGE_ALL) == 10)
                            || (meta.hasEnchant(Enchantment.DIG_SPEED) && currentItem.getEnchantmentLevel(Enchantment.DIG_SPEED) == 10)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS) == 10)
                            || (meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS) && currentItem.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) == 10 && !isSifter(currentItem))
                            || (meta.hasEnchant(Enchantment.IMPALING) && currentItem.getEnchantmentLevel(Enchantment.IMPALING) == 10)
                    ) {
                        ItemStack netheriteRod = null;
                        for (int i = 0; i < view.countSlots(); i++) {
                            if (view.getSlotType(i) != InventoryType.SlotType.CRAFTING) continue;
                            if (isNetheriteRod(view.getItem(i))) {
                                netheriteRod = new ItemStack(view.getItem(i));
                                break;
                            }
                        }
                        if (netheriteRod == null) {
                            event.setCancelled(true);
                            return;
                        }
                        var rodMeta = netheriteRod.getItemMeta();
                        var lore = rodMeta.getLore();
                        var line = lore.indexOf(DiplomacyRecipes.getInstance().REMAINING_USES_LORE);
                        var uses = Integer.parseInt(lore.get(line + 1));
                        if (uses > 1) {
                            lore.set(line + 1, String.valueOf(uses - 1));
                            rodMeta.setLore(lore);
                            netheriteRod.setItemMeta(rodMeta);
                            netheriteRod.setAmount(1);
                            var first = event.getWhoClicked().getInventory().firstEmpty();
                            if (first == -1) {
                                event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), netheriteRod);
                            } else {
                                event.getWhoClicked().getInventory().addItem(netheriteRod);
                            }
                            return;
                        }
                    }

                    // Crafting iron rod
                    if (isIronRod(currentItem)) {
                        ItemStack chisel = null;
                        for (int i = 0; i < view.countSlots(); i++) {
                            if (view.getSlotType(i) != InventoryType.SlotType.CRAFTING) continue;
                            if (isChisel(view.getItem(i))) {
                                chisel = new ItemStack(view.getItem(i));
                                break;
                            }
                        }
                        if (chisel == null) {
                            event.setCancelled(true);
                            return;
                        }
                        var durability = chisel.getType().getMaxDurability() - ((Damageable) chisel.getItemMeta()).getDamage();
                        var unbreaking = 0;
                        if (chisel.getItemMeta().hasEnchant(Enchantment.DURABILITY))
                            unbreaking = chisel.getEnchantmentLevel(Enchantment.DURABILITY);
                        durability = (int) (durability - 1000.0 / (1.0 + unbreaking));
                        if (durability < 1) return;
                        meta = chisel.getItemMeta();
                        ((Damageable) meta).setDamage(chisel.getType().getMaxDurability() - durability);
                        var nChisel = new ItemStack(chisel);
                        nChisel.setItemMeta(meta);
                        var first = event.getWhoClicked().getInventory().firstEmpty();
                        if (first == -1) {
                            event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), nChisel);
                        } else {
                            event.getWhoClicked().getInventory().addItem(nChisel);
                        }
                        return;
                    }
                    // Crafting netherite rod
                    if (isNetheriteRod(currentItem)) {
                        ItemStack chisel = null;
                        for (int i = 0; i < view.countSlots(); i++) {
                            if (view.getSlotType(i) != InventoryType.SlotType.CRAFTING) continue;
                            if (isChisel(view.getItem(i))) {
                                chisel = new ItemStack(view.getItem(i));
                                break;
                            }
                        }
                        if (chisel == null) {
                            event.setCancelled(true);
                            return;
                        }
                        var durability = chisel.getType().getMaxDurability() - ((Damageable) chisel.getItemMeta()).getDamage();
                        var unbreaking = 0;
                        if (chisel.getItemMeta().hasEnchant(Enchantment.DURABILITY))
                            unbreaking = chisel.getEnchantmentLevel(Enchantment.DURABILITY);
                        durability = (int) (durability - 2500.0 / (1.0 + unbreaking));
                        if (durability < 1) return;
                        meta = chisel.getItemMeta();
                        ((Damageable) meta).setDamage(chisel.getType().getMaxDurability() - durability);
                        var nChisel = new ItemStack(chisel);
                        nChisel.setItemMeta(meta);
                        var first = event.getWhoClicked().getInventory().firstEmpty();
                        if (first == -1) {
                            event.getWhoClicked().getWorld().dropItem(event.getWhoClicked().getLocation(), nChisel);
                        } else {
                            event.getWhoClicked().getInventory().addItem(nChisel);
                        }
                        return;
                    }
                }
            }

            if (view.getType() == InventoryType.GRINDSTONE) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Diplomacy.getInstance(), () -> prepareGrindstone((GrindstoneInventory) view.getTopInventory()), 1L);
                if (event.getRawSlot() == 2 && view.getItem(2) != null && view.getItem(2).getType() != Material.AIR) {
                    var item = view.getItem(2);
                    var enchants = item.getEnchantments();
                    if ((enchants.containsKey(Enchantment.DAMAGE_ALL) && enchants.get(Enchantment.DAMAGE_ALL) == 2)
                            || (enchants.containsKey(Enchantment.ARROW_DAMAGE) && enchants.get(Enchantment.ARROW_DAMAGE) == 2)
                            || (enchants.containsKey(Enchantment.LOOT_BONUS_MOBS) && enchants.get(Enchantment.LOOT_BONUS_MOBS) == 2)
                            || (enchants.containsKey(Enchantment.DIG_SPEED) && enchants.get(Enchantment.DIG_SPEED) == 2)
                            || (enchants.containsKey(Enchantment.IMPALING) && enchants.get(Enchantment.IMPALING) == 2)
                    ) {
                        var itemAmount = item.getAmount();
                        var lapis = view.getItem(1);
                        var amount = lapis.getAmount();
                        if (lapis.getAmount() > itemAmount) {
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(1, new ItemStack(Material.LAPIS_LAZULI, amount - itemAmount)), 0L);
                        } else if (lapis.getAmount() == itemAmount) {
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(1, air), 0L);
                        } else {
                            event.setCancelled(true);
                            return;
                        }
                    } else {
                        var lapis = view.getItem(1);
                        var amount = lapis.getAmount();
                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(1, new ItemStack(Material.LAPIS_LAZULI, amount)), 0L);
                    }
                    return;
                }
                if (event.getClick() == ClickType.DOUBLE_CLICK) return;

                if (event.getClick().isShiftClick()) {
                    if (currentItem == null) return;
                    var type = currentItem.getType();
                    if (!(tools.contains(type) || type == Material.ARROW || type == Material.TIPPED_ARROW || type == Material.SPECTRAL_ARROW)
                            && type != Material.LAPIS_LAZULI) {
                        event.setCancelled(true);
                        return;
                    }
                    if (type == Material.LAPIS_LAZULI) {
                        var slot = event.getRawSlot();
                        if (slot != 1) {
                            var amount = 0;
                            var slotItem = view.getItem(1);
                            if (slotItem != null && slotItem.getType() == Material.LAPIS_LAZULI)
                                amount += slotItem.getAmount();
                            amount += currentItem.getAmount();

                            if (amount >= 64)
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(1, new ItemStack(Material.LAPIS_LAZULI, 64)), 0L);
                            if (amount > 64) {
                                int finalAmount = amount;
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(slot, new ItemStack(Material.LAPIS_LAZULI, finalAmount - 64)), 0L);

                            } else {
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(slot, air), 0L);
                                int finalAmount = amount;
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(1, new ItemStack(Material.LAPIS_LAZULI, finalAmount)), 0L);
                            }
                        }
                    }
                    if (type == Material.ARROW || type == Material.SPECTRAL_ARROW || type == Material.TIPPED_ARROW) {
                        var slot = event.getRawSlot();
                        if (slot != 0) {
                            var amount = 0;
                            var slotItem = view.getItem(0);
                            if (slotItem != null && slotItem.getType() == type)
                                amount += slotItem.getAmount();
                            amount += currentItem.getAmount();

                            if (amount >= 64) {
                                var nStack = new ItemStack(currentItem);
                                nStack.setAmount(64);
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(0, nStack), 0L);
                            }
                            if (amount > 64) {
                                var nStack = new ItemStack(currentItem);
                                nStack.setAmount(amount - 64);
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(slot, nStack), 0L);

                            } else {
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(slot, air), 0L);
                                var nStack = new ItemStack(currentItem);
                                nStack.setAmount(amount);
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(0, nStack), 0L);
                            }
                        }
                    }
                    if (tools.contains(type) || type == Material.ARROW || type == Material.TIPPED_ARROW || type == Material.SPECTRAL_ARROW) {
                        if (!(event.getRawSlot() == 0 || event.getRawSlot() == 2 || (view.getItem(0) == null || view.getItem(0).getType() == Material.AIR))) {
                            event.setCancelled(true);
                            return;
                        }
                        if ((type == Material.WOODEN_HOE || type == Material.STONE_HOE || type == Material.IRON_HOE || type == Material.GOLDEN_HOE || type == Material.DIAMOND_HOE || type == Material.NETHERITE_HOE)
                                && !(isChisel(currentItem) || isSaw(currentItem))) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                } else {
                    if ((cursorItem == null || cursorItem.getType() == Material.AIR) && currentItem != null && (event.getRawSlot() == 1 || event.getRawSlot() == 0)) {
                        var slot = event.getRawSlot();
                        var type = currentItem.getType();
                        if (type == Material.LAPIS_LAZULI) {
                            if (event.getClick() == ClickType.RIGHT && view.getItem(slot) != null) {
                                var stack = new ItemStack(Material.LAPIS_LAZULI, view.getItem(slot).getAmount() / 2);
                                var stack2 = new ItemStack(Material.LAPIS_LAZULI, view.getItem(slot).getAmount() - stack.getAmount());
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(stack), 0L);
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(slot, stack2), 0L);
                            } else {
                                return;
                            }
                        } else if (type == Material.ARROW || type == Material.TIPPED_ARROW || type == Material.SPECTRAL_ARROW) {
                            if (event.getClick() == ClickType.RIGHT && view.getItem(slot) != null) {
                                var stack = new ItemStack(view.getItem(slot));
                                stack.setAmount(view.getItem(slot).getAmount() / 2);
                                var stack2 = new ItemStack(view.getItem(slot));
                                stack2.setAmount(view.getItem(slot).getAmount() - stack.getAmount());
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(stack), 0L);
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(slot, stack2), 0L);
                            } else {
                                return;
                            }
                        } else {
                            event.setCancelled(true);
                            return;
                        }
                    }
                    var type = cursorItem.getType();
                    if (event.getRawSlot() == 1 && cursorItem.getType() != Material.LAPIS_LAZULI) {
                        event.setCancelled(true);
                        return;
                    }
                    if (type == Material.LAPIS_LAZULI) {
                        if (event.getRawSlot() == 0) {
                            event.setCancelled(true);
                            return;
                        }
                        if (event.getRawSlot() == 1) {
                            if (event.isRightClick() && currentItem != null && currentItem.getType() == Material.LAPIS_LAZULI) {
                                if (currentItem.getAmount() < 64) {
                                    var amount = currentItem.getAmount();
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(1, new ItemStack(Material.LAPIS_LAZULI, amount + 1)), 0L);
                                    if (cursorItem.getAmount() > 1) {
                                        var amount2 = cursorItem.getAmount();
                                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(new ItemStack(Material.LAPIS_LAZULI, amount2 - 1)), 0L);
                                    } else {
                                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(air), 0L);
                                    }
                                }
                            } else if (event.isRightClick() && (currentItem == null || currentItem.getType() == Material.AIR)) {
                                var amount = cursorItem.getAmount();
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(1, new ItemStack(Material.LAPIS_LAZULI, 1)), 0L);
                                if (amount > 1)
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(new ItemStack(Material.LAPIS_LAZULI, amount - 1)), 0L);
                                else
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(air), 0L);

                            } else {
                                var amount = 0;
                                var slotItem = view.getItem(1);
                                if (slotItem != null && slotItem.getType() == Material.LAPIS_LAZULI)
                                    amount += slotItem.getAmount();
                                amount += cursorItem.getAmount();

                                if (amount >= 64)
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(1, new ItemStack(Material.LAPIS_LAZULI, 64)), 0L);
                                int finalAmount = amount;
                                if (amount > 64) {
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(new ItemStack(Material.LAPIS_LAZULI, finalAmount - 64)), 0L);
                                } else {
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(1, new ItemStack(Material.LAPIS_LAZULI, finalAmount)), 0L);
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(air), 0L);
                                }
                            }
                        }
                    } else if (type == Material.ARROW || type == Material.TIPPED_ARROW || type == Material.SPECTRAL_ARROW) {
                        if (event.getRawSlot() == 1) {
                            event.setCancelled(true);
                            return;
                        }
                        if (event.getRawSlot() == 0) {
                            if (event.isRightClick() && currentItem != null && currentItem.getType() == type) {
                                if (currentItem.getAmount() < 64) {
                                    var amount = currentItem.getAmount();
                                    var nStack = new ItemStack(currentItem);
                                    nStack.setAmount(amount + 1);
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(0, nStack), 0L);
                                    if (cursorItem.getAmount() > 1) {
                                        var amount2 = cursorItem.getAmount();
                                        var nStack2 = new ItemStack(currentItem);
                                        nStack2.setAmount(amount2 - 1);
                                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(nStack2), 0L);
                                    } else {
                                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(air), 0L);
                                    }
                                }
                            } else if (event.isRightClick() && (currentItem == null || currentItem.getType() == Material.AIR)) {
                                var amount = cursorItem.getAmount();
                                var nStack = new ItemStack(currentItem);
                                nStack.setAmount(1);
                                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(0, nStack), 0L);
                                if (amount > 1) {
                                    var nStack2 = new ItemStack(currentItem);
                                    nStack2.setAmount(amount - 1);
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(nStack2), 0L);
                                } else
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(air), 0L);

                            } else {
                                var amount = 0;
                                var slotItem = view.getItem(0);
                                if (slotItem != null && slotItem.getType() == type)
                                    amount += slotItem.getAmount();
                                amount += cursorItem.getAmount();

                                if (amount >= 64) {
                                    var nStack = new ItemStack(cursorItem);
                                    nStack.setAmount(64);
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(0, nStack), 0L);
                                }
                                int finalAmount = amount;
                                if (amount > 64) {
                                    var nStack = new ItemStack(cursorItem);
                                    nStack.setAmount(finalAmount - 64);
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(nStack), 0L);
                                } else {
                                    var nStack = new ItemStack(cursorItem);
                                    nStack.setAmount(finalAmount);
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setItem(0, nStack), 0L);
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> view.setCursor(air), 0L);
                                }
                            }
                        }
                    }
                    if (tools.contains(type) || type == Material.ARROW || type == Material.TIPPED_ARROW || type == Material.SPECTRAL_ARROW) {
                        if (event.getRawSlot() == 1) {
                            event.setCancelled(true);
                            return;
                        }
                        if (event.getRawSlot() == 0 && (type == Material.WOODEN_HOE || type == Material.STONE_HOE || type == Material.IRON_HOE || type == Material.GOLDEN_HOE || type == Material.DIAMOND_HOE || type == Material.NETHERITE_HOE)
                                && !(isChisel(currentItem) || isSaw(currentItem))) {
                            event.setCancelled(true);
                            return;
                        }
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
                    if (isMetal(currentItem) && canCombine(currentItem, cursorItem)) {
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
                    currentItem != null &&
                    currentItem.getType() != Material.AIR &&
                    (view.getTopInventory().getType() == InventoryType.WORKBENCH ||
                            view.getTopInventory().getType() == InventoryType.CRAFTING)) {
                var usesMetal = false;
                var refined = false;
                var slots = view.countSlots() - 5;
                for (int i = 0; i < slots; i++) {
                    if (view.getSlotType(i) != InventoryType.SlotType.CRAFTING) continue;
                    var item = view.getItem(i);
                    if (item != null && isMetal(item)) {
                        usesMetal = true;
                        if (isRefined(item)) refined = true;
                        break;
                    }
                }
                var purityResult = currentItem != null && hasPurity(currentItem);

                if (!usesMetal && !purityResult) return;
                var slurried = isSlurried(currentItem);
                int ingredients;
                int start;
                int craftSlots;
                if (event.getInventory().getType() == InventoryType.WORKBENCH) {
                    start = 10;
                    craftSlots = 9;
                } else {
                    start = 9;
                    craftSlots = 4;
                }


                if (event.getClick().isShiftClick()) {
                    var crafts = 0;
                    var space = 0.0;
                    var amount = currentItem.getAmount();
                    var maxSize = currentItem.getMaxStackSize();
                    if (isSlurried(currentItem)
                            || (currentItem.getItemMeta() != null && currentItem.getItemMeta().hasEnchants() && !isGrenade(currentItem)))
                        maxSize = 1;
                    for (int i = start; i < start + 36; i++) {
                        var item = view.getItem(i);
                        if (item == null || item.getType() == Material.AIR)
                            space += ((double) currentItem.getMaxStackSize()) / amount;
                        else if (canCombine(item, currentItem))
                            space += ((double) (item.getMaxStackSize() - item.getAmount())) / amount;
                    }

                    ingredients = 64;
                    for (int i = 1; i <= craftSlots; i++) {
                        var item = view.getItem(i);
                        if (item != null && item.getType() != Material.AIR)
                            ingredients = Math.min(ingredients, item.getAmount());
                    }
                    crafts = Math.min((int) space, ingredients);
                    if (crafts == 0) {
                        event.setCancelled(true);
                        return;
                    }

                    List<ItemStack[]> newStacks = new ArrayList<>();
                    for (int i = 1; i <= craftSlots; i++) {
                        var item = view.getItem(i);
                        if (item == null || item.getType() == Material.AIR) {
                            var stacks = new ItemStack[2];
                            stacks[0] = air;
                            stacks[1] = air;
                            newStacks.add(stacks);
                        } else {
                            if (hasPurity(item)) {
                                newStacks.add(dropItemsFromPurity(item, crafts));
                            } else {
                                var stacks = new ItemStack[2];
                                var nItem = new ItemStack(item);
                                nItem.setAmount(crafts);
                                item.setAmount(item.getAmount() - crafts);
                                stacks[0] = item;
                                stacks[1] = nItem;
                                newStacks.add(stacks);
                            }
                        }
                    }
                    for (int i = 1; i <= craftSlots; i++) {
                        view.setItem(i, newStacks.get(i - 1)[0]);
                    }
                    if (purityResult && !isSlurried(currentItem)) {
                        var nPurities = new float[crafts * amount];
                        int j = 0;
                        for (int i = 0; i < crafts; i++) {
                            var avg = 0.0;
                            var divide = 0;
                            for (var stack : newStacks) {
                                if (hasPurity(stack[1])) {
                                    avg += getPurity(stack[1])[i];
                                    divide += 1;
                                }
                            }
                            avg /= divide;
                            for (; j < (i + 1) * amount; j++)
                                nPurities[j] = (float) avg;
                        }
                        var stackCount = (crafts * amount) / maxSize;
                        if ((crafts * amount) % maxSize != 0) stackCount++;
                        var stacks = new ItemStack[stackCount];
                        int index = 0;
                        for (int i = 0; i < crafts * amount; i += maxSize) {
                            var item = new ItemStack(currentItem);
                            item.setAmount(Math.min(crafts * amount - i, maxSize));
                            var purities = new float[item.getAmount()];
                            if (item.getAmount() >= 0)
                                System.arraycopy(nPurities, i, purities, 0, item.getAmount());
                            setPurity(item, purities);
                            if (refined) {
                                var meta = item.getItemMeta();
                                var lore = meta.getLore();
                                lore.add("");
                                lore.add(refinedLore);
                                meta.setLore(lore);
                                item.setItemMeta(meta);
                            }
                            stacks[index++] = item;
                        }
                        index = 0;
                        for (int i = start + 35; i >= start; i--) {
                            if (index == stacks.length) break;
                            var item = view.getItem(i);
                            if (item == null || item.getType() == Material.AIR) {
                                view.setItem(i, stacks[index]);
                                index++;
                            } else if (isMetal(item) && canCombine(item, stacks[index])) {
                                var combined = getCombinedPurity(item, stacks[index]);
                                view.setItem(i, combined[0]);
                                if (combined.length > 1) stacks[index] = combined[1];
                                else index++;
                                if (combined[0].getAmount() < maxSize) i--;
                            }
                        }

                    } else {
                        var stackCount = crafts / maxSize;
                        var nItem = new ItemStack(currentItem);
                        nItem.setAmount(maxSize);
                        for (int i = 0; i < stackCount; i++) {
                            view.getPlayer().getInventory().addItem(nItem);
                        }
                        if (crafts % maxSize != 0) {
                            nItem.setAmount(crafts % maxSize);
                            view.getPlayer().getInventory().addItem(nItem);
                        }
                        if (slurried) {
                            var first = event.getView().getPlayer().getInventory().firstEmpty();
                            if (first == -1)
                                event.getView().getPlayer().getWorld().dropItem(event.getView().getPlayer().getLocation(), new ItemStack(Material.GLASS_BOTTLE, crafts));
                            else
                                event.getView().getPlayer().getInventory().addItem(new ItemStack(Material.GLASS_BOTTLE, crafts));
                        }
                    }
                    event.setCurrentItem(air);
                } else {
                    var crafted = true;
                    if (cursorItem == null || cursorItem.getType() == Material.AIR) {
                        event.getView().setCursor(currentItem);
                        event.setCurrentItem(air);
                    } else if (isMetal(currentItem) && canCombine(currentItem, cursorItem) && currentItem.getAmount() + cursorItem.getAmount() <= cursorItem.getMaxStackSize()) {
                        if (hasPurity(currentItem)) {
                            event.getView().setCursor(getCombinedPurity(cursorItem, currentItem)[0]);
                        } else {
                            cursorItem.setAmount(currentItem.getAmount() + cursorItem.getAmount());
                            event.getView().setCursor(cursorItem);
                            event.setCurrentItem(air);
                        }
                    } else {
                        crafted = false;
                    }
                    if (crafted) {
                        for (int i = 1; i <= craftSlots; i++) {
                            var item = view.getItem(i);
                            if (item == null || item.getType() == Material.AIR) continue;
                            var amount = item.getAmount();
                            if (amount == 1) {
                                view.setItem(i, air);
                            } else {
                                if (isMetal(item)) view.setItem(i, dropItemFromPurity(item)[0]);
                                else item.setAmount(item.getAmount() - 1);
                            }
                        }
                    }
                }
                event.setCancelled(true);
                return;
            }

            var click = event.getClick();
            switch (click) {
                case DOUBLE_CLICK -> {
                    if (cursorItem != null && isMetal(cursorItem)) {
                        var slots = view.countSlots() - 5;
                        for (int i = 0; i < slots; i++) {
                            if (view.getSlotType(i).equals(InventoryType.SlotType.RESULT)) continue;
                            var item = view.getItem(i);
                            if (isMetal(item) && canCombine(item, cursorItem)) {
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
                    if (isMetal(cursorItem) && cursorItem.getEnchantments().keySet().size() > 0) {
                        if (cursorItem.isSimilar(currentItem)) event.setCancelled(true);
                        return;
                    }
                    if (isMetal(currentItem) && canCombine(currentItem, cursorItem)) {
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
                    if ((isMetal(cursorItem) && cursorItem.getEnchantments().keySet().size() > 0) || (isMetal(currentItem) && currentItem.getEnchantments().keySet().size() > 0)) {
                        if (cursorItem.isSimilar(currentItem)) event.setCancelled(true);
                        return;
                    }
                    if (event.getSlot() == -999) {
                        if (cursorItem != null && isMetal(cursorItem)) {
                            if (cursorItem.getEnchantments().keySet().size() > 0) return;
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
                            if (cursorItem.getEnchantments().keySet().size() > 0) return;
                            var dropped = dropItemFromPurity(cursorItem);
                            event.getWhoClicked().setItemOnCursor(dropped[0]);
                            event.setCurrentItem(dropped[1]);
                            event.setCancelled(true);
                            return;
                        }
                    } else if (cursorItem != null && cursorItem.getType() != Material.AIR &&
                            currentItem != null && currentItem.getType() != Material.AIR &&
                            event.getSlotType() != InventoryType.SlotType.ARMOR) {
                        if (isMetal(currentItem) && canCombine(currentItem, cursorItem) && currentItem.getAmount() < 64) {
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
                                    if (isMetal(slotItem) && canCombine(slotItem, nCurrentItem)) {
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
                                    if (canCombine(slotItem, nCurrentItem) && isMetal(slotItem)) {
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
                            if (canCombine(slotItem, nCurrentItem) && isMetal(slotItem)) {
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
            var item = event.getItem();
            if (isGrenade(item.getItemStack()) && item.getItemStack().getType() == Material.TNT) {
                event.setCancelled(true);
                return;
            }
            if (!isMetal(item.getItemStack())) return;
            var inv = event.getInventory();
            for (var content : inv.getContents()) {
                if (content != null && content.getType() != Material.AIR && Objects.equals(content.getItemMeta(), item.getItemStack().getItemMeta())) {
                    event.setCancelled(true);
                    return;
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
        private void onInventoryOpen(InventoryOpenEvent event) {
            var inv = event.getInventory();
            for (var content : inv.getContents()) {
                if (isMetal(content) && !hasPurity(content)) {
                    generatePurity(content, 1.0);
                }
            }
        }

        private void manualTransfer(InventoryHolder dHolder, InventoryHolder sHolder, ItemStack item) { // todo maybe move stacks at a time to reduce potential lag?
            var slot = -1;


            Inventory destination;
            Inventory source;
            if (dHolder instanceof BlockInventoryHolder) {
                var block = ((BlockInventoryHolder) dHolder).getBlock();
                var loc = block.getLocation();
                block = loc.getBlock();
                var state = block.getState();
                if (!(state instanceof BlockInventoryHolder)) return;
                destination = ((BlockInventoryHolder) state).getInventory();
            } else {
                var entity = ((Entity) dHolder);
                destination = ((InventoryHolder) entity).getInventory();
            }

            if (sHolder instanceof BlockInventoryHolder) {
                var block = ((BlockInventoryHolder) sHolder).getBlock();
                var loc = block.getLocation();
                block = loc.getBlock();
                var state = block.getState();
                if (!(state instanceof BlockInventoryHolder)) return;
                source = ((BlockInventoryHolder) state).getInventory();
            } else {
                var entity = ((Entity) sHolder);
                source = ((InventoryHolder) entity).getInventory();
            }


            for (int i = 0; i < source.getSize(); i++) {
                var temp = source.getItem(i);
                if (temp != null && temp.isSimilar(item)) {
                    slot = i;
                    break;
                }
            }
            if (slot == -1) return;
            var slotItem = source.getItem(slot);
            int finalSlot = slot;
            ItemStack transfer;
            ItemStack nSource;
            if (slotItem.getAmount() > 1) {
                var drop = dropItemFromPurity(slotItem);
                transfer = drop[1];
                nSource = drop[0];
            } else {
                transfer = item;
                nSource = air;
            }

            var full = true;
            for (int i = 0; i < destination.getSize(); i++) {
                var content = destination.getItem(i);
                if (!(canCombine(content, transfer) || content == null || content.getType() == Material.AIR))
                    continue;
                if (content == null || content.getType() == Material.AIR) {
                    destination.setItem(i, transfer);
                    full = false;
                    break;
                } else if (content.getAmount() < 64) {
                    destination.setItem(i, getCombinedPurity(transfer, content)[0]);
                    full = false;
                    break;
                }
            }
            if (!full)
                source.setItem(finalSlot, nSource);

        }

        @EventHandler
        private void onInventoryMoveItem(InventoryMoveItemEvent event) {
            var item = event.getItem();
            if (!isMetal(item)) return;
            var destination = event.getDestination();
            var source = event.getSource();
            event.setCancelled(true);
            var dHolder = destination.getHolder();
            var sHolder = source.getHolder();
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> manualTransfer(dHolder, sHolder, item), 0L);
        }

        @EventHandler
        private void onEntityPickupItem(EntityPickupItemEvent event) {
            var drop = event.getItem();
            var item = drop.getItemStack();
            if (item == null) return;
            if (isGrenade(item) && item.getType() == Material.TNT) {
                event.setCancelled(true);
                return;
            }
            var entity = event.getEntity();
            if (isMetal(item) && !(entity instanceof Player)) {
                event.setCancelled(true);
                return;
            }

            if (entity instanceof Player) {
                if (!getAutoPickup((Player) entity) && !((Player) entity).isSneaking()) {
                    event.setCancelled(true);
                    return;
                }
            }

            if (itemPickupList.contains(drop)) {
                event.setCancelled(true);
                return;
            }

            if (entity instanceof Player) {
                itemPickupList.add(drop);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> itemPickupList.remove(drop), 2L);
            }


            if (isMetal(item) && !hasPurity(item))
                generatePurity(item, 1.0);

            if (isMetal(item) && event.getEntity() instanceof Player) {
                if (item.getItemMeta().hasEnchants()) return;
                var inv = ((Player) event.getEntity()).getInventory();

                for (var content : inv.getContents()) {
                    if (canCombine(item, content)) {
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
                ItemStack newResult;
                if (material == Material.NETHERITE_SCRAP) {
                    newResult = generatePurity(item, Math.random() / 100.0);
                } else
                    newResult = generatePurity(item, 0.01);

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
                if (item.getType() == Material.NETHERITE_SCRAP && Math.random() < 0.25) {
                    newResult = generatePurity(item, 0.15);
                } else if (item.getType() != Material.AIR)
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
            if (result != null && result.getType() != Material.AIR) {
                result.removeEnchantment(Enchantment.DURABILITY);
                var ingredient = inventory[1];
                if (ingredient != null && ingredient.getItemMeta() != null && ingredient.getItemMeta().hasEnchant(Enchantment.DURABILITY)) {
                    result.addUnsafeEnchantment(Enchantment.DURABILITY, ingredient.getEnchantmentLevel(Enchantment.DURABILITY));
                }
                var meta = result.getItemMeta();
                var displayName = meta.getDisplayName();

                if (isSaw(result)) displayName = "Netherite Saw";
                else if (isChisel(result)) displayName = "Netherite Chisel";
                else if (isHuntingKnife(result)) displayName = "Netherite Hunting Knife";
                meta.setDisplayName(ChatColor.RESET + displayName);
                result.setItemMeta(meta);
            }
        }

        @EventHandler
        private void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
            var entity = event.getRightClicked();
            var hand = event.getHand();
            var playerEquipment = event.getPlayer().getEquipment();
            var item = playerEquipment.getItem(hand);

            if (entity.getType() == EntityType.PIGLIN) {
                var equipment = ((LivingEntity) entity).getEquipment();
                if ((equipment.getItemInOffHand() == null || equipment.getItemInOffHand().getType() == Material.AIR) && item.getType() == Material.GOLD_INGOT) {
                    event.setCancelled(true);
                    ItemStack keep;
                    ItemStack give;

                    if (item.getAmount() > 1) {
                        var drops = dropItemFromPurity(item);
                        keep = drops[0];
                        give = drops[1];
                    } else {
                        keep = air;
                        give = item;
                    }
                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> playerEquipment.setItem(hand, keep), 0L);
                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> equipment.setItemInOffHand(give), 0L);
                }
            }
        }

        @EventHandler
        private void onPlayerInteract(PlayerInteractEvent event) {
            var player = event.getPlayer();
            var hand = event.getHand();
            if (hand == null) return;
            var equip = event.getPlayer().getEquipment();
            if (equip == null) return;
            var item = equip.getItem(hand);
            var type = item.getType();
            var block = event.getClickedBlock();
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
            } else if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event.getHand() != EquipmentSlot.OFF_HAND && !event.isBlockInHand()) {
                var lookingAt = getItemLookedAt(player);
                if (lookingAt != null) {
                    var lookingItem = lookingAt.getItemStack();
                    if (isGrenade(lookingItem) && lookingItem.getType() == Material.TNT) return;
                    event.setUseItemInHand(Event.Result.DENY);
                    event.setCancelled(true);

                    if (lookingAt.getPickupDelay() > 0) {
                        event.setCancelled(true);
                        return;
                    }

                    if (itemPickupList.contains(lookingAt)) {
                        event.setCancelled(true);
                        return;
                    }

                    itemPickupList.add(lookingAt);
                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> itemPickupList.remove(lookingAt), 2L);

                    var ogAmount = lookingItem.getAmount();

                    if (item.getType() == Material.AIR) {
                        player.getEquipment().setItem(event.getHand(), lookingItem);
                        lookingAt.remove();
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3f, (float) (Math.random() * 8));
                        return;
                    } else if (isMetal(lookingItem)) {
                        for (var content : player.getInventory().getContents()) {
                            if (content != null && content.getType() != Material.AIR && content.getAmount() < 64 && canCombine(lookingItem, content)) {
                                var combined = getCombinedPurity(lookingItem, content);
                                content.setAmount(combined[0].getAmount());
                                content.setItemMeta(combined[0].getItemMeta());
                                if (combined.length > 1) lookingItem = combined[1];
                                else lookingItem = air;
                                break;
                            }
                        }
                        if (lookingItem.getType() != Material.AIR) {
                            var first = player.getInventory().firstEmpty();
                            if (first != -1) {
                                player.getInventory().setItem(first, lookingItem);
                                lookingItem = air;
                            }
                        }
                        if (lookingItem == air) {
                            lookingAt.remove();
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3f, (float) (Math.random() * 8));
                        } else {
                            if (lookingItem.getAmount() != ogAmount) {
                                lookingAt.setItemStack(lookingItem);
                                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3f, (float) (Math.random() * 8));
                            } else {
                                player.sendMessage(ChatColor.RED + "You do not have enough room in your inventory to pick up this item.");
                            }
                        }
                    } else {
                        var leftover = player.getInventory().addItem(lookingItem).get(0);
                        if (leftover != null && leftover.getAmount() == lookingItem.getAmount()) {
                            player.sendMessage(ChatColor.RED + "You do not have enough room in your inventory to pick up this item.");
                        } else {
                            lookingAt.remove();
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3f, (float) (Math.random() * 8));
                        }

                    }
                    return;
                }
            }

        }

    }

    private Item getItemLookedAt(Player player) {
        var location = player.getEyeLocation().clone();
        var direction = location.getDirection();
        for (int i = 0; i <= 20; i++) {
            var nearby = player.getNearbyEntities(i, i, i);
            for (var entity : nearby) {
                var box = entity.getBoundingBox().clone();
                box.expand(0.15, 0.0, 0.15, 0.15, 1.2, 0.15);
                if (box.contains(location.toVector())) {
                    if (entity instanceof Item) {
                        return ((Item) entity);
                    } else {
                        return null;
                    }
                }
            }
            location.setX(location.getX() + direction.getX() * 0.25);
            location.setY(location.getY() + direction.getY() * 0.25);
            location.setZ(location.getZ() + direction.getZ() * 0.25);
        }
        return null;
    }

    private void prepareGrindstone(GrindstoneInventory inv) {
        var tool = inv.getItem(0);
        var lapis = inv.getItem(1);

        if (tool == null || (!tools.contains(tool.getType()) && !(tool.getType() == Material.ARROW || tool.getType() == Material.TIPPED_ARROW || tool.getType() == Material.SPECTRAL_ARROW))) {
            inv.setItem(2, air);
            return;
        }

        var hasLapis = false;
        if (lapis != null) {
            if (lapis.getType() == Material.LAPIS_LAZULI) hasLapis = true;
            else if (lapis.getType() != Material.AIR) {
                inv.setItem(2, air);
                return;
            }
        }

        if (hasLapis && lapis.getAmount() < tool.getAmount()) {
            inv.setItem(2, air);
            return;
        }
        var type = tool.getType();
        if ((type == Material.WOODEN_HOE || type == Material.STONE_HOE || type == Material.IRON_HOE || type == Material.GOLDEN_HOE || type == Material.DIAMOND_HOE || type == Material.NETHERITE_HOE)
                && !(isChisel(tool) || isSaw(tool))) {
            inv.setItem(2, air);
            return;
        }

        if (type == Material.WOODEN_AXE
                || type == Material.STONE_AXE
                || type == Material.IRON_AXE
                || type == Material.GOLDEN_AXE
                || type == Material.DIAMOND_AXE
                || type == Material.NETHERITE_AXE
        ) {
            var sharpness = 0;
            var meta = tool.getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.DAMAGE_ALL))
                sharpness = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);

            if (sharpness > 1) {
                inv.setItem(2, air);
                return;
            }
            if (sharpness == 1) {
                if (!hasLapis) {
                    inv.setItem(2, air);
                    return;
                }
                var result = new ItemStack(tool);
                result.removeEnchantment(Enchantment.DAMAGE_ALL);
                result.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
                result.removeEnchantment(Enchantment.DIG_SPEED);
                result.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.925);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            } else {
                var result = new ItemStack(tool);
                result.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                result.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.85);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            }
            return;
        }
        if (isChisel(tool)) {
            var fortune = 0;
            var meta = tool.getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.LOOT_BONUS_BLOCKS))
                fortune = meta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS);

            if (fortune > 1) {
                inv.setItem(2, air);
                return;
            }
            if (fortune == 1) {
                if (!hasLapis) {
                    inv.setItem(2, air);
                    return;
                }
                var result = new ItemStack(tool);
                result.removeEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
                result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.925);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            } else {
                var result = new ItemStack(tool);
                result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.85);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            }
            return;
        }
        if (tool.getType() == Material.ARROW || tool.getType() == Material.TIPPED_ARROW || tool.getType() == Material.SPECTRAL_ARROW) {
            var power = 0;
            var meta = tool.getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.ARROW_DAMAGE))
                power = meta.getEnchantLevel(Enchantment.ARROW_DAMAGE);

            if (power > 1) {
                inv.setItem(2, air);
                return;
            }
            if (power == 1) {
                if (!hasLapis) {
                    inv.setItem(2, air);
                    return;
                }
                var result = new ItemStack(tool);
                result.removeEnchantment(Enchantment.ARROW_DAMAGE);
                result.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 2);
                inv.setItem(2, result);
            } else {
                var result = new ItemStack(tool);
                result.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
                inv.setItem(2, result);
            }
            return;
        }
        if (isHuntingKnife(tool)) {
            var looting = 0;
            var meta = tool.getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.LOOT_BONUS_MOBS))
                looting = meta.getEnchantLevel(Enchantment.LOOT_BONUS_MOBS);

            if (looting > 1) {
                inv.setItem(2, air);
                return;
            }
            if (looting == 1) {
                if (!hasLapis) {
                    inv.setItem(2, air);
                    return;
                }
                var result = new ItemStack(tool);
                result.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
                result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 2);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.925);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            } else {
                var result = new ItemStack(tool);
                result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 1);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.85);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            }
            return;
        }
        if (type == Material.WOODEN_PICKAXE
                || type == Material.STONE_PICKAXE
                || type == Material.IRON_PICKAXE
                || type == Material.GOLDEN_PICKAXE
                || type == Material.DIAMOND_PICKAXE
                || type == Material.NETHERITE_PICKAXE
        ) {
            var looting = 0;
            var meta = tool.getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.DIG_SPEED))
                looting = meta.getEnchantLevel(Enchantment.DIG_SPEED);

            if (looting > 1) {
                inv.setItem(2, air);
                return;
            }
            if (looting == 1) {
                if (!hasLapis) {
                    inv.setItem(2, air);
                    return;
                }
                var result = new ItemStack(tool);
                result.removeEnchantment(Enchantment.DIG_SPEED);
                result.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.925);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            } else {
                var result = new ItemStack(tool);
                result.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.85);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            }
            return;
        }

        if (isSaw(tool)) {
            var efficiency = 0;
            var meta = tool.getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.DIG_SPEED))
                efficiency = meta.getEnchantLevel(Enchantment.DIG_SPEED);

            if (efficiency > 1) {
                inv.setItem(2, air);
                return;
            }
            if (efficiency == 1) {
                if (!hasLapis) {
                    inv.setItem(2, air);
                    return;
                }
                var result = new ItemStack(tool);
                result.removeEnchantment(Enchantment.DIG_SPEED);
                result.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.925);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            } else {
                var result = new ItemStack(tool);
                result.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.85);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            }
            return;
        }

        if (type == Material.WOODEN_SHOVEL
                || type == Material.STONE_SHOVEL
                || type == Material.IRON_SHOVEL
                || type == Material.GOLDEN_SHOVEL
                || type == Material.DIAMOND_SHOVEL
                || type == Material.NETHERITE_SHOVEL
        ) {
            var looting = 0;
            var meta = tool.getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.DIG_SPEED))
                looting = meta.getEnchantLevel(Enchantment.DIG_SPEED);

            if (looting > 1) {
                inv.setItem(2, air);
                return;
            }
            if (looting == 1) {
                if (!hasLapis) {
                    inv.setItem(2, air);
                    return;
                }
                var result = new ItemStack(tool);
                result.removeEnchantment(Enchantment.DIG_SPEED);
                result.addUnsafeEnchantment(Enchantment.DIG_SPEED, 2);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.925);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            } else {
                var result = new ItemStack(tool);
                result.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.85);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            }
            return;
        }

        if (type == Material.WOODEN_SWORD
                || type == Material.STONE_SWORD
                || type == Material.IRON_SWORD
                || type == Material.GOLDEN_SWORD
                || type == Material.DIAMOND_SWORD
                || type == Material.NETHERITE_SWORD
        ) {
            var sharpness = 0;
            var meta = tool.getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.DAMAGE_ALL))
                sharpness = meta.getEnchantLevel(Enchantment.DAMAGE_ALL);

            if (sharpness > 1) {
                inv.setItem(2, air);
                return;
            }
            if (sharpness == 1) {
                if (!hasLapis) {
                    inv.setItem(2, air);
                    return;
                }
                var result = new ItemStack(tool);
                result.removeEnchantment(Enchantment.DAMAGE_ALL);
                result.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
                result.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 1);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.925);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            } else {
                var result = new ItemStack(tool);
                result.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.85);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            }
            return;
        }

        if (type == Material.TRIDENT) {
            var impaling = 0;
            var meta = tool.getItemMeta();
            if (meta != null && meta.hasEnchant(Enchantment.IMPALING))
                impaling = meta.getEnchantLevel(Enchantment.IMPALING);

            if (impaling > 1) {
                inv.setItem(2, air);
                return;
            }
            if (impaling == 1) {
                if (!hasLapis) {
                    inv.setItem(2, air);
                    return;
                }
                var result = new ItemStack(tool);
                result.removeEnchantment(Enchantment.IMPALING);
                result.addUnsafeEnchantment(Enchantment.IMPALING, 2);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.925);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            } else {
                var result = new ItemStack(tool);
                result.addUnsafeEnchantment(Enchantment.IMPALING, 1);
                if (result.getItemMeta() instanceof Damageable) {
                    var rMeta = result.getItemMeta();
                    var damageable = (Damageable) rMeta;
                    var max = result.getType().getMaxDurability();
                    var nDurability = (int) ((max - damageable.getDamage()) * 0.85);
                    if (nDurability < 1) {
                        inv.setItem(2, air);
                        return;
                    }
                    damageable.setDamage(max - nDurability);
                    result.setItemMeta(rMeta);
                    inv.setItem(2, result);
                }
            }
        }
    }

    private boolean isChisel(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().CHISEL_LORE);
    }

    private boolean isSaw(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().SAW_LORE);
    }

    private boolean isHuntingKnife(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().KNIFE_LORE);
    }

    private boolean isCoarseSandPaper(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().COARSE_PAPER_LORE);
    }

    private boolean isFineSandPaper(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().FINE_PAPER_LORE);
    }

    private boolean isWhetstone(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().WHETSTONE_LORE);
    }

    private boolean isWaterstone(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().WATERSTONE_LORE);
    }

    private boolean isCoarseBlade(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().COARSE_BLADE_LORE);
    }

    private boolean isFineBlade(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().FINE_BLADE_LORE);
    }

    private boolean isIronRod(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().IRON_ROD_LORE);
    }

    private boolean isNetheriteRod(ItemStack currentItem) {
        return currentItem != null &&
                currentItem.getItemMeta() != null &&
                currentItem.getItemMeta().getLore() != null &&
                currentItem.getItemMeta().getLore().contains(DiplomacyRecipes.getInstance().NETHERITE_ROD_LORE);
    }

    public boolean getAutoPickup(Player player) {
        var container = player.getPersistentDataContainer();
        if (!container.has(pickupKey, BooleanPersistentDataType.instance)) {
            container.set(pickupKey, BooleanPersistentDataType.instance, true);
            return true;
        } else {
            return container.get(pickupKey, BooleanPersistentDataType.instance);
        }

    }

    public void toggleAutoPickup(Player player) {
        var container = player.getPersistentDataContainer();
        if (!container.has(pickupKey, BooleanPersistentDataType.instance)) {
            container.set(pickupKey, BooleanPersistentDataType.instance, true);
        } else {
            container.set(pickupKey, BooleanPersistentDataType.instance, !container.get(pickupKey, BooleanPersistentDataType.instance));
        }

    }
}
