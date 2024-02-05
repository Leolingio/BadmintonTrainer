package com.sensolic.badmintontrainer.matchesAdapter;

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
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Match;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;

import java.util.ArrayList;
import java.util.List;

public class RecentMatchesAdapter extends BaseAdapter {

    Storage storage;
    Context context;
    LayoutInflater inflater;
    List<Match> matchList;
    ArrayList<Match> arrayList;

    public RecentMatchesAdapter(Context context, List<Match> entryList) {
        this.context = context;
        this.matchList = entryList;
        inflater = LayoutInflater.from(context);
        arrayList = new ArrayList<>();
        arrayList.addAll(entryList);
        storage = Storage.getInstance(context);
    }

    public static class ViewHolder {
        TextView matchInfoTitle, team1player1, team1player2, team2player1, team2player2, score1,
                score2, score3;
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
        float sizeHead = 0, sizeText = 0;
        switch(Settings.textSize()){
            case 1:
                sizeHead = Settings.TEXTSIZE_SMALL_HEADER;
                sizeText = Settings.TEXTSIZE_SMALL_TEXT;
                break;
            case 2:
                sizeHead = Settings.TEXTSIZE_NORMAL_HEADER;
                sizeText = Settings.TEXTSIZE_NORMAL_TEXT;
                break;
            case 3:
                sizeHead = Settings.TEXTSIZE_BIG_HEADER;
                sizeText = Settings.TEXTSIZE_BIG_TEXT;
                break;
        }

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.match_list_view, null);

            holder.matchInfoTitle = view.findViewById(R.id.matchInfoTitle);
            holder.team1player1 = view.findViewById(R.id.team1player1);
            holder.team1player2 = view.findViewById(R.id.team1player2);
            holder.team2player1 = view.findViewById(R.id.team2player1);
            holder.team2player2 = view.findViewById(R.id.team2player2);
            holder.score1 = view.findViewById(R.id.score1);
            holder.score2 = view.findViewById(R.id.score2);
            holder.score3 = view.findViewById(R.id.score3);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Match current = arrayList.get(position);
        Player buffer = current.getTeam1Player1();
        holder.matchInfoTitle.setText(current.getInfo()+" "+current.getIDInfo());
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
        holder.score1.setText(current.getScoreFirst());
        holder.score2.setText(current.getScoreSecond());
        holder.score3.setText(current.getScoreThird());

        view.setClickable(true);
        view.setOnTouchListener((view1, motionEvent) -> {
            //Identifying the match
            ViewHolder h = (ViewHolder) view1.getTag();
            String id = h.matchInfoTitle.getText().toString();
            id = id.substring(id.indexOf('#'));
            if (id.charAt(1) == 'M') {
                id = id.substring(id.indexOf('M') + 1);
            } else {
                // Invalid ID-notation
                return false;
            }
            int index = 0;
            for (Match m : matchList) {
                if (m.getIDInfo().equals("#M" + id)) {
                    break;
                }
                index++;
            }
            Match match = matchList.get(index);

            if(storage.getMatchData(match.getMatchID()) == null){
                if(holder.matchInfoTitle.getVisibility() == View.GONE){
                    return false;
                }
                holder.matchInfoTitle.setVisibility(View.GONE);
                holder.team1player1.setVisibility(View.GONE);
                holder.team1player2.setVisibility(View.GONE);
                holder.team2player1.setVisibility(View.GONE);
                holder.team2player2.setVisibility(View.GONE);
                holder.score1.setVisibility(View.GONE);
                holder.score2.setVisibility(View.GONE);
                holder.score3.setVisibility(View.GONE);
                Toast.makeText(context, "This match doesn't exist", Toast.LENGTH_SHORT).show();
                return false;
            }

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressed = true;
                    start = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    end = System.currentTimeMillis();
                    if (end - start < 200) {
                        // Click animation of list item
                        Animation animation = new AlphaAnimation(0.3f, 1.0f);
                        animation.setDuration(300);
                        view1.startAnimation(animation);
                        if (!popupMenuShowing) {
                            StatsActivity.showInfo(match);
                            context.startActivity(new Intent(context, ReloadActivity.class));
                        }
                    }
                    pressed = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
            }
            return true;
        });

        holder.matchInfoTitle.setTextSize(sizeHead);
        holder.team1player1.setTextSize(sizeText);
        holder.team1player2.setTextSize(sizeText);
        holder.team2player1.setTextSize(sizeText);
        holder.team2player2.setTextSize(sizeText);
        holder.score1.setTextSize(sizeText);
        holder.score2.setTextSize(sizeText);
        holder.score3.setTextSize(sizeText);
        return view;
    }

}
