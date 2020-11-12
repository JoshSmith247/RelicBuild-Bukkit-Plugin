package com.pezapp.relicbuild;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CommandManager implements Listener {
	
	/*
	Plugin plugin;
    public CommandManager(Plugin instance) {
          this.plugin = instance; //now you can access plugin
    }*/
    
    public static Map<String, Long> grantedOps = new HashMap<String, Long>();
    public Timer timer = com.pezapp.relicbuild.Plugin.timer;
    public static boolean trailsEnabled = false;
    
    @EventHandler
    public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("relicbuild")) {
	    	sender.sendMessage(ChatColor.GREEN + "[RelicBuild Plugin Features/Info]");
	    	sender.sendMessage("- Combat Enhancements");
	    	return false;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("grantop")) {
    		if (args.length == 2) {
    			grantedOps.put(args[0], Long.parseLong(args[1]));
	    		sender.sendMessage(ChatColor.GREEN + "Granted player op.");
	    		sender.sendMessage("To remove player op, use command /remop");
	    		return false;
    		}
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("remop")) {
    		if (args.length == 1) {
    			grantedOps.remove(args[0]);
	    		sender.sendMessage(ChatColor.GREEN + "Removed player op.");
	    		return false;
    		}
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("listops")) {
	    	sender.sendMessage(grantedOps.toString());
	    	return false;
    	}
    	if(cmd.getName().equalsIgnoreCase("enabletrails")) {
    		trailsEnabled = true;
	    	sender.sendMessage(ChatColor.DARK_GRAY+"Trails Enabled.");
	    	return false;
    	}
    	if(cmd.getName().equalsIgnoreCase("disabletrails")) {
    		trailsEnabled = false;
	    	sender.sendMessage(ChatColor.DARK_GRAY+"Trails Disabled.");
	    	return false;
    	}
        return false;
    }
}
