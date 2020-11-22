package me.tedwoodworth.diplomacy.dynmap.area;

import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;

import java.util.ArrayList;

public class AreaCommon {

    public static String formatInfoWindow(final String infoWindow, final Nation nation) {
        String formattedWindow = new StringBuilder("<div class=\"regioninfo\">").append(infoWindow).append("</div>").toString();

        formattedWindow = formattedWindow.replace("%nation%", nation.getName());
        formattedWindow = formattedWindow.replace("%Leader%", nation.getClassFromID("8").getName());
        formattedWindow = formattedWindow.replace("%leaders%", getNationLeaders(nation));
        formattedWindow = formattedWindow.replace("%members%", getNationMembers(nation));

        return formattedWindow;

    }

    private static String getNationLeaders(final Nation nation) {
        var leaders = new ArrayList<DiplomacyPlayer>();
        for (final var member : nation.getMembers()) {
            if (nation.getMemberClass(member).getClassID().equalsIgnoreCase("8")) {
                leaders.add(member);
            }
        }

        var strLeaders = new StringBuilder();
        for (var leader : leaders) {
            if (strLeaders.length() > 0) {
                strLeaders.append(", ");
            }
            strLeaders.append(leader.getOfflinePlayer().getName());
        }
        return strLeaders.toString();
    }

    private static String getNationMembers(final Nation nation) {
        var strMembers = new StringBuilder();
        for (var member : nation.getMembers()) {
            if (strMembers.length() > 0) {
                strMembers.append(", ");
            }
            strMembers.append(member.getOfflinePlayer().getName());
        }
        return strMembers.toString();
    }
}
