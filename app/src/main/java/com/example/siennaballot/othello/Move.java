package com.example.siennaballot.othello;

public class Move {
    int row = -1;
    int column = -1;
    int endR[] = new int[8];
    int endY[] = new int[8];
    int color = 0;
    int points = 0;
    boolean legal = false;

    public Move() {
        for (int i = 0; i < 8; i++) {
            endR[i] = -1;
            endY[i] = -1;
        }
    }
}
