package com.ryanmichela.toxicskies;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.material.TrapDoor;

/**
 */
public abstract class SkyFinder {
    public abstract boolean canSeeSky(Location startLoc, int distance);

    protected boolean solidBlock(Block b) {
        return solidBlock(b.getType());
    }

    protected boolean solidBlock(Location l) {
        return solidBlock(l.getWorld().getBlockAt(l));
    }

    protected boolean solidBlock(Material type) {
         return  type != Material.AIR &&
                 type != Material.TORCH &&
                 type != Material.SUGAR_CANE_BLOCK &&
                 type != Material.SAPLING &&
                 type != Material.WEB &&
                 type != Material.LONG_GRASS &&
                 type != Material.DEAD_BUSH &&
                 type != Material.YELLOW_FLOWER &&
                 type != Material.RED_ROSE &&
                 type != Material.BROWN_MUSHROOM &&
                 type != Material.RED_MUSHROOM &&
                 type != Material.REDSTONE_TORCH_ON &&
                 type != Material.REDSTONE_TORCH_OFF &&
                 type != Material.REDSTONE_WIRE &&
                 type != Material.LEVER &&
                 type != Material.DIODE_BLOCK_ON &&
                 type != Material.DIODE_BLOCK_OFF &&
                 type != Material.VINE &&
                 type != Material.CAKE_BLOCK &&
                 type != Material.LADDER &&
                 type != Material.FENCE &&
                 type != Material.FENCE_GATE &&
                 type != Material.NETHER_FENCE &&
                 type != Material.IRON_FENCE &&
                 type != Material.RAILS &&
                 type != Material.POWERED_RAIL &&
                 type != Material.DETECTOR_RAIL &&
                 type != Material.SNOW &&
                 type != Material.WOODEN_DOOR &&
                 type != Material.IRON_DOOR_BLOCK &&
                 type != Material.TRAP_DOOR &&
                 type != Material.DOUBLE_PLANT;
    }

    protected boolean blockSeesSky(Block b) {
        return blockSeesSky(b.getX(), b.getY(), b.getZ(), b.getWorld());
    }

    protected boolean blockSeesSky(Location l) {
        return blockSeesSky(l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld());
    }

    protected boolean blockSeesSky(int x, int y, int z, World w) {
        if(y >= w.getHighestBlockYAt(x, z)) {
            for(int yy = y; yy < 256; yy++) {
                Material blockType = w.getBlockAt(x, yy, z).getType();
                if(solidBlock(blockType)) {
                    return false;
                } else if (blockType == Material.TRAP_DOOR) { // Trap doors conditionally block the sky
                    TrapDoor trapDoorBlock = (TrapDoor)w.getBlockAt(x, yy, z).getState().getData();
                    if (!trapDoorBlock.isOpen()) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
