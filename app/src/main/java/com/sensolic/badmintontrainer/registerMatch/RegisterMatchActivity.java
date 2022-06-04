package com.sensolic.badmintontrainer.registerMatch;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Storage;

public class RegisterMatchActivity extends AppCompatActivity {

    private static long matchID;
    static boolean singlesMatch = true;
    boolean completed = false;
    boolean player1 = false;
    boolean player2 = false;
    boolean player3 = false;
    boolean player4 = false;
    boolean score1Team1 = false;
    boolean score1Team2 = false;
    boolean score2Team1 = false;
    boolean score2Team2 = false;
    boolean score3Team1 = false;
    boolean score3Team2 = false;
    CustomEditText player1editText;
    CustomEditText player2editText;
    CustomEditText player3editText;
    CustomEditText player4editText;
    CustomEditText score1team1editText;
    CustomEditText score1team2editText;
    CustomEditText score2team1editText;
    CustomEditText score2team2editText;
    CustomEditText score3team1editText;
    CustomEditText score3team2editText;
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_match);

        // Setup edittext references
        player1editText = findViewById(R.id.player1editText);
        player2editText = findViewById(R.id.player2editText);
        player3editText = findViewById(R.id.player3editText);
        player4editText = findViewById(R.id.player4editText);
        score1team1editText = findViewById(R.id.score1Team1);
        score1team2editText = findViewById(R.id.score1Team2);
        score2team1editText = findViewById(R.id.score2Team1);
        score2team2editText = findViewById(R.id.score2Team2);
        score3team1editText = findViewById(R.id.score3Team1);
        score3team2editText = findViewById(R.id.score3Team2);


        RadioGroup radioGroup = findViewById(R.id.matchTypeSelector);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TextInputLayout team1player1 = findViewById(R.id.team1player1);
                TextInputLayout team1player2 = findViewById(R.id.team1player2);
                TextInputLayout team2player1 = findViewById(R.id.team2player1);
                TextInputLayout team2player2 = findViewById(R.id.team2player2);
                TextView text;
                EditText editText;
                LinearLayout score3layout = findViewById(R.id.score3Container);

                switch (checkedId) {
                    case R.id.singlesMatchSelector: // Singles match selected
                        singlesMatch = true;
                        // Remove 2 players
                        team1player2.setVisibility(View.GONE);
                        team2player2.setVisibility(View.GONE);

                        // Change player hint
                        team1player1.setHint(R.string.player1hint);
                        team2player1.setHint(R.string.player2hint);

                        // Reset every edittext
                        player1editText.setText("");
                        player2editText.setText("");
                        player3editText.setText("");
                        player4editText.setText("");
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
                        team1player2.setVisibility(View.VISIBLE);
                        team2player2.setVisibility(View.VISIBLE);

                        // Change player hint
                        team1player1.setHint(R.string.team1Player1hint);
                        team1player2.setHint(R.string.team1Player2hint);
                        team2player1.setHint(R.string.team2Player1hint);
                        team2player2.setHint(R.string.team2Player2hint);

                        // Reset every edittext
                        player1editText.setText("");
                        player2editText.setText("");
                        player3editText.setText("");
                        player4editText.setText("");
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
                hideKeyboard(group);
            }
        });

        RadioButton singlesMatchSelector = findViewById(R.id.singlesMatchSelector);
        singlesMatchSelector.setChecked(true);

        CustomEditText.KeyImeChange keyImeChangeListener = new CustomEditText.KeyImeChange() {
            @Override
            public void onKeyIme(int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
                    clearFocus();
                }
            }
        };
        View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus) resetErrorText();
            }
        };

        player1editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        player1editText.setKeyImeChangeListener(keyImeChangeListener);
        player1editText.setOnFocusChangeListener(focusChangeListener);

        player2editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        player2editText.setKeyImeChangeListener(keyImeChangeListener);
        player2editText.setOnFocusChangeListener(focusChangeListener);

        player3editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        player3editText.setKeyImeChangeListener(keyImeChangeListener);
        player3editText.setOnFocusChangeListener(focusChangeListener);

        player4editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        player4editText.setKeyImeChangeListener(keyImeChangeListener);
        player4editText.setOnFocusChangeListener(focusChangeListener);

        score1team1editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score1team1editText, score1team2editText)});
        score1team1editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    score1team1editText.clearFocus();
                }
                return false;
            }
        });
        score1team1editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
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
                            score1team2editText.clearFocus();
                        }
                    }
                } else showThirdSet();
            }
        });
        score1team1editText.setKeyImeChangeListener(keyImeChangeListener);

        score1team2editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score1team2editText, score1team1editText)});
        score1team2editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    score1team2editText.clearFocus();
                }
                return false;
            }
        });
        score1team2editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
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
                            score1team2editText.clearFocus();

                        }
                    }
                } else showThirdSet();
            }
        });
        score1team2editText.setKeyImeChangeListener(keyImeChangeListener);

        score2team1editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score2team1editText, score2team2editText)});
        score2team1editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    score2team1editText.clearFocus();
                }
                return false;
            }
        });
        score2team1editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
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
                            score2team2editText.clearFocus();
                        }
                    }
                } else showThirdSet();
            }
        });
        score2team1editText.setKeyImeChangeListener(keyImeChangeListener);

        score2team2editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score2team2editText, score2team1editText)});
        score2team2editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    score2team2editText.clearFocus();
                }
                return false;
            }
        });
        score2team2editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
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
                            score2team2editText.clearFocus();

                        }
                    }
                } else showThirdSet();
            }
        });
        score2team2editText.setKeyImeChangeListener(keyImeChangeListener);

        score3team1editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score3team1editText, score3team2editText)});
        score3team1editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    score3team1editText.clearFocus();
                }
                return false;
            }
        });
        score3team1editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
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
                            score3team2editText.clearFocus();
                        }
                    }
                }
            }
        });
        score3team1editText.setKeyImeChangeListener(keyImeChangeListener);

        score3team2editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score3team2editText, score3team1editText)});
        score3team2editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    score3team2editText.clearFocus();
                }
                return false;
            }
        });
        score3team2editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
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
                            score3team2editText.clearFocus();

                        }
                    }
                }
            }
        });
        score3team2editText.setKeyImeChangeListener(keyImeChangeListener);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText p1 = findViewById(R.id.player1editText);
                EditText p2 = findViewById(R.id.player2editText);
                EditText p3 = findViewById(R.id.player3editText);
                EditText p4 = findViewById(R.id.player4editText);
                EditText s1 = findViewById(R.id.score1Team1);
                EditText s2 = findViewById(R.id.score1Team2);
                EditText s3 = findViewById(R.id.score2Team1);
                EditText s4 = findViewById(R.id.score2Team2);
                EditText s5 = findViewById(R.id.score3Team1);
                EditText s6 = findViewById(R.id.score3Team2);

                player1 = !p1.getText().toString().isEmpty();
                player2 = !p2.getText().toString().isEmpty();
                player3 = !p3.getText().toString().isEmpty();
                player4 = !p4.getText().toString().isEmpty();
                score1Team1 = !s1.getText().toString().isEmpty();
                score1Team2 = !s2.getText().toString().isEmpty();
                score2Team1 = !s3.getText().toString().isEmpty();
                score2Team2 = !s4.getText().toString().isEmpty();
                score3Team1 = !s5.getText().toString().isEmpty();
                score3Team2 = !s6.getText().toString().isEmpty();

                boolean scoreValid = true;

                int firstScore = -1, secondScore = -1, thirdScore = -1, fourthScore = -1, fifthScore = -1, sixthScore = -1;
                try {
                    if(score1Team1 && score1Team2 && score2Team1 && score2Team2)
                    firstScore = Integer.parseInt(s1.getText().toString());
                    secondScore = Integer.parseInt(s2.getText().toString());
                    thirdScore = Integer.parseInt(s3.getText().toString());
                    fourthScore = Integer.parseInt(s4.getText().toString());
                    if(score3Team1 && score3Team2) {
                        fifthScore = Integer.parseInt(s5.getText().toString());
                        sixthScore = Integer.parseInt(s6.getText().toString());
                    }
                } catch (NumberFormatException nfe) {
                    // score field are empty or have invalid value
                }

                if(firstScore == -1 || secondScore == -1 || thirdScore == -1 || fourthScore == -1
                        || ((score3Team1 && score3Team2) && (fifthScore == -1 || sixthScore == -1))
                        || !InputFilterScore.checkScore(firstScore, secondScore)
                        || !InputFilterScore.checkScore(thirdScore, fourthScore)
                        || ((score3Team1 || score3Team2) && !InputFilterScore.checkScore(fifthScore, sixthScore))){
                    scoreValid = false;
                }

                if((score3Team1 && score3Team2) && firstScore > secondScore && thirdScore > fourthScore
                        || (score3Team1 && score3Team2) && firstScore < secondScore && thirdScore < fourthScore
                        || !(score3Team1 && score3Team2) && firstScore < secondScore && thirdScore > fourthScore
                        || !(score3Team1 && score3Team2) && firstScore > secondScore && thirdScore < fourthScore){
                    scoreValid = false;
                }

                String errorMessage = "";
                TextView errorText = findViewById(R.id.errorText);

                if (singlesMatch) {
                    if (player1 && player3 && scoreValid) {
                        errorText.setText("");

                        long[] playerIDs = new long[]{1L,2L};
                        String s = s1.getText() + ":" + s2.getText();
                        String[] scores = new String[2];
                        if(score3Team1 && score3Team2){
                            scores = new String[3];
                        }
                        scores[0] = s;
                        s = s3.getText() + ":" + s4.getText();
                        scores[1] = s;
                        if(score3Team1 && score3Team2){
                            s = s5.getText() + ":" + s6.getText();
                            scores[2] = s;
                        }
                        Match matchNew = new Match(matchID, 'S', playerIDs, scores.length, scores);
                        storage.storeMatch(matchNew);
                        matchID++;
                        storage.setCurrentMatchID(matchID);
                        Toast.makeText(getApplicationContext(), "Successfully created match", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (!player1) errorMessage = errorMessage + "Player 1 missing \n ";
                        if (!player3) errorMessage = errorMessage + "Player 2 missing \n ";
                        if (!scoreValid) errorMessage = errorMessage + "Score invalid \n";

                        errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
                        errorText.setText(errorMessage);
                    }
                } else {
                    if (player1 && player2 && player3 && player4 && scoreValid) {
                        errorText.setText("");

                        long[] playerIDs = new long[]{1L,2L,3L,4L};
                        String s = s1.getText() + ":" + s2.getText();
                        String[] scores = new String[2];
                        if(score3Team1 && score3Team2){
                            scores = new String[3];
                        }
                        scores[0] = s;
                        s = s3.getText() + ":" + s4.getText();
                        scores[1] = s;
                        if(score3Team1 && score3Team2){
                            s = s5.getText() + ":" + s6.getText();
                            scores[2] = s;
                        }
                        Match matchNew = new Match(matchID, 'D', playerIDs, scores.length, scores);
                        storage.storeMatch(matchNew);
                        matchID++;
                        storage.setCurrentMatchID(matchID);
                        Toast.makeText(getApplicationContext(), "Successfully created match", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (!player1) errorMessage = errorMessage + "Team 1 Player 1 missing \n";
                        if (!player2) errorMessage = errorMessage + "Team 1 Player 2 missing \n";
                        if (!player3) errorMessage = errorMessage + "Team 2 Player 1 missing \n";
                        if (!player4) errorMessage = errorMessage + "Team 2 Player 2 missing \n";
                        if(!scoreValid) errorMessage = errorMessage + "Score invalid \n";

                        errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
                        errorText.setText(errorMessage);
                    }
                }
                clearFocus();
                hideKeyboard(view);
            }
        });

        RelativeLayout container = findViewById(R.id.registerMatchContainer);
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearFocus();
                hideKeyboard(view);
            }
        });

        storage = Storage.getInstance(getApplicationContext());
        matchID = storage.getCurrentMatchID();

    }

    private void showThirdSet(){
        LinearLayout score3layout = findViewById(R.id.score3Container);
        if(score1team1editText.getText().length() != 0
                &&score1team2editText.getText().length() != 0
                &&score2team1editText.getText().length() != 0
                &&score2team2editText.getText().length() != 0){
            try{
                int score11 = Integer.parseInt(score1team1editText.getText().toString());
                int score12 = Integer.parseInt(score1team2editText.getText().toString());
                int score21 = Integer.parseInt(score2team1editText.getText().toString());
                int score22 = Integer.parseInt(score2team2editText.getText().toString());
                if((score11 < score12 && score21 > score22
                        || score11 > score12 && score21 < score22)
                        && InputFilterScore.checkScore(score11,score12)
                        && InputFilterScore.checkScore(score21,score22)){
                    score3layout.setVisibility(View.VISIBLE);
                } else{
                    score3team1editText.setText("");
                    score3team2editText.setText("");
                    score3layout.setVisibility(View.GONE);
                }
            } catch (Exception e){
                //ignored
            }

        } else{
            score3layout.setVisibility(View.GONE);
        }
    }

    private void hideKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void clearFocus() {
        player1editText.clearFocus();
        player2editText.clearFocus();
        player3editText.clearFocus();
        player4editText.clearFocus();
        score1team1editText.clearFocus();
        score1team2editText.clearFocus();
        score2team1editText.clearFocus();
        score2team2editText.clearFocus();
        score3team1editText.clearFocus();
        score3team2editText.clearFocus();
    }

    private void resetErrorText(){
        TextView errorText  = findViewById(R.id.errorText);
        errorText.setText("");
    }
}