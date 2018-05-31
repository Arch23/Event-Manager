package com.example.viniciusmn.events.DAO;

import android.arch.persistence.room.TypeConverter;

import com.example.viniciusmn.events.Classes.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.example.viniciusmn.events.Utils.dateToString;
import static com.example.viniciusmn.events.Utils.stringToDate;

public class Converters {
    @TypeConverter
    public static ArrayList<Person> fromJSONGuests(String value){
        Type listType = new TypeToken<ArrayList<Person>>() {}.getType();
        return new Gson().fromJson(value,listType);
    }


    @TypeConverter
    public static String fromArrayListGuests(ArrayList<Person> list){
        return new Gson().toJson(list);
    }

    @TypeConverter
    public static Date fromStringDate(String value){
        try {
            return stringToDate(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    @TypeConverter
    public static String fromDateDate(Date date){
        return dateToString(date);
    }
}
