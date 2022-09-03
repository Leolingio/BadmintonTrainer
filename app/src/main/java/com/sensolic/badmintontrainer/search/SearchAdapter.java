package com.sensolic.badmintontrainer.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends BaseAdapter {

    Storage storage;
    Context context;
    LayoutInflater inflater;
    List<Searchable> entryList;
    ArrayList<Searchable> arrayList;
    SearchActivity searchActivityInstance;

    public SearchAdapter(Context context, List<Searchable> entryList, SearchActivity searchActivityInstance){
        this.context = context;
        this.entryList = entryList;
        inflater = LayoutInflater.from(context);
        arrayList = new ArrayList<>();
        arrayList.addAll(entryList);
        storage = Storage.getInstance(context);
        this.searchActivityInstance = searchActivityInstance;
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
    boolean pressed = false, popupMenuShowing = false;
    long start, end;

    @SuppressLint("ClickableViewAccessibility")
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
        holder.name.setText(entryList.get(position).getInfo());
        holder.ID.setText(entryList.get(position).getIDInfo());

        view.setClickable(true);
        view.setOnTouchListener((view1, motionEvent) -> {
            //Identifying the match
            ViewHolder h = (ViewHolder) view1.getTag();
            String id = h.ID.getText().toString();
            boolean isMatch = false;
            if(id.charAt(1) == 'M'){
                id = id.substring(id.indexOf('M') + 1);
                isMatch = true;
            } else if(id.charAt(1) == 'P'){
                id = id.substring(id.indexOf('P') + 1);
            } else{
                // Invalid ID-notation
                return false;
            }
            int index = 0;
            for (Searchable s : entryList) {
                if (isMatch && s.getIDInfo().equals("#M" + id)
                    || !isMatch && s.getIDInfo().equals("#P" + id)) {
                    break;
                }
                index++;
            }
            Searchable searchable = entryList.get(index);

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressed = true;
                    start = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    end = System.currentTimeMillis();
                    if (end - start < 200){
                        // Click animation of list item
                        Animation animation = new AlphaAnimation(0.3f, 1.0f);
                        animation.setDuration(300);
                        view1.startAnimation(animation);
                        if(!popupMenuShowing){
                            StatsActivity.showInfo(searchable);
                            searchActivityInstance.finish();
                        }
                    }
                    pressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    end = System.currentTimeMillis();
                    if (pressed && (end - start >= 200) && !popupMenuShowing) {
                        // Showing the popup menu
                        PopupMenu menu = new PopupMenu(context, view1);
                        menu.getMenuInflater().inflate(R.menu.popup_menu_search, menu.getMenu());
                        menu.setOnMenuItemClickListener(menuItem -> {
                            if (menuItem.getTitle().equals("Delete")) {
                                storage.deleteMatch(((Match) searchable).getMatchID());

                                arrayList.clear();
                                storage.addStoredObjects(arrayList);
                                entryList.clear();
                                entryList.addAll(arrayList);

                                // Vanishing Animation of list item
                                Animation animation = new ScaleAnimation(1, 1, 1, 0);
                                animation.setDuration(300);
                                view1.startAnimation(animation);
                                view1.postDelayed(SearchAdapter.this::notifyDataSetChanged, 300);
                            }
                            popupMenuShowing = false;
                            return true;
                        });
                        menu.setOnDismissListener(new PopupMenu.OnDismissListener() {
                            @Override
                            public void onDismiss(PopupMenu popupMenu) {
                                popupMenuShowing = false;
                            }
                        });
                        if(searchable instanceof Match) {
                            menu.show();
                            popupMenuShowing = true;
                        }

                        start = 0;
                        end = 0;
                    }
                    break;
            }
            return true;
        });

        return view;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        entryList.clear();
        if(charText.length() == 0){
            entryList.addAll(arrayList);
        } else{
            for(Searchable e : arrayList){
                if(e.getInfo().toLowerCase(Locale.getDefault()).contains(charText)
                    || e.getIDInfo().toLowerCase(Locale.getDefault()).contains(charText)){
                    entryList.add(e);
                }
            }
        }
        notifyDataSetChanged();
    }
}
