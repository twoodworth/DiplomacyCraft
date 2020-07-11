package me.tedwoodworth.diplomacy.nations;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
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
        this.nationID = nationID;
        this.name = nationSection.getString("Name");
        this.leaderIDs = new ArrayList<>();
        List<String> leadersStr = nationSection.getStringList("Leaders");
        for (String LeaderID : leadersStr) {
            leaderIDs.add(UUID.fromString(LeaderID));
        }

        this.classes = new ArrayList<>(9);
        for (int classID = 0; classID < 9; classID++) {
            String strClassID = String.valueOf(classID);
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
            NationClass nationClass = new NationClass(strClassID, className, classPrefix, classTax, classPermissions, nationID);
            classes.add(nationClass);
        }

        this.memberInfos = new ArrayList<>();
        Set<String> membersStr = nationSection.getConfigurationSection("Members").getKeys(false);
        for (String memberID : membersStr) {
            DiplomacyPlayer member = DiplomacyPlayers.getInstance().get(UUID.fromString(memberID));
            String memberClassID = nationSection.getString("Members." + memberID);
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
                World world = Bukkit.getWorld(String.valueOf(map.get("world")));
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
            World world = Bukkit.getWorld(String.valueOf(map.get("world")));
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nation nation = (Nation) o;
        return balance == nation.balance &&
                created == nation.created &&
                com.google.common.base.Objects.equal(name, nation.name) &&
                com.google.common.base.Objects.equal(leaderIDs, nation.leaderIDs) &&
                com.google.common.base.Objects.equal(classes, nation.classes) &&
                com.google.common.base.Objects.equal(memberInfos, nation.memberInfos) &&
                com.google.common.base.Objects.equal(groups, nation.groups) &&
                com.google.common.base.Objects.equal(founderID, nation.founderID) &&
                com.google.common.base.Objects.equal(color, nation.color) &&
                com.google.common.base.Objects.equal(outlawIDs, nation.outlawIDs) &&
                com.google.common.base.Objects.equal(allyNationIDs, nation.allyNationIDs) &&
                com.google.common.base.Objects.equal(enemyNationIDs, nation.enemyNationIDs) &&
                com.google.common.base.Objects.equal(chunks, nation.chunks);
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(name, leaderIDs, classes, memberInfos, groups, balance, created, founderID, color, outlawIDs, allyNationIDs, enemyNationIDs, chunks);
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

    public List<DiplomacyChunk> getChunks() {
        return chunks;
    }

    public NationClass getNationClass(String classID) {
        for (NationClass nationClass : classes) {
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
