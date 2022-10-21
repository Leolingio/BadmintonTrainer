package com.sensolic.badmintontrainer.statsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;
import com.sensolic.badmintontrainer.leaderboard.LeaderboardAdapter;
import com.sensolic.badmintontrainer.search.SearchActivity;
import com.sensolic.badmintontrainer.search.SearchAdapter;
import com.sensolic.badmintontrainer.search.Searchable;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    Storage storage;
    ListView listView;
    LeaderboardAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        assert view != null;

        storage = Storage.getInstance(view.getContext());
        listView = view.findViewById(R.id.listView);

        ArrayList<Player> arrayList = storage.getStoredPlayers();

        adapter = new LeaderboardAdapter(view.getContext(), arrayList, new StatsActivity());

        listView.setAdapter(adapter);

        SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(() -> {
            adapter.notifyDataSetChanged();
            pullToRefresh.setRefreshing(false);
        });

        return view;
    }
}