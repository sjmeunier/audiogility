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
 
public class LevelCompleteScreen extends AbstractScreen
{

    private Profile profile;
    private long timeElapsed;
    private int levelId;
    private String levelName;
    private int medal;
    
    public LevelCompleteScreen(Audiogility game, int levelId, String levelName, long timeElapsed, int medal)
    {
        super(game);
        profile = game.getProfileManager().retrieveProfile();
        profile.notifyScore(levelId, timeElapsed);
        game.getProfileManager().persist();
        this.levelId = levelId;
        this.levelName = levelName;
        this.timeElapsed = timeElapsed;
        this.medal = medal;
    }
 
    @Override
    public void show()
    {
        super.show();
        
        stage.addActor( game.getMainBackground() );

        
        Table table = super.getTable();
        table.add("Congratulations").colspan(2).spaceBottom(30);
        table.row();
        
        table.add( "You completed " + levelName + " in " + String.valueOf(timeElapsed) + " seconds.").colspan(2).spaceBottom(20);
        table.row();
        
        String medalStr = "bronze";
        if (medal == 1)
        	medalStr = "gold";
        else if (medal == 2)
        	medalStr = "silver";
        
        table.add( "You earned a " + medalStr + " medal.").colspan(2).spaceBottom(30);
        table.row();

        TextButton levelsButton = new TextButton( "Levels", game.getSkin() );
        levelsButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y )
            {
            	game.getSoundManager().play( AudiogilitySound.CLICK );
            	game.setScreen( new LevelScreen(game) );
            }
        } );
        table.add( levelsButton ).size(250, 60).spaceRight(20);
            
        TextButton nextButton = new TextButton( "Next Level", game.getSkin() );
        nextButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y )
            {
            	game.getSoundManager().play( AudiogilitySound.CLICK );
            	game.setScreen( new GameScreen(game, levelId + 1) );
            }
        } );
        table.add( nextButton ).size(250, 60);
        table.row();
    }
    
}
