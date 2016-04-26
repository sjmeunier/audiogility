package com.neutrinostorm.audiogility;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.neutrinostorm.audiogility.screens.SplashScreen;
import com.neutrinostorm.audiogility.services.MusicManager;
import com.neutrinostorm.audiogility.services.PreferencesManager;
import com.neutrinostorm.audiogility.services.ProfileManager;
import com.neutrinostorm.audiogility.services.SoundManager;

public class Audiogility extends Game {
	public static final String LOG = Audiogility.class.getSimpleName();
	private FPSLogger fpsLogger;
    private MusicManager musicManager;
    private SoundManager soundManager;
    private PreferencesManager preferencesManager;
    private ProfileManager profileManager;


    private Skin skin;
    private TextureAtlas atlas;
    private Image mainBackgroundImage;
    
    public Image getMainBackground()
    {
    	if (mainBackgroundImage == null){
            AtlasRegion backgroundRegion = getAtlas().findRegion( "main-screen/background" );
            Drawable backgroundDrawable = new TextureRegionDrawable( backgroundRegion );
            mainBackgroundImage = new Image( backgroundDrawable, Scaling.stretch );
            mainBackgroundImage.setFillParent( true );
            mainBackgroundImage.getColor().a = 0f;
            mainBackgroundImage.addAction( sequence( fadeIn( 0.25f )) );
        }
        return mainBackgroundImage;
    }
    
    public TextureAtlas getAtlas()
    {
        if( atlas == null ) {
            atlas = new TextureAtlas( Gdx.files.internal( "image-atlases/pages.atlas" ) );
        	//atlas = game.getAssetManager().get("image-atlases/pages.atlas", TextureAtlas.class);
        }
        return atlas;
    }

    public Skin getSkin()
    {
        if( skin == null ) {
            FileHandle skinFile = Gdx.files.internal( "skin/uiskin.json" );
            skin = new Skin( skinFile );
           // skin = game.getAssetManager().get("skin/uiskin.json", Skin.class);
        }
        return skin;
    }
    
    public ProfileManager getProfileManager()
    {
        return profileManager;
    }
    public MusicManager getMusicManager()
    {
        return musicManager;
    }

    public SoundManager getSoundManager()
    {
        return soundManager;
    }

    public PreferencesManager getPreferencesManager()
    {
        return preferencesManager;
    }

    
	@Override
	public void create() {	
		Gdx.app.log( Audiogility.LOG, "Creating game" );
		
		fpsLogger = new FPSLogger();
		preferencesManager = new PreferencesManager();

        // create the music manager
        musicManager = new MusicManager();
        musicManager.setVolume( preferencesManager.getVolume() );
        musicManager.setEnabled( preferencesManager.isMusicEnabled() );


        // create the sound manager
        soundManager = new SoundManager();
        soundManager.setVolume( preferencesManager.getVolume() );
        soundManager.setEnabled( preferencesManager.isSoundEnabled() );

        profileManager = new ProfileManager();
        profileManager.retrieveProfile();


		setScreen( getSplashScreen() );
	}

	@Override
	public void dispose() {

        if( skin != null ) skin.dispose();
        if( atlas != null ) atlas.dispose();
        
        // dipose some services
        musicManager.dispose();
        soundManager.dispose();

	}

	@Override
	public void render() {		
		super.render();
		
		fpsLogger.log();
	}

    @Override
    public void setScreen(Screen screen)
    {
        super.setScreen( screen );
        Gdx.app.log( Audiogility.LOG, "Setting screen: " + screen.getClass().getSimpleName() );
    }

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		super.pause();
		Gdx.app.log(Audiogility.LOG, "Pausing game" );

		profileManager.persist();

	}

	@Override
	public void resume() {
		super.resume();

	}
	
	public SplashScreen getSplashScreen()
	{
	    return new SplashScreen( this );
	}

}
