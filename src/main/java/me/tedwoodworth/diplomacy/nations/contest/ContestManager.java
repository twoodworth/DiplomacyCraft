package me.tedwoodworth.diplomacy.nations.contest;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

public class ContestManager {

    private static ContestManager instance = null;

    private File ongoingContestsFile = new File(Diplomacy.getInstance().getDataFolder(), "ongoingContests.yml");
    private YamlConfiguration config;

    private Map<DiplomacyChunk, Contest> contests = new HashMap<>();

    private int contestTaskID = -1;
    private int particleTaskID = -1;

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    public void startContest(Nation attackingNation, DiplomacyChunk diplomacyChunk, boolean isWilderness) {
        var contest = contests.get(diplomacyChunk);
        if (contest == null) {
            var nextContestID = config.getString("NextContestID");
            if (nextContestID == null) {
                config.set("NextContestID", "0");
                nextContestID = "0";
            }

            var contestID = nextContestID;
            nextContestID = String.valueOf(Integer.parseInt(nextContestID) + 1);

            Reader reader = new InputStreamReader(Objects.requireNonNull(Diplomacy.getInstance().getResource("Default/Contest.yml")));
            ConfigurationSection contestSection = YamlConfiguration.loadConfiguration(reader);
            config.set("Contests." + contestID, contestSection);
            config.set("NextContestID", nextContestID);

            var initializedContestSection = Contest.initializeContest(contestSection, attackingNation, diplomacyChunk, isWilderness);
            contest = new Contest(contestID, initializedContestSection);
            contests.put(diplomacyChunk, contest);
        }

        if (contestTaskID == -1) {
            contestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onContestTask, 0L, 10L);
            particleTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onParticleTask, 0L, 30L);
            config.set("ContestTaskRunning", "true");
        }
    }


    public static ContestManager getInstance() {
        if (instance == null) {
            instance = new ContestManager();
        }
        return instance;
    }

    private ContestManager() {
        config = YamlConfiguration.loadConfiguration(ongoingContestsFile);
        var contestsSection = config.getConfigurationSection("Contests");
        if (contestsSection == null) {
            contestsSection = config.createSection("Contests");
        }
        var strContestTaskRunning = config.getString("ContestTaskRunning");
        if (strContestTaskRunning == null) {
            config.set("ContestTaskRunning", "false");
        }
        var contestTaskRunning = Boolean.parseBoolean(strContestTaskRunning);
        if (contestTaskRunning) {
            contestTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onContestTask, 0L, 2L);
            particleTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onParticleTask, 0L, 30L);
        } else {
            contestTaskID = -1;
            particleTaskID = -1;
        }

        for (var contestID : Objects.requireNonNull(contestsSection).getKeys(false)) {
            var contestSection = config.getConfigurationSection("Contests." + contestID);
            Validate.notNull(contestSection);


            var contest = new Contest(contestID, contestSection);
            DiplomacyChunk diplomacyChunk = contest.getDiplomacyChunk();
            contests.put(diplomacyChunk, contest);
        }
    }


    private void onContestTask() {
        for (var contest : new ArrayList<>(contests.values())) {
            if (contest.isWilderness()) {
                wildernessProgressChange(contest);
            } else {
                updateProgress(contest);
            }
            contest.sendSubtitles();
            if (contest.getProgress() >= 1.0) {
                if (contest.isWilderness()) {
                    winWildernessContest(contest);
                } else {
                    winContest(contest);
                }
            } else if (contest.getProgress() < 0.0) {
                endContest(contest);
            }
        }
    }

    private void onParticleTask() {
        for (var contest : new ArrayList<>(contests.values())) {
            contest.sendParticles();
        }
    }

    public void winContest(Contest contest) {
        var diplomacyChunk = contest.getDiplomacyChunk();
        var defendingNation = diplomacyChunk.getNation();
        Objects.requireNonNull(defendingNation).removeChunk(contest.getDiplomacyChunk());
        contest.getAttackingNation().addChunk(diplomacyChunk);
        if (defendingNation.getGroups() != null) {
            for (var group : defendingNation.getGroups()) {
                if (group.getChunks().contains(diplomacyChunk)) {
                    group.removeChunk(diplomacyChunk);
                }
            }
        }
        contest.sendFireworks();
        contest.sendNewNationTitles();
        endContest(contest);
    }

    private void winWildernessContest(Contest contest) {
        var diplomacyChunk = contest.getDiplomacyChunk();
        contest.getAttackingNation().addChunk(diplomacyChunk);
        contest.sendFireworks();
        contest.sendNewNationTitles();
        endContest(contest);
    }

    public void endContest(Contest contest) {
        config.set("Contests." + contest.getContestID(), null);
        contests.remove(contest.getDiplomacyChunk());
        if (contests.size() == 0) {
            Bukkit.getScheduler().cancelTask(contestTaskID);
            contestTaskID = -1;

            Bukkit.getScheduler().cancelTask(particleTaskID);
            particleTaskID = -1;

            config.set("ContestTaskRunning", "false");

        }
    }

    private boolean defenderIsVisible(HashSet<DiplomacyPlayer> attackers, DiplomacyPlayer diplomacyDefender) {
        //D to A
        var defender = diplomacyDefender.getPlayer().getPlayer();
        if (defender == null) {
            return false;
        }
        var dLocation = defender.getEyeLocation();
        for (var diplomacyAttacker : attackers) {
            var attacker = diplomacyAttacker.getPlayer().getPlayer();
            if (attacker == null) {
                continue;
            }
            var aLocation = attacker.getEyeLocation();
            var vector = aLocation.toVector().subtract(dLocation.toVector());
            var j = Math.floor(vector.length());
            vector.multiply(1 / vector.length());
            for (var i = 0; i <= j; i++) {
                vector = aLocation.toVector().subtract(dLocation.toVector());
                vector.multiply(1 / vector.length());
                var world = defender.getWorld();
                var block = world.getBlockAt((dLocation.toVector().add(vector.multiply(i))).toLocation(defender.getWorld()));
                if (!block.getType().equals(Material.AIR) && !block.getType().equals(Material.WATER) && !block.isPassable()) {
                    System.out.println("False");
                    return false;
                }
            }
        }
        System.out.println("True");
        return true;
    }

    public void updateProgress(Contest contest) {

        var diplomacyChunk = contest.getDiplomacyChunk();
        var chunk = diplomacyChunk.getChunk();
        var attackingNation = contest.getAttackingNation();
        var defendingNation = diplomacyChunk.getNation();
        Validate.notNull(defendingNation);
        var attackingPlayers = new HashSet<DiplomacyPlayer>();
        var defendingPlayers = new HashSet<DiplomacyPlayer>();
        for (var player : Bukkit.getOnlinePlayers()) {
            var pChunk = player.getLocation().getChunk();
            var pX = pChunk.getX();
            var x = chunk.getX();
            if (pX != x) continue;


            var z = chunk.getZ();
            var pZ = pChunk.getZ();
            if (pZ != z) continue;

            var pWorld = pChunk.getWorld();
            var world = chunk.getWorld();
            if (!world.equals(pWorld)) continue;

            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            var nation = Nations.getInstance().get(diplomacyPlayer);
            if (nation == null) continue;

            var nationID = nation.getNationID();
            var isAttackingNationAlly = nationID != null && attackingNation.getAllyNationIDs().contains(nationID);
            var isDefendingNationAlly = defendingNation.getAllyNationIDs().contains(nationID);
            var isAttackingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), attackingNation);
            var isDefendingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), defendingNation);
            if (isAttackingNation || isAttackingNationAlly && !isDefendingNationAlly) {
                attackingPlayers.add(diplomacyPlayer);
            } else if (isDefendingNation || isDefendingNationAlly && !isAttackingNationAlly) {
                defendingPlayers.add(diplomacyPlayer);
            }
        }

        var attackingPlayerCount = attackingPlayers.size();
        int defendingPlayerCount;
        if (attackingPlayerCount == 0) {
           defendingPlayerCount = defendingPlayers.size();
        } else {
            defendingPlayerCount = 0;
        }

        for (var defendingPlayer : defendingPlayers) {
            if (defenderIsVisible(attackingPlayers, defendingPlayer)) {
                defendingPlayerCount++;
            }
        }

        //TODO defendingPlayers++ for every defending nation guard within the chunk

        var attackingAdjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, attackingNation, false);
        var defendingAdjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, defendingNation, false);

        if (attackingPlayerCount > defendingPlayerCount) {
            contest.setProgress(contest.getProgress() + Math.pow(2.0, (attackingPlayerCount - defendingPlayerCount)) * attackingAdjacentCoefficient);
        } else if (attackingPlayerCount < defendingPlayerCount) {
            contest.setProgress(contest.getProgress() - Math.pow(2.0, (defendingPlayerCount - attackingPlayerCount)) * defendingAdjacentCoefficient);
        } else if (attackingPlayerCount == 0 && contest.getVacantTimer() == 600) {
            contest.setProgress(contest.getProgress() - Math.pow(2.0, (0.5)) * defendingAdjacentCoefficient);
        }

        if (attackingPlayerCount == 0 && defendingPlayerCount == 0) {
            if (contest.getVacantTimer() < 600) {
                contest.setVacantTimer(contest.getVacantTimer() + 1);
            }
        } else {
            contest.setVacantTimer(0);
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
                var testNation = Nations.getInstance().get(diplomacyPlayer);
                var isAttackingNationAlly = testNation != null && nation.getAllyNationIDs().contains(testNation.getNationID());
                if (isAttackingNation || isAttackingNationAlly) {
                    players++;
                }
            }
        }
        var adjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, nation, true);
        if (players > 0) {
            contest.setProgress(contest.getProgress() + Math.pow(2.0, players) * adjacentCoefficient);
            if (contest.getVacantTimer() != 0) {
                contest.setVacantTimer(0);
            }
        } else if (contest.getVacantTimer() < 120) {
            contest.setVacantTimer(contest.getVacantTimer() + 1);
        } else if (contest.getVacantTimer() == 120) {
            contest.setProgress(-.01);
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
            return switch (adjacentChunks) {
                default -> 5.0 / 1728000.0;
                case 1 -> 5.0 / 345600.0;
                case 2 -> 5.0 / 14400.0;
                case 3 -> 5.0 / 3840.0;
                case 4 -> 5.0 / 2880.0;
                case 5 -> 5.0 / 2400.0;
                case 6 -> 5.0 / 1920.0;
                case 7 -> 5.0 / 1440.0;
                case 8 -> 5.0 / 960.0;
            };
        } else {
            return switch (adjacentChunks) {
                case 8 -> 5.0 / 100.0;
                case 7 -> 5.0 / 150.0;
                case 6 -> 5.0 / 200.0;
                case 5 -> 5.0 / 250.0;
                case 4 -> 5.0 / 300.0;
                case 3 -> 5.0 / 400.0;
                case 2 -> 5.0 / 600.0;
                case 1 -> 5.0 / 3000.0;
                default -> 5.0 / 12000.0;
            };

        }
    }

    public boolean isBeingContested(DiplomacyChunk diplomacyChunk) {
        return contests.containsKey(diplomacyChunk);
    }

    public Collection<Contest> getContests() {
        return contests.values();
    }

    public void save() {
        try {
            config.save(ongoingContestsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }

        @EventHandler
        void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin().equals(Diplomacy.getInstance())) {
                save();
            }
        }
    }
}
