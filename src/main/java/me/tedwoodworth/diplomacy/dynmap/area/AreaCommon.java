//package me.tedwoodworth.diplomacy.dynmap.area;
//
//import me.tedwoodworth.diplomacy.nations.Nation;
//import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
//
//import java.util.ArrayList;
//
//public class AreaCommon {
//
//    public static String formatInfoWindow(final String infoWindow, final Nation nation) {
//        String formattedWindow = "<div class=\"regioninfo\">" + infoWindow + "</div>";
//
//        var ranks = getNationRanks(nation);
//        formattedWindow = formattedWindow.replace("%nation%", nation.getName());
//        formattedWindow = formattedWindow.replace("%Leader%", nation.getClassFromID("8").getName());
//        formattedWindow = formattedWindow.replace("%leaders%", ranks[0]);
//        formattedWindow = formattedWindow.replace("%Assistant%", nation.getClassFromID("7").getName());
//        formattedWindow = formattedWindow.replace("%assistants%", ranks[1]);
//        formattedWindow = formattedWindow.replace("%Diplomat%", nation.getClassFromID("6").getName());
//        formattedWindow = formattedWindow.replace("%diplomats%", ranks[2]);
//
//        return formattedWindow;
//
//    }
//
//    private static String[] getNationRanks(final Nation nation) {
//        var leaders = new ArrayList<DiplomacyPlayer>();
//        var assistants = new ArrayList<DiplomacyPlayer>();
//        var diplomats = new ArrayList<DiplomacyPlayer>();
//        for (final var member : nation.getMembers()) {
//            if (nation.getMemberClass(member).getClassID().equalsIgnoreCase("8")) {
//                leaders.add(member);
//            } else if (nation.getMemberClass(member).getClassID().equalsIgnoreCase("7")) {
//                assistants.add(member);
//            } else if (nation.getMemberClass(member).getClassID().equalsIgnoreCase("6")) {
//                diplomats.add(member);
//            }
//        }
//
//        var ranks = new String[3];
//        var strLeaders = new StringBuilder();
//        if (leaders.size() > 0) {
//            for (var leader : leaders) {
//                if (strLeaders.length() > 0) {
//                    strLeaders.append(", ");
//                }
//                strLeaders.append(leader.getOfflinePlayer().getName());
//            }
//            ranks[0] = strLeaders.toString();
//        } else {
//            ranks[0] = " ";
//        }
//
//        var strAssistants = new StringBuilder();
//        if (assistants.size() > 0) {
//            for (var assistant : assistants) {
//                if (strAssistants.length() > 0) {
//                    strAssistants.append(", ");
//                }
//                strAssistants.append(assistant.getOfflinePlayer().getName());
//            }
//            ranks[1] = strAssistants.toString();
//        } else {
//            ranks[1] = " ";
//        }
//
//        var strDiplomats = new StringBuilder();
//        if (diplomats.size() > 0) {
//            for (var diplomat : diplomats) {
//                if (strDiplomats.length() > 0) {
//                    strDiplomats.append(", ");
//                }
//                strDiplomats.append(diplomat.getOfflinePlayer().getName());
//            }
//            ranks[2] = strDiplomats.toString();
//        } else {
//            ranks[2] = " ";
//        }
//        return ranks;
//    }
//}