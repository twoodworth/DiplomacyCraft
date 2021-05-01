package me.tedwoodworth.diplomacy.Guis;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItemRecipes;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.HashMap;
import java.util.Map;

public class GuardGuis {
    private static GuardGuis instance = null;

    public static GuardGuis getInstance() {
        if (instance == null) {
            instance = new GuardGuis();
        }
        return instance;
    }
    public InventoryGui generateGui(Entity guard) {
        var type = GuardManager.getInstance().getType(guard);

        switch (type) {
            case BASIC -> {
                // Main Recipe menu
                var title = "" + ChatColor.GRAY + ChatColor.BOLD + "Basic Guard Crystal";

                // Crate setup
                String[] guiSetup = {
                        "abcdefghi",
                        "         ",
                        "         "
                };

                InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
                gui.setCloseAction(close -> false);
                gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

                // add sniper
                gui.addElement(new StaticGuiElement(
                        'a',
                        new ItemStack(Material.BOW),
                        click -> {
                            var clicker = click.getEvent().getWhoClicked();
                            var contents = clicker.getInventory().getContents();
                            var magicDust = 0;
                            var bow = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.BOW && ((Damageable) item.getItemMeta()).getDamage() == 0) {
                                    bow = true;
                                }
                            }
                            if (magicDust >= 2 && bow) {
                                var success = GuardManager.getInstance().setType(guard, GuardManager.Type.SNIPER);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    bow = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (bow && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!bow && item.getType() == Material.BOW && ((Damageable) item.getItemMeta()).getDamage() == 0) {
                                            inv.setItem(i, new ItemStack(Material.AIR));
                                            bow = true;
                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                remaining--;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else if (amount == 2 && remaining == 2) {
                                                remaining = 0;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - remaining);
                                                remaining = 0;
                                                inv.setItem(i, item);
                                            }
                                        }
                                    }
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                }
                                gui.close();
                            } else {
                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
                            }
                            return true;
                        },
                        ChatColor.DARK_GREEN + "Sniper - Click to upgrade",
                        ChatColor.GRAY + "Shoots powerful arrows with a large attack radius",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Bow (Undamaged)",
                        ChatColor.GRAY + "- 2x Magic Dust"
                ));

                // add gunner
                gui.addElement(new StaticGuiElement(
                        'b',
                        new ItemStack(Material.CROSSBOW),
                        click -> {
                            var clicker = click.getEvent().getWhoClicked();
                            var contents = clicker.getInventory().getContents();
                            var magicDust = 0;
                            var bow = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.CROSSBOW && ((Damageable) item.getItemMeta()).getDamage() == 0) {
                                    bow = true;
                                }
                            }
                            if (magicDust >= 2 && bow) {
                                var success = GuardManager.getInstance().setType(guard, GuardManager.Type.GUNNER);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    bow = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (bow && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!bow && item.getType() == Material.CROSSBOW && ((Damageable) item.getItemMeta()).getDamage() == 0) {
                                            inv.setItem(i, new ItemStack(Material.AIR));
                                            bow = true;
                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                remaining--;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else if (amount == 2 && remaining == 2) {
                                                remaining = 0;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - remaining);
                                                remaining = 0;
                                                inv.setItem(i, item);
                                            }
                                        }
                                    }
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                }
                                gui.close();
                            } else {
                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
                            }
                            return true;
                        },
                        ChatColor.DARK_GRAY + "Gunner - Click to upgrade",
                        ChatColor.GRAY + "Rapidly fires arrows at targets",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Crossbow (Undamaged)",
                        ChatColor.GRAY + "- 2x Magic Dust"
                ));

                // add flamethrower
                gui.addElement(new StaticGuiElement(
                        'c',
                        new ItemStack(Material.BLAZE_POWDER),
                        click -> {
                            var clicker = click.getEvent().getWhoClicked();
                            var contents = clicker.getInventory().getContents();
                            var magicDust = 0;
                            var powder = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.BLAZE_POWDER) {
                                    powder = true;
                                }
                            }
                            if (magicDust >= 2 && powder) {
                                var success = GuardManager.getInstance().setType(guard, GuardManager.Type.FLAMETHROWER);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    powder = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (powder && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!powder && item.getType() == Material.BLAZE_POWDER) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
                                            powder = true;
                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                remaining--;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else if (amount == 2 && remaining == 2) {
                                                remaining = 0;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - remaining);
                                                remaining = 0;
                                                inv.setItem(i, item);
                                            }
                                        }
                                    }
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                }
                                gui.close();
                            } else {
                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
                            }
                            return true;
                        },
                        ChatColor.RED + "Flamethrower - Click to upgrade",
                        ChatColor.GRAY + "Shoots flames at nearby targets",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Blaze powder",
                        ChatColor.GRAY + "- 2x Magic Dust"
                ));

                // add grenader
                gui.addElement(new StaticGuiElement(
                        'd',
                        CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GRENADE, 1),
                        click -> {
                            var clicker = click.getEvent().getWhoClicked();
                            var contents = clicker.getInventory().getContents();
                            var magicDust = 0;
                            var grenade = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.FIREWORK_STAR && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    grenade = true;
                                }
                            }
                            if (magicDust >= 2 && grenade) {
                                var success = GuardManager.getInstance().setType(guard, GuardManager.Type.GRENADER);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    grenade = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (grenade && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!grenade && item.getType() == Material.FIREWORK_STAR && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
                                            grenade = true;
                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                remaining--;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else if (amount == 2 && remaining == 2) {
                                                remaining = 0;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - remaining);
                                                remaining = 0;
                                                inv.setItem(i, item);
                                            }
                                        }
                                    }
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                }
                                gui.close();
                            } else {
                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
                            }
                            return true;
                        },
                        ChatColor.DARK_RED + "Grenader - Click to upgrade",
                        ChatColor.GRAY + "Fires grenades at targets",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Grenade",
                        ChatColor.GRAY + "- 2x Magic Dust"
                ));

                var potion = new ItemStack(Material.POTION);
                CustomItems.getInstance().addEnchant(potion);
                var meta = potion.getItemMeta();
                ((PotionMeta) meta).setColor(Color.fromRGB(0xFFC0CB));
                potion.setItemMeta(meta);

                // add healer
                gui.addElement(new StaticGuiElement(
                        'e',
                        potion,
                        click -> {
                            var clicker = click.getEvent().getWhoClicked();
                            var contents = clicker.getInventory().getContents();
                            var magicDust = 0;
                            var tear = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.GHAST_TEAR) {
                                    tear = true;
                                }
                            }
                            if (magicDust >= 2 && tear) {
                                var success = GuardManager.getInstance().setType(guard, GuardManager.Type.HEALER);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    tear = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (tear && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!tear && item.getType() == Material.GHAST_TEAR) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
                                            tear = true;
                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                remaining--;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else if (amount == 2 && remaining == 2) {
                                                remaining = 0;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - remaining);
                                                remaining = 0;
                                                inv.setItem(i, item);
                                            }
                                        }
                                    }
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                }
                                gui.close();
                            } else {
                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
                            }
                            return true;
                        },
                        ChatColor.LIGHT_PURPLE + "Healer - Click to upgrade",
                        ChatColor.GRAY + "Heals nearby guard crystals",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Ghast Tear",
                        ChatColor.GRAY + "- 2x Magic Dust"
                ));

                // add tank
                gui.addElement(new StaticGuiElement(
                        'f',
                        new ItemStack(Material.SHIELD),
                        click -> {
                            var clicker = click.getEvent().getWhoClicked();
                            var contents = clicker.getInventory().getContents();
                            var magicDust = 0;
                            var shell = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.SHULKER_SHELL) {
                                    shell = true;
                                }
                            }
                            if (magicDust >= 2 && shell) {
                                var success = GuardManager.getInstance().setType(guard, GuardManager.Type.TANK);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    shell = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (shell && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!shell && item.getType() == Material.SHULKER_SHELL) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
                                            shell = true;
                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                remaining--;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else if (amount == 2 && remaining == 2) {
                                                remaining = 0;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - remaining);
                                                remaining = 0;
                                                inv.setItem(i, item);
                                            }
                                        }
                                    }
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                }
                                gui.close();
                            } else {
                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
                            }
                            return true;
                        },
                        ChatColor.DARK_PURPLE + "Tank - Click to upgrade",
                        ChatColor.GRAY + "Does not attack, but is highly resistant to attacks",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Shulker Shell",
                        ChatColor.GRAY + "- 2x Magic Dust"
                ));

                // add teleporter
                gui.addElement(new StaticGuiElement(
                        'g',
                        new ItemStack(Material.ENDER_PEARL),
                        click -> {
                            var clicker = click.getEvent().getWhoClicked();
                            var contents = clicker.getInventory().getContents();
                            var magicDust = 0;
                            var pearl = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.ENDER_PEARL) {
                                    pearl = true;
                                }
                            }
                            if (magicDust >= 2 && pearl) {
                                var success = GuardManager.getInstance().setType(guard, GuardManager.Type.TELEPORTER);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    pearl = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (pearl && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!pearl && item.getType() == Material.ENDER_PEARL) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
                                            pearl = true;
                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                remaining--;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else if (amount == 2 && remaining == 2) {
                                                remaining = 0;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - remaining);
                                                remaining = 0;
                                                inv.setItem(i, item);
                                            }
                                        }
                                    }
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                }
                                gui.close();
                            } else {
                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
                            }
                            return true;
                        },
                        ChatColor.YELLOW + "Teleporter - Click to upgrade",
                        ChatColor.GRAY + "Allows players to teleport to other teleporter crystals",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Ender Pearl",
                        ChatColor.GRAY + "- 2x Magic Dust"
                ));

                // add generator
                gui.addElement(new StaticGuiElement(
                        'h',
                        new ItemStack(Material.DAYLIGHT_DETECTOR),
                        click -> {
                            var clicker = click.getEvent().getWhoClicked();
                            var contents = clicker.getInventory().getContents();
                            var magicDust = 0;
                            var star = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.NETHER_STAR) {
                                    star = true;
                                }
                            }
                            if (magicDust >= 2 && star) {
                                var success = GuardManager.getInstance().setType(guard, GuardManager.Type.GENERATOR);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    star = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (star && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!star && item.getType() == Material.NETHER_STAR) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
                                            star = true;
                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                remaining--;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else if (amount == 2 && remaining == 2) {
                                                remaining = 0;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - remaining);
                                                remaining = 0;
                                                inv.setItem(i, item);
                                            }
                                        }
                                    }
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                }
                                gui.close();
                            } else {
                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
                            }
                            return true;
                        },
                        ChatColor.GOLD + "Generator - Click to upgrade",
                        ChatColor.GRAY + "Magically generates resources",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Nether Star",
                        ChatColor.GRAY + "- 2x Magic Dust"
                ));

                // add snowmaker
                gui.addElement(new StaticGuiElement(
                        'i',
                        new ItemStack(Material.SNOWBALL),
                        click -> {
                            var clicker = click.getEvent().getWhoClicked();
                            var contents = clicker.getInventory().getContents();
                            var magicDust = 0;
                            var ball = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.SNOWBALL) {
                                    ball = true;
                                }
                            }
                            if (magicDust >= 2 && ball) {
                                var success = GuardManager.getInstance().setType(guard, GuardManager.Type.SNOWMAKER);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    ball = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (ball && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!ball && item.getType() == Material.SNOWBALL) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
                                            ball = true;
                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                remaining--;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else if (amount == 2 && remaining == 2) {
                                                remaining = 0;
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - remaining);
                                                remaining = 0;
                                                inv.setItem(i, item);
                                            }
                                        }
                                    }
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                }
                                gui.close();
                            } else {
                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
                            }
                            return true;
                        },
                        ChatColor.AQUA + "Snowmaker - Click to upgrade",
                        ChatColor.GRAY + "Launches a barrage of snowballs to slow and push away targets",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Snowball",
                        ChatColor.GRAY + "- 2x Magic Dust"
                ));
                return gui;
            }
            default -> {
                return null;
            }
        }
    }
}
