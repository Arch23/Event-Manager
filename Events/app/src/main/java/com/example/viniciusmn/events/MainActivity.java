package com.example.viniciusmn.events;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.viniciusmn.events.Adapter.EventsListAdapter;
import com.example.viniciusmn.events.Classes.Event;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ListView contentListView;

    EventsListAdapter list_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentListView = findViewById(R.id.contentListView);
        list_adapter = new EventsListAdapter(this,getEvents());
        contentListView.setAdapter(list_adapter);
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
                createActivity.call(this);
                return true;
            case R.id.main_menu_about:
                aboutActivity.call(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ArrayList<Event> getEvents(){
        ArrayList<Event> events = new ArrayList<>();

        ArrayList<String> convidados = new ArrayList<>();
        convidados.add("convidado 1");
        convidados.add("convidado 2");
        convidados.add("convidado 3");
        convidados.add("convidado 4");
        convidados.add("convidado 5");

        events.add(new Event("name teste 1", Calendar.getInstance().getTime(),"local teste 1","descriçao teste 1",convidados));
        events.add(new Event("name teste 2", Calendar.getInstance().getTime(),"local teste 2","descriçao teste 2",convidados));
        events.add(new Event("name teste 3", Calendar.getInstance().getTime(),"local teste 3","descriçao teste 3",convidados));
        events.add(new Event("name teste 4", Calendar.getInstance().getTime(),"local teste 4","descriçao teste 4",convidados));
        events.add(new Event("name teste 5", Calendar.getInstance().getTime(),"local teste 5","descriçao teste 5",convidados));
        events.add(new Event("name teste 6", Calendar.getInstance().getTime(),"local teste 6","descriçao teste 6",convidados));
        events.add(new Event("name teste 7", Calendar.getInstance().getTime(),"local teste 7","descriçao teste 7",convidados));
        events.add(new Event("name teste 8", Calendar.getInstance().getTime(),"local teste 8","descriçao teste 8",convidados));
        events.add(new Event("name teste 9", Calendar.getInstance().getTime(),"local teste 9","descriçao teste 9",convidados));
        events.add(new Event("name teste 10", Calendar.getInstance().getTime(),"local teste 10","descriçao teste 10",convidados));

        return events;
    }
}
