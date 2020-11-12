package com.pezapp.relicbuild.PlayerClasses;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import com.pezapp.relicbuild.Plugin;

public class Tank implements Listener {
	
	public Plugin plugin;
    public Tank(Plugin instance)
    {
        plugin = instance;
    }
    
	public static Map<String, Long> stuns = new HashMap<String, Long>();
	public void stunEvent(PlayerInteractEntityEvent event) {
		long startStun = 0;
		int stunLength = 5;
		
		if (event.getRightClicked() instanceof Player) {
			Player targetPlayer = (Player) event.getRightClicked();
			
			if (stuns.containsKey(event.getPlayer().getName())) {
				 startStun = stuns.get(event.getPlayer().getName());
				 // if the player has used this item recently, lastUsed becomes last usage
			}
			
			if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK && System.currentTimeMillis() - startStun <= stunLength * 1000) {
				stuns.put(targetPlayer.getName(), System.currentTimeMillis());
				targetPlayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, stunLength, 1));
				final BukkitScheduler a = Bukkit.getScheduler();
		        final int task = a.scheduleSyncRepeatingTask((org.bukkit.plugin.Plugin) this, new Runnable(){
			        @Override
				    public void run() {
			        	targetPlayer.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, targetPlayer.getLocation(), 1, 0, 0, 0);
			        }
		        }, 0, 1);
				event.getPlayer().sendMessage(ChatColor.GRAY + "Stunned for "+stunLength+" seconds.");
				
				BukkitScheduler schedulerrr = Bukkit.getScheduler();
	            schedulerrr.scheduleSyncDelayedTask((org.bukkit.plugin.Plugin) this, new Runnable(){
	                @Override
	                public void run(){
	                    a.cancelTask(task);
	                }
	            }, 150); // Length
			}
			
			event.getPlayer().sendMessage(ChatColor.AQUA + stuns.toString());
			/*if (stuns.containsKey(event.getPlayer().getName())) { // Get back to this later
				startStun = stuns.get(event.getPlayer().getName());
				int stunLeft = (int)(stunLength - (System.currentTimeMillis() - startStun) / 1000);
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.AQUA + "Stunned for "+stunLeft+" seconds.");
			}*/
		}
	}
}
