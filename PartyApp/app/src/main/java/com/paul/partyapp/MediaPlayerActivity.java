package com.paul.partyapp;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaPlayerActivity extends ActionBarActivity {

    private static final String TAG = "MediaPlayerActivity";

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                loadSong();
            }

        };

        new Thread(runnable).start();

    }

    @Override
    public void onPause() {

        super.onPause();

        mediaPlayer.release();
        mediaPlayer = null;

    }

    @Override
    public void onResume() {
        super.onResume();

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                loadSong();
            }

        };

        new Thread(runnable).start();

    }

    /**
     * Loads the song tapped on the song list and creates a MediaPlayer for it.
     */
    public void loadSong() {

        // Retrieve the ID of the song tapped on the home screen
        Intent intent = getIntent();
        Bundle songInformation = intent.getBundleExtra(HomeActivity.SONG_INFORMATION);

        // Generate a URI for that song and create a MediaPlayer
        Uri songUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(songInformation.getString("SONG_ID"))
        );

        try {
            setSongMetaData(songUri);
            mediaPlayer = MediaPlayer.create(this, songUri);
        } catch (Exception e) {
            Log.e(TAG, "Error opening the song!");
            e.printStackTrace();
        }

    }

    public void setSongMetaData(Uri songUri) {

        final MediaMetadataRetriever metaDataRetriever = new MediaMetadataRetriever();
        metaDataRetriever.setDataSource(this, songUri);

        final byte[] albumArtBytes = metaDataRetriever.getEmbeddedPicture();

        // Set all of the metadata information on the UI thread so that we can access the views
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                ImageView albumArt = (ImageView) findViewById(R.id.media_player_album_art);
                albumArt.setImageBitmap(BitmapFactory.decodeByteArray(albumArtBytes, 0, albumArtBytes.length));

                TextView songTitle = (TextView) findViewById(R.id.media_player_title);
                songTitle.setText(metaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));

                TextView albumTitle = (TextView) findViewById(R.id.media_player_album);
                albumTitle.setText(metaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));

                TextView artist = (TextView) findViewById(R.id.media_player_artist);
                artist.setText(metaDataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));

                TextView duration = (TextView) findViewById(R.id.media_player_duration);

                duration.setText(new SimpleDateFormat("mm:ss").format(
                        new Date(Long.parseLong(metaDataRetriever.extractMetadata(
                                MediaMetadataRetriever.METADATA_KEY_DURATION)))));

            }

        });

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

    // TODO: Play and pause really don't need to be separate
    public void playSong(View view) throws Exception {

        if (!(mediaPlayer.isPlaying())) {
            mediaPlayer.start();
        }

    }

    public void pauseSong(View view) throws Exception {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }

    }

    public void stopSong(View view) throws Exception {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }

    }

}
