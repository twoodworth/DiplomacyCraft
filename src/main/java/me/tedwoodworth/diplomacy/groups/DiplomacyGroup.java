package me.tedwoodworth.diplomacy.groups;

import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class DiplomacyGroup {
    private final String groupID;
    private ConfigurationSection groupSection;

    public DiplomacyGroup(String groupID, ConfigurationSection groupSection) {
        this.groupID = groupID;
        this.groupSection = groupSection;
    }

    public static ConfigurationSection initializeGroup(ConfigurationSection groupSection, DiplomacyPlayer founder, Nation nation, String name) {
        var random = (int) (Math.random() * 15);
        var banner = Material.WHITE_BANNER;
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
        }

        var itemStack = new ItemStack(banner, 1);
        groupSection.set("Banner", itemStack);
        groupSection.set("Nation", nation.getNationID());
        groupSection.set("Created", Instant.now().getEpochSecond());
        groupSection.set("Name", name);

        var UUID = founder.getUUID();
        groupSection.set("Founder", UUID.toString());
        return groupSection;
    }

    public String getGroupID() {
        return groupID;
    }

    public Nation getNation() {
        String nationID = groupSection.getString("Nation");
        return Nations.getInstance().get(Integer.parseInt(nationID));
    }

    public void setNation(Nation nation) {
        groupSection.set("Nation", nation.getNationID());
    }

    public String getName() {
        return groupSection.getString("Name");
    }

    public void setName(String name) {
        groupSection.set("Name", name);
    }

    public void setBanner(ItemStack banner) {
        var bannerCopy = banner.clone();
        bannerCopy.setAmount(1);
        groupSection.set("Banner", bannerCopy);
    }

    public ItemStack getBanner() {
        return Objects.requireNonNull(groupSection.getItemStack("Banner")).clone();
    }

    public ItemStack getShield() {
        var banner = this.getBanner();
        var shield = new ItemStack(Material.SHIELD);
        ItemMeta meta = shield.getItemMeta();
        BlockStateMeta bsm = (BlockStateMeta) meta;
        Banner nBanner = (Banner) bsm.getBlockState();

        var bannerColor = banner.getType();
        var dyeColor = DyeColor.WHITE;
        switch (bannerColor) {
            case ORANGE_BANNER -> dyeColor = DyeColor.ORANGE;
            case MAGENTA_BANNER -> dyeColor = DyeColor.MAGENTA;
            case LIGHT_BLUE_BANNER -> dyeColor = DyeColor.LIGHT_BLUE;
            case YELLOW_BANNER -> dyeColor = DyeColor.YELLOW;
            case LIME_BANNER -> dyeColor = DyeColor.LIME;
            case PINK_BANNER -> dyeColor = DyeColor.PINK;
            case GRAY_BANNER -> dyeColor = DyeColor.GRAY;
            case LIGHT_GRAY_BANNER -> dyeColor = DyeColor.LIGHT_GRAY;
            case CYAN_BANNER -> dyeColor = DyeColor.CYAN;
            case PURPLE_BANNER -> dyeColor = DyeColor.PURPLE;
            case BLUE_BANNER -> dyeColor = DyeColor.BLUE;
            case BROWN_BANNER -> dyeColor = DyeColor.BROWN;
            case GREEN_BANNER -> dyeColor = DyeColor.GREEN;
            case RED_BANNER -> dyeColor = DyeColor.RED;
            case BLACK_BANNER -> dyeColor = DyeColor.BLACK;
        }

        nBanner.setBaseColor(dyeColor);
        nBanner.setPatterns(((BannerMeta) (banner.getItemMeta())).getPatterns());
        nBanner.update();
        bsm.setBlockState(nBanner);
        shield.setItemMeta(bsm);
        return shield;
    }

    public String getFounder() {
        return groupSection.getString("Founder");
    }

    public String getDateCreated() {
        var strUnix = groupSection.getString("Created");
        var unix = Integer.parseInt(Objects.requireNonNull(strUnix));
        var time = new java.util.Date((long) unix * 1000);
        SimpleDateFormat jdf = new SimpleDateFormat("MM/dd/yyyy");
        return jdf.format(time);
    }

    public long getAge() {
        var strUnix = groupSection.getString("Created");
        var unix = Integer.parseInt(Objects.requireNonNull(strUnix));
        var now = Instant.now().getEpochSecond();
        return now - unix;
    }

    public List<DiplomacyPlayer> getMembers() {
        List<DiplomacyPlayer> members = new ArrayList<>();
        for (var player : Bukkit.getOfflinePlayers()) {
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            if (diplomacyPlayer.getGroups().contains(this.getGroupID())) {
                members.add(diplomacyPlayer);
            } else if (Objects.equals(Nations.getInstance().get(diplomacyPlayer), this.getNation()) && this.getNation().getMemberClass(diplomacyPlayer).getPermissions().get("CanLeadAllGroups")) {
                members.add(diplomacyPlayer);
            }
        }
        return members;
    }

    public int getRole(DiplomacyPlayer player) {
        if (Objects.equals(Nations.getInstance().get(player), this.getNation())) {
            var permissions = this.getNation().getMemberClass(player).getPermissions();
            if (permissions.get("CanLeadAllGroups")) {
                return 0;
            }
        } else if (player.getGroupsLed().contains(this.getGroupID())) {
            return 1;
        } else if (player.getGroups().contains(this.getGroupID())) {
            return 2;
        }
        return 3;
    }

    public List<DiplomacyPlayer> getLeaders() {
        List<DiplomacyPlayer> members = new ArrayList<>();
        for (var player : Bukkit.getOfflinePlayers()) {
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            if (diplomacyPlayer.getGroupsLed().contains(this.getGroupID())) {
                members.add(diplomacyPlayer);
            }
        }
        return members;
    }

    public Set<DiplomacyChunk> getChunks() {

        var chunkMaps = groupSection.getMapList("Chunks");
        return chunkMaps.stream()
                .map(DiplomacyChunks.getInstance()::configMapToChunk)
                .collect(Collectors.toSet());
    }

    public boolean hasChunk(DiplomacyChunk diplomacyChunk) {
        var diplomacyChunkMap = DiplomacyChunks.getInstance().chunkToConfigMap(diplomacyChunk);
        var chunkMaps = groupSection.getMapList("Chunks");
        return chunkMaps.contains(diplomacyChunkMap);
    }

    public void addChunk(DiplomacyChunk diplomacyChunk) {
        var list = groupSection.getMapList("Chunks");
        list.add(DiplomacyChunks.getInstance().chunkToConfigMap(diplomacyChunk));
        groupSection.set("Chunks", list);
    }

    public void removeChunk(DiplomacyChunk diplomacyChunk) {
        var list = groupSection.getMapList("Chunks");
        list.remove(DiplomacyChunks.getInstance().chunkToConfigMap(diplomacyChunk));
        groupSection.set("Chunks", list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiplomacyGroup that = (DiplomacyGroup) o;
        return Objects.equals(groupID, that.groupID) &&
                Objects.equals(groupSection, that.groupSection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupID, groupSection);
    }
}
