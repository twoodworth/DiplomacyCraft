//package me.tedwoodworth.diplomacy.Items;
//
//import me.tedwoodworth.diplomacy.Diplomacy;
//import org.bukkit.ChatColor;
//import org.bukkit.NamespacedKey;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.persistence.PersistentDataType;
//
//import java.util.*;
//
//public class CustomItemGenerator {
//    private static CustomItemGenerator instance = null;
//    private final HashMap<Integer, ItemStack> itemMap = new HashMap<>();
//    private final NamespacedKey KEY = new NamespacedKey(Diplomacy.getInstance(), "custom_items");
//
//
//    public static CustomItemGenerator getInstance() {
//        if (instance == null) {
//            instance = new CustomItemGenerator();
//        }
//        return instance;
//    }
//
//    public CustomItemGenerator() {
//
//    }
//
//    public ItemStack generateCustomItem(
//            ItemStack baseItem,
//            CustomItems.CustomID enumID,
//            String name,
//            String... lore) {
//        var id = enumID.ordinal();
//        if (itemMap.containsKey(id)) {
//            throw new IllegalArgumentException("Error: Custom item ID already taken");
//        }
//
//        var item = baseItem.clone();
//        var meta = item.getItemMeta();
//        meta.setDisplayName("" + ChatColor.RESET + name);
//        meta.setLocalizedName("" + ChatColor.RESET + name);
//        if (lore.length > 0) {
//            var loreList = new ArrayList<String>();
//            for (var str : lore) {
//                loreList.add("" + ChatColor.RESET + str);
//            }
//            meta.setLore(loreList);
//        }
//        var container = meta.getPersistentDataContainer();
//        container.set(KEY, PersistentDataType.INTEGER, id);
//        item.setItemMeta(meta);
//        itemMap.put(id, item);
//        return item;
//    }
//
//    public ItemStack getCustomItem(int id, int amount) {
//        var nItem = itemMap.get(id).clone();
//        nItem.setAmount(amount);
//        return nItem;
//    }
//
//    public ItemStack getCustomItem(CustomItems.CustomID id, int amount) {
//        var nItem = itemMap.get(id.ordinal()).clone();
//        nItem.setAmount(amount);
//        return nItem;
//    }
//
//    public int getCustomID(ItemStack item) {
//        return item.getItemMeta().getPersistentDataContainer().get(KEY, PersistentDataType.INTEGER);
//    }
//
//    public boolean isCustomItem(ItemStack item) {
//        return item != null &&
//                item.getItemMeta() != null &&
//                item.getItemMeta().getPersistentDataContainer().has(KEY, PersistentDataType.INTEGER);
//    }
//}
