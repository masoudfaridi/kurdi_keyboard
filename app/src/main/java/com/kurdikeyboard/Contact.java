package com.kurdikeyboard;

/**
 * Created by Stat on 3/1/2017.
 */public class Contact {

    private String name;
    private String phone;

    public Contact(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    //GETTERS AND SETTERS
    public String getName(){

        return this.name;

    }
    public String getPhone(){

        return this.phone;

    }

}