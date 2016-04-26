package com.neutrinostorm.audiogility.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.neutrinostorm.audiogility.utils.Level;

public class LevelButton extends TextButton {
	
	public LevelButton(Level level, Skin skin){
		super(level.getName(), skin);
	}

}
