//package me.tedwoodworth.diplomacy.Items;
//
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.inventory.ItemFlag;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.PlayerInventory;
//
//public class CustomItems {
//    private static CustomItems instance = null;
//
//    public enum CustomID {
//        MAGICAL_DUST,
//        APPLE_OF_LIFE,
//        GUARD_CRYSTAL,
//        GRENADE,
//        THROWN_GRENADE
//    }
//
//    public static CustomItems getInstance() {
//        if (instance == null) {
//            instance = new CustomItems();
//        }
//        return instance;
//    }
//
//    public boolean contains(PlayerInventory inventory, CustomID customID) {
//        var contents = inventory.getContents();
//        for (var content : contents) {
//            if (content != null && CustomItemGenerator.getInstance().isCustomItem(content) && getEnum(CustomItemGenerator.getInstance().getCustomID(content)) == customID) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean contains(PlayerInventory inventory, CustomID customID, int amount) {
//        var contents = inventory.getContents();
//        int count = 0;
//        for (var content : contents) {
//            if (content != null && CustomItemGenerator.getInstance().isCustomItem(content) && getEnum(CustomItemGenerator.getInstance().getCustomID(content)) == customID) {
//                count += content.getAmount();
//            }
//        }
//        return (count >= amount);
//    }
//
//    public boolean contains(PlayerInventory inventory, Material material) {
//        var contents = inventory.getContents();
//        for (var content : contents) {
//            if (content != null && content.getType() == material && !CustomItemGenerator.getInstance().isCustomItem(content)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean contains(PlayerInventory inventory, Material material, int amount) {
//        var contents = inventory.getContents();
//        int count = 0;
//        for (var content : contents) {
//            if (content != null && content.getType() == material && !CustomItemGenerator.getInstance().isCustomItem(content)) {
//                count += content.getAmount();
//            }
//        }
//        return (count >= amount);
//    }
//
//    public void removeItems(PlayerInventory inventory, Material material, int amount) {
//        var size = inventory.getSize();
//        for (int i = 0; i < size; i++) {
//            if (amount == 0) {
//                return;
//            }
//            var item = inventory.getItem(i);
//            if (item == null || item.getType() != material || CustomItemGenerator.getInstance().isCustomItem(item)) {
//                continue;
//            }
//            var itemAmount = item.getAmount();
//            if (itemAmount <= amount) {
//                amount -= itemAmount;
//                inventory.setItem(i, new ItemStack(Material.AIR));
//            } else {
//                var remainder = itemAmount - amount;
//                item.setAmount(remainder);
//                inventory.setItem(i, item);
//                amount = 0;
//            }
//
//        }
//    }
//
//    public void removeCustomItems(PlayerInventory inventory, CustomID customID, int amount) {
//        var size = inventory.getSize();
//        for (int i = 0; i < size; i++) {
//            if (amount == 0) {
//                return;
//            }
//            var item = inventory.getItem(i);
//            if (item == null || !CustomItemGenerator.getInstance().isCustomItem(item) || getEnum(CustomItemGenerator.getInstance().getCustomID(item)) != customID) {
//                continue;
//            }
//            var itemAmount = item.getAmount();
//            if (itemAmount <= amount) {
//                amount -= itemAmount;
//                inventory.setItem(i, new ItemStack(Material.AIR));
//            } else {
//                var remainder = itemAmount - amount;
//                item.setAmount(remainder);
//                inventory.setItem(i, item);
//                amount = 0;
//            }
//
//        }
//    }
//
//    public CustomID getEnum(int id) {
//        var array = CustomID.values();
//        if (array.length > id) {
//            return array[id];
//        } else {
//            throw new IndexOutOfBoundsException("Error: CustomID " + id + " is out of bounds");
//        }
//    }
//
//    public ItemStack addEnchant(ItemStack item) {
//        item.addUnsafeEnchantment(Enchantment.LUCK, 1);
//        var meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//        item.setItemMeta(meta);
//        return item;
//    }
//
//    private CustomItems() {
//        var gInstance = CustomItemGenerator.getInstance();
//        gInstance.generateCustomItem(
//                addEnchant(new ItemStack(Material.GOLDEN_APPLE)),
//                CustomID.APPLE_OF_LIFE,
//                ChatColor.GOLD + "Apple of Life",
//                ChatColor.GRAY + "Can be eaten to gain +1 life, or crafted into magical dust"
//        );
//        gInstance.generateCustomItem(
//                addEnchant(new ItemStack(Material.GLOWSTONE_DUST)),
//                CustomID.MAGICAL_DUST,
//                ChatColor.GOLD + "Magical Dust"
//        );
//        gInstance.generateCustomItem(
//                new ItemStack(Material.END_CRYSTAL),
//                CustomID.GUARD_CRYSTAL,
//                ChatColor.GREEN + "Guard Crystal",
//                ChatColor.GRAY + "Once placed, the crystal cannot be moved. It must be killed if you want to remove it."
//        );
//        gInstance.generateCustomItem(
//                addEnchant(new ItemStack(Material.FIREWORK_STAR)),
//                CustomID.GRENADE,
//                "Grenade",
//                ChatColor.YELLOW + "Right Click: " + ChatColor.GRAY + "Overhand throw",
//                ChatColor.YELLOW + "Left Click: " + ChatColor.GRAY + "Underhand throw",
//                ChatColor.YELLOW + "Hold Shift: " + ChatColor.GRAY + "Unpin and wait until releasing shift to throw",
//                "",
//                ChatColor.RED + "Grenade will explode 4-6 seconds after thrown."
//        );
//        gInstance.generateCustomItem(
//                new ItemStack(Material.TNT),
//                CustomID.THROWN_GRENADE,
//                "Thrown Grenade",
//                ChatColor.YELLOW + "Right Click: " + ChatColor.GRAY + "Overhand throw",
//                ChatColor.YELLOW + "Left Click: " + ChatColor.GRAY + "Underhand throw",
//                ChatColor.YELLOW + "Hold Shift: " + ChatColor.GRAY + "Unpin and wait until releasing shift to throw",
//                "",
//                ChatColor.RED + "Grenade will explode 4-6 seconds after thrown."
//        );
//
//    }
//
//}
