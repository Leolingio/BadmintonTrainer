package com.sensolic.badmintontrainer.data;

import android.content.Context;
import android.widget.Toast;

import com.sensolic.badmintontrainer.BuildConfig;
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.registerMatch.InputFilterScore;
import com.sensolic.badmintontrainer.search.Searchable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Storage {

    private static Storage instance;
    private long currentMatchID = -1;
    public static boolean isSaved = false;      // If Positions of Characters are saved
    private Context context;

    private Storage(Context applicationContext) {
        context = applicationContext;
        File data = new File(context.getFilesDir().getAbsolutePath()+"/data");

        try {
            if(!data.exists()) {
                data.delete();
                data.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        File matches = new File(context.getFilesDir().getAbsolutePath()+"/matches");

        try {
            if(!matches.exists()) {
                matches.delete();
                matches.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Storage getInstance(Context context){
        if(instance == null){
            instance = new Storage(context);
        }
        return instance;
    }

    public void showChangelog(){
        // Updating appVersion in file
        if(!changeSetting("lastAppVersion",BuildConfig.VERSION_CODE+"")){
            if(!addSetting("lastAppVersion", BuildConfig.VERSION_CODE+"")){
                System.out.println("ERROR in Saving AppVersion");
            }
        }
        // Showing Dialog
        StatsActivity.showChangelog();
    }

    /**
     * This method saves the position of the given character
     * @param character Name of the character
     * @param position Poasition of the character
     */
    public void storePos(String character, float[] position){

        if(position.length!=2) return;

        String toStore = character+":" + position[0]+ "-" + position[1]+";";

        if(!changeSetting(character, position[0]+ "-" + position[1])){
            if(!addSetting(character, position[0]+ "-" + position[1])){
                System.out.println("ERROR in Storing Position");
            }
        }

        isSaved = true;
    }

    /**
     * This method gets the saved position of the given character
     * @param character Name of the character
     * @return Position of the character
     */
    public float[] getPos(String character) {
        boolean resume = true;
        float[] result = new float[2];
        String buffer = "";
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/data");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while(resume) {
                buffer = reader.readLine();
                if(buffer == null) return null;
                if(buffer.contains(character)) resume = false;
            }
            buffer = buffer.substring(buffer.indexOf(":"),buffer.indexOf(";"));
            result[0] = Float.parseFloat(buffer.substring(1,buffer.indexOf("-")));
            result[1] = Float.parseFloat(buffer.substring(buffer.indexOf("-")+1));
            reader.close();
        } catch(Exception e){
            return null;
        }
        return result;
    }

    /**
     * This method deletes the dataFile
     * @param showToast Should a toast be shown at the end
     */
    public void resetFile(boolean showToast){
        File positions = new File(context.getFilesDir().getAbsolutePath()+"/data");
        if(positions.exists()){
            if(positions.delete()){
                if(showToast) {
                    Toast.makeText(context, "Successfully deleted existing file", Toast.LENGTH_LONG).show();
                }
            }
            else{
                if(showToast) {
                    Toast.makeText(context, "Error while deleting file", Toast.LENGTH_LONG).show();
                }
            }
        }
        else {
            if(showToast) {
                Toast.makeText(context, "File does not exist already", Toast.LENGTH_SHORT).show();
            }
        }
        try {
            positions.createNewFile();
        } catch(Exception ignored){
            if (showToast) {
                Toast.makeText(context, "File could not be created", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * This method saves the settings
     */
    public void saveSettings(){

        if(!changeSetting("manualStartPos", Settings.manualStartPos()+"")){
            if(!addSetting("manualStartPos", Settings.manualStartPos()+"")){
                System.out.println("ERROR in Saving Settings");
            }
        }

        if(!changeSetting("debugMode", Settings.debugMode()+"")){
            if(!addSetting("debugMode", Settings.debugMode()+"")){
                System.out.println("ERROR in Saving Settings");
            }
        }
        if(!changeSetting("autocompleteScore", Settings.autocompleteScore()+"")){
            if(!addSetting("autocompleteScore", Settings.autocompleteScore()+"")){
                System.out.println("ERROR in Saving Settings");
            }
        }
    }

    /**
     *  Changes an existing Setting value
     * @param identifier Name of the setting
     * @param value Value of the Setting
     * @return If the operation was successful
     */
    private boolean changeSetting(String identifier, String value){
        String content = "";
        String newContent = identifier+":"+value+";";
        String buffer;
        boolean successful = false;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/data");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while(buffer != null) {
                if(buffer.contains(identifier)){
                    content = content + newContent + System.lineSeparator();
                    successful = true;
                } else {
                    content = content + buffer + System.lineSeparator();
                }
                buffer = reader.readLine();
            }
            resetFile(false);

            FileWriter w = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(content);

            reader.close();
            writer.close();
        } catch(Exception e){
            return false;
        }
        return successful;
    }

    /**
     *  Adds an Entry to the data file
     * @param identifier Name of the Entry
     * @param value Value of the Entry
     * @return If the operation was successful
     */
    private boolean addSetting(String identifier, String value){
        String content = "";
        String newContent = identifier+":"+value+";";
        String buffer;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/data");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while(buffer != null) {
                content = content + buffer + System.lineSeparator();
                buffer = reader.readLine();
            }

            resetFile(false);

            FileWriter w = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(content);
            writer.append(newContent);

            reader.close();
            writer.close();
        } catch(Exception e){
            return false;
        }
        return true;
    }

    /**
     * This method gets the saved settings
     * @return String with all information belonging to settings
     */
    public String getSettings(){
        String buffer;
        String result = "";
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/data");
            BufferedReader r = new BufferedReader(new FileReader(file));
            BufferedReader reader = new BufferedReader(r);
            buffer = reader.readLine();
            while(buffer != null) {
                result = result + buffer;
                buffer = reader.readLine();
            }
            reader.close();
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
        if(result.length() == 0) return null;
        return result;
    }

    /**
     * This method deletes a match
     * @param ID ID of the match
     */
    public boolean deleteMatch(long ID){
        String content = "";
        String read;
        String buffer = "";         // Here the matchEntry will be loaded to
        String toCompare;      // Here the substrings of buffer will be saved
        boolean matchfound = false, result = false;
        char c;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/matches");
            FileReader reader = new FileReader(file);
            while(reader.ready()){
                c = (char) reader.read();
                if(c == '{'){
                    buffer = c+"";
                } else if(c == '}'){
                    buffer = buffer+c;
                    read = buffer;

                    read = read.replaceAll(" ","");
                    read = read.replaceAll("\n","");

                    toCompare = read.substring(1,read.indexOf(';'));
                    read = read.substring(read.indexOf(';')+1);
                    do {
                        if (toCompare.substring(0, toCompare.indexOf(':')).equals("id")
                                && toCompare.substring(toCompare.indexOf(':') + 1).equals(String.valueOf(ID))) {
                            matchfound = true;
                            result = true;
                            break;
                        }
                        if(!read.equals("}")) {
                            toCompare = read.substring(0, read.indexOf(';'));
                            read = read.substring(read.indexOf(';') + 1);
                        } else toCompare = "";
                    } while (toCompare.length() != 0);
                    if(!matchfound){
                        content = content + buffer;
                    } else matchfound = false;
                } else{
                    buffer = buffer + c;
                }
            }

            resetFile(false);

            FileWriter w = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(content);

            reader.close();
            writer.close();
        } catch(Exception e){
            // ignored
        }
        return result;
    }

    public long getCurrentMatchID(){
        if(currentMatchID != -1) return currentMatchID;
        String buffer;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/data");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while(buffer != null) {
                if(buffer.contains("currentMatchID")){
                    buffer = buffer.substring(buffer.indexOf(':')+1,buffer.length()-1);
                    return Long.parseLong(buffer);
                }
                buffer = reader.readLine();
            }
        } catch(Exception e){
            return -1;
        }
        setCurrentMatchID(1);
        return 1;
    }
    public void setCurrentMatchID(long newMatchID){
        currentMatchID = newMatchID;
        String content = "";
        String newContent = "currentMatchID"+":"+ newMatchID +";";
        String buffer;
        boolean existed = false;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/data");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while(buffer != null) {
                if(buffer.contains("currentMatchID")){
                    content = content + newContent + System.lineSeparator();
                    existed = true;
                } else {
                    content = content + buffer + System.lineSeparator();
                }
                buffer = reader.readLine();
            }
            if(!existed) content = content + newContent;

            resetFile(false);

            FileWriter w = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(content);

            reader.close();
            writer.close();
        } catch(Exception e){
            //ignored
        }
    }

    /**
     * Adds all matches stored in matches-file to the Arraylist from parameter
     * @param list  ArrayList where loaded matches should be added to
     */
    public void addStoredObjects(ArrayList<Searchable> list){
        Searchable toAdd;
        String buffer = "";         // Here the matchEntry will be loaded to
        char c;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/matches");
            FileReader reader = new FileReader(file);
            while(reader.ready()){
                c = (char) reader.read();
                if(c == '{'){
                    buffer = c+"";
                } else if(c == '}'){
                    buffer = buffer+c;
                    toAdd = convertEntryToObject(buffer);
                    if(toAdd != null) list.add(toAdd);
                } else{
                    buffer = buffer + c;
                }
            }
        } catch(Exception e){
            // ignored
        }
    }

    private Searchable convertEntryToObject(String entryCode){
        long ID = 0, playerOne = 0, playerTwo = 0, playerThree = 0, playerFour = 0,
                tournamentID = 0, leagueID = 0;
        char matchType = '0', mainHand = '0';
        int setCount = 0, firstSetTeamOne = 0, firstSetTeamTwo = 0, secondSetTeamOne = 0, secondSetTeamTwo = 0,
                thirdSetTeamOne = 0, thirdSetTeamTwo = 0, rankingPoints = 0, matchesPlayed = 0, teamNumber = 0;
        String objectType = "";
        String matchDependency = "", playerName = "";

        String buffer1;

        entryCode = entryCode.replaceAll(" ","");
        entryCode = entryCode.replaceAll("\n","");

        String buffer2 = entryCode;

        buffer1 = buffer2.substring(1,buffer2.indexOf(';'));
        buffer2 = buffer2.substring(buffer2.indexOf(';')+1);
        String value = buffer1.substring(buffer1.indexOf(':') + 1);

        do {
            switch (buffer1.substring(0, buffer1.indexOf(':'))) {
                case "objectType":
                    objectType = value;
                    break;
                case "id":
                    ID = Long.parseLong(value);
                    if(ID < 0) return null;
                    break;
                case "matchType":
                    if (value.equals("Singles")) {
                        matchType = 'S';
                    } else if (value.equals("Doubles")) {
                        matchType = 'D';
                    } else return null;
                    break;
                case "playerOne":
                    playerOne = Long.parseLong(value);
                    if(playerOne < 0) return null;
                    break;
                case "playerTwo":
                    playerTwo = Long.parseLong(value);
                    if(playerTwo < 0) return null;
                    break;
                case "playerThree":
                    playerThree = Long.parseLong(value);
                    if(playerThree < 0) return null;
                    break;
                case "playerFour":
                    playerFour = Long.parseLong(value);
                    if(playerFour < 0) return null;
                    break;
                case "setCount":
                    setCount = Integer.parseInt(value);
                    if(setCount < 2 || setCount > 4) return null;
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
                    if(tournamentID < 0) return null;
                    break;
                case "leagueID":
                    leagueID = Long.parseLong(value);
                    if(leagueID < 0) return null;
                    break;
                case "teamNumber":
                    teamNumber = Integer.parseInt(value);
                    if(teamNumber < 0) return null;
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
                    if (value.equals("R")) {
                        matchType = 'R';
                    } else if (value.equals("L")) {
                        matchType = 'L';
                    } else matchType = 'U';
                    break;
            }
            if(!buffer2.equals("}")) {
                buffer1 = buffer2.substring(0, buffer2.indexOf(';'));
                buffer2 = buffer2.substring(buffer2.indexOf(';') + 1);
                value = buffer1.substring(buffer1.indexOf(':') + 1);
            } else buffer1 = "";

        } while(!buffer1.isEmpty());

        if(objectType.equals("match")){
            long[] players;
            if(matchType == 'S'){
                players = new long[2];
                players[0] = playerOne;
                players[1] = playerTwo;
            } else {
                players = new long[4];
                players[0] = playerOne;
                players[1] = playerTwo;
                players[2] = playerThree;
                players[3] = playerFour;
            }
            String[] scores = new String[setCount];
            if(!InputFilterScore.checkScore(firstSetTeamOne,firstSetTeamTwo)) return null;
            scores[0] = firstSetTeamOne + ":" + firstSetTeamTwo;
            if(!InputFilterScore.checkScore(secondSetTeamOne,secondSetTeamTwo)) return null;
            scores[1] = secondSetTeamOne + ":" + secondSetTeamTwo;
            if(setCount == 3){
                if(!InputFilterScore.checkScore(thirdSetTeamOne,thirdSetTeamTwo)) return null;
                scores[2] = thirdSetTeamOne + ":" + thirdSetTeamTwo;
            }
            switch (matchDependency) {
                case "Ranking":
                    return new Match(ID, matchType, players, setCount, scores);
                case "Tournament":
                    return new Match(ID, matchType, players, setCount, scores, tournamentID);
                case "League":
                    return new Match(ID, matchType, players, setCount, scores, leagueID, teamNumber);
                default:
                    return null;
            }
        } else if(objectType.equals("player")){
            // TODO Add player code here, similar to match code above
        }
        return null;
    }

    public void storeMatch(Match match){
        // If already saved then just update the existing data
        if(containsMatch(match)) updateMatch(match);
        else{   // Create new entry for the match
            String buffer;
            String toStore = convertMatchToEntry(match);

            String content = "";
            try{
                File file = new File(context.getFilesDir().getAbsolutePath()+"/matches");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                buffer = reader.readLine();
                while(buffer != null) {
                    content = content + buffer + System.lineSeparator();
                    buffer = reader.readLine();
                }

                resetFile(false);

                FileWriter w = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(w);

                writer.write(content);
                writer.append(toStore);

                reader.close();
                writer.close();
            } catch(Exception e){
                // ignore
            }
        }
    }

    private boolean containsMatch(Match match){
        String buffer;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/matches");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while(buffer != null) {
                if(buffer.contains("id:"+match.getMatchID())){
                    return true;
                }
                buffer = reader.readLine();
            }
        } catch(Exception e){
            //ignored
        }
        return false;
    }

    private String convertMatchToEntry(Match match){
        String buffer;
        String toStore = "{"+System.lineSeparator()+ "objectType:match;" + System.lineSeparator();

        //TODO Separate between match and player -> Searchable as return value

        // Add matchID attribute
        toStore = toStore + "id:" + match.getMatchID() +";" + System.lineSeparator();

        // Add matchType attribute
        buffer = "matchType:";
        if(match.getMatchType() == 'S'){
            buffer = buffer + "Singles";
        } else{
            buffer = buffer + "Doubles";
        }
        toStore = toStore + buffer + ";" + System.lineSeparator();

        // Add player attributes
        toStore = toStore + "playerOne:" + match.getPlayerOneID() + ";"+ System.lineSeparator();
        toStore = toStore + "playerTwo:" + match.getPlayerTwoID() + ";"+ System.lineSeparator();
        if(match.getMatchType() == 'D'){
            toStore = toStore + "playerThree:" + match.getPlayerThreeID() + ";"+ System.lineSeparator();
            toStore = toStore + "playerFour:" + match.getPlayerFourID() + ";"+ System.lineSeparator();
        }

        // Add set attributes
        toStore = toStore + "setCount:" + match.getSetCount() + ";" + System.lineSeparator();
        buffer = match.getScoreFirst();
        toStore = toStore + "firstSetTeamOne:" + buffer.substring(0,buffer.indexOf(':')) + ";" + System.lineSeparator();
        toStore = toStore + "firstSetTeamTwo:" + buffer.substring(buffer.indexOf(':')+1) + ";" + System.lineSeparator();
        buffer = match.getScoreSecond();
        toStore = toStore + "secondSetTeamOne:" + buffer.substring(0,buffer.indexOf(':')) + ";" + System.lineSeparator();
        toStore = toStore + "secondSetTeamTwo:" + buffer.substring(buffer.indexOf(':')+1) + ";" + System.lineSeparator();
        if(match.getSetCount() == 3){
            buffer = match.getScoreThird();
            toStore = toStore + "thirdSetTeamOne:" + buffer.substring(0,buffer.indexOf(':')) + ";" + System.lineSeparator();
            toStore = toStore + "thirdSetTeamTwo:" + buffer.substring(buffer.indexOf(':')+1) + ";" + System.lineSeparator();
        }

        // Adding match dependency
        toStore = toStore + "matchDependency:" + match.getMatchDependency() + ";" + System.lineSeparator();
        if(match.getMatchDependency().equals("Tournament")){
            toStore = toStore + "tournamentID:" + match.getTournamentID() + ";" + System.lineSeparator();
        } else if(match.getMatchDependency().equals("League")){
            toStore = toStore + "leagueID:" + match.getLeagueID() + ";" + System.lineSeparator();
            toStore = toStore + "teamNumber:" + match.getTeamNumber() + ";" + System.lineSeparator();
        }
        toStore = toStore + "}";

        return toStore;
    }

    private void updateMatch(Match match){
        String content = "";
        String newContent = convertMatchToEntry(match);
        String read;
        String buffer = "";         // Here the matchEntry will be loaded to
        String toCompare;      // Here the substrings of buffer will be saved
        boolean matchfound = false;
        char c;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/matches");
            FileReader reader = new FileReader(file);

            while(reader.ready()){
                c = (char) reader.read();
                if(c == '{'){
                    buffer = c+"";
                    read = buffer;
                } else if(c == '}'){
                    buffer = buffer+c;
                    read = buffer;

                    read = read.replaceAll(" ","");
                    read = read.replaceAll("\n","");

                    toCompare = read.substring(1,read.indexOf(';'));
                    read = read.substring(read.indexOf(';')+1);
                    do {
                        if (toCompare.substring(0, toCompare.indexOf(':')).equals("id")
                                && toCompare.substring(buffer.indexOf(':') + 1).equals(String.valueOf(match.getMatchID()))) {
                            content = content + newContent;
                            matchfound = true;
                            break;
                        }
                        if(!read.equals("}")) {
                            toCompare = read.substring(0, read.indexOf(';'));
                            read = read.substring(read.indexOf(';') + 1);
                        } else toCompare = "";
                    } while (toCompare.length() != 0);
                    if(matchfound){
                        content = content + buffer;
                        matchfound = false;
                    }
                } else{
                    buffer = buffer + c;
                }
            }

            resetFile(false);

            FileWriter w = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(w);

            writer.write(content);

            reader.close();
            writer.close();
        } catch(Exception e){
            // ignored
        }
    }
}
