package com.example.viniciusmn.events.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.viniciusmn.events.Classes.Person;
import com.example.viniciusmn.events.R;

import java.util.ArrayList;

public class GuestListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Person> guestList;
    private ArrayList<Integer> selectedPositions;
    private boolean lightTheme;

    private static LayoutInflater inflater = null;

    public GuestListAdapter(Context context, ArrayList<Person> guestList,boolean lightTheme) {
        this.context = context;
        this.guestList = guestList;
        this.lightTheme = lightTheme;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        selectedPositions = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return guestList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public class Holder{
        TextView guest_name;
        CheckBox guest_checkListView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView = inflater.inflate(R.layout.layout_guest_item,parent,false);
        }

        Holder holder = new Holder();

        holder.guest_name = convertView.findViewById(R.id.guest_name);
        holder.guest_checkListView = convertView.findViewById(R.id.guest_checkListView);

        holder.guest_name.setText(guestList.get(position).getName());

        holder.guest_checkListView.setOnCheckedChangeListener(null);

        holder.guest_checkListView.setChecked(guestList.get(position).isConfirmed());

        holder.guest_checkListView.setOnCheckedChangeListener((buttonView, isChecked) -> {
            guestList.get(position).setConfirmed(isChecked);
        });

        if(selectedPositions.contains(position)){
            int light = ContextCompat.getColor(context, R.color.CardBackgroundSelected),
                    dark = ContextCompat.getColor(context,R.color.DarkCardBackgroundSelected);
            convertView.setBackgroundColor(lightTheme?light:dark);
        }else{
            int light = ContextCompat.getColor(context,R.color.CardBackground),
                    dark = ContextCompat.getColor(context,R.color.DarkCardBackground);
            convertView.setBackgroundColor(lightTheme?light:dark);
        }

        return convertView;
    }
}
