package com.pezapp.relicbuildweapons1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

public class Archer implements Listener {

    @EventHandler
    public static void onArrowHit(EntityDamageByEntityEvent event) {
    	if (event.getDamager() instanceof Arrow) {
			ProjectileSource shooter = ((Arrow)event.getDamager()).getShooter();
			if (event.getEntity() instanceof Player) {
				Player targetPlayer = (Player) event.getEntity();
				if (targetPlayer.getHealth() - event.getDamage() <= 0) {
					if (shooter instanceof Player) {
						Player playerShooter = (Player) shooter;
						ItemStack item = new ItemStack(Material.ARROW);
						Random r = new Random();
						int max = 8;
						int min = 3;
						int a = r.nextInt((max - min) + 1) + min;
						item.setAmount(a);
				        ItemMeta itemMeta = item.getItemMeta();
				        itemMeta.setDisplayName("Arrow");
				        item.setItemMeta(itemMeta);
				        playerShooter.getInventory().addItem(item);
				    }
				}
				if (shooter instanceof Player) {
					Player playerShooter = (Player) shooter;
			        Random r = new Random();
			        int max = 10;
			        int min = 1;
			        int a = r.nextInt((max - min) + 1) + min;
			        if (a > 7) { // Give this player an effect
			        	ArrayList<PotionEffectType> effects = new ArrayList<PotionEffectType>();
			            effects.add(PotionEffectType.ABSORPTION);
			            effects.add(PotionEffectType.BLINDNESS);
			            effects.add(PotionEffectType.BAD_OMEN);
			            effects.add(PotionEffectType.CONFUSION);
			            effects.add(PotionEffectType.DAMAGE_RESISTANCE);
			            effects.add(PotionEffectType.GLOWING);
			            effects.add(PotionEffectType.FIRE_RESISTANCE);
			            effects.add(PotionEffectType.HARM);
			            effects.add(PotionEffectType.INVISIBILITY);
			            effects.add(PotionEffectType.HEAL);
			            effects.add(PotionEffectType.LEVITATION);
			            effects.add(PotionEffectType.JUMP);
			            effects.add(PotionEffectType.SLOW_FALLING);
			            effects.add(PotionEffectType.POISON);
			            effects.add(PotionEffectType.SLOW);
			            effects.add(PotionEffectType.WEAKNESS);
			            effects.add(PotionEffectType.WITHER);
				        Collections.shuffle(effects);
				        PotionEffectType chosen = effects.get(0);
				        targetPlayer.addPotionEffect(new PotionEffect(chosen, 20, 1));
				        targetPlayer.sendMessage(ChatColor.GRAY + (playerShooter.getName()+" shot you with a spiked arrow!"));
			        }
				}
			}
    	}
    }
}
