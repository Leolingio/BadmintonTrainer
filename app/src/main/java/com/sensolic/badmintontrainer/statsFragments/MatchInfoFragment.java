package com.sensolic.badmintontrainer.statsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.ReloadActivity;
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;

public class MatchInfoFragment extends Fragment {

    private View view;
    private Match currentShowing;
    private boolean showing = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_matchinfo, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        RelativeLayout team1player1icon = view.findViewById(R.id.team1player1icon);
        team1player1icon.setOnClickListener(view -> {
            if(currentShowing != null){
                Player toShow = currentShowing.getTeam1Player1();
                if(toShow != null){
                    StatsActivity.showInfo(toShow);
                    StatsActivity.linkedMatch = true;
                    startActivity(new Intent(getContext(),ReloadActivity.class));
                } else{
                    Toast.makeText(getContext(), "Player not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RelativeLayout team1player2icon = view.findViewById(R.id.team1player2icon);
        team1player2icon.setOnClickListener(view -> {
            if(currentShowing != null){
                Player toShow = currentShowing.getTeam1Player2();
                if(toShow != null){
                    StatsActivity.showInfo(toShow);
                    StatsActivity.linkedMatch = true;
                    startActivity(new Intent(getContext(),ReloadActivity.class));
                } else{
                    Toast.makeText(getContext(), "Player not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RelativeLayout team2player1icon = view.findViewById(R.id.team2player1icon);
        team2player1icon.setOnClickListener(view -> {
            if(currentShowing != null){
                Player toShow = currentShowing.getTeam2Player1();
                if(toShow != null){
                    StatsActivity.showInfo(toShow);
                    StatsActivity.linkedMatch = true;
                    startActivity(new Intent(getContext(),ReloadActivity.class));
                } else{
                    Toast.makeText(getContext(), "Player not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RelativeLayout team2player2icon = view.findViewById(R.id.team2player2icon);
        team2player2icon.setOnClickListener(view -> {
            if(currentShowing != null){
                Player toShow = currentShowing.getTeam2Player2();
                if(toShow != null){
                    StatsActivity.showInfo(toShow);
                    StatsActivity.linkedMatch = true;
                    startActivity(new Intent(getContext(),ReloadActivity.class));
                } else{
                    Toast.makeText(getContext(), "Player not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView deleteMatchButton = view.findViewById(R.id.deleteImage);
        deleteMatchButton.setOnClickListener(view -> {
            Storage storage = Storage.getInstance(getContext());
            if(currentShowing.getMatchDependency().equals("Pending")) {
                storage.deletePendingMatch(currentShowing.getMatchID());
            } else{
                storage.deleteMatch(currentShowing.getMatchID());
            }
            currentShowing = null;
            showing = false;
            getActivity().onBackPressed();
        });

        if(!showing && currentShowing != null){
            setInfo(currentShowing);
        }

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

        // Change size of texts
        TextView tv = view.findViewById(R.id.matchInfoTitle);
        tv.setTextSize(1.5f * sizeHead);
        tv = view.findViewById(R.id.vsText);
        tv.setTextSize(2f * sizeHead);
        tv = view.findViewById(R.id.resultText);
        tv.setTextSize(sizeHead);
        tv = view.findViewById(R.id.first_set);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.second_set);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.third_set);
        tv.setTextSize(sizeText);

        //Team 1 Player 1
        tv = view.findViewById(R.id.team1player1name);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.team1player1ID);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.team1player1points);
        tv.setTextSize(sizeText);
        team1player1icon.getLayoutParams().height = (int)(15 * sizeHead);
        team1player1icon.getLayoutParams().width = (int)(15 * sizeHead);
        ImageView iv = view.findViewById(R.id.team1player1iconInside);
        iv.getLayoutParams().height = (int)(15 * sizeHead) - 28;
        iv.getLayoutParams().width = (int)(15 * sizeHead) - 28;

        //Team 1 Player 2
        tv = view.findViewById(R.id.team1player2name);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.team1player2ID);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.team1player2points);
        tv.setTextSize(sizeText);
        team1player2icon.getLayoutParams().height = (int)(15 * sizeHead);
        team1player2icon.getLayoutParams().width = (int)(15 * sizeHead);
        iv = view.findViewById(R.id.team1player2iconInside);
        iv.getLayoutParams().height = (int)(15 * sizeHead) - 28;
        iv.getLayoutParams().width = (int)(15 * sizeHead) - 28;

        //Team 2 Player 1
        tv = view.findViewById(R.id.team2player1name);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.team2player1ID);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.team2player1points);
        tv.setTextSize(sizeText);
        team2player1icon.getLayoutParams().height = (int)(15 * sizeHead);
        team2player1icon.getLayoutParams().width = (int)(15 * sizeHead);
        iv = view.findViewById(R.id.team2player1iconInside);
        iv.getLayoutParams().height = (int)(15 * sizeHead) - 28;
        iv.getLayoutParams().width = (int)(15 * sizeHead) - 28;

        //Team 2 Player 2
        tv = view.findViewById(R.id.team2player2name);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.team2player2ID);
        tv.setTextSize(sizeText);
        tv = view.findViewById(R.id.team2player2points);
        tv.setTextSize(sizeText);
        team2player2icon.getLayoutParams().height = (int)(15 * sizeHead);
        team2player2icon.getLayoutParams().width = (int)(15 * sizeHead);
        iv = view.findViewById(R.id.team2player2iconInside);
        iv.getLayoutParams().height = (int)(15 * sizeHead) - 28;
        iv.getLayoutParams().width = (int)(15 * sizeHead) - 28;

        return view;
    }

    public void setInfo(Match match){
        TextView textView;
        RelativeLayout icon;

        currentShowing = match;

        if(view != null){
            if (match.getMatchType() == 'S') {
                textView = view.findViewById(R.id.matchInfoTitle);
                if(match.getMatchDependency().equals("Pending")){
                    textView.setText("Pending \n Singles Match");
                } else {
                    textView.setText("Singles Match \n" + match.getIDInfo());
                }
                textView = view.findViewById(R.id.team1player1name);
                textView.setText(match.getTeam1Player1().getPlayerName());
                textView = view.findViewById(R.id.team1player1ID);
                textView.setText("#P" + match.getTeam1Player1ID());
                textView = view.findViewById(R.id.team2player1name);
                textView.setText(match.getTeam2Player1().getPlayerName());
                textView = view.findViewById(R.id.team2player1ID);
                textView.setText("#P" + match.getTeam2Player1ID());
                if(!match.getMatchDependency().equals("Pending") && match.getWinner() == 1){
                    textView = view.findViewById(R.id.team1player1points);
                    textView.setText("+"+match.getTeam1Player1points()+" Points");
                    textView = view.findViewById(R.id.team1player2points);
                    textView.setVisibility(View.INVISIBLE);
                    textView = view.findViewById(R.id.team2player1points);
                    textView.setVisibility(View.INVISIBLE);
                    textView = view.findViewById(R.id.team2player2points);
                    textView.setVisibility(View.INVISIBLE);
                }else if(!match.getMatchDependency().equals("Pending")){
                    textView = view.findViewById(R.id.team1player1points);
                    textView.setVisibility(View.INVISIBLE);
                    textView = view.findViewById(R.id.team1player2points);
                    textView.setVisibility(View.INVISIBLE);
                    textView = view.findViewById(R.id.team2player2points);
                    textView.setVisibility(View.INVISIBLE);
                    textView = view.findViewById(R.id.team2player1points);
                    textView.setText("+"+match.getTeam2Player1points()+" Points");
                }
                if(!match.getMatchDependency().equals("Pending")) {
                    textView = view.findViewById(R.id.first_set);
                    textView.setText("First Set - " + match.getScoreFirst());
                    textView = view.findViewById(R.id.second_set);
                    textView.setText("Second Set - " + match.getScoreSecond());
                    if (match.getSetCount() == 3) {
                        textView = view.findViewById(R.id.third_set);
                        textView.setText("Third Set - " + match.getScoreThird());
                    } else {
                        textView = view.findViewById(R.id.third_set);
                        textView.setVisibility(View.GONE);
                    }
                }

                textView = view.findViewById(R.id.team1player2name);
                textView.setVisibility(View.GONE);
                textView = view.findViewById(R.id.team1player2ID);
                textView.setVisibility(View.GONE);
                textView = view.findViewById(R.id.team2player2name);
                textView.setVisibility(View.GONE);
                textView = view.findViewById(R.id.team2player2ID);
                textView.setVisibility(View.GONE);
                icon = view.findViewById(R.id.team1player2icon);
                icon.setVisibility(View.GONE);
                icon = view.findViewById(R.id.team2player2icon);
                icon.setVisibility(View.GONE);

            } else {
                textView = view.findViewById(R.id.matchInfoTitle);
                if(match.getMatchDependency().equals("Pending")){
                    textView.setText("Pending \n Doubles Match");
                } else {
                    textView.setText("Doubles Match \n" + match.getIDInfo());
                }
                textView = view.findViewById(R.id.team1player1name);
                textView.setText(match.getTeam1Player1().getPlayerName());
                textView = view.findViewById(R.id.team1player1ID);
                textView.setText("#P" + match.getTeam1Player1ID());
                textView = view.findViewById(R.id.team1player2name);
                textView.setText(match.getTeam1Player2().getPlayerName());
                textView = view.findViewById(R.id.team1player2ID);
                textView.setText("#P" + match.getTeam1Player2ID());
                textView = view.findViewById(R.id.team2player1name);
                textView.setText(match.getTeam2Player1().getPlayerName());
                textView = view.findViewById(R.id.team2player1ID);
                textView.setText("#P" + match.getTeam2Player1ID());
                textView = view.findViewById(R.id.team2player2name);
                textView.setText(match.getTeam2Player2().getPlayerName());
                textView = view.findViewById(R.id.team2player2ID);
                textView.setText("#P" + match.getTeam2Player2ID());
                if(!match.getMatchDependency().equals("Pending") && match.getWinner() == 1){
                    textView = view.findViewById(R.id.team1player1points);
                    textView.setText("+"+match.getTeam1Player1points()+" Points");
                    textView = view.findViewById(R.id.team1player2points);
                    textView.setText("+"+match.getTeam1Player2points()+" Points");
                    textView = view.findViewById(R.id.team2player1points);
                    textView.setVisibility(View.INVISIBLE);
                    textView = view.findViewById(R.id.team2player2points);
                    textView.setVisibility(View.INVISIBLE);
                }else if(!match.getMatchDependency().equals("Pending")){
                    textView = view.findViewById(R.id.team1player1points);
                    textView.setVisibility(View.INVISIBLE);
                    textView = view.findViewById(R.id.team1player2points);
                    textView.setVisibility(View.INVISIBLE);
                    textView = view.findViewById(R.id.team2player1points);
                    textView.setText("+"+match.getTeam2Player1points()+" Points");
                    textView = view.findViewById(R.id.team2player2points);
                    textView.setText("+"+match.getTeam2Player2points()+" Points");
                }
                if(!match.getMatchDependency().equals("Pending")) {
                    textView = view.findViewById(R.id.first_set);
                    textView.setText("First Game - " + match.getScoreFirst());
                    textView = view.findViewById(R.id.second_set);
                    textView.setText("Second Game - " + match.getScoreSecond());
                    if (match.getSetCount() == 3) {
                        textView = view.findViewById(R.id.third_set);
                        textView.setText("Third Game - " + match.getScoreThird());
                    } else {
                        textView = view.findViewById(R.id.third_set);
                        textView.setVisibility(View.GONE);
                    }
                }
                textView = view.findViewById(R.id.team1player2name);
                textView.setVisibility(View.VISIBLE);
                textView = view.findViewById(R.id.team1player2ID);
                textView.setVisibility(View.VISIBLE);
                textView = view.findViewById(R.id.team2player2name);
                textView.setVisibility(View.VISIBLE);
                textView = view.findViewById(R.id.team2player2ID);
                textView.setVisibility(View.VISIBLE);
                icon = view.findViewById(R.id.team1player2icon);
                icon.setVisibility(View.VISIBLE);
                icon = view.findViewById(R.id.team2player2icon);
                icon.setVisibility(View.VISIBLE);

                showing = true;
            }

            // Make Info below invisible when it is a pending match TODO
            if(match.getMatchDependency().equals("Pending")){
                TextView resultText = view.findViewById(R.id.resultText);
                resultText.setVisibility(View.GONE);
            }

        }
    }
}
