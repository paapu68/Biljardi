/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.mka.biljardi;

import android.util.Log;

import java.util.ArrayList;

/**
 * Pallojen liikutusrutiinit
 *
 * Pallojen keskinäisistä voimat
 * lisätään palloihin. 
 
 * 1) pallojen keskinäisistä coulombin voimista
 * (lisaaCoulombKiihtyvyydetBiljardiPallot)
 * 2) pallojen välisestä hard-core repulsiosta, joka estää pallojen
 * menemisen päällekkäin
 * 3) kitkasta joka pysäyttää liikkeen
 *
 */

public class Liike {
    LautaData lautadata = new LautaData();
    private float maxSiirtyma, maxNopeus, maxKiihtyvyys;
    private boolean stopMotion;

    public Liike() {
        this.maxSiirtyma = 10.0f;
        this.maxNopeus = 10.0f;
        this.maxKiihtyvyys = 10.0f;
        this.stopMotion = false;
    }


    // liikkuvatko pallot enää?
    public boolean getPallotLiikkuu() {
        Log.i("maxSiirtyma", String.valueOf(maxSiirtyma));
        Log.i("maxNopeus", String.valueOf(maxNopeus));
        Log.i("maxKiihtyvyys", String.valueOf(maxKiihtyvyys));


        if ((this.maxSiirtyma < lautadata.maxSiirtyma)
                & (this.maxNopeus < lautadata.maxNopeus)){
            return false;
        //if ((this.maxSiirtyma < lautadata.maxSiirtyma)
        //        & (this.maxNopeus < lautadata.maxNopeus)
        //        & (this.maxKiihtyvyys < lautadata.maxKiihtyvyys)){
        //    return false;
        //if ((this.maxSiirtyma < lautadata.maxSiirtyma)
        //        & (this.maxKiihtyvyys < lautadata.maxKiihtyvyys)) {
        //    return false;
        } else {
            return true;
        }
    }

    // tehdään aika-askel ja päivitetään x ja y ja vx ja vy
    public void update(float dt, Pallot pallot) {

        ArrayList<Pallo> p1 = pallot.getPallotArray();
        // uudet paikat
        this.maxSiirtyma = 0.0f;
        for (Pallo pallo1 : p1) {
            float xold = pallo1.getPalloX();
            float yold = pallo1.getPalloY();

            float xnew = pallo1.getPalloX() + pallo1.getPalloVX() * dt
                    + 0.5f * pallo1.getPalloAX() * dt * dt;
            float ynew = pallo1.getPalloY() + pallo1.getPalloVY() * dt
                    + 0.5f * pallo1.getPalloAY() * dt * dt;
            pallo1.setPalloX(xnew);
            pallo1.setPalloY(ynew);
            float siirtyma = (float) Math.sqrt((xold - xnew) * (xold - xnew)
                    + (yold - ynew) * (yold - ynew));
            if (siirtyma > this.maxSiirtyma) {
                this.maxSiirtyma = siirtyma;
            }
        }
        this.maxNopeus = pallot.suurinNopeus();
        this.maxKiihtyvyys = pallot.suurinKiihtyvyys();

        // voimat uusissa paikoissa
        this.nollaaVoimat(pallot);
        this.lisaaCoulombVoimatBiljardiPallot(pallot);
        this.lisaaHardCoreVoimat(pallot);
        //this.oldLisaaHardCoreVoimat(pallot);

        this.lisaaKitka(pallot);

        // uudet nopeudet
        for (Pallo pallo1 : p1) {
            float vxnew = pallo1.getPalloVX()
                    + 0.5f * (pallo1.getPalloFX() / lautadata.getPallonMassa()
                    + pallo1.getPalloAX()) * dt;
            float vynew = pallo1.getPalloVY()
                    + 0.5f * (pallo1.getPalloFY() / lautadata.getPallonMassa()
                    + pallo1.getPalloAY()) * dt;
            pallo1.setPalloVX(vxnew);
            pallo1.setPalloVY(vynew);
        }
        // uudet kiihtyvyydet
        for (Pallo pallo1 : p1) {
            pallo1.setPalloAX(pallo1.getPalloFX() / lautadata.getPallonMassa());
            pallo1.setPalloAY(pallo1.getPalloFY() / lautadata.getPallonMassa());
        }
    }


    /**
     * Lisätään pallojen voimiin
     * pallojen keskinäisistä Coulombin
     * voimista aiheutuvat kiihtyvyydet
     * fx = coulombsConstant * q1 * q2 * dx / (r²)
     * fy = coulombsConstant * q1 * q2 * dy / (r²)
     * yksiköt coulombi, metri, kilogramma
     *
     * @param pallot Pallot jotka vuorovaikuttavat keskenään.
     */
    public void lisaaCoulombVoimatBiljardiPallot(Pallot pallot) {
        float dx, dy, d2, d;
        final float coulombsConstant = 10000f;

        ArrayList<Pallo> p1 = pallot.getPallotArray();
        for (Pallo pallo1 : p1) {
            for (Pallo pallo2 : p1) {
                dx = pallo1.getPalloX() - pallo2.getPalloX();
                dy = pallo1.getPalloY() - pallo2.getPalloY();
                d2 = (float) dx * dx + dy * dy;
                d = (float) Math.sqrt(d2);
                float varaus1 = pallo1.getPalloVaraus();
                float varaus2 = pallo2.getPalloVaraus();
                if (d > lautadata.getPallonSade()*2f) {
                    pallo1.lisaaPalloFX(coulombsConstant *
                            varaus1 * varaus2 * dx / (d*d2));
                    pallo1.lisaaPalloFY(coulombsConstant *
                            varaus1 * varaus2 * dy / (d*d2));
                }
            }
        }
    }

    // liikemäärän säilymisen avulla tehty törmäys
    public void lisaaHardCoreVoimat(Pallot pallot) {
        // Pallojen törmäys liikemäärän säilymisen avulla
        // https://en.wikipedia.org/wiki/Elastic_collision#Two-dimensional_collision_with_two_moving_objects
        ArrayList<Pallo> p1 = pallot.getPallotArray();
        p1 = pallot.getPallotArray();
        int imax = pallot.getPallotArray().size();
        for (int i = 0; i < imax; i++){
            for (int j = i+1; j < imax; j++) {
                Pallo pallo1, pallo2;
                pallo1 = p1.get(i);
                pallo2 = p1.get(j);
                // ovatko samaan suuntaan? (nopeuksien pistetulo > 0)
                float vdot = pallo1.getPalloVX() * pallo2.getPalloVX() + pallo1.getPalloVY() * pallo2.getPalloVY();
                float dx = pallo1.getPalloX() - pallo2.getPalloX();
                float dy = pallo1.getPalloY() - pallo2.getPalloY();
                float d2 = (float) dx * dx + dy * dy;
                float d = (float) Math.sqrt(d2);
                if (d < lautadata.getPallonSade()*2f) {
                    float dvx = pallo1.getPalloVX() - pallo2.getPalloVX();
                    float dvy = pallo1.getPalloVY() - pallo2.getPalloVY();
                    float vdotxd2 = (dvx * dx + dvy * dy) / d2;
                    float v1x = pallo1.getPalloVX() - dx * vdotxd2;
                    float v1y = pallo1.getPalloVY() - dy * vdotxd2;
                    float v2x = pallo2.getPalloVX() + dx * vdotxd2;
                    float v2y = pallo2.getPalloVY() + dy * vdotxd2;
                    pallo1.setPalloVX(v1x);
                    pallo1.setPalloVY(v1y);
                    pallo2.setPalloVX(v2x);
                    pallo2.setPalloVY(v2y);
                }
            }
        }

    }

        /**
         * Lisätään pallojen kiihtyvyyksiin
         * pallojen overlap vuorovaikutus.
         * Eli kun pallot meinaavat mennä päällekkäin sen estää
         * Lennard Jones repulsio
         * @see http://en.wikipedia.org/wiki/Lennard-Jones_potential
         * Tässä vain repulsiivine C12 termi.
         * @param pallot Pallot jotka vuorovaikuttavat keskenään.
         */
    public void oldLisaaHardCoreVoimat(Pallot pallot) {
        float dx, dy, d, d10;
        final float epsilon = 50.f;
        //final float epsilon = 0.0000000000001f;
        //LautaData lautadata = new LautaData();
        final float minDist = lautadata.getPallonSade() *1.0f;

        ArrayList<Pallo> p1 = pallot.getPallotArray();
        int imax = pallot.getPallotArray().size();
        for (int i = 0; i < imax; i++){
            for (int j = i+1; j < imax; j++) {
                Pallo pallo1, pallo2;
                pallo1 = p1.get(i);
                pallo2 = p1.get(j);
                dx = pallo1.getPalloX() - pallo2.getPalloX();
                dy = pallo1.getPalloY() - pallo2.getPalloY();
                d = (float) Math.sqrt(dx*dx + dy*dy);
                //d10 = (float) Math.pow(0.5*minDist/d,10);
                float myexp = (float) Math.exp(-400f*d);
                pallo1.lisaaPalloFX(epsilon * dx /d * myexp);
                pallo1.lisaaPalloFY(epsilon * dy /d * myexp);
                pallo2.lisaaPalloFX(-epsilon * dx /d * myexp);
                pallo2.lisaaPalloFY(-epsilon * dy /d * myexp);
            }
        }
    }      
    
   /**
    * Lisätään kitkavoima palloille.
    * Kitkan suunta on nopeutta vastaan.
    * @param pallot 
    * @see http://en.wikipedia.org/wiki/Friction#Dry_friction
    */
   public void lisaaKitka(Pallot pallot) {
        float vx, vy, massa, kitkaVoima;
        final float kitkaKerroin = 0.1f;
        final float gravitaatioVakio = 9.81f;
        //LautaData lautadata = new LautaData();
        
        ArrayList<Pallo> p = pallot.getPallotArray();
        for (Pallo pallo : p) {
                vx = pallo.getPalloVX();
                vy = pallo.getPalloVY();
                pallo.setPalloVX(0.995f * vx);
                pallo.setPalloVY(0.995f * vy);
                //massa = lautadata.getPallonMassa();
                //kitkaVoima = kitkaKerroin * massa * gravitaatioVakio;
                //kitkaVoima = kitkaKerroin * (float) Math.sqrt(vx*vx + vy*vy);
                //pallo.lisaaPalloVX(-this.getYksikkoVektoriX(vx, vy) * kitkaVoima);
                //pallo.lisaaPalloVY(-this.getYksikkoVektoriY(vx, vy) * kitkaVoima);
        }
   }        

   /**
    * nollataan palloihin kohdistuvat voimat
    * @param pallot 
    */
   public void nollaaVoimat(Pallot pallot) {
        
        ArrayList<Pallo> p = pallot.getPallotArray();
        for (Pallo pallo : p) {
            pallo.nollaaPalloFX();
            pallo.nollaaPalloFY();
        }
   }        
      
      
    /**
     * @param x vektorin x komponentti
     * @param y vektorin y komponentti
     * @return palautetaan xi+yi suuntaisen yksikkövektorin X komponentti 
     */   
    public float getYksikkoVektoriX(float x, float y){
        float d = (float) Math.sqrt(x*x + y*y);
        if (d < 0.1){
            return 0.0f;
        }
        else{
            return x/d;
        }
    }
    
    /**
     * @param x vektorin x komponentti
     * @param y vektorin y komponentti
     * @return palautetaan xi+yi suuntaisen yksikkövektorin Y komponentti 
     */
    public float getYksikkoVektoriY(float x, float y){
        float d = (float) Math.sqrt(x*x + y*y);
        if (d < 0.1){
            return 0.0f;
        }
        else{
            return y/d;
        }         
    }    
        
    
}
