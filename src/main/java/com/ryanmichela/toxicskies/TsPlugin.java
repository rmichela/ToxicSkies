package com.ryanmichela.toxicskies;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 */
public class TsPlugin extends JavaPlugin  implements Listener
{
    private static Plugin instance;
    public TsPlugin() {
        instance = this;
    }

    public void onEnable() {
        saveDefaultConfig();

        for (String worldName : TsSettings.getAffectedWorlds()) {
            getLogger().info("Making the skies toxic in " + worldName);
            AmbianceTask task = new AmbianceTask(this, worldName);
            getServer().getScheduler().scheduleSyncDelayedTask(this, task);
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
//        getLogger().warning("Player Login");
        PoisonCheckTask task = new PoisonCheckTask(this, event.getPlayer());
        getServer().getScheduler().scheduleSyncDelayedTask(this, task, 20*10);
    }

    static Plugin getInstance() {
        return instance;
    }
}
