package com.example.mdgproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameView extends SurfaceView implements Runnable{

    private Thread thread;
    private Point screenSize;
    boolean threadRun;
    FirebaseDatabase db;
    DatabaseReference reference, reference2;
    private final int noblock=25;
    private int blockSize;
    private int blockhigh;
    private GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    private float absx, absy;
    static final int mindist=50;
    private Food food;
    private Person person;
    private Bitmap backimage;
    private SurfaceHolder surfaceHolder;
    private Boolean paused=true;
    private Boolean AddScore=true;
    private Canvas canvas;
    private int s=300;
    private int t=0;
    private int highScore=0;
    private long nxtfrmtime;
    private Paint paint=new Paint();

    GameView(Context context, Point screenSize)
    {
        super(context);
        this.screenSize=screenSize;
        blockSize=screenSize.x/noblock;
        blockhigh=screenSize.y/blockSize;
        backimage= BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
        backimage=Bitmap.createScaledBitmap(backimage, screenSize.x+100,screenSize.y+100,false);
        food = new Food(context, blockhigh, noblock, blockSize);
        person= new Person(context, new Point(noblock, blockhigh), blockSize);
        gestureDetector= new GestureDetector(getContext(), getGestureListener());
        surfaceHolder= getHolder();
        newGame();
    }

    void newGame()
    {
        s=600;
        food.setFoodPos();
        person.moveSnake();
        nxtfrmtime=System.currentTimeMillis();
    }

    @Override
    public void run() {

        while(threadRun){
            if(updatereq())
            {
                if(!paused)
                {
                    update();
                }
                draw();
            }

        }
    }
    private Boolean updatereq()
    {
        if(System.currentTimeMillis()>nxtfrmtime) {
            if(s<700)
            {
            nxtfrmtime += 100;
            if (!paused)
            {
                s -= 5;
                t++;
            }
            }
            else
            {
                nxtfrmtime += 200;
                if (!paused)
                {
                    s -= 10;
                    t=t+2;
                }
            }

            return true;
        }
        return false;
    }
    private void update()
    {

        person.moveSnake();
        if(s<=5)
        {
            paused = true;
            AddScore=true;


        }

        if(person.haveSnakeEatenApple(food.getPosFood()))
        {

            s=s+food.getKcal();
            food.setFoodPos();
        }
    }
    private void draw()
    {
        if(surfaceHolder.getSurface().isValid())
        {
            canvas=surfaceHolder.lockCanvas();
            canvas.drawBitmap(backimage,0,0,paint);
            if(!paused)
            {
                food.drawable(canvas, paint);
                person.drawSnake(canvas, paint);
                paint.setTextSize(80);
                if(s>700)
                    paint.setColor(Color.parseColor("#FF0000"));
                else paint.setColor(Color.parseColor("#FF000000"));
                canvas.drawText(""+ s,1,100,paint);
                canvas.drawText(""+ t,screenSize.x-200,100,paint);
                paint.setColor(Color.parseColor("#FF000000"));
            }
            else
            {
                db=FirebaseDatabase.getInstance();
                reference=db.getReference("HighScore");
                reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        DataSnapshot dataSnapshot=task.getResult();
                        String str=String.valueOf(dataSnapshot.getValue());
                        highScore=Integer.parseInt(str);
                    }
                });
                reference2=db.getReference("Other Scores");



                paint.setTextSize(250);
                paint.setColor(Color.BLACK);
                canvas.drawText("TAP TO PLAY", 400, 600, paint);
                paint.setColor(Color.BLACK);
                paint.setTextSize(80);
                canvas.drawText(""+ s,1,100,paint);
                canvas.drawText(""+ t,screenSize.x-200,100,paint);
                if(AddScore&&t!=0)
                {
                    reference2.push().setValue(t);
                    AddScore=false;
                }
                if(t>=highScore&&highScore!=0) {
                    paint.setTextSize(150);
                    canvas.drawText("NEW HIGHSCORE!", 550, 300, paint);
                    reference.setValue(Integer.toString(t));
                }
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private GestureDetector.OnGestureListener getGestureListener()
    {
        return new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                if(paused)
                {
                    paused=false;
                    s=600;
                    t=0;
                    person.reset();
                }
                return false;
            }

            @Override
            public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(@NonNull MotionEvent dEvent, @NonNull MotionEvent uEvent, float v, float v1) {
                x1=dEvent.getX();
                y1=dEvent.getY();
                x2=uEvent.getX();
                y2=uEvent.getY();
                absx=Math.abs(x2-x1);
                absy=Math.abs(y2-y1);
                if(absx>mindist||absy>mindist)
                {
                    if(absy>absx)
                    {
                        if(y1>y2)
                            person.setMovingDirection(Heading.UP);
                        else person.setMovingDirection(Heading.DOWN);
                    }
                    else if(absx>mindist)
                    {
                        if(x1>x2)
                            person.setMovingDirection(Heading.LEFT);
                        else person.setMovingDirection(Heading.RIGHT);
                    }
                }
                return false;
            }
        };
    }
    public void onResume()
    {
        threadRun=true;
        thread=new Thread(this);
        thread.start();
    }
    public  void onPause()
    {
        threadRun=false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
