package com.sensolic.badmintontrainer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.sensolic.badmintontrainer.statsFragments.PlayerSearchFragment;
import com.sensolic.badmintontrainer.statsFragments.RankingFragment;
import com.sensolic.badmintontrainer.statsFragments.RegisterMatchFragment;
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
        adapter.addFragment(new PlayerSearchFragment(), "Search");
        adapter.addFragment(new RegisterMatchFragment(), "Register Match");
        adapter.addFragment(new RankingFragment(), "Ranking");

        ViewPager vp = findViewById(R.id.viewPager);
        vp.setAdapter(adapter);

        TabLayout tl = findViewById(R.id.tabs);
        tl.setupWithViewPager(vp);
    }
}