package com.pezapp.relicbuildproxy;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class ProxyPlugin extends Plugin implements Listener {
	
	public static Map<String, ArrayList<ProxiedPlayer>> queues = new HashMap<String, ArrayList<ProxiedPlayer>>();
	public static Map<String, Integer> serverData = new HashMap<String, Integer>();
	
	public static int siegeThreshold = 1;
	
	public final static Timer timer = new Timer();
	
	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerListener(this, this); // This class contains the listeners
		
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Hub());
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new Siege());
		
		getProxy().registerChannel("BungeeCord"); // For Bungee Commands: https://www.spigotmc.org/wiki/bukkit-bungee-plugin-messaging-channel/#message
		getProxy().registerChannel("relicbuild:proxy");
		
		startQueueHandler();
		
		getLogger().info("Relicbuild Proxy Plugin Enabled.");
	}
	
	@Override
	public void onDisable() {
		timer.cancel();
	}
	
	/*@EventHandler
	public void onPlayerHandshake(PlayerHandshakeEvent event)   { // TODO: Wrong Event
		if (BungeeCord.getInstance().getOnlineCount() == siegeThreshold) {
			BungeeCord.getInstance().broadcast(ChatColor.ITALIC + "Siege player minimum has been reached. Use /siege to queue for castle battle.");
		}
	}*/
	
	@EventHandler
	public void onPluginMessage(PluginMessageEvent event) {
        if(event.getTag().equalsIgnoreCase("BungeeCord")) {
            return;
        }
     
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        try {
            String subchannel = in.readUTF();
         
            if(subchannel.equals("QueueSwitch")) {
                String input = in.readUTF();
                String maxPlayers = in.readUTF();
                ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
                
                serverData.put(player.getServer().getInfo().getName(), Integer.valueOf(maxPlayers));
                
                if (!player.getServer().getInfo().getName().equals(input)) {
                	if (!serverData.containsKey(input) || ProxyServer.getInstance().getServerInfo(input).getPlayers().size() < serverData.get(input)) {
                		ServerInfo target = ProxyServer.getInstance().getServerInfo(input);
                		
                		player.connect(target);
                	} else {
                		
                		for (String i : queues.keySet()) {
                			if (queues.get(i).contains(player)) {
                				queues.get(i).remove(player);
                			}
                		}
                		
                		if (!queues.containsKey(input)) {
                			queues.put(input, new ArrayList<ProxiedPlayer>());
                		}
                		
                		queues.get(input).add(player);
                		
                		player.sendMessage(new ComponentBuilder("Server ").color(ChatColor.RED).bold(true).append("Server " + player.getServer().getInfo().getName()  + " is out of space. (" + serverData.get(input) + " slots) There are " + String.valueOf(queues.get(input).size() - 1) + " others waiting.").bold(false).color(ChatColor.GREEN).create());
                	}
                } else {
                	player.sendMessage(new ComponentBuilder("Server ").color(ChatColor.RED).bold(true).append("Already Connected to " + player.getServer().getInfo().getName()).bold(false).color(ChatColor.GREEN).create());
                }
                
            } else if(subchannel.equals("SetHeader")) {
                //String input = in.readUTF();
            	getLogger().info("Header Set.");
                ProxiedPlayer player = (ProxiedPlayer) event.getReceiver();
                
            	player.setTabHeader(new ComponentBuilder(player.getServer().getInfo().getName()).color(ChatColor.RED).bold(true).append("\n  Adventures In Cardboard MC Network ").bold(false).color(ChatColor.GREEN).create(), null);
            }
            
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (NullPointerException e) {
            //e.printStackTrace();
        }
    }
	
	static void startQueueHandler() {
		QueueHandler task = new QueueHandler();
		
		timer.schedule(task, (long) 50, (long) 50);
	}
}