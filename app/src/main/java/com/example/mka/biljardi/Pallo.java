/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.mka.biljardi;

import android.graphics.Color;
import android.text.style.CharacterStyle;

/**
 *  Sisältää pallon 2d paikan, 2d nopeuden, 2d kiihtyvyyden
 *  massan varauksen ja värin
 *  sekä metodit niiden asettamiseen ja antamiseen ulos
 *  Lyöntipallon väri on 'valkoinen' mustan pallon väri on 'musta'
 *  yksiköt m, , m/s, m/s², kg, mikro Coulomb
 * 
 */
public class Pallo {
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float ax;
    private float ay;
    private float fx, fy;
    private float varaus;
    private Character vari;
    private int color;

   /**
    *  @param x pallon x koordinaatti
    *  @param y pallon y koordinaatti
    *  vx pallon x suunnan vauhti
    *  vy pallon y suunnan vauhti
    *  ax pallon x suunnan kiihtyvyys
    *  ay pallon y suunnan kiihtyvyys
    * fx, fy palloon kohdituvan voiman x, y komponentti
    *  varaus pallon varaus mikrocoulombeina
    *  vari pallon väri
    */
    public Pallo(float x, float y) {
        this.x = x;
        this.y = y;
        this.vx = 0.0f;
        this.vy = 0.0f;
        this.ax = 0.0f;
        this.ay = 0.0f;
        this.fx = 0.0f;
        this.fy = 0.0f;
        this.varaus = 0.0f;
        this.vari = ' ';
        this.color = Color.WHITE;
    }

    public float getPalloX(){
        return this.x;
    }
    
    public float getPalloY(){
        return this.y;
    }
    
    public float getPalloVX(){
        return this.vx;
    }
    
    public float getPalloVY(){
        return this.vy;
    }
    
    public float getPalloAX(){
        return this.ax;
    }
    
    public float getPalloAY(){
        return this.ay;
    }
    
    public float getPalloFX(){
        return this.fx;
    }
    
    public float getPalloFY(){
        return this.fy;
    }    
    
    public void setPalloX(float x){
        this.x = x;
    }
    
    public void setPalloY(float y){
            this.y= y;
    }
    
    public void setPalloVX(float vx){
        this.vx = vx;
    }
    
    public void setPalloVY(float vy){
        this.vy = vy;
    }
    
    public void setPalloAX(float ax){
        this.ax = ax;
    }
    
    public void setPalloAY(float ay){
        this.ay = ay;
    }
    
    public void nollaaPalloFX(){
        this.fx = 0.0f;
    }
    
    public void nollaaPalloFY(){
        this.fy = 0.0f;
    }    
    
    /**
    * lisätään pallon voiman x koordinaattia 
    */    
    public void lisaaPalloFX(float fx){
        this.fx += fx;
    }
    
    /**
    * lisätään pallon voiman y koordinaattia 
    */
    public void lisaaPalloFY(float fy){
        this.fy += fy;
    }    
    
    public Character getPalloVari(){
        return this.vari;
    }

    public void setColor(int color){
        this.color = color;
    }

    public int getColor() {return this.color;}
    
    public void setPalloVari(Character vari){
        this.vari = vari;
    }
    
    public float getPalloVaraus(){
        return this.varaus;
    }
    
    /**
    * asetetaan pallon coulombin varaus (mikrocoulombeissa)
    */
    public void setPalloVaraus(int varaus){        
        this.varaus = varaus * 0.000001f;
    }    
    
    @Override
    public String toString() {
        return "PALLO x: "+this.x+",y: "+this.y+"  "+this.varaus+"  "+this.vari;
    }    
}    

