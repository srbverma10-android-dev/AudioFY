package com.sourabhverma.audiofy.models;

public class RUnderR {

    public RUnderR(String songId, String songName, String songPhoto) {
        this.songId = songId;
        this.songName = songName;
        this.songPhoto = songPhoto;
    }

    public String getSongId() {
        return songId;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongPhoto() {
        return songPhoto;
    }


    private final String songId;
    private final String songName;
    private final String songPhoto;

}
