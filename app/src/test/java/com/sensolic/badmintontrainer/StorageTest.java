package com.sensolic.badmintontrainer;

import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;
import android.media.metrics.PlaybackErrorEvent;

import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;

public class StorageTest {
    Storage storage = Storage.getInstance(new StatsActivity().getApplicationContext());
    Player[] testPlayers = new Player[]{
            new Player(0, "TestPlayer0", 0, 0 ,-1, 'r'),
            new Player(1, "TestPlayer1", 0, 0 ,-1, 'r'),
            new Player(2, "TestPlayer2", 0, 0 ,-1, 'r'),
            new Player(3, "TestPlayer3", 0, 0 ,-1, 'r'),
            new Player(4, "TestPlayer4", 0, 0 ,-1, 'r'),
            new Player(5, "TestPlayer5", 0, 0 ,-1, 'r'),
            new Player(6, "TestPlayer6", 0, 0 ,-1, 'r'),
            new Player(7, "TestPlayer7", 0, 0 ,-1, 'r'),
            new Player(8, "TestPlayer8", 0, 0 ,-1, 'r'),
            new Player(9, "TestPlayer9", 0, 0 ,-1, 'r')
    };

    @Test
    public void newMatch() {
        for(Player p : testPlayers) {
            storage.storePlayer(p);
        }
        Match[] matches = new Match[testPlayers.length-1];
        for(int i = 0; i < testPlayers.length-1; i++) {
            matches[i] = new Match(storage, i, 'S', new long[]{i, i+1},
                    2, new String[]{"0:21", "0:21"}, new int[]{5, 0});
        }
        Match mCompare;
        for(Match m : matches){
            mCompare = storage.getMatchData(m.getMatchID());
            assertEquals(mCompare.getMatchID(),m.getMatchID());
            assertEquals(mCompare.getTeam1Player1ID(),m.getTeam1Player1ID());
            assertEquals(mCompare.getTeam1Player2ID(),m.getTeam1Player1ID());
            assertEquals(mCompare.getTeam2Player1ID(),m.getTeam1Player1ID());
            assertEquals(mCompare.getTeam2Player2ID(),m.getTeam1Player1ID());
        }
        for(Player p : testPlayers) {
            storage.deletePlayer(p.getPlayerID());
        }
        for(Match m : matches) {
            storage.deleteMatch(m.getMatchID());
        }
    }

}