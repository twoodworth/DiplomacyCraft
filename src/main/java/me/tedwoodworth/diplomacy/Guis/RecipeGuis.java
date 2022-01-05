//package me.tedwoodworth.diplomacy.Guis;
//
//import de.themoep.inventorygui.*;
//import me.tedwoodworth.diplomacy.Diplomacy;
//import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
//import me.tedwoodworth.diplomacy.Items.CustomItems;
//import org.bukkit.ChatColor;
//import org.bukkit.Material;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.event.inventory.ClickType;
//import org.bukkit.inventory.ItemFlag;
//import org.bukkit.inventory.ItemStack;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class RecipeGuis {
//    private static RecipeGuis instance = null;
//
//    private final Map<String, InventoryGui> recipeMenus = new HashMap<>();
//
//    public final String RECIPES_KEY = "recipes";
//    public final String CHAIN_HELMET_KEY = "chain_helmet";
//    public final String CHAIN_CHESTPLATE_KEY = "chain_chestplate";
//    public final String CHAIN_LEGGINGS_KEY = "chain_leggings";
//    public final String CHAIN_BOOTS_KEY = "chain_boots";
//    public final String NAME_TAG_KEY = "name_tag";
//    public final String SADDLE_KEY = "saddle";
//    public final String IRON_HORSE_ARMOR_KEY = "iron_horse_armor";
//    public final String GOLD_HORSE_ARMOR_KEY = "gold_horse_armor";
//    public final String DIAMOND_HORSE_ARMOR_KEY = "diamond_horse_armor";
//    public final String GRENADE_KEY = "grenade";
//    public final String MAGICAL_DUST_FURNACE_KEY = "magical_dust_furnace";
//    public final String MAGICAL_DUST_CRAFTING_KEY = "magical_dust_crafting";
//    public final String APPLE_OF_LIFE_KEY = "apple_of_life";
//    public final String EXPERIENCE_BOTTLE_KEY = "experience_bottle";
//    public final String GUARD_CRYSTAL_KEY = "guard_crystal";
//
//
//    public static RecipeGuis getInstance() {
//        if (instance == null) {
//            instance = new RecipeGuis();
//        }
//        return instance;
//    }
//
//    public RecipeGuis() {
//    }
//
//    public void loadRecipeMenus() {
//
//        // item menu
//        createItemMenu();
//
//        // item recipes
//        createItemRecipes();
//    }
//
//    private void createItemRecipes() {
//        var backElement = new GuiBackElement('B',
//                new ItemStack(Material.ARROW),
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to previous page"
//        );
//        var craftingTableElement = new StaticGuiElement('T',
//                new ItemStack(Material.CRAFTING_TABLE),
//                click -> true,
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Crafting Table",
//                ChatColor.GRAY + "Item created in a crafting table"
//        );
//        var furnaceElement = new StaticGuiElement('T',
//                new ItemStack(Material.FURNACE),
//                click -> true,
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Furnace",
//                ChatColor.GRAY + "Item created in a furnace"
//        );
//        var smithingTableElement = new StaticGuiElement('T',
//                new ItemStack(Material.SMITHING_TABLE),
//                click -> true,
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Smithing Table",
//                ChatColor.GRAY + "Item created in a smithing table"
//        );
//        var lodestoneElement = new StaticGuiElement('T',
//                new ItemStack(Material.LODESTONE),
//                click -> true,
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Lodestone",
//                ChatColor.GRAY + "Item created by right-clicking a lodestone"
//        );
//        var airElement = new StaticGuiElement('a',
//                new ItemStack(Material.AIR));
//        var stickElement = new StaticGuiElement('s',
//                new ItemStack(Material.STICK));
//        var menuElement = new StaticGuiElement('M',
//                new ItemStack(Material.KNOWLEDGE_BOOK),
//                click -> {
//                    var clicker = click.getEvent().getWhoClicked();
//                    var menu = recipeMenus.get(RECIPES_KEY);
//                    menu.show(clicker);
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Recipe List",
//                ChatColor.BLUE + "Click " + ChatColor.GRAY + "Return to recipe list"
//        );
//
//        // chain helmet
//        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Chainmail Helmet";
//        var guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " iii aaa ",
//                " iai ara ",
//                " aaa aaa ",
//                "         ",
//        };
//        var itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('i',
//                new ItemStack(Material.IRON_NUGGET),
//                click -> true,
//                ChatColor.WHITE + "Iron Nugget")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                new ItemStack(Material.CHAINMAIL_HELMET),
//                ChatColor.WHITE + "Chainmail Helmet")
//        );
//        recipeMenus.put(CHAIN_HELMET_KEY, itemGui);
//
//        // chain chestplate
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Chainmail Chestplate";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " iai aaa ",
//                " iii ara ",
//                " iii aaa ",
//                "         ",
//        };
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('i',
//                new ItemStack(Material.IRON_NUGGET),
//                click -> true,
//                ChatColor.WHITE + "Iron Nugget")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                new ItemStack(Material.CHAINMAIL_CHESTPLATE),
//                ChatColor.WHITE + "Chainmail Chestplate")
//        );
//        recipeMenus.put(CHAIN_CHESTPLATE_KEY, itemGui);
//
//        // chain leggings
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Chainmail Chestplate";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " iii aaa ",
//                " iai ara ",
//                " iai aaa ",
//                "         ",
//        };
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('i',
//                new ItemStack(Material.IRON_NUGGET),
//                click -> true,
//                ChatColor.WHITE + "Iron Nugget")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                new ItemStack(Material.CHAINMAIL_LEGGINGS),
//                ChatColor.WHITE + "Chainmail Leggings")
//        );
//        recipeMenus.put(CHAIN_LEGGINGS_KEY, itemGui);
//
//        // chain boots
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Chainmail Boots";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " aaa aaa ",
//                " iai ara ",
//                " iai aaa ",
//                "         ",
//        };
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('i',
//                new ItemStack(Material.IRON_NUGGET),
//                click -> true,
//                ChatColor.WHITE + "Iron Nugget")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                new ItemStack(Material.CHAINMAIL_BOOTS),
//                ChatColor.WHITE + "Chainmail Boots")
//        );
//        recipeMenus.put(CHAIN_BOOTS_KEY, itemGui);
//
//        // name tag
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Name Tag";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " aaa aaa ",
//                " lli ara ",
//                " aaa aaa ",
//                "         ",
//        };
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('i',
//                new ItemStack(Material.IRON_INGOT),
//                click -> true,
//                ChatColor.WHITE + "Iron Ingot")
//        );
//        itemGui.addElement(new StaticGuiElement('l',
//                new ItemStack(Material.LEATHER),
//                click -> true,
//                ChatColor.WHITE + "Leather")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                new ItemStack(Material.NAME_TAG),
//                ChatColor.WHITE + "Name Tag")
//        );
//        recipeMenus.put(NAME_TAG_KEY, itemGui);
//
//        // saddle
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Name Tag";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " lll aaa ",
//                " lal ara ",
//                " iai aaa ",
//                "         ",
//        };
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('i',
//                new ItemStack(Material.IRON_INGOT),
//                click -> true,
//                ChatColor.WHITE + "Iron Ingot")
//        );
//        itemGui.addElement(new StaticGuiElement('l',
//                new ItemStack(Material.LEATHER),
//                click -> true,
//                ChatColor.WHITE + "Leather")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                new ItemStack(Material.SADDLE),
//                ChatColor.WHITE + "Saddle")
//        );
//        recipeMenus.put(SADDLE_KEY, itemGui);
//
//        // iron horse armor
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Iron Horse Armor";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " aai aaa ",
//                " isi ara ",
//                " iai aaa ",
//                "         ",
//        };
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('i',
//                new ItemStack(Material.IRON_INGOT),
//                click -> true,
//                ChatColor.WHITE + "Iron Ingot")
//        );
//        itemGui.addElement(new StaticGuiElement('s',
//                new ItemStack(Material.SADDLE),
//                click -> true,
//                ChatColor.WHITE + "Saddle")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                new ItemStack(Material.IRON_HORSE_ARMOR),
//                ChatColor.WHITE + "Iron Horse Armor")
//        );
//        recipeMenus.put(IRON_HORSE_ARMOR_KEY, itemGui);
//
//        // golden horse armor
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Golden Horse Armor";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " aai aaa ",
//                " isi ara ",
//                " iai aaa ",
//                "         ",
//        };
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('i',
//                new ItemStack(Material.GOLD_INGOT),
//                click -> true,
//                ChatColor.WHITE + "Gold Ingot")
//        );
//        itemGui.addElement(new StaticGuiElement('s',
//                new ItemStack(Material.SADDLE),
//                click -> true,
//                ChatColor.WHITE + "Saddle")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                new ItemStack(Material.GOLDEN_HORSE_ARMOR),
//                ChatColor.WHITE + "Golden Horse Armor")
//        );
//        recipeMenus.put(GOLD_HORSE_ARMOR_KEY, itemGui);
//
//        // diamond horse armor
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Diamond Horse Armor";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " aai aaa ",
//                " isi ara ",
//                " iai aaa ",
//                "         ",
//        };
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('i',
//                new ItemStack(Material.DIAMOND),
//                click -> true,
//                ChatColor.WHITE + "Diamond")
//        );
//        itemGui.addElement(new StaticGuiElement('s',
//                new ItemStack(Material.SADDLE),
//                click -> true,
//                ChatColor.WHITE + "Saddle")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                new ItemStack(Material.DIAMOND_HORSE_ARMOR),
//                ChatColor.WHITE + "Diamond Horse Armor")
//        );
//        recipeMenus.put(DIAMOND_HORSE_ARMOR_KEY, itemGui);
//
//        // Grenade
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Grenade";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " snn aaa ",
//                " nfn ara ",
//                " nnn aaa ",
//                "         ",
//        };
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(stickElement);
//        itemGui.addElement(new StaticGuiElement('n',
//                new ItemStack(Material.IRON_NUGGET),
//                ChatColor.WHITE + "Iron Nugget")
//        );
//        itemGui.addElement(new StaticGuiElement('s',
//                new ItemStack(Material.REDSTONE),
//                ChatColor.WHITE + "Redstone")
//        );
//        itemGui.addElement(new StaticGuiElement('f',
//                new ItemStack(Material.FIRE_CHARGE),
//                ChatColor.WHITE + "Fire Charge")
//        );
//        var result = new ItemStack(Material.FIREWORK_STAR, 2);
//        result.addUnsafeEnchantment(Enchantment.OXYGEN, 1);
//        var meta = result.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
//        result.setItemMeta(meta);
//        itemGui.addElement(new StaticGuiElement('r',
//                        result,
//                        ChatColor.AQUA + "Grenade"
//                )
//        );
//        recipeMenus.put(GRENADE_KEY, itemGui);
//
//        // Apple of life
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Apple of Life";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " ddd aaa ",
//                " dfd ara ",
//                " ddd aaa ",
//                "         ",
//        };
//        var magicalDust1 = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, 1);
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('f',
//                new ItemStack(Material.APPLE),
//                ChatColor.WHITE + "Apple")
//        );
//        itemGui.addElement(new StaticGuiElement('d',
//                magicalDust1,
//                click -> {
//                    var e = click.getEvent();
//                    var type = e.getClick();
//                    var clicker = e.getWhoClicked();
//                    if (type == ClickType.LEFT) {
//                        var nGui = recipeMenus.get(MAGICAL_DUST_FURNACE_KEY);
//                        nGui.show(click.getEvent().getWhoClicked());
//                    } else if (type == ClickType.RIGHT) {
//                        var nGui = recipeMenus.get(MAGICAL_DUST_CRAFTING_KEY);
//                        nGui.show(click.getEvent().getWhoClicked());
//                    }
//                    return true;
//                },
//                ChatColor.GOLD + "Magical Dust",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View smelting recipe",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "View crafting recipe")
//        );
//        var appleOfLife = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.APPLE_OF_LIFE, 1);
//        itemGui.addElement(new StaticGuiElement('r',
//                        appleOfLife,
//                        ChatColor.GOLD + "Apple of Life"
//                )
//        );
//        recipeMenus.put(APPLE_OF_LIFE_KEY, itemGui);
//
//
//        // Magical dust (crafting)
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Magical Dust (Crafting Table)";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " aaa aaa ",
//                " afa ara ",
//                " aaa aaa ",
//                "         ",
//        };
//        var magicalDust2 = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, 8);
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('f',
//                appleOfLife,
//                click -> {
//                    var clicker = click.getEvent().getWhoClicked();
//                    var nGui = recipeMenus.get(APPLE_OF_LIFE_KEY);
//                    nGui.show(clicker);
//                    return true;
//                },
//                ChatColor.GOLD + "Apple of Life",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                magicalDust2,
//                ChatColor.GOLD + "Magical Dust"
//                )
//        );
//        recipeMenus.put(MAGICAL_DUST_CRAFTING_KEY, itemGui);
//
//
//        // Magical dust (furnace)
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Magical Dust (Furnace)";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                "         ",
//                "   f r   ",
//                "         ",
//                "         ",
//        };
//
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(furnaceElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('f',
//                new ItemStack(Material.ENCHANTED_BOOK),
//                ChatColor.YELLOW + "Enchanted Book (Any enchantment)"
//                )
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                        magicalDust1,
//                        ChatColor.GOLD + "Magical Dust"
//                )
//        );
//        recipeMenus.put(MAGICAL_DUST_FURNACE_KEY, itemGui);
//
//
//        // bottle of xp
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Bottle o' Enchanting";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " bdd aaa ",
//                " dda ara ",
//                " aaa aaa ",
//                "         ",
//        };
//
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('b',
//                        new ItemStack(Material.GLASS_BOTTLE),
//                        ChatColor.WHITE + "Glass Bottle"
//                )
//        );
//        itemGui.addElement(new StaticGuiElement('d',
//                magicalDust1,
//                click -> {
//                    var e = click.getEvent();
//                    var type = e.getClick();
//                    var clicker = e.getWhoClicked();
//                    if (type == ClickType.LEFT) {
//                        var nGui = recipeMenus.get(MAGICAL_DUST_FURNACE_KEY);
//                        nGui.show(clicker);
//                    } else if (type == ClickType.RIGHT) {
//                        var nGui = recipeMenus.get(MAGICAL_DUST_CRAFTING_KEY);
//                        nGui.show(clicker);
//                    }
//                    return true;
//                },
//                ChatColor.GOLD + "Magical Dust",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View smelting recipe",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "View crafting recipe")
//        );
//        itemGui.addElement(new StaticGuiElement('r',
//                        new ItemStack(Material.EXPERIENCE_BOTTLE),
//                        ChatColor.YELLOW + "Bottle o' Enchanting"
//                )
//        );
//        recipeMenus.put(EXPERIENCE_BOTTLE_KEY, itemGui);
//
//        // guard crystal
//        title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Guard Crystal";
//        guiSetup = new String[]{
//                " B  T  M ",
//                "         ",
//                " ggg aaa ",
//                " gfg ara ",
//                " gdg aaa ",
//                "         ",
//        };
//
//        itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//        itemGui.addElement(airElement);
//        itemGui.addElement(backElement);
//        itemGui.addElement(craftingTableElement);
//        itemGui.addElement(menuElement);
//        itemGui.addElement(new StaticGuiElement('g',
//                        new ItemStack(Material.GLASS),
//                        ChatColor.WHITE + "Glass"
//                )
//        );
//        itemGui.addElement(new StaticGuiElement('f',
//                appleOfLife,
//                click -> {
//                    var clicker = click.getEvent().getWhoClicked();
//                    var nGui = recipeMenus.get(APPLE_OF_LIFE_KEY);
//                    nGui.show(clicker);
//                    return true;
//                },
//                ChatColor.GOLD + "Apple of Life",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe")
//        );
//        itemGui.addElement(new StaticGuiElement('d',
//                new ItemStack(Material.DIAMOND),
//                ChatColor.WHITE + "Diamond")
//        );
//        var guardCrystal = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GUARD_CRYSTAL, 1);
//        itemGui.addElement(new StaticGuiElement('r',
//                        guardCrystal,
//                        ChatColor.GREEN + "Guard Crystal"
//                )
//        );
//        recipeMenus.put(GUARD_CRYSTAL_KEY, itemGui);
//    }
//
//
//    private void createItemMenu() {
//        var title = "" + ChatColor.DARK_GREEN + ChatColor.BOLD + "Item Recipes";
//
//        // Create setup
//        var guiSetup = new String[]{
//                " R     M ",
//                "iiiiiiiii",
//                "iiiiiiiii",
//                "iiiiiiiii",
//                "iiiiiiiii",
//                " P     N ",
//        };
//
//        // create item gui
//        var itemGui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
//        itemGui.setCloseAction(close -> false);
//        itemGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
//
//        // create return to recipes
//        var backElement = new GuiBackElement('R',
//                new ItemStack(Material.ARROW),
//                ChatColor.YELLOW + "Go Back",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to previous page");
//        itemGui.addElement(backElement);
//
//        // create return to menu
//        var menuElement = new StaticGuiElement('M',
//                new ItemStack(Material.KNOWLEDGE_BOOK),
//                click -> {
//                    var clicker = click.getEvent().getWhoClicked();
//                    var nGui = recipeMenus.get(RECIPES_KEY);
//                    nGui.show(clicker);
//                    return true;
//                },
//                ChatColor.YELLOW + "Recipes",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to recipe menu");
//        itemGui.addElement(menuElement);
//
//        // create next page
//        var pageElement = new GuiPageElement('N',
//                new ItemStack(Material.LIME_STAINED_GLASS_PANE),
//                GuiPageElement.PageAction.NEXT,
//                ChatColor.GREEN + "Next Page");
//        itemGui.addElement(pageElement);
//
//        // create previous page
//        pageElement = new GuiPageElement('P',
//                new ItemStack(Material.RED_STAINED_GLASS_PANE),
//                GuiPageElement.PageAction.PREVIOUS,
//                ChatColor.RED + "Previous Page");
//        itemGui.addElement(pageElement);
//
//
//        // create item group
//        var itemGroup = new GuiElementGroup('i');
//
//        // add chain helmet element
//        var item = new ItemStack(Material.CHAINMAIL_HELMET);
//        var meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        item.setItemMeta(meta);
//        var element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var nGui = recipeMenus.get(CHAIN_HELMET_KEY);
//                    nGui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.WHITE + "Chainmail Helmet",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add chain chestplate element
//        item = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
//        meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        item.setItemMeta(meta);
//        element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var nGui = recipeMenus.get(CHAIN_CHESTPLATE_KEY);
//                    nGui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.WHITE + "Chainmail Chestplate",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add chain leggings element
//        item = new ItemStack(Material.CHAINMAIL_LEGGINGS);
//        meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        item.setItemMeta(meta);
//        element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var nGui = recipeMenus.get(CHAIN_LEGGINGS_KEY);
//                    nGui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.WHITE + "Chainmail Leggings",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add chain boots element
//        item = new ItemStack(Material.CHAINMAIL_BOOTS);
//        meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        item.setItemMeta(meta);
//        element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var nGui = recipeMenus.get(CHAIN_BOOTS_KEY);
//                    nGui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.WHITE + "Chainmail Boots",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add name tag element
//        item = new ItemStack(Material.NAME_TAG);
//        meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        item.setItemMeta(meta);
//        element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var nGui = recipeMenus.get(NAME_TAG_KEY);
//                    nGui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.WHITE + "Name Tag",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add saddle element
//        item = new ItemStack(Material.SADDLE);
//        meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        item.setItemMeta(meta);
//        element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var nGui = recipeMenus.get(SADDLE_KEY);
//                    nGui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.WHITE + "Saddle",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add iron horse armor element
//        item = new ItemStack(Material.IRON_HORSE_ARMOR);
//        meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        item.setItemMeta(meta);
//        element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var nGui = recipeMenus.get(IRON_HORSE_ARMOR_KEY);
//                    nGui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.WHITE + "Iron Horse Armor",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add golden horse armor element
//        item = new ItemStack(Material.GOLDEN_HORSE_ARMOR);
//        meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        item.setItemMeta(meta);
//        element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var nGui = recipeMenus.get(GOLD_HORSE_ARMOR_KEY);
//                    nGui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.WHITE + "Golden Horse Armor",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add diamond horse armor element
//        item = new ItemStack(Material.DIAMOND_HORSE_ARMOR);
//        meta = item.getItemMeta();
//        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
//        item.setItemMeta(meta);
//        element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var nGui = recipeMenus.get(DIAMOND_HORSE_ARMOR_KEY);
//                    nGui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.WHITE + "Diamond Horse Armor",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add grenade element
//        item = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GRENADE, 1);
//        element = new StaticGuiElement(
//                'i',
//                item,
//                click -> {
//                    var gui = recipeMenus.get(GRENADE_KEY);
//                    gui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.AQUA + "Grenade",
//                ChatColor.GRAY + "An explosive ranged weapon",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add apple of life element
//        item = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.APPLE_OF_LIFE, 1);
//        element = new StaticGuiElement(
//                'j',
//                item,
//                click -> {
//                    var gui = recipeMenus.get(APPLE_OF_LIFE_KEY);
//                    gui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.GOLD + "Apple of Life",
//                ChatColor.GRAY + "A magical fruit which provides +1 life when consumed",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add magical dust element (furnace)
//        item = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, 1);
//        element = new StaticGuiElement(
//                'k',
//                item,
//                click -> {
//                    var gui = recipeMenus.get(MAGICAL_DUST_FURNACE_KEY);
//                    gui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.GOLD + "Magical Dust (Furnace Recipe)",
//                ChatColor.GRAY + "Magic in its purest physical form",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add magical dust element (crafting)
//        item = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, 8);
//        element = new StaticGuiElement(
//                'l',
//                item,
//                click -> {
//                    var gui = recipeMenus.get(MAGICAL_DUST_CRAFTING_KEY);
//                    gui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.GOLD + "Magical Dust (Crafting Table Recipe)",
//                ChatColor.GRAY + "Magic in its purest physical form",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        // add experience bottle element
//        item = new ItemStack(Material.EXPERIENCE_BOTTLE);
//        element = new StaticGuiElement(
//                'm',
//                item,
//                click -> {
//                    var gui = recipeMenus.get(EXPERIENCE_BOTTLE_KEY);
//                    gui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.YELLOW + "Bottle o' Enchanting",
//                ChatColor.GRAY + "A magical bottle which provides orbs of experience when thrown.",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//
//        // add guard crystal
//        item = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.GUARD_CRYSTAL, 1);
//        element = new StaticGuiElement(
//                'n',
//                item,
//                click -> {
//                    var gui = recipeMenus.get(GUARD_CRYSTAL_KEY);
//                    gui.show(click.getEvent().getWhoClicked());
//                    return true;
//                },
//                ChatColor.GREEN + "Guard Crystal",
//                ChatColor.GRAY + "A magical crystal which is used primarily to defend territory from intruders",
//                " ",
//                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View recipe"
//        );
//        itemGroup.addElement(element);
//
//        itemGui.addElement(itemGroup);
//        recipeMenus.put(RECIPES_KEY, itemGui);
//    }
//
//    public InventoryGui getRecipeMenu(String key) {
//        return recipeMenus.get(key);
//    }
//}
