package com.example.mka.biljardi;

import android.graphics.RectF;

/**
 * Created by mka on 19.12.2015.
 */
public class Keppi {
    RectF rect;
    private LautaData lautadata = new LautaData();
    private double jousivakio;
    private double poikkeama_x, poikkeama_y;

    public Keppi(int screenX, int screenY){
        this.jousivakio = 10;
        this.poikkeama_x = 0.0;
        this.poikkeama_y = 0.0;
        rect = new RectF();
    }

    // Isketään lyöntipalloa
    public void iske(){
        System.out.print("Hello");
    }

    // palautetaan tämänhetkinen kepin paikka
    public RectF getRect(){
        return rect;
    }

    //asetetaan alkupiste, joka on lyöntipallon paikka
    public void update_alku(float x, float y){
        rect.left = x;
        rect.top = y;
    }

    //asetetaan kepin loppupiste, joka raahataan sormella
    public void update_loppu(float x, float y){
        rect.right = x;
        rect.bottom = y;
    }

    public void reset(int x, int y){
        rect.left = x / 2;
        rect.top = y / 2;
        rect.right = x / 2;
        rect.bottom = y /2;
    }



    //Asetetaan pallolle kepin iskua vastaava lähtönopeus
    //public void iske(Pallo pallo) {
    //    pallo.setPalloVX(this.jousivakio * this.poikkeama_x);
    //    pallo.setPalloVY(this.jousivakio * this.poikkeama_y);
    //}

    public void set_poikkeama_x(double x1, double x2){
        this.poikkeama_x = x2-x1;
    }

    public void set_poikkeama_y(double y1, double y2){
        this.poikkeama_y = y2-y1;
    }

}