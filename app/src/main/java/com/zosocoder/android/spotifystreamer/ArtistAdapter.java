package com.zosocoder.android.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Tony on 6/23/2015.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {

    Context context;
    int resource;
    Artist data[] = null;

    public ArtistAdapter(Context context, int resource, Artist[] data) {
        super(context, resource, data);

        this.context = context;
        this.resource = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ArtistHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new ArtistHolder();
            holder.artistName = (TextView) row.findViewById(R.id.tv_artist);
            holder.artistThumb = (ImageView) row.findViewById(R.id.iv_artist_thumb);

            row.setTag(holder);
        } else { holder = (ArtistHolder) row.getTag(); }

        Artist artist = data[position];
        FetchArtistThumbTask artistThumbTask = new FetchArtistThumbTask(holder.artistThumb);

        holder.artistName.setText(artist.name);
        artistThumbTask.execute(artist.imageUrl);

        return row;
    }

    public class FetchArtistThumbTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImg;

        public FetchArtistThumbTask(ImageView bmImg) { this.bmImg = bmImg; }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap artistImg = null;

            try {
                InputStream is = new URL(url).openStream();
                artistImg = BitmapFactory.decodeStream(is);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return artistImg;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) { bmImg.setImageBitmap(bitmap); }
    }

    static class ArtistHolder {
        ImageView artistThumb;
        TextView artistName;
    }
}
