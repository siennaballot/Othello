package com.example.siennaballot.othello;

public class AI {
    int maxValue;
    int minValue;

    public AI(){

    }
    //data structure of possible enemy moves
    //data
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

    public int search() {

        return 0;
    }



    private int maxValue() {
        return 0;
    }

    private int minValue() {
        return 0;
    }


}
