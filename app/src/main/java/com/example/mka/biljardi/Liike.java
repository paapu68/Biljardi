/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.mka.biljardi;

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

public class Liike{
    LautaData lautadata = new LautaData();
    private float maxSiirtyma;
    public Liike() {
        this.maxSiirtyma = 1.0f;
    }

// tehdään aika-askel ja päivitetään x ja y ja vx ja vy
    public void update(long fps, Pallot pallot){
        float dt = 1.0f/fps;

        ArrayList<Pallo> p1 = pallot.getPallotArray();
        // uudet paikat
        for (Pallo pallo1 : p1) {
            float xold = pallo1.getPalloX();
            float yold = pallo1.getPalloY();

            float xnew = pallo1.getPalloX() + pallo1.getPalloVX()*dt
                    + 0.5f * pallo1.getPalloAX() * dt * dt;
            float ynew = pallo1.getPalloY() + pallo1.getPalloVY()*dt
                    + 0.5f * pallo1.getPalloAY() * dt * dt;
            pallo1.setPalloX(xnew);
            pallo1.setPalloY(ynew);
            float siirtyma = (float) Math.sqrt((xold-xnew)*(xold-xnew)
                    + (yold-ynew)*(yold-ynew));
            if (siirtyma > this.maxSiirtyma){
                this.maxSiirtyma = siirtyma;
            }

        }


        // voimat uusissa paikoissa
        this.nollaaVoimat(pallot);
        //lisaavoimat.lisaaCoulombVoimatBiljardiPallot(pallot);
        //lisaavoimat.lisaaHardCoreVoimat(pallot);
        this.lisaaKitka(pallot);

        // uudet nopeudet
        for (Pallo pallo1 : p1) {
            float vxnew = pallo1.getPalloVX()
                        + 0.5f * (pallo1.getPalloFX()/lautadata.getPallonMassa()
                        + pallo1.getPalloAX()) * dt;
            float vynew = pallo1.getPalloVY()
                        + 0.5f * (pallo1.getPalloFY()/lautadata.getPallonMassa()
                        + pallo1.getPalloAY()) * dt;
            pallo1.setPalloVX(vxnew);
            pallo1.setPalloVY(vynew);
        }
        // uudet kiihtyvyydet
        for (Pallo pallo1 : p1) {
            pallo1.setPalloAX(pallo1.getPalloFX()/lautadata.getPallonMassa());
            pallo1.setPalloAY(pallo1.getPalloFY()/lautadata.getPallonMassa());
        }
    }



    /**
     * Lisätään pallojen voimiin
     * pallojen keskinäisistä Coulombin
     * voimista aiheutuvat kiihtyvyydet
     * fx = coulombsConstant * q1 * q2 * dx / (r²)
     * fy = coulombsConstant * q1 * q2 * dy / (r²)
     * yksiköt coulombi, metri, kilogramma
     * @param pallot Pallot jotka vuorovaikuttavat keskenään.
     */
    public void lisaaCoulombVoimatBiljardiPallot(Pallot pallot) {
        float dx, dy, d2;
        final float coulombsConstant = 8.987551787368f*1000000000f;
        
        ArrayList<Pallo> p1 = pallot.getPallotArray();
        for (Pallo pallo1 : p1) {
            for (Pallo pallo2 : p1) {
                dx = pallo1.getPalloX() - pallo2.getPalloX();
                dy = pallo1.getPalloY() - pallo2.getPalloY();
                d2 = (float) Math.sqrt(dx*dx + dy*dy);
                float varaus1 = pallo1.getPalloVaraus();
                float varaus2 = pallo2.getPalloVaraus();
                if (d2 > 0.01) {
                    pallo1.lisaaPalloFX(coulombsConstant *  
                        varaus1 * varaus2 * dx / (d2));
                    pallo1.lisaaPalloFY(coulombsConstant *  
                        varaus1 * varaus2 * dy / (d2));
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
    public void lisaaHardCoreVoimat(Pallot pallot) {
        float dx, dy, d, d10;
        final float epsilon = 1f-13;
        //LautaData lautadata = new LautaData();
        final float minDist = lautadata.getPallonHalkaisija() /2.0f;
        
        ArrayList<Pallo> p1 = pallot.getPallotArray();
        for (Pallo pallo1 : p1) {
            for (Pallo pallo2 : p1) {
                dx = pallo1.getPalloX() - pallo2.getPalloX();
                dy = pallo1.getPalloY() - pallo2.getPalloY();
                d = (float) Math.sqrt(dx*dx + dy*dy) - minDist;
                d10 = (float) Math.pow(d,10);
                if (pallo1 != pallo2) {
                    pallo1.lisaaPalloFX(
                    (epsilon * dx) / (d10));
                    pallo1.lisaaPalloFY( 
                    (epsilon * dy) / (d10));
                }
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
        final float kitkaKerroin = 10f;
        final float gravitaatioVakio = 9.81f;
        //LautaData lautadata = new LautaData();
        
        ArrayList<Pallo> p = pallot.getPallotArray();
        for (Pallo pallo : p) {
                vx = pallo.getPalloVX();
                vy = pallo.getPalloVY();
                massa = lautadata.getPallonMassa();
                kitkaVoima = kitkaKerroin * massa * gravitaatioVakio;
                pallo.lisaaPalloFX(-this.getYksikkoVektoriX(vx, vy)*
                        kitkaVoima);            
                pallo.lisaaPalloFY(-this.getYksikkoVektoriY(vx, vy)*
                        kitkaVoima);
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
