package com.example.viniciusmn.events.Classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

@Entity
public class Event implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String name;

    private Date date;

    private String place;

    private String description;

    private ArrayList<Person> invited;

    private String imageURIString;

    @Ignore
    public Event(String name, Date date, String place, String description, ArrayList<Person> invited,String imageURIString) {
        this.name = name;
        this.date = date;
        this.place = place;
        this.description = description;
        this.invited = invited;
        this.imageURIString = imageURIString;
    }

    public Event(int uid, String name, Date date, String place, String description, ArrayList<Person> invited, String imageURIString) {
        this.uid = uid;
        this.name = name;
        this.date = date;
        this.place = place;
        this.description = description;
        this.invited = invited;
        this.imageURIString = imageURIString;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Person> getInvited() {
        return invited;
    }

    public void setInvited(ArrayList<Person> invited) {
        this.invited = invited;
    }

    public int getInvitedSize(){
        return invited.size();
    }

    public int getConfirmedInvited(){
        return (int) invited.stream().filter(el -> el.isConfirmed()).count();
    }

    public String getImageURIString() {
        return imageURIString;
    }

    public void setImageURIString(String imageURIString) {
        this.imageURIString = imageURIString;
    }

    public Uri getImageURI(){
        return Uri.parse(imageURIString);
    }

    @Override
    public String toString() {
        return "Event{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", place='" + place + '\'' +
                ", description='" + description + '\'' +
                ", invited=" + invited +
                '}';
    }
}
