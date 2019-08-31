package com.eventor.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Activity {

    @PrimaryKey
    public final int id;
    public String name;
    public String street;
    public String type;
    public String category;
    public String housenumber;
    public int postcode;
    public String city;


    public Activity(int id, String name, String street, String type, String category, String housenumber, int postcode, String city) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.type = type;
        this.category = category;
        this.housenumber = housenumber;
        this.postcode = postcode;
        this.city = city;
    }

}