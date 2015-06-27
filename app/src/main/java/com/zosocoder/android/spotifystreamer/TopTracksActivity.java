package com.zosocoder.android.spotifystreamer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;


public class TopTracksActivity extends Activity {

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
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);
            Intent intent =  getActivity().getIntent();
            AQuery aq = new AQuery(rootView);
            ArtistTrack tracks[] = new ArtistTrack[] {
                    new ArtistTrack("Stairway To Heaven", "Led Zeppelin IV", "https://i.scdn.co/image/5ef60ee8ba01f7042961420378733beb308e46f0"),
                    new ArtistTrack("Whole Lotta Love", "Led Zeppelin II", "https://i.scdn.co/image/84bdbd976b0a3a6b9d423f69c3e23915aa380f78"),
                    new ArtistTrack("Good Times Bad Times", "Led Zeppelin", "https://i.scdn.co/image/b52f80e6899f0f32bf303d60eeb1c70ece18be96"),
                    new ArtistTrack("Ramble On", "Led Zeppelin II", "https://i.scdn.co/image/84bdbd976b0a3a6b9d423f69c3e23915aa380f78"),
                    new ArtistTrack("Immigrant Song", "Led Zeppelin III", "https://i.scdn.co/image/6ba168517b3965ba6067706ec213fba4dd52c532")
            };


            if (intent != null && intent.hasExtra(MainActivityFragment.ARTIST)) {
                Artist artist = intent.getParcelableExtra(MainActivityFragment.ARTIST);
                ImageView artistImage =
                        (ImageView) rootView.findViewById(R.id.iv_artist_image);

                getActivity().setTitle(artist.name);
//              ((TextView) rootView.findViewById(R.id.tv_artist_name)).setText(artist.name);
                aq.id(artistImage).image(artist.imageUrl, true, true, 0, R.drawable.question);

                trackAdapter = new ArtistTrackAdapter(
                        getActivity(),
                        R.layout.list_item_track,
                        tracks);

                ListView listView = (ListView) rootView.findViewById(R.id.lv_top_tracks);
                listView.setAdapter(trackAdapter);
            }

            return rootView;
        }
    }
}
