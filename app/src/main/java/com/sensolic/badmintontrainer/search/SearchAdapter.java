package com.sensolic.badmintontrainer.search;

import android.content.Context;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.sensolic.badmintontrainer.MainActivity;
import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.Storage;

import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends BaseAdapter {

    Storage storage;
    Context context;
    LayoutInflater inflater;
    List<SearchEntry> entryList;
    ArrayList<SearchEntry> arrayList;

    public SearchAdapter(Context context, List<SearchEntry> entryList){
        this.context = context;
        this.entryList = entryList;
        inflater = LayoutInflater.from(context);
        arrayList = new ArrayList<>();
        arrayList.addAll(entryList);
        storage = Storage.getInstance(context);
    }

    public class ViewHolder{
        TextView name, ID;
    }

    @Override
    public int getCount() {
        return entryList.size();
    }

    @Override
    public Object getItem(int i) {
        return entryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    //Variables for touch detection
    boolean pressed = false;
    long start, end;

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_list_view, null);

            holder.name = view.findViewById(R.id.name);
            holder.ID = view.findViewById(R.id.ID);

            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(entryList.get(position).getName());
        holder.ID.setText(entryList.get(position).getID());

        view.setClickable(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pressed = true;
                        start = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                        end = System.currentTimeMillis();
                        if(pressed && (end-start >= 200)){
                            // Showing the popup menu
                            PopupMenu menu = new PopupMenu(context, view);
                            menu.getMenuInflater().inflate(R.menu.popup_menu_search, menu.getMenu());
                            menu.setOnMenuItemClickListener(menuItem -> {
                                if(menuItem.getTitle().equals("Delete")){
                                    ViewHolder h = (ViewHolder) view.getTag();
                                    String id = h.ID.getText().toString();
                                    id = id.substring(id.indexOf('M')+1);
                                    storage.deleteMatch(id);
                                    int index = 0;
                                    for(SearchEntry s : entryList){
                                        if(s.getID().equals("#M"+id)){
                                            break;
                                        }
                                        index++;
                                    }
                                    entryList.remove(index);
                                    arrayList.remove(index);

                                    // Vanishing Animation of list item
                                    Animation animation = new ScaleAnimation(1, 1, 1, 0);
                                    animation.setDuration(300);
                                    view.startAnimation(animation);
                                    view.postDelayed(() -> notifyDataSetChanged(),300);
                                }
                                return true;
                            });
                            menu.show();

                            start = 0;
                            end = 0;
                            pressed = false;
                        } else {
                            // Click animation of list item
                            Animation animation = new AlphaAnimation(0.3f, 1.0f);
                            animation.setDuration(300);
                            view.startAnimation(animation);
                        }
                        break;
                }
                return true;
            }
        });

        return view;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        entryList.clear();
        if(charText.length() == 0){
            entryList.addAll(arrayList);
        } else{
            for(SearchEntry e : arrayList){
                if(e.getName().toLowerCase(Locale.getDefault()).contains(charText)
                    || e.getID().toLowerCase(Locale.getDefault()).contains(charText)){
                    entryList.add(e);
                }
            }
        }
        notifyDataSetChanged();
    }
}
