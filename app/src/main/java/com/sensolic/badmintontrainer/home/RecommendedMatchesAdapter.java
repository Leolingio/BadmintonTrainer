package com.sensolic.badmintontrainer.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.ReloadActivity;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;

import java.util.ArrayList;
import java.util.List;

public class RecommendedMatchesAdapter extends BaseAdapter {

    Storage storage;
    Context context;
    LayoutInflater inflater;
    List<Match> matchList;
    ArrayList<Match> arrayList;

    public RecommendedMatchesAdapter(Context context, List<Match> entryList) {
        this.context = context;
        this.matchList = entryList;
        inflater = LayoutInflater.from(context);
        arrayList = new ArrayList<>();
        arrayList.addAll(entryList);
        storage = Storage.getInstance(context);
    }

    public static class ViewHolder {
        TextView matchInfoTitle, team1player1, team1player2, team2player1, team2player2;
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int i) {
        return matchList.get(i);
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
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.recommended_matches_list_view, null);

            holder.matchInfoTitle = view.findViewById(R.id.matchInfoTitle);
            holder.team1player1 = view.findViewById(R.id.team1player1);
            holder.team1player2 = view.findViewById(R.id.team1player2);
            holder.team2player1 = view.findViewById(R.id.team2player1);
            holder.team2player2 = view.findViewById(R.id.team2player2);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Match current = arrayList.get(position);
        Player buffer = current.getTeam1Player1();
        holder.matchInfoTitle.setText(current.getInfo());
        if(buffer != null) {
            holder.team1player1.setText(buffer.getInfo());
        }
        buffer = current.getTeam1Player2();
        if(buffer != null) {
            holder.team1player2.setText(buffer.getInfo());
        }
        buffer = current.getTeam2Player1();
        if(buffer != null) {
            holder.team2player1.setText(buffer.getInfo());
        }
        buffer = current.getTeam2Player2();
        if(buffer != null) {
            holder.team2player2.setText(buffer.getInfo());
        }
        if(current.getMatchType() == 'S'){
            holder.team1player2.setVisibility(View.GONE);
            holder.team2player2.setVisibility(View.GONE);
        } else{
            holder.team1player2.setVisibility(View.VISIBLE);
            holder.team2player2.setVisibility(View.VISIBLE);
        }

        view.setClickable(false);

        return view;
    }

    public ArrayList<Match> getList(){
        return arrayList;
    }

}
