package com.sensolic.badmintontrainer;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

public class Storage {

    public static boolean isSaved = false;      // If Positions of Characters are saved
    private Context context;

    public Storage(Context applicationContext) {
        context = applicationContext;
        File data = new File(context.getFilesDir().getAbsolutePath()+"/data");
        try {
            data.delete();
            data.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
     * @param settings Settings to save
     */
    public void saveSettings(Settings settings){

        if(!changeSetting("manualStartPos", settings.manualStartPos()+"")){
            if(!addSetting("manualStartPos", settings.manualStartPos()+"")){
                System.out.println("ERROR in Saving Settings");
            }
        }

        if(!changeSetting("debugMode", settings.debugMode()+"")){
            if(!addSetting("debugMode", settings.debugMode()+"")){
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
        String result = null;
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
        return result;
    }

}
