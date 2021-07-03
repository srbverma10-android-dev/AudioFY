package com.sourabhverma.audiofy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sourabhverma.audiofy.R;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home,container,false);

        initViewPagerAndTablayout(view);

        return view;
    }

    private void initViewPagerAndTablayout(View view) {

        ViewPager2 viewPager = view.findViewById(R.id.viewPagerHomeFragment);

        viewPager.setUserInputEnabled(false);

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(getActivity());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout_home_fragment);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, true, (tab, position) -> {

            switch (position){

                case 0:{
                    tab.setText("Music");
                    break;
                }
                case 1:{
                    tab.setText("News");
                    break;
                }
                case 2:{
                    tab.setText("Podcast");
                    break;
                }

            }

        });

        tabLayoutMediator.attach();

    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public @NotNull Fragment createFragment(int position) {

            switch (position){

                case 0:{
                    return new MusicFragment();
                }
                case 1:{
                    return new NewsFragment();
                }
                case 2:{
                    return new PodcastFragment();
                }

            }

            return new MusicFragment();

        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }





}