package com.pezapp.relicbuildadventure;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.pezapp.relicbuildadventure.Battles.SlimePit;
import com.pezapp.relicbuildadventure.Battles.SpidersDen;

public class Plugin extends JavaPlugin implements Listener {
	
	public static ArrayList<Battle> bossBattles = new ArrayList<>();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info("RelicBuild-Adventure plugin has been enabled!");
        
        SpidersDen.setupBattle();
        SlimePit.setupBattle();
        
        battleChecker();
	}
	
	public static void setNameTag(Entity entity) {
		Attributable mob = (Attributable) entity;
		
		double health = ((LivingEntity) entity).getHealth();
		double maxhealth = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		
		entity.setCustomName(ChatColor.WHITE + "[" + ChatColor.RED + String.valueOf((int) health) + "/" + String.valueOf(maxhealth) + ChatColor.WHITE + "] " + ChatColor.AQUA + entity.getType().toString());
	}
	
	// Boss Battles	
	public void battleChecker() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.this, new Runnable() {
			@Override
		    public void run() {
				for (int i = 0; i < bossBattles.size(); i++) {
					Battle battle = bossBattles.get(i);
					
					// Player check
					for (int j = 0; j < Bukkit.getOnlinePlayers().size(); j++) {
						Player player = (Player) Bukkit.getOnlinePlayers().toArray()[j];
						
						if (battle.battleLoc.distance(player.getLocation()) < 25) {
							if (!battle.isActive  && !player.getGameMode().equals(GameMode.SPECTATOR)) {
								battle.isActive = true;
								battle.battlePlayers.add(player);
								
								player.sendTitle(ChatColor.RED + battle.name, "Battle has started", 1, 100, 1);
								Bukkit.getOnlinePlayers().forEach(onlineplayer -> {
									onlineplayer.sendMessage(ChatColor.DARK_GRAY + player.getName() + " has summoned the wrath of the " + battle.name);
								});
								
								Battle.startBattle(battle);
							} else if (!battle.battlePlayers.contains(player) && !player.getGameMode().equals(GameMode.SPECTATOR)) {
								player.sendMessage(ChatColor.DARK_GRAY + "Battle is currently in progress. Come back later!");
								player.teleport(battle.returnLoc);
							}
						}
					}
					
					// Mob check
					getLogger().info(String.valueOf(battle.wave));
					if (battle.isActive && battle.mobs.size() == 0) {
						if (battle.wave < battle.waves.size()) {
							battle.wave++;
							Battle.runWave(battle.waves.get(battle.wave));
							
							battle.battlePlayers.forEach(player -> {
								com.pezapp.relicbuildcore.Plugin.changeCredits(player, 3*battle.wave, "Wave survived", ChatColor.BOLD, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
								player.sendMessage(ChatColor.DARK_GRAY + "Wave " + battle.wave + " is starting!");
							});
						} else {
							battle.battlePlayers.forEach(player -> {
								player.sendMessage("You have won the battle!");
								player.teleport(battle.returnLoc);
								
								com.pezapp.relicbuildcore.Plugin.changeCredits(player, 20, "Boss battle", ChatColor.BOLD, Sound.BLOCK_BELL_USE);
							});
							
							battle.battlePlayers.clear();
							battle.wave = 0;
							battle.isActive = false;
						}
					} else if (battle.isActive) {
						for (int j = 0; j < battle.mobs.size(); j++) {
							/*Entity mob = battle.mobs.get(j);
							
							if (mob.getVelocity().equals(new Vector(0, 0, 0)) || mob.getLocation().distance(battle.battlePlayers.get(0).getLocation()) > 50) {
								mob.teleport(battle.battlePlayers.get(0).getLocation());
							}*/
						}
					}
				}
			}
		}, 100, 100);
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		
		if (Battle.getEntityBattle(entity) != null && event.getCause() == DamageCause.FALL) {
			event.setCancelled(true);
		}
		
		setNameTag(entity);
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		
		Battle.getEntityBattle(entity).mobs.remove(entity);
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = (Player) event.getEntity();
		
		Battle battle = Battle.getPlayerBattle(player);
		if (battle != null) {
			battle.battlePlayers.remove(player);
			
			if (battle.battlePlayers.size() == 0) {
				for (int i = 0; i < battle.mobs.size(); i++) {
					battle.mobs.get(i).remove();
					battle.mobs.remove(i);
				}
				
				battle.isActive = false;
			}
		}
	}
}