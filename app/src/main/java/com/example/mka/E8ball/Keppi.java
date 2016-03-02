package com.example.mka.E8ball;

import android.graphics.RectF;

/**
 * Created by mka on 19.12.2015.
 */
public class Keppi {
    RectF rect;
    private LautaData lautadata = new LautaData();
    private float jousivakio;
    private float poikkeama_x, poikkeama_y;
    private float startX, startY, stopX, stopY;
    private boolean piirra;

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
        this.piirra = false;
    }

    public void setPiirra(boolean piirra){
        this.piirra = piirra;
    }

    public boolean getPiirra(){
        return this.piirra;
    }

    // Isketään lyöntipalloa
    public void iske(Pallo pallo)
    {float vx, vy, v, vmax;
        vmax = 0.5f;
        vx = -(this.getStopX()-this.getStartX())*0.005f;
        vy = -(this.getStopY()-this.getStartY())*0.005f;
        v= (float) Math.sqrt(vx*vx + vy*vy);
        if (v > vmax){
            vx=vx/v*vmax;
            vy=vy/v*vmax;
        }
        //*0.005
        //pallo.setPalloVX(vx/fps);
        //pallo.setPalloVY(vy/fps);
        pallo.setPalloVX(vx);
        pallo.setPalloVY(vy);
        //Log.i("IIISKU", Float.toString(vy));
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
        //Log.i("SIIRTO", " NYT!");
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
