package com.example.viniciusmn.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class manageGuestActivity extends AppCompatActivity {
    //why dont call work to request result?

    private ArrayList<String> guestList;
    private ArrayAdapter<String> adapter;
    private ListView guest_listView;
    private String newName;

    private static final int NEW = 0;
    private static final int ALTER = 1;
    private int selectedPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_guest);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//maybe not for this screen
        guest_listView = findViewById(R.id.guest_listView);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if(bundle != null){
            guestList = bundle.getStringArrayList(createActivity.GUEST_LIST);
        }else{
            guestList = new ArrayList<>();
        }

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,guestList);
        guest_listView.setAdapter(adapter);

        guest_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                getName(ALTER);
            }
        });

        guest_listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        guest_listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                boolean selected = guest_listView.isItemChecked(position);

                View  view = guest_listView.getChildAt(position);

                if(selected){
                    view.setBackgroundColor(Color.LTGRAY);
                }else{
                    view.setBackgroundColor(Color.TRANSPARENT);
                }

                int totalSelected = guest_listView.getCheckedItemCount();

                if(totalSelected > 0){
                    mode.setTitle(getResources().getQuantityString(R.plurals.selected,totalSelected,totalSelected));
                }

                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.guest_manage_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                if(guest_listView.getCheckedItemCount()>1){
                    menu.getItem(0).setVisible(false);
                }else{
                    menu.getItem(0).setVisible(true);
                }
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.guest_manage_menu_alter:
                        for(int position = guest_listView.getChildCount();position>=0;position--){
                            if(guest_listView.isItemChecked(position)){
                                selectedPosition = position;
                                getName(ALTER);
                            }
                        }
                        mode.finish();
                        break;
                    case R.id.guest_manage_menu_delete:
                        deleteGuests();
                        mode.finish();
                        break;
                    default:
                        return false;
                }
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                for(int position = 0;position<guest_listView.getChildCount();position++){
                    View view = guest_listView.getChildAt(position);

                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guest_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.guest_menu_add:
                getName(NEW);
                return true;
            case R.id.guest_menu_back:
                processFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNewGuest(){
        if(!newName.isEmpty()){
                guestList.add(newName);
                adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(this, R.string.empty_name_error, Toast.LENGTH_SHORT).show();
            }

    }

    private void changeGuest(){
        if(newName != guestList.get(selectedPosition)){
            guestList.set(selectedPosition,newName);
            adapter.notifyDataSetChanged();
        }

        selectedPosition = -1;
    }

    private void deleteGuests(){
        for(int position = guest_listView.getChildCount();position>=0;position--){
            if(guest_listView.isItemChecked(position)){
                guestList.remove(position);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void getName(final int MODE){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(MODE == NEW){
            builder.setTitle(R.string.new_guest);
        }else{
            builder.setTitle(R.string.alter_guest_name);
        }

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);

        if(MODE == ALTER){
            input.setText(guestList.get(selectedPosition));
        }

        builder.setView(input);

        builder.setPositiveButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newName = input.getText().toString();

                if(MODE == ALTER){
                    changeGuest();
                }else{
                    addNewGuest();
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void processFinish(){
        Intent intent = new Intent();

        intent.putStringArrayListExtra(createActivity.GUEST_LIST,guestList);

        setResult(Activity.RESULT_OK,intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        processFinish();
    }
}
