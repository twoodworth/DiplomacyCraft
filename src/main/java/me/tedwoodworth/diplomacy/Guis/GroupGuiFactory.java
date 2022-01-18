package me.tedwoodworth.diplomacy.Guis;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Guis.Guis;
import me.tedwoodworth.diplomacy.Guis.NationGuiFactory;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
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

    public static InventoryGui create(DiplomacyGroup group) {
        var groupNation = group.getNation();
        var color = ChatColor.GREEN;
        var title = "" + color + ChatColor.BOLD + group.getName() + ChatColor.DARK_GRAY + ChatColor.BOLD + " Main";
        String[] guiSetup = {
                "        l",
                "        h",
                "   bag  i",
                "   ced  j",
                "    f    ",
                "        k"
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), title, guiSetup);
        gui.setCloseAction(close -> false);
        gui.setFiller(new ItemStack(Material.GREEN_STAINED_GLASS_PANE));

        gui.addElement(new StaticGuiElement('l',
                new ItemStack(Material.PAINTING),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = NationGuiFactory.createMenu((Player) clicker);
                    nGui.show(clicker);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Menu",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Go to menu"
        ));

        gui.addElement(new StaticGuiElement('a',
                new ItemStack(Material.NAME_TAG),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Group Name:",
                ChatColor.GRAY + group.getName(),
                " ",
                ChatColor.BLUE + "Change Name: " + ChatColor.GRAY + "/group rename " + group.getName() + " <name>"
        ));

        gui.addElement(new StaticGuiElement('b',
                group.getShield(),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Group Banner:",
                ChatColor.BLUE + "Change Banner: " + ChatColor.GRAY + "/group banner " + group.getName()
        ));

        gui.addElement(new StaticGuiElement('c',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    clicker.sendMessage(ChatColor.GREEN + group.getName() + " members: ");
                    for (var member : group.getMembers()) {
                        clicker.sendMessage(ChatColor.GREEN + "- " + member.getOfflinePlayer().getName());
                    }
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "List members"
        ));

        var banner = group.getNation().getBanner();
        var itemMeta = banner.getItemMeta();
        if (itemMeta != null) {
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        }
        banner.setItemMeta(itemMeta);

        gui.addElement(new StaticGuiElement('d',
                banner,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = Guis.getInstance().getNationMenu(group.getNation());
                    nGui.show(clicker);
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
                    var clicker = click.getEvent().getWhoClicked();
                    var nGui = NationGuiFactory.createPlayer(founder);
                    nGui.show(clicker);
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
                    var clicker = click.getEvent().getWhoClicked();
                    Nations.getInstance().listNations((Player) clicker, 1);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Nations",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all nations"
        ));
        gui.addElement(new StaticGuiElement('i',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
//                    var clicker = click.getEvent().getWhoClicked(); todo fix playerMenus
//                    var nGui = Guis.getInstance().getPlayers("alphabetically");
//                    nGui.show(clicker);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "All Players",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "View all players",
                ChatColor.RED + "Currently under maintenance"
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
}
