package com.pezapp.relicbuildcore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class MenuManager implements Listener {
	
	public Plugin plugin;
    public MenuManager(Plugin instance)
    {
        plugin = instance;
    }
    
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player eventPlayer = event.getPlayer();
			ItemStack attackItem = eventPlayer.getInventory().getItemInMainHand();
			if (attackItem.getType() == Material.CLOCK) { // && Get name
				//ItemStack adminHelp = new ItemStack(Material.MAP, 1);
				//ItemMeta helpMeta = adminHelp.getItemMeta();
				/*for (int i = 0; i < menuInventory.getSize(); i++) { // Look through every item
					ItemMeta itemMeta = menuInventory.getItem(i).getItemMeta();
					itemMeta.setDisplayName(" ");
				}*/
				
				openNewInventory(eventPlayer, menuInventory);
			}
		}
	}
	
	public void openNewInventory(Player player, Inventory inventory) {
		
		Inventory newPlayerInv = Bukkit.createInventory(null, inventory.getSize(), player.openInventory(inventory).getTitle());
		
		newPlayerInv.setContents(inventory.getContents());
		player.openInventory(newPlayerInv);
		
		if (inventory == menuInventory) {
			ItemMeta BankMeta = menuInventory.getItem(41).getItemMeta();
			List<String> BLore = new ArrayList<>();
			BLore.add(String.valueOf(Plugin.balances.get(player.getName())) + " Gold");
			BankMeta.setLore(BLore);
			player.getOpenInventory().getItem(41).setItemMeta(BankMeta);
			
			ItemStack playerhead = new ItemStack(Material.PLAYER_HEAD, 1);
			SkullMeta playerheadmeta = (SkullMeta) playerhead.getItemMeta();
            playerheadmeta.setOwningPlayer(player);
            playerheadmeta.setDisplayName(ChatColor.WHITE + player.getName() + "'s Stats");
            playerhead.setItemMeta(playerheadmeta);
			player.getOpenInventory().setItem(39, playerhead);
			
		} else if (inventory == shopInventory_deathSounds || inventory == shopInventory_hostileDeathSounds || inventory == shopInventory_legendaryDeathSounds) {
			for (ItemStack i : newPlayerInv.getContents()) {
				if (com.pezapp.relicbuildcore.Shop.DeathSoundValues.get(ChatColor.stripColor(i.getItemMeta().getDisplayName())) != null) {
					ItemMeta meta = i.getItemMeta().clone();
					List<String> lore = new ArrayList<String>();
					
					//meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); // Not working for swords?
					boolean exists = false;
					for (String item : com.pezapp.relicbuildcore.Plugin.bought_deathSounds.keySet()) {
						if (item.equals(player.getName()) && com.pezapp.relicbuildcore.Plugin.bought_deathSounds.get(player.getName()).size() > 0) exists = true;
					}
					
					if (exists) {
						Map<String, Integer> deathSoundData = com.pezapp.relicbuildcore.Plugin.bought_deathSounds.get(player.getName());
						if (deathSoundData.get(ChatColor.stripColor(i.getItemMeta().getDisplayName())) == 0) {
							lore.add(String.valueOf(com.pezapp.relicbuildcore.Shop.DeathSoundPrices.get(ChatColor.stripColor(i.getItemMeta().getDisplayName()))) + " Gold");
						} else if (deathSoundData.get(ChatColor.stripColor(i.getItemMeta().getDisplayName())) == 1) {
							lore.add("Owned");
						}
						
						if (com.pezapp.relicbuildcore.Shop.DeathSoundPrices.get(ChatColor.stripColor(i.getItemMeta().getDisplayName())) == null) {
							lore.clear();
							lore.add("Unavailable");
							i.setType(Material.COBWEB);
						}
						
						if (com.pezapp.relicbuildcore.Plugin.deathSounds.get(player.getName()).equals(ChatColor.stripColor(i.getItemMeta().getDisplayName()))) {
							lore.clear();
							lore.add("Equipped");
							meta.addEnchant(Enchantment.LOYALTY, 1, true);
						}
					}/* else {
						com.pezapp.relicbuildcore.Plugin.loadPlayerData(player);
						player.sendMessage(ChatColor.RED + "Error Loading User Data. Trying Again...");
						player.closeInventory();
					}*/
					
					meta.setLore(lore);
					i.setItemMeta(meta);
				}
			}
		}
	}
	
	public void openNewVolumedInventory(Player player, Inventory inventory, int volume) {
		
		Inventory newPlayerInv = Bukkit.createInventory(null, inventory.getSize(), player.openInventory(inventory).getTitle());
		
		newPlayerInv.setContents(inventory.getContents());
		player.openInventory(newPlayerInv);
		
		ItemStack fourthItem = player.getOpenInventory().getItem(4);
		ItemMeta fourthMeta = fourthItem.getItemMeta();
		fourthMeta.setDisplayName(String.valueOf(volume));
		fourthItem.setItemMeta(fourthMeta); // Volume Key for switching
		
		if (inventory == shopInventory_killMessages) {
			int j = volume * 15; // Number of open spots per page
			for (ItemStack i : newPlayerInv.getContents()) {
				if (i.getType() == Material.PAPER) {
					if (j < com.pezapp.relicbuildcore.Shop.KillMessageValues.size()) {
						String[] keys = com.pezapp.relicbuildcore.Shop.KillMessagePrices.keySet().toArray(new String[com.pezapp.relicbuildcore.Shop.KillMessageValues.size()]);
						
						ItemMeta meta = i.getItemMeta();
						List<String> lore = new ArrayList<String>();
						
						meta.setDisplayName(ChatColor.YELLOW + keys[j]);
						
						boolean exists = false;
						for (String item : com.pezapp.relicbuildcore.Plugin.bought_killMessages.keySet()) {
							if (item.equals(player.getName()) && com.pezapp.relicbuildcore.Plugin.bought_killMessages.get(player.getName()).size() > 0) exists = true;
						}
						
						if (exists) {
							Map<String, Integer> killMessageData = com.pezapp.relicbuildcore.Plugin.bought_killMessages.get(player.getName());
							if (killMessageData.get(ChatColor.stripColor(meta.getDisplayName())) == 0) {
								lore.add(String.valueOf(com.pezapp.relicbuildcore.Shop.KillMessagePrices.get(ChatColor.stripColor(meta.getDisplayName()))) + " Gold");
							} else if (killMessageData.get(ChatColor.stripColor(meta.getDisplayName())) == 1) {
								lore.add("Owned");
								i.setType(Material.FILLED_MAP);
							}
							
							if (com.pezapp.relicbuildcore.Shop.KillMessagePrices.get(ChatColor.stripColor(meta.getDisplayName())) == null) {
								lore.clear();
								lore.add("Unavailable");
								i.setType(Material.COBWEB);
							}
							
							if (com.pezapp.relicbuildcore.Plugin.killMessages.get(player.getName()) != null && com.pezapp.relicbuildcore.Plugin.killMessages.get(player.getName()).equals(ChatColor.stripColor(meta.getDisplayName()))) {
								lore.clear();
								lore.add("Equipped");
								meta.addEnchant(Enchantment.LOYALTY, 1, true);
							}
						}
						
						meta.setLore(lore);
						i.setItemMeta(meta);
						
						j++;
					} else {
						i.setType(Material.COBWEB);
					}
				}
			}
		}
	}
	
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked(); // The player that clicked the item
		ItemStack clicked = event.getCurrentItem(); // The item that was clicked
		//Inventory inventory = event.getInventory(); // The inventory that was clicked in
		
		if (event.getClickedInventory() == null) {
			player.closeInventory();
		}
		
		if (player.getOpenInventory().getTitle().equals(ChatColor.DARK_GREEN + "Menu")) { // The inventory is our custom Inventory
			if (clicked.getType() == Material.NETHER_STAR) { // The item that the player clicked
				//player.openNewInventory(warpInventory);
			} else if (clicked.getType() == Material.GOLD_NUGGET) {
				openNewInventory(player, shopInventory);
			} else if (clicked.getType() == Material.ENDER_PEARL) { // The item that the player clicked
				String[] data = { "Hub", String.valueOf(Bukkit.getServer().getMaxPlayers()) };
		    	com.pezapp.relicbuildcore.Plugin.sendOutgoingPacket(player, true, "QueueSwitch", data);
			} else if (clicked.getType() == Material.DIAMOND_SWORD) {
				String[] data = { "Arenas1", String.valueOf(Bukkit.getServer().getMaxPlayers()) };
		    	com.pezapp.relicbuildcore.Plugin.sendOutgoingPacket(player, true, "QueueSwitch", data);
			} else if (clicked.getType() == Material.DIAMOND_PICKAXE) {
				String[] data = { "CrSr", String.valueOf(Bukkit.getServer().getMaxPlayers()) };
		    	com.pezapp.relicbuildcore.Plugin.sendOutgoingPacket(player, true, "QueueSwitch", data);
			} else if (clicked.getType() == Material.DIAMOND_HOE) { // The item that the player clicked
				String[] data = { "Adventure1", String.valueOf(Bukkit.getServer().getMaxPlayers()) };
		    	com.pezapp.relicbuildcore.Plugin.sendOutgoingPacket(player, true, "QueueSwitch", data);
			}
			event.setCancelled(true);
			
		} else if (player.getOpenInventory().getTitle().equals(ChatColor.GOLD + "Warp")) {
			if (clicked.getType() == Material.BARRIER) {
				openNewInventory(player, menuInventory);
				player.closeInventory();
			}
			event.setCancelled(true);
		} else if (player.getOpenInventory().getTitle().equals(ChatColor.GREEN + "Shop Selector")) {
			if (clicked.getType() == Material.BARRIER) {
				openNewInventory(player, menuInventory);
				
				/*player.closeInventory();
				
				Inventory newPlayerInv = Bukkit.createInventory(null, menuInventory.getSize(), ChatColor.DARK_GREEN+"Menu");
				newPlayerInv.setContents(menuInventory.getContents());
				player.openNewInventory(newPlayerInv);*/
			} else if (clicked.getType() == Material.LEATHER_BOOTS) {
				player.closeInventory();
				//player.openNewInventory();
			} else if (clicked.getType() == Material.NOTE_BLOCK) {
				player.closeInventory();
				openNewInventory(player, shopInventory_deathSounds);
				//player.openNewInventory(shopInventory_deathSounds);
			} else if (clicked.getType() == Material.OAK_SIGN) {
				openNewVolumedInventory(player, shopInventory_killMessages, 1);
			}
			event.setCancelled(true);
			
		} else if (player.getOpenInventory().getTitle().equals(ChatColor.GREEN + "Death Sounds Shop")) {
			if (clicked.getType() == Material.BARRIER) {
				player.closeInventory();
				openNewInventory(player, shopInventory);
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Next (L)")) {
				player.closeInventory();
				openNewInventory(player, shopInventory_legendaryDeathSounds);
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Next (R)")) {
				player.closeInventory();
				openNewInventory(player, shopInventory_hostileDeathSounds);
			} else if (clicked.getItemMeta().getDisplayName().length() > 1 && event.getWhoClicked() instanceof Player) {
				String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
				
				if (com.pezapp.relicbuildcore.Plugin.bought_deathSounds.get(player.getName()).get(displayName) == 0) {
					boolean purchase = com.pezapp.relicbuildcore.Plugin.changeCredits(Bukkit.getPlayer(event.getWhoClicked().getName()), com.pezapp.relicbuildcore.Shop.DeathSoundPrices.get(displayName) * -1, "Purchased "+displayName+" Death Sound", ChatColor.GREEN, com.pezapp.relicbuildcore.Shop.DeathSoundValues.get(displayName));
					
					if (purchase) {
						Bukkit.getLogger().info(event.getWhoClicked().getName() + " purchased " + displayName + " death effect.");
						
						com.pezapp.relicbuildcore.Plugin.addToDatabase("player_death_sounds", "player_name", "death_sound", event.getWhoClicked().getName(), displayName);
						com.pezapp.relicbuildcore.Plugin.updateSelected("death_sound", displayName, "player_name='"+event.getWhoClicked().getName()+"'");
						
						com.pezapp.relicbuildcore.Plugin.deathSounds.put(event.getWhoClicked().getName(), displayName);
						com.pezapp.relicbuildcore.Plugin.bought_deathSounds.get(event.getWhoClicked().getName()).put(displayName, 1);
						
						openNewInventory(Bukkit.getPlayer(event.getWhoClicked().getName()), shopInventory_deathSounds);
					}
				} else if (com.pezapp.relicbuildcore.Plugin.bought_deathSounds.get(player.getName()).get(displayName) == 1) {
					com.pezapp.relicbuildcore.Plugin.deathSounds.put(event.getWhoClicked().getName(), displayName);
					openNewInventory(Bukkit.getPlayer(event.getWhoClicked().getName()), shopInventory_deathSounds);
				}
			}
			event.setCancelled(true);
			
		} else if (player.getOpenInventory().getTitle().equals(ChatColor.GREEN + "Hostile Death Sounds Shop")) {
			if (clicked.getType() == Material.BARRIER) {
				openNewInventory(player, shopInventory);
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Next (L)")) {
				openNewInventory(player, shopInventory_deathSounds);
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Next (R)")) {
				openNewInventory(player, shopInventory_legendaryDeathSounds);
			} else if (clicked.getItemMeta().getDisplayName().length() > 1 && event.getWhoClicked() instanceof Player) {
					String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
					
					if (com.pezapp.relicbuildcore.Plugin.bought_deathSounds.get(player.getName()).get(displayName) == 0) {
						boolean purchase = com.pezapp.relicbuildcore.Plugin.changeCredits(Bukkit.getPlayer(event.getWhoClicked().getName()), com.pezapp.relicbuildcore.Shop.DeathSoundPrices.get(displayName) * -1, "Purchased "+displayName+" Death Sound", ChatColor.GREEN, com.pezapp.relicbuildcore.Shop.DeathSoundValues.get(displayName));
						
						if (purchase) {
							Bukkit.getLogger().info(event.getWhoClicked().getName() + " purchased " + displayName + " death effect.");
							
							com.pezapp.relicbuildcore.Plugin.addToDatabase("player_death_sounds", "player_name", "death_sound", event.getWhoClicked().getName(), displayName);
							com.pezapp.relicbuildcore.Plugin.updateSelected("death_sound", displayName, "player_name='"+event.getWhoClicked().getName()+"'");
							
							com.pezapp.relicbuildcore.Plugin.deathSounds.put(event.getWhoClicked().getName(), displayName);
							com.pezapp.relicbuildcore.Plugin.bought_deathSounds.get(event.getWhoClicked().getName()).put(displayName, 1);
							
							openNewInventory(Bukkit.getPlayer(event.getWhoClicked().getName()), shopInventory_hostileDeathSounds);
							
						}
					} else if (com.pezapp.relicbuildcore.Plugin.bought_deathSounds.get(player.getName()).get(displayName) == 1) {
						com.pezapp.relicbuildcore.Plugin.deathSounds.put(event.getWhoClicked().getName(), displayName);
						openNewInventory(Bukkit.getPlayer(event.getWhoClicked().getName()), shopInventory_hostileDeathSounds);
					}
				}
			event.setCancelled(true);
		} else if (player.getOpenInventory().getTitle().equals(ChatColor.GOLD + "Legendary Death Sounds Shop")) {
			if (clicked.getType() == Material.BARRIER) {
				player.closeInventory();
				openNewInventory(player, shopInventory);
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Next (L)")) {
				player.closeInventory();
				openNewInventory(player, shopInventory_hostileDeathSounds);
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Next (R)")) {
					player.closeInventory();
					openNewInventory(player, shopInventory_deathSounds);
			}
			event.setCancelled(true);
		} else if (player.getOpenInventory().getTitle().equals(ChatColor.GREEN + "Shop")) {
			if (clicked.getType() == Material.BARRIER) {
				player.closeInventory();
			}
			event.setCancelled(true);
		} else if (player.getOpenInventory().getTitle().equals(ChatColor.GREEN + "Kill Message Shop")) {
			if (clicked.getType() == Material.PAPER || clicked.getType() == Material.FILLED_MAP) {
				String displayName = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
				
				if (com.pezapp.relicbuildcore.Plugin.bought_killMessages.get(player.getName()).get(displayName) == 0) {
					boolean purchase = com.pezapp.relicbuildcore.Plugin.changeCredits(Bukkit.getPlayer(event.getWhoClicked().getName()), com.pezapp.relicbuildcore.Shop.KillMessagePrices.get(displayName) * -1, "Purchased "+displayName+" Kill Message", ChatColor.GREEN, Sound.ITEM_BOOK_PAGE_TURN);
					
					if (purchase) {
						Bukkit.getLogger().info(event.getWhoClicked().getName() + " purchased " + displayName + " kill message.");
						
						com.pezapp.relicbuildcore.Plugin.addToDatabase("player_kill_messages", "player_name", "kill_message", event.getWhoClicked().getName(), displayName);
						com.pezapp.relicbuildcore.Plugin.updateSelected("kill_message", displayName, "player_name='"+event.getWhoClicked().getName()+"'");
						
						com.pezapp.relicbuildcore.Plugin.killMessages.put(event.getWhoClicked().getName(), displayName);
						com.pezapp.relicbuildcore.Plugin.bought_killMessages.get(event.getWhoClicked().getName()).put(displayName, 1);
						
						openNewVolumedInventory(Bukkit.getPlayer(event.getWhoClicked().getName()), shopInventory_killMessages, Integer.valueOf(ChatColor.stripColor((Bukkit.getPlayer(event.getWhoClicked().getName()).getOpenInventory().getItem(4).getItemMeta().getDisplayName()))));
						
					}
				} else if (com.pezapp.relicbuildcore.Plugin.bought_killMessages.get(player.getName()).get(displayName) == 1) {
					com.pezapp.relicbuildcore.Plugin.killMessages.put(event.getWhoClicked().getName(), displayName);
					openNewVolumedInventory(Bukkit.getPlayer(event.getWhoClicked().getName()), shopInventory_killMessages, Integer.valueOf(ChatColor.stripColor((Bukkit.getPlayer(event.getWhoClicked().getName()).getOpenInventory().getItem(4).getItemMeta().getDisplayName()))));
				}
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Next (L)") && Integer.valueOf(player.getOpenInventory().getItem(4).getItemMeta().getDisplayName()) > 0) {
				openNewVolumedInventory(player, shopInventory_killMessages, Integer.valueOf(player.getOpenInventory().getItem(4).getItemMeta().getDisplayName()) - 1);
			} else if (clicked.getItemMeta().getDisplayName().equals(ChatColor.WHITE + "Next (R)") && Integer.valueOf(player.getOpenInventory().getItem(4).getItemMeta().getDisplayName()) < Math.floor(com.pezapp.relicbuildcore.Shop.KillMessageValues.size()/15) + 2) {
				openNewVolumedInventory(player, shopInventory_killMessages, Integer.valueOf(player.getOpenInventory().getItem(4).getItemMeta().getDisplayName()) + 1);
			}
			event.setCancelled(true);
		}
	}
	
	// Menu items:
	
	public static ItemStack redPane = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
	public static ItemStack orangePane = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1);
	public static ItemStack yellowPane = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1);
	public static ItemStack greenPane = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
	public static ItemStack limePane = new ItemStack(Material.LIME_STAINED_GLASS_PANE, 1);
	public static ItemStack bluePane = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
	public static ItemStack lightBluePane = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1);
	public static ItemStack cyanPane = new ItemStack(Material.CYAN_STAINED_GLASS_PANE, 1);
	public static ItemStack purplePane = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1);
	public static ItemStack magentaPane = new ItemStack(Material.MAGENTA_STAINED_GLASS_PANE, 1);
	public static ItemStack pinkPane = new ItemStack(Material.PINK_STAINED_GLASS_PANE, 1);
	public static ItemStack brownPane = new ItemStack(Material.BROWN_STAINED_GLASS_PANE, 1);
	public static ItemStack blackPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
	
	public static ItemStack Barrier = new ItemStack(Material.BARRIER, 1);
	public static ItemStack Pearl = new ItemStack(Material.ENDER_PEARL, 1);
	public static ItemStack Emerald = new ItemStack(Material.GOLD_NUGGET, 1);
	public static ItemStack LeftArrow = new ItemStack(Material.ARROW, 1);
	public static ItemStack RightArrow = new ItemStack(Material.ARROW, 1);
	
	static {
		ItemMeta redMeta = redPane.getItemMeta();
		redMeta.setDisplayName(" ");
		redPane.setItemMeta(redMeta);
		
		ItemMeta orangeMeta = orangePane.getItemMeta();
		orangeMeta.setDisplayName(" ");
		orangePane.setItemMeta(orangeMeta);
		
		ItemMeta yellowMeta = yellowPane.getItemMeta();
		yellowMeta.setDisplayName(" ");
		yellowPane.setItemMeta(yellowMeta);
		
		ItemMeta greenMeta = greenPane.getItemMeta();
		greenMeta.setDisplayName(" ");
		greenPane.setItemMeta(greenMeta);
		
		ItemMeta limeMeta = limePane.getItemMeta();
		limeMeta.setDisplayName(" ");
		limePane.setItemMeta(limeMeta);
		
		ItemMeta blueMeta = bluePane.getItemMeta();
		blueMeta.setDisplayName(" ");
		bluePane.setItemMeta(blueMeta);
		
		ItemMeta lightBlueMeta = lightBluePane.getItemMeta();
		lightBlueMeta.setDisplayName(" ");
		lightBluePane.setItemMeta(lightBlueMeta);
		
		ItemMeta cyanMeta = cyanPane.getItemMeta();
		cyanMeta.setDisplayName(" ");
		cyanPane.setItemMeta(cyanMeta);
		
		ItemMeta purpleMeta = purplePane.getItemMeta();
		purpleMeta.setDisplayName(" ");
		purplePane.setItemMeta(purpleMeta);
		
		ItemMeta magentaMeta = magentaPane.getItemMeta();
		magentaMeta.setDisplayName(" ");
		magentaPane.setItemMeta(magentaMeta);
		
		ItemMeta pinkMeta = pinkPane.getItemMeta();
		pinkMeta.setDisplayName(" ");
		pinkPane.setItemMeta(pinkMeta);
		
		ItemMeta brownMeta = brownPane.getItemMeta();
		brownMeta.setDisplayName(" ");
		brownPane.setItemMeta(brownMeta);
		
		ItemMeta blackMeta = blackPane.getItemMeta();
		blackMeta.setDisplayName(" ");
		blackPane.setItemMeta(blackMeta);
		
		ItemMeta EMeta = Emerald.getItemMeta();
		EMeta.setDisplayName(ChatColor.GOLD + "Shop");
		List<String> ELore = new ArrayList<String>();
		EMeta.setLore(ELore);
		Emerald.setItemMeta(EMeta);
		
		// Barrier
		ItemMeta BMeta = Barrier.getItemMeta();
		BMeta.setDisplayName(ChatColor.RED + "Back");
		Barrier.setItemMeta(BMeta);

		// LArrow
		ItemMeta LAMeta = LeftArrow.getItemMeta();
		LAMeta.setDisplayName(ChatColor.WHITE + "Next (L)");
		LeftArrow.setItemMeta(LAMeta);
		
		// RArrow
		ItemMeta RAMeta = RightArrow.getItemMeta();
		RAMeta.setDisplayName(ChatColor.WHITE + "Next (R)");
		RightArrow.setItemMeta(RAMeta);
	}
	
	// Main Menu:
	// 1 = inventory owner, 2 = # of slots (multiple of 9), 3 = name
	static Inventory menuInventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + "Menu");
	static {
		
		// Warp
		ItemStack Star = new ItemStack(Material.NETHER_STAR, 1);
		ItemMeta SMeta = Star.getItemMeta();
		SMeta.setDisplayName(ChatColor.DARK_PURPLE + "Warps");
		Star.setItemMeta(SMeta);
		
		// Hub
		ItemMeta HubMeta = Pearl.getItemMeta();
		HubMeta.setDisplayName("Hub");
		Pearl.setItemMeta(HubMeta);
		
		// Arenas
		ItemStack Diamond_Sword = new ItemStack(Material.DIAMOND_SWORD, 1);
		ItemMeta ArenaMeta = Diamond_Sword.getItemMeta();
		ArenaMeta.setDisplayName("Arenas");
		Diamond_Sword.setItemMeta(ArenaMeta);
		
		// CrSr
		ItemStack Diamond_Pickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
		ItemMeta CrSrMeta = Diamond_Pickaxe.getItemMeta();
		CrSrMeta.setDisplayName("Creative/Survival");
		Diamond_Pickaxe.setItemMeta(CrSrMeta);
		
		// Adventure
		ItemStack Diamond_Hoe = new ItemStack(Material.DIAMOND_HOE, 1);
		ItemMeta AdventureMeta = Diamond_Hoe.getItemMeta();
		AdventureMeta.setDisplayName("Adventure");
		Diamond_Hoe.setItemMeta(AdventureMeta);
				
		
		menuInventory.setItem(0, redPane);
		menuInventory.setItem(1, redPane);
		menuInventory.setItem(2, blackPane);
		menuInventory.setItem(3, blackPane);
		menuInventory.setItem(4, blackPane);
		menuInventory.setItem(5, blackPane);
		menuInventory.setItem(6, blackPane);
		menuInventory.setItem(7, redPane);
		menuInventory.setItem(8, redPane);
		//
		menuInventory.setItem(9, redPane);
		menuInventory.setItem(10, orangePane);
		menuInventory.setItem(11, blackPane);
		menuInventory.setItem(12, blackPane);
		menuInventory.setItem(13, Pearl);
		menuInventory.setItem(14, blackPane);
		menuInventory.setItem(15, blackPane);
		menuInventory.setItem(16, orangePane);
		menuInventory.setItem(17, redPane);
		//
		menuInventory.setItem(18, orangePane);
		menuInventory.setItem(19, yellowPane);
		menuInventory.setItem(20, blackPane);
		menuInventory.setItem(21, Diamond_Sword);
		menuInventory.setItem(22, Diamond_Pickaxe);
		menuInventory.setItem(23, Diamond_Hoe);
		menuInventory.setItem(24, blackPane);
		menuInventory.setItem(25, yellowPane);
		menuInventory.setItem(26, orangePane);
		//
		menuInventory.setItem(27, orangePane);
		menuInventory.setItem(28, yellowPane);
		menuInventory.setItem(29, blackPane);
		menuInventory.setItem(30, blackPane);
		menuInventory.setItem(31, blackPane);
		menuInventory.setItem(32, blackPane);
		menuInventory.setItem(33, blackPane);
		menuInventory.setItem(34, yellowPane);
		menuInventory.setItem(35, orangePane);
		//
		menuInventory.setItem(36, redPane);
		menuInventory.setItem(37, orangePane);
		menuInventory.setItem(38, blackPane);
		menuInventory.setItem(39, new ItemStack(Material.PLAYER_HEAD, 1));
		menuInventory.setItem(40, Star);
		menuInventory.setItem(41, Emerald);
		menuInventory.setItem(42, blackPane);
		menuInventory.setItem(43, orangePane);
		menuInventory.setItem(44, redPane);
		//
		menuInventory.setItem(45, redPane);
		menuInventory.setItem(46, redPane);
		menuInventory.setItem(47, blackPane);
		menuInventory.setItem(48, blackPane);
		menuInventory.setItem(49, blackPane);
		menuInventory.setItem(50, blackPane);
		menuInventory.setItem(51, blackPane);
		menuInventory.setItem(52, redPane);
		menuInventory.setItem(53, redPane);
	}
	
	static Inventory confirmationInventory = Bukkit.createInventory(null, 9, ChatColor.GOLD + "Warp");
	static {
		// EmeraldBlock
		ItemStack EmeraldBlock = new ItemStack(Material.EMERALD_BLOCK, 1);
		ItemMeta EBMeta = EmeraldBlock.getItemMeta();
		EBMeta.setDisplayName("YES");
		EmeraldBlock.setItemMeta(EBMeta);
		
		// RedstoneBlock
		ItemStack RedstoneBlock = new ItemStack(Material.REDSTONE_BLOCK, 1);
		ItemMeta RBMeta = RedstoneBlock.getItemMeta();
		RBMeta.setDisplayName("NO");
		RedstoneBlock.setItemMeta(RBMeta);
			
		confirmationInventory.setItem(0, new ItemStack(Material.BARRIER, 1));
		//warpInventory.setItem(1, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
		confirmationInventory.setItem(3, EmeraldBlock);
		confirmationInventory.setItem(4, new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));
		confirmationInventory.setItem(5, RedstoneBlock);
		//warpInventory.setItem(5, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
		//warpInventory.setItem(6, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	}
	
	static Inventory warpInventory = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Warp");
	/*static {
		warpInventory.setItem(0, );
		warpInventory.setItem(1, );
		warpInventory.setItem(2,  );
		warpInventory.setItem(3,  );
		warpInventory.setItem(4,  );
		warpInventory.setItem(5,  );
		warpInventory.setItem(6,  );
		warpInventory.setItem(7,  );
		warpInventory.setItem(8,  );
		//
		warpInventory.setItem(9,  );
		warpInventory.setItem(10,  );
		warpInventory.setItem(11,  );
		warpInventory.setItem(12,  );
		warpInventory.setItem(13,  );
		warpInventory.setItem(14,  );
		warpInventory.setItem(15,  );
		warpInventory.setItem(16,  );
		warpInventory.setItem(17,  );
		//
		warpInventory.setItem(18,  );
		warpInventory.setItem(19,  );
		warpInventory.setItem(20,  );
		warpInventory.setItem(21,  );
		warpInventory.setItem(22,  );
		warpInventory.setItem(23,  );
		warpInventory.setItem(24,  );
		warpInventory.setItem(25,  );
		warpInventory.setItem(26,  );
		//
		warpInventory.setItem(27,  );
		warpInventory.setItem(28,  );
		warpInventory.setItem(29,  );
		warpInventory.setItem(30,  );
		warpInventory.setItem(31,  );
		warpInventory.setItem(32,  );
		warpInventory.setItem(33,  );
		warpInventory.setItem(34,  );
		warpInventory.setItem(35,  );
		//
		warpInventory.setItem(36,  );
		warpInventory.setItem(37,  );
		warpInventory.setItem(38,  );
		warpInventory.setItem(39,  );
		warpInventory.setItem(40,  );
		warpInventory.setItem(41,  );
		warpInventory.setItem(42,  );
		warpInventory.setItem(43,  );
		warpInventory.setItem(44,  );
		//
		warpInventory.setItem(45,  );
		warpInventory.setItem(46,  );
		warpInventory.setItem(47,  );
		warpInventory.setItem(48,  );
		warpInventory.setItem(49,  );
		warpInventory.setItem(50,  );
		warpInventory.setItem(51,  );
		warpInventory.setItem(52,  );
		warpInventory.setItem(53,  );
	}*/
	
	static Inventory shopInventory = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Shop Selector");
    static {
		
		// LeatherBoots
		ItemStack LeatherBoots = new ItemStack(Material.LEATHER_BOOTS, 1);
		ItemMeta LbMeta = LeatherBoots.getItemMeta();
		LbMeta.setDisplayName(ChatColor.YELLOW + "Trails");
		LeatherBoots.setItemMeta(LbMeta);
		
		// NoteBlock
		ItemStack NoteBlock = new ItemStack(Material.NOTE_BLOCK, 1);
		ItemMeta NbMeta = NoteBlock.getItemMeta();
		NbMeta.setDisplayName(ChatColor.YELLOW + "Death Sounds");
		NoteBlock.setItemMeta(NbMeta);
		
		// OakSign
		ItemStack OakSign = new ItemStack(Material.OAK_SIGN, 1);
		ItemMeta OsMeta = OakSign.getItemMeta();
		OsMeta.setDisplayName(ChatColor.YELLOW + "Kill Messages");
		OakSign.setItemMeta(OsMeta);
		
	    shopInventory.setItem(0, purplePane);
	    shopInventory.setItem(1, magentaPane);
	    shopInventory.setItem(2, blackPane);
	    shopInventory.setItem(3, blackPane);
	    shopInventory.setItem(4, blackPane);
	    shopInventory.setItem(5, blackPane);
	    shopInventory.setItem(6, blackPane);
	    shopInventory.setItem(7, magentaPane );
	    shopInventory.setItem(8, purplePane);
	    //
	    shopInventory.setItem(9, pinkPane);
	    shopInventory.setItem(10, purplePane);
	    shopInventory.setItem(11, blackPane);
	    shopInventory.setItem(12, blackPane);
	    shopInventory.setItem(13, blackPane);
	    shopInventory.setItem(14, blackPane);
	    shopInventory.setItem(15, blackPane);
	    shopInventory.setItem(16, purplePane);
	    shopInventory.setItem(17, pinkPane);
	    //
	    shopInventory.setItem(18, magentaPane);
	    shopInventory.setItem(19, pinkPane);
	    shopInventory.setItem(20, blackPane);
	    shopInventory.setItem(21, LeatherBoots);
	    shopInventory.setItem(22, NoteBlock);
	    shopInventory.setItem(23, OakSign);
	    shopInventory.setItem(24, blackPane);
	    shopInventory.setItem(25, pinkPane);
	    shopInventory.setItem(26, magentaPane);
	    //
	    shopInventory.setItem(27, pinkPane);
	    shopInventory.setItem(28, magentaPane);
	    shopInventory.setItem(29, blackPane);
	    shopInventory.setItem(30, blackPane);
	    shopInventory.setItem(31, blackPane);
	    shopInventory.setItem(32, blackPane);
	    shopInventory.setItem(33, blackPane);
	    shopInventory.setItem(34, magentaPane);
	    shopInventory.setItem(35, pinkPane);
	    //
	    shopInventory.setItem(36, magentaPane);
	    shopInventory.setItem(37, purplePane);
	    shopInventory.setItem(38, blackPane);
	    shopInventory.setItem(39, blackPane);
	    shopInventory.setItem(40, blackPane);
	    shopInventory.setItem(41, blackPane);
	    shopInventory.setItem(42, blackPane);
	    shopInventory.setItem(43, purplePane);
	    shopInventory.setItem(44, magentaPane);
	    //
	    shopInventory.setItem(45, purplePane);
	    shopInventory.setItem(46, purplePane);
	    shopInventory.setItem(47, LeftArrow);
	    shopInventory.setItem(48, blackPane);
	    shopInventory.setItem(49, Barrier);
	    shopInventory.setItem(50, blackPane);
	    shopInventory.setItem(51, blackPane);
	    shopInventory.setItem(52, purplePane );
	    shopInventory.setItem(53, purplePane);
    }

	
	// passive death sounds shop

	static Inventory shopInventory_deathSounds = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Death Sounds Shop");
    static {
	    
	    // Feather
		ItemStack Feather = new ItemStack(Material.FEATHER, 1);
		ItemMeta FMeta = Feather.getItemMeta();
		FMeta.setDisplayName(ChatColor.YELLOW + "Chicken");
		Feather.setItemMeta(FMeta);
		
		// Porkchop
		ItemStack Porkchop = new ItemStack(Material.PORKCHOP, 1);
		ItemMeta PcMeta = Porkchop.getItemMeta();
		PcMeta.setDisplayName(ChatColor.YELLOW + "Pig");
		Porkchop.setItemMeta(PcMeta);
		
		// Beef
		ItemStack Beef = new ItemStack(Material.BEEF, 1);
		ItemMeta BeMeta = Beef.getItemMeta();
		BeMeta.setDisplayName(ChatColor.YELLOW + "Cow");
		Beef.setItemMeta(BeMeta);
		
		// Sweetberries
		ItemStack SweetBerries = new ItemStack(Material.SWEET_BERRIES, 1);
		ItemMeta SbMeta = SweetBerries.getItemMeta();
		SbMeta.setDisplayName(ChatColor.YELLOW + "Fox");
		SweetBerries.setItemMeta(SbMeta);
		
		// Cod
		ItemStack Cod = new ItemStack(Material.COD, 1);
		ItemMeta CoMeta = Cod.getItemMeta();
		CoMeta.setDisplayName(ChatColor.YELLOW + "Cat");
		Cod.setItemMeta(CoMeta);
		
		// DolphinSpawnEgg
		ItemStack DSP = new ItemStack(Material.DOLPHIN_SPAWN_EGG, 1);
		ItemMeta DSPMeta = DSP.getItemMeta();
		DSPMeta.setDisplayName(ChatColor.YELLOW + "Dolphin");
		DSP.setItemMeta(DSPMeta);
		
		// LlamaSpawnEgg
		ItemStack LSP = new ItemStack(Material.LLAMA_SPAWN_EGG, 1);
		ItemMeta LSPMeta = LSP.getItemMeta();
		LSPMeta.setDisplayName(ChatColor.YELLOW + "Llama");
		LSP.setItemMeta(LSPMeta);
		
		// Bamboo
		ItemStack Bamboo = new ItemStack(Material.BAMBOO, 1);
		ItemMeta BaMeta = Bamboo.getItemMeta();
		BaMeta.setDisplayName(ChatColor.YELLOW + "Panda");
		Bamboo.setItemMeta(BaMeta);
		
		// IronHorseArmor
		ItemStack IHA = new ItemStack(Material.IRON_HORSE_ARMOR, 1);
		ItemMeta IHAMeta = IHA.getItemMeta();
		IHAMeta.setDisplayName(ChatColor.YELLOW + "Horse");
		IHA.setItemMeta(IHAMeta);
	    
	    shopInventory_deathSounds.setItem(0, limePane);
	    shopInventory_deathSounds.setItem(1, limePane);
	    shopInventory_deathSounds.setItem(2, blackPane);
	    shopInventory_deathSounds.setItem(3, blackPane);
	    shopInventory_deathSounds.setItem(4, blackPane);
	    shopInventory_deathSounds.setItem(5, blackPane);
	    shopInventory_deathSounds.setItem(6, blackPane);
	    shopInventory_deathSounds.setItem(7, limePane);
	    shopInventory_deathSounds.setItem(8, limePane);
	    //
	    shopInventory_deathSounds.setItem(9, greenPane);
	    shopInventory_deathSounds.setItem(10, limePane);
	    shopInventory_deathSounds.setItem(11, blackPane);
	    shopInventory_deathSounds.setItem(12, Feather);
	    shopInventory_deathSounds.setItem(13, Porkchop);
	    shopInventory_deathSounds.setItem(14, Beef);
	    shopInventory_deathSounds.setItem(15, blackPane);
	    shopInventory_deathSounds.setItem(16, limePane);
	    shopInventory_deathSounds.setItem(17, greenPane);
	    //
	    shopInventory_deathSounds.setItem(18, greenPane);
	    shopInventory_deathSounds.setItem(19, greenPane);
	    shopInventory_deathSounds.setItem(20, blackPane);
	    shopInventory_deathSounds.setItem(21, SweetBerries);
	    shopInventory_deathSounds.setItem(22, Cod);
	    shopInventory_deathSounds.setItem(23, DSP);
	    shopInventory_deathSounds.setItem(24, blackPane);
	    shopInventory_deathSounds.setItem(25, greenPane);
	    shopInventory_deathSounds.setItem(26, greenPane);
	    //
	    shopInventory_deathSounds.setItem(27, brownPane);
	    shopInventory_deathSounds.setItem(28, brownPane);
	    shopInventory_deathSounds.setItem(29, blackPane);
	    shopInventory_deathSounds.setItem(30, LSP);
	    shopInventory_deathSounds.setItem(31, Bamboo);
	    shopInventory_deathSounds.setItem(32, IHA);
	    shopInventory_deathSounds.setItem(33, blackPane);
	    shopInventory_deathSounds.setItem(34, brownPane);
	    shopInventory_deathSounds.setItem(35, brownPane);
	    //
	    shopInventory_deathSounds.setItem(36, brownPane);
	    shopInventory_deathSounds.setItem(37, brownPane);
	    shopInventory_deathSounds.setItem(38, blackPane);
	    shopInventory_deathSounds.setItem(39, blackPane);
	    shopInventory_deathSounds.setItem(40, blackPane);
	    shopInventory_deathSounds.setItem(41, blackPane);
	    shopInventory_deathSounds.setItem(42, blackPane);
	    shopInventory_deathSounds.setItem(43, brownPane);
	    shopInventory_deathSounds.setItem(44, brownPane);
	    //
	    shopInventory_deathSounds.setItem(45, lightBluePane);
	    shopInventory_deathSounds.setItem(46, cyanPane);
	    shopInventory_deathSounds.setItem(47, LeftArrow);
	    shopInventory_deathSounds.setItem(48, blackPane);
	    shopInventory_deathSounds.setItem(49, Barrier);
	    shopInventory_deathSounds.setItem(50, blackPane);
	    shopInventory_deathSounds.setItem(51, RightArrow);
	    shopInventory_deathSounds.setItem(52, cyanPane);
	    shopInventory_deathSounds.setItem(53, lightBluePane);
    }

    // hostile death sounds shop

    static Inventory shopInventory_hostileDeathSounds = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Hostile Death Sounds Shop");
    static {
		
	    // RottenFlesh
		ItemStack RottenFlesh = new ItemStack(Material.ROTTEN_FLESH, 1);
		ItemMeta RFMeta = RottenFlesh.getItemMeta();
		RFMeta.setDisplayName(ChatColor.YELLOW + "Zombie");
		RottenFlesh.setItemMeta(RFMeta);
		
		// Bone
		ItemStack Bone = new ItemStack(Material.BONE, 1);
		ItemMeta BoMeta = Bone.getItemMeta();
		BoMeta.setDisplayName(ChatColor.YELLOW + "Skeleton");
		Bone.setItemMeta(BoMeta);
		
		// Gunpowder
		ItemStack Gunpowder = new ItemStack(Material.GUNPOWDER, 1);
		ItemMeta GpMeta = Gunpowder.getItemMeta();
		GpMeta.setDisplayName(ChatColor.YELLOW + "Creeper");
		Gunpowder.setItemMeta(GpMeta);
		
		// GhastTear
		ItemStack GhastTear = new ItemStack(Material.GHAST_TEAR, 1);
		ItemMeta GtMeta = GhastTear.getItemMeta();
		GtMeta.setDisplayName(ChatColor.YELLOW + "Ghast");
		GhastTear.setItemMeta(GtMeta);
		
		// GoldenSword
		ItemStack GoldenSword = new ItemStack(Material.GOLD_INGOT, 1);
		ItemMeta GsMeta = GoldenSword.getItemMeta();
		GsMeta.setDisplayName(ChatColor.YELLOW + "Zombie Pigman");
		GoldenSword.setItemMeta(GsMeta);
		
		// BlazeRod
		ItemStack BlazeRod = new ItemStack(Material.BLAZE_ROD, 1);
		ItemMeta BrMeta = BlazeRod.getItemMeta();
		BrMeta.setDisplayName(ChatColor.YELLOW + "Blaze");
		BlazeRod.setItemMeta(BrMeta);
		
		// ChorusFruit
		ItemStack ChorusFruit = new ItemStack(Material.CHORUS_FRUIT, 1);
		ItemMeta CfMeta = ChorusFruit.getItemMeta();
		CfMeta.setDisplayName(ChatColor.YELLOW + "Endermite");
		ChorusFruit.setItemMeta(CfMeta);
		
		// ShulkerShell
		ItemStack ShulkerShell = new ItemStack(Material.SHULKER_SHELL, 1);
		ItemMeta SSMeta = ShulkerShell.getItemMeta();
		SSMeta.setDisplayName(ChatColor.YELLOW + "Shulker");
		ShulkerShell.setItemMeta(SSMeta);
		
		// EnderPearl
		ItemStack EnderPearl = new ItemStack(Material.ENDER_PEARL, 1);
		ItemMeta EpeMeta = EnderPearl.getItemMeta();
		EpeMeta.setDisplayName(ChatColor.YELLOW + "Enderman");
		EnderPearl.setItemMeta(EpeMeta);
		
	    shopInventory_hostileDeathSounds.setItem(0, yellowPane);
	    shopInventory_hostileDeathSounds.setItem(1, yellowPane);
	    shopInventory_hostileDeathSounds.setItem(2, blackPane);
	    shopInventory_hostileDeathSounds.setItem(3, blackPane);
	    shopInventory_hostileDeathSounds.setItem(4, blackPane);
	    shopInventory_hostileDeathSounds.setItem(5, blackPane);
	    shopInventory_hostileDeathSounds.setItem(6, blackPane);
	    shopInventory_hostileDeathSounds.setItem(7, yellowPane);
	    shopInventory_hostileDeathSounds.setItem(8, yellowPane);
	    //
	    shopInventory_hostileDeathSounds.setItem(9,  orangePane);
	    shopInventory_hostileDeathSounds.setItem(10, yellowPane);
	    shopInventory_hostileDeathSounds.setItem(11, blackPane);
	    shopInventory_hostileDeathSounds.setItem(12, RottenFlesh);
	    shopInventory_hostileDeathSounds.setItem(13, Bone);
	    shopInventory_hostileDeathSounds.setItem(14, Gunpowder);
	    shopInventory_hostileDeathSounds.setItem(15, blackPane);
	    shopInventory_hostileDeathSounds.setItem(16, yellowPane);
	    shopInventory_hostileDeathSounds.setItem(17, orangePane);
	    //
	    shopInventory_hostileDeathSounds.setItem(18, orangePane);
	    shopInventory_hostileDeathSounds.setItem(19, orangePane);
	    shopInventory_hostileDeathSounds.setItem(20, blackPane);
	    shopInventory_hostileDeathSounds.setItem(21, GhastTear);
	    shopInventory_hostileDeathSounds.setItem(22, GoldenSword);
	    shopInventory_hostileDeathSounds.setItem(23, BlazeRod);
	    shopInventory_hostileDeathSounds.setItem(24, blackPane);
	    shopInventory_hostileDeathSounds.setItem(25, orangePane);
	    shopInventory_hostileDeathSounds.setItem(26, orangePane);
	    //
	    shopInventory_hostileDeathSounds.setItem(27, redPane);
	    shopInventory_hostileDeathSounds.setItem(28, orangePane);
	    shopInventory_hostileDeathSounds.setItem(29, blackPane);
	    shopInventory_hostileDeathSounds.setItem(30, ChorusFruit);
	    shopInventory_hostileDeathSounds.setItem(31, ShulkerShell);
	    shopInventory_hostileDeathSounds.setItem(32, EnderPearl);
	    shopInventory_hostileDeathSounds.setItem(33, blackPane);
	    shopInventory_hostileDeathSounds.setItem(34, orangePane);
	    shopInventory_hostileDeathSounds.setItem(35, redPane);
	    //
	    shopInventory_hostileDeathSounds.setItem(36, redPane);
	    shopInventory_hostileDeathSounds.setItem(37, redPane);
	    shopInventory_hostileDeathSounds.setItem(38, blackPane);
	    shopInventory_hostileDeathSounds.setItem(39, blackPane);
	    shopInventory_hostileDeathSounds.setItem(40, blackPane);
	    shopInventory_hostileDeathSounds.setItem(41, blackPane);
	    shopInventory_hostileDeathSounds.setItem(42, blackPane);
	    shopInventory_hostileDeathSounds.setItem(43, redPane);
	    shopInventory_hostileDeathSounds.setItem(44, redPane);
	    //
	    shopInventory_hostileDeathSounds.setItem(45, redPane);
	    shopInventory_hostileDeathSounds.setItem(46, redPane);
	    shopInventory_hostileDeathSounds.setItem(47, LeftArrow);
	    shopInventory_hostileDeathSounds.setItem(48, blackPane);
	    shopInventory_hostileDeathSounds.setItem(49, Barrier);
	    shopInventory_hostileDeathSounds.setItem(50, blackPane);
	    shopInventory_hostileDeathSounds.setItem(51, RightArrow);
	    shopInventory_hostileDeathSounds.setItem(52, redPane);
	    shopInventory_hostileDeathSounds.setItem(53, redPane);
    }

 // special and reward death sounds shop

    static Inventory shopInventory_legendaryDeathSounds = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Legendary Death Sounds Shop");
    static {
    	
    	// DragonEgg
    	ItemStack DragonEgg = new ItemStack(Material.DRAGON_EGG, 1);
    	ItemMeta DEMeta = DragonEgg.getItemMeta();
    	DEMeta.setDisplayName(ChatColor.YELLOW + "Ender Dragon");
    	DragonEgg.setItemMeta(DEMeta);
    	
    	// IronBlock
    	ItemStack IronBlock = new ItemStack(Material.IRON_BLOCK, 1);
    	ItemMeta IBMeta = IronBlock.getItemMeta();
    	IBMeta.setDisplayName(ChatColor.YELLOW + "Iron Golem");
    	IronBlock.setItemMeta(IBMeta);
    	
    	// NetherStar
    	ItemStack NetherStar = new ItemStack(Material.NETHER_STAR, 1);
    	ItemMeta NSMeta = NetherStar.getItemMeta();
    	NSMeta.setDisplayName(ChatColor.YELLOW + "Wither");
    	NetherStar.setItemMeta(NSMeta);
    	
    	shopInventory_legendaryDeathSounds.setItem(0, pinkPane);
    	shopInventory_legendaryDeathSounds.setItem(1, purplePane);
    	shopInventory_legendaryDeathSounds.setItem(2, blackPane);
    	shopInventory_legendaryDeathSounds.setItem(3, blackPane);
    	shopInventory_legendaryDeathSounds.setItem(4, blackPane);
    	shopInventory_legendaryDeathSounds.setItem(5, blackPane);
    	shopInventory_legendaryDeathSounds.setItem(6, blackPane);
    	shopInventory_legendaryDeathSounds.setItem(7, purplePane);
    	shopInventory_legendaryDeathSounds.setItem(8, pinkPane);
        //
        shopInventory_legendaryDeathSounds.setItem(9, purplePane);
        shopInventory_legendaryDeathSounds.setItem(10, magentaPane);
        shopInventory_legendaryDeathSounds.setItem(11, blackPane);
        shopInventory_legendaryDeathSounds.setItem(12, blackPane);
        shopInventory_legendaryDeathSounds.setItem(13, blackPane);
        shopInventory_legendaryDeathSounds.setItem(14, blackPane);
        shopInventory_legendaryDeathSounds.setItem(15, blackPane);
        shopInventory_legendaryDeathSounds.setItem(16, magentaPane);
        shopInventory_legendaryDeathSounds.setItem(17, purplePane);
        //
        shopInventory_legendaryDeathSounds.setItem(18, pinkPane);
        shopInventory_legendaryDeathSounds.setItem(19, magentaPane);
        shopInventory_legendaryDeathSounds.setItem(20, blackPane);
        shopInventory_legendaryDeathSounds.setItem(21, IronBlock);
        shopInventory_legendaryDeathSounds.setItem(22, NetherStar);
        shopInventory_legendaryDeathSounds.setItem(23, DragonEgg);
        shopInventory_legendaryDeathSounds.setItem(24, blackPane);
        shopInventory_legendaryDeathSounds.setItem(25, magentaPane);
        shopInventory_legendaryDeathSounds.setItem(26, pinkPane);
        //
        shopInventory_legendaryDeathSounds.setItem(27, magentaPane);
        shopInventory_legendaryDeathSounds.setItem(28, purplePane);
        shopInventory_legendaryDeathSounds.setItem(29, blackPane);
        shopInventory_legendaryDeathSounds.setItem(30, blackPane);
        shopInventory_legendaryDeathSounds.setItem(31, blackPane);
        shopInventory_legendaryDeathSounds.setItem(32, blackPane);
        shopInventory_legendaryDeathSounds.setItem(33, blackPane);
        shopInventory_legendaryDeathSounds.setItem(34, purplePane);
        shopInventory_legendaryDeathSounds.setItem(35, magentaPane);
        //
        shopInventory_legendaryDeathSounds.setItem(36, pinkPane);
        shopInventory_legendaryDeathSounds.setItem(37, purplePane);
        shopInventory_legendaryDeathSounds.setItem(38, blackPane);
        shopInventory_legendaryDeathSounds.setItem(39, blackPane);
        shopInventory_legendaryDeathSounds.setItem(40, blackPane);
        shopInventory_legendaryDeathSounds.setItem(41, blackPane);
        shopInventory_legendaryDeathSounds.setItem(42, blackPane);
        shopInventory_legendaryDeathSounds.setItem(43, purplePane);
        shopInventory_legendaryDeathSounds.setItem(44, pinkPane);
        //
        shopInventory_legendaryDeathSounds.setItem(45, purplePane);
        shopInventory_legendaryDeathSounds.setItem(46, magentaPane);
        shopInventory_legendaryDeathSounds.setItem(47, LeftArrow);
        shopInventory_legendaryDeathSounds.setItem(48, blackPane);
        shopInventory_legendaryDeathSounds.setItem(49, Barrier);
        shopInventory_legendaryDeathSounds.setItem(50, blackPane);
        shopInventory_legendaryDeathSounds.setItem(51, RightArrow);
        shopInventory_legendaryDeathSounds.setItem(52, magentaPane);
        shopInventory_legendaryDeathSounds.setItem(53, purplePane);
    }

    static Inventory shopInventory_killMessages = Bukkit.createInventory(null, 54, ChatColor.GREEN + "Kill Message Shop");
    static {
    
    // Paper
	ItemStack Paper = new ItemStack(Material.PAPER, 1);
	ItemMeta PMeta = Paper.getItemMeta();
	PMeta.setDisplayName(ChatColor.YELLOW + " ");
	Paper.setItemMeta(PMeta);
    
    shopInventory_killMessages.setItem(0, limePane);
    shopInventory_killMessages.setItem(1, limePane);
    shopInventory_killMessages.setItem(2, blackPane);
    shopInventory_killMessages.setItem(3, blackPane);
    shopInventory_killMessages.setItem(4, blackPane);
    shopInventory_killMessages.setItem(5, blackPane);
    shopInventory_killMessages.setItem(6, blackPane);
    shopInventory_killMessages.setItem(7, limePane);
    shopInventory_killMessages.setItem(8, limePane);
    //
    shopInventory_killMessages.setItem(9, greenPane);
    shopInventory_killMessages.setItem(10, limePane);
    shopInventory_killMessages.setItem(11, Paper);
    shopInventory_killMessages.setItem(12, Paper);
    shopInventory_killMessages.setItem(13, Paper);
    shopInventory_killMessages.setItem(14, Paper);
    shopInventory_killMessages.setItem(15, Paper);
    shopInventory_killMessages.setItem(16, limePane);
    shopInventory_killMessages.setItem(17, greenPane);
    //
    shopInventory_killMessages.setItem(18, greenPane);
    shopInventory_killMessages.setItem(19, greenPane);
    shopInventory_killMessages.setItem(20, Paper);
    shopInventory_killMessages.setItem(21, Paper);
    shopInventory_killMessages.setItem(22, Paper);
    shopInventory_killMessages.setItem(23, Paper);
    shopInventory_killMessages.setItem(24, Paper);
    shopInventory_killMessages.setItem(25, greenPane);
    shopInventory_killMessages.setItem(26, greenPane);
    //
    shopInventory_killMessages.setItem(27, brownPane);
    shopInventory_killMessages.setItem(28, brownPane);
    shopInventory_killMessages.setItem(29, Paper);
    shopInventory_killMessages.setItem(30, Paper);
    shopInventory_killMessages.setItem(31, Paper);
    shopInventory_killMessages.setItem(32, Paper);
    shopInventory_killMessages.setItem(33, Paper);
    shopInventory_killMessages.setItem(34, brownPane);
    shopInventory_killMessages.setItem(35, brownPane);
    //
    shopInventory_killMessages.setItem(36, brownPane);
    shopInventory_killMessages.setItem(37, brownPane);
    shopInventory_killMessages.setItem(38, blackPane);
    shopInventory_killMessages.setItem(39, blackPane);
    shopInventory_killMessages.setItem(40, blackPane);
    shopInventory_killMessages.setItem(41, blackPane);
    shopInventory_killMessages.setItem(42, blackPane);
    shopInventory_killMessages.setItem(43, brownPane);
    shopInventory_killMessages.setItem(44, brownPane);
    //
    shopInventory_killMessages.setItem(45, lightBluePane);
    shopInventory_killMessages.setItem(46, cyanPane);
    shopInventory_killMessages.setItem(47, LeftArrow);
    shopInventory_killMessages.setItem(48, blackPane);
    shopInventory_killMessages.setItem(49, Barrier);
    shopInventory_killMessages.setItem(50, blackPane);
    shopInventory_killMessages.setItem(51, RightArrow);
    shopInventory_killMessages.setItem(52, cyanPane);
    shopInventory_killMessages.setItem(53, lightBluePane);
    }
}
