package com.pezapp.relicbuildcore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Plugin extends JavaPlugin implements Listener, PluginMessageListener {
	
	public static boolean texturesEnabled = false;
	
	public static Connection connection;
    public static String host;

	public static String database;

	public static String username;

	public static String password;
    public static int port;
    
	static List<String> dbPlayers = new ArrayList<String>();
    public static Map<String, Integer> balances = new HashMap<String, Integer>();
    public static Map<String, String> deathSounds = new HashMap<String, String>();
    public static Map<String, String> killMessages = new HashMap<String, String>();
    public static Map<String, String> walkingEffects = new HashMap<String, String>();
    
    public static Map<String, Map<String, Integer>> bought_deathSounds = new HashMap<>();
    public static Map<String, Map<String, Integer>> bought_killMessages = new HashMap<>();
    public static Map<String, Map<String, Integer>> bought_walkingEffects = new HashMap<>();
    
    public Timer timer;
    
    public TimerTask savetask;
    
    private static Plugin plugin;
    
	public void onEnable() {
		plugin = this;
		timer = new Timer("Timer");
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	    this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
	    
	    this.getServer().getMessenger().registerOutgoingPluginChannel(this, "relicbuild:proxy");
	    this.getServer().getMessenger().registerIncomingPluginChannel(this, "relicbuild:proxy", this);
	    
        Bukkit.getLogger().info("RelicBuild-Core plugin has been enabled!");
        
        host = "138.197.79.49";
        port = 3306;
        database = "aicrealmdb?useSSL=false&allowPublicKeyRetrieval=true"; // Disables errors
        username = "plugin"; // "root" // Create new user for server
        password = "aicrealmpass"; // "aicrealmpass"
    	//Bukkit.getServer().getPluginManager().registerEvents(new CommandManager(), this); // NOT NEEDED
        try {
            openConnection();
            final Statement statement = connection.createStatement(); 
            ResultSet sqlresult = statement.executeQuery("SELECT * FROM player_balances;");
            while (sqlresult.next()) {
            	String name = sqlresult.getString("player_name");
                dbPlayers.add(name);
            }
            Bukkit.getLogger().info(dbPlayers.toString() + " players accounted for in db.");
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        saveData();
        
        com.pezapp.relicbuildcore.Shop.setShopPrices();
        setupSurvival();
        setupEconomy();
        
        Bukkit.getOnlinePlayers().forEach(player -> {
        	loadPlayerData(player);
        });
    }
	
	public void onDisable() {
		timer.cancel();
	}
	
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) { // 'throws' for mysql query
		if (texturesEnabled == true) {
			event.getPlayer().setResourcePack("https://cdn.discordapp.com/attachments/733667551489556561/739317219451994182/AIC_mc_texture_pack_V3.3.zip");
		} else {
			event.getPlayer().setResourcePack("");
		}
    	
    	if (event.getPlayer().isOp() == true) {
    		event.setJoinMessage(ChatColor.GOLD + "The Administrator "+event.getPlayer().getName()+" has landed in the Realm!");
    	} else {
    		event.setJoinMessage(ChatColor.GOLD + event.getPlayer().getName()+" has landed in the Realm!");
    	}
    	
    	if(!event.getPlayer().hasPlayedBefore()) {
    		event.getPlayer().chat(ChatColor.GREEN + "A new Player has entered the virtual realm!");
    	}
    	event.getPlayer().sendMessage(ChatColor.GRAY + "VC Link: https://discord.gg/Qzz4cmAdpt");
		
    	if (event.getPlayer().getInventory().contains(Material.CLOCK) == false && event.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
    		ItemStack item = new ItemStack(Material.CLOCK);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RED + "Game Menu");
            event.getPlayer().getInventory().addItem(item);
    	}
    	
    	if (System.currentTimeMillis() - event.getPlayer().getLastPlayed() > 10000) { // 10 seconds
    		event.getPlayer().teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
    	}
    	
    	// DB Connection:
    	loadPlayerData(event.getPlayer());
    	
    	// Set tab list header:
    	TimerTask task2 = new TimerTask() {
	        public void run() {
	        	com.pezapp.relicbuildcore.Plugin.sendOutgoingPacket(event.getPlayer(), true, "SetHeader", new String[0]);
	        }
	    };
	    timer.schedule(task2, 5000);
	}
	
	public static void loadPlayerData(Player player) {
		if(dbPlayers.contains(player.getName()) == false) { // Accounts for players who started before 1.16.update
    		try {
	    		openConnection();
	            final Statement statement = connection.createStatement(); 
	            String player_name = player.getName();
	            String query = String.join("'", "INSERT INTO player_balances (player_name, player_balance, player_rank) VALUES (", player_name, ", 100, 'camper');");
	    		statement.executeUpdate(query);
	    		query = String.join("'", "INSERT INTO player_selected (player_name, walking_effect, death_sound, kill_message) VALUES (", player_name, ", null, null, null);");
	    		statement.executeUpdate(query);
	    		dbPlayers.add(player.getName());
	    		balances.put(player_name, 100);
	    		connection.close();
	    		
    		} catch (ClassNotFoundException e) {  
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
    	} else {
	    	try {    
	            openConnection();
	            
	            // Selected
	            final Statement statement = connection.createStatement(); 
	            ResultSet sqlresult = statement.executeQuery("SELECT * FROM player_selected WHERE player_name='"+player.getName()+"';");
	            int p = 0;
	            while (sqlresult.next()) {
	            	String name = sqlresult.getString("player_name");
	            	String deathSound = sqlresult.getString("death_sound");
	            	String killMessage = sqlresult.getString("kill_message");
	            	String walkingEffect = sqlresult.getString("walking_effect");
	            	deathSounds.put(name, deathSound);
	            	killMessages.put(name, killMessage);
	            	walkingEffects.put(name, walkingEffect);
	                p++;
	            }
	            if (p > 1) {
	            	Bukkit.getLogger().info("ERROR: MULTIPLE PLAYER OCCURRANCES");
	            } else if (p == 0) {
	            	player.sendMessage(ChatColor.RED + "Adding extra database column: Not Detected");
	            	
	            	String query = String.join("'", "INSERT INTO player_selected (player_name, walking_effect, death_sound, kill_message) VALUES (", player.getName(), ", null, null, null);");
		    		statement.executeUpdate(query);
		    		
		    		deathSounds.put(player.getName(), null);
	            	killMessages.put(player.getName(), null);
	            	walkingEffects.put(player.getName(), null);
	            }
	            
	            // Balances
	            sqlresult = statement.executeQuery("SELECT * FROM player_balances WHERE player_name='"+player.getName()+"';");
	            while (sqlresult.next()) {
	            	String name = sqlresult.getString("player_name");
	            	int balance = sqlresult.getInt("player_balance");
	                balances.put(name, balance);
	                
	                Bukkit.getLogger().info(name + " accounted for in db.");
	                
	            }
	            
	            connection.close();
	        } catch (ClassNotFoundException e) {  
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
    	}
		
		try {
			openConnection();
			final Statement statement = connection.createStatement();
			
            // Death sounds
            ResultSet sqlresult = statement.executeQuery("SELECT * FROM player_death_sounds WHERE player_name='"+player.getName()+"';");
            bought_deathSounds.put(player.getName(), new HashMap<>());
            
            while (sqlresult.next()) {
            	bought_deathSounds.get(player.getName()).put(sqlresult.getString("death_sound").replace("_", " "), 1);
            }
            
            // Fill in extras
            for (String i : com.pezapp.relicbuildcore.Shop.DeathSoundPrices.keySet()) {
            	if (!bought_deathSounds.get(player.getName()).containsKey(i)) {
            		bought_deathSounds.get(player.getName()).put(i, 0);
            	}
            }
            
            // Kill Messages
            sqlresult = statement.executeQuery("SELECT * FROM player_kill_messages WHERE player_name='"+player.getName()+"';");
            bought_killMessages.put(player.getName(), new HashMap<>());
            
            while (sqlresult.next()) {
            	bought_killMessages.get(player.getName()).put(sqlresult.getString("kill_message").replace("_", " "), 1);
            }
            
            // Fill in extras
            for (String i : com.pezapp.relicbuildcore.Shop.KillMessagePrices.keySet()) {
            	if (!bought_killMessages.get(player.getName()).containsKey(i)) {
            		bought_killMessages.get(player.getName()).put(i, 0);
            	}
            }
            
            // Walking Effects
            sqlresult = statement.executeQuery("SELECT * FROM player_walking_effects WHERE player_name='"+player.getName()+"';");
            bought_walkingEffects.put(player.getName(), new HashMap<>());
            
            /*for (String i : com.pezapp.relicbuildcore.Shop.WalkingEffectPrices.keySet()) {
                String a = i.replace(" ", "_").toLowerCase();
            	bought_walkingEffects.get(player.getName()).put(i, sqlresult.getInt(a));
            }*/
            
            connection.close();
		} catch (ClassNotFoundException e) {  
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
	}
	
	public static void updateDatabase(String db, String column, int value, String conditions) {
		try {
            openConnection();
            final Statement statement = connection.createStatement(); 
            statement.executeUpdate("UPDATE "+db+" SET "+column+"="+value+" WHERE "+conditions+";");
		} catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static void addToDatabase(String db, String column1, String column2, String value1, String value2) {
		try {
            openConnection();
            final Statement statement = connection.createStatement(); 
            statement.executeUpdate("INSERT INTO "+db+" ("+column1+", "+column2+") VALUES ('"+value1+"', '"+value2+"');");
		} catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	
	public static void updateSelected(String column, String value, String conditions) {
		try {
            openConnection();
            final Statement statement = connection.createStatement(); 
            statement.executeUpdate("UPDATE player_selected SET "+column+"='"+value+"' WHERE "+conditions+";");
		} catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static Economy economy = null;
	
	private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        } else {
        	economy = new VaultManager();
        }
        
        return (economy != null);
    }
	
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		CommandManager.onCommand(sender, cmd, label, args);
    	
    	return false;
	}
    
    public static boolean canUseClock = true;
    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
    	// Menu Manager:
    	MenuManager i = new com.pezapp.relicbuildcore.MenuManager(null);
    	
    	if (canUseClock) {
    		i.PlayerInteractEvent(event);
    	}
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
		// Menu Manager:
		MenuManager i = new com.pezapp.relicbuildcore.MenuManager(null);
		i.onInventoryClick(event);
	}
    
    public static void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
     
        if (connection != null && !connection.isClosed()) {
            return;
        }
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
    }
    
    public static boolean changeCredits(Player player, Integer credits, String msg, ChatColor color, Sound sound) {
    	if (balances.containsKey(player.getName())) {
    		if (credits != null && (balances.get(player.getName()) + credits > -1 || credits > -1)) {
	    		balances.put(player.getName(), balances.get(player.getName()) + credits);
		    	
		    	//player.sendMessage("Current balance (Testing only): "+String.valueOf(balances.get(player.getName())));
		    	if (credits > 0) {
		    		player.sendMessage(color + "+" + String.valueOf(credits) + " Gold : " + msg);
		    	} else {
		    		player.sendMessage(color + String.valueOf(credits) + " Gold : " + msg);
		    	}
		    	player.playSound(player.getLocation(), sound, 1.0F, 1.0F);
		    	return true;
    		} else {
    			player.sendMessage(ChatColor.RED + "Credit Exchange could not be made; Balance insufficient");
    			return false;
    		}
    	} else {
    		player.sendMessage(ChatColor.RED + "Your username was not found in the current database. This transaction could not go through. Rejoin the game to reconnect.");
    		return false;
    	}
    }
    
    public void saveData() {
    	savetask = new TimerTask() {
	        public void run() {
	        	
		        Bukkit.getOnlinePlayers().forEach(player -> {
		        	try {
						openConnection();
				        final Statement statement = connection.createStatement();
				        //player.sendMessage(String.valueOf(balances.get(player.getName())));
				        
				        String query = "UPDATE player_balances SET player_balance="+String.valueOf(balances.get(player.getName()))+" WHERE player_name='"+player.getName()+"';";
						statement.executeUpdate(query);
						
						connection.close();
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			    });
					
		        //Bukkit.getLogger().info("Relicbuild database updated.");
	        }
    	};
    	timer.schedule(savetask, 12000, 12000); // 2 Minutes
    }
    
    // Survival Tweaks
    
    public static void setupSurvival() {
    	whitelistedPlacement.add(Material.ACACIA_BOAT);
    	whitelistedPlacement.add(Material.BIRCH_BOAT);
    	whitelistedPlacement.add(Material.DARK_OAK_BOAT);
    	whitelistedPlacement.add(Material.OAK_BOAT);
    	whitelistedPlacement.add(Material.SPRUCE_BOAT);
    }
    
    public static boolean blockBreakBlocked = true;
    public static ArrayList<Material> whitelistedBlocks = new ArrayList<>();
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
    	if (blockBreakBlocked == true && whitelistedBlocks.contains(event.getBlock().getType()) == false && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
    		event.setCancelled(true);
    	}
    }
    
    public static boolean blockPlaceBlocked = true;
    public static ArrayList<Material> whitelistedPlacement = new ArrayList<>();
    
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
    	if (blockPlaceBlocked == true && whitelistedPlacement.contains(event.getBlockPlaced().getType()) == false && event.getPlayer().getGameMode() != GameMode.CREATIVE) {
    		event.setCancelled(true);
    	}
    }
    
    public static boolean foodLevelChangeBlocked = false;
    
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
    	if (foodLevelChangeBlocked == false) {
    		event.setCancelled(true);
    	}
    }

    // Cosmetic / Point Accumulation Listeners:
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
    	if (event.getEntity() instanceof Player) {
    		if (deathSounds.get(event.getEntity().getName()) != null) {
    			Sound deathSound = Shop.DeathSoundValues.get(deathSounds.get(event.getEntity().getName()));
    			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
    				p.playSound(event.getEntity().getLocation(), deathSound, 3.0F, 0.533F);
    			}
    		}
    	}
    	if (event.getEntity().getKiller() instanceof Player) {
    		changeCredits(event.getEntity().getKiller(), 3, "Kill ("+event.getEntity().getDisplayName()+")", ChatColor.GOLD, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    		
    		if (event.getEntity() instanceof Player) {
    			if (killMessages.get(event.getEntity().getKiller().getName()) != null) {
    				String dm = Shop.KillMessageValues.get(killMessages.get(event.getEntity().getKiller().getName())).replace("Dummy1", event.getEntity().getName()).replace("Dummy2", event.getEntity().getKiller().getName());
    				
    				event.setDeathMessage(dm);
    			}
    		}
    	}
    }
    
    public static boolean canPickupGold = true;
    
    @EventHandler
    public void onEntityItemPickup(EntityPickupItemEvent event) {
    	if (event.getEntity() instanceof Player && canPickupGold == true) {
    		Player player = (Player) event.getEntity();
    		ItemStack item = event.getItem().getItemStack();
    		
    		if (item.getType() == Material.IRON_NUGGET) {
    			changeCredits(player, 1*item.getAmount(), "Silver Coin x"+String.valueOf(item.getAmount()), ChatColor.GRAY, Sound.BLOCK_ANVIL_USE);
    			event.setCancelled(true);
    			event.getItem().remove();
    		} else if (item.getType() == Material.GOLD_NUGGET) {
    			changeCredits(player, 3*item.getAmount(), "Gold Coin x"+String.valueOf(item.getAmount()), ChatColor.GRAY, Sound.BLOCK_ANVIL_USE);
    			event.setCancelled(true);
    			event.getItem().remove();
    		} else if (item.getType() == Material.IRON_INGOT) {
    			changeCredits(player, 9*item.getAmount(), "Ancient Silver x"+String.valueOf(item.getAmount()), ChatColor.GRAY, Sound.BLOCK_ANVIL_USE);
    			event.setCancelled(true);
    			event.getItem().remove();
    		} else if (item.getType() == Material.GOLD_INGOT) {
    			changeCredits(player, 27*item.getAmount(), "Ancient Gold x"+String.valueOf(item.getAmount()), ChatColor.GRAY, Sound.BLOCK_ANVIL_USE);
    			event.setCancelled(true);
    			event.getItem().remove();
    		} else if (item.getType() == Material.DIAMOND) {
    			changeCredits(player, 50*item.getAmount(), "Diamond x"+String.valueOf(item.getAmount()), ChatColor.GRAY, Sound.BLOCK_ANVIL_USE);
    			event.setCancelled(true);
    			event.getItem().remove();
    		} else if (item.getType() == Material.NETHERITE_SCRAP) {
    			changeCredits(player, 75*item.getAmount(), "Netherite Ring"+String.valueOf(item.getAmount()), ChatColor.GRAY, Sound.BLOCK_ANVIL_USE);
    			event.setCancelled(true);
    			event.getItem().remove();
    		} else if (item.getType() == Material.NETHERITE_INGOT) {
    			changeCredits(player, 408*item.getAmount(), "Ancient Netherite Artifact x"+String.valueOf(item.getAmount()), ChatColor.GRAY, Sound.BLOCK_ANVIL_USE);
    			event.setCancelled(true);
    			event.getItem().remove();
    		} else if (item.getType() == Material.QUARTZ) {
    			changeCredits(player, 15*item.getAmount(), "White Gemstone x"+String.valueOf(item.getAmount()), ChatColor.GRAY, Sound.BLOCK_ANVIL_USE);
    			event.setCancelled(true);
    			event.getItem().remove();
    		} else if (item.getType() == Material.LAPIS_LAZULI) {
    			changeCredits(player, 25*item.getAmount(), "Blue Opal x"+String.valueOf(item.getAmount()), ChatColor.GRAY, Sound.BLOCK_ANVIL_USE);
    			event.setCancelled(true);
    			event.getItem().remove();
    		}
    	}
    }

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		/*if (!channel.equals("BungeeCord")) {
	      return;
	    }*/
		
	    ByteArrayDataInput in = ByteStreams.newDataInput(message);
	    String subchannel = in.readUTF();
	    
	    if (subchannel.equals("GetServers")) {
	    	
	    	String serverList = in.readUTF();
	    	Bukkit.getServer().broadcastMessage(serverList);
	    }
	    // To Send Packet:
	    /*
	    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    	
    	out.writeUTF("GetServers");
    	
    	Player p = Bukkit.getPlayerExact(event.getPlayer().getName());
    	p.sendPluginMessage(this, "BungeeCord", out.toByteArray());*/
	}
	
	public static void sendOutgoingPacket(Player player, boolean isCustom, String subchannel, String[] data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
    	
		Bukkit.getLogger().info("SENDING PACKET");
		
    	out.writeUTF(subchannel);
    	
    	for (String i : data) {
    		out.writeUTF(i);
    	}
    	
    	Player p = Bukkit.getPlayerExact(player.getName());
    	
    	if (isCustom) {
    		p.sendPluginMessage(plugin, "relicbuild:proxy", out.toByteArray());
    	} else {
    		p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    	}
	}
}
