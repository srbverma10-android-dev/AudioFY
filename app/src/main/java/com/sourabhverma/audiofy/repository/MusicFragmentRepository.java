package com.sourabhverma.audiofy.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.sourabhverma.audiofy.helperClasses.GetDataFromPlaylists;
import com.sourabhverma.audiofy.models.ArtistIdAndSongId;
import com.sourabhverma.audiofy.models.RUnderR;
import com.sourabhverma.audiofy.models.dataForRecyclerView;
import com.sourabhverma.audiofy.models.upperSlideImages;
import com.sourabhverma.audiofy.roomdatabase.ArtistIdAndSongIdDatabase;
import com.sourabhverma.audiofy.roomdatabase.otherPlaylistDatabase;
import com.sourabhverma.audiofy.roomdatabase.upperSlideDatabase;
import com.sourabhverma.audiofy.utils.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicFragmentRepository {

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final upperSlideDatabase mDatabase;

    private final otherPlaylistDatabase otherPlaylistDatabase;

    private final ArtistIdAndSongIdDatabase artistIdAndSongIdDatabase;


    private GetDataFromPlaylists getDataFromPlaylists;

    public final LiveData<List<upperSlideImages>> upperSlideImages;

    public final LiveData<List<dataForRecyclerView>> listLiveData;

    private final List<upperSlideImages> usi = new ArrayList<>();

    private ArrayList<String> stringArrayList = new ArrayList<>();

    private final List<dataForRecyclerView> dfr = new ArrayList<>();

    public static MusicFragmentRepository getInstance(Context context) {
        return new MusicFragmentRepository(context); }

    private MusicFragmentRepository (Context context) {

        mDatabase = upperSlideDatabase.getInstance(context);

        otherPlaylistDatabase = com.sourabhverma.audiofy.roomdatabase.otherPlaylistDatabase.getInstance(context);

        artistIdAndSongIdDatabase = ArtistIdAndSongIdDatabase.getInstance(context);

        upperSlideImages = getAll();

        listLiveData = getAllForRecyclerView();

        getDataFromApi();

    }

    private void getDataFromApi(){

        executor.execute(() -> {
            String TAG = Constants.TAG;
            getDataFromPlaylists = GetDataFromPlaylists.retrofit.create(GetDataFromPlaylists.class);
            Call<ArrayList<upperSlideImages>> dataCall = getDataFromPlaylists.DataFromPlaylist();

            dataCall.enqueue(new Callback<ArrayList<com.sourabhverma.audiofy.models.upperSlideImages>>() {
                @Override
                public void onResponse(@NotNull Call<ArrayList<com.sourabhverma.audiofy.models.upperSlideImages>> call, @NotNull Response<ArrayList<com.sourabhverma.audiofy.models.upperSlideImages>> response) {

                    ArrayList<upperSlideImages> upperSlideImagesArrayList = response.body();

                    for(int i = 0; i < Objects.requireNonNull(upperSlideImagesArrayList).size() ; i++ ){

                        String songId = upperSlideImagesArrayList.get(i).getSongId();
                        String longSongPhoto = upperSlideImagesArrayList.get(i).getLongSongPhoto();
                        String songPhoto = upperSlideImagesArrayList.get(i).getSongPhoto();
                        String songName = upperSlideImagesArrayList.get(i).getSongName();

                        Call<ArrayList<String>> dataCall3 = getDataFromPlaylists.getArtist(songId);
                        dataCall3.enqueue(new Callback<ArrayList<String>>() {
                            @Override
                            public void onResponse(@NotNull Call<ArrayList<String>> call, @NotNull Response<ArrayList<String>> response) {
                                stringArrayList = response.body();
                                sendDataToRoom3(new ArtistIdAndSongId(songId,stringArrayList));
                            }
                            @Override
                            public void onFailure(@NotNull Call<ArrayList<String>> call, @NotNull Throwable t) {
                                Log.d(TAG, "onFailure: " + t);
                            }
                        });
                        usi.add(new upperSlideImages(songPhoto,longSongPhoto,songName,songId,i+1));
                    }

                    sendDataToRoom(usi);

                }

                @Override
                public void onFailure(@NotNull Call<ArrayList<com.sourabhverma.audiofy.models.upperSlideImages>> call, @NotNull Throwable t) {

                }
            });




            Call<ArrayList<dataForRecyclerView>> dataCall2 = getDataFromPlaylists.DataForRecyclerView();
            dataCall2.enqueue(new Callback<ArrayList<dataForRecyclerView>>() {
                @Override
                public void onResponse(@NotNull Call<ArrayList<dataForRecyclerView>> call, @NotNull Response<ArrayList<dataForRecyclerView>> response) {

                    ArrayList<dataForRecyclerView> dataForRecyclerViewArrayList = response.body();
                    assert dataForRecyclerViewArrayList != null;

                    for(int i = 0 ; i < dataForRecyclerViewArrayList.size() ; i++ ){

                        String otherPlaylistName = dataForRecyclerViewArrayList.get(i).getName();

                        ArrayList<RUnderR> rUnderRArrayList = dataForRecyclerViewArrayList.get(i).getArray();

                        dfr.add(new dataForRecyclerView(i+1,otherPlaylistName,rUnderRArrayList));

                        Log.d(TAG, "onResponse: \t" + otherPlaylistName);

                    }

                    sendDataToRoom2(dfr);

                }

                @Override
                public void onFailure(@NotNull Call<ArrayList<dataForRecyclerView>> call, @NotNull Throwable t) {

                    Log.d(TAG, "onFailure 2 : " + t);

                }
            });

        });

    }

    private void sendDataToRoom3(ArtistIdAndSongId artistIdAndSongId) {
        executor.execute(() -> artistIdAndSongIdDatabase.artistIdAndSongIdDAO().insert(artistIdAndSongId));
    }

    private void sendDataToRoom2(List<dataForRecyclerView> dfr) {

        executor.execute(() -> otherPlaylistDatabase.otherPlaylistsDAO().insertAll(dfr));

    }

    private void sendDataToRoom(List<com.sourabhverma.audiofy.models.upperSlideImages> usi) {

        executor.execute(() -> mDatabase.upperSlideImagesDAO().insertAll(usi));

    }

    private LiveData<List<upperSlideImages>> getAll() {

        return mDatabase.upperSlideImagesDAO().getAll();

    }

    private LiveData<List<dataForRecyclerView>> getAllForRecyclerView(){

        return otherPlaylistDatabase.otherPlaylistsDAO().getAll();

    }

}
