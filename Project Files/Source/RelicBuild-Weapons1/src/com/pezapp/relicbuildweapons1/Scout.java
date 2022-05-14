package com.pezapp.relicbuildweapons1;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Scout {
    
    @EventHandler
    public static void onSnowballHit(EntityDamageByEntityEvent event) {
    	if (event.getDamager() instanceof Snowball) {
			//ProjectileSource shooter = ((Arrow)event.getDamager()).getShooter();
			Entity targetEntity = event.getEntity();
			if (targetEntity instanceof LivingEntity) { // Allows damaging -- v Lowers by 1 heart v
				((LivingEntity) targetEntity).setHealth(((LivingEntity) targetEntity).getHealth()-2);
			}
    	}
    }
}
