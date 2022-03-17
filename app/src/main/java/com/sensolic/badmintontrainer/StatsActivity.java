package com.sensolic.badmintontrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

        FloatingActionButton searchButton = findViewById(R.id.SearchButton);
        Intent intentSearch = new Intent(this, SearchActivity.class);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentSearch);
            }
        });

        FloatingActionButton registerMatchButton = findViewById(R.id.RegisterMatchButton);
        Intent intentRegisterMatch = new Intent(this, RegisterMatchActivity.class);
        registerMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentRegisterMatch);
            }
        });
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