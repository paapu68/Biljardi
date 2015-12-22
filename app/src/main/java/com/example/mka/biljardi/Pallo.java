/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.mka.biljardi;

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
    private double vx;
    private double vy;
    private double ax;
    private double ay;
    private double fx, fy;
    private double varaus;
    private String vari;

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
        this.vx = 0.0;
        this.vy = 0.0;
        this.ax = 0.0;
        this.ay = 0.0;
        this.fx = 0.0;
        this.fy = 0.0;
        this.varaus = 0.0;
        this.vari = "";
    }

    public float getPalloX(){
        return this.x;
    }
    
    public float getPalloY(){
        return this.y;
    }
    
    public double getPalloVX(){
        return this.vx;
    }
    
    public double getPalloVY(){
        return this.vy;
    }
    
    public double getPalloAX(){
        return this.ax;
    }
    
    public double getPalloAY(){
        return this.ay;
    }
    
    public double getPalloFX(){
        return this.fx;
    }
    
    public double getPalloFY(){
        return this.fy;
    }    
    
    public void setPalloX(float x){
        this.x = x;
    }
    
    public void setPalloY(float y){
            this.y= y;
    }
    
    public void setPalloVX(double vx){
        this.vx = vx;
    }
    
    public void setPalloVY(double vy){
        this.vy = vy;
    }
    
    public void setPalloAX(double ax){
        this.ax = ax;
    }
    
    public void setPalloAY(double ay){
        this.ay = ay;
    }
    
    public void nollaaPalloFX(){
        this.fx = 0.0;
    }
    
    public void nollaaPalloFY(){
        this.fy = 0;
    }    
    
    /**
    * lisätään pallon voiman x koordinaattia 
    */    
    public void lisaaPalloFX(double fx){
        this.fx += fx;
    }
    
    /**
    * lisätään pallon voiman y koordinaattia 
    */
    public void lisaaPalloFY(double fy){
        this.fy += fy;
    }    
    
    public String getPalloVari(){
        return this.vari;
    }
    
    public void setPalloVari(String vari){
        this.vari = vari;
    }
    
    public double getPalloVaraus(){
        return this.varaus;
    }
    
    /**
    * asetetaan pallon coulombin varaus (mikrocoulombeissa)
    */
    public void setPalloVaraus(int varaus){        
        this.varaus = varaus * 0.000001;
    }    
    
    @Override
    public String toString() {
        return "PALLO x: "+this.x+",y: "+this.y+"  "+this.varaus+"  "+this.vari;
    }    
}    

