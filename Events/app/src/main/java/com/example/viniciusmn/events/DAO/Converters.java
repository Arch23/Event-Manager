package com.example.viniciusmn.events.DAO;

import android.arch.persistence.room.TypeConverter;

import com.example.viniciusmn.events.Classes.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static com.example.viniciusmn.events.Utils.Misc.dateToString;
import static com.example.viniciusmn.events.Utils.Misc.stringToDate;

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
    public static Date fromTimestamp(Long value){
        return value == null? null: new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null? null : date.getTime();
    }
//
//    @TypeConverter
//    public static Uri fromStringUri(String uri){
//        return Uri.parse(uri);
//    }
//
//    @TypeConverter
//    public static String fromUriString(Uri uri){
//        return uri.toString();
//    }
}
