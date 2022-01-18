package me.tedwoodworth.diplomacy.nations.contest;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.events.NationAddChunksEvent;
import me.tedwoodworth.diplomacy.events.NationRemoveChunksEvent;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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

    private final File ongoingContestsFile = new File(Diplomacy.getInstance().getDataFolder(), "ongoingContests.yml");
    private final YamlConfiguration config;

    private final Map<DiplomacyChunk, Contest> contests = new HashMap<>();
    private final Map<Entity, Integer> glowing = new HashMap<>();

    private int contestTaskID;
    private int particleTaskID;

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    public void addGlow(Entity entity) {
        if (glowing.containsKey(entity)) {
            glowing.replace(entity, glowing.get(entity) + 1);
        } else {
            glowing.put(entity, 1);
            entity.setGlowing(true);
        }
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> removeGlow(entity), 100L);
    }

    private void removeGlow(Entity entity) {
        if (glowing.containsKey(entity)) {
            var i = glowing.get(entity);
            if (i == 1) {
                glowing.remove(entity);
                entity.setGlowing(false);
            } else {
                glowing.replace(entity, glowing.get(entity) - 1);
            }
        }
    }

    public void startContest(Nation attackingNation, DiplomacyChunk diplomacyChunk) {
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

            var initializedContestSection = Contest.initializeContest(contestSection, attackingNation, diplomacyChunk);
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
            updateProgress(contest);
            contest.sendGlow();
            contest.sendSubtitles();
            if (contest.getProgress() >= 1.0) {
                if (contest.isWilderness()) {
                    winWildernessContest(contest);
                } else {
                    winContest(contest);
                }
            } else if (contest.getProgress() < 0.0) {
                loseContest(contest);
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


        var attackingNation = contest.getAttackingNation();
        attackingNation.addChunk(diplomacyChunk);

        var set = new HashSet<DiplomacyChunk>();
        set.add(diplomacyChunk);
        Bukkit.getPluginManager().callEvent(new NationRemoveChunksEvent(defendingNation, set));
        Bukkit.getPluginManager().callEvent(new NationAddChunksEvent(attackingNation, set));

        if (defendingNation.getGroups() != null) {
            for (var group : defendingNation.getGroups()) {
                if (group.getChunks().contains(diplomacyChunk)) {
                    group.removeChunk(diplomacyChunk);
                }
            }
        }
        contest.sendWinSound();
        contest.sendNewNationTitles();
        endContest(contest);
    }


    public void loseContest(Contest contest) {
        contest.sendLoseSound();
        contest.sendBlockdTitles();
        endContest(contest);
    }


    private void winWildernessContest(Contest contest) {
        var diplomacyChunk = contest.getDiplomacyChunk();
        var attackingNation = contest.getAttackingNation();
        attackingNation.addChunk(diplomacyChunk);

        var set = new HashSet<DiplomacyChunk>();
        set.add(diplomacyChunk);
        Bukkit.getPluginManager().callEvent(new NationAddChunksEvent(attackingNation, set));

        contest.sendWinSound();
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

    public void updateProgress(Contest contest) {
        var diplomacyChunk = contest.getDiplomacyChunk();
        var chunk = diplomacyChunk.getChunk();
        var attackingNation = contest.getAttackingNation();
        var isWilderness = contest.isWilderness();
        var attackingPlayers = new HashSet<DiplomacyPlayer>();


        if (isWilderness) {
            for (var entity : chunk.getEntities()) {
                if (entity instanceof Player) {
                    var player = ((Player) entity);
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    if (nation == null) continue;
                    var nationID = nation.getNationID();
                    var isAttackingNationAlly = nationID != null && attackingNation.getAllyNationIDs().contains(nationID);
                    var isAttackingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), attackingNation);
                    if (isAttackingNation || isAttackingNationAlly) {
                        attackingPlayers.add(diplomacyPlayer);
                    }
                }
            }
            var attackingPlayerCount = attackingPlayers.size();
            if (attackingPlayerCount > 0) { // if there are attackers
                contest.setProgress(contest.getProgress() + 10.0 / 200.0 * attackingPlayerCount);
            } else { // if there are no defenders and no attackers
                contest.setProgress(contest.getProgress() - 10.0 / 200.0);
            }
        } else {
            var defendingNation = diplomacyChunk.getNation();
            if (defendingNation == null) {
                Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> endContest(contest));
                return;
            }

            var defending = new HashSet<>();
            for (var entity : chunk.getEntities()) {
                if (GuardManager.getInstance().isGuard(entity)) {
                    defending.add(entity);
                } else if (entity instanceof Player) {
                    var player = ((Player) entity);
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
                        defending.add(diplomacyPlayer);
                    }
                }
            }
            var attackingPlayerCount = attackingPlayers.size();
            var defendingCount = defending.size();

            if (attackingPlayerCount > 0 && defendingCount > 0) { // if there are attackers and defenders
                for (var attacker : attackingPlayers) {
                    var offline = attacker.getOfflinePlayer();
                    if (offline.getPlayer() == null) continue;
                    offline.getPlayer().sendTitle(ChatColor.RED + "Progress Blocked", ChatColor.RED + "Must kill all defenders and guards to gain progress", 0, 30, 5);
                }

                for (var defender : defending) {
                    if (defender instanceof DiplomacyPlayer) {
                        var offline = ((DiplomacyPlayer) defender).getOfflinePlayer();
                        if (offline.getPlayer() == null) continue;
                        offline.getPlayer().sendTitle(ChatColor.GREEN + "Blocking Progress", ChatColor.RED + "Must kill all attackers to reduce progress", 0, 30, 5);
                    }
                }
            } else if (attackingPlayerCount > 0) { // if there are attackers and no defenders
                contest.setProgress(contest.getProgress() + 10.0 / 1200.0 * attackingPlayerCount);
            } else if (defendingCount > 0) { // if there are defenders and no attackers
                contest.setProgress(contest.getProgress() - 10.0 / 1200.0 * defendingCount);
            } else { // if there are no defenders and no attackers
                contest.setProgress(contest.getProgress() - 10.0 / 1200.0 * 0.5);
            }
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
