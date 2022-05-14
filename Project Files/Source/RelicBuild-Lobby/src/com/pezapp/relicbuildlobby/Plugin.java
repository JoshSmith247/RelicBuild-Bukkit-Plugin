package com.pezapp.relicbuildlobby;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class Plugin extends JavaPlugin implements Listener {
    
	public final BukkitScheduler a = Bukkit.getScheduler();
	public int messager;
	public int scoreupdater;
	public Timer timer;
	
	public static ArrayList<EasterEgg> EasterEggs = new ArrayList<EasterEgg>();
	
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info("RelicBuild-Lobby plugin has been enabled!");
        
        startTasks();
        EasterEgg.setEggs();
        
        timer = new Timer("Timer");
	}
	
	public void onDisable() {
		a.cancelTask(messager);
		a.cancelTask(scoreupdater);
		timer.cancel();
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!event.getPlayer().isOp()) {
			event.getPlayer().getInventory().clear();
			
			ItemStack clock = new ItemStack(Material.CLOCK, 1);
			ItemMeta clockMeta = clock.getItemMeta();
			clockMeta.setDisplayName(ChatColor.GREEN + "Server Menu");
			clock.setItemMeta(clockMeta);
			
			event.getPlayer().getInventory().addItem(clock);
			
			ItemStack startBook = new ItemStack(Material.WRITTEN_BOOK, 1);
	        BookMeta bookMeta = (BookMeta) startBook.getItemMeta();
	        bookMeta.setDisplayName(ChatColor.RED + "Server Info");
	        bookMeta.setAuthor("The Server Developers");
	        bookMeta.setTitle("AIC REALM");
	        bookMeta.addPage("Welcome to the Adventures In Cardboard Minecraft Server: \n THE REALM! --\n\n Page 2: Rules \n Page 3: Discord VC Link");
	        bookMeta.addPage("Server Rules: \n Don't use profanity \n No cyberbullying \n No communication or construction of anything that would not be deemed camp-appropriate \n By Playing on this server you hereby abide to all these rules and their consequences");
	        bookMeta.addPage("Link: https://discord.gg/Qzz4cmAdpt\n\nAny violation of the rules may result in a ban (Link also in chat)");
	        startBook.setItemMeta(bookMeta);
	        
	        event.getPlayer().getInventory().addItem(startBook);
	        
	        if (!event.getPlayer().hasPlayedBefore()) {
		    	event.getPlayer().openBook(startBook);
			}
	        
			// Daily reward
			if (System.currentTimeMillis() - event.getPlayer().getLastPlayed() > 36000000) { // 10 seconds
				TimerTask task = new TimerTask() {
			        public void run() {
			        	com.pezapp.relicbuildcore.Plugin.changeCredits(event.getPlayer(), 20, "Daily login reward", ChatColor.GOLD, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
			        }
			    };
			    timer.schedule(task, 5000);
	    	}
		}
		
		event.getPlayer().setScoreboard(LobbyScoreboard.newScoreboard(event.getPlayer().getName()));
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player && !event.getEntity().isOp()) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		LobbyScoreboard.updateScoreboard((Player) event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.PLAYER_HEAD) {
			Block block = event.getClickedBlock();
			
			int eggNumber = EasterEgg.checkIfEgg(block.getX(), block.getY(), block.getZ());
			if (eggNumber != -1) {
				event.getPlayer().sendMessage(ChatColor.AQUA + "Lobby easter egg #"+eggNumber+" found.");
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_BELL_USE, 1.0F, 1.0F);
			}
		}
	}
	
	public void startTasks() {
		messager = a.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Bukkit.getServer().broadcastMessage(ChatColor.AQUA + com.pezapp.relicbuildlobby.Messager.randMessage());
			}
		}, 0, 2400);
		scoreupdater = a.scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				Bukkit.getOnlinePlayers().forEach(player -> {
					LobbyScoreboard.updateScoreboard(player);
				});
			}
		}, 0, 100);
	}
}