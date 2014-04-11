package com.ryanmichela.toxicskies;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class TsSettings {
    private static int TICKS_PER_SECOND = 20;
    private static Map<String, Integer> worldModes;

    // Static initializer
    static {
        worldModes = new HashMap<String, Integer>();
        List<String> worlds = TsPlugin.getInstance().getConfig().getStringList("AffectedWorlds");
        int defaultMode = TsPlugin.getInstance().getConfig().getInt("Mode", 1);
        if (worlds == null || worlds.size() == 0) {
            worlds = new ArrayList<String>();
            worlds.add("world");
        }

        for (String world : worlds) {
            String[] splits = world.split("\\|", 2);
            if (splits.length == 1) {
                worldModes.put(splits[0], defaultMode);
            } else {
                try {
                    worldModes.put(splits[0], Integer.parseInt(splits[1]));
                } catch (NumberFormatException e) {
                    TsPlugin.getInstance().getLogger().severe("Invalid world mode " + splits[1]);
                    worldModes.put(splits[0], defaultMode);
                }
            }
        }
    }

    public static int getMode(String world) {
        return worldModes.get(world);
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

    public static Set<String> getAffectedWorlds() {
        return worldModes.keySet();
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

    public static Material getPumpkinHelmetMaterial() {
        String materialName = TsPlugin.getInstance().getConfig().getString("PumpkinHelmetMaterial", "PUMPKIN");
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            return Material.PUMPKIN;
        }
        return material;
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
