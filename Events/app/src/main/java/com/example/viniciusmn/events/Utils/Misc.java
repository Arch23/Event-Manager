package com.example.viniciusmn.events.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.viniciusmn.events.MainActivity;
import com.example.viniciusmn.events.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Misc {
    public static String dateToString(Date date){
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    public static Date stringToDate(String string) throws ParseException {
        return DateFormat.getDateInstance(DateFormat.SHORT).parse(string);
    }


    public static int readSharedTheme(Context ctx){
        SharedPreferences shared = ctx.getSharedPreferences(MainActivity.SHARED_FILE, Context.MODE_PRIVATE);
        int theme = shared.getInt(MainActivity.STYLE, R.style.AppTheme);
        ctx.setTheme(theme);
        return theme;
    }

//    public static Bitmap getBitmapFromURI(Context ctx,Uri imageUri){
//        try {
//            InputStream imageStream = ctx.getContentResolver().openInputStream(imageUri);
//            return BitmapFactory.decodeStream(imageStream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static Bitmap getBitmapFromURIResized(Context ctx,Uri imageUri,int reqWidth, int reqHeight){

        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            InputStream imageStream = ctx.getContentResolver().openInputStream(imageUri);
            options.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(imageStream,null,options);
            imageStream = ctx.getContentResolver().openInputStream(imageUri);

            options.inSampleSize = calculateSample(options,reqWidth,reqWidth);

            options.inJustDecodeBounds = false;

            return BitmapFactory.decodeStream(imageStream,null,options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static int calculateSample(BitmapFactory.Options options,int reqWidth,int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v){
        imageViewAnimatedChange(c,v,null);
    }

    public static void imageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);

        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                if(new_image!=null){
                    v.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    v.setImageBitmap(new_image);
                }else{
                    v.setScaleType(ImageView.ScaleType.CENTER);
                    v.setImageDrawable(c.getDrawable(android.R.drawable.ic_menu_report_image));
                }
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
}
