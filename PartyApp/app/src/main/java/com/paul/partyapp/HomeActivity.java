package com.paul.partyapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class HomeActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String SONG_INFORMATION = "com.paul.partyapp.songInformation";

    private static final String TAG = "HomeActivity";
    private static final int LOADER_ID = 0;
    private static final boolean TEST_MODE = false;

    private SongListAdapter songListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return (id == R.id.action_settings) || super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        Uri mediaUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        final String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TRACK
        };

        final String selection = MediaStore.Audio.Media.IS_MUSIC + ">0";
        final String sortOrder = MediaStore.Audio.Media.ARTIST + "," + MediaStore.Audio.Media.ALBUM + "," + MediaStore.Audio.Media.TRACK;

        /*
         * If the Loader ID is valid, generate a new CursorLoader that gets all of the available
         * audio files.
         */
         return new CursorLoader(this, mediaUri, projection, selection, null, sortOrder);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor songListCursor) {

        ListView songList = (ListView) findViewById(R.id.song_list);

        songListAdapter = new SongListAdapter(
                this,
                R.layout.song_list_row,
                getSongsFromCursor(songListCursor)
        );

        songList.setAdapter(songListAdapter);

        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Song song = songListAdapter.getItem(position);

                Log.i(TAG, "Song ID: " + song.getId() + ", Artist: " + song.getArtist() + ", Song: " + song.getTitle());

                // Create intent used to move to SendMessage activity
                Intent intent = new Intent(HomeActivity.this, MediaPlayerActivity.class);

                Bundle songInformation = new Bundle();
                intent.putExtra(SONG_INFORMATION, songInformation);

                songInformation.putString("SONG_ID", String.valueOf(song.getId()));

                Log.i(TAG, "Starting MediaPlayer activity");

                startActivity(intent);

            }

        });

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        songListAdapter.clear();
    }

    /**
     * Uses the Cursor returned from the loader to generate an ArrayList of Song objects used to
     * populate the ListView.
     *
     * @param songListCursor The Cursor returned from the loader.
     * @return               An ArrayList of Song objects.
     */
    private ArrayList<Song> getSongsFromCursor(Cursor songListCursor) {

        if (TEST_MODE) {

            while (songListCursor.moveToNext()) {

                for (int i = 0; i < songListCursor.getColumnCount(); i++) {
                    Log.d(songListCursor.getColumnName(i), songListCursor.getString(i));
                }

            }

        }

        final int SONG_ID_COLUMN_INDEX = 0;
        final int SONG_ARTIST_COLUMN_INDEX = 1;
        final int SONG_TITLE_COLUMN_INDEX = 2;
        final int SONG_DURATION_COLUMN_INDEX = 3;
        final int SONG_ALBUM_TITLE_COLUMN_INDEX = 4;

        ArrayList<Song> songs = new ArrayList<Song>();

        while (songListCursor.moveToNext()) {

            songs.add(new Song(
                    songListCursor.getInt(SONG_ID_COLUMN_INDEX),
                    songListCursor.getString(SONG_ARTIST_COLUMN_INDEX),
                    songListCursor.getString(SONG_TITLE_COLUMN_INDEX),
                    songListCursor.getString(SONG_DURATION_COLUMN_INDEX),
                    songListCursor.getString(SONG_ALBUM_TITLE_COLUMN_INDEX)
            ));

        }

        return songs;

    }

}
