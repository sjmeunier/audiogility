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

public class HelpScreen extends AbstractScreen
{
    public HelpScreen(Audiogility game)
    {
        super(game);
    }

    private Image backgroundImage;
    
    @Override
    public void show()
    {
        super.show();
        game.getMusicManager().play( AudiogilityMusic.MENU );
        
        stage.addActor( game.getMainBackground() );
       

        // retrieve the default table actor
        Table table = super.getTable();
        table.center();

        Label creditHeadingLabel = new Label("Help", game.getSkin());
        
        table.add( creditHeadingLabel ).spaceBottom( 20 );
        table.row();

        int VERTICALSPACING = 30;
        
        table.add(new Label("The objective is to merge the input waves\r\non the left so that the resulting wave on\r\nthe right matches the target wave as\r\nquickly as possible.", game.getSkin()) ).spaceBottom( VERTICALSPACING ).width(760).left();
        table.row();
        
        table.add(new Label("The frequency and amplitude of the input\r\nwaves can be changed by adjusting the set\r\nof sliders matching the colour of the wave.", game.getSkin()) ).width(760).spaceBottom( 50 ).left();
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
        table.add( exitButton ).size(250, 60);
        table.row();

      }
}
