package com.sensolic.badmintontrainer.search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.Storage;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    static boolean keepRunning = true;
    Storage storage;
    ListView listView;
    SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        storage = Storage.getInstance(getApplicationContext());
        listView = findViewById(R.id.listView);
        /*
        SearchEntry p1 = new SearchEntry("Player 1", "#P0001");
        SearchEntry p2 = new SearchEntry("Player 2", "#P0002");
        SearchEntry p3 = new SearchEntry("Player 3", "#P0003");
        SearchEntry p4 = new SearchEntry("Player 4", "#P0004");

        SearchEntry[] entries = new SearchEntry[]{
                p1,p2,p3,p4
        };
         */

        ArrayList<SearchEntry> arrayList = new ArrayList<>();

        storage.addStoredMatches(arrayList);

        adapter = new SearchAdapter(getApplicationContext(), arrayList);

        listView.setAdapter(adapter);


        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.isEmpty()){
                    adapter.filter("");
                } else{
                    adapter.filter(s);
                }
                return true;
            }
        });

        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                pullToRefresh.setRefreshing(false);
            }
        });

    }
}