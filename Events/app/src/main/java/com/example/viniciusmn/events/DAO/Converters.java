package com.example.viniciusmn.events.DAO;

import android.arch.persistence.room.TypeConverter;

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
    public static ArrayList<String> fromStringGuests(String value){
        String[] splited = value.split(",");
        return new ArrayList<>(Arrays.asList(splited));
    }


    @TypeConverter
    public static String fromArrayListGuests(ArrayList<String> list){
        String result = "";
        for(String s : list){
            result += s+",";
        }
        return result.substring(0,result.length()-1);
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
