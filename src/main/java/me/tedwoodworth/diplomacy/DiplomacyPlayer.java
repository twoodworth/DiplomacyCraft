package me.tedwoodworth.diplomacy;

import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.NationClass;
import me.tedwoodworth.diplomacy.nations.NationManager;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public class DiplomacyPlayer {
    private static NamespacedKey nationKey = new NamespacedKey(Diplomacy.getInstance(), "nation");
    private OfflinePlayer player;
    private UUID uuid;
    private static NationClass nationClass;
    private static List<String> groups;
    private static List<String> groupsLed;
    private String nationID;


    public DiplomacyPlayer(OfflinePlayer player, UUID uuid, String nationID, NationClass nationClass, List<String> groups, List<String> groupsLed) {
        this.player = player;
        this.uuid = uuid;
        this.nationClass = nationClass;
        this.groups = groups;
        this.groupsLed = groupsLed;
        this.nationID = nationID;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public NationClass getClass(UUID uuid) {
        return nationClass;
    }

    public static List<String> getGroups(UUID uuid) {
        return groups;
    }

    public static List<String> getGroupsLed(UUID uuid) {
        return groupsLed;
    }

    public Nation getNation() {
        for (Nation nation : NationManager.getNations()) {
            if (Nation.getNationID().equals(nationID)) {
                return nation;
            }
        }
        return null;
    }

    public static void setNationClass(UUID uuid, NationClass newNationClass) {
        nationClass = newNationClass;
    }

//    public Nation getNation() {
//        return new Nation(player.getPersistentDataContainer().get(nationKey, PersistentDataType.STRING));
//    }

//    public void setNation(Nation nation) {
//        player.getPersistentDataContainer().set(nationKey, PersistentDataType.STRING, nation.getName());
//    }
}

