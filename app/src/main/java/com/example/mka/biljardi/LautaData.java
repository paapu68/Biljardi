package com.example.mka.biljardi;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;

/**
 * Tähän luokkaan talletetaan pelilautaan liittyvät mitta yms tiedot.
 * @see http://en.wikipedia.org/wiki/Billiard_table
 */
public class LautaData {    
    private final float dt;
    final float minLautaX, minLautaY;
    final float maxLautaX, maxLautaY;
    final float pallonHalkaisija;
    final float pallonMassa;
    final float kepinPituus;
    final float reianHalkaisija;
    final float scale;
    final float alkuX, alkuY, valkoinenX, valkoinenY;
   
    final int pixelOffsetX;
    final int pixelOffsetY;
    final int pallonHalkaisijaPixel, reianHalkaisijaPixel;
    final int pituusXpixel, pituusYpixel;
    
    
    public LautaData(){
        this.dt = 0.0001f;  // aika-askel
        this.minLautaX = 0.0f;
        this.minLautaY = 0.0f;
        this.maxLautaX = 1.4f;
        this.maxLautaY = 2.7f;
        this.pallonHalkaisija = 0.0517f;
        this.pallonMassa = 0.16f;
        this.kepinPituus = 6.0f * this.pallonHalkaisija;
        this.reianHalkaisija = this.pallonHalkaisija * 1.6f;
        this.scale = 200.0f;
        this.pixelOffsetX = 50;
        this.pixelOffsetY = 50; 
        float ph;
        ph = this.pallonHalkaisija * this.scale;
        this.pallonHalkaisijaPixel = Math.round(ph);
        float rh;
        rh = this.reianHalkaisija * this.scale;
        this.reianHalkaisijaPixel = Math.round(rh);
        float pituusX = (this.maxLautaX-this.minLautaX)*this.scale;
        this.pituusXpixel = Math.round(pituusX);
        float pituusY = (this.maxLautaY-this.minLautaY)*this.scale;
        this.pituusYpixel = Math.round(pituusY);
        this.alkuX = this.maxLautaX / 2.0f;
        this.alkuY = 1.0f/4.0f*this.maxLautaY;
        this.valkoinenX = this.alkuX;
        this.valkoinenY = 3.0f/4.0f*this.maxLautaY;
        
    }

    public float getDT(){
        return this.dt;
    }
    
    public float getMaxLautaX(){
        return this.maxLautaX;
    }
    
    public float getMaxLautaY(){
        return this.maxLautaY;
    }
    
    public float getMinLautaX(){
        return this.minLautaX;
    }
    
    public float getMinLautaY(){
        return this.minLautaY;
    }
    
    public float getPallonHalkaisija(){
        return this.pallonHalkaisija;
    }
    
    public float getPallonMassa(){
        return this.pallonMassa;
    }
    
    public float getKepinPituus(){
        return this.kepinPituus;
    }
    
    public int getpixelOffsetX(){
        return this.pixelOffsetX;
    }
    
    public int getpixelOffsetY(){
        return this.pixelOffsetY;
    }
    
    public int getpituusXpixel(){
        return this.pituusXpixel;
    }  
    
    public int getpituusYpixel(){
        return this.pituusYpixel;
    }  
    
    public int getpallonHalkaisijaPixel(){
        return this.pallonHalkaisijaPixel;
    }
    
    public int getreianHalkaisijaPixel(){
        return this.reianHalkaisijaPixel;
    }
    
    public float getScale(){
        return this.scale;
    }
    
    /**
    * siirrytään reaalimaailman koordinaateista 
    * piirrettävän pelilaudan koordinaatteihin
    * @param x reaalimaailman x koordinaatti
    * @return pelilaudan x pixeli koordinaatti
    */
    public int lautafloat2PixelX(float x){
        float pixelXfloat;
        pixelXfloat = this.getpixelOffsetX()+
                this.getScale() * (x-this.getMinLautaX());     
        //return pixelXfloat.intValue();
        return 0;
    }
    
    /**
    * siirrytään reaalimaailman koordinaateista 
    * piirrettävän pelilaudan koordinaatteihin
    * @param y reaalimaailman y koordinaatti
    * @return pelilaudan y pixeli koordinaatti
    */
    public int lautafloat2PixelY(float y){
        // siirrytään reaalimaailman koordinaateista 
        // piirrettävän pelilaudan koordinaatteihin
        float pixelYfloat;
        pixelYfloat = this.getpixelOffsetY()+
                this.getScale() * (y-this.getMinLautaY());
        //return pixelYfloat.intValue();
        return 0;
    }    
    
}
