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
    private ArrayList<Pallo> pallotArr;
    private LautaData lautadata = new LautaData();
    private int perusvaraus;
    /**
     * Biljardipallojen jono
     */
    public Pallot() {
        this.pallotArr = new ArrayList<Pallo>();
        this.asetaPallojenAlkupaikat();
        this.asetaPallojenVarit();
        this.asetaPallojenPerusVaraus(1);
        this.asetaPallojenVaraukset();
        this.vaihdaPallojenPaikat();
    }

    // pallot resetoidaan alkutilaan
    public void reset() {
        this.asetaPallojenAlkupaikat();
        this.asetaPallojenVarit();
        this.asetaPallojenPerusVaraus(1);
        this.asetaPallojenVaraukset();
        this.vaihdaPallojenPaikat();
    }

    public ArrayList<Pallo> getPallotArray() {
        return this.pallotArr;
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
        this.pallotArr.add(pallo);
    }

    /**
     * poista indexillä osoitettu pallo pallojonosta
     */
    public void poistaPallo(int killIndex) {
        this.pallotArr.remove(killIndex);
    }

    /**
     * nollataan pallojonon pallojen x ja y nopeudet
     */
    public void nollaaNopeudet() {
        for (Pallo pallo : this.pallotArr) {
            pallo.setPalloVX(0.0f);
            pallo.setPalloVY(0.0f);
        }
    }

    /**
     * nollataan pallojonon pallojen x ja y kiihtyvyydet
     */
    public void nollaaKiihtyvyydet() {
        for (Pallo pallo : this.pallotArr) {
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
        for (Pallo pallo : this.pallotArr) {
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
        for (Pallo pallo : this.pallotArr) {
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
        lyontiPallo = this.pallotArr.get(0);
        float d, newx, newy;
        float minDist = 0.0f;
        newx = 0.0f;
        newy = 0.0f;

        while (minDist < Dist){
            newx = (float) (minX + (Math.random() * (maxX - minX)));
            newy = (float) (minY + (Math.random() * (maxY - minY)));

            minDist = 100.0f;
            for (Pallo pallo : this.pallotArr.subList(1, this.pallotArr.size())) {
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
        this.pallotArr.add(pallo);
        // musta pallo toiseksi
        pallo = new Pallo(lautadata.alkuX,lautadata.alkuY-2.0f*lautadata.pallonSade);
        this.pallotArr.add(pallo);
        // kärkipallo
        pallo = new Pallo(lautadata.alkuX,lautadata.alkuY);
        this.pallotArr.add(pallo);

        // 2 palloa 2. rivissä
        y = lautadata.alkuY-lautadata.pallonSade;
        x = lautadata.alkuX-1f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        x=x+2f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        // 2palloa 3. riviin (musta on jo laitettua)
        y = y - lautadata.pallonSade;
        x = lautadata.alkuX-2.0f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        // tässä skipataan musta
        x = x+4.0f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        // 4 palloa 4. riviin
        y = y - lautadata.pallonSade;
        x = lautadata.alkuX-3.0f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        x = x + 2f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        x = x + 2f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        x = x + 2f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        // 5palloa 5. riviin
        y = y - lautadata.pallonSade;
        x = lautadata.alkuX-4.0f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        x = x + 2f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        x = x + 2f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        x = x + 2f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
        x = x + 2f*lautadata.pallonSade;
        pallo = new Pallo(x,y);
        this.pallotArr.add(pallo);
    }

    /**
     * vaihdetaan pallojen varit (paitsi ei musta ja valkoinen)
     */
    public void vaihdaPallojenVarit(){
        char iv,kv;
        Pallo pallo;
        int min = 2;
        int max = 15;
        int k;
        for (int j = 1; j < 100; j = j + 1){
            for (int i = 2; i <= 15 ; i = i + 1) {
                k = min + (int)(Math.random() * ((max - min) + 1));
                kv = this.pallotArr.get(k).getPalloVari();
                iv = this.pallotArr.get(i).getPalloVari();
                this.pallotArr.get(i).setPalloVari(kv);
                this.pallotArr.get(k).setPalloVari(iv);
            }
        }
    }

    /**
     * vaihdetaan pallojen varit (paitsi ei musta ja valkoinen)
     */
    public void vaihdaPallojenPaikat(){
        float ix, iy, kx, ky;
        Pallo pallo;
        int min = 2;
        int max = 15;
        int k;
        for (int j = 1; j < 100; j = j + 1){
            for (int i = 2; i <= 15 ; i = i + 1) {
                k = min + (int)(Math.random() * ((max - min) + 1));
                kx = this.pallotArr.get(k).getPalloX();
                ky = this.pallotArr.get(k).getPalloY();
                ix = this.pallotArr.get(i).getPalloX();
                iy = this.pallotArr.get(i).getPalloY();
                this.pallotArr.get(i).setPalloX(kx);
                this.pallotArr.get(i).setPalloY(ky);
                this.pallotArr.get(k).setPalloX(ix);
                this.pallotArr.get(k).setPalloY(iy);
            }
        }
    }

    /**
     * Asetetaan pallojen varit.
     * vain 0. ja 1. pallo, muut väri varauksen perusteella
     */

    public void asetaPallojenVarit(){
        // valkoinen pallo ensin
        this.pallotArr.get(0).setPalloVari('v');
        // musta pallo toiseksi
        this.pallotArr.get(1).setPalloVari('m');
        // negatiiviset (punaiset pallot, 2-8)
        for (int i = 2; i <= 8 ; i = i + 1) {
            this.pallotArr.get(i).setPalloVari('p');
        }
        // positiiviset (siniset pallot, 9-15)
        for (int i = 9; i <= 15 ; i = i + 1) {
            this.pallotArr.get(i).setPalloVari('s');
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
            this.pallotArr.get(i).setPalloVaraus(-perusvaraus);
        }
        // positiiviset (siniset pallot, 9-15)
        for (int i = 9; i <= 15 ; i = i + 1) {
            this.pallotArr.get(i).setPalloVaraus(perusvaraus);
        }
    }

    /**
     * palautetaan vain varaukset
     */
    @Override
    public String toString() {
        String message;
        message = "PALLOT (varit)";
        for ( Pallo pallo : this.pallotArr ) {
            message = message + " " + pallo.getPalloVaraus();
        }
        return message;
    }
}
