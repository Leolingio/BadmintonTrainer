package com.sensolic.badmintontrainer.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sensolic.badmintontrainer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends BaseAdapter {

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
