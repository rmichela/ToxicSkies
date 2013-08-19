package com.ryanmichela.toxicskies;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class AmbianceTask implements Runnable {
    private static int TICKS_PER_SECOND = 20;
    private int SECONDS_BETWEEN_RESETS = 30;
    private World world;

    public AmbianceTask(World world) {
        this.world = world;
    }

    public void run() {
        world.setStorm(true);
        world.setWeatherDuration(TICKS_PER_SECOND * SECONDS_BETWEEN_RESETS + 5);
        world.setThundering(true);
        world.setThunderDuration(TICKS_PER_SECOND * SECONDS_BETWEEN_RESETS + 5);

        if (TsPlugin.getInstance().isEnabled()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(TsPlugin.getInstance(), this, TICKS_PER_SECOND * SECONDS_BETWEEN_RESETS);
        }
    }
}