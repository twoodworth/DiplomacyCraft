package me.tedwoodworth.diplomacy.nations;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.events.*;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class Nation {

    private ConfigurationSection configSection;
    private final String nationID;
    private String name;
    private long created;
    private UUID founderID;
    private String color;

    Nation(String nationID, ConfigurationSection nationSection) {
        this.configSection = nationSection;
        this.nationID = nationID;
        this.name = nationSection.getString("Name");
        this.created = nationSection.getInt("Created");
        this.founderID = UUID.fromString(Objects.requireNonNull(nationSection.getString("Founder")));
        this.color = nationSection.getString("Color");
    }

    public static ConfigurationSection initializeNation(ConfigurationSection nationSection, OfflinePlayer founder, String name) {
        Map<String, String> membersMap = ImmutableMap.of(
                founder.getUniqueId().toString(), "8");

        nationSection.createSection("Members", membersMap);
        nationSection.set("Founder", founder.getUniqueId().toString());
        var random = (int) (Math.random() * 17);
        Material banner;
        switch (random) {
            case 0 -> banner = Material.ORANGE_BANNER;
            case 1 -> banner = Material.MAGENTA_BANNER;
            case 2 -> banner = Material.LIGHT_BLUE_BANNER;
            case 3 -> banner = Material.YELLOW_BANNER;
            case 4 -> banner = Material.LIME_BANNER;
            case 5 -> banner = Material.PINK_BANNER;
            case 6 -> banner = Material.GRAY_BANNER;
            case 7 -> banner = Material.LIGHT_GRAY_BANNER;
            case 8 -> banner = Material.CYAN_BANNER;
            case 9 -> banner = Material.PURPLE_BANNER;
            case 10 -> banner = Material.BLUE_BANNER;
            case 11 -> banner = Material.BROWN_BANNER;
            case 12 -> banner = Material.GREEN_BANNER;
            case 13 -> banner = Material.RED_BANNER;
            case 14 -> banner = Material.BLACK_BANNER;
            default -> banner = Material.WHITE_BANNER;
        }

        var itemStack = new ItemStack(banner, 1);
        nationSection.set("Banner", itemStack);
        nationSection.set("Created", Instant.now().getEpochSecond());
        nationSection.set("Name", name);

        var r = (int) (Math.random() * 256);
        var g = (int) (Math.random() * 256);
        var b = (int) (Math.random() * 256);

        var color = new Color(r, g, b);
        nationSection.set("Color.Red", color.getRed());
        nationSection.set("Color.Green", color.getGreen());
        nationSection.set("Color.Blue", color.getBlue());
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

    public long getAge() {
        var strUnix = configSection.getString("Created");
        var unix = Integer.parseInt(Objects.requireNonNull(strUnix));
        var now = Instant.now().getEpochSecond();
        return now - unix;
    }

    public Set<DiplomacyPlayer> getMembers() {
        var members = new HashSet<DiplomacyPlayer>();
        var strMembers = Objects.requireNonNull(configSection.getConfigurationSection("Members")).getKeys(false);
        for (var strMember : strMembers) {
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(UUID.fromString(strMember));
            members.add(diplomacyPlayer);
        }
        return members;
    }

    public void addMember(DiplomacyPlayer diplomacyPlayer) {
        Bukkit.getPluginManager().callEvent(new NationJoinEvent(diplomacyPlayer, this));
        var id = diplomacyPlayer.getUUID().toString();
        var memberConfig = configSection.getConfigurationSection("Members");
        Validate.notNull(memberConfig);
        memberConfig.set(id, "0");
        configSection.set("Members", memberConfig);

    }

    public void removeMember(DiplomacyPlayer diplomacyPlayer) {
        Bukkit.getPluginManager().callEvent(new NationLeaveEvent(diplomacyPlayer, this));
        var id = diplomacyPlayer.getUUID().toString();
        var memberConfig = configSection.getConfigurationSection("Members");
        Validate.notNull(memberConfig);
        memberConfig.set(id, null);
        configSection.set("Members", memberConfig);
    }

    public List<NationClass> getClasses() {
        var classes = new ArrayList<NationClass>(9);
        for (var classID = 0; classID < 9; classID++) {
            var strClassID = String.valueOf(classID);
            var classSection = configSection.getConfigurationSection("Classes." + classID);
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
        return classes;
    }

    public void toggleClassPermission(NationClass nationClass, Player user, String permission) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(user.getUniqueId());
        var nationClassNation = nationClass.getNation();
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        if (!Objects.equals(playerNation, nationClassNation)) {
            user.sendMessage(ChatColor.DARK_RED + "You must be in the nation " + nationClassNation.getName() + " to do this.");
            return;
        }

        var playerPermissions = playerNation.getMemberClass(diplomacyPlayer).getPermissions();

        if (!playerPermissions.get("CanManageClasses")) {
            user.sendMessage(ChatColor.DARK_RED + "You do not have sufficient permissions to do this.");
            return;
        }


        var classID = nationClass.getClassID();
        var current = nationClass.getPermissions().get(permission);

        if (current) {
            configSection.set("Classes." + classID + ".Permissions." + permission, false);
        } else {
            configSection.set("Classes." + classID + ".Permissions." + permission, true);
        }
        Map<String, Boolean> classPermissions = new HashMap<>();
        var permissionSection = configSection.getConfigurationSection("Classes." + classID + ".Permissions");
        var permissionSet = permissionSection.getKeys(false);
        for (var setPermission : permissionSet) {
            classPermissions.put(setPermission, permissionSection.getBoolean(setPermission));
        }
        nationClass.setPermissions(classPermissions);

        for (var member : this.getMembers()) {
            if (Bukkit.getPlayer(member.getUUID()) != null) {
                var player = Bukkit.getPlayer(member.getUUID());
                player.sendMessage(ChatColor.AQUA + permission + " set to " + !current + " for the class " + nationClass.getName());
            }
        }
    }

    public void renameClass(NationClass nationClass, String newName) {
        var classID = nationClass.getClassID();
        configSection.set("Classes." + classID + ".Name", newName);
        nationClass.setName(newName);
    }

    public void setClassPrefix(NationClass nationClass, String newPrefix) {
        var classID = nationClass.getClassID();
        configSection.set("Classes." + classID + ".Prefix", newPrefix);
        nationClass.setPrefix(newPrefix);
    }

    public void clearClassPrefix(NationClass nationClass) {
        var classID = nationClass.getClassID();
        configSection.set("Classes." + classID + ".Prefix", null);
        nationClass.setPrefix(null);
    }

    public void setClassTax(NationClass nationClass, double amount) {
        var classID = nationClass.getClassID();
        configSection.set("Classes." + classID + ".Tax", amount);
        nationClass.setTax(amount);
    }

    @Nullable
    public NationClass getMemberClass(DiplomacyPlayer diplomacyPlayer) {
        var uuid = diplomacyPlayer.getUUID();
        var classID = configSection.getString("Members." + uuid.toString());
        return getClassFromID(classID);

    }

    public void setMemberClass(DiplomacyPlayer diplomacyPlayer, NationClass nationClass) {
        var uuid = diplomacyPlayer.getUUID();
        var classID = nationClass.getClassID();
        configSection.set("Members." + uuid.toString(), classID);
        for (var otherDiplomacyPlayer : this.getMembers()) {
            var player = otherDiplomacyPlayer.getPlayer();
            if (player.isOnline()) {
                if (Objects.equals(otherDiplomacyPlayer, diplomacyPlayer)) {
                    player.getPlayer().sendMessage(ChatColor.AQUA + "Your class has been set to " + nationClass.getName() + ".");
                } else {
                    player.getPlayer().sendMessage(ChatColor.GREEN + diplomacyPlayer.getPlayer().getName() + ChatColor.AQUA + " had their class set to " + nationClass.getName() + ".");
                }
            }
        }
    }

    @Nullable
    public NationClass getClassFromID(String classID) {
        for (var nationClass : this.getClasses()) {
            if (Objects.equals(nationClass.getClassID(), classID)) {
                return nationClass;
            }
        }
        return null;
    }

    public double getPower() {
        var totalPower = 0.0;
        for (var nation : Nations.getInstance().getNations()) {
            totalPower += nation.getUnscaledPower();
        }
        if (totalPower == 0.0) {
            totalPower = 1.0;
        }
        return this.getUnscaledPower() / totalPower;
    }

    public double getUnscaledPower() {
        var totalPopulation = 0.0;
        var totalTerritory = 0.0;
        var totalBalance = 0.0;

        for (var testNation : Nations.getInstance().getNations()) {
            totalPopulation += testNation.getMembers().size();
            totalTerritory += testNation.getChunks().size();
            totalBalance += testNation.getBalance();
        }

        if (totalPopulation == 0.0) {
            totalPopulation = 1.0;
        }
        if (totalTerritory == 0.0) {
            totalTerritory = 1.0;
        }
        if (totalBalance == 0.0) {
            totalBalance = 1.0;
        }

        var nationPopulation = this.getMembers().size();
        var nationTerritory = this.getChunks().size();
        var nationBalance = this.getBalance();

        var scaledPopulation = nationPopulation / totalPopulation;
        var scaledTerritory = nationTerritory / totalTerritory;
        var scaledBalance = nationBalance / totalBalance;

        var squaredPopulation = Math.pow(scaledPopulation, 2);
        var squaredTerritory = Math.pow(scaledTerritory, 2);
        var squaredBalance = Math.pow(scaledBalance, 2);

        var squaredPower = squaredPopulation + squaredTerritory + squaredBalance;
        return Math.sqrt(squaredPower);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        var oldName = this.getName();
        this.name = name;
        Bukkit.getPluginManager().callEvent(new NationRenameEvent(this, oldName, this.getName()));
    }

    public void setBanner(ItemStack banner) {
        var bannerCopy = banner.clone();
        bannerCopy.setAmount(1);
        configSection.set("Banner", bannerCopy);
    }

    public ItemStack getBanner() {
        return Objects.requireNonNull(configSection.getItemStack("Banner")).clone();
    }

    public Set<DiplomacyChunk> getChunks() {

        var chunkMaps = configSection.getMapList("Chunks");
        return chunkMaps.stream()
                .map(DiplomacyChunks.getInstance()::configMapToChunk)
                .collect(Collectors.toSet());
    }

    public boolean hasChunk(DiplomacyChunk diplomacyChunk) {
        var diplomacyChunkMap = DiplomacyChunks.getInstance().chunkToConfigMap(diplomacyChunk);
        var chunkMaps = configSection.getMapList("Chunks");
        return chunkMaps.contains(diplomacyChunkMap);
    }

    public void addChunk(DiplomacyChunk diplomacyChunk) {
        var list = configSection.getMapList("Chunks");
        list.add(DiplomacyChunks.getInstance().chunkToConfigMap(diplomacyChunk));
        configSection.set("Chunks", list);
        Bukkit.getPluginManager().callEvent(new NationAddChunkEvent(this, diplomacyChunk));
    }

    public void removeChunk(DiplomacyChunk diplomacyChunk) {
        var chunk = diplomacyChunk.getChunk();
        var list = configSection.getMapList("Chunks");
        list.remove(DiplomacyChunks.getInstance().chunkToConfigMap(diplomacyChunk));
        configSection.set("Chunks", list);
        Bukkit.getPluginManager().callEvent(new NationRemoveChunkEvent(this, chunk));
    }

    public List<DiplomacyGroup> getGroups() {
        List<DiplomacyGroup> groups = new ArrayList<>();
        for (var group : DiplomacyGroups.getInstance().getGroups()) {
            if (Objects.equals(group.getNation(), this)) {
                groups.add(group);
            }
        }
        return groups;
    }

    public List<UUID> getOutlaws() {
        List<UUID> UUIDs = new ArrayList<>();
        for (var strUUID : configSection.getStringList("Outlaws")) {
            var uuid = UUID.fromString(strUUID);
            UUIDs.add(uuid);
        }
        return UUIDs;
    }

    public void addOutlaw(OfflinePlayer player) {
        var uuid = player.getUniqueId();
        var list = configSection.getStringList("Outlaws");
        list.add(uuid.toString());
        configSection.set("Outlaws", list);
    }

    public String getFounder() {
        return configSection.getString("Founder");
    }

    public String getDateCreated() {
        var strUnix = configSection.getString("Created");
        var unix = Integer.parseInt(Objects.requireNonNull(strUnix));
        var time = new java.util.Date((long) unix * 1000);
        SimpleDateFormat jdf = new SimpleDateFormat("MM/dd/yyyy");
        return jdf.format(time);
    }

    public void removeOutlaw(OfflinePlayer player) {
        var uuid = player.getUniqueId();
        var list = configSection.getStringList("Outlaws");
        list.remove(uuid.toString());
        configSection.set("Outlaws", list);
    }

    @Nullable
    public NationClass getNationClass(String classID) {
        for (var nationClass : this.getClasses()) {
            if (nationClass.getClassID().equals(classID)) {
                return nationClass;
            }
        }
        return null;
    }

    public double getBalance() {
        return configSection.getDouble("Balance");
    }

    public void setBalance(double balance) {
        configSection.set("Balance", balance);
    }

    public List<String> getAllyNationIDs() {
        return configSection.getStringList("AllyNations");
    }

    public List<String> getEnemyNationIDs() {
        return configSection.getStringList("EnemyNations");
    }

    public void addEnemyNation(Nation nation) {
        var list = configSection.getStringList("EnemyNations");
        list.add(nation.getNationID());
        configSection.set("EnemyNations", list);
    }

    public void removeEnemyNation(Nation nation) {
        var list = configSection.getStringList("EnemyNations");
        list.remove(nation.getNationID());
        configSection.set("EnemyNations", list);

    }

    public void addAllyNation(Nation nation) {
        var list = configSection.getStringList("AllyNations");
        list.add(nation.getNationID());
        configSection.set("AllyNations", list);
    }

    public void removeAllyNation(Nation nation) {
        var list = configSection.getStringList("AllyNations");
        list.remove(nation.getNationID());
        configSection.set("AllyNations", list);
    }

    public boolean getIsOpen() {
        var isOpen = configSection.getBoolean("IsOpen");
        return isOpen;
    }

    public void guiSetIsOpen(boolean isOpen, Player player) {
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);
        if (nation == null) {
            player.sendMessage(ChatColor.DARK_RED + "You are not a member of this nation.");
            return;
        }

        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canToggle = permissions.get("CanToggleBorder");
        if (!canToggle) {
            player.sendMessage(ChatColor.DARK_RED + "You do not have permission to toggle the border.");
            return;
        }

        setIsOpen(isOpen);
    }

    public Color getColor() {
        var red = configSection.getInt("Color.Red");
        var green = configSection.getInt("Color.Green");
        var blue = configSection.getInt("Color.Blue");

        return new Color(red, green, blue);
    }

    public void setColor(int red, int green, int blue) {
        configSection.set("Color.Red", red);
        configSection.set("Color.Green", green);
        configSection.set("Color.Blue", blue);
        Bukkit.getPluginManager().callEvent(new NationColorEvent(this, new Color(red, green, blue)));
    }

    public void setIsOpen(boolean isOpen) {

        configSection.set("IsOpen", isOpen);
        if (isOpen) {
            for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
                var testNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testNation == null) {
                    onlinePlayer.sendMessage(ChatColor.BLUE + this.getName() + ChatColor.AQUA + " has opened its borders.");
                } else {
                    if (testNation.getEnemyNationIDs().contains(this.getNationID())) {
                        onlinePlayer.sendMessage(ChatColor.RED + this.getName() + ChatColor.AQUA + " has opened its borders.");
                    } else if (testNation.getAllyNationIDs().contains(this.getNationID())) {
                        onlinePlayer.sendMessage(ChatColor.GREEN + this.getName() + ChatColor.AQUA + " has opened its borders.");
                    } else if (Objects.equals(testNation, this)) {
                        onlinePlayer.sendMessage(ChatColor.AQUA + "Your nation has opened its borders.");
                    } else {
                        onlinePlayer.sendMessage(ChatColor.BLUE + this.getName() + ChatColor.AQUA + " has opened its borders.");
                    }
                }
            }
        } else {
            for (var onlinePlayer : Bukkit.getOnlinePlayers()) {
                var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(onlinePlayer.getUniqueId());
                var testNation = Nations.getInstance().get(testDiplomacyPlayer);
                if (testNation == null) {
                    onlinePlayer.sendMessage(ChatColor.BLUE + this.getName() + ChatColor.AQUA + " has closed its borders.");
                } else {
                    if (testNation.getEnemyNationIDs().contains(this.getNationID())) {
                        onlinePlayer.sendMessage(ChatColor.RED + this.getName() + ChatColor.AQUA + " has closed its borders.");
                    } else if (testNation.getAllyNationIDs().contains(this.getNationID())) {
                        onlinePlayer.sendMessage(ChatColor.GREEN + this.getName() + ChatColor.AQUA + " has closed its borders.");
                    } else if (Objects.equals(testNation, this)) {
                        onlinePlayer.sendMessage(ChatColor.AQUA + "Your nation has closed its borders.");
                    } else {
                        onlinePlayer.sendMessage(ChatColor.BLUE + this.getName() + ChatColor.AQUA + " has closed its borders.");
                    }
                }
            }
        }
    }
}
