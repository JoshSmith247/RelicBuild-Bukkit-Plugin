package com.pezapp.relicbuildcore;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class CommandManager implements Listener {
	
	/*
	Plugin plugin;
    public CommandManager(Plugin instance) {
          this.plugin = instance; //now you can access plugin
    }*/
    
    public static Map<String, Long> grantedOps = new HashMap<String, Long>();

    public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("rb")) {
    		if (args.length == 0) {
    			sender.sendMessage(ChatColor.AQUA + "Running Relicbuild Core. Commands: /rb textures");
    			return false;
    		} 
    		if (args.length == 1) {
    			if (args[0].equalsIgnoreCase("textures")) {
    				if (com.pezapp.relicbuildcore.Plugin.texturesEnabled == true) {
    					com.pezapp.relicbuildcore.Plugin.texturesEnabled = false;
    					sender.sendMessage(ChatColor.AQUA + "Textures disabled.");
    				} else {
    					com.pezapp.relicbuildcore.Plugin.texturesEnabled = true;
    					sender.sendMessage(ChatColor.AQUA + "Textures enabled.");
    				}
    			}
	    		return false;
    		}
    		if (args.length == 3) {
    			if (args[0].equalsIgnoreCase("credits")) {
    				Plugin.changeCredits(Bukkit.getPlayer(args[1]), Integer.valueOf(args[2]), "Credits added via command.", ChatColor.GOLD, Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
    				sender.sendMessage(args[2] + " credits granted to " + args[1] + ".");
    			}
    			return false;
    		}
    	}
    	
    	return false;
    }
}
