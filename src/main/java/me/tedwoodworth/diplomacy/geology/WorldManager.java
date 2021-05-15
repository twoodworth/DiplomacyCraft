package me.tedwoodworth.diplomacy.geology;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.data.BooleanPersistentDataType;
import me.tedwoodworth.diplomacy.players.DiplomacyPlayers;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.BrewingStandFuelEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.jetbrains.annotations.Nullable;

public class WorldManager {

    public final World subworld = Bukkit.createWorld(new WorldCreator("subworld"));
    public final World overworld = Bukkit.getWorld("world");
    public final World nether = Bukkit.getWorld("world_nether");
    public final World end = Bukkit.getWorld("world_the_end");
    private static WorldManager instance = null;
    public final NamespacedKey ADJUSTED_KEY = new NamespacedKey(Diplomacy.getInstance(), "chunk_is_adjusted");

    public static WorldManager getInstance() {
        if (instance == null) {
            instance = new WorldManager();
        }
        return instance;
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


    public void adjustChunks(int x, int z) {
        if (overworld == null || subworld == null || nether == null) return;
        var overChunk = overworld.getChunkAt(x, z);
        var subChunk = subworld.getChunkAt(x, z);
        var netherChunk = nether.getChunkAt(x, z);
        var container = ((PersistentDataHolder) subChunk).getPersistentDataContainer();
        if (container.has(ADJUSTED_KEY, BooleanPersistentDataType.instance)) {
            return;
        }
        for (int tempX = 0; tempX < 16; tempX++) {
            for (int tempZ = 0; tempZ < 16; tempZ++) {

                for (int tempY = 127; tempY < 135; tempY++) { // subchunk create roof
                    subChunk.getBlock(tempX, tempY, tempZ).setType(Material.BEDROCK, true);
                }
                for (int tempY = 119; tempY < 127; tempY++) { // copy over overworld blocks to subchunk
                    var overBlock = overChunk.getBlock(tempX, tempY - 118, tempZ);
                    var subBlock = subChunk.getBlock(tempX, tempY, tempZ);
                    subBlock.setType(overBlock.getType(), true);
                    subBlock.setBlockData(overBlock.getBlockData(), true);
                }
                for (int tempY = 1; tempY < 6; tempY++) { // get rid of all but y=0 bedrock in subworld
                    var subBlock = subChunk.getBlock(tempX, tempY, tempZ);
                    if (subBlock.getType() == Material.BEDROCK) {
                        subBlock.setType(Material.STONE);
                    }
                }
                for (int tempY = 123; tempY < 128; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    if (netherBlock.getType() == Material.BEDROCK) {
                        netherBlock.setType(Material.NETHERRACK);
                    }
                }
                for (int tempY = 128; tempY < 144; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    if (Math.random() < (tempY - 128.0) / 16.0) {
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
                for (int tempY = 148; tempY < 162; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    if (Math.random() < (161 - tempY) / 16.0) {
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
                for (int tempY = 162; tempY < 172; tempY++) {
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    netherBlock.setType(Material.BEDROCK);
                }

                for (int tempY = 154; tempY < 162; tempY++) { // copy over overworld blocks to subchunk
                    var netherBlock = netherChunk.getBlock(tempX, tempY, tempZ);
                    var subBlock = subChunk.getBlock(tempX, tempY - 153, tempZ);
                    if (subBlock.getType() == Material.LAVA || subBlock.getType() == Material.AIR || subBlock.getType() == Material.CAVE_AIR || subBlock.getType() == Material.VOID_AIR) {
                        netherBlock.setType(subBlock.getType());
                        netherBlock.setBlockData(subBlock.getBlockData());
                    } else {
                        subBlock.setType(netherBlock.getType(), true);
                        subBlock.setBlockData(netherBlock.getBlockData(), true);
                    }
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
        World otherWorld;
        var y = block.getY();
        if (world.equals(subworld)) {
            otherWorld = nether;
            y += 153;
        } else {
            otherWorld = subworld;
            y -= 153;
        }

        var otherBlock = otherWorld.getBlockAt(block.getX(), y, block.getZ());
        otherBlock.setType(block.getType());
        otherBlock.setBlockData(block.getBlockData());
    }
    public void blockEventCheck(@Nullable Block block, World world) {
        if (block == null) return;
        var y = block.getY();
        if (world.equals(overworld) && y <= 8 && y >= 1) {
            Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> adjustBlocks(block));
        } else if (world.equals(subworld) && y <= 126 && y >= 119) {
            Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> adjustBlocks(block));
        } else if (world.equals(subworld) && y <= 8 && y >= 1) {
            Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> adjustBlocksNether(block));
        } else if (world.equals(nether) && y <= 161 && y >= 154) {
            Bukkit.getScheduler().runTask(Diplomacy.getInstance(), () -> adjustBlocksNether(block));
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
            var blocks = event.getBlocks();
            for (var block : blocks) {
                var world = block.getWorld();
                blockEventCheck(block, world);
            }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockPistonRetract(BlockPistonRetractEvent event) {
            var blocks = event.getBlocks();
            for (var block : blocks) {
                var world = block.getWorld();
                blockEventCheck(block, world);
            }
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockPlace(BlockPlaceEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onBlockRedstone(BlockRedstoneEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
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
            var world = block.getWorld();
            blockEventCheck(block, world);
        }

        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        private void onFluidLevelChange(FluidLevelChangeEvent event) {
            var block = event.getBlock();
            var world = block.getWorld();
            blockEventCheck(block, world);
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
            blockEventCheck(block, world);
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
