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

import java.util.ArrayList;


public class BoardView extends SurfaceView implements SurfaceHolder.Callback {

    int[][] board = new int[8][8];
    boolean start;
    boolean pvp, avp, ava;
    int player1;
    int player2;
    int width;
    int height;
    int row1, col1;
    int black, white;
    int square;
    int radius;
    int turn;
    //ArrayList<Piece> black;
    //ArrayList<Piece> white;

    AI ai;

    Bitmap board_pic;
    Bitmap background;

    public BoardView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        //black = new ArrayList<>();
        //white = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }

        //set starting pieces
        board[3][3] = 2;
        //white.add(new Piece(3, 3, 2));
        board[4][4] = 2;
        //white.add(new Piece(4, 4, 2));
        board[3][4] = 1;
        //black.add(new Piece(3, 4, 1));
        board[4][3] = 1;
        //black.add(new Piece(4, 3, 1));
        black = 2;
        white = 2;

        player1 = 1;  //player 1 (user) automatically black
        player2 = 2;  //player 2 (AI) automatically white
        turn = 1;     //black gets first turn

        start = true;
        avp = true;

        ai = new AI();

        board_pic = BitmapFactory.decodeResource(getResources(), R.drawable.board);
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        height = getHeight();
        width = getWidth();
        square = width / 8;
        radius = (width / 16) - 10;
        Rect dst = new Rect();
        c.drawColor(Color.WHITE);

        dst.set(0, square, width, square*9);
        c.drawBitmap(board_pic, null, dst, null);

        /*if (start) {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            TextPaint text = new TextPaint();
            text.setColor(Color.WHITE);
            text.setTextSize(60);
            text.setTextAlign(Paint.Align.CENTER);

            //Player v Player button
            dst.set(square, square*5, width-square, square*6);
            c.drawRect(dst, paint);
            c.drawText("Player v Player", width/2, (square*5)+(square/2) , text);

            //Player v AI button
            dst.set(square, square*7, width-square, square*8);
            c.drawRect(dst, paint);
            c.drawText("Player v Player", width/2, (square*7)+(square/2) , text);

            //AI v AI button
            dst.set(square, square*9, width-square, square*10);
            c.drawRect(dst, paint);
            c.drawText("Player v Player", width/2, (square*9)+(square/2) , text);

            //choose game type (ex. player v player; AI v AI
        }
        else {*/

        Paint black_paint = new Paint();
        black_paint.setColor(Color.BLACK);

        Paint white_paint = new Paint();
        white_paint.setColor(Color.WHITE);

        black = 0;
        white = 0;

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (board[y][x] == 1) {
                    c.drawCircle((x*square)+(square/2), ((y+1)*square)+(square/2), radius, black_paint);
                    black++;
                }
                else if (board[y][x] == 2) {
                    c.drawCircle((x*square)+(square/2), ((y+1)*square)+(square/2), radius, white_paint);
                    white++;
                }
            }
        }

        //if (black == 0) { turn = 2; }
        //if (white == 0) { turn = 1; }
        //if (black+white == 64) { turn = -1; }

        //number of white pieces
        String whitestring = Integer.toString(white);
        TextPaint whitepieces = new TextPaint();
        whitepieces.setColor(Color.BLACK);
        whitepieces.setTextSize(60);
        whitepieces.setTextAlign(Paint.Align.LEFT);
        c.drawText("WHITE: "+whitestring, width/10, square-20, whitepieces);

        //number of black pieces
        String blackstring = Integer.toString(black);
        TextPaint blackpieces = new TextPaint();
        blackpieces.setColor(Color.BLACK);
        blackpieces.setTextSize(60);
        blackpieces.setTextAlign(Paint.Align.RIGHT);
        c.drawText("BLACK: "+blackstring, width*8/10, square-20, blackpieces);

//        if (turn == 2 && avp) {
//            //Thread t = new AI(board, turn);
//            //t.start();
//            ai.best_move(board, turn);
//            if (ai.num_moves[0] == 0) { turn = 1; }
//            else {
//                int x = ai.bestX;
//                int y = ai.bestY;
//                System.out.println(y + ", " + x);
//                if (legal(x, y, turn, true))
//                    board[y][x] = turn;
//                turn = 1;
//                ai.bestX = 10;
//                ai.bestY = 10;
//                //invalidate();
//            }
//        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setWillNotDraw(false);
        System.out.println("Surface created");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) { }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        System.out.println("In onTouchEvent");
        float x = e.getX();
        float y = e.getY();

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }

        //get square at upward motion event
        if (e.getAction() == MotionEvent.ACTION_UP) {
            /*if (start) {
                if (x > square && x < width-square && y > square*5 && y < square*6) {
                    pvp = true;
                    start = false;
                }
                else if (x > square && x < width-square && y > square*7 && y < square*8) {
                    avp = true;
                    start = false;
                }
                else if (x > square && x < width-square && y > square*9 && y < square*10) {
                    ava = true;
                    start = false;
                }

            }
            else {*/
            if (avp && turn == 1) {
                row1 = (int) (y / square) - 1;
                col1 = (int) x / square;
                System.out.println(row1 + ", " + col1);
                boolean move = true;
                //check legality of move
                if (legal(col1, row1, turn, move)) {
                    System.out.println("legal move");
                    board[row1][col1] = turn;
                    turn = 2;
//                    if (turn == 1) {
//                        turn = 2;
//                    } else if (turn == 2) {
//                        turn = 1;
//                    }
                    if (turn == 2 && avp) {
                        //Thread t = new AI(board, turn);
                        //t.start();
                        ai.best_move(board, turn);
                        if (ai.num_moves[0] == 0) { turn = 1; }
                        else {
                            System.out.println(ai.bestY + ", " + ai.bestX);
                            if (legal(ai.bestX, ai.bestY, turn, true))
                                board[ai.bestY][ai.bestX] = turn;
                            turn = 1;
                            ai.bestX = 10;
                            ai.bestY = 10;
                            //invalidate();
                        }
                    }
                    invalidate();
                } else {
                    System.out.println("illegal move");
                }
                return true;
            }
        }
        return false;
    }

    public boolean legal(int x, int y, int c, boolean move) {
        boolean legal = false;

        if (board[y][x] == 0) {
            int posX;
            int posY;
            boolean valid;
            int curr;

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i==0 && j==0) { continue; }
                    posX = x + i;
                    posY = y + j;
                    if (posX < 0  || posY < 0 || posX > 7 || posY > 7) { continue; }

                    curr = board[posY][posX];
                    valid = false;

                    if (curr == 0 || curr == -1 || curr == c) { continue; }

                    while(!valid) {
                        posX += i;
                        posY += j;
                        if (posX < 0  || posY < 0 || posX > 7 || posY > 7) { break; }
                        curr = board[posY][posX];

                        if (curr == c) {
                            valid = true;
                            legal = true;

                            if (move) {
                                posX -= i;
                                posY -= j;
                                if (posX < 0  || posY < 0 || posX > 7 || posY > 7) { break; }

                                while (!(posX == x && posY == y)) {//curr != 0) {
                                    board[posY][posX] = c;
                                    posX -= i;
                                    posY -= j;
                                    if (posX < 0  || posY < 0 || posX > 7 || posY > 7) { break; }
                                }
                            }
                            else
                                return true;
                        }
                        else if (curr == -1 || curr == 0) {
                            valid = true;
                        }
                    }
                }
            }
        }
        return legal;
    }
}
