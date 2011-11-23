package com.ryanmichela.toxicskies;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 */
public class RecursiveBlockWalker extends SkyFinder{

    private static HashSet<Location> path = new HashSet<Location>();

    public boolean canSeeSky(Location startLoc, int distance) {
        System.out.println("-----");
        return canSeeSkyRec(startLoc, distance, new HashMap<Location, Integer>());
    }

    private boolean canSeeSkyRec(Location cLoc, int depth, Map<Location, Integer> visited) {
        // We've evaluated this block before, return false
        if(visited.containsKey(cLoc)) {
            if(depth > visited.get(cLoc)) {
                System.out.println(loc2str(cLoc, depth, "L"));
                visited.put(cLoc, depth);
            } else {
                System.out.println(loc2str(cLoc, depth, "v"));
                return false;
            }
        } else {
            // Note where we have been
            visited.put(cLoc, depth);
        }

        Block cBlock = cLoc.getBlock();

        // We've gone too far, return false
        if(depth <= 0) {
            System.out.println(loc2str(cLoc, depth, "d"));
            return false;
        }
        // This block is not air, return false
        if(solidBlock(cBlock)) {
            System.out.println(loc2str(cLoc, depth, "s"));
            return false;
        }

        // This block sees the sky! return true
        if(blockSeesSky(cBlock)) {
            path.add(cLoc);
            System.out.println(loc2str(cLoc, depth, "!"));
            return true;
        }



        // Begin recursive stage
        System.out.println(loc2str(cLoc, depth, "r"));
        boolean r = (canSeeSkyRec(cLoc.clone().add(0, 1, 0), depth - 1, visited) ||
                canSeeSkyRec(cLoc.clone().add(1, 0, 0), depth - 1, visited) ||
                canSeeSkyRec(cLoc.clone().add(0, 0, 1), depth - 1, visited) ||
                canSeeSkyRec(cLoc.clone().subtract(1, 0, 0), depth - 1, visited) ||
                canSeeSkyRec(cLoc.clone().subtract(0, 0, 1), depth - 1, visited) ||
                canSeeSkyRec(cLoc.clone().subtract(0, 1, 0), depth - 1, visited));

        if(r) {
            System.out.println(loc2str(cLoc, depth, "!"));
        }


        return r;
    }
}
