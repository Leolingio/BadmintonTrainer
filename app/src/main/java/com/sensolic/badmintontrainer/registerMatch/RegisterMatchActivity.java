package com.sensolic.badmintontrainer.registerMatch;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;

import java.util.ArrayList;
import java.util.Arrays;

public class RegisterMatchActivity extends AppCompatActivity {

    private static long matchID;
    static boolean singlesMatch = true;
    boolean closeActivity = true;
    boolean score1Team1 = false;
    boolean score1Team2 = false;
    boolean score2Team1 = false;
    boolean score2Team2 = false;
    boolean score3Team1 = false;
    boolean score3Team2 = false;
    RelativeLayout container;
    CustomEditText score1team1editText;
    CustomEditText score1team2editText;
    CustomEditText score2team1editText;
    CustomEditText score2team2editText;
    CustomEditText score3team1editText;
    CustomEditText score3team2editText;
    AutoCompleteTextView team1player1chooser;
    AutoCompleteTextView team1player2chooser;
    AutoCompleteTextView team2player1chooser;
    AutoCompleteTextView team2player2chooser;
    private Storage storage;
    private String[] players;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_match);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        storage = Storage.getInstance(getApplicationContext());

        matchID = storage.getLastMatchID();

        container = findViewById(R.id.registerMatchContainer);

        ArrayList<Player> list = storage.getStoredPlayers();
        players = new String[list.size()];
        int index = 0;
        for (Player p : list) {
            players[index] = p.getPlayerName() + " - " + p.getIDInfo();
            index++;
        }

        // Setup edittext references
        score1team1editText = findViewById(R.id.score1Team1);
        score1team2editText = findViewById(R.id.score1Team2);
        score2team1editText = findViewById(R.id.score2Team1);
        score2team2editText = findViewById(R.id.score2Team2);
        score3team1editText = findViewById(R.id.score3Team1);
        score3team2editText = findViewById(R.id.score3Team2);

        Arrays.sort(players);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, players);

        team1player1chooser = findViewById(R.id.team1player1chooser);
        team1player2chooser = findViewById(R.id.team1player2chooser);
        team2player1chooser = findViewById(R.id.team2player1chooser);
        team2player2chooser = findViewById(R.id.team2player2chooser);

        View.OnKeyListener onKeyListener = (view, i, keyEvent) -> {
            if (KeyEvent.KEYCODE_BACK == keyEvent.getKeyCode()) {
                clearFocus();
            }
            closeActivity = false;
            return false;
        };

        team1player1chooser.setAdapter(adapter);
        team1player1chooser.setOnFocusChangeListener((view, focused) -> {
            if (focused) {
                String[] newList;
                String s0 = team1player1chooser.getText().toString();
                String s1 = team1player2chooser.getText().toString();
                String s2 = team2player1chooser.getText().toString();
                String s3 = team2player2chooser.getText().toString();
                int count = 0;
                if(s0.length() != 0){
                    for(String str : players){
                        if(str.equals(s0)){
                            count++;
                        }
                    }
                }
                if(s1.length() != 0){
                    for(String str : players){
                        if(str.equals(s1)){
                            count++;
                        }
                    }
                }
                if(s2.length() != 0){
                    for(String str : players){
                        if(str.equals(s2)){
                            count++;
                        }
                    }
                }
                if(s3.length() != 0){
                    for(String str : players){
                        if(str.equals(s3)){
                            count++;
                        }
                    }
                }
                if(count > 0) {
                    newList = new String[players.length - count];
                    int idx = 0;
                    for (String str : players) {
                        if (!str.equals(s0)
                                && !str.equals(s1)
                                && !str.equals(s2)
                                && !str.equals(s3)) {
                            newList[idx] = str;
                            idx++;
                        }
                    }
                    team1player1chooser.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newList));
                }
                team1player1chooser.showDropDown();
            } else {
                String s = team1player1chooser.getText().toString();
                Arrays.sort(players);
                if (Arrays.binarySearch(players, s) < 0) {
                    team1player1chooser.setText("");
                }

                if(team1player1chooser.getText().toString().equals(team1player2chooser.getText().toString())
                        || team1player1chooser.getText().toString().equals(team2player1chooser.getText().toString())
                        || team1player1chooser.getText().toString().equals(team2player2chooser.getText().toString())){
                    team1player1chooser.setText("");
                }
            }
        });
        team1player1chooser.setOnTouchListener((view, motionEvent) -> {
            String[] newList;
            String s0 = team1player1chooser.getText().toString();
            String s1 = team1player2chooser.getText().toString();
            String s2 = team2player1chooser.getText().toString();
            String s3 = team2player2chooser.getText().toString();
            int count = 0;
            if(s0.length() != 0){
                for(String str : players){
                    if(str.equals(s0)){
                        count++;
                    }
                }
            }
            if(s1.length() != 0){
                for(String str : players){
                    if(str.equals(s1)){
                        count++;
                    }
                }
            }
            if(s2.length() != 0){
                for(String str : players){
                    if(str.equals(s2)){
                        count++;
                    }
                }
            }
            if(s3.length() != 0){
                for(String str : players){
                    if(str.equals(s3)){
                        count++;
                    }
                }
            }
            if(count > 0) {
                newList = new String[players.length - count];
                int idx = 0;
                for (String str : players) {
                    if (!str.equals(s0)
                            && !str.equals(s1)
                            && !str.equals(s2)
                            && !str.equals(s3)) {
                        newList[idx] = str;
                        idx++;
                    }
                }
                team1player1chooser.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newList));
            } else{
                team1player1chooser.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, players));
            }
            team1player1chooser.showDropDown();
            team1player1chooser.requestFocus();
            showKeyboard(team1player1chooser);
            return true;
        });
        team1player1chooser.setOnKeyListener(onKeyListener);
        team1player1chooser.setOnItemClickListener((adapterView, view, i, l) ->{
            clearFocus();
        });

        team1player2chooser.setAdapter(adapter);
        team1player2chooser.setOnFocusChangeListener((view, focused) -> {
            if (focused) {
                String[] newList;
                String s0 = team1player1chooser.getText().toString();
                String s1 = team1player2chooser.getText().toString();
                String s2 = team2player1chooser.getText().toString();
                String s3 = team2player2chooser.getText().toString();
                int count = 0;
                if(s0.length() != 0){
                    for(String str : players){
                        if(str.equals(s0)){
                            count++;
                        }
                    }
                }
                if(s1.length() != 0){
                    for(String str : players){
                        if(str.equals(s1)){
                            count++;
                        }
                    }
                }
                if(s2.length() != 0){
                    for(String str : players){
                        if(str.equals(s2)){
                            count++;
                        }
                    }
                }
                if(s3.length() != 0){
                    for(String str : players){
                        if(str.equals(s3)){
                            count++;
                        }
                    }
                }
                if(count > 0) {
                    newList = new String[players.length - count];
                    int idx = 0;
                    for (String str : players) {
                        if (!str.equals(s0)
                                && !str.equals(s1)
                                && !str.equals(s2)
                                && !str.equals(s3)) {
                            newList[idx] = str;
                            idx++;
                        }
                    }
                    team1player2chooser.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, newList));
                } else{
                    team1player2chooser.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, players));
                }
                team1player2chooser.showDropDown();
            } else {
                String s = team1player2chooser.getText().toString();
                Arrays.sort(players);
                if (Arrays.binarySearch(players, s) < 0) {
                    team1player2chooser.setText("");
                }
                if(team1player2chooser.getText().toString().equals(team1player1chooser.getText().toString())
                        || team1player2chooser.getText().toString().equals(team2player1chooser.getText().toString())
                        || team1player2chooser.getText().toString().equals(team2player2chooser.getText().toString())){
                    team1player2chooser.setText("");
                }
            }
        });
        team1player2chooser.setOnTouchListener((view, motionEvent) -> {
            String[] newList;
            String s0 = team1player1chooser.getText().toString();
            String s1 = team1player2chooser.getText().toString();
            String s2 = team2player1chooser.getText().toString();
            String s3 = team2player2chooser.getText().toString();
            int count = 0;
            if(s0.length() != 0){
                for(String str : players){
                    if(str.equals(s0)){
                        count++;
                    }
                }
            }
            if(s1.length() != 0){
                for(String str : players){
                    if(str.equals(s1)){
                        count++;
                    }
                }
            }
            if(s2.length() != 0){
                for(String str : players){
                    if(str.equals(s2)){
                        count++;
                    }
                }
            }
            if(s3.length() != 0){
                for(String str : players){
                    if(str.equals(s3)){
                        count++;
                    }
                }
            }
            if(count > 0) {
                newList = new String[players.length - count];
                int idx = 0;
                for (String str : players) {
                    if (!str.equals(s0)
                            && !str.equals(s1)
                            && !str.equals(s2)
                            && !str.equals(s3)) {
                        newList[idx] = str;
                        idx++;
                    }
                }
                team1player2chooser.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, newList));
            } else{
                team1player2chooser.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, players));
            }
            team1player2chooser.showDropDown();
            team1player2chooser.requestFocus();
            showKeyboard(team1player2chooser);
            return true;
        });
        team1player2chooser.setOnKeyListener(onKeyListener);
        team1player2chooser.setOnItemClickListener((adapterView, view, i, l) ->{
            clearFocus();
        });

        team2player1chooser.setAdapter(adapter);
        team2player1chooser.setOnFocusChangeListener((view, focused) -> {
            if (focused) {
                String[] newList;
                String s0 = team1player1chooser.getText().toString();
                String s1 = team1player2chooser.getText().toString();
                String s2 = team2player1chooser.getText().toString();
                String s3 = team2player2chooser.getText().toString();
                int count = 0;
                if(s0.length() != 0){
                    for(String str : players){
                        if(str.equals(s0)){
                            count++;
                        }
                    }
                }
                if(s1.length() != 0){
                    for(String str : players){
                        if(str.equals(s1)){
                            count++;
                        }
                    }
                }
                if(s2.length() != 0){
                    for(String str : players){
                        if(str.equals(s2)){
                            count++;
                        }
                    }
                }
                if(s3.length() != 0){
                    for(String str : players){
                        if(str.equals(s3)){
                            count++;
                        }
                    }
                }
                if(count > 0) {
                    newList = new String[players.length - count];
                    int idx = 0;
                    for (String str : players) {
                        if (!str.equals(s0)
                                && !str.equals(s1)
                                && !str.equals(s2)
                                && !str.equals(s3)) {
                            newList[idx] = str;
                            idx++;
                        }
                    }
                    team2player1chooser.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, newList));
                } else{
                    team2player1chooser.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, players));
                }
                team2player1chooser.showDropDown();
            } else {
                String s = team2player1chooser.getText().toString();
                Arrays.sort(players);
                if (Arrays.binarySearch(players, s) < 0) {
                    team2player1chooser.setText("");
                }
                if(team2player1chooser.getText().toString().equals(team1player2chooser.getText().toString())
                        || team2player1chooser.getText().toString().equals(team1player1chooser.getText().toString())
                        || team2player1chooser.getText().toString().equals(team2player2chooser.getText().toString())){
                    team2player1chooser.setText("");
                }
            }
        });
        team2player1chooser.setOnTouchListener((view, motionEvent) -> {
            String[] newList;
            String s0 = team1player1chooser.getText().toString();
            String s1 = team1player2chooser.getText().toString();
            String s2 = team2player1chooser.getText().toString();
            String s3 = team2player2chooser.getText().toString();
            int count = 0;
            if(s0.length() != 0){
                for(String str : players){
                    if(str.equals(s0)){
                        count++;
                    }
                }
            }
            if(s1.length() != 0){
                for(String str : players){
                    if(str.equals(s1)){
                        count++;
                    }
                }
            }
            if(s2.length() != 0){
                for(String str : players){
                    if(str.equals(s2)){
                        count++;
                    }
                }
            }
            if(s3.length() != 0){
                for(String str : players){
                    if(str.equals(s3)){
                        count++;
                    }
                }
            }
            if(count > 0) {
                newList = new String[players.length - count];
                int idx = 0;
                for (String str : players) {
                    if (!str.equals(s0)
                            && !str.equals(s1)
                            && !str.equals(s2)
                            && !str.equals(s3)) {
                        newList[idx] = str;
                        idx++;
                    }
                }
                team2player1chooser.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newList));
            } else{
                team2player1chooser.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, players));
            }
            team2player1chooser.showDropDown();
            team2player1chooser.requestFocus();
            showKeyboard(team2player1chooser);
            return true;
        });
        team2player1chooser.setOnKeyListener(onKeyListener);
        team2player1chooser.setOnItemClickListener((adapterView, view, i, l) ->{
            clearFocus();
        });

        team2player2chooser.setAdapter(adapter);
        team2player2chooser.setOnFocusChangeListener((view, focused) -> {
            if (focused) {
                String[] newList;
                String s0 = team1player1chooser.getText().toString();
                String s1 = team1player2chooser.getText().toString();
                String s2 = team2player1chooser.getText().toString();
                String s3 = team2player2chooser.getText().toString();
                int count = 0;
                if(s0.length() != 0){
                    for(String str : players){
                        if(str.equals(s0)){
                            count++;
                        }
                    }
                }
                if(s1.length() != 0){
                    for(String str : players){
                        if(str.equals(s1)){
                            count++;
                        }
                    }
                }
                if(s2.length() != 0){
                    for(String str : players){
                        if(str.equals(s2)){
                            count++;
                        }
                    }
                }
                if(s3.length() != 0){
                    for(String str : players){
                        if(str.equals(s3)){
                            count++;
                        }
                    }
                }
                if(count > 0) {
                    newList = new String[players.length - count];
                    int idx = 0;
                    for (String str : players) {
                        if (!str.equals(s0)
                                && !str.equals(s1)
                                && !str.equals(s2)
                                && !str.equals(s3)) {
                            newList[idx] = str;
                            idx++;
                        }
                    }
                    team2player2chooser.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, newList));
                } else{
                    team2player2chooser.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, players));
                }
                team2player2chooser.showDropDown();
            } else {
                String s = team2player2chooser.getText().toString();
                Arrays.sort(players);
                if (Arrays.binarySearch(players, s) < 0) {
                    team2player2chooser.setText("");
                }
                if(team2player2chooser.getText().toString().equals(team1player2chooser.getText().toString())
                        || team2player2chooser.getText().toString().equals(team2player1chooser.getText().toString())
                        || team2player2chooser.getText().toString().equals(team1player1chooser.getText().toString())){
                    team2player2chooser.setText("");
                }
            }
        });
        team2player2chooser.setOnTouchListener((view, motionEvent) -> {
            String[] newList;
            String s0 = team1player1chooser.getText().toString();
            String s1 = team1player2chooser.getText().toString();
            String s2 = team2player1chooser.getText().toString();
            String s3 = team2player2chooser.getText().toString();
            int count = 0;
            if(s0.length() != 0){
                for(String str : players){
                    if(str.equals(s0)){
                        count++;
                    }
                }
            }
            if(s1.length() != 0){
                for(String str : players){
                    if(str.equals(s1)){
                        count++;
                    }
                }
            }
            if(s2.length() != 0){
                for(String str : players){
                    if(str.equals(s2)){
                        count++;
                    }
                }
            }
            if(s3.length() != 0){
                for(String str : players){
                    if(str.equals(s3)){
                        count++;
                    }
                }
            }
            if(count > 0) {
                newList = new String[players.length - count];
                int idx = 0;
                for (String str : players) {
                    if (!str.equals(s0)
                            && !str.equals(s1)
                            && !str.equals(s2)
                            && !str.equals(s3)) {
                        newList[idx] = str;
                        idx++;
                    }
                }
                team2player2chooser.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newList));
            } else{
                team2player2chooser.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, players));
            }
            team2player2chooser.showDropDown();
            team2player2chooser.requestFocus();
            showKeyboard(team2player2chooser);
            return true;
        });
        team2player2chooser.setOnKeyListener(onKeyListener);
        team2player2chooser.setOnItemClickListener((adapterView, view, i, l) ->{
            clearFocus();
        });

        RadioGroup radioGroup = findViewById(R.id.matchTypeSelector);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            AutoCompleteTextView team1player1chooser = findViewById(R.id.team1player1chooser);
            AutoCompleteTextView team1player2chooser = findViewById(R.id.team1player2chooser);
            AutoCompleteTextView team2player1chooser = findViewById(R.id.team2player1chooser);
            AutoCompleteTextView team2player2chooser = findViewById(R.id.team2player2chooser);
            LinearLayout score3layout = findViewById(R.id.score3Container);

            switch (checkedId) {
                case R.id.singlesMatchSelector: // Singles match selected
                    singlesMatch = true;
                    // Remove 2 players
                    team1player2chooser.setVisibility(View.GONE);
                    team2player2chooser.setVisibility(View.GONE);

                    // Change player hint
                    team1player1chooser.setHint(R.string.player1hint);
                    team2player1chooser.setHint(R.string.player2hint);


                    // Reset every edittext
                    team1player1chooser.setText("");
                    team1player2chooser.setText("");
                    team2player1chooser.setText("");
                    team2player2chooser.setText("");
                    score1team1editText.setText("");
                    score1team2editText.setText("");
                    score2team1editText.setText("");
                    score2team2editText.setText("");
                    score3team1editText.setText("");
                    score3team2editText.setText("");
                    score3layout.setVisibility(View.GONE);
                    break;
                case R.id.doublesMatchSelector: // Doubles match selected
                    singlesMatch = false;
                    // Add 2 players
                    team1player2chooser.setVisibility(View.VISIBLE);
                    team2player2chooser.setVisibility(View.VISIBLE);

                    // Change player hint
                    team1player1chooser.setHint(R.string.team1Player1hint);
                    team1player2chooser.setHint(R.string.team1Player2hint);
                    team2player1chooser.setHint(R.string.team2Player1hint);
                    team2player2chooser.setHint(R.string.team2Player2hint);

                    // Reset every edittext
                    team1player1chooser.setText("");
                    team1player2chooser.setText("");
                    team2player1chooser.setText("");
                    team2player2chooser.setText("");
                    score1team1editText.setText("");
                    score1team2editText.setText("");
                    score2team1editText.setText("");
                    score2team2editText.setText("");
                    score3team1editText.setText("");
                    score3team2editText.setText("");
                    score3layout.setVisibility(View.GONE);
                    break;
            }

            // Reset error text
            resetErrorText();

            // Clear Focus
            clearFocus();
        });
        if (Settings.getDefaultMatchType().equals("Singles")) {
            radioGroup.check(R.id.singlesMatchSelector);
        } else {
            radioGroup.check(R.id.doublesMatchSelector);
        }

        CustomEditText.KeyImeChange keyImeChangeListener = (keyCode, event) -> {
            if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
                clearFocus();
            }
            closeActivity = false;
        };

        score1team1editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score1team1editText, score1team2editText, this)});
        score1team1editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                score1team1editText.clearFocus();
            }
            return false;
        });
        score1team1editText.setOnFocusChangeListener((view, hasFocus) -> {
            int scoreThis = -1;
            int scoreOther = -1;
            if (hasFocus) {
                // Reset errorText
                resetErrorText();
                if (!score1team2editText.getText().toString().isEmpty()) {
                    try {
                        scoreOther = Integer.parseInt(score1team2editText.getText().toString());
                        scoreThis = Integer.parseInt(score1team1editText.getText().toString());
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                    if (scoreOther < 20 && scoreThis == -1 && Settings.autocompleteScore()) {
                        score1team1editText.setText("21");
                        clearFocus();
                    }
                }
            } else showThirdSet();
        });
        score1team1editText.setKeyImeChangeListener(keyImeChangeListener);

        score1team2editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score1team2editText, score1team1editText, this)});
        score1team2editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                score1team2editText.clearFocus();
            }
            return false;
        });
        score1team2editText.setOnFocusChangeListener((view, hasFocus) -> {
            int scoreThis = -1;
            int scoreOther = -1;
            if (hasFocus) {
                // Reset errorText
                resetErrorText();
                if (!score1team1editText.getText().toString().isEmpty()) {
                    try {
                        scoreOther = Integer.parseInt(score1team1editText.getText().toString());
                        scoreThis = Integer.parseInt(score1team2editText.getText().toString());
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                    if (scoreOther < 20 && scoreThis == -1 && Settings.autocompleteScore()) {
                        score1team2editText.setText("21");
                        clearFocus();
                    }
                }
            } else showThirdSet();
        });
        score1team2editText.setKeyImeChangeListener(keyImeChangeListener);

        score2team1editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score2team1editText, score2team2editText, this)});
        score2team1editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                score2team1editText.clearFocus();
            }
            return false;
        });
        score2team1editText.setOnFocusChangeListener((view, hasFocus) -> {
            int scoreThis = -1;
            int scoreOther = -1;
            if (hasFocus) {
                // Reset errorText
                resetErrorText();
                if (!score2team2editText.getText().toString().isEmpty()) {
                    try {
                        scoreOther = Integer.parseInt(score2team2editText.getText().toString());
                        scoreThis = Integer.parseInt(score2team1editText.getText().toString());
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                    if (scoreOther < 20 && scoreThis == -1 && Settings.autocompleteScore()) {
                        score2team1editText.setText("21");
                        clearFocus();
                    }
                }
            } else showThirdSet();
        });
        score2team1editText.setKeyImeChangeListener(keyImeChangeListener);

        score2team2editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score2team2editText, score2team1editText, this)});
        score2team2editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                score2team2editText.clearFocus();
            }
            return false;
        });
        score2team2editText.setOnFocusChangeListener((view, hasFocus) -> {
            int scoreThis = -1;
            int scoreOther = -1;
            if (hasFocus) {
                // Reset errorText
                resetErrorText();
                if (!score2team1editText.getText().toString().isEmpty()) {
                    try {
                        scoreOther = Integer.parseInt(score2team1editText.getText().toString());
                        scoreThis = Integer.parseInt(score2team2editText.getText().toString());
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                    if (scoreOther < 20 && scoreThis == -1 && Settings.autocompleteScore()) {
                        score2team2editText.setText("21");
                        clearFocus();
                    }
                }
            } else showThirdSet();
        });
        score2team2editText.setKeyImeChangeListener(keyImeChangeListener);

        score3team1editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score3team1editText, score3team2editText, this)});
        score3team1editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                score3team1editText.clearFocus();
            }
            return false;
        });
        score3team1editText.setOnFocusChangeListener((view, hasFocus) -> {
            int scoreThis = -1;
            int scoreOther = -1;
            if (hasFocus) {
                // Reset errorText
                resetErrorText();
                if (!score3team2editText.getText().toString().isEmpty()) {
                    try {
                        scoreOther = Integer.parseInt(score3team2editText.getText().toString());
                        scoreThis = Integer.parseInt(score3team1editText.getText().toString());
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                    if (scoreOther < 20 && scoreThis == -1 && Settings.autocompleteScore()) {
                        score3team1editText.setText("21");
                        clearFocus();
                    }
                }
            }
        });
        score3team1editText.setKeyImeChangeListener(keyImeChangeListener);

        score3team2editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score3team2editText, score3team1editText, this)});
        score3team2editText.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                score3team2editText.clearFocus();
            }
            return false;
        });
        score3team2editText.setOnFocusChangeListener((view, hasFocus) -> {
            int scoreThis = -1;
            int scoreOther = -1;
            if (hasFocus) {
                // Reset errorText
                resetErrorText();
                if (!score3team1editText.getText().toString().isEmpty()) {
                    try {
                        scoreOther = Integer.parseInt(score3team1editText.getText().toString());
                        scoreThis = Integer.parseInt(score3team2editText.getText().toString());
                    } catch (NumberFormatException nfe) {
                        // Ignore
                    }
                    if (scoreOther < 20 && scoreThis == -1 && Settings.autocompleteScore()) {
                        score3team2editText.setText("21");
                        clearFocus();
                    }
                }
            }
        });
        score3team2editText.setKeyImeChangeListener(keyImeChangeListener);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {

            EditText s1 = findViewById(R.id.score1Team1);
            EditText s2 = findViewById(R.id.score1Team2);
            EditText s3 = findViewById(R.id.score2Team1);
            EditText s4 = findViewById(R.id.score2Team2);
            EditText s5 = findViewById(R.id.score3Team1);
            EditText s6 = findViewById(R.id.score3Team2);

            score1Team1 = !s1.getText().toString().isEmpty();
            score1Team2 = !s2.getText().toString().isEmpty();
            score2Team1 = !s3.getText().toString().isEmpty();
            score2Team2 = !s4.getText().toString().isEmpty();
            score3Team1 = !s5.getText().toString().isEmpty();
            score3Team2 = !s6.getText().toString().isEmpty();

            boolean scoreValid = true;

            int firstScore = -1, secondScore = -1, thirdScore = -1, fourthScore = -1, fifthScore = -1, sixthScore = -1;
            try {
                if (score1Team1 && score1Team2 && score2Team1 && score2Team2)
                    firstScore = Integer.parseInt(s1.getText().toString());
                secondScore = Integer.parseInt(s2.getText().toString());
                thirdScore = Integer.parseInt(s3.getText().toString());
                fourthScore = Integer.parseInt(s4.getText().toString());
                if (score3Team1 && score3Team2) {
                    fifthScore = Integer.parseInt(s5.getText().toString());
                    sixthScore = Integer.parseInt(s6.getText().toString());
                }
            } catch (NumberFormatException nfe) {
                // score field are empty or have invalid value
            }

            if (firstScore == -1 || secondScore == -1 || thirdScore == -1 || fourthScore == -1
                    || ((score3Team1 && score3Team2) && (fifthScore == -1 || sixthScore == -1))
                    || !InputFilterScore.checkScore(firstScore, secondScore)
                    || !InputFilterScore.checkScore(thirdScore, fourthScore)
                    || ((score3Team1 || score3Team2) && !InputFilterScore.checkScore(fifthScore, sixthScore))) {
                scoreValid = false;
            }

            if ((score3Team1 && score3Team2) && firstScore > secondScore && thirdScore > fourthScore
                    || (score3Team1 && score3Team2) && firstScore < secondScore && thirdScore < fourthScore
                    || !(score3Team1 && score3Team2) && firstScore < secondScore && thirdScore > fourthScore
                    || !(score3Team1 && score3Team2) && firstScore > secondScore && thirdScore < fourthScore) {
                scoreValid = false;
            }

            String errorMessage = "";
            TextView errorText = findViewById(R.id.errorText);

            if (singlesMatch) {
                if(validatePlayer(team1player1chooser.getText().toString())
                        && validatePlayer(team2player1chooser.getText().toString())
                        && scoreValid) {
                    errorText.setText("");

                    long[] playerIDs = new long[2];
                    String text;
                    String playerID = "";
                    for(int j = 0; j < 2; j++) {
                        if(j == 0){
                            text = team1player1chooser.getText().toString();
                        } else{
                            text = team2player1chooser.getText().toString();
                        }
                        for (int i = 0; i < text.length(); i++) {
                            if (text.charAt(i) == '#') {
                                for (int k = i+2; k < text.length(); k++) {
                                    playerID = playerID + text.charAt(k);
                                }

                                playerIDs[j] = Long.parseLong(playerID);

                                playerID = "";
                                break;
                            }
                        }
                    }
                    String s = s1.getText() + ":" + s2.getText();
                    String[] scores = new String[2];
                    if (score3Team1 && score3Team2) {
                        scores = new String[3];
                    }
                    scores[0] = s;
                    s = s3.getText() + ":" + s4.getText();
                    scores[1] = s;
                    if (score3Team1 && score3Team2) {
                        s = s5.getText() + ":" + s6.getText();
                        scores[2] = s;
                    }

                    // Update player-profiles
                    int winnerTeam;
                    int idx;

                    if(scores.length == 3) idx = 2;
                    else idx = 1;

                    int a = Integer.parseInt(scores[idx].substring(0,scores[idx].indexOf(':')));
                    int b = Integer.parseInt(scores[idx].substring(scores[idx].indexOf(':')+1));

                    if(a > b) winnerTeam = 1;
                    else winnerTeam = 2;

                    Player buffer;
                    int[] pointsOfTeam = new int[2];
                    for(int i = 0; i < 2; i++){
                        buffer = storage.getPlayerData(playerIDs[i]);
                        // Increment match count
                        buffer.setMatchesPlayed(buffer.getMatchesPlayed()+1);
                        // Points
                        pointsOfTeam[i] = buffer.getRankingPoints();
                        storage.storePlayer(buffer);
                    }
                    Player winner;
                    int pointsWon;
                    double helper;
                    if(winnerTeam == 1){
                        winner = storage.getPlayerData(playerIDs[0]);
                        if(pointsOfTeam[0] == 0){
                            helper = 5;
                        } else if(pointsOfTeam[1] == 0){
                            helper = 5 * (1.0/pointsOfTeam[0]);
                        } else{
                            helper = 5 * (1.0*pointsOfTeam[1]/pointsOfTeam[0]);
                        }
                    } else{
                        winner = storage.getPlayerData(playerIDs[1]);
                        if(pointsOfTeam[1] == 0){
                            helper = 5;
                        } else if(pointsOfTeam[0] == 0){
                            helper = 5 * (1.0/pointsOfTeam[1]);
                        } else{
                            helper = 5 * (1.0*pointsOfTeam[0]/pointsOfTeam[1]);
                        }
                    }
                    helper = Math.round(helper);
                    pointsWon = (int) helper;
                    winner.setRankingPoints(winner.getRankingPoints() + pointsWon);
                    storage.storePlayer(winner);

                    int[] points = new int[2];
                    if(winnerTeam == 1){
                        points[0] = pointsWon;
                    } else{
                        points[1] = pointsWon;
                    }

                    matchID = getNextMatchID();

                    Match matchNew = new Match(storage, matchID, 'S', playerIDs, scores.length, scores, points);
                    storage.storeMatch(matchNew);

                    storage.registerMatchID(matchID);
                    StatsActivity.showInfo(matchNew);
                    finish();
                } else {
                    if (!validatePlayer(team1player1chooser.getText().toString())) errorMessage = errorMessage + "Player 1 missing \n ";
                    if (!validatePlayer(team2player1chooser.getText().toString())) errorMessage = errorMessage + "Player 2 missing \n ";
                    if (!scoreValid) errorMessage = errorMessage + "Score invalid \n";

                    errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
                    errorText.setText(errorMessage);
                }
            } else {
                if(validatePlayer(team1player1chooser.getText().toString())
                        && validatePlayer(team1player2chooser.getText().toString())
                        && validatePlayer(team2player1chooser.getText().toString())
                        && validatePlayer(team2player2chooser.getText().toString())
                        && scoreValid){

                    errorText.setText("");

                    long[] playerIDs = new long[4];
                    String text;
                    String playerID = "";
                    for(int j = 0; j < 4; j++) {
                        if(j == 0){
                            text = team1player1chooser.getText().toString();
                        } else if(j == 1){
                            text = team1player2chooser.getText().toString();
                        } else if(j == 2){
                            text = team2player1chooser.getText().toString();
                        } else{
                            text = team2player2chooser.getText().toString();
                        }
                        for (int i = 0; i < text.length(); i++) {
                            if (text.charAt(i) == '#') {
                                for (int k = i+2; k < text.length(); k++) {
                                    playerID = playerID + text.charAt(k);
                                }

                                playerIDs[j] = Long.parseLong(playerID);

                                playerID = "";
                                break;
                            }
                        }
                    }
                    String s = s1.getText() + ":" + s2.getText();
                    String[] scores = new String[2];
                    if (score3Team1 && score3Team2) {
                        scores = new String[3];
                    }
                    scores[0] = s;
                    s = s3.getText() + ":" + s4.getText();
                    scores[1] = s;
                    if (score3Team1 && score3Team2) {
                        s = s5.getText() + ":" + s6.getText();
                        scores[2] = s;
                    }

                    // Update player-profiles
                    int winnerTeam;
                    int idx;

                    if(scores.length == 3) idx = 2;
                    else idx = 1;

                    int a = Integer.parseInt(scores[idx].substring(0,scores[idx].indexOf(':')));
                    int b = Integer.parseInt(scores[idx].substring(scores[idx].indexOf(':')+1));

                    if(a > b) winnerTeam = 1;
                    else winnerTeam = 2;

                    Player buffer;
                    int[] pointsOfTeam = new int[2];
                    for(int i = 0; i < 4; i++){
                        buffer = storage.getPlayerData(playerIDs[i]);
                        // Increment match count
                        buffer.setMatchesPlayed(buffer.getMatchesPlayed()+1);
                        // Points
                        if(i == 0 || i == 1) {
                            pointsOfTeam[0] = pointsOfTeam[0] + buffer.getRankingPoints();
                        } else {
                            pointsOfTeam[1] = pointsOfTeam[1] + buffer.getRankingPoints();
                        }
                        storage.storePlayer(buffer);
                    }
                    Player[] winners = new Player[2];
                    int pointsWon;
                    double helper;
                    if(winnerTeam == 1){
                        winners[0] = storage.getPlayerData(playerIDs[0]);
                        winners[1] = storage.getPlayerData(playerIDs[1]);
                        if(pointsOfTeam[0] == 0){
                            helper = 5;
                        } else if(pointsOfTeam[1] == 0){
                            helper = 5 * (1.0/pointsOfTeam[0]);
                        } else{
                            helper = 5 * (1.0*pointsOfTeam[1]/pointsOfTeam[0]);
                        }
                    } else{
                        winners[0] = storage.getPlayerData(playerIDs[2]);
                        winners[1] = storage.getPlayerData(playerIDs[3]);
                        if(pointsOfTeam[1] == 0){
                            helper = 5;
                        } else if(pointsOfTeam[0] == 0){
                            helper = 5 * (1.0/pointsOfTeam[1]);
                        } else{
                            helper = 5 * (1.0*pointsOfTeam[0]/pointsOfTeam[1]);
                        }
                    }
                    helper = Math.round(helper);
                    pointsWon = (int) helper;
                    winners[0].setRankingPoints(winners[0].getRankingPoints() + pointsWon);
                    winners[1].setRankingPoints(winners[1].getRankingPoints() + pointsWon);
                    storage.storePlayer(winners[0]);
                    storage.storePlayer(winners[1]);

                    int[] points = new int[4];
                    if(winnerTeam == 1){
                        points[0] = pointsWon;
                        points[1] = pointsWon;
                        points[2] = 0;
                        points[3] = 0;
                    } else{
                        points[0] = 0;
                        points[1] = 0;
                        points[2] = pointsWon;
                        points[3] = pointsWon;
                    }
                    matchID = getNextMatchID();

                    Match matchNew = new Match(storage, matchID, 'D', playerIDs, scores.length, scores, points);
                    storage.storeMatch(matchNew);

                    storage.registerMatchID(matchID);
                    StatsActivity.showInfo(matchNew);
                    finish();
                } else {
                    if (!validatePlayer(team1player1chooser.getText().toString())) errorMessage = errorMessage + "Team 1 Player 1 missing \n";
                    if (!validatePlayer(team1player2chooser.getText().toString())) errorMessage = errorMessage + "Team 1 Player 2 missing \n";
                    if (!validatePlayer(team2player1chooser.getText().toString())) errorMessage = errorMessage + "Team 2 Player 1 missing \n";
                    if (!validatePlayer(team2player2chooser.getText().toString())) errorMessage = errorMessage + "Team 2 Player 2 missing \n";
                    if (!scoreValid) errorMessage = errorMessage + "Score invalid \n";

                    errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
                    errorText.setText(errorMessage);
                }
            }
            clearFocus();
        });

        RelativeLayout container = findViewById(R.id.registerMatchContainer);
        container.setOnClickListener(view -> {
            clearFocus();
        });

        matchID = storage.getLastMatchID();

    }

    @Override
    public void onBackPressed() {
        if(closeActivity) {
            super.onBackPressed();
        }
        closeActivity = true;
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        clearFocus();
        resetErrorText();
        super.onConfigurationChanged(newConfig);
    }

    private void showThirdSet() {
        LinearLayout score3layout = findViewById(R.id.score3Container);
        if (score1team1editText.getText().length() != 0
                && score1team2editText.getText().length() != 0
                && score2team1editText.getText().length() != 0
                && score2team2editText.getText().length() != 0) {
            try {
                int score11 = Integer.parseInt(score1team1editText.getText().toString());
                int score12 = Integer.parseInt(score1team2editText.getText().toString());
                int score21 = Integer.parseInt(score2team1editText.getText().toString());
                int score22 = Integer.parseInt(score2team2editText.getText().toString());
                if ((score11 < score12 && score21 > score22
                        || score11 > score12 && score21 < score22)
                        && InputFilterScore.checkScore(score11, score12)
                        && InputFilterScore.checkScore(score21, score22)) {
                    score3layout.setVisibility(View.VISIBLE);
                } else {
                    score3team1editText.setText("");
                    score3team2editText.setText("");
                    score3layout.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                //ignored
            }

        } else {
            score3layout.setVisibility(View.GONE);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, 0);
    }

    public void clearFocus() {
        team1player1chooser.clearFocus();
        team1player2chooser.clearFocus();
        team2player1chooser.clearFocus();
        team2player2chooser.clearFocus();
        score1team1editText.clearFocus();
        score1team2editText.clearFocus();
        score2team1editText.clearFocus();
        score2team2editText.clearFocus();
        score3team1editText.clearFocus();
        score3team2editText.clearFocus();
        hideKeyboard(container);
    }

    private void resetErrorText() {
        TextView errorText = findViewById(R.id.errorText);
        errorText.setText("");
    }

    private boolean validatePlayer(String entry){
        Arrays.sort(players);
        return Arrays.binarySearch(players, entry) >= 0;
    }

    private long getNextMatchID(){
        long next = matchID+1;
        while(!isValidID(next, Storage.MATCH_ID_DIGITS)){
            next++;
            if(next == Math.pow(10, Storage.MATCH_ID_DIGITS)){
                return storage.getNextFreeMatchID();
            }
        }
        return next;
    }

    public static boolean isValidID(long ID, int length){
        String s = String.valueOf(ID);
        if(s.length() != length) return false;
        char last = s.charAt(0), current;
        for(int i = 1; i < s.length(); i++){
            current = s.charAt(i);
            if(last == current
                    || Math.abs(last-current) == 1){
                return false;
            }
            last = current;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}