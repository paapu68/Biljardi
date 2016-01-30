package com.example.mka.biljardi;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewDebug;

import java.io.IOException;
import java.util.ArrayList;


import com.example.mka.biljardi.LautaData;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import static java.util.concurrent.ThreadLocalRandom.*;

public class Biljardi extends Activity {

    // BiljardiView on pelin view
    // Siinä on myös logiikka ja
    // vastineet ruudun koskettamisiin
    BiljardiView BiljardiView;
    private LautaData lautadata = new LautaData();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Alusta view ja aseta se viewiksi
        BiljardiView = new BiljardiView(this);
        setContentView(BiljardiView);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Biljardi Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.mka.biljardi/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Biljardi Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
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

        // Peli on pausella aluksi
        // boolean paused = true;

        // Liikkuuko pallot
        // boolean pallotLiikkuu = false;

        // Saako lyönnin suorittaa
        // boolean saaLyoda = true;

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


        // Musaa FX
        SoundPool soundPool;
        int beep1ID = -1;
        int beep2ID = -1;
        int beep3ID = -1;
        int loseLifeID = -1;
        int explodeID = -1;

        // Pisteet
        //int score = 0;


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

            pelaajat.add(pelaaja2);
            pelaajat.add(pelaaja1);

            // Load the sounds

            // Musiikki
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);

            try {
                // ".n luokan objektiti
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor descriptor;

                descriptor = assetManager.openFd("beep1.ogg");
                beep1ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep2.ogg");
                beep2ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("beep3.ogg");
                beep3ID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("loseLife.ogg");
                loseLifeID = soundPool.load(descriptor, 0);

                descriptor = assetManager.openFd("explode.ogg");
                explodeID = soundPool.load(descriptor, 0);

            } catch (IOException e) {
                // Print an error message to the console
                Log.e("error", "failed to load sound files");
            }

            //laitaAlkuasemaJaRestart();

        }

        public void laitaAlkuasemaJaRestart() {

            //Kepin alkupaikka lyöntipalloon
            keppi.reset(screenX, screenY);
            pallot.reset();
            //score = 0;
        }

        @Override
        public void run() {
            int idraw = 0;
            // Aika millisekunneissa startFrameTime :n
            dt = 40f;
            logiikka.setPallotLiikkuu(false);

            while (playing) {
                float startFrameTime = System.currentTimeMillis();

                // Lasketaan fps tälle framelle
                // Tätä voi käyttää animaatiossa


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

                // jos pallot liikkuu  niin kepin alku ja loppupaikka laitetaan valkoiseen palloon
                if (logiikka.getPallotLiikkuu()) {
                    keppi.update_alku(screenX * pallot.getLyontiPallo().getPalloX(), screenY * pallot.getLyontiPallo().getPalloY());
                    keppi.update_loppu(screenX * pallot.getLyontiPallo().getPalloX(), screenY * pallot.getLyontiPallo().getPalloY());
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
            }

        }


        // Kaikki päivitettävä tänne
        // Liike, törmäykset jne
        public void update() {


            //seinasta kimpoaminen
            seina.VaihdaLiikemaara(pallot);

            // Move the paddle if required
            //paddle.update(fps)
            //
            // siirretään palloja ja päivitetään voimat ja nopeudet ja kiihtyvyydet
            liike.update(dt * 0.001f, pallot);


        }

        // Piirrä päivitetty näkymä
        public void draw() {
            float mymin;
            // Make sure our drawing surface is valid or we crash
            if (ourHolder.getSurface().isValid()) {
                // Lock the canvas ready to draw
                canvas = ourHolder.lockCanvas();

                // Draw the background color
                canvas.drawColor(Color.argb(255, 0, 128, 0));

                // Jos pallot ei liiku niin keppi piirretään
                if (!logiikka.getPallotLiikkuu()) {
                    //valkoinen keppi
                    paint.setColor(Color.argb(255, 255, 255, 255));

                    // Piirrä keppi
                    paint.setStrokeWidth(4.0f);
                    keppi.update_alku(screenX * pallot.getLyontiPallo().getPalloX(), screenY * pallot.getLyontiPallo().getPalloY());
                    canvas.drawLine(keppi.getStartX(), keppi.getStartY(), keppi.getStopX(), keppi.getStopY(), paint);
                }
                // Draw the paddle
                //canvas.drawRect(paddle.getRect(), paint);

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
                    //else { paint.setColor(Color.argb(255,255,255,255));
                    //}
                    //paint.setColor(pallo.getColor());


                    //Log.i("pallovari", String.valueOf(pallo.getColor()));
                    //Log.i("Omapallovari", pallo.getPalloVari());
                    //Log.i("keppix",Float.toString(keppi.getStopX()));
                }


                // Choose the brush color for drawing
                paint.setColor(Color.argb(255, 255, 255, 255));

                // Draw the score
                paint.setTextSize(screenX / 20);

                // tekstin väri sen mukaan mita yrittaa
                if (pelaaja1.getTries().equals("punainen")) {
                    paint.setColor(Color.argb(255, 255, 0, 0));
                } else if (pelaaja1.getTries().equals("sininen")) {
                    paint.setColor(Color.argb(255, 0, 0, 255));
                }
                String show1 = "";
                String show2 = "";
                if (pelaaja1.getTurn()) {
                    show1 = "TURN:";
                }
                canvas.drawText(show1 + "P: " + pelaaja1.getName() + " S: " +
                                String.valueOf(pelaaja1.getScore()), lautadata.getTekstinPaikkaX(), lautadata.getTekstinPaikkaY(),
                        paint);
                if (pelaaja2.getTurn()) {
                    show2 = "TURN:";
                }
                // tekstin väri sen mukaan mita yrittaa
                if (pelaaja2.getTries().equals("punainen")) {
                    paint.setColor(Color.argb(255, 255, 0, 0));
                } else if (pelaaja2.getTries().equals("sininen")) {
                    paint.setColor(Color.argb(255, 0, 0, 255));
                }
                canvas.drawText(show2 + " P: " + pelaaja2.getName() + " S: " +
                        String.valueOf(pelaaja2.getScore()), lautadata.getTekstinPaikkaX(), lautadata.getTekstinPaikkaY() + screenX / 10, paint);

                // Has the player cleared the screen?
                //if(score == numBricks * 10){
                //    paint.setTextSize(90);
                //    canvas.drawText("YOU HAVE WON!", 10,screenY/2, paint);
                //}

                // Has the player lost?
                //if(lives <= 0){
                //    paint.setTextSize(90);
                //    canvas.drawText("YOU HAVE LOST!", 10,screenY/2, paint);
                //}

                // Draw everything to the screen
                ourHolder.unlockCanvasAndPost(canvas);
            }

        }

        // If SimpleGameEngine Activity is paused/stopped
        // shutdown our thread.
        public void pause() {
            playing = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error:", "joining thread");
            }

        }

        // If SimpleGameEngine Activity is started then
        // start our thread.
        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        // The SurfaceView class implements onTouchListener
        // So we can override this method and detect screen touches.
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {

            if (!logiikka.getPallotLiikkuu() & logiikka.getSaaLyoda()) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

                    // Player has touched the screen
                    case MotionEvent.ACTION_DOWN:

                        //paused = false;
                        keppi.update_loppu(motionEvent.getX(), motionEvent.getY());
                        break;


                    case MotionEvent.ACTION_MOVE:
                        //paused = false;
                        keppi.update_loppu(motionEvent.getX(), motionEvent.getY());

                        // Player has removed finger from screen
                    case MotionEvent.ACTION_UP:
                        //paused = false;
                        pallot.nollaaNopeudet();
                        keppi.iske(pallot.getLyontiPallo());
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
    // This is the end of our BiljardiView inner class

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        BiljardiView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();

        // Tell the gameView pause method to execute
        BiljardiView.pause();
    }

}
// This is the end of the BiljardiGame class

