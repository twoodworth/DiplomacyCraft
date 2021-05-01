package me.tedwoodworth.diplomacy.guards;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Guis.GuardGuis;
import me.tedwoodworth.diplomacy.Items.CustomItemGenerator;
import me.tedwoodworth.diplomacy.Items.CustomItems;
import me.tedwoodworth.diplomacy.Items.Items;
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
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class GuardManager {
    public static GuardManager instance = null;
    private final Set<Entity> guards = new HashSet<>();
    private final Set<Player> interactSet = new HashSet<>();
    private final NamespacedKey TYPE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_type");
    private final NamespacedKey HEALTH_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_health");
    private final NamespacedKey MAX_HEALTH_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_max_health");
    private final NamespacedKey TARGET_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_target");
    private final NamespacedKey RADIUS_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_radius");
    private final NamespacedKey HEALER_RATE_KEY = new NamespacedKey(Diplomacy.getInstance(), "guard_healer_rate");
    private final int GUARD_TICK_DELAY = 2;
    private final int GUARD_HEAL_DELAY = 10;



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

    private String getTypePrefix(Entity entity) {
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
        var entity = Bukkit.getWorld("world").spawnEntity(loc, EntityType.ENDER_CRYSTAL);
        var container = entity.getPersistentDataContainer();

        // type
        container.set(TYPE_KEY, PersistentDataType.INTEGER, 0);

        // health
        container.set(HEALTH_KEY, PersistentDataType.DOUBLE, 20.0);

        // max health
        container.set(MAX_HEALTH_KEY, PersistentDataType.SHORT, (short)0);

        // radius
        container.set(RADIUS_KEY, PersistentDataType.DOUBLE, 0.0);

        entity.setCustomNameVisible(true);
        entity.setCustomName(ChatColor.DARK_AQUA + "[Basic] " + ChatColor.WHITE + "20.00 Health");
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
                setRadius(entity, 24);
                setHealerRate(entity, (short)0);
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
        entity.setCustomName(getTypePrefix(entity) + strHealth + " Health");
    }


    public Nation getNation(Entity entity) {
        return DiplomacyChunks.getInstance().getDiplomacyChunk(entity.getLocation().getChunk()).getNation();
    }

    public double getHealerRate(Entity entity) {
        var id = entity.getPersistentDataContainer().get(HEALER_RATE_KEY, PersistentDataType.SHORT);
        return switch (id) {
            default -> 4.0 / 1200.0;
            case 1 -> 6.0 / 1200.0;
            case 2 -> 9.0 / 1200.0;
            case 3 -> 13.5 / 1200.0;
            case 4 -> 20.25 / 1200.0;
            case 5 -> 30.375 / 1200.0;
            case 6 -> 45.5624 / 1200.0;
            case 7 -> 68.3436 / 1200.0;
            case 8 -> 101.0756 / 1200.0;
            case 9 -> 230.66 / 1200.0;
            case 10 -> 518.98400 / 1200.0;
        };
    }


    public void setHealerRate(Entity entity, short rate) {
        entity.getPersistentDataContainer().set(HEALER_RATE_KEY, PersistentDataType.SHORT, rate);

    }

    public double getMaxHealth(Entity entity) {
        var id = entity.getPersistentDataContainer().get(MAX_HEALTH_KEY, PersistentDataType.SHORT);
        return switch (id) {
            default -> 20.0;
            case 1 -> 35.0;
            case 2 -> 61.0;
            case 3 -> 107.0;
            case 4 -> 188.0;
            case 5 -> 350.0;
            case 6 -> 574.0;
            case 7 -> 1005.0;
            case 8 -> 1759.0;
            case 9 -> 5388.0;
            case 10 -> 16500.0;
        };
    }

    public void setMaxHealth(Entity entity, short rate) {
        entity.getPersistentDataContainer().set(MAX_HEALTH_KEY, PersistentDataType.SHORT, rate);
    }
    public double getRadius(Entity entity) {
        return entity.getPersistentDataContainer().get(RADIUS_KEY, PersistentDataType.DOUBLE);
    }

    public void setRadius(Entity entity, double radius) {
        entity.getPersistentDataContainer().set(RADIUS_KEY, PersistentDataType.DOUBLE, radius);
    }

    private Type getEnum(int id) {
        var array = Type.values();
        if (array.length > id) {
            return array[id];
        } else {
            throw new IndexOutOfBoundsException("Error: Guard type ID " + id + " is out of bounds");
        }
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
        private void onInteractEntity(PlayerInteractEntityEvent event) {
            var entity = event.getRightClicked();
            var player = event.getPlayer();
            if (isGuard(entity)) {
                interactSet.add(player);
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

                        // check nation permissions
                        var permissions = playerNation.getMemberClass(dp).getPermissions();
                        var canManageGuards = permissions.get("CanManageGuards");
                        if (!canManageGuards) {
                            player.sendMessage(ChatColor.RED + "You do not have permission to manage guard crystals.");
                            return;
                        }
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
                }
            }
        }

        public String getKillMessage(Entity damager, Entity guard) {
            var loc = guard.getLocation();
            var message = "" + ChatColor.BOLD + ChatColor.RED + "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "] " + getTypePrefix(guard) + ChatColor.DARK_GREEN + "Guard killed by " + ChatColor.RED;
            if (damager instanceof Projectile) {
                var shooter = ((Projectile) damager).getShooter();
                if (shooter instanceof  Player) message += ((Player) shooter).getName();
            } else if (damager instanceof Item) {
                var shooter = Items.getInstance().grenadeThrowerMap.get(damager);
                if (shooter instanceof  Player) message += shooter.getName();
            } else if (damager instanceof Player) {
                message += damager.getName();
            }
            return message;
        }
        public String getDamageMessage(Entity damager, Entity guard) {
            var loc = guard.getLocation();
            var message = "" + ChatColor.BOLD + ChatColor.RED + "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "] " + getTypePrefix(guard) + ChatColor.DARK_GREEN + "Guard damaged by " + ChatColor.RED;
            if (damager instanceof Projectile) {
                var shooter = ((Projectile) damager).getShooter();
                if (shooter instanceof  Player) message += ((Player) shooter).getName();
            } else if (damager instanceof Item) {
                var shooter = Items.getInstance().grenadeThrowerMap.get(damager);
                if (shooter instanceof  Player) message += shooter.getName();
            } else if (damager instanceof Player) {
                message += damager.getName();
            }
            var strHealth =String.format("%.2f", getHealth(guard));
            message += " " + ChatColor.DARK_GREEN + "(" + strHealth + " Health)";
            return message;
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
                            ((Player) shooter).sendMessage(ChatColor.RED + "You cannot damage this guard crystal.");
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
                            shooter.sendMessage(ChatColor.RED + "You cannot damage this guard crystal.");
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
                        damager.sendMessage(ChatColor.RED + "You cannot damage this guard crystal.");
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
