package com.sensolic.badmintontrainer.statsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.ReloadActivity;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;

import java.util.concurrent.atomic.AtomicBoolean;

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
            AtomicBoolean ready = new AtomicBoolean(false);
            Thread t = new Thread(() -> {
                for(int i = 0; i <= 180; i++) {
                    reloadImage.setRotation(i);
                    reloadImage.invalidate();
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        //ignored
                    }
                    reloadImage.setRotation(0);
                }
                ready.set(true);
            });
            t.start();
            toShow = Storage.getInstance(view.getContext()).getPlayerData(toShow.getPlayerID());
            StatsActivity.showInfo(toShow);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(view.getContext(), ReloadActivity.class));
        });

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