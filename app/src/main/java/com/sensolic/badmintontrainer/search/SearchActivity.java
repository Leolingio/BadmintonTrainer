package com.sensolic.badmintontrainer.search;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.Settings;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        float sizeHead = 0;
        switch(Settings.textSize()){
            case 1:
                sizeHead = Settings.TEXTSIZE_SMALL_HEADER;
                break;
            case 2:
                sizeHead = Settings.TEXTSIZE_NORMAL_HEADER;
                break;
            case 3:
                sizeHead = Settings.TEXTSIZE_BIG_HEADER;
                break;
        }

        SearchView searchView = findViewById(R.id.searchView);
        AutoCompleteTextView search_text = (AutoCompleteTextView) searchView.findViewById(searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        search_text.setTextColor(Color.WHITE);
        search_text.setTextSize(sizeHead);

        storage = Storage.getInstance(getApplicationContext());
        listView = findViewById(R.id.listView);

        ArrayList<Searchable> arrayList = new ArrayList<>();

        storage.addStoredObjects(arrayList);

        adapter = new SearchAdapter(getApplicationContext(), arrayList, this);

        listView.setAdapter(adapter);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}