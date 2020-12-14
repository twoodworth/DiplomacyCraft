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
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDispenseEvent;
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
import org.bukkit.material.Cauldron;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Tools {
    private static Tools instance = null;
    public final NamespacedKey purityKey = new NamespacedKey(Diplomacy.getInstance(), "purity");
    public final NamespacedKey slurryKey = new NamespacedKey(Diplomacy.getInstance(), "slurry");
    public final NamespacedKey foldsKey = new NamespacedKey(Diplomacy.getInstance(), "folds");
    private final String purityLore = ChatColor.GRAY + "Purity:";
    private final String plateLore = ChatColor.BLUE + "Refined Plate";
    private final String refinedLore = ChatColor.BLUE + "Refined";
    private final String slurriedLore = ChatColor.BLUE + "Slurried";
    private final ItemStack air = new ItemStack(Material.AIR);

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

    public boolean isClassItem(ItemStack itemStack) {
        if (itemStack == null) return false;

        var meta = itemStack.getItemMeta();
        return meta != null
                && meta.getLore() != null
                && meta.getLore().get(0).contains("Prefix:");
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
        return type == Material.GOLD_NUGGET ||
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
        var meta = itemStack.getItemMeta();
        var amount = itemStack.getAmount();
        var splitSize = amount / slots;
        var leftover = amount % slots;
        var purities = getPurity(itemStack);

        var stacks = new ItemStack[slots + 1];
        int h = 0;
        for (int i = 0; i < slots; i++) {
            var stack = new ItemStack(itemStack.getType(), splitSize);
            var stackPurities = new float[splitSize];

            if (splitSize >= 0) System.arraycopy(purities, h, stackPurities, 0, splitSize);
            h += splitSize;

            setPurity(stack, stackPurities);
            var stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName(meta.getDisplayName());
            stackMeta.setLocalizedName(meta.getLocalizedName());
            stackMeta.setLore(generatePurityLure(stackPurities));
            stack.setItemMeta(stackMeta);
            stacks[i] = stack;
        }

        if (leftover == 0) {
            stacks[slots] = air;
        } else {
            var stack = new ItemStack(itemStack.getType(), leftover);
            var stackPurities = new float[leftover];
            if (purities.length - h >= 0) System.arraycopy(purities, h, stackPurities, 0, purities.length - h);
            setPurity(stack, stackPurities);
            var stackMeta = stack.getItemMeta();
            stackMeta.setDisplayName(meta.getDisplayName());
            stackMeta.setLocalizedName(meta.getLocalizedName());
            stackMeta.setLore(generatePurityLure(stackPurities));
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
        newMeta.setDisplayName(itemStack.getItemMeta().getDisplayName());
        newMeta.setLocalizedName(itemStack.getItemMeta().getLocalizedName());
        newMeta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, newPurities);
        newStack.setItemMeta(newMeta);

        var otherStack = new ItemStack(itemStack.getType(), otherPurities.length);
        var otherMeta = otherStack.getItemMeta();
        otherMeta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, otherPurities);
        otherMeta.setLore(generatePurityLure(otherPurities));
        otherStack.setItemMeta(otherMeta);

        var stacks = new ItemStack[2];
        stacks[0] = newStack;
        stacks[1] = otherStack;
        return stacks;
    }

    public ItemStack[] dropItemFromPurity(ItemStack stack) {
        var meta = stack.getItemMeta();
        var purities = getPurity(stack);

        var itemPurity = new float[1];
        itemPurity[0] = purities[0];

        var newPurities = new float[purities.length - 1];
        System.arraycopy(purities, 1, newPurities, 0, newPurities.length);

        ItemStack newStack = new ItemStack(stack.getType(), newPurities.length);
        var newMeta = newStack.getItemMeta();
        newMeta.setLore(generatePurityLure(newPurities));
        newMeta.setDisplayName(stack.getItemMeta().getDisplayName());
        newMeta.setLocalizedName(stack.getItemMeta().getLocalizedName());
        newMeta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, newPurities);
        newStack.setItemMeta(newMeta);

        var newStack2 = new ItemStack(stack.getType(), 1);
        var newMeta2 = newStack2.getItemMeta();
        newMeta2.setLore(generatePurityLure(itemPurity));
        newMeta2.setDisplayName(stack.getItemMeta().getDisplayName());
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

        if (purities1.length + purities2.length <= 64) {
            var newPurities = new float[purities1.length + purities2.length];
            System.arraycopy(purities1, 0, newPurities, 0, purities1.length);
            System.arraycopy(purities2, 0, newPurities, purities1.length, purities2.length);
            Arrays.sort(newPurities);

            ItemStack newStack = new ItemStack(stack1.getType(), newPurities.length);
            var meta = newStack.getItemMeta();
            meta.setLore(generatePurityLure(newPurities));
            meta.setDisplayName(stack1.getItemMeta().getDisplayName());
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
            meta.setDisplayName(stack1.getItemMeta().getDisplayName());
            meta.setLocalizedName(stack1.getItemMeta().getLocalizedName());
            meta.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, newPurities1);
            newStack.setItemMeta(meta);

            ItemStack newStack2 = new ItemStack(stack2.getType(), newPurities2.length);
            var meta2 = newStack2.getItemMeta();
            meta2.setLore(generatePurityLure(newPurities2));
            meta2.setDisplayName(stack2.getItemMeta().getDisplayName());
            meta2.setLocalizedName(stack2.getItemMeta().getLocalizedName());
            meta2.getPersistentDataContainer().set(purityKey, FloatArrayPersistentDataType.instance, newPurities2);
            newStack2.setItemMeta(meta2);

            var stackArray = new ItemStack[2];
            stackArray[0] = newStack;
            stackArray[1] = newStack2;
            return stackArray;

        }
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
    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Tools.EventListener(), Diplomacy.getInstance());
    }

    private boolean isNew(ItemStack item) {
        var meta = item.getItemMeta();
        return (!meta.hasLore() || meta.getLore().get(0).equals(DiplomacyRecipes.getInstance().NEW_LORE)) && !meta.hasEnchants() &&
                ((Damageable) meta).getDamage() == 0;
    }

    private boolean isRefined(ItemStack item) {
        var meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null && meta.getLore().size() >= 4) {
            var lore = meta.getLore().get(3);
            return lore.equals(refinedLore);
        }
        return false;
    }

    private boolean isSlurried(ItemStack item) {
        var meta = item.getItemMeta();
        if (meta != null && meta.getLore() != null) {
            var lore = meta.getLore().get(0);
            return lore.equals(slurriedLore);
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
        if (!(isMetal(item) || isPlate(item))) throw new IllegalArgumentException("Item is not a metal.");
        var meta = item.getItemMeta();
        var container = Objects.requireNonNull(meta).getPersistentDataContainer();
        container.set(purityKey, FloatArrayPersistentDataType.instance, purities);
        meta.setLore(generatePurityLure(purities));
        item.setItemMeta(meta);
    }

    private float[] getPurity(ItemStack item) {
        if (!(isMetal(item) || isPlate(item))) {
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
            if (item != null && isMetal(item) && isIngot(item) && !isRefined(item) && item.getAmount() == 1 && item2 != null && item2.getType() == Material.BLAZE_POWDER && item2.getAmount() == 1) {
                var plate = getMetalPlate(item);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> inventory.setItem(2, plate), 1L);
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

            if (isNew(result) && !isMetal(result)) {
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

            // Metal / purities
            if (isMetal(result)) {
                var total = 0.0f;
                var count = 0;

                for (var item : inventory.getMatrix()) {
                    if (isMetal(item)) {
                        count++;
                        total += getPurity(item)[0];
                    }
                }

                var purity = total / count;

                var purities = new float[result.getAmount()];
                for (int i = 0; i < purities.length; i++) {
                    purities[i] = purity;
                }
                setPurity(result, purities);
                event.getInventory().setResult(result);
            }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        private void onBlockBreak(BlockBreakEvent event) {
            var item = event.getPlayer().getInventory().getItemInMainHand();

            // Gravel
            if (event.getBlock().getType() == Material.GRAVEL) {
                event.setDropItems(false);
                if (item.getType() == Material.WOODEN_SHOVEL || item.getType() == Material.STONE_SHOVEL || item.getType() == Material.IRON_SHOVEL
                        || item.getType() == Material.GOLDEN_SHOVEL || item.getType() == Material.DIAMOND_SHOVEL || item.getType() == Material.NETHERITE_SHOVEL) {
                    event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.GRAVEL));
                } else if (item.getType() == Material.AIR || isSifter(item)) {
                    if (isSifter(item)) {
                        event.getPlayer().getWorld().playSound(event.getBlock().getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 0.5f, (float) (Math.random() * 8.0));
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
                        if (drops > 0)
                            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), new ItemStack(Material.FLINT, drops));
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
                        else if (level == 5) chance = 0.053;
                        else if (level == 7) chance = 0.231;
                        else chance = 0.0;

                        var drops = 0;

                        for (int i = 0; i < flints; i++) {
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
                            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), goldDustItem);
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

                        for (int i = 0; i < flints; i++) {
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
                            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), ironDustItem);
                        }
                    }

                    // Ancient dust
                    if (level < 6) return;

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

                        for (int i = 0; i < flints; i++) {
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
                            event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), ancientDustItem);
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
        private void onItemUse(PlayerInteractEvent event) {
            var item = event.getItem();
            if (item == null) return;

            // Metal Plates
            if (isPlate(item)) {
                var block = event.getClickedBlock();
                if (block != null && block.getType() == Material.CAULDRON) {
                    var cauldron = (Levelled) block.getBlockData();
                    if (cauldron.getLevel() == cauldron.getMaximumLevel()) {
                        cauldron.setLevel(0);
                        block.setBlockData(cauldron);
                        var hand = event.getHand();
                        Material material;
                        switch(item.getType()) {
                            case HEAVY_WEIGHTED_PRESSURE_PLATE ->
                                material = Material.IRON_NUGGET;
                            case LIGHT_WEIGHTED_PRESSURE_PLATE ->
                                material = Material.GOLD_NUGGET;
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
                        event.getPlayer().getEquipment().setItem(hand, nuggets);
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
                if (getPurity(item)[0] <= 0.00003511f) {
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
                    var view = event.getView();
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
                var view = event.getView();
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
            if (entityStack.getType() == targetStack.getType() && isMetal(entityStack) && isMetal(targetStack)) {
                event.setCancelled(true);
                var combined = getCombinedPurity(entityStack, targetStack);
                target.setItemStack(combined[0]);
                entity.remove();
                event.setCancelled(true);

            }
        }

        @EventHandler
        private void onInventoryClick(InventoryClickEvent event) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "broadcast " + event.getRawSlot());  // TODO remove
            var currentItem = event.getCurrentItem();
            if (currentItem != null && isMetal(currentItem) && !hasPurity(currentItem))
                generatePurity(currentItem, 1.0);

            var cursorItem = event.getCursor();
            if (cursorItem != null && isMetal(cursorItem) && !hasPurity(cursorItem))
                generatePurity(cursorItem, 1.0);

            var view = event.getView();
            if (view.getSlotType(event.getRawSlot()) == InventoryType.SlotType.RESULT &&
                    view.getTopInventory().getType() == InventoryType.ANVIL && view.getItem(event.getRawSlot()) != null &&
                    (cursorItem == null || cursorItem.getType() == Material.AIR)) {
                event.getView().setCursor(view.getItem(event.getRawSlot()));
                view.setItem(0, air);
                view.setItem(1, air);
                view.setItem(2, air);
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
                                if (item.getType() != Material.AIR)
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                                            () -> view.setItem(num, dropItemFromPurity(item)[0]), 1L);
                            } else {
                                int num = i;
                                if (item.getType() != Material.AIR)
                                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                                            () -> view.setItem(num, air), 1L);
                            }
                        }
                    }
                }
            }

            var click = event.getClick();
            var action = event.getAction();
            switch (click) {
                case DOUBLE_CLICK -> {
                    if (cursorItem != null && isMetal(cursorItem)) {
                        var slots = view.countSlots() - 5;
                        for (int i = 0; i < slots; i++) {
                            if (view.getSlotType(i).equals(InventoryType.SlotType.RESULT)) continue;
                            var item = view.getItem(i);
                            if (item != null && item.getType() == cursorItem.getType() && isMetal(item)) {
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
                            && isMetal(cursorItem) && isMetal(currentItem)) {
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
                        if (isMetal(currentItem) && isMetal(cursorItem) && currentItem.getType() == cursorItem.getType() && currentItem.getAmount() < 64) {
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
                                    if (slotItem != null && slotItem.getType() == nCurrentItem.getType() && isMetal(slotItem)) {
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
                                    if (slotItem != null && slotItem.getType() == nCurrentItem.getType() && isMetal(slotItem)) {
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
                                    case HOPPER, DROPPER, DISPENSER, CHEST, ENDER_CHEST, SHULKER_BOX -> j = size - i - 1;
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
                            if (slotItem != null && slotItem.getType() == nCurrentItem.getType() && isMetal(slotItem)) {
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
                                    case HOPPER, DROPPER, DISPENSER, CHEST, ENDER_CHEST, SHULKER_BOX -> j = size - i - 1;
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
            if (isMetal(item)) {
                var inv = event.getInventory();
                for (var content : inv.getContents()) {
                    if (isMetal(content) && content.getType() == item.getType()) {
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
                    if (content == null || content.getType() == Material.AIR) {
                        inv.setItem(i, transfer);
                        event.setCancelled(true);
                        return;
                    } else if (isMetal(content) && content.getType() == item.getType() && content.getAmount() < 64) {
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
                var inv = ((Player) event.getEntity()).getInventory();
                for (var content : inv.getContents()) {
                    if (isMetal(content) && content.getType() == item.getType()) {
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
                player.getInventory().setItemInMainHand(air);
                event.setCancelled(true);
                return;
            }
        }
    }
}
