package me.tedwoodworth.diplomacy.guards;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.Items.Items;
import me.tedwoodworth.diplomacy.events.NationRemoveChunksEvent;
import me.tedwoodworth.diplomacy.lives_and_tax.LivesManager;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import java.util.Objects;

public class GuardManager {
    public static GuardManager instance = null;
    private NamespacedKey TYPE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_type");
    private NamespacedKey HEALTH_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_health");

    public static GuardManager getInstance() {
        if (instance == null) {
            instance = new GuardManager();
        }
        return instance;
    }

    public enum Type {
        DEFAULT, // does nothing
        SNIPER, // shoots strong arrows far at a slow rate
        MACHINE_GUNNER, // shoots weaker arrows at a fast rate, less accurate
        FLAMETHROWER, // shoots very weak fire arrows
        GRENADE_LAUNCHER, // shoots grenades
        HEALER, // heals nearby crystals
        TANK, // higher max health, higher armor
        TELEPORTER, // allows players to teleport to other teleporters
        GENERATOR // slowly generates resources
    }
    public void spawnGuard(Location loc) {
        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        var entity = Bukkit.getWorld("world").spawnEntity(loc, EntityType.ENDER_CRYSTAL);
        var container = entity.getPersistentDataContainer();
        container.set(TYPE_KEY, PersistentDataType.INTEGER, 0);
        container.set(HEALTH_KEY, PersistentDataType.DOUBLE, 20.0);
        entity.setCustomNameVisible(true);
        entity.setCustomName("20.00 Health");
        ((EnderCrystal) entity).setShowingBottom(false);
    }

    public void killGuard(Entity entity) {
        var loc = entity.getLocation();
        entity.remove();
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, loc, 4);
    }

    public boolean isGuard(Entity entity) {
        var container = entity.getPersistentDataContainer();
        return container.has(TYPE_KEY, PersistentDataType.INTEGER);
    }

    public double getHealth(Entity entity) {
        return entity.getPersistentDataContainer().get(HEALTH_KEY, PersistentDataType.DOUBLE);
    }

    public void setHealth(Entity entity, double health) {
        entity.getPersistentDataContainer().set(HEALTH_KEY, PersistentDataType.DOUBLE, health);
        var strHealth = String.format("%.2f",health);
        entity.setCustomName(strHealth + " Health");
    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new GuardManager.EventListener(), Diplomacy.getInstance());
    }


    private class EventListener implements Listener {

        @EventHandler
        private void onExplode(EntityExplodeEvent event) {
            var entity = event.getEntity();
            if (isGuard(entity)) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onInteract(PlayerInteractEvent event) {
            var type = event.getAction();
            var player = event.getPlayer();
            var hand = event.getHand();
            var equipment = player.getEquipment();
            var item = equipment.getItem(hand);
            if (type == Action.RIGHT_CLICK_BLOCK && CustomItemGenerator.getInstance().isCustomItem(item) &&
                    CustomItems.getInstance().getEnum(CustomItemGenerator.getInstance().getCustomID(item)) == CustomItems.CustomID.GUARD_CRYSTAL) {
                var block = event.getClickedBlock();
                var face = event.getBlockFace();
                var place = block.getRelative(face);
                var loc = place.getLocation();
                event.setCancelled(true);
                var box = new BoundingBox(loc.getX(), loc.getY(), loc.getZ(), loc.getX() + 1, loc.getY() + 1, loc.getZ() + 1);
                var notNearby = loc.getWorld().getNearbyEntities(box).size() == 0;
                if (place.isEmpty() && notNearby) { // if location is okay

                    // check if in nation
                    var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                    var playerNation = Nations.getInstance().get(dp);
                    if (playerNation == null) {
                        player.sendMessage(ChatColor.RED + "You must be part of a nation to place guard crystals.");
                        return;
                    }

                    // check nation permissions
                    var permissions = playerNation.getMemberClass(dp).getPermissions();
                    var canManageGuards = permissions.get("CanManageGuards");
                    if (!canManageGuards) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to place guard crystals.");
                        return;
                    }

                    // Check location
                    var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(loc.getChunk());
                    var nation = diplomacyChunk.getNation();
                    if (nation == null || !Objects.equals(nation, playerNation)) {
                        player.sendMessage(ChatColor.RED + "You can only place guard crystals inside your nation's territory.");
                        return;
                    }


                    spawnGuard(loc);
                    var amount = item.getAmount();
                    if (amount == 1) {
                        equipment.setItem(hand, new ItemStack(Material.AIR));
                    } else {
                        item.setAmount(amount - 1);
                        equipment.setItem(hand, item);
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "Not enough room here.");
                    if (!notNearby) {
                        player.sendMessage(ChatColor.RED + "This location is too close to another entity (make sure you stand far enough away when placing).");
                    }
                }
            }
        }

        @EventHandler
        private void onInteractEntity(PlayerInteractEntityEvent event) {
            var entity = event.getRightClicked();
            if (isGuard(entity)) {
                // if perms, show guard menu
            }
        }

        @EventHandler
        private void onRemoveChunks(NationRemoveChunksEvent event) {
            var chunks = event.getChunks();
            for (var dp : chunks) {
                var chunk = dp.getChunk();
                var entities = chunk.getEntities();
                for (var entity : entities) {
                    if (isGuard(entity)) {
                        killGuard(entity);
                    }
                }
            }
        }

        @EventHandler
        private void onEntityDamage(EntityDamageByEntityEvent event) {
            var entity = event.getEntity();
            var damage = event.getDamage();
            var damager = event.getDamager();
            if (isGuard(entity)) {
                event.setCancelled(true);
                if (!canDamageGuard(damager, entity)) {
                    return;
                }
                var health = getHealth(entity);
                System.out.println("Crystal damage: " + damage);
                health = health - damage;
                var loc = entity.getLocation();
                loc.getWorld().playSound(loc, Sound.BLOCK_NETHERITE_BLOCK_HIT, 2, 1);
                if (health <= 0.0) {
                    killGuard(entity);
                } else {
                    setHealth(entity, health);
                }
            }
        }

        public boolean canDamageGuard(Entity damager, Entity guard) {
            var loc = guard.getLocation();
            var guardNation = DiplomacyChunks.getInstance().getDiplomacyChunk(loc.getChunk()).getNation();
            if (guardNation == null) {
                guard.remove();
                return false;
            }
            if (damager instanceof Projectile) {
                var projectile = ((Projectile) damager);
                var shooter = projectile.getShooter();
                if (shooter instanceof Player) {
                    var dp = DiplomacyPlayers.getInstance().get(((Player) shooter).getUniqueId());
                    var nation = Nations.getInstance().get(dp);
                    if (nation != null && nation.equals(guardNation)) {
                        if (nation.getOutlaws().contains(((Player) shooter).getUniqueId())) {
                            return true;
                        } else {
                            ((Player) shooter).sendMessage(ChatColor.RED + "You cannot damage your own guard crystal.");
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            } else if (damager instanceof Item) {
                var shooter = Items.getInstance().grenadeThrowerMap.get(damager);
                if (shooter instanceof Player) {
                    var dp = DiplomacyPlayers.getInstance().get(shooter.getUniqueId());
                    var nation = Nations.getInstance().get(dp);
                    if (nation != null && nation.equals(guardNation)) {
                        if (nation.getOutlaws().contains(shooter.getUniqueId())) {
                            return true;
                        } else {
                            shooter.sendMessage(ChatColor.RED + "You cannot damage your own guard crystal.");
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }else if (damager instanceof Player) {
                var dp = DiplomacyPlayers.getInstance().get(damager.getUniqueId());
                var nation = Nations.getInstance().get(dp);
                if (nation != null && nation.equals(guardNation)) {
                    if (nation.getOutlaws().contains(damager.getUniqueId())) {
                        return true;
                    } else {
                        damager.sendMessage(ChatColor.RED + "You cannot damage your own guard crystal.");
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }
}
