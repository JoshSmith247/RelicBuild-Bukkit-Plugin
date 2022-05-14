package com.pezapp.relicbuildadventure;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class Battle {
	String name;
	Boolean isActive;
	Integer wave;
	Location battleLoc, returnLoc;
	public ArrayList<Player> battlePlayers;
	public ArrayList<Entity> mobs;
	ArrayList<Runnable> waves;
	
	public Battle(String name, Location battleLoc, Location returnLoc, ArrayList<Runnable> waves) {
		this.name = name;
		this.isActive = false;
		this.wave = 0;
		this.battleLoc = battleLoc;
		this.returnLoc = returnLoc;
		this.battlePlayers = new ArrayList<>();
		this.mobs = new ArrayList<>();
		this.waves = waves;
	}
	
	public static Battle getBattle(String name) {
		for (int i = 0; i < Plugin.bossBattles.size(); i++) {
			Battle battle = Plugin.bossBattles.get(i);
			
			if (battle.name.equals(name)) {
				return battle;
			}
		}
		
		return null;
	}
	
	public static Battle getEntityBattle(Entity entity) {
		for (int i = 0; i < Plugin.bossBattles.size(); i++) {
			Battle battle = Plugin.bossBattles.get(i);
			
			if (battle.mobs.contains(entity)) {
				return battle;
			}
		}
		
		return null;
	}
	
	public static Battle getPlayerBattle(Player player) {
		for (int i = 0; i < Plugin.bossBattles.size(); i++) {
			Battle battle = Plugin.bossBattles.get(i);
			
			if (battle.battlePlayers.contains(player)) {
				return battle;
			}
		}
		
		return null;
	}
	
	public static void startBattle(Battle battle) {
		runWave(battle.waves.get(0));
	}
	
	public static void runWave(Runnable wave) {
		wave.run();
	}
}
