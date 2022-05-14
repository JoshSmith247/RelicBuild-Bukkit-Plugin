package com.pezapp.relicbuildadventure.Battles;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import com.pezapp.relicbuildadventure.Battle;
import com.pezapp.relicbuildadventure.Plugin;

public class SpidersDen {
	
	public static void setupBattle() {
	    Location denLoc = new Location(Bukkit.getWorld("Realm Map"), 182, 166, -239);
		Location denReturn = new Location(Bukkit.getWorld("Realm Map"), 202, 212, -209);
		
		ArrayList<Runnable> waves = new ArrayList<>();
		Runnable wave1 = new Runnable() {
			@Override
			public void run() {
				Location spawnLoc = new Location(Bukkit.getWorld("Realm Map"), 181, 186, -224);
				Battle battle = Battle.getBattle("Spider's Den");
				
				for (int i = 0; i < 3; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SPIDER);
					
					Attributable mob = (Attributable) entity;
					
					AttributeInstance ai = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					ai.setBaseValue(10);
					
					if (entity instanceof Creature) {
						((Creature) entity).setTarget((LivingEntity) battle.battlePlayers.get(0));
					}
					
					Plugin.setNameTag(entity);
					
					battle.mobs.add(entity);
				}
				
				for (int i = 0; i < 3; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.CAVE_SPIDER);
					
					Attributable mob = (Attributable) entity;
					
					AttributeInstance ai = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					ai.setBaseValue(10);
					
					if (entity instanceof Creature) {
						((Creature) entity).setTarget((LivingEntity) battle.battlePlayers.get(0));
					}
					
					Plugin.setNameTag(entity);
					
					battle.mobs.add(entity);
				}
			}
		};
		waves.add(wave1);
		
		Runnable wave2 = new Runnable() {
			@Override
			public void run() {
				Location spawnLoc = new Location(Bukkit.getWorld("Realm Map"), 181, 186, -224);
				Battle battle = Battle.getBattle("Spider's Den");
				
				for (int i = 0; i < 5; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SPIDER);
					
					Attributable mob = (Attributable) entity;
					
					AttributeInstance ai = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					ai.setBaseValue(15);
					
					if (entity instanceof Creature) {
						((Creature) entity).setTarget((LivingEntity) battle.battlePlayers.get(0));
					}
					
					Plugin.setNameTag(entity);
					
					battle.mobs.add(entity);
				}
				
				for (int i = 0; i < 5; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.CAVE_SPIDER);
					
					Attributable mob = (Attributable) entity;
					
					AttributeInstance ai = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					ai.setBaseValue(15);
					
					if (entity instanceof Creature) {
						((Creature) entity).setTarget((LivingEntity) battle.battlePlayers.get(0));
					}
					
					Plugin.setNameTag(entity);
					
					battle.mobs.add(entity);
				}
			}
		};
		waves.add(wave2);
		
		Runnable wave3 = new Runnable() {
			@Override
			public void run() {
				Location spawnLoc = new Location(Bukkit.getWorld("Realm Map"), 181, 186, -224);
				Battle battle = Battle.getBattle("Spider's Den");
				
				for (int i = 0; i < 6; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SPIDER);
					
					Attributable mob = (Attributable) entity;
					
					AttributeInstance ai = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					ai.setBaseValue(20);
					
					if (entity instanceof Creature) {
						((Creature) entity).setTarget((LivingEntity) battle.battlePlayers.get(0));
					}
					
					Plugin.setNameTag(entity);
					
					battle.mobs.add(entity);
				}
				
				for (int i = 0; i < 6; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.CAVE_SPIDER);
					
					Attributable mob = (Attributable) entity;
					
					AttributeInstance ai = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					ai.setBaseValue(15);
					
					if (entity instanceof Creature) {
						((Creature) entity).setTarget((LivingEntity) battle.battlePlayers.get(0));
					}
					
					Plugin.setNameTag(entity);
					
					battle.mobs.add(entity);
				}
				
				for (int i = 0; i < 3; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SKELETON);
					
					Attributable mob = (Attributable) entity;
					
					AttributeInstance ai = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					ai.setBaseValue(15);
					
					if (entity instanceof Creature) {
						((Creature) entity).setTarget((LivingEntity) battle.battlePlayers.get(0));
					}
					
					Plugin.setNameTag(entity);
					
					battle.mobs.add(entity);
				}
				
				for (int i = 0; i < 2; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SPIDER);
					
					Entity rider = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SKELETON);
					entity.addPassenger(rider);
					
					Attributable mob = (Attributable) entity;
					
					AttributeInstance ai = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					ai.setBaseValue(15);
					
					if (entity instanceof Creature) {
						((Creature) entity).setTarget((LivingEntity) battle.battlePlayers.get(0));
					}
					
					Plugin.setNameTag(entity);
					
					battle.mobs.add(entity);
				}
			}
		};
		waves.add(wave3);
		
		Plugin.bossBattles.add(new Battle("Spider's Den", denLoc, denReturn, waves));
	}
}
