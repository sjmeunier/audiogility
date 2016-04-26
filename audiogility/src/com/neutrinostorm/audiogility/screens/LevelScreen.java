package com.neutrinostorm.audiogility.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.neutrinostorm.audiogility.Audiogility;
import com.neutrinostorm.audiogility.services.AudiogilityMusic;
import com.neutrinostorm.audiogility.services.AudiogilitySound;
import com.neutrinostorm.audiogility.utils.Level;
import com.neutrinostorm.audiogility.utils.LevelLoader;
import com.neutrinostorm.audiogility.utils.Profile;
 
public class LevelScreen extends AbstractScreen
{

    private Image backgroundImage;
    private Level[] levels;
    private Profile profile;
    private int sectionId = 1;
    private int highestLevel = 0;
    public LevelScreen(Audiogility game)
    {
        super(game);
    }
 
    @Override
    public void show()
    {
        super.show();
        game.getMusicManager().play( AudiogilityMusic.MENU );
        profile = game.getProfileManager().retrieveProfile();
        highestLevel = profile.getHighestLevelId();
        if (highestLevel <= 20)
        	sectionId = 1;
        else if (highestLevel <= 40)
        	sectionId = 2;
        else if (highestLevel <= 60)
        	sectionId = 3;
        else 
        	sectionId = 4;
        
        stage.addActor( game.getMainBackground() );

        
        Table table = super.getTable();
        
        levels = LevelLoader.LoadSection(sectionId);
        TextButton[] levelButtons = new TextButton[levels.length];
        
        boolean addedRow = false;
        for(int i = 0; i < levels.length; i++){
        	final int id = levels[i].getId();
        	
        	if (id > (highestLevel + 1)){
       			levelButtons[i] = new TextButton(levels[i].getShortName(), game.getSkin(), "locked");
       			levelButtons[i].setDisabled(true);
       	
        	} else if (id == (highestLevel + 1)) {
        		levelButtons[i] = new TextButton(levels[i].getShortName(), game.getSkin(), "enabled");
        	}else {
        		int bestTime = profile.getBestTime(id);
        		if (bestTime <= levels[i].getGoldTime())
        			levelButtons[i] = new TextButton(levels[i].getShortName(), game.getSkin(), "gold");
        		else if (bestTime <= levels[i].getSilverTime())
        			levelButtons[i] = new TextButton(levels[i].getShortName(), game.getSkin(), "silver");
        		else
        			levelButtons[i] = new TextButton(levels[i].getShortName(), game.getSkin(), "bronze");
        	}
        	levelButtons[i].addListener( new ClickListener() {
                @Override
                public void clicked(InputEvent event,float x,float y )
                {
                	game.getSoundManager().play( AudiogilitySound.CLICK );
                	game.setScreen( new GameScreen(game, id) );
                }
            } );
            table.add( levelButtons[i] ).size(60, 60).spaceRight(20).spaceBottom(20).padTop(3);
            
            if ((((i + 1) % 5) == 0) && (i > 0)) {
            	table.row();
            	addedRow = true;
            } else {
            	addedRow = false;
            }
        	
        }

        if (!addedRow)
        	table.row();
        
        TextButton exitButton = new TextButton( "Back", game.getSkin() );
        exitButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y )
            {
            	game.getSoundManager().play( AudiogilitySound.CLICK );
            	game.setScreen( new MenuScreen(game) );
            }
        } );
        table.add( exitButton ).size(250, 60).colspan(5);
        table.row();
    }
    
}
