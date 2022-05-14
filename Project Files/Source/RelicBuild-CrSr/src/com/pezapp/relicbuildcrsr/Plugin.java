package com.pezapp.relicbuildcrsr;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin implements Listener {
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info("RelicBuild-CrSr plugin has been enabled!");
        
        com.pezapp.relicbuildcore.Plugin.blockBreakBlocked = false;
        com.pezapp.relicbuildcore.Plugin.blockPlaceBlocked = false;
        com.pezapp.relicbuildcore.Plugin.texturesEnabled = false;
        com.pezapp.relicbuildcore.Plugin.canPickupGold = false;
        com.pezapp.relicbuildcore.Plugin.canUseClock = false;
        com.pezapp.relicbuildcore.Plugin.foodLevelChangeBlocked = true;
	}
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer().getInventory().contains(Material.GOLDEN_SHOVEL) == false && event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
			ItemStack item = new ItemStack(Material.GOLDEN_SHOVEL);
	        ItemMeta itemMeta = item.getItemMeta();
	        itemMeta.setDisplayName(ChatColor.GOLD + "Plot Editor");
	        item.setItemMeta(itemMeta);
	        event.getPlayer().getInventory().addItem(item);
		}
	}
	
}