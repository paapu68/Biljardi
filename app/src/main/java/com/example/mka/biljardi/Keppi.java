package com.example.mka.biljardi;

import android.graphics.RectF;
import android.util.Log;

/**
 * Created by mka on 19.12.2015.
 */
public class Keppi {
    RectF rect;
    private LautaData lautadata = new LautaData();
    private double jousivakio;
    private double poikkeama_x, poikkeama_y;
    private float startX, startY, stopX, stopY;

    public Keppi(int screenX, int screenY){
        this.jousivakio = 10;
        this.poikkeama_x = 0.0;
        this.poikkeama_y = 0.0;
        rect = new RectF();
        this.startX = 0;
        this.startY = 0;
        this.stopX = lautadata.alkuKeppiX;
        this.stopY = lautadata.alkuKeppiY;
    }

    // Isketään lyöntipalloa
    public void iske(Pallo pallo)
    {float vx, vy;
        vx = (this.getStopX()-this.getStartX())*1f-3;
        vy = (this.getStopY()-this.getStartY())*1f-3;
        pallo.setPalloVX(vx);
        pallo.setPalloVY(vy);
        Log.i("ISKU", Float.toString(vx));
    }

    // palautetaan tämänhetkinen kepin paikka
    public RectF getRect(){
        return rect;
    }

    // palautetaan tämänhetkinen kepin paikka, alkuX
    public float getStartX(){
        return startX;
    }

    // palautetaan tämänhetkinen kepin paikka, alkuY
    public float getStartY(){
        return startY;
    }

    // palautetaan tämänhetkinen kepin paikka, loppuX
    public float getStopX() {
        return stopX;
    }

    // palautetaan tämänhetkinen kepin paikka
    public float getStopY(){
        return stopY;
    }

    //asetetaan alkupiste, joka on lyöntipallon paikka
    public void update_alku(float x, float y){
	    this.startX = x;
	    this.startY = y;
    }

    //asetetaan kepin loppupiste, joka raahataan sormella
    public void update_loppu(float x, float y){
	    this.stopX = x;
	    this.stopY = y;
        Log.i("SIIRTO", " NYT!");
    }

    public void reset(int x, int y){
	    this.startX = x / 2;
	    this.startY = y / 2;
	    this.stopX = x / 2;
	    this.stopY = y / 2;
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
