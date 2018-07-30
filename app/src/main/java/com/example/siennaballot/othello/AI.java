package com.example.siennaballot.othello;

public class AI {
    int[] Xmove;
    int[] Ymove;
    int bestX;
    int bestY;
    int maxValue;
    int minValue;
    int depth;

    public AI(){
        Xmove = new int[64];
        Ymove = new int[64];
        maxValue = -9999;
        minValue = 9999;
        depth = 5;
    }

    public void possible_moves(int board[][], int turn) {
        int x = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (legal(board, i, j, turn, false)){
                    Xmove[x] = i;
                    Ymove[x] = j;
                    x++;
                }
            }
        }
    }

    public int heuristic(int board[][], int turn) {
        int opponent = 1;
        if (turn == 1) {
            opponent = 2;
        }
        int score = 0;
        int oppo_score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == turn) {
                    score++;
                }
                else if (board[i][j] == opponent) {
                    oppo_score++;
                }
            }
        }
        return score - oppo_score;
    }

    public void decision(int board[][], int turn) {

    }

    public void search(int board[][], int turn, int originalTurn, int depth) {


    }

    public boolean legal(int board[][], int x, int y, int c, boolean move) {
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
                    if (posX < 0  || posY < 0 || posX > 8 || posY > 8) { continue; }

                    curr = board[posY][posX];
                    valid = false;

                    if (curr == 0 || curr == -1 || curr == c) { continue; }

                    while(!valid) {
                        posX += i;
                        posY += j;
                        if (posX < 0  || posY < 0 || posX > 8 || posY > 8) { continue; }
                        curr = board[posY][posX];

                        if (curr == c) {
                            valid = true;
                            legal = true;

                            if (move) {
                                posX -= i;
                                posY -= j;
                                if (posX < 0  || posY < 0 || posX > 8 || posY > 8) { continue; }
                                curr = board[posY][posX];

                                while (curr != 0) {
                                    board[posY][posX] = c;
                                    posX -= i;
                                    posY -= j;
                                    if (posX < 0  || posY < 0 || posX > 8 || posY > 8) { continue; }
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
    }

}
