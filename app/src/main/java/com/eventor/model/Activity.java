package com.eventor.model;

import android.util.Log;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static android.content.ContentValues.TAG;

@Entity
public class Activity implements Serializable {

    @PrimaryKey
    public int id;
    public String name;
    public String street;
    public String type;
    public String category;
    public String housenumber;
    public int postcode;
    public String city;

    public Activity(Activity a) {
        this.id = a.id;
        this.name = a.name;
        this.street = a.street;
        this.type = a.type;
        this.category = a.category;
        this.housenumber = a.housenumber;
        this.postcode = a.postcode;
        this.city = a.city;
    }

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