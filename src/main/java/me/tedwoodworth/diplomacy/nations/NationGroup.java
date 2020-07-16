package me.tedwoodworth.diplomacy.nations;

import java.util.List;

public class NationGroup {
    private int groupID;
    private String name;
    private List<DiplomacyChunk> chunks;

    public NationGroup(int groupID, String name, List<DiplomacyChunk> chunks) {
        this.groupID = groupID;
        this.name = name;
        this.chunks = chunks;
    }

    public int getGroupID() {
        return groupID;
    }

    public List<DiplomacyChunk> getChunks() {
        return chunks;
    }

    public void removeChunk(DiplomacyChunk diplomacyChunk) {
        chunks.remove(diplomacyChunk);
    }

    public String getName() {
        return name;
    }
}
