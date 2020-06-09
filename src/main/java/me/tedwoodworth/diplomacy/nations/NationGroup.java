package me.tedwoodworth.diplomacy.nations;

import java.util.List;

public class NationGroup {
    private int groupID;
    private String groupName;
    private List<String> chunks;

    public NationGroup(int groupID, String groupName, List<String> chunks) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.chunks = chunks;
    }

    public int getGroupID() {
        return groupID;
    }
}
