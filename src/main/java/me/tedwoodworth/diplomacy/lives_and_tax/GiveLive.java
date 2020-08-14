package me.tedwoodworth.diplomacy.lives_and_tax;

import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;

import java.util.List;

public class GiveLive implements Runnable {

    private List<DiplomacyPlayer> players;

    GiveLive() {
        this.players = DiplomacyPlayers.getInstance().getPlayers();
    }

    @Override
    public void run() {
        for (var diplomacyPlayer : players) {
            diplomacyPlayer.setJoinedToday(false);
            if (diplomacyPlayer.getPlayer().isOnline()) {
                LivesManager.getInstance().giveLive(diplomacyPlayer);
            }
        }

    }
}
