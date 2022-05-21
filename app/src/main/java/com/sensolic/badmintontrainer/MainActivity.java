package com.sensolic.badmintontrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sensolic.badmintontrainer.data.Storage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Storage.getInstance(getApplicationContext());
        Settings.getInstance(getApplicationContext());
    }

    public void startTraining(View view){
        //Intent intent = new Intent(this, StartActivity.class);
        Toast.makeText(getApplicationContext(),"This function is not available yet",Toast.LENGTH_SHORT).show();
        //startActivity(intent);
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openStatistics(View view){
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent); //comm
    }
}