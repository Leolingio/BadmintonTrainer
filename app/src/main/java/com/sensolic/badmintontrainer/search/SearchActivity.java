package com.sensolic.badmintontrainer.search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.sensolic.badmintontrainer.R;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {

    ListView listView;
    SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        listView = findViewById(R.id.listView);
        SearchEntry p1 = new SearchEntry("Player 1", "#P0001");
        SearchEntry p2 = new SearchEntry("Player 2", "#P0002");
        SearchEntry p3 = new SearchEntry("Player 3", "#P0003");
        SearchEntry p4 = new SearchEntry("Player 4", "#P0004");
        SearchEntry p5 = new SearchEntry("Match 1", "#M0001");
        SearchEntry p6 = new SearchEntry("Match 2", "#M0002");
        SearchEntry p7 = new SearchEntry("Match 3", "#M0003");

        SearchEntry[] entries = new SearchEntry[]{
                p1,p2,p3,p4,p5,p6,p7
        };

        ArrayList<SearchEntry> arrayList = new ArrayList<>(Arrays.asList(entries));

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

    }

}