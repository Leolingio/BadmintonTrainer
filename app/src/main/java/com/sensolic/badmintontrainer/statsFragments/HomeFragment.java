package com.sensolic.badmintontrainer.statsFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;
import com.sensolic.badmintontrainer.home.RecentMatchesAdapter;
import com.sensolic.badmintontrainer.home.RecommendedMatchesAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import kotlinx.coroutines.sync.Mutex;

public class HomeFragment extends Fragment {

    private static boolean refreshDone = false;
    private View view;
    private int recentMatchHeight;
    private int recommendedMatchHeight;
    private final ArrayList[] recommendedMatchesHistory = new ArrayList[5];
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

        this.view = view;

        storage = Storage.getInstance(view.getContext());
        recentMatches = view.findViewById(R.id.recentMatches);
        recommendedMatches = view.findViewById(R.id.recommendedMatches);

        refreshTextSize();

        ArrayList<Match> arrayList = storage.getStoredRecentMatches();

        final TextView[] emptyRecentMatchText = {view.findViewById(R.id.emptyRecentMatchesText)};
        if (arrayList.size() == 0) {
            recentMatches.setVisibility(View.GONE);
            emptyRecentMatchText[0].setVisibility(View.VISIBLE);
        } else {
            recentMatches.setVisibility(View.VISIBLE);
            emptyRecentMatchText[0].setVisibility(View.GONE);
            ViewGroup.LayoutParams params = recentMatches.getLayoutParams();
            params.height = recentMatchHeight * arrayList.size();
        }

        recentMatchAdapter = new RecentMatchesAdapter(view.getContext(), arrayList);

        recentMatches.setAdapter(recentMatchAdapter);

        Lock lockRefreshRecentMatchesButton = new ReentrantLock(true);
        Button refreshRecentMatches = view.findViewById(R.id.refreshRecentMatches);
        refreshRecentMatches.setOnClickListener(view1 -> {
            lockRefreshRecentMatchesButton.lock();
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

            emptyRecentMatchText[0] = view.findViewById(R.id.emptyRecentMatchesText);
            ArrayList<Match> list = storage.getStoredRecentMatches();
            recentMatchAdapter = new RecentMatchesAdapter(view1.getContext(), list);
            recentMatches.setAdapter(recentMatchAdapter);
            recentMatchAdapter.notifyDataSetChanged();
            ViewGroup.LayoutParams params = recentMatches.getLayoutParams();
            params.height = recentMatchHeight * list.size();
            if (list.size() == 0) {
                recentMatches.setVisibility(View.GONE);
                emptyRecentMatchText[0].setVisibility(View.VISIBLE);
            } else {
                recentMatches.setVisibility(View.VISIBLE);
                emptyRecentMatchText[0].setVisibility(View.GONE);
            }
            refreshDone = true;
            lockRefreshRecentMatchesButton.unlock();
        });

        arrayList = generateRecommendedMatches();

        TextView emptyRecommendedMatchText = view.findViewById(R.id.emptyRecommendedMatchesText);
        if (arrayList.size() == 0) {
            recommendedMatches.setVisibility(View.GONE);
            emptyRecommendedMatchText.setVisibility(View.VISIBLE);
        } else {
            recommendedMatches.setVisibility(View.VISIBLE);
            emptyRecommendedMatchText.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = recommendedMatches.getLayoutParams();
            params.height = recommendedMatchHeight * arrayList.size();
            recommendedMatches.setLayoutParams(params);
        }

        recommendedMatchesAdapter = new RecommendedMatchesAdapter(view.getContext(), arrayList);

        recommendedMatches.setAdapter(recommendedMatchesAdapter);

        Lock lockRefreshRecommendedMatchesButton = new ReentrantLock(true);
        Button refreshRecommendedMatches = view.findViewById(R.id.refreshRecommendedMatches);
        refreshRecommendedMatches.setOnClickListener(view1 -> {
            lockRefreshRecommendedMatchesButton.lock();
            refreshDone = false;
            new Thread(() -> {
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
            }).start();

            refreshRecommendedMatches();

            refreshDone = true;
            lockRefreshRecommendedMatchesButton.unlock();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshRecommendedMatches();
        refreshRecentMatches();
        refreshTextSize();
    }

    /**
     * This method generate new recommendations based on all stored players
     * Previous generated matches will not be generated again -> saved in recommendedMatchesHistory
     *
     * @return List of new generated recommended matches
     */
    private ArrayList<Match> generateRecommendedMatches() {
        // Getting all stored players from storage and sorting them
        ArrayList<Player> sortedPlayerList = storage.getStoredPlayers();
        sortedPlayerList.sort(Comparator.comparingInt(Player::getRankingPoints).reversed());

        // Lists where we will add all possible recommendations
        ArrayList<Match> allPossibleSinglesRecommendations = new ArrayList<>();
        ArrayList<Match> allPossibleDoublesRecommendations = new ArrayList<>();

        // Build up list with all previous matches
        ArrayList<Match> previousMatches = new ArrayList<>();
        for (ArrayList list : recommendedMatchesHistory) {
            if (list != null) {
                previousMatches.addAll(list);
            }
        }

        // Find out Singles recommendations
        for (Player player1 : sortedPlayerList) {
            for (int k = 0; k < sortedPlayerList.size(); k++) {
                Player player2 = sortedPlayerList.get(k);
                if (Math.abs(player1.getRankingPoints() - player2.getRankingPoints()) <= Settings.singlesPlayerDifference()) {
                    // Player inside point range -> create new recommendation
                    Match m = new Match(storage, 'S', new long[]{player1.getPlayerID(), player2.getPlayerID()});

                    boolean valid = true;
                    // Check if same match was previously recommended
                    for (Match prev : previousMatches) {
                        if (sameParticipants(prev, m)) {
                            valid = false;
                            break;
                        }
                    }

                    // Check if same match is already recommended
                    for (Match recom : allPossibleSinglesRecommendations) {
                        if (sameParticipants(recom, m)) {
                            valid = false;
                            break;
                        }
                    }

                    if (valid) {
                        // Match is completely new and can be recommended
                        if (isValidMatch(m)) {
                            allPossibleSinglesRecommendations.add(m);
                        }
                    }
                }
            }
        }

        // Find out Doubles recommendations
        for (int l = 0; l < sortedPlayerList.size(); l++) {                     // Team 1 Player 1
            for (int k = 0; k < sortedPlayerList.size(); k++) {                 // Team 1 Player 2
                if (k == l) continue;
                for (int i = 0; i < sortedPlayerList.size(); i++) {             // Team 2 Player 1
                    if (i == k || i == l) continue;
                    for (int j = 0; j < sortedPlayerList.size(); j++) {         // Team 2 Player 2
                        if (j == i || j == k || j == l) continue;
                        Player player1 = sortedPlayerList.get(l);
                        Player player2 = sortedPlayerList.get(k);
                        Player player3 = sortedPlayerList.get(i);
                        Player player4 = sortedPlayerList.get(j);

                        int pointsTeam1, pointsTeam2;
                        // Calculate team points
                        pointsTeam1 = player1.getRankingPoints() + player2.getRankingPoints();
                        pointsTeam2 = player3.getRankingPoints() + player4.getRankingPoints();

                        if (Math.abs(pointsTeam1 - pointsTeam2) <= Settings.doublesPlayerDifference()) {
                            // Player inside point range -> create new recommendation
                            Match m = new Match(storage, 'D',
                                    new long[]{player1.getPlayerID(), player2.getPlayerID(),
                                            player3.getPlayerID(),
                                            player4.getPlayerID()});

                            boolean valid = true;
                            // Check if same match was previously recommended
                            for (Match prev : previousMatches) {
                                if (sameParticipants(prev, m)) {
                                    valid = false;
                                    break;
                                }
                            }

                            // Check if same match is already recommended
                            for (Match recom : allPossibleDoublesRecommendations) {
                                if (sameParticipants(recom, m)) {
                                    valid = false;
                                    break;
                                }
                            }

                            if (valid) {
                                // Match is completely new and can be recommended
                                if (isValidMatch(m)) {
                                    allPossibleDoublesRecommendations.add(m);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Random select of recommendations
        // Pick 2 Singles matches
        Random rand = new Random();

        int idx1;
        int idx2;

        if (allPossibleSinglesRecommendations.size() == 0) {
            idx1 = idx2 = -1;
        } else if (allPossibleSinglesRecommendations.size() == 1) {
            idx1 = 0;
            idx2 = -1;
        } else if (allPossibleSinglesRecommendations.size() == 2) {
            idx1 = 0;
            idx2 = 1;
        } else {
            idx1 = rand.nextInt(allPossibleSinglesRecommendations.size());
            idx2 = rand.nextInt(allPossibleSinglesRecommendations.size());
            while (idx2 == idx1) {
                idx2 = rand.nextInt(allPossibleSinglesRecommendations.size());
            }
        }

        // Creating result list and adding Singles matches
        ArrayList<Match> result = new ArrayList<>();
        if (idx1 != -1) result.add(allPossibleSinglesRecommendations.get(idx1));
        if (idx2 != -1) result.add(allPossibleSinglesRecommendations.get(idx2));

        // Pick 2 Doubles matches
        if (allPossibleDoublesRecommendations.size() == 0) {
            idx1 = idx2 = -1;
        } else if (allPossibleDoublesRecommendations.size() == 1) {
            idx1 = 0;
            idx2 = -1;
        } else if (allPossibleDoublesRecommendations.size() == 2) {
            idx1 = 0;
            idx2 = 1;
        } else {
            idx1 = rand.nextInt(allPossibleDoublesRecommendations.size());
            idx2 = rand.nextInt(allPossibleDoublesRecommendations.size());
            while (idx2 == idx1) {
                idx2 = rand.nextInt(allPossibleDoublesRecommendations.size());
            }
        }

        if (idx1 != -1) result.add(allPossibleDoublesRecommendations.get(idx1));
        if (idx2 != -1) result.add(allPossibleDoublesRecommendations.get(idx2));

        // Store in history
        for (int i = 0; i < recommendedMatchesHistory.length; i++) {
            if (recommendedMatchesHistory[i] == null || recommendedMatchesHistory[i].size() == 0) {
                // Found empty list in history -> add current list
                recommendedMatchesHistory[i] = result;
                return result;
            }
        }
        // Delete oldest history entry and add the current one
        for (int i = 0; i < recommendedMatchesHistory.length - 1; i++) {
            recommendedMatchesHistory[i] = recommendedMatchesHistory[i + 1];
        }
        recommendedMatchesHistory[recommendedMatchesHistory.length - 1] = result;
        return result;
    }

    /**
     * This method examines if the participants of the matches are equal
     *
     * @param a First match
     * @param b Second match
     * @return true if matches have equal participants
     * also counts mirrored matches as equal
     * false: if participants are different or
     * participant order is different or
     * matches are not of same type ('S'/'D' or pending/complete)
     */
    private boolean sameParticipants(Match a, Match b) {
        // Invalid inputs
        if (a == null || b == null) return false;

        // Same ID or unequal matchType
        if (a.getMatchID() != b.getMatchID() || a.getMatchType() != b.getMatchType()) return false;

        // Getting all playerIDs from both matches
        long aTeam1player1 = a.getTeam1Player1ID();
        long aTeam1player2 = a.getTeam1Player2ID();
        long aTeam2player1 = a.getTeam2Player1ID();
        long aTeam2player2 = a.getTeam2Player2ID();

        long bTeam1player1 = b.getTeam1Player1ID();
        long bTeam1player2 = b.getTeam1Player2ID();
        long bTeam2player1 = b.getTeam2Player1ID();
        long bTeam2player2 = b.getTeam2Player2ID();

        // Singles Match -> check if equal order or mirrored order
        if (a.getMatchType() == 'S') {
            if (aTeam1player1 == bTeam1player1
                    && aTeam2player1 == bTeam2player1
                    || aTeam2player1 == bTeam1player1
                    && aTeam1player1 == bTeam2player1)
                return true;
        }

        // Doubles Match -> check if equal order or mirrored order or same teams with different order
        if (a.getMatchType() == 'D') {
            // Check first team of a
            boolean equal =
                    aTeam1player1 == bTeam1player1       // equal order
                            && aTeam1player2 == bTeam1player2
                            || aTeam1player1 == bTeam2player1       // mirrored order
                            && aTeam1player2 == bTeam2player2
                            || aTeam1player1 == bTeam1player2       // same teams a1 and b1
                            && aTeam1player2 == bTeam1player1
                            || aTeam1player1 == bTeam2player2       // same teams a1 and b2
                            && aTeam1player2 == bTeam2player1;

            // Check second team of a
            if (aTeam2player1 == bTeam2player1               // equal order
                    && aTeam2player2 == bTeam2player2
                    || aTeam2player1 == bTeam1player1       // mirrored order
                    && aTeam2player2 == bTeam1player2
                    || aTeam2player1 == bTeam1player2       // same teams a2 and b1
                    && aTeam2player2 == bTeam1player1
                    || aTeam2player1 == bTeam2player2       // same teams a2 and b2
                    && aTeam2player2 == bTeam2player1) {
                // matches have equal participants and teams
                return equal;
            }
        }

        return false;
    }

    /**
     *  This method checks if a match is valid
     * @param m The match that should be checked
     * @return true if match is valid, else false
     */
    private boolean isValidMatch(Match m) {
        long player1 = m.getTeam1Player1ID();
        long player2 = m.getTeam1Player2ID();
        long player3 = m.getTeam2Player1ID();
        long player4 = m.getTeam2Player2ID();
        if (player1 != -1) {
            if (player1 == player2 || player1 == player3 || player1 == player4) return false;
        }
        if (player2 != -1) {
            if (player2 == player3 || player2 == player4) return false;
        }
        if (player3 != -1) {
            if (player3 == player4) return false;
        }
        return true;
    }

    /**
     *  This method refreshes the recommended matches view
     */
    private void refreshRecommendedMatches() {
        ArrayList<Match> arrayList = generateRecommendedMatches();

        TextView emptyRecommendedMatchText = view.findViewById(R.id.emptyRecommendedMatchesText);
        if (arrayList.size() == 0) {
            recommendedMatches.setVisibility(View.GONE);
            emptyRecommendedMatchText.setVisibility(View.VISIBLE);
        } else {
            recommendedMatches.setVisibility(View.VISIBLE);
            emptyRecommendedMatchText.setVisibility(View.GONE);
            ViewGroup.LayoutParams params = recommendedMatches.getLayoutParams();
            params.height = recommendedMatchHeight * arrayList.size();
            recommendedMatches.setLayoutParams(params);
        }

        recommendedMatchesAdapter = new RecommendedMatchesAdapter(view.getContext(), arrayList);

        recommendedMatches.setAdapter(recommendedMatchesAdapter);

        recommendedMatchesAdapter.notifyDataSetChanged();

        recommendedMatches.invalidate();
    }

    private void refreshTextSize(){
        if(view == null) return;
        TextView recommendedMatchesHeader = view.findViewById(R.id.recommendedMatchesHeader);
        TextView recentMatchesHeader = view.findViewById(R.id.recentMatchesHeader);

        if(recommendedMatchesHeader == null || recentMatchesHeader == null) return;

        float txtsizeHead = 0;
        int recent = 0, recomm = 0;
        switch(Settings.textSize()){
            case 1:
                txtsizeHead = Settings.TEXTSIZE_SMALL_HEADER;
                recent = 100;
                recomm = 145;
                break;
            case 2:
                txtsizeHead = Settings.TEXTSIZE_NORMAL_HEADER;
                recent = 110;
                recomm = 155;
                break;
            case 3:
                txtsizeHead = Settings.TEXTSIZE_BIG_HEADER;
                recent = 125;
                recomm = 160;
                break;
        }
        recommendedMatchesHeader.setTextSize(txtsizeHead);
        recentMatchesHeader.setTextSize(txtsizeHead);

        // Refresh layout sizes
        recentMatchHeight = (int) (recent * getResources().getDisplayMetrics().density);
        recommendedMatchHeight = (int) (recomm * getResources().getDisplayMetrics().density);

    }

    /**
     *  This method refreshes the recent matches view
     */
    private void refreshRecentMatches() {
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
            recentMatches.setLayoutParams(params);
        }

        recentMatchAdapter = new RecentMatchesAdapter(view.getContext(), arrayList);

        recentMatches.setAdapter(recentMatchAdapter);

        recentMatchAdapter.notifyDataSetChanged();

        recentMatches.invalidate();
    }
}