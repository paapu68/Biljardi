package com.example.mka.biljardi;

import android.net.Uri;
import android.os.Bundle;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class Biljardi extends Activity {

    // BiljardiView on pelin view
    // Siinä on myös logiikka ja
    // vastineet ruudun koskettamisiin
    BiljardiView BiljardiView;
    private LautaData lautadata = new LautaData();

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Alusta view ja aseta se viewiksi
        BiljardiView = new BiljardiView(this);
        setContentView(BiljardiView);


        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();


        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Biljardi Page",

                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.mka.biljardi/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Biljardi Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.mka.biljardi/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    // BiljardiView
    // on sisempi luokka.
    // Viimeisessä  }
    // on joitain kummaa

    // Implementoidaan runnable metodi
    // Joten voidaan käyttää run-metodia
    class BiljardiView extends SurfaceView implements Runnable {

        // Tämä on säie
        Thread gameThread = null;

        // Tarvitaan SurfaceHolder
        // Jotta voidaa käyttää Paint ja Canvas säikeessä

        SurfaceHolder ourHolder;


        // Joukseeko peli vai ei?
        volatile boolean playing;

        // Canvas Paint objektit
        Canvas canvas;
        Paint paint;

        // Pelin dt
        float dt;

        // Tarvitaan frame raten (fps) laskemiseen
        private float timeThisFrame;

        // Näytön koko pixeleissä
        int screenX;
        int screenY;


        //Reikiin liittyvät toiminnot
        Reiat reiat;


        //Pelin tilanteeseen liittyvä logiikka
        Logiikka logiikka;

        // tämä luokka tarjoaa liikkumiseen ja voimaan liittyvät rutiinit
        Liike liike;

        // Seinat
        // Seiniin törmäkset
        Seina seina;

        // Keppi
        Keppi keppi;

        // Pallot
        Pallot pallot;

        // Pelaajat
        Pelaaja pelaaja1, pelaaja2;


        ArrayList<Pelaaja> pelaajat = new ArrayList<Pelaaja>();


        // Kun kutsutaan (call new())  gameView:ssa niin tämä lähtee liikkeelle
        public BiljardiView(Context context) {
            // Seuraava rivi pyytää
            // SurfaceView class asettamaan objektin

            super(context);

            // Alustus
            ourHolder = getHolder();
            paint = new Paint();

            // Display object jotta saadaan näytön data
            Display display = getWindowManager().getDefaultDisplay();
            // Resoluutio laitetaan Point objektiin
            Point size = new Point();
            display.getSize(size);

            screenX = size.x;
            screenY = size.y;

            reiat = new Reiat();

            logiikka = new Logiikka();

            liike = new Liike();

            seina = new Seina();

            keppi = new Keppi(screenX, screenY);

            pallot = new Pallot();

            pelaaja1 = new Pelaaja("1", true);
            pelaaja2 = new Pelaaja("2", false);

            pelaajat.add(pelaaja1);
            pelaajat.add(pelaaja2);

        }

        public void laitaAlkuasemaJaRestart() {

            //Kepin alkupaikka lyöntipalloon
            keppi.reset(screenX, screenY);
            pallot.reset();
        }

        @Override
        public void run() {
            int idraw = 0;
            // Aika millisekunneissa startFrameTime :n
            dt = 40f;
            logiikka.setPallotLiikkuu(false);
            lautadata.setTekstinPaikkaX(screenX);
            lautadata.setTekstinPaikkaY(screenY);

            while (logiikka.getPlaying()) {
                float startFrameTime = System.currentTimeMillis();


                // Päivitä frame
                if (logiikka.getPallotLiikkuu()) {
                    update();
                }


                // Piirrä frame
                //idraw++;
                //if (idraw > 2){
                //    idraw = 0;
                draw();
                //}
                //Log.i("DRAW", "OK");

                // Tarkastetaan pelitilanne
                if (logiikka.getPallotLiikkuu()) {
                    logiikka.tarkastaTilanne(pelaajat, reiat, pallot, liike);
                    //Log.i("LOGIIKKA", "OK");
                }


                // Jos lyönti on ohi niin asetetaan että saa lyödä
                if (!logiikka.getPallotLiikkuu() & !logiikka.getSaaLyoda()) {

                    logiikka.setSaaLyoda(true);
                }


                if (logiikka.getPallotLiikkuu()) {
                    timeThisFrame = System.currentTimeMillis() - startFrameTime;
                    if (timeThisFrame < dt) {
                        try {
                            Thread.sleep((long) (dt - timeThisFrame));
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
                if (!logiikka.getPlaying()){
                    askFinish();
                }
            }


        }

        // Kysytään halutaanko pelata lisää ja näytetään tulos
        public void askFinish() {
            if (ourHolder.getSurface().isValid()) {
                // lukitaan kanvaasi
                canvas = ourHolder.lockCanvas();
                canvas.drawColor(Color.BLACK);
                paint.setColor(Color.argb(255, 100, 150, 200));
                String winner;
                if (pelaaja1.getWin()) {
                    winner = "PLAYER " + pelaaja1.getName();
                } else {
                    winner = "PLAYER " + pelaaja2.getName();
                }
                //Log.i("Voittaja", winner);

                canvas.drawText("CONTINUE", 0, screenY / 20, paint);
                canvas.drawText(" The winner is: " + winner, 0, screenY/10, paint);
                canvas.drawText("QUIT", 0, screenY/2, paint);
                //piirretään kaikki
                ourHolder.unlockCanvasAndPost(canvas);

                while (logiikka.getFinish().equals("enTieda")) {

                }
                if (logiikka.getFinish().equals("true")) {
                    finish();
                } else {
                    logiikka.totalReset(pelaajat, reiat, pallot);
                }

            }
        }


        // Kaikki päivitettävä tänne
        // Liike, törmäykset jne
        public void update() {

            //seinasta kimpoaminen
            seina.VaihdaLiikemaara(pallot);

            // siirretään palloja ja päivitetään voimat ja nopeudet ja kiihtyvyydet
            liike.update(dt * 0.001f, pallot);


        }

        // Piirrä päivitetty näkymä
        public void draw() {
            float mymin;
            // drawing surface OK?
            if (ourHolder.getSurface().isValid()) {
                // Lukitaan kanvaasi
                canvas = ourHolder.lockCanvas();

                // background color
                canvas.drawColor(Color.argb(255, 0, 128, 0));

                // Jos pallot ei liiku niin keppi piirretään
                if (!logiikka.getPallotLiikkuu()) {
                    //valkoinen keppi
                    paint.setColor(Color.argb(255, 255, 255, 255));

                    // Piirrä keppi
                    paint.setStrokeWidth(4.0f);
                    keppi.update_alku(screenX * pallot.getLyontiPallo().getPalloX(), screenY * pallot.getLyontiPallo().getPalloY());
                    if (keppi.getPiirra()) {
                        canvas.drawLine(keppi.getStartX(), keppi.getStartY(), keppi.getStopX(), keppi.getStopY(), paint);
                    }
                }

                // Piirra reiat
                mymin = Math.min(screenX, screenY);
                paint.setColor(Color.argb(255, 255, 255, 255));
                ArrayList<Float> reiatX = reiat.getReiatX();
                ArrayList<Float> reiatY = reiat.getReiatY();
                int imax = reiatX.size();
                for (int i = 0; i < imax; i++) {
                    canvas.drawCircle(reiatX.get(i) * screenX, reiatY.get(i) * screenY,
                            mymin * lautadata.reianSade, paint);
                }

                // Piirrä pallot
                for (Pallo pallo : pallot.getPallotArray()) {
                    //Log.i("Omapallovari", pallo.getPalloVari());
                    if (pallo.getPalloVari() == "valkoinen") {
                        paint.setColor(Color.argb(255, 255, 255, 255));
                        canvas.drawCircle(pallo.getPalloX() * screenX, pallo.getPalloY() * screenY,
                                mymin * lautadata.pallonSade, paint);
                    } else if (pallo.getPalloVari() == "musta") {
                        paint.setColor(Color.argb(255, 0, 0, 0));
                        canvas.drawCircle(pallo.getPalloX() * screenX, pallo.getPalloY() * screenY,
                                mymin * lautadata.pallonSade, paint);
                    } else if (pallo.getPalloVari() == "punainen") {
                        //Log.i("Omapallovari", " punainen");
                        paint.setColor(Color.argb(255, 255, 0, 0));
                        canvas.drawCircle(pallo.getPalloX() * screenX, pallo.getPalloY() * screenY,
                                mymin * lautadata.pallonSade, paint);
                    } else if (pallo.getPalloVari() == "sininen") {
                        paint.setColor(Color.argb(255, 0, 0, 255));
                        canvas.drawCircle(pallo.getPalloX() * screenX, pallo.getPalloY() * screenY,
                                mymin * lautadata.pallonSade, paint);
                    }

                }

                // VAlitaan vari
                paint.setColor(Color.argb(255, 255, 255, 255));

                // valitaan tekstikoko
                paint.setTextSize(screenX / 20);

                // tekstin väri sen mukaan mita yrittaa
                if (pelaaja1.getTries().equals("punainen")) {
                    paint.setColor(Color.argb(255, 255, 0, 0));
                } else if (pelaaja1.getTries().equals("sininen")) {
                    paint.setColor(Color.argb(255, 0, 0, 255));
                } else if (pelaaja1.getTries().equals("musta")) {
                    paint.setColor(Color.argb(255, 255, 255, 255));
                }
                String show1 = "";
                String show2 = "";
                if (pelaaja1.getTurn() & logiikka.getSaaLyoda()) {
                    show1 = "TURN:";
                }
                canvas.drawText(show1 + " P: " + pelaaja1.getName() + " S: " +
                                String.valueOf(pelaaja1.getScore()), lautadata.getTekstinPaikkaX(), lautadata.getTekstinPaikkaY(),
                        paint);
                if (pelaaja2.getTurn() & logiikka.getSaaLyoda()) {
                    show2 = "TURN:";
                }
                // tekstin väri sen mukaan mita yrittaa
                if (pelaaja2.getTries().equals("punainen")) {
                    paint.setColor(Color.argb(255, 255, 0, 0));
                } else if (pelaaja2.getTries().equals("sininen")) {
                    paint.setColor(Color.argb(255, 0, 0, 255));
                } else if (pelaaja2.getTries().equals("musta")) {
                    paint.setColor(Color.argb(255, 255, 255, 255));
                }

                canvas.drawText(show2 + " P: " + pelaaja2.getName() + " S: " +
                        String.valueOf(pelaaja2.getScore()), lautadata.getTekstinPaikkaX(), lautadata.getTekstinPaikkaY() + screenX / 10, paint);

                // Piirra kaikki (nyt vasta)
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        // JOs peli loppu niin suljataan säie
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Virhe", "saie");
            }

        }

        // jos aktiteetti alkaa niin aloitetaan säie
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }


        // kuunnellaan kosketuksia näyttöön.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            boolean lopeta;
            if (!logiikka.getPlaying()){
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        lopeta = logiikka.tarkastaLopeta(motionEvent.getY(), screenY);
                        if (lopeta) {
                            logiikka.setPlaying(false);
                           logiikka.setFinish("true");
                        } else {
                            logiikka.setPlaying(true);
                            logiikka.setFinish("false");
                        }
                    default:

                }
                return true;
            }

            if (!logiikka.getPallotLiikkuu() & logiikka.getSaaLyoda()) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                    // Kosketus
                    case MotionEvent.ACTION_DOWN:
                        keppi.setPiirra(true);
                        keppi.update_loppu(motionEvent.getX(), motionEvent.getY());
                        break;

                    case MotionEvent.ACTION_MOVE:
                        keppi.setPiirra(true);
                        keppi.update_loppu(motionEvent.getX(), motionEvent.getY());
                        break;

                    case MotionEvent.ACTION_UP:
                        pallot.nollaaNopeudet();
                        keppi.iske(pallot.getLyontiPallo());
                        keppi.setPiirra(false);
                        logiikka.setPallotLiikkuu(true);
                        logiikka.setSaaLyoda(false);
                        lautadata.setTekstinPaikkaX(screenX);
                        lautadata.setTekstinPaikkaY(screenY);
                        break;
                }
            }
            return true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        BiljardiView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BiljardiView.pause();
    }

}

