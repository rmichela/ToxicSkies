package com.ryanmichela.toxicskies;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PoisonCheckTask implements Runnable {
    private int RADIUS_TO_SEEK_SKY = 7;

    private Plugin plugin;
    private Player player;

    public PoisonCheckTask(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        if (player.isOnline()) {

            Location playerHead = normalize(player.getLocation()).add(0, 1, 0);
            SkyFinder skyFinder = new DepthFirstSkyFinder();

            if (skyFinder.canSeeSky(playerHead, RADIUS_TO_SEEK_SKY)) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new DamageApplyTask(player));
            }

            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, this, TsSettings.getSecondsBetweenPolls());
        }
    }

    private Location normalize(Location l) {
        return new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }
}
