package example.codeclan.com.turbofish;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by user on 07/08/2016.
 */

public class Enemy {

    RectF rect;

    // The enemy will be represented by a Bitmap
    private Bitmap bitmap;

    private float screenSizeY;
    private float screenSizeX;

    //     How long and high our sprite will be
    private float length;
    private float height;

    private float randomX;
    private float randomY;
    private float previousRandomX;
    private float previousRandomY;



    // X is the far left of the rectangle which forms our fish sprite
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speed that the fish sprite will move
    private float enemySpeed;

    // Which ways can the enemy Fish move
    public final int STOPPED = 0;
    public final int UP = 1;
    public final int LEFT = 2;
//    public final int RIGHT = 2;

    // Is the playerFish moving and in which direction
    private int enemyMoving = STOPPED;

    boolean isVisible;

//    // Bob starts off not moving
//    boolean isMoving = false;

    // This the the constructor method
    // When we create an object from this class we will pass
    // in the screen width and height
    public Enemy(Context context, int screenX, int screenY){
        // Initialize a blank RectF
        rect = new RectF();

//        length = 100;
//        height = 80;

        length = screenX / 12;
        height = screenY / 12;

        isVisible = true;

//        int padding = screenX / 25;
        int padding = 20;



//        x = column * (length + padding)+500;
//        y = row * (length + padding/10)+150;
        x = getRandomNumberInRange(500, screenX) + 1000;
        y = getRandomNumberInRange(50, screenY-50);

        // Initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shark);


        // stretch the first bitmap to a size appropriate for the screen resolution
        bitmap = Bitmap.createScaledBitmap(bitmap,
                (int) (length),
                (int) (height),
                false);


        // How fast is the invader in pixels per second
        enemySpeed = 100;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    public RectF getRect(){
        return rect;
    }

    // This is a getter method to make the rectangle that
    // defines our paddle available in BreakoutView class
    public Bitmap getBitmap(){
        return bitmap;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }



//    public float getScreenHeight(){
//        return screenSizeY;
//    }

//    public float getLength(){
//        return length;
//    }

//    public RectF getRect(){
//        return rect;
//    }
//
//    // This is a getter method to make the rectangle that
//    // defines our paddle available in BreakoutView class
//    public Bitmap getBitmap(){
//        return bitmap;
//    }
//
//    public float getX(){
//        return x;
//    }
//
//    public float getLength(){
//        return length;
//    }

    //    // This method will be used to change/set if the paddle is going left, right or nowhere
//    public void setMovementState(int state){
//        enemyMoving = state;
//    }



    // This update method will be called from update in SpaceInvadersView
    // It determines if the player ship needs to move and changes the coordinates
    // contained in x if necessary
    public void update(long fps){

        x = x - enemySpeed / fps;
//        if (enemyMoving == UP && y > 40) {
//            y = y - enemySpeed / fps;
//        }
//
//
//        if(enemyMoving == STOPPED && y < screenSizeY -20) {
//            y = y + enemySpeed / fps;
//        }


//         Update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + height;
        rect.left = x;
        rect.right = x + length;

    }







}


