package com.sensolic.badmintontrainer.statsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;
import com.sensolic.badmintontrainer.home.RecentMatchesAdapter;
import com.sensolic.badmintontrainer.home.RecommendedMatchesAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class HomeFragment extends Fragment {

    private static boolean refreshDone = false;
    private static final int SINGLES_PLAYER_DIFFERENCE = 20;
    private static final int DOUBLES_PLAYER_DIFFERENCE = 40;
    private int recentMatchHeight;
    private int recommendedMatchHeight;
    private ArrayList[] recommendedMatchesHistory = new ArrayList[5];
    Storage storage;
    ListView recentMatches;
    ListView recommendedMatches;
    RecentMatchesAdapter recentMatchAdapter;
    RecommendedMatchesAdapter recommendedMatchesAdapter;

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

        recentMatchHeight = (int) (115 * getResources().getDisplayMetrics().density);
        recommendedMatchHeight = (int) (125 * getResources().getDisplayMetrics().density);

        ArrayList<Match> arrayList = storage.getStoredRecentMatches();

        TextView emptyRecentMatchText = view.findViewById(R.id.emptyRecentMatchesText);
        if (arrayList.size() == 0) {
            recentMatches.setVisibility(View.GONE);
            emptyRecentMatchText.setVisibility(View.VISIBLE);
        } else {
            recentMatches.setVisibility(View.VISIBLE);
            emptyRecentMatchText.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = recentMatches.getLayoutParams();
            params.height = recentMatchHeight * arrayList.size();
        }

        recentMatchAdapter = new RecentMatchesAdapter(view.getContext(), arrayList);

        recentMatches.setAdapter(recentMatchAdapter);

        Button refreshRecentMatches = view.findViewById(R.id.refreshRecentMatches);
        refreshRecentMatches.setOnClickListener(view1 -> {
            refreshDone = false;
            Thread t = new Thread(() -> {
                while (!refreshDone) {
                    for (int i = 0; i <= 180; i++) {
                        refreshRecentMatches.setRotation(i);
                        refreshRecentMatches.invalidate();
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            //ignored
                        }
                        refreshRecentMatches.setRotation(0);
                    }
                }
            });
            t.start();

            ArrayList<Match> list = storage.getStoredRecentMatches();
            recentMatchAdapter = new RecentMatchesAdapter(view1.getContext(), list);
            recentMatches.setAdapter(recentMatchAdapter);
            recentMatchAdapter.notifyDataSetChanged();
            ViewGroup.LayoutParams params = recentMatches.getLayoutParams();
            params.height = recentMatchHeight * list.size();
            if (list.size() == 0) {
                recentMatches.setVisibility(View.GONE);
                emptyRecentMatchText.setVisibility(View.VISIBLE);
            } else {
                recentMatches.setVisibility(View.VISIBLE);
                emptyRecentMatchText.setVisibility(View.GONE);
            }
            refreshDone = true;
        });

        arrayList = getRecommendedMatches(null);

        TextView emptyRecommendedMatchText = view.findViewById(R.id.emptyRecommendedMatchesText);
        if (arrayList.size() == 0) {
            recommendedMatches.setVisibility(View.GONE);
            emptyRecommendedMatchText.setVisibility(View.VISIBLE);
        } else {
            recommendedMatches.setVisibility(View.VISIBLE);
            emptyRecommendedMatchText.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = recommendedMatches.getLayoutParams();
            params.height = recommendedMatchHeight * arrayList.size();
        }

        recommendedMatchesAdapter = new RecommendedMatchesAdapter(view.getContext(), arrayList);

        recommendedMatches.setAdapter(recommendedMatchesAdapter);

        Button refreshRecommendedMatches = view.findViewById(R.id.refreshRecommendedMatches);
        refreshRecommendedMatches.setOnClickListener(view1 -> {
            refreshDone = false;
            Thread t = new Thread(() -> {
                while (!refreshDone) {
                    for (int i = 0; i <= 180; i++) {
                        refreshRecommendedMatches.setRotation(i);
                        refreshRecommendedMatches.invalidate();
                        try {
                            Thread.sleep(2);
                        } catch (InterruptedException e) {
                            //ignored
                        }
                        refreshRecommendedMatches.setRotation(0);
                    }
                }
            });
            t.start();

            ArrayList<Match> list = getRecommendedMatches(recommendedMatchesAdapter.getList());
            recommendedMatchesAdapter = new RecommendedMatchesAdapter(view1.getContext(), list);
            recommendedMatches.setAdapter(recommendedMatchesAdapter);
            recommendedMatchesAdapter.notifyDataSetChanged();
            ViewGroup.LayoutParams params = recommendedMatches.getLayoutParams();
            params.height = recommendedMatchHeight * list.size();
            if (list.size() == 0) {
                recommendedMatches.setVisibility(View.GONE);
                emptyRecommendedMatchText.setVisibility(View.VISIBLE);
            } else {
                recommendedMatches.setVisibility(View.VISIBLE);
                emptyRecommendedMatchText.setVisibility(View.GONE);
            }
            refreshDone = true;
        });

        return view;
    }

    private ArrayList<Match> getRecommendedMatches(ArrayList<Match> avoidMatches) {

        if (avoidMatches != null && avoidMatches.size() != 0) {
            boolean added = false;
            for (int i = 0; i < recommendedMatchesHistory.length; i++) {
                if (recommendedMatchesHistory[i] == null) {
                    recommendedMatchesHistory[i] = avoidMatches;
                    added = true;
                    break;
                }
            }
            if (!added) {
                for (int i = 0; i < recommendedMatchesHistory.length - 1; i++) {
                    recommendedMatchesHistory[i] = recommendedMatchesHistory[i + 1];
                }
                recommendedMatchesHistory[recommendedMatchesHistory.length - 1] = avoidMatches;
            }
        }

        ArrayList<Match> result = new ArrayList<>();

        ArrayList<Player> players = storage.getStoredPlayers();

        ArrayList<Player>[] prefPlayers = new ArrayList[players.size()];
        for (int i = 0; i < prefPlayers.length; i++) {
            prefPlayers[i] = new ArrayList<>();
        }

        boolean[] paired = new boolean[players.size()];

        ArrayList<Player> pref;

        // Singles Matches

        for (int i = 0; i < 5; i++) {

            for (Player p : players) {
                if (paired[players.indexOf(p)]) continue;
                pref = new ArrayList<>();
                for (Player player : players) {
                    if (p.getPlayerID() != player.getPlayerID()) {
                        if (!paired[players.indexOf(player)]
                                && Math.abs(p.getRankingPoints() - player.getRankingPoints()) <= SINGLES_PLAYER_DIFFERENCE) {

                            pref.add(player);
                        }
                    }
                }
                if (pref.size() > 0) {
                    pref.sort(Comparator.comparingInt(Player::getRankingPoints).reversed());
                    ArrayList<Match> toAvoid = new ArrayList<>();
                    for (ArrayList list : recommendedMatchesHistory) {
                        if (list != null && list.size() != 0) {
                            toAvoid.addAll(list);
                        }
                    }
                    if (toAvoid.size() != 0) {
                        for (Match m : toAvoid) {
                            if (m.getMatchType() == 'S'
                                    && m.getTeam2Player1().getPlayerID() == pref.get(0).getPlayerID()) {
                                pref.remove(pref.get(0));
                                break;
                            }
                        }
                        if (pref.size() == 0) continue;
                    }
                    Match m = new Match(storage, -2, 'S', new long[]{p.getPlayerID(),
                            pref.get(0).getPlayerID()}, -2, null, null);
                    result.add(m);
                    paired[players.indexOf(p)] = true;
                    paired[players.indexOf(pref.get(0))] = true;
                }
            }
        }

        Arrays.fill(paired, false);

        // Doubles Matches

        for (int i = 0; i < 5; i++) {

            for (Player p : players) {
                if (paired[players.indexOf(p)]) continue;
                pref = new ArrayList<>();
                for (Player player : players) {
                    if (p.getPlayerID() != player.getPlayerID()) {
                        if (!paired[players.indexOf(player)]
                                && Math.abs(p.getRankingPoints() - player.getRankingPoints()) <= DOUBLES_PLAYER_DIFFERENCE) {

                            pref.add(player);
                        }
                    }
                }
                if (pref.size() > 2) {
                    pref.sort(Comparator.comparingInt(Player::getRankingPoints).reversed());
                    ArrayList<Match> toAvoid = new ArrayList<>();
                    for (ArrayList list : recommendedMatchesHistory) {
                        if (list != null && list.size() != 0) {
                            toAvoid.addAll(list);
                        }
                    }
                    if (toAvoid.size() != 0) {
                        for (Match m : toAvoid) {
                            if (m.getMatchType() == 'D'
                                    && m.getTeam1Player2().getPlayerID() == pref.get(1).getPlayerID()) {
                                pref.remove(pref.get(1));
                                break;
                            }
                        }
                        if (pref.size() < 3) continue;
                    }
                    Match m = new Match(storage, -2, 'D', new long[]{p.getPlayerID(),
                            pref.get(1).getPlayerID(), pref.get(0).getPlayerID(),
                            pref.get(3).getPlayerID()}, -2, null, null);
                    result.add(m);
                    paired[players.indexOf(p)] = true;
                    paired[players.indexOf(pref.get(0))] = true;
                    paired[players.indexOf(pref.get(1))] = true;
                    paired[players.indexOf(pref.get(2))] = true;
                }
            }
        }

        return result;
    }
}