package me.tedwoodworth.diplomacy.nations;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

public class Nation {

    private ConfigurationSection configSection;
    private String nationID;
    private String name;
    private List<UUID> leaderIDs;
    private List<NationClass> classes;
    private List<MemberInfo> memberInfos;
    private List<NationGroup> groups;
    private int balance;
    private long created;
    private UUID founderID;
    private String color;
    private List<UUID> outlawIDs;
    private List<String> allyNationIDs;
    private List<String> enemyNationIDs;

    Nation(String nationID, ConfigurationSection nationSection) {
        this.configSection = nationSection;
        this.nationID = nationID;
        this.name = nationSection.getString("Name");
        this.leaderIDs = new ArrayList<>();
        var leadersStr = nationSection.getStringList("Leaders");
        for (var LeaderID : leadersStr) {
            leaderIDs.add(UUID.fromString(LeaderID));
        }

        this.classes = new ArrayList<>(9);
        for (var classID = 0; classID < 9; classID++) {
            var strClassID = String.valueOf(classID);
            var classSection = nationSection.getConfigurationSection("Classes." + classID);
            var className = classSection.getString("Name");
            var classPrefix = classSection.getString("Prefix");
            var classTax = classSection.getInt("Tax");
            Map<String, Boolean> classPermissions = new HashMap<>();
            var permissionSection = classSection.getConfigurationSection("Permissions");
            var permissionSet = permissionSection.getKeys(false);
            for (var permission : permissionSet) {
                classPermissions.put(permission, permissionSection.getBoolean(permission));
            }
            var nationClass = new NationClass(strClassID, className, classPrefix, classTax, classPermissions, nationID);
            classes.add(nationClass);
        }

        this.memberInfos = new ArrayList<>();
        var membersStr = nationSection.getConfigurationSection("Members").getKeys(false);
        for (var memberID : membersStr) {
            var member = DiplomacyPlayers.getInstance().get(UUID.fromString(memberID));
            var memberClassID = nationSection.getString("Members." + memberID);
            var memberInfo = new MemberInfo(member, memberClassID);
            memberInfos.add(memberInfo);
        }

        var stringGroupIDs = Objects.requireNonNull(nationSection.getConfigurationSection("Groups")).getKeys(false);
        List<NationGroup> groups = new ArrayList<>();
        for (var groupID : stringGroupIDs) {
            var groupName = nationSection.getString("Groups." + groupID + ".Name");
            List<DiplomacyChunk> chunks = new ArrayList<>();
            var chunkMaps = nationSection.getMapList("Groups." + groupID + ".Chunks");
            for (Map map : chunkMaps) {
                var world = Bukkit.getWorld(String.valueOf(map.get("world")));
                var x = (int) map.get("x");
                var z = (int) map.get("z");
                var chunk = world.getChunkAt(x, z);
                var diplomacyChunk = new DiplomacyChunk(chunk);
                chunks.add(diplomacyChunk);
            }
            var group = new NationGroup(Integer.parseInt(groupID), groupName, chunks);
            groups.add(group);
        }
        this.balance = nationSection.getInt("Balance");
        this.created = nationSection.getInt("Created");
        this.founderID = UUID.fromString(Objects.requireNonNull(nationSection.getString("Founder")));
        this.color = nationSection.getString("Color");
        this.outlawIDs = new ArrayList<>();
        var outlawsStr = nationSection.getStringList("Outlaws");
        for (var outlaw : outlawsStr) {
            outlawIDs.add(UUID.fromString(outlaw));
        }
        this.allyNationIDs = nationSection.getStringList("AllyNations");
        this.enemyNationIDs = nationSection.getStringList("EnemyNations");
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var nation = (Nation) o;
        return nationID.equals(nation.nationID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nationID);
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

    public Stream<DiplomacyChunk> getChunks() {


        var chunkMaps = configSection.getMapList("Chunks");
        return chunkMaps.stream()
                .map(this::configMapToChunk);
    }

    private Map<?, ?> chunkToConfigMap(DiplomacyChunk diplomacyChunk) {
        return ImmutableMap.of(
                "world", diplomacyChunk.getChunk().getWorld().getName(),
                "x", diplomacyChunk.getChunk().getX(),
                "z", diplomacyChunk.getChunk().getZ()
        );
    }

    private DiplomacyChunk configMapToChunk(Map<?, ?> map) {
        var world = Bukkit.getWorld(String.valueOf(map.get("world")));
        int x = (Integer) map.get("x");
        int z = (Integer) map.get("z");

        var chunk = world.getChunkAt(x, z);

        return new DiplomacyChunk(chunk);
    }

    public void addChunk(DiplomacyChunk diplomacyChunk) {
        var list = configSection.getMapList("Chunks");
        list.add(chunkToConfigMap(diplomacyChunk));
        configSection.set("Chunks", list);
    }

    public void removeChunk(DiplomacyChunk diplomacyChunk) {
        var list = configSection.getMapList("Chunks");
        list.remove(chunkToConfigMap(diplomacyChunk));
        configSection.set("Chunks", list);
    }

    public List<NationGroup> getGroups() {
        return groups;
    }

    public NationClass getNationClass(String classID) {
        for (var nationClass : classes) {
            if (nationClass.getClassID().equals(classID)) {
                return nationClass;
            }
        }
        return null;
    }

    public List<String> getAllyNationIDs() {
        return allyNationIDs;
    }

}
