package com.sensolic.badmintontrainer.statsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.data.Match;

public class MatchInfoFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matchinfo, container, false);
    }

    public void setInfo(Match match){
        TextView textView;
        RelativeLayout icon;
        View view = getView();
        assert view != null;

        if(match.getMatchType() == 'S'){
            textView = view.findViewById(R.id.matchInfoTitle);
            textView.setText("Singles Match "+match.getIDInfo());
            textView = view.findViewById(R.id.team1player1info);
            textView.setText("#"+match.getPlayerOneID());
            textView = view.findViewById(R.id.team2player1info);
            textView.setText("#"+match.getPlayerTwoID());
            textView = view.findViewById(R.id.first_set);
            textView.setText("First Set - "+match.getScoreFirst());
            textView = view.findViewById(R.id.second_set);
            textView.setText("Second Set - "+match.getScoreSecond());
            if(match.getSetCount() == 3) {
                textView = view.findViewById(R.id.third_set);
                textView.setText("Third Set - " + match.getScoreThird());
            } else{
                textView = view.findViewById(R.id.third_set);
                textView.setVisibility(View.GONE);
            }

            textView = view.findViewById(R.id.team1player2info);
            textView.setVisibility(View.GONE);
            textView = view.findViewById(R.id.team2player2info);
            textView.setVisibility(View.GONE);
            icon = view.findViewById(R.id.team1player2icon);
            icon.setVisibility(View.GONE);
            icon = view.findViewById(R.id.team2player2icon);
            icon.setVisibility(View.GONE);
        } else{
            textView = view.findViewById(R.id.matchInfoTitle);
            textView.setText("Doubles Match "+match.getIDInfo());
            textView = view.findViewById(R.id.team1player1info);
            textView.setText("#"+match.getPlayerOneID());
            textView = view.findViewById(R.id.team1player2info);
            textView.setText("#"+match.getPlayerTwoID());
            textView = view.findViewById(R.id.team2player1info);
            textView.setText("#"+match.getPlayerThreeID());
            textView = view.findViewById(R.id.team2player2info);
            textView.setText("#"+match.getPlayerFourID());
            textView = view.findViewById(R.id.first_set);
            textView.setText("First Set - "+match.getScoreFirst());
            textView = view.findViewById(R.id.second_set);
            textView.setText("Second Set - "+match.getScoreSecond());
            if(match.getSetCount() == 3) {
                textView = view.findViewById(R.id.third_set);
                textView.setText("Third Set - " + match.getScoreThird());
            } else{
                textView = view.findViewById(R.id.third_set);
                textView.setVisibility(View.GONE);
            }

            textView = view.findViewById(R.id.team1player2info);
            textView.setVisibility(View.VISIBLE);
            textView = view.findViewById(R.id.team2player2info);
            textView.setVisibility(View.VISIBLE);
            icon = view.findViewById(R.id.team1player2icon);
            icon.setVisibility(View.VISIBLE);
            icon = view.findViewById(R.id.team2player2icon);
            icon.setVisibility(View.VISIBLE);
        }

    }
}
