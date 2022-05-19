package com.sensolic.badmintontrainer;

import android.content.Context;

public class Settings {

    private static Settings instance;
    private static boolean manualStartPos = true;
    private static boolean debugMode = false;
    private static boolean autocompleteScore = true;


    private Storage storage;

    /**
     * This is the constructor if only the context is given -> storage object will be created
     * @param  context This parameter is necessary to build a storage object
     */
    private Settings(Context context){
        storage = Storage.getInstance(context);
        executeSettings(storage.getSettings());
    }

    public static Settings getInstance(Context context){
        if(instance == null){
            instance = new Settings(context);
        }
        return instance;
    }

    /**
     * This method can be used to get the value of the manualStartPos attribute
     * @return  Value of manualStartPos
     */
    public static boolean manualStartPos(){
        return manualStartPos;
    }

    /**
     * This method can be used to get the value of debugMode attribute
     * @return  Value of debugMode
     */
    public static boolean debugMode(){
        return debugMode;
    }

    /**
     * This method can be used to get the value of autocompleteScore attribute
     * @return  Value of debugMode
     */
    public static boolean autocompleteScore(){
        return autocompleteScore;
    }

    /**
     * This method can be used to set the value of the manualStartPos
     * @param value The new value for manualStartPos
     */
    public static void setManualStartPos(boolean value){
        manualStartPos = value;
    }

    /**
     * This method can be used to set the value of debugMode
     * @param value The new value for debugMode
     */
    public static void setDebugMode(boolean value){
        debugMode = value;
    }

    /**
     * This method can be used to set the value of autoCompleteScore
     * @param value The new value for autocompleteScore
     */
    public static void setAutocompleteScore(boolean value){
        autocompleteScore = value;
    }

    /**
     * This method decodes and executes the returned String of the Storage object
     * @param code returned String of Storage
     */
    private void executeSettings(String code){
        String buffer;

        if(code == null) return;
        // Getting startPos value
        buffer = code.substring(code.indexOf(":")+1,code.indexOf(";"));
        code = code.substring(code.indexOf(";")+1);
        try{
            manualStartPos = Boolean.parseBoolean(buffer);
        } catch (Exception e){
            e.printStackTrace();
        }
        // Getting debugMode value
        buffer = code.substring(code.indexOf(":")+1,code.indexOf(";"));
        code = code.substring(code.indexOf(";")+1);
        try{
            debugMode = Boolean.parseBoolean(buffer);
        } catch (Exception e){
            e.printStackTrace();
        }
        // Getting autocompleteScore value
        buffer = code.substring(code.indexOf(":")+1,code.indexOf(";"));
        code = code.substring(code.indexOf(";")+1);
        try{
            autocompleteScore = Boolean.parseBoolean(buffer);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
