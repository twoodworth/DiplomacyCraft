package me.tedwoodworth.diplomacy.nations;

import org.bukkit.Chunk;

import java.util.Map;
import java.util.WeakHashMap;

public class DiplomacyChunks {

    private static DiplomacyChunks instance = null;
    private final Map<Chunk, DiplomacyChunk> diplomacyChunks = new WeakHashMap<>();

    public static DiplomacyChunks getInstance() {
        if (instance == null) {
            instance = new DiplomacyChunks();
        }
        return instance;
    }


    public DiplomacyChunk getDiplomacyChunk(Chunk chunk) {
        var diplomacyChunk = diplomacyChunks.get(chunk);
        if (diplomacyChunk == null) {
            diplomacyChunk = new DiplomacyChunk(chunk);
            diplomacyChunks.put(chunk, diplomacyChunk);
        }
        return diplomacyChunk;
    }


}
