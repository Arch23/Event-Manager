package com.example.viniciusmn.events;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viniciusmn.events.Classes.Event;
import com.example.viniciusmn.events.Classes.Person;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.example.viniciusmn.events.Utils.Misc.dateToString;
import static com.example.viniciusmn.events.Utils.Misc.getBitmapFromURIResized;
import static com.example.viniciusmn.events.Utils.Misc.imageViewAnimatedChange;
import static com.example.viniciusmn.events.Utils.Misc.readSharedTheme;
import static com.example.viniciusmn.events.Utils.Misc.stringToDate;

public class createActivity extends AppCompatActivity {

    public static final String GUEST_LIST = "GUEST_LIST";
    public static final int GET_GUEST = 1;
    public static final int RESULT_LOAD_IMAGE = 0;

    private Calendar myCalendar = Calendar.getInstance();
    private EditText date_editText;
    private EditText name_editText;
    private EditText local_editText;
    private EditText description_editText;
    private Button create_btn;
    private ImageView imageView;
    private ArrayList<Person> guestList;
    private TextView image_textView;
    private ImageButton delete_imageBtn;

    private int uID;
    private Uri imageUri;
    private boolean EDIT;

    private DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        readSharedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        image_textView = findViewById(R.id.image_textView);
        delete_imageBtn = findViewById(R.id.delete_imageBtn);
        delete_imageBtn.setVisibility(View.GONE);
        date_editText = findViewById(R.id.date_editText);
        name_editText = findViewById(R.id.name_editText);
        local_editText = findViewById(R.id.local_editText);
        description_editText = findViewById(R.id.description_editText);
        create_btn = findViewById(R.id.create_btn);
        imageView = findViewById(R.id.imageView);
        imageView.setOnLongClickListener(v -> {
            if(imageUri != null){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(imageUri,"image/*");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }

            return true;
        });
        imageUri = null;
        guestList = new ArrayList<>();

        date_editText.setText(dateToString(Calendar.getInstance().getTime()));

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            EDIT = true;
            fillActivity((Event) Objects.requireNonNull(bundle.getSerializable(MainActivity.EVENT_OBJ)));
            setTitle(getString(R.string.create_edit_title));
            create_btn.setText(R.string.create_btn_change);
        } else {
            EDIT = false;
            setTitle(getString(R.string.create_new_title));
            create_btn.setText(R.string.create_btn_create);
        }
    }

    private void fillActivity(Event event) {
        name_editText.setText(event.getName());
        date_editText.setText(dateToString(event.getDate()));
        myCalendar.setTime(event.getDate());
        local_editText.setText(event.getPlace());
        description_editText.setText(event.getDescription());
        guestList = event.getInvited();
        uID = event.getUid();
        imageUri = event.getImageURI();
        if(event.getImageURIString().isEmpty()){
            hideImageView(false);
        }else{
            loadImage();
        }
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
                newEvent = new Event(uID, name_editText.getText().toString(), date, local_editText.getText().toString(), description_editText.getText().toString(), guestList,imageUri==null?"":imageUri.toString());
            } else {
                newEvent = new Event(name_editText.getText().toString(), date, local_editText.getText().toString(), description_editText.getText().toString(), guestList,imageUri==null?"":imageUri.toString());
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

    public void getImage(View v){
        Intent intent;

        intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);


        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");

        startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.select_image)),RESULT_LOAD_IMAGE);
    }

    public void unsetImage(View v){
        hideImageView(true);
    }

    public void hideImageView(boolean transition){
        imageUri = null;
        if(transition){
            imageViewAnimatedChange(this,imageView);
        }else{
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setImageDrawable(getDrawable(android.R.drawable.ic_menu_report_image));
        }
        image_textView.setVisibility(View.VISIBLE);
        delete_imageBtn.setVisibility(View.GONE);
    }

    public void clear(View v) {
        name_editText.setText("");
        date_editText.setText("");
        local_editText.setText("");
        description_editText.setText("");
        guestList.clear();
        hideImageView(true);
    }

    public void openDatePicker(View v) {
        new DatePickerDialog(createActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void callManageGuest(View v) {
        Intent intent = new Intent(this, manageGuestActivity.class);

        intent.putExtra(createActivity.GUEST_LIST, guestList);

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

    private void loadImage(){
        image_textView.setVisibility(View.GONE);
        delete_imageBtn.setVisibility(View.VISIBLE);

        BitmapWorkerTask task = new BitmapWorkerTask(this,imageUri);
        task.execute();
    }

    private void setImage(Bitmap bitmap){
        if(bitmap!=null){
            imageViewAnimatedChange(this,imageView,bitmap);
        }else{
            hideImageView(false);
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
                guestList = (ArrayList<Person>) bundle.getSerializable(GUEST_LIST);
            }
        }else if(requestCode == RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK){
            final int takeFlags = data.getFlags() & Intent.FLAG_GRANT_READ_URI_PERMISSION;
            ContentResolver resolver = this.getContentResolver();
            Uri tmp = data.getData();
            resolver.takePersistableUriPermission(Objects.requireNonNull(tmp),takeFlags);
            imageUri = tmp;
            loadImage();
        }
    }

    private void updateLabel() {
        date_editText.setText(dateToString(myCalendar.getTime()));
    }

    private class BitmapWorkerTask extends AsyncTask<Void,Void,Void>{

        private Bitmap bitmap;
        private Context ctx;
        private Uri imageUri;

        public BitmapWorkerTask(Context ctx,Uri imageUri) {
            this.ctx = ctx;
            this.imageUri = imageUri;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setImage(bitmap);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            bitmap = getBitmapFromURIResized(ctx,imageUri,1024,300);
            return null;
        }
    }
}
