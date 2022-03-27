package com.sensolic.badmintontrainer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Space;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setIconified(false);
        searchView.requestFocus();

        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        //mainLayout.addView(createNewSearchEntry());
    }

    private LinearLayout createNewSearchEntry(){
        LinearLayout result = new LinearLayout(getApplicationContext());
        result.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50);
        params.setMargins(30,0,30,0);

        result.setLayoutParams(params);

        TextView playerName = new TextView(getApplicationContext());
        playerName.setText("Player 1");

        TextView playerID = new TextView(getApplicationContext());
        playerID.setText("#P000001");

        Space space = new Space(getApplicationContext());
        space.setLayoutParams(new LinearLayout.LayoutParams(0,0,1));

        result.addView(playerName);
        result.addView(space);
        result.addView(playerID);

        return result;
    }
}