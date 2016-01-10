package com.example.mka.biljardi;

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

public class Biljardi extends Activity {

    // BiljardiView on pelin view
    // Siinä on myös logiikka ja
    // vastineet ruudun koskettamisiin
    BiljardiView BiljardiView;
    private LautaData lautadata = new LautaData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Alusta view ja aseta se viewiksi
        BiljardiView = new BiljardiView(this);
        setContentView(BiljardiView);

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
        boolean paused = true;

        // Liikkuuko pallot
        boolean pallotLiikkuu = false;

        // Saako lyönnin suorittaa
        boolean saaLyoda = true;

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



        // The players paddle
        //Paddle paddle;

        //Reikiin liittyvät toiminnot
        Reiat reiat;

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
        Pelaaja dummy;

        // Up to 200 bricks
        //Brick[] bricks = new Brick[200];
        //int numBricks = 0;

        // Musaa FX
        SoundPool soundPool;
        int beep1ID = -1;
        int beep2ID = -1;
        int beep3ID = -1;
        int loseLifeID = -1;
        int explodeID = -1;

        // Pisteet
        int score = 0;

        // Elämät
        int lives = 3;

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

            // paddle = new Paddle(screenX, screenY);

            // Create a ball
            // ball = new Ball(screenX, screenY);

            reiat = new Reiat();

            liike = new Liike();

            seina = new Seina();

            keppi = new Keppi(screenX, screenY);

            pallot = new Pallot();

            pelaaja1 = new Pelaaja("1", true);
            pelaaja2 = new Pelaaja("2", false);

            // Load the sounds

            // Musiikki
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

            try{
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

            }catch(IOException e){
                // Print an error message to the console
                Log.e("error", "failed to load sound files");
            }

            //laitaAlkuasemaJaRestart();

        }

        public void laitaAlkuasemaJaRestart(){

            //Kepin alkupaikka lyöntipalloon
            keppi.reset(screenX, screenY);
            pallot.reset();

            
            // Put the ball back to the start
            //ball.reset(screenX, screenY);

            int brickWidth = screenX / 8;
            int brickHeight = screenY / 10;

            // Build a wall of bricks
            //numBricks = 0;
            for(int column = 0; column < 8; column ++ ){
                for(int row = 0; row < 3; row ++ ){
                    //bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight);
                    //numBricks ++;
                }
            }

            // if game over reset scores and lives
            if(lives == 0) {
                score = 0;
                lives = 3;
            }

        }

        @Override
        public void run() {
            int idraw=0;
            // Aika millisekunneissa startFrameTime :n
            dt=30f;
            pallotLiikkuu = false;
            Pelaaja vuorossa = pelaaja1;
            Pelaaja eiVuorossa = pelaaja2;

            while (playing) {
                float startFrameTime = System.currentTimeMillis();

                // Lasketaan fps tälle framelle
                // Tätä voi käyttää animaatiossa


                //Log.i("TIME", Float.toString(timeThisFrame));
                //if (timeThisFrame >= 1) {
                //    fps = 1000 / timeThisFrame;
                //}
                //else {
                //    fps = 1000;
                //}
                //startFrameTime = System.currentTimeMillis();

                // Päivitä frame
                if(pallotLiikkuu){
                    update();
                }


                // Piirrä frame
                //idraw++;
                //if (idraw > 2){
                //    idraw = 0;
                draw();
                //}

                // tarkastetaan liikkuuko pallot
                pallotLiikkuu = liike.getPallotLiikkuu(pallot);

                // jos pallot liikkuu  niin kepin alku ja loppupaikka laitetaan valkoiseen palloon
                if (pallotLiikkuu) {
                    keppi.update_alku(screenX * pallot.getLyontiPallo().getPalloX(), screenY * pallot.getLyontiPallo().getPalloY());
                    keppi.update_loppu(screenX * pallot.getLyontiPallo().getPalloX(), screenY * pallot.getLyontiPallo().getPalloY());
                }

                if (pallotLiikkuu) {
                    // tarkastetaan mikä väri menee ekana reikään
                    if (reiat.getEkanaReiassa() == "en tieda") {
                        reiat.ekanaReiassa(pallot);
                    }

                    // jos ekana menee musta on peli ratkennut
                    if (reiat.getEkanaReiassa() == "musta") {
                        vuorossa.setWin(false);
                        eiVuorossa.setWin(true);
                        // peli päättyy
                        playing = false;
                        pallotLiikkuu = false;
                        saaLyoda = false;
                    }

                    // Otetaan talteen mitkä pallot meni reikiin
                    reiat.lisaaReikiinMenneet(pallot);
                    // Poistetaan reikiin menneet pallot pelista
                    reiat.tapaNormiPallot(pallot);
                }

                // Jos lyönti on loppu niin vaihdetaan vuoro ja kirjataan tilanne
                if (!pallotLiikkuu & !saaLyoda){
                    // jos pelin ekana menee valkoinen niin vaihdetaan vuoro ja aloitetaan alusta
                    if (vuorossa.getTries() == "en tieda" & reiat.getEkanaReiassa() == "valkoinen") {
                        dummy = vuorossa;
                        vuorossa = eiVuorossa;
                        eiVuorossa = dummy;
                        pallot.asetaPallojenAlkupaikat();
                        saaLyoda = true;
                    }
                    // mitään ei mennyt reikiin
                    else if (reiat.getEkanaReiassa() == "en tieda"){
                        dummy = vuorossa;
                        vuorossa = eiVuorossa;
                        eiVuorossa = dummy;
                        saaLyoda = true;
                    }
                    // jotain punaista tai sinistä meni ensin reikiin
                    else {
                        // jos aiemmin ei ollut mennyt mitään reikiin niin laitetaan
                        // yritettävät värit pelaajille
                        if (vuorossa.getTries() == "en tieda") {
                            if (reiat.getEkanaReiassa() == "punainen") {
                                vuorossa.setTries("punainen");
                                eiVuorossa.setTries("sininen");
                            } else {
                                vuorossa.setTries("sininen");
                                eiVuorossa.setTries("punainen");
                            }
                        }
                        if (vuorossa.getTries() == "punainen") {
                            vuorossa.setScore(reiat.getMitaReiissa().get("punainen"));
                            eiVuorossa.setScore(reiat.getMitaReiissa().get("sininen"));
                        } else {
                            vuorossa.setScore(reiat.getMitaReiissa().get("sininen"));
                            eiVuorossa.setScore(reiat.getMitaReiissa().get("punainen"));
                        }
                        // vaihdetaan lyöntivuoro
                        dummy = vuorossa;
                        vuorossa = eiVuorossa;
                        eiVuorossa = dummy;
                        saaLyoda = true;
                    }
                }

                if (!pallotLiikkuu & !saaLyoda){
                    if (pelaaja1.getTurn()){
                        if (pelaaja1.getTries() == "en tieda"){
                            pelaaja1.
                        }
                    }


                    saaLyoda = true;
                }



                //}
                if (pallotLiikkuu) {
                    timeThisFrame = System.currentTimeMillis() - startFrameTime;
                    if (timeThisFrame < dt){
                        try{
                            Thread.sleep((long) (dt-timeThisFrame));}
                        catch(InterruptedException ex) {
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
            liike.update(dt*0.001f, pallot);




            // Check for ball colliding with a brick
            //for(int i = 0; i < numBricks; i++){

            //    if (bricks[i].getVisibility()){

            //        if(RectF.intersects(bricks[i].getRect(), ball.getRect())) {
            //            bricks[i].setInvisible();
            //            ball.reverseYVelocity();
            //            score = score + 10;

            //            soundPool.play(explodeID, 1, 1, 0, 0, 1);
            //        }
            //    }

            //}

            // Check for ball colliding with paddle
            //if(RectF.intersects(paddle.getRect(),ball.getRect())) {
            //    ball.setRandomXVelocity();
            //    ball.reverseYVelocity();
            //    ball.clearObstacleY(paddle.getRect().top - 2);

            //    soundPool.play(beep1ID, 1, 1, 0, 0, 1);
            //}

            // Bounce the ball back when it hits the bottom of screen
            //if(ball.getRect().bottom > screenY){
            //    ball.reverseYVelocity();
            //    ball.clearObstacleY(screenY - 2);

                // Lose a life
             //   lives --;
             //   soundPool.play(loseLifeID, 1, 1, 0, 0, 1);

             //   if(lives == 0){
             //       paused = true;
             //       createBricksAndRestart();
             //   }
            //}



            // Pause if cleared screen
            //if(score == numBricks * 10){
            //    paused = true;
            //    createBricksAndRestart();
            //}

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
                if (!pallotLiikkuu) {
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
                mymin=Math.min(screenX, screenY);
                paint.setColor(Color.argb(255, 255, 255, 255));
                ArrayList<Float> reiatX= reiat.getReiatX();
                ArrayList<Float> reiatY= reiat.getReiatY();
                int imax = reiatX.size();
                for (int i = 0; i < imax; i++){
                    canvas.drawCircle(reiatX.get(i) * screenX, reiatY.get(i) * screenY,
                            mymin * lautadata.reianSade, paint);
                }


                // Piirrä pallot
                for (Pallo pallo : pallot.getPallotArray()){
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
                    }
                    else if (pallo.getPalloVari() == "sininen") {
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


                //canvas.drawRect(ball.getRect(), paint);

                // Change the brush color for drawing
                //paint.setColor(Color.argb(255,  249, 129, 0));

                // Draw the bricks if visible
                //for(int i = 0; i < numBricks; i++){
                //    if(bricks[i].getVisibility()) {
                //        canvas.drawRect(bricks[i].getRect(), paint);
                //    }
                //}

                // Choose the brush color for drawing
                paint.setColor(Color.argb(255,  255, 255, 255));

                // Draw the score
                paint.setTextSize(40);
                canvas.drawText("Score: " + score + "   Lives: " + lives, 10,50, paint);

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

            if (!pallotLiikkuu & saaLyoda) {
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
                        pallotLiikkuu = true;
                        saaLyoda = false;
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

