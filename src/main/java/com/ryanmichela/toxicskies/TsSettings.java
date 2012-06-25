package com.ryanmichela.toxicskies;

public class TsSettings {
    private static int TICKS_PER_SECOND = 20;

    public static int getSecondsBetweenPolls() {
        return TICKS_PER_SECOND * 10;
    }

    public static int getAboveGroundDamage() {
        return 1;
    }

    public static int getAboveGroundEffectDuration() {
        return getSecondsBetweenPolls() + 1;
    }

    public static String getAboveGroundMessage() {
        return "The air burns your lungs and saps your strength.";
    }
}
