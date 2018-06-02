package com.example.viniciusmn.events.Utils;

import android.app.TaskStackBuilder;
import android.arch.persistence.room.TypeConverter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Base64;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.viniciusmn.events.MainActivity;
import com.example.viniciusmn.events.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Misc {
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
        int theme = shared.getInt(MainActivity.STYLE, R.style.AppTheme);
        ctx.setTheme(theme);
        return theme;
    }

    public static Bitmap getBitmapFromURI(Context ctx,Uri imageUri){
        try {
            InputStream imageStream = ctx.getContentResolver().openInputStream(imageUri);
            return BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v){
        imageViewAnimatedChange(c,v,null);
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_in.setDuration(100);
        anim_out.setDuration(100);
        if(new_image!=null){
            v.setScaleType(ImageView.ScaleType.CENTER_CROP);
            v.setImageBitmap(new_image);
        }else{
            v.setScaleType(ImageView.ScaleType.CENTER);
            v.setImageDrawable(c.getDrawable(android.R.drawable.ic_menu_report_image));
        }
        v.startAnimation(anim_in);

//        anim_out.setAnimationListener(new Animation.AnimationListener()
//        {
//            @Override public void onAnimationStart(Animation animation) {}
//            @Override public void onAnimationRepeat(Animation animation) {}
//            @Override public void onAnimationEnd(Animation animation)
//            {
//                if(new_image!=null){
//                    v.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                    v.setImageBitmap(new_image);
//                }else{
//                    v.setScaleType(ImageView.ScaleType.CENTER);
//                    v.setImageDrawable(c.getDrawable(android.R.drawable.ic_menu_report_image));
//                }
//                anim_in.setAnimationListener(new Animation.AnimationListener() {
//                    @Override public void onAnimationStart(Animation animation) {}
//                    @Override public void onAnimationRepeat(Animation animation) {}
//                    @Override public void onAnimationEnd(Animation animation) {}
//                });
//                v.startAnimation(anim_in);
//            }
//        });
//        v.startAnimation(anim_out);
    }
}
