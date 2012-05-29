package com.ryanmichela.toxicskies;

import org.apache.commons.lang.time.StopWatch;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 */
public class PlayerMoveHandler implements Listener {
    private boolean toggle;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockY() == event.getTo().getBlockY() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        StopWatch sw = new StopWatch();
        sw.start();

//        SkyFinder skyFinder = new RecursiveBlockWalker();
        SkyFinder skyFinder = new DepthFirstSkyFinder();

        Location from = event.getFrom();
        Location to = event.getTo();

        boolean canSeeSky = skyFinder.canSeeSky(normalize(to).add(0, 1, 0), 10);

        if(from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            if(canSeeSky) {
                if(!toggle) {
                    toggle = true;
                    event.getPlayer().sendMessage("Sky");
                }
            } else {
                if(toggle) {
                    toggle = false;
                    event.getPlayer().sendMessage("No Sky");
                }
            }
        }
        sw.stop();
        System.out.println(sw.getTime() + ":" + canSeeSky);
    }

    private Location normalize(Location l) {
        l.setX(l.getBlockX());
        l.setY(l.getBlockY());
        l.setZ(l.getBlockZ());
        l.setPitch(0);
        l.setYaw(0);
        return l;
    }
}
