package com.ryanmichela.toxicskies;

import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

/**
 */
public class TsPlugin extends JavaPlugin
{
    public void onEnable() {
        getServer().getPluginManager().registerEvent(Event.Type.PLAYER_MOVE, new PlayerMoveHandler(), Event.Priority.Normal, this);
    }

    public void onDisable() {

    }
}
