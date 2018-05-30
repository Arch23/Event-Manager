package com.example.viniciusmn.events.Classes;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Event {
    private String name;
    private Date date;
    private String place;
    private String description;
    private ArrayList<String> invited;

    public Event(String name, Date date, String place, String description, ArrayList<String> invited) {
        this.name = name;
        this.date = date;
        this.place = place;
        this.description = description;
        this.invited = invited;
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

    public ArrayList<String> getInvited() {
        return invited;
    }

    public void setInvited(ArrayList<String> invited) {
        this.invited = invited;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", place='" + place + '\'' +
                ", description='" + description + '\'' +
                ", invited=" + invited +
                '}';
    }
}
