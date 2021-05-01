package me.tedwoodworth.diplomacy.Items;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.data.BooleanPersistentDataType;
import me.tedwoodworth.diplomacy.entities.Entities;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.Guis.Guis;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.*;

import static org.bukkit.Material.*;

public class Items {
    private static Items instance = null;
    public final NamespacedKey pickupKey = new NamespacedKey(Diplomacy.getInstance(), "pickup");
    private final ItemStack air = new ItemStack(Material.AIR);
    private final Set<Player> grenadeDropList = new HashSet<>();
    private final Set<Item> itemPickupList = new HashSet<>();
    public final Map<Item, Entity> grenadeThrowerMap = new HashMap<>();

    public final List<Material> helmets = new ArrayList<>();
    public final List<Material> chestplates = new ArrayList<>();
    public final List<Material> leggings = new ArrayList<>();
    public final List<Material> boots = new ArrayList<>();
    public final Set<Material> wooden = new HashSet<>();
    public final Set<Material> stone = new HashSet<>();
    public final Set<Material> iron = new HashSet<>();
    public final Set<Material> gold = new HashSet<>();
    public final Set<Material> diamond = new HashSet<>();
    public final List<Material> planks = new ArrayList<>();
    public final List<Material> wool = new ArrayList<>();
    public final List<Material> tools = new ArrayList<>();

    public static Items getInstance() {
        if (instance == null) {
            instance = new Items();
        }
        return instance;
    }

    public boolean isClassItem(ItemStack itemStack) {
        if (itemStack == null) return false;

        var meta = itemStack.getItemMeta();
        return meta != null
                && meta.getLore() != null
                && meta.getLore().get(0).contains("Prefix:");
    }

    public boolean isGrenade(ItemStack itemStack) {
        if (itemStack == null) return false;
        if (!CustomItemGenerator.getInstance().isCustomItem(itemStack)) return false;
        var id = CustomItemGenerator.getInstance().getCustomID(itemStack);
        var enumID = CustomItems.getInstance().getEnum(id);
        return enumID == CustomItems.CustomID.GRENADE || enumID == CustomItems.CustomID.THROWN_GRENADE;
    }

    public boolean isIngot(ItemStack itemStack) {
        var type = itemStack.getType();
        return type == Material.IRON_INGOT || type == Material.GOLD_INGOT || type == Material.NETHERITE_INGOT;
    }

    public Items() {
        wooden.add(Material.WOODEN_SWORD);
        wooden.add(Material.WOODEN_AXE);
        wooden.add(Material.WOODEN_HOE);
        wooden.add(Material.WOODEN_PICKAXE);
        wooden.add(Material.WOODEN_SHOVEL);

        stone.add(Material.STONE_SWORD);
        stone.add(STONE_AXE);
        stone.add(Material.STONE_HOE);
        stone.add(Material.STONE_PICKAXE);
        stone.add(Material.STONE_SHOVEL);

        iron.add(Material.IRON_SWORD);
        iron.add(Material.IRON_AXE);
        iron.add(Material.IRON_HOE);
        iron.add(Material.IRON_PICKAXE);
        iron.add(Material.IRON_SHOVEL);
        iron.add(Material.IRON_HELMET);
        iron.add(Material.IRON_CHESTPLATE);
        iron.add(Material.IRON_LEGGINGS);
        iron.add(Material.IRON_BOOTS);

        diamond.add(Material.DIAMOND_SWORD);
        diamond.add(DIAMOND_AXE);
        diamond.add(Material.DIAMOND_HOE);
        diamond.add(Material.DIAMOND_PICKAXE);
        diamond.add(Material.DIAMOND_SHOVEL);
        diamond.add(Material.DIAMOND_HELMET);
        diamond.add(Material.DIAMOND_CHESTPLATE);
        diamond.add(Material.DIAMOND_LEGGINGS);
        diamond.add(Material.DIAMOND_BOOTS);

        tools.add(Material.WOODEN_AXE);
        tools.add(STONE_AXE);
        tools.add(Material.IRON_AXE);
        tools.add(GOLDEN_AXE);
        tools.add(DIAMOND_AXE);
        tools.add(Material.NETHERITE_AXE);
        tools.add(Material.WOODEN_SWORD);
        tools.add(Material.STONE_SWORD);
        tools.add(Material.IRON_SWORD);
        tools.add(Material.GOLDEN_SWORD);
        tools.add(Material.DIAMOND_SWORD);
        tools.add(Material.NETHERITE_SWORD);
        tools.add(Material.WOODEN_PICKAXE);
        tools.add(Material.STONE_PICKAXE);
        tools.add(Material.IRON_PICKAXE);
        tools.add(Material.GOLDEN_PICKAXE);
        tools.add(Material.DIAMOND_PICKAXE);
        tools.add(Material.NETHERITE_PICKAXE);
        tools.add(Material.WOODEN_SHOVEL);
        tools.add(Material.STONE_SHOVEL);
        tools.add(Material.IRON_SHOVEL);
        tools.add(Material.GOLDEN_SHOVEL);
        tools.add(Material.DIAMOND_SHOVEL);
        tools.add(Material.NETHERITE_SHOVEL);
        tools.add(Material.TRIDENT);
        tools.add(Material.WOODEN_HOE);
        tools.add(Material.STONE_HOE);
        tools.add(Material.IRON_HOE);
        tools.add(Material.GOLDEN_HOE);
        tools.add(Material.DIAMOND_HOE);
        tools.add(Material.NETHERITE_HOE);

        gold.add(Material.GOLDEN_SWORD);
        gold.add(GOLDEN_AXE);
        gold.add(Material.GOLDEN_HOE);
        gold.add(Material.GOLDEN_PICKAXE);
        gold.add(Material.GOLDEN_SHOVEL);
        gold.add(Material.GOLDEN_HELMET);
        gold.add(Material.GOLDEN_CHESTPLATE);
        gold.add(Material.GOLDEN_LEGGINGS);
        gold.add(Material.GOLDEN_BOOTS);

        planks.add(Material.WARPED_PLANKS);
        planks.add(Material.SPRUCE_PLANKS);
        planks.add(Material.OAK_PLANKS);
        planks.add(Material.JUNGLE_PLANKS);
        planks.add(Material.DARK_OAK_PLANKS);
        planks.add(Material.CRIMSON_PLANKS);
        planks.add(Material.BIRCH_PLANKS);
        planks.add(Material.ACACIA_PLANKS);

        wool.add(Material.WHITE_WOOL);
        wool.add(Material.BLACK_WOOL);
        wool.add(Material.BLUE_WOOL);
        wool.add(Material.BROWN_WOOL);
        wool.add(Material.CYAN_WOOL);
        wool.add(Material.GRAY_WOOL);
        wool.add(Material.GREEN_WOOL);
        wool.add(Material.LIGHT_BLUE_WOOL);
        wool.add(Material.LIGHT_GRAY_WOOL);
        wool.add(Material.LIME_WOOL);
        wool.add(Material.MAGENTA_WOOL);
        wool.add(Material.ORANGE_WOOL);
        wool.add(Material.PINK_WOOL);
        wool.add(Material.PURPLE_WOOL);
        wool.add(Material.RED_WOOL);
        wool.add(Material.YELLOW_WOOL);

        helmets.add(Material.LEATHER_HELMET);
        helmets.add(Material.CHAINMAIL_HELMET);
        helmets.add(Material.IRON_HELMET);
        helmets.add(Material.GOLDEN_HELMET);
        helmets.add(Material.DIAMOND_HELMET);
        helmets.add(Material.NETHERITE_HELMET);
        helmets.add(Material.TURTLE_HELMET);

        chestplates.add(Material.LEATHER_CHESTPLATE);
        chestplates.add(Material.CHAINMAIL_CHESTPLATE);
        chestplates.add(Material.IRON_CHESTPLATE);
        chestplates.add(Material.GOLDEN_CHESTPLATE);
        chestplates.add(Material.DIAMOND_CHESTPLATE);
        chestplates.add(Material.NETHERITE_CHESTPLATE);

        leggings.add(Material.LEATHER_LEGGINGS);
        leggings.add(Material.CHAINMAIL_LEGGINGS);
        leggings.add(Material.IRON_LEGGINGS);
        leggings.add(Material.GOLDEN_LEGGINGS);
        leggings.add(Material.DIAMOND_LEGGINGS);
        leggings.add(Material.NETHERITE_LEGGINGS);

        boots.add(Material.LEATHER_BOOTS);
        boots.add(Material.CHAINMAIL_BOOTS);
        boots.add(Material.IRON_BOOTS);
        boots.add(Material.GOLDEN_BOOTS);
        boots.add(Material.DIAMOND_BOOTS);
        boots.add(Material.NETHERITE_BOOTS);
    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new Items.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler
        private void onPrepareAnvil(PrepareAnvilEvent event) {
            var inventory = event.getInventory();
            inventory.setMaximumRepairCost(9999);
        }

        @EventHandler
        private void onPrepareItemCraft(PrepareItemCraftEvent event) {
            var inventory = event.getInventory();

            var result = inventory.getResult();
            if (result == null) return;

            // suspicious stew
            if (result.getType() == Material.SUSPICIOUS_STEW) {
                inventory.setResult(air);
            }
        }

        private void guardThrowGrenade(Entity guard, Player target) {

        }

        private void throwGrenade(Player player, boolean isOverhand, long explodeTime) {
            if (player.isSneaking() && explodeTime != 0) {
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(),
                        () -> throwGrenade(player, isOverhand, explodeTime - 1), 1L);
                return;
            }
            if (isOverhand)
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 1, 1);
            else
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SNOWBALL_THROW, 0.5f, 2.0f);
            var grenade = CustomItemGenerator.getInstance().getCustomItem(CustomItems.CustomID.THROWN_GRENADE, 1);
            var velocity = player.getEyeLocation().getDirection();
            if (isOverhand) velocity.multiply(2.0);
            else velocity.multiply(0.6);
            var loc = player.getEyeLocation();
            if (isOverhand)
                loc.setY(loc.getY() + 0.33);
            else
                loc.setY(loc.getY() - 0.6);

            velocity.add(player.getVelocity());
            velocity.setY(velocity.getY() + (Math.random() - 0.5) * 0.025);
            velocity.setX(velocity.getX() + (Math.random() - 0.5) * 0.025);
            velocity.setZ(velocity.getZ() + (Math.random() - 0.5) * 0.025);

            var drop = player.getWorld().dropItem(loc, grenade);
            grenadeThrowerMap.put(drop, player);
            drop.setVelocity(velocity);
            drop.setPickupDelay(1000);
            grenadeTick(drop, 0, explodeTime, player);
        }

        private Location getNextLocation(Item item) {
            var location = item.getLocation();
            var velocity = item.getVelocity();
            return new Location(location.getWorld(), location.getX() + velocity.getX(), location.getY() + velocity.getY(), location.getZ() + velocity.getZ());
        }

        private boolean isColliding(Location location, Location nextLocation) {
            var nextBlock = nextLocation.getBlock();
            var nextType = nextBlock.getType();
            return !nextBlock.isLiquid() && !nextBlock.isPassable()
                    && nextType != location.getBlock().getType();
        }

        private boolean entityCollision(Item item, Location location, Location nextLocation, Player player,
                                        long curTime) {
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
                        if (curTime < 10L && entity.equals(player)) continue;
                        var box = entity.getBoundingBox();
                        if (box.contains(x, y, z)) {
                            var bX = Math.min(
                                    Math.abs(x - box.getMinX()),
                                    Math.abs(x - box.getMaxX())
                            );
                            var bY = Math.min(
                                    Math.abs(y - box.getMinY()),
                                    Math.abs(y - box.getMaxY())
                            );
                            var bZ = Math.min(
                                    Math.abs(z - box.getMinZ()),
                                    Math.abs(z - box.getMaxZ())
                            );
                            var b = new double[3];
                            b[0] = bX;
                            b[1] = bY;
                            b[2] = bZ;

                            var vLength = item.getVelocity().length();
                            Arrays.sort(b);
                            if (b[0] == bX) v.setX(-1 * v.getX());
                            else if (b[0] == bY) v.setY(-1 * v.getY());
                            else v.setZ(-1 * v.getZ());


                            if ((entity instanceof LivingEntity && !(entity instanceof Bee))
                                    || entity instanceof EnderCrystal
                                    || entity instanceof ArmorStand) {
                                v.add(entity.getVelocity());
                                v.setX(0.15 * v.getX());
                                v.setY(0.15 * v.getY());
                                v.setZ(0.15 * v.getZ());
                            } else if (
                                    entity instanceof Item
                                            || entity instanceof Explosive
                                            || entity instanceof Projectile
                                            || entity instanceof Bee
                                            || entity instanceof FallingBlock) {
                                var half = v.clone();
                                half.setX(v.getX() / 2);
                                half.setY(v.getY() / 2);
                                half.setZ(v.getZ() / 2);

                                var eV = entity.getVelocity();
                                var half2 = eV.clone();
                                half2.setX(eV.getX() / 2);
                                half2.setY(eV.getY() / 2);
                                half2.setZ(eV.getZ() / 2);
                                v.add(half2);
                                eV.add(half);
                                entity.setVelocity(eV);

                            } else if (
                                    entity instanceof Minecart
                                            || entity instanceof Boat) {
                                v.add(entity.getVelocity());
                            }

                            item.setVelocity(v);

                            if (vLength > 0.05) {
                                item.getWorld().playSound(item.getLocation(), Sound.BLOCK_CHAIN_STEP, 1, 1);
                            }

                            if (entity instanceof LivingEntity && vLength > 0.25) {
                                player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 1);
                                ((LivingEntity) entity).damage(1);
                            }
                            return true;
                        }
                    }
                }
            }
            return false;
        }


        private void grenadeTick(Item item, long curTime, long explodeTime, Player player) {
            if (curTime == explodeTime) {
                var diplomacyChunk = new DiplomacyChunk(item.getLocation().getChunk());
                boolean breakBlocks;
                breakBlocks = diplomacyChunk.getNation() == null;
                item.getWorld().createExplosion(item.getLocation(), 3.85F, false, false, item);
                item.getWorld().createExplosion(item.getLocation(), 0.75F, false, breakBlocks, item);
                grenadeThrowerMap.remove(item);
                item.remove();
                return;
            }
            var location = item.getLocation();
            var location2 = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
            var velocity = item.getVelocity();
            location2.setY(item.getLocation().getY() + 0.25);
            if (curTime % 2 == 0 && curTime > 2) {
                item.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, location2, 1, 0.05, 0.05, 0.05, 0);
            }
            var nextLocation = getNextLocation(item);
            if (location.getBlock().isLiquid()) {
                velocity.setX(velocity.getX() * 0.5);
                velocity.setY(velocity.getY() * 0.5);
                velocity.setY(velocity.getY() * 0.5);
            }

            if (!entityCollision(item, location, nextLocation, player, curTime)) {
                var vLength = velocity.length();

                // X-axis check
                var magnitude = Math.abs(nextLocation.getBlockX() - location.getBlockX());
                if (velocity.getX() > 0
                        && nextLocation.getBlockX() - location.getBlockX() >= 1) {
                    var colliding = true;
                    for (int i = 1; i <= magnitude; i++) {
                        if (item.getWorld().getBlockAt(location.getBlockX() + i, location.getBlockY(), location.getBlockZ()).isPassable()) {
                            colliding = false;
                            break;
                        }
                    }
                    if (colliding)
                        velocity.setX(-0.5 * velocity.getX());
                } else if (velocity.getX() < 0
                        && nextLocation.getBlockX() - location.getBlockX() <= -1) {
                    var colliding = false;
                    for (int i = 1; i <= magnitude; i++) {
                        if (!item.getWorld().getBlockAt(location.getBlockX() - i, location.getBlockY(), location.getBlockZ()).isPassable()) {
                            colliding = true;
                            break;
                        }
                    }
                    if (colliding)
                        velocity.setX(-0.5 * velocity.getX());
                }
                var overallColliding = false;

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
                        velocity.setY(-0.5 * velocity.getY());
                        overallColliding = true;
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
                        velocity.setY(-0.5 * velocity.getY());
                        overallColliding = true;
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
                        velocity.setZ(-0.5 * velocity.getZ());
                        overallColliding = true;
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
                        overallColliding = true;
                        velocity.setZ(-0.5 * velocity.getZ());
                    }
                }
                if (overallColliding) {
                    if (vLength > 0.05)
                        item.getWorld().playSound(item.getLocation(), Sound.BLOCK_CHAIN_STEP, 1, 1);
                    item.setVelocity(velocity);
                }
            }
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> grenadeTick(item, curTime + 1L, explodeTime, player), 1L);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
        private void onEntityDamage(EntityDamageEvent event) {
            var cause = event.getCause();
            var entity = event.getEntity();
            var damage = event.getDamage();
            var reduce = 0.0;
            Entity damager;
            if (event instanceof EntityDamageByEntityEvent)
                damager = ((EntityDamageByEntityEvent) event).getDamager();
            else damager = null;
            if (damager instanceof Player) {
                var player = ((Player) damager);
                var type = player.getEquipment().getItemInMainHand().getType();
                if (type == Material.WOODEN_AXE || type == Material.STONE_AXE || type == Material.IRON_AXE || type == Material.DIAMOND_AXE || type == Material.NETHERITE_AXE) {
                    for (var item : player.getInventory().getContents()) {
                        if (item == null) continue;
                        if (item.getType() == Material.SHIELD) continue;
                        player.setCooldown(item.getType(), 17);
                    }
                }
            }
        }

        @EventHandler
        private void grenadeDrop(PlayerDropItemEvent event) {
            var drop = event.getItemDrop();
            var item = drop.getItemStack();
            if (isGrenade(item) && item.getType() == Material.FIREWORK_STAR) {
                var player = event.getPlayer();
                grenadeDropList.add(player);
                Bukkit.getScheduler().runTaskLater(
                        Diplomacy.getInstance(),
                        () -> grenadeDropList.remove(player),
                        2L);
            }
        }

        @EventHandler
        private void grenadeDrop(InventoryClickEvent event) {
            var item = event.getCurrentItem();
            var player = event.getView().getPlayer();
            var equip = player.getEquipment();
            var item2 = air;
            if (equip != null) item2 = equip.getItemInMainHand();
            if ((isGrenade(item) && item != null && item.getType() == Material.FIREWORK_STAR) || (isGrenade(item2) && item2.getType() == Material.FIREWORK_STAR)) {
                grenadeDropList.add((Player) player);
                Bukkit.getScheduler().runTaskLater(
                        Diplomacy.getInstance(),
                        () -> grenadeDropList.remove(player),
                        2L);
            }
        }

        private void grenadeUse(ItemStack item, Action action, Player player, EquipmentSlot hand) {
            boolean isOverhand = (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK);

            if (!isOverhand && grenadeDropList.contains(player)) {
                grenadeDropList.remove(player);
                return;
            }

            var amount = item.getAmount();

            if (amount > 1) item.setAmount(amount - 1);
            else player.getEquipment().setItem(hand, air);


            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CHAIN_STEP, 1, 1);
            throwGrenade(player, isOverhand, (long) (Math.random() * 40L) + 70L);
        }

        @EventHandler
        private void onItemUse(PlayerInteractEvent event) {
            var item = event.getItem();
            if (item == null) return;
            var action = event.getAction();
            // Grenade
            if (isGrenade(item) && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
                ItemStack finalItem = item;
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> grenadeUse(finalItem, action, event.getPlayer(), event.getHand()), 2L);
                event.setCancelled(true);
                return;
            }
        }

        @EventHandler
        private void onBrew(BrewEvent event) {
            var ingredient = event.getContents().getIngredient();
            var contents = event.getContents().getContents();
            for (var content : contents) {
                // fire potion
                if (content.getItemMeta() instanceof PotionMeta) {
                    var potionType = ((PotionMeta) content.getItemMeta()).getBasePotionData().getType();
                    if (potionType == PotionType.FIRE_RESISTANCE) {
                        event.setCancelled(true);
                    }
                }
            }
        }

        @EventHandler
        private void onInventoryPickupItem(InventoryPickupItemEvent event) {
            var item = event.getItem();
            if (isGrenade(item.getItemStack()) && item.getItemStack().getType() == Material.TNT) {
                event.setCancelled(true);
                return;
            }
        }

        @EventHandler
        private void onEntityPickupItem(EntityPickupItemEvent event) {
            var drop = event.getItem();
            var item = drop.getItemStack();
            if (item == null) return;
            if (isGrenade(item) && item.getType() == Material.TNT) {
                event.setCancelled(true);
                return;
            }
            var entity = event.getEntity();
            if (entity instanceof Player) {
                if (!getAutoPickup((Player) entity) && !((Player) entity).isSneaking()) {
                    event.setCancelled(true);
                    return;
                }
            } else {
                Entities.getInstance().setDropItems(entity, true);
                entity.setRemoveWhenFarAway(false);
            }

            if (itemPickupList.contains(drop)) {
                event.setCancelled(true);
                return;
            }

            if (entity instanceof Player) {
                itemPickupList.add(drop);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> itemPickupList.remove(drop), 2L);
            }
        }

        @EventHandler
        private void onConsume(PlayerItemConsumeEvent event) {
            var item = event.getItem();
            var player = event.getPlayer();
            if (item.getType() == GOLDEN_APPLE && CustomItemGenerator.getInstance().isCustomItem(item)) {
                var amount = item.getAmount();
                if (amount == 1) {
                    player.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
                } else {
                    item.setAmount(amount - 1);
                    player.getEquipment().setItemInMainHand(item);
                }
                event.setCancelled(true);
                var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                var lives = dp.getLives() + 1;
                dp.setLives(lives);
                player.sendMessage(ChatColor.GREEN + "You now have " + lives + " lives");
            }
        }

        @EventHandler
        private void onPlayerInteract(PlayerInteractEvent event) {
            var player = event.getPlayer();
            var hand = event.getHand();
            if (hand == null) return;
            var equip = event.getPlayer().getEquipment();
            if (equip == null) return;
            var item = equip.getItem(hand);
            var type = item.getType();
            var block = event.getClickedBlock();
            if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event.getHand() != EquipmentSlot.OFF_HAND && !event.isBlockInHand()) {
                var lookingAt = getItemLookedAt(player);
                if (lookingAt != null) {
                    var lookingItem = lookingAt.getItemStack();
                    if (isGrenade(lookingItem) && lookingItem.getType() == Material.TNT) return;
                    event.setUseItemInHand(Event.Result.DENY);
                    event.setCancelled(true);

                    if (lookingAt.getPickupDelay() > 0) {
                        event.setCancelled(true);
                        return;
                    }

                    if (itemPickupList.contains(lookingAt)) {
                        event.setCancelled(true);
                        return;
                    }
                    lookingAt.setItemStack(lookingItem);

                    itemPickupList.add(lookingAt);
                    Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> itemPickupList.remove(lookingAt), 2L);

                    var ogAmount = lookingItem.getAmount();

                    if (item.getType() == Material.AIR) {
                        player.getEquipment().setItem(event.getHand(), lookingItem);
                        lookingAt.remove();
                        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3f, (float) (Math.random() * 8));
                        return;
                    } else {
                        var leftover = player.getInventory().addItem(lookingItem).get(0);
                        if (leftover != null && leftover.getAmount() == lookingItem.getAmount()) {
                            player.sendMessage(ChatColor.RED + "You do not have enough room in your inventory to pick up this item.");
                        } else {
                            lookingAt.remove();
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3f, (float) (Math.random() * 8));
                        }
                    }
                    return;
                }
            }
        }
    }

    private Item getItemLookedAt(Player player) {
        var location = player.getEyeLocation().clone();
        var direction = location.getDirection();
        for (int i = 0; i <= 20; i++) {
            var nearby = player.getNearbyEntities(i, i, i);
            for (var entity : nearby) {
                var box = entity.getBoundingBox().clone();
                box.expand(0.15, 0.0, 0.15, 0.15, 1.2, 0.15);
                if (box.contains(location.toVector())) {
                    if (entity instanceof Item) {
                        return ((Item) entity);
                    } else {
                        return null;
                    }
                }
            }
            location.setX(location.getX() + direction.getX() * 0.25);
            location.setY(location.getY() + direction.getY() * 0.25);
            location.setZ(location.getZ() + direction.getZ() * 0.25);
        }
        return null;
    }

    public boolean getAutoPickup(Player player) {
        var container = player.getPersistentDataContainer();
        if (!container.has(pickupKey, BooleanPersistentDataType.instance)) {
            container.set(pickupKey, BooleanPersistentDataType.instance, true);
            return true;
        } else {
            return container.get(pickupKey, BooleanPersistentDataType.instance);
        }

    }

    public void toggleAutoPickup(Player player) {
        var container = player.getPersistentDataContainer();
        if (!container.has(pickupKey, BooleanPersistentDataType.instance)) {
            container.set(pickupKey, BooleanPersistentDataType.instance, true);
        } else {
            container.set(pickupKey, BooleanPersistentDataType.instance, !container.get(pickupKey, BooleanPersistentDataType.instance));
        }

    }
}
