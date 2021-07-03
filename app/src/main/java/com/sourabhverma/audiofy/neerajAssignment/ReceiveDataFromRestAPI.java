package com.sourabhverma.audiofy.neerajAssignment;

import com.sourabhverma.audiofy.models.upperSlideImages;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ReceiveDataFromRestAPI {

    String BASE_URL = "https://randomuser.me/api/";
    String RESULT = "?results=30";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET(RESULT)
    Call<ForNeeraj> Data();

}
