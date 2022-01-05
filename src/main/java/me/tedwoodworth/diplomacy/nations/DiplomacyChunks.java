//package me.tedwoodworth.diplomacy.nations;
//
//import com.google.common.collect.ImmutableMap;
//import org.bukkit.Bukkit;
//import org.bukkit.Chunk;
//
//import java.util.Map;
//import java.util.WeakHashMap;
//
//public class DiplomacyChunks {
//
//    private static DiplomacyChunks instance = null;
//    private final Map<Chunk, DiplomacyChunk> diplomacyChunks = new WeakHashMap<>();
//
//    public static DiplomacyChunks getInstance() {
//        if (instance == null) {
//            instance = new DiplomacyChunks();
//        }
//        return instance;
//    }
//
//
//    public DiplomacyChunk getDiplomacyChunk(Chunk chunk) {
//        var diplomacyChunk = diplomacyChunks.get(chunk);
//        if (diplomacyChunk == null) {
//            diplomacyChunk = new DiplomacyChunk(chunk);
//            diplomacyChunks.put(chunk, diplomacyChunk);
//        }
//        return diplomacyChunk;
//    }
//
//    public Map<?, ?> chunkToConfigMap(DiplomacyChunk diplomacyChunk) {
//        return ImmutableMap.of(
//                "world", diplomacyChunk.getChunk().getWorld().getName(),
//                "x", diplomacyChunk.getChunk().getX(),
//                "z", diplomacyChunk.getChunk().getZ()
//        );
//    }
//
//    public DiplomacyChunk configMapToChunk(Map<?, ?> map) {
//        var world = Bukkit.getWorld(String.valueOf(map.get("world")));
//        int x = (Integer) map.get("x");
//        int z = (Integer) map.get("z");
//
//        var chunk = world.getChunkAt(x, z);
//
//
//        return new DiplomacyChunk(chunk);
//    }
//
//
//}
