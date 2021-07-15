package com.sensolic.badmintontrainer.graphics;

import android.widget.ImageView;

public class Figure {

    public enum Types {OWN, ENEMY, BALL};
    private Types type;
    private ImageView img;
    private int x;
    private int y;

    public Figure(Types type, ImageView img){
        this.type = type;
        this.img = img;
    }

    public Figure(Types type, ImageView img, int x, int y){
        this.type = type;
        this.img = img;
        this.x = x;
        this.y = y;
    }

    public ImageView getImg(){
        return img;
    }

}
