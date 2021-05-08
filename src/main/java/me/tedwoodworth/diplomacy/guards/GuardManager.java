package me.tedwoodworth.diplomacy.guards;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Guis.GuardGuis;
import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.Items.Items;
import me.tedwoodworth.diplomacy.data.BooleanPersistentDataType;
import me.tedwoodworth.diplomacy.events.NationRemoveChunksEvent;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayer;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import java.util.*;

public class GuardManager {
    public static GuardManager instance = null;
    private final Set<Entity> guards = new HashSet<>();
    private final Set<Player> interactSet = new HashSet<>();
    private HashMap<Player, String> guardDamage = new HashMap<>();
    private HashMap<DiplomacyPlayer, HashSet<Nation>> autoOutlaw = new HashMap<>();

    // Math stuff
    private final double GRAVITY_CONSTANT = 0.05;
    private final double DRAG = 0.01;

    // all guards
    private final NamespacedKey LEVEL_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_level");
    private final NamespacedKey TYPE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_type");
    private final NamespacedKey HEALTH_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_health");
    private final NamespacedKey NOTIFY_DAMAGE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_notify_damage");

    // attacking guards
    private final NamespacedKey ATTACK_TRESPASSERS = new NamespacedKey(Diplomacy.getInstance(), "guard_tresspassers");
    private final NamespacedKey ATTACK_NEUTRAL = new NamespacedKey(Diplomacy.getInstance(), "guard_neutral");
    private final NamespacedKey ATTACK_ALLIES = new NamespacedKey(Diplomacy.getInstance(), "guard_allies");
    private final NamespacedKey ATTACK_NEWBIES = new NamespacedKey(Diplomacy.getInstance(), "guard_newbies");
    private static final NamespacedKey GUARD_PROJECTILE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_projectile");

    // sniper
    private final short[] sniperCost = new short[100];
    private final float[] sniperMaxHealth = new float[100];
    private final float[] sniperPrecision = new float[100];
    private final float[] sniperVelocity = new float[100];
    private final float[] sniperResistance = new float[100];
    private final float[] sniperRadius = new float[100];
    private final float[] sniperPower = new float[100];

    // healer
    private final NamespacedKey HEALER_RATE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_healer_rate");
    private final int GUARD_TICK_DELAY = 2;
    private final int GUARD_HEAL_DELAY = 10;
    private final int SNIPER_DELAY = 80;


    public static GuardManager getInstance() {
        if (instance == null) {
            instance = new GuardManager();
        }
        return instance;
    }

    private GuardManager() {
        for (var nation : Nations.getInstance().getNations()) {
            var dChunks = nation.getChunks();
            for (var dChunk : dChunks) {
                var chunk = dChunk.getChunk();
                chunk.setForceLoaded(true);
                chunk.load();
                var entities = chunk.getEntities();
                for (var entity : entities) {
                    if (isGuard(entity)) {
                        System.out.println("Guard entity type: " + entity.getType().getKey());
                        guards.add(entity);
                    }
                }
                chunk.setForceLoaded(false);
            }
        }

        for (int i = 0; i < 100; i++) {
            sniperCost[i] = (short) (Math.pow(1.05, i));
            sniperMaxHealth[i] = (float) ((int) (200.0 * Math.pow(1.01, i) - 180.0));
            sniperPrecision[i] = (float) (15.0 * Math.pow(0.951, i));
            sniperResistance[i] = (float) (1.0 - Math.pow(0.99, i));
            sniperVelocity[i] = (float) (2.0 * Math.pow(1.02123, i));
            sniperRadius[i] = (float) (16.0 * Math.pow(1.0141, i));
            sniperPower[i] = (float) (9.83188423 * Math.pow(1.02, i) - 3.83188423);
        }
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onGuardTick(0), 0L);
    }

    private double adjustHeight(Location guardLoc, Location targetLoc, float speed) {
        var tempLoc = new Location(guardLoc.getWorld(), targetLoc.getX(), guardLoc.getY(), targetLoc.getZ());
        var ds = guardLoc.distanceSquared(tempLoc);
        var d = Math.sqrt(ds);
        var tempSD = guardLoc.distanceSquared(targetLoc);
        var tempD = Math.sqrt(tempSD);
        var avgSpeed = speed - tempD / 200.0; // adjust for drag
        var speedSquared = Math.pow(avgSpeed, 2);
        var y = targetLoc.getY() - guardLoc.getY();
        var a = Math.max(0, Math.pow(speedSquared, 2) - GRAVITY_CONSTANT * (GRAVITY_CONSTANT * ds + 2 * y * speedSquared));
        a = Math.sqrt(a);
        a = Math.min(speedSquared + a, speedSquared - a);
        a = a / (GRAVITY_CONSTANT * d);
        var pitch = Math.atan(a);
        y = Math.tan(pitch) * d;
        var ydiff = guardLoc.getY() - targetLoc.getY();
        var coef = 0.0;
        if (speed > 3.5) {
            ydiff *= 1.01;
        } else if (speed > 2.5) { // speed = 3.0
            ydiff *= 1.01;
            coef = 0.00015;
        } else { // speed = 2.0
            coef = 0.0012;
        }
        return y + targetLoc.getY() - coef * tempSD + ydiff; // Further adjust for distance todo adjust for y height
    }

    private void onGuardTick(int i) {
        var healMap = new HashMap<Entity, Integer>();
        for (var guard : guards) {
            if (i % (GUARD_HEAL_DELAY / GUARD_TICK_DELAY) == 0) {
                var health = getHealth(guard);
                var max = getMaxHealth(guard);
                if (health < max) {
                    setHealth(guard, Math.min(health + GUARD_HEAL_DELAY / 1000.0, max));
                }
            }
            var type = getType(guard);
            switch (type) {
                case FLAMETHROWER -> {
                    // launch fire items, if they collide (like grenades) deal tiny fire damage and light player on fire

                }
                case SNIPER -> {
                    if (i % (SNIPER_DELAY / GUARD_TICK_DELAY) == 0) {
                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                            var radius = getRadius(guard);
                            var speed = getSniperSpeed(guard);
                            var squared = Math.pow(speed, 2);
                            var nearby = guard.getNearbyEntities(radius, radius, radius);
                            var gLoc = guard.getLocation();
                            var gNation = getNation(guard);
                            Player closest = null;
                            double distance = Double.MAX_VALUE;
                            Location loc = null;
                            for (var entity : nearby) {
                                if (entity instanceof Player && canAttackPlayer((Player) entity, guard) && Objects.equals(gNation, getNation(entity))) {
                                    var pLoc = ((Player) entity).getEyeLocation();
                                    pLoc.setY(pLoc.getY() - 0.5);
                                    var d = gLoc.distanceSquared(pLoc);
                                    if (d < distance && squared > d / 10000.0) {
                                        distance = d;
                                        closest = (Player) entity;
                                        loc = pLoc;
                                    }
                                }
                            }

                            if (closest != null) {
                                var vec = loc.toVector().subtract(gLoc.toVector()).normalize();
                                var aLoc = new Location(gLoc.getWorld(), gLoc.getX() + vec.getX(), gLoc.getY() + vec.getY(), gLoc.getZ() + vec.getZ());
                                var y = adjustHeight(aLoc, loc, speed);
                                loc.setY(y);
                                System.out.println("Y height: " + loc.getY());
                                vec = loc.toVector().subtract(aLoc.toVector()).normalize();
                                var arrow = guard.getWorld().spawnArrow(aLoc, vec, speed, getSniperPrecision(guard));
                                arrow.setDamage(getPower(guard));
                                arrow.getPersistentDataContainer().set(GUARD_PROJECTILE_KEY, BooleanPersistentDataType.instance, true);
                                trackNewArrow(arrow, guard);
                                System.out.println("Guard ID#" + guard.getEntityId() + " fired an arrow."); //todo remove
                            }
                        }, (long) (Math.random() * (SNIPER_DELAY * 0.4)));
                    }
                }
                case HEALER -> {
                    if (i % (GUARD_HEAL_DELAY / GUARD_TICK_DELAY) == 0) { // only 1/10 ticks
                        var radius = getRadius(guard);
                        var nearby = guard.getNearbyEntities(radius, radius, radius);
                        var nation = getNation(guard);
                        var percent = 1.0;
                        Entity lowest = null;
                        var lowestHealth = 0.0;
                        var lowestMax = 0.0;
                        for (var entity : nearby) {
                            if (isGuard(entity) && Objects.equals(nation, getNation(entity)) && !(healMap.containsKey(entity) && healMap.get(entity) == 3)) {
                                var health = getHealth(entity);
                                var max = getMaxHealth(entity);
                                var temp = health / max;
                                if (temp < percent) {
                                    percent = temp;
                                    lowest = entity;
                                    lowestHealth = health;
                                    lowestMax = max;
                                }
                            }
                        }
                        if (lowest != null) {
                            ((EnderCrystal) guard).setBeamTarget(lowest.getLocation());
                            setHealth(lowest, Math.min(lowestMax, lowestHealth + (GUARD_HEAL_DELAY * getHealerRate(guard))));
                            if (healMap.containsKey(lowest)) {
                                healMap.replace(lowest, healMap.get(lowest) + 1);
                            } else {
                                healMap.put(lowest, 1);
                            }
                        } else {
                            ((EnderCrystal) guard).setBeamTarget(null);
                        }
                    }
                }
            }
        }
        if (i == 6000 / GUARD_TICK_DELAY) {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onGuardTick(0), GUARD_TICK_DELAY);
        } else {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onGuardTick(i + 1), GUARD_TICK_DELAY);
        }
    }

    public enum Type {
        BASIC, // does nothing
        SNIPER, // shoots strong arrows far at a slow rate
        GUNNER, // shoots weaker arrows at a fast rate, less accurate
        FLAMETHROWER, // shoots very weak fire arrows
        GRENADER, // shoots grenades
        HEALER, // heals nearby crystals
        TANK, // higher max health, higher armor
        TELEPORTER, // allows players to teleport to other teleporters
        GENERATOR, // slowly generates resources
        SNOWMAKER // pummels snowballs at target
    }

    public String getTypePrefix(Entity entity) {
        var type = getType(entity);
        var level = getLevel(entity);
        return switch (type) {
            case BASIC -> ChatColor.DARK_AQUA + "[Level " + level + " Guard] " + ChatColor.WHITE;
            case SNIPER -> ChatColor.DARK_GREEN + "[Level " + level + " Sniper] " + ChatColor.WHITE;
            case GUNNER -> ChatColor.DARK_GRAY + "[Level " + level + " Gunner] " + ChatColor.WHITE;
            case FLAMETHROWER -> ChatColor.RED + "[Level " + level + " Flamethrower] " + ChatColor.WHITE;
            case GRENADER -> ChatColor.DARK_RED + "[Level " + level + " Grenader] " + ChatColor.WHITE;
            case HEALER -> ChatColor.LIGHT_PURPLE + "[Level " + level + " Healer] " + ChatColor.WHITE;
            case TANK -> ChatColor.DARK_PURPLE + "[Level " + level + " Tank] " + ChatColor.WHITE;
            case TELEPORTER -> ChatColor.YELLOW + "[Level " + level + " Teleporter] " + ChatColor.WHITE;
            case GENERATOR -> ChatColor.GOLD + "[Level " + level + " Generator] " + ChatColor.WHITE;
            case SNOWMAKER -> ChatColor.AQUA + "[Level " + level + " Snowmaker] " + ChatColor.WHITE;
        };
    }

    public Entity spawnGuard(Location loc) {
        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        var entity = loc.getWorld().spawnEntity(loc, EntityType.ENDER_CRYSTAL);
        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
        var container = entity.getPersistentDataContainer();

        // type
        container.set(TYPE_KEY, PersistentDataType.BYTE, (byte)0);

        // health
        container.set(HEALTH_KEY, PersistentDataType.FLOAT, 20.00f);

        // level
        container.set(LEVEL_KEY, PersistentDataType.SHORT, (short)0);

        // preferences
        container.set(NOTIFY_DAMAGE_KEY, BooleanPersistentDataType.instance, true);
        container.set(ATTACK_TRESPASSERS, BooleanPersistentDataType.instance, false);
        container.set(ATTACK_NEUTRAL, BooleanPersistentDataType.instance, true);
        container.set(ATTACK_ALLIES, BooleanPersistentDataType.instance, false);
        container.set(ATTACK_NEWBIES, BooleanPersistentDataType.instance, false);

        entity.setCustomNameVisible(true);
        entity.setCustomName(ChatColor.DARK_AQUA + "[Level 0 Guard] " + ChatColor.WHITE + "20.00 HP");
        ((EnderCrystal) entity).setShowingBottom(false);
        guards.add(entity);
        return entity;
    }

    public void killGuard(Entity entity) {
        var loc = entity.getLocation();
        var container = entity.getPersistentDataContainer();
        var keys = container.getKeys();
        for (var key : keys) {
            container.remove(key);
        }
        System.out.println("Guard ID#" + entity.getEntityId() + " killed."); //todo remove
        guards.remove(entity);
        entity.remove();
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 4);
    }

    public boolean isGuard(Entity entity) {
        var container = entity.getPersistentDataContainer();
        return container.has(TYPE_KEY, PersistentDataType.BYTE);
    }

    public short getLevel(Entity entity) {
        return entity.getPersistentDataContainer().get(LEVEL_KEY, PersistentDataType.SHORT);
    }

    public short getCost(Entity entity) {
        var level = getLevel(entity);
        if (level == 100) return -1;
        var type = getType(entity);
        return switch (type) {
            default -> (short) 1;
            case SNIPER -> sniperCost[level];
        };
    }

    public void setLevel(Entity entity, int level) {
        entity.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.SHORT, (short) level);
        setHealth(entity, getHealth(entity));
    }

    public Type getType(Entity entity) {
        var id = entity.getPersistentDataContainer().get(TYPE_KEY, PersistentDataType.BYTE);
        return getEnum(id);
    }

    public void setNotifyDamage(Entity entity, boolean notifyDamage) {
        entity.getPersistentDataContainer().set(NOTIFY_DAMAGE_KEY, BooleanPersistentDataType.instance, notifyDamage);
    }

    public boolean getNotifyDamage(Entity entity) {
        return entity.getPersistentDataContainer().get(NOTIFY_DAMAGE_KEY, BooleanPersistentDataType.instance);
    }

    public void setAttackTrespassers(Entity entity, boolean attackTrespasser) {
        entity.getPersistentDataContainer().set(ATTACK_TRESPASSERS, BooleanPersistentDataType.instance, attackTrespasser);
    }

    public boolean getAttackTrespassers(Entity entity) {
        return entity.getPersistentDataContainer().get(ATTACK_TRESPASSERS, BooleanPersistentDataType.instance);
    }

    public void setAttackNeutral(Entity entity, boolean attackNeutral) {
        entity.getPersistentDataContainer().set(ATTACK_NEUTRAL, BooleanPersistentDataType.instance, attackNeutral);
    }

    public boolean getAttackNeutral(Entity entity) {
        return entity.getPersistentDataContainer().get(ATTACK_NEUTRAL, BooleanPersistentDataType.instance);
    }

    public void setAttackAllies(Entity entity, boolean attackAllies) {
        entity.getPersistentDataContainer().set(ATTACK_ALLIES, BooleanPersistentDataType.instance, attackAllies);
    }

    public boolean getAttackAllies(Entity entity) {
        return entity.getPersistentDataContainer().get(ATTACK_ALLIES, BooleanPersistentDataType.instance);
    }

    public void setAttackNewbies(Entity entity, boolean attackNewbies) {
        entity.getPersistentDataContainer().set(ATTACK_NEWBIES, BooleanPersistentDataType.instance, attackNewbies);
    }

    public boolean getAttackNewbies(Entity entity) {
        return entity.getPersistentDataContainer().get(ATTACK_NEWBIES, BooleanPersistentDataType.instance);
    }

    public boolean upgradeDefault(Entity entity, Type type) {
        var curType = getType(entity);
        if (curType != Type.BASIC || type == Type.BASIC) return false;

        var id = type.ordinal();
        entity.getPersistentDataContainer().set(TYPE_KEY, PersistentDataType.BYTE, (byte)id);
        entity.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.SHORT, (short)1);
        switch (type) {
            case HEALER -> {
                setHealerRate(entity, (short) 0);
            }
        }
        setHealth(entity, getHealth(entity));
        return true;
    }

    public double getHealth(Entity entity) {
        return entity.getPersistentDataContainer().get(HEALTH_KEY, PersistentDataType.FLOAT);
    }

    public void setHealth(Entity entity, double health) {
        entity.getPersistentDataContainer().set(HEALTH_KEY, PersistentDataType.FLOAT, (float)health);
        var strHealth = String.format("%.2f", health);
        entity.setCustomName(getTypePrefix(entity) + strHealth + " HP");
    }


    public Nation getNation(Entity entity) {
        return DiplomacyChunks.getInstance().getDiplomacyChunk(entity.getLocation().getChunk()).getNation();
    }

    public short getShortHealerRate(Entity entity) {
        return entity.getPersistentDataContainer().get(HEALER_RATE_KEY, PersistentDataType.SHORT);
    }

    public double getHealerRate(Entity entity) {
        var id = entity.getPersistentDataContainer().get(HEALER_RATE_KEY, PersistentDataType.SHORT);
        return switch (id) {
            default -> 4.0 / 1200.0;
            case 1 -> 6.0 / 1200.0; // 4
            case 2 -> 9.0 / 1200.0; // 5
            case 3 -> 13.5 / 1200.0; // 7
            case 4 -> 20.25 / 1200.0; // 10
            case 5 -> 30.375 / 1200.0; // 13
            case 6 -> 45.5624 / 1200.0; // 18
            case 7 -> 68.3436 / 1200.0; // 24
            case 8 -> 101.0756 / 1200.0; // 32
            case 9 -> 230.66 / 1200.0; // 59
            case 10 -> 518.98400 / 1200.0; // 109
        };
    }


    public void setHealerRate(Entity entity, short rate) {
        entity.getPersistentDataContainer().set(HEALER_RATE_KEY, PersistentDataType.SHORT, rate);

    }

    public float getResistance(Entity entity) {
        var type = getType(entity);
        var level = getLevel(entity);
        return switch (type) {
            default -> 0.0f;
            case SNIPER -> sniperResistance[level - 1];
        };
    }

    public float getMaxHealth(Entity entity) {
        var type = getType(entity);
        var level = getLevel(entity);
        return switch (type) {
            default -> 20.0f;
            case SNIPER -> sniperMaxHealth[level - 1];
        };
    }

    public float getRadius(Entity entity) {
        var type = getType(entity);
        var level = getLevel(entity);
        return switch (type) {
            default -> 0.0f;
            case SNIPER -> sniperRadius[level - 1];
        };
    }

    public float getSniperPrecision(Entity entity) {
        var level = getLevel(entity);
        return sniperPrecision[level - 1];
    }

    public float getSniperSpeed(Entity entity) {
        var level = getLevel(entity);
        return sniperVelocity[level - 1];
    }

    public float getPower(Entity entity) {
        var type = getType(entity);
        var level = getLevel(entity);
        return switch (type) {
            default -> 0.0f;
            case SNIPER -> sniperPower[level - 1];
        };
    }

    private Type getEnum(int id) {
        var array = Type.values();
        if (array.length > id) {
            return array[id];
        } else {
            throw new IndexOutOfBoundsException("Error: Guard type ID " + id + " is out of bounds");
        }
    }


    public void sendGuardNotification(Entity guard, String message) {
        var nation = getNation(guard);
        for (var player : Bukkit.getOnlinePlayers()) {
            var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            var pNation = Nations.getInstance().get(dp);
            if (Objects.equals(pNation, nation) && pNation != null) {
                // check nation permissions
                var permissions = pNation.getMemberClass(dp).getPermissions();
                var canSee = permissions.get("CanSeeGuardNotifications");
                if (canSee) {
                    player.sendMessage(message);
                }
            }
        }
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new GuardManager.EventListener(), Diplomacy.getInstance());
    }

    private HashMap<Arrow, Entity> trackedArrows = new HashMap<>();

    public void trackNewArrow(Arrow arrow, Entity guard) {
        trackedArrows.put(arrow, guard);
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> trackArrow(arrow), 1L);
    }

    private void trackArrow(Arrow arrow) {
        if (trackedArrows.containsKey(arrow)) {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> trackArrow(arrow), 1L);
        }
    }

    public boolean canGetAutoOutlawed(Player player, Entity guard) {
        // if ((from other nation || if does not have canManageGuards) && !isOutlawed)
        var nation = getNation(guard);
        var uuid = player.getUniqueId();
        var isOutlawed = nation.getOutlaws().contains(uuid);
        if (isOutlawed) return false;
        var dp = DiplomacyPlayers.getInstance().get(uuid);
        var pNation = Nations.getInstance().get(dp);
        if (!Objects.equals(nation, pNation)) return true;

        // check nation permissions
        var permissions = pNation.getMemberClass(dp).getPermissions();
        var canManageGuards = permissions.get("CanManageGuards");
        return !canManageGuards;
    }

    public boolean canAttackEntity(Entity entity, Entity guard) {
        if (!(entity instanceof Player)) return false;
        return canAttackPlayer((Player) entity, guard);
    }

    public boolean canAttackPlayer(Player damager, Entity guard) {

        // 1. can always attack outlaws, enemies, and nomads
        // 2. if tresspassing and can attack tresspassers, will attack, regardless of other settings
        // 3. if not tresspassing or can't attack tresspassers:
        // if ally: check attack allies
        // if neutral: check attack neutral
        // if newbie: check attack newbie

        var dp = DiplomacyPlayers.getInstance().get(((Player) damager).getUniqueId());
        var nation = Nations.getInstance().get(dp);
        var guardNation = getNation(guard);
        if (nation == null || (guardNation.getEnemyNationIDs().contains(nation.getNationID())) || (guardNation.getOutlaws().contains(damager.getUniqueId()))) {
            return true;
        }

        var chunk = damager.getLocation().getChunk();
        var canBuild = DiplomacyPlayers.getInstance().canBuildHere(chunk, damager);
        var attackTresspassers = getAttackTrespassers(guard);
        var isNationChunk = Objects.equals(guardNation, DiplomacyChunks.getInstance().getDiplomacyChunk(chunk).getNation());
        if (!canBuild && attackTresspassers && isNationChunk) {
            return true;
        }

        var allied = guardNation.getAllyNationIDs().contains(nation.getNationID());
        var attackAllies = getAttackAllies(guard);
        if (allied && attackAllies) {
            return true;
        }

        var attackNeutral = getAttackNeutral(guard);
        var same = guardNation.equals(nation);
        if (!allied && !same && attackNeutral) {
            return true;
        }

        if (same) {
            var nClass = guardNation.getMemberClass(dp);
            var attackNewbies = getAttackNewbies(guard);
            if (nClass.getClassID().equalsIgnoreCase("0") && attackNewbies) {
                return true;
            }
        }

        return false;
    }

    public Entity getTrueDamager(Entity damager) {
        if (damager instanceof Projectile) {
            return (Entity) ((Projectile) damager).getShooter();
        } else if (damager instanceof Item) {
            return Items.getInstance().grenadeThrowerMap.get(damager);
        } else {
            return damager;
        }
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
        private void onDeath(PlayerDeathEvent event) {
            var player = event.getEntity();
            if (guardDamage.containsKey(player)) {
                event.setDeathMessage(guardDamage.get(player));
                guardDamage.remove(player);
            }
        }

        @EventHandler
        private void onInteract(PlayerInteractEvent event) {
            var type = event.getAction();
            var player = event.getPlayer();
            if (interactSet.contains(player)) {
                event.setCancelled(true);
                interactSet.remove(player);
                return;
            }
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
                var notNearby = true;
                var nearby = loc.getWorld().getNearbyEntities(loc, 3, 3, 3);
                for (var near : nearby) {
                    if (isGuard(near)) {
                        notNearby = false;
                        break;
                    }
                }
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
                        player.sendMessage(ChatColor.RED + "This location is too close to another guard crystal.");
                    }
                }
            }
        }


        @EventHandler
        private void arrowHit(ProjectileHitEvent event) {
            var entity = event.getEntity();
            var hit = event.getHitEntity();
            if (hit != null && trackedArrows.containsKey(entity) && hit instanceof Player) {
                var guard = trackedArrows.get(entity);
                if (!canAttackPlayer((Player) hit, guard)) {
                    entity.setBounce(true);
                }
            }
            trackedArrows.remove(entity);
        }


        @EventHandler
        private void onInteractEntity(PlayerInteractEntityEvent event) {
            var entity = event.getRightClicked();
            var player = event.getPlayer();
            if (isGuard(entity)) {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> interactSet.remove(player), 5L);
                var type = getType(entity);
                switch (type) {
                    case TELEPORTER -> {

                    }
                    default -> {
                        // check if in nation
                        var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                        var playerNation = Nations.getInstance().get(dp);
                        if (playerNation == null) { // not in nation
                            return;
                        }

                        // check if in correct nation
                        var guardNation = DiplomacyChunks.getInstance().getDiplomacyChunk(entity.getLocation().getChunk()).getNation();
                        if (!playerNation.equals(guardNation)) {
                            return;
                        }
                        // check if outlaw
                        if (guardNation.getOutlaws().contains(player.getUniqueId())) {
                            return;
                        }

                        // check nation permissions
                        var permissions = playerNation.getMemberClass(dp).getPermissions();
                        var canManageGuards = permissions.get("CanManageGuards");
                        if (!canManageGuards) {
                            player.sendMessage(ChatColor.RED + "You do not have permission to manage guard crystals.");
                            return;
                        }
                        interactSet.add(player);
                        var menu = GuardGuis.getInstance().generateGui(entity, player);
                        menu.show(player);
                    }
                }


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

                var trueDamager = getTrueDamager(damager);
                if (damager instanceof Projectile) {
                    if (!isGuardArrow(damager)) {
                        if (damager instanceof Trident) {
                        } else {
                            damager.remove();
                            System.out.println("Arrow removed.");
                        }
                    }
                }

                if (trueDamager instanceof Player) {
                    var player = ((Player) trueDamager);
                    damage = damage * (1 - getResistance(entity));
                    var health = getHealth(entity);
                    health = health - damage;
                    var loc = entity.getLocation();
                    if (damager instanceof Projectile) {
                        player.playSound(trueDamager.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                    }
                    loc.getWorld().playSound(loc, Sound.BLOCK_NETHERITE_BLOCK_HIT, 2, 1);
                    if (health <= 0.0) {
                        var message = getKillMessage(player, entity);
                        sendGuardNotification(entity, message);
                        killGuard(entity);
                    } else {
                        setHealth(entity, health);
                        if (getNotifyDamage(entity)) {
                            var message = getDamageMessage(player, entity);
                            sendGuardNotification(entity, message);
                        }
                    }
                    if (canGetAutoOutlawed(player, entity)) {
                        var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                        autoOutlaw.putIfAbsent(dp, new HashSet<>());
                        var nation = getNation(entity);
                        if (autoOutlaw.get(dp).contains(nation)) {
                            nation.addOutlaw(dp.getOfflinePlayer());
                            autoOutlaw.get(dp).remove(nation);
                            for (var member : nation.getMembers()) {
                                var op = member.getOfflinePlayer();
                                if (op.getPlayer() != null) {
                                    var p = op.getPlayer();
                                    p.sendMessage(ChatColor.AQUA + player.getName() + " has been automatically outlawed for attacking guard crystals.");
                                }
                            }
                            player.sendMessage(ChatColor.AQUA + "You have been automatically outlawed by " + nation.getName() + " for attacking their guard crystals.");
                        } else {
                            autoOutlaw.get(dp).add(nation);
                            player.sendMessage(ChatColor.RED + "Warning: If you attack " + nation.getName() + "'s guard crystals again (in the next 30 seconds), they will automatically outlaw you.");
                            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                                var contained = autoOutlaw.get(dp).remove(nation);
                                if (contained && dp.getOfflinePlayer().getPlayer() != null) {
                                    dp.getOfflinePlayer().getPlayer().sendMessage(ChatColor.RED + "Warning for attacking " + nation.getName() + "'s guard crystals has worn off.");
                                }
                            }, 600L);
                        }
                    }
                } else {
                    return;
                }
            } else {
                if (damager instanceof Arrow && isGuardArrow(damager) && entity instanceof Player) {
                    event.setDamage(((Arrow) damager).getDamage());
                    guardDamage.remove(entity);
                    guardDamage.put((Player) entity, entity.getName() + " was shot by a Guard Crystal");

                } else {
                    guardDamage.remove(entity);
                }
            }
        }

        public boolean isGuardArrow(Entity entity) {
            return entity.getPersistentDataContainer().has(GUARD_PROJECTILE_KEY, BooleanPersistentDataType.instance);
        }

        public String getKillMessage(Entity damager, Entity guard) {
            var loc = guard.getLocation();
            var message = ChatColor.RED + "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "] " + ChatColor.DARK_GREEN + "Guard killed by " + ChatColor.RED;
            if (damager instanceof Projectile) {
                var shooter = ((Projectile) damager).getShooter();
                if (shooter instanceof Player) message += ((Player) shooter).getName();
            } else if (damager instanceof Item) {
                var shooter = Items.getInstance().grenadeThrowerMap.get(damager);
                if (shooter instanceof Player) message += shooter.getName();
            } else if (damager instanceof Player) {
                message += damager.getName();
            }
            return message;
        }

        public String getDamageMessage(Entity damager, Entity guard) {
            var loc = guard.getLocation();
            var message = ChatColor.RED + "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "] " + ChatColor.DARK_GREEN + "Guard damaged by " + ChatColor.RED;
            if (damager instanceof Projectile) {
                var shooter = ((Projectile) damager).getShooter();
                if (shooter instanceof Player) message += ((Player) shooter).getName();
            } else if (damager instanceof Item) {
                var shooter = Items.getInstance().grenadeThrowerMap.get(damager);
                if (shooter instanceof Player) message += shooter.getName();
            } else if (damager instanceof Player) {
                message += damager.getName();
            }
            var strHealth = String.format("%.2f", getHealth(guard));
            message += " " + ChatColor.DARK_GREEN + "(" + strHealth + " HP)";
            return message;
        }
    }
}
