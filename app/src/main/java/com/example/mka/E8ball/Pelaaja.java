/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.mka.E8ball;

/**
 * Biljardinpelaajan tiedot
 */
public class Pelaaja {
    private String name, tries;
    private int score;
    private boolean win, turn;

    public Pelaaja(String name, boolean turn) {
        this.name = name;
        this.tries = "enTieda";
        this.score = 0;
        this.win = false;
        this.turn = turn;
    }

    public void reset(){
        this.tries = "enTieda";
        this.score = 0;
        this.win = false;
    }

    public String getName() {
        return this.name;
    }

    public String getTries() {
        return this.tries;
    }

    public int getScore() {
        return this.score;
    }

    public boolean getWin() {
        return this.win;
    }

    public boolean getTurn() {
        return this.turn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTries(String tries) {
        this.tries = tries;
    }

    public void setScore(int score) {
        this.score = this.score + score;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public char getTryColor() {
        if (this.tries.equals("enTieda")) {
            return '?';
        } else if (this.tries.equals("punainen")) {
            return 'R';
        } else if (this.tries.equals("sininen")) {
            return 'B';
        } else {
            return '?';
        }
    }
}
