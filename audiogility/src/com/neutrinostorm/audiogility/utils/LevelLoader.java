package com.neutrinostorm.audiogility.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

public class LevelLoader {
	public static Level LoadLevel(int levelId) {
		FileHandle levelFileHandle = Gdx.files.internal( "levels/levels.xml" );
		Level level = new Level();
		
		try{
			XmlReader reader = new XmlReader();
			Element root = reader.parse(levelFileHandle);
			Array<Element> sectionElements = root.getChildrenByName("section");
			for(Element sectionElement : sectionElements) {
				Array<Element> levelElements = sectionElement.getChildrenByName("level");
				for (Element levelElement : levelElements)
				{
					int id = Integer.parseInt(levelElement.getAttribute("id"));
					if (id != levelId)
						continue;
					
					level.setId(id);
					level.setName(levelElement.getAttribute("name"));
					level.setShortName(levelElement.getAttribute("shortName"));
					level.setGoldTime(Integer.parseInt(levelElement.getAttribute("goldTime")));
					level.setSilverTime(Integer.parseInt(levelElement.getAttribute("silverTime")));
					level.setBronzeTime(Integer.parseInt(levelElement.getAttribute("bronzeTime")));
					
					WaveForm[] waveForms = new WaveForm[4];
					int waveCount = 0;
					
					Array<Element> waveElements = levelElement.getChildByName("waveForms").getChildrenByName("wave");
					
					for(Element waveElement : waveElements) {
						waveForms[waveCount] = new WaveForm(
								Integer.parseInt(waveElement.getAttribute("frequency")),
								Integer.parseInt(waveElement.getAttribute("amplitude")),
								WaveType.values()[Integer.parseInt(waveElement.getAttribute("type"))],
								0, 0, 0, 0);		
						waveCount++;
					}
					if (waveCount < 4) {
						for(int i = waveCount; i < 4; i++) {
							waveForms[i] = new WaveForm(0, 0, WaveType.Sine,0, 0, 0, 0);
						}
					}
					
					level.setWaveCount(waveCount);
					level.setWaveForms(waveForms);
					return level;
	
				}
			}
		} catch (IOException e){
			
		}
		
		return level;
	}
	
	public static Level[] LoadSection(int sectionId) {
		FileHandle levelFileHandle = Gdx.files.internal( "levels/levels.xml" );
		
		List<Level> levels = new ArrayList<Level>();
	
		try{
			XmlReader reader = new XmlReader();
			Element root = reader.parse(levelFileHandle);
			Array<Element> sectionElements = root.getChildrenByName("section");
			for(Element sectionElement : sectionElements) {
				if (Integer.parseInt(sectionElement.getAttribute("sectionId")) == sectionId){
					Array<Element> levelElements = sectionElement.getChildrenByName("level");
					for (Element levelElement : levelElements)
					{
						Level level = new Level();
						level.setId(Integer.parseInt(levelElement.getAttribute("id")));
						level.setName(levelElement.getAttribute("name"));
						level.setShortName(levelElement.getAttribute("shortName"));
						level.setGoldTime(Integer.parseInt(levelElement.getAttribute("goldTime")));
						level.setSilverTime(Integer.parseInt(levelElement.getAttribute("silverTime")));
						level.setBronzeTime(Integer.parseInt(levelElement.getAttribute("bronzeTime")));
						
						WaveForm[] waveForms = new WaveForm[4];
						int waveCount = 0;
						
						Array<Element> waveElements = levelElement.getChildByName("waveForms").getChildrenByName("wave");
						
						for(Element waveElement : waveElements) {
							waveForms[waveCount] = new WaveForm(
									Integer.parseInt(waveElement.getAttribute("frequency")),
									Integer.parseInt(waveElement.getAttribute("amplitude")),
									WaveType.values()[Integer.parseInt(waveElement.getAttribute("type"))],
									0, 0, 0, 0);	
							waveCount++;
						}
						
						level.setWaveCount(waveCount);
						
						if (waveCount < 4) {
							for(int i = waveCount; i < 4; i++) {
								waveForms[i] = new WaveForm(0, 0, WaveType.Sine,0, 0, 0, 0);
							}
						}
						level.setWaveForms(waveForms);
						levels.add(level);
					}
					break;
				}
			}
		} catch (IOException e){
			
		}
		
		return levels.toArray(new Level[levels.size()]);
	}
}
