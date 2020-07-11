package me.tedwoodworth.diplomacy.nations;

import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DiplomacyChunks {

    private static List<DiplomacyChunk> diplomacyChunks;
    private static DiplomacyChunks instance = null;

    public static DiplomacyChunks getInstance() {
        if (instance == null) {
            instance = new DiplomacyChunks();
        }
        return instance;
    }

    public void createDiplomacyChunk(DiplomacyPlayer diplomacyPlayer) {
        Player player = Bukkit.getPlayer(diplomacyPlayer.getUUID());
        assert player != null;
        Chunk chunk = player.getLocation().getChunk();
        Nation nation = Nations.getInstance().get(diplomacyPlayer);
        DiplomacyChunk diplomacyChunk = new DiplomacyChunk(chunk, nation);
        diplomacyChunks.add(diplomacyChunk);
    }

    public DiplomacyChunk getFromPlayer(DiplomacyPlayer diplomacyPlayer) {
        Player player = Bukkit.getPlayer(diplomacyPlayer.getUUID());
        assert player != null;
        Chunk chunk = player.getLocation().getChunk();
        for (DiplomacyChunk diplomacyChunk : diplomacyChunks) {
            if (diplomacyChunk.getChunk().equals(chunk)) {
                return diplomacyChunk;
            }
        }
        return null;
    }

    public DiplomacyChunk getFromChunk(Chunk chunk) {
        for (DiplomacyChunk diplomacyChunk : diplomacyChunks) {
            if (diplomacyChunk.getChunk().equals(chunk)) {
                return diplomacyChunk;
            }
        }
        return null;
    }

    public List<DiplomacyChunk> getNationChunks(Nation nation) {
        List<DiplomacyChunk> nationChunks = new ArrayList<>(1);
        for (DiplomacyChunk diplomacyChunk : diplomacyChunks) {
            if (diplomacyChunk.getNation().equals(nation)) {
                nationChunks.add(diplomacyChunk);
            }
        }
        return nationChunks;
    }
}
