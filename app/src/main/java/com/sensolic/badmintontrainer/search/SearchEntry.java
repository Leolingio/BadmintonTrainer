package com.sensolic.badmintontrainer.search;

public class SearchEntry implements Searchable{

    private final String name;      // Name
    private final String ID;      // ID

    public SearchEntry(String name, String ID){
        this.name = name;
        this.ID = ID;
    }

    public String getInfo(){
        return name;
    }

    public String getIDInfo(){
        return ID;
    }
}
