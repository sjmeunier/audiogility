package com.neutrinostorm.audiogility.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.neutrinostorm.audiogility.Audiogility;
import com.neutrinostorm.audiogility.utils.LRUCache;
import com.neutrinostorm.audiogility.utils.LRUCache.CacheEntryRemovedListener;



/**
 * A service that manages the sound effects.
 */
public class SoundManager implements CacheEntryRemovedListener<AudiogilitySound,Sound>, Disposable
{



    private float volume = 1f;
    private boolean enabled = true;
    private final LRUCache<AudiogilitySound,Sound> soundCache;

    public SoundManager()
    {
        soundCache = new LRUCache<AudiogilitySound,Sound>( 10 );
        soundCache.setEntryRemovedListener( this );
    }

    public void play(AudiogilitySound sound)
    {
        // check if the sound is enabled
        if( ! enabled ) return;

        // try and get the sound from the cache
        Sound soundToPlay = soundCache.get( sound );
        if( soundToPlay == null ) {
            FileHandle soundFile = Gdx.files.internal( sound.getFileName() );
            soundToPlay = Gdx.audio.newSound( soundFile );
            soundCache.add( sound, soundToPlay );
        }

        // play the sound
        Gdx.app.log( Audiogility.LOG, "Playing sound: " + sound.name() );
        soundToPlay.play( volume );
    }

    /**
     * Sets the sound volume which must be inside the range [0,1].
     */
    public void setVolume(float volume)
    {
        Gdx.app.log( Audiogility.LOG, "Adjusting sound volume to: " + volume );

        // check and set the new volume
        if( volume < 0 || volume > 1f ) {
            throw new IllegalArgumentException( "The volume must be inside the range: [0,1]" );
        }
        this.volume = volume;
    }

    /**
     * Enables or disabled the sound.
     */
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
    	return this.enabled;
    }
    // EntryRemovedListener implementation

    @Override
    public void notifyEntryRemoved(AudiogilitySound key, Sound value)
    {
        Gdx.app.log( Audiogility.LOG, "Disposing sound: " + key.name() );
        value.dispose();
    }

    /**
     * Disposes the sound manager.
     */
    public void dispose()
    {
        Gdx.app.log( Audiogility.LOG, "Disposing sound manager" );
        for( Sound sound : soundCache.retrieveAll() ) {
            sound.stop();
            sound.dispose();
        }
    }
}

