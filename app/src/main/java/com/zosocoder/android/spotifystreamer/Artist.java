package com.zosocoder.android.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

public class Artist implements Parcelable{
    public String name;
    public String imageUrl;

    public Artist() { super(); }

    public Artist(Parcel in) {
        this.name = in.readString();
        this.imageUrl = in.readString();
    }

    public Artist(String name, String imageUrl) {
        super();
        this.name = name;
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) { return new Artist(source); }

        @Override
        public Object[] newArray(int size) { return new Artist[size]; }
    };
}
