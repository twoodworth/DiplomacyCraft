package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.players.AccountManager;
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
        scoreboard.registerNewObjective("Nation", "Nation", "Wilderness");
        var wildernessTeam = scoreboard.getTeam("Wilderness");
        wildernessTeam.setDisplayName("Wilderness");
        wildernessTeam.setPrefix(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "Nomad" + ChatColor.GRAY + "] ");

        scoreboard.registerNewTeam("Wilderness-O");
        var outlawWildernessTeam = scoreboard.getTeam("Wilderness-O");
        outlawWildernessTeam.setDisplayName("Wilderness-O");
        outlawWildernessTeam.setPrefix(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "Nomad" + ChatColor.GRAY + "] [" + ChatColor.DARK_RED + "O" + ChatColor.GRAY + "] ");

        scoreboard.registerNewTeam("Wilderness-A");
        var altWildernessTeam = scoreboard.getTeam("Wilderness-A");
        altWildernessTeam.setDisplayName("Wilderness-A");
        altWildernessTeam.setPrefix(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "Nomad" + ChatColor.GRAY + "] [" + ChatColor.DARK_GRAY + "Alt" + ChatColor.GRAY + "] ");

        scoreboard.registerNewTeam("Wilderness-OA");
        var outlawAltWildernessTeam = scoreboard.getTeam("Wilderness-OA");
        outlawAltWildernessTeam.setDisplayName("Wilderness-OA");
        outlawAltWildernessTeam.setPrefix(ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "Nomad" + ChatColor.GRAY + "] [" + ChatColor.DARK_RED + "O" + ChatColor.GRAY + "] [" + ChatColor.DARK_GRAY + "Alt" + ChatColor.GRAY + "] ");


        for (var testNation : Nations.getInstance().getNations()) {
            scoreboard.registerNewTeam(String.valueOf(testNation.getNationID()));
            var team = scoreboard.getTeam(String.valueOf(testNation.getNationID()));
            team.setDisplayName(testNation.getName());
            var color = ChatColor.BLUE;
            if (nation != null) {
                if (nation.getAllyNationIDs().contains(testNation.getNationID())) {
                    color = ChatColor.DARK_GREEN;
                } else if (nation.getEnemyNationIDs().contains(testNation.getNationID())) {
                    color = ChatColor.RED;
                } else if (Objects.equals(nation, testNation)) {
                    color = ChatColor.GREEN;
                }
            }

            scoreboard.registerNewTeam(testNation.getNationID() + "-O");
            var outlawTeam = scoreboard.getTeam(testNation.getNationID() + "-O");
            outlawTeam.setDisplayName(testNation.getNationID() + "-O");
            team.setPrefix(ChatColor.GRAY + "[" + color + testNation.getName() + ChatColor.GRAY + "] ");
            outlawTeam.setPrefix(ChatColor.GRAY + "[" + color + testNation.getName() + ChatColor.GRAY + "] [" + ChatColor.DARK_RED + "O" + ChatColor.GRAY + "] ");

            scoreboard.registerNewTeam(testNation.getNationID() + "-A");
            var altTeam = scoreboard.getTeam(testNation.getNationID() + "-A");
            altTeam.setDisplayName(testNation.getNationID() + "-A");
            altTeam.setPrefix(ChatColor.GRAY + "[" + color + testNation.getName() + ChatColor.GRAY + "] [" + ChatColor.DARK_GRAY + "Alt" + ChatColor.GRAY + "] ");


            scoreboard.registerNewTeam(testNation.getNationID() + "-OA");
            var outlawAltTeam = scoreboard.getTeam(testNation.getNationID() + "-OA");
            outlawAltTeam.setDisplayName(testNation.getNationID() + "-OA");
            outlawAltTeam.setPrefix(ChatColor.GRAY + "[" + color + testNation.getName() + ChatColor.GRAY + "] [" + ChatColor.DARK_RED + "O" + ChatColor.GRAY + "] [" + ChatColor.DARK_GRAY + "Alt" + ChatColor.GRAY + "] ");
        }

        for (var testPlayer : DiplomacyPlayers.getInstance().getPlayers()) {

            var testOnlinePlayer = testPlayer.getPlayer().getPlayer();
            if (testOnlinePlayer == null) {
                continue;
            }
            var testNation = Nations.getInstance().get(testPlayer);
            var uuid = testOnlinePlayer.getUniqueId();
            var account = AccountManager.getInstance().getAccount(uuid);

            // Not an alt
            if (uuid.equals(account.getMain())) {
                if (testNation == null) {
                    if (nation != null && nation.getOutlaws().contains(testPlayer.getUUID())) {
                        outlawWildernessTeam.addEntry(testOnlinePlayer.getName());
                    } else {
                        wildernessTeam.addEntry(testOnlinePlayer.getName());
                    }
                } else if (nation != null && nation.getOutlaws().contains(testPlayer.getUUID())) {
                    scoreboard.getTeam(testNation.getNationID() + "-O").addEntry(testOnlinePlayer.getName());
                } else {
                    scoreboard.getTeam(String.valueOf(testNation.getNationID())).addEntry(testOnlinePlayer.getName());
                }
            } else {
                // Is an alt
                if (testNation == null) {
                    if (nation != null && nation.getOutlaws().contains(testPlayer.getUUID())) {
                        outlawAltWildernessTeam.addEntry(testOnlinePlayer.getName());
                    } else {
                        altWildernessTeam.addEntry(testOnlinePlayer.getName());
                    }
                } else if (nation != null && nation.getOutlaws().contains(testPlayer.getUUID())) {
                    scoreboard.getTeam(testNation.getNationID() + "-OA").addEntry(testOnlinePlayer.getName());
                } else {
                    scoreboard.getTeam(testNation.getNationID() + "-A").addEntry(testOnlinePlayer.getName());
                }
            }
        }

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboards() {
        for (var player : Bukkit.getOnlinePlayers()) {
            createScoreboard(player);
        }
    }
}
