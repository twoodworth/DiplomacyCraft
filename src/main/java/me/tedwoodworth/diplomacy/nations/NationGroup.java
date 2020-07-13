package me.tedwoodworth.diplomacy.nations;

import java.util.List;

public class NationGroup {
    private int groupID;
    private String groupName;
    private List<DiplomacyChunk> chunks;

    public NationGroup(int groupID, String groupName, List<DiplomacyChunk> chunks) {
        this.groupID = groupID;
        this.groupName = groupName;
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
}
