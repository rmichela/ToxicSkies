package com.ryanmichela.toxicskies;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class AmbianceTask implements Runnable {
    private static int TICKS_PER_SECOND = 20;
    private int SECONDS_BETWEEN_RESETS = 30;
    private Plugin plugin;
    private String worldName;

    public AmbianceTask(Plugin plugin, String worldName) {
        this.plugin = plugin;
        this.worldName = worldName;
    }

    public void run() {
//        plugin.getLogger().warning("Updating weather");
        try {
            World world = plugin.getServer().getWorld(worldName);
            world.setStorm(true);
            world.setWeatherDuration(TICKS_PER_SECOND * SECONDS_BETWEEN_RESETS + 5);
            world.setThundering(true);
            world.setThunderDuration(TICKS_PER_SECOND * SECONDS_BETWEEN_RESETS + 5);
        } catch (NullPointerException e) {}

        if (TsPlugin.getInstance().isEnabled()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(TsPlugin.getInstance(), this, TICKS_PER_SECOND * SECONDS_BETWEEN_RESETS);
        }
    }
}