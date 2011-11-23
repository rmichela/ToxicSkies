package com.ryanmichela.toxicskies;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 */
public abstract class SkyFinder {
    public abstract boolean canSeeSky(Location startLoc, int distance);

    protected boolean solidBlock(Block b) {
         Material m = b.getType();
         return m != Material.AIR &&
                m != Material.TORCH &&
                m != Material.SUGAR_CANE_BLOCK &&
                m != Material.SAPLING &&
                m != Material.WEB &&
                m != Material.LONG_GRASS &&
                m != Material.DEAD_BUSH &&
                m != Material.YELLOW_FLOWER &&
                m != Material.RED_ROSE &&
                m != Material.BROWN_MUSHROOM &&
                m != Material.RED_MUSHROOM &&
                m != Material.REDSTONE_TORCH_ON &&
                m != Material.REDSTONE_TORCH_OFF &&
                m != Material.REDSTONE_WIRE &&
                m != Material.VINE &&
                m != Material.CAKE_BLOCK &&
                m != Material.LADDER &&
                m != Material.FENCE &&
                m != Material.FENCE_GATE &&
                m != Material.RAILS &&
                m != Material.POWERED_RAIL &&
                m != Material.DETECTOR_RAIL;
    }

    protected boolean blockSeesSky(Block b) {
        if(b.getY() >= b.getWorld().getHighestBlockYAt(b.getLocation())) {
            int x = b.getX();
            int z = b.getZ();
            World w = b.getWorld();
            for(int y = b.getY(); y < 128; y++) {
                if(solidBlock(w.getBlockAt(x, y, z))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    protected String loc2str(Location l, int d, String r) {
        String s = r + " ";
        for(int i = 0; i < 10-d; i++) s += "..";
        s += "x:" + (int)Math.floor(l.getX()) + " y:" + (int)Math.floor(l.getY()) + " z:" + (int)Math.floor(l.getZ());
        return s;
    }
}
