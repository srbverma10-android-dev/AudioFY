package com.sourabhverma.audiofy.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.adapter.NewsFragmentAdapter;
import com.sourabhverma.audiofy.adapter.otherPlaylistAdapter;
import com.sourabhverma.audiofy.models.News;
import com.sourabhverma.audiofy.viewModel.NewsFragmentViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.sourabhverma.audiofy.utils.Constants.TAG;

public class NewsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NewsFragmentAdapter newsFragmentAdapter;

    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_news,container,false);

        TextView date = view.findViewById(R.id.dateInNewsFragment);
        recyclerView = view.findViewById(R.id.recyclerViewInNewsFragment);

        //============================================================================

        setUpDate(date);
        initRecyclerView(view);
        initViewModel();
        //============================================================================

        return view;
    }

    private void initRecyclerView(View view) {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void initViewModel() {

        Observer<News> observer = new Observer<News>() {
            @Override
            public void onChanged(News news) {
                if (newsFragmentAdapter == null){
                    newsFragmentAdapter = new NewsFragmentAdapter(news.getArticles(), getActivity());
                    recyclerView.setAdapter(newsFragmentAdapter);
                } else {
                    newsFragmentAdapter.notifyDataSetChanged();
                }
            }
        };

        NewsFragmentViewModel mViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(NewsFragmentViewModel.class);
        mViewModel.newsLiveData.observe(getViewLifecycleOwner(),observer);

    }

    private void setUpDate(TextView date) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        date.setText(formattedDate);
    }
}