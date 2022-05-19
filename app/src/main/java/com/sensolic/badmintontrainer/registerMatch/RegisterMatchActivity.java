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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.Storage;

public class RegisterMatchActivity extends AppCompatActivity {

    private static long matchID;
    static boolean singlesMatch = true;
    boolean completed = false;
    boolean player1 = false;
    boolean player2 = false;
    boolean player3 = false;
    boolean player4 = false;
    boolean score1 = false;
    boolean score2 = false;
    CustomEditText player1editText;
    CustomEditText player2editText;
    CustomEditText player3editText;
    CustomEditText player4editText;
    CustomEditText score1editText;
    CustomEditText score2editText;
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
        score1editText = findViewById(R.id.scoreTeam1);
        score2editText = findViewById(R.id.scoreTeam2);


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
                        score1editText.setText("");
                        score2editText.setText("");
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
                        score1editText.setText("");
                        score2editText.setText("");
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

        score1editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score1editText, score2editText)});
        score1editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    score1editText.clearFocus();
                }
                return false;
            }
        });
        score1editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                int scoreThis = -1;
                int scoreOther = -1;
                if (hasFocus) {
                    // Reset errorText
                    resetErrorText();
                    if (!score2editText.getText().toString().isEmpty()) {
                        try {
                            scoreOther = Integer.parseInt(score2editText.getText().toString());
                            scoreThis = Integer.parseInt(score1editText.getText().toString());
                        } catch (NumberFormatException nfe) {
                            // Ignore
                        }
                        if (scoreOther < 20 && scoreThis == -1 && Settings.autocompleteScore()) {
                            score1editText.setText("21");
                            score2editText.clearFocus();
                        }
                    }
                }
            }
        });
        score1editText.addTextChangedListener(new TextWatcher() {
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
        score1editText.setKeyImeChangeListener(keyImeChangeListener);

        score2editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score2editText, score1editText)});
        score2editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    score2editText.clearFocus();
                }
                return false;
            }
        });
        score2editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                int scoreThis = -1;
                int scoreOther = -1;
                if (hasFocus) {
                    // Reset errorText
                    resetErrorText();
                    if (!score1editText.getText().toString().isEmpty()) {
                        try {
                            scoreOther = Integer.parseInt(score1editText.getText().toString());
                            scoreThis = Integer.parseInt(score2editText.getText().toString());
                        } catch (NumberFormatException nfe) {
                            // Ignore
                        }
                        if (scoreOther < 20 && scoreThis == -1 && Settings.autocompleteScore()) {
                            score2editText.setText("21");
                            score2editText.clearFocus();

                        }
                    }
                }
            }
        });
        score2editText.addTextChangedListener(new TextWatcher() {
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
        score2editText.setKeyImeChangeListener(keyImeChangeListener);

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText p1 = findViewById(R.id.player1editText);
                EditText p2 = findViewById(R.id.player2editText);
                EditText p3 = findViewById(R.id.player3editText);
                EditText p4 = findViewById(R.id.player4editText);
                EditText s1 = findViewById(R.id.scoreTeam1);
                EditText s2 = findViewById(R.id.scoreTeam2);

                player1 = !p1.getText().toString().isEmpty();
                player2 = !p2.getText().toString().isEmpty();
                player3 = !p3.getText().toString().isEmpty();
                player4 = !p4.getText().toString().isEmpty();
                score1 = !s1.getText().toString().isEmpty();
                score2 = !s2.getText().toString().isEmpty();

                boolean scoreValid;
                int firstScore = -1, secondScore = -1;
                try {
                    firstScore = Integer.parseInt(s1.getText().toString());
                    secondScore = Integer.parseInt(s2.getText().toString());
                } catch (NumberFormatException nfe) {
                    // score field are empty or have invalid value
                }
                scoreValid = InputFilterScore.checkScore(firstScore, secondScore);

                String errorMessage = "";
                TextView errorText = findViewById(R.id.errorText);

                if (singlesMatch) {
                    if (player1 && player3 && score1 && score2 && scoreValid) {
                        errorText.setText("");
                        storage.saveMatch(String.valueOf(matchID),p1.getText()+" vs "+p3.getText());
                        matchID++;
                        storage.setCurrentMatchID(matchID);
                        Toast.makeText(getApplicationContext(), "Successfully created match", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (!player1) errorMessage = errorMessage + "Player 1 missing \n ";
                        if (!player3) errorMessage = errorMessage + "Player 2 missing \n ";
                        if (!score1 || !score2) errorMessage = errorMessage + "Score missing \n";
                        else if (!scoreValid) errorMessage = errorMessage + "Score invalid \n";

                        errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
                        errorText.setText(errorMessage);
                    }
                } else {
                    if (player1 && player2 && player3 && player4 && score1 && score2 && scoreValid) {
                        errorText.setText("");
                        storage.saveMatch(String.valueOf(matchID),p1.getText()+" & "+p2.getText()+" vs "+p3.getText()+" & "+p4.getText());
                        matchID++;
                        storage.setCurrentMatchID(matchID);
                        Toast.makeText(getApplicationContext(), "Successfully created match", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if (!player1) errorMessage = errorMessage + "Team 1 Player 1 missing \n";
                        if (!player2) errorMessage = errorMessage + "Team 1 Player 2 missing \n";
                        if (!player3) errorMessage = errorMessage + "Team 2 Player 1 missing \n";
                        if (!player4) errorMessage = errorMessage + "Team 2 Player 2 missing \n";
                        if (!score1 || !score2) errorMessage = errorMessage + "Score missing \n";
                        else if(!scoreValid) errorMessage = errorMessage + "Score invalid \n";

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

    private void hideKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void clearFocus() {
        player1editText.clearFocus();
        player2editText.clearFocus();
        player3editText.clearFocus();
        player4editText.clearFocus();
        score1editText.clearFocus();
        score2editText.clearFocus();
    }

    private void resetErrorText(){
        TextView errorText  = findViewById(R.id.errorText);
        errorText.setText("");
    }
}