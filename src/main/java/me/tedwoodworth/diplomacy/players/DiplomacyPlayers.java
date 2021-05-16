package me.tedwoodworth.diplomacy.players;

import com.google.common.collect.ImmutableMap;
import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.Items.Items;
import me.tedwoodworth.diplomacy.geology.WorldManager;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroup;
import me.tedwoodworth.diplomacy.groups.DiplomacyGroups;
import me.tedwoodworth.diplomacy.nations.*;
import me.tedwoodworth.diplomacy.spawning.SpawnManager;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
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
    private final File diplomacyPlayerConfigFile = new File(Diplomacy.getInstance().getDataFolder(), "diplomacyPlayers.yml");
    private final Map<UUID, DiplomacyPlayer> diplomacyPlayers = new WeakHashMap<>();
    private final YamlConfiguration config;
    private ItemStack guideBook;

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

    public ItemStack getGuideBook() {
        if (guideBook == null) {
            createGuideBook();
        }
        return guideBook;
    }

    private void createGuideBook() {
        var guideBook = new ItemStack(Material.WRITTEN_BOOK, 1);
        var bookMeta = (BookMeta) guideBook.getItemMeta();
        bookMeta.setTitle("" + ChatColor.GREEN + ChatColor.BOLD + "Server Guide");
        bookMeta.setAuthor(ChatColor.BOLD + "Unknown");
        bookMeta.setGeneration(BookMeta.Generation.TATTERED);
        var pages = new ArrayList<String>();

        pages.add(
                ChatColor.BOLD + "Table of contents:\n\n" + ChatColor.RESET +
                        "2 - Intro\n" +
                        "3 - Server Genre\n" +
                        "4 - Rules\n" +
                        "5 - Respawning\n" +
                        "6 - One-time Teleport\n" +
                        "7 - Lives\n" +
                        "8 - Combat\n" +
                        "9 - Menu\n" +
                        "10 - Nations\n" +
                        "11 - Groups\n" +
                        "12 - Economy\n" +
                        "13 - World Map\n"
        );

        pages.add(
                ChatColor.BOLD + "Intro\n\n" + ChatColor.RESET +
                        "This guide is an overview of the most " +
                        "important features a new player should be " +
                        "aware of. " +
                        "A more detailed guide can be found on the discord server (accessed via \"/discord\")"
        );
        pages.add(
                ChatColor.BOLD + "Server Genre\n\n" + ChatColor.RESET +
                        "DiplomacyCraft is a geopolitical strategy & anarchy server. " +
                        "This means that there are minimal rules, and that nations compete " +
                        "with other nations for power and stability. "
        );
        pages.add(
                ChatColor.BOLD + "Rules\n\n" + ChatColor.RESET +
                        ChatColor.BOLD + "1)" + ChatColor.RESET + " No hacking\n" +
                        ChatColor.BOLD + "2)" + ChatColor.RESET + " No cheating\n" +
                        ChatColor.BOLD + "3)" + ChatColor.RESET + " No spamming\n" +
                        ChatColor.BOLD + "4)" + ChatColor.RESET + " No racism\n" +
                        ChatColor.BOLD + "5)" + ChatColor.RESET + " No sexism\n" +
                        ChatColor.BOLD + "6)" + ChatColor.RESET + " No homophobia\n" +
                        ChatColor.BOLD + "7)" + ChatColor.RESET + " No doxxing.\n" +
                        ChatColor.BOLD + "8)" + ChatColor.RESET + " No real-life threats."
        );
        pages.add(
                ChatColor.BOLD + "Respawning\n\n" + ChatColor.RESET +
                        ChatColor.BOLD + "1)" + ChatColor.RESET + " If you die in the nether, you will respawn in the nether.\n" +
                        ChatColor.BOLD + "2)" + ChatColor.RESET + " If you don't have a bed/respawn anchor you will randomly respawn within 2000 blocks of where you died."
        );
        pages.add(
                ChatColor.BOLD + "One-Time Teleport\n\n" + ChatColor.RESET +
                        "Players are only able to teleport once with \"/ott\" (one-time teleport). Once used, it is impossible to teleport again."
        );
        pages.add(
                ChatColor.BOLD + "Lives\n\n" + ChatColor.RESET +
                        ChatColor.BOLD + "1)" + ChatColor.RESET + " Every time you die, you lose a life. \n" +
                        ChatColor.BOLD + "2)" + ChatColor.RESET + " If you run out of lives, you will be banned until the next day begins.\n" +
                        ChatColor.BOLD + "3)" + ChatColor.RESET + " Players can gain 5 lives a day: one for joining, and four for voting with \"/vote\"."
        );
        pages.add(
                ChatColor.BOLD + "Combat\n\n" + ChatColor.RESET +
                        "The server will be using pre-1.9 style combat. This means that there will be no cooldown between swings."
        );
        pages.add(
                ChatColor.BOLD + "Menu\n\n" + ChatColor.RESET +
                        "The menu contains almost all the info you need to know when it comes to nation, group, and player stats. " +
                        "The menu can be accessed by typing \"/menu\"."
        );
        pages.add(
                ChatColor.BOLD + "Nations\n\n" + ChatColor.RESET +
                        ChatColor.BOLD + "1)" + ChatColor.RESET + " Nations are created in order to protect territory and builds from outsiders.\n" +
                        ChatColor.BOLD + "2)" + ChatColor.RESET + " Create a nation with \"/nation create\"\n" +
                        ChatColor.BOLD + "3)" + ChatColor.RESET + " Expand territory with \"/plot contest\"\n" +
                        ChatColor.BOLD + "4)" + ChatColor.RESET + " Join a nation with \"/nation join\" or by accepting an invite."
        );
        pages.add(
                ChatColor.BOLD + "Groups\n\n" + ChatColor.RESET +
                        ChatColor.BOLD + "1)" + ChatColor.RESET + " Groups are sub-sections of a nation.\n" +
                        ChatColor.BOLD + "2)" + ChatColor.RESET + " Only a group's members can build in its plots.\n" +
                        ChatColor.BOLD + "3)" + ChatColor.RESET + " Players can join a foreign nation's groups.\n" +
                        ChatColor.BOLD + "4)" + ChatColor.RESET + " Groups are created with \"/group create\"."
        );
        pages.add(
                ChatColor.BOLD + "Economy\n\n" + ChatColor.RESET +
                        ChatColor.BOLD + "1)" + ChatColor.RESET + " The currency is diamond-based; 1 diamond = \u00A41,000.00.\n" +
                        ChatColor.BOLD + "2)" + ChatColor.RESET + " Turn diamonds into currency with \"/deposit\".\n" +
                        ChatColor.BOLD + "3)" + ChatColor.RESET + " Turn currency into diamonds with \"/withdraw\".\n" +
                        ChatColor.BOLD + "4)" + ChatColor.RESET + " Check your balance with \"/balance\"."
        );
        pages.add(
                ChatColor.BOLD + "World Map\n\n" + ChatColor.RESET +
                        "The world map displays all explored territory and nation borders. It is accessed via \"/map\"."
        );

        bookMeta.setPages(pages);
        guideBook.setItemMeta(bookMeta);
        guideBook.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        this.guideBook = guideBook;
    }

    public boolean canBuildHere(Block block, Player player) {
        var biome = block.getBiome();
        if (biome == Biome.OCEAN || biome == Biome.COLD_OCEAN || biome == Biome.DEEP_COLD_OCEAN || biome == Biome.DEEP_FROZEN_OCEAN
                || biome == Biome.DEEP_LUKEWARM_OCEAN || biome == Biome.DEEP_OCEAN || biome == Biome.DEEP_WARM_OCEAN || biome == Biome.FROZEN_OCEAN
                || biome == Biome.LUKEWARM_OCEAN || biome == Biome.WARM_OCEAN) {
            var height = block.getY();
            var seaLevel = new Location(block.getWorld(), block.getX(), 33, block.getZ());
            if (height >= 34 && seaLevel.getBlock().getType() == Material.WATER) {
                return false;
            } else {
                var isBelow = false;
                var current = block.getRelative(BlockFace.UP);
                for (int i = height; i < 34; i++) {
                    if (current.getType() != Material.WATER) {
                        isBelow = true;
                        break;
                    } else {
                        current = current.getRelative(BlockFace.UP);
                    }
                }
                if (!isBelow) {
                    return false;
                }
            }
        }
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
        }

        // Return true if there is no nation
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
            save();
        }

        @EventHandler
        void onPluginDisable(PluginDisableEvent event) {
            if (event.getPlugin().equals(Diplomacy.getInstance())) {
                save();
            }
        }

        @EventHandler
        private void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
            var player = event.getPlayer();
            if (event.getFrom().getEnvironment().equals(World.Environment.THE_END) && !player.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
                if (player.getBedSpawnLocation() == null) {
                    var location = SpawnManager.getInstance().getRespawnLocation(player.getLocation(), false);
                    if (location != null) {
                        player.teleport(location);
                    }
                }
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
            var from = event.getFrom();
            var to = event.getTo();
            var toY = to.getY();
            var toWorld = to.getWorld();
            if (Objects.equals(toWorld, WorldManager.getInstance().getOverworld()) && toY < 4) {
                var subword = WorldManager.getInstance().getSubworld();
                var player = event.getPlayer();
                var velocity = player.getVelocity();
                var chunk = to.getChunk();
                var yaw = to.getYaw();
                var pitch = to.getPitch();
                WorldManager.getInstance().adjustChunks(chunk.getX(), chunk.getZ());
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
                var player = event.getPlayer();
                var velocity = player.getVelocity();
                var chunk = to.getChunk();
                WorldManager.getInstance().adjustChunks(chunk.getX(), chunk.getZ());
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
                var player = event.getPlayer();
                var velocity = player.getVelocity();
                var chunk = to.getChunk();
                WorldManager.getInstance().adjustChunks(chunk.getX(), chunk.getZ());
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
                var player = event.getPlayer();
                var velocity = player.getVelocity();
                var chunk = to.getChunk();
                WorldManager.getInstance().adjustChunks(chunk.getX(), chunk.getZ());
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
                WorldManager.getInstance().adjustChunks(toChunk.getX(), toChunk.getZ());
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
            var block = event.getBlock();
            var player = event.getPlayer();
            if (canBuildHere(block, player)) return;

            player.sendMessage(ChatColor.RED + "You cannot build here.");
            event.setCancelled(true);
        }

        @EventHandler
        private void onPlayerBucketFill(PlayerBucketFillEvent event) {
            var block = event.getBlock();

            var player = event.getPlayer();

            if (canBuildHere(block, player)) return;

            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
            event.setCancelled(true);
        }

        @EventHandler
        private void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
            var block = event.getBlock();
            var player = event.getPlayer();
            if (canBuildHere(block, player)) return;

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
                        case BEACON -> {
                            event.getPlayer().sendMessage(ChatColor.RED + "Beacons are currently disabled.");
                            event.setCancelled(true);
                            return;
                        }
                        case ENCHANTING_TABLE -> {
                            event.getPlayer().sendMessage(ChatColor.RED + "Enchanting tables are disabled. (enchantments obtained via crafting recipes, view #recipes in /discord)");
                            event.setCancelled(true);
                            return;
                        }
                        case RESPAWN_ANCHOR -> {
                            if (block.getWorld().getEnvironment().equals(World.Environment.NETHER)) return;

                            var player = event.getPlayer();
                            if (canBuildHere(block, player)) return;

                            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                            event.setCancelled(true);
                            return;
                        }
                        case ITEM_FRAME, COMPOSTER -> {
                            var player = event.getPlayer();
                            if (canBuildHere(block, player)) return;

                            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                            event.setCancelled(true);
                            return;
                        }
                        case BLACK_BED, BLUE_BED, BROWN_BED, CYAN_BED, GRAY_BED, GREEN_BED, LIGHT_BLUE_BED, LIME_BED, MAGENTA_BED, ORANGE_BED, PINK_BED,
                                PURPLE_BED, RED_BED, WHITE_BED, YELLOW_BED, LIGHT_GRAY_BED -> {
                            if (block.getWorld().getEnvironment().equals(World.Environment.NORMAL)) return;

                            var player = event.getPlayer();
                            if (canBuildHere(block, player)) return;

                            player.sendMessage(ChatColor.RED + "You don't have permission to use that here.");
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
                case PHYSICAL -> {
                    if (block.getType() == Material.FARMLAND) {
                        var player = event.getPlayer();
                        if (canBuildHere(block, player)) return;

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
                            if (canBuildHere(block, player)) return;

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

            // Disable villager trades/interaction
            if (entity instanceof Villager || entity instanceof WanderingTrader) {
                event.getPlayer().sendMessage(ChatColor.RED + "Villager trading is currently disabled.");
                event.setCancelled(true);
                return;
            }

            // Disable adding chests to horses/donkeys/etc.
            if (entity instanceof ChestedHorse && event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.CHEST)) {
                event.getPlayer().sendMessage(ChatColor.RED + "Equipping chests to donkeys/mules is disabled.");
                event.setCancelled(true);
                return;
            }

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

            // Cancel the event
            if (event.getRemover() instanceof Projectile) {
                event.getRemover().remove();
            }
            player.sendMessage(ChatColor.RED + "You don't have permission to break that here.");
            event.setCancelled(true);

        }


        @EventHandler(ignoreCancelled = true)
        private void onBlockBreakEvent(BlockBreakEvent event) {


            var block = event.getBlock();
            var player = event.getPlayer();

            if (canBuildHere(block, player)) return;

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
            } else if (entity instanceof Creeper || entity instanceof Wither || entity instanceof WitherSkull || entity instanceof DragonFireball || entity instanceof EnderCrystal || entity instanceof Item) {
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
