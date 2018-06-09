package com.example.viniciusmn.events.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.viniciusmn.events.Classes.Event;
import com.example.viniciusmn.events.MainActivity;
import com.example.viniciusmn.events.R;

import java.util.ArrayList;

import static com.example.viniciusmn.events.Utils.Misc.dateToString;
import static com.example.viniciusmn.events.Utils.Misc.getBitmapFromURIResized;
import static com.example.viniciusmn.events.Utils.Misc.imageViewAnimatedChange;

public class EventsListAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<Event> eventList;
    private ArrayList<Integer> selectedPositions;
    private boolean lightTheme;

    private LruCache<String,Bitmap> mMemoryCache;

    private static LayoutInflater inflater = null;

    public EventsListAdapter(MainActivity mainActivity, ArrayList<Event> eventList, boolean lightTheme){
        this.eventList = eventList;
        this.context = mainActivity;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        selectedPositions = new ArrayList<>();
        this.lightTheme = lightTheme;

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);

        final int cacheSize = maxMemory/6;

        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount()/1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key,Bitmap bitmap){
        if(getBitmapFromMenCache(key)==null){
            mMemoryCache.put(key,bitmap);
        }
    }

    public Bitmap getBitmapFromMenCache(String key){
        return mMemoryCache.get(key);
    }

    public void loadBitmap(int redId, ImageView imageV, Uri imageUri){
        final String imageKey = String.valueOf(redId);

        final Bitmap bitmap = getBitmapFromMenCache(imageKey);
        if(bitmap != null){
            setBitmap(bitmap,imageV,false);
        }else{
            imageV.setScaleType(ImageView.ScaleType.CENTER);
            imageV.setImageResource(android.R.drawable.ic_menu_report_image);
            BitmapWorkerTask task = new BitmapWorkerTask(imageV,context,imageUri);
            task.execute(redId);
        }
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

    private void setBitmap(Bitmap b, ImageView i,boolean transition){
        if(b==null){
            i.setVisibility(View.GONE);
            i.setImageDrawable(null);
//            i.setScaleType(ImageView.ScaleType.CENTER);
//            i.setImageResource(android.R.drawable.ic_menu_report_image);
        }else{
            if(transition){
                imageViewAnimatedChange(context,i,b);
            }else{
                i.setScaleType(ImageView.ScaleType.CENTER_CROP);
                i.setImageBitmap(b);

            }
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
            convertView = inflater.inflate(R.layout.layout_event_item,parent,false);
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
            loadBitmap(currentEvent.getUid(),holder.event_imageView,currentEvent.getImageURI());
            holder.event_imageView.setVisibility(View.VISIBLE);
        }

        holder.event_name.setText(currentEvent.getName());
        holder.event_date.setText(dateToString(currentEvent.getDate()));
        holder.event_place.setText(currentEvent.getPlace());

        holder.event_invited.setText(String.format(context.getResources().getString(R.string.confirmed),currentEvent.getConfirmedInvited(),currentEvent.getInvitedSize()));

        return convertView;
    }

    private class BitmapWorkerTask extends AsyncTask<Integer,Void,Void> {

        private ImageView imageV;
        private Context ctx;
        private Uri imageUri;
        private Bitmap bitmap;

        public BitmapWorkerTask(ImageView imageV, Context ctx, Uri imageUri) {
            this.imageV = imageV;
            this.ctx = ctx;
            this.imageUri = imageUri;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            setBitmap(bitmap,imageV,true);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            Bitmap bitmap = getBitmapFromURIResized(ctx,imageUri,200,200);
            if(bitmap != null){
                addBitmapToMemoryCache(String.valueOf(integers[0]),bitmap);
            }
            this.bitmap = bitmap;
            return null;
        }
    }
}
