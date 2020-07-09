package me.tedwoodworth.diplomacy.nations;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Instant;
import java.util.*;

public class Nation {

    private static String nationID;
    private String name;
    private List<UUID> leaderIDs;
    private List<NationClass> classes;
    private List<MemberInfo> memberInfos;
    private List<String> groups;
    private int balance;
    private long created;
    private UUID founderID;
    private String color;
    private List<UUID> outlawIDs;
    private List<String> allyNationIDs;
    private List<String> enemyNationIDs;
    private List<DiplomacyChunk> chunks;

    Nation(String nationID, ConfigurationSection nationSection) {
        this.name = nationSection.getString("Name");
        this.leaderIDs = new ArrayList<>();
        List<String> leadersStr = nationSection.getStringList("Leaders");
        for (String LeaderID : leadersStr) {
            leaderIDs.add(UUID.fromString(LeaderID));
        }

        this.classes = new ArrayList<>(9);
        for (int classID = 0; classID < 9; classID++) {
            ConfigurationSection classSection = nationSection.getConfigurationSection("Classes." + classID);
            String className = classSection.getString("Name");
            String classPrefix = classSection.getString("Prefix");
            int classTax = classSection.getInt("Tax");
            Map<String, Boolean> classPermissions = new HashMap<>();
            ConfigurationSection permissionSection = classSection.getConfigurationSection("Permissions");
            Set<String> permissionSet = permissionSection.getKeys(false);
            for (String permission : permissionSet) {
                classPermissions.put(permission, permissionSection.getBoolean(permission));
            }
            NationClass nationClass = new NationClass(classID, className, classPrefix, classTax, classPermissions, nationID);
            classes.add(nationClass);
        }

        this.memberInfos = new ArrayList<>();
        Set<String> membersStr = nationSection.getConfigurationSection("Members").getKeys(false);
        for (String memberID : membersStr) {
            DiplomacyPlayer member = DiplomacyPlayers.getInstance().get(UUID.fromString(memberID));
            String memberClassID = nationSection.getString("Members.memberID");
            MemberInfo memberInfo = new MemberInfo(member, memberClassID);
            memberInfos.add(memberInfo);
        }

        Set<String> stringGroupIDs = Objects.requireNonNull(nationSection.getConfigurationSection("Groups")).getKeys(false);
        List<NationGroup> groups = new ArrayList<>();
        for (String groupID : stringGroupIDs) {
            String groupName = nationSection.getString("Groups." + groupID + ".Name");
            List<DiplomacyChunk> chunks = new ArrayList<>();
            List<Map<?, ?>> chunkMaps = nationSection.getMapList("Groups." + groupID + ".Chunks");
            for (Map map : chunkMaps) {
                String world = String.valueOf(map.get("world"));
                int x = (int) map.get("x");
                int z = (int) map.get("z");
                DiplomacyChunk chunk = new DiplomacyChunk(world, x, z);
                chunks.add(chunk);
            }
            NationGroup group = new NationGroup(Integer.parseInt(groupID), groupName, chunks);
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

        List<DiplomacyChunk> chunks = new ArrayList<>();
        List<Map<?, ?>> chunkMaps = nationSection.getMapList("Chunks");
        for (Map map : chunkMaps) {
            String world = String.valueOf(map.get("world"));
            int x = (int) map.get("x");
            int z = (int) map.get("z");
            DiplomacyChunk chunk = new DiplomacyChunk(world, x, z);
            chunks.add(chunk);
        }
        this.chunks = chunks;
    }

    public static ConfigurationSection initializeNation(ConfigurationSection nationSection, OfflinePlayer leader, String name) {
        Map<String, String> membersMap = ImmutableMap.of(
                leader.getUniqueId().toString(), "8");

        nationSection.createSection("Members", membersMap);
        nationSection.set("Founder", leader.getUniqueId().toString());

        List<String> leaders = new ArrayList<>(1);
        leaders.add(leader.getUniqueId().toString());
        nationSection.set("Leaders", leaders);
        nationSection.set("Created", Instant.now().getEpochSecond());
        nationSection.set("Name", name);
        return nationSection;
    }


    public String getNationID() {
        return nationID;
    }

    public List<MemberInfo> getMemberInfos() {
        return memberInfos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
