package com.pezapp.relicbuild.PlayerClasses;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.pezapp.relicbuild.Plugin;

public class Mage implements Listener {
	
    public Plugin plugin;
    public Mage(Plugin instance)
    {
        plugin = instance;
    }
    
	public static Map<String, Long> cooldowns = new HashMap<String, Long>(); // Add one for each weapon cooldown
	
	@EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
		// Stores current cooldowns
		long lastUsed = 0; // will store currentTimeMillis
		int cooldown = 5; // cooldown in seconds
		
		if (cooldowns.containsKey(event.getPlayer().getName())) {
		  lastUsed = cooldowns.get(event.getPlayer().getName());
		  // if the player has used this item recently, lastUsed becomes last usage
		}
		
    	if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
    		Player eventPlayer = event.getPlayer();
  		    ItemStack attackItem = eventPlayer.getInventory().getItemInMainHand();
    		if (attackItem.getType() == Material.BLAZE_ROD && System.currentTimeMillis() - lastUsed >= cooldown * 1000) {
    			cooldowns.put(event.getPlayer().getName(), System.currentTimeMillis());
    	        Fireball f = event.getPlayer().launchProjectile(Fireball.class);
    	        f.setIsIncendiary(false);
    	        f.setYield(0);
    	        // Cooldown: // works only on tools?
    	        //final Damageable im = (Damageable) attackItem.getItemMeta();
    	        //im.setDamage((short) 1);
    		} else if (attackItem.getType() == Material.BLAZE_ROD) {
    			event.setCancelled(true); // cancel event
    			int timeLeft = (int) (cooldown - ((System.currentTimeMillis() - lastUsed) / 1000));
    			event.getPlayer().sendMessage(ChatColor.GRAY + "Active Cooldown of " + timeLeft + " seconds remaining");
    		}
    	}
    }
}
