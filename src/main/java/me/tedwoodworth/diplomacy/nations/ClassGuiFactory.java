package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.Objects;

public class ClassGuiFactory {

    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

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

        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Classes";
        String[] guiSetup = {
                "        k",
                "         ",
                "abcdefghi",
                "         ",
                "         ",
                "        l"
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

        var canManageClasses = false;
        if (Objects.equals(nation, playerNation)) {
            var nationClass = nation.getMemberClass(diplomacyPlayer);
            var permissions = nationClass.getPermissions();
            canManageClasses = permissions.get("CanManageClasses");
        }

        var classes = nation.getClasses();

        if (canManageClasses) {

            var class0prefix = "";
            if (classes.get(0).getPrefix() != null) {
                class0prefix = classes.get(0).getPrefix();
            }
            gui.addElement(new StaticGuiElement('a',
                    new ItemStack(Material.DIRT),
                    click -> {
                        if (click.getType().isRightClick()) {
                            var nGui = createChangeClassSettings(nation, player, classes.get(0));
                            nGui.show(player);
                        } else {
                            var nGui = createViewClassSettings(nation, player, classes.get(0));
                            nGui.show(player);
                        }
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(0).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class0prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(0).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings",
                    ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Change class settings"
            ));

            var class1prefix = "";
            if (classes.get(1).getPrefix() != null) {
                class1prefix = classes.get(1).getPrefix();
            }
            gui.addElement(new StaticGuiElement('b',
                    new ItemStack(Material.STONE),
                    click -> {
                        if (click.getType().isRightClick()) {
                            var nGui = createChangeClassSettings(nation, player, classes.get(1));
                            nGui.show(player);
                        } else {
                            var nGui = createViewClassSettings(nation, player, classes.get(1));
                            nGui.show(player);
                        }
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(1).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class1prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(1).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings",
                    ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Change class settings"
            ));
            var class2prefix = "";
            if (classes.get(2).getPrefix() != null) {
                class2prefix = classes.get(2).getPrefix();
            }
            gui.addElement(new StaticGuiElement('c',
                    new ItemStack(Material.IRON_BLOCK),
                    click -> {
                        if (click.getType().isRightClick()) {
                            var nGui = createChangeClassSettings(nation, player, classes.get(2));
                            nGui.show(player);
                        } else {
                            var nGui = createViewClassSettings(nation, player, classes.get(2));
                            nGui.show(player);
                        }
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(2).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class2prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(2).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings",
                    ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Change class settings"
            ));
            var class3prefix = "";
            if (classes.get(3).getPrefix() != null) {
                class3prefix = classes.get(3).getPrefix();
            }
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.GOLD_BLOCK),
                    click -> {
                        if (click.getType().isRightClick()) {
                            var nGui = createChangeClassSettings(nation, player, classes.get(3));
                            nGui.show(player);
                        } else {
                            var nGui = createViewClassSettings(nation, player, classes.get(3));
                            nGui.show(player);
                        }
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(3).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class3prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(3).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings",
                    ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Change class settings"
            ));
            var class4prefix = "";
            if (classes.get(4).getPrefix() != null) {
                class4prefix = classes.get(4).getPrefix();
            }
            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.DIAMOND_BLOCK),
                    click -> {
                        if (click.getType().isRightClick()) {
                            var nGui = createChangeClassSettings(nation, player, classes.get(4));
                            nGui.show(player);
                        } else {
                            var nGui = createViewClassSettings(nation, player, classes.get(4));
                            nGui.show(player);
                        }
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(4).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class4prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(4).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings",
                    ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Change class settings"
            ));
            var class5prefix = "";
            if (classes.get(5).getPrefix() != null) {
                class5prefix = classes.get(5).getPrefix();
            }
            gui.addElement(new StaticGuiElement('f',
                    new ItemStack(Material.OBSIDIAN),
                    click -> {
                        if (click.getType().isRightClick()) {
                            var nGui = createChangeClassSettings(nation, player, classes.get(5));
                            nGui.show(player);
                        } else {
                            var nGui = createViewClassSettings(nation, player, classes.get(5));
                            nGui.show(player);
                        }
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(5).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class5prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(5).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings",
                    ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Change class settings"
            ));
            var class6prefix = "";
            if (classes.get(6).getPrefix() != null) {
                class6prefix = classes.get(6).getPrefix();
            }
            gui.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.ANCIENT_DEBRIS),
                    click -> {
                        if (click.getType().isRightClick()) {
                            var nGui = createChangeClassSettings(nation, player, classes.get(6));
                            nGui.show(player);
                        } else {
                            var nGui = createViewClassSettings(nation, player, classes.get(6));
                            nGui.show(player);
                        }
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(6).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class6prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(6).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings",
                    ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Change class settings"
            ));
            var class7prefix = "";
            if (classes.get(7).getPrefix() != null) {
                class7prefix = classes.get(7).getPrefix();
            }
            gui.addElement(new StaticGuiElement('h',
                    new ItemStack(Material.NETHERITE_BLOCK),
                    click -> {
                        if (click.getType().isRightClick()) {
                            var nGui = createChangeClassSettings(nation, player, classes.get(7));
                            nGui.show(player);
                        } else {
                            var nGui = createViewClassSettings(nation, player, classes.get(7));
                            nGui.show(player);
                        }
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(7).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class7prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(7).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings",
                    ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Change class settings"
            ));
            var class8prefix = "";
            if (classes.get(8).getPrefix() != null) {
                class8prefix = classes.get(8).getPrefix();
            }
            gui.addElement(new StaticGuiElement('i',
                    new ItemStack(Material.BEDROCK),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(8));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(8).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class8prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(8).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

        } else {
            var class0prefix = "";
            if (classes.get(0).getPrefix() != null) {
                class0prefix = classes.get(0).getPrefix();
            }
            gui.addElement(new StaticGuiElement('a',
                    new ItemStack(Material.DIRT),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(0));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(0).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class0prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(0).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class1prefix = "";
            if (classes.get(1).getPrefix() != null) {
                class1prefix = classes.get(1).getPrefix();
            }
            gui.addElement(new StaticGuiElement('b',
                    new ItemStack(Material.STONE),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(1));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(1).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class1prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(1).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class2prefix = "";
            if (classes.get(2).getPrefix() != null) {
                class2prefix = classes.get(2).getPrefix();
            }
            gui.addElement(new StaticGuiElement('c',
                    new ItemStack(Material.IRON_BLOCK),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(2));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(2).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class2prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(2).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class3prefix = "";
            if (classes.get(3).getPrefix() != null) {
                class3prefix = classes.get(3).getPrefix();
            }
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.GOLD_BLOCK),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(3));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(3).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class3prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(3).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class4prefix = "";
            if (classes.get(4).getPrefix() != null) {
                class4prefix = classes.get(4).getPrefix();
            }
            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.DIAMOND_BLOCK),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(4));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(4).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class4prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(4).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class5prefix = "";
            if (classes.get(5).getPrefix() != null) {
                class5prefix = classes.get(5).getPrefix();
            }
            gui.addElement(new StaticGuiElement('f',
                    new ItemStack(Material.OBSIDIAN),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(5));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(5).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class5prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(5).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class6prefix = "";
            if (classes.get(6).getPrefix() != null) {
                class6prefix = classes.get(6).getPrefix();
            }
            gui.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.ANCIENT_DEBRIS),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(6));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(6).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class6prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(6).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class7prefix = "";
            if (classes.get(7).getPrefix() != null) {
                class7prefix = classes.get(7).getPrefix();
            }
            gui.addElement(new StaticGuiElement('h',
                    new ItemStack(Material.NETHERITE_BLOCK),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(7));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(7).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class7prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(7).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class8prefix = "";
            if (classes.get(8).getPrefix() != null) {
                class8prefix = classes.get(8).getPrefix();
            }
            gui.addElement(new StaticGuiElement('i',
                    new ItemStack(Material.BEDROCK),
                    click -> {
                        var nGui = createViewClassSettings(nation, player, classes.get(8));
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(8).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class8prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(8).getTax()),
                    " ",
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));
        }

        var banner = nation.getBanner();
        var bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        banner.setItemMeta(bannerMeta);

        gui.addElement(new StaticGuiElement('k',
                banner,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getNationMenu(nation);
                    nGui.show(clicker);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
                ChatColor.GRAY + "Click to go back"
        ));

        gui.addElement(new StaticGuiElement('l',
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

    private static InventoryGui createChangeClassSettings(Nation nation, Player player, NationClass nationClass) {
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

        var title = "" + color + ChatColor.BOLD + nation.getName() + " " + ChatColor.DARK_GRAY + ChatColor.BOLD + nationClass.getName();
        String[] guiSetup = {
                "F ABCD  E",
                "abcdefghP",
                "ijklmnopq",
                "rstuvwxyz",
                "123456789",
                "GHIJKLMNO"
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

        var classMaterial = Material.DIRT;
        switch (nationClass.getClassID()) {
            case "1" -> classMaterial = Material.STONE;
            case "2" -> classMaterial = Material.IRON_BLOCK;
            case "3" -> classMaterial = Material.GOLD_BLOCK;
            case "4" -> classMaterial = Material.DIAMOND_BLOCK;
            case "5" -> classMaterial = Material.OBSIDIAN;
            case "6" -> classMaterial = Material.ANCIENT_DEBRIS;
            case "7" -> classMaterial = Material.NETHERITE_BLOCK;
            case "8" -> classMaterial = Material.BEDROCK;
        }

        var classItem = new ItemStack(classMaterial);


        var currentPrefix = "";
        if (nationClass.getPrefix() != null) {
            currentPrefix = nationClass.getPrefix();
        }
        gui.addElement(new StaticGuiElement('A',
                classItem,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + nationClass.getName(),
                ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + currentPrefix,
                ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(nationClass.getTax())
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.NAME_TAG),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Class Name",
                ChatColor.GRAY + nationClass.getName(),
                " ",
                ChatColor.BLUE + "Change Name: " + ChatColor.GRAY + "/class rename " + nationClass.getName() + " <new name>"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.PAPER),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Class Prefix",
                ChatColor.GRAY + currentPrefix,
                " ",
                ChatColor.BLUE + "Change Prefix: " + ChatColor.GRAY + "/class prefix " + nationClass.getName() + " <new prefix>",
                ChatColor.BLUE + "Remove Prefix: " + ChatColor.GRAY + "/class clearPrefix " + nationClass.getName()
        ));
        gui.addElement(new StaticGuiElement('D',
                new ItemStack(Material.DIAMOND),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Class Tax",
                ChatColor.GRAY + "\u00A4" + formatter.format(nationClass.getTax()),
                " ",
                ChatColor.BLUE + "Set Tax: " + ChatColor.GRAY + "/class tax " + nationClass.getName() + " <amount>"
        ));
        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.NETHER_STAR),
                click -> {
                    var nGui = create(nation, player);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to all classes"
        ));
        gui.addElement(new StaticGuiElement('F',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));

        var permissions = nationClass.getPermissions();
        var aPermissionKey = "CanBuildAnywhere";
        var item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var permission = permissions.get(aPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('a',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, aPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + aPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can build anywhere in the nation,",
                ChatColor.GRAY + "regardless of what groups they are in.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player can only build in a plot if they are",
                ChatColor.GRAY + "a member of the group that the owns the plot"
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var bPermissionKey = "CanContest";
        permission = permissions.get(bPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('b',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, bPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + bPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/plot contest\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/plot contest\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var cPermissionKey = "CanSurrenderPlot";
        permission = permissions.get(cPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('c',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, cPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + cPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the commands \"/plot surrender\"",
                ChatColor.GRAY + "and \"/plot disband\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the commands \"/plot surrender\"",
                ChatColor.GRAY + "and \"/plot disband\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var dPermissionKey = "CanSurrenderGroup";
        permission = permissions.get(dPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('d',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, dPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + dPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/group surrender\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/group surrender\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var ePermissionKey = "CanSurrenderNation";
        permission = permissions.get(ePermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('e',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, ePermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + ePermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the commands \"/nation surrender\"",
                ChatColor.GRAY + "and \"/nation disband\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the commands \"/nation surrender\"",
                ChatColor.GRAY + "and \"/nation disband\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var fPermissionKey = "CanCreateGroups";
        permission = permissions.get(fPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('f',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, fPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + fPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/group create\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/group create\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var gPermissionKey = "CanRemoveGroups";
        permission = permissions.get(gPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('g',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, gPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + gPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/group disband\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/group disband\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var permissionKeyP = "CanUnlock";
        permission = permissions.get(permissionKeyP);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('P',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, permissionKeyP);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + permissionKeyP,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/plot unlock\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/plot unlock\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var hPermissionKey = "CanManagePlotsOfLedGroups";
        permission = permissions.get(hPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('h',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, hPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + hPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the commands \"/group plot claim\" and ",
                ChatColor.GRAY + "\"/group plot unclaim\" for any group that they are",
                ChatColor.GRAY + "a leader of.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the commands \"/group plot claim\" and",
                ChatColor.GRAY + "\"/group plot unclaim\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var iPermissionKey = "CanLeadAllGroups";
        permission = permissions.get(iPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('i',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, iPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + iPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player automatically becomes a leader of all ",
                ChatColor.GRAY + "groups in the nation and cannot be demoted.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player will only be a leader of a group if they were",
                ChatColor.GRAY + "promoted to that position by another group leader"

        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var jPermissionKey = "CanRenameNation";
        permission = permissions.get(jPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('j',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, jPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + jPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation rename\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation rename\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var kPermissionKey = "CanAllyNations";
        permission = permissions.get(kPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('k',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, kPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + kPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation ally\"",
                ChatColor.GRAY + "and accept ally requests from other nations",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation ally\"",
                ChatColor.GRAY + "and cannot accept ally requests from other nations"
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var lPermissionKey = "CanNeutralNations";
        permission = permissions.get(lPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('l',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, lPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + lPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation neutral\"",
                ChatColor.GRAY + "and accept neutral requests from other nations",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation neutral\"",
                ChatColor.GRAY + "and cannot accept neutral requests from other nations"
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var mPermissionKey = "CanEnemyNations";
        permission = permissions.get(mPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('m',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, mPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + mPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation enemy\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation enemy\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var nPermissionKey = "CanManageTaxes";
        permission = permissions.get(nPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('n',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, nPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + nPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change how much each class is taxed.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change how much each class is taxed."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var oPermissionKey = "CanInvitePlayers";
        permission = permissions.get(oPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('o',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, oPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + oPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can invite other players to the nation.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot invite other players to the nation."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var pPermissionKey = "CanKickPlayers";
        permission = permissions.get(pPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('p',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, pPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + pPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can kick players from the nation.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot kick players from the nation."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var qPermissionKey = "CanSetNationColor";
        permission = permissions.get(qPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('q',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, qPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + qPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set the nation's color on the Dynmap.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set the nation's color on the Dynmap."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var rPermissionKey = "CanManageOutlaws";
        permission = permissions.get(rPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('r',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, rPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + rPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/outlaw\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/outlaw\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var sPermissionKey = "CanManageGuards";
        permission = permissions.get(sPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('s',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, sPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + sPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can  manage guards.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot manage guards."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var tPermissionKey = "CanSeeGuardNotifications";
        permission = permissions.get(tPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('t',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, tPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + tPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can see guard notifications.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot see guard notifications."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var uPermissionKey = "CanManageClasses";
        permission = permissions.get(uPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('u',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, uPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + uPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the settings of each class.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change any class settings."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var vPermissionKey = "CanBeKicked";
        permission = permissions.get(vPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('v',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, vPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + vPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can be kicked out of the nation.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot be kicked out of the nation."
        ));


        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var wPermissionKey = "CanDeposit";
        permission = permissions.get(wPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('w',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, wPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + wPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/nation deposit\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/nation deposit\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var xPermissionKey = "CanWithdraw";
        permission = permissions.get(xPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('x',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, xPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + xPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/nation withdraw\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/nation withdraw\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var yPermissionKey = "CanChangeBanner";
        permission = permissions.get(yPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('y',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, yPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + yPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/nation banner\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/nation banner\"."
        ));


        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var zPermissionKey = "CanToggleBorder";
        permission = permissions.get(zPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('z',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, zPermissionKey);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + zPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the nation's border status.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the nation's border status."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey1 = "CanSetClassTo1";
        permission = permissions.get(PermissionKey1);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('1',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKey1);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey1,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(0)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(0)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey2 = "CanSetClassTo2";
        permission = permissions.get(PermissionKey2);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('2',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKey2);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey2,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(1)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(1)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey3 = "CanSetClassTo3";
        permission = permissions.get(PermissionKey3);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('3',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKey3);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey3,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(2)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(2)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey4 = "CanSetClassTo4";
        permission = permissions.get(PermissionKey4);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('4',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKey4);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey4,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(3)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(3)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey5 = "CanSetClassTo5";
        permission = permissions.get(PermissionKey5);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('5',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKey5);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey5,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(4)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(4)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey6 = "CanSetClassTo6";
        permission = permissions.get(PermissionKey6);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('6',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKey6);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey6,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(5)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(5)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey7 = "CanSetClassTo7";
        permission = permissions.get(PermissionKey7);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('7',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKey7);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey7,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(6)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(6)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey8 = "CanSetClassTo8";
        permission = permissions.get(PermissionKey8);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('8',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKey8);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey8,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(7)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(7)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey9 = "CanSetClassTo9";
        permission = permissions.get(PermissionKey9);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('9',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKey9);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey9,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(8)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(8)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyG = "CanRemoveFromClass1";
        permission = permissions.get(PermissionKeyG);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('G',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKeyG);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyG,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(0)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(0)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyH = "CanRemoveFromClass2";
        permission = permissions.get(PermissionKeyH);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('H',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKeyH);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyH,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(1)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(1)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyI = "CanRemoveFromClass3";
        permission = permissions.get(PermissionKeyI);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('I',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKeyI);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyI,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(2)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(2)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyJ = "CanRemoveFromClass4";
        permission = permissions.get(PermissionKeyJ);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('J',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKeyJ);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyJ,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(3)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(3)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyK = "CanRemoveFromClass5";
        permission = permissions.get(PermissionKeyK);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('K',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKeyK);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyK,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(4)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(4)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyL = "CanRemoveFromClass6";
        permission = permissions.get(PermissionKeyL);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('L',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKeyL);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyL,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(5)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(5)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyM = "CanRemoveFromClass7";
        permission = permissions.get(PermissionKeyM);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('M',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKeyM);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyM,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(6)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(6)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyN = "CanRemoveFromClass8";
        permission = permissions.get(PermissionKeyN);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('N',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKeyN);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyN,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(7)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(7)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyO = "CanRemoveFromClass9";
        permission = permissions.get(PermissionKeyO);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('O',
                item,
                click -> {
                    nation.toggleClassPermission(nationClass, player, PermissionKeyO);
                    var nGui = createChangeClassSettings(nation, player, nationClass);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyO,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(8)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(8)).getName()
        ));
        return gui;
    }

    private static InventoryGui createViewClassSettings(Nation nation, Player player, NationClass nationClass) {
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

        var title = "" + color + ChatColor.BOLD + nation.getName() + " " + ChatColor.DARK_GRAY + ChatColor.BOLD + nationClass.getName();
        String[] guiSetup = {
                "F ABCD  E",
                "abcdefghP",
                "ijklmnopq",
                "rstuvwxyz",
                "123456789",
                "GHIJKLMNO"
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

        var classMaterial = Material.DIRT;
        switch (nationClass.getClassID()) {
            case "1" -> classMaterial = Material.STONE;
            case "2" -> classMaterial = Material.IRON_BLOCK;
            case "3" -> classMaterial = Material.GOLD_BLOCK;
            case "4" -> classMaterial = Material.DIAMOND_BLOCK;
            case "5" -> classMaterial = Material.OBSIDIAN;
            case "6" -> classMaterial = Material.ANCIENT_DEBRIS;
            case "7" -> classMaterial = Material.NETHERITE_BLOCK;
            case "8" -> classMaterial = Material.BEDROCK;
        }

        var classItem = new ItemStack(classMaterial);


        var currentPrefix = "";
        if (nationClass.getPrefix() != null) {
            currentPrefix = nationClass.getPrefix();
        }
        gui.addElement(new StaticGuiElement('A',
                classItem,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + nationClass.getName(),
                ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + currentPrefix,
                ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(nationClass.getTax())
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.NAME_TAG),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Class Name",
                ChatColor.GRAY + nationClass.getName()
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.PAPER),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Class Prefix",
                ChatColor.GRAY + currentPrefix
        ));
        gui.addElement(new StaticGuiElement('D',
                new ItemStack(Material.DIAMOND),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Class Tax",
                ChatColor.GRAY + "\u00A4" + formatter.format(nationClass.getTax())
        ));
        gui.addElement(new StaticGuiElement('E',
                new ItemStack(Material.NETHER_STAR),
                click -> {
                    var nGui = create(nation, player);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Go Back",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to all classes"
        ));
        gui.addElement(new StaticGuiElement('F',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));

        var permissions = nationClass.getPermissions();
        var aPermissionKey = "CanBuildAnywhere";
        var item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var permission = permissions.get(aPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('a',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + aPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can build anywhere in the nation",
                ChatColor.GRAY + "that isn't part of a group.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player can only build in a plot if they are",
                ChatColor.GRAY + "a member of the group that the owns the plot"
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var bPermissionKey = "CanContest";
        permission = permissions.get(bPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('b',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + bPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/plot contest\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/plot contest\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var cPermissionKey = "CanSurrenderPlot";
        permission = permissions.get(cPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('c',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + cPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the commands \"/plot surrender\"",
                ChatColor.GRAY + "and \"/plot disband\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the commands \"/plot surrender\"",
                ChatColor.GRAY + "and \"/plot disband\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var dPermissionKey = "CanSurrenderGroup";
        permission = permissions.get(dPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('d',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + dPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/group surrender\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/group surrender\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var ePermissionKey = "CanSurrenderNation";
        permission = permissions.get(ePermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('e',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + ePermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the commands \"/nation surrender\"",
                ChatColor.GRAY + "and \"/nation disband\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the commands \"/nation surrender\"",
                ChatColor.GRAY + "and \"/nation disband\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var fPermissionKey = "CanCreateGroups";
        permission = permissions.get(fPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('f',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + fPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/group create\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/group create\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var gPermissionKey = "CanRemoveGroups";
        permission = permissions.get(gPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('g',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + gPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/group disband\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/group disband\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var permissionKeyP = "CanUnlock";
        permission = permissions.get(permissionKeyP);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('P',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + permissionKeyP,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/plot unlock\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/plot unlock\"."
        ));


        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var hPermissionKey = "CanManagePlotsOfLedGroups";
        permission = permissions.get(hPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('h',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + hPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the commands \"/group plot claim\" and ",
                ChatColor.GRAY + "\"/group plot unclaim\" for any group that they are",
                ChatColor.GRAY + "a leader of.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the commands \"/group plot claim\" and",
                ChatColor.GRAY + "\"/group plot unclaim\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var iPermissionKey = "CanLeadAllGroups";
        permission = permissions.get(iPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('i',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + iPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player automatically becomes a leader of all ",
                ChatColor.GRAY + "groups in the nation and cannot be demoted.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player will only be a leader of a group if they were",
                ChatColor.GRAY + "promoted to that position by another group leader"

        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var jPermissionKey = "CanRenameNation";
        permission = permissions.get(jPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('j',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + jPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation rename\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation rename\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var kPermissionKey = "CanAllyNations";
        permission = permissions.get(kPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('k',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + kPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation ally\"",
                ChatColor.GRAY + "and accept ally requests from other nations",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation ally\"",
                ChatColor.GRAY + "and cannot accept ally requests from other nations"
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var lPermissionKey = "CanNeutralNations";
        permission = permissions.get(lPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('l',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + lPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation neutral\"",
                ChatColor.GRAY + "and accept neutral requests from other nations",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation neutral\"",
                ChatColor.GRAY + "and cannot accept neutral requests from other nations"
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var mPermissionKey = "CanEnemyNations";
        permission = permissions.get(mPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('m',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + mPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation enemy\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation enemy\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var nPermissionKey = "CanManageTaxes";
        permission = permissions.get(nPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('n',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + nPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change how much each class is taxed.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change how much each class is taxed."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var oPermissionKey = "CanInvitePlayers";
        permission = permissions.get(oPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('o',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + oPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can invite other players to the nation.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot invite other players to the nation."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var pPermissionKey = "CanKickPlayers";
        permission = permissions.get(pPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('p',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + pPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can kick players from the nation.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot kick players from the nation."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var qPermissionKey = "CanSetNationColor";
        permission = permissions.get(qPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('q',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + qPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set the nation's color on the Dynmap.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set the nation's color on the Dynmap."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var rPermissionKey = "CanManageOutlaws";
        permission = permissions.get(rPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('r',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + rPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/outlaw\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/outlaw\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var sPermissionKey = "CanManageGuards";
        permission = permissions.get(sPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('s',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + sPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can  manage guards.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot manage guards."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var tPermissionKey = "CanSeeGuardNotifications";
        permission = permissions.get(tPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('t',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + tPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can see guard notifications.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot see guard notifications."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var uPermissionKey = "CanManageClasses";
        permission = permissions.get(uPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('u',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + uPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the settings of each class.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change any class settings."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var vPermissionKey = "CanBeKicked";
        permission = permissions.get(vPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('v',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + vPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can be kicked out of the nation.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot be kicked out of the nation."
        ));


        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var wPermissionKey = "CanDeposit";
        permission = permissions.get(wPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('w',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + wPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/nation deposit\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/nation deposit\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var xPermissionKey = "CanWithdraw";
        permission = permissions.get(xPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('x',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + xPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/nation withdraw\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/nation withdraw\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var yPermissionKey = "CanChangeBanner";
        permission = permissions.get(yPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('y',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + yPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/nation banner\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/nation banner\"."
        ));


        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var zPermissionKey = "CanToggleBorder";
        permission = permissions.get(zPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('z',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + zPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the nation's border status.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the nation's border status."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey1 = "CanSetClassTo1";
        permission = permissions.get(PermissionKey1);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('1',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey1,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(0)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(0)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey2 = "CanSetClassTo2";
        permission = permissions.get(PermissionKey2);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('2',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey2,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(1)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(1)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey3 = "CanSetClassTo3";
        permission = permissions.get(PermissionKey3);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('3',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey3,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(2)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(2)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey4 = "CanSetClassTo4";
        permission = permissions.get(PermissionKey4);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('4',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey4,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(3)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(3)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey5 = "CanSetClassTo5";
        permission = permissions.get(PermissionKey5);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('5',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey5,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(4)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(4)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey6 = "CanSetClassTo6";
        permission = permissions.get(PermissionKey6);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('6',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey6,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(5)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(5)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey7 = "CanSetClassTo7";
        permission = permissions.get(PermissionKey7);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('7',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey7,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(6)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(6)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey8 = "CanSetClassTo8";
        permission = permissions.get(PermissionKey8);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('8',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey8,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(7)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(7)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey9 = "CanSetClassTo9";
        permission = permissions.get(PermissionKey9);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('9',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey9,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(8)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(8)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyG = "CanRemoveFromClass1";
        permission = permissions.get(PermissionKeyG);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('G',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyG,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(0)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(0)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyH = "CanRemoveFromClass2";
        permission = permissions.get(PermissionKeyH);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('H',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyH,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(1)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(1)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyI = "CanRemoveFromClass3";
        permission = permissions.get(PermissionKeyI);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('I',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyI,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(2)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(2)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyJ = "CanRemoveFromClass4";
        permission = permissions.get(PermissionKeyJ);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('J',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyJ,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(3)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(3)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyK = "CanRemoveFromClass5";
        permission = permissions.get(PermissionKeyK);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('K',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyK,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(4)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(4)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyL = "CanRemoveFromClass6";
        permission = permissions.get(PermissionKeyL);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('L',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyL,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(5)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(5)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyM = "CanRemoveFromClass7";
        permission = permissions.get(PermissionKeyM);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('M',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyM,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(6)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(6)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyN = "CanRemoveFromClass8";
        permission = permissions.get(PermissionKeyN);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('N',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyN,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(7)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(7)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyO = "CanRemoveFromClass9";
        permission = permissions.get(PermissionKeyO);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('O',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyO,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(8)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(8)).getName()
        ));
        return gui;
    }

    public static InventoryGui createPlayerClassSettings(DiplomacyPlayer otherDiplomacyPlayer, Player player) {
        var nation = Nations.getInstance().get(otherDiplomacyPlayer);
        var otherPlayer = Bukkit.getOfflinePlayer(otherDiplomacyPlayer.getUUID());
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

        var title = "" + color + ChatColor.BOLD + otherPlayer.getName() + " " + ChatColor.DARK_GRAY + ChatColor.BOLD + "Class";
        String[] guiSetup = {
                "F ABCD  E",
                "abcdefghP",
                "ijklmnopq",
                "rstuvwxyz",
                "123456789",
                "GHIJKLMNO"
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

        var nationClass = nation.getMemberClass(otherDiplomacyPlayer);
        var classMaterial = Material.DIRT;
        switch (nationClass.getClassID()) {
            case "1" -> classMaterial = Material.STONE;
            case "2" -> classMaterial = Material.IRON_BLOCK;
            case "3" -> classMaterial = Material.GOLD_BLOCK;
            case "4" -> classMaterial = Material.DIAMOND_BLOCK;
            case "5" -> classMaterial = Material.OBSIDIAN;
            case "6" -> classMaterial = Material.ANCIENT_DEBRIS;
            case "7" -> classMaterial = Material.NETHERITE_BLOCK;
            case "8" -> classMaterial = Material.BEDROCK;
        }

        var classItem = new ItemStack(classMaterial);


        var currentPrefix = "";
        if (nationClass.getPrefix() != null) {
            currentPrefix = nationClass.getPrefix();
        }
        gui.addElement(new StaticGuiElement('A',
                classItem,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + nationClass.getName(),
                ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + currentPrefix,
                ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(nationClass.getTax())
        ));
        gui.addElement(new StaticGuiElement('B',
                new ItemStack(Material.NAME_TAG),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Class Name",
                ChatColor.GRAY + nationClass.getName()
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.PAPER),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Class Prefix",
                ChatColor.GRAY + currentPrefix
        ));
        gui.addElement(new StaticGuiElement('D',
                new ItemStack(Material.DIAMOND),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Class Tax",
                ChatColor.GRAY + "\u00A4" + formatter.format(nationClass.getTax())
        ));

        var playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (playerHead.getItemMeta());
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(otherDiplomacyPlayer.getUUID()));
        playerHead.setItemMeta(skullMeta);

        gui.addElement(new StaticGuiElement('E',
                playerHead,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = NationGuiFactory.createPlayer(otherPlayer);
                    nGui.show(clicker);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + otherPlayer.getName(),
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Return to player info"
        ));
        gui.addElement(new StaticGuiElement('F',
                new ItemStack(Material.BARRIER),
                click -> {
                    gui.close();
                    return true;
                },
                "" + ChatColor.RED + ChatColor.BOLD + "Escape",
                ChatColor.GRAY + "Click to escape"
        ));

        var permissions = nationClass.getPermissions();
        var aPermissionKey = "CanBuildAnywhere";
        var item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var permission = permissions.get(aPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('a',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + aPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can build anywhere in the nation",
                ChatColor.GRAY + "that isn't part of a group.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player can only build in a plot if they are",
                ChatColor.GRAY + "a member of the group that the owns the plot"
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var bPermissionKey = "CanContest";
        permission = permissions.get(bPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('b',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + bPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/plot contest\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/plot contest\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var cPermissionKey = "CanSurrenderPlot";
        permission = permissions.get(cPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('c',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + cPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the commands \"/plot surrender\"",
                ChatColor.GRAY + "and \"/plot disband\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the commands \"/plot surrender\"",
                ChatColor.GRAY + "and \"/plot disband\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var dPermissionKey = "CanSurrenderGroup";
        permission = permissions.get(dPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('d',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + dPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/group surrender\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/group surrender\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var ePermissionKey = "CanSurrenderNation";
        permission = permissions.get(ePermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('e',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + ePermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the commands \"/nation surrender\"",
                ChatColor.GRAY + "and \"/nation disband\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the commands \"/nation surrender\"",
                ChatColor.GRAY + "and \"/nation disband\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var fPermissionKey = "CanCreateGroups";
        permission = permissions.get(fPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('f',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + fPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/group create\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/group create\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var gPermissionKey = "CanRemoveGroups";
        permission = permissions.get(gPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('g',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + gPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/group disband\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/group disband\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var permissionKeyP = "CanUnlock";
        permission = permissions.get(permissionKeyP);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('P',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + permissionKeyP,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/plot unlock\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/plot unlock\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var hPermissionKey = "CanManagePlotsOfLedGroups";
        permission = permissions.get(hPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('h',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + hPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the commands \"/group plot claim\" and ",
                ChatColor.GRAY + "\"/group plot unclaim\" for any group that they are",
                ChatColor.GRAY + "a leader of.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the commands \"/group plot claim\" and",
                ChatColor.GRAY + "\"/group plot unclaim\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var iPermissionKey = "CanLeadAllGroups";
        permission = permissions.get(iPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('i',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + iPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player automatically becomes a leader of all ",
                ChatColor.GRAY + "groups in the nation and cannot be demoted.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player will only be a leader of a group if they were",
                ChatColor.GRAY + "promoted to that position by another group leader"

        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var jPermissionKey = "CanRenameNation";
        permission = permissions.get(jPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('j',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + jPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation rename\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation rename\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var kPermissionKey = "CanAllyNations";
        permission = permissions.get(kPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('k',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + kPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation ally\"",
                ChatColor.GRAY + "and accept ally requests from other nations",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation ally\"",
                ChatColor.GRAY + "and cannot accept ally requests from other nations"
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var lPermissionKey = "CanNeutralNations";
        permission = permissions.get(lPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('l',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + lPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation neutral\"",
                ChatColor.GRAY + "and accept neutral requests from other nations",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation neutral\"",
                ChatColor.GRAY + "and cannot accept neutral requests from other nations"
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var mPermissionKey = "CanEnemyNations";
        permission = permissions.get(mPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('m',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + mPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use the command \"/nation enemy\"",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use the command \"/nation enemy\""
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var nPermissionKey = "CanManageTaxes";
        permission = permissions.get(nPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('n',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + nPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change how much each class is taxed.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change how much each class is taxed."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var oPermissionKey = "CanInvitePlayers";
        permission = permissions.get(oPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('o',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + oPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can invite other players to the nation.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot invite other players to the nation."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var pPermissionKey = "CanKickPlayers";
        permission = permissions.get(pPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('p',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + pPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can kick players from the nation.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot kick players from the nation."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var qPermissionKey = "CanSetNationColor";
        permission = permissions.get(qPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('q',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + qPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set the nation's color on the Dynmap.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set the nation's color on the Dynmap."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var rPermissionKey = "CanManageOutlaws";
        permission = permissions.get(rPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('r',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + rPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/outlaw\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/outlaw\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var sPermissionKey = "CanManageGuards";
        permission = permissions.get(sPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('s',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + sPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can  manage guards.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot manage guards."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var tPermissionKey = "CanSeeGuardNotifications";
        permission = permissions.get(tPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('t',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + tPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can see guard notifications.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot see guard notifications."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var uPermissionKey = "CanManageClasses";
        permission = permissions.get(uPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('u',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + uPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the settings of each class.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change any class settings."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var vPermissionKey = "CanBeKicked";
        permission = permissions.get(vPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('v',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + vPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can be kicked out of the nation.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot be kicked out of the nation."
        ));


        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var wPermissionKey = "CanDeposit";
        permission = permissions.get(wPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('w',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + wPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/nation deposit\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/nation deposit\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var xPermissionKey = "CanWithdraw";
        permission = permissions.get(xPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('x',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + xPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/nation withdraw\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/nation withdraw\"."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var yPermissionKey = "CanChangeBanner";
        permission = permissions.get(yPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('y',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + yPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can use \"/nation banner\".",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot use \"/nation banner\"."
        ));


        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var zPermissionKey = "CanToggleBorder";
        permission = permissions.get(zPermissionKey);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('z',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + zPermissionKey,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the nation's border status.",
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the nation's border status."
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey1 = "CanSetClassTo1";
        permission = permissions.get(PermissionKey1);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('1',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey1,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(0)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(0)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey2 = "CanSetClassTo2";
        permission = permissions.get(PermissionKey2);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('2',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey2,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(1)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(1)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey3 = "CanSetClassTo3";
        permission = permissions.get(PermissionKey3);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('3',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey3,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(2)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(2)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey4 = "CanSetClassTo4";
        permission = permissions.get(PermissionKey4);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('4',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey4,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(3)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(3)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey5 = "CanSetClassTo5";
        permission = permissions.get(PermissionKey5);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('5',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey5,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(4)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(4)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey6 = "CanSetClassTo6";
        permission = permissions.get(PermissionKey6);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('6',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey6,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(5)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(5)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey7 = "CanSetClassTo7";
        permission = permissions.get(PermissionKey7);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('7',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey7,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(6)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(6)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey8 = "CanSetClassTo8";
        permission = permissions.get(PermissionKey8);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('8',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey8,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(7)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(7)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKey9 = "CanSetClassTo9";
        permission = permissions.get(PermissionKey9);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('9',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKey9,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can set other player's classes to " + nation.getClassFromID(String.valueOf(8)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot set other player's classes to " + nation.getClassFromID(String.valueOf(8)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyG = "CanRemoveFromClass1";
        permission = permissions.get(PermissionKeyG);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('G',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyG,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(0)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(0)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyH = "CanRemoveFromClass2";
        permission = permissions.get(PermissionKeyH);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('H',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyH,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(1)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(1)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyI = "CanRemoveFromClass3";
        permission = permissions.get(PermissionKeyI);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('I',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyI,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(2)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(2)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyJ = "CanRemoveFromClass4";
        permission = permissions.get(PermissionKeyJ);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('J',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyJ,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(3)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(3)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyK = "CanRemoveFromClass5";
        permission = permissions.get(PermissionKeyK);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('K',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyK,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(4)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(4)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyL = "CanRemoveFromClass6";
        permission = permissions.get(PermissionKeyL);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('L',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyL,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(5)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(5)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyM = "CanRemoveFromClass7";
        permission = permissions.get(PermissionKeyM);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('M',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyM,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(6)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(6)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyN = "CanRemoveFromClass8";
        permission = permissions.get(PermissionKeyN);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('N',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyN,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(7)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(7)).getName()
        ));

        item = new ItemStack(Material.REDSTONE_BLOCK);
        color = ChatColor.RED;
        var PermissionKeyO = "CanRemoveFromClass9";
        permission = permissions.get(PermissionKeyO);
        if (permission) {
            item = new ItemStack(Material.EMERALD_BLOCK);
            color = ChatColor.GREEN;
        }
        gui.addElement(new StaticGuiElement('O',
                item,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + PermissionKeyO,
                ChatColor.BLUE + "Currently: " + color + permission,
                " ",
                ChatColor.BLUE + "If true: ",
                ChatColor.GRAY + "Player can change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(8)).getName(),
                " ",
                ChatColor.BLUE + "If false: ",
                ChatColor.GRAY + "Player cannot change the class of another player if ",
                ChatColor.GRAY + "the other player's class is " + nation.getClassFromID(String.valueOf(8)).getName()
        ));
        return gui;
    }
}
