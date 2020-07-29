package me.tedwoodworth.diplomacy.groups;

import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

public class DiplomacyGroup {
    private final String groupID;
    private ConfigurationSection groupSection;

    public DiplomacyGroup(String groupID, ConfigurationSection groupSection) {
        this.groupID = groupID;
        this.groupSection = groupSection;
    }

    public static ConfigurationSection initializeGroup(ConfigurationSection groupSection, Nation nation, String name) {
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
        return groupSection;
    }

    public String getGroupID() {
        return groupID;
    }

    public Nation getNation() {
        String nationName = groupSection.getString("Nation");
        return Nations.getInstance().get(nationName);
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
}
