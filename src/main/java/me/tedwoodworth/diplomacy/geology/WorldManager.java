package me.tedwoodworth.diplomacy.geology;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.data.BooleanPersistentDataType;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;

public class WorldManager {

    private final World subworld = Bukkit.createWorld(new WorldCreator("subworld"));
    private final World spawn = Bukkit.createWorld(new WorldCreator("spawnworld"));
    private final World overworld = Bukkit.getWorld("world");
    private final World nether = Bukkit.getWorld("world_nether");
    private final World end = Bukkit.getWorld("world_the_end");
    private final HashSet<Material> dupable = new HashSet<Material>();
    private static WorldManager instance = null;
    public final NamespacedKey ADJUSTED_KEY = new NamespacedKey(Diplomacy.getInstance(), "chunk_is_adjusted");

    public static WorldManager getInstance() {
        if (instance == null) {
            instance = new WorldManager();
        }
        return instance;
    }

    private WorldManager() {
        dupable.add(Material.TORCH);
        dupable.add(Material.REDSTONE_TORCH);
        dupable.add(Material.REDSTONE_WALL_TORCH);
        dupable.add(Material.SOUL_TORCH);
        dupable.add(Material.SOUL_WALL_TORCH);
        dupable.add(Material.WALL_TORCH);
        dupable.add(Material.REDSTONE);
        dupable.add(Material.REDSTONE_WIRE);
        dupable.add(Material.STRING);
        dupable.add(Material.TRIPWIRE);
        dupable.add(Material.REPEATER);
        dupable.add(Material.RAIL);
        dupable.add(Material.ACTIVATOR_RAIL);
        dupable.add(Material.DETECTOR_RAIL);
        dupable.add(Material.POWERED_RAIL);
        dupable.add(Material.SPRUCE_SAPLING);
        dupable.add(Material.ACACIA_SAPLING);
        dupable.add(Material.BAMBOO_SAPLING);
        dupable.add(Material.BIRCH_SAPLING);
        dupable.add(Material.DARK_OAK_SAPLING);
        dupable.add(Material.JUNGLE_SAPLING);
        dupable.add(Material.OAK_SAPLING);
        dupable.add(Material.GRASS);
        dupable.add(Material.TALL_GRASS);
        dupable.add(Material.FERN);
        dupable.add(Material.DEAD_BUSH);
        dupable.add(Material.SEAGRASS);
        dupable.add(Material.DANDELION);
        dupable.add(Material.POPPY);
        dupable.add(Material.BLUE_ORCHID);
        dupable.add(Material.ALLIUM);
        dupable.add(Material.AZURE_BLUET);
        dupable.add(Material.RED_TULIP);
        dupable.add(Material.ORANGE_TULIP);
        dupable.add(Material.WHITE_TULIP);
        dupable.add(Material.PINK_TULIP);
        dupable.add(Material.OXEYE_DAISY);
        dupable.add(Material.CORNFLOWER);
        dupable.add(Material.LILY_OF_THE_VALLEY);
        dupable.add(Material.WITHER_ROSE);
        dupable.add(Material.BROWN_MUSHROOM);
        dupable.add(Material.RED_MUSHROOM);
        dupable.add(Material.CRIMSON_FUNGUS);
        dupable.add(Material.WARPED_FUNGUS);
        dupable.add(Material.CRIMSON_ROOTS);
        dupable.add(Material.WARPED_ROOTS);
        dupable.add(Material.WHEAT);
        dupable.add(Material.CARROTS);
        dupable.add(Material.POTATOES);
        dupable.add(Material.BEETROOTS);
        dupable.add(Material.MELON_STEM);
        dupable.add(Material.PUMPKIN_STEM);
        dupable.add(Material.NETHER_SPROUTS);
        dupable.add(Material.TWISTING_VINES);
        dupable.add(Material.SUGAR_CANE);
        dupable.add(Material.KELP);
        dupable.add(Material.BAMBOO);
        dupable.add(Material.CHORUS_PLANT);
        dupable.add(Material.CHORUS_FLOWER);
        dupable.add(Material.LADDER);
        dupable.add(Material.LEVER);
        dupable.add(Material.ACACIA_PRESSURE_PLATE);
        dupable.add(Material.BIRCH_PRESSURE_PLATE);
        dupable.add(Material.CRIMSON_PRESSURE_PLATE);
        dupable.add(Material.JUNGLE_PRESSURE_PLATE);
        dupable.add(Material.OAK_PRESSURE_PLATE);
        dupable.add(Material.SPRUCE_PRESSURE_PLATE);
        dupable.add(Material.STONE_PRESSURE_PLATE);
        dupable.add(Material.WARPED_PRESSURE_PLATE);
        dupable.add(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE);
        dupable.add(Material.DARK_OAK_PRESSURE_PLATE);
        dupable.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        dupable.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
        dupable.add(Material.SNOW);
        dupable.add(Material.CACTUS);
        dupable.add(Material.VINE);
        dupable.add(Material.BIRCH_BUTTON);
        dupable.add(Material.ACACIA_BUTTON);
        dupable.add(Material.CRIMSON_BUTTON);
        dupable.add(Material.DARK_OAK_BUTTON);
        dupable.add(Material.JUNGLE_BUTTON);
        dupable.add(Material.OAK_BUTTON);
        dupable.add(Material.POLISHED_BLACKSTONE_BUTTON);
        dupable.add(Material.SPRUCE_BUTTON);
        dupable.add(Material.STONE_BUTTON);
        dupable.add(Material.WARPED_BUTTON);
        dupable.add(Material.CYAN_CARPET);
        dupable.add(Material.BLACK_CARPET);
        dupable.add(Material.BLUE_CARPET);
        dupable.add(Material.BROWN_CARPET);
        dupable.add(Material.GRAY_CARPET);
        dupable.add(Material.GREEN_CARPET);
        dupable.add(Material.LIGHT_BLUE_CARPET);
        dupable.add(Material.LIGHT_GRAY_CARPET);
        dupable.add(Material.LIME_CARPET);
        dupable.add(Material.MAGENTA_CARPET);
        dupable.add(Material.ORANGE_CARPET);
        dupable.add(Material.PINK_CARPET);
        dupable.add(Material.PURPLE_CARPET);
        dupable.add(Material.RED_CARPET);
        dupable.add(Material.WHITE_CARPET);
        dupable.add(Material.YELLOW_CARPET);
        dupable.add(Material.SUNFLOWER);
        dupable.add(Material.LILAC);
        dupable.add(Material.ROSE_BUSH);
        dupable.add(Material.PEONY);
        dupable.add(Material.TALL_SEAGRASS);
        dupable.add(Material.LARGE_FERN);
        dupable.add(Material.SCAFFOLDING);
        dupable.add(Material.WARPED_WALL_SIGN);
        dupable.add(Material.WARPED_SIGN);
        dupable.add(Material.OAK_WALL_SIGN);
        dupable.add(Material.JUNGLE_WALL_SIGN);
        dupable.add(Material.JUNGLE_SIGN);
        dupable.add(Material.DARK_OAK_WALL_SIGN);
        dupable.add(Material.DARK_OAK_SIGN);
        dupable.add(Material.BLACK_BANNER);
        dupable.add(Material.BLACK_WALL_BANNER);
        dupable.add(Material.BLUE_BANNER);
        dupable.add(Material.BLUE_WALL_BANNER);
        dupable.add(Material.BROWN_BANNER);
        dupable.add(Material.BROWN_WALL_BANNER);
        dupable.add(Material.CYAN_BANNER);
        dupable.add(Material.CYAN_WALL_BANNER);
        dupable.add(Material.GRAY_BANNER);
        dupable.add(Material.GRAY_WALL_BANNER);
        dupable.add(Material.GREEN_BANNER);
        dupable.add(Material.GREEN_WALL_BANNER);
        dupable.add(Material.LIGHT_BLUE_BANNER);
        dupable.add(Material.LIGHT_BLUE_WALL_BANNER);
        dupable.add(Material.LIGHT_GRAY_BANNER);
        dupable.add(Material.LIGHT_GRAY_WALL_BANNER);
        dupable.add(Material.LIME_BANNER);
        dupable.add(Material.LIME_WALL_BANNER);
        dupable.add(Material.MAGENTA_BANNER);
        dupable.add(Material.MAGENTA_WALL_BANNER);
        dupable.add(Material.ORANGE_BANNER);
        dupable.add(Material.ORANGE_WALL_BANNER);
        dupable.add(Material.PINK_BANNER);
        dupable.add(Material.PINK_WALL_BANNER);
        dupable.add(Material.PURPLE_BANNER);
        dupable.add(Material.PURPLE_WALL_BANNER);
        dupable.add(Material.RED_BANNER);
        dupable.add(Material.RED_WALL_BANNER);
        dupable.add(Material.WHITE_BANNER);
        dupable.add(Material.WHITE_WALL_BANNER);
        dupable.add(Material.YELLOW_BANNER);
        dupable.add(Material.YELLOW_WALL_BANNER);
        dupable.add(Material.BELL);
        dupable.add(Material.PISTON);
        dupable.add(Material.STICKY_PISTON);
        dupable.add(Material.NETHER_WART_BLOCK);
        dupable.add(Material.REDSTONE_BLOCK);
        dupable.add(Material.TNT);
        dupable.add(Material.TRIPWIRE_HOOK);
    }

    public World getOverworld() {
        return overworld;
    }

    public World getSubworld() {
        return subworld;
    }

    public World getNether() {
        return nether;
    }

    public World getEnd() {
        return end;
    }

    public World getSpawn() {
        return spawn;
    }


    public void adjustChunks(int x, int z) {
        if (overworld == null || subworld == null || nether == null) return;
        var overChunk = overworld.getChunkAt(x, z);
        var subChunk = subworld.getChunkAt(x, z);
        var netherChunk = nether.getChunkAt(x, z);
        var container = ((PersistentDataHolder) netherChunk).getPersistentDataContainer();
        if (container.has(ADJUSTED_KEY, BooleanPersistentDataType.instance)) {
            return;
        }
        for (int tempX = 0; tempX < 16; tempX++) {
            for (int tempZ = 0; tempZ < 16; tempZ++) {
//                for (int tempY = 1; tempY < overworld.getMaxHeight(); tempY++) {
//                    var block = overChunk.getBlock(tempX, tempY, tempZ);
//                    var type = block.getType();
//                    if (type == Material.BEDROCK) {
//                        block.setType(Material.STONE);
//                    } else if (type == Material.DIAMOND_ORE || type == Material.EMERALD_ORE || type == Material.GOLD_ORE || type == Material.LAPIS_ORE || type == Material.REDSTONE_ORE) {
//                        block.setType(Material.IRON_ORE);
//                    }
//                }
//                for (int tempY = 127; tempY < 135; tempY++) { // subchunk create roof
//                    subChunk.getBlock(tempX, tempY, tempZ).setType(Material.BEDROCK, true);
//                }
//                for (int tempY = 119; tempY < 127; tempY++) { // copy over overworld blocks to subchunk
//                    var overBlock = overChunk.getBlock(tempX, tempY - 118, tempZ);
//                    var subBlock = subChunk.getBlock(tempX, tempY, tempZ);
//                    subBlock.setType(overBlock.getType(), true);
//                    subBlock.setBlockData(overBlock.getBlockData(), true);
//                }
//                for (int tempY = 1; tempY < 6; tempY++) { // get rid of all but y=0 bedrock in subworld
//                    var subBlock = subChunk.getBlock(tempX, tempY, tempZ);
//                    if (subBlock.getType() == Material.BEDROCK) {
//                        subBlock.setType(Material.STONE);
//                    }
//                }
                for (int tempY = 123; tempY < 128; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    if (netherBlock.getType() == Material.BEDROCK) {
                        netherBlock.setType(Material.NETHERRACK);
                    }
                }
                for (int tempY = 128; tempY < 144; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    if (Math.random() < Math.pow(tempY - 128.0, 3) / Math.pow(14.0, 3)) {
                        if (Math.random() < 0.01) {
                            netherBlock.setType(Material.CRYING_OBSIDIAN);
                        } else {
                            netherBlock.setType(Material.OBSIDIAN);
                        }
                    } else if (Math.random() < 0.02) {
                        netherBlock.setType(Material.NETHER_QUARTZ_ORE);
                    } else if (Math.random() < 0.0005) {
                        netherBlock.setType(Material.ANCIENT_DEBRIS);
                    } else if (Math.random() < 0.04) {
                        netherBlock.setType(Material.LAVA);
                    } else {
                        netherBlock.setType(Material.NETHERRACK);
                    }
                }
                for (int tempY = 144; tempY < 148; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    if (tempY < 146 && Math.random() < 0.0003) {
                        netherBlock.setType(Material.NETHERITE_BLOCK);
                    } else if (tempY >= 146 && Math.random() < 0.0006) {
                        netherBlock.setType(Material.DIAMOND_BLOCK);
                    } else if (Math.random() < 0.01) {
                        netherBlock.setType(Material.CRYING_OBSIDIAN);
                    } else {
                        netherBlock.setType(Material.OBSIDIAN);
                    }
                }
                for (int tempY = 148; tempY < 154; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    if (Math.random() < Math.pow(161 - tempY, 3) / Math.pow(14.0, 3)) {
                        if (Math.random() < 0.01) {
                            netherBlock.setType(Material.CRYING_OBSIDIAN);
                        } else {
                            netherBlock.setType(Material.OBSIDIAN);
                        }
                    } else if (Math.random() < 0.003) {
                        netherBlock.setType(Material.EMERALD_ORE);
                    } else if (Math.random() < 0.001) {
                        netherBlock.setType(Material.DIAMOND_ORE);
                    } else if (Math.random() < 0.04) {
                        netherBlock.setType(Material.LAVA);
                    } else {
                        netherBlock.setType(Material.STONE);
                    }
                }
                for (int tempY = 154; tempY < 162; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    var subBlock = subChunk.getBlock(tempX, tempY - 153, tempZ);
                    netherBlock.setType(subBlock.getType());
                    netherBlock.setBlockData(subBlock.getBlockData());
                }
                for (int tempY = 162; tempY < 172; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    netherBlock.setType(Material.BEDROCK);
                }
            }
        }
        container.set(ADJUSTED_KEY, BooleanPersistentDataType.instance, true);
    }

    public void adjustBlocks(Block block) {
        var world = block.getWorld();
        World otherWorld;
        var y = block.getY();
        if (world.equals(getOverworld())) {
            otherWorld = getSubworld();
            y += 118;
        } else {
            otherWorld = getOverworld();
            y -= 118;
        }

        var otherBlock = otherWorld.getBlockAt(block.getX(), y, block.getZ());
        otherBlock.setType(block.getType());
        otherBlock.setBlockData(block.getBlockData());
    }

    public void adjustBlocksNether(Block block) {
        var world = block.getWorld();
        if (block.getType() == Material.WATER) {
            block.setType(Material.AIR);
        }
        World otherWorld;
        var y = block.getY();
        if (world.equals(subworld)) {
            otherWorld = nether;
            y += 153;
        } else {
            otherWorld = subworld;
            y -= 153;
        }
        if (otherWorld == null) return;
        var otherBlock = otherWorld.getBlockAt(block.getX(), y, block.getZ());
        otherBlock.setType(block.getType());
        otherBlock.setBlockData(block.getBlockData());
    }

    public void blockEventCheck(@Nullable Block block, World world) {
        if (block == null) return;
        var y = block.getY();
        if (world.equals(overworld) && y <= 8 && y >= 1) {
            adjustBlocks(block);
        } else if (world.equals(subworld) && y <= 126 && y >= 119) {
            adjustBlocks(block);
        } else if (world.equals(subworld) && y <= 8 && y >= 1) {
            adjustBlocksNether(block);
        } else if (world.equals(nether) && y <= 161 && y >= 154) {
            adjustBlocksNether(block);
        }
    }


    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new WorldManager.EventListener(), Diplomacy.getInstance());
    }

    private class EventListener implements Listener {

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockBurn(BlockBurnEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
            var block2 = event.getIgnitingBlock();
            blockEventCheck(block2, world);

        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockCanBuild(BlockCanBuildEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockCook(BlockCookEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockDamage(BlockDamageEvent event) {
            var block = event.getBlock();

            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockDispense(BlockDispenseEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockDropItem(BlockDropItemEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockExp(BlockExpEvent event) {

            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockExplode(BlockExplodeEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockFadeEvent(BlockFadeEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockFertilizeEvent(BlockFertilizeEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockFromToEvent(BlockFromToEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
            var block2 = event.getToBlock();
            blockEventCheck(block2, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockGrow(BlockGrowEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            var n = event.getNewState();
            var state = block.getState();
            state.setBlockData(n.getBlockData());
            state.setData(n.getData());
            state.setType(n.getType());
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockIgnite(BlockIgniteEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
            var block2 = event.getIgnitingBlock();
            blockEventCheck(block2, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockPistonExtend(BlockPistonExtendEvent event) {
            var world = event.getBlock().getWorld();
            var y = event.getBlock().getY();
            if (world.equals(WorldManager.getInstance().overworld) && y <= 9) {
                event.setCancelled(true);
                return;
            } else if (world.equals(subworld) && y >= 118) {
                event.setCancelled(true);
                return;
            } else if (world.equals(subworld) && y <= 9) {
                event.setCancelled(true);
                return;
            } else if (world.equals(nether) && y >= 153) {
                event.setCancelled(true);
                return;
            }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockPistonRetract(BlockPistonRetractEvent event) {
            var world = event.getBlock().getWorld();
            var y = event.getBlock().getY();
            if (world.equals(WorldManager.getInstance().overworld) && y <= 9) {
                event.setCancelled(true);
                return;
            } else if (world.equals(subworld) && y >= 118) {
                event.setCancelled(true);
                return;
            } else if (world.equals(subworld) && y <= 9) {
                event.setCancelled(true);
                return;
            } else if (world.equals(nether) && y >= 153) {
                event.setCancelled(true);
                return;
            }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockPlace(BlockPlaceEvent event) {
            var block = event.getBlock();
            var type = block.getType();
            var world = block.getWorld();
            var y = block.getY();
            boolean dupable = WorldManager.this.dupable.contains(type);
            if (world.equals(WorldManager.getInstance().overworld) && block.getY() <= 9) {
                if (dupable) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot place this item below y = 10 in the overworld.");
                    return;
                }
            } else if (world.equals(subworld) && y >= 118) {
                if (dupable) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot place this item above y = 117 in the subworld.");
                    return;
                }
            } else if (world.equals(subworld) && y <= 9) {
                if (dupable) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot place this item below y = 10 in the subworld.");
                    return;
                }
            } else if (world.equals(nether) && y >= 153) {
                if (dupable) {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "You cannot place this item above y = 152 in the overworld.");
                    return;
                }
            }

            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockShearEntity(BlockShearEntityEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockBrew(BrewEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockBreak(BlockBreakEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockBrewingStandFuel(BrewingStandFuelEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onCauldronLevelChangeEvent(CauldronLevelChangeEvent event) {
            var block = event.getBlock();
            var n = event.getNewLevel();
            var data = block.getBlockData();
            ((Levelled) data).setLevel(n);
            block.setBlockData(data);
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onFluidLevelChange(FluidLevelChangeEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            var nData = event.getNewData();
            block.setBlockData(nData);
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBucketFill(PlayerBucketFillEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> blockEventCheck(block, world));
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBucketEmpty(PlayerBucketEmptyEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> blockEventCheck(block, world));
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onFurnaceBurn(FurnaceBurnEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onLeavesDecay(LeavesDecayEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onMoistureChange(MoistureChangeEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> blockEventCheck(block, world));
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onNotePlay(NotePlayEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onSignChange(SignChangeEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onSpongeAbsorb(SpongeAbsorbEvent event) {
            var blocks = event.getBlocks();
            for (var blockState : blocks) {
                var block = blockState.getBlock();
                var world = block.getWorld();
                blockEventCheck(block, world);
            }
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler
        private void onPortalCreate(PortalCreateEvent event) {
            var r = event.getReason();
            if (r == PortalCreateEvent.CreateReason.NETHER_PAIR || r == PortalCreateEvent.CreateReason.FIRE) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
