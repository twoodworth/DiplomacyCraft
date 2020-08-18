package me.tedwoodworth.diplomacy.nations.contest;

import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Contest {

    private ConfigurationSection configSection;
    private double progress;
    private int vacantTimer;
    private final Nation attackingNation;
    private final DiplomacyChunk diplomacyChunk;
    private boolean isWilderness;
    private final String contestID;

    Contest(String contestID, ConfigurationSection configSection) {
        String strProgress = configSection.getString("Progress");
        if (strProgress == null) {
            this.progress = 0.0;
        } else {
            this.progress = Double.parseDouble(strProgress);
        }
        String strVacantTimer = configSection.getString("VacantTimer");
        if (strVacantTimer == null) {
            this.vacantTimer = 0;
        } else {
            this.vacantTimer = Integer.parseInt(strVacantTimer);
        }
        this.configSection = configSection;
        this.contestID = contestID;

        this.attackingNation = Nations.getInstance().get(configSection.getString("AttackingNation"));

        World world = Bukkit.getWorld(Objects.requireNonNull(configSection.getString("World")));
        int x = configSection.getInt("x");
        int z = configSection.getInt("z");
        Chunk chunk = Objects.requireNonNull(world).getChunkAt(x, z);
        this.diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);

        this.isWilderness = configSection.getBoolean("IsWilderness");
    }

    public static ConfigurationSection initializeContest(ConfigurationSection contestSection, Nation attackingNation, DiplomacyChunk diplomacyChunk, boolean isWilderness) {

        contestSection.set("AttackingNation", attackingNation.getName());
        Nation defendingNation = diplomacyChunk.getNation();

        contestSection.set("IsWilderness", defendingNation == null);

        Chunk chunk = diplomacyChunk.getChunk();
        World world = chunk.getWorld();
        int x = chunk.getX();
        int z = chunk.getZ();
        contestSection.set("World", world.getName());
        contestSection.set("x", x);
        contestSection.set("z", z);
        return contestSection;
    }

    public boolean isNearChunk(Player player, Chunk chunk) {
        var chunkCenter = new Location(chunk.getWorld(), chunk.getX() * 16 + 8, player.getLocation().getY(), chunk.getZ() * 16 + 8);
        return player.getWorld().equals(chunk.getWorld()) && player.getLocation().distanceSquared(chunkCenter) < 10000;
    }

    public void sendNewNationTitles() {
        var chunk = diplomacyChunk.getChunk();
        for (var player : Bukkit.getOnlinePlayers()) {
            var playerChunk = player.getLocation().getChunk();
            if (chunk.equals(playerChunk)) {
                var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                Nation nation = Nations.getInstance().get(diplomacyPlayer);
                if (nation == null) {
                    player.sendTitle(ChatColor.BLUE + attackingNation.getName(), null, 5, 40, 10);
                } else if (attackingNation.getEnemyNationIDs().contains(nation.getNationID())) {
                    player.sendTitle(ChatColor.RED + attackingNation.getName(), null, 5, 40, 10);
                } else if (attackingNation.getAllyNationIDs().contains(nation.getNationID()) || attackingNation.equals(nation)) {
                    player.sendTitle(ChatColor.GREEN + attackingNation.getName(), null, 5, 40, 10);
                } else {
                    player.sendTitle(ChatColor.BLUE + attackingNation.getName(), null, 5, 40, 10);
                }
            }
        }
    }

    public void sendSubtitles() {
        var defendingNation = diplomacyChunk.getNation();
        var chunk = diplomacyChunk.getChunk();
        for (var player : Bukkit.getOnlinePlayers()) {
            var playerChunk = player.getLocation().getChunk();
            if (chunk.equals(playerChunk)) {
                var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                sendPlayerSubtitle(diplomacyPlayer, attackingNation, defendingNation);
            }
        }
    }

    private void sendPlayerSubtitle(DiplomacyPlayer diplomacyPlayer, Nation attackingNation, Nation defendingNation) {
        var player = Bukkit.getPlayer(diplomacyPlayer.getUUID());
        Validate.notNull(player);
        var nation = Nations.getInstance().get(diplomacyPlayer);
        var playerIsWilderness = nation == null;
        var defendingNationIsWilderness = Nations.isWilderness(defendingNation);

        ChatColor color1;
        ChatColor color2;

        if (defendingNationIsWilderness) {
            color2 = ChatColor.GRAY;
            if (playerIsWilderness) {
                color1 = ChatColor.DARK_BLUE;
            } else {
                var isAttackingNationAlly = attackingNation.getAllyNationIDs().contains(nation.getNationID());
                var isAttackingNationEnemy = attackingNation.getEnemyNationIDs().contains(nation.getNationID());
                var isAttackingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), attackingNation);

                if (isAttackingNationAlly || isAttackingNation) {
                    color1 = ChatColor.GREEN;
                } else if (isAttackingNationEnemy) {
                    color1 = ChatColor.DARK_RED;
                } else {
                    color1 = ChatColor.DARK_BLUE;
                }
            }
        } else {
            if (playerIsWilderness) {
                color1 = ChatColor.DARK_BLUE;
                color2 = ChatColor.AQUA;
            } else {
                var isAttackingNationAlly = attackingNation.getAllyNationIDs().contains(nation.getNationID());
                var isDefendingNationAlly = defendingNation.getAllyNationIDs().contains(nation.getNationID());
                var isAttackingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), attackingNation);
                var isDefendingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), defendingNation);
                var isAttackingNationEnemy = attackingNation.getEnemyNationIDs().contains(nation.getNationID());
                var isDefendingNationEnemy = defendingNation.getEnemyNationIDs().contains(nation.getNationID());
                if (isAttackingNation || isAttackingNationAlly && !isDefendingNationAlly) {
                    color1 = ChatColor.GREEN;
                    if (isDefendingNationEnemy) {
                        color2 = ChatColor.DARK_RED;
                    } else {
                        color2 = ChatColor.DARK_BLUE;
                    }

                } else if (isDefendingNation || isDefendingNationAlly && !isAttackingNationAlly) {
                    color2 = ChatColor.GREEN;
                    if (isAttackingNationEnemy) {
                        color1 = ChatColor.DARK_RED;
                    } else {
                        color1 = ChatColor.DARK_BLUE;
                    }
                } else if (isAttackingNationAlly && !isDefendingNationEnemy) {
                    color1 = ChatColor.DARK_GREEN;
                    color2 = ChatColor.GREEN;
                } else if (isAttackingNationEnemy && isDefendingNationEnemy) {
                    color1 = ChatColor.DARK_RED;
                    color2 = ChatColor.RED;
                } else if (isAttackingNationEnemy) {
                    color1 = ChatColor.DARK_RED;
                    color2 = ChatColor.DARK_BLUE;
                } else if (isDefendingNationEnemy) {
                    color1 = ChatColor.DARK_BLUE;
                    color2 = ChatColor.DARK_RED;
                } else {
                    color1 = ChatColor.DARK_BLUE;
                    color2 = ChatColor.AQUA;
                }
            }
        }

        String attackingNationName = attackingNation.getName();
        String defendingNationName;

        if (defendingNationIsWilderness) {
            defendingNationName = "Wilderness";
        } else {
            defendingNationName = defendingNation.getName();
        }

        String subtitle = createSubtitle(color1, color2, attackingNationName, defendingNationName);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(subtitle));

    }

    private String createSubtitle(ChatColor color1, ChatColor color2, String attackingNationName, String
            defendingNationName) {
        var color1bars = (int) (progress * 50);
        var color2bars = 50 - color1bars;

        String subtitle = "" + color1 + attackingNationName + " ";
        for (var i = 0; i < color1bars; i++) {
            subtitle += "|";
        }
        subtitle += color2;

        for (var i = 0; i < color2bars; i++) {
            subtitle += "|";
        }

        subtitle += " " + defendingNationName;

        return subtitle;
    }

    public void sendParticles() {
        var defendingNation = diplomacyChunk.getNation();
        var chunk = diplomacyChunk.getChunk();
        for (var player : Bukkit.getOnlinePlayers()) {
            if (isNearChunk(player, chunk)) {
                var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());

                sendPlayerParticles(chunk, diplomacyPlayer, attackingNation, defendingNation);
            }
        }
    }

    public void sendPlayerParticles(Chunk chunk, DiplomacyPlayer diplomacyPlayer, Nation attackingNation, Nation
            defendingNation) {
        var player = Bukkit.getPlayer(diplomacyPlayer.getUUID());
        Validate.notNull(player);
        var baseX = chunk.getX() * 16;
        var baseZ = chunk.getZ() * 16;
        var world = chunk.getWorld();
        var nation = Nations.getInstance().get(diplomacyPlayer);
        var isWilderness = nation == null;


        var particle = Particle.REDSTONE;
        Particle.DustOptions dustOptions;
        if (isWilderness) {
            dustOptions = new Particle.DustOptions(Color.fromRGB(220, 220, 220), 1);
        } else {
            var defendingNationIsWilderness = Nations.isWilderness(defendingNation);
            var isAttackingNationAlly = attackingNation.getAllyNationIDs().contains(nation.getNationID());
            var isDefendingNationAlly = !defendingNationIsWilderness && defendingNation.getAllyNationIDs().contains(nation.getNationID());
            var isAttackingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), attackingNation);
            var isDefendingNation = Objects.equals(Nations.getInstance().get(diplomacyPlayer), defendingNation);

            if (isAttackingNation || isAttackingNationAlly && !isDefendingNationAlly) {
                dustOptions = new Particle.DustOptions(Color.LIME, 1);
            } else if (isDefendingNation || isDefendingNationAlly && !isAttackingNationAlly) {
                dustOptions = new Particle.DustOptions(Color.RED, 1);
            } else if (isDefendingNationAlly) {
                dustOptions = new Particle.DustOptions(Color.YELLOW, 1);
            } else {
                dustOptions = new Particle.DustOptions(Color.AQUA, 1);
            }
        }
        var min = Math.max(0, player.getLocation().getBlockY() - 17);
        var max = Math.min(255, player.getLocation().getBlockY() + 20);
        for (var y = min; y < max; y += 6) {
            for (var i = 0; i < 16; i += 1) {
                player.spawnParticle(particle, new Location(world, baseX + i, y, baseZ + .3), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX + 16 - .3, y, baseZ + i), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX + 16 - i, y, baseZ + 16 - .3), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX - .3, y, baseZ + 16 - i), 1, dustOptions);

            }

        }

    }


    public void sendFireworks() {
        var chunk = diplomacyChunk.getChunk();
        for (var player : Bukkit.getOnlinePlayers()) {
            var playerChunk = player.getLocation().getChunk();
            if (chunk.equals(playerChunk)) {
                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1, 1);
            }
        }
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        configSection.set("Progress", progress);
    }

    public int getVacantTimer() {
        return vacantTimer;
    }

    public void setVacantTimer(int vacantTimer) {
        this.vacantTimer = vacantTimer;
        configSection.set("VacantTimer", vacantTimer);
    }

    public Nation getAttackingNation() {
        return attackingNation;
    }

    public DiplomacyChunk getDiplomacyChunk() {
        return diplomacyChunk;
    }

    public boolean isWilderness() {
        return isWilderness;
    }

    public void setIsWilderness(boolean isWilderness) {
        this.isWilderness = isWilderness;
        configSection.set("IsWilderness", isWilderness);
    }

    public String getContestID() {
        return contestID;
    }
}
