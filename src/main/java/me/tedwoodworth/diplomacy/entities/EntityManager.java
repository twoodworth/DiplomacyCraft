package me.tedwoodworth.diplomacy.entities;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.geology.WorldManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;

import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

public class EntityManager {
    private int randomTickTaskID = -1;
    private static EntityManager instance = null;
    private final NamespacedKey HUNGER_KEY = new NamespacedKey(Diplomacy.getInstance(), "entity_hunger");
    private final NamespacedKey GENDER_KEY = new NamespacedKey(Diplomacy.getInstance(), "entity_gender");
    private final NamespacedKey PREGNANT_KEY = new NamespacedKey(Diplomacy.getInstance(), "entity_pregnant");
    private final NamespacedKey AGE_KEY = new NamespacedKey(Diplomacy.getInstance(), "entity_age");
    private final HashSet<LivingEntity> pregnantSet = new HashSet<>();

    public static EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    private enum Gender {
        MALE,
        FEMALE,
        NEUTRAL
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
                trampleTick(entity, world);
                hungerTick(entity);
                ageTick(entity);
            }
        }
        for (var entity : pregnantSet) {
            var pregnantTime = getPregnantTime(entity);
            var type = entity.getType();
            switch (type) {
                case COW -> {
                    if (pregnantTime > 3600 && Math.random() < 0.0033) {
                        setPregnantTime(entity, -1);
                        var baby = entity.getWorld().spawnEntity(entity.getLocation(), entity.getType());
                        ((Ageable) baby).setBaby();
                        ((Ageable) baby).setAge(-72000);
                        setHungerBars((LivingEntity) baby, 10);
                        ((Mob) baby).setTarget(entity);
                        setGender((LivingEntity) baby, Gender.values()[(int) (Math.random() * 2)]);
                        entity.getWorld().spawn(entity.getLocation(), ExperienceOrb.class).setExperience(1 + (int) (Math.random() + 7));
                        setPregnantTime(entity, -1);
                        pregnantSet.remove(entity);
                    } else if (pregnantTime > 6600) {
                        setPregnantTime(entity, -1);
                        pregnantSet.remove(entity);
                    } else {
                        setPregnantTime(entity, pregnantTime + 1);
                    }
                }
            }
        }
    }

    private void ageTick(LivingEntity entity) {
        incrementAge(entity);
        if (entity instanceof Ageable) {
            var age = ((Ageable) entity).getAge();
            switch (entity.getType()) {
                case COW -> {
                    if (Math.random() < (age - 24000 * 1000) / (24000 * 10000.0)) {
                        entity.damage(1);
                    } else if (age < -48000 && Math.random() < 0.003) {
                        entity.damage(1);
                    }
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

    private void hungerTick(LivingEntity entity) {
        var hunger = getHungerBars(entity);
        var type = entity.getType();
        if (type != EntityType.COW) return;

        if (hunger < 20 && Math.random() < 0.1) {
            switch (type) {
                case COW -> {
                    var cow = ((Cow) entity);
                    if (!cow.isAdult()) {
                        var mother = cow.getTarget();
                        if (mother == null) {
                            var nearby = cow.getNearbyEntities(3, 3, 3)
                                    .stream()
                                    .filter(e -> e instanceof Cow)
                                    .map(e -> (Cow) e)
                                    .filter(Ageable::isAdult)
                                    .filter(Ageable::canBreed)
                                    .filter(e -> getGender(e) == Gender.FEMALE)
                                    .sorted(Comparator.comparingInt(p -> -getHungerBars(p)))
                                    .collect(Collectors.toList());
                            if (!nearby.isEmpty()) {
                                cow.setTarget(nearby.get(0));
                                mother = cow.getTarget();
                            }
                        }
                        if (mother != null && getHungerBars(mother) > 10) {
                            setHungerBars(mother, getHungerBars(mother) - 1);
                            hunger++;
                            setHungerBars(cow, hunger);
                            cow.getWorld().playSound(cow.getLocation(), Sound.ENTITY_GENERIC_DRINK, 0.1f, 1);
                        }
                    } else {
                        var nearby = entity.getNearbyEntities(3, 3, 3);
                        var itemFed = false;
                        for (var e : nearby) {
                            if (e instanceof Item) {
                                var item = (Item) e;
                                var stack = item.getItemStack();
                                if (stack.getType() == Material.WHEAT || stack.getType() == Material.GRASS || stack.getType() == Material.TALL_GRASS) {
                                    var amount = stack.getAmount();
                                    item.getWorld().playSound(item.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.3f, 1.5f);
                                    var loc = entity.getLocation();
                                    loc.setPitch(70);
                                    if (amount == 1) {
                                        item.remove();
                                    } else {
                                        stack.setAmount(stack.getAmount() - 1);
                                        item.setItemStack(stack);
                                    }
                                    setHungerBars(entity, hunger + 1);
                                    itemFed = true;
                                    break;
                                }
                            }
                        }
                        if (itemFed) break;

                        var block = entity.getLocation().getBlock();
                        var d = block.getRelative(BlockFace.DOWN);

                        var blocks = new HashSet<Block>();
                        blocks.add(block);
                        blocks.add(d);
                        blocks.add(block.getRelative(BlockFace.NORTH));
                        blocks.add(block.getRelative(BlockFace.NORTH_EAST));
                        blocks.add(block.getRelative(BlockFace.EAST));
                        blocks.add(block.getRelative(BlockFace.SOUTH_EAST));
                        blocks.add(block.getRelative(BlockFace.SOUTH));
                        blocks.add(block.getRelative(BlockFace.SOUTH_WEST));
                        blocks.add(block.getRelative(BlockFace.WEST));
                        blocks.add(block.getRelative(BlockFace.NORTH_WEST));
                        blocks.add(d.getRelative(BlockFace.NORTH));
                        blocks.add(d.getRelative(BlockFace.NORTH_EAST));
                        blocks.add(d.getRelative(BlockFace.EAST));
                        blocks.add(d.getRelative(BlockFace.SOUTH_EAST));
                        blocks.add(d.getRelative(BlockFace.SOUTH));
                        blocks.add(d.getRelative(BlockFace.SOUTH_WEST));
                        blocks.add(d.getRelative(BlockFace.WEST));
                        blocks.add(d.getRelative(BlockFace.NORTH_WEST));

                        for (var b : blocks) {
                            if (b.getType() == Material.GRASS || b.getType() == Material.TALL_GRASS) {
                                b.setType(Material.AIR);
                                setHungerBars(entity, hunger + 1);
                                b.getWorld().playSound(b.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.3f, 1.15f);
                                break;
                            }
                            if (b.getType() == Material.GRASS_BLOCK) {
                                var u = b.getRelative(BlockFace.UP);
                                if (u.getType() == Material.GRASS || u.getType() == Material.TALL_GRASS) {
                                    u.setType(Material.AIR);
                                } else {
                                    b.setType(Material.DIRT);
                                }
                                b.getWorld().playSound(b.getLocation(), Sound.BLOCK_GRASS_BREAK, 0.3f, 1.15f);
                                setHungerBars(entity, hunger + 1);
                                break;
                            }

                        }
                    }
                }
            }
        }
        var health = entity.getHealth();
        var max = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        if (hunger > 5 && health < max && Math.random() < 0.004) {
            entity.setHealth(Math.min(entity.getHealth() + 1, max));
            setHungerBars(entity, hunger - 1);
            hunger--;
        }
        if ((isPregnant(entity) && Math.random() < 0.015) || Math.random() < 0.005) {
            if (hunger == 0) {
                if (Math.random() < 0.02) {
                    entity.damage(1);
                }
            } else {
                setHungerBars(entity, hunger - 1);
                hunger--;
            }
        }

        var cow = (Cow) entity;
        if (cow.isAdult() && cow.canBreed() && getGender(cow) == Gender.MALE && hunger > 5 && cow.getAge() < 24000000) {
            var nearby = cow.getNearbyEntities(3, 3, 3)
                    .stream()
                    .filter(e -> e instanceof Cow)
                    .map(e -> (Cow) e)
                    .filter(Ageable::isAdult)
                    .filter(Ageable::canBreed)
                    .filter(e -> !isPregnant(e))
                    .filter(e -> e.getAge() < 4800000)
                    .filter(e -> getGender(e) == Gender.FEMALE)
                    .filter(e -> getHungerBars(e) > 15)
                    .sorted(Comparator.comparingDouble(e -> -cow.getHealth()))
                    .collect(Collectors.toList());

            if (!nearby.isEmpty()) {
                var target = nearby.get(0);
                cow.setLoveModeTicks(100);
                cow.setTarget(target);
                target.setLoveModeTicks(100);
            }
        }
    }

    private void setHungerBars(LivingEntity entity, int hunger) {
        var container = entity.getPersistentDataContainer();
        container.set(HUNGER_KEY, PersistentDataType.INTEGER, hunger);
    }

    private int getHungerBars(LivingEntity entity) {
        var container = entity.getPersistentDataContainer();
        if (!container.has(HUNGER_KEY, PersistentDataType.INTEGER)) {
            container.set(HUNGER_KEY, PersistentDataType.INTEGER, 20);
            return 20;
        } else {
            return container.get(HUNGER_KEY, PersistentDataType.INTEGER);
        }
    }

    private Gender getGender(LivingEntity entity) {
        var container = entity.getPersistentDataContainer();
        if (!container.has(GENDER_KEY, PersistentDataType.BYTE)) {
            var b = (byte) (Math.random() * 2);
            container.set(GENDER_KEY, PersistentDataType.BYTE, b);
            return Gender.values()[b];
        } else {
            var b = container.get(GENDER_KEY, PersistentDataType.BYTE);
            return Gender.values()[b];
        }
    }

    private void setGender(LivingEntity entity, Gender gender) {
        var container = entity.getPersistentDataContainer();
        container.set(GENDER_KEY, PersistentDataType.BYTE, (byte) gender.ordinal());
    }

    private void setPregnantTime(LivingEntity entity, int time) {
        var container = entity.getPersistentDataContainer();
        container.set(PREGNANT_KEY, PersistentDataType.INTEGER, time);
    }

    private int getPregnantTime(LivingEntity entity) {
        var container = entity.getPersistentDataContainer();
        return container.get(PREGNANT_KEY, PersistentDataType.INTEGER);
    }

    private boolean isPregnant(LivingEntity entity) {
        var container = entity.getPersistentDataContainer();
        if (!container.has(PREGNANT_KEY, PersistentDataType.INTEGER)) {
            return false;
        }
        return getPregnantTime(entity) > -1;
    }

    private long getAge(LivingEntity entity) {
        if ((entity instanceof Ageable) && !((Ageable) entity).isAdult()) {
            return (long) ((Ageable) entity).getAge() + 72000L;
        } else {
            var container = entity.getPersistentDataContainer();
            if (!container.has(AGE_KEY, PersistentDataType.LONG)) {
                container.set(AGE_KEY, PersistentDataType.LONG, 0L);
            }
            return container.get(AGE_KEY, PersistentDataType.LONG);
        }
    }

    private void incrementAge(LivingEntity entity) {
        if (!(entity instanceof Ageable) || ((Ageable) entity).isAdult()) {
            var container = entity.getPersistentDataContainer();
            if (!container.has(AGE_KEY, PersistentDataType.LONG)) {
                if (entity instanceof Ageable) {
                    container.set(AGE_KEY, PersistentDataType.LONG, 72000L);
                } else {
                    container.set(AGE_KEY, PersistentDataType.LONG, 0L);
                }
            } else {
                container.set(AGE_KEY, PersistentDataType.LONG, getAge(entity) + 20L);
            }
        }
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EntityManager.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onInteract(PlayerInteractEntityEvent event) {
            var entity = event.getRightClicked();
            var hand = event.getHand();
            if (entity instanceof LivingEntity && event.getHand() == EquipmentSlot.HAND) {
                var living = (LivingEntity) entity;
                var type = living.getType();
                if (type == EntityType.COW) {
                    var player = event.getPlayer();
                    var equipment = player.getEquipment();
                    if (equipment != null) {
                        var item = equipment.getItem(hand);
                        if (item.getType() == Material.WHEAT) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.RED + "To feed cows, drop wheat or grass on the ground for them to eat. Cows will also" +
                                    "naturally eat placed grass, tall grass, and grass from grass blocks.");
                            return;
                        }
                    }
                    var gender = getGender(living);
                    var health = living.getHealth();
                    var hunger = getHungerBars(living);

                    String strGender;
                    if (gender == Gender.FEMALE) strGender = ChatColor.LIGHT_PURPLE + "Female";
                    else if (gender == Gender.MALE) strGender = ChatColor.BLUE + "Male";
                    else strGender = ChatColor.GRAY + "Neutral";
                    var strAge = String.format("%.2f", getAge(living) / (20 * 60.0 * 20)) + " MC Days";
                    String name;
                    if (entity.isCustomNameVisible()) {
                        name = entity.getCustomName();
                    } else {
                        name = switch (entity.getType()) {
                            case COW -> "Cow";
                            default -> entity.getType().name();
                        };
                    }
                    player.sendMessage("");
                    player.sendMessage("" + ChatColor.YELLOW + ChatColor.BOLD + name + " Stats");
                    if (isPregnant(living)) {
                        var pregTime = getPregnantTime(living);
                        var strPregTime = String.format("%.2f", pregTime / (60.0 * 20)) + " Days Pregnant";
                        player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + strPregTime);
                    }
                    player.sendMessage(ChatColor.YELLOW + "Age: " + ChatColor.WHITE + strAge);
                    player.sendMessage(ChatColor.YELLOW + "Gender: " + strGender);
                    player.sendMessage(ChatColor.YELLOW + "Hunger: " + ChatColor.WHITE + hunger + "/20");
                    player.sendMessage(ChatColor.YELLOW + "Health: " + ChatColor.WHITE + String.format("%.2f", health) + " HP");
                }
            }
        }

        @EventHandler
        private void onBreed(EntityBreedEvent event) {
            var father = event.getFather();
            var mother = event.getMother();
            if (getGender(father) == Gender.FEMALE) {
                father = mother;
                mother = event.getFather();
            }
            setHungerBars(father, getHungerBars(father) - 1);
            setHungerBars(mother, getHungerBars(mother) - 1);
            setPregnantTime(mother, 0);
            pregnantSet.add(mother);
            event.setCancelled(true);
            mother.getWorld().spawnParticle(Particle.HEART, mother.getEyeLocation(), 1);
        }

        @EventHandler
        private void onChunkLoad(ChunkLoadEvent event) {
            var entities = event.getChunk().getEntities();
            for (var entity : entities) {
                if (entity instanceof LivingEntity) {
                    var living = (LivingEntity) entity;
                    if (isPregnant(living)) {
                        System.out.println("Pregnant entity added to set");
                        pregnantSet.add(living);
                    }
                }
            }
        }

        @EventHandler
        private void onChunkUnload(ChunkUnloadEvent event) {
            var chunk = event.getChunk();
            for (var entity : chunk.getEntities()) {
                if (entity instanceof LivingEntity) {
                    var living = (LivingEntity) entity;
                    pregnantSet.remove(living);
                }
            }
        }

        @EventHandler
        private void onDeath(EntityDeathEvent event) {
            var entity = event.getEntity();
            if (isPregnant(entity)) {
                pregnantSet.remove(entity);
            }
        }
    }

    public void loadInitialPregnant() {
        for (var chunk : WorldManager.getInstance().getOverworld().getForceLoadedChunks()) {
            var entities = chunk.getEntities();
            for (var entity : entities) {
                if (entity instanceof LivingEntity) {
                    var living = (LivingEntity) entity;
                    if (isPregnant(living)) {
                        System.out.println("Pregnant entity added to set");
                        pregnantSet.add(living);
                    }
                }
            }
        }
        for (var chunk : WorldManager.getInstance().getOverworld().getLoadedChunks()) {
            var entities = chunk.getEntities();
            for (var entity : entities) {
                if (entity instanceof LivingEntity) {
                    var living = (LivingEntity) entity;
                    if (isPregnant(living)) {
                        System.out.println("Pregnant entity added to set");
                        pregnantSet.add(living);
                    }
                }
            }
        }
    }

}
