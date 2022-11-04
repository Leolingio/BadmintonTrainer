package com.sensolic.badmintontrainer.data;

import android.content.Context;
import android.widget.Toast;

import com.sensolic.badmintontrainer.BuildConfig;
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.registerMatch.InputFilterScore;
import com.sensolic.badmintontrainer.registerMatch.RegisterMatchActivity;
import com.sensolic.badmintontrainer.search.Searchable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public class Storage {

    public static final int PLAYER_ID_DIGITS = 4;
    public static final int MATCH_ID_DIGITS = 6;
    private static final boolean resetAll = false;
    private static final boolean resetPlayers = false;
    private static final boolean resetMatches = false;
    private final boolean showToasts = true;
    private static Storage instance;
    private long lastMatchID;
    public static boolean isSaved = false;      // If Positions of Characters are saved
    private final Context context;
    private ArrayList<Long> matchIDs = new ArrayList<>();
    private ArrayList<Long> playerIDs = new ArrayList<>();

    private Storage(Context applicationContext) {
        context = applicationContext;

        File file;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                file = new File(context.getFilesDir().getAbsolutePath() + "/data");
            } else if (i == 1) {
                file = new File(context.getFilesDir().getAbsolutePath() + "/matches");
            } else {
                file = new File(context.getFilesDir().getAbsolutePath() + "/players");
            }
            if((resetAll || resetPlayers) && i == 2) file.delete();
            if((resetAll || resetMatches) && i == 1){
                file.delete();
            }

            try {
                if (!file.exists()) {
                    file.delete();
                    file.createNewFile();

                    if (i == 2) {
                        file = new File(context.getFilesDir().getAbsolutePath() + "/players");
                        FileWriter w = new FileWriter(file);
                        BufferedWriter writer = new BufferedWriter(w);
                        Player[] players = new Player[]{
                            new Player(1302, "Daniil Pindiurin", 0, 0, -1, 'r'),
                            new Player(1303, "Rouven Wulandoko", 0, 0, -1, 'r'),
                            new Player(1304, "Aurelia Wulandoko", 0, 0, -1, 'r'),
                            new Player(1305, "Leo Hofmann", 0, 0, -1, 'r'),
                            new Player(1306, "Luna Schmid", 0, 0, -1, 'r'),
                            new Player(1307, "Jonas Schmid", 0, 0, -1, 'r'),
                            new Player(1308, "Mian Khan", 0, 0, -1, 'r')
                        };
                        for (Player player : players) {
                            writer.write(convertPlayerToEntry(player) + "\n");
                        }

                        writer.close();
                        w.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Searchable toAdd;
        String buffer = "";         // Here the entries will be loaded to
        char c;
        try {
            FileReader reader;
            for (int k = 0; k <= 1; k++) {
                if (k == 0) {
                    file = new File(context.getFilesDir().getAbsolutePath() + "/matches");
                } else {
                    file = new File(context.getFilesDir().getAbsolutePath() + "/players");
                }
                reader = new FileReader(file);
                while (reader.ready()) {
                    c = (char) reader.read();
                    if (c == '{') {
                        buffer = c + "";
                    } else if (c == '}') {
                        buffer = buffer + c;
                        toAdd = convertEntryToObject(buffer);
                        if (toAdd instanceof Match){
                            matchIDs.add(((Match) toAdd).getMatchID());
                        } else if(toAdd instanceof Player){
                            playerIDs.add(((Player) toAdd).getPlayerID());
                        }
                    } else {
                        buffer = buffer + c;
                    }
                }
            }
        } catch (Exception e) {
            // ignored
        }
        Comparator<Long> comp= (l1, l2) -> Math.toIntExact(l1-l2);
        playerIDs.sort(comp);
        matchIDs.sort(comp);
        if(matchIDs.size() != 0) lastMatchID = matchIDs.get(matchIDs.size()-1);
    }

    public static Storage getInstance(Context context) {
        if (instance == null) {
            instance = new Storage(context);
        }
        return instance;
    }

    public void showChangelog() {
        // Updating appVersion in file
        if (!changeSetting("lastAppVersion", BuildConfig.VERSION_CODE + "")) {
            if (!addSetting("lastAppVersion", BuildConfig.VERSION_CODE + "")) {
                System.out.println("ERROR in Saving AppVersion");
            }
        }
        // Showing Dialog
        StatsActivity.showChangelog();
    }

    /**
     * This method saves the position of the given character
     *
     * @param character Name of the character
     * @param position  Poasition of the character
     */
    public void storePos(String character, float[] position) {

        if (position.length != 2) return;

        String toStore = character + ":" + position[0] + "-" + position[1] + ";";

        if (!changeSetting(character, position[0] + "-" + position[1])) {
            if (!addSetting(character, position[0] + "-" + position[1])) {
                System.out.println("ERROR in Storing Position");
            }
        }

        isSaved = true;
    }

    /**
     * This method gets the saved position of the given character
     *
     * @param character Name of the character
     * @return Position of the character
     */
    public float[] getPos(String character) {
        boolean resume = true;
        float[] result = new float[2];
        String buffer = "";
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/data");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (resume) {
                buffer = reader.readLine();
                if (buffer == null) return null;
                if (buffer.contains(character)) resume = false;
            }
            buffer = buffer.substring(buffer.indexOf(":"), buffer.indexOf(";"));
            result[0] = Float.parseFloat(buffer.substring(1, buffer.indexOf("-")));
            result[1] = Float.parseFloat(buffer.substring(buffer.indexOf("-") + 1));
            reader.close();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * This method deletes the dataFile
     *
     * @param filename Name of the file to be reset
     */
    public void resetFile(String filename) {
        File toReset = new File(context.getFilesDir().getAbsolutePath() + "/" + filename);
        if (toReset.exists()) {
            if (!toReset.delete()) {
                if (showToasts) {
                    Toast.makeText(context, "Error while deleting " + filename + "  file", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            if (showToasts) {
                Toast.makeText(context, filename + " file does not exist already", Toast.LENGTH_SHORT).show();
            }
        }
        try {
            toReset.createNewFile();
        } catch (Exception ignored) {
            if (showToasts) {
                Toast.makeText(context, filename + " File could not be created", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method saves the settings
     */
    public void saveSettings() {

        if (!changeSetting("manualStartPos", Settings.manualStartPos() + "")) {
            if (!addSetting("manualStartPos", Settings.manualStartPos() + "")) {
                System.out.println("ERROR in Saving Settings");
            }
        }

        if (!changeSetting("debugMode", Settings.debugMode() + "")) {
            if (!addSetting("debugMode", Settings.debugMode() + "")) {
                System.out.println("ERROR in Saving Settings");
            }
        }
        if (!changeSetting("autocompleteScore", Settings.autocompleteScore() + "")) {
            if (!addSetting("autocompleteScore", Settings.autocompleteScore() + "")) {
                System.out.println("ERROR in Saving Settings");
            }
        }
        if (!changeSetting("defaultMatchType", Settings.getDefaultMatchType())) {
            if (!addSetting("defaultMatchType", Settings.getDefaultMatchType())) {
                System.out.println("ERROR in Saving Settings");
            }
        }
    }

    /**
     * Changes an existing Setting value
     *
     * @param identifier Name of the setting
     * @param value      Value of the Setting
     * @return If the operation was successful
     */
    private boolean changeSetting(String identifier, String value) {
        String content = "";
        String newContent = identifier + ":" + value + ";";
        String buffer;
        boolean successful = false;
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/data");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while (buffer != null) {
                if (buffer.contains(identifier)) {
                    content = content + newContent + System.lineSeparator();
                    successful = true;
                } else {
                    content = content + buffer + System.lineSeparator();
                }
                buffer = reader.readLine();
            }
            resetFile("data");

            FileWriter w = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(content);

            reader.close();
            writer.close();
        } catch (Exception e) {
            return false;
        }
        return successful;
    }

    /**
     * Adds an Entry to the data file
     *
     * @param identifier Name of the Entry
     * @param value      Value of the Entry
     * @return If the operation was successful
     */
    private boolean addSetting(String identifier, String value) {
        String content = "";
        String newContent = identifier + ":" + value + ";";
        String buffer;
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/data");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while (buffer != null) {
                content = content + buffer + System.lineSeparator();
                buffer = reader.readLine();
            }

            resetFile("data");

            FileWriter w = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(content);
            writer.append(newContent);

            reader.close();
            writer.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * This method gets the saved settings
     *
     * @return String with all information belonging to settings
     */
    public String getSettings() {
        String buffer;
        String result = "";
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/data");
            BufferedReader r = new BufferedReader(new FileReader(file));
            BufferedReader reader = new BufferedReader(r);
            buffer = reader.readLine();
            while (buffer != null) {
                result = result + buffer;
                buffer = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (result.length() == 0) return null;
        return result;
    }

    /**
     * This method deletes a match
     *
     * @param ID ID of the match
     */
    public boolean deleteMatch(long ID) {
        // Adjust player profile stats
        Match toDelete = getMatchData(ID);
        Player team1player1, team1player2, team2player1, team2player2;
        if(toDelete.getMatchType() == 'S'){
            team1player1 = toDelete.getTeam1Player1();
            team2player1 = toDelete.getTeam2Player1();
            team1player1.setRankingPoints(team1player1.getRankingPoints()-toDelete.getTeam1Player1points());
            team2player1.setRankingPoints(team2player1.getRankingPoints()-toDelete.getTeam2Player1points());

            team1player1.setMatchesPlayed(team1player1.getMatchesPlayed()-1);
            team2player1.setMatchesPlayed(team2player1.getMatchesPlayed()-1);

            storePlayer(team1player1);
            storePlayer(team2player1);
        } else{
            team1player1 = toDelete.getTeam1Player1();
            team1player2 = toDelete.getTeam1Player2();
            team2player1 = toDelete.getTeam2Player1();
            team2player2 = toDelete.getTeam2Player2();
            team1player1.setRankingPoints(team1player1.getRankingPoints()-toDelete.getTeam1Player1points());
            team1player2.setRankingPoints(team1player2.getRankingPoints()-toDelete.getTeam1Player2points());
            team2player1.setRankingPoints(team2player1.getRankingPoints()-toDelete.getTeam2Player1points());
            team2player2.setRankingPoints(team2player2.getRankingPoints()-toDelete.getTeam2Player2points());

            team1player1.setMatchesPlayed(team1player1.getMatchesPlayed()-1);
            team1player2.setMatchesPlayed(team1player2.getMatchesPlayed()-1);
            team2player1.setMatchesPlayed(team2player1.getMatchesPlayed()-1);
            team2player2.setMatchesPlayed(team2player2.getMatchesPlayed()-1);

            storePlayer(team1player1);
            storePlayer(team1player2);
            storePlayer(team2player1);
            storePlayer(team2player2);
        }

        String content = "";
        String read;
        String buffer = "";         // Here the matchEntry will be loaded to
        String toCompare;      // Here the substrings of buffer will be saved
        boolean matchfound = false, result = false;
        char c;
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/matches");
            FileReader reader = new FileReader(file);
            while (reader.ready()) {
                c = (char) reader.read();
                if (c == '{') {
                    buffer = c + "";
                } else if (c == '}') {
                    buffer = buffer + c;
                    read = buffer;

                    read = read.replaceAll(" ", "");
                    read = read.replaceAll("\n", "");

                    toCompare = read.substring(1, read.indexOf(';'));
                    read = read.substring(read.indexOf(';') + 1);
                    do {
                        if (toCompare.substring(0, toCompare.indexOf(':')).equals("id")
                                && toCompare.substring(toCompare.indexOf(':') + 1).equals(String.valueOf(ID))) {
                            matchfound = true;
                            result = true;
                            break;
                        }
                        if (!read.equals("}")) {
                            toCompare = read.substring(0, read.indexOf(';'));
                            read = read.substring(read.indexOf(';') + 1);
                        } else toCompare = "";
                    } while (toCompare.length() != 0);
                    if (!matchfound) {
                        content = content + buffer;
                    } else matchfound = false;
                } else {
                    buffer = buffer + c;
                }
            }

            resetFile("matches");

            FileWriter w = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(content);

            reader.close();
            writer.close();
        } catch (Exception e) {
            // ignored
        }
        if(result) matchIDs.remove(ID);
        return result;
    }

    public long getLastMatchID() {
        if (lastMatchID != 0) return lastMatchID;
        else return (long) Math.pow(10, MATCH_ID_DIGITS-1);
    }

    public void registerMatchID(long matchID) {
        lastMatchID = matchID;
        matchIDs.add(lastMatchID);
    }

    /**
     * Adds all matches stored in matches-file to the Arraylist from parameter
     *
     * @param list ArrayList where loaded matches should be added to
     */
    public void addStoredObjects(ArrayList<Searchable> list) {
        Searchable toAdd;
        String buffer = "";         // Here the entries will be loaded to
        char c;
        try {
            File file;
            FileReader reader;
            for (int i = 0; i <= 1; i++) {
                if (i == 0) {
                    file = new File(context.getFilesDir().getAbsolutePath() + "/matches");
                } else {
                    file = new File(context.getFilesDir().getAbsolutePath() + "/players");
                }
                reader = new FileReader(file);
                while (reader.ready()) {
                    c = (char) reader.read();
                    if (c == '{') {
                        buffer = c + "";
                    } else if (c == '}') {
                        buffer = buffer + c;
                        toAdd = convertEntryToObject(buffer);
                        if (toAdd != null) list.add(toAdd);
                    } else {
                        buffer = buffer + c;
                    }
                }
            }
        } catch (Exception e) {
            // ignored
        }
    }

    private Searchable convertEntryToObject(String entryCode) {
        long ID = 0, team1player1 = 0, team1player2 = 0, team2player1 = 0, team2player2 = 0,
                tournamentID = 0, leagueID = 0;
        char matchType = '0', mainHand = '0';
        int setCount = 0, firstSetTeamOne = 0, firstSetTeamTwo = 0, secondSetTeamOne = 0, secondSetTeamTwo = 0,
                thirdSetTeamOne = 0, thirdSetTeamTwo = 0, rankingPoints = 0, matchesPlayed = 0, teamNumber = 0,
                team1player1points = 0, team1player2points = 0, team2player1points = 0, team2player2points = 0;
        String objectType = "";
        String matchDependency = "", playerName = "";

        String buffer1;

        entryCode = entryCode.replaceAll(" ", "");
        entryCode = entryCode.replaceAll("\n", "");

        String buffer2 = entryCode;

        buffer1 = buffer2.substring(1, buffer2.indexOf(';'));
        buffer2 = buffer2.substring(buffer2.indexOf(';') + 1);
        String value = buffer1.substring(buffer1.indexOf(':') + 1);

        do {
            switch (buffer1.substring(0, buffer1.indexOf(':'))) {
                case "objectType":
                    objectType = value;
                    break;
                case "id":
                    ID = Long.parseLong(value);
                    if (ID < 0) return null;
                    break;
                case "matchType":
                    if (value.equals("Singles")) {
                        matchType = 'S';
                    } else if (value.equals("Doubles")) {
                        matchType = 'D';
                    } else return null;
                    break;
                case "team1player1":
                    team1player1 = Long.parseLong(value);
                    if (team1player1 < 0) return null;
                    break;
                case "team2player1":
                    team2player1 = Long.parseLong(value);
                    if (team2player1 < 0) return null;
                    break;
                case "team1player2":
                    team1player2 = Long.parseLong(value);
                    if (team1player2 < 0) return null;
                    break;
                case "team2player2":
                    team2player2 = Long.parseLong(value);
                    if (team2player2 < 0) return null;
                    break;
                case "team1player1points":
                    team1player1points = Integer.parseInt(value);
                    if (team1player1points < 0) return null;
                    break;
                case "team2player1points":
                    team2player1points = Integer.parseInt(value);
                    if (team2player1points < 0) return null;
                    break;
                case "team1player2points":
                    team1player2points = Integer.parseInt(value);
                    if (team1player2points < 0) return null;
                    break;
                case "team2player2points":
                    team2player2points = Integer.parseInt(value);
                    if (team2player2points < 0) return null;
                    break;
                case "setCount":
                    setCount = Integer.parseInt(value);
                    if (setCount < 2 || setCount > 4) return null;
                    break;
                case "firstSetTeamOne":
                    firstSetTeamOne = Integer.parseInt(value);
                    break;
                case "firstSetTeamTwo":
                    firstSetTeamTwo = Integer.parseInt(value);
                    break;
                case "secondSetTeamOne":
                    secondSetTeamOne = Integer.parseInt(value);
                    break;
                case "secondSetTeamTwo":
                    secondSetTeamTwo = Integer.parseInt(value);
                    break;
                case "thirdSetTeamOne":
                    thirdSetTeamOne = Integer.parseInt(value);
                    break;
                case "thirdSetTeamTwo":
                    thirdSetTeamTwo = Integer.parseInt(value);
                    break;
                case "matchDependency":
                    matchDependency = value;
                    break;
                case "tournamentID":
                    tournamentID = Long.parseLong(value);
                    if (tournamentID < 0) return null;
                    break;
                case "leagueID":
                    leagueID = Long.parseLong(value);
                    if (leagueID < 0) return null;
                    break;
                case "teamNumber":
                    teamNumber = Integer.parseInt(value);
                    if (teamNumber < 0) teamNumber = -1;
                    break;
                case "playerName":
                    playerName = value;
                    break;
                case "rankingPoints":
                    rankingPoints = Integer.parseInt(value);
                    break;
                case "matchesPlayed":
                    matchesPlayed = Integer.parseInt(value);
                    break;
                case "mainHand":
                    if (value.equals("right")) {
                        matchType = 'R';
                    } else if (value.equals("left")) {
                        matchType = 'L';
                    } else matchType = 'U';
                    break;
            }
            if (!buffer2.equals("}")) {
                buffer1 = buffer2.substring(0, buffer2.indexOf(';'));
                buffer2 = buffer2.substring(buffer2.indexOf(';') + 1);
                value = buffer1.substring(buffer1.indexOf(':') + 1);
            } else buffer1 = "";

        } while (!buffer1.isEmpty());

        if (objectType.equals("match")) {
            long[] players;
            int[] points;
            if (matchType == 'S') {
                players = new long[2];
                points = new int[2];
                players[0] = team1player1;
                players[1] = team2player1;
                points[0] = team1player1points;
                points[1] = team2player1points;
            } else {
                players = new long[4];
                points = new int[4];
                players[0] = team1player1;
                players[1] = team1player2;
                players[2] = team2player1;
                players[3] = team2player2;
                points[0] = team1player1points;
                points[1] = team1player2points;
                points[2] = team2player1points;
                points[3] = team2player2points;
            }
            String[] scores = new String[setCount];
            if (!InputFilterScore.checkScore(firstSetTeamOne, firstSetTeamTwo)) return null;
            scores[0] = firstSetTeamOne + ":" + firstSetTeamTwo;
            if (!InputFilterScore.checkScore(secondSetTeamOne, secondSetTeamTwo)) return null;
            scores[1] = secondSetTeamOne + ":" + secondSetTeamTwo;
            if (setCount == 3) {
                if (!InputFilterScore.checkScore(thirdSetTeamOne, thirdSetTeamTwo)) return null;
                scores[2] = thirdSetTeamOne + ":" + thirdSetTeamTwo;
            }
            switch (matchDependency) {
                case "Ranking":
                    return new Match(instance, ID, matchType, players, setCount, scores, points);
                case "Tournament":
                    return new Match(instance, ID, matchType, players, setCount, scores, points, tournamentID);
                case "League":
                    return new Match(instance, ID, matchType, players, setCount, scores, points, leagueID, teamNumber);
                default:
                    return null;
            }
        } else if (objectType.equals("player")) {
            return new Player(ID, playerName, rankingPoints, matchesPlayed, teamNumber, mainHand);
        }
        return null;
    }

    public void storeMatch(Match match) {
        // If already saved then just update the existing data
        if (containsMatch(match)) updateMatch(match);
        else {   // Create new entry for the match
            String buffer;
            String toStore = convertMatchToEntry(match);

            String content = "";
            try {
                File file = new File(context.getFilesDir().getAbsolutePath() + "/matches");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                buffer = reader.readLine();
                while (buffer != null) {
                    content = content + buffer + System.lineSeparator();
                    buffer = reader.readLine();
                }

                resetFile("matches");

                file = new File(context.getFilesDir().getAbsolutePath() + "/matches");
                FileWriter w = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(w);

                writer.write(content + toStore);

                reader.close();
                writer.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    private boolean containsMatch(Match match) {
        String buffer;
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/matches");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while (buffer != null) {
                if (buffer.contains("id:" + match.getMatchID())) {
                    return true;
                }
                buffer = reader.readLine();
            }
        } catch (Exception e) {
            //ignored
        }
        return false;
    }

    private String convertMatchToEntry(Match match) {
        String buffer;
        String toStore = "{" + System.lineSeparator() + "objectType:match;" + System.lineSeparator();

        // Add matchID attribute
        toStore = toStore + "id:" + match.getMatchID() + ";" + System.lineSeparator();

        // Add matchType attribute
        buffer = "matchType:";
        if (match.getMatchType() == 'S') {
            buffer = buffer + "Singles";
        } else {
            buffer = buffer + "Doubles";
        }
        toStore = toStore + buffer + ";" + System.lineSeparator();

        // Add player attributes
        toStore = toStore + "team1player1:" + match.getTeam1Player1ID() + ";" + System.lineSeparator();
        toStore = toStore + "team2player1:" + match.getTeam2Player1ID() + ";" + System.lineSeparator();
        if (match.getMatchType() == 'D') {
            toStore = toStore + "team1player2:" + match.getTeam1Player2ID() + ";" + System.lineSeparator();
            toStore = toStore + "team2player2:" + match.getTeam2Player2ID() + ";" + System.lineSeparator();
        }

        // Points
        toStore = toStore + "team1player1points:" + match.getTeam1Player1points() + ";" + System.lineSeparator();
        toStore = toStore + "team2player1points:" + match.getTeam2Player1points() + ";" + System.lineSeparator();
        if (match.getMatchType() == 'D') {
            toStore = toStore + "team1player2points:" + match.getTeam1Player2points() + ";" + System.lineSeparator();
            toStore = toStore + "team2player2points:" + match.getTeam2Player2points() + ";" + System.lineSeparator();
        }

        // Add set attributes
        toStore = toStore + "setCount:" + match.getSetCount() + ";" + System.lineSeparator();
        buffer = match.getScoreFirst();
        toStore = toStore + "firstSetTeamOne:" + buffer.substring(0, buffer.indexOf(':')) + ";" + System.lineSeparator();
        toStore = toStore + "firstSetTeamTwo:" + buffer.substring(buffer.indexOf(':') + 1) + ";" + System.lineSeparator();
        buffer = match.getScoreSecond();
        toStore = toStore + "secondSetTeamOne:" + buffer.substring(0, buffer.indexOf(':')) + ";" + System.lineSeparator();
        toStore = toStore + "secondSetTeamTwo:" + buffer.substring(buffer.indexOf(':') + 1) + ";" + System.lineSeparator();
        if (match.getSetCount() == 3) {
            buffer = match.getScoreThird();
            toStore = toStore + "thirdSetTeamOne:" + buffer.substring(0, buffer.indexOf(':')) + ";" + System.lineSeparator();
            toStore = toStore + "thirdSetTeamTwo:" + buffer.substring(buffer.indexOf(':') + 1) + ";" + System.lineSeparator();
        }

        // Adding match dependency
        toStore = toStore + "matchDependency:" + match.getMatchDependency() + ";" + System.lineSeparator();
        if (match.getMatchDependency().equals("Tournament")) {
            toStore = toStore + "tournamentID:" + match.getTournamentID() + ";" + System.lineSeparator();
        } else if (match.getMatchDependency().equals("League")) {
            toStore = toStore + "leagueID:" + match.getLeagueID() + ";" + System.lineSeparator();
            toStore = toStore + "teamNumber:" + match.getTeamNumber() + ";" + System.lineSeparator();
        }
        toStore = toStore + "}";

        return toStore;
    }

    private String convertPlayerToEntry(Player player) {
        String buffer;
        String toStore = "{" + System.lineSeparator() + "objectType:player;" + System.lineSeparator();

        // Add playerID attribute
        toStore = toStore + "id:" + player.getPlayerID() + ";" + System.lineSeparator();

        // Add playerName attribute
        toStore = toStore + "playerName:" + player.getPlayerName() + ";" + System.lineSeparator();

        // Add rankingPoints attribute
        toStore = toStore + "rankingPoints:" + player.getRankingPoints() + ";" + System.lineSeparator();

        // Add matchesPlayed attribute
        toStore = toStore + "matchesPlayed:" + player.getMatchesPlayed() + ";" + System.lineSeparator();

        // Add teamNumber attribute
        toStore = toStore + "teamNumber:" + player.getTeamNumber() + ";" + System.lineSeparator();

        // Add mainHand attribute
        if (player.getMainHand() == 'r') {
            toStore = toStore + "mainHand:right;" + System.lineSeparator();
        } else if (player.getMainHand() == 'l') {
            toStore = toStore + "mainHand:left;" + System.lineSeparator();
        } else {
            toStore = toStore + "mainHand:unknown;" + System.lineSeparator();
        }
        toStore = toStore + "}";

        return toStore;
    }

    private void updateMatch(Match match) {
        String content = "";
        String newContent = convertMatchToEntry(match);
        String read;
        String buffer = "";         // Here the matchEntry will be loaded to
        String toCompare;      // Here the substrings of buffer will be saved
        boolean matchfound = false;
        char c;
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + "/matches");
            FileReader reader = new FileReader(file);

            while (reader.ready()) {
                c = (char) reader.read();
                if (c == '{') {
                    buffer = c + "";
                    read = buffer;
                } else if (c == '}') {
                    buffer = buffer + c;
                    read = buffer;

                    read = read.replaceAll(" ", "");
                    read = read.replaceAll("\n", "");

                    toCompare = read.substring(1, read.indexOf(';'));
                    read = read.substring(read.indexOf(';') + 1);
                    do {
                        if (toCompare.substring(0, toCompare.indexOf(':')).equals("id")
                                && toCompare.substring(buffer.indexOf(':') + 1).equals(String.valueOf(match.getMatchID()))) {
                            content = content + newContent;
                            matchfound = true;
                            break;
                        }
                        if (!read.equals("}")) {
                            toCompare = read.substring(0, read.indexOf(';'));
                            read = read.substring(read.indexOf(';') + 1);
                        } else toCompare = "";
                    } while (toCompare.length() != 0);
                    if (matchfound) {
                        content = content + buffer;
                        matchfound = false;
                    }
                } else {
                    buffer = buffer + c;
                }
            }

            resetFile("matches");

            FileWriter w = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(content);

            reader.close();
            writer.close();
        } catch (Exception e) {
            // ignored
        }
    }

    public Player getPlayerData(long playerID) {
        Searchable toAdd;
        String buffer = "";         // Here the playerEntry will be loaded to
        char c;
        try {
            File file;
            FileReader reader;

            file = new File(context.getFilesDir().getAbsolutePath() + "/players");

            reader = new FileReader(file);
            while (reader.ready()) {
                c = (char) reader.read();
                if (c == '{') {
                    buffer = c + "";
                } else if (c == '}') {
                    buffer = buffer + c;
                    toAdd = convertEntryToObject(buffer);
                    if (toAdd instanceof Player) {
                        if (((Player) toAdd).getPlayerID() == playerID) {
                            return (Player) toAdd;
                        }
                    }
                } else {
                    buffer = buffer + c;
                }
            }
        } catch (Exception e) {
            // ignored
        }
        return null;
    }
}
