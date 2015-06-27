package com.zosocoder.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

public class ArtistTrack implements Parcelable{
    public String trackName;
    public String albumName;
    public String albumImgUrl;

    public ArtistTrack() { super(); }

    public ArtistTrack(String trackName, String albumName, String albumImgUrl) {
        super();

        this.trackName = trackName;
        this.albumName = albumName;
        this.albumImgUrl = albumImgUrl;
    }

    public ArtistTrack(Parcel in) {
        trackName = in.readString();
        albumName = in.readString();
        albumImgUrl = in.readString();
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trackName);
        dest.writeString(albumName);
        dest.writeString(albumImgUrl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) { return new ArtistTrack(source); }

        @Override
        public Object[] newArray(int size) { return new ArtistTrack[size]; }
    };
}
