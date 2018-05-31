package com.example.viniciusmn.events.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.viniciusmn.events.Classes.Event;
import com.example.viniciusmn.events.MainActivity;
import com.example.viniciusmn.events.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.viniciusmn.events.Utils.dateToString;
import static com.example.viniciusmn.events.Utils.getBitmapFromURI;
import static com.example.viniciusmn.events.Utils.setTopCrop;

public class EventsListAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Event> eventList;
    private ArrayList<Integer> selectedPositions;
    private boolean lightTheme;

    private static LayoutInflater inflater = null;

    public EventsListAdapter(MainActivity mainActivity, ArrayList<Event> eventList, boolean lightTheme){
        this.eventList = eventList;
        this.context = mainActivity;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        selectedPositions = new ArrayList<>();
        this.lightTheme = lightTheme;
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
        ImageView event_imageView;
        View event_card;
    }

    public void toggleItemSelected(int position){
        if(selectedPositions.contains(position)){
            selectedPositions.remove(new Integer(position));
        }else{
            selectedPositions.add(position);
        }
    }

    public ArrayList<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    public void clearItemSelected(){
        selectedPositions.clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();

        if(convertView == null){
            convertView = inflater.inflate(R.layout.layout_event_item,null);
        }

        holder.event_name = convertView.findViewById(R.id.event_name);
        holder.event_date = convertView.findViewById(R.id.event_date);
        holder.event_place = convertView.findViewById(R.id.event_place);
        holder.event_invited = convertView.findViewById(R.id.event_invited);
        holder.event_card = convertView.findViewById(R.id.card_view);
        holder.event_imageView = convertView.findViewById(R.id.event_imageView);


        if(selectedPositions.contains(position)){
            int light = ContextCompat.getColor(context, R.color.CardBackgroundSelected),
                dark = ContextCompat.getColor(context,R.color.DarkCardBackgroundSelected);
            holder.event_card.setBackgroundColor(lightTheme?light:dark);
        }else{
            int light = ContextCompat.getColor(context,R.color.CardBackground),
                    dark = ContextCompat.getColor(context,R.color.DarkCardBackground);
            holder.event_card.setBackgroundColor(lightTheme?light:dark);
        }

        Event currentEvent = eventList.get(position);

        if(currentEvent.getImageURIString().isEmpty()){
            holder.event_imageView.setVisibility(View.GONE);
            holder.event_imageView.setImageDrawable(null);
        }else{
            holder.event_imageView.setImageBitmap(getBitmapFromURI(context,currentEvent.getImageURI()));
            holder.event_imageView.setVisibility(View.VISIBLE);
//            setTopCrop(convertView,R.id.event_imageView);
        }

        holder.event_name.setText(currentEvent.getName());
        holder.event_date.setText(dateToString(currentEvent.getDate()));
        holder.event_place.setText(currentEvent.getPlace());

        holder.event_invited.setText(context.getString(R.string.confirmed,currentEvent.getConfirmedInvited(),currentEvent.getInvitedSize()));

        return convertView;
    }
}
