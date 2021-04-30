package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.*;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyRecipes;
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
    public final String CHAIN_HELMET_KEY = "chain_helmet";
    public final String CHAIN_CHESTPLATE_KEY = "chain_chestplate";
    public final String CHAIN_LEGGINGS_KEY = "chain_leggings";
    public final String CHAIN_BOOTS_KEY = "chain_boots";
    public final String WOODEN_KNIFE_KEY = "wooden_knife";
    public final String STONE_KNIFE_KEY = "stone_knife";
    public final String GOLDEN_KNIFE_KEY = "golden_knife";
    public final String IRON_KNIFE_KEY = "iron_knife";
    public final String DIAMOND_KNIFE_KEY = "diamond_knife";
    public final String NETHERITE_KNIFE_KEY = "netherite_knife";
    public final String WOODEN_CHISEL_KEY = "wooden_chisel";
    public final String STONE_CHISEL_KEY = "stone_chisel";
    public final String GOLDEN_CHISEL_KEY = "golden_chisel";
    public final String IRON_CHISEL_KEY = "iron_chisel";
    public final String DIAMOND_CHISEL_KEY = "diamond_chisel";
    public final String NETHERITE_CHISEL_KEY = "netherite_chisel";
    public final String WOODEN_SAW_KEY = "wooden_chisel";
    public final String STONE_SAW_KEY = "stone_chisel";
    public final String GOLDEN_SAW_KEY = "golden_chisel";
    public final String IRON_SAW_KEY = "iron_chisel";
    public final String DIAMOND_SAW_KEY = "diamond_chisel";
    public final String NETHERITE_SAW_KEY = "netherite_chisel";
    public final String WOODEN_SIFTER_KEY = "wooden_sifter";
    public final String STRING_SIFTER_KEY = "string_sifter";
    public final String CHAIN_SIFTER_KEY = "chain_sifter";
    public final String REDSTONE_SIFTER_KEY = "redstone_sifter";
    public final String LODESTONE_SIFTER_KEY = "lodestone_sifter";
    public final String MAGNETIC_SIFTER_KEY = "magnetic_sifter";
    public final String NETHERITE_SIFTER_KEY = "netherite_sifter";
    public final String SIFTER_DROPS_KEY = "sifter_drops";
    public final String GRENADE_KEY = "grenade";
    public final String IRON_PLATE_KEY = "iron_plate";
    public final String GOLD_PLATE_KEY = "gold_plate";
    public final String REFINED_IRON_NUGGET_KEY = "refined_iron_nugget";
    public final String REFINED_GOLD_NUGGET_KEY = "refined_gold_nugget";
    public final String REFINED_IRON_INGOT_KEY = "refined_iron_ingot";
    public final String REFINED_GOLD_INGOT_KEY = "refined_gold_ingot";
    public final String IRON_MAGNET_KEY = "iron_magnet";
    public final String NETHERITE_MAGNET_KEY = "netherite_magnet";
    public final String COARSE_SANDPAPER_KEY = "coarse_sandpaper";
    public final String FINE_SANDPAPER_KEY = "fine_sandpaper";
    public final String WHETSTONE_KEY = "whestone";
    public final String WATERSTONE_KEY = "waterstone";
    public final String COARSE_BLADES_KEY = "coarse_blades";
    public final String FINE_BLADES_KEY = "fine_blades";
    public final String IRON_ROD_KEY = "iron_rod";
    public final String NETHERITE_ROD_KEY = "netherite_rod";
    public final String IRON_SLURRY_KEY = "iron_slurry";
    public final String GOLD_SLURRY_KEY = "gold_slurry";
    public final String NETHERITE_SLURRY_KEY = "netherite_slurry";
    public final String SLURRIED_IRON_KEY = "slurried_iron";
    public final String SLURRIED_GOLD_KEY = "slurried_gold";
    public final String SLURRIED_NETHERITE_KEY = "slurried_netherite";
    public final String UNBREAKING_IRON_KEY = "unbreaking_iron";
    public final String UNBREAKING_GOLD_KEY = "unbreaking_gold";
    public final String UNBREAKING_NETHERITE_KEY = "unbreaking_netherite";
    public final String RENAME_KEY = "rename";
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
        gui.setCloseAction(close -> false);
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
                },
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
                "" + ChatColor.RESET + ChatColor.RED + "Currently under maintenence",
                ChatColor.GRAY + "Visit #recipes in /discord to view custom enchantment recipes"
        );
        gui.addElement(enchantElement);

        recipeMenus.put(RECIPES_KEY, gui);

        // item menu
        createItemMenu();

        // item recipes
        createItemRecipes();
    }

    private void createItemRecipes() {
        var backElement = new GuiBackElement('B',
                new ItemStack(Material.ARROW),
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to previous page"
        );
        var craftingTableElement = new StaticGuiElement('T',
                new ItemStack(Material.CRAFTING_TABLE),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Crafting Table",
                ChatColor.GRAY + "Item created in a crafting table"
        );
        var smithingTableElement = new StaticGuiElement('T',
                new ItemStack(Material.SMITHING_TABLE),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Smithing Table",
                ChatColor.GRAY + "Item created in a smithing table"
        );
        var lodestoneElement = new StaticGuiElement('T',
                new ItemStack(Material.LODESTONE),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Lodestone",
                ChatColor.GRAY + "Item created by right-clicking a lodestone"
        );
        var airElement = new StaticGuiElement('a',
                new ItemStack(Material.AIR));
        var stickElement = new StaticGuiElement('s',
                new ItemStack(Material.STICK));
        var menuElement = new StaticGuiElement('M',
                new ItemStack(Material.KNOWLEDGE_BOOK),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var menu = recipeMenus.get(ITEMS_KEY);
                    menu.show(clicker);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Recipe List",
                ChatColor.BLUE + "Click " + ChatColor.GRAY + "Return to recipe list"
        );

        // chain helmet
        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Chainmail Helmet";
        var guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " iii aaa ",
                " iai ara ",
                " aaa aaa ",
                "         ",
        };
        var itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.IRON_NUGGET),
                click -> true,
                ChatColor.WHITE + "Iron Nugget",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.CHAINMAIL_HELMET),
                ChatColor.WHITE + "Chainmail Helmet")
        );
        recipeMenus.put(CHAIN_HELMET_KEY, itemGui);

        // chain chestplate
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Chainmail Chestplate";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " iai aaa ",
                " iii ara ",
                " iii aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.IRON_NUGGET),
                click -> true,
                ChatColor.WHITE + "Iron Nugget",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.CHAINMAIL_CHESTPLATE),
                ChatColor.WHITE + "Chainmail Chestplate")
        );
        recipeMenus.put(CHAIN_CHESTPLATE_KEY, itemGui);

        // chain leggings
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Chainmail Chestplate";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " iii aaa ",
                " iai ara ",
                " iai aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.IRON_NUGGET),
                click -> true,
                ChatColor.WHITE + "Iron Nugget",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.CHAINMAIL_LEGGINGS),
                ChatColor.WHITE + "Chainmail Leggings")
        );
        recipeMenus.put(CHAIN_LEGGINGS_KEY, itemGui);

        // chain boots
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Chainmail Boots";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " iai ara ",
                " iai aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.IRON_NUGGET),
                click -> true,
                ChatColor.WHITE + "Iron Nugget",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.CHAINMAIL_BOOTS),
                ChatColor.WHITE + "Chainmail Boots")
        );
        recipeMenus.put(CHAIN_BOOTS_KEY, itemGui);

        // wooden hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Wooden Hunting Knife";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " aia ara ",
                " asa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.OAK_PLANKS),
                click -> true,
                ChatColor.WHITE + "Wooden Planks",
                " ",
                ChatColor.GRAY + "Any wood type will work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.WOODEN_SWORD),
                ChatColor.WHITE + "Wooden Hunting Knife")
        );
        recipeMenus.put(WOODEN_KNIFE_KEY, itemGui);

        // Stone hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Stone Hunting Knife";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " aia ara ",
                " asa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.COBBLESTONE),
                click -> true,
                ChatColor.WHITE + "Cobblestone",
                " ",
                ChatColor.GRAY + "Blackstone will also work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.STONE_SWORD),
                ChatColor.WHITE + "Stone Hunting Knife")
        );
        recipeMenus.put(STONE_KNIFE_KEY, itemGui);

        // Gold hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Golden Hunting Knife";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " aia ara ",
                " asa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.GOLD_INGOT),
                click -> true,
                ChatColor.WHITE + "Gold Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.GOLDEN_SWORD),
                ChatColor.WHITE + "Golden Hunting Knife")
        );
        recipeMenus.put(GOLDEN_KNIFE_KEY, itemGui);

        // Iron hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Iron Hunting Knife";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " aia ara ",
                " asa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.IRON_INGOT),
                click -> true,
                ChatColor.WHITE + "Iron Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.IRON_SWORD),
                ChatColor.WHITE + "Iron Hunting Knife")
        );
        recipeMenus.put(IRON_KNIFE_KEY, itemGui);

        // Diamond hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Diamond Hunting Knife";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " aia ara ",
                " asa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.DIAMOND),
                click -> true,
                ChatColor.WHITE + "Diamond")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.DIAMOND_SWORD),
                ChatColor.WHITE + "Diamond Hunting Knife")
        );
        recipeMenus.put(DIAMOND_KNIFE_KEY, itemGui);

        // Gold hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Netherite Hunting Knife";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     aaa ",
                " i n ara ",
                "     aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(smithingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.DIAMOND_SWORD),
                click -> true,
                ChatColor.WHITE + "Diamond Hunting Knife")
        );
        itemGui.addElement(new StaticGuiElement('n',
                new ItemStack(Material.NETHERITE_INGOT),
                click -> true,
                ChatColor.WHITE + "Netherite Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.NETHERITE_SWORD),
                ChatColor.WHITE + "Netherite Hunting Knife")
        );
        recipeMenus.put(NETHERITE_KNIFE_KEY, itemGui);

        // wooden chisel
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Wooden Chisel";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " sii ara ",
                " aaa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.OAK_PLANKS),
                click -> true,
                ChatColor.WHITE + "Wooden Planks",
                " ",
                ChatColor.GRAY + "Any wood type will work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.WOODEN_HOE),
                ChatColor.WHITE + "Wooden Chisel")
        );
        recipeMenus.put(WOODEN_CHISEL_KEY, itemGui);

        // Stone hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Stone Chisel";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " sii ara ",
                " aaa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.COBBLESTONE),
                click -> true,
                ChatColor.WHITE + "Cobblestone",
                " ",
                ChatColor.GRAY + "Blackstone will also work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.STONE_HOE),
                ChatColor.WHITE + "Stone Chisel")
        );
        recipeMenus.put(STONE_CHISEL_KEY, itemGui);

        // Gold hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Golden Chisel";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " sii ara ",
                " aaa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.GOLD_INGOT),
                click -> true,
                ChatColor.WHITE + "Gold Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.GOLDEN_HOE),
                ChatColor.WHITE + "Golden Chisel")
        );
        recipeMenus.put(GOLDEN_CHISEL_KEY, itemGui);

        // Iron hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Iron Chisel";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " sii ara ",
                " aaa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.IRON_INGOT),
                click -> true,
                ChatColor.WHITE + "Iron Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.IRON_HOE),
                ChatColor.WHITE + "Iron Chisel")
        );
        recipeMenus.put(IRON_CHISEL_KEY, itemGui);

        // Diamond hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Diamond Chisel";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " sii ara ",
                " aaa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.DIAMOND),
                click -> true,
                ChatColor.WHITE + "Diamond")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.DIAMOND_HOE),
                ChatColor.WHITE + "Diamond Chisel")
        );
        recipeMenus.put(DIAMOND_CHISEL_KEY, itemGui);

        // Netherite hunting knife
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Netherite Chisel";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     aaa ",
                " i n ara ",
                "     aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(smithingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.DIAMOND_HOE),
                click -> true,
                ChatColor.WHITE + "Diamond Chisel")
        );
        itemGui.addElement(new StaticGuiElement('n',
                new ItemStack(Material.NETHERITE_INGOT),
                click -> true,
                ChatColor.WHITE + "Netherite Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );

        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.NETHERITE_HOE),
                ChatColor.WHITE + "Netherite Chisel")
        );
        recipeMenus.put(NETHERITE_CHISEL_KEY, itemGui);

        // wooden saw
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Wooden Saw";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " asa aaa ",
                " sas ara ",
                " iii aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.OAK_PLANKS),
                click -> true,
                ChatColor.WHITE + "Wooden Planks",
                " ",
                ChatColor.GRAY + "Any wood type will work")
        );
        var result = new ItemStack(Material.WOODEN_HOE);
        result.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                ChatColor.WHITE + "Wooden Saw")
        );
        recipeMenus.put(WOODEN_SAW_KEY, itemGui);

        // Stone saw
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Stone Saw";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " asa aaa ",
                " sas ara ",
                " iii aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.COBBLESTONE),
                click -> true,
                ChatColor.WHITE + "Cobblestone",
                " ",
                ChatColor.GRAY + "Blackstone will also work")
        );

        result = new ItemStack(Material.STONE_HOE);
        result.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                ChatColor.WHITE + "Stone Saw")
        );
        recipeMenus.put(STONE_SAW_KEY, itemGui);

        // Gold saw
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Golden Saw";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " asa aaa ",
                " sas ara ",
                " iii aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.GOLD_INGOT),
                click -> true,
                ChatColor.WHITE + "Gold Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );

        result = new ItemStack(Material.GOLDEN_HOE);
        result.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                ChatColor.WHITE + "Golden Saw")
        );
        recipeMenus.put(GOLDEN_SAW_KEY, itemGui);

        // Iron saw
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Iron Saw";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " asa aaa ",
                " sas ara ",
                " iii aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.IRON_INGOT),
                click -> true,
                ChatColor.WHITE + "Iron Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );

        result = new ItemStack(Material.IRON_HOE);
        result.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                ChatColor.WHITE + "Iron Saw")
        );
        recipeMenus.put(IRON_SAW_KEY, itemGui);

        // Diamond saw
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Diamond Saw";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " asa aaa ",
                " sas ara ",
                " iii aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.DIAMOND),
                click -> true,
                ChatColor.WHITE + "Diamond")
        );
        result = new ItemStack(Material.DIAMOND_HOE);
        result.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                ChatColor.WHITE + "Diamond Chisel")
        );
        recipeMenus.put(DIAMOND_SAW_KEY, itemGui);

        // Netherite saw
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Netherite Saw";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     aaa ",
                " i n ara ",
                "     aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(smithingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(new StaticGuiElement('i',
                result,
                click -> true,
                ChatColor.WHITE + "Diamond Saw")
        );
        itemGui.addElement(new StaticGuiElement('n',
                new ItemStack(Material.NETHERITE_INGOT),
                click -> true,
                ChatColor.WHITE + "Netherite Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        result = new ItemStack(Material.NETHERITE_HOE);
        result.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                ChatColor.WHITE + "Netherite Saw")
        );
        recipeMenus.put(NETHERITE_SAW_KEY, itemGui);

        // Wooden Sifter
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Wooden Sifter";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " sss aaa ",
                " sss ara ",
                " sss aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        result = new ItemStack(Material.CHAINMAIL_HELMET);
        result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
        var meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                click -> {
                    var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Wooden Sifter",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View info")
        );
        recipeMenus.put(WOODEN_SIFTER_KEY, itemGui);

        // String Sifter
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "String Sifter";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " sis aaa ",
                " sis ara ",
                " sis aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.STRING),
                ChatColor.WHITE + "String")
        );
        result = new ItemStack(Material.CHAINMAIL_HELMET);
        result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 2);
        meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                click -> {
                    var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "String Sifter",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View info")
        );
        recipeMenus.put(STRING_SIFTER_KEY, itemGui);

        // Chain Sifter
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Chain Sifter";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " nnn aaa ",
                " nin ara ",
                " nnn aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.IRON_INGOT),
                ChatColor.WHITE + "Iron Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        itemGui.addElement(new StaticGuiElement('n',
                new ItemStack(Material.IRON_NUGGET),
                ChatColor.WHITE + "Iron Nugget",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        result = new ItemStack(Material.CHAINMAIL_HELMET);
        result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 3);
        result.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                click -> {
                    var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Chain Sifter",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View info")
        );
        recipeMenus.put(CHAIN_SIFTER_KEY, itemGui);

        // Redstone Sifter
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Redstone Sifter";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aoa aaa ",
                " oio ara ",
                " aoa aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                result,
                click -> {
                    var gui = recipeMenus.get(CHAIN_SIFTER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Chain Sifter",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe")
        );
        itemGui.addElement(new StaticGuiElement('o',
                new ItemStack(Material.REDSTONE),
                ChatColor.WHITE + "Redstone")
        );
        result = new ItemStack(Material.CHAINMAIL_HELMET);
        result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 4);
        result.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                click -> {
                    var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Redstone Sifter",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View info")
        );
        recipeMenus.put(REDSTONE_SIFTER_KEY, itemGui);

        // Lodestone Sifter
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Redstone Sifter";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     aaa ",
                "  i  ara ",
                "     aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(lodestoneElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                result,
                click -> {
                    var gui = recipeMenus.get(REDSTONE_SIFTER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Redstone Sifter",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe")
        );
        itemGui.addElement(new StaticGuiElement('o',
                new ItemStack(Material.REDSTONE),
                ChatColor.WHITE + "Redstone")
        );
        result = new ItemStack(Material.CHAINMAIL_HELMET);
        result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 5);
        result.addUnsafeEnchantment(Enchantment.DURABILITY, 2);
        meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                click -> {
                    var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Lodestone Sifter",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View info")
        );
        recipeMenus.put(LODESTONE_SIFTER_KEY, itemGui);


        // Magnetic Sifter
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Magnetic Sifter";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " nnn aaa ",
                " nmn ara ",
                " nnn aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('n',
                new ItemStack(Material.IRON_NUGGET),
                ChatColor.WHITE + "Iron Nugget",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        result = new ItemStack(Material.IRON_INGOT);
        result.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('m',
                result,
                click -> {
                    var gui = recipeMenus.get(IRON_MAGNET_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Iron Magnet",
                GUI_METAL_LORE,
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe")
        );
        result = new ItemStack(Material.CHAINMAIL_HELMET);
        result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 6);
        result.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
        meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                click -> {
                    var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Magnetic Sifter",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View info")
        );
        recipeMenus.put(MAGNETIC_SIFTER_KEY, itemGui);


        // Netherite Sifter
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Netherite Sifter";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " nnn aaa ",
                " nmn ara ",
                " nnn aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('n',
                new ItemStack(Material.IRON_NUGGET),
                ChatColor.AQUA + "Iron Nugget",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        result = new ItemStack(Material.NETHERITE_INGOT);
        result.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('m',
                result,
                click -> {
                    var gui = recipeMenus.get(NETHERITE_MAGNET_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Netherite Magnet",
                GUI_METAL_LORE,
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe")
        );
        result = new ItemStack(Material.CHAINMAIL_HELMET);
        result.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 7);
        result.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
        meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                click -> {
                    var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Netherite Sifter",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View info")
        );
        recipeMenus.put(NETHERITE_SIFTER_KEY, itemGui);


        // Sifter Drops
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Sifter";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "         ",
                "0 12345  ",
                "         ",
                "a bcdefgh",
        };

        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);

        var sifter = new ItemStack(Material.CHAINMAIL_HELMET);
        meta = sifter.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        sifter.setItemMeta(meta);
        sifter.addUnsafeEnchantment(Enchantment.SILK_TOUCH, 1);
        itemGui.addElement(new StaticGuiElement('T',
                sifter,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sifter",
                " ",
                ChatColor.GRAY + "A tool used for carefully digging through gravel",
                ChatColor.GRAY + "and soul sand which yields rarer resources",
                " ",
                ChatColor.BLUE + "Levels:",
                ChatColor.GRAY + "Fortune 0 - Bare Hands",
                ChatColor.GRAY + "Fortune 1 - Wooden Sifter",
                ChatColor.GRAY + "Fortune 2 - String Sifter",
                ChatColor.GRAY + "Fortune 3 - Chain Sifter",
                ChatColor.GRAY + "Fortune 4 - Redstone Sifter",
                ChatColor.GRAY + "Fortune 5 - Lodestone Sifter",
                ChatColor.GRAY + "Fortune 6 - Magnetic Sifter",
                ChatColor.GRAY + "Fortune 7 - Netherite Sifter")
        );

        itemGui.addElement(new StaticGuiElement('0',
                new ItemStack(Material.GRAVEL),
                ChatColor.WHITE + "Gravel",
                " ",
                ChatColor.GRAY + "Items dropped by gravel")
        );

        itemGui.addElement(new StaticGuiElement('1',
                        new ItemStack(Material.FLINT),
                        ChatColor.WHITE + "Flint",
                        " ",
                        ChatColor.BLUE + "Required Level:",
                        ChatColor.GRAY + "Fortune 0"
                )
        );

        itemGui.addElement(new StaticGuiElement('2',
                        new ItemStack(Material.COAL),
                        ChatColor.WHITE + "Coal",
                        " ",
                        ChatColor.BLUE + "Required Level:",
                        ChatColor.GRAY + "Fortune 2"
                )
        );
        var goldDust = new ItemStack(Material.GLOWSTONE_DUST);
        goldDust.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        meta = goldDust.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        goldDust.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('3',
                        goldDust,
                        ChatColor.AQUA + "Gold Dust",
                        " ",
                        ChatColor.BLUE + "Required Level:",
                        ChatColor.GRAY + "Fortune 4",
                        " ",
                        ChatColor.GRAY + "Can be smelted into high-purity gold"
                )
        );

        var ironDust = new ItemStack(Material.SUGAR);
        ironDust.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        meta = ironDust.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ironDust.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('4',
                        ironDust,
                        ChatColor.AQUA + "Iron Dust",
                        " ",
                        ChatColor.BLUE + "Required Level:",
                        ChatColor.GRAY + "Fortune 4",
                        " ",
                        ChatColor.GRAY + "Can be smelted into high-purity iron"
                )
        );

        itemGui.addElement(new StaticGuiElement('5',
                new ItemStack(Material.REDSTONE),
                ChatColor.WHITE + "Redstone Dust",
                " ",
                ChatColor.BLUE + "Required Level:",
                ChatColor.GRAY + "Fortune 5")
        );

        itemGui.addElement(new StaticGuiElement('a',
                new ItemStack(Material.SOUL_SAND),
                ChatColor.WHITE + "Soul Sand",
                " ",
                ChatColor.GRAY + "Items dropped by soul sand")
        );

        itemGui.addElement(new StaticGuiElement('b',
                new ItemStack(Material.SOUL_SOIL),
                ChatColor.WHITE + "Soul Soil",
                " ",
                ChatColor.BLUE + "Required Level:",
                ChatColor.GRAY + "Fortune 0")
        );

        itemGui.addElement(new StaticGuiElement('c',
                new ItemStack(Material.CHARCOAL),
                ChatColor.WHITE + "Charcoal",
                " ",
                ChatColor.BLUE + "Required Level:",
                ChatColor.GRAY + "Fortune 1")
        );

        itemGui.addElement(new StaticGuiElement('d',
                        new ItemStack(Material.GOLD_NUGGET),
                        ChatColor.WHITE + "Gold Nugget",
                        GUI_METAL_LORE,
                        ChatColor.BLUE + "Required Level:",
                        ChatColor.GRAY + "Fortune 3"
                )
        );

        itemGui.addElement(new StaticGuiElement('e',
                        goldDust,
                        ChatColor.AQUA + "Gold Dust",
                        " ",
                        ChatColor.BLUE + "Required Level:",
                        ChatColor.GRAY + "Fortune 4",
                        " ",
                        ChatColor.GRAY + "Can be smelted into high-purity gold"
                )
        );

        itemGui.addElement(new StaticGuiElement('f',
                        new ItemStack(Material.GLOWSTONE_DUST),
                        ChatColor.WHITE + "Glowstone Dust",
                        " ",
                        ChatColor.BLUE + "Required Level:",
                        ChatColor.GRAY + "Fortune 5"
                )
        );

        itemGui.addElement(new StaticGuiElement('g',
                        new ItemStack(Material.GUNPOWDER),
                        ChatColor.WHITE + "Gunpowder",
                        " ",
                        ChatColor.BLUE + "Required Level:",
                        ChatColor.GRAY + "Fortune 6"
                )
        );

        var ancientDust = new ItemStack(Material.REDSTONE);
        ancientDust.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        meta = ancientDust.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        ancientDust.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('h',
                        ancientDust,
                        ChatColor.AQUA + "Ancient Dust",
                        " ",
                        ChatColor.BLUE + "Required Level:",
                        ChatColor.GRAY + "Fortune 6",
                        " ",
                        ChatColor.GRAY + "Can be smelted into high-purity netherite"
                )
        );
        recipeMenus.put(SIFTER_DROPS_KEY, itemGui);

        // Grenade
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Grenade";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " snn aaa ",
                " nfn ara ",
                " nnn aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('n',
                new ItemStack(Material.IRON_NUGGET),
                ChatColor.WHITE + "Iron Nugget",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        itemGui.addElement(new StaticGuiElement('s',
                new ItemStack(Material.REDSTONE),
                ChatColor.WHITE + "Redstone")
        );
        itemGui.addElement(new StaticGuiElement('f',
                new ItemStack(Material.FIRE_CHARGE),
                ChatColor.WHITE + "Fire Charge")
        );
        result = new ItemStack(Material.FIREWORK_STAR, 2);
        result.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('r',
                result,
                ChatColor.AQUA + "Grenade"
                )
        );
        recipeMenus.put(GRENADE_KEY, itemGui);

        // Refined iron plate
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Refined Iron Plate";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     aaa ",
                " i b ara ",
                "     aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        var anvilElement = new StaticGuiElement(
                'T',
                new ItemStack(Material.ANVIL),
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Anvil",
                ChatColor.GRAY + "Item forged in an anvil"
        );
        itemGui.addElement(anvilElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.IRON_INGOT),
                ChatColor.WHITE + "Iron Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        itemGui.addElement(new StaticGuiElement('b',
                new ItemStack(Material.BLAZE_POWDER),
                ChatColor.WHITE + "Blaze Powder")
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE),
                ChatColor.WHITE + "Refined Iron Plate")
        );
        recipeMenus.put(IRON_PLATE_KEY, itemGui);

        // Refined gold plate
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Refined Gold Plate";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     aaa ",
                " i b ara ",
                "     aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(anvilElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.GOLD_INGOT),
                ChatColor.WHITE + "Gold Ingot",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Any purity level will work")
        );
        itemGui.addElement(new StaticGuiElement('b',
                new ItemStack(Material.BLAZE_POWDER),
                ChatColor.WHITE + "Blaze Powder")
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE),
                ChatColor.WHITE + "Refined Gold Plate")
        );
        recipeMenus.put(GOLD_PLATE_KEY, itemGui);


        // Refined iron nugget
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Refined Iron Nugget";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     rrr ",
                "  p  rrr ",
                "     rrr ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        var cauldronElement = new StaticGuiElement(
                'T',
                new ItemStack(Material.CAULDRON),
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Cauldron",
                ChatColor.GRAY + "Item created by right-clicking a cauldron containing water"
        );
        itemGui.addElement(cauldronElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('p',
                        new ItemStack(Material.HEAVY_WEIGHTED_PRESSURE_PLATE),
                        click -> {
                            var gui = recipeMenus.get(IRON_PLATE_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.WHITE + "Refined Iron Plate",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
                )
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.IRON_NUGGET),
                ChatColor.WHITE + "Iron Nugget",
                ChatColor.BLUE + "Refined",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Purity greater than or equal to the original plate's purity.")
        );
        recipeMenus.put(REFINED_IRON_NUGGET_KEY, itemGui);

        // Refined gold nugget
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Refined Gold Nugget";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     rrr ",
                "  p  rrr ",
                "     rrr ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(cauldronElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('p',
                        new ItemStack(Material.LIGHT_WEIGHTED_PRESSURE_PLATE),
                        click -> {
                            var gui = recipeMenus.get(GOLD_PLATE_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.WHITE + "Refined Gold Plate",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
                )
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.GOLD_NUGGET),
                ChatColor.WHITE + "Gold Nugget",
                ChatColor.BLUE + "Refined",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Purity greater than or equal to the original plate's purity.")
        );
        recipeMenus.put(REFINED_GOLD_NUGGET_KEY, itemGui);


        // Refined iron ingot
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Refined Iron Ingot";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " nnn aaa ",
                " nnn ara ",
                " nnn aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('n',
                        new ItemStack(Material.IRON_NUGGET),
                        click -> {
                            var gui = recipeMenus.get(REFINED_IRON_NUGGET_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.WHITE + "Iron Nugget",
                        ChatColor.BLUE + "Refined",
                        ChatColor.GRAY + "Any purity level will work",
                        GUI_METAL_LORE,
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
                )
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.IRON_INGOT),
                ChatColor.WHITE + "Iron Ingot",
                ChatColor.BLUE + "Refined",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Purity equal to the average purity of nuggets.")
        );
        recipeMenus.put(REFINED_IRON_INGOT_KEY, itemGui);

        // Refined gold ingot
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Refined Gold Ingot";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " nnn aaa ",
                " nnn ara ",
                " nnn aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('n',
                        new ItemStack(Material.GOLD_NUGGET),
                        click -> {
                            var gui = recipeMenus.get(REFINED_GOLD_NUGGET_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.WHITE + "Gold Nugget",
                        ChatColor.BLUE + "Refined",
                        ChatColor.GRAY + "Any purity level will work",
                        GUI_METAL_LORE,
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
                )
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.GOLD_INGOT),
                ChatColor.WHITE + "Gold Ingot",
                ChatColor.BLUE + "Refined",
                GUI_METAL_LORE,
                ChatColor.GRAY + "Purity equal to the average purity of nuggets.")
        );
        recipeMenus.put(REFINED_GOLD_INGOT_KEY, itemGui);

        // iron magnet
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Iron Magnet";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     aaa ",
                " b i ara ",
                "     aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(lodestoneElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                        new ItemStack(Material.IRON_INGOT),
                        click -> true,
                        ChatColor.WHITE + "Iron Ingot"
                )
        );
        itemGui.addElement(new StaticGuiElement('b',
                        new ItemStack(Material.EXPERIENCE_BOTTLE, 15),
                        click -> true,
                        ChatColor.YELLOW + "Bottle of Life",
                        ChatColor.GRAY + "- Must be held in the off-hand",
                        ChatColor.GRAY + "- Must have at least 15",
                        ChatColor.GRAY + "- Obtained by logging on and/or voting each day"
                )
        );
        var enchantedItem = new ItemStack(Material.IRON_INGOT);
        meta = enchantedItem.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        enchantedItem.setItemMeta(meta);
        enchantedItem.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        itemGui.addElement(new StaticGuiElement('r',
                enchantedItem,
                ChatColor.AQUA + "Iron Magnet",
                GUI_METAL_LORE)
        );
        recipeMenus.put(IRON_MAGNET_KEY, itemGui);


        // netherite magnet
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Netherite Magnet";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     aaa ",
                " b i ara ",
                "     aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(lodestoneElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('i',
                        new ItemStack(Material.NETHERITE_INGOT),
                        click -> true,
                        ChatColor.WHITE + "Netherite Ingot"
                )
        );
        itemGui.addElement(new StaticGuiElement('b',
                        new ItemStack(Material.EXPERIENCE_BOTTLE, 28),
                        click -> true,
                        ChatColor.YELLOW + "Bottle of Life",
                        ChatColor.GRAY + "- Must be held in the off-hand",
                        ChatColor.GRAY + "- Must have at least 28",
                        ChatColor.GRAY + "- Obtained by logging on and/or voting each day"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.NETHERITE_INGOT);
        itemGui.addElement(new StaticGuiElement('r',
                enchantedItem,
                ChatColor.AQUA + "Netherite Magnet",
                GUI_METAL_LORE)
        );
        recipeMenus.put(NETHERITE_MAGNET_KEY, itemGui);

        // coarse sandpaper
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Coarse Sandpaper";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " qqq aaa ",
                " qsq ara ",
                " ppp aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);
        itemGui.addElement(new StaticGuiElement('q',
                        new ItemStack(Material.QUARTZ),
                        ChatColor.WHITE + "Netherite Quartz"
                )
        );
        itemGui.addElement(new StaticGuiElement('s',
                        new ItemStack(Material.SLIME_BALL),
                        ChatColor.WHITE + "Slimeball"
                )
        );
        itemGui.addElement(new StaticGuiElement('p',
                        new ItemStack(Material.PAPER),
                        ChatColor.WHITE + "Paper"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.PAPER);
        enchantedItem.setAmount(3);
        itemGui.addElement(new StaticGuiElement('r',
                        enchantedItem,
                        ChatColor.AQUA + "Coarse Sandpaper"
                )
        );
        recipeMenus.put(COARSE_SANDPAPER_KEY, itemGui);

        // fine sandpaper
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Fine Sandpaper";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " ada aaa ",
                " dsd ara ",
                " ppp aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);

        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.SUGAR);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('d',
                        enchantedItem,
                        click -> {
                            var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.AQUA + "Iron Dust",
                        " ",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View sifter drops"
                )
        );
        itemGui.addElement(new StaticGuiElement('s',
                        new ItemStack(Material.SLIME_BALL),
                        ChatColor.WHITE + "Slimeball"
                )
        );
        itemGui.addElement(new StaticGuiElement('p',
                        new ItemStack(Material.PAPER),
                        ChatColor.WHITE + "Paper"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.PAPER);
        enchantedItem.setAmount(3);
        itemGui.addElement(new StaticGuiElement('r',
                        enchantedItem,
                        ChatColor.AQUA + "Fine Sandpaper"
                )
        );
        recipeMenus.put(FINE_SANDPAPER_KEY, itemGui);

        // whetstone
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Whetstone";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " ddd aaa ",
                " iii ara ",
                " ooo aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);

        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.IRON_INGOT);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('i',
                        enchantedItem,
                        click -> {
                            var gui = recipeMenus.get(UNBREAKING_IRON_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.AQUA + "Iron Ingot",
                        ChatColor.GRAY + "Must have Unbreaking II or greater",
                        GUI_METAL_LORE,
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View unbreaking ingot recipe"
                )
        );
        itemGui.addElement(new StaticGuiElement('d',
                        new ItemStack(Material.DIAMOND),
                        ChatColor.WHITE + "Diamond"
                )
        );
        itemGui.addElement(new StaticGuiElement('o',
                        new ItemStack(Material.OBSIDIAN),
                        ChatColor.WHITE + "Obsidian"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.BRICK);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('r',
                        enchantedItem,
                        ChatColor.AQUA + "Whetstone",
                        ChatColor.GRAY + "Can be used at least 200 times. Will have a greater",
                        ChatColor.GRAY + "durability if ingots have higher unbreaking levels."
                )
        );
        recipeMenus.put(WHETSTONE_KEY, itemGui);

        // waterstone
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Waterstone";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " ngn aaa ",
                " gwg ara ",
                " ngn aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);

        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.BRICK);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('w',
                        enchantedItem,
                        click -> {
                            var gui = recipeMenus.get(WHETSTONE_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.AQUA + "Whetstone",
                        " ",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View whetstone recipe"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.GLOWSTONE_DUST);
        itemGui.addElement(new StaticGuiElement('g',
                        enchantedItem,
                        click -> {
                            var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.AQUA + "Gold Dust",
                        " ",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View sifter drops"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.REDSTONE);
        itemGui.addElement(new StaticGuiElement('n',
                        enchantedItem,
                        click -> {
                            var gui = recipeMenus.get(SIFTER_DROPS_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.AQUA + "Ancient Dust",
                        " ",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View sifter drops"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.NETHER_BRICK);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('r',
                        enchantedItem,
                        ChatColor.AQUA + "Waterstone",
                        ChatColor.GRAY + "Durability depends on the durability of the whetstone."
                )
        );
        recipeMenus.put(WATERSTONE_KEY, itemGui);


        // coarse sharpening blade
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Coarse Sharpening Blades";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " sas ara ",
                " ooo aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);

        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.IRON_SWORD);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('s',
                        enchantedItem,
                        click -> {
                            click.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "Enchanting recipe menus currently under maintenence.");
                            click.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "Visit #recipes in /discord to view enchantment recipes.");
                            return true;
                        },//todo view sharpness 6 recipe
                        ChatColor.AQUA + "Iron Sword",
                        ChatColor.GRAY + "- Must have Unbreaking V or greater",
                        ChatColor.GRAY + "- Must have Sharpness VI or greater",
                        " ",
                        ChatColor.BLUE + "Left click: " + ChatColor.GRAY + "View sharpness VI recipe",
                        ChatColor.BLUE + "Right click: " + ChatColor.GRAY + "View unbreaking recipe"
                )
        );
        itemGui.addElement(new StaticGuiElement('o',
                        new ItemStack(Material.OBSIDIAN),
                        ChatColor.WHITE + "Obsidian"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.SMOOTH_QUARTZ_STAIRS);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('r',
                        enchantedItem,
                        ChatColor.AQUA + "Coarse Sharpening Blades",
                        ChatColor.GRAY + "Durability depends on the durability",
                        ChatColor.GRAY + "and unbreaking level of the swords."
                )
        );
        recipeMenus.put(COARSE_BLADES_KEY, itemGui);

        // fine sharpening blade
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Fine Sharpening Blades";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aaa aaa ",
                " sas ara ",
                " ooo aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);

        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.NETHERITE_SWORD);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('s',
                        enchantedItem,
                        click -> {
                            click.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "Enchanting recipe menus currently under maintenence.");
                            click.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "Visit #recipes in /discord to view enchantment recipes.");
                            return true;
                        },//todo view sharpness 7 recipe
                        ChatColor.AQUA + "Netherite Sword",
                        ChatColor.GRAY + "- Must have Unbreaking V or greater",
                        ChatColor.GRAY + "- Must have Sharpness VII or greater",
                        " ",
                        ChatColor.BLUE + "Left click: " + ChatColor.GRAY + "View sharpness VII recipe",
                        ChatColor.BLUE + "Right click: " + ChatColor.GRAY + "View unbreaking recipe"
                )
        );
        itemGui.addElement(new StaticGuiElement('o',
                        new ItemStack(Material.OBSIDIAN),
                        ChatColor.WHITE + "Obsidian"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.POLISHED_BLACKSTONE_STAIRS);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('r',
                        enchantedItem,
                        ChatColor.AQUA + "Fine Sharpening Blades",
                        ChatColor.GRAY + "Durability depends on the durability",
                        ChatColor.GRAY + "and unbreaking level of the swords."
                )
        );
        recipeMenus.put(FINE_BLADES_KEY, itemGui);

        // iron honing rod
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Iron Honing Rod";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aia aaa ",
                " cia ara ",
                " aia aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);

        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.DIAMOND_HOE);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('c',
                        enchantedItem,
                        click -> {
                            click.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "Enchanting recipe menus currently under maintenence.");
                            click.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "Visit #recipes in /discord to view enchantment recipes.");
                            return true;
                        },//todo view fortune 8 recipe
                        ChatColor.AQUA + "Diamond Chisel",
                        ChatColor.GRAY + "- Will lose 1000 durability when used (reduced by unbreaking)",
                        ChatColor.GRAY + "- Must have fortune VIII or greater",
                        " ",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View fortune VIII recipe"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.IRON_INGOT);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('i',
                        enchantedItem,
                        click -> {
                            var gui = recipeMenus.get(UNBREAKING_IRON_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.AQUA + "Iron Ingot",
                        ChatColor.GRAY + "Must have Unbreaking X",
                        GUI_METAL_LORE,
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View unbreaking ingot recipe"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.END_ROD);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('r',
                        enchantedItem,
                        ChatColor.AQUA + "Iron Honing Rod",
                        ChatColor.GRAY + "Can be used 1000 times"
                )
        );
        recipeMenus.put(IRON_ROD_KEY, itemGui);

        // netherite honing rod
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Netherite Honing Rod";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                " aia aaa ",
                " cia ara ",
                " aia aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);

        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.NETHERITE_HOE);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('c',
                        enchantedItem,
                        click -> {
                            click.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "Enchanting recipe menus currently under maintenence.");
                            click.getEvent().getWhoClicked().sendMessage(ChatColor.RED + "Visit #recipes in /discord to view enchantment recipes.");
                            return true;
                        },//todo view fortune 9 recipe
                        ChatColor.AQUA + "Netherite Chisel",
                        ChatColor.GRAY + "- Will lose 2500 durability when used (reduced by unbreaking)",
                        ChatColor.GRAY + "- Must have fortune IX or greater",
                        " ",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View fortune IX recipe"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.NETHERITE_INGOT);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('i',
                        enchantedItem,
                        click -> {
                            var gui = recipeMenus.get(UNBREAKING_NETHERITE_KEY);
                            gui.show(click.getEvent().getWhoClicked());
                            return true;
                        },
                        ChatColor.AQUA + "Netherite Ingot",
                        ChatColor.GRAY + "Must have Unbreaking X",
                        GUI_METAL_LORE,
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View unbreaking ingot recipe"
                )
        );
        enchantedItem = enchantedItem.clone();
        enchantedItem.setType(Material.END_ROD);
        enchantedItem.setAmount(1);
        itemGui.addElement(new StaticGuiElement('r',
                        enchantedItem,
                        ChatColor.AQUA + "Netherite Honing Rod",
                        ChatColor.GRAY + "Can be used 2500 times"
                )
        );
        recipeMenus.put(NETHERITE_ROD_KEY, itemGui);

        // Rename Item
        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Item Rename";
        guiSetup = new String[]{
                " B  T  M ",
                "         ",
                "     aaa ",
                " it  ara ",
                "     aaa ",
                "         ",
        };
        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        itemGui.addElement(airElement);
        itemGui.addElement(backElement);
        itemGui.addElement(craftingTableElement);
        itemGui.addElement(menuElement);
        itemGui.addElement(stickElement);


        itemGui.addElement(new StaticGuiElement('i',
                        new ItemStack(Material.IRON_SWORD),
                        ChatColor.WHITE + "Tool / Weapon (can be any tool / weapon)",
                        " "
                )
        );

        itemGui.addElement(new StaticGuiElement('t',
                        new ItemStack(Material.NAME_TAG),
                        ChatColor.WHITE + "Name Tag",
                        ChatColor.GRAY + "Use anvil to write a name on the name tag"
                )
        );
        itemGui.addElement(new StaticGuiElement('r',
                        new ItemStack(Material.IRON_SWORD),
                        ChatColor.WHITE + "Custom Name",
                        ChatColor.GRAY + "Item will rename to the name on the name tag"
                )
        );
        recipeMenus.put(RENAME_KEY, itemGui);
    }

    private void createItemMenu() {
        var title = "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Item Recipes";

        // Create setup
        var guiSetup = new String[]{
                " R     M ",
                "iiiiiiiii",
                "iiiiiiiii",
                "iiiiiiiii",
                "iiiiiiiii",
                " P     N ",
        };

        // create item gui
        var itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        itemGui.setCloseAction(close -> false);
        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));

        // create return to recipes
        var backElement = new GuiBackElement('R',
                new ItemStack(Material.ARROW),
                ChatColor.YELLOW + "Go Back",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to previous page");
        itemGui.addElement(backElement);

        // create return to menu
        var menuElement = new StaticGuiElement('M',
                new ItemStack(Material.PAINTING),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = recipeMenus.get(RECIPES_KEY);
                    nGui.show(clicker);
                    return true;
                },
                ChatColor.YELLOW + "Recipes",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to recipe menu");
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
                    var nGui = recipeMenus.get(CHAIN_HELMET_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Chainmail Helmet",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(CHAIN_CHESTPLATE_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Chainmail Chestplate",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(CHAIN_LEGGINGS_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Chainmail Leggings",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(CHAIN_BOOTS_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Chainmail Boots",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(WOODEN_KNIFE_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Wooden Hunting Knife",
                ChatColor.GRAY + "Yields more loot when compared to a sword,",
                ChatColor.GRAY + "but deals much less damage per hit",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(STONE_KNIFE_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Stone Hunting Knife",
                ChatColor.GRAY + "Yields more loot when compared to a sword,",
                ChatColor.GRAY + "but deals much less damage per hit",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"

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
                    var nGui = recipeMenus.get(GOLDEN_KNIFE_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Golden Hunting Knife",
                ChatColor.GRAY + "Yields more loot when compared to a sword,",
                ChatColor.GRAY + "but deals much less damage per hit",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(IRON_KNIFE_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Iron Hunting Knife",
                ChatColor.GRAY + "Yields more loot when compared to a sword,",
                ChatColor.GRAY + "but deals much less damage per hit",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe",
                " "
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
                    var nGui = recipeMenus.get(DIAMOND_KNIFE_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Diamond Hunting Knife",
                ChatColor.GRAY + "Yields more loot when compared to a sword,",
                ChatColor.GRAY + "but deals much less damage per hit",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(NETHERITE_KNIFE_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Netherite Hunting Knife",
                ChatColor.GRAY + "Yields more loot when compared to a sword,",
                ChatColor.GRAY + "but deals much less damage per hit",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(WOODEN_CHISEL_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Wooden Chisel",
                ChatColor.GRAY + "A tool used for slowly and carefully mining through",
                ChatColor.GRAY + "stone, yielding more resources than a pickaxe",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(STONE_CHISEL_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Stone Chisel",
                ChatColor.GRAY + "A tool used for slowly and carefully mining through",
                ChatColor.GRAY + "stone, yielding more resources than a pickaxe",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(GOLDEN_CHISEL_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Golden Chisel",
                ChatColor.GRAY + "A tool used for slowly and carefully mining through",
                ChatColor.GRAY + "stone, yielding more resources than a pickaxe",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(IRON_CHISEL_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Iron Chisel",
                ChatColor.GRAY + "A tool used for slowly and carefully mining through",
                ChatColor.GRAY + "stone, yielding more resources than a pickaxe",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(DIAMOND_CHISEL_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Diamond Chisel",
                ChatColor.GRAY + "A tool used for slowly and carefully mining through",
                ChatColor.GRAY + "stone, yielding more resources than a pickaxe",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(NETHERITE_CHISEL_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Netherite Chisel",
                ChatColor.GRAY + "A tool used for slowly and carefully mining through",
                ChatColor.GRAY + "stone, yielding more resources than a pickaxe",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(WOODEN_SAW_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Wooden Saw",
                ChatColor.GRAY + "A tool used for cutting through a block with",
                ChatColor.GRAY + "minimal damage, providing the silk-touch effect",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(STONE_SAW_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Stone Saw",
                ChatColor.GRAY + "A tool used for cutting through a block with",
                ChatColor.GRAY + "minimal damage, providing the silk-touch effect",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(GOLDEN_SAW_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Golden Saw",
                ChatColor.GRAY + "A tool used for cutting through a block with",
                ChatColor.GRAY + "minimal damage, providing the silk-touch effect",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(IRON_SAW_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Iron Saw",
                ChatColor.GRAY + "A tool used for cutting through a block with",
                ChatColor.GRAY + "minimal damage, providing the silk-touch effect",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(DIAMOND_SAW_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Diamond Saw",
                ChatColor.GRAY + "A tool used for cutting through a block with",
                ChatColor.GRAY + "minimal damage, providing the silk-touch effect",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var nGui = recipeMenus.get(NETHERITE_SAW_KEY);
                    nGui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Netherite Saw",
                ChatColor.GRAY + "A tool used for cutting through a block with",
                ChatColor.GRAY + "minimal damage, providing the silk-touch effect",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.CHAINMAIL_HELMET);
        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 1);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    var gui = recipeMenus.get(WOODEN_SIFTER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Wooden Sifter",
                ChatColor.GRAY + "A tool used for carefully digging through gravel",
                ChatColor.GRAY + "and soul sand which yields rarer resources",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(STRING_SIFTER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "String Sifter",
                ChatColor.GRAY + "A tool used for carefully digging through gravel",
                ChatColor.GRAY + "and soul sand which yields rarer resources",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(CHAIN_SIFTER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Chain Sifter",
                ChatColor.GRAY + "A tool used for carefully digging through gravel",
                ChatColor.GRAY + "and soul sand which yields rarer resources",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(REDSTONE_SIFTER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Redstone Sifter",
                ChatColor.GRAY + "A tool used for carefully digging through gravel",
                ChatColor.GRAY + "and soul sand which yields rarer resources",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(LODESTONE_SIFTER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Lodestone Sifter",
                ChatColor.GRAY + "A tool used for carefully digging through gravel",
                ChatColor.GRAY + "and soul sand which yields rarer resources",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(MAGNETIC_SIFTER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Magnetic Sifter",
                ChatColor.GRAY + "A tool used for carefully digging through gravel",
                ChatColor.GRAY + "and soul sand which yields rarer resources",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(NETHERITE_SIFTER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Netherite Sifter",
                ChatColor.GRAY + "A tool used for carefully digging through gravel",
                ChatColor.GRAY + "and soul sand which yields rarer resources",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(GRENADE_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Grenade",
                ChatColor.GRAY + "An explosive ranged weapon",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(IRON_PLATE_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Refined Iron Plate",
                ChatColor.GRAY + "Used to create refined iron nuggets",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(GOLD_PLATE_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Refined Gold Plate",
                ChatColor.GRAY + "Used to create refined gold nuggets",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(REFINED_IRON_NUGGET_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Refined Iron Nugget",
                ChatColor.GRAY + "Iron nugget that had its purity increased",
                GUI_METAL_LORE,
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(REFINED_GOLD_NUGGET_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Refined Gold Nugget",
                ChatColor.GRAY + "Gold nugget that had its purity increased",
                GUI_METAL_LORE,
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(REFINED_IRON_INGOT_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Refined Iron Ingot",
                ChatColor.GRAY + "Iron ingot that had its purity increased",
                GUI_METAL_LORE,
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(REFINED_GOLD_INGOT_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Refined Gold Ingot",
                ChatColor.GRAY + "Gold ingot that had its purity increased",
                GUI_METAL_LORE,
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(IRON_MAGNET_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Iron Magnet",
                ChatColor.GRAY + "Magnet made of iron",
                GUI_METAL_LORE,
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(NETHERITE_MAGNET_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Netherite Magnet",
                ChatColor.GRAY + "Magnet made of netherite",
                GUI_METAL_LORE,
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(COARSE_SANDPAPER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Coarse Sandpaper",
                ChatColor.GRAY + "Used to sharpen a blade to level 3",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(FINE_SANDPAPER_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Fine Sandpaper",
                ChatColor.GRAY + "Used to sharpen a blade to level 4",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(WHETSTONE_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Whetstone",
                ChatColor.GRAY + "Used to sharpen a blade to level 5",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(WATERSTONE_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Water Stone",
                ChatColor.GRAY + "Used to sharpen a blade to level 6",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(COARSE_BLADES_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Coarse Sharpening Blades",
                ChatColor.GRAY + "Used to sharpen a blade to level 7",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(FINE_BLADES_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Fine Sharpening Blades",
                ChatColor.GRAY + "Used to sharpen a blade to level 8",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(IRON_ROD_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Iron Honing Rod",
                ChatColor.GRAY + "Used to sharpen a blade to level 9",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(NETHERITE_ROD_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.AQUA + "Netherite Honing Rod",
                ChatColor.GRAY + "Used to sharpen a blade to level 10",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(IRON_SLURRY_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Iron Slurry",
                ChatColor.GRAY + "Used to create slurried iron ingots",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(GOLD_SLURRY_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Gold Slurry",
                ChatColor.GRAY + "Used to create slurried gold ingots",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(NETHERITE_SLURRY_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Netherite Slurry",
                ChatColor.GRAY + "Used to create slurried netherite ingots",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(SLURRIED_IRON_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Slurried Iron Ingot",
                ChatColor.GRAY + "Used to create unbreaking iron ingots",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(SLURRIED_GOLD_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Slurried Gold Ingot",
                ChatColor.GRAY + "Used to create unbreaking gold ingots",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(SLURRIED_NETHERITE_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Slurried Netherite Ingot",
                ChatColor.GRAY + "Used to create unbreaking netherite ingots",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(UNBREAKING_IRON_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Unbreaking Iron Ingot",
                ChatColor.GRAY + "Used to craft iron items with",
                ChatColor.GRAY + "the unbreaking enchantment",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
        );
        itemGroup.addElement(element);

        item = new ItemStack(Material.GOLD_INGOT);
        meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        item.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        element = new StaticGuiElement(
                'i',
                item,
                click -> {
                    var gui = recipeMenus.get(UNBREAKING_GOLD_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Unbreaking Gold Ingot",
                ChatColor.GRAY + "Used to craft gold items with",
                ChatColor.GRAY + "the unbreaking enchantment",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(UNBREAKING_NETHERITE_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Unbreaking Netherite Ingot",
                ChatColor.GRAY + "Used to craft netherite items with",
                ChatColor.GRAY + "the unbreaking enchantment",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
                    var gui = recipeMenus.get(RENAME_KEY);
                    gui.show(click.getEvent().getWhoClicked());
                    return true;
                },
                ChatColor.WHITE + "Item Rename",
                ChatColor.GRAY + "Rename an item",
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
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
