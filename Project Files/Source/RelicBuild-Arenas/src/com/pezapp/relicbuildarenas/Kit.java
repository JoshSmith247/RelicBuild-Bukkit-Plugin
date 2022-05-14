package com.pezapp.relicbuildarenas;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class Kit {
	String name;
	Material menuItem;
	String role;
	ChatColor nameColor;
	String description;
	PotionEffectType[] effects;
	ItemStack[] items;
	ItemStack[] armor;
	
	Kit(String kitName, Material menuItem, String role, String description, PotionEffectType[] effects, ItemStack[] items, ItemStack[] armor) {
		this.name = kitName;
		this.menuItem = menuItem;
		this.role = role;
		
		this.nameColor = ChatColor.WHITE;
		if (role.equals("Offense")) {
			this.nameColor = ChatColor.RED;
		} else if (role.equals("Defense")) {
			this.nameColor = ChatColor.GREEN;
		} else if (role.equals("Support")) {
			this.nameColor = ChatColor.BLUE;
		}
		
		this.description = description;
		this.effects = effects;
		this.items = items;
		this.armor = armor;
	}
}
