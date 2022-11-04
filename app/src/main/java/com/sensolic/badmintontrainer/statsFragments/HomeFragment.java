package com.sensolic.badmintontrainer.statsFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;
import com.sensolic.badmintontrainer.home.RecentMatchesAdapter;
import com.sensolic.badmintontrainer.leaderboard.LeaderboardAdapter;
import com.sensolic.badmintontrainer.search.SearchAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    Storage storage;
    ListView recentMatches;
    ListView recommendedMatches;
    RecentMatchesAdapter recentMatchAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        storage = Storage.getInstance(view.getContext());
        recentMatches = view.findViewById(R.id.recentMatches);
        recommendedMatches = view.findViewById(R.id.recommendedMatches);

        ArrayList<Player> arrayList = storage.getStoredPlayers();

        recentMatchAdapter = new RecentMatchesAdapter(view.getContext(), arrayList);

        recentMatches.setAdapter(recentMatchAdapter);

        return view;
    }
}