/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.mka.E8ball;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;

/**
 * Biljardipöydän reikien koordinaatit kahdessa ArrayList:ssä
 * Toisessa x koordinaatit toisessa y.
 * Lisäksi metodeja joissa tutkitaan onko pallo mennyt reikään.
 */
public final class Reiat {
    /**
     * Alustetaan 6 reikää paikoilleen
     * @param reiatX reikien x koordinaatit reaaliavaruudessa
     * @param reiatY reikien y koordinaatit reaaliavaruudessa
     * HashMap 'mitaReiissa' kertoo montako palloa kutakin väriä
     * on mennyt reikiin tämän lyönnin aikana.
     */
    private ArrayList<Float> reiatX = new ArrayList<Float>();
    private ArrayList<Float> reiatY = new ArrayList<Float>();
    private LautaData lautadata = new LautaData();
    private HashMap<String, Integer> mitaReiissa;
    private String peliEkanaReiassa;
    private String lyontiEkanaReiassa;
    boolean jotainReiissa;
    
    public Reiat(){
        this.mitaReiissa = new HashMap<String, Integer>();
        this.setReikaXY(0.0f, 0.0f);
        this.setReikaXY(lautadata.maxLautaX, 0.0f);
        this.setReikaXY(0.0f, lautadata.maxLautaY/2.0f);
        this.setReikaXY(lautadata.maxLautaX, lautadata.maxLautaY/2.0f);
        this.setReikaXY(0.0f, lautadata.maxLautaY);
        this.setReikaXY(lautadata.maxLautaX, lautadata.maxLautaY);
        this.mitaReiissa.put("musta", 0);
        this.mitaReiissa.put("valkoinen", 0);
        this.mitaReiissa.put("punainen", 0);
        this.mitaReiissa.put("sininen", 0);
        this.peliEkanaReiassa = "enTieda";
        this.lyontiEkanaReiassa = "enTieda";
    }
    
    public void resetoiReiat(){
        this.mitaReiissa.put("musta", 0);
        this.mitaReiissa.put("valkoinen", 0);
        this.mitaReiissa.put("punainen", 0);
        this.mitaReiissa.put("sininen", 0);
    }

    public void resetoiLyontiEkanaReiassa(){
        this.lyontiEkanaReiassa = "enTieda";
    }

    public void resetoiPeliEkanaReiassa(){
        this.peliEkanaReiassa = "enTieda";
    }


    public void setReikaXY(float x, float y){
        this.reiatX.add(x);
        this.reiatY.add(y);
    }
    
    public ArrayList<Float> getReiatX(){
        return this.reiatX;
    }
    
    public ArrayList<Float> getReiatY(){
        return this.reiatY;
    }
    
    /** 
     * lisätään HashMapiin jos palloja on mennyt reikiin.
     */
    public void lisaaReikiinMenneet(Pallot pallot){
        for (Pallo p1 : pallot.getPallotArray()){
             if (!this.tarkastaPallo(p1)) {
                 // pallo on reiassa            
                 String vari = p1.getPalloVari();
                 int lkm = this.mitaReiissa.get(vari);
                 lkm = lkm + 1;
                 this.mitaReiissa.put(vari, lkm);
                 Log.i("reika:REIASSA", String.valueOf(this.getMitaReiissa()));

             }
        }
    }
    
    /** 
     * otetaan tämänhetkinen tilanne mitä rei'issä on.vuoro
     * @return hashmap joka kertoo montako minkäkin väristä 
     * palloa rei'issä tämän lyönnin aikana.
     */
    
    public HashMap<String, Integer> getMitaReiissa(){
        return this.mitaReiissa;
    }
    
    /** Minkä värinen pallo meni ensimmäisenä reikään?
     * Vastaus on "enTieda" jos mitään ei mennyt reikään.
     */
    public void setLyontiEkanaReiassa(Pallot pallot){
        if (this.getLyontiEkanaReiassa().equals("enTieda")){
          for (Pallo p1 : pallot.getPallotArray()){
             if (!this.tarkastaPallo(p1)) {
                 // pallo on reiassa
                 this.lyontiEkanaReiassa = p1.getPalloVari();
                 Log.i("reika1:REIASSA", String.valueOf(this.lyontiEkanaReiassa));
             }
          }
        }
    }

    public void setPeliEkanaReiassa(Pallot pallot){
        if (this.getPeliEkanaReiassa().equals("enTieda")){
            for (Pallo p1 : pallot.getPallotArray()){
                if (!this.tarkastaPallo(p1)) {
                    // pallo on reiassa
                    this.peliEkanaReiassa = p1.getPalloVari();
                    Log.i("reika:peli EKA REIASSA", String.valueOf(this.peliEkanaReiassa));
                }
            }
        }
    }

    public String getLyontiEkanaReiassa(){
        return this.lyontiEkanaReiassa;
    }
    public String getPeliEkanaReiassa(){
        return this.peliEkanaReiassa;
    }

    /**
     * Jos normipallo (eli ei valkoinen lyöntipallo, eikä musta)
     * on mennyt reikään niin se tapetaan pois
     * @param pallot biljardipallot 
     */
    public void tapaNormiPallot(Pallot pallot) {
        ArrayList<Pallo> p1 = pallot.getPallotArray();
        ArrayList<Integer> tapa = new ArrayList<Integer>();
        Boolean found;
        double x, y, d;
        for(int i = p1.size()-1; i >= 2 ; i = i - 1) {
            if (!this.tarkastaPallo(p1.get(i))){
                tapa.add(i);
            }
        }   
        for (int t1: tapa){
            pallot.poistaPallo(t1);
        }
    }
    
    /**
     * Tutkitaan onko pallo mennyt reikään.
     * @param pallo turkittava pallo
     * @return jos pallo on reiässä palautetaan false, muuten true
     */
    public Boolean tarkastaPallo(Pallo pallo) {
        double x, y, d;
        boolean jatka = true;
        
        x = pallo.getPalloX();
        y = pallo.getPalloY();
        for(int j = 0; j <= this.reiatX.size()-1 ; j = j + 1) {
            d = Math.sqrt((x-this.reiatX.get(j))*(x-this.reiatX.get(j))
            +(y-this.reiatY.get(j))*(y-this.reiatY.get(j)));
            if (d < lautadata.reianSade) {
                jatka = false;
            }
        }   
        return jatka;
    }    
}
