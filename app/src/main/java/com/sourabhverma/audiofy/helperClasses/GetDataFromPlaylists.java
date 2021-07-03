package com.sourabhverma.audiofy.helperClasses;

import com.sourabhverma.audiofy.models.dataForRecyclerView;
import com.sourabhverma.audiofy.models.upperSlideImages;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GetDataFromPlaylists {

    String BASE_URL = "http://192.168.43.144/";
    String GET_DATA_FROM_PLAYLIST = "getDataFromPlaylists.php";
    String GET_DATA_FOR_RECYCLERVIEW = "getDataForRecyclerView.php";
    String GET_ARTIST_ID = "getArtist.php";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    @GET(GET_DATA_FROM_PLAYLIST)
    Call<ArrayList<upperSlideImages>> DataFromPlaylist();

    @GET(GET_DATA_FOR_RECYCLERVIEW)
    Call<ArrayList<dataForRecyclerView>> DataForRecyclerView();

    @FormUrlEncoded
    @POST(GET_ARTIST_ID)
    Call<ArrayList<String>> getArtist(@Field("id") String id);

}
