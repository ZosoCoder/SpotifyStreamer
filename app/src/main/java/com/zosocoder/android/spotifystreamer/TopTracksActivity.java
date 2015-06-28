package com.zosocoder.android.spotifystreamer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class TopTracksActivity extends Activity {

    private static final String LOG_TAG = TopTracksActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_tracks);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_top_tracks, new TopTracksFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class TopTracksFragment extends Fragment {

        private ArtistTrackAdapter trackAdapter;

        public TopTracksFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);
            Intent intent =  getActivity().getIntent();
            AQuery aq = new AQuery(rootView);
//            ArtistTrack tracks[] = new ArtistTrack[] {
//                    new ArtistTrack("Stairway To Heaven", "Led Zeppelin IV", "https://i.scdn.co/image/5ef60ee8ba01f7042961420378733beb308e46f0"),
//                    new ArtistTrack("Whole Lotta Love", "Led Zeppelin II", "https://i.scdn.co/image/84bdbd976b0a3a6b9d423f69c3e23915aa380f78"),
//                    new ArtistTrack("Good Times Bad Times", "Led Zeppelin", "https://i.scdn.co/image/b52f80e6899f0f32bf303d60eeb1c70ece18be96"),
//                    new ArtistTrack("Ramble On", "Led Zeppelin II", "https://i.scdn.co/image/84bdbd976b0a3a6b9d423f69c3e23915aa380f78"),
//                    new ArtistTrack("Immigrant Song", "Led Zeppelin III", "https://i.scdn.co/image/6ba168517b3965ba6067706ec213fba4dd52c532")
//            };


            if (intent != null && intent.hasExtra(MainActivityFragment.ARTIST)) {
                Artist artist = intent.getParcelableExtra(MainActivityFragment.ARTIST);
                ImageView artistImage =
                        (ImageView) rootView.findViewById(R.id.iv_artist_image);

                getActivity().setTitle(artist.name);
                aq.id(artistImage).image(artist.imageUrl, true, true, 0, R.drawable.question);

                trackAdapter = new ArtistTrackAdapter(
                        getActivity(),
                        R.layout.list_item_track,
                        new ArrayList<>()
                );

                ListView listView = (ListView) rootView.findViewById(R.id.lv_top_tracks);
                listView.setAdapter(trackAdapter);

                FetchTrackTask task = new FetchTrackTask();
                task.execute(artist.id);
            }

            return rootView;
        }

        public class FetchTrackTask extends AsyncTask<String,Void,ArtistTrack[]> {

            @Override
            protected ArtistTrack[] doInBackground(String... params) {
                if (params.length == 0) return null;

                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;
                String trackJsonStr = null;

                try {
                    final String SPOTIFY_BASE_URL = "https://api.spotify.com/v1/artists/" +
                            params[0] + "/top-tracks?";
                    final String COUNTRY_PARAM = "country";
                    final String COUNTRY = "US";

                    Uri builtUri = Uri.parse(SPOTIFY_BASE_URL).buildUpon()
                            .appendQueryParameter(COUNTRY_PARAM, COUNTRY)
                            .build();

                    URL url = new URL(builtUri.toString());
                    Log.v(LOG_TAG, builtUri.toString());

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder builder = new StringBuilder();

                    if (inputStream == null) return null;
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) { builder.append(line).append('\n'); }
                    if (builder.length() == 0) return null;

                    trackJsonStr = builder.toString();
                    Log.v(LOG_TAG, "TrackJSON: " + trackJsonStr);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error: " + e);
                } finally {
                    if (urlConnection != null) urlConnection.disconnect();

                    if (reader != null) {
                        try {reader.close();}
                        catch (final IOException e) { Log.e(LOG_TAG, "Error closing stream", e); }
                    }
                }

                try { return getTracksFromJson(trackJsonStr); }
                catch (JSONException e) { Log.e("PARSING: ", e.getMessage(), e); }

                return null;
            }

            @Override
            protected void onPostExecute(ArtistTrack[] artistTracks) {
                if (artistTracks != null) {
                    if (artistTracks.length == 0)
                        Toast.makeText(getActivity(), "No Top-Tracks Available", Toast.LENGTH_SHORT)
                                .show();

                    trackAdapter.clear();
                    trackAdapter.addAll(artistTracks);
                }
            }

            private ArtistTrack[] getTracksFromJson(String trackJsonStr) throws JSONException {
                final String TRACK_ARRAY = "tracks";
                final String ITEMS = "items";
                final String ALBUM = "album";
                final String IMAGES = "images";
                final String OBJ_NAME = "name";
                final String ALBUM_URL = "url";

                JSONObject tracksJsonObj = new JSONObject(trackJsonStr);
                JSONArray tracksArray = tracksJsonObj.getJSONArray(TRACK_ARRAY);

                if (tracksArray.length() == 0) return new ArtistTrack[0];

                ArtistTrack[] tracks = new ArtistTrack[tracksArray.length()];

                for (int i = 0; i < tracksArray.length(); i++) {
                    String trackName, albumName, albumImgUrl;

                    JSONObject trackObj = tracksArray.getJSONObject(i);
                    JSONObject albumObj = trackObj.getJSONObject(ALBUM);
                    JSONArray albumImgs = albumObj.getJSONArray(IMAGES);

                    trackName = trackObj.getString(OBJ_NAME);
                    albumName = albumObj.getString(OBJ_NAME);

                    if (albumImgs.length() == 0) albumImgUrl = "";
                    else albumImgUrl = albumImgs.getJSONObject(0).getString(ALBUM_URL);

                    Log.v(LOG_TAG, "Track: " + trackName + "\n" +
                            "Album: " + albumName + "\n" +
                            "Url: " + albumImgUrl);
                    tracks[i] = new ArtistTrack(trackName, albumName, albumImgUrl);
                }

                return tracks;
            }
        }
    }
}
