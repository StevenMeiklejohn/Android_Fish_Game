package example.codeclan.com.turbofish;

/**
 * Created by user on 07/08/2016.
 */
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;


// Notice we implement runnable so we have
// A thread and can override the run method.
class TurboFishView extends SurfaceView implements Runnable {

    private Context context;

    // This is our thread
    private Thread gameThread = null;

    // This is new. We need a SurfaceHolder
    // When we use Paint and Canvas in a thread
    // We will see it in action in the draw method soon.
    private SurfaceHolder ourHolder;

//    Instantiate the playerFish
    private PlayerFish playerFish;

//    Instantiate an enemy
    private Enemy enemy1;

    // A boolean which we will set and unset
    // when the game is running- or not.
    volatile boolean playing;

    // For sound FX
    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int damageShelterID = -1;
    private int uhID = -1;
    private int ohID = -1;

    // The score
    private int score = 0;

    // Lives
    private int lives = 3;

    // How menacing should the sound be?
    private long menaceInterval = 1000;
    // Which menace sound should play next

    // Which menace sound should play next
    private boolean uhOrOh;

    // When did we last play a menacing sound
    private long lastMenaceTime = System.currentTimeMillis();

    // The size of the screen in pixels
    private int screenX;
    private int screenY;

    // A Canvas and a Paint object
    Canvas canvas;
    Paint paint;

    // This variable tracks the game frame rate
    long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    // Game is paused at the start
    private boolean paused = true;

    // Up to 60 invaders
    private Enemy[] enemies = new Enemy[500];
    private int numEnemies = 0;


    // When the we initialize (call new()) on gameView
    // This special constructor method runs
    public TurboFishView(Context context, int x, int y) {

        // The next line of code asks the
        // SurfaceView class to set up our object.
        // How kind.
        super(context);

        // Make a globally available copy of the context so we can use it in another method
        this.context = context;

        // Initialize ourHolder and paint objects
        ourHolder = getHolder();
        paint = new Paint();


        screenX = x;
        screenY = y;

        // Set our boolean to true - game on!
        playing = true;

        // This SoundPool is deprecated but don't worry
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        try{
            // Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("shoot.ogg");
            shootID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderexplode.ogg");
            invaderExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("playerexplode.ogg");
            playerExplodeID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("damageshelter.ogg");
            damageShelterID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("uh.ogg");
            uhID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("oh.ogg");
            ohID = soundPool.load(descriptor, 0);

        }catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }


        prepareLevel();

    }

    private void prepareLevel() {

        // Here we will initialize all the game objects

        // Make a new player fish
        playerFish = new PlayerFish(context, screenX, screenY);

//        Make a new enemy fish
//        enemy1 = new Enemy(context, screenX, screenY);
//    }

        // Build an army of invaders
        numEnemies = 0;
        for (int column = 0; column < 20; column++) {
            for (int row = 0; row < 5; row++) {
                enemies[numEnemies] = new
                        Enemy(context, screenX, screenY);
                numEnemies++;
            }
        }
    }

    @Override
    public void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            if(!paused){
                update();
            }

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame > 0) {
                fps = 1000 / timeThisFrame;
            }

            // We will do something new here towards the end of the project
            // Play a sound based on the menace level
            if(!paused) {
                if ((startFrameTime - lastMenaceTime)> menaceInterval) {
                    if (uhOrOh) {
                        // Play Uh
                        soundPool.play(uhID, 1, 1, 0, 0, 1);

                    } else {
                        // Play Oh
                        soundPool.play(ohID, 1, 1, 0, 0, 1);
                    }

                    // Reset the last menace time
                    lastMenaceTime = System.currentTimeMillis();
                    // Alter value of uhOrOh
                    uhOrOh = !uhOrOh;
                }
            }

        }

    }

    // Everything that needs to be updated goes in here
    // In later projects we will have dozens (arrays) of objects.
    // We will also do other things like collision detection.
    public void update() {


//        // Did an invader bump into the side of the screen
//        boolean bumped = false;

//        // Has the player lost
//        boolean lost = false;

        // Move the player's ship
//        Log.d("Here", Integer.toString(screenY));
        playerFish.update(fps);
//
//        enemy1.update(fps);

        // Update all the invaders if visible
        for(int i = 0; i < numEnemies; i++) {
            if (enemies[i].getVisibility()) {
                // Move the next invader
                enemies[i].update(fps);
            }
        }
//        // If bob is moving (the player is touching the screen)
//        // then move him to the right based on his target speed and the current fps.
//        if (isMoving) {
//            bobXPosition = bobXPosition + (walkSpeedPerSecond / fps);
//        }
    }

    // Draw the newly updated scene
    public void draw() {

        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255,  255, 255, 255));

            // Now draw the player fish
            canvas.drawBitmap(playerFish.getBitmap(), playerFish.getX(), playerFish.getY() - 50, paint);

            // Draw the invaders
            for(int i = 0; i < numEnemies; i++) {
                if (enemies[i].getVisibility()) {
                    if(uhOrOh) {
                    canvas.drawBitmap(enemies[i].getBitmap1(), enemies[i].getX(), enemies[i].getY(), paint);
                }else{
                    canvas.drawBitmap(enemies[i].getBitmap2(), enemies[i].getX(), enemies[i].getY(), paint);
                    }
                    }
            }




//            // Now draw the enemy shark
//            canvas.drawBitmap(enemy1.getBitmap(), enemy1.getX(), enemy1.getY() - 50, paint);

            // Make the text a bit bigger
            paint.setTextSize(45);

            // Display the current fps on the screen
            canvas.drawText("FPS:" + fps, 20, 40, paint);

            // Display the current player y position on the screen
            canvas.drawText("Y:" + playerFish.getY(), 20, 100, paint);
//
//            // Draw bob at bobXPosition, 200 pixels
//            canvas.drawBitmap(bitmapBob, bobXPosition, 200, paint);

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

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                paused = false;
                playerFish.setMovementState(playerFish.UP);
                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                playerFish.setMovementState(playerFish.STOPPED);

                break;
        }
        return true;
    }






}



