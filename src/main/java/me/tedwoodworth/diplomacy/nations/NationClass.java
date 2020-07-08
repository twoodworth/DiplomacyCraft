package me.tedwoodworth.diplomacy.nations;

import java.util.Map;

public class NationClass {

    public static final String WILDERNESS_ID = "WILDERNESS";
    private int classID;
    private String name;
    private String prefix;
    private int tax;
    private Map<String, Boolean> permissions;
    private String nationID;

    public NationClass(int classID, String name, String prefix, int tax, Map<String, Boolean> permissions, String nationID) {
        this.classID = classID;
        this.name = name;
        this.prefix = prefix;
        this.tax = tax;
        this.permissions = permissions;
        this.nationID = nationID;
    }

//    public static NationClass getWildernessClass() {
//        NationClass nationClass = new NationClass(WILDERNESS_ID, "Nomad", "", 0, )
//    }
}
