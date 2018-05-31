package com.example.viniciusmn.events.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.viniciusmn.events.R;

import java.util.ArrayList;

public class GuestListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> guestList;
    private ArrayList<Integer> selectedPositions;
    private boolean lightTheme;

    private static LayoutInflater inflater = null;

    public GuestListAdapter(Context context, ArrayList<String> guestList,boolean lightTheme) {
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
        if(selectedPositions.contains(new Integer(position))){
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
        if(convertView==null){
            convertView = inflater.inflate(R.layout.layout_guest_item,null);
        }

        ((TextView)convertView.findViewById(R.id.guest_name)).setText(guestList.get(position));

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
