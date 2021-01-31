package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.events.*;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static me.tedwoodworth.diplomacy.nations.NationGuiFactory.formatter;

public class Guis {
    private static Guis instance = null;

    private final Map<String, InventoryGui> nationMenus = new HashMap<>();
    private final Map<String, InventoryGui> recipeMenus = new HashMap<>();

    public final String RECIPES_KEY = "recipes";
    public final String ITEMS_KEY = "items";
    public final String GUI_METAL_LORE = ChatColor.BLACK + ". ";

    public static Guis getInstance() {
        if (instance == null) {
            instance = new Guis();
        }
        return instance;
    }

    public Guis() {
    }

    public void loadNationMenus() {
        var nations = new ArrayList<>(Nations.getInstance().getNations());
        var size = nations.size();
        var current = 0;
        System.out.println("[Diplomacy] [GUIs] Loading nation menus (" + current + "/" + size + ")");
        for (var nation : nations) {
            getNationMenu(nation);
            current++;
            System.out.println("[Diplomacy] [GUIs] Loading nation menus (" + current + "/" + size + ")");
        }
    }

    public void loadRecipeMenus() {
        // Main Recipe menu
        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Recipes";

        // Crate setup
        String[] guiSetup = {
                "         ",
                "   c e   ",
                "         "
        };

        // Create gui & set filler
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        // add itemElement
        var itemElement = new StaticGuiElement(
                'c',
                new ItemStack(Material.CRAFTING_TABLE),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = recipeMenus.get(ITEMS_KEY);
                    nGui.show(clicker);
                    return true;
                }, //todo view item recipes
                "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Item Recipes",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        gui.addElement(itemElement);

        // add enchantElement
        var enchantElement = new StaticGuiElement(
                'e',
                new ItemStack(Material.ENCHANTING_TABLE),
                click -> {
                    return true;
                }, //todo view enchantment recipes
                "" + ChatColor.AQUA + ChatColor.BOLD + "Enchantment Recipes",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        gui.addElement(enchantElement);

        recipeMenus.put(RECIPES_KEY, gui);

        // item recipe menu
        title = "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Item Recipes";

        // Crate setup
        guiSetup = new String[]{
                " R     M ",
                "iiiiiiiii",
                "iiiiiiiii",
                "iiiiiiiii",
                "iiiiiiiii",
                " P     N ",
        };

        // create item gui
        var itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        // create return to recipes
        var recipeElement = new StaticGuiElement('R',
                new ItemStack(Material.KNOWLEDGE_BOOK),
                click -> {
                    var nGui = recipeMenus.get(RECIPES_KEY);
                    var clicker = click.getEvent().getWhoClicked();
                    nGui.show(clicker);
                    return true;
                },
                ChatColor.YELLOW + "Recipes",
                " ",
                ChatColor.BLUE + "Return to recipe menu");
        itemGui.addElement(recipeElement);

        // create return to menu
        var menuElement = new StaticGuiElement('M',
                new ItemStack(Material.PAINTING),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = NationGuiFactory.createMenu((Player) clicker);
                    nGui.show(clicker);
                    return true;
                },
                ChatColor.YELLOW + "Menu",
                " ",
                ChatColor.BLUE + "Return to menu");
        itemGui.addElement(menuElement);

        // create next page
        var pageElement = new GuiPageElement('N',
                new ItemStack(Material.LIME_STAINED_GLASS_PANE),
                GuiPageElement.PageAction.NEXT,
                ChatColor.GREEN + "Next Page");
        itemGui.addElement(pageElement);

        // create previous page
        pageElement = new GuiPageElement('P',
                new ItemStack(Material.RED_STAINED_GLASS_PANE),
                GuiPageElement.PageAction.PREVIOUS,
                ChatColor.RED + "Previous Page");
        itemGui.addElement(pageElement);


        // create item group
        var itemGroup = new GuiElementGroup('i');

        // add chain helmet element
        var item = new ItemStack(Material.CHAINMAIL_HELMET);
        var meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        var element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Chainmail Helmet",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        // add chain chestplate element
        item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Chainmail Chestplate",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        // add chain leggings element
        item = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Chainmail Leggings",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        // add chain boots element
        item = new ItemStack(Material.CHAINMAIL_BOOTS);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Chainmail Boots",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        // add hunting knives
        item = new ItemStack(Material.WOODEN_SWORD);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Wooden Hunting Knife",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.STONE_SWORD);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Stone Hunting Knife",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.GOLDEN_SWORD);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Golden Hunting Knife",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.IRON_SWORD);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Iron Hunting Knife",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.DIAMOND_SWORD);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Diamond Hunting Knife",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.NETHERITE_SWORD);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Netherite Hunting Knife",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        // add chisels
        item = new ItemStack(Material.WOODEN_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Wooden Chisel",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.STONE_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Stone Chisel",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.GOLDEN_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Golden Chisel",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.IRON_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Iron Chisel",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.DIAMOND_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA+ "Diamond Chisel",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.NETHERITE_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Netherite Chisel",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);
        // add saw elements
        item = new ItemStack(Material.WOODEN_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Wooden Saw",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.STONE_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Stone Saw",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.GOLDEN_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Golden Saw",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.IRON_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Iron Saw",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.DIAMOND_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Diamond Saw",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.NETHERITE_HOE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Netherite Saw",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.CHAINMAIL_HELMET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Wooden Sifter",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.CHAINMAIL_HELMET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "String Sifter",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.CHAINMAIL_HELMET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Chain Sifter",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.CHAINMAIL_HELMET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Redstone Sifter",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.CHAINMAIL_HELMET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Lodestone Sifter",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.CHAINMAIL_HELMET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Magnetic Sifter",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.CHAINMAIL_HELMET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Netherite Sifter",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.FIREWORK_STAR);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Grenade",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.SUGAR);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Iron Dust",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.GLOWSTONE_DUST);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Gold Dust",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.REDSTONE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Ancient Dust",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.IRON_NUGGET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Iron Nugget",
                GUI_METAL_LORE,
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.GOLD_NUGGET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Gold Nugget",
                GUI_METAL_LORE,
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.NETHERITE_SCRAP);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Netherite Scrap",
                GUI_METAL_LORE,
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Refined Iron Plate",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Refined Gold Plate",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.IRON_NUGGET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Refined Iron Nugget",
                GUI_METAL_LORE,
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.GOLD_NUGGET);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Refined Gold Nugget",
                GUI_METAL_LORE,
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.IRON_INGOT);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Refined Iron Ingot",
                GUI_METAL_LORE,
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.GOLD_INGOT);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Refined Gold Ingot",
                GUI_METAL_LORE,
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.IRON_INGOT);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Iron Magnet",
                GUI_METAL_LORE,
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.NETHERITE_INGOT);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Netherite Magnet",
                GUI_METAL_LORE,
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.PAPER);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Coarse Sandpaper",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.PAPER);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Fine Sandpaper",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.BRICK);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Whetstone",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.NETHER_BRICK);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Water Stone",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.SMOOTH_QUARTZ_STAIRS);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Coarse Sharpening Blades",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.POLISHED_BLACKSTONE_STAIRS);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Fine Sharpening Blades",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.END_ROD);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Iron Honing Rod",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.END_ROD);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.AQUA + "Netherite Honing Rod",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.POTION);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ((PotionMeta) meta).setColor(Color.fromRGB(130, 107, 92));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Iron Slurry",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.POTION);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ((PotionMeta) meta).setColor(Color.fromRGB(199, 160, 62));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Gold Slurry",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.POTION);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ((PotionMeta) meta).setColor(Color.fromRGB(50, 0, 10));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Netherite Slurry",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.BRICK);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Slurried Iron Ingot",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.BRICK);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Slurried Gold Ingot",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.BRICK);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Slurried Netherite Ingot",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.NAME_TAG);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    return true;
                }, //todo view recipe
                ChatColor.WHITE + "Item Rename",
                " ",
                "" + ChatColor.RESET + ChatColor.BLUE + "Click to view"
        );
        itemGroup.addElement(element);

        itemGui.addElement(itemGroup);
        recipeMenus.put(ITEMS_KEY, itemGui);
    }

    public InventoryGui getRecipeMenu(String key) {
        return recipeMenus.get(key);
    }

    public InventoryGui getNationMenu(Nation nation) {
        if (!nationMenus.containsKey(nation.getNationID())) {
            var menu = NationGuiFactory.create(nation);
            nationMenus.put(nation.getNationID(), menu);
        }
        return nationMenus.get(nation.getNationID());
    }

    public StaticGuiElement getStatusElement(Nation nation) {
        var isOpen = nation.getIsOpen();
        // Create strStatus & ItemStack
        String status;
        ItemStack itemStack;
        if (isOpen) {
            status = "Open";
            itemStack = new ItemStack(Material.OAK_DOOR); //TODO figure out why door doesn't change when clicking from player menu
        } else {
            status = "Closed";
            itemStack = new ItemStack(Material.IRON_DOOR);
        }

        // Border status element
        return new StaticGuiElement('k',
                itemStack,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var diplomacyClicker = DiplomacyPlayers.getInstance().get(clicker.getUniqueId());
                    var testNation = Nations.getInstance().get(diplomacyClicker);
                    if (!Objects.equals(testNation, nation)) {
                        clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                    try {
                        var nationClass = Objects.requireNonNull(testNation).getMemberClass(diplomacyClicker);
                        var permissions = Objects.requireNonNull(nationClass).getPermissions();
                        if (!permissions.get("CanToggleBorder")) {
                            clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        } else {
                            nation.setIsOpen(!nation.getIsOpen());
                        }
                        return true;
                    } catch (NullPointerException e) {
                        clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Border Status:",
                ChatColor.GRAY + status,
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Toggle border status",
                " ",
                ChatColor.BLUE + "Open Borders: " + ChatColor.GRAY + "/nation open",
                ChatColor.BLUE + "Close Borders: " + ChatColor.GRAY + "/nation close"
        );
    }

    public StaticGuiElement getBannerElement(Nation nation) {
        var banner = nation.getBanner();
        return new StaticGuiElement('b',
                banner,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Banner:",
                " ",
                ChatColor.BLUE + "Change Banner: " + ChatColor.GRAY + "/nation banner"
        );
    }

    public StaticGuiElement getBalanceElement(Nation nation) {
        var nationWealth = nation.getBalance();
        var strNationWealth = "\u00A4" + formatter.format(nationWealth);
        return new StaticGuiElement('e',
                new ItemStack(Material.DIAMOND),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + strNationWealth,
                " ",
                ChatColor.BLUE + "Deposit: " + ChatColor.GRAY + "/nation deposit <amount>",
                ChatColor.BLUE + "Withdraw: " + ChatColor.GRAY + "/nation withdraw <amount>"

        );
    }

    public StaticGuiElement getPlotElement(Nation nation) {
        var plots = nation.getChunks().size();
        var label = " plots";
        if (plots == 1) {
            label = " plot";
        }
        return new StaticGuiElement('l',
                new ItemStack(Material.GRASS_BLOCK),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Territory Size",
                ChatColor.GRAY + String.valueOf(plots) + label
        );
    }

    public StaticGuiElement getNameElement(Nation nation) {
        return new StaticGuiElement('a',
                new ItemStack(Material.NAME_TAG),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Name:",
                ChatColor.GRAY + nation.getName(),
                " ",
                ChatColor.BLUE + "Change Name: " + ChatColor.GRAY + "/nation rename <name>"
        );
    }

    public StaticGuiElement getMembersElement(Nation nation) {
        return new StaticGuiElement('c',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    Nations.getInstance().listMembers((Player) clicker, nation, 1);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",
                ChatColor.BLUE + "Population: " + ChatColor.GRAY + nation.getMembers().size(),
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "List members"
        );
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Guis.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onNationToggleBorder(NationToggleBorderEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(Guis.getInstance().getStatusElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationChangeBanner(NationChangeBannerEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(Guis.getInstance().getBannerElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationChangeBalance(NationChangeBalanceEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            var nationWealth = nation.getBalance();

            var strNationWealth = "\u00A4" + formatter.format(nationWealth);
            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.DIAMOND),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                    ChatColor.BLUE + "Balance: " + ChatColor.GRAY + strNationWealth,
                    " ",
                    ChatColor.BLUE + "Deposit: " + ChatColor.GRAY + "/nation deposit <amount>",
                    ChatColor.BLUE + "Withdraw: " + ChatColor.GRAY + "/nation withdraw <amount>"

            ));
            gui.draw();
        }

        @EventHandler
        private void onNationAddChunks(NationAddChunksEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(Guis.getInstance().getPlotElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationRemoveChunk(NationRemoveChunksEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(Guis.getInstance().getPlotElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationRename(NationRenameEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.setTitle("" + ChatColor.DARK_GRAY + ChatColor.BOLD + nation.getName() + " Main");
            gui.addElement(getNameElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationJoin(NationJoinEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(getMembersElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationLeave(NationLeaveEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(getMembersElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationDisband(NationDisbandEvent event) {
            nationMenus.remove(event.getNationID());
        }

        @EventHandler
        private void onNationCreate(NationCreateEvent event) {
            var nation = event.getNation();
            nationMenus.put(nation.getNationID(), getNationMenu(nation));
        }
    }
}
