package me.tedwoodworth.diplomacy.groups;

import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;
import java.util.stream.Collectors;

public class DiplomacyGroup {
    private final String groupID;
    private ConfigurationSection groupSection;

    public DiplomacyGroup(String groupID, ConfigurationSection groupSection) {
        this.groupID = groupID;
        this.groupSection = groupSection;
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
