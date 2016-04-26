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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.neutrinostorm.audiogility.Audiogility;
import com.neutrinostorm.audiogility.services.AudiogilityMusic;
import com.neutrinostorm.audiogility.services.AudiogilitySound;

public class CreditsScreen extends AbstractScreen
{
    public CreditsScreen(Audiogility game)
    {
        super(game);
    }

    
    
    @Override
    public void show()
    {
        super.show();
        game.getMusicManager().play( AudiogilityMusic.MENU );
        
        stage.addActor( game.getMainBackground() );
        

        // retrieve the default table actor
        Table table = super.getTable();
        table.center();

        Label creditHeadingLabel = new Label("Credits", game.getSkin());
        
        table.add( creditHeadingLabel ).spaceBottom( 30 ).colspan(2);
        table.row();

        int HORSPACING = 30;
        int VERTICALSPACING = 18;
        
        table.add(new Label("Game Concept", game.getSkin()) ).spaceBottom( VERTICALSPACING ).spaceRight(HORSPACING).left();
        table.add(new Label("Serge Meunier", game.getSkin()) ).spaceBottom( VERTICALSPACING ).left();
        table.row();
        
        table.add(new Label("Programming", game.getSkin()) ).spaceBottom( VERTICALSPACING ).spaceRight(HORSPACING).left();
        table.add(new Label("Serge Meunier", game.getSkin()) ).spaceBottom( VERTICALSPACING ).left();
        table.row();
        
        table.add(new Label("Graphics", game.getSkin()) ).spaceBottom( VERTICALSPACING ).spaceRight(HORSPACING).left();
        table.add(new Label("Serge Meunier", game.getSkin()) ).spaceBottom( VERTICALSPACING ).left();
        table.row();
        
        table.add(new Label("Level Design", game.getSkin()) ).spaceBottom( 2 ).spaceRight(HORSPACING).left();
        table.add(new Label("Serge Meunier", game.getSkin()) ).spaceBottom( 2 ).left();
        table.row();
        
        table.add(new Label("", game.getSkin()) ).spaceBottom( VERTICALSPACING ).spaceRight(HORSPACING).left();
        table.add(new Label("Steven Meunier", game.getSkin()) ).spaceBottom( VERTICALSPACING ).left();
        table.row();
        
        table.add(new Label("Music", game.getSkin()) ).spaceBottom( VERTICALSPACING ).spaceRight(HORSPACING).left();
        table.add(new Label("FoxSynergy", game.getSkin()) ).spaceBottom( VERTICALSPACING ).left();
        table.row();
        
        table.add(new Label("Testing", game.getSkin()) ).spaceBottom( 30 ).spaceRight(HORSPACING).left();
        table.add(new Label("Steven Meunier", game.getSkin()) ).spaceBottom(30).left();
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
        table.add( exitButton ).size(250, 60).colspan(2);
        table.row();

      }
}
