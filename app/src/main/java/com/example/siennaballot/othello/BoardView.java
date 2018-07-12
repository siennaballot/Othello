package com.example.siennaballot.othello;

import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextPaint;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;


public class BoardView extends SurfaceView implements SurfaceHolder.Callback {

    int[][] board = new int[8][8];
    boolean start;
    boolean p1_move;
    boolean p2_move;
    int player1;
    int player2;


    public BoardView(Context context) {
        super(context);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }
        board[4][4] = 1;
        board[5][5] = 1;
        board[4][5] = 2;
        board[5][4] = 2;

        player1 = 1;  //player 1 automatically white
        player2 = 2;  //player 2 automatically black

        start = true;
        p1_move = true;  //player 1 gets first move
        p2_move = false;
    }

    @Override
    public void onDraw(Canvas c) {
        if (start) {
            //choose game type (ex. player v player; AI v AI
        }
        else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == 0) {

                    }
                }
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
        System.out.println("Surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        System.out.println("In onTouchEvent");
        float width = getWidth();
        float height = getHeight();
        float x = e.getX();
        float y = e.getY();

        /*if (e.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }*/
        //move at spot where upward motion event
        if (e.getAction() == MotionEvent.ACTION_UP) {
            if (legal()) {
                return true;
            }
        }
        return false;
    }

    public boolean legal() {

        return false;
    }
}
