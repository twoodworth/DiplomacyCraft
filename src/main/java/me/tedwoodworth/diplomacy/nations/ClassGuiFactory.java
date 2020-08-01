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
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;

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
                "    j    ",
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
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
                    " ",//TODO add click command
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));
        }

        if (Objects.equals(nation, playerNation)) {
            gui.addElement(new StaticGuiElement('j',
                    new ItemStack(Material.PLAYER_HEAD),
                    click -> {
                        var nGui = createChangeRankList(nation, player, "alphabet", 0);
                        nGui.show(player);
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Change a player's class",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "List players",//TODO add click command
                    " ",
                    ChatColor.DARK_GRAY + "Players will only be listed",
                    ChatColor.DARK_GRAY + "if you have sufficient",
                    ChatColor.DARK_GRAY + "permissions to change",
                    ChatColor.DARK_GRAY + "their class."
            ));
        }

        var banner = nation.getBanner();
        var bannerMeta = (BannerMeta) banner.getItemMeta();
        bannerMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        banner.setItemMeta(bannerMeta);

        gui.addElement(new StaticGuiElement('k',
                banner,
                click -> {
                    var nGui = NationGuiFactory.create(nation, player);
                    nGui.show(player);
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

    public static InventoryGui createChangeRankList(Nation nation, Player player, String sortType, int slot) {
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
        var title = "" + color + ChatColor.BOLD + nation.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Change Class";
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
                        var nGui = createChangeRankList(nation, player, "alphabet", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createChangeRankList(nation, player, "reverseAlphabet", slot);
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
                        var nGui = createChangeRankList(nation, player, "balance", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createChangeRankList(nation, player, "reverseBalance", slot);
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
                        var nGui = createChangeRankList(nation, player, "class", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createChangeRankList(nation, player, "reverseClass", slot);
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
                        var nGui = createChangeRankList(nation, player, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createChangeRankList(nation, player, "reverseAge", slot);
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
                        var nGui = createChangeRankList(nation, player, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createChangeRankList(nation, player, sortType, nSlot);
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
                            var nGui = createChangeRankList(nation, player, sortType, nSlot);
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > nation.getMembers().size() + (5 - nation.getMembers().size() % 5) - 30) {
                            nSlot = nation.getMembers().size() + (5 - nation.getMembers().size() % 5) - 30;
                        }
                        if (nation.getMembers().size() > 30) {
                            var nGui = createChangeRankList(nation, player, sortType, nSlot);
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
                new ItemStack(Material.NETHER_STAR),
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

        List<OfflinePlayer> members = new ArrayList<>();

        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();

        for (int index = 0; index < 8; index++) {
            var next = String.valueOf(index + 1);
            if (permissions.get("CanRemoveFromClass" + next)) {
                for (var memberStr : nation.getMembers()) {
                    var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(UUID.fromString(memberStr));
                    if (nation.getMemberClass(testDiplomacyPlayer).getClassID().equals(String.valueOf(index))) {
                        members.add(Bukkit.getOfflinePlayer(testDiplomacyPlayer.getUUID()));
                    }
                }
            }
        }

        var leaderCount = 0;
        for (var memberStr : nation.getMembers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(UUID.fromString(memberStr));
            if (nation.getMemberClass(testDiplomacyPlayer).getClassID().equals("8")) {
                leaderCount++;
            }
        }

        if (leaderCount > 1 && permissions.get("CanRemoveFromClass9")) {
            for (var memberStr : nation.getMembers()) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(UUID.fromString(memberStr));
                if (nation.getMemberClass(testDiplomacyPlayer).getClassID().equals("8")) {
                    members.add(Bukkit.getOfflinePlayer(testDiplomacyPlayer.getUUID()));
                }
            }
        }


        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            members.stream()
                    .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createChangeRankListElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            members.stream()
                    .sorted((p1, p2) -> -p1.getName().compareToIgnoreCase(p2.getName()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createChangeRankListElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            members.stream()
                    .sorted(comparingDouble(p -> -Diplomacy.getEconomy().getBalance(p)))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createChangeRankListElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            members.stream()
                    .sorted(comparingDouble(p -> Diplomacy.getEconomy().getBalance(p)))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createChangeRankListElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("class")) {
            members.stream()
                    .sorted(comparingInt(p -> -Integer.parseInt(nation.getMemberClass(DiplomacyPlayers.getInstance().get(p.getUniqueId())).getClassID())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createChangeRankListElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseClass")) {
            members.stream()
                    .sorted(comparingInt(p -> Integer.parseInt(nation.getMemberClass(DiplomacyPlayers.getInstance().get(p.getUniqueId())).getClassID())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createChangeRankListElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            members.stream()
                    .sorted((p1, p2) -> (int) -((DiplomacyPlayers.getInstance().get(p1.getUniqueId()).getAge() - DiplomacyPlayers.getInstance().get(p2.getUniqueId()).getAge())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createChangeRankListElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            members.stream()
                    .sorted((p1, p2) -> (int) (DiplomacyPlayers.getInstance().get(p1.getUniqueId()).getAge() - DiplomacyPlayers.getInstance().get(p2.getUniqueId()).getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createChangeRankListElement(nation, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private static StaticGuiElement createChangeRankListElement(Nation nation, Player player, OfflinePlayer member, char slot) {
        var memberHead = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (memberHead.getItemMeta());
        skullMeta.setOwningPlayer(member);

        memberHead.setItemMeta(skullMeta);

        var diplomacyMember = DiplomacyPlayers.getInstance().get(member.getUniqueId());
        var memberClass = nation.getMemberClass(diplomacyMember).getName();

        return new StaticGuiElement(slot,
                memberHead,
                click -> {
                    var nGui = createSelectNewRankList(nation, player, member);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + member.getName(),
                ChatColor.BLUE + "Class: " + ChatColor.GRAY + memberClass,
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(member)),
                ChatColor.BLUE + "Joined: " + ChatColor.GRAY + DiplomacyPlayers.getInstance().get(member.getUniqueId()).getDateJoined()
        );
    }

    private static InventoryGui createSelectNewRankList(Nation nation, Player player, OfflinePlayer member) {
        var title = "" + ChatColor.DARK_GRAY + ChatColor.BOLD + member.getName() + ChatColor.DARK_GREEN + ChatColor.BOLD + " Change Class";
        String[] guiSetup = {
                "        k",
                "    j    ",
                "         ",
                "abcdefghi",
                "         ",
                "        l"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);

        gui.setFiller(new ItemStack(Material.LIME_STAINED_GLASS_PANE));


        var classes = nation.getClasses();

        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var otherDiplomacyPlayer = DiplomacyPlayers.getInstance().get(member.getUniqueId());
        var currentClass = nation.getMemberClass(otherDiplomacyPlayer);

        if (permissions.get("CanSetClassTo1") && !currentClass.getClassID().equals("0")) {
            var class0prefix = "";
            if (classes.get(0).getPrefix() != null) {
                class0prefix = classes.get(0).getPrefix();
            }
            gui.addElement(new StaticGuiElement('a',
                    new ItemStack(Material.DIRT),
                    click -> {
                        nation.setMemberClass(otherDiplomacyPlayer, classes.get(0));
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(0).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class0prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(0).getTax()),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Set class to " + classes.get(0).getName()
            ));
        }

        if (permissions.get("CanSetClassTo2") && !currentClass.getClassID().equals("1")) {
            var class1prefix = "";
            if (classes.get(1).getPrefix() != null) {
                class1prefix = classes.get(1).getPrefix();
            }
            gui.addElement(new StaticGuiElement('b',
                    new ItemStack(Material.STONE),
                    click -> {
                        nation.setMemberClass(otherDiplomacyPlayer, classes.get(1));
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(1).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class1prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(1).getTax()),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Set class to " + classes.get(1).getName()
            ));
        }

        if (permissions.get("CanSetClassTo3") && !currentClass.getClassID().equals("2")) {
            var class2prefix = "";
            if (classes.get(2).getPrefix() != null) {
                class2prefix = classes.get(2).getPrefix();
            }
            gui.addElement(new StaticGuiElement('c',
                    new ItemStack(Material.IRON_BLOCK),
                    click -> {
                        nation.setMemberClass(otherDiplomacyPlayer, classes.get(2));
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(2).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class2prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(2).getTax()),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Set class to " + classes.get(2).getName()
            ));
        }

        if (permissions.get("CanSetClassTo4") && !currentClass.getClassID().equals("3")) {
            var class3prefix = "";
            if (classes.get(3).getPrefix() != null) {
                class3prefix = classes.get(3).getPrefix();
            }
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.GOLD_BLOCK),
                    click -> {
                        nation.setMemberClass(otherDiplomacyPlayer, classes.get(3));
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(3).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class3prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(3).getTax()),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Set class to " + classes.get(3).getName()
            ));
        }

        if (permissions.get("CanSetClassTo5") && !currentClass.getClassID().equals("4")) {
            var class4prefix = "";
            if (classes.get(4).getPrefix() != null) {
                class4prefix = classes.get(4).getPrefix();
            }
            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.DIAMOND_BLOCK),
                    click -> {
                        nation.setMemberClass(otherDiplomacyPlayer, classes.get(4));
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(4).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class4prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(4).getTax()),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Set class to " + classes.get(4).getName()
            ));
        }

        if (permissions.get("CanSetClassTo6") && !currentClass.getClassID().equals("5")) {
            var class5prefix = "";
            if (classes.get(5).getPrefix() != null) {
                class5prefix = classes.get(5).getPrefix();
            }
            gui.addElement(new StaticGuiElement('f',
                    new ItemStack(Material.OBSIDIAN),
                    click -> {
                        nation.setMemberClass(otherDiplomacyPlayer, classes.get(5));
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(5).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class5prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(5).getTax()),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Set class to " + classes.get(5).getName()
            ));
        }

        if (permissions.get("CanSetClassTo7") && !currentClass.getClassID().equals("6")) {
            var class6prefix = "";
            if (classes.get(6).getPrefix() != null) {
                class6prefix = classes.get(6).getPrefix();
            }
            gui.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.ANCIENT_DEBRIS),
                    click -> {
                        nation.setMemberClass(otherDiplomacyPlayer, classes.get(6));
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(6).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class6prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(6).getTax()),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Set class to " + classes.get(6).getName()
            ));
        }

        if (permissions.get("CanSetClassTo8") && !currentClass.getClassID().equals("7")) {
            var class7prefix = "";
            if (classes.get(7).getPrefix() != null) {
                class7prefix = classes.get(7).getPrefix();
            }
            gui.addElement(new StaticGuiElement('h',
                    new ItemStack(Material.NETHERITE_BLOCK),
                    click -> {
                        nation.setMemberClass(otherDiplomacyPlayer, classes.get(7));
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(7).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class7prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(7).getTax()),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Set class to " + classes.get(7).getName()
            ));
        }

        if (permissions.get("CanSetClassTo9") && !currentClass.getClassID().equals("8")) {
            var class8prefix = "";
            if (classes.get(8).getPrefix() != null) {
                class8prefix = classes.get(8).getPrefix();
            }
            gui.addElement(new StaticGuiElement('i',
                    new ItemStack(Material.BEDROCK),
                    click -> {
                        nation.setMemberClass(otherDiplomacyPlayer, classes.get(8));
                        gui.close();
                        return true;
                    },
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(8).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class8prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(classes.get(8).getTax()),
                    " ",
                    ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Set class to " + classes.get(8).getName()
            ));
        }

        var classMaterial = Material.DIRT;
        switch (currentClass.getClassID()) {
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
        if (currentClass.getPrefix() != null) {
            currentPrefix = currentClass.getPrefix();
        }

        gui.addElement(new StaticGuiElement('j',
                classItem,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Current Class",
                "" + ChatColor.GRAY + currentClass.getName(),
                ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + currentPrefix,
                ChatColor.BLUE + "Tax: " + ChatColor.GRAY + "\u00A4" + formatter.format(currentClass.getTax())
        ));

        gui.addElement(new StaticGuiElement('k',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var nGui = createChangeRankList(nation, player, "alphabet", 0);
                    nGui.show(player);
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
                "  ABCD  E",
                "abcdefghF",
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
                ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(nationClass.getTax())
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
                "  ABCD  E",
                "abcdefghF",
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
                ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(nationClass.getTax())
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
                click -> true,
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
