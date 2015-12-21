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
    private float startX, startY, stopX, stopY;

    public Keppi(int screenX, int screenY){
        this.jousivakio = 10;
        this.poikkeama_x = 0.0;
        this.poikkeama_y = 0.0;
        rect = new RectF();
        this.startX = 0;
        this.startY = 0;
        this.stopX = 0;
        this.stopY = 0;
    }

    // Isketään lyöntipalloa
    public void iske(){
        System.out.print("Hello");
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
        rect.left = x;
        rect.top = y;
	    this.startX = x;
	    this.startY = y;
    }

    //asetetaan kepin loppupiste, joka raahataan sormella
    public void update_loppu(float x, float y){
        rect.right = x;
        rect.bottom = y;
	    this.stopX = x;
	    this.stopY = y;
    }

    public void reset(int x, int y){
        rect.left = x / 2;
        rect.top = y / 2;
        rect.right = x / 2;
        rect.bottom = y /2;
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
