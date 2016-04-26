package com.neutrinostorm.audiogility.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
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

public class MenuScreen extends AbstractScreen
{
    public MenuScreen(Audiogility game)
    {
        super(game);
    }

    private Image backgroundImage;
    private Image logoImage;
    
    @Override
    public void show()
    {
        super.show();
        game.getMusicManager().play( AudiogilityMusic.MENU );
        
        stage.addActor( game.getMainBackground() );

        //Set up logo
        AtlasRegion logoRegion = game.getAtlas().findRegion( "main-screen/audiogilitylogo" );
        Drawable logoDrawable = new TextureRegionDrawable( logoRegion );

        logoImage = new Image( logoDrawable, Scaling.none );
        logoImage.setPosition(10,  180);
        stage.addActor(logoImage);
        
        // retrieve the default table actor
        Table table = super.getTable();
        table.right();

        table.padRight(20);
        // register the button "start game"

        TextButton startGameButton = new TextButton( "Start game", game.getSkin() );
        startGameButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y )
            {
            	game.getSoundManager().play( AudiogilitySound.CLICK );
                game.setScreen( new LevelScreen(game) );
            }
        } );
        table.add( startGameButton ).size( 250, 60 ).uniform().spaceBottom( 10 );
        table.row();
        
        TextButton helpButton = new TextButton( "Help", game.getSkin() );
        helpButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y )
            {
            	game.getSoundManager().play( AudiogilitySound.CLICK );
                game.setScreen( new HelpScreen(game) );
            }
        } );
        table.add( helpButton ).uniform().fill().spaceBottom( 10 );
        table.row();

        
        TextButton optionsButton = new TextButton( "Options", game.getSkin() );
        optionsButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y )
            {
            	game.getSoundManager().play( AudiogilitySound.CLICK );
                game.setScreen( new OptionsScreen(game) );
            }
        } );
        table.add( optionsButton ).uniform().fill().spaceBottom( 10 );
        table.row();

        TextButton creditsButton = new TextButton( "Credits", game.getSkin() );
        creditsButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y )
            {
            	game.getSoundManager().play( AudiogilitySound.CLICK );
                game.setScreen( new CreditsScreen(game) );
            }
        } );
        table.add( creditsButton ).uniform().fill().spaceBottom( 10 );
        table.row();

        TextButton exitButton = new TextButton( "Quit", game.getSkin() );
        exitButton.addListener( new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y )
            {
            	game.getSoundManager().play( AudiogilitySound.CLICK );
            	Gdx.app.exit();
            }
        } );
        table.add( exitButton ).uniform().fill();
        table.row();

      }
}
