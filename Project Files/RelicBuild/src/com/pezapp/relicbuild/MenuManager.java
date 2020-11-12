package com.pezapp.relicbuild;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuManager implements Listener {
	
	public Plugin plugin;
    public MenuManager(Plugin instance)
    {
        plugin = instance;
    }
    
	@EventHandler
	public void PlayerInteractEvent(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Player eventPlayer = event.getPlayer();
			ItemStack attackItem = eventPlayer.getInventory().getItemInMainHand();
			if (attackItem.getType() == Material.COMPASS) { // && Get name
				//ItemStack adminHelp = new ItemStack(Material.MAP, 1);
				//ItemMeta helpMeta = adminHelp.getItemMeta();
				for (int i = 0; i < menuInventory.getSize(); i++) { // Look through every item
					ItemMeta itemMeta = menuInventory.getItem(i).getItemMeta();
					itemMeta.setDisplayName(" ");
				}
				eventPlayer.openInventory(menuInventory);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked(); // The player that clicked the item
		ItemStack clicked = event.getCurrentItem(); // The item that was clicked
		//Inventory inventory = event.getInventory(); // The inventory that was clicked in
		if (player.getOpenInventory().getTitle().equals(ChatColor.DARK_GREEN + "Menu")) { // The inventory is our custom Inventory
			if (clicked.getType() == Material.ENDER_PEARL) { // The item that the player clicked it dirt
				player.closeInventory(); // Closes there inventory
				player.openInventory(warpInventory);
			}
			event.setCancelled(true);
		} else if (player.getOpenInventory().getTitle().equals(ChatColor.GOLD + "Warp")) {
			if (clicked.getType() == Material.ENDER_PEARL) {
				player.closeInventory();
			}
			event.setCancelled(true);
		}
	}
	
	// Main Menu:
	// 1 = inventory owner, 2 = # of slots (multiple of 9), 3 = name
	static Inventory menuInventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GREEN + "Menu");
	static {
	menuInventory.setItem(0, new ItemStack(Material.MAP, 1));
	menuInventory.setItem(1, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(3, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(4, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(5, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(6, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(7, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(8, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	//
	menuInventory.setItem(9, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(10, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(11, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(12, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(13, new ItemStack(Material.PAPER, 1));
	menuInventory.setItem(14, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(15, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(16, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(17, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	//
	menuInventory.setItem(18, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(19, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(20, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(21, new ItemStack(Material.BEDROCK, 1));
	menuInventory.setItem(22, new ItemStack(Material.IRON_SWORD, 1));
	menuInventory.setItem(23, new ItemStack(Material.GRASS_BLOCK, 1));
	menuInventory.setItem(24, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(25, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(26, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1));
	//
	menuInventory.setItem(27, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(28, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(29, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(30, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(31, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(32, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(33, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(34, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(35, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1));
	//
	menuInventory.setItem(36, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(37, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(38, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(39, new ItemStack(Material.IRON_HELMET, 1));
	menuInventory.setItem(40, new ItemStack(Material.ENDER_PEARL, 1));
	menuInventory.setItem(41, new ItemStack(Material.EMERALD, 1));
	menuInventory.setItem(42, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(43, new ItemStack(Material.ORANGE_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(44, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	//
	menuInventory.setItem(45, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(46, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(47, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(48, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(49, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(50, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(51, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(52, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	menuInventory.setItem(53, new ItemStack(Material.RED_STAINED_GLASS_PANE, 1));
	}
	
	static Inventory warpInventory = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Warp");
	static {
	warpInventory.setItem(0, new ItemStack(Material.MAP, 1));
	warpInventory.setItem(1, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(2, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(3, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(4, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(5, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(6, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(7, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(8, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	//
	warpInventory.setItem(9, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(10, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(11, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(12, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(13, new ItemStack(Material.ENDER_EYE, 1));
	warpInventory.setItem(14, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(15, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(16, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(17, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	//
	warpInventory.setItem(18, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(19, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(20, new ItemStack(Material.OBSIDIAN, 1));
	warpInventory.setItem(21, new ItemStack(Material.BEDROCK, 1));
	warpInventory.setItem(22, new ItemStack(Material.ENDER_EYE, 1));
	warpInventory.setItem(23, new ItemStack(Material.GRASS_BLOCK, 1));
	warpInventory.setItem(24, new ItemStack(Material.OBSIDIAN, 1));
	warpInventory.setItem(25, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(26, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
	//
	warpInventory.setItem(27, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(28, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(29, new ItemStack(Material.OBSIDIAN, 1));
	warpInventory.setItem(30, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(31, new ItemStack(Material.ENDER_EYE, 1));
	warpInventory.setItem(32, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(33, new ItemStack(Material.OBSIDIAN, 1));
	warpInventory.setItem(34, new ItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(35, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
	//
	warpInventory.setItem(36, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(37, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(38, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(39, new ItemStack(Material.IRON_HELMET, 1));
	warpInventory.setItem(40, new ItemStack(Material.ENDER_EYE, 1));
	warpInventory.setItem(41, new ItemStack(Material.EMERALD, 1));
	warpInventory.setItem(42, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(43, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(44, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	//
	warpInventory.setItem(45, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(46, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(47, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(48, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(49, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(50, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(51, new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(52, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	warpInventory.setItem(53, new ItemStack(Material.PURPLE_STAINED_GLASS_PANE, 1));
	}
}
