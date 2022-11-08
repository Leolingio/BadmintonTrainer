package com.sensolic.badmintontrainer.statsFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.data.Match;
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

        ArrayList<Match> arrayList = storage.getStoredRecentMatches();

        TextView emptyRecentMatchText = view.findViewById(R.id.emptyRecentMatchesText);
        if(arrayList.size() == 0){
            recentMatches.setVisibility(View.GONE);
            emptyRecentMatchText.setVisibility(View.VISIBLE);
        } else{
            recentMatches.setVisibility(View.VISIBLE);
            emptyRecentMatchText.setVisibility(View.GONE);
            int height = (int) (115 * getResources().getDisplayMetrics().density);
            ViewGroup.LayoutParams params = recentMatches.getLayoutParams();
            params.height = height * arrayList.size();
        }

        recentMatchAdapter = new RecentMatchesAdapter(view.getContext(), arrayList);

        recentMatches.setAdapter(recentMatchAdapter);

        Button refreshRecentMatches = view.findViewById(R.id.refreshRecentMatches);
        refreshRecentMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Match> arrayList = storage.getStoredRecentMatches();
                recentMatchAdapter = new RecentMatchesAdapter(view.getContext(), arrayList);
                recentMatches.setAdapter(recentMatchAdapter);
                recentMatchAdapter.notifyDataSetChanged();
                int height = (int) (115 * getResources().getDisplayMetrics().density);
                ViewGroup.LayoutParams params = recentMatches.getLayoutParams();
                params.height = height * arrayList.size();
            }
        });

        return view;
    }
}