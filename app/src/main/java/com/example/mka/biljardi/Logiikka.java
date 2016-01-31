
package com.example.mka.biljardi;

import android.util.Log;

import com.example.mka.biljardi.LautaData;

import java.util.ArrayList;

/**
 * Pelin logiikka
 */
public class Logiikka {
    private LautaData lautadata = new LautaData();
    private boolean playing;
    private boolean pallotLiikkuu;
    private boolean saaLyoda;
    //private Pelaaja vuorossa;
    //private int lyonti_vuoro;
    //private int lyonti_ekana_reiassa;
    //private int lyonti_punaiset;
    //private int lyonti_siniset;
    private boolean lyontiOhi;

    //private int peli_punaiset;
    //private int peli_siniset;
    

    public Logiikka() {
        this.playing = true;
        this.pallotLiikkuu = false;
        this.saaLyoda = true;
        this.lyontiOhi = false;
    }

    public void reset(){
        this.playing = true;
        this.pallotLiikkuu = false;
        this.saaLyoda = true;
        this.lyontiOhi = false;
    }

    public boolean getPlaying() {return this.playing;    }
    public void setPlaying(boolean playing){ this.playing = playing;}
    public boolean getPallotLiikkuu() {return this.pallotLiikkuu; }
    public void setPallotLiikkuu(boolean pallotLiikkuu){ this.pallotLiikkuu = pallotLiikkuu;}
    public boolean getSaaLyoda() { return this.saaLyoda;    }
    public void setSaaLyoda(boolean saaLyoda){ this.saaLyoda = saaLyoda;}
    public boolean getLyontiOhi() {
        return this.lyontiOhi;
    }
    public void setLyontiOhi(boolean lyontiOhi){ this.lyontiOhi = lyontiOhi;}

    public void tarkastaTilanne(ArrayList<Pelaaja> pelaajat, Reiat reiat, Pallot pallot, Liike liike ){
        // asetetaan mikä väri meni ekana reikään
        reiat.setPeliEkanaReiassa(pallot);
        reiat.setLyontiEkanaReiassa(pallot);
        // päivitetään lyönnin aikana pussiin mennet pallot
        reiat.lisaaReikiinMenneet(pallot);
        //poistetaan reikiin menneet siniset ja punaiset pallot pelistä
        reiat.tapaNormiPallot(pallot);
        // Log.i("LoMitaRe", String.valueOf(reiat.getMitaReiissa()));
        // jos musta meni niin lopetaan peli
        if (reiat.getMitaReiissa().get("musta").equals(1)){
            if (pelaajat.get(0).getTurn()){
                pelaajat.get(1).setWin(true);
            } else {
                pelaajat.get(0).setWin(true);
            }
            this.setPlaying(false);
            this.setSaaLyoda(false);
            return;
        } else if(reiat.getMitaReiissa().get("valkoinen").equals(1)) {
            // valkoinen meni, päivitetään tilanne
            pallot.arvoLyontiPallonPaikka(lautadata.minLautaX, lautadata.minLautaY,
                    lautadata.maxLautaX, lautadata.maxLautaY, lautadata.reianSade);
            if (pelaajat.get(0).getTries().equals("punainen")) {
                pelaajat.get(0).setScore(reiat.getMitaReiissa().get("punainen"));
                pelaajat.get(1).setScore(reiat.getMitaReiissa().get("sininen"));

            } else if (pelaajat.get(0).getTries().equals("sininen")) {
                pelaajat.get(0).setScore(reiat.getMitaReiissa().get("sininen"));
                pelaajat.get(1).setScore(reiat.getMitaReiissa().get("punainen"));
            } else {
                // koska kukaan ei vielä yritä mitään niin valkoinen meni ekana pussiin
                // vaihdetaan vuoro ja aloitetaan alusta. Pallojen alkupaikat asetetaan pelin alkuun.
                pallot.asetaPallojenAlkupaikat();
                pallot.reset();
                reiat.resetoiReiat();
            }
            // vaihdetaan vuoro
            this.setPallotLiikkuu(false);
            pelaajat.get(0).setTurn(!pelaajat.get(0).getTurn());
            pelaajat.get(1).setTurn(!pelaajat.get(1).getTurn());
            this.setSaaLyoda(true);
            reiat.resetoiReiat();
            return;
        } else {
            // Jos ensimmäinen värillinen pallo menee pussiin niin se
            // määrää mitä väriä pelaajat alkavat yrittää
            if (pelaajat.get(0).getTries().equals("enTieda")) {
                // värillinen meni
                if (reiat.getPeliEkanaReiassa().equals("punainen")) {
                    if (pelaajat.get(0).getTurn()) {
                        pelaajat.get(0).setTries("punainen");
                        pelaajat.get(1).setTries("sininen");
                        Log.i("variMAAR1", pelaajat.get(0).getTries());
                    }
                    else {
                        pelaajat.get(0).setTries("sininen");
                        pelaajat.get(1).setTries("punainen");
                    }
                }
                else if (reiat.getPeliEkanaReiassa().equals("sininen")){
                    if (pelaajat.get(0).getTurn()) {
                        pelaajat.get(0).setTries("sininen");
                        pelaajat.get(1).setTries("punainen");
                        Log.i("variMAAR1", pelaajat.get(0).getTries());
                    }
                    else {
                        pelaajat.get(0).setTries("punainen");
                        pelaajat.get(1).setTries("sininen");
                    }
                }
            }
        }
        // päivitetään pelitilanne ja tapetaan reikiin menneet pallot
        if (pelaajat.get(0).getTries().equals("punainen")){
            pelaajat.get(0).setScore(reiat.getMitaReiissa().get("punainen"));
            pelaajat.get(1).setScore(reiat.getMitaReiissa().get("sininen"));
            Log.i("LO1-score-sin", String.valueOf(reiat.getMitaReiissa().get("sininen")));
            Log.i("LO1-score-pun", String.valueOf(reiat.getMitaReiissa().get("punainen")));
        }else if (pelaajat.get(0).getTries().equals("sininen")) {
            pelaajat.get(0).setScore(reiat.getMitaReiissa().get("sininen"));
            pelaajat.get(1).setScore(reiat.getMitaReiissa().get("punainen"));
            Log.i("LO2-score-sin", String.valueOf(reiat.getMitaReiissa().get("sininen")));
            Log.i("LO2-score-pun", String.valueOf(reiat.getMitaReiissa().get("punainen")));
        }

        // tarkastetaan liikkuuko pallot
        this.setPallotLiikkuu(liike.getPallotLiikkuu(pallot));

        // Jos pallot ei liiku niin vaihdetaan mahdollisesti lyöntivuoroa
        if (!this.getPallotLiikkuu()){
            this.setSaaLyoda(true);
            // Mitään ei mennyt reikään
            if (reiat.getLyontiEkanaReiassa().equals("enTieda")){
                pelaajat.get(0).setTurn(!pelaajat.get(0).getTurn());
                pelaajat.get(1).setTurn(!pelaajat.get(1).getTurn());
            }
            //jotain meni reikään
            else if (pelaajat.get(0).getTurn()) {
                if (reiat.getLyontiEkanaReiassa().equals(pelaajat.get(0).getTries())) {
                    // ei vaihdeta vuoroa
                } else {
                    pelaajat.get(0).setTurn(!pelaajat.get(0).getTurn());
                    pelaajat.get(1).setTurn(!pelaajat.get(1).getTurn());
                }
            } else {
                if (reiat.getLyontiEkanaReiassa().equals(pelaajat.get(1).getTries())) {
                    // ei vaihdeta vuoroa
                } else {
                    pelaajat.get(0).setTurn(!pelaajat.get(0).getTurn());
                    pelaajat.get(1).setTurn(!pelaajat.get(1).getTurn());
                }
            }
        }
        // reikien kannalta palloja ei ole reiässä
        reiat.resetoiReiat();
    }
}

