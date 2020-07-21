package me.tedwoodworth.diplomacy.players;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DiplomacyPlayers {

    private static DiplomacyPlayers instance = null;
    private File diplomacyPlayerConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "diplomacyPlayers.yml");
    private Map<UUID, DiplomacyPlayer> diplomacyPlayers = new WeakHashMap<>();
    private YamlConfiguration config;

    public static DiplomacyPlayers getInstance() {
        if (instance == null) {
            instance = new DiplomacyPlayers();
        }
        return instance;
    }

    private DiplomacyPlayers() {
        config = YamlConfiguration.loadConfiguration(diplomacyPlayerConfigFile);
        save();
    }

    public DiplomacyPlayer get(UUID uuid) {
        List<String> groups = new ArrayList<>(1);
        List<String> groupsLed = new ArrayList<>(1);
        var player = diplomacyPlayers.get(uuid);
        if (player == null) {
            Map<String, Object> playersMap = ImmutableMap.of(
                    "Groups", groups,
                    "GroupsLed", groupsLed);

            config.createSection(uuid.toString(), playersMap);
            player = new DiplomacyPlayer(uuid, config.getConfigurationSection(uuid.toString()));
            diplomacyPlayers.put(uuid, player);
        }
        return player;
    }

    public List<DiplomacyPlayer> getLeaders(DiplomacyGroup group) {
        List<DiplomacyPlayer> leaders = new ArrayList<>();

        for (var player : diplomacyPlayers.values()) {
            if (player.getGroupsLed().contains(group)) {
                leaders.add(player);
            }
        }
        return leaders;
    }

    public List<DiplomacyPlayer> getMembers(DiplomacyGroup group) {
        List<DiplomacyPlayer> members = new ArrayList<>();

        for (var player : diplomacyPlayers.values()) {
            if (player.getGroups().contains(group)) {
                members.add(player);
            }
        }
        return members;
    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    public void save() {
        try {
            config.save(diplomacyPlayerConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        private void onPlayerMove(PlayerMoveEvent event) {
            var fromChunk = event.getFrom().getChunk();
            var toChunk = event.getTo().getChunk();
            if (!fromChunk.equals(toChunk)) {
                var fromDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(fromChunk);
                var toDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(toChunk);

                var fromNation = fromDiplomacyChunk.getNation();
                var toNation = toDiplomacyChunk.getNation();

                if (!Objects.equals(fromNation, toNation)) {
                    var player = event.getPlayer();
                    if (toNation == null) {
                        player.sendTitle(ChatColor.GRAY + "Wilderness", null, 5, 30, 5);
                    } else {
                        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                        Nation nation = Nations.getInstance().get(diplomacyPlayer);
                        if (nation == null) {
                            player.sendTitle(ChatColor.BLUE + toNation.getName(), null, 5, 40, 10);
                        } else if (toNation.getEnemyNationIDs().contains(nation.getNationID())) {
                            player.sendTitle(ChatColor.RED + toNation.getName(), null, 5, 40, 10);
                        } else if (toNation.getAllyNationIDs().contains(nation.getNationID()) || toNation.equals(nation)) {
                            player.sendTitle(ChatColor.GREEN + toNation.getName(), null, 5, 40, 10);
                        } else {
                            player.sendTitle(ChatColor.BLUE + toNation.getName(), null, 5, 40, 10);
                        }
                    }
                }
            }
        }
    }
}
