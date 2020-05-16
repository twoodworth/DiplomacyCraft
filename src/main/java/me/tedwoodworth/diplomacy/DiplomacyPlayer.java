package me.tedwoodworth.diplomacy;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class DiplomacyPlayer {
    private static NamespacedKey nationKey = new NamespacedKey(Diplomacy.getInstance(), "nation");
    private Player player;

    public DiplomacyPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Nation getNation() {
        return new Nation(player.getPersistentDataContainer().get(nationKey, PersistentDataType.STRING));
    }

    public void setNation(Nation nation) {
        player.getPersistentDataContainer().set(nationKey, PersistentDataType.STRING, nation.getName());
    }
}

