package com.ryanmichela.toxicskies;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Door;
import org.bukkit.material.TrapDoor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DepthFirstSkyFinder extends SkyFinder {



    @Override
    public boolean canSeeSky(Location startLoc, int distance) {
        // 0. Scan the top layer to see if there is even a possibility of seeing the sky
        boolean somethingSawSky = false;
        outer:for(int x = startLoc.getBlockX() - distance; x < startLoc.getBlockX() + distance; x++) {
            for(int z = startLoc.getBlockZ() - distance; z < startLoc.getBlockZ() + distance; z++) {
                if (startLoc.getBlockY() + distance >= startLoc.getWorld().getHighestBlockYAt(x, z)) {
                    somethingSawSky = true;
                    break outer;
                }
            }
        }
        if (!somethingSawSky) {
            return false;
        }


        // 1. Build the void-adjacency list
        HashMap<Location, Set<Location>> adj = new HashMap<Location, Set<Location>>();

        for(int x = startLoc.getBlockX() - distance; x < startLoc.getBlockX() + distance; x++) {
            for(int y = startLoc.getBlockY() - distance; y < startLoc.getBlockY() + distance; y++) {
                for(int z = startLoc.getBlockZ() - distance; z < startLoc.getBlockZ() + distance; z++) {
                    Location l = new Location(startLoc.getWorld(), x, y, z);
                    if (!solidBlock(l)) {
                        considerAdjacency(l, l.clone().add(1, 0, 0), BlockFace.SOUTH, adj);
                        considerAdjacency(l, l.clone().add(-1, 0, 0), BlockFace.NORTH, adj);
                        considerAdjacency(l, l.clone().add(0, 1, 0), BlockFace.UP, adj);
                        considerAdjacency(l, l.clone().add(0, -1, 0), BlockFace.DOWN, adj);
                        considerAdjacency(l, l.clone().add(0, 0, 1), BlockFace.WEST, adj);
                        considerAdjacency(l, l.clone().add(0, 0, -1), BlockFace.EAST, adj);
                    }
                }
            }
        }

        // 2. Depth first search the void-adjacency matrix for sky-seeing blocks
        Map<Location, Integer> seen = new HashMap<Location, Integer>();
        return canSeeSkyRec(startLoc, distance, adj, seen);
    }

    private void considerAdjacency(Location base, Location offset, BlockFace fromDirection, Map<Location, Set<Location>> adj) {
        if (!solidBlock(offset) && passable(offset, oppositeDirection(fromDirection)) && passable(base, fromDirection)) {
            setAdjacency(base, offset, adj);
        }
    }

    private BlockFace oppositeDirection(BlockFace d) {
        if (d == BlockFace.WEST) return BlockFace.EAST;
        if (d == BlockFace.EAST) return BlockFace.WEST;
        if (d == BlockFace.SOUTH) return BlockFace.NORTH;
        if (d == BlockFace.NORTH) return BlockFace.SOUTH;
        if (d == BlockFace.UP) return BlockFace.DOWN;
        if (d == BlockFace.DOWN) return BlockFace.UP;
        return BlockFace.SELF;
    }

    private boolean passable(Location l, BlockFace fromDirection) {
        Material blockType = l.getWorld().getBlockAt(l).getType();
        // Deal with doors being open
        if (blockType == Material.IRON_DOOR_BLOCK || blockType == Material.WOODEN_DOOR) {
            if (isVerticallyAxial(fromDirection))  {
                return true;
            } else {
                Door doorBlock = (Door)l.getBlock().getState().getData();
                if (doorBlock.isTopHalf()) {
                    return passable(l.clone().subtract(0,1,0), fromDirection); // Needed because the tops of doors are always closed
                } else {
                    return !doorBlocksDirection(doorBlock, fromDirection);
                }
            }
        // Deal with hatches being open
        } else if (blockType == Material.TRAP_DOOR) {
            TrapDoor trapDoorBlock = (TrapDoor)l.getBlock().getState().getData();
            if (trapDoorBlock.isOpen() || isHorizontallyPlanar(fromDirection)) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private boolean isHorizontallyPlanar(BlockFace b) {
        return b == BlockFace.NORTH || b == BlockFace.SOUTH || b == BlockFace.EAST || b == BlockFace.WEST;
    }

    private boolean isVerticallyAxial(BlockFace b) {
        return b == BlockFace.UP || b == BlockFace.DOWN;
    }

    private boolean doorBlocksDirection(Door door, BlockFace fromDirection) {
        if ((door.getHingeCorner() == BlockFace.NORTH_EAST && door.isOpen() && fromDirection == BlockFace.EAST) ||
            (door.getHingeCorner() == BlockFace.NORTH_EAST && !door.isOpen() && fromDirection == BlockFace.NORTH) ||
            (door.getHingeCorner() == BlockFace.SOUTH_EAST && door.isOpen() && fromDirection == BlockFace.SOUTH) ||
            (door.getHingeCorner() == BlockFace.SOUTH_EAST && !door.isOpen() && fromDirection == BlockFace.EAST) ||
            (door.getHingeCorner() == BlockFace.NORTH_WEST && door.isOpen() && fromDirection == BlockFace.NORTH) ||
            (door.getHingeCorner() == BlockFace.NORTH_WEST && !door.isOpen() && fromDirection == BlockFace.WEST) ||
            (door.getHingeCorner() == BlockFace.SOUTH_WEST && door.isOpen() && fromDirection == BlockFace.WEST) ||
            (door.getHingeCorner() == BlockFace.SOUTH_WEST && !door.isOpen() && fromDirection == BlockFace.SOUTH)) {
            return true;
        } else {
            return false;//!isVerticallyAxial(fromDirection);
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
