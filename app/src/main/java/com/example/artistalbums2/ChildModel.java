package com.example.artistalbums2;

import java.util.ArrayList;
import java.util.HashMap;

public class ChildModel {
    private String artist, albumName, albumImage;
    private ArrayList<String> albumItems;
    private HashMap<String, String> albumItemsUri;

    public String getArtist() {
        return artist;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public ArrayList<String> getAlbumItems() {
        return albumItems;
    }

    public HashMap<String, String> getAlbumItemsUri() {
        return albumItemsUri;
    }

    public ChildModel(String artist, String albumName, String albumImage, ArrayList<String> albumItems, HashMap<String, String> albumItemsUri) {
        this.artist = artist;
        this.albumName = albumName;
        this.albumImage = albumImage;
        this.albumItems = albumItems;
        this.albumItemsUri = albumItemsUri;
    }
}
