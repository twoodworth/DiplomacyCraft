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
        var title = nation.getName();
        String[] guiSetup = {
                "         ",
                " a       ",
                "         ",
                "         ",
                "         ",
                "         "
        };
        InventoryGui gui = new InventoryGui(Diplomacy.getInstance(), player, title, guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS, 1));

        gui.addElement(new StaticGuiElement('a',
                new ItemStack(Material.NAME_TAG),
                click -> {
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                    var testNation = Nations.getInstance().get(diplomacyPlayer);
                    if (Objects.equals(testNation, nation)) {
                        Nations.getInstance().rename(nation, name, diplomacyPlayer);
                        return true;
                    }
                    player.sendMessage(ChatColor.RED + "You are not a member of this nation.");
                    return true;
                },
                ChatColor.BOLD + "Nation Name",
                nation.getName(),
                "Click: Change name"
        ));
        return gui;
    }
}
