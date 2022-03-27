package com.sensolic.badmintontrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class RegisterMatchActivity extends AppCompatActivity {

    static boolean singlesMatch = true;
    boolean completed = false;
    boolean player1 = false;
    boolean player2 = false;
    boolean player3 = false;
    boolean player4 = false;
    boolean score1 = false;
    boolean score2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_match);

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

                        // Reset invisible Edittext
                        editText = findViewById(R.id.player2editText);
                        editText.setText("");
                        editText = findViewById(R.id.player4editText);
                        editText.setText("");
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
                        break;
                }
            }
        });

        RadioButton singlesMatchSelector = findViewById(R.id.singlesMatchSelector);
        singlesMatchSelector.setChecked(true);

        EditText player1editText = findViewById(R.id.player1editText);
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

        EditText player2editText = findViewById(R.id.player2editText);
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

        EditText player3editText = findViewById(R.id.player3editText);
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

        EditText player4editText = findViewById(R.id.player4editText);
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

        EditText score1editText  = findViewById(R.id.scoreTeam1);
        EditText score2editText = findViewById(R.id.scoreTeam2);

        score1editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score1editText,score2editText)});
        score1editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int scoreThis = -1;
                int scoreOther = -1;
                if(b){
                    if(!score2editText.getText().toString().isEmpty()){
                        try{
                            scoreOther = Integer.parseInt(score2editText.getText().toString());
                            scoreThis = Integer.parseInt(score1editText.getText().toString());
                        } catch (NumberFormatException nfe){
                            // Ignore
                        }
                        if(scoreOther < 20 && scoreThis == -1){
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

        score2editText.setFilters(new InputFilter[]{new InputFilterScore(0, 30, score2editText, score1editText)});
        score2editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int scoreThis = -1;
                int scoreOther = -1;
                if(b){
                    if(!score1editText.getText().toString().isEmpty()){
                        try{
                            scoreOther = Integer.parseInt(score1editText.getText().toString());
                            scoreThis = Integer.parseInt(score2editText.getText().toString());
                        } catch (NumberFormatException nfe){
                            // Ignore
                        }
                        if(scoreOther < 20 && scoreThis == -1){
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
                try{
                    firstScore = Integer.parseInt(s1.getText().toString());
                    secondScore = Integer.parseInt(s2.getText().toString());
                } catch (NumberFormatException nfe){
                    // score field are empty or have invalid value
                }
                scoreValid = InputFilterScore.checkScore(firstScore, secondScore);

                String errorMessage = "";
                TextView errorText = findViewById(R.id.errorText);

                if(singlesMatch){
                    if(player1 && player3 && score1 && score2 && scoreValid){
                        errorText.setText("");
                        Toast.makeText(getApplicationContext(), "Successfully created match",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if(!player1) errorMessage = errorMessage + "Player 1 missing, ";
                        if(!player3) errorMessage = errorMessage + "Player 2 missing, ";
                        if(!score1 || !score2) errorMessage = errorMessage + "Score missing, ";
                        else if(!scoreValid) errorMessage = errorMessage + "Score invalid, ";

                        errorMessage = errorMessage.substring(0,errorMessage.length()-2);
                        errorText.setText(errorMessage);
                    }
                } else{
                    if(player1 && player2 && player3 && player4 && score1 && score2){
                        errorText.setText("");
                        Toast.makeText(getApplicationContext(), "Successfully created match",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if(!player1) errorMessage = errorMessage + "Team 1 Player 1 missing, ";
                        if(!player2) errorMessage = errorMessage + "Team 1 Player 2 missing, ";
                        if(!player3) errorMessage = errorMessage + "Team 2 Player 1 missing, ";
                        if(!player4) errorMessage = errorMessage + "Team 2 Player 2 missing, ";
                        if(!score1 || !score2) errorMessage = errorMessage + "Score missing, ";
                        errorMessage = errorMessage.substring(0,errorMessage.length()-2);
                        errorText.setText(errorMessage);
                    }
                }
            }
        });

        LinearLayout container = findViewById(R.id.registerMatchContainer);
        ScrollView scrollView = findViewById(R.id.registerMatchScrollView);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player1editText.clearFocus();
                player2editText.clearFocus();
                player3editText.clearFocus();
                player4editText.clearFocus();
                score1editText.clearFocus();
                score2editText.clearFocus();
            }
        };

        container.setOnClickListener(onClickListener);
        scrollView.setOnClickListener(onClickListener);
    }
}