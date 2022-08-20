package com.sensolic.badmintontrainer.data;

import com.sensolic.badmintontrainer.search.Searchable;

public class Player implements Searchable {

    /**
     *  ID of the Player without #P
     */
    private long playerID;

    /**
     *  Name of the player in format FIRSTNAME,LASTNAME
     */
    private String playerName = "";

    /**
     *  Current ranking points of the player
     */
    private int rankingPoints;

    /**
     *  Number of matches the player played
     */
    private int matchesPlayed;

    /**
     *  Number of team, the player is playing in
     *  -1 if currently no team
     */
    private int teamNumber;

    /**
     *  If player is right-handed 'R' or left-handed 'L' or unknown 'U'
     */
    private char mainHand;

    /**
     *  Creates a new player object with attributes given in parameter list
     */
    public Player(long playerID, String playerName, int rankingPoints, int matchesPlayed, int teamNumber, char mainHand){
        this.playerID = playerID;
        if(!playerName.contains(" ")){
            for(int i = 0; i < playerName.length(); i++) {
                if (i != 0) {
                    if (Character.isUpperCase(playerName.charAt(i))) {
                        this.playerName = this.playerName + " " + playerName.charAt(i);
                    } else {
                        this.playerName = this.playerName + playerName.charAt(i);
                    }
                } else {
                    this.playerName = this.playerName + playerName.charAt(0);
                }
            }
        } else{
            this.playerName = playerName;
        }
        this.rankingPoints = Math.max(rankingPoints, 0);
        this.matchesPlayed = Math.max(matchesPlayed,0);
        this.teamNumber = Math.max(teamNumber,-1);
        if(mainHand == 'L' || mainHand == 'R') this.mainHand = mainHand;
        else mainHand = 'U';
    }

    /**
     *  Returns the info that should be displayed by default
     * @return String with playerName
     */
    @Override
    public String getInfo() {
        return playerName;
    }

    /**
     *  Returns the ID of the object with objectType-identifier
     * @return "#P" + playerID
     */
    @Override
    public String getIDInfo() {
        return "#P"+playerID;
    }

    public long getPlayerID() {
        return playerID;
    }

    public void setPlayerID(long playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getRankingPoints() {
        return rankingPoints;
    }

    public void setRankingPoints(int rankingPoints) {
        this.rankingPoints = rankingPoints;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public char getMainHand() {
        return mainHand;
    }

    public void setMainHand(char mainHand) {
        this.mainHand = mainHand;
    }
}
