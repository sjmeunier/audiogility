package com.neutrinostorm.audiogility.services;

public enum AudiogilitySound
{
    CLICK( "sound/click.wav" );

    private final String fileName;

    private AudiogilitySound(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
}