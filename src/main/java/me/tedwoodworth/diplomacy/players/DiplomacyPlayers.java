package me.tedwoodworth.diplomacy.players;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Items.Items;
import me.tedwoodworth.diplomacy.geology.WorldManager;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.guards.GuardManager;
import me.tedwoodworth.diplomacy.nations.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DiplomacyPlayers {

    private static DiplomacyPlayers instance = null;
    private final File diplomacyPlayerConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "diplomacyPlayers.yml");
    private final Map<UUID, DiplomacyPlayer> diplomacyPlayers = new WeakHashMap<>();
    private final YamlConfiguration config;
    public final HashMap<Player, Integer> combatLogged = new HashMap<>();
    public final HashMap<Player, Integer> teleportMap = new HashMap<>();
    public final HashMap<Block, BlockState> griefedBlocks = new HashMap<>();
    public final HashSet<HashMap<Block, BlockState>> explodedBlocks = new HashSet<>();

    public static DiplomacyPlayers getInstance() {
        if (instance == null) {
            instance = new DiplomacyPlayers();
        }
        return instance;
    }

    private DiplomacyPlayers() {
        config = YamlConfiguration.loadConfiguration(diplomacyPlayerConfigFile);
        save();
    }

    public DiplomacyPlayer get(UUID uuid) {
        var player = diplomacyPlayers.get(uuid);
        if (player == null) {
            if (config.getConfigurationSection(uuid.toString()) == null) {
                List<String> groups = new ArrayList<>(1);
                List<String> groupsLed = new ArrayList<>(1);
                Map<String, Object> playersMap = ImmutableMap.of(
                        "Groups", groups,
                        "GroupsLed", groupsLed,
                        "Lives", 20,
                        "JoinedToday", true,
                        "CanTeleport", true
                );
                config.createSection(uuid.toString(), playersMap);
            }
            player = new DiplomacyPlayer(uuid, config.getConfigurationSection(uuid.toString()));
            diplomacyPlayers.put(uuid, player);
        }
        return player;
    }

    public @Nullable DiplomacyPlayer get(String strPlayer) {
        for (var player : this.getPlayers()) {
            var name = player.getOfflinePlayer().getName();
            if (name == null) {
                continue;
            }
            if (name.equalsIgnoreCase(strPlayer)) {
                return player;
            }
        }
        return null;
    }

    public List<DiplomacyPlayer> getPlayers() {
        var players = new ArrayList<DiplomacyPlayer>();

        var strUUIDs = config.getKeys(false);
        for (var strUUID : strUUIDs) {
            var uuid = UUID.fromString(strUUID);
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(uuid);
            players.add(diplomacyPlayer);
        }
        return players;
    }

    public List<DiplomacyPlayer> getLeaders(DiplomacyGroup group) {
        List<DiplomacyPlayer> leaders = new ArrayList<>();

        for (var player : diplomacyPlayers.values()) {
            if (player.getGroupsLed().contains(group)) {
                leaders.add(player);
            }
        }
        return leaders;
    }

    public List<DiplomacyPlayer> getMembers(DiplomacyGroup group) {
        List<DiplomacyPlayer> members = new ArrayList<>();

        for (var player : diplomacyPlayers.values()) {
            if (player.getGroups().contains(group)) {
                members.add(player);
            }
        }
        return members;
    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new EventListener(), Diplomacy.getInstance());
    }

    public void save() {
        try {
            config.save(diplomacyPlayerConfigFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean canBuildHere(Block block, Entity entity, Material itemUsed) {
        var chunk = block.getChunk();
        var world = chunk.getWorld();
        var wm = WorldManager.getInstance();
        if (world.equals(wm.getEnd()) || world.equals(wm.getNether())) {
            return true;
        } else if (world.equals(wm.getSubworld())) {
            if (block.getLocation().getY() < 119) {
                return true;
            } else {
                var x = chunk.getX();
                var z = chunk.getZ();
                chunk = wm.getOverworld().getChunkAt(x, z);
            }
        } else if (world.equals(wm.getSpawn()) && (!(entity instanceof Player) || ((Player) entity).getGameMode() != GameMode.CREATIVE)) {
            return false;
        }

        // Return true if there is no nation
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        var nation = diplomacyChunk.getNation();
        if (nation == null) return true;

        // return false if it is not a player
        if (!(entity instanceof Player)) return false;

        var player = (Player) entity;

        // return true if there is a group and the player is in it
        var group = diplomacyChunk.getGroup();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        if (group != null && group.getMembers().contains(diplomacyPlayer)) return true;


        // return false if the player is not part of the group (checked above) or nation
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        if (!Objects.equals(nation, playerNation)) return false;

        // check if player has nation build permissions
        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canBuildAnywhere = permissions.get("CanBuildAnywhere");
        return canBuildAnywhere;
    }

    public boolean canPlaceGuards(Chunk chunk, Player player) {

        // Return false if there is no nation
        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
        var nation = diplomacyChunk.getNation();
        if (nation == null) return true;

        // return true if there is a group and the player is in it
        var group = diplomacyChunk.getGroup();
        var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
        if (group != null && group.getMembers().contains(diplomacyPlayer)) return true;


        // return false if the player is not part of the group (checked above) or nation
        var playerNation = Nations.getInstance().get(diplomacyPlayer);
        if (!Objects.equals(nation, playerNation)) return false;

        // check if player has nation build permissions
        var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
        var canBuildAnywhere = permissions.get("CanBuildAnywhere");
        if (canBuildAnywhere) return true;
        else return false;
    }

    private class EventListener implements Listener {

        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            for (var player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().equals(WorldManager.getInstance().getSpawn()) && player.getHealth() > 0.0) {
                    var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                    dp.setLastLocation(player.getLocation());
                }
            }
            save();
        }

        @EventHandler
        void onPluginDisable(PluginDisableEvent event) {
            for (var player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().equals(WorldManager.getInstance().getSpawn()) && player.getHealth() > 0.0) {
                    var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                    dp.setLastLocation(player.getLocation());
                }
            }
            if (event.getPlugin().equals(Diplomacy.getInstance())) {
                save();
            }
        }

        @EventHandler
        private void onPlayerItemMend(PlayerItemMendEvent event) {
            var item = event.getItem();
            item.removeEnchantment(Enchantment.MENDING);
            event.setCancelled(true);
        }

        @EventHandler
        private void onBlockIgnite(BlockIgniteEvent event) {
            var toBlock = event.getBlock();
            var to = toBlock.getLocation();
            var toChunk = to.getChunk();
            var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(toChunk).getNation();
            if (toNation == null) return;

            var fromBlock = event.getIgnitingBlock();
            if (fromBlock == null) return;
            var from = fromBlock.getLocation();
            var fromChunk = from.getChunk();
            var fromNation = DiplomacyChunks.getInstance().getDiplomacyChunk(fromChunk).getNation();
            if (!Objects.equals(fromNation, toNation))
                event.setCancelled(true);
        }

        @EventHandler
        private void onBlockSpread(BlockSpreadEvent event) {
            var type = event.getSource().getType();
            if (type != Material.FIRE && type != Material.LAVA) return;
            var toBlock = event.getBlock();
            var to = toBlock.getLocation();
            var toChunk = to.getChunk();
            var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(toChunk).getNation();
            if (toNation == null) return;

            var fromBlock = event.getSource();
            var from = fromBlock.getLocation();
            var fromChunk = from.getChunk();
            var fromNation = DiplomacyChunks.getInstance().getDiplomacyChunk(fromChunk).getNation();
            if (!Objects.equals(fromNation, toNation))
                event.setCancelled(true);
        }

        @EventHandler
        private void onInteract(EntityDamageByEntityEvent event) {
            var entity = event.getDamager();
            if (!(entity instanceof Player)) return;
            var player = ((Player) entity);
            var equipment = player.getEquipment();
            if (equipment == null) return;
            var item = equipment.getItemInMainHand();
            if (player.hasCooldown(item.getType())) {
                event.setCancelled(true);
            }
        }

        @EventHandler
        private void onBlockBurn(BlockBurnEvent event) {
            var block = event.getBlock().getLocation();
            var chunk = block.getChunk();

            var adj = new Location(block.getWorld(), block.getX() + 1, block.getY(), block.getZ());
            var adjChunk = adj.getChunk();
            if (!chunk.equals(adjChunk)) {
                var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk).getNation();
                var adjNation = DiplomacyChunks.getInstance().getDiplomacyChunk(adjChunk).getNation();
                if (toNation != null && !Objects.equals(toNation, adjNation)) {
                    event.setCancelled(true);
                    return;
                }
            }

            adj = new Location(block.getWorld(), block.getX() - 1, block.getY(), block.getZ());
            adjChunk = adj.getChunk();
            if (!chunk.equals(adjChunk)) {
                var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk).getNation();
                var adjNation = DiplomacyChunks.getInstance().getDiplomacyChunk(adjChunk).getNation();
                if (toNation != null && !Objects.equals(toNation, adjNation)) {
                    event.setCancelled(true);
                    return;
                }
            }

            adj = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() + 1);
            adjChunk = adj.getChunk();
            if (!chunk.equals(adjChunk)) {
                var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk).getNation();
                var adjNation = DiplomacyChunks.getInstance().getDiplomacyChunk(adjChunk).getNation();
                if (toNation != null && !Objects.equals(toNation, adjNation)) {
                    event.setCancelled(true);
                    return;
                }
            }

            adj = new Location(block.getWorld(), block.getX(), block.getY(), block.getZ() - 1);
            adjChunk = adj.getChunk();
            if (!chunk.equals(adjChunk)) {
                var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk).getNation();
                var adjNation = DiplomacyChunks.getInstance().getDiplomacyChunk(adjChunk).getNation();
                if (toNation != null && !Objects.equals(toNation, adjNation)) {
                    event.setCancelled(true);
                }
            }
        }

        @EventHandler
        private void onBlockFromTo(BlockFromToEvent event) {
            if (event.getBlock().getType().equals(Material.DRAGON_EGG)) return;
            var to = event.getToBlock().getLocation();
            var toChunk = to.getChunk();

            var adj = new Location(to.getWorld(), to.getX() + 1, to.getY(), to.getZ());
            var adjChunk = adj.getChunk();
            if (!toChunk.equals(adjChunk)) {
                var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(toChunk).getNation();
                var adjNation = DiplomacyChunks.getInstance().getDiplomacyChunk(adjChunk).getNation();
                if (toNation != null && !Objects.equals(toNation, adjNation)) {
                    event.setCancelled(true);
                    return;
                }
            }

            adj = new Location(to.getWorld(), to.getX() - 1, to.getY(), to.getZ());
            adjChunk = adj.getChunk();
            if (!toChunk.equals(adjChunk)) {
                var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(toChunk).getNation();
                var adjNation = DiplomacyChunks.getInstance().getDiplomacyChunk(adjChunk).getNation();
                if (toNation != null && !Objects.equals(toNation, adjNation)) {
                    event.setCancelled(true);
                    return;
                }
            }

            adj = new Location(to.getWorld(), to.getX(), to.getY(), to.getZ() + 1);
            adjChunk = adj.getChunk();
            if (!toChunk.equals(adjChunk)) {
                var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(toChunk).getNation();
                var adjNation = DiplomacyChunks.getInstance().getDiplomacyChunk(adjChunk).getNation();
                if (toNation != null && !Objects.equals(toNation, adjNation)) {
                    event.setCancelled(true);
                    return;
                }
            }

            adj = new Location(to.getWorld(), to.getX(), to.getY(), to.getZ() - 1);
            adjChunk = adj.getChunk();
            if (!toChunk.equals(adjChunk)) {
                var toNation = DiplomacyChunks.getInstance().getDiplomacyChunk(toChunk).getNation();
                var adjNation = DiplomacyChunks.getInstance().getDiplomacyChunk(adjChunk).getNation();
                if (toNation != null && !Objects.equals(toNation, adjNation)) {
                    event.setCancelled(true);
                }
            }
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        private void onPlayerMove(PlayerMoveEvent event) {
            var player = event.getPlayer();
            var from = event.getFrom();
            var to = event.getTo();

            if (teleportMap.containsKey(player) && to != null && ((!Objects.equals(from.getWorld(), to.getWorld())) || (from.distanceSquared(to) > 0.01))) {
                player.sendMessage(ChatColor.RED + "Teleport to spawn cancelled.");
                teleportMap.remove(player);
            }

            var toY = to.getY();
            var toWorld = to.getWorld();
            if (Objects.equals(toWorld, WorldManager.getInstance().getOverworld()) && toY < 4) {
                var subword = WorldManager.getInstance().getSubworld();
                var velocity = player.getVelocity();
                var chunk = to.getChunk();
                var yaw = to.getYaw();
                var pitch = to.getPitch();
                var nLoc = new Location(subword, to.getX(), toY + 118, to.getZ(), yaw, pitch);
                player.teleport(nLoc);
                if (player.getLocation().distanceSquared(nLoc) > 2.0) {
                    player.teleport(nLoc);
                }
                player.setVelocity(velocity);
                player.sendTitle(ChatColor.GRAY + "Subworld", ChatColor.GRAY + "Travel back upwards to return to the overworld", 10, 60, 10);
                return;
            } else if (Objects.equals(toWorld, WorldManager.getInstance().getSubworld()) && toY >= 122.2) {
                var world = WorldManager.getInstance().getOverworld();
                var velocity = player.getVelocity();
                var chunk = to.getChunk();
                var yaw = to.getYaw();
                var pitch = to.getPitch();
                var nLoc = new Location(world, to.getX(), toY - 118, to.getZ(), yaw, pitch);
                player.teleport(nLoc);
                if (player.getLocation().distanceSquared(nLoc) > 2.0) {
                    player.teleport(nLoc);
                }
                player.setVelocity(velocity);
                player.sendTitle(ChatColor.GREEN + "Overworld", ChatColor.GREEN + "Travel back downwards to return to the subworld", 10, 60, 10);
                to = player.getLocation();
            } else if (Objects.equals(toWorld, WorldManager.getInstance().getSubworld()) && toY < 4) {
                var world = WorldManager.getInstance().getNether();
                var velocity = player.getVelocity();
                var chunk = to.getChunk();
                var yaw = to.getYaw();
                var pitch = to.getPitch();
                var nLoc = new Location(world, to.getX(), toY + 153, to.getZ(), yaw, pitch);
                player.teleport(nLoc);
                if (player.getLocation().distanceSquared(nLoc) > 2.0) {
                    player.teleport(nLoc);
                }
                player.setVelocity(velocity);
                player.sendTitle(ChatColor.RED + "Nether", ChatColor.RED + "Travel back upwards to return to the subworld", 10, 60, 10);
                return;
            } else if (Objects.equals(toWorld, WorldManager.getInstance().getNether()) && toY >= 157.2) {
                var world = WorldManager.getInstance().getSubworld();
                var velocity = player.getVelocity();
                var chunk = to.getChunk();
                var yaw = to.getYaw();
                var pitch = to.getPitch();
                player.teleport(new Location(world, to.getX(), toY - 153, to.getZ(), yaw, pitch));
                player.teleport(new Location(world, to.getX(), toY - 153, to.getZ(), yaw, pitch));
                player.setVelocity(velocity);
                player.sendTitle(ChatColor.GRAY + "Subworld", ChatColor.GRAY + "Travel downwards to return to the nether, or upwards to return to the overworld", 10, 60, 10);
                return;
            }


            var fromChunk = event.getFrom().getChunk();
            var toChunk = to.getChunk();
            if (!fromChunk.equals(toChunk)) {
                var fromDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(fromChunk);
                var toDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(toChunk);

                var fromNation = fromDiplomacyChunk.getNation();
                var toNation = toDiplomacyChunk.getNation();

                var fromGroup = fromDiplomacyChunk.getGroup();
                var toGroup = toDiplomacyChunk.getGroup();

                if (!Objects.equals(fromNation, toNation) || !Objects.equals(fromGroup, toGroup)) {
                    if (toNation == null) {
                        player.sendTitle(ChatColor.GRAY + "Wilderness", " ", 5, 30, 5);
                    } else {
                        DiplomacyPlayer diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
                        Nation nation = Nations.getInstance().get(diplomacyPlayer);
                        var toGroupName = "";
                        if (toGroup != null) {
                            toGroupName = toGroup.getName();
                        }
                        if (nation == null) {
                            player.sendTitle(ChatColor.BLUE + toNation.getName(), ChatColor.BLUE + toGroupName, 5, 40, 10);
                        } else if (toNation.getEnemyNationIDs().contains(nation.getNationID())) {
                            player.sendTitle(ChatColor.RED + toNation.getName(), ChatColor.RED + toGroupName, 5, 40, 10);
                        } else if (toNation.getAllyNationIDs().contains(nation.getNationID()) || toNation.equals(nation)) {
                            player.sendTitle(ChatColor.GREEN + toNation.getName(), ChatColor.GREEN + toGroupName, 5, 40, 10);
                        } else {
                            player.sendTitle(ChatColor.BLUE + toNation.getName(), ChatColor.BLUE + toGroupName, 5, 40, 10);
                        }
                    }
                }
            }
        }

        @EventHandler(ignoreCancelled = true)
        private void onBlockPlaceEvent(BlockPlaceEvent event) {
            var block = event.getBlock();
            var player = event.getPlayer();
            if (canBuildHere(block, player, null)) return;

            player.sendMessage(ChatColor.RED + "You cannot build here.");
            event.setCancelled(true);
        }

        @EventHandler
        private void onPlayerBucketFill(PlayerBucketFillEvent event) {
            var block = event.getBlock();

            var player = event.getPlayer();
            var bucket = event.getBucket();
            if (canBuildHere(block, player, bucket)) return;

            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
            event.setCancelled(true);
        }

        @EventHandler
        private void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
            var block = event.getBlock();
            var player = event.getPlayer();
            var bucket = event.getBucket();
            if (canBuildHere(block, player, bucket)) return;

            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
            event.setCancelled(true);
        }

        @EventHandler(ignoreCancelled = true)
        private void onPlayerInteract(PlayerInteractEvent event) {
            if (event.getClickedBlock() == null) {
                return;
            }

            var block = event.getClickedBlock();
            var action = event.getAction();

            switch (action) {
                case RIGHT_CLICK_BLOCK -> {
                    var type = block.getType();
                    switch (type) {
                        case RESPAWN_ANCHOR -> {
                            if (block.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

                            var player = event.getPlayer();
                            if (canBuildHere(block, player, null)) return;

                            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                            event.setCancelled(true);
                            return;
                        }
                        case ITEM_FRAME, COMPOSTER -> {
                            var player = event.getPlayer();
                            if (canBuildHere(block, player, null)) return;

                            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                            event.setCancelled(true);
                            return;
                        }
                        case BLACK_BED, BLUE_BED, BROWN_BED, CYAN_BED, GRAY_BED, GREEN_BED, LIGHT_BLUE_BED, LIME_BED, MAGENTA_BED, ORANGE_BED, PINK_BED,
                                PURPLE_BED, RED_BED, WHITE_BED, YELLOW_BED, LIGHT_GRAY_BED -> {
                            if (block.getWorld().getEnvironment().equals(World.Environment.NORMAL)) return;

                            var player = event.getPlayer();
                            if (canBuildHere(block, player, null)) return;

                            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
                case PHYSICAL -> {
                    if (block.getType() == Material.FARMLAND) {
                        var player = event.getPlayer();
                        if (canBuildHere(block, player, null)) return;

                        player.sendMessage(ChatColor.RED + "You cannot trample farmland here.");
                        event.setCancelled(true);
                        return;
                    }

                }
                default -> {
                    var item = event.getItem();
                    if (item == null) return;
                    var itemType = item.getType();
                    switch (itemType) {
                        case FLINT_AND_STEEL, FIRE_CHARGE, END_CRYSTAL, PAINTING, ITEM_FRAME -> {
                            var player = event.getPlayer();
                            if (canBuildHere(block, player, null)) return;

                            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

            var entity = event.getRightClicked();
            var chunk = entity.getLocation().getChunk();
            var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
            var nation = diplomacyChunk.getNation();

            if (nation == null) {
                return;
            }

            var group = DiplomacyGroups.getInstance().get(diplomacyChunk);
            var player = event.getPlayer();
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());

            // if there is a group and the player is part of it
            if (group != null && group.getMembers().contains(diplomacyPlayer)) return;

            // if there isn't a group and the player can build anywhere, and its the player's nation
            var playerNation = Nations.getInstance().get(diplomacyPlayer);
            if (Objects.equals(nation, playerNation)) {
                var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
                var canBuildAnywhere = permissions.get("CanBuildAnywhere");
                if (group == null && canBuildAnywhere && Objects.equals(nation, playerNation)) {
                    return;
                }
            }

            if (event.getRightClicked() instanceof ItemFrame || event.getRightClicked() instanceof Painting) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                event.setCancelled(true);
            }
        }

        private void removeCombat(Player player) {
            if (combatLogged.containsKey(player)) {
                var count = combatLogged.get(player);
                if (count == 1) {
                    combatLogged.remove(player);
                    player.sendMessage(ChatColor.RED + "You are no longer in combat.");
                } else {
                    combatLogged.replace(player, count - 1);
                }
            }
        }

        @EventHandler(ignoreCancelled = true)
        private void onDamageByEntity(EntityDamageByEntityEvent event) {
            var damager = event.getDamager();
            var damaged = event.getEntity();
            var trueDamager = GuardManager.getInstance().getTrueDamager(damager);
            if (!(trueDamager instanceof Player && damaged instanceof Player)) {
                return;
            }
            {
                var player = ((Player) damaged);

                if (teleportMap.containsKey(player)) {
                    player.sendMessage(ChatColor.RED + "Teleport to spawn cancelled.");
                    teleportMap.remove(player);
                }

                if (combatLogged.containsKey(player)) {
                    var count = combatLogged.get(player);
                    combatLogged.replace(player, count + 1);
                } else {
                    combatLogged.put(player, 1);
                    player.sendMessage(ChatColor.RED + "You are now in combat. You will be killed if you attempt to quit.");
                    player.sendMessage(ChatColor.RED + "Avoid damage for 15 seconds to leave combat.");
                }
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> removeCombat(player), 300L);
            }
            var player = ((Player) trueDamager);
            if (teleportMap.containsKey(player)) {
                player.sendMessage(ChatColor.RED + "Teleport to spawn cancelled.");
                teleportMap.remove(player);
            }
            if (combatLogged.containsKey(player)) {
                var count = combatLogged.get(player);
                combatLogged.replace(player, count + 1);
            } else {
                combatLogged.put(player, 1);
                player.sendMessage(ChatColor.RED + "You are now in combat. You will be killed if you attempt to quit.");
            }
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> removeCombat(player), 300L);
        }

        @EventHandler(ignoreCancelled = true)
        public void onHangingBreak(HangingBreakByEntityEvent event) {
            // Make sure it's a player/player's projectile damaging an item frame/painting
            var cause = event.getCause();

            if (cause != HangingBreakEvent.RemoveCause.ENTITY) {
                event.setCancelled(true);
            }

            var remover = event.getRemover();
            var trueRemover = GuardManager.getInstance().getTrueDamager(remover);

            if (!(trueRemover instanceof Player)) {
                return;
            }


            // Make sure the player isn't allowed to build/destroy at the event location.
            var entity = event.getEntity();
            var canBuild = canBuildHere(entity.getLocation().getBlock(), entity, null);

            if (!canBuild) {
                // Cancel the event
                if (event.getRemover() instanceof Projectile) {
                    event.getRemover().remove();
                }
                trueRemover.sendMessage(ChatColor.RED + "You don't have permission to break that here.");
                event.setCancelled(true);
            }

        }


        private boolean isBorder(DiplomacyChunk diplomacyChunk) {
            var chunk = diplomacyChunk.getChunk();
            var world = chunk.getWorld();
            if (!world.equals(WorldManager.getInstance().getOverworld())) return false;
            var nation = diplomacyChunk.getNation();
            if (nation == null) return false;

            var x = chunk.getX();
            var z = chunk.getZ();

            var a = Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(chunk.getWorld().getChunkAt(x - 1, z - 1)).getNation(), nation);
            var b = Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(chunk.getWorld().getChunkAt(x, z - 1)).getNation(), nation);
            var c = Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(chunk.getWorld().getChunkAt(x + 1, z - 1)).getNation(), nation);
            var d = Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(chunk.getWorld().getChunkAt(x - 1, z)).getNation(), nation);
            var e = Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(chunk.getWorld().getChunkAt(x + 1, z)).getNation(), nation);
            var f = Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(chunk.getWorld().getChunkAt(x - 1, z + 1)).getNation(), nation);
            var g = Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(chunk.getWorld().getChunkAt(x, z + 1)).getNation(), nation);
            var h = Objects.equals(DiplomacyChunks.getInstance().getDiplomacyChunk(chunk.getWorld().getChunkAt(x + 1, z + 1)).getNation(), nation);

            return !(a && b && c && d && e && f && g && h);
        }

        @EventHandler(ignoreCancelled = true)
        private void onBlockBreakEvent(BlockBreakEvent event) {
            var block = event.getBlock();
            var player = event.getPlayer();

            var canBuild = canBuildHere(block, player, null);

            if (canBuild) return;
            var isBorder = isBorder(DiplomacyChunks.getInstance().getDiplomacyChunk(block.getChunk()));
            if (isBorder) {
                event.setDropItems(false);
                event.setExpToDrop(0);
                var state = block.getState();
                griefedBlocks.putIfAbsent(block, state);
                player.sendMessage(ChatColor.RED + "Block destroyed, but will regenerate back in 5 seconds.");
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> {
                            var originalState = griefedBlocks.get(block);
                            block.breakNaturally();
                            originalState.update(true, false);
                            griefedBlocks.remove(block);
                        },
                        100L
                );
            } else {
                event.setCancelled(true);
                player.sendMessage(ChatColor.RED + "You cannot destroy here.");
            }
        }

        @EventHandler
        private void onPlayerTeleport(PlayerTeleportEvent event) {
            var from = event.getFrom();
            var to = event.getTo();
            var cause = event.getCause();
            if (cause == PlayerTeleportEvent.TeleportCause.COMMAND && Objects.equals(to.getWorld(), WorldManager.getInstance().getSpawn()) && !Objects.equals(from.getWorld(), WorldManager.getInstance().getSpawn())) {
                var dp = DiplomacyPlayers.getInstance().get(event.getPlayer().getUniqueId());
                dp.setLastLocation(from);
            }
        }

        @EventHandler
        private void onKick(PlayerKickEvent event) {
            var player = event.getPlayer();
            combatLogged.remove(player);
        }


        @EventHandler
        private void onPlayerQuit(PlayerQuitEvent event) {
            var player = event.getPlayer();
            if (combatLogged.containsKey(player)) {
                Entity source = null;
                if (player.getLastDamageCause() != null) {
                    source = player.getLastDamageCause().getEntity();
                }
                player.damage(9999, source);
                for (var online : Bukkit.getOnlinePlayers()) {
                    online.sendMessage(ChatColor.RED + player.getName() + " was killed for logging out during combat.");
                }
                combatLogged.remove(player);
            }

            var loc = player.getLocation();

            if (!Objects.equals(loc.getWorld(), WorldManager.getInstance().getSpawn())) {
                var dp = DiplomacyPlayers.getInstance().get(event.getPlayer().getUniqueId());
                dp.setLastLocation(loc);
            }
        }

        @EventHandler
        private void onPlayerJoinEvent(PlayerJoinEvent event) {
            var player = event.getPlayer();
            var point = WorldManager.getInstance().getSpawn().getSpawnLocation();
            point.setX(point.getX() + 0.5);
            point.setZ(point.getZ() + 0.5);
            player.teleport(point);

            ScoreboardManager.getInstance().updateScoreboards();
            if (!player.hasPlayedBefore()) {
                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 8));
                player.getInventory().addItem(new ItemStack(Material.OAK_LOG, 8));
            }
        }

        @EventHandler(ignoreCancelled = false, priority = EventPriority.HIGHEST)
        private void onPlayerRespawn(PlayerRespawnEvent event) {
            var player = event.getPlayer();
            var dp = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            if (dp.getLives() == 0) {
                var point = WorldManager.getInstance().getSpawn().getSpawnLocation();
                point.setX(point.getX() + 0.5);
                point.setZ(point.getZ() + 0.5);
                event.setRespawnLocation(point);
                player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "You have 0 lives left. You will not be able to re-enter the world until you gain more lives.");
                player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "Gain more lives by voting for our server, or waiting until the next day begins.");
                return;
            }
            var bed = player.getBedSpawnLocation();
            if (bed == null) {
                var world = WorldManager.getInstance().getSpawn();
                var point = world.getSpawnLocation();
                point.setX(point.getX() + 0.5);
                point.setZ(point.getZ() + 0.5);
                event.setRespawnLocation(point);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private void onDamage(EntityDamageEvent event) {
            var entity = event.getEntity();
            if (entity instanceof Item) {
                var item = (Item) entity;
                if (Items.getInstance().isGrenade(item.getItemStack()) && item.getItemStack().getType() == Material.TNT) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (!(entity instanceof LivingEntity) && DiplomacyChunks.getInstance().getDiplomacyChunk(entity.getLocation().getChunk()).getNation() != null)
                event.setCancelled(true);//todo figure out

        }

        @EventHandler(priority = EventPriority.HIGH)
        private void onEntityExplode(EntityExplodeEvent event) {
            var entity = event.getEntity();
            var trueEntity = GuardManager.getInstance().getTrueDamager(entity);
            List<Block> keepBlocks = new ArrayList<>();
            HashMap<Block, BlockState> tempGriefed = new HashMap<>();
            for (var block : event.blockList()) {
                var canBuildHere = canBuildHere(block, trueEntity, null);
                if (!canBuildHere) {
                    keepBlocks.add(block);
                    if (isBorder(DiplomacyChunks.getInstance().getDiplomacyChunk(block.getChunk()))) {
                        tempGriefed.put(block, block.getState());
                    }
                }
            }
            event.blockList().removeAll(keepBlocks);
            for (var block : tempGriefed.keySet()) {
                block.setType(Material.AIR);
            }
            explodedBlocks.add(tempGriefed);
            Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> regenerateExplodedBlocks(tempGriefed), 600L);
        }

        private void regenerateExplodedBlocks(HashMap<Block, BlockState> blocks) {
            explodedBlocks.remove(blocks);
            var array = blocks.keySet().toArray();
            var size = blocks.size();
            var r = (int) (Math.random() * size);
            var block = array[r];
            var state = blocks.get(block);
            state.update(true, false);
            blocks.remove(block);
            if (blocks.size() > 0) {
                explodedBlocks.add(blocks);
                Bukkit.getScheduler().runTaskLater(Diplomacy.getInstance(), () -> regenerateExplodedBlocks(blocks), 20L);
            }
        }
    }
}
