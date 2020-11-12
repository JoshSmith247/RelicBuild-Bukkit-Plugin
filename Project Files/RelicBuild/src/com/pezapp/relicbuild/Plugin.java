package com.pezapp.relicbuild;

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
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import com.pezapp.relicbuild.Admins.CounselorLightning;
import com.pezapp.relicbuild.PlayerClasses.Anarchist;
import com.pezapp.relicbuild.PlayerClasses.Archer;
import com.pezapp.relicbuild.PlayerClasses.Healer;
import com.pezapp.relicbuild.PlayerClasses.Mage;
import com.pezapp.relicbuild.PlayerClasses.Scout;
import com.pezapp.relicbuild.PlayerClasses.Tank;

public class Plugin extends JavaPlugin implements Listener {
	// get other classes:
	//private CommandManager event = new CommandManager(this);
    // ran whenever the plugin is enabled.
	private Connection connection;
    private String host, database, username, password;
    private int port;
    private static boolean trailsEnabled = com.pezapp.relicbuild.CommandManager.trailsEnabled;
    List<String> dbPlayers = new ArrayList<String>();
    Map<String, Integer> balances = new HashMap<String, Integer>();
    Map<String, String> deathSounds = new HashMap<String, String>();
    Map<String, String> killMessages = new HashMap<String, String>();
    Map<String, String> walkingEffects = new HashMap<String, String>();
    //Map<String, String> selectedCosmetics = new HashMap<String, String>();
    public static Timer timer = new Timer();
    
	@Override // v Tables: player_balances v
    public void onEnable() { // Columns: player_name, player_balance
        host = "157.245.15.149";
        port = 3306;
        database = "aicrealmdb";
        username = "testinguser"; // "root" // Create new user for server
        password = "@TestPass123"; // "aicrealmpass"
    	Bukkit.getServer().getPluginManager().registerEvents(this, this);
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
        
        timer.schedule(new SayHello(), 50);
        
        Bukkit.getLogger().info("RelicBuild plugin has been enabled!");
    }
 
    // ran whenever the plugin is disabled.
    public void onDisable() {
    	timer.cancel();
        Bukkit.getLogger().info("RelicBuild plugin has been disabled!");
    }
    
    Map<String, Long> grantedOps = CommandManager.grantedOps; // Playername, permslevel
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) throws SQLException, ClassNotFoundException { // 'throws' for mysql query
    	Bukkit.getLogger().info("Player joined.");
    	event.getPlayer().sendMessage(ChatColor.AQUA + "Server running private 'RelicBuild' plugin version " + getDescription().getVersion());
    	
    	event.getPlayer().setResourcePack("https://cdn.discordapp.com/attachments/733667551489556561/739317219451994182/AIC_mc_texture_pack_V3.3.zip");
    	
    	event.setJoinMessage(ChatColor.GOLD + event.getPlayer().getName()+" has landed in the Realm!");
    	
    	if(event.getPlayer().hasPlayedBefore() == false) {
    		event.getPlayer().chat(ChatColor.GREEN + "A new Player has entered the virtual realm!");
    	}
    	
    	if(dbPlayers.contains(event.getPlayer().getName()) == false) { // Accounts for players who started before 1.16.update
    		openConnection();
            final Statement statement = connection.createStatement(); 
            String player_name = event.getPlayer().getName();
            String query = String.join("'", "INSERT INTO player_balances (player_name, player_balance) VALUES (", player_name, ", 100);");
    		statement.executeUpdate(query);
    		query = String.join("'", "INSERT INTO player_death_sounds (player_name, chicken, pig, cow, fox, cat, panda, dolphin, llama, horse, zombie, skeleton, creeper, zombie_pigman, blaze, ghast, endermite, shulker, enderman, iron_golem, wither, ender_dragon) VALUES (", player_name, ", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);");
    		statement.executeUpdate(query);
    		query = String.join("'", "INSERT INTO player_kill_messages (player_name, poked, slapped, tripped, pwned, rekt, murked, beaned, custom) VALUES (", player_name, ", 0, 0, 0, 0, 0, 0, 0, null);");
    		statement.executeUpdate(query);
    		query = String.join("'", "INSERT INTO player_walking_effects (player_name, heart, ash, bubble, water, lava, note, enchanted, flame, blue_flame, soul, explosion) VALUES (", player_name, ", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);");
    		statement.executeUpdate(query);
    		query = String.join("'", "INSERT INTO player_selected (player_name, walking_effect, death_sound, kill_message) VALUES (", player_name, ", null, null, null);");
    		dbPlayers.add(event.getPlayer().getName());
    		balances.put(player_name, 100);
    	} else {
	    	try {    
	            openConnection();
	            final Statement statement = connection.createStatement(); 
	            ResultSet sqlresult = statement.executeQuery("SELECT * FROM player_balances;");
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
	            	System.out.println("ERROR: MULTIPLE PLAYER OCCURRANCES");
	            }
	            
	            connection.close();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    	
	    	try {    
	            openConnection();
	            final Statement statement = connection.createStatement(); 
	            ResultSet sqlresult = statement.executeQuery("SELECT * FROM player_balances;");
	            while (sqlresult.next()) {
	            	String name = sqlresult.getString("player_name");
	            	int balance = sqlresult.getInt("player_balance");
	                balances.put(name, balance);
	            }
	            Bukkit.getLogger().info(dbPlayers.toString() + " players accounted for in db.");
	            connection.close();
	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
    	}
    	
    	ItemStack startBook = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bookMeta = (BookMeta) startBook.getItemMeta();
        bookMeta.setDisplayName(ChatColor.RED + "Server Info");
        bookMeta.setAuthor("FunnzyBat && KingElectic, Devs");
        bookMeta.setTitle("AIC REALM");
        bookMeta.addPage("Welcome to the AIC REALM! --");
        startBook.setItemMeta(bookMeta);
        //event.getPlayer().getInventory().addItem(startBook);
    	event.getPlayer().openBook(startBook);
    	
    	if (event.getPlayer().getInventory().contains(Material.COMPASS) == false && event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)) {
    		ItemStack item = new ItemStack(Material.COMPASS);
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(ChatColor.RED + "Game Menu");
            event.getPlayer().getInventory().addItem(item);
    	}
    	
    	// Item Stacks:
    	/*
    	ItemStack item = new ItemStack(Material.RED_DYE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Texture Pack");
        item.setItemMeta(itemMeta);
        event.getPlayer().getInventory().addItem(item);*/
    }
    
    /*
    @EventHandler
    public void entityDamage(EntityDamageByEntityEvent event) // when player damaged
    {
	  @SuppressWarnings("deprecation")
	  ItemStack stunStick = new ItemStack(Material.STICK, 1, (short)10);
      ItemMeta im = stunStick.getItemMeta();
      im.setDisplayName(ChatColor.GREEN + "Stun Stick");
      stunStick.setItemMeta(im);
      if (((event.getDamager() instanceof Player)) && ((event.getEntity() instanceof Player))) {
        if (((Player)event.getEntity()).getInventory().contains(stunStick))
        {
          ((Player)event.getEntity()).getInventory().remove(stunStick);
          ((Player)event.getDamager()).getInventory().addItem(new ItemStack[] { stunStick });
          ((Player)event.getEntity()).sendMessage(ChatColor.AQUA + ((Player)event.getDamager()).getPlayerListName() + "Tacou a mao na sua goiaba!");
          ((Player)event.getDamager()).sendMessage(ChatColor.AQUA + "Voce tascou a mao na goiaba de" + ((Player)event.getEntity()).getPlayerListName());
        }
        else
        {
          event.setCancelled(true); // STOPS THE HIT FROM HAPPENING
        }
      }
    }*/
    
    @EventHandler
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("rbmysqlexe")) {
    		try {
				openConnection();
	            final Statement statement = connection.createStatement(); 
	            String query = String.join(" ", args);
	            ResultSet sqlresult = statement.executeQuery(query);
	            List<String> result = new ArrayList<String>();
	            
	            java.sql.ResultSetMetaData rsmd = sqlresult.getMetaData();
	            int columnsNumber = rsmd.getColumnCount();
	            while (sqlresult.next()) {
	            	for(int j = 1; j < columnsNumber+1; j++) {
	            		result.add(sqlresult.getString(j));
	            	}
	            }
	            sender.sendMessage(result.toString());
	            connection.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				sender.sendMessage("Error: Class not found exception.");
			} catch (SQLException e) {
				e.printStackTrace();
				sender.sendMessage("Error: SQLException.");
			}
	    	return false;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("rbmysqlupdate")) {
    		try {
				openConnection();
	            final Statement statement = connection.createStatement(); 
	            String query = String.join(" ", args);
	            statement.executeUpdate(query);
	            sender.sendMessage("Update Executed");
	            connection.close();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				sender.sendMessage("Error: Class not found exception.");
			} catch (SQLException e) {
				e.printStackTrace();
				sender.sendMessage("Error: SQLException.");
			}
	    	return false;
    	}
    	
    	CommandManager.onCommand(sender, cmd, label, args);
    	return false;
    }
    
    Map<String, Long> stuns = com.pezapp.relicbuild.PlayerClasses.Tank.stuns;
    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event)
    {
      if(event.getDamager() instanceof Player) { // if both parties are players // event.getEntity() instanceof Player && [removed]
	  	  ((Player)event.getDamager()).sendMessage(ChatColor.AQUA + "Attacked.");
	      Entity damagerEntity = event.getDamager();
	      Player damagerPlayer = (Player) damagerEntity;
		  ItemStack attackItem = damagerPlayer.getInventory().getItemInMainHand();
			  
		  //if (damagerPlayer.getLocation().getDirection()) // -- Backstabbing
		  ((Player)event.getDamager()).sendMessage(damagerPlayer.getLocation().getDirection().normalize().toString());
		  
	      if (attackItem.getType() == Material.BLAZE_ROD && attackItem.getItemMeta().getDisplayName().equalsIgnoreCase("mage_staff")) { // Wizard Staff
	    	  //Supply amount of damage that weapon does/actions it takes
	          //event.setDamage((double) 50);
	      	  //event.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 3, 1));
	    	  event.getEntity().setFireTicks(25);
	          ((Player)event.getDamager()).sendMessage(ChatColor.AQUA + "Attacked with mage staff.");
	      }//http://bit.ly/2LB9HV4
	          
	      if (attackItem.getType() == Material.TRIDENT) {
	        	  event.setCancelled(true); // Disables trident melee attack
	      }
	          
	      if (attackItem.getType() == Material.STICK) {// && attackItem.getItemMeta().getDisplayName().equalsIgnoreCase("tank_baton")) {
	     	  if(event.getEntity() instanceof Player) {
	     		  Player eventPlayer = (Player) event.getEntity();
	    		  stuns.put(eventPlayer.getName(), System.currentTimeMillis());// Stuns
	       	  }
	      }
       }
    }
    
    Map<String, Long> cooldowns = com.pezapp.relicbuild.PlayerClasses.Mage.cooldowns; // Add one for each weapon cooldown
	@EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
		
		// Mage:
		Mage m = new com.pezapp.relicbuild.PlayerClasses.Mage(null);
		m.PlayerInteractEvent(event);
		
		// Healer:
		Healer h = new com.pezapp.relicbuild.PlayerClasses.Healer(null);
		h.PlayerInteractEvent(event);
		
		// Anarchist:
		Anarchist a = new com.pezapp.relicbuild.PlayerClasses.Anarchist(null);
		a.summonTnt(event);
		a.summonSmokeBomb(event);
		
		// Menu Manager:
		MenuManager i = new com.pezapp.relicbuild.MenuManager(null);
		i.PlayerInteractEvent(event);
		
		// Counselor Lightning:
		CounselorLightning c = new com.pezapp.relicbuild.Admins.CounselorLightning(null);
		c.PlayerInteractEvent(event);

		//event.getPlayer().getWorld().spawnParticle(Particle.NOTE, event.getPlayer().getLocation(),  1, new Particle.DustOptions(org.bukkit.Color.fromRGB(100, 255, 0), 1));
		
		//if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
    		//Player eventPlayer = event.getPlayer();
  		    //ItemStack attackItem = eventPlayer.getInventory().getItemInMainHand();
  		    //if (attackItem.getType() == Material.BLAZE_ROD) {
  		    //}
		//}
    }
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
		// Menu Manager:
		MenuManager i = new com.pezapp.relicbuild.MenuManager(null);
		i.onInventoryClick(event);
	}
    
    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {
    	// Archer:
    	Archer a = new com.pezapp.relicbuild.PlayerClasses.Archer(null);
		a.onArrowHit(event);
		
		// Scout:
    	Scout s = new com.pezapp.relicbuild.PlayerClasses.Scout(null);
		s.onSnowballHit(event);
		
		// Counselor Lightning:
		CounselorLightning c = new com.pezapp.relicbuild.Admins.CounselorLightning(null);
		c.onArrowHit(event);
    }
    
    @EventHandler
    public void onPlayerEntityInteract(PlayerInteractEntityEvent event) {
    	// Tank:
    	Tank t = new com.pezapp.relicbuild.PlayerClasses.Tank(null);
    	t.stunEvent(event);
    }
    
    public static Map<UUID, Long> healthPots = new HashMap<UUID, Long>();
    public static Map<String, Long> healthCooldowns = new HashMap<String, Long>();
    
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
    	Entity potion = event.getEntity();
    	Location hitLocation = potion.getLocation();
    	if (event.getEntity().getShooter() instanceof Player && healthPots.containsKey(potion.getUniqueId())) {
    		Player thrower = (Player) event.getEntity().getShooter();
    		thrower.sendMessage(ChatColor.GRAY + "Deployed Health Circle.");
    		int points = 50;
    		int radius = 5;
    		
    		final BukkitScheduler a = Bukkit.getScheduler();
            final int task = a.scheduleSyncRepeatingTask(this, new Runnable(){
    		@Override
	    	public void run() {
		    	final Location origin = hitLocation; // put inside for loop to follow loc/player
		    		for (int i = 0; i < points; i++) {
		                double angle = 2 * Math.PI * i / points;
		                Location point = origin.clone().add(radius * Math.sin(angle), 0.0d, radius * Math.cos(angle));
		                thrower.getWorld().spawnParticle(Particle.HEART, point, 1, 0, 0, 0);
		            }
		    		for (Player p : Bukkit.getOnlinePlayers()){ // Box collider health area
                        if (p.getLocation().getWorld() == hitLocation.getWorld()) {
                        	if (p.getLocation().getX() > hitLocation.getX() - radius && p.getLocation().getX() < hitLocation.getX() + radius) {
                            	if (p.getLocation().getY() > hitLocation.getY() - radius && p.getLocation().getY() < hitLocation.getY() + radius) {
                            		p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 5, 1));
                            	}
                        	}
                        }
                    }
	    		}
            }, 0, 1);
            
            BukkitScheduler schedulerrr = Bukkit.getScheduler();
            schedulerrr.scheduleSyncDelayedTask(this, new Runnable(){
                @Override
                public void run(){
                    a.cancelTask(task);
                }
            }, 150); // Length
    	}
    }
    	
    public class SayHello extends TimerTask {
        // Trails:
        CosmeticsManager cm = new com.pezapp.relicbuild.CosmeticsManager(null);
		@Override
		public void run() {
			// Put all repeating tasks here:
			
			if (trailsEnabled == true) {
				cm.particleTrails();
			}
			
			timer.schedule(new SayHello(), 500); // (First one in onenable)
		}
    }
    
    public void openConnection() throws SQLException, ClassNotFoundException {
        if (connection != null && !connection.isClosed()) {
            return;
        }
     
        synchronized (this) {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
        }
    }
}
