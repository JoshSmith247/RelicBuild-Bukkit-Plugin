package com.pezapp.relicbuildlobby;

import java.util.ArrayList;

public class EasterEgg {
	int EggNumber;
	int x, y, z;
	
	public EasterEgg(int EggNumber, int x, int y, int z) {
		this.EggNumber = EggNumber;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static void setEggs() {
		ArrayList<EasterEgg> EasterEggs = Plugin.EasterEggs;
		
		EasterEggs.add(new EasterEgg(1, -113, 154, 97));
		EasterEggs.add(new EasterEgg(2, -99, 155, 71));
		EasterEggs.add(new EasterEgg(3, -120, 164, 153));
		EasterEggs.add(new EasterEgg(4, -74, 181, 81));
		EasterEggs.add(new EasterEgg(5, -21, 158, 97));
		EasterEggs.add(new EasterEgg(6, -80, 156, 117));
	}
	
	public static int checkIfEgg(int x, int y, int z) {
		for (int i = 0; i < Plugin.EasterEggs.size(); i++) {
			EasterEgg egg = Plugin.EasterEggs.get(i);
			
			if (egg.x == x && egg.y == y && egg.z == z) {
				return egg.EggNumber;
			}
		};
		
		return -1;
	}
}
