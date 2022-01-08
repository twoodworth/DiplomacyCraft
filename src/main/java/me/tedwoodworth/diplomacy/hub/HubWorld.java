package me.tedwoodworth.diplomacy.hub;

import org.bukkit.*;

public class HubWorld {
    private World hub;
    private static HubWorld instance = null;


    public static HubWorld getInstance() {
        if (instance == null) {
            instance = new HubWorld();
        }
        return instance;
    }

    public World getHub() {
        return hub;
    }

    private HubWorld() {
        hub = Bukkit.getWorld("world");
        if (hub == null) {
            hub = Bukkit.createWorld(
                    new WorldCreator("world")
                            .type(WorldType.FLAT)
                            .generateStructures(false)
            );
            var border = hub.getWorldBorder();
            border.setCenter(0, 0);
            hub.setSpawnLocation(0, 4, 0);
            border.setSize(100);
            hub.setDifficulty(Difficulty.PEACEFUL);
            hub.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            hub.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            hub.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            hub.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            hub.setGameRule(GameRule.DO_WEATHER_CYCLE, false);

        }
    }


}
