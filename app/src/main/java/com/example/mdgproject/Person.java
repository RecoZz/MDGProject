package com.example.mdgproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.widget.Toast;

import java.util.ArrayList;


enum Heading
{
    UP,DOWN,RIGHT,LEFT
}
public class Person {
    private Context context;
    private  int BlOCK_SIZE;
    private Point head;
    private Bitmap headRightBitmap;
    private Heading heading;
    private Bitmap headLeftBitmap;
    private Bitmap headUpBitmap;
    private Bitmap headDownBitmap;
    private Point movingRange;
    private Bitmap snakeBodyBitmap;


    Person(Context context, Point movingRange , int BlOCK_SIZE)
    {
        this.context=context;
        this.BlOCK_SIZE = BlOCK_SIZE;

        this.movingRange = movingRange;
        Matrix matrix = new Matrix();
        headRightBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.headright);

        headRightBitmap= Bitmap.createScaledBitmap(headRightBitmap ,BlOCK_SIZE ,BlOCK_SIZE, true);
        matrix.preScale(-1,1);
        headLeftBitmap = Bitmap.createBitmap(headRightBitmap ,0,0,BlOCK_SIZE ,BlOCK_SIZE ,matrix, true);
        matrix.preRotate(90);
        headDownBitmap = Bitmap.createBitmap(headRightBitmap ,0,0,BlOCK_SIZE ,BlOCK_SIZE ,matrix, true);
        matrix.preRotate(-180);
        headUpBitmap = Bitmap.createBitmap(headRightBitmap ,0,0,BlOCK_SIZE ,BlOCK_SIZE ,matrix, true);


        

        heading = Heading.LEFT;

        head= new Point(20,2);
    }
    boolean haveSnakeEatenApple(Point applePosition)
    {
        if(applePosition.x  == head.x  && applePosition.y  == head.y )
        {


            return true;
        }
        return false;
    }
    void moveSnake()
    {

        switch (heading)
        {
            case RIGHT:
                head.x += 1;
                if(head.x==movingRange.x)
                    head.x=0;
                break;
            case UP:
                head.y -= 1;
                if(head.y==-1)
                    head.y= movingRange.y;
                break;
            case DOWN:
                head.y += 1;
                if(head.y==movingRange.y)
                    head.y=0;
                break;
            case LEFT:
                head.x -= 1;
                if(head.x==-1)
                    head.x= movingRange.x;
                break;
        }
    }
    void drawSnake(Canvas canvas , Paint paint)
    {
        

        switch (heading)
        {
            case RIGHT:
                canvas.drawBitmap(headRightBitmap,head.x*BlOCK_SIZE, head.y*BlOCK_SIZE ,paint);
                break;
            case UP:
                canvas.drawBitmap(headUpBitmap,head.x*BlOCK_SIZE, head.y*BlOCK_SIZE ,paint);
                break;
            case DOWN:
                canvas.drawBitmap(headDownBitmap,head.x*BlOCK_SIZE, head.y*BlOCK_SIZE ,paint);
                break;
            case LEFT:
                canvas.drawBitmap(headLeftBitmap,head.x*BlOCK_SIZE, head.y*BlOCK_SIZE ,paint);
                break;
        }
    }

    void reset()
    {
        head.x=20;
        head.y=2;
        heading = Heading.RIGHT;
    }

    void setMovingDirection(Heading h)
    {
        heading = h;
    }
}