package com.ryanmichela.toxicskies;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 */
public class TsPlugin extends JavaPlugin  implements Listener
{
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PoisonCheckTask task = new PoisonCheckTask(this, event.getPlayer());
        getServer().getScheduler().scheduleAsyncDelayedTask(this, task, 20*10);
    }
}
