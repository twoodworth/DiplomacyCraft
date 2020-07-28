package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

public class NationGuiFactory {

    private static final DecimalFormat formatter = new DecimalFormat("#,###.00");

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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view members"
            ));
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.SKELETON_SKULL),
                    click -> true,
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
            gui.addElement(new StaticGuiElement('r',
                    new ItemStack(Material.FILLED_MAP),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view members"
            ));
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.SKELETON_SKULL),
                    click -> true,
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
            gui.addElement(new StaticGuiElement('r',
                    new ItemStack(Material.FILLED_MAP),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "World Map",//TODO add
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View world map"
            ));
            gui.addElement(new StaticGuiElement('t',
                    new ItemStack(Material.BARRIER),
                    click -> {
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                    ChatColor.GRAY + "Click to escape"//TODO add
            ));
        }
        return gui;
    }
}
