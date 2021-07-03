package com.sourabhverma.audiofy.helperClasses;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sourabhverma.audiofy.models.News;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NewsHelper {

    String BASE_URL = "https://newsapi.org/v2/";
    String METHOD = "top-headlines?country=in&sortBy=popularity&apiKey=a053f8eca2ec438a9779af3b0bccf92d";

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    @GET(METHOD)
    Call<News> getNews();

}
