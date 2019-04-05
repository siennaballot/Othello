package com.example.siennaballot.othello;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    //private Thread th;
    private BoardView bv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getSupportActionBar().hide();
        bv = new BoardView(this);
        /*th = new Thread(new BoardView(this));
        th.start();*/
        setContentView(bv);
    }
}
