package me.tedwoodworth.diplomacy.groups;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.NationGuiFactory;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.UUID;

import static java.util.Comparator.comparingDouble;
import static java.util.Comparator.comparingInt;

public class GroupGuiFactory {
    private static final DecimalFormat formatter = new DecimalFormat("#,##0.00");

    public static InventoryGui create(DiplomacyGroup group, Player player) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        var groupNation = group.getNation();
        var color = ChatColor.BLUE;
        if (playerNation != null) {
            if ((groupNation.getAllyNationIDs() != null && groupNation.getAllyNationIDs().contains(playerNation.getNationID())) || Objects.equals(groupNation, playerNation)) {
                color = ChatColor.DARK_GREEN;
            } else if (groupNation.getEnemyNationIDs() != null && groupNation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                color = ChatColor.RED;
            }
        }
        var title = "" + color + ChatColor.BOLD + group.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Main";
        String[] guiSetup = {
                "        l",
                "        h",
                "   bag  i",
                "   ced  j",
                "    f    ",
                "        k"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        var glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        if (playerNation != null) {
            if (Objects.equals(groupNation, playerNation) || groupNation.getAllyNationIDs().contains(playerNation.getNationID())) {
                glass = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            } else if (groupNation.getEnemyNationIDs().contains(playerNation.getNationID())) {
                glass = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
            }
        }

        gui.setFiller(glass);

        var isLeader = diplomacyPlayer.getGroupsLed().contains(group);
        var isSameNation = Objects.equals(playerNation, groupNation);
        var leadsAllGroups = false;
        if (playerNation != null) {
            var permissions = playerNation.getMemberClass(diplomacyPlayer).getPermissions();
            leadsAllGroups = isSameNation && permissions.get("CanLeadAllGroups");
        }

        gui.addElement(new StaticGuiElement('l',
                new ItemStack(Material.PAINTING),
                click -> {
                    var nGui = NationGuiFactory.createMenu(player);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Menu",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Go to menu"
        ));

        if (isLeader || leadsAllGroups) {
            gui.addElement(new StaticGuiElement('a',
                    new ItemStack(Material.NAME_TAG),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Group Name:",
                    ChatColor.GRAY + group.getName(),
                    " ",
                    ChatColor.BLUE + "Change Name: " + ChatColor.GRAY + "/group rename " + group.getName() + " <name>"
            ));
        } else {
            gui.addElement(new StaticGuiElement('a',
                    new ItemStack(Material.NAME_TAG),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Group Name:",
                    ChatColor.GRAY + group.getName()
            ));
        }
        if (isLeader || leadsAllGroups) {
            gui.addElement(new StaticGuiElement('b',
                    group.getShield(),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Group Banner:",
                    ChatColor.BLUE + "Change Banner: " + ChatColor.GRAY + "/group banner " + group.getName()
            ));
        } else {
            gui.addElement(new StaticGuiElement('b',
                    group.getShield(),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Group Banner:"
            ));
        }
        gui.addElement(new StaticGuiElement('c',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var nGui = createMembers(group, player, "alphabet", 0);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view members"
        ));

        var banner = group.getNation().getBanner();
        var itemMeta = banner.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        banner.setItemMeta(itemMeta);

        gui.addElement(new StaticGuiElement('d',
                banner,
                click -> {
                    var nGui = NationGuiFactory.create(group.getNation(), player);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation",
                ChatColor.GRAY + group.getNation().getName(),
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "view nation"
        ));

        var plots = group.getChunks().size();
        var label = " plots";
        if (plots == 1) {
            label = " plot";
        }
        gui.addElement(new StaticGuiElement('e',
                new ItemStack(Material.GRASS_BLOCK),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Territory Size",
                ChatColor.GRAY + String.valueOf(plots) + label
        ));

        var founderItem = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (founderItem.getItemMeta());
        var founder = Bukkit.getOfflinePlayer(UUID.fromString(group.getFounder()));
        Objects.requireNonNull(skullMeta).setOwningPlayer(founder);
        founderItem.setItemMeta(skullMeta);

        gui.addElement(new StaticGuiElement('f',
                founderItem,
                click -> {
                    var nGui = NationGuiFactory.createPlayer(player, founder);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Founder",
                ChatColor.GRAY + founder.getName()
        ));
        gui.addElement(new StaticGuiElement('g',
                new ItemStack(Material.CLOCK),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Date Founded:",
                ChatColor.GRAY + group.getDateCreated()
        ));

        gui.addElement(new StaticGuiElement('h',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    var nGui = NationGuiFactory.createNations(player, "alphabet", 0);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Nations",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all nations"
        ));
        gui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var nGui = NationGuiFactory.createPlayers(player, "alphabet", 0);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players"
        ));
        gui.addElement(new StaticGuiElement('j',
                new ItemStack(Material.SHIELD),
                click -> {
                    var nGui = NationGuiFactory.createAllGroups(player, "alphabet", 0);
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Groups",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all groups"
        ));
        gui.addElement(new StaticGuiElement('k',
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

    public static InventoryGui createMembers(DiplomacyGroup group, Player player, String sortType, int slot) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        var color = ChatColor.BLUE;
        if (playerNation != null) {
            if (group.getNation().getAllyNationIDs().contains(playerNation.getNationID()) || Objects.equals(group.getNation(), playerNation)) {
                color = ChatColor.DARK_GREEN;
            } else if (group.getNation().getEnemyNationIDs().contains(playerNation.getNationID())) {
                color = ChatColor.RED;
            }
        }
        var title = "" + color + ChatColor.BOLD + group.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Members";
        String[] guiSetup = {
                "A abcde N",
                "B fghij  ",
                "R klmno U",
                "C pqrst D",
                "G uvwxy  ",
                "  z{|}~ E"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        var glass = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        if (playerNation != null) {
            if (Objects.equals(group.getNation(), playerNation) || group.getNation().getAllyNationIDs().contains(playerNation.getNationID())) {
                glass = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
            } else if (group.getNation().getEnemyNationIDs().contains(playerNation.getNationID())) {
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
                        var nGui = createMembers(group, player, "alphabet", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(group, player, "reverseAlphabet", slot);
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
                        var nGui = createMembers(group, player, "balance", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(group, player, "reverseBalance", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Balance",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Largest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Smallest first"
        ));
        gui.addElement(new StaticGuiElement('R',
                new ItemStack(Material.NETHER_STAR),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createMembers(group, player, "role", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(group, player, "reverseRole", slot);
                        nGui.show(player);
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Sort By Role",
                ChatColor.BLUE + "Left Click: " + ChatColor.GRAY + "Highest first",
                ChatColor.BLUE + "Right Click: " + ChatColor.GRAY + "Lowest first"
        ));
        gui.addElement(new StaticGuiElement('C',
                new ItemStack(Material.BLUE_BANNER),
                click -> {
                    if (click.getType().isLeftClick()) {
                        var nGui = createMembers(group, player, "nation", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(group, player, "reverseNation", slot);
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
                        var nGui = createMembers(group, player, "age", slot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nGui = createMembers(group, player, "reverseAge", slot);
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
                        var nGui = createMembers(group, player, sortType, nSlot);
                        nGui.show(player);
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot - 30;
                        if (nSlot < 0) {
                            nSlot = 0;
                        }
                        var nGui = createMembers(group, player, sortType, nSlot);
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
                        if (nSlot > group.getMembers().size() + (5 - group.getMembers().size() % 5) - 30) {
                            nSlot = group.getMembers().size() + (5 - group.getMembers().size() % 5) - 30;
                        }
                        if (group.getMembers().size() > 30) {
                            var nGui = createMembers(group, player, sortType, nSlot);
                            nGui.show(player);
                        }
                    } else if (click.getType().isRightClick()) {
                        var nSlot = slot + 30;
                        if (nSlot > group.getMembers().size() + (5 - group.getMembers().size() % 5) - 30) {
                            nSlot = group.getMembers().size() + (5 - group.getMembers().size() % 5) - 30;
                        }
                        if (group.getMembers().size() > 30) {
                            var nGui = createMembers(group, player, sortType, nSlot);
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
                group.getShield(),
                click -> {
                    var nGui = create(group, player);
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

        var members = group.getMembers();

        var slotChar = new char[]{'a'};

        if (sortType.equals("alphabet")) {
            members.stream()
                    .sorted((p1, p2) -> Objects.requireNonNull(Bukkit.getOfflinePlayer(p1.getUUID()).getName()).compareToIgnoreCase(Objects.requireNonNull(Bukkit.getOfflinePlayer(p2.getUUID()).getName())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAlphabet")) {
            members.stream()
                    .sorted((p1, p2) -> -Objects.requireNonNull(Bukkit.getOfflinePlayer(p1.getUUID()).getName()).compareToIgnoreCase(Objects.requireNonNull(Bukkit.getOfflinePlayer(p2.getUUID()).getName())))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("balance")) {
            members.stream()
                    .sorted(comparingDouble(p -> -Diplomacy.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUUID()))))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseBalance")) {
            members.stream()
                    .sorted(comparingDouble(p -> Diplomacy.getEconomy().getBalance(Bukkit.getOfflinePlayer(p.getUUID()))))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("role")) {
            members.stream()
                    .sorted(comparingInt(group::getRole))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseRole")) {
            members.stream()
                    .sorted((p1, p2) -> -(group.getRole(p1) - group.getRole(p2)))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("nation")) {
            members.stream()
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
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseNation")) {
            members.stream()
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
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("age")) {
            members.stream()
                    .sorted((p1, p2) -> (int) -(p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        } else if (sortType.equals("reverseAge")) {
            members.stream()
                    .sorted((p1, p2) -> (int) (p1.getAge() - p2.getAge()))
                    .skip(slot)
                    .limit(30)
                    .forEach(member -> {
                        var element = createMemberElement(group, player, member, slotChar[0]++);
                        gui.addElement(element);
                    });
        }
        return gui;
    }

    private static StaticGuiElement createMemberElement(DiplomacyGroup group, Player player, DiplomacyPlayer member, char slot) {
        var memberHead = new ItemStack(Material.PLAYER_HEAD, 1);
        var skullMeta = (SkullMeta) (memberHead.getItemMeta());
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(member.getUUID()));
        memberHead.setItemMeta(skullMeta);

        var nation = group.getNation();
        var memberNation = Nations.getInstance().get(member);
        var offlinePlayer = Bukkit.getOfflinePlayer(member.getUUID());

        var role = "Unknown";
        var memberRole = group.getRole(member);
        if (memberRole == 0) {
            role = "Leader of all " + group.getNation().getName() + " groups";
        } else if (memberRole == 1) {
            role = "Leader";
        } else if (memberRole == 2) {
            role = "Member";
        }

        var strNation = "None";
        if (memberNation != null) {
            strNation = memberNation.getName();
        }

        return new StaticGuiElement(slot,
                memberHead,
                click -> {
                    var nGui = NationGuiFactory.createPlayer(player, Bukkit.getOfflinePlayer(member.getUUID()));
                    nGui.show(player);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + offlinePlayer.getName(),
                ChatColor.BLUE + "Nation: " + ChatColor.GRAY + strNation,
                ChatColor.BLUE + "Role: " + ChatColor.GRAY + role,
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + "\u00A4" + formatter.format(Diplomacy.getEconomy().getBalance(offlinePlayer)),
                ChatColor.BLUE + "Server Join Date: " + ChatColor.GRAY + DiplomacyPlayers.getInstance().get(member.getUUID()).getDateJoined()
        );
    }
}
