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

/**
 * Created by user on 07/08/2016.
 */

public class Enemy {

    RectF rect;

    // The enemy will be represented by a Bitmap
    private Bitmap bitmap;

    private float screenSizeY;
    private float screenSizeX;

    //     How long and high our paddle will be
    private float length;
    private float height;

    // X is the far left of the rectangle which forms our fish sprite
    private float x;

    // Y is the top coordinate
    private float y;

    // This will hold the pixels per second speed that the fish sprite will move
    private float enemySpeed;

    // Which ways can the playerFish move
    public final int STOPPED = 0;
    public final int UP = 1;
    public final int LEFT = 2;
//    public final int RIGHT = 2;

    // Is the playerFish moving and in which direction
    private int enemyMoving = STOPPED;

//    // Bob starts off not moving
//    boolean isMoving = false;

    // This the the constructor method
    // When we create an object from this class we will pass
    // in the screen width and height
    public Enemy(Context context, int screenX, int screenY){

        // Initialize a blank RectF
        rect = new RectF();

        length = 10;
        height = 10;

//        length = screenX/10;
//        height = screenY/10;

        screenSizeX = screenX;
        screenSizeY = screenY;

        // Start playerFish in roughly the screen centre
        x = screenX - 100;
        y = screenY - 500;

        // Initialize the bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shark);

        // stretch the bitmap to a size appropriate for the screen resolution
//        bitmap = Bitmap.createScaledBitmap(bitmap,
//                (int) (length),
//                (int) (height),
//                false);

        // How fast is the playerShip in pixels per second
        enemySpeed = 350;
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
    public void setMovementState(int state){
        enemyMoving = state;
    }



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


