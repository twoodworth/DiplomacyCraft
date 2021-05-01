package me.tedwoodworth.diplomacy.Guis;

import de.themoep.inventorygui.*;
import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RecipeGuis {
    private static RecipeGuis instance = null;

    private final Map<String, InventoryGui> recipeMenus = new HashMap<>();

    public final String RECIPES_KEY = "recipes";
    public final String ITEMS_KEY = "items";
    public final String CHAIN_HELMET_KEY = "chain_helmet";
    public final String CHAIN_CHESTPLATE_KEY = "chain_chestplate";
    public final String CHAIN_LEGGINGS_KEY = "chain_leggings";
    public final String CHAIN_BOOTS_KEY = "chain_boots";
    public final String GRENADE_KEY = "grenade";


    public static RecipeGuis getInstance() {
        if (instance == null) {
            instance = new RecipeGuis();
        }
        return instance;
    }

    public RecipeGuis() {
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
                ChatColor.WHITE + "Iron Nugget")
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
                ChatColor.WHITE + "Iron Nugget")
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
                ChatColor.WHITE + "Iron Nugget")
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
                ChatColor.WHITE + "Iron Nugget")
        );
        itemGui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.CHAINMAIL_BOOTS),
                ChatColor.WHITE + "Chainmail Boots")
        );
        recipeMenus.put(CHAIN_BOOTS_KEY, itemGui);

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
                ChatColor.WHITE + "Iron Nugget")
        );
        itemGui.addElement(new StaticGuiElement('s',
                new ItemStack(Material.REDSTONE),
                ChatColor.WHITE + "Redstone")
        );
        itemGui.addElement(new StaticGuiElement('f',
                new ItemStack(Material.FIRE_CHARGE),
                ChatColor.WHITE + "Fire Charge")
        );
        var result = new ItemStack(Material.FIREWORK_STAR, 2);
        result.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
        var meta = result.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        result.setItemMeta(meta);
        itemGui.addElement(new StaticGuiElement('r',
                        result,
                        ChatColor.AQUA + "Grenade"
                )
        );
        recipeMenus.put(GRENADE_KEY, itemGui);
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

        itemGui.addElement(itemGroup);
        recipeMenus.put(ITEMS_KEY, itemGui);
    }

    public InventoryGui getRecipeMenu(String key) {
        return recipeMenus.get(key);
    }
}
