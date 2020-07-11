package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class ContestManager {
    public void contestPlot(DiplomacyChunk diplomacyChunk, Nation attackingNation) {
        int baseX = diplomacyChunk.getChunk().getX();
        int baseZ = diplomacyChunk.getChunk().getZ();
        double progress = 0.0;
        boolean isWilderness = (diplomacyChunk.getNation() == null);
        Chunk chunk = diplomacyChunk.getChunk();
        Nation defendingNation = diplomacyChunk.getNation();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(
                Diplomacy.getInstance(),
                () -> sendParticles(chunk, attackingNation, defendingNation, isWilderness),
                0L,
                20L
        );


    }

    public double progressChange(DiplomacyChunk diplomacyChunk, Nation attackingNation, Nation defendingNation) {
        Chunk chunk = diplomacyChunk.getChunk();
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
            return Math.pow(2.0, (attackingPlayers - defendingPlayers)) * attackingAdjacentCoefficient;
        } else if (attackingPlayers < defendingPlayers) {
            return -Math.pow(2.0, (defendingPlayers - attackingPlayers)) * defendingAdjacentCoefficient;
        } else {
            return 0.0;
        }
    }

    public double wildernessProgressChange(DiplomacyChunk diplomacyChunk, Nation nation) {
        Chunk chunk = diplomacyChunk.getChunk();
        int players = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().getChunk().equals(chunk)) {
                DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                boolean isAttackingNation = Nations.getInstance().get(diplomacyPlayer).equals(nation);
                boolean isAttackingNationAlly = nation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
                if (isAttackingNation || isAttackingNationAlly) {
                    players++;
                }
            }
        }
        double adjacentCoefficient = getAdjacentCoefficient(diplomacyChunk, nation, true);
        if (players > 0) {
            return Math.pow(2.0, players) * adjacentCoefficient;
        } else {
            return 0.0;
        }
    }

    public double getAdjacentCoefficient(DiplomacyChunk diplomacyChunk, Nation nation, boolean isWilderness) {

        int x = diplomacyChunk.getChunk().getX();
        int z = diplomacyChunk.getChunk().getZ();
        int adjacentChunks = 0;
        if (DiplomacyChunks.getInstance().getFromChunk(diplomacyChunk.getChunk().getWorld().getChunkAt(x + 1, z)).getNation().equals(nation)) {
            adjacentChunks++;
        }
        if (DiplomacyChunks.getInstance().getFromChunk(diplomacyChunk.getChunk().getWorld().getChunkAt(x + 1, z + 1)).getNation().equals(nation)) {
            adjacentChunks++;
        }
        if (DiplomacyChunks.getInstance().getFromChunk(diplomacyChunk.getChunk().getWorld().getChunkAt(x, z + 1)).getNation().equals(nation)) {
            adjacentChunks++;
        }
        if (DiplomacyChunks.getInstance().getFromChunk(diplomacyChunk.getChunk().getWorld().getChunkAt(x - 1, z + 1)).getNation().equals(nation)) {
            adjacentChunks++;
        }
        if (DiplomacyChunks.getInstance().getFromChunk(diplomacyChunk.getChunk().getWorld().getChunkAt(x - 1, z)).getNation().equals(nation)) {
            adjacentChunks++;
        }
        if (DiplomacyChunks.getInstance().getFromChunk(diplomacyChunk.getChunk().getWorld().getChunkAt(x - 1, z - 1)).getNation().equals(nation)) {
            adjacentChunks++;
        }
        if (DiplomacyChunks.getInstance().getFromChunk(diplomacyChunk.getChunk().getWorld().getChunkAt(x, z - 1)).getNation().equals(nation)) {
            adjacentChunks++;
        }
        if (DiplomacyChunks.getInstance().getFromChunk(diplomacyChunk.getChunk().getWorld().getChunkAt(x + 1, z - 1)).getNation().equals(nation)) {
            adjacentChunks++;
        }

        if (!isWilderness) {
            if (adjacentChunks == 8) {
                return 100.0 / 1200.0;
            } else if (adjacentChunks == 7) {
                return 100.0 / 2400.0;
            } else if (adjacentChunks == 6) {
                return 100.0 / 4800.0;
            } else if (adjacentChunks == 5) {
                return 100.0 / 7200.0;
            } else if (adjacentChunks == 4) {
                return 100.0 / 12000.0;
            } else if (adjacentChunks == 3) {
                return 100.0 / 24000.0;
            } else if (adjacentChunks == 2) {
                return 100.0 / 48000.0;
            } else if (adjacentChunks == 1) {
                return 100.0 / 72000.0;
            } else {
                return 100.0 / 288000.0;
            }
        } else {
            if (adjacentChunks == 8) {
                return 100.0 / 200.0;
            } else if (adjacentChunks == 7) {
                return 100.0 / 400.0;
            } else if (adjacentChunks == 6) {
                return 100.0 / 600.0;
            } else if (adjacentChunks == 5) {
                return 100.0 / 800.0;
            } else if (adjacentChunks == 4) {
                return 100.0 / 1000.0;
            } else if (adjacentChunks == 3) {
                return 100.0 / 1200.0;
            } else if (adjacentChunks == 2) {
                return 100.0 / 1800.0;
            } else if (adjacentChunks == 1) {
                return 100.0 / 2400.0;
            } else {
                return 100.0 / 12000.0;
            }

        }
    }

    public boolean isNearChunk(Player player, Chunk chunk) {
        Location chunkCenter = new Location(chunk.getWorld(), chunk.getX() * 16 + 8, player.getLocation().getY(), chunk.getZ() * 16 + 8);
        return player.getWorld().equals(chunk.getWorld()) && player.getLocation().distanceSquared(chunkCenter) < 10000;
    }

    public void sendParticles(Chunk chunk, Nation attackingNation, Nation defendingNation, boolean isWilderness) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isNearChunk(player, chunk)) {
                DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());

                sendPlayerParticles(chunk, diplomacyPlayer, attackingNation, defendingNation, isWilderness);
            }
        }
    }

    public void sendPlayerParticles(Chunk chunk, DiplomacyPlayer diplomacyPlayer, Nation attackingNation, Nation defendingNation, boolean isWilderness) {
        Player player = Bukkit.getPlayer(diplomacyPlayer.getUUID());
        int baseX = chunk.getX() * 16;
        int baseZ = chunk.getZ() * 16;
        World world = chunk.getWorld();

        boolean isAttackingNationAlly = attackingNation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
        boolean isDefendingNationAlly = defendingNation.getAllyNationIDs().contains(Nations.getInstance().get(diplomacyPlayer).getNationID());
        boolean isAttackingNation = Nations.getInstance().get(diplomacyPlayer).equals(attackingNation);
        boolean isDefendingNation = Nations.getInstance().get(diplomacyPlayer).equals(defendingNation);

        Particle particle = Particle.REDSTONE;
        Particle.DustOptions dustOptions;
        if (isAttackingNation || isAttackingNationAlly && !isDefendingNationAlly) {
            dustOptions = new Particle.DustOptions(Color.GREEN, 1);
        } else if (!isWilderness && isDefendingNation || isDefendingNationAlly && !isAttackingNationAlly) {
            dustOptions = new Particle.DustOptions(Color.RED, 1);
        } else {
            dustOptions = new Particle.DustOptions(Color.YELLOW, 1);
        }
        for (int y = 0; y < 255; y++) {
            for (int i = 0; i < 16; i++) {
                player.spawnParticle(particle, new Location(world, baseX + i, y, baseZ), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX + 16, y, baseZ + i), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX + 16 - i, y, baseZ + 16), 1, dustOptions);
                player.spawnParticle(particle, new Location(world, baseX, y, baseZ + 16 - i), 1, dustOptions);

            }

        }

    }
}
