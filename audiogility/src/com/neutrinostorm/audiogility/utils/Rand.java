package com.neutrinostorm.audiogility.utils;

import java.util.Random;

public class Rand {
	private Random random = new Random();
	
	public Rand()
	{
		
	}
	
	public int getInt(int min, int max)
	{
		return random.nextInt(max - min) + min;
	}
	
	public int getInt(int min, int max, int exclude)
	{
		int num = 0;
		boolean found = false;
		
		while(!found) {
			num = random.nextInt(max - min) + min;
			if (num != exclude)
				found = true;
		}
		
		return num;
	}
}
