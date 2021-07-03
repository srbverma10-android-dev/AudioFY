package com.sourabhverma.audiofy.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "otherPlaylists")
public class dataForRecyclerView implements Parcelable {


    public dataForRecyclerView(int sn, String name, ArrayList<RUnderR> array) {
        this.sn = sn;
        this.name = name;
        this.array = array;
    }

    public int getSn() {
        return sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<RUnderR> getArray() {
        return array;
    }

    @PrimaryKey(autoGenerate = true)
    private final int sn;
    private String name;
    private final ArrayList<RUnderR> array;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sn);
        dest.writeString(this.name);
        dest.writeList(this.array);
    }

    protected dataForRecyclerView(Parcel in) {
        this.sn = in.readInt();
        this.name = in.readString();
        this.array = new ArrayList<>();
        in.readList(this.array, RUnderR.class.getClassLoader());
    }

    public static final Creator<dataForRecyclerView> CREATOR = new Creator<dataForRecyclerView>() {
        @Override
        public dataForRecyclerView createFromParcel(Parcel source) {
            return new dataForRecyclerView(source);
        }

        @Override
        public dataForRecyclerView[] newArray(int size) {
            return new dataForRecyclerView[size];
        }
    };
}
