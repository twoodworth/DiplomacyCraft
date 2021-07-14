package me.tedwoodworth.diplomacy.entities;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;

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
            var chunks = world.getLoadedChunks();
            for (var chunk : chunks) {
                var entities = chunk.getEntities();
                for (var entity : entities) {
                    if (!(entity instanceof LivingEntity)) continue;
                    if (entity instanceof Player || entity instanceof EnderCrystal || entity instanceof EnderDragon || entity instanceof Wither) {
                        continue;
                    }
                    var living = (LivingEntity) entity;
                    trampleTick(living, world);
                }
            }
        }
    }

    private void trampleTick(LivingEntity entity, World world) {
        var eBox = entity.getBoundingBox();
        var nearby = world.getNearbyEntities(eBox);
        nearby.remove(entity);
        var volume = eBox.getVolume();

        for (var near : nearby) {
            if (!(near instanceof LivingEntity)) continue;
            var nBox = near.getBoundingBox();
            try {
                var intersection = eBox.intersection(nBox);
                if (intersection.getVolume() / volume > 0.2) {
                    entity.damage(1);
                }
            } catch (IllegalArgumentException ignored) {

            }
        }
    }

}
