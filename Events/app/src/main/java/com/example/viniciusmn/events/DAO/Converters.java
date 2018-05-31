package com.example.viniciusmn.events.DAO;

import android.arch.persistence.room.TypeConverter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import com.example.viniciusmn.events.Classes.Person;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
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
