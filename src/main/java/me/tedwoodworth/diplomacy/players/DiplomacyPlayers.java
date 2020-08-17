package me.tedwoodworth.diplomacy.players;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import me.tedwoodworth.diplomacy.nations.Nation;
import me.tedwoodworth.diplomacy.nations.Nations;
import me.tedwoodworth.diplomacy.nations.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class DiplomacyPlayers {

    private static DiplomacyPlayers instance = null;
    private File diplomacyPlayerConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "diplomacyPlayers.yml");
    private Map<UUID, DiplomacyPlayer> diplomacyPlayers = new WeakHashMap<>();
    private YamlConfiguration config;

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
            if (player.getPlayer().getName().equalsIgnoreCase(strPlayer)) {
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

    private class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }

        @EventHandler
        void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin().equals(Diplomacy.getInstance())) {
                save();
            }
        }

        @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
        private void onPlayerMove(PlayerMoveEvent event) {
            var fromChunk = event.getFrom().getChunk();
            var toChunk = event.getTo().getChunk();
            if (!fromChunk.equals(toChunk)) {
                var fromDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(fromChunk);
                var toDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(toChunk);

                var fromNation = fromDiplomacyChunk.getNation();
                var toNation = toDiplomacyChunk.getNation();

                var fromGroup = fromDiplomacyChunk.getGroup();
                var toGroup = toDiplomacyChunk.getGroup();

                if (!Objects.equals(fromNation, toNation) || !Objects.equals(fromGroup, toGroup)) {
                    var player = event.getPlayer();
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
            var chunk = event.getBlock().getLocation().getChunk();
            var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
            var nation = diplomacyChunk.getNation();

            if (nation == null) {
                return;
            }

            var group = DiplomacyGroups.getInstance().get(diplomacyChunk);
            var player = event.getPlayer();
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());

            if (group != null) {
                if (group.getMembers().contains(diplomacyPlayer)) {
                    return;
                }
            }


            var playerNation = Nations.getInstance().get(diplomacyPlayer);

            if (Objects.equals(nation, playerNation)) {
                var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
                var canBuildAnywhere = permissions.get("CanBuildAnywhere");
                if (canBuildAnywhere) {
                    return;
                }
            }

            player.sendMessage(ChatColor.RED + "You cannot build here.");
            event.setCancelled(true);
        }

        @EventHandler(ignoreCancelled = true)
        private void onPlayerInteract(PlayerInteractEvent event) {
            if (event.getClickedBlock() == null) {
                return;
            }

            var block = event.getClickedBlock();
            var chunk = event.getClickedBlock().getLocation().getChunk();
            var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
            var nation = diplomacyChunk.getNation();

            if (nation == null) {
                return;
            }

            var group = DiplomacyGroups.getInstance().get(diplomacyChunk);
            var player = event.getPlayer();
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());

            if (group != null) {
                if (group.getMembers().contains(diplomacyPlayer)) {
                    return;
                }
            }


            var playerNation = Nations.getInstance().get(diplomacyPlayer);

            if (Objects.equals(nation, playerNation)) {
                var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
                var canBuildAnywhere = permissions.get("CanBuildAnywhere");
                if (canBuildAnywhere) {
                    return;
                }
            }

            ArrayList<Material> badItems = new ArrayList<>();
            badItems.add(Material.BUCKET);
            badItems.add(Material.COD_BUCKET);
            badItems.add(Material.LAVA_BUCKET);
            badItems.add(Material.PUFFERFISH_BUCKET);
            badItems.add(Material.SALMON_BUCKET);
            badItems.add(Material.TROPICAL_FISH_BUCKET);
            badItems.add(Material.WATER_BUCKET);
            badItems.add(Material.FLINT_AND_STEEL);
            badItems.add(Material.FIRE_CHARGE);
            badItems.add(Material.END_CRYSTAL);
            badItems.add(Material.PAINTING);
            badItems.add(Material.ITEM_FRAME);


            if (event.getItem() != null && badItems.contains(event.getItem().getType())) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                event.setCancelled(true);
            } else if (block != null) {

                ArrayList<Material> beds = new ArrayList<>();
                beds.add(Material.BLACK_BED);
                beds.add(Material.BLUE_BED);
                beds.add(Material.BROWN_BED);
                beds.add(Material.BLUE_BED);
                beds.add(Material.CYAN_BED);
                beds.add(Material.GRAY_BED);
                beds.add(Material.GREEN_BED);
                beds.add(Material.LIGHT_BLUE_BED);
                beds.add(Material.LIME_BED);
                beds.add(Material.MAGENTA_BED);
                beds.add(Material.ORANGE_BED);
                beds.add(Material.PINK_BED);
                beds.add(Material.PURPLE_BED);
                beds.add(Material.RED_BED);
                beds.add(Material.WHITE_BED);
                beds.add(Material.YELLOW_BED);

                if (block.getType().equals(Material.RESPAWN_ANCHOR) && !block.getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                    event.setCancelled(true);
                } else if (block.getType().equals(Material.ITEM_FRAME)) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                    event.setCancelled(true);
                } else if (beds.contains(block.getType()) && !block.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                    event.setCancelled(true);
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

            if (group != null) {
                if (group.getMembers().contains(diplomacyPlayer)) {
                    return;
                }
            }

            var playerNation = Nations.getInstance().get(diplomacyPlayer);

            if (Objects.equals(nation, playerNation)) {
                var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
                var canBuildAnywhere = permissions.get("CanBuildAnywhere");
                if (canBuildAnywhere) {
                    return;
                }
            }

            if (event.getRightClicked() instanceof ItemFrame || event.getRightClicked() instanceof Painting) {
                player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                event.setCancelled(true);
            }
        }

        @EventHandler(ignoreCancelled = true)
        public void onEntityDamageByEntity(HangingBreakByEntityEvent event) {
            // Make sure it's a player/player's projectile damaging an item frame/painting
            if (!(event.getEntity() instanceof ItemFrame || event.getEntity() instanceof Painting)) {
                return;
            }
            if (!(event.getRemover() instanceof Player || event.getRemover() instanceof Projectile)) {
                return;
            }
            if (event.getRemover() instanceof Projectile) {
                if (!(((Projectile) event.getRemover()).getShooter() instanceof Player)) {
                    return;
                }
            }

            // Make sure the player isn't allowed to build/destroy at the event location.
            var entity = event.getEntity();
            var chunk = entity.getLocation().getChunk();
            var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
            var nation = diplomacyChunk.getNation();

            if (nation == null) {
                return;
            }

            Player player;

            if (event.getRemover() instanceof Player) {
                player = (Player) event.getRemover();
            } else {
                player = (Player) ((Projectile) event.getRemover()).getShooter();
            }

            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());
            var group = DiplomacyGroups.getInstance().get(diplomacyChunk);

            if (group != null) {
                if (group.getMembers().contains(diplomacyPlayer)) {
                    return;
                }
            }

            var playerNation = Nations.getInstance().get(diplomacyPlayer);

            if (Objects.equals(nation, playerNation)) {
                var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
                var canBuildAnywhere = permissions.get("CanBuildAnywhere");
                if (canBuildAnywhere) {
                    return;
                }
            }

            // Cancel the event
            if (event.getRemover() instanceof Projectile) {
                event.getRemover().remove();
            }
            player.sendMessage(ChatColor.RED + "You don't have permission to break that here.");
            event.setCancelled(true);

        }


        @EventHandler(ignoreCancelled = true)
        private void onBlockBreakEvent(BlockBreakEvent event) {
            var chunk = event.getBlock().getLocation().getChunk();
            var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
            var nation = diplomacyChunk.getNation();

            if (nation == null) {
                return;
            }

            var group = DiplomacyGroups.getInstance().get(diplomacyChunk);
            var player = event.getPlayer();
            var diplomacyPlayer = DiplomacyPlayers.getInstance().get(player.getUniqueId());

            if (group != null) {
                if (group.getMembers().contains(diplomacyPlayer)) {
                    return;
                }
            }


            var playerNation = Nations.getInstance().get(diplomacyPlayer);

            if (Objects.equals(nation, playerNation)) {
                var permissions = nation.getMemberClass(diplomacyPlayer).getPermissions();
                var canBuildAnywhere = permissions.get("CanBuildAnywhere");
                if (canBuildAnywhere) {
                    return;
                }
            }

            player.sendMessage(ChatColor.RED + "You cannot destroy here.");
            event.setCancelled(true);
        }

        @EventHandler
        private void onPlayerJoinEvent(PlayerJoinEvent event) {
            var player = event.getPlayer();
            ScoreboardManager.getInstance().updateScoreboards();

            if (!player.hasPlayedBefore()) {
                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 8));
            }
        }

        @EventHandler(priority = EventPriority.MONITOR)
        private void onEntityExplode(EntityExplodeEvent event) {
            var entity = event.getEntity();
            List<Block> keepBlocks = new ArrayList<>();
            if (entity instanceof Player) {
                var diplomacyPlayer = DiplomacyPlayers.getInstance().get(entity.getUniqueId());
                var nation = Nations.getInstance().get(diplomacyPlayer);
                for (var block : event.blockList()) {
                    var chunk = block.getChunk();
                    var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
                    var blockNation = diplomacyChunk.getNation();
                    if (blockNation == null) {
                        continue;
                    }
                    if (!Objects.equals(blockNation, nation)) {
                        keepBlocks.add(block);
                    }
                }
                event.blockList().removeAll(keepBlocks);
            } else if (entity instanceof Creeper || entity instanceof Wither || entity instanceof WitherSkull || entity instanceof DragonFireball || entity instanceof EnderCrystal) {
                for (var block : event.blockList()) {
                    var chunk = block.getChunk();
                    var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
                    var blockNation = diplomacyChunk.getNation();
                    if (blockNation != null) {
                        keepBlocks.add(block);
                    }
                }
                event.blockList().removeAll(keepBlocks);
            } else if (entity instanceof Projectile) {
                var projectile = (Projectile) event.getEntity();
                if (projectile.getShooter() instanceof Player) {
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(entity.getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    for (var block : event.blockList()) {
                        var chunk = block.getChunk();
                        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
                        var blockNation = diplomacyChunk.getNation();
                        if (blockNation == null) {
                            continue;
                        }
                        if (!Objects.equals(blockNation, nation)) {
                            keepBlocks.add(block);
                        }
                    }
                } else {
                    for (var block : event.blockList()) {
                        var chunk = block.getChunk();
                        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
                        var blockNation = diplomacyChunk.getNation();
                        if (blockNation != null) {
                            keepBlocks.add(block);
                        }
                    }
                }
            } else if (entity instanceof TNTPrimed) {
                var tnt = (TNTPrimed) event.getEntity();
                var source = tnt.getSource();
                if (source instanceof Player) {
                    var diplomacyPlayer = DiplomacyPlayers.getInstance().get(entity.getUniqueId());
                    var nation = Nations.getInstance().get(diplomacyPlayer);
                    for (var block : event.blockList()) {
                        var chunk = block.getChunk();
                        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
                        var blockNation = diplomacyChunk.getNation();
                        if (blockNation == null) {
                            continue;
                        }
                        if (!Objects.equals(blockNation, nation)) {
                            keepBlocks.add(block);
                        }
                    }
                } else {
                    for (var block : event.blockList()) {
                        var chunk = block.getChunk();
                        var diplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(chunk);
                        var blockNation = diplomacyChunk.getNation();
                        if (blockNation != null) {
                            keepBlocks.add(block);
                        }
                    }
                }
            }
            event.blockList().removeAll(keepBlocks);
        }
    }
}
