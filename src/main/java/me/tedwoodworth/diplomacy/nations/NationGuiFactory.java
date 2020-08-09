package me.tedwoodworth.diplomacy.nations;

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
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;

public class NationGuiFactory {

    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");


    public static InventoryGui createMenu(Player player) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Menu";
        String[] guiSetup = {
                "         ",
                " abcdefg ",
                "        E",
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        gui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        gui.addElement(new StaticGuiElement('a',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    var nGui = createNations(player, "alphabet", 0);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Nations",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all nations"
        ));
        gui.addElement(new StaticGuiElement('b',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var nGui = createPlayers(player, "alphabet", 0);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players"
        ));
        gui.addElement(new StaticGuiElement('c',
                new ItemStack(Material.SHIELD),
                click -> {
                    var nGui = createAllGroups(player, "alphabet", 0);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Groups",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all groups"
        ));
        var map = new ItemStack(Material.FILLED_MAP);
        var mapMeta = (MapMeta) map.getItemMeta();
        mapMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        map.setItemMeta(mapMeta);

        gui.addElement(new StaticGuiElement('d',
                map,
                click -> true,//TODO add
                "" + ChatColor.YELLOW + ChatColor.BOLD + "World Dynmap",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View world dynmap"
        ));

        if (playerNation != null) {
            var banner = playerNation.getBanner();
            var bannerMeta = (BannerMeta) banner.getItemMeta();
            bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            banner.setItemMeta(bannerMeta);
            gui.addElement(new StaticGuiElement('e',
                    banner,
                    click -> {
                        var nGui = create(playerNation, player);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Your Nation (" + playerNation.getName() + ")",
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
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
        head.setItemMeta(meta);

        gui.addElement(new StaticGuiElement('f',
                head,
                click -> {
                    var nGui = createPlayer(player, Bukkit.getOfflinePlayer(player.getUniqueId()));
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + player.getName() + " (You)",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View player info"
        ));

        gui.addElement(new StaticGuiElement('g',
                new ItemStack(Material.SHIELD),
                click -> {
                    var nGui = createPlayerGroups(player, diplomacyPlayer, "alphabet", 0);
                    nGui.show(player);
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

    public static InventoryGui createPlayer(Player player, OfflinePlayer otherPlayer) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var otherDiplomacyPlayer = DiplomacyPlayers.getInstance().get(otherPlayer.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        var otherPlayerNation = Nations.getInstance().get(otherDiplomacyPlayer);

        var color = ChatColor.BLUE;
        var glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        if (otherPlayerNation == null) {
            color = ChatColor.DARK_GRAY;
            glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        } else {
            if (playerNation != null) {
                if (otherPlayerNation.getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(otherPlayerNation, playerNation)) {
                    color = ChatColor.DARK_GREEN;
                    glass = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
                } else if (otherPlayerNation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                    color = ChatColor.RED;
                    glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
                }
            }
        }

        var title = "" + color + ChatColor.BOLD + otherPlayer.getName();
        String[] guiSetup = {
                "        g",
                " abcdef  ",
                "        h"
        };

        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        gui.setFiller(glass);

        var head = new ItemStack(Material.PLAYER_HEAD);
        var headMeta = (SkullMeta) head.getItemMeta();
        headMeta.setOwningPlayer(otherPlayer);
        head.setItemMeta(headMeta);

        gui.addElement(new StaticGuiElement('a',
                head,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + otherPlayer.getName()
        ));

        if (otherPlayerNation != null) {

            var banner = otherPlayerNation.getBanner();
            var bannerMeta = (BannerMeta) banner.getItemMeta();
            bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            banner.setItemMeta(bannerMeta);
            gui.addElement(new StaticGuiElement('b',
                    banner,
                    click -> {
                        var nGui = create(otherPlayerNation, player);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation",
                    ChatColor.GRAY + otherPlayerNation.getName(),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View nation"
            ));
        } else {
            if (Objects.equals(Bukkit.getOfflinePlayer(player.getUniqueId()), otherPlayer)) {
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
                    var nGui = createPlayerGroups(player, otherDiplomacyPlayer, "alphabet", 0);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Groups",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View groups"
        ));


        if (Objects.equals(Bukkit.getOfflinePlayer(player.getUniqueId()), otherPlayer)) {
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.DIAMOND),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                    ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(otherPlayer)),
                    " ",
                    ChatColor.BLUE + "Deposit: " + ChatColor.GRAY + "/deposit <amount>",
                    ChatColor.BLUE + "Withdraw: " + ChatColor.GRAY + "/withdraw <amount>"
            ));
        } else {
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.DIAMOND),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                    ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(otherPlayer))
            ));
        }

        if (otherPlayerNation != null) {
            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.NETHER_STAR),
                    click -> true,//TODO view permissions
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Class",
                    ChatColor.GRAY + otherPlayerNation.getMemberClass(otherDiplomacyPlayer).getName(),
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
                ChatColor.GRAY + otherDiplomacyPlayer.getDateJoined()
        ));

        gui.addElement(new StaticGuiElement('g',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var nGui = createPlayers(player, "alphabet", 0);
                    nGui.show(player);
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

    public static InventoryGui create(Nation nation, Player player) {
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
        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Main";
        String[] guiSetup = {
                "        z",
                "   bai  p",
                "  oeumn q",
                "  cdsgf r",
                "   klj   ",
                "    h   t"
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
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view members"
            ));

            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.SKELETON_SKULL),
                    click -> {
                        var nGui = createOutlaws(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Outlaws",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view outlaws"
            ));

            gui.addElement(new StaticGuiElement('z',
                    new ItemStack(Material.PAINTING),
                    click -> {
                        var nGui = createMenu(player);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Menu",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Go to menu"
            ));

            var nationWealth = nation.getBalance();

            var strNationWealth = "\u00A4" + formatter.format(nationWealth);
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
                    click -> {
                        var nGui = createGroups(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Groups",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View groups"
            ));
            gui.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.NETHER_STAR),
                    click -> {
                        var nGui = ClassGuiFactory.create(nation, player);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Classes",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View classes"
            ));

            var founderItem = new ItemStack(Material.PLAYER_HEAD, 1);
            var skullMeta = (SkullMeta) (founderItem.getItemMeta());
            var founder = Bukkit.getOfflinePlayer(UUID.fromString(nation.getFounder()));
            Objects.requireNonNull(skullMeta).setOwningPlayer(founder);
            founderItem.setItemMeta(skullMeta);

            gui.addElement(new StaticGuiElement('h',
                    founderItem,
                    click -> {
                        var nGui = createPlayer(player, founder);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Founder",
                    ChatColor.GRAY + founder.getName(),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View player info"
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

            var plots = nation.getChunks().size();
            var label = " plots";
            if (plots == 1) {
                label = " plot";
            }
            gui.addElement(new StaticGuiElement('l',
                    new ItemStack(Material.GRASS_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Territory Size",
                    ChatColor.GRAY + String.valueOf(plots) + label
            ));
            gui.addElement(new StaticGuiElement('m',
                    new ItemStack(Material.LIME_BANNER),
                    click -> {
                        var nGui = createAllyNations(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Ally Nations",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View ally nations"
            ));
            gui.addElement(new StaticGuiElement('n',
                    new ItemStack(Material.RED_BANNER),
                    click -> {
                        var nGui = createEnemyNations(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Enemy Nations",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View enemy nations"
            ));
            gui.addElement(new StaticGuiElement('o',
                    new ItemStack(Material.GOLD_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Rankings",
                    ChatColor.BLUE + "Power: " + ChatColor.GRAY + "#" + Nations.getInstance().getPowerRank(nation),
                    ChatColor.BLUE + "Population: " + ChatColor.GRAY + "#" + Nations.getInstance().getPopulationRank(nation),
                    ChatColor.BLUE + "Territory Size: " + ChatColor.GRAY + "#" + Nations.getInstance().getTerritoryRank(nation),
                    ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "#" + Nations.getInstance().getBalanceRank(nation),
                    ChatColor.BLUE + "Age: " + ChatColor.GRAY + "#" + Nations.getInstance().getAgeRank(nation)
            ));
            gui.addElement(new StaticGuiElement('p',
                    new ItemStack(Material.BLUE_BANNER),
                    click -> {
                        var nGui = createNations(player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Nations",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all nations"
            ));
            gui.addElement(new StaticGuiElement('q',
                    new ItemStack(Material.PLAYER_HEAD),
                    click -> {
                        var nGui = createPlayers(player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players"
            ));

            gui.addElement(new StaticGuiElement('r',
                    new ItemStack(Material.SHIELD),
                    click -> {
                        var nGui = createAllGroups(player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Groups",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all groups"
            ));
            gui.addElement(new StaticGuiElement('u',
                    new ItemStack(Material.EMERALD),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Power",
                    ChatColor.GRAY + formatter.format(100 * nation.getPower()) + "%"
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
                    ChatColor.GRAY + "Click to escape"
            ));
        } else {
            gui.addElement(new StaticGuiElement('z',
                    new ItemStack(Material.PAINTING),
                    click -> {
                        var nGui = createMenu(player);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Menu",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Go to menu"
            ));
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
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View members"
            ));
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.SKELETON_SKULL),
                    click -> {
                        var nGui = createOutlaws(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Outlaws",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View outlaws"
            ));

            var nationWealth = nation.getBalance();

            var strNationWealth = "\u00A4" + formatter.format(nationWealth);

            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.DIAMOND),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                    ChatColor.BLUE + "Balance: " + ChatColor.GRAY + strNationWealth

            ));
            gui.addElement(new StaticGuiElement('f',
                    new ItemStack(Material.SHIELD),
                    click -> {
                        var nGui = createGroups(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Groups",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View groups"
            ));
            gui.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.NETHER_STAR),
                    click -> {
                        var nGui = ClassGuiFactory.create(nation, player);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Classes",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View classes"
            ));

            var founderItem = new ItemStack(Material.PLAYER_HEAD, 1);
            var skullMeta = (SkullMeta) (founderItem.getItemMeta());
            var founder = Bukkit.getOfflinePlayer(UUID.fromString(nation.getFounder()));
            skullMeta.setOwningPlayer(founder);
            founderItem.setItemMeta(skullMeta);

            gui.addElement(new StaticGuiElement('h',
                    founderItem,
                    click -> {
                        var nGui = createPlayer(player, founder);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Founder",
                    ChatColor.GRAY + founder.getName(),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View player info"
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

            var plots = nation.getChunks().size();
            var label = " plots";
            if (plots == 1) {
                label = " plot";
            }
            gui.addElement(new StaticGuiElement('l',
                    new ItemStack(Material.GRASS_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Territory Size",
                    ChatColor.GRAY + String.valueOf(plots) + label
            ));
            gui.addElement(new StaticGuiElement('m',
                    new ItemStack(Material.LIME_BANNER),
                    click -> {
                        var nGui = createAllyNations(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Ally Nations",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View ally nations"
            ));
            gui.addElement(new StaticGuiElement('n',
                    new ItemStack(Material.RED_BANNER),
                    click -> {
                        var nGui = createEnemyNations(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Enemy Nations",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View enemy nations"
            ));
            gui.addElement(new StaticGuiElement('o',
                    new ItemStack(Material.GOLD_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Rankings",
                    ChatColor.BLUE + "Power: " + ChatColor.GRAY + "#" + Nations.getInstance().getPowerRank(nation),
                    ChatColor.BLUE + "Population: " + ChatColor.GRAY + "#" + Nations.getInstance().getPopulationRank(nation),
                    ChatColor.BLUE + "Territory Size: " + ChatColor.GRAY + "#" + Nations.getInstance().getTerritoryRank(nation),
                    ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "#" + Nations.getInstance().getBalanceRank(nation),
                    ChatColor.BLUE + "Age: " + ChatColor.GRAY + "#" + Nations.getInstance().getAgeRank(nation)
            ));
            gui.addElement(new StaticGuiElement('p',
                    new ItemStack(Material.BLUE_BANNER),
                    click -> {
                        var nGui = createNations(player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Nations",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all nations"
            ));
            gui.addElement(new StaticGuiElement('q',
                    new ItemStack(Material.PLAYER_HEAD),
                    click -> {
                        var nGui = createPlayers(player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players"
            ));
            gui.addElement(new StaticGuiElement('u',
                    new ItemStack(Material.EMERALD),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Power",
                    ChatColor.GRAY + formatter.format(100 * nation.getPower()) + "%"
            ));
            gui.addElement(new StaticGuiElement('r',
                    new ItemStack(Material.SHIELD),
                    click -> {
                        var nGui = createAllGroups(player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "All Groups",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all groups"
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
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Balance",
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
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Class",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Highest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Lowest first"
        ));
        gui.addElement(new StaticGuiElement('S',
                new ItemStack(Material.CLOCK),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createMembers(nation, player, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(nation, player, "reverseAge", slot);
                        nGui.show(player);
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
                        var nGui = createMembers(nation, player, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createMembers(nation, player, sortType, nSlot);
                        nGui.show(player);
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
                    var nGui = createPlayer(player, member);
                    nGui.show(player);
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
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Balance",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.CLOCK),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createOutlaws(nation, player, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createOutlaws(nation, player, "reverseAge", slot);
                        nGui.show(player);
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createOutlaws(nation, player, sortType, nSlot);
                        nGui.show(player);
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
        gui.addElement(new StaticGuiElement('G',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createOutlaws(nation, player, "nation", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createOutlaws(nation, player, "reverseNation", slot);
                        nGui.show(player);
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
                    var nGui = createPlayer(player, outlaw);
                    nGui.show(player);
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createGroups(nation, player, "reverseAlphabet", slot);
                        nGui.show(player);
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createGroups(nation, player, "reversePopulation", slot);
                        nGui.show(player);
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createGroups(nation, player, "reverseTerritory", slot);
                        nGui.show(player);
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createGroups(nation, player, "reverseAge", slot);
                        nGui.show(player);
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
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createGroups(nation, player, sortType, nSlot);
                        nGui.show(player);
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
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > nation.getGroups().size() + (5 - nation.getGroups().size() % 5) - 30) {
                            nSlot = nation.getGroups().size() + (5 - nation.getGroups().size() % 5) - 30;
                        }
                        if (nation.getGroups().size() > 30) {
                            var nGui = createGroups(nation, player, sortType, nSlot);
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
                    var nGui = GroupGuiFactory.create(group, player);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + group.getName(),
                ChatColor.BLUE + "Population: " + ChatColor.GRAY + group.getMembers().size(),
                ChatColor.BLUE + "Territory: " + ChatColor.GRAY + plots + label,
                ChatColor.BLUE + "Created On: " + ChatColor.GRAY + group.getDateCreated()
        );
    }

    public static InventoryGui createAllyNations(Nation nation, Player player, String sortType, int slot) {
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
        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Allies";
        String[] guiSetup = {
                "A abcde N",
                "P fghij  ",
                "L klmno U",
                "B pqrst D",
                "T uvwxy  ",
                "G z{|}~ E"
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
                        var nGui = createAllyNations(nation, player, "alphabet", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllyNations(nation, player, "reverseAlphabet", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllyNations(nation, player, "population", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllyNations(nation, player, "reversePopulation", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllyNations(nation, player, "territory", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllyNations(nation, player, "reverseTerritory", slot);
                        nGui.show(player);
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
                        var nGui = createAllyNations(nation, player, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllyNations(nation, player, "reverseAge", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
        ));
        gui.addElement(new StaticGuiElement('P',
                new ItemStack(Material.EMERALD),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllyNations(nation, player, "power", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllyNations(nation, player, "reversePower", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Power",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Strongest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Weakest First"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.DIAMOND),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllyNations(nation, player, "balance", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllyNations(nation, player, "reverseBalance", slot);
                        nGui.show(player);
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

        gui.addElement(new StaticGuiElement('U',
                scrollUp,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createAllyNations(nation, player, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createAllyNations(nation, player, sortType, nSlot);
                        nGui.show(player);
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
                        if (nSlot > nation.getAllyNationIDs().size() + (5 - nation.getAllyNationIDs().size() % 5) - 30) {
                            nSlot = nation.getAllyNationIDs().size() + (5 - nation.getAllyNationIDs().size() % 5) - 30;
                        }
                        if (nation.getAllyNationIDs().size() > 30) {
                            var nGui = createAllyNations(nation, player, sortType, nSlot);
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > nation.getAllyNationIDs().size() + (5 - nation.getAllyNationIDs().size() % 5) - 30) {
                            nSlot = nation.getAllyNationIDs().size() + (5 - nation.getAllyNationIDs().size() % 5) - 30;
                        }
                        if (nation.getAllyNationIDs().size() > 30) {
                            var nGui = createAllyNations(nation, player, sortType, nSlot);
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

        var allyNationIDs = nation.getAllyNationIDs();
        var allyNations = new ArrayList<Nation>();
        for (var id : allyNationIDs) {
            allyNations.add(Nations.getInstance().get(Integer.parseInt(id)));
        }

        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            allyNations.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            allyNations.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("population")) {
            allyNations.stream()
                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reversePopulation")) {
            allyNations.stream()
                    .sorted(comparingInt(p -> p.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("territory")) {
            allyNations.stream()
                    .sorted(comparingInt(p -> -p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseTerritory")) {
            allyNations.stream()
                    .sorted(comparingInt(p -> p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            allyNations.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            allyNations.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            allyNations.stream()
                    .sorted((p1, p2) -> (int) -(100 * (p1.getBalance() - p2.getBalance())))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            allyNations.stream()
                    .sorted((p1, p2) -> (int) (100 * (p1.getBalance() - p2.getBalance())))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("power")) {
            allyNations.stream()
                    .sorted((p1, p2) -> (int) -(10000 * (p1.getPower() - p2.getPower())))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reversePower")) {
            allyNations.stream()
                    .sorted((p1, p2) -> (int) (10000 * (p1.getPower() - p2.getPower())))
                    .skip(slot)
                    .limit(30)
                    .forEach(allyNation -> {
                        var element = createNationElement(allyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    public static InventoryGui createEnemyNations(Nation nation, Player player, String sortType, int slot) {
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
        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Enemies";
        String[] guiSetup = {
                "A abcde N",
                "P fghij  ",
                "L klmno U",
                "B pqrst D",
                "T uvwxy  ",
                "G z{|}~ E"
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
                        var nGui = createEnemyNations(nation, player, "alphabet", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createEnemyNations(nation, player, "reverseAlphabet", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createEnemyNations(nation, player, "population", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createEnemyNations(nation, player, "reversePopulation", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createEnemyNations(nation, player, "territory", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createEnemyNations(nation, player, "reverseTerritory", slot);
                        nGui.show(player);
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
                        var nGui = createEnemyNations(nation, player, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createEnemyNations(nation, player, "reverseAge", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
        ));
        gui.addElement(new StaticGuiElement('P',
                new ItemStack(Material.EMERALD),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createEnemyNations(nation, player, "power", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createEnemyNations(nation, player, "reversePower", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Power",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Strongest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Weakest First"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.DIAMOND),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createEnemyNations(nation, player, "balance", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createEnemyNations(nation, player, "reverseBalance", slot);
                        nGui.show(player);
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

        gui.addElement(new StaticGuiElement('U',
                scrollUp,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createEnemyNations(nation, player, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createEnemyNations(nation, player, sortType, nSlot);
                        nGui.show(player);
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
                        if (nSlot > nation.getEnemyNationIDs().size() + (5 - nation.getEnemyNationIDs().size() % 5) - 30) {
                            nSlot = nation.getEnemyNationIDs().size() + (5 - nation.getEnemyNationIDs().size() % 5) - 30;
                        }
                        if (nation.getEnemyNationIDs().size() > 30) {
                            var nGui = createEnemyNations(nation, player, sortType, nSlot);
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > nation.getEnemyNationIDs().size() + (5 - nation.getEnemyNationIDs().size() % 5) - 30) {
                            nSlot = nation.getEnemyNationIDs().size() + (5 - nation.getEnemyNationIDs().size() % 5) - 30;
                        }
                        if (nation.getEnemyNationIDs().size() > 30) {
                            var nGui = createEnemyNations(nation, player, sortType, nSlot);
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

        var enemyNationIDs = nation.getEnemyNationIDs();
        var enemyNations = new ArrayList<Nation>();
        for (var id : enemyNationIDs) {
            enemyNations.add(Nations.getInstance().get(Integer.parseInt(id)));
        }

        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            enemyNations.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            enemyNations.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("population")) {
            enemyNations.stream()
                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reversePopulation")) {
            enemyNations.stream()
                    .sorted(comparingInt(p -> p.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("territory")) {
            enemyNations.stream()
                    .sorted(comparingInt(p -> -p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseTerritory")) {
            enemyNations.stream()
                    .sorted(comparingInt(p -> p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            enemyNations.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            enemyNations.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            enemyNations.stream()
                    .sorted((p1, p2) -> (int) -(100 * (p1.getBalance() - p2.getBalance())))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            enemyNations.stream()
                    .sorted((p1, p2) -> (int) (100 * (p1.getBalance() - p2.getBalance())))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("power")) {
            enemyNations.stream()
                    .sorted((p1, p2) -> (int) -(10000 * (p1.getPower() - p2.getPower())))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reversePower")) {
            enemyNations.stream()
                    .sorted((p1, p2) -> (int) (10000 * (p1.getPower() - p2.getPower())))
                    .skip(slot)
                    .limit(30)
                    .forEach(enemyNation -> {
                        var element = createNationElement(enemyNation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    public static InventoryGui createNations(Player player, String sortType, int slot) {

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Nations";
        String[] guiSetup = {
                "A abcde Z",
                "P fghij  ",
                "L klmno U",
                "B pqrst D",
                "T uvwxy  ",
                "G z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);

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

        gui.addElement(new StaticGuiElement('Z',
                new ItemStack(Material.PAINTING),
                click -> {
                    var nGui = createMenu(player);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Menu",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Go to menu"
        ));
        gui.addElement(new StaticGuiElement('A',
                alphabetical,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createNations(player, "alphabet", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createNations(player, "reverseAlphabet", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createNations(player, "population", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createNations(player, "reversePopulation", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createNations(player, "territory", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createNations(player, "reverseTerritory", slot);
                        nGui.show(player);
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
                        var nGui = createNations(player, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createNations(player, "reverseAge", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Age",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Oldest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Youngest First"
        ));
        gui.addElement(new StaticGuiElement('P',
                new ItemStack(Material.EMERALD),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createNations(player, "power", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createNations(player, "reversePower", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Power",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Strongest First",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Weakest First"
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.DIAMOND),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createNations(player, "balance", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createNations(player, "reverseBalance", slot);
                        nGui.show(player);
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

        gui.addElement(new StaticGuiElement('U',
                scrollUp,
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createNations(player, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createNations(player, sortType, nSlot);
                        nGui.show(player);
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
                        if (nSlot > Nations.getInstance().getNations().size() + (5 - Nations.getInstance().getNations().size() % 5) - 30) {
                            nSlot = Nations.getInstance().getNations().size() + (5 - Nations.getInstance().getNations().size() % 5) - 30;
                        }
                        if (Nations.getInstance().getNations().size() > 30) {
                            var nGui = createNations(player, sortType, nSlot);
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > Nations.getInstance().getNations().size() + (5 - Nations.getInstance().getNations().size() % 5) - 30) {
                            nSlot = Nations.getInstance().getNations().size() + (5 - Nations.getInstance().getNations().size() % 5) - 30;
                        }
                        if (Nations.getInstance().getNations().size() > 30) {
                            var nGui = createNations(player, sortType, nSlot);
                            nGui.show(player);
                        }
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Down",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll down one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll down six lines"
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

        var nations = Nations.getInstance().getNations();

        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            nations.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            nations.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("population")) {
            nations.stream()
                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reversePopulation")) {
            nations.stream()
                    .sorted(comparingInt(p -> p.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("territory")) {
            nations.stream()
                    .sorted(comparingInt(p -> -p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseTerritory")) {
            nations.stream()
                    .sorted(comparingInt(p -> p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            nations.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            nations.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            nations.stream()
                    .sorted((p1, p2) -> (int) -(100 * (p1.getBalance() - p2.getBalance())))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            nations.stream()
                    .sorted((p1, p2) -> (int) (100 * (p1.getBalance() - p2.getBalance())))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("power")) {
            nations.stream()
                    .sorted((p1, p2) -> (int) -(10000 * (p1.getPower() - p2.getPower())))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reversePower")) {
            nations.stream()
                    .sorted((p1, p2) -> (int) (10000 * (p1.getPower() - p2.getPower())))
                    .skip(slot)
                    .limit(30)
                    .forEach(nation -> {
                        var element = createNationElement(nation, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private static StaticGuiElement createNationElement(Nation nation, Player player, char slot) {
        var banner = nation.getBanner();
        var bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        banner.setItemMeta(bannerMeta);

        var plots = nation.getChunks().size();
        var label = " plots";
        if (plots == 1) {
            label = " plot";
        }

        return new StaticGuiElement(slot,
                banner,
                click -> {
                    var nGui = create(nation, player);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + nation.getName(),
                ChatColor.BLUE + "Power: " + ChatColor.GRAY + formatter.format(nation.getPower() * 100) + "%",
                ChatColor.BLUE + "Population: " + ChatColor.GRAY + nation.getMembers().size(),
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "\u00A4" + formatter.format(nation.getBalance()),
                ChatColor.BLUE + "Territory: " + ChatColor.GRAY + plots + label,
                ChatColor.BLUE + "Created On: " + ChatColor.GRAY + nation.getDateCreated()
        );
    }

    public static InventoryGui createPlayers(Player player, String sortType, int slot) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Players";
        String[] guiSetup = {
                "A abcde N",
                "B fghij  ",
                "C klmno U",
                "G pqrst D",
                "  uvwxy  ",
                "  z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayers(player, "alphabet", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayers(player, "reverseAlphabet", slot);
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
                        var nGui = createPlayers(player, "balance", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayers(player, "reverseBalance", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayers(player, "nation", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayers(player, "reverseNation", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayers(player, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayers(player, "reverseAge", slot);
                        nGui.show(player);
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
                        var nGui = createPlayers(player, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createPlayers(player, sortType, nSlot);
                        nGui.show(player);
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
                        if (nSlot > DiplomacyPlayers.getInstance().getPlayers().size() + (5 - DiplomacyPlayers.getInstance().getPlayers().size() % 5) - 30) {
                            nSlot = DiplomacyPlayers.getInstance().getPlayers().size() + (5 - DiplomacyPlayers.getInstance().getPlayers().size() % 5) - 30;
                        }
                        if (DiplomacyPlayers.getInstance().getPlayers().size() > 30) {
                            var nGui = createPlayers(player, sortType, nSlot);
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > DiplomacyPlayers.getInstance().getPlayers().size() + (5 - DiplomacyPlayers.getInstance().getPlayers().size() % 5) - 30) {
                            nSlot = DiplomacyPlayers.getInstance().getPlayers().size() + (5 - DiplomacyPlayers.getInstance().getPlayers().size() % 5) - 30;
                        }
                        if (DiplomacyPlayers.getInstance().getPlayers().size() > 30) {
                            var nGui = createPlayers(player, sortType, nSlot);
                            nGui.show(player);
                        }
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Scroll Down",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Scroll down one line",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Scroll down six lines"
        ));

        gui.addElement(new StaticGuiElement('N',
                new ItemStack(Material.PAINTING),
                click -> {
                    var nGui = createMenu(player);
                    nGui.show(player);
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

        var players = DiplomacyPlayers.getInstance().getPlayers();

        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            players.stream()
                    .sorted((p1, p2) -> Objects.requireNonNull(Bukkit.getOfflinePlayer(p1.getUUID()).getName()).compareToIgnoreCase(Objects.requireNonNull(Bukkit.getOfflinePlayer(p2.getUUID()).getName())))
                    .skip(slot)
                    .limit(30)
                    .forEach(testPlayer -> {
                        var element = createPlayerElement(testPlayer, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            players.stream()
                    .sorted((p1, p2) -> -Objects.requireNonNull(Bukkit.getOfflinePlayer(p1.getUUID()).getName()).compareToIgnoreCase(Objects.requireNonNull(Bukkit.getOfflinePlayer(p2.getUUID()).getName())))
                    .skip(slot)
                    .limit(30)
                    .forEach(testPlayer -> {
                        var element = createPlayerElement(testPlayer, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            players.stream()
                    .sorted(comparingDouble(p -> -Diplomacy.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUUID()))))
                    .skip(slot)
                    .limit(30)
                    .forEach(testPlayer -> {
                        var element = createPlayerElement(testPlayer, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            players.stream()
                    .sorted(comparingDouble(p -> Diplomacy.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUUID()))))
                    .skip(slot)
                    .limit(30)
                    .forEach(testPlayer -> {
                        var element = createPlayerElement(testPlayer, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("nation")) {
            players.stream()
                    .sorted((p1, p2) -> {
                        var p1value = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(p1.getUUID()));
                        var p2value = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(p2.getUUID()));
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
                    .skip(slot)
                    .limit(30)
                    .forEach(testPlayer -> {
                        var element = createPlayerElement(testPlayer, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseNation")) {
            players.stream()
                    .sorted((p1, p2) -> {
                        var p1value = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(p1.getUUID()));
                        var p2value = Nations.getInstance().get(DiplomacyPlayers.getInstance().get(p2.getUUID()));
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
                    .skip(slot)
                    .limit(30)
                    .forEach(testPlayer -> {
                        var element = createPlayerElement(testPlayer, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            players.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(testPlayer -> {
                        var element = createPlayerElement(testPlayer, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            players.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(testPlayer -> {
                        var element = createPlayerElement(testPlayer, player, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private static StaticGuiElement createPlayerElement(DiplomacyPlayer player, Player sender, char slot) {
        var memberHead = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (memberHead.getItemMeta());
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUUID()));
        memberHead.setItemMeta(skullMeta);

        var nation = Nations.getInstance().get(player);
        var offlinePlayer = Bukkit.getOfflinePlayer(player.getUUID());

        var strNation = "None";
        if (nation != null) {
            strNation = nation.getName();
        }

        return new StaticGuiElement(slot,
                memberHead,
                click -> {
                    var nGui = createPlayer(sender, player.getPlayer());
                    nGui.show(sender);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + offlinePlayer.getName(),
                ChatColor.BLUE + "Nation: " + ChatColor.GRAY + strNation,
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(offlinePlayer)),
                ChatColor.BLUE + "Server Join Date: " + ChatColor.GRAY + DiplomacyPlayers.getInstance().get(player.getUUID()).getDateJoined()
        );
    }

    public static InventoryGui createAllGroups(Player player, String sortType, int slot) {

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Groups";
        String[] guiSetup = {
                "A abcde Z",
                "B fghij  ",
                "C klmno U",
                "G pqrst D",
                "N uvwxy  ",
                "  z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllGroups(player, "alphabet", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups(player, "reverseAlphabet", slot);
                        nGui.show(player);
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
                        var nGui = createAllGroups(player, "population", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups(player, "reversePopulation", slot);
                        nGui.show(player);
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
                        var nGui = createAllGroups(player, "territory", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups(player, "reverseTerritory", slot);
                        nGui.show(player);
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
                        var nGui = createAllGroups(player, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups(player, "reverseAge", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createAllGroups(player, "nation", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createAllGroups(player, "reverseNation", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createAllGroups(player, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createAllGroups(player, sortType, nSlot);
                        nGui.show(player);
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
                        if (nSlot > DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30) {
                            nSlot = DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30;
                        }
                        if (DiplomacyGroups.getInstance().getGroups().size() > 30) {
                            var nGui = createAllGroups(player, sortType, nSlot);
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30) {
                            nSlot = DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30;
                        }
                        if (DiplomacyGroups.getInstance().getGroups().size() > 30) {
                            var nGui = createAllGroups(player, sortType, nSlot);
                            nGui.show(player);
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
                    var nGui = createMenu(player);
                    nGui.show(player);
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
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            groups.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("population")) {
            groups.stream()
                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reversePopulation")) {
            groups.stream()
                    .sorted(comparingInt(p -> p.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("territory")) {
            groups.stream()
                    .sorted(comparingInt(p -> -p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseTerritory")) {
            groups.stream()
                    .sorted(comparingInt(p -> p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            groups.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            groups.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("nation")) {
            groups.stream()
                    .sorted((p1, p2) -> p1.getNation().getName().compareToIgnoreCase(p2.getNation().getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseNation")) {
            groups.stream()
                    .sorted((p1, p2) -> -p1.getNation().getName().compareToIgnoreCase(p2.getNation().getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private static StaticGuiElement createAllGroupsElement(Player player, DiplomacyGroup group, char slot) {
        var shield = group.getShield();

        var plots = group.getChunks().size();
        var label = " plots";
        if (plots == 1) {
            label = " plot";
        }

        return new StaticGuiElement(slot,
                shield,
                click -> {
                    var nGui = GroupGuiFactory.create(group, player);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + group.getName(),
                ChatColor.BLUE + "Population: " + ChatColor.GRAY + group.getMembers().size(),
                ChatColor.BLUE + "Territory: " + ChatColor.GRAY + plots + label,
                ChatColor.BLUE + "Nation: " + ChatColor.GRAY + group.getNation().getName(),
                ChatColor.BLUE + "Created On: " + ChatColor.GRAY + group.getDateCreated()
        );
    }

    public static InventoryGui createPlayerGroups(Player player, DiplomacyPlayer otherPlayer, String sortType, int slot) {

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Groups";
        String[] guiSetup = {
                "A abcde Z",
                "B fghij  ",
                "C klmno U",
                "G pqrst D",
                "N uvwxy  ",
                "  z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayerGroups(player, otherPlayer, "alphabet", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(player, otherPlayer, "reverseAlphabet", slot);
                        nGui.show(player);
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
                        var nGui = createPlayerGroups(player, otherPlayer, "population", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(player, otherPlayer, "reversePopulation", slot);
                        nGui.show(player);
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
                        var nGui = createPlayerGroups(player, otherPlayer, "territory", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(player, otherPlayer, "reverseTerritory", slot);
                        nGui.show(player);
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
                        var nGui = createPlayerGroups(player, otherPlayer, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(player, otherPlayer, "reverseAge", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nGui = createPlayerGroups(player, otherPlayer, "nation", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createPlayerGroups(player, otherPlayer, "reverseNation", slot);
                        nGui.show(player);
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
                    if (click.getType().isLeftClick()) {
                        var nSlot = slot - 5;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createPlayerGroups(player, otherPlayer, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createPlayerGroups(player, otherPlayer, sortType, nSlot);
                        nGui.show(player);
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
                        if (nSlot > DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30) {
                            nSlot = DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30;
                        }
                        if (DiplomacyGroups.getInstance().getGroups().size() > 30) {
                            var nGui = createPlayerGroups(player, otherPlayer, sortType, nSlot);
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30) {
                            nSlot = DiplomacyGroups.getInstance().getGroups().size() + (5 - DiplomacyGroups.getInstance().getGroups().size() % 5) - 30;
                        }
                        if (DiplomacyGroups.getInstance().getGroups().size() > 30) {
                            var nGui = createPlayerGroups(player, otherPlayer, sortType, nSlot);
                            nGui.show(player);
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
                    var nGui = createPlayer(player, Bukkit.getOfflinePlayer(otherPlayer.getUUID()));
                    nGui.show(player);
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
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reverseAlphabet" -> groups.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "population" -> groups.stream()
                    .sorted((p1, p2) -> -(p1.getMembers().size() - p2.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reversePopulation" -> groups.stream()
                    .sorted(comparingInt(p -> p.getMembers().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "territory" -> groups.stream()
                    .sorted(comparingInt(p -> -p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reverseTerritory" -> groups.stream()
                    .sorted(comparingInt(p -> p.getChunks().size()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "age" -> groups.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reverseAge" -> groups.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "nation" -> groups.stream()
                    .sorted((p1, p2) -> p1.getNation().getName().compareToIgnoreCase(p2.getNation().getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
            case "reverseNation" -> groups.stream()
                    .sorted((p1, p2) -> -p1.getNation().getName().compareToIgnoreCase(p2.getNation().getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(group -> {
                        var element = createAllGroupsElement(player, group, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }
}
