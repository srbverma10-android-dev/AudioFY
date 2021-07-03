package com.sourabhverma.audiofy.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.sourabhverma.audiofy.helperClasses.NewsHelper;
import com.sourabhverma.audiofy.models.News;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sourabhverma.audiofy.utils.Constants.TAG;

public class NewsFragmentRepository {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final NewsHelper newsHelper = NewsHelper.retrofit.create(NewsHelper.class);

    public MutableLiveData<News> newsLiveData = new MutableLiveData<>();

    public static NewsFragmentRepository getInstance() {
        return new NewsFragmentRepository(); }

    private NewsFragmentRepository () {

        getDataFromApi();

    }
    private void getDataFromApi() {

        executor.execute(() -> {

            Call<News> dataCall = newsHelper.getNews();
            dataCall.enqueue(new Callback<News>() {
                @Override
                public void onResponse(@NotNull Call<News> call, @NotNull Response<News> response) {
                    Log.d(TAG, "onResponse: " + response.body());

                    News news = response.body();
                    assert news != null;
                    newsLiveData.setValue(news);

                    Log.d(TAG, "onResponse: " + news.getArticles().size());

                }

                @Override
                public void onFailure(@NotNull Call<News> call, @NotNull Throwable t) {

                    Log.d(TAG, "onFailure: " + t);

                }
            });

        });

    }

}
