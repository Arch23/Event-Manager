package com.example.viniciusmn.events;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Utils {
    public static String dateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    public static Date stringToDate(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(string);
    }


    public static int readSharedTheme(Context ctx){
        SharedPreferences shared = ctx.getSharedPreferences(MainActivity.SHARED_FILE, Context.MODE_PRIVATE);
        int theme = shared.getInt(MainActivity.STYLE,R.style.AppTheme);
        ctx.setTheme(theme);
        return theme;
    }
}
