package com.neutrinostorm.audiogility.utils;

public class Level {
	private String name;
	private String shortName;
	private int id;
	private WaveForm[] waveForms;
	private int waveCount;
	
	private int goldTime;
	private int silverTime;
	private int bronzeTime;
	
	public Level()
	{
		
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}

	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}
	
	public String getShortName()
	{
		return this.shortName;
	}
	public void setWaveCount(int waveCount)
	{
		this.waveCount = waveCount;
	}
	
	public int getWaveCount()
	{
		return this.waveCount;
	}

	public void setWaveForms(WaveForm[] waveForms)
	{
		this.waveForms = waveForms;
	}
	
	public WaveForm[] getWaveForms()
	{
		return this.waveForms;
	}
	
	public void setGoldTime(int goldTime)
	{
		this.goldTime = goldTime;
	}
	
	public int getGoldTime()
	{
		return this.goldTime;
	}
	
	public void setSilverTime(int silverTime)
	{
		this.silverTime = silverTime;
	}
	
	public int getSilverTime()
	{
		return this.silverTime;
	}
	
	public void setBronzeTime(int bronzeTime)
	{
		this.bronzeTime = bronzeTime;
	}
	
	public int getBronzeTime()
	{
		return this.bronzeTime;
	}
	
}
