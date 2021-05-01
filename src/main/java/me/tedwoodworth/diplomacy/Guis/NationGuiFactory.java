package me.tedwoodworth.diplomacy.Guis;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Guis.ClassGuiFactory;
import me.tedwoodworth.diplomacy.Guis.Guis;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

public class NationGuiFactory {
    public static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public static InventoryGui createMenu(Player player) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + "Menu";
        String[] guiSetup = {
                "    f    ",
                "   rea   ",
                "         ",
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        gui.setCloseAction(close -> false);
        gui.setFiller(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        gui.addElement(new StaticGuiElement('r',
                new ItemStack(Material.KNOWLEDGE_BOOK),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = RecipeGuis.getInstance().getRecipeMenu(RecipeGuis.getInstance().RECIPES_KEY);
                    nGui.show(clicker);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Custom Recipes",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View custom recipes")
        );

        gui.addElement(new StaticGuiElement('a',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    Nations.getInstance().listNations((Player) clicker, 1);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation List",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View nation list"
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
                    nGui.show(clicker, true);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "You",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View your player info"
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
        gui.setCloseAction(close -> false);
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
        gui.setCloseAction(close -> false);
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
                    Nations.getInstance().listOutlaws((Player) clicker, nation, 0);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Outlaws",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "List outlaws"
        ));

        // Nation menu element
        gui.addElement(new StaticGuiElement('z',
                new ItemStack(Material.PAINTING),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = createMenu((Player) clicker);
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
                    Nations.getInstance().listGroups(nation, (Player) clicker, 1);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Groups",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "List groups"
        ));

        // Nation classes element
        gui.addElement(new StaticGuiElement('g',
                new ItemStack(Material.NETHER_STAR),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = ClassGuiFactory.create(nation, (Player) clicker);
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
                    var clicker = click.getEvent().getWhoClicked();
                    Nations.getInstance().listAllyNations((Player) clicker, nation, 1);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Ally Nations",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "List ally nations"
        ));

        // Nation enemies element
        gui.addElement(new StaticGuiElement('n',
                new ItemStack(Material.RED_BANNER),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    Nations.getInstance().listEnemyNations((Player) clicker, nation, 1);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Enemy Nations",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "List enemy nations"
        ));

        // Nation nations element
        gui.addElement(new StaticGuiElement('p',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    Nations.getInstance().listNations((Player) clicker, 1);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation list",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View nation list"
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
}
