package me.tedwoodworth.diplomacy.dynmap.blocks;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class NationChunks {
    private final Map<String, LinkedList<NationChunk>> chunks = new HashMap<>();

    public Map<String, LinkedList<NationChunk>> getChunks() {
        return chunks;
    }

    public void clear() {
        chunks.clear();
    }
}