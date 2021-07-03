package com.sourabhverma.audiofy.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sourabhverma.audiofy.models.dataForRecyclerView;
import com.sourabhverma.audiofy.models.upperSlideImages;
import com.sourabhverma.audiofy.repository.MusicFragmentRepository;

import java.util.List;

public class MusicFragmentViewModel extends AndroidViewModel {
    public final LiveData<List<upperSlideImages>> bitmapList;
    public final LiveData<List<dataForRecyclerView>> listLiveData;
    public MusicFragmentViewModel(@NonNull Application application) {
        super(application);
        MusicFragmentRepository mRepository = MusicFragmentRepository.getInstance(application.getApplicationContext());
        bitmapList = mRepository.upperSlideImages;
        listLiveData = mRepository.listLiveData;
    }
}
