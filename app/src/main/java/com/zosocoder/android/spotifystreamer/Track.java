package com.zosocoder.android.spotifystreamer;

/**
 * Created by Tony on 6/24/2015.
 */
public class Track {
    public String title;
    public String album;
    public String albumArtUrl;

    public Track() {}

    public Track(String title, String album, String albumArtUrl) {
        this.title = title;
        this.album = album;
        this.albumArtUrl = albumArtUrl;
    }
}
