package com.zosocoder.android.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
        ArtistHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);

            holder = new ArtistHolder();
            holder.artistName = (TextView) convertView.findViewById(R.id.tv_artist);
            holder.artistThumb = (ImageView) convertView.findViewById(R.id.iv_artist_thumb);

            convertView.setTag(holder);
        } else { holder = (ArtistHolder) convertView.getTag(); }

        Artist artist = data[position];
        FetchArtistThumbTask artistThumbTask = new FetchArtistThumbTask(holder.artistThumb);

        holder.artistName.setText(artist.name);
        artistThumbTask.execute(artist.imageUrl);

        return convertView;
    }

    public class FetchArtistThumbTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImg;
        private final String LOG_TAG = FetchArtistThumbTask.class.getSimpleName();

        public FetchArtistThumbTask(ImageView bmImg) { this.bmImg = bmImg; }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap artistImg = null;
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                artistImg = BitmapFactory.decodeStream(is);
                Log.v(LOG_TAG, "Setting Bitmap: " + params[0]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                artistImg = BitmapFactory.decodeResource(
                        context.getResources(), R.drawable.question);

            } catch (Exception e) {
                Log.v(LOG_TAG, "NETWORK ERROR");
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
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
