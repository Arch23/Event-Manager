package com.example.viniciusmn.events.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.viniciusmn.events.Classes.Event;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EventDAO {
    @Query("SELECT * FROM event")
    List<Event> getAll();

    @Query("SELECT * FROM event WHERE uid IN (:eventId)")
    Event getByID(int eventId);

    @Insert
    void insertEvent(Event e);

    @Delete
    void deleteEvent(Event e);

    @Update
    void updateEvent(Event e);
}
