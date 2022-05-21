package com.sensolic.badmintontrainer.search;

public interface Searchable {
    /**
     *  Returns the info that should be displayed by default
     * @return String with default value
     */
    String getInfo();
    /**
     *  Returns the ID of the object with objectType-identifier
     * @return String with ID + identifier
     */
    String getIDInfo();
}
