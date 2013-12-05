package com.ryanmichela.toxicskies;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class TsSettings {
    private static int TICKS_PER_SECOND = 20;

    public static int getMode() {
        return TsPlugin.getInstance().getConfig().getInt("Mode", 1);
    }

    public static int getSecondsBetweenPolls() {
        return TICKS_PER_SECOND * TsPlugin.getInstance().getConfig().getInt("SecondsBetweenPolls", 10);
    }

    public static int getAboveGroundDamage() {
        return TsPlugin.getInstance().getConfig().getInt("AboveGroundDamage", 1);
    }

    public static int getAboveGroundEffectDuration() {
        return getSecondsBetweenPolls() + 2 * TICKS_PER_SECOND;
    }

    public static String getAboveGroundMessage() {
        return TsPlugin.getInstance().getConfig().getString("AboveGroundMessage", "The air burns your lungs and saps your strength!");
    }

    public static String getCleanAirMessage() {
        return TsPlugin.getInstance().getConfig().getString("CleanAirMessage", "The air is clean here.");
    }

    public static List<String> getAffectedWorlds() {
        List<String> worlds = TsPlugin.getInstance().getConfig().getStringList("AffectedWorlds");
        if (worlds == null || worlds.size() == 0) {
            worlds = new ArrayList<String>();
            worlds.add("world");
        }
        return worlds;
    }

    public static boolean playerInAffectedWorld(Player player) {
        for (String worldName : getAffectedWorlds()) {
            if (player.getWorld().getName().equals(worldName)) {
                return true;
            }
        }
        return false;
    }

    public static int getPumpkinHelmetBreakChancePercent() {
        return TsPlugin.getInstance().getConfig().getInt("PumpkinHelmetBreakChancePercent", 8);
    }

    public static String getPumpkinHelmetBreakMessage() {
        return TsPlugin.getInstance().getConfig().getString("PumpkinHelmetBreakMessage", "Your pumpkin helmet falls apart in your hands!");
    }

    public static String getPumpkinHelmetSurviveMessage() {
        return TsPlugin.getInstance().getConfig().getString("PumpkinHelmetSurviveMessage", "Your pumpkin helmet weakens.");
    }

    private static List<PotionEffect> potionEffects;

    public static List<PotionEffect> getPoisonEffects() {
        if (potionEffects != null) {
            return potionEffects;
        }

        List<String> potionEffectNames = TsPlugin.getInstance().getConfig().getStringList("PoisonEffects");
        if (potionEffectNames == null || potionEffectNames.size() == 0) {
            potionEffectNames = new ArrayList<String>();
            potionEffectNames.add("SLOW");
            potionEffectNames.add("SLOW_DIGGING");
            potionEffectNames.add("HUNGER");
        }

        potionEffects = new ArrayList<PotionEffect>();
        for (String effectName : potionEffectNames) {
            PotionEffectType effectType = PotionEffectType.getByName(effectName);
            if (effectType == null) {
                TsPlugin.getInstance().getLogger().severe(effectName + " is not a valid potion effect name!");
                continue;
            }
            potionEffects.add(new PotionEffect(effectType, getAboveGroundEffectDuration(), 0));
        }

        return potionEffects;
    }
}
