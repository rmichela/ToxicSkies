package com.ryanmichela.toxicskies;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class PoisonCheckTask implements Runnable {
    private int RADIUS_TO_SEEK_SKY = 7;

    private Plugin plugin;
    private Player player;
    private Random r = new Random();

    public PoisonCheckTask(Plugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        if (player.isOnline() && TsSettings.playerInAffectedWorld(player) && modeAllowsDamage(player) && player.getGameMode() != GameMode.CREATIVE) {
            try
            {
                Location playerHead = normalize(player.getLocation()).add(0, 1, 0);
                SkyFinder skyFinder = new DepthFirstSkyFinder();

                if (skyFinder.canSeeSky(playerHead, RADIUS_TO_SEEK_SKY)) {
                    Runnable nextTask;
                    if (player.getInventory().getHelmet() != null &&
                        player.getInventory().getHelmet().getType() == Material.PUMPKIN) {
                        nextTask = new PumpkinDecayTask(player);
                    } else {
                        nextTask = new DamageApplyTask(player);
                    }
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, nextTask);
                } else {
                    MessageTracker.sendMessage(player, TsSettings.getCleanAirMessage());
                }
            } catch (Throwable t) {
                plugin.getLogger().severe(t.toString());
            }
        }
        if (plugin.isEnabled()) {
            int offset = r.nextInt(20);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, TsSettings.getSecondsBetweenPolls() + offset);
        }
    }

    private Location normalize(Location l) {
        return new Location(l.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
    }

    private boolean modeAllowsDamage(Player p) {
        if (TsSettings.getMode() == 1 || TsSettings.getMode() == 2) {
            return true;
        }
        return p.getWorld().hasStorm();
    }
}
