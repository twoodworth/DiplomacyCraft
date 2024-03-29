package me.tedwoodworth.diplomacy.Guis;

import de.themoep.inventorygui.*;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.stream.Collectors;


public class GuardGuis {
    private static GuardGuis instance = null;

    public static GuardGuis getInstance() {
        if (instance == null) {
            instance = new GuardGuis();
        }
        return instance;
    }

    private ChatColor getColor(Material material) {
        return switch (material) {
            case LIME_STAINED_GLASS_PANE -> ChatColor.GREEN;
            case YELLOW_STAINED_GLASS_PANE -> ChatColor.YELLOW;
            default -> ChatColor.RED;
        };
    }

    private StaticGuiElement getNotifyDamageElement(Entity guard, Player viewer) {
        Material material;
        ChatColor color;
        var isOn = GuardManager.getInstance().getNotifyDamage(guard);
        if (isOn) {
            material = Material.EMERALD_BLOCK;
            color = ChatColor.GREEN;
        } else {
            material = Material.REDSTONE_BLOCK;
            color = ChatColor.RED;
        }

        var e = new StaticGuiElement(
                'A',
                new ItemStack(material),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    GuardManager.getInstance().setNotifyDamage(guard, !isOn);
                    ((Player) clicker).playSound(clicker.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
                    var nGui = generateGui(guard, viewer);
                    nGui.show(clicker);
                    return true;
                },
                color + "Send Damage Notifications",
                ChatColor.GRAY + "When enabled, this guard will notify nation members with",
                ChatColor.GRAY + "the permission 'CanSeeGuardNotifications' whenever it is damaged."
        );
        return e;
    }

    private StaticGuiElement getAttackTrespassers(Entity guard, Player viewer) {
        Material material;
        ChatColor color;
        var isOn = GuardManager.getInstance().getAttackTrespassers(guard);
        if (isOn) {
            material = Material.EMERALD_BLOCK;
            color = ChatColor.GREEN;
        } else {
            material = Material.REDSTONE_BLOCK;
            color = ChatColor.RED;
        }

        var e = new StaticGuiElement(
                'B',
                new ItemStack(material),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    GuardManager.getInstance().setAttackTrespassers(guard, !isOn);
                    ((Player) clicker).playSound(clicker.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
                    var nGui = generateGui(guard, viewer);
                    nGui.show(clicker);
                    return true;
                },
                color + "Attack Trespassers",
                ChatColor.GRAY + "When enabled, this guard will attack any trespasser it sees, regardless of",
                ChatColor.GRAY + "who that trespasser is / what nation they are from.",
                " ",
                ChatColor.GRAY + "A trespasser is a player who is inside territory that they do not have permission",
                ChatColor.GRAY + "to build in. This setting is intended for very high security places."
        );
        return e;
    }

    private StaticGuiElement getAttackNeutral(Entity guard, Player viewer) {
        Material material;
        ChatColor color;
        var isOn = GuardManager.getInstance().getAttackNeutral(guard);
        if (isOn) {
            material = Material.EMERALD_BLOCK;
            color = ChatColor.GREEN;
        } else {
            material = Material.REDSTONE_BLOCK;
            color = ChatColor.RED;
        }

        var e = new StaticGuiElement(
                'C',
                new ItemStack(material),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    GuardManager.getInstance().setAttackNeutral(guard, !isOn);
                    ((Player) clicker).playSound(clicker.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
                    var nGui = generateGui(guard, viewer);
                    nGui.show(clicker);
                    return true;
                },
                color + "Attack Neutral Players",
                ChatColor.GRAY + "When enabled, this guard will attack any player from a neutral nation that it sees."
        );
        return e;
    }

    private StaticGuiElement getAttackAllies(Entity guard, Player viewer) {
        Material material;
        ChatColor color;
        var isOn = GuardManager.getInstance().getAttackAllies(guard);
        if (isOn) {
            material = Material.EMERALD_BLOCK;
            color = ChatColor.GREEN;
        } else {
            material = Material.REDSTONE_BLOCK;
            color = ChatColor.RED;
        }

        var e = new StaticGuiElement(
                'D',
                new ItemStack(material),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    GuardManager.getInstance().setAttackAllies(guard, !isOn);
                    ((Player) clicker).playSound(clicker.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
                    var nGui = generateGui(guard, viewer);
                    nGui.show(clicker);
                    return true;
                },
                color + "Attack Allied Players",
                ChatColor.GRAY + "When enabled, this guard will attack any player from an allied nation that it sees."
        );
        return e;
    }

    private StaticGuiElement getAttackNewbies(Entity guard, Player viewer) {
        Material material;
        ChatColor color;
        var isOn = GuardManager.getInstance().getAttackNewbies(guard);
        if (isOn) {
            material = Material.EMERALD_BLOCK;
            color = ChatColor.GREEN;
        } else {
            material = Material.REDSTONE_BLOCK;
            color = ChatColor.RED;
        }

        var name = GuardManager.getInstance().getNation(guard).getClasses().get(0).getName();

        var e = new StaticGuiElement(
                'E',
                new ItemStack(material),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    GuardManager.getInstance().setAttackNewbies(guard, !isOn);
                    ((Player) clicker).playSound(clicker.getLocation(), Sound.BLOCK_LEVER_CLICK, 1, 1);
                    var nGui = generateGui(guard, viewer);
                    nGui.show(clicker);
                    return true;
                },
                color + "Attack Players of the '" + name + "' Class",
                ChatColor.GRAY + "When enabled, this guard will attack any player from the nation with the",
                ChatColor.GRAY + "'" + name + "' class. This setting is intended for nations with open doors,",
                ChatColor.GRAY + "where players can join without permission and be assigned to the lowest",
                ChatColor.GRAY + "class."
        );
        return e;
    }

    private StaticGuiElement getSelfDestructElement(Entity guard, InventoryGui gui) {
        return new StaticGuiElement(
                'J',
                CustomItems.getInstance().addEnchant(new ItemStack(Material.TNT)),
                click -> {
                    var loc = guard.getLocation();
                    var clicker = click.getEvent().getWhoClicked();
                    var message = ChatColor.RED + "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "] " + ChatColor.DARK_GREEN + "Guard self-destructed by " + clicker.getName();
                    GuardManager.getInstance().sendGuardNotification(guard, message);
                    GuardManager.getInstance().killGuard(guard);
                    gui.close();
                    return true;
                },
                "" + ChatColor.BOLD + ChatColor.RED + "Self Destruct: Click to destroy this crystal"
        );
    }

    private StaticGuiElement getLevelUpElement(Entity guard, Player viewer) {
        var level = GuardManager.getInstance().getLevel(guard);
        if (level == 100) {
            return new StaticGuiElement('l',
                    new ItemStack(Material.NETHER_STAR),
                    "" + ChatColor.GOLD + ChatColor.BOLD + "Max Level Reached");
        } else {
            var cost = GuardManager.getInstance().getCost(guard);
            Material material;
            ChatColor color;
            if (CustomItems.getInstance().contains(viewer.getInventory(), CustomItems.CustomID.MAGICAL_DUST, cost)) {
                material = Material.EXPERIENCE_BOTTLE;
                color = ChatColor.GREEN;
            } else {
                material = Material.GLASS_BOTTLE;
                color = ChatColor.RED;
            }
            return new StaticGuiElement('l',
                    new ItemStack(material),
                    click -> {
                        if (material == Material.EXPERIENCE_BOTTLE) {
                            var clicker = click.getEvent().getWhoClicked();
                            var inv = clicker.getInventory();
                            if (CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, cost)) {
                                CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, cost);
                                GuardManager.getInstance().setLevel(guard, GuardManager.getInstance().getLevel(guard) + 1);
                                ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                generateGui(guard, viewer).show(clicker);
                            } else {
                                generateGui(guard, viewer).show(clicker);
                                ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                                (clicker).sendMessage(ChatColor.RED + "Error: Insufficient amount of magical dust.");
                            }
                        }
                        return true;
                    },
                    ChatColor.YELLOW + "Level up to level " + (level + 1),
                    ChatColor.BLUE + "Cost: " + color + cost + " Magical Dust"
            );
        }
    }


    private StaticGuiElement getStarUpElement(Entity guard, Player viewer) {
        var stars = GuardManager.getInstance().getStarCount(guard);
        if (stars == 3) {
            return new StaticGuiElement('k',
                    new ItemStack(Material.NETHER_STAR),
                    "" + ChatColor.GOLD + ChatColor.BOLD + "Max Stars Reached");
        } else if (stars == 2) {
            ChatColor color;
            Material material;
            if (CustomItems.getInstance().contains(viewer.getInventory(), Material.NETHER_STAR)) {
                material = Material.EXPERIENCE_BOTTLE;
                color = ChatColor.GREEN;
            } else {
                material = Material.GLASS_BOTTLE;
                color = ChatColor.RED;
            }

            return new StaticGuiElement('k',
                    new ItemStack(material),
                    click -> {
                        if (material == Material.EXPERIENCE_BOTTLE) {
                            var clicker = click.getEvent().getWhoClicked();
                            var inv = clicker.getInventory();
                            if (CustomItems.getInstance().contains(inv, Material.NETHER_STAR)) {
                                CustomItems.getInstance().removeItems(inv, Material.NETHER_STAR, 1);
                                GuardManager.getInstance().setStarCount(guard, stars + 1);
                                ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                generateGui(guard, viewer).show(clicker);
                            } else {
                                generateGui(guard, viewer).show(clicker);
                                ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                                (clicker).sendMessage(ChatColor.RED + "Error: Cost items not found.");
                            }
                        }
                        return true;
                    },
                    ChatColor.YELLOW + "Upgrade to three stars (\u272f\u272f\u272f)",
                    ChatColor.BLUE + "Cost: " + color + 1 + " Nether Star",
                    "",
                    ChatColor.GRAY + "Guards with three stars cannot be damaged",
                    ChatColor.GRAY + "until all two-star, one-star, and zero-star",
                    ChatColor.GRAY + "guards within a 64-block radius of them are",
                    ChatColor.GRAY + "destroyed."
            );
        } else if (stars == 1) {
            ChatColor color;
            Material material;
            if (CustomItems.getInstance().contains(viewer.getInventory(), Material.NETHERITE_INGOT)) {
                material = Material.EXPERIENCE_BOTTLE;
                color = ChatColor.GREEN;
            } else {
                material = Material.GLASS_BOTTLE;
                color = ChatColor.RED;
            }

            return new StaticGuiElement('k',
                    new ItemStack(material),
                    click -> {
                        if (material == Material.EXPERIENCE_BOTTLE) {
                            var clicker = click.getEvent().getWhoClicked();
                            var inv = clicker.getInventory();
                            if (CustomItems.getInstance().contains(inv, Material.NETHERITE_INGOT)) {
                                CustomItems.getInstance().removeItems(inv, Material.NETHERITE_INGOT, 1);
                                GuardManager.getInstance().setStarCount(guard, stars + 1);
                                ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                generateGui(guard, viewer).show(clicker);
                            } else {
                                generateGui(guard, viewer).show(clicker);
                                ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                                (clicker).sendMessage(ChatColor.RED + "Error: Cost items not found.");
                            }
                        }
                        return true;
                    },
                    ChatColor.YELLOW + "Upgrade to two stars (\u272f\u272f)",
                    ChatColor.BLUE + "Cost: " + color + 1 + " Netherite Ingot",
                    "",
                    ChatColor.GRAY + "Guards with two stars cannot be damaged",
                    ChatColor.GRAY + "until all one-star and zero-star guards",
                    ChatColor.GRAY + "within a 32-block radius of them are",
                    ChatColor.GRAY + "destroyed."
            );
        } else {
            ChatColor color;
            Material material;
            if (CustomItems.getInstance().contains(viewer.getInventory(), Material.EMERALD, 8)) {
                material = Material.EXPERIENCE_BOTTLE;
                color = ChatColor.GREEN;
            } else {
                material = Material.GLASS_BOTTLE;
                color = ChatColor.RED;
            }

            return new StaticGuiElement('k',
                    new ItemStack(material),
                    click -> {
                        if (material == Material.EXPERIENCE_BOTTLE) {
                            var clicker = click.getEvent().getWhoClicked();
                            var inv = clicker.getInventory();
                            if (CustomItems.getInstance().contains(inv, Material.EMERALD, 8)) {
                                CustomItems.getInstance().removeItems(inv, Material.EMERALD, 8);
                                GuardManager.getInstance().setStarCount(guard, stars + 1);
                                ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                generateGui(guard, viewer).show(clicker);
                            } else {
                                generateGui(guard, viewer).show(clicker);
                                ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                                (clicker).sendMessage(ChatColor.RED + "Error: Cost items not found.");
                            }
                        }
                        return true;
                    },
                    ChatColor.YELLOW + "Upgrade to one star (\u272f)",
                    ChatColor.BLUE + "Cost: " + color + 8 + " Emerald",
                    "",
                    ChatColor.GRAY + "Guards with one star cannot be damaged",
                    ChatColor.GRAY + "until all zero-star guards within a 16-block",
                    ChatColor.GRAY + "radius of them are destroyed."
            );
        }
    }

    public InventoryGui generateGui(Entity guard, Player viewer) {
        var type = GuardManager.getInstance().getType(guard);

        switch (type) {
            case BASIC -> {
                // title
                var title = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Level 0 Guard Crystal";

                // Crate setup
                String[] guiSetup = {
                        "abcefghid",
                        "    A    ",
                        "    J    "
                };

                InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
                gui.setCloseAction(close -> false);
                gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

                gui.addElement(getSelfDestructElement(guard, gui));
                gui.addElement(getNotifyDamageElement(guard, viewer));
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
                            if (magicDust >= 1 && bow) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.SNIPER);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    bow = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (bow && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!bow && item.getType() == Material.BOW && ((Damageable) item.getItemMeta()).getDamage() == 0) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
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
                                    var nGui = generateGui(guard, viewer);
                                    ((Player) clicker).playSound(((Player) clicker).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    nGui.show(clicker);
                                } else {
                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
                                    ((Player) clicker).playSound(((Player) clicker).getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                                    gui.close();
                                }
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
                            if (magicDust >= 1 && bow) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.GUNNER);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    bow = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (bow && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!bow && item.getType() == Material.CROSSBOW && ((Damageable) item.getItemMeta()).getDamage() == 0) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
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
                                    var nGui = generateGui(guard, viewer);
                                    ((Player) clicker).playSound(((Player) clicker).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    nGui.show(clicker);
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
                            if (magicDust >= 1 && powder) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.FLAMETHROWER);
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
                                    var nGui = generateGui(guard, viewer);
                                    ((Player) clicker).playSound(((Player) clicker).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    nGui.show(clicker);
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

//                // add grenader
//                gui.addElement(new StaticGuiElement(
//                        'd',
//                        CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GRENADE, 1),
//                        click -> {
//                            var clicker = click.getEvent().getWhoClicked();
//                            var contents = clicker.getInventory().getContents();
//                            var magicDust = 0;
//                            var grenade = false;
//                            for (var item : contents) {
//                                if (item == null) continue;
//                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
//                                    magicDust += item.getAmount();
//                                } else if (item.getType() == Material.FIREWORK_STAR && CustomItemGenerator.getInstance().isCustomItem(item)) {
//                                    grenade = true;
//                                }
//                            }
//                            if (magicDust >= 1 && grenade) {
//                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.GRENADER);
//                                if (success) {
//                                    var inv = clicker.getInventory();
//                                    var remaining = 2;
//                                    grenade = false;
//                                    for (int i = 0; i < inv.getSize(); i++) {
//                                        if (grenade && remaining == 0) break;
//                                        var item = inv.getItem(i);
//                                        if (item == null) continue;
//                                        if (!grenade && item.getType() == Material.FIREWORK_STAR && CustomItemGenerator.getInstance().isCustomItem(item)) {
//                                            var amount = item.getAmount();
//                                            if (amount == 1) {
//                                                inv.setItem(i, new ItemStack(Material.AIR));
//                                            } else {
//                                                item.setAmount(amount - 1);
//                                                inv.setItem(i, item);
//                                            }
//                                            grenade = true;
//                                        } else if (remaining > 0 && item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
//                                            var amount = item.getAmount();
//                                            if (amount == 1) {
//                                                remaining--;
//                                                inv.setItem(i, new ItemStack(Material.AIR));
//                                            } else if (amount == 2 && remaining == 2) {
//                                                remaining = 0;
//                                                inv.setItem(i, new ItemStack(Material.AIR));
//                                            } else {
//                                                item.setAmount(amount - remaining);
//                                                remaining = 0;
//                                                inv.setItem(i, item);
//                                            }
//                                        }
//                                    }
//                                    var nGui = generateGui(guard, viewer);
//                                    ((Player) clicker).playSound(((Player) clicker).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
//                                    nGui.show(clicker);
//                                } else {
//                                    clicker.sendMessage(ChatColor.RED + "Error: guard is already set to this type.");
//                                }
//                                gui.close();
//                            } else {
//                                clicker.sendMessage(ChatColor.RED + "You do not have the required ingredients.");
//                            }
//                            return true;
//                        },
//                        ChatColor.DARK_RED + "Grenader - Click to upgrade",
//                        ChatColor.GRAY + "Fires grenades at targets",
//                        " ",
//                        ChatColor.BLUE + "Cost:",
//                        ChatColor.GRAY + "- 1x Grenade",
//                        ChatColor.GRAY + "- 2x Magic Dust"
//                ));

                var potion = new ItemStack(Material.POTION);
                CustomItems.getInstance().addEnchant(potion);
                var meta = potion.getItemMeta();
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
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
                            if (magicDust >= 1 && tear) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.HEALER);
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
                                    var nGui = generateGui(guard, viewer);
                                    ((Player) clicker).playSound(((Player) clicker).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    nGui.show(clicker);
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
                            var shield = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.SHIELD && ((Damageable) item.getItemMeta()).getDamage() == 0) {
                                    shield = true;
                                }
                            }
                            if (magicDust >= 1 && shield) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.TANK);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    shield = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (shield && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!shield && item.getType() == Material.SHIELD && ((Damageable) item.getItemMeta()).getDamage() == 0) {
                                            var amount = item.getAmount();
                                            if (amount == 1) {
                                                inv.setItem(i, new ItemStack(Material.AIR));
                                            } else {
                                                item.setAmount(amount - 1);
                                                inv.setItem(i, item);
                                            }
                                            shield = true;
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
                                    var nGui = generateGui(guard, viewer);
                                    ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    nGui.show(clicker);
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
                        ChatColor.GRAY + "Has high health, is highly resistant, and fires explosive missiles",
                        " ",
                        ChatColor.BLUE + "Cost:",
                        ChatColor.GRAY + "- 1x Shield (Undamaged)",
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
                            if (magicDust >= 1 && pearl) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.TELEPORTER);
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
                                    var nGui = generateGui(guard, viewer);
                                    ((Player) clicker).playSound(((Player) clicker).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    nGui.show(clicker);
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
                                } else if (item.getType() == Material.DAYLIGHT_DETECTOR) {
                                    star = true;
                                }
                            }
                            if (magicDust >= 1 && star) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.GENERATOR);
                                if (success) {
                                    var inv = clicker.getInventory();
                                    var remaining = 2;
                                    star = false;
                                    for (int i = 0; i < inv.getSize(); i++) {
                                        if (star && remaining == 0) break;
                                        var item = inv.getItem(i);
                                        if (item == null) continue;
                                        if (!star && item.getType() == Material.DAYLIGHT_DETECTOR) {
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
                                    var nGui = generateGui(guard, viewer);
                                    ((Player) clicker).playSound(((Player) clicker).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    nGui.show(clicker);
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
                        ChatColor.GRAY + "- 1x Light Detector",
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
                            if (magicDust >= 1 && ball) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.SNOWMAKER);
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
                                    var nGui = generateGui(guard, viewer);
                                    ((Player) clicker).playSound(((Player) clicker).getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                                    nGui.show(clicker);
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
            case SNIPER, GUNNER, TANK, FLAMETHROWER, HEALER, SNOWMAKER -> {
                // title
                var title = GuardManager.getInstance().getTypePrefix(guard);

                // Crate setup
                String[] guiSetup = {
                        "         ",
                        "lkABCDE J",
                        "         ",
                };

                InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
                gui.setCloseAction(close -> false);
                gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                gui.addElement(getLevelUpElement(guard, viewer)); // level up
                gui.addElement(getStarUpElement(guard, viewer)); // star up
                gui.addElement(getNotifyDamageElement(guard, viewer)); // guard notify damage
                gui.addElement(getAttackTrespassers(guard, viewer)); // tresspassers
                gui.addElement(getAttackAllies(guard, viewer)); // allies
                gui.addElement(getAttackNeutral(guard, viewer)); // neutral
                gui.addElement(getAttackNewbies(guard, viewer)); // newbies
                gui.addElement(getSelfDestructElement(guard, gui)); // auto kill

                return gui;
            }
            case TELEPORTER -> {
                InventoryGui gui;
                if (GuardManager.getInstance().canManageGuard(viewer, guard)) {
                    // title
                    var title = GuardManager.getInstance().getTypePrefix(guard);

                    // Crate setup
                    String[] guiSetup = {
                            "         ",
                            "lkABC   J",
                            "         ",
                    };

                    gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
                    gui.setCloseAction(close -> false);
                    gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                    gui.addElement(getLevelUpElement(guard, viewer)); // level up
                    gui.addElement(getStarUpElement(guard, viewer)); // star up
                    gui.addElement(getNotifyDamageElement(guard, viewer)); // guard notify damage
                    gui.addElement(getToggleIncomingTeleportersElement(guard, viewer, gui)); // toggle teleporters
                    gui.addElement(getViewDestinationsElement(guard, viewer, gui)); // view destinations
                    gui.addElement(getSelfDestructElement(guard, gui)); // auto kill
                } else {
                    gui = getViewDestinationsGui(guard, viewer);
                }
                return gui;
            }
            case GENERATOR -> {
                // title
                var title = GuardManager.getInstance().getTypePrefix(guard);

                // Crate setup
                String[] guiSetup = {
                        "         ",
                        "lkA    J ",
                        "         ",
                        " mnopqrs ",
                        " tuvw    ",
                        "         "
                };

                var container = guard.getPersistentDataContainer();

                var diamond = container.get(GuardManager.getInstance().GENERATOR_DIAMOND_KEY, PersistentDataType.DOUBLE);
                var emerald = container.get(GuardManager.getInstance().GENERATOR_EMERALD_KEY, PersistentDataType.DOUBLE);
                var glowstone = container.get(GuardManager.getInstance().GENERATOR_GLOWSTONE_KEY, PersistentDataType.DOUBLE);
                var gold = container.get(GuardManager.getInstance().GENERATOR_GOLD_KEY, PersistentDataType.DOUBLE);
                var gunpowder = container.get(GuardManager.getInstance().GENERATOR_GUNPOWDER_KEY, PersistentDataType.DOUBLE);
                var iron = container.get(GuardManager.getInstance().GENERATOR_IRON_KEY, PersistentDataType.DOUBLE);
                var lapis = container.get(GuardManager.getInstance().GENERATOR_LAPIS_KEY, PersistentDataType.DOUBLE);
                var magicDust = container.get(GuardManager.getInstance().GENERATOR_MAGIC_DUST_KEY, PersistentDataType.DOUBLE);
                var netherite = container.get(GuardManager.getInstance().GENERATOR_NETHERITE_KEY, PersistentDataType.DOUBLE);
                var quartz = container.get(GuardManager.getInstance().GENERATOR_QUARTZ_KEY, PersistentDataType.DOUBLE);
                var redstone = container.get(GuardManager.getInstance().GENERATOR_REDSTONE_KEY, PersistentDataType.DOUBLE);

                InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
                gui.setCloseAction(close -> false);
                gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                gui.addElement(getLevelUpElement(guard, viewer)); // level up
                gui.addElement(getStarUpElement(guard, viewer)); // star up
                gui.addElement(getNotifyDamageElement(guard, viewer)); // guard notify damage
                gui.addElement(getSelfDestructElement(guard, gui)); // auto kill

                gui.addElement(new StaticGuiElement('m',
                                new ItemStack(Material.DIAMOND),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Diamond",
                                ChatColor.GRAY + String.format("%.2f", diamond * 100) + "% of next diamond generated"
                        )
                );
                gui.addElement(new StaticGuiElement('n',
                                new ItemStack(Material.EMERALD),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Emerald",
                                ChatColor.GRAY + String.format("%.2f", emerald * 100) + "% of next emerald generated"
                        )
                );
                gui.addElement(new StaticGuiElement('o',
                                new ItemStack(Material.GLOWSTONE_DUST),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Glowstone Dust",
                                ChatColor.GRAY + String.format("%.2f", glowstone * 100) + "% of next glowstone dust generated"
                        )
                );
                gui.addElement(new StaticGuiElement('p',
                                new ItemStack(Material.GOLD_NUGGET),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Gold Nugget",
                                ChatColor.GRAY + String.format("%.2f", gold * 100) + "% of next gold nugget generated"
                        )
                );
                gui.addElement(new StaticGuiElement('q',
                                new ItemStack(Material.GUNPOWDER),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Gunpowder",
                                ChatColor.GRAY + String.format("%.2f", gunpowder * 100) + "% of next gunpowder generated"
                        )
                );
                gui.addElement(new StaticGuiElement('r',
                                new ItemStack(Material.IRON_NUGGET),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Iron Nugget",
                                ChatColor.GRAY + String.format("%.2f", iron * 100) + "% of next iron nugget generated"
                        )
                );
                gui.addElement(new StaticGuiElement('s',
                                new ItemStack(Material.LAPIS_LAZULI),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Lapis Lazuli",
                                ChatColor.GRAY + String.format("%.2f", lapis * 100) + "% of next lapis lazuili generated"
                        )
                );
                gui.addElement(new StaticGuiElement('t',
                                CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, 1),
                                "" + ChatColor.GOLD + ChatColor.BOLD + "Magic Dust",
                                ChatColor.GRAY + String.format("%.2f", magicDust * 100) + "% of next magic dust generated"
                        )
                );
                gui.addElement(new StaticGuiElement('u',
                                new ItemStack(Material.NETHERITE_SCRAP),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Netherite Scrap",
                                ChatColor.GRAY + String.format("%.2f", netherite * 100) + "% of next netherite scrap generated"
                        )
                );
                gui.addElement(new StaticGuiElement('v',
                                new ItemStack(Material.QUARTZ),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Nether Quartz",
                                ChatColor.GRAY + String.format("%.2f", quartz * 100) + "% of next nether quartz generated"
                        )
                );
                gui.addElement(new StaticGuiElement('w',
                                new ItemStack(Material.REDSTONE),
                                "" + ChatColor.WHITE + ChatColor.BOLD + "Redstone Dust",
                                ChatColor.GRAY + String.format("%.2f", redstone * 100) + "% of next redstone dust generated"
                        )
                );
                return gui;
            }
            default -> {
                return null;
            }
        }
    }

    private StaticGuiElement getToggleIncomingTeleportersElement(Entity guard, Player viewer, InventoryGui gui) {
        return new StaticGuiElement('B',
                new ItemStack(Material.REPEATER),
                click -> {
                    if (guard.isDead()) {
                        gui.close();
                        viewer.sendMessage(ChatColor.RED + "Error: The guard you were managing was destroyed.");
                        return true;
                    }
                    var nGui = getToggleIncomingTeleportersGui(guard, viewer);
                    nGui.show(viewer);
                    return true;
                },
                "" + ChatColor.BLUE + ChatColor.BOLD + "Toggle incoming teleporters",
                ChatColor.GRAY + "Allow/prevent other teleporters from teleporting players here"
        );
    }

    private StaticGuiElement getViewDestinationsElement(Entity guard, Player viewer, InventoryGui gui) {
        return new StaticGuiElement('C',
                new ItemStack(Material.FILLED_MAP),
                click -> {
                    if (guard.isDead()) {
                        gui.close();
                        viewer.sendMessage(ChatColor.RED + "Error: The guard you were managing was destroyed.");
                        return true;
                    }
                    var nGui = getViewDestinationsGui(guard, viewer);
                    nGui.show(viewer);
                    return true;
                },
                "" + ChatColor.BLUE + ChatColor.BOLD + "View Destinations"
        );
    }

    private InventoryGui getToggleIncomingTeleportersGui(Entity guard, Player viewer) {
        // title
        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Incoming teleporters";

        // Crate setup
        String[] guiSetup = {
                " B  I    ",
                "ggggggggg",
                "ggggggggg",
                "ggggggggg",
                "ggggggggg",
                " P     N "
        };

        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        gui.setCloseAction(close -> false);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        gui.addElement(new StaticGuiElement('B',
                        new ItemStack(Material.ARROW),
                        click -> {
                            if (guard.isDead()) {
                                gui.close();
                                viewer.sendMessage(ChatColor.RED + "Error: The guard you were managing was destroyed.");
                                return true;
                            }
                            var nGui = generateGui(guard, viewer);
                            nGui.show(viewer);
                            return true;
                        },
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to primary menu"
                )
        );
        gui.addElement(new StaticGuiElement('I',
                        new ItemStack(Material.PAPER),
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Incoming teleporters",
                        ChatColor.GRAY + "Players can potentially teleport to this teleporter from ",
                        ChatColor.GRAY + "any of the teleporters shown below. To prevent specific ",
                        ChatColor.GRAY + "teleporters from being able to teleport players to",
                        ChatColor.GRAY + "this location, toggle it by clicking below."
                )
        );
        var group = new GuiElementGroup('g');
        var gLoc = guard.getLocation();
        var tps = GuardManager.getInstance().getAllTeleporters();
        var tpList = tps.stream().filter((p) -> p.getLocation().distanceSquared(gLoc) < Math.pow(GuardManager.getInstance().getRadius(p), 2)).sorted((p1, p2) -> GuardManager.getInstance().getName(p1).compareToIgnoreCase(GuardManager.getInstance().getName(p2))).collect(Collectors.toList());
        var allowed = GuardManager.getInstance().getAllowedTeleporters(guard);
        for (var tp : tpList) {
            if (tp.equals(guard)) continue;
            var loc = tp.getLocation();
            String defaultState;
            // get default state
            if (allowed.contains(tp)) {
                defaultState = "enabled";
            } else {
                defaultState = "disabled";
            }
            var chunk = loc.getChunk();
            var dc = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
            String g;
            if (dc.getGroup() == null) {
                g = "N/A";
            } else {
                g = dc.getGroup().getName();
            }
            var name = GuardManager.getInstance().getName(tp);
            // create state1
            var state1 = new GuiStateElement.State(
                    change -> {
                        if (!tp.isDead()) {
                            if (guard.isDead()) {
                                gui.close();
                                viewer.sendMessage(ChatColor.RED + "Error: The guard you were managing was destroyed.");
                                return;
                            }
                            GuardManager.getInstance().addAllowedTeleporter(guard, tp);
                        } else {
                            gui.close();
                            viewer.sendMessage(ChatColor.RED + "Error: That teleporter no longer exists.");
                        }
                    },
                    "enabled",
                    new ItemStack(Material.EMERALD_BLOCK),
                    "" + ChatColor.GREEN + ChatColor.BOLD + name,
                    ChatColor.BLUE + "Nation: " + ChatColor.GRAY + dc.getNation().getName(),
                    ChatColor.BLUE + "Group: " + ChatColor.GRAY + g,
                    " ",
                    ChatColor.RED + "Click to disable"
            );
            // create state2
            var state2 = new GuiStateElement.State(
                    change -> {
                        if (!tp.isDead()) {
                            if (guard.isDead()) {
                                gui.close();
                                viewer.sendMessage(ChatColor.RED + "Error: The guard you were managing was destroyed.");
                                return;
                            }
                            GuardManager.getInstance().removeAllowedTeleporter(guard, tp);
                        } else {
                            gui.close();
                            viewer.sendMessage(ChatColor.RED + "Error: That teleporter no longer exists.");
                        }
                    },
                    "disabled",
                    new ItemStack(Material.REDSTONE_BLOCK),
                    "" + ChatColor.RED + ChatColor.BOLD + name,
                    ChatColor.BLUE + "Nation: " + ChatColor.GRAY + dc.getNation().getName(),
                    ChatColor.BLUE + "Group: " + ChatColor.GRAY + g,
                    " ",
                    ChatColor.GREEN + "Click to enable"
            );
            var element = new GuiStateElement('g', defaultState, state1, state2);
            group.addElement(element);
        }

        gui.addElement(group);
        // create next page
        var pageElement = new GuiPageElement('N',
                new ItemStack(Material.LIME_STAINED_GLASS_PANE),
                GuiPageElement.PageAction.NEXT,
                ChatColor.GREEN + "Next Page");
        gui.addElement(pageElement);

        // create previous page
        pageElement = new GuiPageElement('P',
                new ItemStack(Material.RED_STAINED_GLASS_PANE),
                GuiPageElement.PageAction.PREVIOUS,
                ChatColor.RED + "Previous Page");
        gui.addElement(pageElement);
        return gui;

    }

    private InventoryGui getViewDestinationsGui(Entity guard, Player viewer) {
        // title
        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Teleporter Destinations";

        // Crate setup
        String[] guiSetup = {
                " B  I    ",
                "ggggggggg",
                "ggggggggg",
                "ggggggggg",
                "ggggggggg",
                " P     N "
        };

        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        gui.setCloseAction(close -> false);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        if (GuardManager.getInstance().canManageGuard(viewer, guard)) {
            gui.addElement(new StaticGuiElement('B',
                            new ItemStack(Material.ARROW),
                            click -> {
                                if (guard.isDead()) {
                                    gui.close();
                                    viewer.sendMessage(ChatColor.RED + "Error: The guard you were managing was destroyed.");
                                    return true;
                                }
                                var nGui = generateGui(guard, viewer);
                                nGui.show(viewer);
                                return true;
                            },
                            "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
                            ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to primary menu"
                    )
            );
        }
        var radius = GuardManager.getInstance().getRadius(guard);
        var radiusSquared = Math.pow(radius, 2);


        var strRadius = String.format("%.1f", radius);
        gui.addElement(new StaticGuiElement('I',
                        new ItemStack(Material.PAPER),
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Teleporter Destination",
                        ChatColor.GRAY + "This teleporter has a radius of " + strRadius + " blocks",
                        ChatColor.GRAY + "and can teleport to any of the locations shown below.",
                        ChatColor.GRAY + "Click one of the icons below to teleport to them."
                )
        );
        var group = new GuiElementGroup('g');
        var tps = GuardManager.getInstance().getAllTeleporters();
        var gLoc = guard.getLocation();
        var sorted = tps.stream()
                .filter((p) -> GuardManager.getInstance().getAllowedTeleporters(p).contains(guard) && p.getLocation().distanceSquared(gLoc) < radiusSquared)
                .sorted((p1, p2) -> GuardManager.getInstance().getName(p1).compareToIgnoreCase(GuardManager.getInstance().getName(p2)))
                .collect(Collectors.toList());
        for (var tp : sorted) {
            if (tp.equals(guard)) continue;
            var loc = tp.getLocation();
            var dc = DiplomacyChunks.getInstance().getDiplomacyChunk(loc.getChunk());
            String g;
            if (dc.getGroup() != null) {
                g = dc.getGroup().getName();
            } else {
                g = "N/A";
            }
            var dist = loc.distance(gLoc);
            var strDist = String.format("%.1f", dist);
            var rate = GuardManager.getInstance().getTeleporterLoadRate(guard);
            var time = dist / 12.0 * rate;
            var strTime = String.format("%.1f", time);
            var element = new StaticGuiElement('g',
                    new ItemStack(Material.END_CRYSTAL),
                    click -> {
                        gui.close();
                        GuardManager.getInstance().beginTeleportation(viewer, guard, tp, (long) (time * 20.0));
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + GuardManager.getInstance().getName(tp),
                    ChatColor.BLUE + "Nation: " + ChatColor.GRAY + dc.getNation().getName(),
                    ChatColor.BLUE + "Group: " + ChatColor.GRAY + g,
                    ChatColor.BLUE + "XYZ: " + ChatColor.GRAY + "[" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + "]",
                    ChatColor.BLUE + "Distance: " + ChatColor.GRAY + strDist + " blocks",
                    ChatColor.BLUE + "Teleport Time: " + ChatColor.GRAY + strTime + "s"
            );
            group.addElement(element);
        }
        gui.addElement(group);
        // create next page
        var pageElement = new GuiPageElement('N',
                new ItemStack(Material.LIME_STAINED_GLASS_PANE),
                GuiPageElement.PageAction.NEXT,
                ChatColor.GREEN + "Next Page");
        gui.addElement(pageElement);

        // create previous page
        pageElement = new GuiPageElement('P',
                new ItemStack(Material.RED_STAINED_GLASS_PANE),
                GuiPageElement.PageAction.PREVIOUS,
                ChatColor.RED + "Previous Page");
        gui.addElement(pageElement);
        return gui;

    }
}
