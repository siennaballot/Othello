package com.example.siennaballot.othello;

public class Piece {
    private int x;
    private int y;
    private int color;

    public Piece (int w, int h, int c) {
        y = w;
        x = h;
        color = c;
    }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public void setColor(int color) { this.color = color; }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getColor() { return color; }
}
