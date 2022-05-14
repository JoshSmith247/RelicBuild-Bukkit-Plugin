package com.pezapp.relicbuildctf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class House {
	String name;
	ChatColor displayColor;
	ArrayList<Player> team = new ArrayList<>();
	Map<Player, UUID> flagBearers = new HashMap<>();
	Location spawn;
	Map<Location, UUID> flagLocs = new HashMap<>();
	Map<Location, UUID> returnLocs = new HashMap<>();
	
	public House(String name, ChatColor displayColor, Location spawnLoc, Location flag1Loc, Location flag2Loc, Location flag3Loc, Location return1Loc, Location return2Loc, Location return3Loc) {
		this.name = name;
		this.displayColor = displayColor;
		this.spawn = spawnLoc;
		
		this.flagLocs.put(flag1Loc, null);
		this.flagLocs.put(flag2Loc, null);
		this.flagLocs.put(flag3Loc, null);
		
		this.returnLocs.put(return1Loc, null);
		this.returnLocs.put(return2Loc, null);
		this.returnLocs.put(return3Loc, null);
	}
}
