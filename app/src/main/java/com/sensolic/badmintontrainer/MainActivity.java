package com.sensolic.badmintontrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startTraining(View view){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void closeApp(View view) {
        finish();
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openStatistics(View view){
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }
}