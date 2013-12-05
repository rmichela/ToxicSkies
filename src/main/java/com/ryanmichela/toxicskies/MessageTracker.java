package com.ryanmichela.toxicskies;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2013 Ryan Michela
 */
public class MessageTracker {
    private static Map<String, String> playerMessages = new HashMap<String, String>();

    public static void sendMessage(Player player, String message) {
        if (setMessage(player.getName(), message)) {
            player.sendMessage(message);
        }
    }

    private static boolean setMessage(String playerName, String message) {
        if (playerMessages.containsKey(playerName) && playerMessages.get(playerName).equals(message)) {
            return false;
        } else {
            playerMessages.put(playerName, message);
            return true;
        }
    }

    public static void initPlayer(Player player) {
        playerMessages.put(player.getName(), TsSettings.getCleanAirMessage());
    }

    public static void clearMessage(Player player) {
        playerMessages.remove(player.getName());
    }
}
