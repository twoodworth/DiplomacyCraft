package me.tedwoodworth.diplomacy.geology;

import me.tedwoodworth.diplomacy.Diplomacy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.FluidLevelChangeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class FluidDynamics {
    public static FluidDynamics instance = null;

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new FluidDynamics.EventListener(), Diplomacy.getInstance());
    }

    public static FluidDynamics getInstance() {
        if (instance == null) {
            instance = new FluidDynamics();
        }
        return instance;
    }

    private FluidDynamics() {

    }

    /**
     * Calculates the fluid level (0-8) of a given block.
     * 0 means the block is not a fluid, and 1-8 represents the
     * fluid level, where 8 is the highest possible level.
     *
     * @param block: fluid block
     * @return fluid level of block
     * @throws IllegalArgumentException: if block is not a fluid
     */
    public int getFluidLevel(Block block) throws IllegalArgumentException {
        if (!isFluid(block)) throw new IllegalArgumentException("Block must be a fluid");
        var data = block.getBlockData();
        if (!(data instanceof Levelled)) return 0;
        var level = ((Levelled) data).getLevel();

        if (level == 0) return 8;
        else return level;
    }

    public boolean isFluid(Block block) {
        return block != null
                && (block.getType() == Material.AIR
                || ((block.getType() == Material.WATER
                || block.getType() == Material.LAVA)
                && block.getBlockData() instanceof Levelled));
    }

    /**
     * Sets the fluid level of a block between 0-8. 0 sets the block to air.
     *
     * @param block: Block to set fluid level
     * @param type:  Type of fluid (water/lava)
     * @param level: Level to set
     * @throws IndexOutOfBoundsException: level provided is outside the range of 0-8
     * @throws IllegalArgumentException:  Block cannot have fluid, or type is not water or lava
     */
    public void setFluidLevel(Block block, Material type, int level) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (level < 0 || level > 8)
            throw new IndexOutOfBoundsException("Level cannot be less than 0 or greater than 8");
        if (type != Material.WATER && type != Material.LAVA)
            throw new IllegalArgumentException("Type must be lava or water");
        if (level == 0) {
            block.setType(Material.AIR);
        } else {
            if (block.getType() == Material.AIR) {
                block.setType(type);
            }
            var data = block.getBlockData();
            if (!(data instanceof Levelled)) throw new IllegalArgumentException("Block must be water, lava, or air");
            if (level == 8) {
                ((Levelled) data).setLevel(0);
            } else {
                ((Levelled) data).setLevel(level);
            }
            block.setBlockData(data);
        }
    }

    public void liquidFlow(Block block, Material type) {
        if (block == null || (block.getType() != type && block.getType() != Material.AIR)) return;

        var level = getFluidLevel(block);
        var max = 8;
        var callAgain = false;

        var geo = GeoData.getInstance();
        var u = geo.getRelativeBlock(BlockFace.UP, block);
        var d = geo.getRelativeBlock(BlockFace.DOWN, block);
        var n = geo.getRelativeBlock(BlockFace.NORTH, block);
        var s = geo.getRelativeBlock(BlockFace.SOUTH, block);
        var e = geo.getRelativeBlock(BlockFace.EAST, block);
        var w = geo.getRelativeBlock(BlockFace.WEST, block);

        var scheduler = Bukkit.getScheduler();
        if (isFluid(u) && level < max) { // is a fluid and current is not full

            // get the level
            var uLevel = getFluidLevel(u);

            // get the combined levels
            var combined = level + uLevel;
            uLevel = Math.max(0, combined - max); // determine what new uLevel will be
            level = combined - level; // determine what new level will be

            // set the new levels
            setFluidLevel(block, type, level);
            setFluidLevel(u, type, uLevel);
            callAgain = true;
        }

        if (level != 0 && isFluid(d)) { // if DOWN is water/lava/air
            // get DOWN level
            var dLevel = getFluidLevel(d);
            if (dLevel < max) { // if DOWN isn't already full
                var combined = level + dLevel;
                level = Math.max(0, combined - max);
                dLevel = combined - level;

                setFluidLevel(d, type, dLevel);
                setFluidLevel(block, type, level);
                callAgain = true;
            }
        }

        var sides = new ArrayList<Block>();
        var count = 0;
        var total = 0;
        var low = 8;
        var high = 0;

        // add blocks to array
        sides.add(block);
        sides.add(n);
        sides.add(s);
        sides.add(e);
        sides.add(w);

        for (var item : new ArrayList<>(sides)) {
            if (isFluid(item) && item.getType() == type) {
                var l = getFluidLevel(item);
                count++;
                total += l;
                low = Math.min(low, l);
                high = Math.max(high, l);
            } else {
                sides.remove(item);
            }
        }

        if (high - low > 1) {
            Collections.shuffle(sides);
            var avg = total / count;
            var remainder = total % count;
            for (var item : sides) {
                if (remainder > 0) {
                    setFluidLevel(item, type, avg + 1);
                    remainder--;
                } else {
                    setFluidLevel(item, type, avg);
                }
            }
            callAgain = true;
        }


//        if (d != null && level == 1) {
//            var possibleSides = new ArrayList<Block>();
//
//            if (n.getType() == Material.AIR) {
//                var below = geo.getRelativeBlock(BlockFace.DOWN, n);
//                if (isFluid(below) && getFluidLevel(below) < 8) {
//                    possibleSides.add(n);
//                }
//            }
//            if (s.getType() == Material.AIR) {
//                var below = geo.getRelativeBlock(BlockFace.DOWN, s);
//                if (isFluid(below) && getFluidLevel(below) < 8) {
//                    possibleSides.add(s);
//                }
//            }
//            if (e.getType() == Material.AIR) {
//                var below = geo.getRelativeBlock(BlockFace.DOWN, e);
//                if (isFluid(below) && getFluidLevel(below) < 8) {
//                    possibleSides.add(e);
//                }
//            }
//            if (w.getType() == Material.AIR) {
//                var below = geo.getRelativeBlock(BlockFace.DOWN, w);
//                if (isFluid(below) && getFluidLevel(below) < 8) {
//                    possibleSides.add(w);
//                }
//            }
//            if (possibleSides.size() > 0) {
//                var to = possibleSides.get((int) (Math.random() * possibleSides.size()));
//                setFluidLevel(to, type, 1);
//                setFluidLevel(block, type, 0);
//                callAgain = true;
//            }
//        }

        // schedule new liquidFlows
        if (callAgain) {
            long time;
            if (type == Material.WATER) time = 10L;
            else time = 20L;
            scheduler.runTaskLater(Diplomacy.getInstance(), () -> liquidFlow(block, type), time);
            scheduler.runTaskLater(Diplomacy.getInstance(), () -> liquidFlow(d, type), time);
            scheduler.runTaskLater(Diplomacy.getInstance(), () -> liquidFlow(u, type), time);
            scheduler.runTaskLater(Diplomacy.getInstance(), () -> liquidFlow(n, type), time);
            scheduler.runTaskLater(Diplomacy.getInstance(), () -> liquidFlow(s, type), time);
            scheduler.runTaskLater(Diplomacy.getInstance(), () -> liquidFlow(e, type), time);
            scheduler.runTaskLater(Diplomacy.getInstance(), () -> liquidFlow(w, type), time);
        }
    }

    private class EventListener implements Listener {

        //todo enable fluid dynamics
//        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//        void onLiquidFlow(BlockFromToEvent event) {
//            var block = event.getBlock();
//            var type = block.getType();
//            if (type == Material.WATER) {
//                event.setCancelled(true);
//                liquidFlow(block, Material.WATER);
//            } else if (type == Material.LAVA) {
//                event.setCancelled(true);
//                liquidFlow(block, Material.LAVA);
//            }
//        }
//
//        @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
//        void onFluidLevelChange(FluidLevelChangeEvent event) {
//            event.setCancelled(true);
//        }
//
//        @EventHandler
//        void onBlockBreak(BlockBreakEvent event) {
//            var block = event.getBlock();
//            var scheduler = Bukkit.getScheduler();
//            scheduler.runTaskLater(Diplomacy.getInstance(), () -> liquidFlow(block, Material.WATER), 10L);
//            scheduler.runTaskLater(Diplomacy.getInstance(), () -> liquidFlow(block, Material.LAVA), 20L);
//        }
    }
}
