package com.example.viniciusmn.events;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.viniciusmn.events.Classes.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.viniciusmn.events.Utils.dateToString;
import static com.example.viniciusmn.events.Utils.readSharedTheme;
import static com.example.viniciusmn.events.Utils.stringToDate;

public class createActivity extends AppCompatActivity {

    public static final String GUEST_LIST = "GUEST_LIST";
    public static final int GET_GUEST = 1;

    private Calendar myCalendar = Calendar.getInstance();
    private EditText date_editText;
    private EditText name_editText;
    private EditText local_editText;
    private EditText description_editText;
    private Button create_btn;
    private ArrayList<String> guestList;

    private int uID;
    private boolean EDIT;

    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        readSharedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        date_editText = findViewById(R.id.date_editText);
        name_editText = findViewById(R.id.name_editText);
        local_editText = findViewById(R.id.local_editText);
        description_editText = findViewById(R.id.description_editText);
        create_btn = findViewById(R.id.create_btn);
        guestList = new ArrayList<>();

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            fillActivity((Event) bundle.getSerializable(MainActivity.EVENT_OBJ));
            setTitle(getString(R.string.create_edit_title));
            create_btn.setText(R.string.create_btn_change);
            EDIT = true;
        } else {
            setTitle(getString(R.string.create_new_title));
            create_btn.setText(R.string.create_btn_create);
            EDIT = false;
        }
    }

    private void fillActivity(Event event) {
        name_editText.setText(event.getName());
        date_editText.setText(dateToString(event.getDate()));
        local_editText.setText(event.getPlace());
        description_editText.setText(event.getDescription());
        guestList = event.getInvited();
        uID = event.getUid();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_menu_save:
                processFinishAndSave();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public Event createEvent() {

        try {
            Date date = stringToDate(date_editText.getText().toString());
            Event newEvent;
            if (EDIT) {
                newEvent = new Event(uID, name_editText.getText().toString(), date, local_editText.getText().toString(), description_editText.getText().toString(), guestList);
            } else {
                newEvent = new Event(name_editText.getText().toString(), date, local_editText.getText().toString(), description_editText.getText().toString(), guestList);
            }
            return newEvent;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(View v) {
        processFinishAndSave();
    }

    public void clear(View v) {
        name_editText.setText("");
        date_editText.setText("");
        local_editText.setText("");
        description_editText.setText("");
        guestList.clear();
    }

    public void openDatePicker(View v) {
        new DatePickerDialog(createActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void callManageGuest(View v) {
        Intent intent = new Intent(this, manageGuestActivity.class);

        intent.putStringArrayListExtra(createActivity.GUEST_LIST, guestList);

        startActivityForResult(intent, GET_GUEST);
    }

    private boolean validate() {
        String errorMessage = "";
        if (name_editText.getText().toString().isEmpty()) {
            errorMessage += getString(R.string.empty_event_name) + "\n";
        }
        if (date_editText.getText().toString().isEmpty()) {
            errorMessage += getString(R.string.empty_event_date) + "\n";
        }
        if (local_editText.getText().toString().isEmpty()) {
            errorMessage += getString(R.string.empty_evet_location) + "\n";
        }
        if (description_editText.getText().toString().isEmpty()) {
            errorMessage += getString(R.string.empty_event_description) + "\n";
        }
        if (guestList.isEmpty()) {
            errorMessage += getString(R.string.empty_event_guests) + "\n";
        }
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void processFinishAndSave() {
        if (validate()) {
            Intent intent = new Intent();

            intent.putExtra(MainActivity.EVENT_OBJ, createEvent());

            setResult(Activity.RESULT_OK, intent);

            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_GUEST && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();

            if (bundle != null) {
                guestList = bundle.getStringArrayList(GUEST_LIST);
            }
        }
    }

    private void updateLabel() {
        date_editText.setText(dateToString(myCalendar.getTime()));
    }


}
