package com.pezapp.relicbuildadventure.Battles;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.pezapp.relicbuildadventure.Battle;
import com.pezapp.relicbuildadventure.Plugin;

public class SlimePit {
	
	public static void setupBattle() {
	    Location pitLoc = new Location(Bukkit.getWorld("Realm Map"), 897, 42, 345);
		Location pitReturn = new Location(Bukkit.getWorld("Realm Map"), 888, 77, 345);
		
		ArrayList<Runnable> waves = new ArrayList<>();
		Runnable wave1 = new Runnable() {
			@Override
			public void run() {
				Location spawnLoc = new Location(Bukkit.getWorld("Realm Map"), 900, 44, 369);
				Battle battle = Battle.getBattle("Slime Pit");
				
				for (int i = 0; i < 3; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SLIME);
					
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
				Location spawnLoc = new Location(Bukkit.getWorld("Realm Map"), 900, 44, 369);
				Battle battle = Battle.getBattle("Slime Pit");
				
				for (int i = 0; i < 5; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SLIME);
					
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
				Location spawnLoc = new Location(Bukkit.getWorld("Realm Map"), 900, 44, 369);
				Battle battle = Battle.getBattle("Spider's Den");
				
				for (int i = 0; i < 6; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SLIME);
					
					Attributable mob = (Attributable) entity;
					
					AttributeInstance ai = mob.getAttribute(Attribute.GENERIC_MAX_HEALTH);
					ai.setBaseValue(20);
					
					if (entity instanceof Creature) {
						((Creature) entity).setTarget((LivingEntity) battle.battlePlayers.get(0));
					}
					
					Plugin.setNameTag(entity);
					
					battle.mobs.add(entity);
				}
				
				for (int i = 0; i < 2; i++) {
					Entity entity = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SLIME);
					
					Entity rider = spawnLoc.getWorld().spawnEntity(spawnLoc, EntityType.SKELETON);
					((LivingEntity)rider).getEquipment().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
					((LivingEntity)rider).getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.IRON_AXE));
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
		
		Plugin.bossBattles.add(new Battle("Slime Pit", pitLoc, pitReturn, waves));
	}
}
