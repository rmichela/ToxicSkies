
package com.ryanmichela.toxicskies;

import org.bukkit.entity.Player;

import java.util.Random;

/**
 */
public class PumpkinDecayTask implements Runnable {
    private Player player;

    public PumpkinDecayTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        Random r = new Random();
        int breakRoll = r.nextInt(100);
        if (breakRoll < TsSettings.getPumpkinHelmetBreakChancePercent()) {
            player.getInventory().setHelmet(null);
            player.sendMessage(TsSettings.getPumpkinHelmetBreakMessage());
        } else {
            player.sendMessage(TsSettings.getPumpkinHelmetSurviveMessage());
        }
    }
}
