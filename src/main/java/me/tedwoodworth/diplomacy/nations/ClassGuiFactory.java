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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(0).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class0prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(0).getTax()),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(1).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class1prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(1).getTax()),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(2).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class2prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(2).getTax()),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(3).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class3prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(3).getTax()),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(4).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class4prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(4).getTax()),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(5).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class5prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(5).getTax()),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(6).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class6prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(6).getTax()),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(7).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class7prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(7).getTax()),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(8).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class8prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(8).getTax()),
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
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(0).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class0prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(0).getTax()),
                    " ",//TODO add click command
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class1prefix = "";
            if (classes.get(1).getPrefix() != null) {
                class1prefix = classes.get(1).getPrefix();
            }
            gui.addElement(new StaticGuiElement('b',
                    new ItemStack(Material.STONE),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(1).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class1prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(1).getTax()),
                    " ",//TODO add click command
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class2prefix = "";
            if (classes.get(2).getPrefix() != null) {
                class2prefix = classes.get(2).getPrefix();
            }
            gui.addElement(new StaticGuiElement('c',
                    new ItemStack(Material.IRON_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(2).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class2prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(2).getTax()),
                    " ",//TODO add click command
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class3prefix = "";
            if (classes.get(3).getPrefix() != null) {
                class3prefix = classes.get(3).getPrefix();
            }
            gui.addElement(new StaticGuiElement('d',
                    new ItemStack(Material.GOLD_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(3).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class3prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(3).getTax()),
                    " ",//TODO add click command
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class4prefix = "";
            if (classes.get(4).getPrefix() != null) {
                class4prefix = classes.get(4).getPrefix();
            }
            gui.addElement(new StaticGuiElement('e',
                    new ItemStack(Material.DIAMOND_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(4).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class4prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(4).getTax()),
                    " ",//TODO add click command
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class5prefix = "";
            if (classes.get(5).getPrefix() != null) {
                class5prefix = classes.get(5).getPrefix();
            }
            gui.addElement(new StaticGuiElement('f',
                    new ItemStack(Material.OBSIDIAN),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(5).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class5prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(5).getTax()),
                    " ",//TODO add click command
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class6prefix = "";
            if (classes.get(6).getPrefix() != null) {
                class6prefix = classes.get(6).getPrefix();
            }
            gui.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.ANCIENT_DEBRIS),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(6).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class6prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(6).getTax()),
                    " ",//TODO add click command
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class7prefix = "";
            if (classes.get(7).getPrefix() != null) {
                class7prefix = classes.get(7).getPrefix();
            }
            gui.addElement(new StaticGuiElement('h',
                    new ItemStack(Material.NETHERITE_BLOCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(7).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class7prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(7).getTax()),
                    " ",//TODO add click command
                    ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "View class settings"
            ));

            var class8prefix = "";
            if (classes.get(8).getPrefix() != null) {
                class8prefix = classes.get(8).getPrefix();
            }
            gui.addElement(new StaticGuiElement('i',
                    new ItemStack(Material.BEDROCK),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + classes.get(8).getName(),
                    ChatColor.BLUE + "Prefix: " + ChatColor.GRAY + class8prefix,
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(8).getTax()),
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
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(0).getTax()),
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
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(1).getTax()),
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
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(2).getTax()),
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
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(3).getTax()),
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
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(4).getTax()),
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
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(5).getTax()),
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
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(6).getTax()),
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
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(7).getTax()),
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
                    ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(classes.get(8).getTax()),
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
                ChatColor.BLUE + "Tax: " + ChatColor.GRAY + formatter.format(currentClass.getTax())
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

}
