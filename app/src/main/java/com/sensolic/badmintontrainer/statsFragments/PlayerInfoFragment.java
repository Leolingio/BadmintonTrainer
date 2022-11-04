package com.sensolic.badmintontrainer.statsFragments;

import android.content.Intent;
import android.media.Image;
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
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;

import java.util.Objects;

public class PlayerInfoFragment extends Fragment {

    private View view;
    private Player toShow;
    private ImageView reloadImage;
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

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        if (!showing && toShow != null) {
            setInfo(toShow);
        }

        reloadImage = view.findViewById(R.id.reloadImage);
        reloadImage.setOnClickListener(view -> {
            toShow = Storage.getInstance(view.getContext()).getPlayerData(toShow.getPlayerID());
            StatsActivity.showInfo(toShow);
            startActivity(new Intent(view.getContext(), ReloadActivity.class));
        });
        reloadImage.setVisibility(View.GONE);

        return view;
    }

    public void setInfo(Player player) {
        TextView textView;

        if (view == null) {
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
            /*
            textView = view.findViewById(R.id.teamNumber);
            if (player.getTeamNumber() != -1) {
                textView.setText(player.getTeamNumber());
            }
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

    public void hideReloadImage(){
        if(showing) {
            reloadImage.setVisibility(View.GONE);
        }
    }

    public void showReloadImage(){
        if(showing) {
            reloadImage.setVisibility(View.VISIBLE);
        }
    }
}