package com.pezapp.relicbuildproxy;

import java.util.ArrayList;
import java.util.Map;
import java.util.TimerTask;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class QueueHandler extends TimerTask {
	public void run() {
		Map<String, ArrayList<ProxiedPlayer>> queues = com.pezapp.relicbuildproxy.ProxyPlugin.queues;
		
		for (String i : queues.keySet()) {
			queues.get(i).forEach(player -> {
				if (ProxyServer.getInstance().getServerInfo(i).getPlayers().size() < com.pezapp.relicbuildproxy.ProxyPlugin.serverData.get(i)) {
					ServerInfo target = ProxyServer.getInstance().getServerInfo(i);
					
					player.connect(target);
					
					// Remove player from queue
					queues.get(i).remove(player);
				}
			});
		}
	}
}
