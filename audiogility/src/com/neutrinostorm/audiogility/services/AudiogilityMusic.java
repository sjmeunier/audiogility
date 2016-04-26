package com.neutrinostorm.audiogility.services;

import com.badlogic.gdx.audio.Music;

public enum AudiogilityMusic
{
    MENU( "music/menu.ogg" ),
    LEVEL( "music/level.ogg" );

    private String fileName;
    private Music musicResource;

    private AudiogilityMusic(String fileName )
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }

    public Music getMusicResource()
    {
        return musicResource;
    }

    public void setMusicResource( Music musicBeingPlayed )
    {
        this.musicResource = musicBeingPlayed;
    }
}