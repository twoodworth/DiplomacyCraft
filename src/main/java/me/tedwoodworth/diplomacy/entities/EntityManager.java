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
                if (Math.random() < 0.25) {
                    var nearby = entity.getNearbyEntities(0.5, 0.5, 0.5);
                    var trueNearby = 0;
                    var superNearby = 0;
                    var loc = entity.getLocation();
                    for (var near : new HashSet<>(nearby)) {
                        if (near.equals(entity)) continue;
                        if (near instanceof LivingEntity) {
                            trueNearby++;
                            if (near.getLocation().distanceSquared(loc) < 0.0625) {
                                superNearby++;
                            }
                        }
                    }
                    if (trueNearby > 7 || superNearby > 0) {
                        entity.damage(1);
                    }
                }
            }
        }
    }


}
