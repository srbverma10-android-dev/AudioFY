package com.sourabhverma.audiofy.helperClasses;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetSongBase64String {

    String BASE_URL = "http://192.168.43.144/";
    String GET_MP3 = "getMp3.php";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @FormUrlEncoded
    @POST(GET_MP3)
    Call<String> getMp3(@Field("id") String id);


}
