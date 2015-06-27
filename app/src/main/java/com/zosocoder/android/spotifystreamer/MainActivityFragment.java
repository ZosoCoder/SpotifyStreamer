package com.zosocoder.android.spotifystreamer;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


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
        Artist artist_data[] = new Artist[] {
              new Artist("Led Zeppelin", "https://i.scdn.co/image/bc9b5c84f5806bdf34879f5c9a0628eaadb348c8"),
              new Artist("Slayer", "https://i.scdn.co/image/44fcee9becb9e2a4e7af2722481dfeec2b3fed1e"),
              new Artist("Pink Floyd", "https://i.scdn.co/image/b954149fed21dcbafe1cee4c30454eb934c384ee")
        };

        artistAdapter = new ArtistAdapter(
                getActivity(),
                R.layout.list_item_artist,
                artist_data);

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

        return rootView;
    }

    public class FetchArtistTask extends AsyncTask<String,Void,Artist[]> {

        @Override
        protected Artist[] doInBackground(String... params) {
            return null;
        }
    }
}
