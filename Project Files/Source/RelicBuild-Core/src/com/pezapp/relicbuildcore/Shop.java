package com.pezapp.relicbuildcore;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Sound;

public class Shop {
	public static Map<String, Integer> DeathSoundPrices = new HashMap<>();
	public static Map<String, Sound> DeathSoundValues = new HashMap<>();
	
	public static Map<String, Integer> KillMessagePrices = new HashMap<>();
	public static Map<String, String> KillMessageValues = new HashMap<>();
	
	public static void setShopPrices() {
		
		// Death Sounds
		
		DeathSoundPrices.put("Chicken", 100);
		DeathSoundPrices.put("Pig", 150);
		DeathSoundPrices.put("Cow", 180);
		DeathSoundPrices.put("Fox", 200);
		DeathSoundPrices.put("Cat", 250);
		DeathSoundPrices.put("Panda", 300);
		DeathSoundPrices.put("Dolphin", 320);
		DeathSoundPrices.put("Llama", 400);
		DeathSoundPrices.put("Horse", 1000);
		DeathSoundPrices.put("Zombie", 500);
		DeathSoundPrices.put("Skeleton", 650);
		DeathSoundPrices.put("Creeper", 700);
		DeathSoundPrices.put("Zombie Pigman", 800);
		DeathSoundPrices.put("Blaze", 900);
		DeathSoundPrices.put("Ghast", 1000);
		DeathSoundPrices.put("Endermite", 1200);
		DeathSoundPrices.put("Shulker", 1500);
		DeathSoundPrices.put("Enderman", 1800);
		
		DeathSoundPrices.put("Iron Golem", null);
		DeathSoundPrices.put("Wither", null);
		DeathSoundPrices.put("Ender Dragon", null);
		
		DeathSoundValues.put("Chicken", Sound.ENTITY_CHICKEN_DEATH);
		DeathSoundValues.put("Pig", Sound.ENTITY_PIG_DEATH);
		DeathSoundValues.put("Cow", Sound.ENTITY_COW_DEATH);
		DeathSoundValues.put("Fox", Sound.ENTITY_FOX_DEATH);
		DeathSoundValues.put("Cat", Sound.ENTITY_CAT_DEATH);
		DeathSoundValues.put("Panda", Sound.ENTITY_PANDA_DEATH);
		DeathSoundValues.put("Dolphin", Sound.ENTITY_DOLPHIN_DEATH);
		DeathSoundValues.put("Llama", Sound.ENTITY_LLAMA_DEATH);
		DeathSoundValues.put("Horse", Sound.ENTITY_HORSE_DEATH);
		DeathSoundValues.put("Zombie", Sound.ENTITY_ZOMBIE_DEATH);
		DeathSoundValues.put("Skeleton", Sound.ENTITY_SKELETON_DEATH);
		DeathSoundValues.put("Creeper", Sound.ENTITY_CREEPER_DEATH);
		DeathSoundValues.put("Zombie Pigman", Sound.ENTITY_PIGLIN_DEATH);
		DeathSoundValues.put("Blaze", Sound.ENTITY_BLAZE_DEATH);
		DeathSoundValues.put("Ghast", Sound.ENTITY_GHAST_DEATH);
		DeathSoundValues.put("Endermite", Sound.ENTITY_ENDERMITE_DEATH);
		DeathSoundValues.put("Shulker", Sound.ENTITY_SHULKER_DEATH);
		DeathSoundValues.put("Enderman", Sound.ENTITY_ENDERMAN_DEATH);
		DeathSoundValues.put("Iron Golem", Sound.ENTITY_IRON_GOLEM_DEATH);
		DeathSoundValues.put("Wither", Sound.ENTITY_WITHER_DEATH);
		DeathSoundValues.put("Ender Dragon", Sound.ENTITY_ENDER_DRAGON_DEATH);
		
		// Kill Messages
		
		KillMessagePrices.put("Slapped", 200);
		KillMessagePrices.put("Tripped", 200);
		KillMessagePrices.put("Flicked", 200);
		KillMessagePrices.put("Feathered", 200);
		KillMessagePrices.put("Pwned", 200);
		KillMessagePrices.put("Rekt", 200);
		KillMessagePrices.put("Boofed", 200);
		//KillMessagePrices.put("Curb Stomp", 200);
		KillMessagePrices.put("Beaned", 200);
		KillMessagePrices.put("Land Down Under", 200);
		KillMessagePrices.put("Brazil", 200);
		KillMessagePrices.put("The Shadow Realm", 200);
		KillMessagePrices.put("Murked", 500);
		//KillMessagePrices.put("Custom (with admin aproval)", 200);
		
		KillMessagePrices.put("Downed", 200);
		KillMessagePrices.put("Dropped", 200);
		KillMessagePrices.put("Shot down", 200);
		KillMessagePrices.put("Vaporized", 200);
		KillMessagePrices.put("Toasted", 200);
		KillMessagePrices.put("Eastwooded", 200);
		KillMessagePrices.put("Sent home", 200);
		KillMessagePrices.put("Run Over", 200);
		KillMessagePrices.put("Ate it", 200);
		KillMessagePrices.put("Underestimated", 200);
		KillMessagePrices.put("Hax", 200);
		KillMessagePrices.put("Lunched", 200);
		KillMessagePrices.put("Pickled", 200);
		KillMessagePrices.put("Redacted", 200);
		KillMessagePrices.put("Processed", 200);
		KillMessagePrices.put("Banished", 200);
		KillMessagePrices.put("Admined", 200);
		KillMessagePrices.put("Deleted", 200);
		KillMessagePrices.put("Prosecution", 200);
		KillMessagePrices.put("Life Insurance", 200);
		KillMessagePrices.put("Rent", 200);
		KillMessagePrices.put("Pirate booty", 200);
		KillMessagePrices.put("Ended", 200);
		KillMessagePrices.put("Yee hawed", 200);
		KillMessagePrices.put("Confusion", 200);
		KillMessagePrices.put("Grounded", 200);
		KillMessagePrices.put("Achillesed", 200);
		KillMessagePrices.put("Tossed", 200);
		KillMessagePrices.put("Unvictoried", 200);
		KillMessagePrices.put("Cut Short", 200);
		KillMessagePrices.put("Tricked", 200);
		KillMessagePrices.put("Unlocked", 200);
		KillMessagePrices.put("Underestimated", 200);
		KillMessagePrices.put("Demise", 200);
		KillMessagePrices.put("Midas Touch", 200);
		KillMessagePrices.put("Fe Fi Fo Fumed", 200);
		KillMessagePrices.put("Origamied", 200);
		
		KillMessagePrices.put("Emoji", 200);
		
		KillMessagePrices = sortByValue(KillMessagePrices);
		
		KillMessageValues.put("Slapped", "Dummy1 was slapped by Dummy2");
		KillMessageValues.put("Tripped", "Dummy1 was tripped by Dummy2");
		KillMessageValues.put("Flicked", "Dummy1 was flicked by Dummy2");
		KillMessageValues.put("Feathered", "Dummy1 was feathered by Dummy2");
		KillMessageValues.put("Pwned", "Dummy1 got pwned by Dummy2");
		KillMessageValues.put("Rekt", "Dummy1 got rekt by Dummy2");
		KillMessageValues.put("Boofed", "Dummy1 was boofed by Dummy2");
		//KillMessageValues.put("Curb Stomp", "Dummy1 was curb stomped by Dummy2");
		KillMessageValues.put("Beaned", "Dummy1 got beaned by Dummy2");
		KillMessageValues.put("Land Down Under", "Dummy1 was sent to the Land Down Under by Dummy2");
		KillMessageValues.put("Brazil", "Dummy1 was sent to Brazil by Dummy2");
		KillMessageValues.put("The Shadow Realm", "Dummy1 was sent to The Shadow Realm by Dummy2");
		KillMessageValues.put("Murked", "Dummy1 got murked by Dummy2");
		//KillMessageValues.put("Custom (with admin aproval)", "Dummy1 was (N/A) by Dummy2");
		
		KillMessageValues.put("Downed", "Dummy1 was downed by Dummy2");
		KillMessageValues.put("Dropped", "Dummy1 was dropped by Dummy2");
		KillMessageValues.put("Shot down", "Dummy1 was no-scoped by Dummy2");
		KillMessageValues.put("Vaporized", "Dummy1 was vaporized by Dummy2");
		KillMessageValues.put("Toasted", "Dummy1 was toasted and put on graham crackers by Dummy2");
		KillMessageValues.put("Eastwooded", "Dummy2 decided there wasn't enough room for Dummy1 on this server");
		KillMessageValues.put("Sent home", "Dummy1 was given a juice box and sent home by Dummy2");
		KillMessageValues.put("Run Over", "Dummy1 made like a deer and got run over by Dummy2");
		KillMessageValues.put("Ate it", "Dummy1 tried to eat Dummy2's lunch");
		KillMessageValues.put("Underestimated", "Dummy1 mistakenly challenged Dummy2's high ground");
		KillMessageValues.put("Hax", "Dummy1 was hacked to death by Dummy2");
		KillMessageValues.put("Lunched", "Dummy1 was force-fed mystery meatloaf by Dummy2");
		KillMessageValues.put("Pickled", "Dummy1 was turned into a pickle by Dummy2");
		KillMessageValues.put("Redacted", "Dummy1 was permanently redacted by Dummy2");
		KillMessageValues.put("Processed", "Dummy1 was processed into Spam by Dummy2");
		KillMessageValues.put("Banished", "Dummy1 was banished to Iztan by Dummy2");
		KillMessageValues.put("Admined", "Dummy1 forgot that Dummy2 was a moderator");
		KillMessageValues.put("Deleted", "Dummy1 was deleted by dummy2");
		KillMessageValues.put("Prosecution", "Dummy1 was prosecuted by dummy2");
		KillMessageValues.put("Life Insurance", "Dummy2 claimed Dummy1's life insurance");
		KillMessageValues.put("Rent", "Dummy1 forgot to pay rent for living to Dummy2");
		KillMessageValues.put("Pirate booty", "Dummy1 ate Dummy2's last doubloon");
		KillMessageValues.put("Ended", "Dummy1 was ended by Dummy2");
		KillMessageValues.put("Yee hawed", "Dummy1 yee'd their last haw to Dummy2");
		KillMessageValues.put("Confusion", "Dummy1 was killed to death by Dummy2");
		KillMessageValues.put("Grounded", "Dummy1 was ground to dust by Dummy2");
		KillMessageValues.put("Achillesed", "Dummy1 was shot in the heel by Dummy2");
		KillMessageValues.put("Tossed", "Dummy1's corpse was thrown into the air by Dummy2");
		KillMessageValues.put("Unvictoried", "Dummy1's victory was short lived by Dummy2");
		KillMessageValues.put("Cut Short", "Dummy1's life was cut short by Dummy2");
		KillMessageValues.put("Tricked", "Dummy1 was tricked into removing their hard hat by Dummy2");
		KillMessageValues.put("Unlocked", "Dummy1 gave Dummy2 their password");
		KillMessageValues.put("Underestimated", "Dummy1 underestimated Dummy2's power");
		KillMessageValues.put("Demise", "Dummy1 met their own demise at the hand of Dummy2");
		KillMessageValues.put("Midas Touch", "Dummy1 was turned to gold by Dummy2");
		KillMessageValues.put("Fe Fi Fo Fumed", "Dummy1's Bones were ground to make Dummy2's bread");
		KillMessageValues.put("Origamied", "Dummy1 was folded into a snowflake by Dummy2");
		KillMessageValues.put("Emoji", "Dummy2  ◎==||:::::::> Dummy1");
	}
	
	public static Map<String, Integer> sortByValue(Map<String, Integer> thisMap) {
		
        List<Map.Entry<String, Integer> > list = new LinkedList<Map.Entry<String, Integer> >(thisMap.entrySet());
 
        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        
        // Turn List Back into Hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
