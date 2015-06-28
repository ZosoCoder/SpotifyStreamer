package com.zosocoder.android.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

public class ArtistTrackAdapter extends ArrayAdapter<ArtistTrack> {

    Context context;
    int resource;
    ArrayList<ArtistTrack> data = null;

    public ArtistTrackAdapter(Context context, int resource, ArrayList<ArtistTrack> data) {
        super(context, resource, data);

        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArtistTrackHolder holder;
        AQuery aq;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);

            holder = new ArtistTrackHolder();
            holder.albumImage = (ImageView) convertView.findViewById(R.id.iv_item_thumb);
            holder.trackTitle= (TextView) convertView.findViewById(R.id.tv_track_name);
            holder.trackAlbum = (TextView) convertView.findViewById(R.id.tv_track_album);

            convertView.setTag(holder);
        } else { holder = (ArtistTrackHolder) convertView.getTag();}

        ArtistTrack artistTrack = data.get(position);
        aq = new AQuery(convertView);

        holder.trackTitle.setText(artistTrack.trackName);
        holder.trackAlbum.setText(artistTrack.albumName);
        Log.v("TRACKS", "Setting track: " + artistTrack.trackName);
        aq.id(holder.albumImage).image(artistTrack.albumImgUrl, true, true, 0, R.drawable.question);

        return convertView;
    }

    static class ArtistTrackHolder {
        ImageView albumImage;
        TextView trackTitle;
        TextView trackAlbum;
    }
}
