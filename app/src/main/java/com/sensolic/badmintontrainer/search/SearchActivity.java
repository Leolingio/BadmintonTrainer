package com.sensolic.badmintontrainer.search;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.data.Storage;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    Storage storage;
    ListView listView;
    SearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        storage = Storage.getInstance(getApplicationContext());
        listView = findViewById(R.id.listView);

        ArrayList<Searchable> arrayList = new ArrayList<>();

        storage.addStoredObjects(arrayList);

        adapter = new SearchAdapter(getApplicationContext(), arrayList, this);

        listView.setAdapter(adapter);


        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.clearFocus();
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
        pullToRefresh.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });
    }
}