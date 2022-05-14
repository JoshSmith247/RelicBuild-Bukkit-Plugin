package com.pezapp.relicbuildlobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class LobbyScoreboard {
	
	public static Scoreboard newScoreboard(String playerName) {
		
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("scoreboard", "dummy", "-- AIC Server --  ");
		
		//Setting where to display the scoreboard/objective (either SIDEBAR, PLAYER_LIST or BELOW_NAME)
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		Score score = objective.getScore(ChatColor.GREEN + "       Welcome to the"); //Get a fake offline player
		score.setScore(10);
		
		score = objective.getScore(ChatColor.GREEN + "  Adventures in Cardboard");
		score.setScore(9);
		
		score = objective.getScore(ChatColor.GREEN + "     Minecraft Server!");
		score.setScore(8);
		
		score = objective.getScore(ChatColor.GREEN + "   ");
		score.setScore(7);
		
		score = objective.getScore(ChatColor.GREEN + "   ");
		score.setScore(6);
		
		score = objective.getScore(ChatColor.GOLD + "Gold: " + com.pezapp.relicbuildcore.Plugin.balances.get(playerName));
		score.setScore(5);
		
		return board;
	}
	
	public static void updateScoreboard(Player player) {
		Scoreboard board = player.getScoreboard();
		
		// Update gold
		Object[] entries = board.getEntries().toArray();
		for (int i = 0; i < entries.length; i++) {
			Object entry = entries[i];
			
			if (entry.toString().contains("Gold:")) {
				Objective objective = board.getObjective("scoreboard");
				
				int last = objective.getScore(entry.toString()).getScore();
				board.resetScores(entry.toString());
				Score score = objective.getScore(ChatColor.GOLD + "Gold: " + com.pezapp.relicbuildcore.Plugin.balances.get(player.getName()));
				score.setScore(last);
			}
		}
	}
}
