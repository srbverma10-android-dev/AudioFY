package com.sourabhverma.audiofy.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sourabhverma.audiofy.models.News;
import com.sourabhverma.audiofy.repository.NewsFragmentRepository;

public class NewsFragmentViewModel extends AndroidViewModel {

    public MutableLiveData<News> newsLiveData;

    public NewsFragmentViewModel(@NonNull Application application) {
        super(application);

        NewsFragmentRepository mRepository = NewsFragmentRepository.getInstance();
        newsLiveData = mRepository.newsLiveData;

    }


}
