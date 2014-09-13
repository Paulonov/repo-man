package com.paul.partyapp;

public class Song {

    private int id;
    private String artist;
    private String title;
    private String duration;
    private String albumTitle;

    public Song(int id, String artist, String title, String duration, String albumTitle) {
        this.id = id;
        this.artist = artist;
        this.title = title;
        this.duration = duration;
        this.albumTitle = albumTitle;
    }

    public int getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getDuration() {
        return duration;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

}
