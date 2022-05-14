package com.pezapp.relicbuildconsumables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class Plugin extends JavaPlugin implements Listener {
    
	List<Material> foodItems = new ArrayList<>();
	Material[] foodItemsArray = { 
			Material.APPLE,
			Material.MUSHROOM_STEW,
			Material.BREAD,
			Material.PORKCHOP,
			Material.COOKED_PORKCHOP,
			Material.GOLDEN_APPLE,
			Material.ENCHANTED_GOLDEN_APPLE,
			Material.COD,
			Material.SALMON,
			Material.TROPICAL_FISH,
			Material.PUFFERFISH,
			Material.COOKED_COD,
			Material.COOKED_SALMON,
			Material.CAKE,
			Material.COOKIE,
			Material.MELON_SLICE,
			Material.DRIED_KELP,
			Material.BEEF,
			Material.COOKED_BEEF,
			Material.CHICKEN,
			Material.COOKED_CHICKEN,
			Material.ROTTEN_FLESH,
			Material.CARROT,
			Material.POTATO,
			Material.BAKED_POTATO,
			Material.POISONOUS_POTATO,
			Material.GOLDEN_CARROT,
			Material.PUMPKIN_PIE,
			Material.RABBIT,
			Material.COOKED_RABBIT,
			Material.RABBIT_STEW,
			Material.MUTTON,
			Material.COOKED_MUTTON,
			Material.CHORUS_FRUIT,
			Material.BEETROOT,
			Material.BEETROOT_SOUP,
			Material.SUSPICIOUS_STEW,
			Material.SWEET_BERRIES,
			Material.HONEY_BOTTLE };
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info("RelicBuild-Consumables plugin has been enabled!");
        
        foodItems = Arrays.asList(foodItemsArray);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (foodItems.contains(event.getPlayer().getInventory().getItemInMainHand().getType())) {
				event.getPlayer().setFoodLevel(19);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.this, new Runnable() {
					@Override
				    public void run() {
						event.getPlayer().setFoodLevel(20);
					}
				}, 1);
			}
		}
	}
	
	@EventHandler
	public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		Material item = event.getItem().getType();
		
		if (item == Material.POISONOUS_POTATO) {
			player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(1000, 1));
			player.addPotionEffect(PotionEffectType.POISON.createEffect(1000, 1));
			player.addPotionEffect(PotionEffectType.HUNGER.createEffect(1000, 1));
			player.addPotionEffect(PotionEffectType.NIGHT_VISION.createEffect(1000, 1));
			player.addPotionEffect(PotionEffectType.SPEED.createEffect(1000, 1));
			player.addPotionEffect(PotionEffectType.ABSORPTION.createEffect(1000, 1));
		}
	}
}