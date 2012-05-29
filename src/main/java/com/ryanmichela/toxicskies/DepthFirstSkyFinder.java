package com.ryanmichela.toxicskies;

import org.bukkit.Location;

import java.util.*;

public class DepthFirstSkyFinder extends SkyFinder {



    @Override
    public boolean canSeeSky(Location startLoc, int distance) {

        // 1. Build the void-adjacency list
        HashMap<Location, Set<Location>> adj = new HashMap<Location, Set<Location>>();

        for(double x = startLoc.getBlockX() - distance; x < startLoc.getBlockX() + distance; x++) {
            for(double y = startLoc.getBlockY() - distance; y < startLoc.getBlockY() + distance; y++) {
                for(double z = startLoc.getBlockZ() - distance; z < startLoc.getBlockZ() + distance; z++) {
                    Location l = new Location(startLoc.getWorld(), x, y, z);
                    if (!solidBlock(l)) {
                        Location xp = l.clone().add(1, 0, 0);
                        Location xm = l.clone().add(-1, 0, 0);
                        Location yp = l.clone().add(0, 1, 0);
                        Location ym = l.clone().add(0, -1, 0);
                        Location zp = l.clone().add(0, 0, 1);
                        Location zm = l.clone().add(0, 0, -1);

                        if (!solidBlock(xp)) {
                            setAdjacency(l, xp, adj);
                        }
                        if (!solidBlock(xm)) {
                            setAdjacency(l, xm, adj);
                        }
                        if (!solidBlock(yp)) {
                            setAdjacency(l, yp, adj);
                        }
                        if (!solidBlock(ym)) {
                            setAdjacency(l, ym, adj);
                        }
                        if (!solidBlock(zp)) {
                            setAdjacency(l, zp, adj);
                        }
                        if (!solidBlock(zm)) {
                            setAdjacency(l, zm, adj);
                        }
                    }
                }
            }
        }

        // 2. Depth first search the void-adjacency matrix for sky-seeing blocks
        Map<Location, Integer> seen = new HashMap<Location, Integer>();
        return canSeeSkyRec(startLoc, distance, adj, seen);
    }

    private boolean canSeeSkyRec(Location l, int depth, Map<Location, Set<Location>> adj, Map<Location, Integer> seen){
        // ran out of depth
        if (depth == 0) {
            return false;
        }
        // found sky
        if (blockSeesSky(l)) {
            return true;
        }

        boolean found = false;
        for (Location m : adj.get(l)) {
            // Only evaluate/reevaluate a location if we have found a shorter way to get to it
            Integer lastSeen = seen.get(m);
            if (lastSeen == null || lastSeen < depth - 1) {
                seen.put(m, depth);
            } else {
                continue;
            }

            if (canSeeSkyRec(m, depth - 1, adj, seen)) {
                found = true;
                break;
            }
        }
        return found;
    }

    private void setAdjacency(Location from, Location to, Map<Location, Set<Location>> adj) {
        if (!adj.containsKey(from)) {
            adj.put(from, new HashSet<Location>());
        }
        if (!adj.containsKey(to)) {
            adj.put(to, new HashSet<Location>());
        }
        adj.get(from).add(to);
        adj.get(to).add(from);
    }
}
