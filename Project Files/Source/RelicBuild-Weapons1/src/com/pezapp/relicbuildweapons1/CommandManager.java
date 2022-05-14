package com.pezapp.relicbuildweapons1;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public class CommandManager implements Listener {

    public static boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if(cmd.getName().equalsIgnoreCase("rb")) {
    		if (args.length == 2 && args[0].equals("weapons1")) {
    			if (args[2].equals("enable")) {
    				com.pezapp.relicbuildweapons1.Plugin.isActive = true;
    				sender.sendMessage(ChatColor.GRAY + "Weapons1 has been enabled");
    				
    			} else if (args[2].equals("disable")) {
    				com.pezapp.relicbuildweapons1.Plugin.isActive = false;
    				sender.sendMessage(ChatColor.GRAY + "Weapons1 has been disabled");
    				
    			}
    		}
    	}
    	
    	return false;
    }
}
