package me.tedwoodworth.diplomacy.dynmap;

import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import org.dynmap.markers.MarkerAPI;

import java.util.HashSet;
import java.util.Set;

public class PlayerSetCommon {

    private static final String MESSAGE_ADDED_PLAYER = "Added player visibility set '";
    private static final String MESSAGE_FOR_NATION = "' for nation ";

    public static void updatePlayerSet(final MarkerAPI markerApi, String nationID) {

        final Set<String> playerIds = new HashSet<>();

        final Nation nation = Nations.getInstance().get(Integer.parseInt(nationID)); // Get nation
        if (nation != null) {
            final var members = nation.getMembers();
            for (final var member : members) {
                playerIds.add(member.getUUID().toString());
            }
        }

        final var setId = "diplomacy." + nationID;
        final var set = markerApi.getPlayerSet(setId); // See if set exists

        if (set == null && nation != null) {
            markerApi.createPlayerSet(setId, true, playerIds, false);
        } else if (nation != null) {
            set.setPlayers(playerIds);
        } else {
            set.deleteSet();
        }
    }
}
