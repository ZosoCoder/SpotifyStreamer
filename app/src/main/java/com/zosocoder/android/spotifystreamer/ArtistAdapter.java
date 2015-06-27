package com.zosocoder.android.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;

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
        AQuery aq;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(resource, parent, false);

            holder = new ArtistHolder();
            holder.artistName = (TextView) convertView.findViewById(R.id.tv_item_text);
            holder.artistThumb = (ImageView) convertView.findViewById(R.id.iv_item_thumb);

            convertView.setTag(holder);
        } else { holder = (ArtistHolder) convertView.getTag(); }

        Artist artist = data[position];
        aq = new AQuery(convertView);

        holder.artistName.setText(artist.name);
        aq.id(holder.artistThumb).image(artist.imageUrl, true, true, 0, R.drawable.question);

        return convertView;
    }

    static class ArtistHolder {
        ImageView artistThumb;
        TextView artistName;
    }
}
