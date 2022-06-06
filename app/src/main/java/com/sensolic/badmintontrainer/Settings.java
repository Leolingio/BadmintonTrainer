package com.sensolic.badmintontrainer;

import android.content.Context;

import com.sensolic.badmintontrainer.data.Storage;

public class Settings {

    private static int lastAppVersion = -1;
    private static Settings instance;
    private static boolean manualStartPos = true;
    private static boolean debugMode = false;
    private static boolean autocompleteScore = true;
    private static char defaultMatchType = 'S';


    private Storage storage;

    /**
     * This is the constructor if only the context is given -> storage object will be created
     * @param  context This parameter is necessary to build a storage object
     */
    private Settings(Context context){
        storage = Storage.getInstance(context);
        executeSettings(storage.getSettings());
        if(lastAppVersion != BuildConfig.VERSION_CODE){
            storage.showChangelog();
            lastAppVersion = BuildConfig.VERSION_CODE;
        }
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
     * This method can be used to get the value of defaultMatchType attribute
     * @return  Value of defaultMatchType
     */
    public static String getDefaultMatchType(){
        if(defaultMatchType == 'S'){
            return "Singles";
        }
        return "Doubles";
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
     * This method can be used to set the value of defaultMatchType
     * @param value The new value for defaultMatchType
     */
    public static void setDefaultMatchType(char value){
        defaultMatchType = value;
    }

    /**
     * This method decodes and executes the returned String of the Storage object
     * @param code returned String of Storage
     */
    private void executeSettings(String code){
        String buffer;
        String name, value;

        if(code == null) return;

        do {
            buffer = code.substring(0,code.indexOf(";"));
            code = code.substring(code.indexOf(";")+1);

            name = buffer.substring(0, buffer.indexOf(':'));
            value = buffer.substring(buffer.indexOf(':') + 1);

            try {
                switch (name) {
                    case "manualStartPos":
                        manualStartPos = Boolean.parseBoolean(value);
                        break;
                    case "debugMode":
                        debugMode = Boolean.parseBoolean(value);
                        break;
                    case "autocompleteScore":
                        autocompleteScore = Boolean.parseBoolean(value);
                        break;
                    case "defaultMatchType":
                        if(value.equals("Singles")){
                            defaultMatchType = 'S';
                        } else if(value.equals("Doubles")){
                            defaultMatchType = 'D';
                        }
                    case "lastAppVersion":
                        try {
                            lastAppVersion = Integer.parseInt(value);
                        } catch(NumberFormatException e){
                            // Ignored
                        }
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while(code.contains(";"));
    }
}
