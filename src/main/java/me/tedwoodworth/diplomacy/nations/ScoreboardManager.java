package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.players.AccountManager;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

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

        for (var testPlayer : Bukkit.getOnlinePlayers()) {
            // Register Team
            var name = testPlayer.getName();
            var team = scoreboard.registerNewTeam(name);

            // Get rank prefix
            var testDiplomacyPlayer = DiplomacyPlayers.getInstance().get(testPlayer.getUniqueId());
            StringBuilder prefix = new StringBuilder();
            String rankPrefix;
            switch (testDiplomacyPlayer.getRank()) {
                case "Owner" -> rankPrefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "Owner" + ChatColor.GRAY + "] ";
                case "Dev" -> rankPrefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "Dev" + ChatColor.GRAY + "] ";
                case "Admin" -> rankPrefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "Admin" + ChatColor.GRAY + "] ";
                case "Mod" -> rankPrefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "Mod" + ChatColor.GRAY + "] ";
                case "TrialMod" -> rankPrefix = ChatColor.GRAY + "[" + ChatColor.GOLD + "TrialMod" + ChatColor.GRAY + "] ";
                default -> rankPrefix = "";
            }
            prefix.append(rankPrefix);

            String nationPrefix;
            var testNation = Nations.getInstance().get(testDiplomacyPlayer);
            if (testNation == null)
                nationPrefix = ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "Nomad" + ChatColor.GRAY + "] ";
            else if (nation != null && Objects.equals(Nations.getInstance().get(testDiplomacyPlayer), nation))
                nationPrefix = ChatColor.GRAY + "[" + ChatColor.GREEN + testNation.getName() + ChatColor.GRAY + "] ";
            else if (nation != null && nation.getAllyNationIDs().contains(testNation.getNationID()))
                nationPrefix = ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + testNation.getName() + ChatColor.GRAY + "] ";
            else if (nation != null && nation.getEnemyNationIDs().contains(testNation.getNationID()))
                nationPrefix = ChatColor.GRAY + "[" + ChatColor.RED + testNation.getName() + ChatColor.GRAY + "] ";
            else
                nationPrefix = ChatColor.GRAY + "[" + ChatColor.BLUE + testNation.getName() + ChatColor.GRAY + "] ";
            prefix.append(nationPrefix);


            // Get outlaw prefix
            String outlawPrefix;
            if (nation != null && nation.getOutlaws().contains(testPlayer.getUniqueId()))
                outlawPrefix = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "O" + ChatColor.GRAY + "] ";
            else
                outlawPrefix = "";
            prefix.append(outlawPrefix);

            // Get alt Prefix
            String altPrefix;
            var uuid = testPlayer.getUniqueId();
            if (!AccountManager.getInstance().getAccount(uuid).getMain().equals(uuid))
                altPrefix = ChatColor.GRAY + "[" + ChatColor.DARK_GRAY + "Alt" + ChatColor.GRAY + "] ";
            else
                altPrefix = "";
            prefix.append(altPrefix);
            team.setPrefix(prefix.toString() + ChatColor.WHITE);
            team.addEntry(testPlayer.getName());
        }
        player.setScoreboard(scoreboard);
    }

    public void updateScoreboards() {
        for (var player : Bukkit.getOnlinePlayers()) {
            createScoreboard(player);
        }
    }
}
