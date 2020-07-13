package me.tedwoodworth.diplomacy.nations.contest;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.*;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;

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
        Contest contest = contests.get(diplomacyChunk);
        if (contest == null) {
            contest = new Contest(attackingNation, diplomacyChunk, isWilderness);
            contests.put(diplomacyChunk, contest);
        }

        if (contestTaskID == -1) {
            contestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onContestTask, 0L, 20L);
        }
    }

    private void onContestTask() {
        for (Contest contest : contests.values()) {
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
        DiplomacyChunk diplomacyChunk = contest.getDiplomacyChunk();
        Nation defendingNation = diplomacyChunk.getNation();
        defendingNation.removeChunk(contest.getDiplomacyChunk());
        contest.getAttackingNation().addChunk(diplomacyChunk);
        for (NationGroup group : defendingNation.getGroups()) {
            if (group.getChunks().contains(diplomacyChunk)) {
                group.removeChunk(diplomacyChunk);
            }
        }
        endContest(contest);
    }

    private void winWildernessContest(Contest contest) {
        DiplomacyChunk diplomacyChunk = contest.getDiplomacyChunk();
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

        DiplomacyChunk diplomacyChunk = contest.getDiplomacyChunk();
        Chunk chunk = diplomacyChunk.getChunk();
        Nation attackingNation = contest.getAttackingNation();
        Nation defendingNation = diplomacyChunk.getNation();
        int attackingPlayers = 0;
        int defendingPlayers = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getChunk().equals(chunk)) {
                DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                boolean isAttackingNationAlly = attackingNation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
                boolean isDefendingNationAlly = defendingNation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
                boolean isAttackingNation = Nations.getInstance().get(diplomacyPlayer).equals(attackingNation);
                boolean isDefendingNation = Nations.getInstance().get(diplomacyPlayer).equals(defendingNation);
                if (isAttackingNation || isAttackingNationAlly && !isDefendingNationAlly) {
                    attackingPlayers++;
                } else if (isDefendingNation || isDefendingNationAlly && !isAttackingNationAlly) {
                    defendingPlayers++;
                }
            }
        }
        //TODO defendingPlayers++ for every defending nation guard within the chunk
        //TODO only defendingPlayers++ if there is an unobstructed line of view between the defender and the attackers (meaning the defenders can be attacked)

        double attackingAdjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, attackingNation, false);
        double defendingAdjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, defendingNation, false);

        if (attackingPlayers > defendingPlayers) {
            contest.setProgress(contest.getProgress() + Math.pow(2.0, (attackingPlayers - defendingPlayers)) * attackingAdjacentCoefficient);
        } else if (attackingPlayers < defendingPlayers) {
            contest.setProgress(contest.getProgress() - Math.pow(2.0, (defendingPlayers - attackingPlayers)) * defendingAdjacentCoefficient);
        }
    }

    public void wildernessProgressChange(Contest contest) {
        DiplomacyChunk diplomacyChunk = contest.getDiplomacyChunk();
        Chunk chunk = diplomacyChunk.getChunk();
        Nation nation = contest.getAttackingNation();
        int players = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getChunk().equals(chunk)) {
                DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                boolean isAttackingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), nation);
                boolean isAttackingNationAlly = nation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
                if (isAttackingNation || isAttackingNationAlly) {
                    players++;
                }
            }
        }
        double adjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, nation, true);
        if (players > 0) {
            contest.setProgress(contest.getProgress() + Math.pow(2.0, players) * adjacentCoefficient);
        }
    }

    public double getAdjacentCoefficient(DiplomacyChunk diplomacyChunk, Nation nation, boolean isWilderness) {
        World world = diplomacyChunk.getChunk().getWorld();
        int x = diplomacyChunk.getChunk().getX();
        int z = diplomacyChunk.getChunk().getZ();
        int adjacentChunks = 0;
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
