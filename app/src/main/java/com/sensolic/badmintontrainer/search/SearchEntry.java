package com.sensolic.badmintontrainer.search;

public class SearchEntry {

    private final String name;      // Name
    private final String ID;      // ID

    public SearchEntry(String name, String ID){
        this.name = name;
        this.ID = ID;
    }

    public String getName(){
        return name;
    }

    public String getID(){
        return ID;
    }
}
