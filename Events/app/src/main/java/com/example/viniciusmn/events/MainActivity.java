package com.example.viniciusmn.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.viniciusmn.events.Adapter.EventsListAdapter;
import com.example.viniciusmn.events.Classes.Event;
import com.example.viniciusmn.events.DAO.EventDatabase;

import java.util.ArrayList;

import static com.example.viniciusmn.events.Utils.Misc.readSharedTheme;

public class MainActivity extends AppCompatActivity {

    private ListView contentListView;

    private EventsListAdapter list_adapter;
    private ArrayList<Event> events;

    public static final int NEW_EVENT = 0;
    public static final int ALTER_EVENT = 1;
    public static final String EVENT_OBJ = "EVENT_OBJ";

    public static final String SHARED_FILE = "com.example.viniciusmn.events.shared_file";
    public static final String STYLE = "STYLE";


    private int selectedPosition = -1;
    private boolean lightTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lightTheme = readSharedTheme(this)==R.style.AppTheme;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentListView = findViewById(R.id.contentListView);

        contentListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            callCreate(true);
        });

        contentListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        contentListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                list_adapter.toggleItemSelected(position);
                list_adapter.notifyDataSetChanged();

                int totalSelected = contentListView.getCheckedItemCount();

                if(totalSelected > 0){
                    mode.setTitle(getResources().getQuantityString(R.plurals.selected,totalSelected,totalSelected));
                }

                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.main_menu_context,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                menu.getItem(0).setVisible(contentListView.getCheckedItemCount() < 2);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.main_menu_context_edit:
                        selectedPosition = list_adapter.getSelectedPositions().get(0);
                        callCreate(true);
                        break;
                    case R.id.main_menu_context_delete:
                        deleteEvents();
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
            }
        });
        populateListView();
    }

    private void populateListView(){
        AsyncTask.execute(()->{
            events = new ArrayList<>(EventDatabase.getInstance(this).eventDAO().getAll());
            list_adapter = new EventsListAdapter(this, events,lightTheme);
            MainActivity.this.runOnUiThread(()-> contentListView.setAdapter(list_adapter));
        });
    }

    private void deleteEvents(){
        for(int i : list_adapter.getSelectedPositions()){
            confirmDialog(events.get(i).getName(),events.get(i));
        }
    }

    private void deleteEvent(Event event){
        AsyncTask.execute(()->{
            EventDatabase.getInstance(this).eventDAO().deleteEvent(event);
            populateListView();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.main_menu_add:
                callCreate();
                return true;
            case R.id.main_menu_about:
                aboutActivity.call(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callCreate(){
        callCreate(false);
    }

    private void callCreate(boolean edit){
        Intent intent = new Intent(this,createActivity.class);

        if(edit){
            intent.putExtra(EVENT_OBJ,events.get(selectedPosition));
            startActivityForResult(intent,ALTER_EVENT);
        }else{
            startActivityForResult(intent,NEW_EVENT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            Bundle bundle = data.getExtras();

            if(bundle != null){
                Event event = (Event) bundle.getSerializable(EVENT_OBJ);
                AsyncTask.execute(()->{
                    if(requestCode == NEW_EVENT){
                        EventDatabase.getInstance(this).eventDAO().insertEvent(event);
                    }else{
                        EventDatabase.getInstance(this).eventDAO().updateEvent(event);
                        selectedPosition = -1;
                    }
                    MainActivity.this.runOnUiThread(()-> Toast.makeText(this,requestCode==NEW_EVENT?R.string.event_created: R.string.event_saved, Toast.LENGTH_SHORT).show());
                    populateListView();
                });
            }
        }
    }

    private void confirmDialog(String eventName, final Event event){
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_dialog_title)
                .setMessage(getString(R.string.confirm_dialog_body)+" "+eventName+"?")
                .setPositiveButton(R.string.OK, (dialog, which) -> deleteEvent(event))
                .setNegativeButton(R.string.cancel, null).show();
    }
}
