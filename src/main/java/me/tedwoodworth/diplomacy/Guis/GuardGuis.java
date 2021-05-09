package me.tedwoodworth.diplomacy.Guis;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.PotionMeta;


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

    public InventoryGui generateGui(Entity guard, Player viewer) {
        var type = GuardManager.getInstance().getType(guard);

        switch (type) {
            case BASIC -> {
                // title
                var title = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Level 0 Guard Crystal";

                // Crate setup
                String[] guiSetup = {
                        "abcdefghi",
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
                            if (magicDust >= 1 && grenade) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.GRENADER);
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
                            var shell = false;
                            for (var item : contents) {
                                if (item == null) continue;
                                if (item.getType() == Material.GLOWSTONE_DUST && CustomItemGenerator.getInstance().isCustomItem(item)) {
                                    magicDust += item.getAmount();
                                } else if (item.getType() == Material.SHULKER_SHELL) {
                                    shell = true;
                                }
                            }
                            if (magicDust >= 1 && shell) {
                                var success = GuardManager.getInstance().upgradeDefault(guard, GuardManager.Type.TANK);
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
                        ChatColor.DARK_PURPLE + "Tank - Click to upgrade",
                        ChatColor.GRAY + "Has high health, is highly resistant, and fires explosive missiles",
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
                                } else if (item.getType() == Material.NETHER_STAR) {
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
            case SNIPER, GUNNER, TANK -> {
                var level = GuardManager.getInstance().getLevel(guard);
                // title
                var title = GuardManager.getInstance().getTypePrefix(guard) + ChatColor.DARK_GRAY + " Guard Menu";

                // Crate setup
                String[] guiSetup = {
                        "         ",
                        " lABCDE J",
                        "         ",
                };

                InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
                gui.setCloseAction(close -> false);
                gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                gui.addElement(getLevelUpElement(guard, viewer)); // level up
                gui.addElement(getNotifyDamageElement(guard, viewer)); // guard notify damage
                gui.addElement(getAttackTrespassers(guard, viewer)); // tresspassers
                gui.addElement(getAttackAllies(guard, viewer)); // allies
                gui.addElement(getAttackNeutral(guard, viewer)); // neutral
                gui.addElement(getAttackNewbies(guard, viewer)); // newbies
                gui.addElement(getSelfDestructElement(guard, gui)); // auto kill

                return gui;
            }
            default -> {
                return null;
            }
        }
    }
}
