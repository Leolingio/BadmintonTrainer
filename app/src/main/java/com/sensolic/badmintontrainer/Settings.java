package com.sensolic.badmintontrainer;

import android.content.Context;

public class Settings {

    public static boolean manualStartPos;
    public static boolean debugMode;
    private Storage storage;

    /**
     * This is the constructor if only the context is given -> storage object will be created
     * @param  context This parameter is necessary to build a storage object
     */
    public Settings(Context context){
        storage = new Storage(context);
        executeSettings(storage.getSettings());
    }

    /**
     * This is the constructor if the storage object already exists
     * @param storage This parameter is necessary to build the settings object
     */
    public Settings(Storage storage){
        this.storage = storage;
        executeSettings(storage.getSettings());
    }

    /**
     * This method can be used to get the value of the manualStartPos attribute
     * @return  Value of manualStartPos
     */
    public boolean manualStartPos(){
        return manualStartPos;
    }

    /**
     * This method can be used to get the value of debugMode attribute
     * @return  Value of debugMode
     */
    public boolean debugMode(){
        return debugMode;
    }

    /**
     * This method can be used to set the value of the manualStartPos
     * @param value The new value for manualStartPos
     */
    public void setManualStartPos(boolean value){
        manualStartPos = value;
    }

    /**
     * This method can be used to set the value of debugMode
     * @param value The new value for debugMode
     */
    public void setDebugMode(boolean value){
        debugMode = value;
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
        try{
            debugMode = Boolean.parseBoolean(buffer);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
