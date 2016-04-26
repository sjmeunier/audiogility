package com.neutrinostorm.audiogility.utils;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;


public class Profile implements Serializable
{
	private int highestLevelId;
	private Map<Integer,Integer> bestTimes;
	
	public Profile()
	{
	    highestLevelId = 0;
	    bestTimes = new HashMap<Integer,Integer>();
	}


	public int getHighestLevelId()
	{
	    return highestLevelId;
	}
	

	public Map<Integer,Integer> getBestTimes()
	{
	    return bestTimes;
	}
	
	public int getBestTime(int levelId)
	{
	    if (bestTimes == null) 
	    	return 9999999;
	    Integer bestTime = bestTimes.get(levelId);
	    return (bestTime == null ? 9999999 : bestTime);
	}
	
	public boolean notifyScore(int levelId, long score)
	{
	    if (levelId > this.highestLevelId)
	    	this.highestLevelId = levelId;

	    if( score < getBestTime(levelId) ) {
	        bestTimes.put(levelId, (int)score);
	        return true;
	    }
	    
	    return false;
	}
	

	
	// Serializable implementation

	@SuppressWarnings( "unchecked" )
	@Override
	public void read(Json json, JsonValue jsonData )
	{
	    // read the some basic properties
		highestLevelId = json.readValue( "highestLevelId", Integer.class, jsonData );
	

	    Map<String,Integer> bestTimes = json.readValue( "bestTimes", HashMap.class, Integer.class, jsonData );
	    for( String levelIdAsString : bestTimes.keySet() ) {
	        int levelId = Integer.valueOf( levelIdAsString );
	        Integer bestTime = bestTimes.get( levelIdAsString );
	        this.bestTimes.put( levelId, bestTime );
	    }

	}
	
	@Override
	public void write(
	    Json json )
	{
	    json.writeValue( "highestLevelId", highestLevelId );
	    json.writeValue( "bestTimes", bestTimes );
	}
}


