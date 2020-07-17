package me.tedwoodworth.diplomacy.nations;

import java.util.Map;

public class NationClass {

    public static final String WILDERNESS_ID = "WILDERNESS";
    private String classID;
    private String name;
    private String prefix;
    private int tax;
    private Map<String, Boolean> permissions;
    private String nationID;

    public NationClass(String classID, String name, String prefix, int tax, Map<String, Boolean> permissions, String nationID) {
        this.classID = classID;
        this.name = name;
        this.prefix = prefix;
        this.tax = tax;
        this.permissions = permissions;
        this.nationID = nationID;
    }

    public String getClassID() {
        return classID;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
