package com.zosocoder.android.spotifystreamer;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.AbstractList;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public static final String ARTIST = "artist";
    private ArtistAdapter artistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        artistAdapter = new ArtistAdapter(
                getActivity(),
                R.layout.list_item_artist,
                new ArrayList<Artist>());

        ListView listView = (ListView) rootView.findViewById(R.id.lvArtistsResult);
        listView.setAdapter(artistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = artistAdapter.getItem(position);
//              Toast.makeText(getActivity(), artist.name + " selected", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(), TopTracksActivity.class);
                intent.putExtra(ARTIST, artist);
                startActivity(intent);
            }
        });

        final EditText searchText = (EditText) rootView.findViewById(R.id.etSearchArtist);

        final Button searchBtn = (Button) rootView.findViewById(R.id.btnSearch);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchArtistTask task = new FetchArtistTask();
                task.execute(searchText.getText().toString());
            }
        });

        return rootView;
    }

    public class FetchArtistTask extends AsyncTask<String,Void,Artist[]> {

        private Artist[] getArtistsFromJson(String artistJsonStr) throws JSONException{
            final String ARTISTS_OBJ = "artists";
            final String ITEMS = "items";
            final String ARTIST_NAME = "name";
            final String ARTIST_ID = "id";
            final String IMAGES = "images";
            final String URL = "url";

            JSONObject artistJson = new JSONObject(artistJsonStr);
            JSONArray artistArray = artistJson.getJSONObject(ARTISTS_OBJ).getJSONArray(ITEMS);

            Artist[] artists = new Artist[artistArray.length()];

            for (int i = 0; i < artistArray.length(); i++) {
                String name, url, id;

                JSONObject artistObj = artistArray.getJSONObject(i);

                name = artistObj.getString(ARTIST_NAME);
                id = artistObj.getString(ARTIST_ID);
                JSONArray imagesArray = artistObj.getJSONArray(IMAGES);

                if (imagesArray.length() > 0) {
                    JSONObject imageObj = imagesArray.getJSONObject(0);
                    url = imageObj.getString(URL);
                } else {
                    url = "";
                }

                artists[i] = new Artist(name,url,id);
            }

            return artists;
        }

        @Override
        protected Artist[] doInBackground(String... params) {
            if (params.length == 0) return null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String type = "artist";
            String artistJsonStr = null;

            try {
                final String SPOTIFY_BASE_URL = "https://api.spotify.com/v1/search?";
                final String QUERY_PARAM = "q";
                final String TYPE_PARAM = "type";

                Uri builtUri = Uri.parse(SPOTIFY_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(TYPE_PARAM, type)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder builder = new StringBuilder();

                if (inputStream == null) return null;
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {builder.append(line).append('\n'); }
                if (builder.length() == 0) return null;

                artistJsonStr = builder.toString();
//                Log.v(LOG_TAG, "JSON: " + artistJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error: ", e);
            } finally {
                if (urlConnection != null) urlConnection.disconnect();

                if (reader != null) {
                    try {reader.close();}
                    catch (final IOException e) { Log.e(LOG_TAG, "Error closing stream", e); }
                }
            }

            try { return getArtistsFromJson(artistJsonStr); }
            catch (JSONException e) { Log.e(LOG_TAG, e.getMessage(), e); }

            return null;
        }

        @Override
        protected void onPostExecute(Artist[] artists) {
            if (artists != null) {
                if (artists.length == 0)
                    Toast.makeText(
                            getActivity(),
                            "No Results - Please refine your search.",
                            Toast.LENGTH_SHORT)
                            .show();

                artistAdapter.clear();
                artistAdapter.addAll(artists);
            }
        }
    }
}
