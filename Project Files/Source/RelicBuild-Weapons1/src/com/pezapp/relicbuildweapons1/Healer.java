package com.pezapp.relicbuildweapons1;

import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Healer implements Listener {
    
    Map<UUID, Long> healthPots = com.pezapp.relicbuildweapons1.Plugin.healthPots;
    Map<String, Long> healthCooldowns = com.pezapp.relicbuildweapons1.Plugin.healthCooldowns;
    
	@EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		
		long lastUsed = 0; // will store currentTimeMillis
		int cooldown = 25; // cooldown in seconds
		
		if (healthCooldowns.containsKey(event.getPlayer().getName())) {
		  lastUsed = healthCooldowns.get(event.getPlayer().getName());
		  // if the player has used this item recently, lastUsed becomes last usage
		}
		
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player eventPlayer = event.getPlayer();
			ItemStack attackItem = eventPlayer.getInventory().getItemInMainHand();
			if (attackItem.getType() == Material.MAGMA_CREAM && System.currentTimeMillis() - lastUsed >= cooldown * 1000) {
	    		ThrownPotion s = event.getPlayer().launchProjectile(ThrownPotion.class);
	    		healthPots.put(s.getUniqueId(), System.currentTimeMillis());
	    		healthCooldowns.put(event.getPlayer().getName(), System.currentTimeMillis());
			} else if (attackItem.getType() == Material.MAGMA_CREAM) {
				event.setCancelled(true); // cancel event
    			int timeLeft = (int) (cooldown - ((System.currentTimeMillis() - lastUsed) / 1000));
    			event.getPlayer().sendMessage(ChatColor.GRAY + "Active Cooldown of " + timeLeft + " seconds remaining");
			}
		}
	}
}
