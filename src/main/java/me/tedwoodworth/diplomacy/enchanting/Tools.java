package me.tedwoodworth.diplomacy.enchanting;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.EventListener;

public class Tools {
    private static Tools instance = null;
    final String HAMMER_LORE = ChatColor.BLUE + "Hammer";

    public static Tools getInstance() {
        if (instance == null) {
            instance = new Tools();
        }
        return instance;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Tools.EventListener(), Diplomacy.getInstance());
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
        private void onPlayerInteract(PlayerInteractEvent event) {
            var player = event.getPlayer();
            var item = event.getPlayer().getInventory().getItemInMainHand();
            if (isHammer(item)) {
                event.getPlayer().sendMessage(ChatColor.RED + "Hammers are a removed feature and can no longer be used. " +
                        "Your hammer has been reverted into the ingredients used to craft it.");
                player.playEffect(EntityEffect.BREAK_EQUIPMENT_MAIN_HAND);
                switch(item.getType()) {
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
                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                event.setCancelled(true);
                return;
            }
        }
    }
}
