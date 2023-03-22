package com.example.mdgproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends Activity {

    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display= getWindowManager().getDefaultDisplay();
        Point size= new Point();
        display.getSize(size);
        gameView=new GameView(this, size);
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        gameView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        gameView.onResume();
        super.onResume();
    }
}