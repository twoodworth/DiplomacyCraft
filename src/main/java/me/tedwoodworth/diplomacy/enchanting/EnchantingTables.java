package me.tedwoodworth.diplomacy.enchanting;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.events.NationCreateEvent;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.ScoreboardManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class EnchantingTables {
    private static EnchantingTables instance = null;
    private final File tableConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "enchantingTables.yml");
    private final List<EnchantingTable> tables = new ArrayList<>();
    private final YamlConfiguration tableConfig;

    public static EnchantingTables getInstance() {
        if (instance == null) {
            instance = new EnchantingTables();
        }
        return instance;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EnchantingTables.EventListener(), Diplomacy.getInstance());
    }

    private EnchantingTables() {
        tableConfig = YamlConfiguration.loadConfiguration(tableConfigFile);
        var tablesSection = tableConfig.getConfigurationSection("Tables");
        if (tablesSection == null) {
            tablesSection = tableConfig.createSection("Tables");
        }
        for (var tableID : tablesSection.getKeys(false)) {
            var tableSection = tableConfig.getConfigurationSection("Tables." + tableID);
            if (tableSection == null) {
                tableConfig.set("Tables." + tableID, null);
            } else {
                var table = new EnchantingTable(tableID, tableSection);
                tables.add(table);
            }
        }
    }

    private void removeTable(EnchantingTable table) {
        var id = table.getID();
        tableConfig.set("Tables." + id, null);
        tables.remove(table);
    }

    public EnchantingTable createTable(Location location) {
        var nextTableID = tableConfig.getString("NextTableID");
        if (nextTableID == null) {
            tableConfig.set("NextTableID", "0");
            nextTableID = "0";
        }

        var tableID = nextTableID;
        nextTableID = String.valueOf(Integer.parseInt(nextTableID) + 1);

        Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Table.yml")));
        ConfigurationSection tableSection = YamlConfiguration.loadConfiguration(reader);



        var x = location.getX();
        var y = location.getY();
        var z = location.getZ();
        var world = location.getWorld().getName();

        Map<String, String> map = ImmutableMap.of(
                "x", String.valueOf(x),
                "y", String.valueOf(y),
                "z", String.valueOf(z),
                "World", world
        );

        tableSection.createSection("Location", map);
        tableSection.set("Tables." + tableID + ".Enchantments", new ArrayList<>());
        tableConfig.set("NextTableID", nextTableID);
        var table = new EnchantingTable(tableID, tableSection);
        tables.add(table);
        return table;
    }

    private EnchantingTable getTable(Location location) {
        for (var table : tables) {
            if (table.getLocation().equals(location)) {
                return table;
            }
        }
        return createTable(location);
    }

    public void save() {
        try {
            for (var table : tables) {
                tableConfig.set("Tables." + table.getID(), table.getConfigSection());
            }
            tableConfig.save(tableConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventListener implements Listener {

        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }

        @EventHandler
        void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin().equals(Diplomacy.getInstance())) {
                save();
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        void onBlockPlace(BlockPlaceEvent event) {
            if (event.getBlockPlaced().getType().equals(Material.ENCHANTING_TABLE)) {
                createTable(event.getBlockPlaced().getLocation());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
        void onBlockBreak(BlockBreakEvent event) {
            var block = event.getBlock();
            if (event.getBlock().getType().equals(Material.ENCHANTING_TABLE)) {
                var location = block.getLocation();
                var world = location.getWorld();
                if (world == null) {
                    return;
                }
                var table = EnchantingTables.getInstance().getTable(location);
                var enchantments = table.getEnchantmentKeys();
                for (var enchantmentKey : enchantments) {
                    var enchantment = Enchantment.getByKey(NamespacedKey.minecraft(enchantmentKey));
                    var tome = EnchantingTomes.getInstance().getTome(enchantment);
                    if (tome != null) world.dropItem(location, tome);
                }
                EnchantingTables.getInstance().removeTable(table);
            }
        }


        @EventHandler
        void onPlayerInteract(PlayerInteractEvent event) {
            var player = event.getPlayer();
            if (player.isSneaking() && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                var block = event.getClickedBlock();
                if (block != null && block.getType().equals(Material.ENCHANTING_TABLE)) {
                    var item = event.getItem();
                    var table = EnchantingTables.getInstance().getTable(block.getLocation());
                    if (item == null) {
                        var enchants = table.getEnchantmentKeys();
                        if (enchants.size() > 0) {
                            enchants.sort(String::compareTo);
                            player.sendMessage(ChatColor.GREEN + "This enchanting table contains the following tomes:");
                            for (var enchant : enchants) {
                                player.sendMessage(ChatColor.DARK_GREEN + "- " + enchant);
                            }
                        } else {
                            player.sendMessage(ChatColor.GREEN + "This enchanting table does not contain any tomes.");
                        }
                        event.setUseInteractedBlock(Event.Result.DENY);
                        return;
                    }
                    if (EnchantingTomes.getInstance().isTome(item)) {
                        var itemMeta = item.getItemMeta();
                        if (itemMeta == null) return;
                        var lore = itemMeta.getLore();
                        if (lore == null) {
                            return;
                        }
                        var enchantment = EnchantingTomes.getInstance().getEnchantment(lore.get(0));
                        if (enchantment == null) {
                            return;
                        }
                        if (table.hasEnchantment(enchantment)) {
                            player.sendMessage(ChatColor.RED + "This enchanting table already contains this tome.");
                        } else {
                            if (item.getAmount() > 1) {
                                item.setAmount(item.getAmount() - 1);
                                player.getInventory().setItemInMainHand(item);
                            } else {
                                player.getInventory().setItemInMainHand(null);
                            }
                            table.addEnchantment(enchantment);
                            player.sendMessage(ChatColor.GREEN + "Tome has been added to the enchanting table.");
                            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_END_PORTAL_FRAME_FILL, 1, 1);
                            player.getWorld().playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PIG_SADDLE, 1, 1);
                        }
                        event.setUseItemInHand(Event.Result.DENY);
                        event.setUseInteractedBlock(Event.Result.DENY);
                    }
                } else if (block != null && block.getType().equals(Material.OBSIDIAN)) {//TODO remove
                    for (var item : EnchantingTomes.getInstance().getAllTomes())
                        player.getWorld().dropItem(player.getLocation(), item);
                }
            }
        }
    }
}

