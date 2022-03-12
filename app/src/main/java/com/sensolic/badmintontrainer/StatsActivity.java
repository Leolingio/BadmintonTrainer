package com.sensolic.badmintontrainer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sensolic.badmintontrainer.statsFragments.HomeFragment;
import com.sensolic.badmintontrainer.statsFragments.LeaderboardFragment;
import com.sensolic.badmintontrainer.statsFragments.adapters.ViewPagerAdapter;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        setUpTabs();
    }

    private void setUpTabs(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new LeaderboardFragment(), "Leaderboard");

        ViewPager vp = findViewById(R.id.viewPager);
        vp.setAdapter(adapter);

        TabLayout tl = findViewById(R.id.tabs);
        tl.setupWithViewPager(vp);

        tl.getTabAt(0).setIcon(R.drawable.ic_home_24);
        tl.getTabAt(1).setIcon(R.drawable.ic_leaderboard_24);

    }
}