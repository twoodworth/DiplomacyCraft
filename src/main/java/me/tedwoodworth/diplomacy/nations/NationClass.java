package me.tedwoodworth.diplomacy.nations;

import java.util.Map;

public class NationClass {

    private final String classID;
    private String name;
    private String prefix;
    private double tax;
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

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Nation getNation() {
        return Nations.getInstance().get(Integer.parseInt(nationID));
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Boolean> permissions) {
        this.permissions = permissions;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
