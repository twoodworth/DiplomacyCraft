package me.tedwoodworth.diplomacy.lives_and_tax;

import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;

import java.util.List;

public class GiveApple implements Runnable {

    private List<DiplomacyPlayer> players;

    GiveApple() {
        this.players = DiplomacyPlayers.getInstance().getPlayers();
    }

    @Override
    public void run() {
        for (var diplomacyPlayer : players) {
            diplomacyPlayer.setJoinedToday(false);
            if (diplomacyPlayer.getOfflinePlayer().isOnline() && !diplomacyPlayer.getJoinedToday()) {
                LivesManager.getInstance().giveApple(diplomacyPlayer.getOfflinePlayer().getPlayer(), "playing today");
            }
        }

    }
}
