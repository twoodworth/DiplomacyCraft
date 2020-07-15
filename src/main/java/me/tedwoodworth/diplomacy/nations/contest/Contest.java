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
import org.bukkit.FireworkEffect.Type;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Objects;
import java.util.Random;

public class Contest {

    private ConfigurationSection configSection;
    private double progress = 0.0;
    private final Nation attackingNation;
    private final DiplomacyChunk diplomacyChunk;
    private final boolean isWilderness;
    private final String contestID;

    Contest(String contestID, ConfigurationSection configSection) {
        this.configSection = configSection;
        this.contestID = contestID;

        Nation attackingNation = Nations.getInstance().get(configSection.getString("AttackingNation"));
        this.attackingNation = attackingNation;

        World world = Bukkit.getWorld(configSection.getString("World"));
        int x = configSection.getInt("x");
        int z = configSection.getInt("z");
        Chunk chunk = world.getChunkAt(x, z);
        DiplomacyChunk diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        this.diplomacyChunk = diplomacyChunk;

        this.isWilderness = configSection.getBoolean("IsWilderness");
    }

    public static ConfigurationSection initializeContest(ConfigurationSection contestSection, Nation attackingNation, DiplomacyChunk diplomacyChunk, boolean isWilderness) {

        contestSection.set("AttackingNation", attackingNation.getName());
        Nation defendingNation = diplomacyChunk.getNation();
        if (defendingNation != null) {
            contestSection.createSection("DefendingNation");
            contestSection.set("DefendingNation", diplomacyChunk.getNation().getName());
            contestSection.set("IsWilderness", false);
        } else {
            contestSection.set("IsWilderness", true);
        }
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
                    player.sendTitle(ChatColor.BLUE + attackingNation.getName(), null, 5, 30, 5);
                } else if (attackingNation.getEnemyNationIDs().contains(nation.getNationID())) {
                    player.sendTitle(ChatColor.RED + attackingNation.getName(), null, 5, 30, 5);
                } else if (attackingNation.getAllyNationIDs().contains(nation.getNationID()) || attackingNation.equals(nation)) {
                    player.sendTitle(ChatColor.GREEN + attackingNation.getName(), null, 5, 30, 5);
                } else {
                    player.sendTitle(ChatColor.BLUE + attackingNation.getName(), null, 5, 30, 5);
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
                } else if (isAttackingNationAlly && isDefendingNationAlly) {
                    color1 = ChatColor.DARK_GREEN;
                    color2 = ChatColor.GREEN;
                } else if (isAttackingNationEnemy && isDefendingNationEnemy) {
                    color1 = ChatColor.DARK_RED;
                    color2 = ChatColor.RED;
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
            } else if (isDefendingNationAlly && isAttackingNationAlly) {
                dustOptions = new Particle.DustOptions(Color.YELLOW, 1);
            } else {
                dustOptions = new Particle.DustOptions(Color.AQUA, 1);
            }
        }
        var min = Math.max(0, player.getLocation().getBlockY() - 20);
        var max = Math.min(255, player.getLocation().getBlockY() + 20);
        for (var y = min; y < max; y++) {
            for (var i = 0; i < 16; i += 2) {
                player.spawnParticle(particle, new Location(world, baseX + i, y, baseZ), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX + 16, y, baseZ + i), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX + 16 - i, y, baseZ + 16), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX, y, baseZ + 16 - i), 1, dustOptions);

            }

        }

    }


    public void sendFireworks() {
        var chunk = diplomacyChunk.getChunk();
        for (var player : Bukkit.getOnlinePlayers()) {
            var playerChunk = player.getLocation().getChunk();
            if (chunk.equals(playerChunk)) {
                sendPlayerFireworks(chunk, player);
            }
        }
    }

    private void sendPlayerFireworks(Chunk chunk, Player player) {
        Random r = new Random();
        int x = chunk.getX() * 16;
        int z = chunk.getZ() * 16;
        for (int i = 0; i < 3; i++) {
            int rx = x + r.nextInt(15) + 1;
            int rz = z + r.nextInt(15) + 1;
            Location location = new Location(chunk.getWorld(), rx, player.getLocation().getBlockY(), rz);
            generateFireworks(chunk, location);
        }
    }

    private void generateFireworks(Chunk chunk, Location location) {
        var firework = (Firework) chunk.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        Random r = new Random();

        int rt = r.nextInt(4) + 1;
        Type type = Type.STAR;
        switch (rt) {
            case 1 -> type = Type.BALL;
            case 2 -> type = Type.BALL_LARGE;
            case 3 -> type = Type.BURST;
            case 4 -> type = Type.CREEPER;
        }

        int red = r.nextInt(256);
        int blue = r.nextInt(256);
        int green = r.nextInt(256);
        Color c1 = Color.fromRGB(red, green, blue);

        int red2 = r.nextInt(256);
        int blue2 = r.nextInt(256);
        int green2 = r.nextInt(256);
        Color c2 = Color.fromRGB(red2, green2, blue2);

        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();

        fireworkMeta.addEffect(effect);

        int rp = r.nextInt(2) + 1;
        fireworkMeta.setPower(rp);

        firework.setFireworkMeta(fireworkMeta);
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        configSection.set("Progress", progress);
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

    public String getContestID() {
        return contestID;
    }
}
