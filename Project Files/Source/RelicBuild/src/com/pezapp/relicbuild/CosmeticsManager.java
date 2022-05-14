package com.pezapp.relicbuild;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class CosmeticsManager { // Maybe change to trailmanager

	public Plugin plugin;
    public CosmeticsManager(Plugin instance)
    {
        plugin = instance;
    }
    
    int points = 10;
	int radius = 2;
	int variation = 10;
	
	Random rand = new Random();
	
	public void particleTrails() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			final Location origin = p.getLocation(); // put inside for loop to follow loc/player
			for (int i = 0; i < points; i++) {
	            double angle = 2 * Math.PI * i / points;
	            Location point = origin.clone().add(radius * Math.sin(angle) - rand.nextInt(variation)/10, 0.0d, radius * Math.cos(angle) - rand.nextInt(variation)/10);
	            p.getWorld().spawnParticle(Particle.NOTE, point, 1, 0, 0, 0);
	        }
			/*for (int i = 0; i < points; i++) {
				Location point = origin.clone().add(0.0d, rand.nextInt(variation)/10, 0.0d);
				p.getWorld().spawnParticle(Particle.NOTE, point, 1, 0, 0, 0);
			}*/
		}
	}
}
