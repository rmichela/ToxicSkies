package com.ryanmichela.toxicskies;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.material.Door;

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
                        considerAdjacency(l, l.clone().add(1, 0, 0), adj);
                        considerAdjacency(l, l.clone().add(-1, 0, 0), adj);
                        considerAdjacency(l, l.clone().add(0, 1, 0), adj);
                        considerAdjacency(l, l.clone().add(0, -1, 0), adj);
                        considerAdjacency(l, l.clone().add(0, 0, 1), adj);
                        considerAdjacency(l, l.clone().add(0, 0, -1), adj);
                    }
                }
            }
        }

        // 2. Depth first search the void-adjacency matrix for sky-seeing blocks
        Map<Location, Integer> seen = new HashMap<Location, Integer>();
        return canSeeSkyRec(startLoc, distance, adj, seen);
    }

    private void considerAdjacency(Location base, Location offset, Map<Location, Set<Location>> adj) {
        int offsetType = offset.getWorld().getBlockTypeIdAt(offset);
        if (!solidBlock(offsetType)) {
            if (offsetType == Material.IRON_DOOR.getId() || offsetType == Material.WOODEN_DOOR.getId()) {
                Door doorBlock = (Door)offset.getWorld().getBlockAt(offset);
                if (doorBlock.isOpen()) {
                    setAdjacency(base, offset, adj);
                }
            } else {
                setAdjacency(base, offset, adj);
            }
        }
    }

    private boolean canSeeSkyRec(Location l, int depth, Map<Location, Set<Location>> adj, Map<Location, Integer> seen) {
        // ran out of depth
        if (depth == 0) {
            return false;
        }
        // found sky
        if (blockSeesSky(l)) {
            return true;
        }

        Set<Location> adjToThis = adj.get(l);
        if (adjToThis != null) {
            boolean found = false;
            for (Location m : adjToThis) {
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
        } else {
            return false;
        }
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
