package com.sourabhverma.audiofy.helperClasses;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public interface SignUpHelper {

    String BASE_URL = "http://192.168.43.144/";
    String SIGN_UP = "signup.php";

    Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build();

    @FormUrlEncoded
    @POST(SIGN_UP)
    Call<Void> createUser(@Field("id") String id,
                          @Field("email") String email,
                          @Field("firstname") String firstname,
                          @Field("lastname") String lastname,
                          @Field("fullname") String fullname,
                          @Field("photourl") String photourl);

}
