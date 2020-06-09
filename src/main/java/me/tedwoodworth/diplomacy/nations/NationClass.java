package me.tedwoodworth.diplomacy.nations;

import java.util.List;

public class NationClass {

    private int classID;
    private String name;
    private String prefix;
    private int tax;
    private List<Boolean> permissions;
    private String nationID;

    public NationClass(int classID, String name, String prefix, int tax, List<Boolean> permissions, String nationID) {
        this.classID = classID;
        this.name = name;
        this.prefix = prefix;
        this.tax = tax;
        this.permissions = permissions;
        this.nationID = nationID;
    }
}
