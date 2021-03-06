package com.example.viniciusmn.events;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Objects;

public class aboutActivity extends AppCompatActivity {

     private int themeId;

    public static void call(Context context){
        Intent intent = new Intent(context,aboutActivity.class);

        context.startActivity(intent);
    }

    private int readSharedTheme(){
        SharedPreferences shared = getSharedPreferences(MainActivity.SHARED_FILE,Context.MODE_PRIVATE);

        return shared.getInt(MainActivity.STYLE,R.style.AppTheme);
    }

    private void changeTheme(boolean oncreate,int theme){
        setTheme(theme);

        if(!oncreate){
            TaskStackBuilder.create(this)
                    .addNextIntent(new Intent(this, MainActivity.class))
                    .addNextIntent(this.getIntent())
                    .startActivities();
        }
    }

    private void writeSharedTheme(){
        SharedPreferences shared = getSharedPreferences(MainActivity.SHARED_FILE,Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(MainActivity.STYLE,themeId);

        editor.commit();
    }



    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeId = readSharedTheme();
        changeTheme(true,themeId);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setTitle(R.string.about);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Switch theme_switch = findViewById(R.id.theme_switch);
        TextView theme_textView = findViewById(R.id.theme_textView);

        theme_switch.setChecked(themeId==R.style.AppThemeDark);
        theme_textView.setText(theme_switch.isChecked()?R.string.dark_theme:R.string.light_theme);

        theme_switch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            themeId = isChecked?R.style.AppThemeDark:R.style.AppTheme;
            writeSharedTheme();
            changeTheme(false,themeId);
        });
    }
}
