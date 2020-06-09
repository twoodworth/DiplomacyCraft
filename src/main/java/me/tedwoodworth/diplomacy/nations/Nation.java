package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.DiplomacyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class Nation {

    private static String nationID;
    private List<UUID> leaderIDs;
    private List<NationClass> classes;
    private List<DiplomacyPlayer> members;
    private int plotCount;
    private List<String> groups;
    private int balance;
    private int created;
    private UUID founderID;
    private String color;
    private List<UUID> outlawIDs;
    private List<String> allyNationIDs;
    private List<String> enemyNationIDs;
    private boolean isWilderness;
    private Set<Chunk> chunks;

    Nation(String nationID, ConfigurationSection nationSection) {
        String name = nationSection.getString("Name");
        this.leaderIDs = new ArrayList<>();
        List<String> leadersStr = nationSection.getStringList("Leaders");
        for (String LeaderID : leadersStr) {
            leaderIDs.add(UUID.fromString(LeaderID));
        }

        List<NationClass> classes = new ArrayList<>(9);
        for (int i = 0; i < 9; i++) {
            int classID = i + 1;
            String className = nationSection.getString("Classes." + classID + ".Name");
            String classPrefix = nationSection.getString("Classes." + classID + ".Prefix");
            int classTax = nationSection.getInt("Classes." + classID + ".Tax");
            List<Boolean> classPermissions = nationSection.getBooleanList("Classes." + classID + ".Permissions");
            NationClass nationClass = new NationClass(classID, className, classPrefix, classTax, classPermissions, nationID);
            classes.set(i, nationClass);
        }

        this.members = new ArrayList<>();
        Set<String> membersStr = nationSection.getConfigurationSection("members").getKeys(true);
        for (String memberID : membersStr) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(memberID));
            NationClass nationClass = classes.get(nationSection.getInt("Members." + memberID + ".class"));
            List<String> groups = nationSection.getStringList("Members." + memberID + ".Groups");
            List<String> groupsLed = nationSection.getStringList("Members." + memberID + ".GroupsLed");
            DiplomacyPlayer member = new DiplomacyPlayer(player, UUID.fromString(memberID), nationID, nationClass, groups, groupsLed);
            members.add(member);
        }

        this.plotCount = nationSection.getInt("name");
        Set<String> groupIDs = nationSection.getConfigurationSection("groups").getKeys(true);
        List<NationGroup> groups = new ArrayList<>();
        for (String groupID : groupIDs) {
            List<String> chunks = nationSection.getStringList("Groups." + groupID + "Chunks");
            String groupName = nationSection.getString("Groups." + groupID + ".Name");
            int intGroupID = Integer.parseInt(groupID);
            NationGroup group = new NationGroup(intGroupID, groupName, chunks);
            groups.add(group);
        }
        this.balance = nationSection.getInt("Balance");
        this.created = nationSection.getInt("Created");
        this.founderID = UUID.fromString(Objects.requireNonNull(nationSection.getString("Founder")));
        this.color = nationSection.getString("Color");
        this.outlawIDs = new ArrayList<>();
        List<String> outlawsStr = nationSection.getStringList("Outlaws");
        for (String outlaw : outlawsStr) {
            outlawIDs.add(UUID.fromString(outlaw));
        }
        this.allyNationIDs = nationSection.getStringList("AllyNations");
        this.enemyNationIDs = nationSection.getStringList("EnemyNations");
        this.isWilderness = nationSection.getBoolean("IsWilderness");
        this.chunks = new HashSet<>();
    }

    public static String getNationID() {
        return nationID;
    }

    public void setName(String nationID) {
        this.nationID = nationID;
    }
}
