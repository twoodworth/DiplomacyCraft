package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.GuiPageElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.groups.GroupGuiFactory;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;

public class NationGuiFactory {
    public static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public static InventoryGui createMenu(Player player) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Menu";
        String[] guiSetup = {
                "   abc   ",
                "         ",
                "   efg  E",
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        gui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        gui.addElement(new StaticGuiElement('a',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getNations("alphabetically");
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Nations",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all nations"
        ));
        gui.addElement(new StaticGuiElement('b',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var nGui = Guis.getInstance().getPlayers("alphabetically");
                    InventoryGui.clearHistory(player);
                    nGui.show(player, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players"
        ));
        gui.addElement(new StaticGuiElement('c',
                new ItemStack(Material.SHIELD),
                click -> {
                    var nGui = createAllGroups("alphabet", 0);
                    InventoryGui.clearHistory(player);
                    nGui.show(player, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Groups",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all groups"
        ));

        if (nation != null) {
            var banner = nation.getBanner();
            var bannerMeta = (BannerMeta) banner.getItemMeta();
            if (bannerMeta != null) {
                bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            }
            banner.setItemMeta(bannerMeta);
            gui.addElement(new StaticGuiElement('e',
                    banner,
                    click -> {
                        var clicker = click.getEvent().getWhoClicked();
                        var nGui = Guis.getInstance().getNationMenu(nation);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Your Nation",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View your nation"
            ));
        } else {
            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.WHITE_BANNER),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "You do not belong to a nation",
                    ChatColor.BLUE + "Create a nation: " + ChatColor.GRAY + "/nation create <name>"
            ));
        }
        var head = new ItemStack(Material.PLAYER_HEAD);
        var meta = (SkullMeta) head.getItemMeta();
        if (meta != null) {
            meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
            head.setItemMeta(meta);
        }

        gui.addElement(new StaticGuiElement('f',
                head,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "You",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View your player info"
        ));

        gui.addElement(new StaticGuiElement('g',
                new ItemStack(Material.SHIELD),
                click -> {
                    var nGui = createPlayerGroups(diplomacyPlayer, "alphabet", 0);
                    InventoryGui.clearHistory(player);
                    nGui.show(player, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Your Groups",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View your groups"
        ));
        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));
        return gui;
    }

    public static InventoryGui createPlayer(OfflinePlayer player) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        var title = "" + ChatColor.BLUE + ChatColor.BOLD + player.getName();
        String[] guiSetup = {
                "   bac  g",
                "         ",
                "   def  h"
        };

        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        gui.setFiller(new ItemStack(Material.BLUE_STAINED_GLASS_PANE));

        var head = new ItemStack(Material.PLAYER_HEAD);
        var headMeta = (SkullMeta) head.getItemMeta();
        assert headMeta != null;
        headMeta.setOwningPlayer(player);
        head.setItemMeta(headMeta);

        gui.addElement(new StaticGuiElement('a',
                head,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + player.getName()
        ));

        if (nation != null) {

            var banner = nation.getBanner();
            var bannerMeta = (BannerMeta) banner.getItemMeta();
            if (bannerMeta != null) {
                bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            }
            banner.setItemMeta(bannerMeta);
            gui.addElement(new StaticGuiElement('b',
                    banner,
                    click -> {
                        var clicker = click.getEvent().getWhoClicked();
                        var nGui = Guis.getInstance().getNationMenu(nation);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation",
                    ChatColor.GRAY + nation.getName(),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View nation"
            ));
        } else {
            if (Objects.equals(Bukkit.getOfflinePlayer(player.getUniqueId()), player)) {
                gui.addElement(new StaticGuiElement('b',
                        new ItemStack(Material.WHITE_BANNER),
                        click -> true,
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "No nation",
                        ChatColor.BLUE + "Create a nation: " + ChatColor.GRAY + "/nation create <name>"
                ));
            } else {
                gui.addElement(new StaticGuiElement('b',
                        new ItemStack(Material.WHITE_BANNER),
                        click -> true,
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "No nation"
                ));
            }
        }

        gui.addElement(new StaticGuiElement('c',
                new ItemStack(Material.SHIELD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createPlayerGroups(diplomacyPlayer, "alphabet", 0);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Groups",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View groups"
        ));


        if (Objects.equals(Bukkit.getOfflinePlayer(player.getUniqueId()), player)) {
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.DIAMOND),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                    ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(player)),
                    " ",
                    ChatColor.BLUE + "Deposit: " + ChatColor.GRAY + "/deposit <amount>",
                    ChatColor.BLUE + "Withdraw: " + ChatColor.GRAY + "/withdraw <amount>"
            ));
        } else {
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.DIAMOND),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                    ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(player))
            ));
        }

        if (nation != null) {
            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.NETHER_STAR),
                    click -> {
                        var clicker = click.getEvent().getWhoClicked();
                        var nGui = ClassGuiFactory.createPlayerClassSettings(diplomacyPlayer, (Player) clicker);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Class",
                    ChatColor.GRAY + Objects.requireNonNull(nation.getMemberClass(diplomacyPlayer)).getName(),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View permissions"
            ));
        } else {
            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.NETHER_STAR),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Class",
                    ChatColor.GRAY + "None"
            ));
        }
        gui.addElement(new StaticGuiElement('f',
                new ItemStack(Material.CLOCK),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Date Joined",
                ChatColor.GRAY + diplomacyPlayer.getDateJoined()
        ));

        gui.addElement(new StaticGuiElement('g',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getPlayers("alphabetically");
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players"
        ));

        gui.addElement(new StaticGuiElement('h',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));
        return gui;
    }

    public static InventoryGui create(Nation nation) {

        // Create Title
        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + nation.getName() + " Main";

        // Crate setup
        String[] guiSetup = {
                "        z",
                "   bai  p",
                "  oeumn q",
                "  cdsgf r",
                "   klh   ",
                "        t"
        };

        // Create gui & set filler
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        gui.setFiller(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));

        // Nation name element
        gui.addElement(Guis.getInstance().getNameElement(nation));

        // Nation banner element
        gui.addElement(Guis.getInstance().getBannerElement(nation));

        // Nation member element
        gui.addElement(Guis.getInstance().getMembersElement(nation));

        // Nation outlaws element
        gui.addElement(new StaticGuiElement('d',
                new ItemStack(Material.SKELETON_SKULL),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createOutlaws(nation, (Player) clicker, "alphabet", 0);
                    InventoryGui.clearHistory(clicker);
                    InventoryGui.clearHistory(clicker);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Outlaws",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view outlaws"
        ));

        // Nation menu element
        gui.addElement(new StaticGuiElement('z',
                new ItemStack(Material.PAINTING),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createMenu((Player) clicker);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Menu",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Go to menu"
        ));

        // Nation balance element
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

        // Nation groups element
        gui.addElement(new StaticGuiElement('f',
                new ItemStack(Material.SHIELD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createGroups(nation, (Player) clicker, "alphabet", 0);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Groups",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View groups",
                ChatColor.RED + "Currently under maintenance" //TODO fix
        ));

        // Nation classes element
        gui.addElement(new StaticGuiElement('g',
                new ItemStack(Material.NETHER_STAR),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = ClassGuiFactory.create(nation, (Player) clicker);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Classes",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View classes"
        ));

        // Create founder ItemStack
        var founderItem = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (founderItem.getItemMeta());
        var founder = Bukkit.getOfflinePlayer(UUID.fromString(nation.getFounder()));
        Objects.requireNonNull(skullMeta).setOwningPlayer(founder);
        founderItem.setItemMeta(skullMeta);

        // Nation founder element
        gui.addElement(new StaticGuiElement('h',
                founderItem,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createPlayer(founder);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Founder",
                ChatColor.GRAY + founder.getName(),
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View player info"
        ));

        // Nation founded element
        gui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.CLOCK),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Date Founded:",
                ChatColor.GRAY + nation.getDateCreated()
        ));

        // Nation border status element
        gui.addElement(Guis.getInstance().getStatusElement(nation));

        // Nation plot element
        gui.addElement(Guis.getInstance().getPlotElement(nation));

        // Nation allies element
        gui.addElement(new StaticGuiElement('m',
                new ItemStack(Material.LIME_BANNER),
                click -> {
//                    var clicker = click.getEvent().getWhoClicked();
//                    var nGui = createAllyNations(nation, (Player) clicker, "alphabet", 0); //TODO get instead of createInventoryGui.clearHistory(clicker);
//                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Ally Nations",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View ally nations",
                ChatColor.RED + "Currently under maintenance" //TODO fix
        ));

        // Nation enemies element
        gui.addElement(new StaticGuiElement('n',
                new ItemStack(Material.RED_BANNER),
                click -> {
//                    var clicker = click.getEvent().getWhoClicked();
//                    var nGui = createEnemyNations(nation, (Player) clicker, "alphabet", 0); //TODO get instead of createInventoryGui.clearHistory(clicker);
//                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Enemy Nations",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View enemy nations",
                ChatColor.RED + "Currently under maintenance" //TODO fix
        ));

        // Nation rankings element
//        gui.addElement(Guis.getInstance().getRankElement(nation));

        // Nation nations element
        gui.addElement(new StaticGuiElement('p',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getNations("alphabetically");
                    Objects.requireNonNull(nGui).show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Nations",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all nations"
        ));

        // Nation players element
        gui.addElement(new StaticGuiElement('q',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getPlayers("alphabetically");
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players",
                ChatColor.RED + "Currently under maintenance" //TODO fix
        ));

        // Nation groups element
        gui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.SHIELD),
                click -> {
//                    var clicker = click.getEvent().getWhoClicked();
//                    var nGui = createAllGroups("alphabet", 0); //TODO get instead of createInventoryGui.clearHistory(clicker);
//                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Groups",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all groups",
                ChatColor.RED + "Currently under maintenance" //TODO fix
        ));

        // Nation guards element
        gui.addElement(new StaticGuiElement('s',
                new ItemStack(Material.ZOMBIE_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var diplomacyClicker = DiplomacyPlayers.getInstance().get(clicker.getUniqueId());
                    var clickerNation = Nations.getInstance().get(diplomacyClicker);
                    if (!(Objects.equals(nation, clickerNation))) {
                        clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                    var memberClass = nation.getMemberClass(diplomacyClicker);
                    if (memberClass == null || !memberClass.getPermissions().get("CanManageGuards")) {
                        clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                    clicker.sendMessage(ChatColor.RED + "Feature coming soon."); //TODO add
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Guards",
                ChatColor.BLUE + "Coming soon: " + ChatColor.GRAY + "View guards"
        ));

        // Nation escape element
        gui.addElement(new StaticGuiElement('t',
                new ItemStack(Material.BARRIER),
                click -> {
                    InventoryGui.getOpen(click.getEvent().getWhoClicked()).close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));
        return gui;
    }

    public static InventoryGui createMembers(Nation nation, Player player, String sortType, int slot) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        var color = ChatColor.BLUE;
        if (playerNation != null) {
            if (nation.getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(nation, playerNation)) {
                color = ChatColor.DARK_GREEN;
            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                color = ChatColor.RED;
            }
        }
        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Members";
        String[] guiSetup = {
                "  abcde N",
                "A fghij  ",
                "B klmno U",
                "C pqrst D",
                "S uvwxy  ",
                "  z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        var glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        if (playerNation != null) {
            if (Objects.equals(nation, playerNation) || nation.getAllyNationIDs().contains(playerNation.getNationID())) {
                glass = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            }
        }

        gui.setFiller(glass);

        var alphabetical = new ItemStack(Material.WHITE_BANNER);
        var alphabeticalMeta = (BannerMeta) (alphabetical.getItemMeta());
        assert alphabeticalMeta != null;
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        alphabeticalMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        alphabetical.setItemMeta(alphabeticalMeta);

        gui.addElement(new StaticGuiElement('A',
                alphabetical,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createMembers(nation, player, "alphabet", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(nation, player, "reverseAlphabet", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort Alphabetically",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.DIAMOND),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createMembers(nation, player, "balance", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(nation, player, "reverseBalance", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Balance",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.NETHER_STAR),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createMembers(nation, player, "class", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(nation, player, "reverseClass", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Class",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Highest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Lowest first"
        ));
        gui.addElement(new StaticGuiElement('S',
                new ItemStack(Material.CLOCK),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createMembers(nation, player, "age", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(nation, player, "reverseAge", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest first"
        ));

        var scrollUp = new ItemStack(Material.WHITE_BANNER);
        var scrollUpMeta = (BannerMeta) (scrollUp.getItemMeta());
        assert scrollUpMeta != null;
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollUpMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollUp.setItemMeta(scrollUpMeta);

        gui.addElement(new StaticGuiElement('U',
                scrollUp,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createMembers(nation, player, sortType, nSlot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createMembers(nation, player, sortType, nSlot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Up",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll up one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll up six lines"
        ));

        var scrollDown = new ItemStack(Material.WHITE_BANNER);
        var scrollDownMeta = (BannerMeta) (scrollDown.getItemMeta());
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollDownMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollDown.setItemMeta(scrollDownMeta);

        gui.addElement(new StaticGuiElement('D',
                scrollDown,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot + 5;
                        if (nSlot > nation.getMembers().size() + (5 - nation.getMembers().size() % 5) - 30) {
                            nSlot = nation.getMembers().size() + (5 - nation.getMembers().size() % 5) - 30;
                        }
                        if (nation.getMembers().size() > 30) {
                            var nGui = createMembers(nation, player, sortType, nSlot);
                            InventoryGui.clearHistory(player);
                            nGui.show(player, true);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > nation.getMembers().size() + (5 - nation.getMembers().size() % 5) - 30) {
                            nSlot = nation.getMembers().size() + (5 - nation.getMembers().size() % 5) - 30;
                        }
                        if (nation.getMembers().size() > 30) {
                            var nGui = createMembers(nation, player, sortType, nSlot);
                            InventoryGui.clearHistory(player);
                            nGui.show(player, true);
                        }
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Down",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll down one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll down six lines"
        ));

        var banner = nation.getBanner();
        var bannerMeta = (BannerMeta) banner.getItemMeta();
        assert bannerMeta != null;
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        banner.setItemMeta(bannerMeta);

        gui.addElement(new StaticGuiElement('N',
                banner,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getNationMenu(nation);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
                ChatColor.GRAY + "Click to go back"
        ));

        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));

        var membersStr = nation.getMembers();

        List<OfflinePlayer> members = new ArrayList<>();

        for (var member : membersStr) {
            members.add(member.getPlayer());
        }


        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            members.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            members.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            members.stream()
                    .sorted(comparingDouble(p -> -Diplomacy.getEconomy().getBalance(p)))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            members.stream()
                    .sorted(comparingDouble(p -> Diplomacy.getEconomy().getBalance(p)))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("class")) {
            members.stream()
                    .sorted(comparingInt(p -> -Integer.parseInt(nation.getMemberClass(DiplomacyPlayers.getInstance().get(p.getUniqueId())).getClassID())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseClass")) {
            members.stream()
                    .sorted(comparingInt(p -> Integer.parseInt(nation.getMemberClass(DiplomacyPlayers.getInstance().get(p.getUniqueId())).getClassID())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            members.stream()
                    .sorted((p1, p2) -> (int) -((DiplomacyPlayers.getInstance().get(p1.getUniqueId()).getAge() - DiplomacyPlayers.getInstance().get(p2.getUniqueId()).getAge())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            members.stream()
                    .sorted((p1, p2) -> (int) (DiplomacyPlayers.getInstance().get(p1.getUniqueId()).getAge() - DiplomacyPlayers.getInstance().get(p2.getUniqueId()).getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private static StaticGuiElement createMemberElement(Nation nation, Player player, OfflinePlayer member, char slot) {
        var memberHead = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (memberHead.getItemMeta());
        skullMeta.setOwningPlayer(member);
        memberHead.setItemMeta(skullMeta);

        var diplomacyMember = DiplomacyPlayers.getInstance().get(member.getUniqueId());
        var memberClass = nation.getMemberClass(diplomacyMember).getName();

        return new StaticGuiElement(slot,
                memberHead,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createPlayer(member);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + member.getName(),
                ChatColor.BLUE + "Class: " + ChatColor.GRAY + memberClass,
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(member)),
                ChatColor.BLUE + "Joined: " + ChatColor.GRAY + DiplomacyPlayers.getInstance().get(member.getUniqueId()).getDateJoined()
        );
    }

    public static InventoryGui createOutlaws(Nation nation, Player player, String sortType, int slot) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        var color = ChatColor.BLUE;
        if (playerNation != null) {
            if (nation.getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(nation, playerNation)) {
                color = ChatColor.DARK_GREEN;
            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                color = ChatColor.RED;
            }
        }
        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Outlaws";
        String[] guiSetup = {
                "  abcde N",
                "A fghij  ",
                "B klmno U",
                "G pqrst D",
                "C uvwxy  ",
                "  z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        var glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        if (playerNation != null) {
            if (Objects.equals(nation, playerNation) || nation.getAllyNationIDs().contains(playerNation.getNationID())) {
                glass = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            }
        }

        gui.setFiller(glass);

        var alphabetical = new ItemStack(Material.WHITE_BANNER);
        var alphabeticalMeta = (BannerMeta) (alphabetical.getItemMeta());
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        alphabeticalMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        alphabetical.setItemMeta(alphabeticalMeta);

        gui.addElement(new StaticGuiElement('A',
                alphabetical,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createOutlaws(nation, player, "alphabet", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createOutlaws(nation, player, "reverseAlphabet", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort Alphabetically",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.DIAMOND),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createOutlaws(nation, player, "balance", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createOutlaws(nation, player, "reverseBalance", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Balance",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.CLOCK),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createOutlaws(nation, player, "age", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createOutlaws(nation, player, "reverseAge", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest first"
        ));

        var scrollUp = new ItemStack(Material.WHITE_BANNER);
        var scrollUpMeta = (BannerMeta) (scrollUp.getItemMeta());
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollUpMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollUp.setItemMeta(scrollUpMeta);

        gui.addElement(new StaticGuiElement('U',
                scrollUp,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createOutlaws(nation, player, sortType, nSlot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createOutlaws(nation, player, sortType, nSlot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Up",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll up one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll up six lines"
        ));

        var scrollDown = new ItemStack(Material.WHITE_BANNER);
        var scrollDownMeta = (BannerMeta) (scrollDown.getItemMeta());
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollDownMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollDown.setItemMeta(scrollDownMeta);

        gui.addElement(new StaticGuiElement('D',
                scrollDown,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot + 5;
                        if (nSlot > nation.getOutlaws().size() + (5 - nation.getOutlaws().size() % 5) - 30) {
                            nSlot = nation.getOutlaws().size() + (5 - nation.getOutlaws().size() % 5) - 30;
                        }
                        if (nation.getOutlaws().size() > 30) {
                            var nGui = createOutlaws(nation, player, sortType, nSlot);
                            InventoryGui.clearHistory(player);
                            nGui.show(player, true);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > nation.getOutlaws().size() + (5 - nation.getOutlaws().size() % 5) - 30) {
                            nSlot = nation.getOutlaws().size() + (5 - nation.getOutlaws().size() % 5) - 30;
                        }
                        if (nation.getOutlaws().size() > 30) {
                            var nGui = createOutlaws(nation, player, sortType, nSlot);
                            InventoryGui.clearHistory(player);
                            nGui.show(player, true);
                        }
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Down",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll down one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll down six lines"
        ));

        var banner = nation.getBanner();
        var bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        banner.setItemMeta(bannerMeta);

        gui.addElement(new StaticGuiElement('N',
                banner,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getNationMenu(nation);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
                ChatColor.GRAY + "Click to go back"
        ));

        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));
        gui.addElement(new StaticGuiElement('G',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createOutlaws(nation, player, "nation", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createOutlaws(nation, player, "reverseNation", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Nation Name",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));

        var outlawsStr = nation.getOutlaws();
        List<OfflinePlayer> outlaws = new ArrayList<>();

        for (var outlawStr : outlawsStr) {
            var outlaw = Bukkit.getOfflinePlayer(outlawStr);
            outlaws.add(outlaw);
        }


        var skipped = slot * 5;
        var charSlot = new char[]{'a'};

        if (sortType.equals("alphabet")) {

            outlaws.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(skipped)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createOutlawElement(nation, player, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            outlaws.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(skipped)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createMemberElement(nation, player, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            outlaws.stream()
                    .sorted(comparingDouble(p -> -Diplomacy.getEconomy().getBalance(p)))
                    .skip(skipped)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createMemberElement(nation, player, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            outlaws.stream()
                    .sorted(comparingDouble(p -> Diplomacy.getEconomy().getBalance(p)))
                    .skip(skipped)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createOutlawElement(nation, player, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            outlaws.stream()
                    .sorted((p1, p2) -> (int) ((DiplomacyPlayers.getInstance().get(p1.getUniqueId()).getAge() - DiplomacyPlayers.getInstance().get(p2.getUniqueId()).getAge())))
                    .skip(slot)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createMemberElement(nation, player, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            outlaws.stream()
                    .sorted((p1, p2) -> (int) -(DiplomacyPlayers.getInstance().get(p1.getUniqueId()).getAge() - DiplomacyPlayers.getInstance().get(p2.getUniqueId()).getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createMemberElement(nation, player, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("nation")) {
            outlaws.stream()
                    .sorted((p1, p2) -> {
                        var p1value = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(p1.getUniqueId()));
                        var p2value = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(p2.getUniqueId()));
                        if (p1value == null) {
                            if (p2value == null) {
                                return 0;
                            }
                            return 1;
                        } else if (p2value == null) {
                            return -1;
                        }
                        return p1value.getName().compareToIgnoreCase(p2value.getName());
                    })
                    .skip(skipped)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createOutlawElement(nation, player, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseNation")) {
            outlaws.stream()
                    .sorted((p1, p2) -> {
                        var p1value = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(p1.getUniqueId()));
                        var p2value = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(p2.getUniqueId()));
                        if (p1value == null) {
                            if (p2value == null) {
                                return 0;
                            }
                            return -1;
                        } else if (p2value == null) {
                            return 1;
                        }
                        return -p1value.getName().compareToIgnoreCase(p2value.getName());
                    })
                    .skip(skipped)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createOutlawElement(nation, player, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private static StaticGuiElement createOutlawElement(Nation nation, Player player, OfflinePlayer outlaw, char slot) {
        var memberHead = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (memberHead.getItemMeta());
        skullMeta.setOwningPlayer(outlaw);
        memberHead.setItemMeta(skullMeta);

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(outlaw.getUniqueId());
        var strOutlawNation = "None";
        var strClass = "None";
        var color = ChatColor.GRAY;
        if (Nations.getInstance().get(diplomacyPlayer) != null) {
            var outlawNation = Nations.getInstance().get(diplomacyPlayer);
            var outlawClass = outlawNation.getMemberClass(diplomacyPlayer);
            strClass = outlawClass.getName();
            strOutlawNation = outlawNation.getName();
            if (nation.getAllyNationIDs().contains(outlawNation.getNationID()) || Objects.equals(nation, outlawNation)) {
                color = ChatColor.GREEN;
            } else if (nation.getEnemyNationIDs().contains(outlawNation.getNationID())) {
                color = ChatColor.RED;
            } else {
                color = ChatColor.BLUE;
            }
        }


        return new StaticGuiElement(slot,
                memberHead,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createPlayer(outlaw);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + outlaw.getName(),
                ChatColor.BLUE + "Nation: " + color + strOutlawNation,
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(outlaw)),
                ChatColor.BLUE + "Server Join Date: " + ChatColor.GRAY + DiplomacyPlayers.getInstance().get(outlaw.getUniqueId()).getDateJoined()
        );
    }

    public static InventoryGui createGroups(Nation nation, Player player, String sortType, int slot) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        var color = ChatColor.BLUE;
        if (playerNation != null) {
            if (nation.getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(nation, playerNation)) {
                color = ChatColor.DARK_GREEN;
            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                color = ChatColor.RED;
            }
        }
        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Groups";
        String[] guiSetup = {
                "  abcde N",
                "A fghij  ",
                "B klmno U",
                "C pqrst D",
                "G uvwxy  ",
                "  z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        var glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        if (playerNation != null) {
            if (Objects.equals(nation, playerNation) || nation.getAllyNationIDs().contains(playerNation.getNationID())) {
                glass = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            }
        }

        gui.setFiller(glass);

        var alphabetical = new ItemStack(Material.WHITE_BANNER);
        var alphabeticalMeta = (BannerMeta) (alphabetical.getItemMeta());
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        alphabeticalMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        alphabetical.setItemMeta(alphabeticalMeta);

        gui.addElement(new StaticGuiElement('A',
                alphabetical,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createGroups(nation, player, "alphabet", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createGroups(nation, player, "reverseAlphabet", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort Alphabetically",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createGroups(nation, player, "population", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createGroups(nation, player, "reversePopulation", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Population",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.GRASS_BLOCK),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createGroups(nation, player, "territory", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createGroups(nation, player, "reverseTerritory", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Territory Size",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('G',
                new ItemStack(Material.CLOCK),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createGroups(nation, player, "age", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createGroups(nation, player, "reverseAge", slot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
        ));

        var scrollUp = new ItemStack(Material.WHITE_BANNER);
        var scrollUpMeta = (BannerMeta) (scrollUp.getItemMeta());
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollUpMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollUp.setItemMeta(scrollUpMeta);

        gui.addElement(new StaticGuiElement('U',
                scrollUp,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createGroups(nation, player, sortType, nSlot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createGroups(nation, player, sortType, nSlot);
                        InventoryGui.clearHistory(player);
                        nGui.show(player, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Up",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll up one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll up six lines"
        ));

        var scrollDown = new ItemStack(Material.WHITE_BANNER);
        var scrollDownMeta = (BannerMeta) (scrollDown.getItemMeta());
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollDownMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollDown.setItemMeta(scrollDownMeta);

        gui.addElement(new StaticGuiElement('D',
                scrollDown,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot + 5;
                        if (nSlot > nation.getGroups().size() + (5 - nation.getGroups().size() % 5) - 30) {
                            nSlot = nation.getGroups().size() + (5 - nation.getGroups().size() % 5) - 30;
                        }
                        if (nation.getGroups().size() > 30) {
                            var nGui = createGroups(nation, player, sortType, nSlot);
                            InventoryGui.clearHistory(player);
                            nGui.show(player, true);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > nation.getGroups().size() + (5 - nation.getGroups().size() % 5) - 30) {
                            nSlot = nation.getGroups().size() + (5 - nation.getGroups().size() % 5) - 30;
                        }
                        if (nation.getGroups().size() > 30) {
                            var nGui = createGroups(nation, player, sortType, nSlot);
                            InventoryGui.clearHistory(player);
                            nGui.show(player, true);
                        }
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Down",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll down one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll down six lines"
        ));

        var banner = nation.getBanner();
        var bannerMeta = (BannerMeta) banner.getItemMeta();
        if (bannerMeta != null) {
            bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
        banner.setItemMeta(bannerMeta);

        gui.addElement(new StaticGuiElement('N',
                banner,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getNationMenu(nation);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
                ChatColor.GRAY + "Click to go back"
        ));

        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));

        var groups = nation.getGroups();

        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            groups.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createGroupElement(nation, player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            groups.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createGroupElement(nation, player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("population")) {
            groups.stream()
                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createGroupElement(nation, player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reversePopulation")) {
            groups.stream()
                    .sorted(comparingInt(p -> p.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createGroupElement(nation, player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("territory")) {
            groups.stream()
                    .sorted(comparingInt(p -> -p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createGroupElement(nation, player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseTerritory")) {
            groups.stream()
                    .sorted(comparingInt(p -> p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createGroupElement(nation, player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            groups.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createGroupElement(nation, player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            groups.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createGroupElement(nation, player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private static StaticGuiElement createGroupElement(Nation nation, Player player, DiplomacyGroup group, char slot) {
        var shield = group.getShield();

        var plots = group.getChunks().size();
        var label = " plots";
        if (plots == 1) {
            label = " plot";
        }

        return new StaticGuiElement(slot,
                shield,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = GroupGuiFactory.create(group);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + group.getName(),
                ChatColor.BLUE + "Population: " + ChatColor.GRAY + group.getMembers().size(),
                ChatColor.BLUE + "Territory: " + ChatColor.GRAY + plots + label,
                ChatColor.BLUE + "Created On: " + ChatColor.GRAY + group.getDateCreated()
        );
    }

//    public static InventoryGui createAllyNations(Nation nation, Player player, String sortType, int slot) {
//        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
//        var playerNation = Nations.getInstance().get(diplomacyPlayer);
//        var color = ChatColor.BLUE;
//        if (playerNation != null) {
//            if (nation.getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(nation, playerNation)) {
//                color = ChatColor.DARK_GREEN;
//            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
//                color = ChatColor.RED;
//            }
//        }
//        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Allies";
//        String[] guiSetup = {
//                "A abcde N",
//                "P fghij  ",
//                "L klmno U",
//                "B pqrst D",
//                "T uvwxy  ",
//                "G z{|}~ E"
//        };
//        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
//        var glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
//        if (playerNation != null) {
//            if (Objects.equals(nation, playerNation) || nation.getAllyNationIDs().contains(playerNation.getNationID())) {
//                glass = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
//            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
//                glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
//            }
//        }
//
//        gui.setFiller(glass);
//
//        var alphabetical = new ItemStack(Material.WHITE_BANNER);
//        var alphabeticalMeta = (BannerMeta) (alphabetical.getItemMeta());
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
//        alphabeticalMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        alphabetical.setItemMeta(alphabeticalMeta);
//
//        gui.addElement(new StaticGuiElement('A',
//                alphabetical,
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createAllyNations(nation, player, "alphabet", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createAllyNations(nation, player, "reverseAlphabet", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort Alphabetically",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
//        ));
//        gui.addElement(new StaticGuiElement('L',
//                new ItemStack(Material.PLAYER_HEAD),
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createAllyNations(nation, player, "population", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createAllyNations(nation, player, "reversePopulation", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Population",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
//        ));
//        gui.addElement(new StaticGuiElement('T',
//                new ItemStack(Material.GRASS_BLOCK),
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createAllyNations(nation, player, "territory", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createAllyNations(nation, player, "reverseTerritory", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Territory Size",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
//        ));
//        gui.addElement(new StaticGuiElement('G',
//                new ItemStack(Material.CLOCK),
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createAllyNations(nation, player, "age", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createAllyNations(nation, player, "reverseAge", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
//        ));
//        gui.addElement(new StaticGuiElement('B',
//                new ItemStack(Material.DIAMOND),
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createAllyNations(nation, player, "balance", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createAllyNations(nation, player, "reverseBalance", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Balance",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest First",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest First"
//        ));
//
//        var scrollUp = new ItemStack(Material.WHITE_BANNER);
//        var scrollUpMeta = (BannerMeta) (scrollUp.getItemMeta());
//        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
//        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
//        scrollUpMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
//        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
//        scrollUpMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        scrollUp.setItemMeta(scrollUpMeta);
//
//        gui.addElement(new StaticGuiElement('U',
//                scrollUp,
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nSlot = slot - 5;
//                        if (nSlot < 0) {
//                            nSlot = 0;
//                        }
//                        var nGui = createAllyNations(nation, player, sortType, nSlot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nSlot = slot - 30;
//                        if (nSlot < 0) {
//                            nSlot = 0;
//                        }
//                        var nGui = createAllyNations(nation, player, sortType, nSlot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Up",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll up one line",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll up six lines"
//        ));
//
//        var scrollDown = new ItemStack(Material.WHITE_BANNER);
//        var scrollDownMeta = (BannerMeta) (scrollDown.getItemMeta());
//        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
//        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
//        scrollDownMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
//        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
//        scrollDownMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        scrollDown.setItemMeta(scrollDownMeta);
//
//        gui.addElement(new StaticGuiElement('D',
//                scrollDown,
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nSlot = slot + 5;
//                        if (nSlot > nation.getAllyNationIDs().size() + (5 - nation.getAllyNationIDs().size() % 5) - 30) {
//                            nSlot = nation.getAllyNationIDs().size() + (5 - nation.getAllyNationIDs().size() % 5) - 30;
//                        }
//                        if (nation.getAllyNationIDs().size() > 30) {
//                            var nGui = createAllyNations(nation, player, sortType, nSlot);
//                            InventoryGui.clearHistory(player);
//                            nGui.show(player, true);
//                        }
//                    } else if (click.getType().isRightClick()) {
//                        var nSlot = slot + 30;
//                        if (nSlot > nation.getAllyNationIDs().size() + (5 - nation.getAllyNationIDs().size() % 5) - 30) {
//                            nSlot = nation.getAllyNationIDs().size() + (5 - nation.getAllyNationIDs().size() % 5) - 30;
//                        }
//                        if (nation.getAllyNationIDs().size() > 30) {
//                            var nGui = createAllyNations(nation, player, sortType, nSlot);
//                            InventoryGui.clearHistory(player);
//                            nGui.show(player, true);
//                        }
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Down",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll down one line",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll down six lines"
//        ));
//
//        var banner = nation.getBanner();
//        var bannerMeta = (BannerMeta) banner.getItemMeta();
//        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        banner.setItemMeta(bannerMeta);
//
//        gui.addElement(new StaticGuiElement('N',
//                banner,
//                click -> {
//                    var clicker = click.getEvent().getWhoClicked();
//                    var nGui = Guis.getInstance().getNationMenu(nation);
//                    InventoryGui.clearHistory(clicker);
//                    nGui.show(clicker, true);
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
//                ChatColor.GRAY + "Click to go back"
//        ));
//
//        gui.addElement(new StaticGuiElement('E',
//                new ItemStack(Material.BARRIER),
//                click -> {
//                    gui.close();
//                    return true;
//                },
//                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
//                ChatColor.GRAY + "Click to escape"
//        ));
//
//        var allyNationIDs = nation.getAllyNationIDs();
//        var allyNations = new ArrayList<Nation>();
//        for (var id : allyNationIDs) {
//            allyNations.add(Nations.getInstance().get(Integer.parseInt(id)));
//        }
//
//        var slotChar = new char[]{'a'};
//
//        if (sortType.equals("alphabet")) {
//            allyNations.stream()
//                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reverseAlphabet")) {
//            allyNations.stream()
//                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("population")) {
//            allyNations.stream()
//                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reversePopulation")) {
//            allyNations.stream()
//                    .sorted(comparingInt(p -> p.getMembers().size()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("territory")) {
//            allyNations.stream()
//                    .sorted(comparingInt(p -> -p.getChunks().size()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reverseTerritory")) {
//            allyNations.stream()
//                    .sorted(comparingInt(p -> p.getChunks().size()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("age")) {
//            allyNations.stream()
//                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reverseAge")) {
//            allyNations.stream()
//                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("balance")) {
//            allyNations.stream()
//                    .sorted((p1, p2) -> (int) -(100 * (p1.getBalance() - p2.getBalance())))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reverseBalance")) {
//            allyNations.stream()
//                    .sorted((p1, p2) -> (int) (100 * (p1.getBalance() - p2.getBalance())))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(allyNation -> {
//                        var element = createNationElement(allyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        }
//        return gui;
//    }

//    public static InventoryGui createEnemyNations(Nation nation, Player player, String sortType, int slot) {
//        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
//        var playerNation = Nations.getInstance().get(diplomacyPlayer);
//        var color = ChatColor.BLUE;
//        if (playerNation != null) {
//            if (nation.getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(nation, playerNation)) {
//                color = ChatColor.DARK_GREEN;
//            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
//                color = ChatColor.RED;
//            }
//        }
//        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Enemies";
//        String[] guiSetup = {
//                "A abcde N",
//                "P fghij  ",
//                "L klmno U",
//                "B pqrst D",
//                "T uvwxy  ",
//                "G z{|}~ E"
//        };
//        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
//        var glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
//        if (playerNation != null) {
//            if (Objects.equals(nation, playerNation) || nation.getAllyNationIDs().contains(playerNation.getNationID())) {
//                glass = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
//            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
//                glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
//            }
//        }
//
//        gui.setFiller(glass);
//
//        var alphabetical = new ItemStack(Material.WHITE_BANNER);
//        var alphabeticalMeta = (BannerMeta) (alphabetical.getItemMeta());
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
//        alphabeticalMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
//        alphabeticalMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        alphabetical.setItemMeta(alphabeticalMeta);
//
//        gui.addElement(new StaticGuiElement('A',
//                alphabetical,
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createEnemyNations(nation, player, "alphabet", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createEnemyNations(nation, player, "reverseAlphabet", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort Alphabetically",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
//        ));
//        gui.addElement(new StaticGuiElement('L',
//                new ItemStack(Material.PLAYER_HEAD),
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createEnemyNations(nation, player, "population", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createEnemyNations(nation, player, "reversePopulation", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Population",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
//        ));
//        gui.addElement(new StaticGuiElement('T',
//                new ItemStack(Material.GRASS_BLOCK),
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createEnemyNations(nation, player, "territory", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createEnemyNations(nation, player, "reverseTerritory", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Territory Size",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
//        ));
//        gui.addElement(new StaticGuiElement('G',
//                new ItemStack(Material.CLOCK),
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createEnemyNations(nation, player, "age", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createEnemyNations(nation, player, "reverseAge", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
//        ));
//        gui.addElement(new StaticGuiElement('B',
//                new ItemStack(Material.DIAMOND),
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nGui = createEnemyNations(nation, player, "balance", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nGui = createEnemyNations(nation, player, "reverseBalance", slot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Balance",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest First",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest First"
//        ));
//
//        var scrollUp = new ItemStack(Material.WHITE_BANNER);
//        var scrollUpMeta = (BannerMeta) (scrollUp.getItemMeta());
//        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
//        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
//        scrollUpMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
//        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
//        scrollUpMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        scrollUp.setItemMeta(scrollUpMeta);
//
//        gui.addElement(new StaticGuiElement('U',
//                scrollUp,
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nSlot = slot - 5;
//                        if (nSlot < 0) {
//                            nSlot = 0;
//                        }
//                        var nGui = createEnemyNations(nation, player, sortType, nSlot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    } else if (click.getType().isRightClick()) {
//                        var nSlot = slot - 30;
//                        if (nSlot < 0) {
//                            nSlot = 0;
//                        }
//                        var nGui = createEnemyNations(nation, player, sortType, nSlot);
//                        InventoryGui.clearHistory(player);
//                        nGui.show(player, true);
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Up",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll up one line",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll up six lines"
//        ));
//
//        var scrollDown = new ItemStack(Material.WHITE_BANNER);
//        var scrollDownMeta = (BannerMeta) (scrollDown.getItemMeta());
//        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
//        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
//        scrollDownMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
//        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
//        scrollDownMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        scrollDown.setItemMeta(scrollDownMeta);
//
//        gui.addElement(new StaticGuiElement('D',
//                scrollDown,
//                click -> {
//                    if (click.getType().isLeftClick()) {
//                        var nSlot = slot + 5;
//                        if (nSlot > nation.getEnemyNationIDs().size() + (5 - nation.getEnemyNationIDs().size() % 5) - 30) {
//                            nSlot = nation.getEnemyNationIDs().size() + (5 - nation.getEnemyNationIDs().size() % 5) - 30;
//                        }
//                        if (nation.getEnemyNationIDs().size() > 30) {
//                            var nGui = createEnemyNations(nation, player, sortType, nSlot);
//                            InventoryGui.clearHistory(player);
//                            nGui.show(player, true);
//                        }
//                    } else if (click.getType().isRightClick()) {
//                        var nSlot = slot + 30;
//                        if (nSlot > nation.getEnemyNationIDs().size() + (5 - nation.getEnemyNationIDs().size() % 5) - 30) {
//                            nSlot = nation.getEnemyNationIDs().size() + (5 - nation.getEnemyNationIDs().size() % 5) - 30;
//                        }
//                        if (nation.getEnemyNationIDs().size() > 30) {
//                            var nGui = createEnemyNations(nation, player, sortType, nSlot);
//                            InventoryGui.clearHistory(player);
//                            nGui.show(player, true);
//                        }
//                    }
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Down",
//                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll down one line",
//                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll down six lines"
//        ));
//
//        var banner = nation.getBanner();
//        var bannerMeta = (BannerMeta) banner.getItemMeta();
//        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
//        banner.setItemMeta(bannerMeta);
//
//        gui.addElement(new StaticGuiElement('N',
//                banner,
//                click -> {
//                    var clicker = click.getEvent().getWhoClicked();
//                    var nGui = Guis.getInstance().getNationMenu(nation);
//                    InventoryGui.clearHistory(clicker);
//                    nGui.show(clicker, true);
//                    return true;
//                },
//                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
//                ChatColor.GRAY + "Click to go back"
//        ));
//
//        gui.addElement(new StaticGuiElement('E',
//                new ItemStack(Material.BARRIER),
//                click -> {
//                    gui.close();
//                    return true;
//                },
//                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
//                ChatColor.GRAY + "Click to escape"
//        ));
//
//        var enemyNationIDs = nation.getEnemyNationIDs();
//        var enemyNations = new ArrayList<Nation>();
//        for (var id : enemyNationIDs) {
//            enemyNations.add(Nations.getInstance().get(Integer.parseInt(id)));
//        }
//
//        var slotChar = new char[]{'a'};
//
//        if (sortType.equals("alphabet")) {
//            enemyNations.stream()
//                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reverseAlphabet")) {
//            enemyNations.stream()
//                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("population")) {
//            enemyNations.stream()
//                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reversePopulation")) {
//            enemyNations.stream()
//                    .sorted(comparingInt(p -> p.getMembers().size()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("territory")) {
//            enemyNations.stream()
//                    .sorted(comparingInt(p -> -p.getChunks().size()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reverseTerritory")) {
//            enemyNations.stream()
//                    .sorted(comparingInt(p -> p.getChunks().size()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("age")) {
//            enemyNations.stream()
//                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reverseAge")) {
//            enemyNations.stream()
//                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("balance")) {
//            enemyNations.stream()
//                    .sorted((p1, p2) -> (int) -(100 * (p1.getBalance() - p2.getBalance())))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        } else if (sortType.equals("reverseBalance")) {
//            enemyNations.stream()
//                    .sorted((p1, p2) -> (int) (100 * (p1.getBalance() - p2.getBalance())))
//                    .skip(slot)
//                    .limit(30)
//                    .forEach(enemyNation -> {
//                        var element = createNationElement(enemyNation, slotChar[0]++);
//                        gui.addElement(element);
//                    });
//        }
//        return gui;
//    }

    public static InventoryGui createNations(String sortType) {

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Nations";
        String[] guiSetup = {
                "X ggggg Z",
                "A ggggg  ",
                "L ggggg U",
                "B ggggg D",
                "T ggggg  ",
                "G ggggg E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);

        gui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        var alphabetical = new ItemStack(Material.WHITE_BANNER);
        var alphabeticalMeta = (BannerMeta) (alphabetical.getItemMeta());
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        alphabeticalMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        alphabetical.setItemMeta(alphabeticalMeta);

        gui.addElement(Guis.getInstance().getTimeElement());

        gui.addElement(new StaticGuiElement('Z',
                new ItemStack(Material.PAINTING),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createMenu((Player) clicker);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Menu",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Go to menu"
        ));
        gui.addElement(new StaticGuiElement('A',
                alphabetical,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick() && !sortType.equals("alphabetically")) {
                        var nGui = Guis.getInstance().getNations("alphabetically");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick() && !sortType.equals("reverseAlphabetically")) {
                        var nGui = Guis.getInstance().getNations("reverseAlphabetically");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort Alphabetically",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));
        gui.addElement(new StaticGuiElement('L',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick() && !sortType.equals("population")) {
                        var nGui = Guis.getInstance().getNations("population");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick() && !sortType.equals("reversePopulation")) {
                        var nGui = Guis.getInstance().getNations("reversePopulation");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Population",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('T',
                new ItemStack(Material.GRASS_BLOCK),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick() && !sortType.equals("territory")) {
                        var nGui = Guis.getInstance().getNations("territory");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick() && !sortType.equals("reverseTerritory")) {
                        var nGui = Guis.getInstance().getNations("reverseTerritory");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Territory Size",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('G',
                new ItemStack(Material.CLOCK),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick() && !sortType.equals("age")) {
                        var nGui = Guis.getInstance().getNations("age");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick() && !sortType.equals("reverseAge")) {
                        var nGui = Guis.getInstance().getNations("reverseAge");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.DIAMOND),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick() && !sortType.equals("balance")) {
                        var nGui = Guis.getInstance().getNations("balance");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick() && !sortType.equals("reverseBalance")) {
                        var nGui = Guis.getInstance().getNations("reverseBalance");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Balance",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest First"
        ));

        var scrollUp = new ItemStack(Material.WHITE_BANNER);
        var scrollUpMeta = (BannerMeta) (scrollUp.getItemMeta());
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollUpMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollUp.setItemMeta(scrollUpMeta);

        gui.addElement(new GuiPageElement('U',
                scrollUp,
                GuiPageElement.PageAction.PREVIOUS,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Previous page",
                ChatColor.BLUE + "Current: " + ChatColor.GRAY + "%page%"
        ));

        var scrollDown = new ItemStack(Material.WHITE_BANNER);
        var scrollDownMeta = (BannerMeta) (scrollDown.getItemMeta());
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollDownMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollDown.setItemMeta(scrollDownMeta);

        gui.addElement(new GuiPageElement('D',
                scrollDown,
                GuiPageElement.PageAction.NEXT,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Next page",
                ChatColor.BLUE + "Current: " + ChatColor.GRAY + "%page%"
        ));

        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));

        gui.addElement(Guis.getInstance().getNationsGroup(sortType));
        return gui;
    }

    public static InventoryGui createPlayers(String sortType) {

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Players";
        String[] guiSetup = {
                "X ggggg N",
                "A ggggg  ",
                "B ggggg U",
                "C ggggg D",
                "G ggggg  ",
                "  ggggg E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        gui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        var alphabetical = new ItemStack(Material.WHITE_BANNER);
        var alphabeticalMeta = (BannerMeta) (alphabetical.getItemMeta());
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        alphabeticalMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        alphabetical.setItemMeta(alphabeticalMeta);

        // X
        gui.addElement(Guis.getInstance().getTimeElement());

        gui.addElement(new StaticGuiElement('A',
                alphabetical,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = Guis.getInstance().getPlayers("alphabetically");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = Guis.getInstance().getPlayers("reverseAlphabetically");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort Alphabetically",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.DIAMOND),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = Guis.getInstance().getPlayers("balance");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = Guis.getInstance().getPlayers("reverseBalance");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Balance",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = Guis.getInstance().getPlayers("nation");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = Guis.getInstance().getPlayers("reverseNation");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Nation Name",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));
        gui.addElement(new StaticGuiElement('G',
                new ItemStack(Material.CLOCK),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = Guis.getInstance().getPlayers("age");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = Guis.getInstance().getPlayers("reverseAge");
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
        ));

        var scrollUp = new ItemStack(Material.WHITE_BANNER);
        var scrollUpMeta = (BannerMeta) (scrollUp.getItemMeta());
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollUpMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollUp.setItemMeta(scrollUpMeta);

        gui.addElement(new GuiPageElement('U',
                scrollUp,
                GuiPageElement.PageAction.PREVIOUS,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Previous page",
                ChatColor.BLUE + "Current: " + ChatColor.GRAY + "%page%"
        ));

        var scrollDown = new ItemStack(Material.WHITE_BANNER);
        var scrollDownMeta = (BannerMeta) (scrollDown.getItemMeta());
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollDownMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollDown.setItemMeta(scrollDownMeta);

        gui.addElement(new GuiPageElement('D',
                scrollDown,
                GuiPageElement.PageAction.NEXT,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Next page",
                ChatColor.BLUE + "Current: " + ChatColor.GRAY + "%page%"
        ));

        gui.addElement(new StaticGuiElement('N',
                new ItemStack(Material.PAINTING),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createMenu((Player) clicker);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Menu",
                ChatColor.GRAY + "Go to menu"
        ));

        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));
        var allPlayers = DiplomacyPlayers.getInstance().getPlayers();
        var players = new ArrayList<DiplomacyPlayer>();
        for (var testPlayer : allPlayers) {
            if (testPlayer.getPlayer().hasPlayedBefore()) {
                players.add(testPlayer);
            }
        }

        gui.addElement(Guis.getInstance().getPlayersGroup(sortType));
        return gui;
    }

    private static StaticGuiElement createPlayerElement(DiplomacyPlayer player, char slot) {
        var memberHead = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (memberHead.getItemMeta());
        if (skullMeta != null) {
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUUID()));
            memberHead.setItemMeta(skullMeta);
        }

        var nation = Nations.getInstance().get(player);
        var offlinePlayer = Bukkit.getOfflinePlayer(player.getUUID());

        var strNation = "None";
        if (nation != null) {
            strNation = nation.getName();
        }

        return new StaticGuiElement(slot,
                memberHead,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createPlayer(player.getPlayer());
                    nGui.show(clicker);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + offlinePlayer.getName(),
                ChatColor.BLUE + "Nation: " + ChatColor.GRAY + strNation,
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(offlinePlayer)),
                ChatColor.BLUE + "Server Join Date: " + ChatColor.GRAY + DiplomacyPlayers.getInstance().get(player.getUUID()).getDateJoined()
        );
    }

    public static InventoryGui createAllGroups(String sortType, int slot) {

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Groups";
        String[] guiSetup = {
                "A abcde Z",
                "B fghij  ",
                "C klmno U",
                "G pqrst D",
                "N uvwxy  ",
                "  z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        gui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        var alphabetical = new ItemStack(Material.WHITE_BANNER);
        var alphabeticalMeta = (BannerMeta) (alphabetical.getItemMeta());
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        alphabeticalMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        alphabetical.setItemMeta(alphabeticalMeta);

        gui.addElement(new StaticGuiElement('A',
                alphabetical,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllGroups("alphabet", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups("reverseAlphabet", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort Alphabetically",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllGroups("population", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups("reversePopulation", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Population",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.GRASS_BLOCK),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllGroups("territory", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups("reverseTerritory", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Territory Size",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('G',
                new ItemStack(Material.CLOCK),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllGroups("age", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups("reverseAge", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
        ));

        gui.addElement(new StaticGuiElement('N',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllGroups("nation", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups("reverseNation", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Nation Name",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));

        var scrollUp = new ItemStack(Material.WHITE_BANNER);
        var scrollUpMeta = (BannerMeta) (scrollUp.getItemMeta());
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollUpMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollUp.setItemMeta(scrollUpMeta);

        gui.addElement(new StaticGuiElement('U',
                scrollUp,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createAllGroups(sortType, nSlot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createAllGroups(sortType, nSlot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Up",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll up one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll up six lines"
        ));

        var scrollDown = new ItemStack(Material.WHITE_BANNER);
        var scrollDownMeta = (BannerMeta) (scrollDown.getItemMeta());
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollDownMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollDown.setItemMeta(scrollDownMeta);

        gui.addElement(new StaticGuiElement('D',
                scrollDown,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot + 5;
                        if (nSlot > DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30) {
                            nSlot = DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30;
                        }
                        if (DiplomacyGroups.getInstance().getGroups().size() > 30) {
                            var nGui = createAllGroups(sortType, nSlot);
                            InventoryGui.clearHistory(clicker);
                            nGui.show(clicker, true);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30) {
                            nSlot = DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30;
                        }
                        if (DiplomacyGroups.getInstance().getGroups().size() > 30) {
                            var nGui = createAllGroups(sortType, nSlot);
                            InventoryGui.clearHistory(clicker);
                            nGui.show(clicker, true);
                        }
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Down",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll down one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll down six lines"
        ));

        gui.addElement(new StaticGuiElement('Z',
                new ItemStack(Material.PAINTING),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createMenu((Player) clicker);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Menu",
                ChatColor.GRAY + "Click to go to menu"
        ));

        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));

        var groups = DiplomacyGroups.getInstance().getGroups();

        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            groups.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            groups.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("population")) {
            groups.stream()
                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reversePopulation")) {
            groups.stream()
                    .sorted(comparingInt(p -> p.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("territory")) {
            groups.stream()
                    .sorted(comparingInt(p -> -p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseTerritory")) {
            groups.stream()
                    .sorted(comparingInt(p -> p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            groups.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            groups.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("nation")) {
            groups.stream()
                    .sorted((p1, p2) -> p1.getNation().getName().compareToIgnoreCase(p2.getNation().getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseNation")) {
            groups.stream()
                    .sorted((p1, p2) -> -p1.getNation().getName().compareToIgnoreCase(p2.getNation().getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private static StaticGuiElement createAllGroupsElement(DiplomacyGroup group, char slot) {
        var shield = group.getShield();

        var plots = group.getChunks().size();
        var label = " plots";
        if (plots == 1) {
            label = " plot";
        }

        return new StaticGuiElement(slot,
                shield,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = GroupGuiFactory.create(group);
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + group.getName(),
                ChatColor.BLUE + "Population: " + ChatColor.GRAY + group.getMembers().size(),
                ChatColor.BLUE + "Territory: " + ChatColor.GRAY + plots + label,
                ChatColor.BLUE + "Nation: " + ChatColor.GRAY + group.getNation().getName(),
                ChatColor.BLUE + "Created On: " + ChatColor.GRAY + group.getDateCreated()
        );
    }

    public static InventoryGui createPlayerGroups(DiplomacyPlayer otherPlayer, String sortType, int slot) {

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Groups";
        String[] guiSetup = {
                "A abcde Z",
                "B fghij  ",
                "C klmno U",
                "G pqrst D",
                "N uvwxy  ",
                "  z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        gui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        var alphabetical = new ItemStack(Material.WHITE_BANNER);
        var alphabeticalMeta = (BannerMeta) (alphabetical.getItemMeta());
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_MIDDLE));
        alphabeticalMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.BORDER));
        alphabeticalMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        alphabetical.setItemMeta(alphabeticalMeta);

        gui.addElement(new StaticGuiElement('A',
                alphabetical,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "alphabet", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "reverseAlphabet", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort Alphabetically",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "population", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "reversePopulation", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Population",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.GRASS_BLOCK),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "territory", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "reverseTerritory", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Territory Size",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('G',
                new ItemStack(Material.CLOCK),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "age", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "reverseAge", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
        ));

        gui.addElement(new StaticGuiElement('N',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "nation", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(otherPlayer, "reverseNation", slot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Nation Name",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "A-Z",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Z-A"
        ));

        var scrollUp = new ItemStack(Material.WHITE_BANNER);
        var scrollUpMeta = (BannerMeta) (scrollUp.getItemMeta());
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollUpMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        scrollUpMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollUpMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollUp.setItemMeta(scrollUpMeta);

        gui.addElement(new StaticGuiElement('U',
                scrollUp,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createPlayerGroups(otherPlayer, sortType, nSlot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createPlayerGroups(otherPlayer, sortType, nSlot);
                        InventoryGui.clearHistory(clicker);
                        nGui.show(clicker, true);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Up",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll up one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll up six lines"
        ));

        var scrollDown = new ItemStack(Material.WHITE_BANNER);
        var scrollDownMeta = (BannerMeta) (scrollDown.getItemMeta());
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_LEFT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.STRIPE_RIGHT));
        scrollDownMeta.addPattern(new Pattern(DyeColor.WHITE, PatternType.STRIPE_BOTTOM));
        scrollDownMeta.addPattern(new Pattern(DyeColor.BLACK, PatternType.CURLY_BORDER));
        scrollDownMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        scrollDown.setItemMeta(scrollDownMeta);

        gui.addElement(new StaticGuiElement('D',
                scrollDown,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot + 5;
                        if (nSlot > DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30) {
                            nSlot = DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30;
                        }
                        if (DiplomacyGroups.getInstance().getGroups().size() > 30) {
                            var nGui = createPlayerGroups(otherPlayer, sortType, nSlot);
                            InventoryGui.clearHistory(clicker);
                            nGui.show(clicker, true);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30) {
                            nSlot = DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30;
                        }
                        if (DiplomacyGroups.getInstance().getGroups().size() > 30) {
                            var nGui = createPlayerGroups(otherPlayer, sortType, nSlot);
                            InventoryGui.clearHistory(clicker);
                            nGui.show(clicker, true);
                        }
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Down",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll down one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll down six lines"
        ));

        var playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (playerHead.getItemMeta());
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(otherPlayer.getUUID()));
        playerHead.setItemMeta(skullMeta);

        gui.addElement(new StaticGuiElement('Z',
                playerHead,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createPlayer(Bukkit.getOfflinePlayer(otherPlayer.getUUID()));
                    InventoryGui.clearHistory(clicker);
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + Bukkit.getOfflinePlayer(otherPlayer.getUUID()).getName(),
                ChatColor.GRAY + "Click to go to player info"
        ));

        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));

        var groups = new ArrayList<DiplomacyGroup>();
        for (var group : DiplomacyGroups.getInstance().getGroups()) {
            if (group.getMembers().contains(otherPlayer)) {
                groups.add(group);
            }
        }


        var slotChar = new char[]{'a'};

        switch (sortType) {
            case "alphabet" -> groups.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reverseAlphabet" -> groups.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "population" -> groups.stream()
                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reversePopulation" -> groups.stream()
                    .sorted(comparingInt(p -> p.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "territory" -> groups.stream()
                    .sorted(comparingInt(p -> -p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reverseTerritory" -> groups.stream()
                    .sorted(comparingInt(p -> p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "age" -> groups.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reverseAge" -> groups.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "nation" -> groups.stream()
                    .sorted((p1, p2) -> p1.getNation().getName().compareToIgnoreCase(p2.getNation().getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reverseNation" -> groups.stream()
                    .sorted((p1, p2) -> -p1.getNation().getName().compareToIgnoreCase(p2.getNation().getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(group, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }
}
