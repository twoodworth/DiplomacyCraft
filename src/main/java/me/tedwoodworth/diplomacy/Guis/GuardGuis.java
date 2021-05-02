package me.tedwoodworth.diplomacy.Guis;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
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
        return switch(material) {
            case LIME_STAINED_GLASS_PANE -> ChatColor.GREEN;
            case YELLOW_STAINED_GLASS_PANE -> ChatColor.YELLOW;
            default -> ChatColor.RED;
        };
    }

    private GuiElementGroup[] getTankMaxHealthGroups(Entity guard) {
        var sMaxHealth = GuardManager.getInstance().getShortMaxHealth(guard);
        var materials = new Material[10];
        for (int i = 0; i < sMaxHealth; i++) {
            materials[i] = Material.LIME_STAINED_GLASS_PANE;
        }
        for (int i = sMaxHealth; i < materials.length; i++) {
            if (i == sMaxHealth) {
                materials[i] = Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                materials[i] = Material.RED_STAINED_GLASS_PANE;
            }
        }

        var group = new GuiElementGroup('m');
        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.GOLDEN_APPLE),
                click -> true,
                ChatColor.GOLD + "Max Health Upgrades",
                ChatColor.GRAY + "Increase max health",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[0]),
                click -> {
                    if (materials[0] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 5)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 5);
                            GuardManager.getInstance().setMaxHealth(guard, (short)1);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 15.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[0]) + "Max Health I",
                ChatColor.GRAY + "Increase max health to 35 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Golden Apple",
                ChatColor.GRAY + "- 5x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 1) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 7)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 7);
                            GuardManager.getInstance().setMaxHealth(guard, (short)2);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 26.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[1]) + "Max Health II",
                ChatColor.GRAY + "Increase max health to 61 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Golden Apple",
                ChatColor.GRAY + "- 7x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 2) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 9)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 2);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 9);
                            GuardManager.getInstance().setMaxHealth(guard, (short)3);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 46.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[2]) + "Max Health III",
                ChatColor.GRAY + "Increase max health to 107 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 2x Golden Apple",
                ChatColor.GRAY + "- 9x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 2) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 12)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 2);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 12);
                            GuardManager.getInstance().setMaxHealth(guard, (short)4);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 81.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[3]) + "Max Health IV",
                ChatColor.GRAY + "Increase max health to 188 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 2x Golden Apple",
                ChatColor.GRAY + "- 12x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 3) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 17)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 17);
                            GuardManager.getInstance().setMaxHealth(guard, (short)5);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 162.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[4]) + "Max Health V",
                ChatColor.GRAY + "Increase max health to 350 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Golden Apple",
                ChatColor.GRAY + "- 17x Magic Dust"
        ));
        var groups = new GuiElementGroup[2];
        groups[0] = group;

        var group2 = new GuiElementGroup('n');


        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[5]),
                click -> {
                    if (materials[5] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 4) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 22)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 4);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 22);
                            GuardManager.getInstance().setMaxHealth(guard, (short)6);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 224.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[5]) + "Max Health VI",
                ChatColor.GRAY + "Increase max health to 574 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 4x Golden Apple",
                ChatColor.GRAY + "- 22x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[6]),
                click -> {
                    if (materials[6] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 5) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 30)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 5);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 30);
                            GuardManager.getInstance().setMaxHealth(guard, (short)7);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 431.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[6]) + "Max Health VII",
                ChatColor.GRAY + "Increase max health to 1005 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 5x Golden Apple",
                ChatColor.GRAY + "- 30x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[7]),
                click -> {
                    if (materials[7] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 6) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 41)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 6);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 41);
                            GuardManager.getInstance().setMaxHealth(guard, (short)8);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 754.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[7]) + "Max Health VIII",
                ChatColor.GRAY + "Increase max health to 1759 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 6x Golden Apple",
                ChatColor.GRAY + "- 41x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[8]),
                click -> {
                    if (materials[8] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 8) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 74)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 8);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 74);
                            GuardManager.getInstance().setMaxHealth(guard, (short)9);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 3629.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[8]) + "Max Health IX",
                ChatColor.GRAY + "Increase max health to 5388 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 8x Golden Apple",
                ChatColor.GRAY + "- 74x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[9]),
                click -> {
                    if (materials[9] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 10) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 183)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 10);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 183);
                            GuardManager.getInstance().setMaxHealth(guard, (short)10);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 11112.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[8]) + "Max Health X",
                ChatColor.GRAY + "Increase max health to 16500 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 10x Golden Apple",
                ChatColor.GRAY + "- 183x Magic Dust"
        ));
        groups[1] = group2;

        return groups;
    }

    private GuiElementGroup getMaxHealthGroup(Entity guard) {
        var sMaxHealth = GuardManager.getInstance().getShortMaxHealth(guard);
        var materials = new Material[5];
        for (int i = 0; i < sMaxHealth; i++) {
            materials[i] = Material.LIME_STAINED_GLASS_PANE;
        }
        for (int i = sMaxHealth; i < materials.length; i++) {
            if (i == sMaxHealth) {
                materials[i] = Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                materials[i] = Material.RED_STAINED_GLASS_PANE;
            }
        }

        var group = new GuiElementGroup('m');
        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.GOLDEN_APPLE),
                click -> true,
                ChatColor.GOLD + "Max Health Upgrades",
                ChatColor.GRAY + "Increase max health",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[0]),
                click -> {
                    if (materials[0] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 5)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 5);
                            GuardManager.getInstance().setMaxHealth(guard, (short)1);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 15.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[0]) + "Max Health I",
                ChatColor.GRAY + "Increase max health to 35 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Golden Apple",
                ChatColor.GRAY + "- 5x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 1) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 7)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 7);
                            GuardManager.getInstance().setMaxHealth(guard, (short)2);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 26.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[1]) + "Max Health II",
                ChatColor.GRAY + "Increase max health to 61 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Golden Apple",
                ChatColor.GRAY + "- 7x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 2) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 9)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 2);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 9);
                            GuardManager.getInstance().setMaxHealth(guard, (short)3);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 46.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[2]) + "Max Health III",
                ChatColor.GRAY + "Increase max health to 107 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 2x Golden Apple",
                ChatColor.GRAY + "- 9x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 2) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 12)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 2);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 12);
                            GuardManager.getInstance().setMaxHealth(guard, (short)4);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 81.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[3]) + "Max Health IV",
                ChatColor.GRAY + "Increase max health to 188 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 2x Golden Apple",
                ChatColor.GRAY + "- 12x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 3) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 17)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 17);
                            GuardManager.getInstance().setMaxHealth(guard, (short)5);
                            GuardManager.getInstance().setHealth(guard, GuardManager.getInstance().getHealth(guard) + 162.0);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[4]) + "Max Health V",
                ChatColor.GRAY + "Increase max health to 350 HP",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Golden Apple",
                ChatColor.GRAY + "- 17x Magic Dust"
        ));
        return group;
    }

    private GuiElementGroup getResistanceGroup(Entity guard) {
        var sResistance = GuardManager.getInstance().getShortResistance(guard);
        var materials = new Material[5];
        for (int i = 0; i < sResistance; i++) {
            materials[i] = Material.LIME_STAINED_GLASS_PANE;
        }
        for (int i = sResistance; i < materials.length; i++) {
            if (i == sResistance) {
                materials[i] = Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                materials[i] = Material.RED_STAINED_GLASS_PANE;
            }
        }
        var group = new GuiElementGroup('p');
        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.SHIELD),
                click -> true,
                ChatColor.GOLD + "Resistance Upgrades",
                ChatColor.GRAY + "Increases resistance to damage",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[0]),
                click -> {
                    if (materials[0] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 4)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 4);
                            GuardManager.getInstance().setResistance(guard, (short)1);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[0]) + "Resistance I",
                ChatColor.GRAY + "Increases damage resistance to 6%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Iron Ingot",
                ChatColor.GRAY + "- 4x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 5)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 5);
                            GuardManager.getInstance().setResistance(guard, (short)2);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[1]) + "Resistance II",
                ChatColor.GRAY + "Increases damage resistance to 12%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Iron Ingot",
                ChatColor.GRAY + "- 5x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 7)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 7);
                            GuardManager.getInstance().setResistance(guard, (short)3);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[2]) + "Resistance III",
                ChatColor.GRAY + "Increases damage resistance to 18%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Iron Block",
                ChatColor.GRAY + "- 7x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_BLOCK, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 9)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_BLOCK, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 9);
                            GuardManager.getInstance().setResistance(guard, (short)4);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[3]) + "Resistance IV",
                ChatColor.GRAY + "Increases damage resistance to 24%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Iron Block",
                ChatColor.GRAY + "- 9x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 11)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 11);
                            GuardManager.getInstance().setResistance(guard, (short)5);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[4]) + "Resistance V",
                ChatColor.GRAY + "Increases damage resistance to 31%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Diamond",
                ChatColor.GRAY + "- 11x Magic Dust"
        ));


        return group;
    }

    private GuiElementGroup[] getSniperRadiusGroup(Entity guard) {
        var radius = GuardManager.getInstance().getShortRadius(guard);
        var materials = new Material[10];
        for (int i = 0; i < radius; i++) {
            materials[i] = Material.LIME_STAINED_GLASS_PANE;
        }
        for (int i = radius; i < materials.length; i++) {
            if (i == radius) {
                materials[i] = Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                materials[i] = Material.RED_STAINED_GLASS_PANE;
            }
        }
        var group = new GuiElementGroup('r');
        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.ENDER_EYE),
                click -> true,
                ChatColor.GOLD + "Scope Upgrades (I-V)",
                ChatColor.GRAY + "Increases how far the sniper can see",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));

        group.addElement(new StaticGuiElement( // todo spyglass & 1.17
                'g',
                new ItemStack(materials[0]),
                click -> {
                    if (materials[0] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GLASS_PANE) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 6)) {
                            CustomItems.getInstance().removeItems(inv, Material.GLASS_PANE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 6);
                            GuardManager.getInstance().setRadius(guard, (short)1);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[0]) + "Scope I",
                ChatColor.GRAY + "Increases attack range to 16 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Glass Pane",
                ChatColor.GRAY + "- 6x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 9)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 9);
                            GuardManager.getInstance().setRadius(guard, (short)2);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[1]) + "Scope II",
                ChatColor.GRAY + "Increases attack range to 24 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Iron Ingot",
                ChatColor.GRAY + "- 9x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GLASS_PANE) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 12)) {
                            CustomItems.getInstance().removeItems(inv, Material.GLASS_PANE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 12);
                            GuardManager.getInstance().setRadius(guard, (short)3);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[2]) + "Scope III",
                ChatColor.GRAY + "Increases attack range to 32 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Glass Pane",
                ChatColor.GRAY + "- 12x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 15)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 15);
                            GuardManager.getInstance().setRadius(guard, (short)4);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[3]) + "Scope IV",
                ChatColor.GRAY + "Increases attack range to 40 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Iron Ingot",
                ChatColor.GRAY + "- 15x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GLASS_PANE) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 18)) {
                            CustomItems.getInstance().removeItems(inv, Material.GLASS_PANE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 18);
                            GuardManager.getInstance().setRadius(guard, (short)5);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[4]) + "Scope V",
                ChatColor.GRAY + "Increases attack range to 48 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Glass Pane",
                ChatColor.GRAY + "- 18x Magic Dust"
        ));

        var group2 = new GuiElementGroup('s');
        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.ENDER_EYE),
                click -> true,
                ChatColor.GOLD + "Scope Upgrades (VI-X)",
                ChatColor.GRAY + "Increases how far the sniper can see",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));
        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[5]),
                click -> {
                    if (materials[5] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 24)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 24);
                            GuardManager.getInstance().setRadius(guard, (short)6);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[5]) + "Scope VI",
                ChatColor.GRAY + "Increases attack range to 64 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Diamond",
                ChatColor.GRAY + "- 24x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[6]),
                click -> {
                    if (materials[6] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.QUARTZ) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 30)) {
                            CustomItems.getInstance().removeItems(inv, Material.QUARTZ, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 30);
                            GuardManager.getInstance().setRadius(guard, (short)7);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[6]) + "Scope VII",
                ChatColor.GRAY + "Increases attack range to 80 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Nether Quartz",
                ChatColor.GRAY + "- 30x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[7]),
                click -> {
                    if (materials[7] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 48)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 48);
                            GuardManager.getInstance().setRadius(guard, (short)8);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[7]) + "Scope VIII",
                ChatColor.GRAY + "Increases attack range to 180 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Diamond",
                ChatColor.GRAY + "- 48x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[8]),
                click -> {
                    if (materials[8] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.QUARTZ) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 96)) {
                            CustomItems.getInstance().removeItems(inv, Material.QUARTZ, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 96);
                            GuardManager.getInstance().setRadius(guard, (short)9);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[8]) + "Scope IX",
                ChatColor.GRAY + "Increases attack range to 256 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Quartz",
                ChatColor.GRAY + "- 96x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[9]),
                click -> {
                    if (materials[9] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 150)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 150);
                            GuardManager.getInstance().setRadius(guard, (short)10);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[7]) + "Scope X",
                ChatColor.GRAY + "Increases attack range to 400 blocks",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Diamond",
                ChatColor.GRAY + "- 150x Magic Dust"
        ));
        var groups = new GuiElementGroup[2];
        groups[0] = group;
        groups[1] = group2;
        return groups;
    }

    private GuiElementGroup getSniperPrecisionGroup(Entity guard) {
        var sPrecision = GuardManager.getInstance().getShortSniperPrecision(guard);
        var materials = new Material[5];
        for (int i = 0; i < sPrecision; i++) {
            materials[i] = Material.LIME_STAINED_GLASS_PANE;
        }
        for (int i = sPrecision; i < materials.length; i++) {
            if (i == sPrecision) {
                materials[i] = Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                materials[i] = Material.RED_STAINED_GLASS_PANE;
            }
        }
        var group = new GuiElementGroup('a');
        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.TARGET),
                click -> true,
                ChatColor.GOLD + "Precision Upgrades",
                ChatColor.GRAY + "Increases sniper precision",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[0]),
                click -> {
                    if (materials[0] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.STICK, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 4)) {
                            CustomItems.getInstance().removeItems(inv, Material.STICK, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 4);
                            GuardManager.getInstance().setSniperPrecision(guard, (short)1);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[0]) + "Precision I",
                ChatColor.GRAY + "2x more precise than the base-level",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Stick",
                ChatColor.GRAY + "- 4x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.REDSTONE) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 12)) {
                            CustomItems.getInstance().removeItems(inv, Material.REDSTONE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 12);
                            GuardManager.getInstance().setSniperPrecision(guard, (short)2);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[1]) + "Precision II",
                ChatColor.GRAY + "4x more precise than the base-level",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Redstone",
                ChatColor.GRAY + "- 12x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.TARGET) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 31)) {
                            CustomItems.getInstance().removeItems(inv, Material.TARGET, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 31);
                            GuardManager.getInstance().setSniperPrecision(guard, (short)3);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[2]) + "Precision III",
                ChatColor.GRAY + "12x more precise than the base-level",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Target",
                ChatColor.GRAY + "- 31x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 62)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 62);
                            GuardManager.getInstance().setSniperPrecision(guard, (short)4);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[3]) + "Precision IV",
                ChatColor.GRAY + "48x more precise than the base-level",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Iron Ingot",
                ChatColor.GRAY + "- 62x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 120)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 120);
                            GuardManager.getInstance().setSniperPrecision(guard, (short)5);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[4]) + "Precision V",
                ChatColor.GRAY + "240x more precise than the base-level",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Netherite Ingot",
                ChatColor.GRAY + "- 120x Magic Dust"
        ));
        return group;
    }

    private GuiElementGroup getSniperVelocityGroup(Entity guard) {
        var sVelocity = GuardManager.getInstance().getShortSniperVelocity(guard);
        var materials = new Material[5];
        for (int i = 0; i < sVelocity; i++) {
            materials[i] = Material.LIME_STAINED_GLASS_PANE;
        }
        for (int i = sVelocity; i < materials.length; i++) {
            if (i == sVelocity) {
                materials[i] = Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                materials[i] = Material.RED_STAINED_GLASS_PANE;
            }
        }
        var group = new GuiElementGroup('v');
        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.CLOCK),
                click -> true,
                ChatColor.GOLD + "Velocity Upgrades",
                ChatColor.GRAY + "Increases arrow velocity",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[0]),
                click -> {
                    if (materials[0] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.BAMBOO) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 4)) {
                            CustomItems.getInstance().removeItems(inv, Material.BAMBOO, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 4);
                            GuardManager.getInstance().setSniperVelocity(guard, (short)1);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[0]) + "Velocity I",
                ChatColor.GRAY + "Increases arrow velocity by 50% (to 60 pbs)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Bamboo",
                ChatColor.GRAY + "- 4x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 7)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 7);
                            GuardManager.getInstance().setSniperVelocity(guard, (short)2);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[1]) + "Velocity II",
                ChatColor.GRAY + "Increases arrow velocity by 50% (to 90 pbs)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Iron Ingot",
                ChatColor.GRAY + "- 7x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.REDSTONE, 4) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 12)) {
                            CustomItems.getInstance().removeItems(inv, Material.REDSTONE, 4);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 12);
                            GuardManager.getInstance().setSniperVelocity(guard, (short)3);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[2]) + "Velocity III",
                ChatColor.GRAY + "Increases arrow velocity by 50% (to 135 pbs)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 4x Redstone",
                ChatColor.GRAY + "- 12x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.PISTON) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 38)) {
                            CustomItems.getInstance().removeItems(inv, Material.PISTON, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 38);
                            GuardManager.getInstance().setSniperVelocity(guard, (short)4);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[3]) + "Velocity IV",
                ChatColor.GRAY + "Increases arrow velocity by 50% (to 200 pbs)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Piston",
                ChatColor.GRAY + "- 38x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GUNPOWDER, 32) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 115)) {
                            CustomItems.getInstance().removeItems(inv, Material.GUNPOWDER, 32);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 115);
                            GuardManager.getInstance().setSniperVelocity(guard, (short)5);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[4]) + "Velocity V",
                ChatColor.GRAY + "Increases arrow velocity by 50% (to 300 pbs)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 32x Piston",
                ChatColor.GRAY + "- 115x Magic Dust"
        ));


        return group;
    }

    private GuiElementGroup[] getSniperPowerGroups(Entity guard) {
        var sPower = GuardManager.getInstance().getShortSniperPower(guard);
        var materials = new Material[10];
        for (int i = 0; i < sPower; i++) {
            materials[i] = Material.LIME_STAINED_GLASS_PANE;
        }
        for (int i = sPower; i < materials.length; i++) {
            if (i == sPower) {
                materials[i] = Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                materials[i] = Material.RED_STAINED_GLASS_PANE;
            }
        }
        var group = new GuiElementGroup('t');
        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.ARROW),
                click -> true,
                ChatColor.GOLD + "Power Upgrades (I-V)",
                ChatColor.GRAY + "Increases arrow power",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[0]),
                click -> {
                    if (materials[0] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.FLINT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 7)) {
                            CustomItems.getInstance().removeItems(inv, Material.FLINT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 7);
                            GuardManager.getInstance().setSniperPower(guard, (short)1);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[0]) + "Power I",
                ChatColor.GRAY + "Increases power by 2.0 (to 10.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Flint",
                ChatColor.GRAY + "- 7x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.FLINT, 9) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 9)) {
                            CustomItems.getInstance().removeItems(inv, Material.FLINT, 9);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 9);
                            GuardManager.getInstance().setSniperPower(guard, (short)2);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[1]) + "Power II",
                ChatColor.GRAY + "Increases power by 2.0 (to 12.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 9x Flint",
                ChatColor.GRAY + "- 9x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 11)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 11);
                            GuardManager.getInstance().setSniperPower(guard, (short)3);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[2]) + "Power III",
                ChatColor.GRAY + "Increases power by 2.0 (to 14.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Iron Ingot",
                ChatColor.GRAY + "- 11x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 15)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 15);
                            GuardManager.getInstance().setSniperPower(guard, (short)4);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[3]) + "Power IV",
                ChatColor.GRAY + "Increases power by 2.0 (to 16.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Iron Block",
                ChatColor.GRAY + "- 15x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 19)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 19);
                            GuardManager.getInstance().setSniperPower(guard, (short)5);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[4]) + "Power V",
                ChatColor.GRAY + "Increases power by 2.0 (to 18.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Diamond",
                ChatColor.GRAY + "- 19x Magic Dust"
        ));

        var groups = new GuiElementGroup[2];
        groups[0] = group;


        var group2 = new GuiElementGroup('u');
        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.ARROW),
                click -> true,
                ChatColor.GOLD + "Power Upgrades (VI-X)",
                ChatColor.GRAY + "Increases arrow power",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));



        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[5]),
                click -> {
                    if (materials[5] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.OBSIDIAN) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 25)) {
                            CustomItems.getInstance().removeItems(inv, Material.OBSIDIAN, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 25);
                            GuardManager.getInstance().setSniperPower(guard, (short)6);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[5]) + "Power VI",
                ChatColor.GRAY + "Increases power by 2.0 (to 20.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Obsidian",
                ChatColor.GRAY + "- 25x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[6]),
                click -> {
                    if (materials[6] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 33)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 33);
                            GuardManager.getInstance().setSniperPower(guard, (short)7);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[6]) + "Power VII",
                ChatColor.GRAY + "Increases power by 2.0 (to 22.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Diamond Block",
                ChatColor.GRAY + "- 25x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[7]),
                click -> {
                    if (materials[7] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.OBSIDIAN, 9) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 42)) {
                            CustomItems.getInstance().removeItems(inv, Material.OBSIDIAN, 9);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 42);
                            GuardManager.getInstance().setSniperPower(guard, (short)8);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[7]) + "Power VIII",
                ChatColor.GRAY + "Increases power by 2.0 (to 24.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 9x Obsidian",
                ChatColor.GRAY + "- 42x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[8]),
                click -> {
                    if (materials[8] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_SCRAP) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 72)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_SCRAP, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 72);
                            GuardManager.getInstance().setSniperPower(guard, (short)9);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[8]) + "Power IX",
                ChatColor.GRAY + "Increases power by 4.0 (to 28.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Netherite Scrap",
                ChatColor.GRAY + "- 72x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[9]),
                click -> {
                    if (materials[9] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_INGOT, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 121)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_INGOT, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 121);
                            GuardManager.getInstance().setSniperPower(guard, (short)10);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[9]) + "Power X",
                ChatColor.GRAY + "Increases power by 4.0 (to 32.0 damage points)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Netherite Ingot",
                ChatColor.GRAY + "- 121x Magic Dust"
        ));

        groups[1] = group2;
        return groups;
    }

    private GuiElementGroup[] getTankResistanceGroups(Entity guard) {
        var sMaxHealth = GuardManager.getInstance().getShortResistance(guard);
        var materials = new Material[10];
        for (int i = 0; i < sMaxHealth; i++) {
            materials[i] = Material.LIME_STAINED_GLASS_PANE;
        }
        for (int i = sMaxHealth; i < materials.length; i++) {
            if (i == sMaxHealth) {
                materials[i] = Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                materials[i] = Material.RED_STAINED_GLASS_PANE;
            }
        }
        var group = new GuiElementGroup('p');
        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(Material.SHIELD),
                click -> true,
                ChatColor.GOLD + "Resistance Upgrades",
                ChatColor.GRAY + "Increases resistance to damage",
                ChatColor.GRAY + "Click the yellow panel to upgrade"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[0]),
                click -> {
                    if (materials[0] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 4)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 4);
                            GuardManager.getInstance().setResistance(guard, (short)1);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[0]) + "Resistance I",
                ChatColor.GRAY + "Increases damage resistance to 6%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Iron Ingot",
                ChatColor.GRAY + "- 4x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 5)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 5);
                            GuardManager.getInstance().setResistance(guard, (short)2);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[1]) + "Resistance II",
                ChatColor.GRAY + "Increases damage resistance to 12%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Iron Ingot",
                ChatColor.GRAY + "- 5x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 7)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 7);
                            GuardManager.getInstance().setResistance(guard, (short)3);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[2]) + "Resistance III",
                ChatColor.GRAY + "Increases damage resistance to 18%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Iron Block",
                ChatColor.GRAY + "- 7x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_BLOCK, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 9)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_BLOCK, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 9);
                            GuardManager.getInstance().setResistance(guard, (short)4);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[3]) + "Resistance IV",
                ChatColor.GRAY + "Increases damage resistance to 24%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Iron Block",
                ChatColor.GRAY + "- 9x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 11)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 11);
                            GuardManager.getInstance().setResistance(guard, (short)5);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[4]) + "Resistance V",
                ChatColor.GRAY + "Increases damage resistance to 31%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Diamond",
                ChatColor.GRAY + "- 11x Magic Dust"
        ));

        var groups = new GuiElementGroup[2];
        groups[0] = group;

        var group2 = new GuiElementGroup('q');
        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[5]),
                click -> {
                    if (materials[5] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 14)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 14);
                            GuardManager.getInstance().setResistance(guard, (short)6);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[5]) + "Resistance VI",
                ChatColor.GRAY + "Increases damage resistance to 38%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Diamond",
                ChatColor.GRAY + "- 14x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[6]),
                click -> {
                    if (materials[6] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 19)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 19);
                            GuardManager.getInstance().setResistance(guard, (short)7);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[6]) + "Resistance VII",
                ChatColor.GRAY + "Increases damage resistance to 45%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Diamond Block",
                ChatColor.GRAY + "- 19x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[7]),
                click -> {
                    if (materials[7] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_SCRAP) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 24)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_SCRAP, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 24);
                            GuardManager.getInstance().setResistance(guard, (short)8);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[7]) + "Resistance VIII",
                ChatColor.GRAY + "Increases damage resistance to 52%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Netherite Scrap",
                ChatColor.GRAY + "- 24x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[8]),
                click -> {
                    if (materials[8] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 41)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 41);
                            GuardManager.getInstance().setResistance(guard, (short)9);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[8]) + "Resistance IX",
                ChatColor.GRAY + "Increases damage resistance to 66%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 1x Netherite Ingot",
                ChatColor.GRAY + "- 41x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[9]),
                click -> {
                    if (materials[9] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_INGOT, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 70)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_INGOT, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 70);
                            GuardManager.getInstance().setResistance(guard, (short)10);
                            ((Player) clicker).playSound(clicker.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                            var nGui = GuardGuis.getInstance().generateGui(guard);
                            nGui.show(clicker);
                        }
                    }
                    return true;
                },
                getColor(materials[9]) + "Resistance X",
                ChatColor.GRAY + "Increases damage resistance to 80%",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Netherite Ingot",
                ChatColor.GRAY + "- 70x Magic Dust"
        ));

        groups[1] = group2;
        return groups;
    }

    public InventoryGui generateGui(Entity guard) {
        var type = GuardManager.getInstance().getType(guard);

        switch (type) {
            case BASIC -> {
                // title
                var title = "" + ChatColor.DARK_AQUA + ChatColor.BOLD + "Basic Guard Crystal";

                // Crate setup
                String[] guiSetup = {
                        "abcdefghi",
                        "         ",
                        "    j    "
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
                                    var nGui = generateGui(guard);
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

                // add kill button
                gui.addElement(new StaticGuiElement(
                        'j',
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
                ));
                return gui;
            }
            case SNIPER -> {

                // title
                var title = "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Sniper Guard Crystal";

                // Crate setup
                String[] guiSetup = {
                        "mprsavtu ",
                        "mprsavtu ",
                        "mprsavtu ",
                        "mprsavtu ",
                        "mprsavtu ",
                        "mprsavtuJ"
                };

                InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
                gui.setCloseAction(close -> false);
                gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                gui.addElement(getMaxHealthGroup(guard)); // max health
                gui.addElement(getResistanceGroup(guard)); // resistance
                var scopes = getSniperRadiusGroup(guard);
                gui.addElement(scopes[0]); // radius
                gui.addElement(scopes[1]); // radius2
                gui.addElement(getSniperPrecisionGroup(guard)); // arrow precision
                gui.addElement(getSniperVelocityGroup(guard)); // arrow velocity
                var power = getSniperPowerGroups(guard);
                gui.addElement(power[0]); // arrow power
                gui.addElement(power[1]); // arrow power2
                // arrow power
                return gui;
            }
            default -> {
                return null;
            }
        }
    }
}
