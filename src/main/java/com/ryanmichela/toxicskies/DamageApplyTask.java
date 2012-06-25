package com.ryanmichela.toxicskies;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DamageApplyTask implements Runnable{
    private Player player;

    public DamageApplyTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        player.sendMessage(TsSettings.getAboveGroundMessage());
        player.damage(TsSettings.getAboveGroundDamage());
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, TsSettings.getAboveGroundEffectDuration(), 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, TsSettings.getAboveGroundEffectDuration(), 0));
        player.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, TsSettings.getAboveGroundEffectDuration(), 0));
    }
}
