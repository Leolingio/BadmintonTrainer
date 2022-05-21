package com.sensolic.badmintontrainer.data;

import android.content.Context;
import android.widget.Toast;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.search.SearchEntry;
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
     *  Adds an Entry to the matches file
     * @param matchID Name of the Entry
     * @param matchName Value of the Entry
     * @return If the operation was successful
     */
    private boolean addMatch(String matchID, String matchName){
        String content = "";
        String newContent = matchID+":"+matchName+";";
        String buffer;
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
            writer.append(newContent);

            reader.close();
            writer.close();
        } catch(Exception e){
            return false;
        }
        return true;
    }

    /**
     *  Changes an existing Match entry
     * @param matchID Name of the setting
     * @param newMatchName Value of the Setting
     * @return If the operation was successful
     */
    private boolean changeMatch(String matchID, String newMatchName){
        String content = "";
        String newContent = matchID+":"+newMatchName+";";
        String buffer;
        boolean successful = false;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/matches");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while(buffer != null) {
                if(buffer.contains(matchID)){
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
     * This method saves a match
     */
    public void saveMatch(String matchID, String matchName){
        if(!changeMatch(matchID, matchName)){
            if(!addMatch(matchID, matchName)){
                System.out.println("ERROR in Saving Match");
            }
        }
    }

    /**
     * This method deletes a match
     * @param matchID ID of the match without #M
     */
    public boolean deleteMatch(String matchID){
        String content = "";
        String buffer;
        boolean successful = false;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/matches");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while(buffer != null) {
                if(!buffer.contains(matchID)){
                    content = content + buffer + System.lineSeparator();
                } else successful = true;
                buffer = reader.readLine();
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
        return successful;
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

    public void addStoredMatches(ArrayList<Searchable> list){
        String buffer;
        try{
            File file = new File(context.getFilesDir().getAbsolutePath()+"/matches");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            buffer = reader.readLine();
            while(buffer != null) {
                if(!buffer.substring(0,buffer.indexOf(':')).contains("currentMatchID")){
                    Searchable toAdd = new SearchEntry(buffer.substring(buffer.indexOf(':')+1,buffer.length()-1), "#M"+buffer.substring(0,buffer.indexOf(':')));
                    list.add(toAdd);
                }
                buffer = reader.readLine();
            }
        } catch(Exception e){
            //ignore
        }

    }

    public void storeMatch(Match match){
        // If already saved then just update the existing data
        if(containsMatch(match)) updateMatch(match);
        else{   // Create new entry for the match
            String buffer;
            String toStore = "{"+System.lineSeparator()+ "objectType:match;" + System.lineSeparator();

            // Add matchID attribute
            toStore = toStore + "id:" + match.getMatchID() +";" + System.lineSeparator();

            // Add matchType attribute
            buffer = "matchType:";
            if(match.getMatchType() == 'S'){
                buffer = buffer + context.getResources().getString(R.string.matchTypeSingles);
            } else{
                buffer = buffer + context.getResources().getString(R.string.matchTypeDoubles);
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
        return false;
    }

    public void updateMatch(Match match){

    }
}
