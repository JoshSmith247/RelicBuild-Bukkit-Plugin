package com.pezapp.relicbuildarenas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


public class Plugin extends JavaPlugin implements Listener {
	
	Plugin instance;
	
	public static Timer timer = new Timer();
	
	public static BossBar playerbar;
	public static BossBar arenabar;
	
	public static ArrayList<String> playerBar = new ArrayList<>();
	public static ArrayList<String> arenaBar = new ArrayList<>();
	
	public static ArrayList<Kit> kits = new ArrayList<>();
	
	public static ArrayList<Arena> arenas = new ArrayList<>();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info("RelicBuild-Arenas plugin has been enabled!");
        
        instance = this;
        
    	timer.schedule(new assignArenas(), 5000);
        
        arenas.add(new Arena("Hart's Village", "roryou", 0, "SKIRMISH1", 501, 54, 411, 498, 33, 430, "SKIRMISH2", 501, 54, 486, 501, 33, 465));
        arenas.add(new Arena("Dune City", "KingElectic", 0, "IKKERIKRAIDERS", 1038, 97, 37, 1037, 97, 26, "IZTANITITANS", 1038, 97, -35, 1037, 97, -24));
        arenas.add(new Arena("Counter Stronk", "KingElectic", 0, "Team1", 587, 57, -322, 524, 58, -323, "Team2", 590, 58, -358, 578, 58, -343));
        arenas.add(new Arena("Fight no Flight", "FunnzyBat", 0, "DRAGOONS", 744, 87, 11, 692, 75, 2, "E-GIRLS", 579, 84, 6, 646, 75, 24));
        
        setupKits();
        
        for (int i = 0; i < kits.size(); i++) {
        	Kit kit = kits.get(i);
        	ItemStack item = new ItemStack(kit.menuItem, 1);
        	
        	ItemMeta meta = item.getItemMeta();
        	meta.setDisplayName(kit.nameColor + kit.name);
        	ArrayList<String> lore = new ArrayList<String>();
        	lore.add(kit.description);
        	meta.setLore(lore);
        	item.setItemMeta(meta);
        	
        	kitInventory.addItem(item);
        }
        
        Bukkit.getOnlinePlayers().forEach(player -> {
        	addAvailablePlayer(player);
        });
        
        playerbar = Bukkit.getServer().createBossBar("Waiting for more players...", BarColor.BLUE, BarStyle.SOLID, new BarFlag[0]);
        playerbar.setProgress(0.33);
        
        arenabar = Bukkit.getServer().createBossBar("Waiting for available arena...", BarColor.BLUE, BarStyle.SOLID, new BarFlag[0]);
        arenabar.setProgress(0.66);
        
        // Requires RelicBuild-Core Plugin
        com.pezapp.relicbuildcore.Plugin.whitelistedBlocks.add(Material.SPRUCE_FENCE);
    }
	
	public void onDisable() {
		timer.cancel();
	}
	
	public static int playerLimit = 20;
	
	public static ArrayList<Player> availablePlayers = new ArrayList<>();
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.getPlayer().sendMessage(ChatColor.GRAY + "Queueing for arenas in 10 seconds...");
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.this, new Runnable() {
			@Override
		    public void run() {
				addAvailablePlayer(event.getPlayer());
				
			}  
    	}, 200L);
		
	}
	
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        
        availablePlayers.remove(player);
    }
	
	public static void addAvailablePlayer(Player player) {
		if (!availablePlayers.contains(player)) {
			availablePlayers.add(player);
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		// Lobby Player Protection
		if (event.getEntity() instanceof Player) {
			if (availablePlayers.contains((Player) event.getEntity())) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	/*public static void joinGroup(String groupName, String playerName) {
		if (groups.get(groupName).length < playerLimit) {
			leaveArenas(playerName);
			
			String[] newGroup = groups.get(groupName);
			ArrayList<String> groupList = new ArrayList<String>(Arrays.asList(newGroup));  
			groupList.add(playerName);  
			groups.put(groupName, groupList.toArray(newGroup));
		} else {
			Bukkit.getPlayer(playerName).sendMessage(ChatColor.GRAY + "The attempted group ('" + groupName + "') is full. Please try again later.");
		}
	}*/
	
	public static void selectKit(String playerName) {
		Player player = Bukkit.getPlayer(playerName);
		
		player.openInventory(kitInventory);
	}
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (ChatColor.stripColor(player.getOpenInventory().getTitle()).equals("Kits")) {
			ItemStack item = event.getCurrentItem();
			String kitName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
			
			kits.forEach(kit -> {
				if (kit.name.equals(kitName)) { // Clicked kit
					
					player.getInventory().clear();
					player.getActivePotionEffects().forEach(pe -> {
						player.removePotionEffect(pe.getType());
					});
					
					for (int j = 0; j < kit.items.length; j++) {
						player.getInventory().addItem(kit.items[j]);
					}
					
					if (player.getInventory().contains(new ItemStack(Material.SHIELD, 1))) {
						player.getInventory().remove(new ItemStack(Material.SHIELD, 1));
						player.getInventory().setItemInOffHand(new ItemStack(Material.SHIELD, 1));
					}
					
					player.getEquipment().setArmorContents(kit.armor);
					
					for (int j = 0; j < kit.effects.length; j++) {
						player.addPotionEffect(kit.effects[j].createEffect(10000, 1));
					}
					
					player.closeInventory();
					
					return;
				}
			});
			
			// If no kit is found
			event.setCancelled(true);
			
		}
	}
	
	@EventHandler
	public static void onPlayerClick(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getItem() != null && event.getItem().getType() == Material.LIGHT_BLUE_DYE) {
				selectKit(event.getPlayer().getName());
			}
		}
	}
	
	public static void leaveArenas(String playerName) {
		Scoreboard sboard = Bukkit.getScoreboardManager().getMainScoreboard();
		Team pendingTeam = sboard.getTeam("pendingGames");
		pendingTeam.addEntry(playerName);
		
		Player player = Bukkit.getPlayer(playerName);
		player.getInventory().clear();
		player.getActivePotionEffects().forEach(pe -> {
			player.removePotionEffect(pe.getType());
		});
		
		arenas.forEach( arena -> {
			if (arena.team1.contains(player)) {
				arena.team1.remove(player);
			}
			if (arena.team2.contains(player)) {
				arena.team2.remove(player);
			}
		});
		
		Location returnLocation = new Location(Bukkit.getPlayer(playerName).getWorld(), 0, 120, 0);
		Bukkit.getPlayer(playerName).teleport(returnLocation);
	}
	
	public class assignArenas extends TimerTask {
		
		@Override
		public void run() {
			// Put all repeating tasks here:
			
			ArrayList<Arena> availableArenas = new ArrayList<>();
			
			arenas.forEach(arena -> {
				if (arena.status.equals("available")) {
					availableArenas.add(arena);
				}
			});
			
			Collections.shuffle(availableArenas);
			
			if (availableArenas.size() == 0) {
				for (int n = 0; n < availablePlayers.size(); n++) {
					arenabar.addPlayer(availablePlayers.get(n));
					
					if (arenaBar.contains(availablePlayers.get(n).getName()) == false) {
						arenaBar.add(availablePlayers.get(n).getName());
					}
				}
				return;
			}
			
			availableArenas.forEach(arena -> {
					
				if (availablePlayers.size() < 2) {
					for (int n = 0; n < availablePlayers.size(); n++) {
						playerbar.addPlayer(availablePlayers.get(n));
						
						if (playerBar.contains(availablePlayers.get(n).getName()) == false) {
							playerBar.add(availablePlayers.get(n).getName());
						}
					}
					return;
				} else {
					int n;
					for (n = 0; n < availablePlayers.size(); n++) {
						if (playerBar.contains(availablePlayers.get(n).getName())) {
							playerbar.removePlayer(availablePlayers.get(n));
						}
						if (arenaBar.contains(availablePlayers.get(n).getName())) {
							arenabar.removePlayer(Bukkit.getPlayer(availablePlayers.get(n).getName()));
						}
						
						arena.players.add(availablePlayers.get(n));
					}
					
					for (n = 0; n < availablePlayers.size(); n++) {
						availablePlayers.remove(n);
					}
					
					/*List<String> instantActionArrayList = instantAction.keySet().stream().collect(Collectors.toList());
					instantActionArrayList.forEach(playerItem -> {
						boolean isAction = instantAction.get(playerItem)
					});*/
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.this, new Runnable() {
						@Override
					    public void run() {
							arena.players.forEach(player -> {
								
						    	player.sendMessage("Teleporting to "+arena.name+" by "+arena.creator+"...");
						    	
						    	Scoreboard sboard = Bukkit.getScoreboardManager().getMainScoreboard();
						    	Team team1 = sboard.getTeam(arena.team1Name);
						    	Team team2 = sboard.getTeam(arena.team2Name);
						    	
						    	 // players per team
						    	int playerTeam = 0;
						    	if (arena.team2.size() > arena.team1.size()) {
						    		playerTeam = 0;
						    		team1.addEntry(player.getName());
						    		arena.team1.add(player);
						    		availablePlayers.remove(player);
						    	} else {
						    		playerTeam = 1;
						    		team2.addEntry(player.getName());
						    		arena.team2.add(player);
						    	}
						    	
								Location returnLocation = new Location(player.getWorld(), 0, 120, 0);
								player.setBedSpawnLocation(returnLocation);
						    	
						    	// Reset player
						    	player.setHealth(20);
						    	player.getInventory().clear();
						    	player.setFallDistance(0);
						    	
						    	if (playerTeam == 0) {
						    		player.teleport(arena.startSpawnT1);
							    	player.setBedSpawnLocation(arena.startSpawnT1);
						    	} else if (playerTeam == 1) {
						    		player.teleport(arena.startSpawnT2);
							    	player.setBedSpawnLocation(arena.startSpawnT2);
						    	}
						    	
						    	selectKit(player.getName()); // Note: Cancels on teleport, so must be opened after or closed before
						    	
						    	ItemStack kitSelector = new ItemStack(Material.LIGHT_BLUE_DYE, 1);
						    	ItemMeta kitMeta = kitSelector.getItemMeta();
						    	kitMeta.setDisplayName(ChatColor.AQUA + "Kit Selector");
						    	kitSelector.setItemMeta(kitMeta);
						    	
						    	player.getInventory().addItem(kitSelector);
						    	
						    	player.sendMessage("Round starting in 20 seconds...");
						    	
							});
							
							Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.this, new Runnable() {
								@Override
							    public void run() {
									arena.players.forEach(player -> {
										player.sendMessage("3...");
									});
									Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.this, new Runnable() {
										@Override
									    public void run() {
											arena.players.forEach(player -> {
												player.sendMessage("2...");
											});
											Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.this, new Runnable() {
												@Override
											    public void run() {
													arena.players.forEach(player -> {
														player.sendMessage("1...");
													});
													Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.this, new Runnable() {
														@Override
													    public void run() {
															arena.team1.forEach(player -> {
																player.teleport(arena.spawnT1);
																player.sendTitle(arena.name, arena.team1Name, 1, 100, 1);
															});
															arena.team2.forEach(player -> {
																player.teleport(arena.spawnT2);
																player.sendTitle(arena.name, arena.team2Name, 1, 100, 1);
															});
															
														}  
											    	}, 20L);
												}  
									    	}, 20L);
										}  
							    	}, 20L);
								}  
					    	}, 340L); // Done in ticks, 20/sec
					    }
					}, 10);
				}
			});
			
			timer.schedule(new assignArenas(), 5000);
		}
	}
	
	@EventHandler
	public static void onPlayerDeath(PlayerDeathEvent event) {
		Bukkit.getLogger().info("1");
		Entity entity = event.getEntity();
		Player player = (Player) entity;
		
		arenas.forEach(arena -> {
			
			Scoreboard sboard = Bukkit.getScoreboardManager().getMainScoreboard();
	    	Team team1 = sboard.getTeam(arena.team1Name);
	    	Team team2 = sboard.getTeam(arena.team2Name);
	    	
	    	Bukkit.getLogger().info(arena.team1Name);
	    	Bukkit.getLogger().info(arena.team2Name);
			
			if (arena.team1.contains(player)) {
				Bukkit.getLogger().info("2");
				team1.removeEntry(player.getName());
				leaveArenas(player.getName());
				addAvailablePlayer(player);
			} else if (arena.team2.contains(player)) {
				Bukkit.getLogger().info("2");
				team2.removeEntry(player.getName());
				leaveArenas(player.getName());
				addAvailablePlayer(player);
			}
			
			//Bukkit.getLogger().info(String.valueOf(arena.team1.size()));
			//Bukkit.getLogger().info(String.valueOf(arena.team2.size()));
			if (arena.team1.size() < 1 || arena.team2.size() < 1) {
				Bukkit.getLogger().info("3");
				arena.status = "available";
				
				if (arena.team1.size() < 1) { // Game winnings distribution
					arena.team2.forEach( entry -> {
						com.pezapp.relicbuildcore.Plugin.changeCredits(entry, 10, "Winning arena round", ChatColor.GOLD, Sound.ENTITY_PLAYER_LEVELUP);
						leaveArenas(entry.getName());
						addAvailablePlayer(entry);
					});
				}
				if (arena.team2.size() < 1) {
					arena.team1.forEach( entry -> {
						com.pezapp.relicbuildcore.Plugin.changeCredits(entry, 10, "Winning arena round", ChatColor.GOLD, Sound.ENTITY_PLAYER_LEVELUP);
						leaveArenas(entry.getName());
						addAvailablePlayer(entry);
					});
				}
				
				arena.team1 = new ArrayList<>();
				arena.team2 = new ArrayList<>();
				
				Team pendingTeam = sboard.getTeam("pendingGames");
				pendingTeam.addEntry(player.getName());
				arena.players = new ArrayList<>();
				
			}
			
			return;
		});
	}
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandManager.onCommand(sender, cmd, label, args);
    	return false;
	}
    
    public static void setupKits() {
    	PotionEffectType[] effects = {};
        ItemStack[] items = { new ItemStack(Material.STONE_SWORD, 1), new ItemStack(Material.SHIELD, 1) };
        ItemStack[] armor = { new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.IRON_LEGGINGS, 1), new ItemStack(Material.IRON_CHESTPLATE, 1), null }; // Must have "null" in unfilled slots
        kits.add(new Kit("Knight", Material.IRON_SWORD, "Offense", "A noble warrior", effects, items, armor));
        
        PotionEffectType[] effects1 = { PotionEffectType.ABSORPTION };
        ItemStack[] items1 = { new ItemStack(Material.TRIDENT, 1), new ItemStack(Material.STONE_AXE, 1) };
        ItemStack[] armor1 = { null, new ItemStack(Material.GOLDEN_LEGGINGS, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), null };
        kits.add(new Kit("Skirmisher", Material.IRON_AXE, "Offense", "A burley barbarian", effects1, items1, armor1));
        
        PotionEffectType[] effects2 = {};
        PotionData potiondata2 = new PotionData(PotionType.INSTANT_DAMAGE);
        PotionMeta potionmeta2 = (PotionMeta) new ItemStack(Material.SPLASH_POTION).getItemMeta();
        potionmeta2.setBasePotionData(potiondata2);
        ItemStack potion2 = new ItemStack(Material.SPLASH_POTION);
        potion2.setItemMeta(potionmeta2);
        PotionData potiondata21 = new PotionData(PotionType.SLOWNESS);
        PotionMeta potionmeta21 = (PotionMeta) new ItemStack(Material.SPLASH_POTION).getItemMeta();
        potionmeta21.setBasePotionData(potiondata21);
        ItemStack potion21 = new ItemStack(Material.SPLASH_POTION);
        potion21.setItemMeta(potionmeta21);
        PotionData potiondata22 = new PotionData(PotionType.SLOW_FALLING);
        PotionMeta potionmeta22 = (PotionMeta) new ItemStack(Material.POTION).getItemMeta();
        potionmeta22.setBasePotionData(potiondata22);
        ItemStack potion22 = new ItemStack(Material.LINGERING_POTION);
        potion22.setItemMeta(potionmeta22);
        PotionData potiondata23 = new PotionData(PotionType.SPEED);
        PotionMeta potionmeta23 = (PotionMeta) new ItemStack(Material.POTION).getItemMeta();
        potionmeta23.setBasePotionData(potiondata23);
        ItemStack potion23 = new ItemStack(Material.LINGERING_POTION);
        potion23.setItemMeta(potionmeta23);
        PotionData potiondata24 = new PotionData(PotionType.REGEN);
        PotionMeta potionmeta24 = (PotionMeta) new ItemStack(Material.POTION).getItemMeta();
        potionmeta24.setBasePotionData(potiondata24);
        ItemStack potion24 = new ItemStack(Material.LINGERING_POTION);
        potion24.setItemMeta(potionmeta24);
        ItemStack[] items2 = { new ItemStack(Material.WOODEN_SWORD, 1), potion24, potion23, potion22, potion21, potion21, potion2, potion2, potion2, potion2, potion2, potion2 };
        ItemStack[] armor2 = { new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_HELMET, 1) }; // Must have "null" in unfilled slots
        kits.add(new Kit("Alchemist", Material.FIRE_CHARGE, "Offense", "A skilled brewer", effects2, items2, armor2));
        
        PotionEffectType[] effects3 = {};
        ItemStack[] items3 = { new ItemStack(Material.BLAZE_ROD, 1) };
        ItemStack[] armor3 = { null, new ItemStack(Material.IRON_LEGGINGS, 1), new ItemStack(Material.IRON_CHESTPLATE, 1), null }; // Must have "null" in unfilled slots
        kits.add(new Kit("Mage", Material.BLAZE_ROD, "Offense", "A powerful spellcaster", effects3, items3, armor3));
        
        PotionEffectType[] effects4 = {};
        ItemStack[] items4 = { new ItemStack(Material.MAGMA_CREAM, 1) };
        ItemStack[] armor4 = { new ItemStack(Material.LEATHER_BOOTS, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1), new ItemStack(Material.LEATHER_HELMET, 1) }; // Must have "null" in unfilled slots
        kits.add(new Kit("Healer", Material.GOLDEN_APPLE, "Support", "A loyal field medic", effects4, items4, armor4));
        
        PotionEffectType[] effects5 = {};
        ItemStack[] items5 = { new ItemStack(Material.BOW, 1), new ItemStack(Material.ARROW, 16) };
        ItemStack[] armor5 = { new ItemStack(Material.LEATHER_BOOTS, 1), new ItemStack(Material.IRON_LEGGINGS, 1), new ItemStack(Material.LEATHER_CHESTPLATE, 1), new ItemStack(Material.LEATHER_HELMET, 1) }; // Must have "null" in unfilled slots
        kits.add(new Kit("Archer", Material.BOW, "Support", "An expert bowslinger", effects5, items5, armor5));
        
        PotionEffectType[] effects6 = {};
        ItemStack item6 = new ItemStack(Material.STICK, 1);
        ItemMeta itemmeta6 = item6.getItemMeta();
        itemmeta6.addEnchant(Enchantment.KNOCKBACK, 6, true);
        item6.setItemMeta(itemmeta6);
        ItemStack[] items6 = { item6, new ItemStack(Material.SHIELD, 1) };
        ItemStack[] armor6 = { new ItemStack(Material.IRON_BOOTS, 1), new ItemStack(Material.IRON_LEGGINGS, 1), new ItemStack(Material.DIAMOND_CHESTPLATE, 1), new ItemStack(Material.IRON_HELMET, 1) }; // Must have "null" in unfilled slots
        kits.add(new Kit("Tank", Material.DIAMOND_CHESTPLATE, "Defense", "A pillar of moving steel", effects6, items6, armor6));
        
        PotionEffectType[] effects7 = { PotionEffectType.REGENERATION };
        ItemStack[] items7 = { new ItemStack(Material.DIAMOND_HOE, 1), new ItemStack(Material.FISHING_ROD, 1), new ItemStack(Material.MILK_BUCKET, 1), new ItemStack(Material.ARROW, 16), new ItemStack(Material.MUSHROOM_STEW, 1), new ItemStack(Material.BEETROOT_SOUP, 1), new ItemStack(Material.COOKED_BEEF, 6) };
        ItemStack[] armor7 = { new ItemStack(Material.LEATHER_BOOTS, 1), new ItemStack(Material.IRON_LEGGINGS, 1), null, new ItemStack(Material.TURTLE_HELMET, 1) }; // Must have "null" in unfilled slots
        kits.add(new Kit("Farmer", Material.WOODEN_HOE, "Support", "A seasoned cultivator", effects7, items7, armor7));
        
        PotionEffectType[] effects8 = { PotionEffectType.SPEED, PotionEffectType.JUMP };
        ItemStack[] items8 = { new ItemStack(Material.STONE_SWORD, 1), new ItemStack(Material.CROSSBOW, 1), new ItemStack(Material.ARROW, 6) };
        ItemStack[] armor8 = { new ItemStack(Material.LEATHER_BOOTS, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), null, new ItemStack(Material.CHAINMAIL_HELMET, 1) }; // Must have "null" in unfilled slots
        kits.add(new Kit("Scout", Material.FEATHER, "Offense", "An agile trickster", effects8, items8, armor8));
        
        PotionEffectType[] effects9 = {};
        ItemStack[] items9 = {};
        ItemStack[] armor9 = { new ItemStack(Material.LEATHER_BOOTS, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.GOLDEN_CHESTPLATE, 1), new ItemStack(Material.LEATHER_HELMET, 1) }; // Must have "null" in unfilled slots
        kits.add(new Kit("Sorcerer", Material.COBWEB, "Support", "A conjurer of illusion", effects9, items9, armor9));
        
        PotionEffectType[] effects10 = {};
        ItemStack[] items10 = {};
        ItemStack[] armor10 = { new ItemStack(Material.GOLDEN_BOOTS, 1), new ItemStack(Material.LEATHER_LEGGINGS, 1), new ItemStack(Material.IRON_CHESTPLATE, 1), new ItemStack(Material.LEATHER_HELMET, 1) }; // Must have "null" in unfilled slots
        kits.add(new Kit("Wizard", Material.COBWEB, "Offense", "A short-range magic user", effects10, items10, armor10));
    }
    
    static Inventory kitInventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Kits");
    static {
    	
    	// White Pane
    	ItemStack whitePane = new ItemStack(Material.WHITE_STAINED_GLASS_PANE, 1);
    	ItemMeta wpMeta = whitePane.getItemMeta();
    	wpMeta.setDisplayName(" ");
    	whitePane.setItemMeta(wpMeta);
    	
    	// Black Pane
    	ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
    	ItemMeta bpMeta = blackPane.getItemMeta();
    	bpMeta.setDisplayName(" ");
    	blackPane.setItemMeta(bpMeta);
    	
    	kitInventory.setItem(0, whitePane);
    	kitInventory.setItem(1, blackPane);
    	kitInventory.setItem(2, blackPane);
    	kitInventory.setItem(3, blackPane);
    	kitInventory.setItem(4, blackPane);
    	kitInventory.setItem(5, blackPane);
    	kitInventory.setItem(6, blackPane);
    	kitInventory.setItem(7, blackPane);
    	kitInventory.setItem(8, whitePane);
        //
        kitInventory.setItem(9, whitePane);
        kitInventory.setItem(10, blackPane);
        kitInventory.setItem(16, blackPane);
        kitInventory.setItem(17, whitePane);
        //
        kitInventory.setItem(18, whitePane);
        kitInventory.setItem(19, blackPane);
        kitInventory.setItem(25, blackPane);
        kitInventory.setItem(26, whitePane);
        //
        kitInventory.setItem(27, whitePane);
        kitInventory.setItem(28, blackPane);
        kitInventory.setItem(34, blackPane);
        kitInventory.setItem(35, whitePane);
        //
        kitInventory.setItem(36, whitePane);
        kitInventory.setItem(37, blackPane);
        kitInventory.setItem(43, blackPane);
        kitInventory.setItem(44, whitePane);
        //
        kitInventory.setItem(45, whitePane);
        kitInventory.setItem(46, blackPane);
        kitInventory.setItem(47, blackPane);
        kitInventory.setItem(48, blackPane);
        kitInventory.setItem(49, blackPane);
        kitInventory.setItem(50, blackPane);
        kitInventory.setItem(51, blackPane);
        kitInventory.setItem(52, blackPane);
        kitInventory.setItem(53, whitePane);
    }
}
