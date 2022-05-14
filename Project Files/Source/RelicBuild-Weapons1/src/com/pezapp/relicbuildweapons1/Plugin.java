package com.pezapp.relicbuildweapons1;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public class Plugin extends JavaPlugin implements Listener {
	
	public static boolean isActive = true;
	
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		Bukkit.getLogger().info("relicbuildweapons1-Weapons-1 plugin has been enabled!");
	}
	
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
		
		// Mage:
		com.pezapp.relicbuildweapons1.Mage.PlayerInteractEvent(event);
		
		// Healer:
		Healer h = new com.pezapp.relicbuildweapons1.Healer();
		h.PlayerInteractEvent(event);
		
		// Anarchist:
		Anarchist a = new com.pezapp.relicbuildweapons1.Anarchist();
		a.summonTnt(event);
		a.summonSmokeBomb(event);
		
		// Counselor Lightning:
		//CounselorLightning c = new com.pezapp.relicbuildweapons1weapons1.Admins.CounselorLightning(null);
		//c.PlayerInteractEvent(event);
	}
	
	Map<String, Long> stuns = com.pezapp.relicbuildweapons1.Tank.stuns;
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event)
    {
      if(event.getDamager() instanceof Player) {
	      Entity damagerEntity = event.getDamager();
	      Player damagerPlayer = (Player) damagerEntity;
		  ItemStack attackItem = damagerPlayer.getInventory().getItemInMainHand();
			  
		  //if (damagerPlayer.getLocation().getDirection()) // -- Backstabbing
		  //((Player)event.getDamager()).sendMessage(damagerPlayer.getLocation().getDirection().normalize().toString());
		  
	      if (attackItem.getType() == Material.BLAZE_ROD && attackItem.getItemMeta().getDisplayName().equalsIgnoreCase("mage_staff")) { // Wizard Staff
	    	  //Supply amount of damage that weapon does/actions it takes
	          //event.setDamage((double) 50);
	      	  //event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 3, 1));
	    	  event.getEntity().setFireTicks(25);
	          //((Player)event.getDamager()).sendMessage(ChatColor.AQUA + "Attacked with mage staff.");
	      }//http://bit.ly/2LB9HV4
	          
	      if (attackItem.getType() == Material.TRIDENT) {
	        	  event.setCancelled(true); // Disables trident melee attack
	      }
	          
	      if (attackItem.getType() == Material.STICK) {// && attackItem.getItemMeta().getDisplayName().equalsIgnoreCase("tank_baton")) {
	     	  if(event.getEntity() instanceof Player) {
	     		  Player eventPlayer = (Player) event.getEntity();
	    		  stuns.put(eventPlayer.getName(), System.currentTimeMillis());// Stuns
	       	  }
	      }
       }
    }
    
    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
    	// Archer:
    	com.pezapp.relicbuildweapons1.Archer.onArrowHit(event);
		
		// Scout:
    	com.pezapp.relicbuildweapons1.Scout.onSnowballHit(event);
		
		// Counselor Lightning:
		//com.pezapp.relicbuildweapons1.CounselorLightning.onArrowHit(event);
    }
    
    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
    	// Tank:
    	com.pezapp.relicbuildweapons1.Tank.stunEvent(event);
    }
    
    public static Map<UUID, Long> healthPots = new HashMap<UUID, Long>();
    public static Map<String, Long> healthCooldowns = new HashMap<String, Long>();
    
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
    	Entity potion = event.getEntity();
    	Location hitLocation = potion.getLocation();
    	if (event.getEntity().getShooter() instanceof Player && healthPots.containsKey(potion.getUniqueId())) {
    		Player thrower = (Player) event.getEntity().getShooter();
    		thrower.sendMessage(ChatColor.GRAY + "Deployed Health Circle.");
    		int points = 50;
    		int radius = 5;
    		
    		final BukkitScheduler a = Bukkit.getScheduler();
            final int task = a.scheduleSyncRepeatingTask(this, new Runnable(){
    		@Override
	    	public void run() {
		    	final Location origin = hitLocation; // put inside for loop to follow loc/player
		    		for (int i = 0; i < points; i++) {
		                double angle = 2 * Math.PI * i / points;
		                Location point = origin.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
		                thrower.getWorld().spawnParticle(Particle.HEART, point, 1, 0, 0, 0);
		            }
		    		for (Player p : Bukkit.getOnlinePlayers()){ // Box collider health area
                        if (p.getLocation().getWorld() == hitLocation.getWorld()) {
                        	if (p.getLocation().getX() > hitLocation.getX() - radius && p.getLocation().getX() < hitLocation.getX() + radius) {
                            	if (p.getLocation().getY() > hitLocation.getY() - radius && p.getLocation().getY() < hitLocation.getY() + radius) {
                            		p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 5, 1));
                            	}
                        	}
                        }
                    }
	    		}
            }, 0, 10);
            
            BukkitScheduler schedulerrr = Bukkit.getScheduler();
            schedulerrr.scheduleSyncDelayedTask(this, new Runnable(){
                @Override
                public void run(){
                    a.cancelTask(task);
                }
            }, 150); // Length
    	}
    }
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandManager.onCommand(sender, cmd, label, args);
    	return false;
	}
}
