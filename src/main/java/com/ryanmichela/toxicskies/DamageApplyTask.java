package com.ryanmichela.toxicskies;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class DamageApplyTask implements Runnable{
    private Player player;

    public DamageApplyTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        player.sendMessage(TsSettings.getAboveGroundMessage());
        // Don't start directly damaging health until after hunger damage has completed
        if (player.getHealth() <= player.getMaxHealth() / 20) {
            player.damage((double)TsSettings.getAboveGroundDamage());
        }
        player.setFoodLevel(player.getFoodLevel() - TsSettings.getAboveGroundDamage());
        player.setSaturation(0);

        // Apply poison effects
        for(PotionEffect effect : TsSettings.getPoisonEffects()) {
            player.addPotionEffect(effect, true);
        }
    }
}
