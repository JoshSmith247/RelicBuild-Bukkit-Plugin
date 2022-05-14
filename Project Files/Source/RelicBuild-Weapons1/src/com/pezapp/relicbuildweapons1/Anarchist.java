package com.pezapp.relicbuildweapons1;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Anarchist {
	
    @EventHandler
    public void summonTnt(PlayerInteractEvent event) {
    	Player eventPlayer = event.getPlayer();
    	ItemStack attackItem = eventPlayer.getInventory().getItemInMainHand();
		if (attackItem.getType() == Material.GUNPOWDER && eventPlayer.isSneaking()) {
			TNTPrimed tnt = eventPlayer.getWorld().spawn(eventPlayer.getLocation(), TNTPrimed.class);
			Vector aim = eventPlayer.getLocation().getDirection();
			tnt.setVelocity(aim);
			tnt.setFuseTicks(10);
			tnt.setIsIncendiary(false);
			tnt.setYield(0);
		}
    }
    
    @EventHandler
    public void summonSmokeBomb(PlayerInteractEvent event) {
    	Player eventPlayer = event.getPlayer();
    	ItemStack attackItem = eventPlayer.getInventory().getItemInMainHand();
		if (attackItem.getType() == Material.CHARCOAL) {
			
		}
    }
}
