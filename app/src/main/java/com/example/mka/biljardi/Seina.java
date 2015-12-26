/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.mka.biljardi;

import com.example.mka.biljardi.LautaData;

import java.util.ArrayList;

/**
 *
 * Pelilaudan seinien rajat annetaan reaalimaailmassa.
 * Lisäksi metodi kimmauttamaan pallo seinästä
 */
public class Seina {
    private final double alax, alay, ylax, ylay;
    private LautaData lautadata = new LautaData();

    public Seina(){
        this.alax = lautadata.getMinLautaX();
        this.alay = lautadata.getMinLautaY();
        this.ylax = lautadata.getMaxLautaX();
        this.ylay = lautadata.getMaxLautaY();
    }
    
/**
 * Jos pallo on lähellä seinää sen 
 * nopeus vaihdetaan vastakkaiseksi.  
 * @param pallot tutkittavat pallot 
 */
    public void VaihdaLiikemaara(Pallot pallot) {             
        
        ArrayList<Pallo> p1 = pallot.getPallotArray();
        for (Pallo pallo1 : p1) {
            if (pallo1.getPalloX() <= this.alax){
                pallo1.setPalloVX(Math.abs(pallo1.getPalloVX()));
            }   
            if (pallo1.getPalloX() >= this.ylax){
                pallo1.setPalloVX(-Math.abs(pallo1.getPalloVX()));
            } 
            if (pallo1.getPalloY() <= this.alay){
                pallo1.setPalloVY(Math.abs(pallo1.getPalloVY()));
            }   
            if (pallo1.getPalloY() >= this.ylay){
                pallo1.setPalloVY(-Math.abs(pallo1.getPalloVY()));
            }    
        }
     }
     
     public double getYlax(){
         return this.ylax;
     }
     
     public double getYlay(){
         return this.ylay;
     }
     
     public double getAlax(){
         return this.alax;
     }
     
     public double getAlay(){
         return this.alay;
     }
     
}
