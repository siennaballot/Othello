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

    int[][] board = new int[9][9];
    boolean start;
    boolean pvp, avp, ava;
    boolean p1_move;
    boolean p2_move;
    int player1;
    int player2;
    int width;
    int height;
    int row1, col1, row2, col2;
    //int row_height;
    //int col_width;
    int square;
    int radius;
    int turn;
    ArrayList<Piece> black;
    ArrayList<Piece> white;

    AI ai;

    Bitmap board_pic;
    Bitmap background;

    public BoardView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);

        black = new ArrayList<>();
        white = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }

        //set starting pieces
        board[3][3] = 2;
        white.add(new Piece(3, 3, 2));
        board[4][4] = 2;
        white.add(new Piece(4, 4, 2));
        board[3][4] = 1;
        black.add(new Piece(3, 4, 1));
        board[4][3] = 1;
        black.add(new Piece(4, 3, 1));

        player1 = 1;  //player 1 automatically black
        player2 = 2;  //player 2 automatically white
        turn = 1;     //black gets first turn

        start = true;
        //p1_move = true;  //player 1 gets first move
        //p2_move = false;
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
            //number of white pieces
            String whitestring = Integer.toString(white.size());
            TextPaint whitepieces = new TextPaint();
            whitepieces.setColor(Color.BLACK);
            whitepieces.setTextSize(60);
            whitepieces.setTextAlign(Paint.Align.LEFT);
            c.drawText("WHITE: "+whitestring, width/10, square-20, whitepieces);

            //number of black pieces
            String blackstring = Integer.toString(black.size());
            TextPaint blackpieces = new TextPaint();
            blackpieces.setColor(Color.BLACK);
            blackpieces.setTextSize(60);
            blackpieces.setTextAlign(Paint.Align.RIGHT);
            c.drawText("BLACK: "+blackstring, width*8/10, square-20, blackpieces);

            Paint black_paint = new Paint();
            black_paint.setColor(Color.BLACK);

            Paint white_paint = new Paint();
            white_paint.setColor(Color.WHITE);

            //display game board


            //c.drawCircle();
            /*for  (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] == 0) {

                    }
                }
            }*/

            //draw white pieces on board from arraylist
            for (int i = 0; i < white.size(); i++) {
                int white_x = ((white.get(i).getX()) * square) + (square/2);
                int white_y = ((white.get(i).getY()+1) * square) + (square/2);
                c.drawCircle(white_x, white_y, radius, white_paint);
            }

            //draw black pieces on board from arraylist
            for (int i = 0; i < black.size(); i++) {
                int black_x = ((black.get(i).getX()) * square) + (square/2);
                int black_y = ((black.get(i).getY()+1) * square) + (square/2);
                c.drawCircle(black_x, black_y, radius, black_paint);
            }

        if (turn == 2 && avp) {
                ai.search();
        }

        invalidate();
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

        /*if (e.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }*/

        //get square at upward motion event
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
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
                row1 = (int) (y / square) - 1;
                col1 = (int) x / square;
                System.out.println(row1+", "+col1);
                boolean move = true;
                //check legality of move
                if (legal(col1, row1, turn, move)) {
                    System.out.println("legal move");
                    board[row1][col1] = turn;
                    if (turn == 1) { turn = 2; }
                    else if (turn == 2) { turn = 1; }
                    //switch pieces
                    //redraw board if move is legal
                    white.clear();
                    black.clear();
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (board[i][j] == 1) {
                                black.add(new Piece(i,j,1));
                            }
                            else if (board[i][j] == 2) {
                                white.add(new Piece(i,j,2));
                            }
                        }
                    }

                    invalidate();
                }
                else {
                    System.out.println("illegal move");
                }
                return true;
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
                    posX = x + i;
                    posY = y + j;
                    if (posX < 0  || posY < 0 || posX > 8 || posY > 8) {
                        continue;
                    }

                    curr = board[posY][posX];
                    valid = false;

                    if (curr == 0 || curr == -1 || curr == c) {
                        continue;
                    }

                    while(!valid) {
                        posX += i;
                        posY += j;
                        if (posX < 0  || posY < 0 || posX > 8 || posY > 8) {
                            continue;
                        }
                        curr = board[posY][posX];

                        if (curr == c) {
                            valid = true;
                            legal = true;

                            if (move) {
                                posX -= i;
                                posY -= j;
                                if (posX < 0  || posY < 0 || posX > 8 || posY > 8) {
                                    continue;
                                }
                                curr = board[posY][posX];

                                while (curr != 0) {
                                    board[posY][posX] = c;
                                    posX -= i;
                                    posY -= j;
                                    if (posX < 0  || posY < 0 || posX > 8 || posY > 8) {
                                        continue;
                                    }
                                    curr = board[posY][posX];

                                }
                            }
                        }
                        else if (curr == -1 || curr == 0) {
                            valid = true;
                        }
                    }
                }
            }
        }
        return legal;
        //out of bounds = not legal
        /*if (x > 7 || x < 0 || y > 7 || y < 0) {
            return false;
        }
        //not empty square = not legal
        if (board[y][x] != 0) {
            return false;
        }
        //if player 1 move, check if other white pieces are in same row or column
        //check if black pieces exist between white
        if (p2_move) {
            if (board[y+1][x] != 2 && board[y-1][x] != 2 && board[y][x+1] != 2 && board[y][x-1] != 2 &&
                board[y+1][x+1] != 2 && board[y-1][x+1] != 2 && board[y+1][x-1] != 2 && board[y-1][x-1] != 2) {
                return false;
            }
            else if (board[y+1][x] == 2) {
                for (int i = 2; i < 8; i++) {
                    if (board[y+i][x] == 0 || y+i > 7) {
                        return false;
                    }
                    else if (board[y+i][x] == 1) {
                        row2 = y+i;
                        col2 = x;
                        move();
                        return true;
                    }
                }
            }
            else if (board[y-1][x] == 2) {
                for (int i = 2; i < 8; i++) {
                    if (board[y-i][x] == 0 || y-i < 0) {
                        return false;
                    }
                    else if (board[y-i][x] == 1) {
                        row2 = y-i;
                        col2 = x;
                        move();
                        return true;
                    }
                }
            }
            else if (board[y][x+1] == 2) {
                for (int i = 2; i < 8; i++) {
                    if (board[y][x+i] == 0 || x+i > 7) {
                        return false;
                    }
                    else if (board[y][x+i] == 1) {
                        row2 = y;
                        col2 = x+i;
                        move();
                        return true;
                    }
                }
            }
            else if (board[y][x-1] == 2) {
                for (int i = 2; i < 8; i++) {
                    if (board[y][x-i] == 0 || x-i < 0) {
                        return false;
                    }
                    else if (board[y][x-i] == 1) {
                        row2 = y;
                        col2 = x-i;
                        move();
                        return true;
                    }
                }
            }
        }
        else if (p1_move) {
            if (board[y+1][x] != 1 && board[y-1][x] != 1 && board[y][x+1] != 1 && board[y][x-1] != 1) {
                return false;
            }
            else if (board[y+1][x] == 1) {
                for (int i = 2; i < 8; i++) {
                    if (board[y+i][x] == 0 || y+i > 7) {
                        return false;
                    }
                    else if (board[y+i][x] == 2) {
                        row2 = y+i;
                        col2 = x;
                        move();
                        return true;
                    }
                }
            }
            else if (board[y-1][x] == 1) {
                for (int i = 2; i < 8; i++) {
                    if (board[y-i][x] == 0 || y-i < 0) {
                        return false;
                    }
                    else if (board[y-i][x] == 2) {
                        row2 = y-i;
                        col2 = x;
                        move();
                        return true;
                    }
                }
            }
            else if (board[y][x+1] == 1) {
                for (int i = 2; i < 8; i++) {
                    if (board[y][x+i] == 0 || x+i > 7) {
                        return false;
                    }
                    else if (board[y][x+i] == 2) {
                        row2 = y;
                        col2 = x+i;
                        move();
                        return true;
                    }
                }
            }
            else if (board[y][x-1] == 1) {
                for (int i = 2; i < 8; i++) {
                    if (board[y][x-i] == 0 || x-i < 0) {
                        return false;
                    }
                    else if (board[y][x-i] == 2) {
                        row2 = y;
                        col2 = x-i;
                        move();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void move() {
        if (p1_move) {
            if (row1 == row2) {
                if (col1 > col2) {
                    for (int i = 1; col2 + i <= col1; i++) {
                        board[col2+i][row1] = 1;
                    }
                }
                else if (col2 > col1) {
                    for (int i = 1; col1 + i <= col2; i++) {
                        board[col1+i][row1] = 1;
                    }
                }
            }
            else if (col1 == col2) {
                if (row1 > row2) {
                    for (int i = 1; row2 + i <= row1; i++) {
                        board[col2][row2 + i] = 1;
                    }
                }
                else if (row2 > row1) {
                    for (int i = 1; row1 + i <= row2; i++) {
                        board[col2][row1 + i] = 1;
                    }
                }
            }
        }
        else if (p2_move) {
            if (row1 == row2) {
                if (col1 > col2) {
                    for (int i = 1; col2 + i <= col1; i++) {
                        board[col2+i][row1] = 2;
                    }
                }
                else if (col2 > col1) {
                    for (int i = 1; col1 + i <= col2; i++) {
                        board[col1+i][row1] = 2;
                    }
                }
            }
            else if (col1 == col2) {
                if (row1 > row2) {
                    for (int i = 1; row2 + i <= row1; i++) {
                        board[col2][row2 + i] = 2;
                    }
                }
                else if (row2 > row1) {
                    for (int i = 1; row1 + i <= row2; i++) {
                        board[col2][row1 + i] = 2;
                    }
                }
            }
        }
        white.clear();
        black.clear();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) {
                    white.add(new Piece(i,j,1));
                }
                else if (board[j][i] == 2) {
                    black.add(new Piece(i,j,1));
                }
            }
        }*/
    }
}
