package com.zosocoder.android.spotifystreamer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    public static final String ARTIST = "Led Zeppelin";
    private ArtistAdapter artistAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        Artist artist_data[] = new Artist[] {
                new Artist("Led Zeppelin", "https://i.scdn.co/image/89f85b03f128056ea2d0ca941be999853bea3d7c"),
                new Artist("Slayer", "https://i.scdn.co/image/43a01ad508ac0ce27d5a0da97e08cc857af0f43b"),
                new Artist("Pink Floyd", "https://i.scdn.co/image/792a417e8500405180a7746908afd7d5e3a30585")
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
                intent.putExtra(ARTIST, artist.name);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
