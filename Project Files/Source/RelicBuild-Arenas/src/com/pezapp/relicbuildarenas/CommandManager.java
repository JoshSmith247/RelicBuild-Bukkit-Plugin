package com.pezapp.relicbuildarenas;

import java.util.HashMap;
import java.util.Map;

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
    	if(cmd.getName().equalsIgnoreCase("arenas")) {
    		if (args.length == 1) {
    			
	    		return false;
    		}
    	}
    	/*if(cmd.getName().equalsIgnoreCase("listArenas")) {
	    	sender.sendMessage(ChatColor.GREEN + "Arena Groups: ");
	    	Plugin.groups.keySet().forEach(groupName -> {
	    		sender.sendMessage(ChatColor.GREEN + "1. " + groupName + " -- " + Plugin.groups.get(groupName).length + " Players");
	    	});
	    	//sender.sendMessage(ChatColor.GREEN + "Arena Teams: " + Plugin.arenaTeams);
	    	//sender.sendMessage(ChatColor.GREEN + "Arena Statuses: " + Plugin.arenaStatuses);
	    	//sender.sendMessage(ChatColor.GREEN + "Arena Locations: " + Plugin.arenaLocations);
	    	return false;
    	}
    	if(cmd.getName().equalsIgnoreCase("addGroup")) {
    		Plugin.newGroup(String.valueOf(Plugin.groups.size()), sender.getName());
	    	sender.sendMessage(ChatColor.GREEN + "Group added.");
	    	return false;
    	}
    	if(cmd.getName().equalsIgnoreCase("leaveGroup")) {
	    	sender.sendMessage(ChatColor.GREEN + "Group left.");
	    	Plugin.leaveArenas(sender.getName());
	    	return false;
    	}*/
    	/*if(cmd.getName().equalsIgnoreCase("instantAction")) {
    		if (Plugin.instantAction.get(sender.getName()) == true) {
    			Plugin.instantAction.put(sender.getName(), false);
    		} else {
    			Plugin.instantAction.put(sender.getName(), true);
    		}
	    	sender.sendMessage(ChatColor.GREEN + "Instant action");
	    	Plugin.leaveArenas(sender.getName());
	    	return false;
    	}*/
    	return false;
    }
}
