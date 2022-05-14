package com.pezapp.relicbuildproxy;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Siege extends Command {
	
	public static ArrayList<ProxiedPlayer> siegeQueue = new ArrayList<>();
	
	public Siege() {
		super("Siege");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if ((sender instanceof ProxiedPlayer)) {
			ProxiedPlayer p = (ProxiedPlayer) sender;
			
			if (!siegeQueue.contains(p)) {
				siegeQueue.add(p);
				
				siegeQueue.forEach(player -> {
					player.sendMessage(new ComponentBuilder("Siege ").color(ChatColor.RED).bold(true).append(p.getDisplayName() + " has joined the Castle Battle queue. (" + siegeQueue.size() + "/" + com.pezapp.relicbuildproxy.ProxyPlugin.siegeThreshold + " needed).").bold(false).color(ChatColor.GREEN).create());
				});
				
				if (siegeQueue.size() == com.pezapp.relicbuildproxy.ProxyPlugin.siegeThreshold) {
					siegeQueue.forEach(player -> {
						player.connect(ProxyServer.getInstance().getServerInfo("arenas1"));
					});
				}
			} else {
				siegeQueue.remove(p);
				p.sendMessage(new ComponentBuilder("Siege ").color(ChatColor.RED).bold(true).append("Leaving queue. (" + siegeQueue.size() + "/" + com.pezapp.relicbuildproxy.ProxyPlugin.siegeThreshold + " needed).").bold(false).color(ChatColor.GREEN).create());
				
				siegeQueue.forEach(player -> {
					player.sendMessage(new ComponentBuilder("Siege ").color(ChatColor.RED).bold(true).append(p.getDisplayName() + " left the Castle Battle queue. (" + siegeQueue.size() + "/" + com.pezapp.relicbuildproxy.ProxyPlugin.siegeThreshold + " needed).").bold(false).color(ChatColor.GREEN).create());
				});
			}
		}
	}
}
