package com.pezapp.relicbuildlobby;

import java.util.Random;

public class Messager {
	static String[] messages = {
			"Contact an admin if you need any support!",
			"Charging attacks can be more effective than spamming attacks.",
			"Use bows to gain an advantage before charging at your opponent.",
			"Easter Eggs are hidden around the lobby. Find all of them for a special reward!",
			"Reach out to an admin for the Discord VoiceChat Link.",
			"Use the '/siege' command to join the castle battle waitlist. You can play while you wait!"
	};
	
	public static String randMessage() {
		return "[Useful Tips] "+messages[new Random().nextInt(messages.length)];
	}
}
