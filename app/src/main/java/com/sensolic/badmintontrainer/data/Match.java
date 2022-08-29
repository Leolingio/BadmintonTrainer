package com.sensolic.badmintontrainer.data;

import com.sensolic.badmintontrainer.search.Searchable;

public class Match implements Searchable {

    Storage storage;

    /**
     * ID of the match without #M
     */
    private long matchID;

    /**
     * Type of the match - 'S' for Singles and 'D' for Doubles
     */
    private char matchType;

    /**
     * IDs of all participating players
     */
    private long team1player1ID, team1player2ID, team2player1ID, team2player2ID;

    /**
     * Number of sets of the match - 2 or 3
     */
    private int setCount;

    /**
     * Sets saved in String format "__:__", e.g. "10:21"
     */
    private String scoreFirst, scoreSecond, scoreThird;

    /**
     * Describes if match is for "Ranking", "Tournament" or "League"
     * This is just for later usage if more functions are implemented
     */
    private String matchDependency;
    private long tournamentID;
    private long leagueID;
    private int teamNumber;

    /**
     * Creates a new match object with attributes given in parameter list
     * This constructor is only for Ranking matches
     *
     * @param matchID   ID of the match
     * @param matchType Type of the match 'S', 'D' - Singles or Doubles
     * @param playerIDs Array with all ID of participating players
     * @param setCount  number of sets
     * @param scores    Array with all scores depending on setCount
     */
    public Match(Storage storage, long matchID, char matchType, long[] playerIDs, int setCount, String[] scores) {
        this.storage = storage;

        this.matchID = matchID;
        this.matchType = matchType;

        // Read our playerIDs
        if(matchType == 'S'){
            team1player1ID = playerIDs[0];
            team2player1ID = playerIDs[1];
        } else{
            team1player1ID = playerIDs[0];
            team1player2ID = playerIDs[1];
            team2player1ID = playerIDs[2];
            team2player2ID = playerIDs[3];
        }

        this.setCount = setCount;

        // Read our set count
        scoreFirst = scores[0];
        scoreSecond = scores[1];
        if(setCount == 3 && scores.length == 3) scoreThird = scores[2];

        matchDependency = "Ranking";
    }

    /**
     * Creates a new match object with attributes given in parameter list
     * This constructor is only for League matches
     *
     * @param matchID    ID of the match
     * @param matchType  Type of the match 'S', 'D' - Singles or Doubles
     * @param playerIDs  Array with all ID of participating players
     * @param setCount   number of sets
     * @param scores     Array with all scores depending on setCount
     * @param leagueID   ID of the league
     * @param teamNumber number of the team the player is playing in
     */
    public Match(Storage storage, long matchID, char matchType, long[] playerIDs, int setCount, String[] scores,
                 long leagueID, int teamNumber) {
        this.storage = storage;

        this.matchID = matchID;
        this.matchType = matchType;

        // Read our playerIDs
        if(matchType == 'S'){
            team1player1ID = playerIDs[0];
            team2player1ID = playerIDs[1];
        } else{
            team1player1ID = playerIDs[0];
            team1player2ID = playerIDs[1];
            team2player1ID = playerIDs[2];
            team2player2ID = playerIDs[3];
        }

        this.setCount = setCount;

        // Read our set count
        scoreFirst = scores[0];
        scoreSecond = scores[1];
        if(setCount == 3) scoreThird = scores[2];

        this.leagueID = leagueID;
        this.teamNumber = teamNumber;

        matchDependency = "League";
    }

    /**
     * Creates a new match object with attributes given in parameter list
     * This constructor is only for Tournament matches
     *
     * @param matchID      ID of the match
     * @param matchType    Type of the match 'S', 'D' - Singles or Doubles
     * @param playerIDs    Array with all ID of participating players
     * @param setCount     number of sets
     * @param scores       Array with all scores depending on setCount
     * @param tournamentID ID of the tournament
     */
    public Match(Storage storage, long matchID, char matchType, long[] playerIDs, int setCount, String[] scores,
                 long tournamentID) {
        this.storage = storage;

        this.matchID = matchID;
        this.matchType = matchType;

        // Read our playerIDs
        if(matchType == 'S'){
            team1player1ID = playerIDs[0];
            team2player1ID = playerIDs[1];
        } else{
            team1player1ID = playerIDs[0];
            team1player2ID = playerIDs[1];
            team2player1ID = playerIDs[2];
            team2player2ID = playerIDs[3];
        }

        this.setCount = setCount;

        // Read our set count
        scoreFirst = scores[0];
        scoreSecond = scores[1];
        if(setCount == 3 && scores.length == 3) scoreThird = scores[2];

        this.tournamentID = tournamentID;

        matchDependency = "Tournament";
    }

    /**
     * Returns the info that should be displayed by default
     *
     * @return matchType in String format or null if the matchType is invalid
     */
    @Override
    public String getInfo() {
        String result;
        if (matchType == 'S') {
            result = "Singles Match";
        } else if (matchType == 'D') {
            result = "Doubles Match";
        } else result = null;
        return result;
    }

    /**
     * Returns the ID of the object with objectType-identifier
     *
     * @return "#M" + matchID
     */
    @Override
    public String getIDInfo() {
        return "#M" + matchID;
    }

    public long getMatchID() {
        return matchID;
    }

    public char getMatchType() {
        return matchType;
    }

    public long getTeam1Player1ID() {
        return team1player1ID;
    }

    public long getTeam1Player2ID() {
        return team1player2ID;
    }

    public long getTeam2Player1ID() {
        return team2player1ID;
    }

    public long getTeam2Player2ID() {
        return team2player2ID;
    }

    public int getSetCount() {
        return setCount;
    }

    public String getScoreFirst() {
        return scoreFirst;
    }

    public String getScoreSecond() {
        return scoreSecond;
    }

    public String getScoreThird() {
        return scoreThird;
    }

    public String getMatchDependency() {
        return matchDependency;
    }

    public long getTournamentID() {
        return tournamentID;
    }

    public long getLeagueID() {
        return leagueID;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public Player getTeam1Player1(){
        return storage.getPlayerData(team1player1ID);
    }

    public Player getTeam1Player2(){
        return storage.getPlayerData(team1player2ID);
    }

    public Player getTeam2Player1(){
        return storage.getPlayerData(team2player1ID);
    }

    public Player getTeam2Player2(){
        return storage.getPlayerData(team2player2ID);
    }

}
