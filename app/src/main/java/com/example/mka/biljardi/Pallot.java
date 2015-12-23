/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.mka.biljardi;
import com.example.mka.biljardi.LautaData;

import java.util.ArrayList;    // imports ArrayList
/**
 * biljardipallojen jono ja jonoon liittyviä toimintoja.
 * Oletus: ensimmäisenä on lyöntipallo ja toisena musta pallo. 
 */
public class Pallot {    
    private ArrayList<Pallo> pallot;
    private LautaData lautadata = new LautaData();
    private int perusvaraus;
    /** 
     * Biljardipallojen jono 
     */    
    public Pallot() {
        this.pallot = new ArrayList<Pallo>();
        this.asetaPallojenAlkupaikat();
        this.asetaPallojenVarit();
        this.asetaPallojenPerusVaraus(1);
        this.asetaPallojenVaraukset();
        this.vaihdaPallojenJarjestys();
    }

    // pallot resetoidaan alkutilaan
    public void reset() {
        this.asetaPallojenAlkupaikat();
        this.asetaPallojenVarit();
        this.asetaPallojenPerusVaraus(1);
        this.asetaPallojenVaraukset();
        this.vaihdaPallojenJarjestys();

    }

    public ArrayList<Pallo> getPallotArray() {
        return this.pallot;
    }

    public void update(long fps){
        for (Pallo pallo : this.pallot) {
            pallo.setPalloVX(0.0f);
            pallo.setPalloVY(0.0f);
        }
    }

    
    /** Annetaan pallojonon ensimmäinen pallo joka on valkoinen lyöntipallo
    * 
    */    
    public Pallo getLyontiPallo() {
        return this.getPallotArray().get(0);
    }
    
    /** Annetaan pallojonon toinen pallo joka on musta pallo
    * 
    */    
    public Pallo getMustaPallo() {
        return this.getPallotArray().get(1);
    }
    
    /**
    * lisää pallo pallojonoon
    */
    public void lisaaPallo(Pallo pallo) {
        this.pallot.add(pallo);
    }
    
   /**
   * poista indexillä osoitettu pallo pallojonosta
   */ 
   public void poistaPallo(int killIndex) {
        this.pallot.remove(killIndex);
    }
    
   /**
   * nollataan pallojonon pallojen x ja y nopeudet
   */   
    public void nollaaNopeudet() {
        for (Pallo pallo : this.pallot) {
            pallo.setPalloVX(0.0f);
            pallo.setPalloVY(0.0f);
        }
    }
   
   /**
   * nollataan pallojonon pallojen x ja y kiihtyvyydet
   */   
    public void nollaaKiihtyvyydet() {
        for (Pallo pallo : this.pallot) {
            pallo.setPalloAX(0.0f);
            pallo.setPalloAY(0.0f);
        }
    }
    
   /**
   * palautetaan 
   * suurin nopeus
   * 
   */   
    public float suurinNopeus() {
        float v=0.0f;
        float maxv=0.0f;
        for (Pallo pallo : this.pallot) {
            v = (float) Math.sqrt(pallo.getPalloVX()*pallo.getPalloVX()
                    +pallo.getPalloVY()*pallo.getPalloVY());
            if (maxv < v){
                maxv = v;
            }
        }
        return maxv;
    }   

    public float suurinKiihtyvyys() {
        float a=0.0f;
        float maxa=0.0f;
        for (Pallo pallo : this.pallot) {
            a = (float) Math.sqrt(pallo.getPalloAX()*pallo.getPalloAX()
                    +pallo.getPalloAY()*pallo.getPalloAY());
            if (maxa < a){
                maxa = a;
            }
        }
        return maxa;
    }       
    
    /**
    * Arvotaan lyontipallolle uusi paikka
    * siten että se ei mene toisen pallon päälle.
    */
    public void arvoLyontiPallonPaikka(
            float minX, float minY, float maxX,  float maxY,
            float Dist) {
        Pallo lyontiPallo;
        lyontiPallo = this.pallot.get(0);
        float d, newx, newy;
        float minDist = 0.0f;
        newx = 0.0f;
        newy = 0.0f;
        
        while (minDist < Dist){
            newx = (float) (minX + (Math.random() * (maxX - minX)));
            newy = (float) (minY + (Math.random() * (maxY - minY)));
            
            minDist = 100.0f;
            for (Pallo pallo : this.pallot.subList(1, this.pallot.size())) {
                d = (float) Math.sqrt((newx - pallo.getPalloX())
                        *(newx - pallo.getPalloX())+
                        (newy - pallo.getPalloY())
                        *(newy - pallo.getPalloY()));
                if (d < minDist) {
                    minDist = d;
                }
            }

        }
        lyontiPallo.setPalloX(newx);
        lyontiPallo.setPalloY(newy);
    }
        
    /**
    * Asetetaan pallojen alkupaikat.
    *
    */
    public void asetaPallojenAlkupaikat(){
        float x, y;
        Pallo pallo;
        // valkoinen pallo ensin
        pallo = new Pallo(lautadata.valkoinenX, lautadata.valkoinenY);
        this.pallot.add(pallo);
        // musta pallo toiseksi
        pallo = new Pallo(lautadata.alkuX,lautadata.alkuY-2.0f*lautadata.pallonHalkaisija);
        this.pallot.add(pallo);
        // kärkipallo
        pallo = new Pallo(lautadata.alkuX,lautadata.alkuY);
        this.pallot.add(pallo); 
        
        y = lautadata.alkuY;
        for (int row = 1; row <= 4 ; row = row + 1) {
            x = lautadata.alkuX 
                    - (row+1)*lautadata.pallonHalkaisija;
            y -= lautadata.pallonHalkaisija;
            for (int column = 0; column <= row ; column = column + 1) {
                x += lautadata.pallonHalkaisija*1.0f;
                // ei laiteta mustaa palloa toiseen kertaan
                if (row == 2 && column ==1){
                   
                }
                else{
                     pallo = new Pallo(x,y);
                    this.pallot.add(pallo); 
                }
            }     
        }
    }
    
    /**
     * vaihdetaan pallojen paikkoja
     */
    public void vaihdaPallojenJarjestys(){
        float xi, yi, xk, yk;
        Pallo pallo;
        int min = 2; 
        int max = this.getPallotArray().size()-1;
        int k;
        for (int j = 1; j < 100; j = j + 1){
          for (int i = min; i <= max; i = i + 1) {
             k = min + (int)(Math.random() * ((max - min) + 1));
             xk = this.getPallotArray().get(k).getPalloX();
             yk = this.getPallotArray().get(k).getPalloY();
             xi = this.getPallotArray().get(i).getPalloX();
             yi = this.getPallotArray().get(i).getPalloY();
             this.getPallotArray().get(i).setPalloX(xk);
             this.getPallotArray().get(i).setPalloY(yk);
             this.getPallotArray().get(k).setPalloX(xi);
             this.getPallotArray().get(k).setPalloY(yi);             
                             
            }     
        }
    }
    
    /**
    * Asetetaan pallojen varit.
    * vain 0. ja 1. pallo, muut väri varauksen perusteella
    */
    
    public void asetaPallojenVarit(){       
        // valkoinen pallo ensin
        this.pallot.get(0).setPalloVari("valkoinen");
        // musta pallo toiseksi
        this.pallot.get(1).setPalloVari("musta");
                // negatiiviset (punaiset pallot, 2-8)
        for (int i = 2; i <= 8 ; i = i + 1) {
            this.pallot.get(i).setPalloVari("punainen");
        }
        // positiiviset (siniset pallot, 9-15)
        for (int i = 9; i <= 15 ; i = i + 1) {
            this.pallot.get(i).setPalloVari("sininen");
        }    
    }
    
    /**
     * Asetetaan pallojen perusvaraus (mikro coulumbeja)
     * punaiset saavat -perusvaraus ja siniset +perusvaraus
     * @param varaus perusvaraus mikrocoulombeissa
     */
     public void asetaPallojenPerusVaraus(int varaus){     
         this.perusvaraus = varaus;
     }
   
     
     public int getPallojenPerusVaraus(){
         return this.perusvaraus;
     }
   
    /**
    * Asetetaan pallojen varaukset.
    * pallot 2-8 negatiivisia
    * pallot 9-15 positiivisia
    */
    public void asetaPallojenVaraukset(){          
        // negatiiviset (punaiset pallot, 2-8)
        for (int i = 2; i <= 8 ; i = i + 1) {
            this.pallot.get(i).setPalloVaraus(-perusvaraus);
        }
        // positiiviset (siniset pallot, 9-15)
        for (int i = 9; i <= 15 ; i = i + 1) {
            this.pallot.get(i).setPalloVaraus(perusvaraus);
        }    
    }
    
    /**
    * palautetaan vain varaukset
    */    
    @Override
    public String toString() {
        String message;
        message = "PALLOT (varit)";
        for ( Pallo pallo : this.pallot ) {
            message = message + " " + pallo.getPalloVaraus();
        }
        return message;
    }        
}
