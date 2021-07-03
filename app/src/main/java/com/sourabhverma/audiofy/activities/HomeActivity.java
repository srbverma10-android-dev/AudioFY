package com.sourabhverma.audiofy.activities;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.sourabhverma.audiofy.R;
import com.sourabhverma.audiofy.fragments.HomeFragment;
import com.sourabhverma.audiofy.fragments.SearchFragment;
import com.sourabhverma.audiofy.fragments.SettingFragment;
import com.sourabhverma.audiofy.viewModel.MusicFragmentViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomeActivity extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //<<<<<====== Initialization ======>>>>>
        initialization();
    }

    private void initialization() {

        ViewPager2 viewPager = findViewById(R.id.viewpagerinHomeActivity);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        viewPager.setUserInputEnabled(false);
        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(HomeActivity.this);
        viewPager.setAdapter(pagerAdapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager, true, (tab, position) -> {

            switch (position){

                case 0:{
                    tab.setIcon(R.drawable.home);
                    break;
                }
                case 1:{
                    tab.setIcon(R.drawable.search);
                    break;
                }
                case 2:{
                    tab.setIcon(R.drawable.settings);
                    break;
                }

            }

        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int tabIconColor = ContextCompat.getColor(HomeActivity.this, R.color.aqua);
                Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int tabIconColor = ContextCompat.getColor(HomeActivity.this, R.color.white);
                Objects.requireNonNull(tab.getIcon()).setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        tabLayoutMediator.attach();

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private static class ScreenSlidePagerAdapter extends FragmentStateAdapter {

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }
        @Override
        public @NotNull Fragment createFragment(int position) {

            switch (position){

                case 0:{
                    return new HomeFragment();
                }
                case 1:{
                    return new SearchFragment();
                }
                case 2:{
                    return new SettingFragment();
                }

            }


            return new HomeFragment();
        }

        @Override
        public int getItemCount() {
            return 3;
        }

    }

}