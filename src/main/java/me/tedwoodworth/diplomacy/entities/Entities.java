package me.tedwoodworth.diplomacy.entities;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Entities {
    private static Entities instance = null;
    public final NamespacedKey entityLootKey = new NamespacedKey(Diplomacy.getInstance(), "lootKey");
    private int healTaskID = -1;
    private Set<Entity> splitSlimes = new HashSet<>();

    public static Entities getInstance() {
        if (instance == null) {
            instance = new Entities();
        }
        return instance;
    }


    public Entities() {
        if (healTaskID == -1)
            healTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Diplomacy.getInstance(),
                    this::worldHeal, 0L, 1200L);
    }

    private void worldHeal() {
        for (var world : Bukkit.getWorlds()) {
            for (var entity : world.getEntities()) {
                if (entity instanceof Player) {
                    var level = ((Player) entity).getFoodLevel();
                    if (level > 0 && Math.random() < 0.25) ((Player) entity).setFoodLevel(level - 1);
                } else if (entity instanceof LivingEntity) {
                    var living = ((LivingEntity) entity);
                    var health = living.getHealth();
                    var attribute = ((LivingEntity) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH);
                    if (Math.random() < 0.5) {
                        if (attribute != null) {
                            var max = attribute.getValue();
                            if (health < max) living.setHealth(Math.min(max, health + max / 40.0));
                        }
                    }
                    if (Math.random() < 0.5) {
                        setLootDamage(living, Math.min(1, getLootDamage(living) + health / 40.0));
                    }
                }
            }
        }
    }

    public void setLootDamage(LivingEntity entity, double damage) {
        if (entity instanceof Player) throw new IllegalArgumentException("Error: Players cannot have loot damage");
        var container = entity.getPersistentDataContainer();
        container.set(entityLootKey, PersistentDataType.DOUBLE, damage);
    }

    public double getLootDamage(LivingEntity entity) {
        if (entity instanceof Player) throw new IllegalArgumentException("Error: Players cannot have loot damage");
        var container = entity.getPersistentDataContainer();
        if (!container.has(entityLootKey, PersistentDataType.DOUBLE)) {
            var damage = getPercentHealth(entity);
            container.set(entityLootKey, PersistentDataType.DOUBLE, damage);
            return damage;
        } else {
            return container.get(entityLootKey, PersistentDataType.DOUBLE);
        }

    }

    public double getPercentHealth(LivingEntity entity) {
        return (entity.getHealth() / Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

    public double getPercentOfMaxHealth(LivingEntity entity, double amount) {
        return (amount / Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Entities.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onSlimeSplit(SlimeSplitEvent event) {
            var entity = event.getEntity();
            if (splitSlimes.contains(entity)) event.setCancelled(true);
            else {
                splitSlimes.add(entity);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> splitSlimes.remove(entity), 20L);
            }
        }
    }
}
