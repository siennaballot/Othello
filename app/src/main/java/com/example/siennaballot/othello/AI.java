package com.example.siennaballot.othello;

public class AI {
    int[] num_moves;
    int[][] Xmove;
    int[][] Ymove;
    int bestX;
    int bestY;
    int bestVal;
    int maxDepth;

    public AI(){
        num_moves = new int[5];
        Xmove = new int[5][64];
        Ymove = new int[5][64];
        maxDepth = 5;
    }

    public void possible_moves(int board[][], int turn, int depth) {
        int x = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (legal(board, i, j, turn, false)){
                    Xmove[depth][x] = i;
                    Ymove[depth][x] = j;
                    x++;
                    num_moves[depth] = x;
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

    public void best_move(int board[][], int turn) {
        for (int i = 0; i < 5; i++) { num_moves[i] = 0; }
        int opponent = 1;
        if (turn == 1) { opponent = 2; }
        //get all possible moves
        possible_moves(board, turn, 0);
        //if no possible moves, then move not possible
        if (num_moves[0] == 0) {
            bestX = 10;
            bestY = 10;
        }
        else {
            bestVal = -9999;
            bestX = Xmove[0][0];
            bestY = Ymove[0][0];
            for (int i = 0; i < num_moves[0]; i++) {
                int[][] temp = new int[8][8];       //temporary board to apply move
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        temp[x][y] = board[x][y];
                    }
                }
                legal(temp, Xmove[0][i], Ymove[0][i], turn, true);
                int val = search(temp, opponent, turn, 1);
                if ((Xmove[0][i] == 0 && Ymove[0][i] == 0) || (Xmove[0][i] == 0 && Ymove[0][i] == 7) || (Xmove[0][i] == 7 && Ymove[0][i] == 0) || (Xmove[0][i] == 7 && Ymove[0][i] == 7)) {
                    val += 1000;
                }
                else if (board[0][0] != turn && ((Xmove[0][i] == 0 && Ymove[0][i] == 1) || (Xmove[0][i] == 1 && Ymove[0][i] == 0) || (Xmove[0][i] == 1 && Ymove[0][i] == 1))) {
                    val -= 100;
                }
                else if (board[7][7] != turn && ((Xmove[0][i] == 7 && Ymove[0][i] == 6) || (Xmove[0][i] == 6 && Ymove[0][i] == 7) || (Xmove[0][i] == 6 && Ymove[0][i] == 6))) {
                    val -= 100;
                }
                else if (board[0][7] != turn && ((Xmove[0][i] == 0 && Ymove[0][i] == 6) || (Xmove[0][i] == 1 && Ymove[0][i] == 6) || (Xmove[0][i] == 1 && Ymove[0][i] == 7))) {
                    val -= 100;
                }
                else if (board[7][0] != turn && ((Xmove[0][i] == 6 && Ymove[0][i] == 0) || (Xmove[0][i] == 6 && Ymove[0][i] == 1) || (Xmove[0][i] == 7 && Ymove[0][i] == 1))) {
                    val -= 100;
                }
                else if (Xmove[0][i] == 0 || Xmove[0][i] == 7 || Ymove[0][i] == 0 || Ymove[0][i] == 7) {
                    val += 100;
                }
                else if (Xmove[0][i] == Ymove[0][i]) {
                    val += 10;
                }
                if (val > bestVal) {
                    bestVal = val;
                    bestX = Xmove[0][i];
                    bestY = Ymove[0][i];
                }
            }
        }
    }

    public int search(int board[][], int turn, int originalTurn, int depth) {
        if (game_over(board) || depth >= maxDepth) { return heuristic(board, originalTurn); }
        int opponent = 1;
        if (turn == 1) { opponent = 2; }
        possible_moves(board, turn, depth);
        if (num_moves[depth] == 0) {
            return search(board, opponent, originalTurn, depth+1);
        }
        else {
            int bestcurrVal = -9999;
            if (originalTurn != turn) { bestcurrVal = 9999; }
            for (int i = 0; i < num_moves[depth]; i++) {
                int[][] temp = new int[8][8];
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        temp[x][y] = board[x][y];
                    }
                }
                legal(temp, Xmove[depth][i], Ymove[depth][i], turn, true);
                int val = search(temp, opponent, originalTurn, depth+1);
                if (turn == originalTurn) {
                    if (val > bestcurrVal) { bestcurrVal = val; }
                }
                else if (turn != originalTurn) {
                    if (val < bestcurrVal) { bestcurrVal = val; }
                }
            }
            return bestcurrVal;
        }
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
                    if (posX < 0  || posY < 0 || posX > 7 || posY > 7) { continue; }

                    curr = board[posY][posX];
                    valid = false;

                    if (curr == 0 || curr == -1 || curr == c) { continue; }

                    while(!valid) {
                        posX += i;
                        posY += j;
                        if (posX < 0  || posY < 0 || posX > 7 || posY > 7) { continue; }
                        curr = board[posY][posX];

                        if (curr == c) {
                            valid = true;
                            legal = true;

                            if (move) {
                                posX -= i;
                                posY -= j;
                                if (posX < 0  || posY < 0 || posX > 7 || posY > 7) { continue; }
                                curr = board[posY][posX];

                                while (curr != 0) {
                                    board[posY][posX] = c;
                                    posX -= i;
                                    posY -= j;
                                    if (posX < 0  || posY < 0 || posX > 7 || posY > 7) { continue; }
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

    public boolean game_over(int[][] board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
                else { continue; }
            }
        }
        return true;
    }
}
