package me.tedwoodworth.diplomacy.players;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PlayerUtil {



    public static void saveInventory(Player p) {
        var inventoryFolder = new File(Diplomacy.getInstance().getDataFolder().getAbsolutePath(), "inventories");
        if (!inventoryFolder.exists()) inventoryFolder.mkdir();
        File f = new File(inventoryFolder.getAbsolutePath(), p.getUniqueId() + ".yml");
        try {
            if (!f.exists()) f.createNewFile();
            FileConfiguration c = YamlConfiguration.loadConfiguration(f);
            c.set("inventory.armor", p.getInventory().getArmorContents());
            c.set("inventory.content", p.getInventory().getContents());
            c.save(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void restoreInventory(Player p) {
        try {
            p.getInventory().clear();
            var inventoryFolder = new File(Diplomacy.getInstance().getDataFolder().getAbsolutePath(), "inventories");
            if (!inventoryFolder.exists()) inventoryFolder.mkdir();
            File f = new File(inventoryFolder.getAbsolutePath(), p.getUniqueId() + ".yml");
            if (!f.exists()) f.createNewFile();
            FileConfiguration c = YamlConfiguration.loadConfiguration(f);
            try {
                ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
                p.getInventory().setArmorContents(content);
            } catch (NullPointerException e) {

            }
            try {
                ItemStack[] content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
                p.getInventory().setContents(content);
            } catch (NullPointerException e) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
