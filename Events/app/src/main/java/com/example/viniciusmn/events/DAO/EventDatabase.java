package com.example.viniciusmn.events.DAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.viniciusmn.events.Classes.Event;

@Database(entities = {Event.class}, version = 2,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class EventDatabase extends RoomDatabase{
    private static EventDatabase INSTANCE;

    public abstract EventDAO eventDAO();

    public static EventDatabase getInstance(final Context context){
        if(INSTANCE == null){
            synchronized (EventDatabase.class){
                if(INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context,EventDatabase.class,"event.db").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
