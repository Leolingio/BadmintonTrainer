package com.sensolic.badmintontrainer.statsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.data.Player;

public class PlayerInfoFragment extends Fragment {

    private View view;
    private Player toShow;
    private boolean showing = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_playerinfo, container, false);

        if(!showing && toShow != null){
            setInfo(toShow);
        }

        return view;
    }

    public void setInfo(Player player){
        TextView textView;

        if(view == null){
            toShow = player;
        } else {
            textView = view.findViewById(R.id.playerName);
            textView.setText(player.getPlayerName());
            textView = view.findViewById(R.id.playerID);
            textView.setText(player.getIDInfo());
            textView = view.findViewById(R.id.rankingPoints);
            textView.setText(player.getRankingPoints() + " Points");
            textView = view.findViewById(R.id.matchesPlayed);
            textView.setText("Total: " + player.getMatchesPlayed());
            textView = view.findViewById(R.id.teamNumber);
            if (player.getTeamNumber() != -1) {
                textView.setText(player.getTeamNumber());
            }
            /*
            textView = view.findViewById(R.id.mainHand);
            if (player.getMainHand() == 'R') {
                textView.setText("Right handed");
            } else if (player.getMainHand() == 'L') {
                textView.setText("Left handed");
            }
            */

            showing = true;
        }
    }
}
