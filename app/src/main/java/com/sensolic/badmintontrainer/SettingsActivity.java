package com.sensolic.badmintontrainer;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity {

    private Storage storage;
    private Settings settings;
    private CheckBox manualStartPos;
    private CheckBox debugMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        storage = new Storage(getApplicationContext());
        settings = new Settings(storage);

        manualStartPos = findViewById(R.id.setManualStartPositions);
        manualStartPos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settings.setManualStartPos(b);
            }
        });

        debugMode = findViewById(R.id.setDebugMode);
        debugMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                settings.setDebugMode(b);
            }
        });

        refreshSettings();

        TextView versionText =findViewById(R.id.version);
        versionText.setText(getString(R.string.version)+ BuildConfig.VERSION_NAME.toString());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshSettings();
    }

    /**
     * Before the settings activity gets paused, the settings should be saved
     */
    @Override
    protected void onPause() {
        save();
        super.onPause();
    }

    /**
     * This method refreshes the displayed settings by the values in the Settings object
     */
    private void refreshSettings() {
        manualStartPos.setChecked(settings.manualStartPos());
        debugMode.setChecked(settings.debugMode());
    }

    /**
     * This method saves the current settings
     */
    private void save() {
        storage.saveSettings(settings);
    }


}