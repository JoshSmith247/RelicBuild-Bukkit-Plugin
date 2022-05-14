package com.pezapp.relicbuildctf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

public class Plugin extends JavaPlugin implements Listener {
	
	ArrayList<House> houses = new ArrayList<>();
	ArrayList<UUID> flags = new ArrayList<>();
	ArrayList<UUID> active = new ArrayList<>();
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info("RelicBuild-CTF plugin has been enabled!");
        
        World world = Bukkit.getWorld("Realm Map");
        
        houses.add(new House("Arko", ChatColor.YELLOW, new Location(world, 465, 123, 172), new Location(world, 499, 124, 189), new Location(world, 497, 824, 183), new Location(world, 499, 124, 177), new Location(world, 496, 111, 207), new Location(world, 469, 104, 212), new Location(world, 462, 103, 213)));
		houses.add(new House("Hart", ChatColor.RED, new Location(world, -223, 103, 807), new Location(world, -177, 103, 851), new Location(world, -193, 101, 859), new Location(world, -213, 102, 844), new Location(world, -181, 95, 755), new Location(world, -191, 94, 758), new Location(world, -213, 94, 749)));
	}
	
	public void startGame() {
		Bukkit.getLogger().info("Beginning a game of Capture the Flag.");
		
		houses.forEach(house -> {
			house.flagLocs.keySet().forEach(location -> {
				summonFlag("flag", location, house);
			});
		});
		
		checkForFlags();
		
	}
	
	public void summonFlag(String type, Location location, House house) {
		ItemStack flag = new ItemStack(Material.NETHER_STAR);
		
		World world = Bukkit.getWorld("Realm Map");
		UUID uuid = world.dropItem(location, flag).getUniqueId();
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.this, new Runnable() {
			@Override
		    public void run() {
				Bukkit.getEntity(uuid).teleport(new Location(location.getWorld(), location.getX() + 0.5, location.getY(), location.getZ() + 0.5));
				Bukkit.getEntity(uuid).setVelocity(new Vector(0, 0, 0));
				Bukkit.getEntity(uuid).setGlowing(true);
				
			}  
    	}, 20L);
		
		if (type.equals("flag")) {
			flags.add(uuid);
			house.flagLocs.put(location, uuid);
		} else if (type.equals("return")) {
			flags.add(uuid);
			house.returnLocs.put(location, uuid);
		}
	}
	
	@EventHandler
	public void onItemDespawn(ItemDespawnEvent event) {
		ItemStack item = event.getEntity().getItemStack();
		
		if (item.getType() == Material.NETHER_STAR) {
			if (flags.contains(event.getEntity().getUniqueId())) {
				houses.forEach(house -> {
					if (house.flagLocs.containsValue(event.getEntity().getUniqueId())) {
						summonFlag("flag", event.getLocation(), house);
					} else if (house.returnLocs.containsValue(event.getEntity().getUniqueId())) {
						summonFlag("return", event.getLocation(), house);
					}
				});
			}
		}
	}
	
	@EventHandler
	public void onItemPickup(EntityPickupItemEvent event) {
		ItemStack item = event.getItem().getItemStack();
		UUID uuid = event.getItem().getUniqueId();
		
		if (event.getEntity() instanceof Player && item.getType() == Material.NETHER_STAR && getFlagHouse(uuid) != null) {
			if (getPlayerHouse((Player) event.getEntity()) == getFlagHouse(uuid) && !active.contains(uuid)) {
				event.setCancelled(true);
				return;
			}
			if (getPlayerHouse((Player) event.getEntity()) == null) {
				event.setCancelled(true);
				event.getEntity().sendMessage("Please join capture the flag to participate! (/ctf)");
				return;
			}
			
			event.getEntity().sendMessage("TEST");
			getPlayerHouse((Player) event.getEntity()).flagBearers.put((Player) event.getEntity(), uuid);
			
			Firework firework = (Firework) event.getItem().getWorld().spawnEntity(event.getEntity().getLocation().add(0, 5, 0), EntityType.FIREWORK);
			FireworkMeta fMeta = firework.getFireworkMeta();
			
			fMeta.addEffect(FireworkEffect.builder()
					.withColor(Color.WHITE)
					.build());
			
			fMeta.setPower(0);
			
			firework.setFireworkMeta(fMeta);
			
			firework.detonate();
			
			event.getEntity().addPotionEffect(PotionEffectType.GLOWING.createEffect(100000, 1));
			
			houses.forEach(house -> {
				house.team.forEach(player -> {
					player.sendTitle(getFlagHouse(uuid).displayColor + "Flag Stolen", getFlagHouse(uuid).displayColor + event.getEntity().getName() + " has stolen " + getFlagHouse(uuid).name + "'s Flag", 1, 100, 1);
				});
			});
			
			active.add(uuid);
		}
	}
	
	public int playerCount = 0;
	
	public void addPlayerToTeam(Player player, House house) {
		houses.forEach(thisHouse -> {
			if (thisHouse.team.contains(player)) {
				thisHouse.team.remove(player);
			}
		});
		
		player.setBedSpawnLocation(house.spawn);
		
		house.team.add(player);
		
		Scoreboard sboard = Bukkit.getScoreboardManager().getMainScoreboard();
    	Team team = sboard.getTeam(house.name);
    	
    	team.addEntry(player.getName());
    	
    	playerCount = 0;
    	houses.forEach(thisHouse -> {
			playerCount += thisHouse.team.size();
    	});
    	
    	if (playerCount == 2) {
    		startGame();
    	}
	}
	
	public House getHouse(String name) {
		for (int i = 0; i < houses.size(); i++) {
			if (houses.get(i).name.equals(name)) {
				return houses.get(i);
			}
		}
		return null;
	}
	
	public House getPlayerHouse(Player player) {
		for (int i = 0; i < houses.size(); i++) {
			if (houses.get(i).team.contains(player)) {
				return houses.get(i);
			}
		}
		return null;
	}
	
	public House getFlagHouse(UUID uuid) {
		for (int i = 0; i < houses.size(); i++) {
			if (houses.get(i).flagLocs.containsValue(uuid)) {
				return houses.get(i);
			}
			if (houses.get(i).returnLocs.containsValue(uuid)) {
				return houses.get(i);
			}
		}
		return null;
	}
	
	public void checkForFlags() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Plugin.this, new Runnable() {
			@Override
		    public void run() {
				houses.forEach(house -> {
					Location spawnLocation = house.spawn;
					
					house.flagBearers.keySet().forEach(bearer -> {
						if (!bearer.getInventory().contains(Material.NETHER_STAR)) house.flagBearers.remove(bearer);
						
						if (bearer.getLocation().distance(spawnLocation) < 10) {
							houses.forEach(thisHouse -> {
								thisHouse.team.forEach(player -> {
									player.sendTitle(getFlagHouse(house.flagBearers.get(bearer)).displayColor + "Flag Captured", getFlagHouse(house.flagBearers.get(bearer)).displayColor + bearer.getName() + " has captured " + getFlagHouse(house.flagBearers.get(bearer)).name + "'s Flag", 1, 100, 1);
								});
							});
							
							//active.remove(getPlayerHouse(bearer).flagBearers.get(bearer));
							
							bearer.getInventory().remove(Material.NETHER_STAR);
							bearer.getActivePotionEffects().clear();
							
							Firework firework = (Firework) bearer.getWorld().spawnEntity(bearer.getLocation().add(0, 5, 0), EntityType.FIREWORK);
							FireworkMeta fMeta = firework.getFireworkMeta();
							
							fMeta.addEffect(FireworkEffect.builder()
									.withColor(Color.WHITE)
									.build());
							
							fMeta.setPower(0);
							
							firework.setFireworkMeta(fMeta);
							
							firework.detonate();
							
							House flagHouse = getFlagHouse(house.flagBearers.get(bearer));
							if (flagHouse.flagLocs.containsValue(house.flagBearers.get(bearer))) {
								for (int i = 0; i < flagHouse.flagLocs.size(); i++) {
									Location key = (Location) flagHouse.flagLocs.keySet().toArray()[i];
									
									if (flagHouse.flagLocs.get(key) == house.flagBearers.get(bearer)) {
										flagHouse.flagLocs.put(key, null);
									}
									
								}
							} else if (flagHouse.returnLocs.containsValue(house.flagBearers.get(bearer))) {
								for (int i = 0; i < flagHouse.returnLocs.size(); i++) {
									Location key = (Location) flagHouse.returnLocs.keySet().toArray()[i];
									
									if (flagHouse.returnLocs.get(key) == house.flagBearers.get(bearer)) {
										flagHouse.returnLocs.put(key, null);
									}
									
								}
							}
							
							addFlag(house, bearer);
							
							com.pezapp.relicbuildcore.Plugin.changeCredits(bearer, 15, "Flag capture", house.displayColor, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
							house.flagBearers.remove(bearer);
						}
					});
				});
			}
		}, 20, 20);
	}
	
	public void addFlag(House house, Player bearer) {
		boolean flagPlaced = false;
		if (house.flagLocs.containsValue(null)) {
			for (int i = 0; i < house.flagLocs.size(); i++) {
				Location key = (Location) house.flagLocs.keySet().toArray()[i];
				if (house.flagLocs.get(key) == null && flagPlaced == false) {
					house.flagLocs.put(key, house.flagBearers.get(bearer));
					summonFlag("flag", key, house);
					house.flagBearers.remove(bearer);
					flagPlaced = true;
				}
			}
		} else if (house.returnLocs.containsValue(null)) {
			for (int i = 0; i < house.returnLocs.size(); i++) {
				Location key = (Location) house.returnLocs.keySet().toArray()[i];
				if (house.returnLocs.get(key) == null && flagPlaced == false) {
					house.returnLocs.put(key, house.flagBearers.get(bearer));
					summonFlag("return", key, house);
					flagPlaced = true;
				}
				if (i == 1 && flagPlaced == false) {
					houses.forEach(thisHouse -> {
						thisHouse.team.forEach(player -> {
							player.sendTitle(house.displayColor + "Game End", house.displayColor + house.name + " has won the game!", 1, 100, 1);
							if (thisHouse.name.equals(house.name)) com.pezapp.relicbuildcore.Plugin.changeCredits(player, 50, "Capture the flag win", house.displayColor, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
						});
					});
					
					// Delete all flags
					for (int j = 0; j < houses.size(); j++) {
						for (int k = 0; k < houses.get(j).flagLocs.size(); k++) {
							houses.get(j).flagLocs.values().forEach(uuid -> {
								if (uuid != null) Bukkit.getEntity(uuid).remove();
							});
						}
						for (int k = 0; k < houses.get(k).returnLocs.size(); k++) {
							houses.get(k).returnLocs.values().forEach(uuid -> {
								if (uuid != null) Bukkit.getEntity(uuid).remove();
							});
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (getPlayerHouse(event.getEntity()).flagBearers.containsKey(event.getEntity())) {
			
			event.getEntity().getInventory().clear();
			
			UUID oldUuid = getPlayerHouse(event.getEntity()).flagBearers.get(event.getEntity());
			
			ItemStack flag = new ItemStack(Material.NETHER_STAR);
			UUID uuid = event.getEntity().getWorld().dropItem(event.getEntity().getLocation(), flag).getUniqueId();
			
			getPlayerHouse(event.getEntity()).flagBearers.put(event.getEntity(), uuid);
			if (getFlagHouse(oldUuid).flagLocs.containsValue(oldUuid)) {
				for (int i = 0; i < getFlagHouse(oldUuid).flagLocs.size(); i++) {
					if (getFlagHouse(oldUuid).flagLocs.get(getFlagHouse(oldUuid).flagLocs.keySet().toArray()[i]) == oldUuid) {
						getFlagHouse(oldUuid).flagLocs.put((Location) getFlagHouse(oldUuid).flagLocs.keySet().toArray()[i], uuid);
						active.remove(oldUuid);
						active.add(uuid);
					}
				}
			} else if (getFlagHouse(oldUuid).returnLocs.containsValue(oldUuid)) {
				for (int i = 0; i < getFlagHouse(oldUuid).returnLocs.size(); i++) {
					if (getFlagHouse(oldUuid).returnLocs.get(getFlagHouse(oldUuid).returnLocs.keySet().toArray()[i]) == oldUuid) {
						getFlagHouse(oldUuid).returnLocs.put((Location) getFlagHouse(oldUuid).returnLocs.keySet().toArray()[i], uuid);
						active.remove(oldUuid);
						active.add(uuid);
					}
				}
			}
			
			Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.this, new Runnable() {
				@Override
			    public void run() {
					Location location = event.getEntity().getLocation();
					
					Bukkit.getEntity(uuid).teleport(new Location(location.getWorld(), location.getX() + 0.5, location.getY(), location.getZ() + 0.5));
					Bukkit.getEntity(uuid).setVelocity(new Vector(0, 0, 0));
					Bukkit.getEntity(uuid).setGlowing(true);
					
				}  
	    	}, 20L);
			
			getPlayerHouse(event.getEntity()).flagBearers.remove(event.getEntity());
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		getLogger().info("command");
		if (cmd.getName().equalsIgnoreCase("rb")) {
			getLogger().info("rb command");
			if (args.length == 2) {
				if (args[0].equals("start") && args[1].equals("ctf")) {
					startGame();
				} else if (args[0].equals("addFlag")) {
					addFlag(getHouse(args[1]), Bukkit.getPlayer(sender.getName()));
				}
				
			} else if (args.length == 3) {
				if (args[0].equals("add") && args[2].equals("fill")) {
					ArrayList<Player> team1 = houses.get(0).team;
					ArrayList<Player> team2 = houses.get(1).team;
					
					if (team1.size() < team2.size()) {
						addPlayerToTeam(Bukkit.getPlayer(args[1]), houses.get(0));
					} else {
						addPlayerToTeam(Bukkit.getPlayer(args[1]), houses.get(1));
					}
				} else if (args[0].equals("add") && args[2].equals("random")) {
					Collections.shuffle(houses);
					addPlayerToTeam(Bukkit.getPlayer(args[1]), houses.get(0));
				} else if (args[0].equals("add")) {
					addPlayerToTeam(Bukkit.getPlayer(args[1]), getHouse(args[2]));
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("ctf")) {
			ArrayList<Player> team1 = houses.get(0).team;
			ArrayList<Player> team2 = houses.get(1).team;
			
			if (team1.size() < team2.size()) {
				addPlayerToTeam(Bukkit.getPlayer(sender.getName()), houses.get(0));
			} else {
				addPlayerToTeam(Bukkit.getPlayer(sender.getName()), houses.get(1));
			}
			
			sender.sendMessage("You have been added to team Arko.");
		}
		return false;
	}
}