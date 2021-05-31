package me.tedwoodworth.diplomacy.entities;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;

import java.util.HashSet;

public class EntityManager {
    private int randomTickTaskID = -1;
    private static EntityManager instance = null;

    public static EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }


    EntityManager() {
        if (randomTickTaskID == -1) {
            randomTickTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(), this::onTick, 0L, 20L);
        }
    }

    private void onTick() {
        var worlds = Bukkit.getWorlds();
        for (var world : worlds) {
            var entities = world.getLivingEntities();
            for (var entity : entities) {
                if (entity instanceof Player || entity instanceof EnderCrystal || entity instanceof EnderDragon || entity instanceof Wither) {
                    continue;
                }
                if (Math.random() < 0.02) {
                    var nearby = entity.getNearbyEntities(1, 1, 1);
                    var trueNearby = 0;
                    for (var near : new HashSet<>(nearby)) {
                        if (near instanceof LivingEntity) {
                            trueNearby++;
                        }
                    }
                    if (trueNearby > 8) {
                        entity.damage(99999);
                    }
                }
            }
        }
    }


}
