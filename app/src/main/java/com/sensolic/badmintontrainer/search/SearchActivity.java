package com.sensolic.badmintontrainer.search;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.sensolic.badmintontrainer.R;

import java.util.ArrayList;

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

        ArrayList<SearchEntry> arrayList = new ArrayList<>();
        arrayList.add(p1);
        arrayList.add(p2);

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