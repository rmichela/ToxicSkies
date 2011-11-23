package com.ryanmichela.toxicskies;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 */
public class PlayerMoveHandler extends PlayerListener {
    private boolean toggle;

    @Override
    public void onPlayerMove(PlayerMoveEvent event) {
        SkyFinder skyFinder = new RecursiveBlockWalker();

        Location from = event.getFrom();
        Location to = event.getTo();

        if(from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ()) {
            if(skyFinder.canSeeSky(to.add(0, 1, 0), 6)) {
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
    }
}
