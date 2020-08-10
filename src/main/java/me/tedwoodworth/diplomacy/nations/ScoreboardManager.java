package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public class ScoreboardManager {

    private static ScoreboardManager instance = null;

    public static ScoreboardManager getInstance() {
        if (instance == null) {
            instance = new ScoreboardManager();
        }
        return instance;
    }

    public void createScoreboard(Player player) {
        var manager = Bukkit.getScoreboardManager();
        var scoreboard = manager.getNewScoreboard();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var nation = Nations.getInstance().get(diplomacyPlayer);

        scoreboard.registerNewTeam("Wilderness");
        var wildernessTeam = scoreboard.getTeam("Wilderness");
        wildernessTeam.setDisplayName("Wilderness");
        wildernessTeam.setColor(ChatColor.GRAY);

        for (var testNation : Nations.getInstance().getNations()) {
            scoreboard.registerNewTeam(testNation.getName());
            var team = scoreboard.getTeam(testNation.getName());
            team.setDisplayName(testNation.getName());
            team.setColor(ChatColor.BLUE);
            if (nation != null) {
                if (nation.getAllyNationIDs().contains(testNation.getNationID())) {
                    team.setColor(ChatColor.GREEN);
                } else if (nation.getEnemyNationIDs().contains(testNation.getNationID())) {
                    team.setColor(ChatColor.RED);
                } else if (Objects.equals(nation, testNation)) {
                    team.setColor(ChatColor.DARK_GREEN);
                }
            }
        }

        for (var testPlayer : DiplomacyPlayers.getInstance().getPlayers()) {
            var testNation = Nations.getInstance().get(testPlayer);

            if (testNation == null) {
                wildernessTeam.addEntry(testPlayer.getPlayer().getName());
            } else {
                scoreboard.getTeam(testNation.getName()).addEntry(testPlayer.getPlayer().getName());
            }
        }

        player.setScoreboard(scoreboard);
    }

}
