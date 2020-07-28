package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class NationGuiFactory {


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
                " ab      ",
                "         ",
                "         ",
                "         ",
                "         "
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
            gui.addElement(new StaticGuiElement('a',
                    new ItemStack(Material.NAME_TAG),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Name:",
                    color + nation.getName(),
                    " ",
                    ChatColor.BLUE + "Change Name:",
                    ChatColor.GRAY + "/nation rename <name>"
            ));
            gui.addElement(new StaticGuiElement('b',
                    nation.getBanner(),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Banner:",
                    " ",
                    ChatColor.BLUE + "Change Banner:",
                    ChatColor.GRAY + "/nation banner name"
            ));
        } else {
            gui.addElement(new StaticGuiElement('a',
                    new ItemStack(Material.NAME_TAG),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Name",
                    color + nation.getName()
            ));
            gui.addElement(new StaticGuiElement('b',
                    nation.getBanner(),
                    click -> true,
                    "" + ChatColor.YELLOW + ChatColor.BOLD + "Nation Banner:"
            ));
        }
        return gui;
    }
}
