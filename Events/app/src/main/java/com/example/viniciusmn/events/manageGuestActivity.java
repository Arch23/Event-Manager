package com.example.viniciusmn.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.viniciusmn.events.Adapter.GuestListAdapter;
import com.example.viniciusmn.events.Classes.Person;

import java.util.ArrayList;

import static com.example.viniciusmn.events.Utils.Misc.readSharedTheme;

public class manageGuestActivity extends AppCompatActivity {
    //why dont call work to request result?

    private ArrayList<Person> guestList;
    private ListView guest_listView;
    private GuestListAdapter list_adapter;
    private String newName;
    private boolean newConfirmed;
    private FloatingActionButton floatingAddBtn;

    private static final int NEW = 0;
    private static final int ALTER = 1;
    private int selectedPosition = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int theme = readSharedTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_guest);

        setTitle(R.string.manage_guets);

        guest_listView = findViewById(R.id.guest_listView);
        floatingAddBtn = findViewById(R.id.floatingAddBtn);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        if(bundle != null){
            guestList = (ArrayList<Person>) bundle.getSerializable(createActivity.GUEST_LIST);
        }else{
            guestList = new ArrayList<>();
        }
        list_adapter = new GuestListAdapter(this,guestList,(theme==R.style.AppTheme));
        guest_listView.setAdapter(list_adapter);

        guest_listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            getName(ALTER);
        });

        guest_listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        guest_listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                list_adapter.toggleItemSelected(position);
                list_adapter.notifyDataSetChanged();

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
                floatingAddBtn.setVisibility(View.GONE);
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
                        selectedPosition = list_adapter.getSelectedPositions().get(0);
                        getName(ALTER);
                        break;
                    case R.id.guest_manage_menu_delete:
                        deleteGuests();
                        break;
                    default:
                        return false;
                }
                mode.finish();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                list_adapter.clearItemSelected();
                floatingAddBtn.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guest_menu,menu);
        return true;
    }

    public void callAdd(View v){
        getName(NEW);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.guest_menu_back:
                processFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNewGuest(){
        if(!newName.isEmpty()){
                guestList.add(new Person(newName,newConfirmed));
                list_adapter.notifyDataSetChanged();
            }else{
                Toast.makeText(this, R.string.empty_name_error, Toast.LENGTH_SHORT).show();
            }

    }

    private void changeGuest(){
        if(newName != guestList.get(selectedPosition).getName()){
//            guestList.set(selectedPosition,newName);
            guestList.get(selectedPosition).setName(newName);
            guestList.get(selectedPosition).setConfirmed(newConfirmed);
            list_adapter.notifyDataSetChanged();
        }

        selectedPosition = -1;
    }

    private void deleteGuests(){
        for(int i : list_adapter.getSelectedPositions()){
            guestList.remove(i);
        }
        list_adapter.notifyDataSetChanged();
    }

    private void getName(final int MODE){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(MODE == NEW){
            builder.setTitle(R.string.new_guest);
        }else{
            builder.setTitle(R.string.edit_guest);
        }



//        View inflatedView = LayoutInflater.from(this).inflate(R.layout.layout_guest_input, null);
        View inflatedView = View.inflate(this,R.layout.layout_guest_input,null);

        final EditText input = inflatedView.findViewById(R.id.guest_editText);
        final Checkable check = inflatedView.findViewById(R.id.guest_checkBox);

        if(MODE == ALTER){
            input.setText(guestList.get(selectedPosition).getName());
            check.setChecked(guestList.get(selectedPosition).isConfirmed());
        }

        builder.setView(inflatedView);

        builder.setPositiveButton(getString(R.string.OK), (dialog, which) -> {
            newName = input.getText().toString();
            newConfirmed = check.isChecked();

            if(MODE == ALTER){
                changeGuest();
            }else{
                addNewGuest();
            }
            dialog.dismiss();
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void processFinish(){
        Intent intent = new Intent();

        intent.putExtra(createActivity.GUEST_LIST,guestList);

        setResult(Activity.RESULT_OK,intent);

        finish();
    }

    @Override
    public void onBackPressed() {
        processFinish();
    }
}
