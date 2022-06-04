package com.sensolic.badmintontrainer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.sensolic.badmintontrainer.data.Storage;
import com.sensolic.badmintontrainer.registerMatch.RegisterMatchActivity;
import com.sensolic.badmintontrainer.search.SearchActivity;
import com.sensolic.badmintontrainer.search.Searchable;
import com.sensolic.badmintontrainer.statsFragments.HomeFragment;
import com.sensolic.badmintontrainer.statsFragments.LeaderboardFragment;
import com.sensolic.badmintontrainer.statsFragments.adapters.ViewPagerAdapter;

public class StatsActivity extends AppCompatActivity {

    private static boolean menuExpanded = false;
    private static boolean showSearchInfo = false;
    private boolean infoShowing = false;
    private static Searchable searchableToShow;
    private static AlertDialog changelog;
    private FloatingActionButton searchButton, registerMatchButton, settingsButton, menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Changelog
        AlertDialog.Builder builder = new AlertDialog.Builder(StatsActivity.this);
        builder.setMessage(getString(R.string.changelog_text))
                .setTitle("What's new?");

        changelog = builder.create();

        // Actions to-do on start-up of the app
        Storage.getInstance(getApplicationContext());
        Settings.getInstance(getApplicationContext());

        setUpTabs();

        searchButton = findViewById(R.id.SearchButton);
        Intent intentSearch = new Intent(this, SearchActivity.class);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentSearch);
                if(menuExpanded) closeMenu();
            }
        });

        registerMatchButton = findViewById(R.id.RegisterMatchButton);
        Intent intentRegisterMatch = new Intent(this, RegisterMatchActivity.class);
        registerMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentRegisterMatch);
                if(menuExpanded) closeMenu();
            }
        });

        settingsButton = findViewById(R.id.SettingsButton);
        Intent intentSettings = new Intent(this, SettingsActivity.class);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentSettings);
                if(menuExpanded) closeMenu();
            }
        });

        // Bring buttons in starting position
        searchButton.setTranslationY(575);
        registerMatchButton.setTranslationY(400);
        settingsButton.setTranslationY(225);

        menuButton = findViewById(R.id.mainMenuButton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if(menuExpanded) {
                    closeMenu();
                } else{
                    expandMenu();
                }
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

    public static void showInfo(Searchable searchable){
        showSearchInfo = true;
        searchableToShow = searchable;
    }

    public static void showChangelog(){
        changelog.show();
    }

    private void closeMenu(){
        searchButton.animate().translationY(575).withEndAction(new Runnable() {
            @Override
            public void run() {
                searchButton.setVisibility(View.GONE);
            }
        });
        registerMatchButton.animate().translationY(400).withEndAction(new Runnable() {
            @Override
            public void run() {
                registerMatchButton.setVisibility(View.GONE);
            }
        });;
        settingsButton.animate().translationY(225).withEndAction(new Runnable() {
            @Override
            public void run() {
                settingsButton.setVisibility(View.GONE);
            }
        });;


        menuButton.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_menu_24));
        menuExpanded = false;
    }

    private void expandMenu(){
        searchButton.setVisibility(View.VISIBLE);
        registerMatchButton.setVisibility(View.VISIBLE);
        settingsButton.setVisibility(View.VISIBLE);

        searchButton.animate().translationY(0);
        registerMatchButton.animate().translationY(0);
        settingsButton.animate().translationY(0);

        menuButton.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_cancel_24));
        menuExpanded = true;
    }

    @Override
    public void onBackPressed() {
        if(menuExpanded){
            closeMenu();
        } else{
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(menuExpanded) expandMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(showSearchInfo){
            showSearchInfo = false;
        }
    }
}