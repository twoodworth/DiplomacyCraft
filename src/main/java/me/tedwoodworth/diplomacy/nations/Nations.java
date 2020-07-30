package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.DiplomacyException;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getOfflinePlayer;

public class Nations {

    private static Nations instance = null;
    private final File nationConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "nations.yml");
    private List<Nation> nations = new ArrayList<>();
    private YamlConfiguration nationConfig;

    public List<Nation> getNations() {
        return nations;
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    public Nation createNation(String name, DiplomacyPlayer leader) {
        var founder = getOfflinePlayer(leader.getUUID());
        var nextNationID = nationConfig.getString("NextNationID");
        if (nextNationID == null) {
            nationConfig.set("NextNationID", "0");
            nextNationID = "0";
        }

        var nationID = nextNationID;
        nextNationID = String.valueOf(Integer.parseInt(nextNationID) + 1);

        Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Nation.yml")));
        ConfigurationSection nationSection = YamlConfiguration.loadConfiguration(reader);

        nationConfig.set("Nations." + nationID, nationSection);
        nationConfig.set("NextNationID", nextNationID);
        var initializedNationSection = Nation.initializeNation(nationSection, founder, name);
        var nation = new Nation(nationID, initializedNationSection);
        nations.add(nation);
        return nation;

    }

    public static Nations getInstance() {
        if (instance == null) {
            instance = new Nations();
        }
        return instance;
    }

    private Nations() {
        nationConfig = YamlConfiguration.loadConfiguration(nationConfigFile);
        var nationsSection = nationConfig.getConfigurationSection("Nations");
        if (nationsSection == null) {
            nationsSection = nationConfig.createSection("Nations");
        }
        for (var nationID : nationsSection.getKeys(false)) {
            var nationSection = nationConfig.getConfigurationSection("Nations." + nationID);
            if (nationSection == null) {
                nationConfig.set("Nations." + nationID, null);
            } else {
                var nation = new Nation(nationID, nationSection);
                nations.add(nation);
            }
        }

    }

    @Nullable
    public Nation get(DiplomacyPlayer player) {
        for (var nation : nations) {
            for (var member : nation.getMembers()) {
                var testPlayer = DiplomacyPlayers.getInstance().get(UUID.fromString(member));
                if (player.equals(testPlayer)) {
                    return nation;
                }
            }
        }
        return null;
    }

    @Nullable
    public Nation get(String name) {
        for (var nation : nations) {
            if (name.equalsIgnoreCase(nation.getName())) {
                return nation;
            }
        }
        return null;
    }

    @Nullable
    public Nation get(int id) {
        for (var nation : nations) {
            if (id == Integer.parseInt(nation.getNationID())) {
                return nation;
            }
        }
        return null;
    }

    @Nullable
    public Nation getFromID(String id) {
        for (var nation : nations) {
            if (id.equalsIgnoreCase(nation.getNationID())) {
                return nation;
            }
        }
        return null;
    }

    public void rename(Nation nation, String name) throws DiplomacyException {
        var oldName = nation.getName();
        if (oldName.equals(name)) {
            throw new DiplomacyException(ChatColor.DARK_RED + "The nation is already named " + ChatColor.GREEN + name + ChatColor.DARK_RED + ".");
        }

        var nameTaken = Nations.getInstance().get(name) != null;
        if (nameTaken) {
            throw new DiplomacyException(ChatColor.DARK_RED + "The name " + ChatColor.BLUE + name + ChatColor.DARK_RED + " is taken, choose another name.");
        }

        nationConfig.set("Nations." + nation.getNationID() + ".Name", name);
        nation.setName(name);

        for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            var color = ChatColor.BLUE;
            if (testNation != null) {
                if (testNation.getEnemyNationIDs().contains(nation.getNationID())) {
                    color = ChatColor.RED;
                } else if (testNation.getAllyNationIDs().contains(nation.getNationID()) || Objects.equals(nation, testNation)) {
                    color = ChatColor.GREEN;
                }
            }
            onlinePlayer.sendMessage(ChatColor.AQUA + "The nation of " + color + oldName + ChatColor.AQUA + " has been renamed to " + color + name + ChatColor.AQUA + ".");
        }
    }

    public void rename(Nation nation, String name, DiplomacyPlayer diplomacyPlayer) throws DiplomacyException {
        if (nation == null) {
            throw new DiplomacyException(ChatColor.DARK_RED + "You are not in a nation.");
        }

        var nationClass = nation.getMemberClass(diplomacyPlayer);
        boolean canRenameNation = nationClass.getPermissions().get("CanRenameNation");
        if (!canRenameNation) {
            throw new DiplomacyException(ChatColor.DARK_RED + "You do not have permission to rename the nation.");
        }
    }

    public void removeNation(Nation nation) {
        nationConfig.set("Nations." + nation.getNationID(), null);
        nations.remove(nation);
    }

    public static boolean isWilderness(Nation nation) {
        return nation == null;
    }

    public void save() {
        try {
            nationConfig.save(nationConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }
    }
}
