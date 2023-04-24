package com.sensolic.badmintontrainer.leaderboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sensolic.badmintontrainer.R;
import com.sensolic.badmintontrainer.ReloadActivity;
import com.sensolic.badmintontrainer.Settings;
import com.sensolic.badmintontrainer.StatsActivity;
import com.sensolic.badmintontrainer.data.Player;
import com.sensolic.badmintontrainer.data.Storage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardAdapter extends BaseAdapter {

    Storage storage;
    Context context;
    LayoutInflater inflater;
    List<Player> playerList;
    ArrayList<Player> arrayList;

    public LeaderboardAdapter(Context context, List<Player> entryList) {
        this.context = context;
        this.playerList = entryList;
        inflater = LayoutInflater.from(context);
        playerList.sort(Comparator.comparingInt(Player::getRankingPoints).reversed());
        arrayList = new ArrayList<>();
        arrayList.addAll(entryList);
        storage = Storage.getInstance(context);
    }

    public static class ViewHolder {
        TextView rankNumber, name, ID, points;
        ImageView featherballIcon;
    }

    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Object getItem(int i) {
        return playerList.get(i);
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

        float sizeHead = 0;
        switch(Settings.textSize()){
            case 1:
                sizeHead = Settings.TEXTSIZE_SMALL_HEADER;
                break;
            case 2:
                sizeHead = Settings.TEXTSIZE_NORMAL_HEADER;
                break;
            case 3:
                sizeHead = Settings.TEXTSIZE_BIG_HEADER;
                break;
        }

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.leaderboard_list_view, null);

            holder.rankNumber = view.findViewById(R.id.rankNumber);
            holder.name = view.findViewById(R.id.name);
            holder.ID = view.findViewById(R.id.ID);
            holder.points = view.findViewById(R.id.points);
            holder.featherballIcon = view.findViewById(R.id.featherballIcon);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.rankNumber.setText((position+1)+".");
        switch(position){
            case 0:
                holder.rankNumber.setTextColor(Color.rgb(255,204,51));
                holder.featherballIcon.setVisibility(View.VISIBLE);
                holder.featherballIcon.setImageResource(R.drawable.featherball_gold);
                break;
            case 1:
                holder.rankNumber.setTextColor(Color.rgb(192,192,192));
                holder.featherballIcon.setVisibility(View.VISIBLE);
                holder.featherballIcon.setImageResource(R.drawable.featherball_silver);
                break;
            case 2:
                holder.rankNumber.setTextColor(Color.rgb(116,78,59));
                holder.featherballIcon.setVisibility(View.VISIBLE);
                holder.featherballIcon.setImageResource(R.drawable.featherball_bronze);
                break;
            default:
                holder.featherballIcon.setVisibility(View.GONE);
                holder.rankNumber.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(view.getResources().getDimensionPixelSize(R.dimen.sizeLeaderboardListItems), view.getResources().getDimensionPixelSize(R.dimen.sizeLeaderboardListItems));
                params.setMargins(view.getResources().getDimensionPixelSize(R.dimen.marginLeaderboardListItems),0,view.getResources().getDimensionPixelSize(R.dimen.marginLeaderboardListItems),0);
                holder.rankNumber.setLayoutParams(params);
                break;
        }
        holder.name.setText(playerList.get(position).getInfo());
        holder.ID.setText(playerList.get(position).getIDInfo());
        int points = playerList.get(position).getRankingPoints();
        if(points == 1) {
            holder.points.setText(points + " Point");
        } else{
            holder.points.setText(points + " Points");
        }

        view.setClickable(true);
        view.setOnTouchListener((view1, motionEvent) -> {
            //Identifying the player
            ViewHolder h = (ViewHolder) view1.getTag();
            String id = h.ID.getText().toString();
            if (id.charAt(1) == 'P') {
                id = id.substring(id.indexOf('P') + 1);
            } else {
                // Invalid ID-notation
                return false;
            }
            int index = 0;
            for (Player p : playerList) {
                if (p.getIDInfo().equals("#P" + id)) {
                    break;
                }
                index++;
            }
            Player player = playerList.get(index);

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
                            StatsActivity.showInfo(player);
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

        holder.name.setTextSize(sizeHead);
        holder.points.setTextSize(sizeHead);
        holder.rankNumber.setTextSize(sizeHead);

        return view;
    }

}