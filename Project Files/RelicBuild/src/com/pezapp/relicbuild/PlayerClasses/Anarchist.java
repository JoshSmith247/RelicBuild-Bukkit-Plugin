package com.pezapp.relicbuild.PlayerClasses;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.pezapp.relicbuild.Plugin;

public class Anarchist {
	
    public Plugin plugin;
    public Anarchist(Plugin instance)
    {
        plugin = instance;
    }
    
    @EventHandler
    public void summonTnt(PlayerInteractEvent event) {
    	Player eventPlayer = event.getPlayer();
    	ItemStack attackItem = eventPlayer.getInventory().getItemInMainHand();
		if (attackItem.getType() == Material.GUNPOWDER && eventPlayer.isSneaking()) {
			/*Entity tnt = eventPlayer.getWorld().spawn(eventPlayer.getLocation(), TNTPrimed.class);
			((TNTPrimed)tnt).setFuseTicks(5);
			((TNTPrimed)tnt).setIsIncendiary(false);*/
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
