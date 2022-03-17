package com.sensolic.badmintontrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

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
                LinearLayout team1player1 = findViewById(R.id.team1player1);
                LinearLayout team1player2 = findViewById(R.id.team1player2);
                LinearLayout team2player1 = findViewById(R.id.team2player1);
                LinearLayout team2player2 = findViewById(R.id.team2player2);
                TextView text;
                EditText editText;

                switch (checkedId) {
                    case R.id.singlesMatchSelector: // Singles match selected
                        singlesMatch = true;
                        // Remove 2 players
                        team1player2.setVisibility(View.GONE);
                        team2player2.setVisibility(View.GONE);

                        // Change Label text
                        text = (TextView) team1player1.getChildAt(0);
                        text.setText(R.string.player1label);
                        text = (TextView) team2player1.getChildAt(0);
                        text.setText(R.string.player2label);

                        // Make second team label invisible
                        text = (TextView) team1player2.getChildAt(0);
                        text.setVisibility(View.INVISIBLE);
                        text = (TextView) team2player2.getChildAt(0);
                        text.setVisibility(View.INVISIBLE);

                        // Reset invisible Edittext
                        editText = (EditText) team1player2.getChildAt(2);
                        editText.setText("");
                        editText = (EditText) team2player2.getChildAt(2);
                        editText.setText("");
                        break;
                    case R.id.doublesMatchSelector: // Doubles match selected
                        singlesMatch = false;
                        // Add 2 players
                        team1player2.setVisibility(View.VISIBLE);
                        team2player2.setVisibility(View.VISIBLE);

                        // Change Label text
                        text = (TextView) team1player1.getChildAt(0);
                        text.setText(R.string.team1label);
                        text = (TextView) team2player1.getChildAt(0);
                        text.setText(R.string.team2label);
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

        EditText score1editText  =findViewById(R.id.scoreTeam1);
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

        EditText score2editText = findViewById(R.id.scoreTeam2);
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

                String errorMessage = "";
                TextView errorText = findViewById(R.id.errorText);

                if(singlesMatch){
                    if(player1 && player3 && score1 && score2){
                        errorText.setText("");
                        Toast.makeText(getApplicationContext(), "Successfully created match",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        if(!player1) errorMessage = errorMessage + "Player 1 missing, ";
                        if(!player3) errorMessage = errorMessage + "Player 2 missing, ";
                        if(!score1 || !score2) errorMessage = errorMessage + "Score missing, ";
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
    }
}