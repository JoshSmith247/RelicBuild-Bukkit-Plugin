package com.pezapp.relicbuildarenas;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Arena {
	String name, creator, status, team1Name, team2Name;
	int number;
	Location startSpawnT1, startSpawnT2, spawnT1, spawnT2;
	ArrayList<Player> players, team1, team2;
	
	public Arena (String name, String creator, Integer number, String team1, Integer team1startx, Integer team1starty, Integer team1startz, Integer team1x, Integer team1y, Integer team1z, String team2, Integer team2startx, Integer team2starty, Integer team2startz, Integer team2x, Integer team2y, Integer team2z) {
		this.name = name;
		this.creator = creator;
		this.status = "available";
		this.team1Name = team1;
		this.team2Name = team2;
		this.number = number;
		
		this.players = new ArrayList<>();
		this.team1 = new ArrayList<>();
		this.team2 = new ArrayList<>();
		
		this.startSpawnT1 = new Location(Bukkit.getWorld("ArenasWorld"), team1startx, team1starty, team1startz);
		this.startSpawnT2 = new Location(Bukkit.getWorld("ArenasWorld"), team2startx, team2starty, team2startz);
		
		this.spawnT1 = new Location(Bukkit.getWorld("ArenasWorld"), team1x, team1y, team1z);
		this.spawnT2 = new Location(Bukkit.getWorld("ArenasWorld"), team2x, team2y, team2z);
	}
}
