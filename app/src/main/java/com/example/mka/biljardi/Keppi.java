package com.example.mka.biljardi;

import android.graphics.RectF;
import android.util.Log;

/**
 * Created by mka on 19.12.2015.
 */
public class Keppi {
    RectF rect;
    private LautaData lautadata = new LautaData();
    private float jousivakio;
    private float poikkeama_x, poikkeama_y;
    private float startX, startY, stopX, stopY;

    public Keppi(int screenX, int screenY){
        this.jousivakio = 10f;
        this.poikkeama_x = 0.0f;
        this.poikkeama_y = 0.0f;
        this.startX = lautadata.valkoinenX * screenX;
        this.startY = lautadata.valkoinenY * screenY;
        //this.stopX = lautadata.alkuKeppiX * screenX;
        //this.stopY = lautadata.alkuKeppiY *screenY;
        this.stopX = lautadata.valkoinenX * screenX;
        this.stopY = lautadata.valkoinenY * screenY;
    }

    // Isketään lyöntipalloa
    public void iske(Pallo pallo)
    {float vx, vy;
        vx = -(this.getStopX()-this.getStartX())*0.005f;
        vy = -(this.getStopY()-this.getStartY())*0.005f;
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

    public void reset(int screenX, int screenY){
        this.startX = lautadata.valkoinenX * screenX;
        this.startY = lautadata.valkoinenY * screenY;
        //this.stopX = lautadata.alkuKeppiX * screenX;
        //this.stopY = lautadata.alkuKeppiY * screenY;
        this.stopX = lautadata.valkoinenX * screenX;
        this.stopY = lautadata.valkoinenY * screenY;
    }



    //Asetetaan pallolle kepin iskua vastaava lähtönopeus
    //public void iske(Pallo pallo) {
    //    pallo.setPalloVX(this.jousivakio * this.poikkeama_x);
    //    pallo.setPalloVY(this.jousivakio * this.poikkeama_y);
    //}

    public void set_poikkeama_x(float x1, float x2){
        this.poikkeama_x = x2-x1;
    }

    public void set_poikkeama_y(float y1, float y2){
        this.poikkeama_y = y2-y1;
    }

}
