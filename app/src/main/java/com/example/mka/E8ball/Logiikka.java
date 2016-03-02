
package com.example.mka.E8ball;

import android.util.Log;

import java.util.ArrayList;

/**
 * Pelin logiikka
 */
public class Logiikka {
    private LautaData lautadata = new LautaData();
    private boolean playing;
    private boolean pallotLiikkuu;
    private boolean saaLyoda;
    private String finish;
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
        this.finish = "enTieda";
    }

    public void reset(){
        this.playing = true;
        this.pallotLiikkuu = false;
        this.saaLyoda = true;
        this.lyontiOhi = false;
        this.finish = "enTieda";
    }

    public void totalReset(ArrayList<Pelaaja> pelaajat, Reiat reiat, Pallot pallot){
        pallot.asetaPallojenAlkupaikat();
        pallot.reset();
        reiat.resetoiReiat();
        reiat.resetoiLyontiEkanaReiassa();
        pelaajat.get(0).reset();
        pelaajat.get(1).reset();
        this.playing = true;
        this.pallotLiikkuu = false;
        this.saaLyoda = true;
        this.lyontiOhi = false;
        this.finish = "enTieda";
    }

    public String getFinish() {return this.finish;    }
    public void setFinish(String finish){ this.finish = finish;}
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

    public boolean tarkastaLopeta(float y, float screeny){
        if (y < screeny/5.0){
            return false;
        }else{
            return true;
        }
    }

    public void tarkastaTilanne(ArrayList<Pelaaja> pelaajat, Reiat reiat, Pallot pallot, Liike liike){
        // asetetaan mikä väri meni ekana reikään
        if (reiat.getPeliEkanaReiassa().equals("enTieda")) {
            reiat.setPeliEkanaReiassa(pallot);
        }
        if (reiat.getLyontiEkanaReiassa().equals("enTieda")) {
            reiat.setLyontiEkanaReiassa(pallot);
        }
        // päivitetään lyönnin aikana pussiin mennet pallot
        reiat.lisaaReikiinMenneet(pallot);
        //poistetaan reikiin menneet siniset ja punaiset pallot pelistä
        reiat.tapaNormiPallot(pallot);
        // Log.i("LoMitaRe", String.valueOf(reiat.getMitaReiissa()));
        // jos musta meni niin lopetaan peli ja
        // määritetään voittaja
        if (reiat.getMitaReiissa().get("musta").equals(1)){
            if (pelaajat.get(0).getTurn()){
                if (pelaajat.get(0).getTries().equals("musta")){
                    pelaajat.get(0).setWin(true);
                } else {
                    pelaajat.get(1).setWin(true);
                }
            } else {
                if (pelaajat.get(1).getTries().equals("musta")){
                    pelaajat.get(1).setWin(true);
                } else {
                    pelaajat.get(0).setWin(true);
                }
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
                // vaihdetaan vuoro.
                pallot.asetaPallojenAlkupaikat();
                pallot.reset();
                reiat.resetoiPeliEkanaReiassa();
                pelaajat.get(0).reset();
                pelaajat.get(1).reset();
            }
            // vaihdetaan vuoro
            this.setPallotLiikkuu(false);
            pelaajat.get(0).setTurn(!pelaajat.get(0).getTurn());
            pelaajat.get(1).setTurn(!pelaajat.get(1).getTurn());
            this.setSaaLyoda(true);
            reiat.resetoiReiat();
            reiat.resetoiLyontiEkanaReiassa();
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
                        Log.i("PunPel0", pelaajat.get(0).getTries());
                    }
                    else {
                        pelaajat.get(0).setTries("sininen");
                        pelaajat.get(1).setTries("punainen");
                        Log.i("PunPel1", pelaajat.get(1).getTries());
                    }
                }
                else if (reiat.getPeliEkanaReiassa().equals("sininen")){
                    if (pelaajat.get(0).getTurn()) {
                        pelaajat.get(0).setTries("sininen");
                        pelaajat.get(1).setTries("punainen");
                        Log.i("SinPel0", pelaajat.get(0).getTries());
                    }
                    else {
                        pelaajat.get(0).setTries("punainen");
                        pelaajat.get(1).setTries("sininen");
                        Log.i("SinPel1", pelaajat.get(1).getTries());
                    }
                }
            }
        }
        // päivitetään pelitilanne ja tapetaan reikiin menneet pallot
        if (pelaajat.get(0).getTries().equals("punainen")){
            pelaajat.get(0).setScore(reiat.getMitaReiissa().get("punainen"));
            pelaajat.get(1).setScore(reiat.getMitaReiissa().get("sininen"));
            //Log.i("LO1-score-sin", String.valueOf(reiat.getMitaReiissa().get("sininen")));
            //Log.i("LO1-score-pun", String.valueOf(reiat.getMitaReiissa().get("punainen")));
        }else if (pelaajat.get(0).getTries().equals("sininen")) {
            pelaajat.get(0).setScore(reiat.getMitaReiissa().get("sininen"));
            pelaajat.get(1).setScore(reiat.getMitaReiissa().get("punainen"));
            //Log.i("LO2-score-sin", String.valueOf(reiat.getMitaReiissa().get("sininen")));
            //Log.i("LO2-score-pun", String.valueOf(reiat.getMitaReiissa().get("punainen")));
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
                Log.i("EI MENNY", "vaihdetaanVuoro");
                Log.i(String.valueOf(reiat.getLyontiEkanaReiassa()), String.valueOf(pelaajat.get(0).getTries()));
                Log.i(String.valueOf(reiat.getLyontiEkanaReiassa()), String.valueOf(pelaajat.get(1).getTries()));
            }
            //jotain meni reikään
            else if (pelaajat.get(0).getTurn()) {
                Log.i("jotain","--meni");
                Log.i(String.valueOf(reiat.getLyontiEkanaReiassa()), String.valueOf(pelaajat.get(0).getTries()));
                if (reiat.getLyontiEkanaReiassa().equals(pelaajat.get(0).getTries())) {
                    // ei vaihdeta vuoroa
                    Log.i("pel0 sai", " ei vaihdeta");
                } else {
                    Log.i("pel0 sai vaaran", " vaihdetaan");
                    pelaajat.get(0).setTurn(!pelaajat.get(0).getTurn());
                    pelaajat.get(1).setTurn(!pelaajat.get(1).getTurn());
                }
            } else  if (pelaajat.get(1).getTurn())  {
                Log.i(String.valueOf(reiat.getLyontiEkanaReiassa()), String.valueOf(pelaajat.get(1).getTries()));
                if (reiat.getLyontiEkanaReiassa().equals(pelaajat.get(1).getTries())) {
                    // ei vpel1 sai", " ei vaihdeta");
                } else {
                    Log.i("pel1 sai vaaran", " vaihdetaan");
                    pelaajat.get(0).setTurn(!pelaajat.get(0).getTurn());
                    pelaajat.get(1).setTurn(!pelaajat.get(1).getTurn());
                }
            }
            // tsekataan alkaako jompikumpi pelaajista yrittää mustaa
            if (pelaajat.get(0).getScore() == 7) {
                pelaajat.get(0).setTries("musta");
            }
            if (pelaajat.get(1).getScore() == 7) {
                pelaajat.get(1).setTries("musta");
            }
            reiat.resetoiLyontiEkanaReiassa();
        }
        // reikien kannalta palloja ei ole reiässä
        reiat.resetoiReiat();
    }
}

