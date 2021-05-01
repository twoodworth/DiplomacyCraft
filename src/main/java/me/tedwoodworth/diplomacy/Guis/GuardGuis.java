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
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 6)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 6);
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
                ChatColor.GRAY + "- 6x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 2) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 10)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 2);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 10);
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
                ChatColor.GRAY + "Increase max health to 61 HP (requires Max Health I)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 2x Golden Apple",
                ChatColor.GRAY + "- 10x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 3) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 17)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 17);
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
                ChatColor.GRAY + "Increase max health to 107 HP (requires Max Health II)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Golden Apple",
                ChatColor.GRAY + "- 17x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 4) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 29)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 4);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 29);
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
                ChatColor.GRAY + "Increase max health to 188 HP (requires Max Health III)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 4x Golden Apple",
                ChatColor.GRAY + "- 29x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 5) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 50)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 5);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 50);
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
                ChatColor.GRAY + "Increase max health to 350 HP (requires Max Health IV)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 5x Golden Apple",
                ChatColor.GRAY + "- 50x Magic Dust"
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
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 7) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 85)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 7);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 85);
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
                ChatColor.GRAY + "Increase max health to 574 HP (requires Max Health V)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 7x Golden Apple",
                ChatColor.GRAY + "- 85x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[6]),
                click -> {
                    if (materials[6] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 9) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 145)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 9);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 145);
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
                ChatColor.GRAY + "Increase max health to 1005 HP (requires Max Health VI)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 9x Golden Apple",
                ChatColor.GRAY + "- 145x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[7]),
                click -> {
                    if (materials[7] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 12) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 246)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 12);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 246);
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
                ChatColor.GRAY + "Increase max health to 1759 HP (requires Max Health VII)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 12x Golden Apple",
                ChatColor.GRAY + "- 246x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[8]),
                click -> {
                    if (materials[8] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 20) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 712)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 20);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 712);
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
                ChatColor.GRAY + "Increase max health to 5388 HP (requires Max Health VIII)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 20x Golden Apple",
                ChatColor.GRAY + "- 712x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[9]),
                click -> {
                    if (materials[9] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 32) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 2056)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 32);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 2056);
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
                ChatColor.GRAY + "Increase max health to 16500 HP (requires Max Health IX)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 32x Golden Apple",
                ChatColor.GRAY + "- 2056x Magic Dust"
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
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 6)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 6);
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
                ChatColor.GRAY + "- 6x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 2) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 10)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 2);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 10);
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
                ChatColor.GRAY + "Increase max health to 61 HP (requires Max Health I)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 2x Golden Apple",
                ChatColor.GRAY + "- 10x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 3) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 17)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 17);
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
                ChatColor.GRAY + "Increase max health to 107 HP (requires Max Health II)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 3x Golden Apple",
                ChatColor.GRAY + "- 17x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 4) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 29)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 4);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 29);
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
                ChatColor.GRAY + "Increase max health to 188 HP (requires Max Health III)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 4x Golden Apple",
                ChatColor.GRAY + "- 29x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.GOLDEN_APPLE, 5) &&  CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 50)) {
                            CustomItems.getInstance().removeItems(inv, Material.GOLDEN_APPLE, 5);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 50);
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
                ChatColor.GRAY + "Increase max health to 350 HP (requires Max Health IV)",
                " ",
                ChatColor.BLUE + "Cost:",
                ChatColor.GRAY + "- 5x Golden Apple",
                ChatColor.GRAY + "- 50x Magic Dust"
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
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 2)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 2);
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
                ChatColor.GRAY + "- 2x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 3)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 3);
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
                ChatColor.GRAY + "- 1x Iron Block",
                ChatColor.GRAY + "- 3x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_BLOCK, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 6)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_BLOCK, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 6);
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
                ChatColor.GRAY + "- 3x Iron Block",
                ChatColor.GRAY + "- 6x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 10)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 10);
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
                ChatColor.GRAY + "- 1x Diamond",
                ChatColor.GRAY + "- 10x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 17)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 17);
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
                ChatColor.GRAY + "- 1x Diamond Block",
                ChatColor.GRAY + "- 17x Magic Dust"
        ));


        return group;
    }

    private GuiElementGroup[] getTankProtGroups(Entity guard) {
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
                        if (CustomItems.getInstance().contains(inv, Material.IRON_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 2)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 2);
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
                ChatColor.GRAY + "- 2x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[1]),
                click -> {
                    if (materials[1] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 3)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 3);
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
                ChatColor.GRAY + "- 1x Iron Block",
                ChatColor.GRAY + "- 3x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[2]),
                click -> {
                    if (materials[2] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.IRON_BLOCK, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 6)) {
                            CustomItems.getInstance().removeItems(inv, Material.IRON_BLOCK, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 6);
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
                ChatColor.GRAY + "- 3x Iron Block",
                ChatColor.GRAY + "- 6x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[3]),
                click -> {
                    if (materials[3] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 10)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 10);
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
                ChatColor.GRAY + "- 1x Diamond",
                ChatColor.GRAY + "- 10x Magic Dust"
        ));

        group.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[4]),
                click -> {
                    if (materials[4] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 17)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 17);
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
                ChatColor.GRAY + "- 1x Diamond Block",
                ChatColor.GRAY + "- 17x Magic Dust"
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
                        if (CustomItems.getInstance().contains(inv, Material.DIAMOND_BLOCK, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 28)) {
                            CustomItems.getInstance().removeItems(inv, Material.DIAMOND_BLOCK, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 28);
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
                ChatColor.GRAY + "- 3x Diamond Block",
                ChatColor.GRAY + "- 28x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[6]),
                click -> {
                    if (materials[6] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_SCRAP) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 48)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_SCRAP, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 48);
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
                ChatColor.GRAY + "- 1x Netherite Scrap",
                ChatColor.GRAY + "- 48x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[7]),
                click -> {
                    if (materials[7] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_INGOT) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 82)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_INGOT, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 82);
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
                ChatColor.GRAY + "- 1x Netherite Ingot",
                ChatColor.GRAY + "- 82x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[8]),
                click -> {
                    if (materials[8] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_BLOCK) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 237)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_BLOCK, 1);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 237);
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
                ChatColor.GRAY + "- 1x Netherite Block",
                ChatColor.GRAY + "- 237x Magic Dust"
        ));

        group2.addElement(new StaticGuiElement(
                'g',
                new ItemStack(materials[9]),
                click -> {
                    if (materials[9] == Material.YELLOW_STAINED_GLASS_PANE) {
                        var clicker = click.getEvent().getWhoClicked();
                        var inv = clicker.getInventory();
                        if (CustomItems.getInstance().contains(inv, Material.NETHERITE_BLOCK, 3) && CustomItems.getInstance().contains(inv, CustomItems.CustomID.MAGICAL_DUST, 685)) {
                            CustomItems.getInstance().removeItems(inv, Material.NETHERITE_BLOCK, 3);
                            CustomItems.getInstance().removeCustomItems(inv, CustomItems.CustomID.MAGICAL_DUST, 685);
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
                ChatColor.GRAY + "- 3x Netherite Block",
                ChatColor.GRAY + "- 685x Magic Dust"
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
                        "mp       ",
                        "mp       ",
                        "mp       ",
                        "mp       ",
                        "mp       ",
                        "mp      J"
                };

                InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
                gui.setCloseAction(close -> false);
                gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
                gui.addElement(getMaxHealthGroup(guard)); // max health
                gui.addElement(getResistanceGroup(guard)); // resistance

                // arrow velocity & radius
                // arrow power
                return gui;
            }
            default -> {
                return null;
            }
        }
    }
}
