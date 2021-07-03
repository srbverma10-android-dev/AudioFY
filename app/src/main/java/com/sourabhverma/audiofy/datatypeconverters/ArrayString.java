package com.sourabhverma.audiofy.datatypeconverters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sourabhverma.audiofy.models.RUnderR;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayString {
    @TypeConverter
    public static ArrayList<RUnderR> fromString(String value) {
        Type listType = new TypeToken<ArrayList<RUnderR>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<RUnderR> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
}
