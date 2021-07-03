package com.sourabhverma.audiofy.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@Entity(tableName = "artistIdAndSongId")
public class ArtistIdAndSongId implements Parcelable {

    public ArtistIdAndSongId(@NotNull String songId, ArrayList<String> stringArrayList) {
        this.songId = songId;
        this.stringArrayList = stringArrayList;
    }

    public @NotNull String getSongId() {
        return songId;
    }

    public ArrayList<String> getStringArrayList() {
        return stringArrayList;
    }

    @PrimaryKey @NotNull
    private final String songId;
    private final ArrayList<String> stringArrayList;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.songId);
        dest.writeStringList(this.stringArrayList);
    }

    protected ArtistIdAndSongId(Parcel in) {
        this.songId = in.readString();
        this.stringArrayList = in.createStringArrayList();
    }

    public static final Parcelable.Creator<ArtistIdAndSongId> CREATOR = new Parcelable.Creator<ArtistIdAndSongId>() {
        @Override
        public ArtistIdAndSongId createFromParcel(Parcel source) {
            return new ArtistIdAndSongId(source);
        }

        @Override
        public ArtistIdAndSongId[] newArray(int size) {
            return new ArtistIdAndSongId[size];
        }
    };
}
