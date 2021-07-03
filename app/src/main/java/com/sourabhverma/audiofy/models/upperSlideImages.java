package com.sourabhverma.audiofy.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "upperSlide")
public class upperSlideImages implements Parcelable {


    public upperSlideImages(String songPhoto, String longSongPhoto, String songName, String songId, int sn) {
        this.songPhoto = songPhoto;
        this.longSongPhoto = longSongPhoto;
        this.songName = songName;
        this.songId = songId;
        this.sn = sn;
    }

    public String getSongPhoto() {
        return songPhoto;
    }

    public String getLongSongPhoto() {
        return longSongPhoto;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongId() {
        return songId;
    }

    public int getSn() {
        return sn;
    }

    private final String songPhoto;
    private final String longSongPhoto;
    private final String songName;
    private final String songId;
    @PrimaryKey(autoGenerate = true)
    private final int sn;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.songPhoto);
        dest.writeString(this.longSongPhoto);
        dest.writeString(this.songName);
        dest.writeString(this.songId);
        dest.writeInt(this.sn);
    }


    protected upperSlideImages(Parcel in) {
        this.songPhoto = in.readString();
        this.longSongPhoto = in.readString();
        this.songName = in.readString();
        this.songId = in.readString();
        this.sn = in.readInt();
    }

    public static final Parcelable.Creator<upperSlideImages> CREATOR = new Parcelable.Creator<upperSlideImages>() {
        @Override
        public upperSlideImages createFromParcel(Parcel source) {
            return new upperSlideImages(source);
        }

        @Override
        public upperSlideImages[] newArray(int size) {
            return new upperSlideImages[size];
        }
    };
}
