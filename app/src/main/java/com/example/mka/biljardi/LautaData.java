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
    private final double dt;
    final Double minLautaX, minLautaY;
    final Double maxLautaX, maxLautaY; 
    final Double pallonHalkaisija;
    final Double pallonMassa;
    final Double kepinPituus;
    final Double reianHalkaisija;
    final double scale;
    final double alkuX, alkuY, valkoinenX, valkoinenY;
   
    final int pixelOffsetX;
    final int pixelOffsetY;
    final int pallonHalkaisijaPixel, reianHalkaisijaPixel;
    final int pituusXpixel, pituusYpixel;
    
    
    public LautaData(){
        this.dt = 0.0001;  // aika-askel
        this.minLautaX = 0.0;
        this.minLautaY = 0.0;
        this.maxLautaX = 1.4;
        this.maxLautaY = 2.7;
        this.pallonHalkaisija = 0.0517;
        this.pallonMassa = 0.16;
        this.kepinPituus = 6.0 * this.pallonHalkaisija;
        this.reianHalkaisija = this.pallonHalkaisija * 1.6;
        this.scale = 200.0;
        this.pixelOffsetX = 50;
        this.pixelOffsetY = 50; 
        Double ph;
        ph = this.pallonHalkaisija * this.scale;
        this.pallonHalkaisijaPixel = ph.intValue();
        Double rh;
        rh = this.reianHalkaisija * this.scale;
        this.reianHalkaisijaPixel = rh.intValue();
        Double pituusX = (this.maxLautaX-this.minLautaX)*this.scale;
        this.pituusXpixel = pituusX.intValue();
        Double pituusY = (this.maxLautaY-this.minLautaY)*this.scale;
        this.pituusYpixel = pituusY.intValue();
        this.alkuX = this.maxLautaX / 2.0;
        this.alkuY = 1.0/4.0*this.maxLautaY;
        this.valkoinenX = this.alkuX;
        this.valkoinenY = 3.0/4.0*this.maxLautaY;
        
    }

    public double getDT(){
        return this.dt;
    }
    
    public double getMaxLautaX(){
        return this.maxLautaX;
    }
    
    public double getMaxLautaY(){
        return this.maxLautaY;
    }
    
    public double getMinLautaX(){
        return this.minLautaX;
    }
    
    public double getMinLautaY(){
        return this.minLautaY;
    }
    
    public double getPallonHalkaisija(){
        return this.pallonHalkaisija;
    }
    
    public double getPallonMassa(){
        return this.pallonMassa;
    }
    
    public double getKepinPituus(){
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
    
    public double getScale(){
        return this.scale;
    }
    
    /**
    * siirrytään reaalimaailman koordinaateista 
    * piirrettävän pelilaudan koordinaatteihin
    * @param x reaalimaailman x koordinaatti
    * @return pelilaudan x pixeli koordinaatti
    */
    public int lautaDouble2PixelX(double x){
        Double pixelXdouble;
        pixelXdouble = this.getpixelOffsetX()+
                this.getScale() * (x-this.getMinLautaX());     
        return pixelXdouble.intValue();
    }
    
    /**
    * siirrytään reaalimaailman koordinaateista 
    * piirrettävän pelilaudan koordinaatteihin
    * @param y reaalimaailman y koordinaatti
    * @return pelilaudan y pixeli koordinaatti
    */
    public int lautaDouble2PixelY(double y){
        // siirrytään reaalimaailman koordinaateista 
        // piirrettävän pelilaudan koordinaatteihin
        Double pixelYdouble;
        pixelYdouble = this.getpixelOffsetY()+
                this.getScale() * (y-this.getMinLautaY());
        return pixelYdouble.intValue();
    }    
    
}
