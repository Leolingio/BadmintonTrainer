package com.sensolic.badmintontrainer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;
import com.sensolic.badmintontrainer.registerMatch.RegisterMatchActivity;
import com.sensolic.badmintontrainer.search.SearchActivity;
import com.sensolic.badmintontrainer.search.Searchable;
import com.sensolic.badmintontrainer.statsFragments.HomeFragment;
import com.sensolic.badmintontrainer.statsFragments.LeaderboardFragment;
import com.sensolic.badmintontrainer.statsFragments.MatchInfoFragment;
import com.sensolic.badmintontrainer.statsFragments.PlayerInfoFragment;
import com.sensolic.badmintontrainer.statsFragments.adapters.ViewPagerAdapter;

public class StatsActivity extends AppCompatActivity {

    private static boolean menuExpanded = false;
    private static boolean showSearchInfo = false;
    private boolean closeDialogShowing = false;
    private boolean infoShowing = false;
    public static boolean linkedMatch = false;
    private static Searchable searchableLastShown;
    private static Searchable searchableCurrentlyShowing;
    private static Searchable searchableToShow;
    private static AlertDialog changelog;
    private static AlertDialog closeDialog;
    private FloatingActionButton searchButton, registerMatchButton, settingsButton, menuButton;
    HomeFragment homeFragment = new HomeFragment();
    LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
    MatchInfoFragment matchInfoFragment;
    PlayerInfoFragment playerInfoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // Changelog
        AlertDialog.Builder builder = new AlertDialog.Builder(StatsActivity.this, R.style.AlertDialogTheme);
        builder.setMessage(getString(R.string.changelog_text))
                .setTitle("What's new?");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changelog.dismiss();
            }
        });
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
                if(infoShowing){
                    removeInfoTab();
                    searchableLastShown = null;
                }
            }
        });

        registerMatchButton = findViewById(R.id.RegisterMatchButton);
        Intent intentRegisterMatch = new Intent(this, RegisterMatchActivity.class);
        registerMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentRegisterMatch);
                if(menuExpanded) closeMenu();
                if(infoShowing){
                    removeInfoTab();
                    searchableLastShown = null;
                }
            }
        });

        settingsButton = findViewById(R.id.SettingsButton);
        Intent intentSettings = new Intent(this, SettingsActivity.class);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intentSettings);
                if(menuExpanded) closeMenu();
                if(infoShowing){
                    removeInfoTab();
                    searchableLastShown = null;
                }
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
        adapter.addFragment(homeFragment, "Home");
        //adapter.addFragment(leaderboardFragment, "Leaderboard");

        ViewPager vp = findViewById(R.id.viewPager);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position != 1 && infoShowing){
                    removeInfoTab();
                    searchableLastShown = null;
                    vp.setCurrentItem(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tl = findViewById(R.id.tabs);
        tl.setupWithViewPager(vp);
        tl.getTabAt(0).setIcon(R.drawable.ic_home_24);
        //tl.getTabAt(1).setIcon(R.drawable.ic_leaderboard_24);
        View v = tl.getChildAt(0);
        v.setMinimumWidth(0);
        v.setPadding(0, v.getPaddingTop(), 0, v.getPaddingBottom());
        tl.requestLayout();
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

    private void removeInfoTab(){
        infoShowing = false;
        searchableLastShown = searchableCurrentlyShowing;
        searchableCurrentlyShowing = null;
        ViewPager vp = findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        adapter.addFragment(homeFragment, "Home");
        //adapter.addFragment(leaderboardFragment, "Leaderboard");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(menuExpanded){
            closeMenu();
        } else if(infoShowing){
            if(linkedMatch && searchableLastShown != null){
                searchableToShow = searchableLastShown;
                searchableLastShown = null;
                linkedMatch = false;
                showSearchInfo = true;
                startActivity(new Intent(getApplicationContext(),ReloadActivity.class));
            } else {
                removeInfoTab();
                ViewPager vp = findViewById(R.id.viewPager);
                vp.setCurrentItem(0);
            }
        } else if(!closeDialogShowing){
            showClosingDialog();
        } else{
            closeClosingDialog();
        }
    }

    private void showClosingDialog(){
        closeDialogShowing = true;
        // Changelog
        AlertDialog.Builder builder = new AlertDialog.Builder(StatsActivity.this, R.style.AlertDialogTheme);
        builder.setMessage("Do you really want to exit?");
        builder.setPositiveButton("Close", (dialogInterface, i) -> {
            closeDialog.dismiss();
            finish();
        });
        builder.setNegativeButton("Stay", (dialogInterface, i) -> {
            closeDialog.dismiss();
            closeDialogShowing = false;
        });
        closeDialog = builder.create();
        closeDialog.show();
    }

    private void closeClosingDialog(){
        if(closeDialogShowing){
            closeDialogShowing = false;
            closeDialog.dismiss();
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
            if(infoShowing){
                removeInfoTab();
            }
            showSearchInfo = false;
            infoShowing = true;
            searchableCurrentlyShowing = searchableToShow;
            ViewPager vp = findViewById(R.id.viewPager);
            ViewPagerAdapter adapter = (ViewPagerAdapter) vp.getAdapter();

            assert adapter != null;
            if(searchableCurrentlyShowing instanceof Match){
                matchInfoFragment = new MatchInfoFragment();
                adapter.addFragment(matchInfoFragment, "Match");
            } else if(searchableCurrentlyShowing instanceof Player){
                playerInfoFragment = new PlayerInfoFragment();
                adapter.addFragment(playerInfoFragment, "Player");
            }
            adapter.notifyDataSetChanged();
            vp.setCurrentItem(1);
            if(searchableCurrentlyShowing instanceof Match){
                matchInfoFragment.setInfo((Match) searchableToShow);
            } else if(searchableCurrentlyShowing instanceof Player){
                playerInfoFragment.setInfo((Player) searchableToShow);
            }
            closeMenu();
        }
    }
}