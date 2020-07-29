package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.*;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;

public class NationGuiFactory {

    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public InventoryGui create(Nation nation, Player player) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        var color = ChatColor.BLUE;
        if (playerNation != null) {
            if (nation.getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(nation, playerNation)) {
                color = ChatColor.GREEN;
            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                color = ChatColor.RED;
            }
        }
        var title = ChatColor.BOLD + "Nation: " + color + ChatColor.BOLD + nation.getName();
        String[] guiSetup = {
                "         ",
                " abcdefg ",
                " hijklmn ",
                "         ",
                " o pqr s ",
                "    t    "
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

        if (Objects.equals(nation, playerNation)) {
            var nationClass = nation.getMemberClass(diplomacyPlayer);
            var permissions = nationClass.getPermissions();


            if (permissions.get("CanRenameNation")) {
                gui.addElement(new StaticGuiElement('a',
                        new ItemStack(Material.NAME_TAG),
                        click -> true,
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Name:",
                        ChatColor.GRAY + nation.getName(),
                        " ",//TODO add click command
                        ChatColor.BLUE + "Change Name: " + ChatColor.GRAY + "/nation rename <name>"

                ));
            } else {
                gui.addElement(new StaticGuiElement('a',
                        new ItemStack(Material.NAME_TAG),
                        click -> true,
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Name:",
                        ChatColor.GRAY + nation.getName()
                ));
            }
            if (permissions.get("CanChangeBanner")) {
                gui.addElement(new StaticGuiElement('b',
                        nation.getBanner(),
                        click -> true,
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Banner:",
                        " ",//TODO add click command
                        ChatColor.BLUE + "Change Banner: " + ChatColor.GRAY + "/nation banner"
                ));
            } else {
                gui.addElement(new StaticGuiElement('b',
                        nation.getBanner(),
                        click -> true,
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Banner:"
                ));
            }

            gui.addElement(new StaticGuiElement('c',
                    new ItemStack(Material.PLAYER_HEAD),
                    click -> {
                        var nGui = createMembers(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view members"
            ));

            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.SKELETON_SKULL),
                    click -> {
                        var nGui = createOutlaws(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Outlaws",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view outlaws"
            ));

            var nationWealth = nation.getBalance();

            var strNationWealth = "\u00A40" + formatter.format(nationWealth);
            if (nationWealth >= 1.0) {
                strNationWealth = "\u00A4" + formatter.format(nationWealth);
            }
            if (permissions.get("CanDeposit")) {
                if (permissions.get("CanWithdraw")) {
                    gui.addElement(new StaticGuiElement('e',
                            new ItemStack(Material.DIAMOND),
                            click -> true,
                            "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                            ChatColor.BLUE + "Balance: " + ChatColor.GRAY + strNationWealth,
                            " ",//TODO add click command
                            ChatColor.BLUE + "Deposit: " + ChatColor.GRAY + "/nation deposit <amount>",
                            ChatColor.BLUE + "Withdraw: " + ChatColor.GRAY + "/nation withdraw <amount>"

                    ));
                } else {
                    gui.addElement(new StaticGuiElement('e',
                            new ItemStack(Material.DIAMOND),
                            click -> true,
                            "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                            ChatColor.BLUE + "Balance: " + ChatColor.GRAY + strNationWealth,
                            " ",//TODO add click command
                            ChatColor.BLUE + "Deposit: " + ChatColor.GRAY + "/nation deposit <amount>"

                    ));
                }
            } else {
                gui.addElement(new StaticGuiElement('e',
                        new ItemStack(Material.DIAMOND),
                        click -> true,
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                        ChatColor.BLUE + "Balance: " + ChatColor.GRAY + strNationWealth
                ));
            }
            gui.addElement(new StaticGuiElement('f',
                    new ItemStack(Material.SHIELD),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Groups",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view groups"
            ));
            gui.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.NETHER_STAR),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Classes",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view classes"
            ));

            var founderItem = new ItemStack(Material.PLAYER_HEAD, 1);
            var skullMeta = (SkullMeta) (founderItem.getItemMeta());
            var founder = Bukkit.getOfflinePlayer(UUID.fromString(nation.getFounder()));
            Objects.requireNonNull(skullMeta).setOwningPlayer(founder);
            founderItem.setItemMeta(skullMeta);

            gui.addElement(new StaticGuiElement('h',
                    founderItem,
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Founder",
                    ChatColor.GRAY + founder.getName()
            ));
            gui.addElement(new StaticGuiElement('i',
                    new ItemStack(Material.CLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Date Founded:",
                    ChatColor.GRAY + nation.getDateCreated()
            ));
            gui.addElement(new StaticGuiElement('j',
                    new ItemStack(Material.WHITE_WOOL),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Dynmap Color:",
                    ChatColor.GRAY + "to be added"//TODO add
            ));
            String status;
            if (nation.getIsOpen()) {
                status = "Open";
            } else {
                status = "Closed";
            }
            if (permissions.get("CanToggleBorder")) {
                gui.addElement(new StaticGuiElement('k',
                        new ItemStack(Material.OAK_FENCE),
                        click -> {
                            if ((nation.getIsOpen() && status.equals("Open")) || (!nation.getIsOpen() && status.equals("Closed"))) {
                                nation.setIsOpen(!nation.getIsOpen());
                            }
                            var nGui = create(nation, player);
                            nGui.show(player);
                            return true;
                        },
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Border Status:",
                        ChatColor.GRAY + status,
                        " ",
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Toggle Status"
                ));
            } else {
                gui.addElement(new StaticGuiElement('k',
                        new ItemStack(Material.OAK_FENCE),
                        click -> true,
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Border Status:",
                        ChatColor.GRAY + status
                ));
            }

            var chunks = nation.getChunks().size();
            var label = " chunks";
            if (chunks == 1) {
                label = " chunk";
            }
            gui.addElement(new StaticGuiElement('l',
                    new ItemStack(Material.GRASS_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Territory Size",
                    ChatColor.GRAY + String.valueOf(chunks) + label
            ));
            gui.addElement(new StaticGuiElement('m',
                    new ItemStack(Material.LIME_BANNER),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Ally Nations",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View ally nations"
            ));
            gui.addElement(new StaticGuiElement('n',
                    new ItemStack(Material.RED_BANNER),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Enemy Nations",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View enemy nations"
            ));
            gui.addElement(new StaticGuiElement('o',
                    new ItemStack(Material.EMERALD),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Rankings",//TODO add
                    ChatColor.BLUE + "Power Rating:",
                    ChatColor.BLUE + "Population:",
                    ChatColor.BLUE + "Territory Size:",
                    ChatColor.BLUE + "Balance:",
                    ChatColor.BLUE + "Age:"

            ));
            gui.addElement(new StaticGuiElement('p',
                    new ItemStack(Material.BLUE_BANNER),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Nations",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all nations"
            ));
            gui.addElement(new StaticGuiElement('q',
                    new ItemStack(Material.PLAYER_HEAD),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players"
            ));

            var map = new ItemStack(Material.FILLED_MAP);
            var mapMeta = (MapMeta) map.getItemMeta();
            mapMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            map.setItemMeta(mapMeta);

            gui.addElement(new StaticGuiElement('r',
                    map,
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "World Map",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View world map"
            ));
            if (permissions.get("CanManageGuards")) {
                gui.addElement(new StaticGuiElement('s',
                        new ItemStack(Material.ZOMBIE_HEAD),
                        click -> true,
                        "" + ChatColor.YELLOW + ChatColor.BOLD + "Guards",//TODO add
                        ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View guards"
                ));
            }
            gui.addElement(new StaticGuiElement('t',
                    new ItemStack(Material.BARRIER),
                    click -> {
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                    ChatColor.GRAY + "Click to escape"//TODO add
            ));
        } else {
            gui.addElement(new StaticGuiElement('a',
                    new ItemStack(Material.NAME_TAG),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Name:",
                    ChatColor.GRAY + nation.getName()
            ));
            gui.addElement(new StaticGuiElement('b',
                    nation.getBanner(),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Banner:"
            ));
            gui.addElement(new StaticGuiElement('c',
                    new ItemStack(Material.PLAYER_HEAD),
                    click -> {
                        var nGui = createMembers(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view members"
            ));
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.SKELETON_SKULL),
                    click -> {
                        var nGui = createOutlaws(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Outlaws",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view outlaws"
            ));

            var nationWealth = nation.getBalance();

            var strNationWealth = "\u00A40" + formatter.format(nationWealth);
            if (nationWealth >= 1.0) {
                strNationWealth = "\u00A4" + formatter.format(nationWealth);
            }

            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.DIAMOND),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                    ChatColor.BLUE + "Balance: " + ChatColor.GRAY + strNationWealth

            ));
            gui.addElement(new StaticGuiElement('f',
                    new ItemStack(Material.SHIELD),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Groups",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view groups"
            ));
            gui.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.NETHER_STAR),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Classes",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view classes"
            ));

            var founderItem = new ItemStack(Material.PLAYER_HEAD, 1);
            var skullMeta = (SkullMeta) (founderItem.getItemMeta());
            var founder = Bukkit.getOfflinePlayer(UUID.fromString(nation.getFounder()));
            skullMeta.setOwningPlayer(founder);
            founderItem.setItemMeta(skullMeta);

            gui.addElement(new StaticGuiElement('h',
                    founderItem,
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Founder",
                    ChatColor.GRAY + founder.getName()
            ));
            gui.addElement(new StaticGuiElement('i',
                    new ItemStack(Material.CLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Date Founded:",
                    ChatColor.GRAY + nation.getDateCreated()
            ));
            gui.addElement(new StaticGuiElement('j',
                    new ItemStack(Material.WHITE_WOOL),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Dynmap Color:",
                    ChatColor.GRAY + "to be added"//TODO add
            ));
            String status;
            if (nation.getIsOpen()) {
                status = "Open";
            } else {
                status = "Closed";
            }
            gui.addElement(new StaticGuiElement('k',
                    new ItemStack(Material.OAK_FENCE),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Border Status:",
                    ChatColor.GRAY + status
            ));

            var chunks = nation.getChunks().size();
            var label = " chunks";
            if (chunks == 1) {
                label = " chunk";
            }
            gui.addElement(new StaticGuiElement('l',
                    new ItemStack(Material.GRASS_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Territory Size",
                    ChatColor.GRAY + String.valueOf(chunks) + label
            ));
            gui.addElement(new StaticGuiElement('m',
                    new ItemStack(Material.LIME_BANNER),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Ally Nations",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View ally nations"
            ));
            gui.addElement(new StaticGuiElement('n',
                    new ItemStack(Material.RED_BANNER),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Enemy Nations",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View enemy nations"
            ));
            gui.addElement(new StaticGuiElement('o',
                    new ItemStack(Material.EMERALD),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Rankings",//TODO add
                    ChatColor.BLUE + "Power Rating:",
                    ChatColor.BLUE + "Population:",
                    ChatColor.BLUE + "Territory Size:",
                    ChatColor.BLUE + "Balance:",
                    ChatColor.BLUE + "Age:"
            ));
            gui.addElement(new StaticGuiElement('p',
                    new ItemStack(Material.BLUE_BANNER),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Nations",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all nations"
            ));
            gui.addElement(new StaticGuiElement('q',
                    new ItemStack(Material.PLAYER_HEAD),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players"
            ));

            var map = new ItemStack(Material.FILLED_MAP);
            var mapMeta = (MapMeta) map.getItemMeta();
            mapMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            map.setItemMeta(mapMeta);

            gui.addElement(new StaticGuiElement('r',
                    map,
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "World Map",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View world map"
            ));
            gui.addElement(new StaticGuiElement('t',
                    new ItemStack(Material.BARRIER),
                    click -> {
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                    ChatColor.GRAY + "Click to escape"
            ));
        }
        return gui;
    }

    public InventoryGui createMembers(Nation nation, Player player, String sortType, int slot) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        var color = ChatColor.BLUE;
        if (playerNation != null) {
            if (nation.getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(nation, playerNation)) {
                color = ChatColor.GREEN;
            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                color = ChatColor.RED;
            }
        }
        var title = ChatColor.BOLD + "Nation: " + color + ChatColor.BOLD + nation.getName();
        String[] guiSetup = {
                "  abcde S",
                "A fghij  ",
                "B klmno U",
                "C pqrst D",
                "  uvwxy  ",
                "N z{|}~ E"
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
                        var nGui = createMembers(nation, player, "alphabet", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(nation, player, "reverseAlphabet", slot);
                        nGui.show(player);
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(nation, player, "reverseBalance", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "By Balance",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.NETHER_STAR),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createMembers(nation, player, "class", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(nation, player, "reverseClass", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "By Class",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Highest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Lowest first"
        ));
        gui.addElement(new StaticGuiElement('S',
                new ItemStack(Material.COMPASS),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Search",//TODO add
                ChatColor.BLUE + "to be added"
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
                        var nGui = createMembers(nation, player, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createMembers(nation, player, sortType, nSlot);
                        nGui.show(player);
                    }//TODO make sure scrolling works properly
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
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > nation.getMembers().size() + (5 - nation.getMembers().size() % 5) - 30) {
                            nSlot = nation.getMembers().size() + (5 - nation.getMembers().size() % 5) - 30;
                        }
                        if (nation.getMembers().size() > 30) {
                            var nGui = createMembers(nation, player, sortType, nSlot);
                            nGui.show(player);
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
                    var nGui = create(nation, player);
                    nGui.show(player);
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

        for (var memberStr : membersStr) {
            var member = Bukkit.getOfflinePlayer(UUID.fromString(memberStr));
            members.add(member);
        }


        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            members.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            members.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            members.stream()
                    .sorted(comparingDouble(p -> -Diplomacy.getEconomy().getBalance(p)))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            members.stream()
                    .sorted(comparingDouble(p -> Diplomacy.getEconomy().getBalance(p)))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("class")) {
            members.stream()
                    .sorted(comparingInt(p -> -Integer.parseInt(nation.getMemberClass(DiplomacyPlayers.getInstance().get(p.getUniqueId())).getClassID())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseClass")) {
            members.stream()
                    .sorted(comparingInt(p -> Integer.parseInt(nation.getMemberClass(DiplomacyPlayers.getInstance().get(p.getUniqueId())).getClassID())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(nation, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    int index = 0;

    private StaticGuiElement createMemberElement(Nation nation, OfflinePlayer member, char slot) {
        var memberHead = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (memberHead.getItemMeta());
        skullMeta.setOwningPlayer(member);
        memberHead.setItemMeta(skullMeta);

        var diplomacyMember = DiplomacyPlayers.getInstance().get(member.getUniqueId());
        var memberClass = nation.getMemberClass(diplomacyMember).getName();

        return new StaticGuiElement(slot,
                memberHead,
                click -> true,//TODO add player info
                "" + ChatColor.YELLOW + ChatColor.BOLD + member.getName(),
                ChatColor.BLUE + "Class: " + ChatColor.GRAY + memberClass,
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(member)),
                ChatColor.BLUE + "Line: " + ChatColor.GRAY + index++
        );
    }

    public InventoryGui createOutlaws(Nation nation, Player player, String sortType, int slot) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        var color = ChatColor.BLUE;
        if (playerNation != null) {
            if (nation.getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(nation, playerNation)) {
                color = ChatColor.GREEN;
            } else if (nation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                color = ChatColor.RED;
            }
        }
        var title = ChatColor.BOLD + "Nation: " + color + ChatColor.BOLD + nation.getName();
        String[] guiSetup = {
                "  abcde S",
                "A fghij  ",
                "B klmno U",
                "C pqrst D",
                "  uvwxy  ",
                "N z0123 E"
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createOutlaws(nation, player, "reverseAlphabet", slot);
                        nGui.show(player);
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createOutlaws(nation, player, "reverseBalance", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "By Balance",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.NETHER_STAR),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createOutlaws(nation, player, "class", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createOutlaws(nation, player, "reverseClass", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "By Class",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Highest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Lowest first"
        ));
        gui.addElement(new StaticGuiElement('S',
                new ItemStack(Material.COMPASS),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Search",//TODO add
                ChatColor.BLUE + "to be added"
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createOutlaws(nation, player, sortType, nSlot);
                        nGui.show(player);
                    }//TODO make sure scrolling works properly
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
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > nation.getOutlaws().size() + (5 - nation.getOutlaws().size() % 5) - 30) {
                            nSlot = nation.getOutlaws().size() + (5 - nation.getOutlaws().size() % 5) - 30;
                        }
                        if (nation.getOutlaws().size() > 30) {
                            var nGui = createOutlaws(nation, player, sortType, nSlot);
                            nGui.show(player);
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
                    var nGui = create(nation, player);
                    nGui.show(player);
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
                        var element = createOutlawElement(nation, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            outlaws.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(skipped)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createMemberElement(nation, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            outlaws.stream()
                    .sorted(comparingDouble(p -> -Diplomacy.getEconomy().getBalance(p)))
                    .skip(skipped)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createMemberElement(nation, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            outlaws.stream()
                    .sorted(comparingDouble(p -> Diplomacy.getEconomy().getBalance(p)))
                    .skip(skipped)
                    .limit(30)
                    .forEach(outlaw -> {
                        var element = createOutlawElement(nation, outlaw, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("class")) {
            outlaws.stream()
                    .sorted(Comparator.nullsLast(comparingInt(p -> -Integer.parseInt(nation.getMemberClass(DiplomacyPlayers.getInstance().get(p.getUniqueId())).getClassID()))))
                    .skip(skipped)
                    .limit(30)
                    .forEach(member -> {
                        var element = createOutlawElement(nation, member, charSlot[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseClass")) {
            outlaws.stream()
                    .sorted(Comparator.nullsLast(comparingInt(p -> Integer.parseInt(nation.getMemberClass(DiplomacyPlayers.getInstance().get(p.getUniqueId())).getClassID()))))
                    .skip(skipped)
                    .limit(30)
                    .forEach(member -> {
                        var element = createOutlawElement(nation, member, charSlot[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private StaticGuiElement createOutlawElement(Nation nation, OfflinePlayer outlaw, char slot) {
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
                click -> true,//TODO add player info
                "" + ChatColor.YELLOW + ChatColor.BOLD + outlaw.getName(),
                ChatColor.BLUE + "Nation: " + color + strOutlawNation,
                ChatColor.BLUE + "Class: " + ChatColor.GRAY + strClass,
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + formatter.format(Diplomacy.getEconomy().getBalance(outlaw))
        );
    }
}
