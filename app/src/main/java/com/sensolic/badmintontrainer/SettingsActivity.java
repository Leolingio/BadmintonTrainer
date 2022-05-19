package com.sensolic.badmintontrainer;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Storage storage;
    private Settings settings;
    private CheckBox manualStartPos;
    private CheckBox debugMode;
    private CheckBox autocompleteScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        storage = Storage.getInstance(getApplicationContext());
        settings = Settings.getInstance(getApplicationContext());

        manualStartPos = findViewById(R.id.setManualStartPositions);
        manualStartPos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.setManualStartPos(b);
                save();
            }
        });

        debugMode = findViewById(R.id.setDebugMode);
        debugMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.setDebugMode(b);
                save();
            }
        });

        autocompleteScore = findViewById(R.id.setAutocompleteScore);
        autocompleteScore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Settings.setAutocompleteScore(b);
                save();
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
        super.onPause();
    }

    /**
     * This method refreshes the displayed settings by the values in the Settings object
     */
    private void refreshSettings() {
        manualStartPos.setChecked(Settings.manualStartPos());
        debugMode.setChecked(Settings.debugMode());
        autocompleteScore.setChecked(Settings.autocompleteScore());
    }

    /**
     * This method saves the current settings
     */
    private void save() {
        storage.saveSettings();
    }


}