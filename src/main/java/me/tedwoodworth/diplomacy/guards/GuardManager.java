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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;

import java.util.*;

public class GuardManager {
    public static GuardManager instance = null;
    private final Set<Entity> guards = new HashSet<>();
    private final Set<Player> interactSet = new HashSet<>();
    private final HashMap<Player, String> guardDamage = new HashMap<>();
    private final HashMap<DiplomacyPlayer, HashSet<Nation>> autoOutlaw = new HashMap<>();

    // all guards
    private final NamespacedKey LEVEL_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_level");
    private final NamespacedKey TYPE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_type");
    private final NamespacedKey HEALTH_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_health");
    private final NamespacedKey NOTIFY_DAMAGE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_notify_damage");
    private final NamespacedKey NAME_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_name");

    // attacking guards
    private final NamespacedKey ATTACK_TRESPASSERS = new NamespacedKey(Diplomacy.getInstance(), "guard_tresspassers");
    private final NamespacedKey ATTACK_NEUTRAL = new NamespacedKey(Diplomacy.getInstance(), "guard_neutral");
    private final NamespacedKey ATTACK_ALLIES = new NamespacedKey(Diplomacy.getInstance(), "guard_allies");
    private final NamespacedKey ATTACK_NEWBIES = new NamespacedKey(Diplomacy.getInstance(), "guard_newbies");
    private final NamespacedKey GUARD_PROJECTILE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_projectile");

    // sniper
    public final short[] sniperCost = new short[100];
    public final float[] sniperMaxHealth = new float[100];
    public final float[] sniperPrecision = new float[100];
    public final float[] sniperVelocity = new float[100];
    public final float[] sniperResistance = new float[100];
    public final float[] sniperRadius = new float[100];
    public final float[] sniperPower = new float[100];

    // gunner
    public final short[] gunnerCost = new short[100];
    public final float[] gunnerMaxHealth = new float[100];
    public final float[] gunnerPrecision = new float[100];
    public final float[] gunnerVelocity = new float[100];
    public final float[] gunnerResistance = new float[100];
    public final float[] gunnerRadius = new float[100];
    public final float[] gunnerPower = new float[100];
    public final float[] gunnerDelay = new float[100];

    // tank
    public final short[] tankCost = new short[100];
    public final float[] tankMaxHealth = new float[100];
    public final float[] tankResistance = new float[100];
    public final float[] tankVelocity = new float[100];
    public final float[] tankPower = new float[100];

    // flamethrower
    public final Map<Item, Entity> flames = new HashMap<>();
    public final short[] flamethrowerCost = new short[100];
    public final float[] flamethrowerMaxHealth = new float[100];
    public final float[] flamethrowerResistance = new float[100];
    public final float[] flamethrowerPower = new float[100];
    public final float[] flamethrowerDelay = new float[100];
    public final short[] flamethrowerBurnTime = new short[100];
    public final short[] flamesPerTick = new short[100];

    // healer
    public final short[] healerCost = new short[100];
    public final float[] healerMaxHealth = new float[100];
    public final float[] healerResistance = new float[100];
    public final float[] healerPower = new float[100];
    public final float[] healerRadius = new float[100];
    public final int HEALER_DELAY = 30;

    // snowballer
    public final short[] snowmakerCost = new short[100];
    public final float[] snowmakerMaxHealth = new float[100];
    public final float[] snowmakerResistance = new float[100];
    public final short[] snowmakerPower = new short[100];
    public final float[] snowmakerDelay = new float[100];
    public final short[] snowmakerSlowTime = new short[100];
    public final short[] snowmakerBallsPerTick = new short[100];

    // teleporter
    public final short[] teleporterCost = new short[100];
    public final float[] teleporterMaxHealth = new float[100];
    public final float[] teleporterResistance = new float[100];
    public final float[] teleporterRadius = new float[100];
    public final float[] teleporterLoadRate = new float[100];
    public final NamespacedKey ALLOWED_TELEPORTS_KEY = new NamespacedKey(Diplomacy.getInstance(), "allowed_teleports");
    public final NamespacedKey TELEPORTER_ID_KEY = new NamespacedKey(Diplomacy.getInstance(), "teleporter_id");
    public final HashMap<Integer, Entity> teleporterMap = new HashMap<>();
    public final HashMap<Player, Long> activeTeleports = new HashMap<>();

    // generator
    public final short[] generatorCost = new short[100];
    public final float[] generatorMaxHealth = new float[100];
    public final float[] generatorResistance = new float[100];
    public final double[] generatorRate = new double[100];
    public final NamespacedKey GENERATOR_GUNPOWDER_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_gunpowder");
    public final NamespacedKey GENERATOR_LAPIS_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_lapis");
    public final NamespacedKey GENERATOR_REDSTONE_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_redstone");
    public final NamespacedKey GENERATOR_QUARTZ_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_quartz");
    public final NamespacedKey GENERATOR_GLOWSTONE_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_glowstone");
    public final NamespacedKey GENERATOR_IRON_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_iron");
    public final NamespacedKey GENERATOR_GOLD_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_gold");
    public final NamespacedKey GENERATOR_EMERALD_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_emerald");
    public final NamespacedKey GENERATOR_MAGIC_DUST_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_magic_dust");
    public final NamespacedKey GENERATOR_DIAMOND_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_diamond");
    public final NamespacedKey GENERATOR_NETHERITE_KEY = new NamespacedKey(Diplomacy.getInstance(), "generator_netherite");

    // all
    private final int GUARD_TICK_DELAY = 2;


    public static GuardManager getInstance() {
        if (instance == null) {
            instance = new GuardManager();
        }
        return instance;
    }

    private int[] getAllowedTeleporterIDs(Entity teleporter) {
        return teleporter.getPersistentDataContainer().get(ALLOWED_TELEPORTS_KEY, PersistentDataType.INTEGER_ARRAY);
    }

    public HashSet<Entity> getAllowedTeleporters(Entity teleporter) {
        var ids = getAllowedTeleporterIDs(teleporter);
        var set = new HashSet<Entity>();
        for (var id : ids) {
            set.add(teleporterMap.get(id));
        }
        return set;
    }

    public HashSet<Entity> getAllTeleporters() {
        var ids = teleporterMap.keySet();
        var tps = new HashSet<Entity>();
        for (var id : ids) {
            tps.add(teleporterMap.get(id));
        }
        return tps;
    }

    public float getTeleporterLoadRate(Entity teleporter) {
        var level = getLevel(teleporter);
        return teleporterLoadRate[level - 1];
    }

    public void removeAllowedTeleporter(Entity teleporter, Entity remove) {
        var id = getTeleporterID(remove);
        var ids = getAllowedTeleporterIDs(teleporter);
        var contains = false;
        for (var i : ids) {
            if (i == id) {
                contains = true;
                break;
            }
        }
        if (!contains) return;
        var nIds = new int[ids.length - 1];
        int j = 0;
        for (int i : ids) {
            if (i == id) continue;
            nIds[j] = i;
            j++;
        }
        teleporter.getPersistentDataContainer().set(ALLOWED_TELEPORTS_KEY, PersistentDataType.INTEGER_ARRAY, nIds);
    }

    public void addAllowedTeleporter(Entity teleporter, Entity add) {
        var id = getTeleporterID(add);
        var ids = getAllowedTeleporterIDs(teleporter);
        var nIds = new int[ids.length + 1];
        System.arraycopy(ids, 0, nIds, 0, ids.length);
        nIds[ids.length] = id;
        teleporter.getPersistentDataContainer().set(ALLOWED_TELEPORTS_KEY, PersistentDataType.INTEGER_ARRAY, nIds);
    }

    public int getTeleporterID(Entity guard) {
        return guard.getPersistentDataContainer().get(TELEPORTER_ID_KEY, PersistentDataType.INTEGER);
    }

    private Entity getTeleporter(int id) {
        return teleporterMap.get(id);
    }


    public int getUniqueTeleporterID() {
        int id;
        var set = teleporterMap.keySet();
        do {
            if (Math.random() < 0.5) {
                id = (int) (Math.random() * Integer.MIN_VALUE);
            } else {
                id = (int) (Math.random() * Integer.MAX_VALUE);
            }
        } while (set.contains(id));
        return id;

    }

    private GuardManager() {
        var gunners = new HashSet<Entity>();
        var flamethrowers = new HashSet<Entity>();
        var snowmakers = new HashSet<Entity>();
        for (var nation : Nations.getInstance().getNations()) {
            var dChunks = nation.getChunks();
            for (var dChunk : dChunks) {
                var chunk = dChunk.getChunk();
                chunk.setForceLoaded(true);
                chunk.load();
                var entities = chunk.getEntities();
                var contains = false;
                for (var entity : entities) {
                    if (isGuard(entity)) {
                        contains = true;
                        guards.add(entity);
                        System.out.println("Guard Loaded: Name = " + getName(entity) + "; ID#" + entity.getEntityId());
                        var type = getType(entity);
                        switch (type) {
                            case GUNNER -> gunners.add(entity);
                            case FLAMETHROWER -> flamethrowers.add(entity);
                            case SNOWMAKER -> snowmakers.add(entity);
                            case TELEPORTER -> {
                                var id = getTeleporterID(entity);
                                teleporterMap.put(id, entity);
                            }
                        }
                    }
                }
                if (!contains) {
                    chunk.unload();
                }
            }
        }

        for (int i = 0; i < 100; i++) {
            // sniper
            sniperCost[i] = (short) (Math.pow(1.032, i + 18));
            sniperMaxHealth[i] = (float) ((int) (200.0 * Math.pow(1.01, i) - 180.0));
            sniperPrecision[i] = (float) (15.0 * Math.pow(0.951, i));
            sniperResistance[i] = (float) (1.0 - Math.pow(0.99, i));
            sniperVelocity[i] = (float) (2.0 * Math.pow(1.02123, i));
            sniperRadius[i] = (float) (16.0 * Math.pow(1.0141, i));
            sniperPower[i] = (float) (9.83188423 * Math.pow(1.02, i) - 3.83188423);

            // gunner
            gunnerCost[i] = (short) (Math.pow(1.026, i + 21));
            gunnerMaxHealth[i] = sniperMaxHealth[i];
            gunnerPrecision[i] = (float) (15.0 * Math.pow(0.98, i));
            gunnerResistance[i] = sniperResistance[i];
            gunnerVelocity[i] = (float) (Math.pow(1.014102, i));
            gunnerPower[i] = (float) (10.0 * Math.pow(1.004, i) - 5.0);
            gunnerRadius[i] = (float) (16.0 * Math.pow(1.0141, 0.5 * i));
            gunnerDelay[i] = (float) (30.0 * Math.pow(0.977, i));

            // tank
            tankCost[i] = (short) (Math.pow(1.058, i - 13) + 1);
            tankMaxHealth[i] = (float) ((int) (200.0 * Math.pow(1.01244, i) - 180.0));
            tankResistance[i] = (float) (1.0 - Math.pow(0.97, i));
            tankVelocity[i] = (float) Math.pow(1.013, i);
            tankPower[i] = (float) (4.0 * Math.pow(1.008, i) - 3.35);

            // flamethrower
            flamethrowerCost[i] = (short) (Math.pow(1.024, i + 14));
            flamethrowerMaxHealth[i] = tankMaxHealth[i];
            flamethrowerResistance[i] = sniperResistance[i];
            flamethrowerPower[i] = (float) (Math.pow(1.0112, i) - 1.0);
            flamethrowerDelay[i] = (float) (30.0 * Math.pow(0.977, (50.0 + 0.5 * i)));
            flamethrowerBurnTime[i] = (short) (19.0 + Math.pow(1.057, i));
            flamesPerTick[i] = (short) (Math.pow(1.0142, 33 + (0.67 * i)));

            // healer
            healerCost[i] = (short) (Math.pow(1.014, i + 50));
            healerMaxHealth[i] = (float) ((int) (20.0 * Math.pow(1.0164, i)));
            healerResistance[i] = (float) (1.0 - Math.pow(0.9948, i));
            healerPower[i] = (float) (0.5 * (Math.pow(1.000954, i + 1) - 1));
            healerRadius[i] = sniperRadius[i];

            // snowmaker
            snowmakerCost[i] = (short) (Math.pow(1.023, i + 8));
            snowmakerMaxHealth[i] = sniperMaxHealth[i];
            snowmakerResistance[i] = sniperResistance[i];
            snowmakerPower[i] = (short) (Math.pow(1.017, i) - 1.0);
            snowmakerDelay[i] = flamethrowerDelay[i];
            snowmakerSlowTime[i] = (short) (29.0 + Math.pow(1.05, i));
            snowmakerBallsPerTick[i] = flamesPerTick[i];

            // teleporter
            teleporterCost[i] = (short) (Math.pow(1.073, i - 20) + 1);
            teleporterMaxHealth[i] = healerMaxHealth[i];
            teleporterResistance[i] = sniperResistance[i];
            teleporterLoadRate[i] = (float) (8.0 * Math.pow(.96125516, i));
            teleporterRadius[i] = (short) (800.0 / teleporterLoadRate[i]);

            // generator
            generatorCost[i] = (short) (1.51 * ((Math.pow(1.042628, i))) + 2);
            generatorMaxHealth[i] = healerMaxHealth[i];
            generatorResistance[i] = healerResistance[i];
            generatorRate[i] = Math.pow(1.042628, i + 1.0) - 1.0;


        }
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onGuardTick(0), 0L);

        for (var gunner : gunners) {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onGunnerTick(gunner), 0L);
        }
        for (var flamethrower : flamethrowers) {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onFlamethrowerTick(flamethrower), 0L);
        }
        for (var snowmaker : snowmakers) {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onSnowmakerTick(snowmaker), 0L);
        }
    }

    private boolean fireHealBeam(Entity healer, Entity target) {
        //T to H
        var tLoc = target.getLocation();
        tLoc.setY(tLoc.getY() + 0.65);
        var hLoc = healer.getLocation();
        hLoc.setY(hLoc.getY() + 0.65);
        var vector = hLoc.toVector().subtract(tLoc.toVector());
        var j = Math.floor(vector.length());
        var world = target.getWorld();
        var locations = new ArrayList<Location>();
        for (int i = 0; i <= j; i++) {
            vector = tLoc.toVector().subtract(hLoc.toVector());
            vector.multiply(1 / vector.length());
            var loc = (hLoc.toVector().add(vector.multiply(i))).toLocation(world);
            locations.add(loc);
            var block = world.getBlockAt(loc);
            if (!block.getType().equals(Material.AIR) && !block.getType().equals(Material.WATER) && !block.isPassable()) {
                return false;
            }
        }
        tickHealBeam(0, j, target, locations, world, HEALER_DELAY * GUARD_TICK_DELAY * getPower(healer));
        return true;
    }

    private void tickHealBeam(int i, final double j, Entity target, ArrayList<Location> locations, World world, float healthBoost) {
        if (i == j) {
            if (!target.isDead()) {
                var max = getMaxHealth(target);
                setHealth(target, Math.min(max, getHealth(target) + healthBoost));
            }
            return;
        }
        var dustOptions = new Particle.DustOptions(Color.fromRGB(255, 50, 120), 3.0f);
        var location = locations.get(i);
        world.spawnParticle(Particle.REDSTONE, location, 1, dustOptions);
        var block = world.getBlockAt(location);
        if (!block.getType().equals(Material.AIR) && !block.getType().equals(Material.WATER) && !block.isPassable()) {
            return;
        }
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> tickHealBeam(i + 1, j, target, locations, world, healthBoost), 1L);
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
        // Math stuff
        double GRAVITY_CONSTANT = 0.05;
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
        return y + targetLoc.getY() - coef * tempSD + ydiff;
    }

    private Arrow spawnGuardArrow(Location targetLoc, Location guardLoc, float speed, Entity guard) {
        var vec = targetLoc.toVector().subtract(guardLoc.toVector()).normalize();
        var aLoc = new Location(guardLoc.getWorld(), guardLoc.getX() + vec.getX(), guardLoc.getY() + vec.getY(), guardLoc.getZ() + vec.getZ());
        var y = adjustHeight(aLoc, targetLoc, speed);
        targetLoc.setY(y);
        vec = targetLoc.toVector().subtract(aLoc.toVector()).normalize();
        var arrow = guard.getWorld().spawnArrow(aLoc, vec, speed, getPrecision(guard));
        aLoc.getWorld().playSound(aLoc, Sound.ENTITY_ARROW_SHOOT, 1, 1);
        arrow.setDamage(getPower(guard));
        arrow.getPersistentDataContainer().set(GUARD_PROJECTILE_KEY, BooleanPersistentDataType.instance, true);
        trackNewProjectile(arrow, guard);
        return arrow;
    }

    private void spawnFlame(Location targetLoc, Location guardLoc, Entity guard, short level) {
        var guardY = guardLoc.getY() + 0.5;
        var targY = targetLoc.getY();
        guardLoc.setY(guardY);
        targetLoc.setY(targY + Math.abs(targY - guardY) * 0.4);
        guard.getWorld().playSound(guard.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.25f, 1.0f);
        var stack = new ItemStack(Material.BLAZE_POWDER, 1);
        var vec = targetLoc.toVector().subtract(guardLoc.toVector()).normalize();
        var aLoc = new Location(guardLoc.getWorld(), guardLoc.getX() + vec.getX(), guardLoc.getY() + vec.getY(), guardLoc.getZ() + vec.getZ());
        var drop = guard.getWorld().dropItem(aLoc, stack);
        drop.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.SHORT, level);
        drop.getPersistentDataContainer().set(GUARD_PROJECTILE_KEY, BooleanPersistentDataType.instance, true);
        var variation = 0.1;
        vec.setX(vec.getX() + Math.random() * variation - 0.5 * variation);
        vec.setY(vec.getY() + Math.random() * variation - 0.5 * variation);
        vec.setZ(vec.getZ() + Math.random() * variation - 0.5 * variation);
        drop.setVelocity(vec);
        drop.setPickupDelay(1000);
        flames.put(drop, guard);
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                    if (!drop.isDead()) {
                        flames.remove(drop);
                        drop.remove();
                    }
                },
                30L
        );
    }

    private void fireSnowball(Location targetLoc, Location guardLoc, Entity guard, short level) {
        var tempLoc = new Location(targetLoc.getWorld(), guardLoc.getX(), targetLoc.getY(), guardLoc.getZ());
        var flatDist = tempLoc.distanceSquared(targetLoc);

        var guardY = guardLoc.getY() + 0.5;
        var targY = targetLoc.getY();
        guardLoc.setY(guardY);
        targetLoc.setY(targY + .0037 * flatDist);
        var vec = targetLoc.toVector().subtract(guardLoc.toVector()).normalize();
        var aLoc = new Location(guardLoc.getWorld(), guardLoc.getX() + vec.getX(), guardLoc.getY() + vec.getY(), guardLoc.getZ() + vec.getZ());
        var ball = guardLoc.getWorld().spawn(aLoc, Snowball.class);
        ball.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.SHORT, level);
        ball.getPersistentDataContainer().set(GUARD_PROJECTILE_KEY, BooleanPersistentDataType.instance, true);
        trackedProjectiles.put(ball, guard);
        var variation = 0.1;
        vec.setX(vec.getX() + Math.random() * variation - 0.5 * variation);
        vec.setY(vec.getY() + Math.random() * variation - 0.5 * variation);
        vec.setZ(vec.getZ() + Math.random() * variation - 0.5 * variation);
        vec.multiply(1.5);
        ball.setVelocity(vec);
        guard.getWorld().playSound(guardLoc, Sound.ENTITY_SNOWBALL_THROW, 1.0f, 1.0f);
    }

    private void fireMissile(Location targetLoc, Location guardLoc, float speed, Entity guard, long time) {
        guard.getWorld().playSound(guard.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 0.25f, 1.0f);
        var stack = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.THROWN_GRENADE, 1);
        var vec = targetLoc.toVector().subtract(guardLoc.toVector()).normalize();
        var aLoc = new Location(guardLoc.getWorld(), guardLoc.getX() + vec.getX(), guardLoc.getY() + vec.getY(), guardLoc.getZ() + vec.getZ());
        var vel = vec.clone();
        vel.multiply(speed);
        var drop = guard.getWorld().dropItem(aLoc, stack);
        drop.getPersistentDataContainer().set(GUARD_PROJECTILE_KEY, BooleanPersistentDataType.instance, true);
        drop.setVelocity(vel);
        drop.setPickupDelay(1000);
        drop.setGravity(false);
        var power = getPower(guard);
        missileTick(drop, time, power, false);
    }

    private Location getNextLocation(Item item) {
        var location = item.getLocation();
        var velocity = item.getVelocity();
        return new Location(location.getWorld(), location.getX() + velocity.getX(), location.getY() + velocity.getY(), location.getZ() + velocity.getZ());
    }

    private boolean entityCollision(Item item, Location location, Location nextLocation) {
        var xDist = Math.abs(location.getX() - nextLocation.getX());
        var yDist = Math.abs(location.getY() - nextLocation.getY());
        var zDist = Math.abs(location.getZ() - nextLocation.getZ());

        var nearby = item.getNearbyEntities(xDist, yDist, zDist);
        if (nearby.size() == 0) return false;
        else {
            var x = location.getX();
            var y = location.getY();
            var z = location.getZ();
            var v = item.getVelocity();
            var vX = v.getX();
            var vY = v.getY();
            var vZ = v.getZ();
            for (int i = 1; i <= 10; i++) {
                x += vX / 10.0;
                y += vY / 10.0;
                z += vZ / 10.0;
                for (var entity : nearby) {
                    if (isGuard(entity)) continue;
                    var box = entity.getBoundingBox();
                    if (box.contains(x, y, z)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void missileTick(Item item, long curTime, float power, boolean isExploding) {
        if (curTime == 0 || isExploding) {
            item.getWorld().createExplosion(item.getLocation(), power, false, false, item);
            item.remove();
            return;
        }
        var location = item.getLocation();
        var location2 = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        var velocity = item.getVelocity();
        location2.setY(item.getLocation().getY() + 0.25);
        item.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location2, 1, 0.05, 0.05, 0.05, 0);
        var nextLocation = getNextLocation(item);
        if (location.getBlock().isLiquid()) {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> missileTick(item, curTime - 1L, power, true), 1L);
            return;
        }

        if (entityCollision(item, item.getLocation(), nextLocation)) {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> missileTick(item, curTime - 1L, power, true), 1L);
            return;
        }
        var vLength = velocity.length();

        // X-axis check
        var magnitude = Math.abs(nextLocation.getBlockX() - location.getBlockX());
        if (velocity.getX() > 0 && nextLocation.getBlockX() - location.getBlockX() >= 1) {
            var colliding = true;
            for (int i = 1; i <= magnitude; i++) {
                if (item.getWorld().getBlockAt(location.getBlockX() + i, location.getBlockY(), location.getBlockZ()).isPassable()) {
                    colliding = false;
                    break;
                }
            }
            if (colliding) {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> missileTick(item, curTime - 1L, power, true), 1L);
                return;
            }
        } else if (velocity.getX() < 0
                && nextLocation.getBlockX() - location.getBlockX() <= -1) {
            var colliding = false;
            for (int i = 1; i <= magnitude; i++) {
                if (!item.getWorld().getBlockAt(location.getBlockX() - i, location.getBlockY(), location.getBlockZ()).isPassable()) {
                    colliding = true;
                    break;
                }
            }
            if (colliding) {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> missileTick(item, curTime - 1L, power, true), 1L);
                return;
            }
        }

        // Y-axis check
        magnitude = Math.abs(nextLocation.getBlockY() - location.getBlockY());
        if (velocity.getY() > 0
                && nextLocation.getBlockY() - location.getBlockY() >= 1) {
            var colliding = true;
            for (int i = 1; i <= magnitude; i++) {
                if (item.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + i, location.getBlockZ()).isPassable()) {
                    colliding = false;
                    break;
                }
            }
            if (colliding) {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> missileTick(item, curTime - 1L, power, true), 1L);
                return;
            }

        } else if (velocity.getY() < 0
                && nextLocation.getBlockY() - location.getBlockY() <= -1) {
            var colliding = false;
            for (int i = 1; i <= magnitude; i++) {
                if (!item.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() - i, location.getBlockZ()).isPassable()) {
                    colliding = true;
                    break;
                }
            }
            if (colliding) {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> missileTick(item, curTime - 1L, power, true), 1L);
                return;
            }
        }

        // Z-axis check
        magnitude = Math.abs(nextLocation.getBlockZ() - location.getBlockZ());
        if (velocity.getZ() > 0
                && nextLocation.getBlockZ() - location.getBlockZ() >= 1) {
            var colliding = true;
            for (int i = 1; i <= magnitude; i++) {
                if (item.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() + i).isPassable()) {
                    colliding = false;
                    break;
                }
            }
            if (colliding) {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> missileTick(item, curTime - 1L, power, true), 1L);
                return;
            }
        } else if (velocity.getZ() < 0
                && nextLocation.getBlockZ() - location.getBlockZ() <= -1) {
            var colliding = false;
            for (int i = 1; i <= magnitude; i++) {
                if (!item.getWorld().getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ() - i).isPassable()) {
                    colliding = true;
                    break;
                }
            }
            if (colliding) {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> missileTick(item, curTime - 1L, power, true), 1L);
                return;
            }
        }
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> missileTick(item, curTime - 1L, power, false), 1L);
    }

    private void onFlamethrowerTick(Entity guard) {
        if (guard.isDead()) {
            return;
        }
        var delay = getDelay(guard);
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
            if (guard.isDead()) {
                return;
            }
            var radius = 12.0f;
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
                    if (d < distance) {
                        distance = d;
                        closest = (Player) entity;
                        loc = pLoc;
                    }
                }
            }

            if (closest != null) {
                var level = getLevel(guard);
                var amount = flamesPerTick[level - 1];
                for (int i = 0; i < amount; i++) {
                    spawnFlame(loc, gLoc, guard, level);
                }
            }
        }, (long) (Math.random() * (delay * 0.4)));

        var lDelay = (long) delay;
        var remainder = lDelay - delay;
        if (Math.random() < remainder) lDelay++;
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onFlamethrowerTick(guard), lDelay);
    }

    private void onSnowmakerTick(Entity guard) {
        if (guard.isDead()) {
            return;
        }
        var delay = getDelay(guard);
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
            if (guard.isDead()) {
                return;
            }
            var radius = 24.0f;
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
                    if (d < distance) {
                        distance = d;
                        closest = (Player) entity;
                        loc = pLoc;
                    }
                }
            }

            if (closest != null) {
                var level = getLevel(guard);
                var amount = snowmakerBallsPerTick[level - 1];
                for (int i = 0; i < amount; i++) {
                    fireSnowball(loc, gLoc, guard, level);
                }
            }
        }, (long) (Math.random() * (delay * 0.4)));

        var lDelay = (long) delay;
        var remainder = lDelay - delay;
        if (Math.random() < remainder) lDelay++;
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onSnowmakerTick(guard), lDelay);
    }

    private void onGunnerTick(Entity guard) {
        if (guard.isDead()) {
            return;
        }
        var delay = getDelay(guard);
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
            if (guard.isDead()) {
                return;
            }
            var radius = getRadius(guard);
            var speed = getProjectileVelocity(guard);
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
                spawnGuardArrow(loc, gLoc, speed, guard);
            }
        }, (long) (Math.random() * (delay * 0.4)));

        var lDelay = (long) delay;
        var remainder = lDelay - delay;
        if (Math.random() < remainder) lDelay++;
        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onGunnerTick(guard), lDelay);
    }

    private void onGuardTick(int i) {
        var healMap = new HashMap<Entity, Integer>();
        int GUARD_HEAL_DELAY = 10;
        var isHealTick = i % (GUARD_HEAL_DELAY / GUARD_TICK_DELAY) == 0;
        for (var guard : new HashSet<>(guards)) {
            if (guard.isDead()) {
                guards.remove(guard);
            }
            if (isHealTick) {
                var health = getHealth(guard);
                var max = getMaxHealth(guard);
                if (health < max) {
                    setHealth(guard, Math.min(health + GUARD_HEAL_DELAY / 1000.0, max));
                }
            }
            var type = getType(guard);
            switch (type) {
                case SNIPER -> {
                    int SNIPER_DELAY = 80;
                    if (i % (SNIPER_DELAY / GUARD_TICK_DELAY) == 0) {
                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                            if (guard.isDead()) {
                                return;
                            }
                            var radius = getRadius(guard);
                            var speed = getProjectileVelocity(guard);
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
                                spawnGuardArrow(loc, gLoc, speed, guard);
                            }
                        }, (long) (Math.random() * (SNIPER_DELAY * 0.4)));
                    }
                }
                case TANK -> {
                    int TANK_DELAY = 160;
                    if (i % (TANK_DELAY / GUARD_TICK_DELAY) == 0) {
                        Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                            var radius = 50.0;
                            var speed = getProjectileVelocity(guard);
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
                                    if (d < distance) {
                                        distance = d;
                                        closest = (Player) entity;
                                        loc = pLoc;
                                    }
                                }
                            }

                            if (closest != null) {
                                var d = Math.sqrt(distance);
                                var ticks = (long) (d / speed * 1.2);
                                fireMissile(loc, gLoc, speed, guard, ticks);
                            }
                        }, (long) (Math.random() * (TANK_DELAY * 0.4)));
                    }
                }
                case HEALER -> {
                    if (i % (HEALER_DELAY / GUARD_TICK_DELAY) == 0) { // only 1/10 ticks
                        var radius = getRadius(guard);
                        var nearby = guard.getNearbyEntities(radius, radius, radius);
                        var nation = getNation(guard);
                        var valid = new ArrayList<Entity>();
                        for (var entity : nearby) {
                            if (isGuard(entity) && !entity.isDead() && Objects.equals(nation, getNation(entity)) && !(healMap.containsKey(entity) && healMap.get(entity) == 3)) {
                                var health = getHealth(entity);
                                var max = getMaxHealth(entity);
                                if (health < max) {
                                    valid.add(entity);
                                }
                            }
                        }
                        valid.sort(Comparator.comparingDouble((p) -> getHealth(p) / getMaxHealth(p)));
                        for (var target : valid) {
                            var success = fireHealBeam(guard, target);
                            if (success) {
                                if (healMap.containsKey(target)) {
                                    healMap.replace(target, healMap.get(target) + 1);
                                } else {
                                    healMap.put(target, 1);
                                }
                                break;
                            }
                        }
                    }
                }
                case GENERATOR -> {
                    if (i % (300 / GUARD_TICK_DELAY) == 0) {
                        var container = guard.getPersistentDataContainer();
                        var level = getLevel(guard);
                        var rate = generatorRate[level - 1];

                        //rate * drops per day
                        var gunpowder = container.get(GENERATOR_GUNPOWDER_KEY, PersistentDataType.DOUBLE) + ((rate * 32.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (gunpowder > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.GUNPOWDER));
                            gunpowder--;
                        }
                        container.set(GENERATOR_GUNPOWDER_KEY, PersistentDataType.DOUBLE, gunpowder);

                        // lapis
                        var lapis = container.get(GENERATOR_LAPIS_KEY, PersistentDataType.DOUBLE) + ((rate * 32.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (lapis > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.LAPIS_LAZULI));
                            lapis--;
                        }
                        container.set(GENERATOR_LAPIS_KEY, PersistentDataType.DOUBLE, lapis);

                        // redstone
                        var redstone = container.get(GENERATOR_REDSTONE_KEY, PersistentDataType.DOUBLE) + ((rate * 32.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (redstone > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.REDSTONE));
                            redstone--;
                        }
                        container.set(GENERATOR_REDSTONE_KEY, PersistentDataType.DOUBLE, redstone);

                        // quartz
                        var quartz = container.get(GENERATOR_QUARTZ_KEY, PersistentDataType.DOUBLE) + ((rate * 32.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (quartz > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.QUARTZ));
                            quartz--;
                        }
                        container.set(GENERATOR_QUARTZ_KEY, PersistentDataType.DOUBLE, quartz);

                        // glowstone
                        var glowstone = container.get(GENERATOR_GLOWSTONE_KEY, PersistentDataType.DOUBLE) + ((rate * 32.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (glowstone > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.GLOWSTONE_DUST));
                            glowstone--;
                        }
                        container.set(GENERATOR_GLOWSTONE_KEY, PersistentDataType.DOUBLE, glowstone);

                        // iron
                        var iron = container.get(GENERATOR_IRON_KEY, PersistentDataType.DOUBLE) + ((rate * 432.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (iron > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.IRON_NUGGET));
                            iron--;
                        }
                        container.set(GENERATOR_IRON_KEY, PersistentDataType.DOUBLE, iron);

                        // gold
                        var gold = container.get(GENERATOR_GOLD_KEY, PersistentDataType.DOUBLE) + ((rate * 432.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (gold > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.GOLD_NUGGET));
                            gold--;
                        }
                        container.set(GENERATOR_GOLD_KEY, PersistentDataType.DOUBLE, gold);

                        // emerald
                        var emerald = container.get(GENERATOR_EMERALD_KEY, PersistentDataType.DOUBLE) + ((rate * 8.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (emerald > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.EMERALD));
                            emerald--;
                        }
                        container.set(GENERATOR_EMERALD_KEY, PersistentDataType.DOUBLE, emerald);

                        // magic dust
                        var magicDust = container.get(GENERATOR_MAGIC_DUST_KEY, PersistentDataType.DOUBLE) + ((rate * 16.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (magicDust > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.MAGICAL_DUST, 1));
                            magicDust--;
                        }
                        container.set(GENERATOR_MAGIC_DUST_KEY, PersistentDataType.DOUBLE, magicDust);

                        // diamond
                        var diamond = container.get(GENERATOR_DIAMOND_KEY, PersistentDataType.DOUBLE) + ((rate * 8.0) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (diamond > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.DIAMOND));
                            diamond--;
                        }
                        container.set(GENERATOR_DIAMOND_KEY, PersistentDataType.DOUBLE, diamond);

                        // netherite
                        var netherite = container.get(GENERATOR_NETHERITE_KEY, PersistentDataType.DOUBLE) + ((rate * 1.5) / (1440.0 * 64.0 * 4.0)) * (.9 + .2 * Math.random());
                        if (netherite > 1.0) {
                            var loc = guard.getLocation();
                            loc.getWorld().dropItem(loc, new ItemStack(Material.NETHERITE_SCRAP));
                            netherite--;
                        }
                        container.set(GENERATOR_NETHERITE_KEY, PersistentDataType.DOUBLE, netherite);
                    }
                }
            }
        }

        // flames
        for (var flame : new ArrayList<>(flames.keySet())) {
            var nearby = flame.getNearbyEntities(0.5, 0.5, 0.5);
            for (var entity : nearby) {
                var guard = flames.get(flame);
                if (entity instanceof Player && canAttackPlayer((Player) entity, guard)) {
                    var level = flame.getPersistentDataContainer().get(LEVEL_KEY, PersistentDataType.SHORT);
                    var burnTime = flamethrowerBurnTime[level - 1];
                    var damage = flamethrowerPower[level - 1];
                    ((LivingEntity) entity).damage(damage, flame);
                    entity.setFireTicks(Math.max(entity.getFireTicks(), burnTime));
                    flames.remove(flame);
                    flame.remove();
                    break;
                } else if (entity instanceof LivingEntity) {
                    var level = flame.getPersistentDataContainer().get(LEVEL_KEY, PersistentDataType.SHORT);
                    var burnTime = flamethrowerBurnTime[level - 1];
                    var damage = flamethrowerPower[level - 1];
                    ((LivingEntity) entity).damage(damage, flame);
                    entity.setFireTicks(Math.max(entity.getFireTicks(), burnTime));
                    flames.remove(flame);
                    flame.remove();
                    break;
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
        var name = getName(entity);
        return switch (type) {
            case BASIC -> ChatColor.DARK_AQUA + "[" + name + " | Level " + level + " Guard] " + ChatColor.WHITE;
            case SNIPER -> ChatColor.DARK_GREEN + "[" + name + " | Level " + level + " Sniper] " + ChatColor.WHITE;
            case GUNNER -> ChatColor.DARK_GRAY + "[" + name + " | Level " + level + " Gunner] " + ChatColor.WHITE;
            case FLAMETHROWER -> ChatColor.RED + "[" + name + " | Level " + level + " Flamethrower] " + ChatColor.WHITE;
            case GRENADER -> ChatColor.DARK_RED + "[" + name + " | Level " + level + " Grenader] " + ChatColor.WHITE;
            case HEALER -> ChatColor.LIGHT_PURPLE + "[" + name + " | Level " + level + " Healer] " + ChatColor.WHITE;
            case TANK -> ChatColor.DARK_PURPLE + "[" + name + " | Level " + level + " Tank] " + ChatColor.WHITE;
            case TELEPORTER -> ChatColor.YELLOW + "[" + name + " | Level " + level + " Teleporter] " + ChatColor.WHITE;
            case GENERATOR -> ChatColor.GOLD + "[" + name + " | Level " + level + " Generator] " + ChatColor.WHITE;
            case SNOWMAKER -> ChatColor.AQUA + "[" + name + " | Level " + level + " Snowmaker] " + ChatColor.WHITE;
        };
    }

    public Entity spawnGuard(Location loc) {
        loc.setX(loc.getX() + 0.5);
        loc.setZ(loc.getZ() + 0.5);
        var chunk = loc.getChunk();
        if (!chunk.isForceLoaded()) {
            chunk.setForceLoaded(true);
        }
        var entity = loc.getWorld().spawnEntity(loc, EntityType.ENDER_CRYSTAL);
        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_ACTIVATE, 1, 1);
        var container = entity.getPersistentDataContainer();

        // type
        container.set(TYPE_KEY, PersistentDataType.BYTE, (byte) 0);

        // health
        container.set(HEALTH_KEY, PersistentDataType.FLOAT, 20.00f);

        // level
        container.set(LEVEL_KEY, PersistentDataType.SHORT, (short) 0);

        // name
        container.set(NAME_KEY, PersistentDataType.STRING, "Guard");
        // preferences
        container.set(NOTIFY_DAMAGE_KEY, BooleanPersistentDataType.instance, true);
        container.set(ATTACK_TRESPASSERS, BooleanPersistentDataType.instance, false);
        container.set(ATTACK_NEUTRAL, BooleanPersistentDataType.instance, true);
        container.set(ATTACK_ALLIES, BooleanPersistentDataType.instance, false);
        container.set(ATTACK_NEWBIES, BooleanPersistentDataType.instance, false);

        entity.setCustomNameVisible(true);
        entity.setCustomName(ChatColor.DARK_AQUA + "[Guard | Level 0 Guard] " + ChatColor.WHITE + "20.00 HP");
        ((EnderCrystal) entity).setShowingBottom(false);
        guards.add(entity);
        return entity;
    }

    public void killGuard(Entity entity) {
        var loc = entity.getLocation();
        var chunk = loc.getChunk();
        if (getType(entity) == Type.TELEPORTER) {
            for (var id : teleporterMap.keySet()) {
                var teleporter = teleporterMap.get(id);
                removeAllowedTeleporter(teleporter, entity);
            }
            teleporterMap.remove(getTeleporterID(entity));
        }
        var container = entity.getPersistentDataContainer();
        var keys = container.getKeys();
        for (var key : keys) {
            container.remove(key);
        }
        guards.remove(entity);
        entity.remove();
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
        loc.getWorld().playSound(loc, Sound.BLOCK_BEACON_DEACTIVATE, 1, 1);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 4);
        var entities = chunk.getEntities();
        var hasGuards = false;
        for (var tEntity : entities) {
            if (isGuard(tEntity)) {
                hasGuards = true;
                break;
            }
        }
        if (!hasGuards && chunk.isForceLoaded()) {
            chunk.setForceLoaded(false);
        }
    }

    public boolean isGuard(Entity entity) {
        var container = entity.getPersistentDataContainer();
        return container.has(TYPE_KEY, PersistentDataType.BYTE);
    }

    public short getLevel(Entity entity) {
        var container = entity.getPersistentDataContainer();
        if (container.has(LEVEL_KEY, PersistentDataType.SHORT)) {
            return entity.getPersistentDataContainer().get(LEVEL_KEY, PersistentDataType.SHORT);
        } else {
            return (short) 0;
        }
    }

    public short getCost(Entity entity) {
        var level = getLevel(entity);
        if (level == 100) return -1;
        var type = getType(entity);
        return switch (type) {
            default -> (short) 1;
            case SNIPER -> sniperCost[level];
            case GUNNER -> gunnerCost[level];
            case TANK -> tankCost[level];
            case FLAMETHROWER -> flamethrowerCost[level];
            case HEALER -> healerCost[level];
            case SNOWMAKER -> snowmakerCost[level];
            case TELEPORTER -> teleporterCost[level];
            case GENERATOR -> generatorCost[level];
        };
    }

    public void setLevel(Entity entity, int level) {
        var cur = getMaxHealth(entity);
        entity.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.SHORT, (short) level);
        var change = getMaxHealth(entity) - cur;
        setHealth(entity, getHealth(entity) + change);
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
        entity.getPersistentDataContainer().set(TYPE_KEY, PersistentDataType.BYTE, (byte) id);
        entity.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.SHORT, (short) 1);
        switch (type) {
            case GUNNER -> Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onGunnerTick(entity), 0L);
            case FLAMETHROWER -> Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onFlamethrowerTick(entity), 0L);
            case SNOWMAKER -> Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> onSnowmakerTick(entity), 0L);
            case TELEPORTER -> {
                var newId = getUniqueTeleporterID();
                var container = entity.getPersistentDataContainer();
                container.set(TELEPORTER_ID_KEY, PersistentDataType.INTEGER, newId);
                container.set(ALLOWED_TELEPORTS_KEY, PersistentDataType.INTEGER_ARRAY, new int[0]);
                teleporterMap.put(newId, entity);
            }
            case GENERATOR -> {
                var container = entity.getPersistentDataContainer();
                container.set(GENERATOR_GUNPOWDER_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_LAPIS_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_IRON_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_GOLD_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_QUARTZ_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_GLOWSTONE_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_REDSTONE_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_EMERALD_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_MAGIC_DUST_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_DIAMOND_KEY, PersistentDataType.DOUBLE, 0.0);
                container.set(GENERATOR_NETHERITE_KEY, PersistentDataType.DOUBLE, 0.0);
            }
        }
        setHealth(entity, getHealth(entity));
        return true;
    }

    public double getHealth(Entity entity) {
        return entity.getPersistentDataContainer().get(HEALTH_KEY, PersistentDataType.FLOAT);
    }

    public void setHealth(Entity entity, double health) {
        entity.getPersistentDataContainer().set(HEALTH_KEY, PersistentDataType.FLOAT, (float) health);
        var strHealth = String.format("%.2f", health);
        entity.setCustomName(getTypePrefix(entity) + strHealth + " HP");
    }


    public Nation getNation(Entity entity) {
        return DiplomacyChunks.getInstance().getDiplomacyChunk(entity.getLocation().getChunk()).getNation();
    }

    public String getName(Entity entity) {
        return entity.getPersistentDataContainer().get(NAME_KEY, PersistentDataType.STRING);
    }

    public void setName(Entity entity, String name) {
        entity.getPersistentDataContainer().set(NAME_KEY, PersistentDataType.STRING, name);
        setHealth(entity, getHealth(entity));
    }

    public float getResistance(Entity entity) {
        var type = getType(entity);
        var level = getLevel(entity);
        return switch (type) {
            default -> 0.0f;
            case SNIPER -> sniperResistance[level - 1];
            case GUNNER -> gunnerResistance[level - 1];
            case TANK -> tankResistance[level - 1];
            case FLAMETHROWER -> flamethrowerResistance[level - 1];
            case HEALER -> healerResistance[level - 1];
            case SNOWMAKER -> snowmakerResistance[level - 1];
            case TELEPORTER -> teleporterResistance[level - 1];
            case GENERATOR -> generatorResistance[level - 1];
        };
    }

    public float getMaxHealth(Entity entity) {
        var type = getType(entity);
        var level = getLevel(entity);
        return switch (type) {
            default -> 20.0f;
            case SNIPER -> sniperMaxHealth[level - 1];
            case GUNNER -> gunnerMaxHealth[level - 1];
            case TANK -> tankMaxHealth[level - 1];
            case FLAMETHROWER -> flamethrowerMaxHealth[level - 1];
            case HEALER -> healerMaxHealth[level - 1];
            case SNOWMAKER -> snowmakerMaxHealth[level - 1];
            case TELEPORTER -> teleporterMaxHealth[level - 1];
            case GENERATOR -> generatorMaxHealth[level - 1];
        };
    }

    public float getRadius(Entity entity) {
        var type = getType(entity);
        var level = getLevel(entity);
        return switch (type) {
            default -> 0.0f;
            case SNIPER -> sniperRadius[level - 1];
            case GUNNER -> gunnerRadius[level - 1];
            case HEALER -> healerRadius[level - 1];
            case TELEPORTER -> teleporterRadius[level - 1];
        };
    }

    public float getPrecision(Entity entity) {
        var level = getLevel(entity);
        var type = getType(entity);
        return switch (type) {
            default -> 90.0f;
            case SNIPER -> sniperPrecision[level - 1];
            case GUNNER -> gunnerPrecision[level - 1];
        };
    }

    public float getProjectileVelocity(Entity entity) {
        var level = getLevel(entity);
        var type = getType(entity);
        return switch (type) {
            default -> 0.0f;
            case SNIPER -> sniperVelocity[level - 1];
            case GUNNER -> gunnerVelocity[level - 1];
            case TANK -> tankVelocity[level - 1];
        };
    }

    public float getPower(Entity entity) {
        var type = getType(entity);
        var level = getLevel(entity);
        return switch (type) {
            default -> 0.0f;
            case SNIPER -> sniperPower[level - 1];
            case GUNNER -> gunnerPower[level - 1];
            case TANK -> tankPower[level - 1];
            case HEALER -> healerPower[level - 1];
        };
    }

    public float getDelay(Entity entity) {
        var level = getLevel(entity);
        var type = getType(entity);
        return switch (type) {
            case GUNNER -> gunnerDelay[level - 1];
            case FLAMETHROWER -> flamethrowerDelay[level - 1];
            case SNOWMAKER -> snowmakerDelay[level - 1];
            default -> 20f;
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

    private HashMap<Projectile, Entity> trackedProjectiles = new HashMap<>(); // projectile entity, guard entity

    public void trackNewProjectile(Projectile projectile, Entity guard) {
        trackedProjectiles.put(projectile, guard);
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
        // is in survival mode
        // 1. can always attack outlaws, enemies, and nomads
        // 2. if tresspassing and can attack tresspassers, will attack, regardless of other settings
        // 3. if not tresspassing or can't attack tresspassers:
        // if ally: check attack allies
        // if neutral: check attack neutral
        // if newbie: check attack newbie
        if (damager.getGameMode() != GameMode.SURVIVAL) return false;

        var dp = DiplomacyPlayers.getInstance().get(((Player) damager).getUniqueId());
        var nation = Nations.getInstance().get(dp);
        var guardNation = getNation(guard);
        if (nation == null || (guardNation.getEnemyNationIDs().contains(nation.getNationID())) || (guardNation.getOutlaws().contains(damager.getUniqueId()))) {
            return true;
        }

        var block = damager.getLocation().getBlock();
        var chunk = block.getChunk();
        var canBuild = DiplomacyPlayers.getInstance().canBuildHere(block, damager, null);
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
        } else if (damager instanceof Item && Items.getInstance().isGrenade(((Item) damager).getItemStack())) {
            return Items.getInstance().grenadeThrowerMap.get(damager);
        } else if (damager instanceof TNTPrimed) {
            var primer = ((TNTPrimed) damager).getSource();
            if (primer == null) return damager;
            else return getTrueDamager(primer);
        } else {
            return damager;
        }
    }

    public boolean canManageGuard(Player player, Entity guard) {
        // check if in nation
        var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        var playerNation = Nations.getInstance().get(dp);
        if (playerNation == null) { // not in nation
            return false;
        }

        // check if in correct nation
        var guardNation = DiplomacyChunks.getInstance().getDiplomacyChunk(guard.getLocation().getChunk()).getNation();
        if (!playerNation.equals(guardNation)) {
            return false;
        }

        // check if outlaw
        if (guardNation.getOutlaws().contains(player.getUniqueId())) {
            return false;
        }

        // check nation permissions
        var permissions = playerNation.getMemberClass(dp).getPermissions();
        var canManageGuards = permissions.get("CanManageGuards");
        if (!canManageGuards) {
            return false;
        }
        return true;
    }

    public boolean isGuardProjectile(Entity entity) {
        return entity.getPersistentDataContainer().has(GUARD_PROJECTILE_KEY, BooleanPersistentDataType.instance);
    }

    public void beginTeleportation(Player player, Entity fromGuard, Entity toGuard, long time) {
        long key = (long) (Math.random() * Long.MAX_VALUE);
        activeTeleports.remove(player);
        activeTeleports.put(player, key);
        var fromLoc = fromGuard.getLocation();
        var x = fromLoc.getX();
        var y = fromLoc.getY();
        var z = fromLoc.getZ();
        var box = new BoundingBox(x - 4, y - 4, z - 4, x + 4, y + 4, z + 4);
        var locs = new HashSet<Location>();
        for (double minX = x - 4; minX < x + 4; minX++) {
            for (double minY = y - 4; minY < y + 4; minY++) {
                locs.add(new Location(fromGuard.getWorld(), minX, minY, z - 4));
                locs.add(new Location(fromGuard.getWorld(), minX, minY, z + 4));
            }

            for (double minZ = z - 4; minZ < z + 4; minZ++) {
                locs.add(new Location(fromGuard.getWorld(), minX, y - 4, minZ));
                locs.add(new Location(fromGuard.getWorld(), minX, y + 4, minZ));
            }
        }
        for (double minZ = z - 4; minZ < z + 4; minZ++) {
            for (double minY = y - 4; minY < y + 4; minY++) {
                locs.add(new Location(fromGuard.getWorld(), x - 4, minY, minZ));
                locs.add(new Location(fromGuard.getWorld(), x + 4, minY, minZ));
            }
        }
        var name = getName(toGuard);
        teleportationTick(player, fromGuard, toGuard, name, time, time, key, box, locs);
    }

    public void teleportationTick(Player player, Entity fromGuard, Entity toGuard, String toName, long remaining, long initial, final long key, BoundingBox box, Set<Location> particleLocs) {
        if (!activeTeleports.containsKey(player)) {
            if (player.isOnline()) {
                player.sendMessage(ChatColor.RED + "Teleportation Cancelled.");
                player.sendTitle(ChatColor.RED + "Teleportation Cancelled", null, 0, 40, 10);
            }
            return;
        }
        if (activeTeleports.get(player) != key) {
            if (player.isOnline()) {
                player.sendMessage(ChatColor.DARK_GREEN + "Previous Teleportation Cancelled: New teleport initiated.");
                player.sendTitle(ChatColor.RED + "Teleportation Cancelled", ChatColor.RED + "New teleport initiated", 0, 40, 10);
            }
            return;
        }
        if (!player.isOnline()) {
            activeTeleports.remove(player);
            return;
        }
        if (fromGuard.isDead()) {
            activeTeleports.remove(player);
            player.sendMessage(ChatColor.RED + "Teleportation Cancelled: Teleporter crystal has been destroyed.");
            player.sendTitle(ChatColor.RED + "Teleportation Cancelled", ChatColor.RED + "Teleporter crystal has been destroyed", 0, 40, 10);

            return;
        }
        if (toGuard.isDead()) {
            activeTeleports.remove(player);
            player.sendMessage(ChatColor.RED + "Teleportation Cancelled: Destination crystal has been destroyed.");
            player.sendTitle(ChatColor.RED + "Teleportation Cancelled", ChatColor.RED + "Destination crystal has been destroyed", 0, 40, 10);
            return;
        }
        if (!box.contains(player.getBoundingBox())) {
            activeTeleports.remove(player);
            player.sendMessage(ChatColor.RED + "Teleportation Cancelled: You have left the teleportation area.");
            player.sendTitle(ChatColor.RED + "Teleportation Cancelled", ChatColor.RED + "You have left the teleportation area", 0, 40, 10);
            return;
        }
        var seconds = remaining / 20.0;
        var strSeconds = String.format("%.1f", seconds) + "s";
        player.sendTitle(ChatColor.GREEN + "Preparing to teleport to " + toName, ChatColor.GREEN + strSeconds, 0, 10, 1);
        if (remaining % 10 == 0) {
            for (var loc : particleLocs) {
                if (Math.random() < 0.25) {
                    var dustOptions = new Particle.DustOptions(Color.fromRGB(230, 80, 255), (float) (1.0 + Math.random() * 3.0));
                    player.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, dustOptions);
                }
            }
        }
        var percent = (((float) remaining) / ((float) initial));
        player.getWorld().playSound(fromGuard.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, .3f + .7f * percent, 2.0f - 1.5f * ((remaining + (800.0f - initial)) / 800.0f));
        if (remaining == 0) {
            player.sendTitle(ChatColor.GREEN + "Teleporting to " + toName, null, 1, 30, 20);
            var loc = player.getLocation();
            var y = loc.getY();
            var dustOptions = new Particle.DustOptions(Color.fromRGB(230, 80, 255), 1.0f);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 2.0f);
            for (double i = loc.getWorld().getMaxHeight(); i > y; i -= 0.5) {
                loc.setY(i);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, 1, dustOptions);
            }
            var nLoc = toGuard.getLocation();
            var nY = nLoc.getY();
            player.teleport(toGuard.getLocation());
            for (double i = nLoc.getWorld().getMaxHeight(); i > nY; i -= 0.5) {
                nLoc.setY(i);
                nLoc.getWorld().spawnParticle(Particle.REDSTONE, nLoc, 1, dustOptions);
            }
            player.getWorld().playSound(nLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        } else {
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> teleportationTick(player, fromGuard, toGuard, toName, remaining - 1, initial, key, box, particleLocs), 1L);
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
            if (equipment != null && hand != null) {
                var item = equipment.getItem(hand);
                if (type == Action.RIGHT_CLICK_BLOCK && CustomItemGenerator.getInstance().isCustomItem(item) &&
                        CustomItems.getInstance().getEnum(CustomItemGenerator.getInstance().getCustomID(item)) == CustomItems.CustomID.GUARD_CRYSTAL) {
                    var block = event.getClickedBlock();
                    var face = event.getBlockFace();
                    var place = block.getRelative(face);
                    var loc = place.getLocation();
                    event.setCancelled(true);
                    var notNearby = true;
                    var nearby = loc.getWorld().getNearbyEntities(loc, 2, 2, 2);
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
        }


        @EventHandler
        private void projectileHit(ProjectileHitEvent event) {
            var entity = event.getEntity();
            var hit = event.getHitEntity();
            if (hit != null && trackedProjectiles.containsKey(entity)) {
                if (hit instanceof Player) {
                    var guard = trackedProjectiles.get(entity);
                    if (!canAttackPlayer((Player) hit, guard)) {
                        entity.setBounce(true);
                    }
                }
                if (!entity.doesBounce() && entity instanceof Snowball && hit instanceof LivingEntity) {
                    var level = entity.getPersistentDataContainer().get(LEVEL_KEY, PersistentDataType.SHORT);
                    var power = snowmakerPower[level - 1];
                    var length = snowmakerSlowTime[level - 1];
                    var living = ((LivingEntity) hit);
                    var effect = new PotionEffect(PotionEffectType.SLOW, length, power, true, true, true);
                    living.addPotionEffect(effect);
                    living.getWorld().playSound(living.getLocation(), Sound.BLOCK_SNOW_PLACE, 1.5f, 1f);
                    var vel = living.getVelocity();
                    var ballVel = entity.getVelocity();
                    ballVel.normalize();
                    ballVel.multiply(0.3);
                    vel.add(ballVel);
                    living.setVelocity(vel);
                }
            }
            trackedProjectiles.remove(entity);
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
                        interactSet.add(player);
                        var menu = GuardGuis.getInstance().generateGui(entity, player);
                        menu.show(player);
                    }
                    default -> {
                        var canManage = canManageGuard(player, entity);
                        if (canManage) {
                            interactSet.add(player);
                            var menu = GuardGuis.getInstance().generateGui(entity, player);
                            menu.show(player);
                        } else {
                            player.sendMessage(ChatColor.RED + "You do not have permission to manage this guard crystal.");
                        }
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
                    if (!isGuardProjectile(damager)) {
                        if (damager instanceof Trident) {
                        } else {
                            damager.remove();
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
                if (damager instanceof Arrow && isGuardProjectile(damager) && entity instanceof Player) {
                    event.setDamage(((Arrow) damager).getDamage());
                    guardDamage.remove(entity);
                    guardDamage.put((Player) entity, entity.getName() + " was shot by a Guard Crystal");

                } else if (damager instanceof Item && isGuardProjectile(damager) && entity instanceof Player) {
                    guardDamage.remove(entity);
                    guardDamage.put((Player) entity, entity.getName() + " was burned by a Guard Crystal");
                }
            }
        }

        public String getKillMessage(Entity damager, Entity guard) {
            var loc = guard.getLocation();
            var message = ChatColor.DARK_GREEN + "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "] " + ChatColor.RED + getName(guard) + ChatColor.DARK_GREEN + " killed by " + ChatColor.RED;
            if (damager instanceof Projectile) {
                var shooter = ((Projectile) damager).getShooter();
                if (shooter instanceof Player) message += ((Player) shooter).getName();
            } else if (damager instanceof Item && Items.getInstance().isGrenade(((Item) damager).getItemStack())) {
                var shooter = Items.getInstance().grenadeThrowerMap.get(damager);
                if (shooter instanceof Player) message += shooter.getName();
            } else if (damager instanceof Player) {
                message += damager.getName();
            }
            return message;
        }

        public String getDamageMessage(Entity damager, Entity guard) {
            var loc = guard.getLocation();
            var message = ChatColor.DARK_GREEN + "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "] " + ChatColor.RED + getName(guard) + ChatColor.DARK_GREEN + " damaged by " + ChatColor.RED;
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
