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


/*
    prot ingredients:
        1: iron ingot 6%  // 2
        2: iron block 12% // 3
        3: 3 iron blocks 18% // 6
        4: diamond 24% // 10
        5: diamond block 31% // 17
        6: 3 diamond blocks 38% // 28
        7: netherite scrap 45% // 48
        8: netherite ingot 52% // 82
        9: netherite block 66% // 237
        10: 3 netherite blocks 80% // 685
 */


public class GuardManager {
    public static GuardManager instance = null;
    private final Set<Entity> guards = new HashSet<>();
    private final Set<Player> interactSet = new HashSet<>();
    private HashMap<Player, String> guardDamage = new HashMap<>();

    // Math stuff
    private final double GRAVITY_CONSTANT = 0.05;
    private final double DRAG = 0.01;

    // all guards
    private final NamespacedKey TYPE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_type");
    private final NamespacedKey HEALTH_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_health");
    private final NamespacedKey MAX_HEALTH_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_max_health");

    // sniper
    private final NamespacedKey SNIPER_ACCURACY_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_sniper_strength");
    private final NamespacedKey SNIPER_VELOCITY_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_sniper_velocity");
    private final NamespacedKey SNIPER_POWER_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_sniper_power");
    private final NamespacedKey GUARD_ARROW_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_arrow");

    // all but basic guards
    private final NamespacedKey RADIUS_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_radius");
    private final NamespacedKey RESISTANCE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_protection");

    // healer
    private final NamespacedKey HEALER_RATE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_healer_rate");
    private final int GUARD_TICK_DELAY = 2;
    private final int GUARD_HEAL_DELAY = 10;
    private final int SNIPER_DELAY = 100;


    public static GuardManager getInstance() {
        if (instance == null) {
            instance = new GuardManager();
        }
        return instance;
    }

    private GuardManager() {
        for (var entity : Bukkit.getWorld("world").getEntities()) {
            if (isGuard(entity)) {
                guards.add(entity);
            }
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
        var a = Math.pow(speedSquared, 2) - GRAVITY_CONSTANT * (GRAVITY_CONSTANT * ds + 2 * y * speedSquared);
        a = Math.sqrt(a);
        a = Math.min(speedSquared + a, speedSquared - a);
        a = a / (GRAVITY_CONSTANT * d);
        var pitch = Math.atan(a);
        y = Math.tan(pitch) * d;
        return y + guardLoc.getY() - 0.00008 * tempSD; // Further adjust for distance
    }

    private void onGuardTick(int i) {
        var healMap = new HashMap<Entity, Integer>();
        for (var guard : guards) {
            if (i % (GUARD_HEAL_DELAY / GUARD_TICK_DELAY) == 0) {
                var health = getHealth(guard);
                var max = getMaxHealth(guard);
                if (health < max) {
                    setHealth(guard, Math.min(health + GUARD_HEAL_DELAY / 1200.0, max));
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
                                if (entity instanceof Player && canDamageGuard(entity, guard) && Objects.equals(gNation, getNation(entity))) {
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
                                var y = adjustHeight(gLoc, loc, speed);
                                loc.setY(y);
                                var vec = loc.toVector().subtract(gLoc.toVector()).normalize();
                                var arrow = guard.getWorld().spawnArrow(gLoc, vec, speed, getSniperPrecision(guard));
                                arrow.setDamage(getPower(guard, true));
                                arrow.getPersistentDataContainer().set(GUARD_ARROW_KEY, BooleanPersistentDataType.instance, true);
                                trackNewArrow(arrow, guard);
                            }
                        }, (long) (Math.random() * (SNIPER_DELAY * 0.6)));
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
        return switch (type) {
            case BASIC -> ChatColor.DARK_AQUA + "[Basic] " + ChatColor.WHITE;
            case SNIPER -> ChatColor.DARK_GREEN + "[Sniper] " + ChatColor.WHITE;
            case GUNNER -> ChatColor.DARK_GRAY + "[Gunner] " + ChatColor.WHITE;
            case FLAMETHROWER -> ChatColor.RED + "[FLamethrower] " + ChatColor.WHITE;
            case GRENADER -> ChatColor.DARK_RED + "[Grenader] " + ChatColor.WHITE;
            case HEALER -> ChatColor.LIGHT_PURPLE + "[Healer] " + ChatColor.WHITE;
            case TANK -> ChatColor.DARK_PURPLE + "[Tank] " + ChatColor.WHITE;
            case TELEPORTER -> ChatColor.YELLOW + "[Teleporter] " + ChatColor.WHITE;
            case GENERATOR -> ChatColor.GOLD + "[Generator] " + ChatColor.WHITE;
            case SNOWMAKER -> ChatColor.AQUA + "[Snowmaker] " + ChatColor.WHITE;
        };
    }

    public void spawnGuard(Location loc) {
        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        var entity = loc.getWorld().spawnEntity(loc, EntityType.ENDER_CRYSTAL);
        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
        var container = entity.getPersistentDataContainer();

        // type
        container.set(TYPE_KEY, PersistentDataType.INTEGER, 0);

        // health
        container.set(HEALTH_KEY, PersistentDataType.DOUBLE, 20.0);

        // max health
        container.set(MAX_HEALTH_KEY, PersistentDataType.SHORT, (short) 0);

        // resistance
        container.set(RESISTANCE_KEY, PersistentDataType.SHORT, (short) 0);

        entity.setCustomNameVisible(true);
        entity.setCustomName(ChatColor.DARK_AQUA + "[Basic] " + ChatColor.WHITE + "20.00 HP");
        ((EnderCrystal) entity).setShowingBottom(false);
        guards.add(entity);
    }

    public void killGuard(Entity entity) {
        var loc = entity.getLocation();
        guards.remove(entity);
        entity.remove();
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 4);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, loc, 4);
    }

    public boolean isGuard(Entity entity) {
        var container = entity.getPersistentDataContainer();
        return container.has(TYPE_KEY, PersistentDataType.INTEGER);
    }

    public Type getType(Entity entity) {
        var id = entity.getPersistentDataContainer().get(TYPE_KEY, PersistentDataType.INTEGER);
        return getEnum(id);
    }

    public boolean setType(Entity entity, Type type) {
        var curType = getType(entity);
        if (curType == type) return false;

        var id = type.ordinal();
        entity.getPersistentDataContainer().set(TYPE_KEY, PersistentDataType.INTEGER, id);
        switch (type) {
            case HEALER -> {
                setRadius(entity, (short) 0);
                setHealerRate(entity, (short) 0);
            }
            case SNIPER -> {
                setRadius(entity, (short) 0);
                setSniperPower(entity, (short) 0);
                setSniperPrecision(entity, (short) 0);
                setSniperVelocity(entity, (short) 0);
            }
        }
        setHealth(entity, getHealth(entity));
        return true;
    }

    public double getHealth(Entity entity) {
        return entity.getPersistentDataContainer().get(HEALTH_KEY, PersistentDataType.DOUBLE);
    }

    public void setHealth(Entity entity, double health) {
        entity.getPersistentDataContainer().set(HEALTH_KEY, PersistentDataType.DOUBLE, health);
        var strHealth = String.format("%.2f", health);
        entity.setCustomName(getTypePrefix(entity) + strHealth + " HP");
    }


    public Nation getNation(Entity entity) {
        return DiplomacyChunks.getInstance().getDiplomacyChunk(entity.getLocation().getChunk()).getNation();
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

    public short getShortResistance(Entity entity) {
        return entity.getPersistentDataContainer().get(RESISTANCE_KEY, PersistentDataType.SHORT);
    }

    public double getResistance(Entity entity) {
        var id = entity.getPersistentDataContainer().get(RESISTANCE_KEY, PersistentDataType.SHORT);
        return switch (id) {
            default -> 1.0;
            case 1 -> 0.94; // 4 / iron ingot
            case 2 -> 0.88; // 5 / 3x iron ingot
            case 3 -> 0.82; // 7 / iron block
            case 4 -> 0.76; // 9 / 3x iron block
            case 5 -> 0.69; // 11 / diamond
            case 6 -> 0.62; // 14 / 3x diamond
            case 7 -> 0.55; // 19 / diamond block
            case 8 -> 0.48; // 24 / 1x netherite scrap
            case 9 -> 0.34; // 41 / 1x netherite
            case 10 -> 0.2; // 70 / 3x netherite
        };
    }


    public void setResistance(Entity entity, short resistance) {
        entity.getPersistentDataContainer().set(RESISTANCE_KEY, PersistentDataType.SHORT, resistance);
    }

    public double getMaxHealth(Entity entity) {
        var id = entity.getPersistentDataContainer().get(MAX_HEALTH_KEY, PersistentDataType.SHORT);
        return switch (id) {
            default -> 20.0;
            case 1 -> 35.0; // 5
            case 2 -> 61.0; // 7
            case 3 -> 107.0; // 9
            case 4 -> 188.0; // 12
            case 5 -> 350.0; // 17
            case 6 -> 574.0; // 22
            case 7 -> 1005.0; // 30
            case 8 -> 1759.0; // 41
            case 9 -> 5388.0; // 74
            case 10 -> 16500.0; // 183
        };
    }

    public short getShortMaxHealth(Entity entity) {
        return entity.getPersistentDataContainer().get(MAX_HEALTH_KEY, PersistentDataType.SHORT);
    }

    public void setMaxHealth(Entity entity, short rate) {
        entity.getPersistentDataContainer().set(MAX_HEALTH_KEY, PersistentDataType.SHORT, rate);
    }

    public short getShortRadius(Entity entity) {
        return entity.getPersistentDataContainer().get(RADIUS_KEY, PersistentDataType.SHORT);
    }

    public double getRadius(Entity entity) {
        var id = entity.getPersistentDataContainer().get(RADIUS_KEY, PersistentDataType.SHORT);
        var type = getType(entity);
        switch (type) {
            case GUNNER, GRENADER -> {
                return switch (id) {
                    default -> 8.0;
                    case 1 -> 10.0;
                    case 2 -> 12.0;
                    case 3 -> 16.0;
                    case 4 -> 24.0;
                    case 5 -> 32.0;
                };
            }
            case HEALER -> {
                return switch (id) {
                    default -> 16.0;
                    case 1 -> 20.0;
                    case 2 -> 24.0;
                    case 3 -> 28.0;
                    case 4 -> 32.0;
                    case 5 -> 36.0;
                    case 6 -> 40.0;
                    case 7 -> 44.0;
                    case 8 -> 48.0;
                    case 9 -> 56.0;
                    case 10 -> 64.0;
                };
            }
            case SNIPER -> {
                return switch (id) {
                    default -> 8.0; // no scope
                    case 1 -> 16.0; // 2x scope / 6 dust // glass
                    case 2 -> 24.0; // 3x scope / 9 dust // iron ingot
                    case 3 -> 32.0; // 4x scope / 12 dust // glass
                    case 4 -> 40.0; // 5x scope / 15 dust // iron ingot
                    case 5 -> 48.0; // 6x scope / 18 dust // glass
                    case 6 -> 64.0; // 8x scope / 24 dust // diamond
                    case 7 -> 80.0; // 10x scope / 30 dust // quartz
                    case 8 -> 180.0; // 16x scope / 48 dust // diamond
                    case 9 -> 256.0; // 32x scope / 96 dust // quartz
                    case 10 -> 400.0; // 50x scope / 150 dust // diamond
                };
            }
            case FLAMETHROWER, SNOWMAKER -> {
                return switch (id) {
                    default -> 4;
                    case 1 -> 5;
                    case 2 -> 6;
                    case 3 -> 7;
                    case 4 -> 9;
                    case 5 -> 12;
                };
            }
            case TELEPORTER -> { // radius (2), regeneration speed, defense, max health
                return switch (id) { // world size: 9984 x 9984
                    default -> 250;
                    case 1 -> 500;
                    case 2 -> 750;
                    case 3 -> 1000;
                    case 4 -> 1250;
                    case 5 -> 1500;
                    case 6 -> 2000;
                    case 7 -> 2500;
                    case 8 -> 3000;
                    case 9 -> 4000;
                    case 10 -> 5000;
                };
            }
            default -> {
                return 0.0;
            }
        }
    }

    public void setRadius(Entity entity, short radius) {
        entity.getPersistentDataContainer().set(RADIUS_KEY, PersistentDataType.SHORT, radius);
    }


//    private final NamespacedKey SNIPER_STRENGTH_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_sniper_strength");
//    private final NamespacedKey SNIPER_VELOCITY_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_sniper_velocity");
//    private final NamespacedKey SNIPER_POWER_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_sniper_power");

    public float getSniperPrecision(Entity entity) {
        var shrt = entity.getPersistentDataContainer().get(SNIPER_ACCURACY_KEY, PersistentDataType.SHORT);
        return switch (shrt) {
            default -> 12.0F;
            case 1 -> 7.0F; // 4 / 3 sticks
            case 2 -> 4F; // 12 / redstone
            case 3 -> 2F; // 31 / target
            case 4 -> 0.5F; // 62 / iron ingot
            case 5 -> 0.1F; // 120 / 3 iron ingots
        };
    }

    public short getShortSniperPrecision(Entity entity) {
        return entity.getPersistentDataContainer().get(SNIPER_ACCURACY_KEY, PersistentDataType.SHORT);
    }

    public void setSniperPrecision(Entity entity, short precision) {
        entity.getPersistentDataContainer().set(SNIPER_ACCURACY_KEY, PersistentDataType.SHORT, precision);
    }

    public short getShortSniperVelocity(Entity entity) {
        return entity.getPersistentDataContainer().get(SNIPER_VELOCITY_KEY, PersistentDataType.SHORT);
    }

    public short getShortSniperPower(Entity entity) {
        return entity.getPersistentDataContainer().get(SNIPER_POWER_KEY, PersistentDataType.SHORT);
    }

    public float getSniperSpeed(Entity entity) {
        var s = getShortSniperVelocity(entity);
        return switch (s) {
            default -> 2F; // 40 bps
            case 1 -> 3F; // 50% (60 bps) / bamboo // 4 dust
            case 2 -> 4.5F; // 50% more (90 bps) / iron // 7 dust
            case 3 -> 6.75F; // 50% more (135 bps) / redstone // 12 dust
            case 4 -> 10F; // 50% more (200 bps) / piston // 38 dust
            case 5 -> 15F; // 50% more (300 bps) / gun powder x32 // 115 dust
        };
    }

    public void setSniperVelocity(Entity entity, short velocity) {
        entity.getPersistentDataContainer().set(SNIPER_VELOCITY_KEY, PersistentDataType.SHORT, velocity);
    }

    public void setSniperPower(Entity entity, short power) {
        entity.getPersistentDataContainer().set(SNIPER_POWER_KEY, PersistentDataType.SHORT, power);
    }

    public double getPower(Entity entity, boolean isSniper) {
        var s = getShortSniperPower(entity);
        if (isSniper) {
            return switch (s) {
                default -> 8.0; // 40 bps
                case 1 -> 10.0; // 20% / flint // 7 dust
                case 2 -> 12.0; // 20% more / flint // 9 dust
                case 3 -> 14.0; // 20% more / iron ingot // 11 dust
                case 4 -> 16.0; // 20% more / iron block // 15 dust
                case 5 -> 18.0; // 20% more / diamond // 19 dust
                case 6 -> 20.0; // 20% more / obsidian // 25 dust
                case 7 -> 22.0; // 20% more / diamond block // 33 dust
                case 8 -> 24.0; // 20% more / obsidian x9 // 42 dust
                case 9 -> 28.0; // 30% more / netherite scrap // 72 dust
                case 10 -> 32.0; // 30% more / netherite ingot x3   // 121 dust
            };
        } else {
            // todo gunner
            return 0.0;
        }
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
            if (Objects.equals(pNation, nation)) {
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
        var loc = arrow.getLocation();
        var chunk = loc.getChunk();
        chunk.load();

        if (trackedArrows.containsKey(arrow)) {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> trackArrow(arrow), 1L);
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
                if (nation != null && (nation.equals(guardNation) || nation.getAllyNationIDs().contains(guardNation.getNationID()))) {
                    if (nation.getOutlaws().contains(((Player) shooter).getUniqueId())) {
                        return true;
                    } else {
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
                if (nation != null && (nation.equals(guardNation) || nation.getAllyNationIDs().contains(guardNation.getNationID()))) {
                    if (nation.getOutlaws().contains(shooter.getUniqueId())) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else if (damager instanceof Player) {
            var dp = DiplomacyPlayers.getInstance().get(damager.getUniqueId());
            var nation = Nations.getInstance().get(dp);
            if (nation != null && (nation.equals(guardNation) || nation.getAllyNationIDs().contains(guardNation.getNationID()))) {
                if (nation.getOutlaws().contains(damager.getUniqueId())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
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
        private void arrowHit(ProjectileHitEvent event) {
            var entity = event.getEntity();
            var hit = event.getHitEntity();
            if (hit != null && trackedArrows.containsKey(entity) && hit instanceof Player) {
                var guard = trackedArrows.get(entity);
                if (!canDamageGuard(hit, guard)) {
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
                        var menu = GuardGuis.getInstance().generateGui(entity);
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
                if (!canDamageGuard(damager, entity)) {
                    return;
                }
                if (damager instanceof Projectile) {
                    if (damager instanceof Trident) {
                        ((Trident) damager).setBounce(true);
                    } else {
                        damager.remove();
                    }
                }
                damage = damage * getResistance(entity);
                var health = getHealth(entity);
                health = health - damage;
                var loc = entity.getLocation();
                loc.getWorld().playSound(loc, Sound.BLOCK_NETHERITE_BLOCK_HIT, 2, 1);
                if (health <= 0.0) {
                    var message = getKillMessage(damager, entity);
                    var nation = getNation(entity);
                    for (var player : Bukkit.getOnlinePlayers()) {
                        var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                        var pNation = Nations.getInstance().get(dp);
                        if (Objects.equals(pNation, nation)) {
                            // check nation permissions
                            var permissions = pNation.getMemberClass(dp).getPermissions();
                            var canSee = permissions.get("CanSeeGuardNotifications");
                            if (canSee) {
                                player.sendMessage(message);
                            }
                        }
                    }
                    killGuard(entity);
                } else {
                    setHealth(entity, health);
                    var message = getDamageMessage(damager, entity);
                    sendGuardNotification(entity, message);
                }
            }  else {
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
            return entity.getPersistentDataContainer().has(GUARD_ARROW_KEY, BooleanPersistentDataType.instance);
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
