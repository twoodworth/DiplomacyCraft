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
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
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

        private ItemStack guideBook;

        private void createGuideBook() {
            var guideBook = new ItemStack(Material.WRITTEN_BOOK, 1);
            var bookMeta = (BookMeta) guideBook.getItemMeta();
            bookMeta.setTitle(ChatColor.BLUE + "Server Guide");
            bookMeta.setAuthor("God");
            var pages = new ArrayList<String>();

            pages.add(
                    "Table of contents:\n\n" +
                            "2 - Intro\n" +
                            "3 - Respawning\n" +
                            "4 - One-time Teleport\n" +
                            "5 - Lives\n" +
                            "6 - Combat\n" +
                            "7 - Menu\n" +
                            "8 - Nations\n" +
                            "9 - Groups\n" +
                            "10 - Economy\n" +
                            "11 - World Map\n"
            );

            pages.add(
                    "Intro\n\n" +
                            "This guide provides an overview of the most " +
                            "important features a new player should be " +
                            "aware of when they first join the server." +
                            "A more detailed guide can be found on the discord server (accessed via \"/discord\")"
            );
            pages.add(
                    "Respawning\n\n" +
                            "1) If you die in the nether, you will respawn in the nether.\n" +
                            "2) If you die in the overworld, you will respawn in the overworld.\n" +
                            "3) If you don't have a bed (or it is in another dimension) you will randomly respawn within 2000 blocks of where you died."
            );
            pages.add(
                    "One-Time Teleport\n\n" +
                            "Each player is given a one-time teleport (which can be used with \"/ott\"). This will be the player’s only opportunity" +
                            " to teleport somewhere on the server. Once it is used, the player cannot teleport ever again."
            );
            pages.add(
                    "Lives\n\n" +
                            "1) When you first join the server, you have 20 lives.\n" +
                            "2) Every time you die, you lose a life. \n" +
                            "3) If you run out of lives, you will be temporarily banned until the next day begins.\n" +
                            "4) Everyday, a player can gain 5 lives: one for just logging in, and four for voting."
            );
            pages.add(
                    "Combat\n\n" +
                            "The server will be using pre-1.9 style combat. This means that there will be no cooldown between swings."
            );
            pages.add(
                    "Menu\n\n" +
                            "1) The menu contains almost all the info you need to know when it comes to nation, group, and player stats.\n" +
                            "2) The menu can be accessed by typing \"/menu\", from which point you can navigate to any nation, group, or player section.\n"
            );
            pages.add(
                    "Nations\n\n" +
                            "1) To protect their territory, players can join or create nations. " +
                            "2) Outsiders will not be able to build / destroy inside a nation’s borders.\n" +
                            "3) Players can create a nation with /nation create.\n" +
                            "4) Players can join a nation with /nation join or by accepting an invite from a nation."
            );
            pages.add(
                    "Groups\n\n" +
                            "1) Groups are sub-sections of a nation. " +
                            "2) By default, players will only be able to build/destroy in the territory of the groups that they belong to.\n" +
                            "3) Players can join any group, even if they aren't from the same nation that the group belongs to.\n" +
                            "4) Groups can be created with /group create."
            );
            pages.add(
                    "Groups\n\n" +
                            "1) Groups are sub-sections of a nation. " +
                            "2) By default, players will only be able to build/destroy in the territory of the groups that they belong to.\n" +
                            "3) Players can join any group, even if they aren't from the same nation that the group belongs to.\n" +
                            "4) Groups can be created with /group create."
            );
            pages.add(
                    "Economy\n\n" +
                            "1) The economy utilizes a diamond-based currency.\n" +
                            "2) 1 diamond = ¤1,000.00\n" +
                            "3) Turn diamonds into currency by with /deposit.\n" +
                            "4) Turn currency back into diamonds with /withdraw.\n" +
                            "5) Check your balance with /balance\n" +
                            "6) Find the value of your inventory with /wallet. \n" +
                            "7) Pay players with /pay\n" +
                            "8) Trade players with /trade"
            );
            pages.add(
                    "World Map\n\n" +
                            "Players will be able to view a world map which displays all explored land and nation borders. It can be accessed using /map"
            );

            bookMeta.setPages(pages);
            guideBook.setItemMeta(bookMeta);
            this.guideBook = guideBook;
        }

        private ItemStack getGuideBook() {
            if (guideBook == null) {
                createGuideBook();
            }
            return guideBook;
        }


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

        @EventHandler
        private void onPlayerPortal(PlayerChangedWorldEvent event) {
            var player = event.getPlayer();
            if (!event.getFrom().getEnvironment().equals(World.Environment.NETHER) && player.getLocation().getWorld().getEnvironment().equals(World.Environment.NETHER)) {
                player.sendMessage("" + ChatColor.RED + ChatColor.BOLD + "WARNING: If you die in the nether, you will respawn in the nether. Be careful!");
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
                player.getInventory().addItem(getGuideBook());
                player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 8));
            }
        }

        @EventHandler
        private void onPlayerRespawn(PlayerRespawnEvent event) {
            event.getPlayer().getInventory().addItem(getGuideBook());
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
