package me.tedwoodworth.diplomacy.nations.contest;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ContestManager {

    private static ContestManager instance = null;

    private Map<DiplomacyChunk, Contest> contests = new HashMap<>();

    private int contestTaskID = -1;

    public static ContestManager getInstance() {
        if (instance == null) {
            instance = new ContestManager();
        }
        return instance;
    }

    public void startContest(Nation attackingNation, DiplomacyChunk diplomacyChunk, boolean isWilderness) {
        var contest = contests.get(diplomacyChunk);
        if (contest == null) {
            contest = new Contest(attackingNation, diplomacyChunk, isWilderness);
            contests.put(diplomacyChunk, contest);
        }

        if (contestTaskID == -1) {
            contestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onContestTask, 0L, 20L);
        }
    }

    private void onContestTask() {
        for (var contest : contests.values()) {
            if (contest.isWilderness()) {
                wildernessProgressChange(contest);
            } else {
                updateProgress(contest);
            }
            contest.sendParticles();
            if (contest.getProgress() >= .03) { //TODO Change to 1.0
                if (contest.isWilderness()) {
                    winWildernessContest(contest);
                } else {
                    winContest(contest);
                }
            } else if (contest.getProgress() < 0.0) {
                endContest(contest);
            }
            Bukkit.broadcastMessage(String.valueOf(contest.getProgress()));//TODO remove
        }
    }

    private void winContest(Contest contest) {
        var diplomacyChunk = contest.getDiplomacyChunk();
        var defendingNation = diplomacyChunk.getNation();
        defendingNation.removeChunk(contest.getDiplomacyChunk());
        contest.getAttackingNation().addChunk(diplomacyChunk);
        for (var group : defendingNation.getGroups()) {
            if (group.getChunks().contains(diplomacyChunk)) {
                group.removeChunk(diplomacyChunk);
            }
        }
        endContest(contest);
    }

    private void winWildernessContest(Contest contest) {
        var diplomacyChunk = contest.getDiplomacyChunk();
        contest.getAttackingNation().addChunk(diplomacyChunk);
        endContest(contest);
    }

    private void endContest(Contest contest) {
        contests.remove(contest.getDiplomacyChunk());
        if (contests.size() == 0) {
            Bukkit.getScheduler().cancelTask(contestTaskID);
            contestTaskID = -1;
        }
    }

    public void updateProgress(Contest contest) {

        var diplomacyChunk = contest.getDiplomacyChunk();
        var chunk = diplomacyChunk.getChunk();
        var attackingNation = contest.getAttackingNation();
        var defendingNation = diplomacyChunk.getNation();
        var attackingPlayers = 0;
        var defendingPlayers = 0;
        for (var player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getChunk().equals(chunk)) {
                var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                var isAttackingNationAlly = attackingNation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
                var isDefendingNationAlly = defendingNation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
                var isAttackingNation = Nations.getInstance().get(diplomacyPlayer).equals(attackingNation);
                var isDefendingNation = Nations.getInstance().get(diplomacyPlayer).equals(defendingNation);
                if (isAttackingNation || isAttackingNationAlly && !isDefendingNationAlly) {
                    attackingPlayers++;
                } else if (isDefendingNation || isDefendingNationAlly && !isAttackingNationAlly) {
                    defendingPlayers++;
                }
            }
        }
        //TODO defendingPlayers++ for every defending nation guard within the chunk
        //TODO only defendingPlayers++ if there is an unobstructed line of view between the defender and the attackers (meaning the defenders can be attacked)

        var attackingAdjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, attackingNation, false);
        var defendingAdjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, defendingNation, false);

        if (attackingPlayers > defendingPlayers) {
            contest.setProgress(contest.getProgress() + Math.pow(2.0, (attackingPlayers - defendingPlayers)) * attackingAdjacentCoefficient);
        } else if (attackingPlayers < defendingPlayers) {
            contest.setProgress(contest.getProgress() - Math.pow(2.0, (defendingPlayers - attackingPlayers)) * defendingAdjacentCoefficient);
        }
    }

    public void wildernessProgressChange(Contest contest) {
        var diplomacyChunk = contest.getDiplomacyChunk();
        var chunk = diplomacyChunk.getChunk();
        var nation = contest.getAttackingNation();
        var players = 0;
        for (var player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getChunk().equals(chunk)) {
                var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                var isAttackingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), nation);
                var isAttackingNationAlly = nation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
                if (isAttackingNation || isAttackingNationAlly) {
                    players++;
                }
            }
        }
        var adjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, nation, true);
        if (players > 0) {
            contest.setProgress(contest.getProgress() + Math.pow(2.0, players) * adjacentCoefficient);
        }
    }

    public double getAdjacentCoefficient(DiplomacyChunk diplomacyChunk, Nation nation, boolean isWilderness) {
        var world = diplomacyChunk.getChunk().getWorld();
        var x = diplomacyChunk.getChunk().getX();
        var z = diplomacyChunk.getChunk().getZ();
        var adjacentChunks = 0;
        if (Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(world.getChunkAt(x + 1, z)).getNation(), nation)) {
            adjacentChunks++;
        }
        if (Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(world.getChunkAt(x + 1, z + 1)).getNation(), nation)) {
            adjacentChunks++;
        }
        if (Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(world.getChunkAt(x, z + 1)).getNation(), nation)) {
            adjacentChunks++;
        }
        if (Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(world.getChunkAt(x - 1, z + 1)).getNation(), nation)) {
            adjacentChunks++;
        }
        if (Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(world.getChunkAt(x - 1, z)).getNation(), nation)) {
            adjacentChunks++;
        }
        if (Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(world.getChunkAt(x - 1, z - 1)).getNation(), nation)) {
            adjacentChunks++;
        }
        if (Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(world.getChunkAt(x, z - 1)).getNation(), nation)) {
            adjacentChunks++;
        }
        if (Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(world.getChunkAt(x + 1, z - 1)).getNation(), nation)) {
            adjacentChunks++;
        }

        if (!isWilderness) {
            switch (adjacentChunks) {
                case 8:
                    return 20.0 / 1200.0;
                case 7:
                    return 20.0 / 2400.0;
                case 6:
                    return 20.0 / 4800.0;
                case 5:
                    return 20.0 / 7200.0;
                case 4:
                    return 20.0 / 12000.0;
                case 3:
                    return 20.0 / 24000.0;
                case 2:
                    return 20.0 / 48000.0;
                case 1:
                    return 20.0 / 72000.0;
                default:
                    return 20.0 / 288000.0;
            }
        } else {
            switch (adjacentChunks) {
                case 8:
                    return 20.0 / 200.0;
                case 7:
                    return 20.0 / 400.0;
                case 6:
                    return 20.0 / 600.0;
                case 5:
                    return 20.0 / 800.0;
                case 4:
                    return 20.0 / 1000.0;
                case 3:
                    return 20.0 / 1200.0;
                case 2:
                    return 20.0 / 1800.0;
                case 1:
                    return 20.0 / 2400.0;
                default:
                    return 20.0 / 12000.0;
            }

        }
    }
}
