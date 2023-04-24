package com.sensolic.badmintontrainer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sensolic.badmintontrainer.data.Storage;
import com.sensolic.badmintontrainer.statsFragments.HomeFragment;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    private Storage storage;
    private Settings settings;
    private CheckBox manualStartPos;
    private CheckBox debugMode;
    private CheckBox autocompleteScore;
    private SeekBar singlesDiffSeekBar;
    private SeekBar doublesDiffSeekBar;
    private SeekBar textSizeSeekBar;

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

        textSizeSeekBar = findViewById(R.id.seekBarTextSize);
        textSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                Settings.setTextSize(progress);
                refreshTextSizeText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        singlesDiffSeekBar = findViewById(R.id.seekBarSingles);
        doublesDiffSeekBar = findViewById(R.id.seekBarDoubles);

        singlesDiffSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.setSinglesPlayerDifference(progress);
                refreshPlayerDiffText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        doublesDiffSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Settings.setDoublesPlayerDifference(progress);
                refreshPlayerDiffText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        manualStartPos = findViewById(R.id.setManualStartPositions);
        manualStartPos.setOnCheckedChangeListener((compoundButton, b) -> {
            Settings.setManualStartPos(b);
            save();
        });

        debugMode = findViewById(R.id.setDebugMode);
        debugMode.setOnCheckedChangeListener((compoundButton, b) -> {
            Settings.setDebugMode(b);
            save();
        });

        autocompleteScore = findViewById(R.id.setAutocompleteScore);
        autocompleteScore.setOnCheckedChangeListener((compoundButton, b) -> {
            Settings.setAutocompleteScore(b);
            save();
        });

        refreshSettings();
        refreshPlayerDiffText();
        refreshTextSizeText();

        TextView versionText =findViewById(R.id.version);
        versionText.setText(getString(R.string.version)+ BuildConfig.VERSION_NAME);

        Button b = findViewById(R.id.showChangelogButton);
        b.setOnClickListener(view -> {
            StatsActivity.showChangelog();
            finish();
        });

        RadioGroup radioGroup = findViewById(R.id.defaultMatchType);
        if(Settings.getDefaultMatchType().equals("Singles")){
            radioGroup.check(R.id.singlesMatchSelector);
        } else{
            radioGroup.check(R.id.doublesMatchSelector);
        }
        radioGroup.setOnCheckedChangeListener((radioGroup1, checkedId) -> {
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
        singlesDiffSeekBar.setProgress(Settings.singlesPlayerDifference());
        doublesDiffSeekBar.setProgress(Settings.doublesPlayerDifference());
        textSizeSeekBar.setProgress(Settings.textSize());

        float sizeHead = 0, sizeText = 0;
        switch(Settings.textSize()){
            case 1:
                sizeHead = Settings.TEXTSIZE_SMALL_HEADER;
                sizeText = Settings.TEXTSIZE_SMALL_TEXT;
                break;
            case 2:
                sizeHead = Settings.TEXTSIZE_NORMAL_HEADER;
                sizeText = Settings.TEXTSIZE_NORMAL_TEXT;
                break;
            case 3:
                sizeHead = Settings.TEXTSIZE_BIG_HEADER;
                sizeText = Settings.TEXTSIZE_BIG_TEXT;
                break;
        }

        // Setting up correct text sizes
        TextView tv = findViewById(R.id.settingsHeadline);
        tv.setTextSize(sizeHead * 2);
        tv = findViewById(R.id.generalHeadline);
        tv.setTextSize(sizeHead + 2);
        tv = findViewById(R.id.textSizeText);
        tv.setTextSize(sizeText + 2);
        tv = findViewById(R.id.recommendationsHeadline);
        tv.setTextSize(sizeHead + 2);
        tv = findViewById(R.id.singlesPointDiffText);
        tv.setTextSize(sizeText + 2);
        tv = findViewById(R.id.doublesPointDiffText);
        tv.setTextSize(sizeText + 2);
        tv = findViewById(R.id.registerMatchHeadline);
        tv.setTextSize(sizeHead + 2);
        CheckBox cb = findViewById(R.id.setAutocompleteScore);
        cb.setTextSize(sizeText + 2);
        tv = findViewById(R.id.defaultMatchTypeText);
        tv.setTextSize(sizeText + 2);
        RadioButton rb = findViewById(R.id.singlesMatchSelector);
        rb.setTextSize(sizeText + 2);
        rb = findViewById(R.id.doublesMatchSelector);
        rb.setTextSize(sizeText + 2);
        tv = findViewById(R.id.version);
        tv.setTextSize(sizeText + 2);

        Button b = findViewById(R.id.showChangelogButton);
        b.setTextSize(sizeText);
    }

    /**
     * This method refreshes the text corresponding to the player difference progress-bar
     */
    private void refreshPlayerDiffText() {
        TextView maxPointDiffSinglesText = findViewById(R.id.singlesPointDiffText);
        TextView maxPointDiffDoublesText = findViewById(R.id.doublesPointDiffText);
        maxPointDiffSinglesText.setText("Max point difference in Singles: "+Settings.singlesPlayerDifference()+" Points");
        maxPointDiffDoublesText.setText("Max point difference in Doubles: "+Settings.doublesPlayerDifference()+" Points");
    }

    /**
     * This method refreshes the text corresponding to the player difference progress-bar
     */
    private void refreshTextSizeText() {
        TextView textSizeText = findViewById(R.id.textSizeText);
        String val  = "";
        switch(Settings.textSize()){
            case 1:
                val = "small";
                break;
            case 2:
                val = "normal";
                break;
            case 3:
                val = "large";
                break;
        }
        textSizeText.setText("Text Size: " + val);
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