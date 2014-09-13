package com.paul.partyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SongListAdapter extends ArrayAdapter<Song> {

    Context context;
    int layoutResourceId;
    ArrayList<Song> songs;

    public SongListAdapter(Context context, int layoutResourceId, ArrayList<Song> songs) {

        super(context, layoutResourceId, songs);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.songs = songs;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        // If the view is null we need to render it
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.song_list_row, null);
        }

        // Get the current song to be added to the ListView
        Song song = songs.get(position);

        if (song != null) {

            TextView songArtist = (TextView) v.findViewById(R.id.song_list_artist);
            TextView songTitle = (TextView) v.findViewById(R.id.song_list_title);
            TextView songDuration = (TextView) v.findViewById(R.id.song_list_duration);
            TextView songAlbumTitle = (TextView) v.findViewById(R.id.song_list_album);

            songArtist.setText(song.getArtist());
            songTitle.setText(song.getTitle());
            songDuration.setText(new SimpleDateFormat("mm:ss").format(new Date(Long.parseLong(song.getDuration()))));
            songAlbumTitle.setText(song.getAlbumTitle());

        }

        return v;

    }

}
