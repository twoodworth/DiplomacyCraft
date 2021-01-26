package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.events.*;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static me.tedwoodworth.diplomacy.nations.NationGuiFactory.formatter;

public class Guis {
    private static Guis instance = null;

    private final Map<String, InventoryGui> nationMenus;

    public static Guis getInstance() {
        if (instance == null) {
            instance = new Guis();
        }
        return instance;
    }

    public Guis() {
        nationMenus = new HashMap<>();
    }

    public void loadNationMenus() {
        for (var nation : new ArrayList<>(Nations.getInstance().getNations())) {
            getInstance().getNationMenu(nation);
        }
    }

    public InventoryGui getNationMenu(Nation nation) {
        if (!nationMenus.containsKey(nation.getNationID())) {
            var menu = NationGuiFactory.create(nation);
            nationMenus.put(nation.getNationID(), menu);
        }
        return nationMenus.get(nation.getNationID());
    }

    public StaticGuiElement getStatusElement(Nation nation) {
        var isOpen = nation.getIsOpen();
        // Create strStatus & ItemStack
        String status;
        ItemStack itemStack;
        if (isOpen) {
            status = "Open";
            itemStack = new ItemStack(Material.OAK_DOOR); //TODO figure out why door doesn't change when clicking from player menu
        } else {
            status = "Closed";
            itemStack = new ItemStack(Material.IRON_DOOR);
        }

        // Border status element
        return new StaticGuiElement('k',
                itemStack,
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    var diplomacyClicker = DiplomacyPlayers.getInstance().get(clicker.getUniqueId());
                    var testNation = Nations.getInstance().get(diplomacyClicker);
                    if (!Objects.equals(testNation, nation)) {
                        clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                    try {
                        var nationClass = Objects.requireNonNull(testNation).getMemberClass(diplomacyClicker);
                        var permissions = Objects.requireNonNull(nationClass).getPermissions();
                        if (!permissions.get("CanToggleBorder")) {
                            clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        } else {
                            nation.setIsOpen(!nation.getIsOpen());
                        }
                        return true;
                    } catch (NullPointerException e) {
                        clicker.sendMessage(ChatColor.RED + "You do not have permission.");
                        return true;
                    }
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Border Status:",
                ChatColor.GRAY + status,
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "Toggle border status",
                " ",
                ChatColor.BLUE + "Open Borders: " + ChatColor.GRAY + "/nation open",
                ChatColor.BLUE + "Close Borders: " + ChatColor.GRAY + "/nation close"
        );
    }

    public StaticGuiElement getBannerElement(Nation nation) {
        var banner = nation.getBanner();
        return new StaticGuiElement('b',
                banner,
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Banner:",
                " ",
                ChatColor.BLUE + "Change Banner: " + ChatColor.GRAY + "/nation banner"
        );
    }

    public StaticGuiElement getBalanceElement(Nation nation) {
        var nationWealth = nation.getBalance();
        var strNationWealth = "\u00A4" + formatter.format(nationWealth);
        return new StaticGuiElement('e',
                new ItemStack(Material.DIAMOND),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Balance",
                ChatColor.BLUE + "Balance: " + ChatColor.GRAY + strNationWealth,
                " ",
                ChatColor.BLUE + "Deposit: " + ChatColor.GRAY + "/nation deposit <amount>",
                ChatColor.BLUE + "Withdraw: " + ChatColor.GRAY + "/nation withdraw <amount>"

        );
    }

    public StaticGuiElement getPlotElement(Nation nation) {
        var plots = nation.getChunks().size();
        var label = " plots";
        if (plots == 1) {
            label = " plot";
        }
        return new StaticGuiElement('l',
                new ItemStack(Material.GRASS_BLOCK),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Territory Size",
                ChatColor.GRAY + String.valueOf(plots) + label
        );
    }

    public StaticGuiElement getNameElement(Nation nation) {
        return new StaticGuiElement('a',
                new ItemStack(Material.NAME_TAG),
                click -> true,
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Name:",
                ChatColor.GRAY + nation.getName(),
                " ",
                ChatColor.BLUE + "Change Name: " + ChatColor.GRAY + "/nation rename <name>"
        );
    }

    public StaticGuiElement getMembersElement(Nation nation) {
        return new StaticGuiElement('c',
                new ItemStack(Material.PLAYER_HEAD),
                click -> {
                    var clicker = click.getEvent().getWhoClicked();
                    Nations.getInstance().listMembers((Player) clicker, nation, 1);
                    return true;
                },
                "" + ChatColor.YELLOW + ChatColor.BOLD + "Members",
                ChatColor.BLUE + "Population: " + ChatColor.GRAY + nation.getMembers().size(),
                " ",
                ChatColor.BLUE + "Click: " + ChatColor.GRAY + "List members"
        );
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Guis.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onNationToggleBorder(NationToggleBorderEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(Guis.getInstance().getStatusElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationChangeBanner(NationChangeBannerEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(Guis.getInstance().getBannerElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationChangeBalance(NationChangeBalanceEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
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
            gui.draw();
        }

        @EventHandler
        private void onNationAddChunks(NationAddChunksEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(Guis.getInstance().getPlotElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationRemoveChunk(NationRemoveChunksEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(Guis.getInstance().getPlotElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationRename(NationRenameEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.setTitle("" + ChatColor.DARK_GRAY + ChatColor.BOLD + nation.getName() + " Main");
            gui.addElement(getNameElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationJoin(NationJoinEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(getMembersElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationLeave(NationLeaveEvent event) {
            var nation = event.getNation();
            var gui = getNationMenu(nation);
            gui.addElement(getMembersElement(nation));
            gui.draw();
        }

        @EventHandler
        private void onNationDisband(NationDisbandEvent event) {
            nationMenus.remove(event.getNationID());
        }

        @EventHandler
        private void onNationCreate(NationCreateEvent event) {
            var nation = event.getNation();
            nationMenus.put(nation.getNationID(), getNationMenu(nation));
        }
    }
}
