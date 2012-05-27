package com.ryanmichela.toxicskies;

import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 */
public class TsPlugin extends JavaPlugin
{
    public void onEnable() {
        getServer().getPluginManager().registerEvents( new PlayerMoveHandler(), this);
    }

    public void onDisable() {

    }
}
