package com.ryanmichela.toxicskies;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PoisonCheckTask task = new PoisonCheckTask(this, event.getPlayer());
        getServer().getScheduler().scheduleAsyncDelayedTask(this, task, 20*10);
    }

    static Plugin getInstance() {
        return instance;
    }
}
