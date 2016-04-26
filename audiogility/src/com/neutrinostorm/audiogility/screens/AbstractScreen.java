package com.neutrinostorm.audiogility.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.neutrinostorm.audiogility.Audiogility;


public abstract class AbstractScreen implements Screen
{
    // the fixed viewport dimensions (ratio: 1.6)
    public static final int VIEWPORT_WIDTH = 800, VIEWPORT_HEIGHT = 480;

    protected final Audiogility game;
    protected final Stage stage;

    private BitmapFont font;
    private SpriteBatch batch;

    private Table table;
    public OrthographicCamera camera;

    public AbstractScreen(Audiogility game)
    {
        this.game = game;

        
        camera = new OrthographicCamera();
        
        this.camera.setToOrtho(false, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        this.camera.position.set(VIEWPORT_WIDTH / 2, VIEWPORT_HEIGHT / 2, 0);
        this.camera.update();
        
        this.stage = new Stage( VIEWPORT_WIDTH, VIEWPORT_HEIGHT, true );
        this.stage.setViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, false);
        this.stage.setCamera(camera);
    }

    protected String getName()
    {
        return getClass().getSimpleName();
    }

    protected boolean isGameScreen()
    {
        return false;
    }

    // Lazily loaded collaborators

    public BitmapFont getFont()
    {
        if( font == null ) {
            font = new BitmapFont();
        }
        return font;
    }

    public SpriteBatch getBatch()
    {
        if( batch == null ) {
            batch = new SpriteBatch();
        }
        return batch;
    }


    protected Table getTable()
    {
        if( table == null ) {
            table = new Table( game.getSkin() );
            table.setFillParent( true );
            //if( Tyrian.DEV_MODE ) {
            //    table.debug();
           // }
            stage.addActor( table );
        }
        return table;
    }

    // Screen implementation

    @Override
    public void show()
    {
        Gdx.app.log( Audiogility.LOG, "Showing screen: " + getName() );

        // set the stage as the input processor
        Gdx.input.setInputProcessor( stage );
    }

    @Override
    public void resize(int width, int height)
    {
        Gdx.app.log( Audiogility.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height );
    }

    @Override
    public void render(float delta)
    {
        // (1) process the game logic

        // update the actors
        stage.act( delta );

        // (2) draw the result

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

        // draw the actors
        stage.draw();

        // draw the table debug lines
        Table.drawDebug( stage );
    }

    @Override
    public void hide()
    {
        Gdx.app.log( Audiogility.LOG, "Hiding screen: " + getName() );

        // dispose the screen when leaving the screen;
        // note that the dipose() method is not called automatically by the
        // framework, so we must figure out when it's appropriate to call it
        dispose();
    }

    @Override
    public void pause()
    {
        Gdx.app.log( Audiogility.LOG, "Pausing screen: " + getName() );
    }

    @Override
    public void resume()
    {
        Gdx.app.log( Audiogility.LOG, "Resuming screen: " + getName() );
    }

    @Override
    public void dispose()
    {
        Gdx.app.log( Audiogility.LOG, "Disposing screen: " + getName() );

        // the following call disposes the screen's stage, but on my computer it
        // crashes the game so I commented it out; more info can be found at:
        // http://www.badlogicgames.com/forum/viewtopic.php?f=11&t=3624
        // stage.dispose();

        // as the collaborators are lazily loaded, they may be null
       
        if( font != null ) font.dispose();
        if( batch != null ) batch.dispose();

    }

}