package com.sourabhverma.audiofy.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.adapter.otherPlaylistAdapter;
import com.sourabhverma.audiofy.adapter.upperSlideAdapter;
import com.sourabhverma.audiofy.models.dataForRecyclerView;
import com.sourabhverma.audiofy.models.upperSlideImages;
import com.sourabhverma.audiofy.neerajAssignment.ForNeeraj;
import com.sourabhverma.audiofy.viewModel.MusicFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sourabhverma.audiofy.utils.Constants.TAG;

public class MusicFragment extends Fragment {

    private final Handler sliderHandler = new Handler();

    private upperSlideAdapter usa;

    private final List<upperSlideImages> list = new ArrayList<>();
    private final List<dataForRecyclerView> dataForRecyclerViewList = new ArrayList<>();

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private TabLayoutMediator tabLayoutMediator;

    private RecyclerView recyclerView;

    private otherPlaylistAdapter otherPlaylistAdapter;

    public MusicFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_music,container,false);

        viewPager2 = view.findViewById(R.id.viewPagerFragmentMusic);

        recyclerView = view.findViewById(R.id.recyclerViewMusicFragment);

        tabLayout = view.findViewById(R.id.tabLayoutUpperSlider);
        tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, (tab, position) -> {

            switch (position){

                case 0:
                case 4:
                case 3:
                case 2:
                case 1: {
                    tab.setIcon(R.drawable.dot);
                    break;
                }

            }

        });

        initViewModel();

        initViewPager2AndTabLayout();

        initRecyclerView(view);


        return view;
    }



    private void initRecyclerView(View view) {

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
    }


    private void initViewPager2AndTabLayout() {

        viewPager2.setUserInputEnabled(true);

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_ALWAYS);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(10));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.80f + r*0.20f);
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHandler .removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(R.drawable.dot_selected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(R.drawable.dot);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tab.setIcon(R.drawable.dot_selected);
            }
        });


    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {

            if (viewPager2.getCurrentItem() == 4) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }


        }
    };

    private void initViewModel() {

        Observer<List<upperSlideImages>> observer = upperSlideImagesList -> {

            list.clear();
            list.addAll(upperSlideImagesList);

            if (usa == null){
                usa = new upperSlideAdapter(list, getActivity());
                viewPager2.setAdapter(usa);
                tabLayoutMediator.attach();
            } else {
                usa.notifyDataSetChanged();
            }

        };

        Observer<List<dataForRecyclerView>> observer1 = dataForRecyclerViews -> {

            dataForRecyclerViewList.clear();
            dataForRecyclerViewList.addAll(dataForRecyclerViews);

            if (otherPlaylistAdapter == null){
                otherPlaylistAdapter = new otherPlaylistAdapter(dataForRecyclerViewList,getActivity());
                recyclerView.setAdapter(otherPlaylistAdapter);
            } else {
                otherPlaylistAdapter.notifyDataSetChanged();
            }

        };

        MusicFragmentViewModel mViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity())).get(MusicFragmentViewModel.class);

        mViewModel.bitmapList.observe(getViewLifecycleOwner(),observer);
        mViewModel.listLiveData.observe(getViewLifecycleOwner(),observer1);
    }
}