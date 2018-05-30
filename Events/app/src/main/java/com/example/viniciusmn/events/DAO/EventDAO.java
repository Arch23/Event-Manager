package com.example.viniciusmn.events.DAO;

import com.example.viniciusmn.events.Classes.Event;

import java.util.ArrayList;

public class EventDAO {
    private static final EventDAO ourInstance = new EventDAO();

    public static EventDAO getInstance() {
        return ourInstance;
    }

    private EventDAO() {
        events = new ArrayList<>();
    }

    private ArrayList<Event> events;

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event){
        events.add(event);
    }

    public void deleteEvent(Event event){

    }
}
