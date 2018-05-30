package com.example.viniciusmn.events.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.viniciusmn.events.Classes.Event;
import com.example.viniciusmn.events.MainActivity;
import com.example.viniciusmn.events.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventsListAdapter extends BaseAdapter{

    Context context;
    ArrayList<Event> eventList;

    private static LayoutInflater inflater = null;

    public EventsListAdapter(MainActivity mainActivity, ArrayList<Event> eventList){
        this.eventList = eventList;
        this.context = mainActivity;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder{
        TextView event_name;
        TextView event_date;
        TextView event_place;
        TextView event_invited;
    }

    public String getDateFormated(Date date){
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context.getApplicationContext());
        return dateFormat.format(date);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View view = inflater.inflate(R.layout.layout_event_item,null);

        holder.event_name = view.findViewById(R.id.event_name);
        holder.event_date = view.findViewById(R.id.event_date);
        holder.event_place = view.findViewById(R.id.event_place);
        holder.event_invited = view.findViewById(R.id.event_invited);

        holder.event_name.setText(eventList.get(position).getName());
        holder.event_date.setText(getDateFormated(eventList.get(position).getDate()));
        holder.event_place.setText(eventList.get(position).getPlace());
        holder.event_invited.setText(Integer.toString(eventList.get(position).getInvited().size()));

        return view;
    }
}
