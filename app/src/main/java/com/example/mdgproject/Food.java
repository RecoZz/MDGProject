package com.example.mdgproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.Random;

public class Food {
    private Point posFood= new Point();
    private Bitmap foodBit;
    private int bsize;
    private int bwide;
    private int bhigh;
    Food(Context context, int bhigh, int bwide, int bsize)
    {
        this.bhigh=bhigh;
        this.bwide=bwide;
        this.bsize=bsize;

        foodBit= BitmapFactory.decodeResource(context.getResources(), R.drawable.apple);
        foodBit=Bitmap.createScaledBitmap(foodBit, bsize,bsize,true);

    }
    void setFoodPos()
    {
        Random random=new Random();
        posFood.x=random.nextInt(bwide);
        posFood.y=random.nextInt(bhigh);
    }
    public Point getPosFood()
    {
        return posFood;
    }
    void drawable(Canvas canvas, Paint paint)
    {
        canvas.drawBitmap(foodBit, posFood.x *bsize, posFood.y * bsize, paint);
    }
}
