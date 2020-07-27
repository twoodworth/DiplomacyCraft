package me.tedwoodworth.diplomacy.nations;

import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class NationGuiFactory {

    private static final JavaPlugin plugin = (JavaPlugin) Bukkit.getPluginManager().getPlugin("Diplomacy");

    public InventoryGui create(Nation nation, Player player) {
        var holder = (InventoryGui.Holder) player;
        var title = nation.getName();
        String[] guiSetup = {
                "         ",
                " a       ",
                "         ",
                "         ",
                "         ",
                "         "
        };
        InventoryGui gui = new InventoryGui(plugin, holder, title, guiSetup);
        gui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS, 1));

        gui.addElement(new StaticGuiElement('a',
                new ItemStack(Material.NAME_TAG),
                click -> {
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                    var testNation = Nations.getInstance().get(diplomacyPlayer);
                    if (Objects.equals(testNation, nation)) {
                        new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nation rename");
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
