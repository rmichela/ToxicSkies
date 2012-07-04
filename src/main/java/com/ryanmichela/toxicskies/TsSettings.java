package com.ryanmichela.toxicskies;

import org.bukkit.entity.Player;

import java.util.List;

public class TsSettings {
    private static int TICKS_PER_SECOND = 20;

    public static int getSecondsBetweenPolls() {
        return TICKS_PER_SECOND * TsPlugin.getInstance().getConfig().getInt("SecondsBetweenPolls");
    }

    public static int getAboveGroundDamage() {
        return TsPlugin.getInstance().getConfig().getInt("AboveGroundDamage");
    }

    public static int getAboveGroundEffectDuration() {
        return getSecondsBetweenPolls() + 1;
    }

    public static String getAboveGroundMessage() {
        return TsPlugin.getInstance().getConfig().getString("AboveGroundMessage");
    }

    public static List<String> getAffectedWorlds() {
        return TsPlugin.getInstance().getConfig().getStringList("AffectedWorlds");
    }

    public static boolean playerInAffectedWorld(Player player) {
        for (String worldName : getAffectedWorlds()) {
            if (player.getWorld().getName().equals(worldName)) {
                return true;
            }
        }
        return false;
    }

    public static int pumpkinHelmetBreakChancePercent() {
        return TsPlugin.getInstance().getConfig().getInt("PumpkinHelmetBreakChancePercent");
    }

    public static String pumpkinHelmetBreakMessage() {
        return TsPlugin.getInstance().getConfig().getString("PumpkinHelmetBreakMessage");
    }

    public static String pumpkinHelmetSurviveMessage() {
        return TsPlugin.getInstance().getConfig().getString("PumpkinHelmetSurviveMessage");
    }
}
