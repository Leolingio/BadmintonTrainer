package com.sensolic.badmintontrainer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sensolic.badmintontrainer.data.Storage;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

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

        Button b = findViewById(R.id.showChangelogButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StatsActivity.showChangelog();
                finish();
            }
        });

        RadioGroup radioGroup = findViewById(R.id.defaultMatchType);
        if(Settings.getDefaultMatchType().equals("Singles")){
            radioGroup.check(R.id.singlesMatchSelector);
        } else{
            radioGroup.check(R.id.doublesMatchSelector);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.singlesMatchSelector:
                        Settings.setDefaultMatchType('S');
                        save();
                        break;
                    case R.id.doublesMatchSelector:
                        Settings.setDefaultMatchType('D');
                        save();
                        break;
                }
            }
        });
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
        save();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}