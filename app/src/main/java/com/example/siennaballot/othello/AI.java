package com.example.siennaballot.othello;

public class AI {
    int[] num_moves;            // number of possible moves at each depth in search
    int[][] Xmove;              // x coordinate for each possible move at each depth in search
    int[][] Ymove;              // y coordinate for each possible move at each depth in search
    int bestX;                  // x coordinate of best possible move
    int bestY;                  // y coordinate of best possible move
    int bestVal;
    int maxDepth;               // max depth to search

    // constructor for AI objects
    // initializes the max search depth
    public AI(){
        maxDepth = 5;
    }

    /*public void run() {
        best_move(board, turn);
    }*/

    public void possible_moves(int board[][], int turn, int depth) {
        // for each empty space on the board, if the move is legal,
        // add to moves list for that depth
        int x = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 0 && legal(board, j, i, turn, false)) {
                    Xmove[depth][x] = j;
                    Ymove[depth][x] = i;
                    x++;
                    num_moves[depth] = x;
                }
            }
        }
    }

    public int heuristic(int board[][], int turn) {
        // for the given turn (player) and board layout, check the score difference

        int opponent = 1;       // set opponent to 1 (black)
        if (turn == 1) {        // if the given player is 1, change opponent to 2 9(white)
            opponent = 2;
        }

        int score = 0;              // current player score
        int oppo_score = 0;         // current opponent score

        // for each position on the board count the number of black and white pieces
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

        // return the difference between the black and white score
        return score - oppo_score;
    }

    public void best_move(int board[][], int turn) {
        // find the x and y coordinates for the move that returns the best value
        // after looking recursively through 5 levels of all possible paths
        num_moves = new int[maxDepth];
        Xmove = new int[maxDepth][64];
        Ymove = new int[maxDepth][64];
        for (int i = 0; i < maxDepth; i++) { num_moves[i] = 0; }
        for (int i = 0; i < maxDepth; i++) {
            for (int j = 0; j < 64; j++) {
                Xmove[i][j] = 0;
                Ymove[i][j] = 0;
            }
        }
        int opponent = 1;
        if (turn == 1) { opponent = 2; }

        //get all possible moves
        possible_moves(board, turn, 0);

        //if no possible moves, then return illegal move
        if (num_moves[0] == 0) {
            bestX = 10;
            bestY = 10;
        }
        else {
            bestVal = -9999;
            bestX = Xmove[0][0];
            bestY = Ymove[0][0];
            for (int i = 1; i < num_moves[0]; i++) {
                int[][] temp = new int[8][8];       //temporary board to apply move
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        temp[x][y] = board[x][y];
                    }
                }
                legal(temp, Xmove[0][i], Ymove[0][i], turn, true);
                int val = search(temp, opponent, turn, 1);

                // if x and y coordinates are a corner space, make this move a priority
                if ((Xmove[0][i] == 0 && Ymove[0][i] == 0) || (Xmove[0][i] == 0 && Ymove[0][i] == 7) || (Xmove[0][i] == 7 && Ymove[0][i] == 0) || (Xmove[0][i] == 7 && Ymove[0][i] == 7)) {
                    val += 1000;
                }

                // if x and y coordinates are spaces surrounding a corner space make the move less of a priority
                else if (board[0][0] != turn && (Xmove[0][i] == 1 || Ymove[0][i] == 1 || Xmove[0][i] == 6 && Ymove[0][i] == 6)) {
                    val -= 100;
                }
//                else if (board[0][0] != turn && ((Xmove[0][i] == 0 && Ymove[0][i] == 1) || (Xmove[0][i] == 1 && Ymove[0][i] == 0) || (Xmove[0][i] == 1 && Ymove[0][i] == 1))) {
//                    val -= 100;
//                }
//                else if (board[7][7] != turn && ((Xmove[0][i] == 7 && Ymove[0][i] == 6) || (Xmove[0][i] == 6 && Ymove[0][i] == 7) || (Xmove[0][i] == 6 && Ymove[0][i] == 6))) {
//                    val -= 100;
//                }
//                else if (board[0][7] != turn && ((Xmove[0][i] == 0 && Ymove[0][i] == 6) || (Xmove[0][i] == 1 && Ymove[0][i] == 6) || (Xmove[0][i] == 1 && Ymove[0][i] == 7))) {
//                    val -= 100;
//                }
//                else if (board[7][0] != turn && ((Xmove[0][i] == 6 && Ymove[0][i] == 0) || (Xmove[0][i] == 6 && Ymove[0][i] == 1) || (Xmove[0][i] == 7 && Ymove[0][i] == 1))) {
//                    val -= 100;
//                }

                // if x and y coordinates are an edge space, add 10 to prioritize
                else if (Xmove[0][i] == 0 || Xmove[0][i] == 7 || Ymove[0][i] == 0 || Ymove[0][i] == 7) {
                    val += 10;
                }

                // x and y coordinate are a diagonal space, add 5 to prioritize
                else if (Xmove[0][i] == Ymove[0][i] || Xmove[0][i] + Ymove[0][i] == 7) {
                    val += 5;
                }

                // if heuristic value (plus any priority value) is less than current best value
                // then make this move the best possible move
                if (val > bestVal) {
                    bestVal = val;
                    bestX = Xmove[0][i];
                    bestY = Ymove[0][i];
                }
            }
        }
    }

    public int search(int board[][], int turn, int originalTurn, int depth) {
        // recursively searches from current depth to find the most profitable move for current player

        // if the game is over or the max depth reached, return the heuristic value of the current board
        if (game_over(board) || depth >= maxDepth) { return heuristic(board, originalTurn); }
        int opponent = 1;
        if (turn == 1) { opponent = 2; }

        // get all possible moves for the current board at the current search depth
        possible_moves(board, turn, depth);

        // if there are no possible moves for this depth, go to next search depth
        // with same player turn settings (skip AI turn since no possible moves)
        if (num_moves[depth] == 0) {
            return search(board, opponent, originalTurn, depth+1);
        }

        else {
            int bestcurrVal = -9999;                            // best move for AI
            if (originalTurn != turn) { bestcurrVal = 9999; }   // find best move for opponent
            for (int i = 0; i < num_moves[depth]; i++) {

                // temp board to test possible moves
                int[][] temp = new int[8][8];
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        temp[x][y] = board[x][y];
                    }
                }
                // update board if a possible move is made
                legal(temp, Xmove[depth][i], Ymove[depth][i], turn, true);

                // recursive call for search on updated board, switching players, incrementing search depth
                int val = search(temp, opponent, originalTurn, depth+1);

                // if the AIs turn and returned value from recursive is better than current best val, update current best val
                if (turn == originalTurn && val > bestcurrVal) {
                   bestcurrVal = val;
                }
                // if the opponents turn and returned value from recursive is better than current best val, update current best val
                else if (turn != originalTurn && val < bestcurrVal) {
                    bestcurrVal = val;
                }
            }
            // best val is most positive value for AI and most negative value for opponent
            return bestcurrVal;
        }
    }

    public boolean legal(int board[][], int x, int y, int c, boolean move) {
        // checks legality of given move
        // if parameter move = true, then also updates the board layout with the new move

        int posX, posY, curr;
        boolean valid, legal = false;

        // check surrounding spaces of given move
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) { continue; }
                posX = x + i;
                posY = y + j;

                // if out of bounds, keep checking
                if (posX < 0 || posY < 0 || posX > 7 || posY > 7) { continue; }

                curr = board[posY][posX];
                valid = false;

                // if neighboring space is empty or same as original, keep checking
                if (curr == 0 || curr == -1 || curr == c) { continue; }

                while (!valid) {
                    // increment to next space in linear path
                    posX += i;
                    posY += j;
                    // if out of bounds, break due to illegality
                    if (posX < 0 || posY < 0 || posX > 7 || posY > 7) { break; }
                    curr = board[posY][posX];
                    // if current space is same color as player move, then legal move
                    if (curr == c) {
                        valid = true;
                        legal = true;

                        // move = true, update board to show result of legal move
                        if (move) {
                            // decrement current position to backtrack along path
                            posX -= i;
                            posY -= j;
                            // if out of bounds, break
                            if (posX < 0 || posY < 0 || posX > 7 || posY > 7) { break; }

                            // while it has not reached the space for the move
                            while (!(posX == x && posY == y)) {
                                // update piece to be players color
                                board[posY][posX] = c;
                                posX -= i;
                                posY -= j;
                                // if out of bounds, break loop
                                if (posX < 0 || posY < 0 || posX > 7 || posY > 7) { break; }
                            }
                        }

                        // else, just return that move is legal, do not update board
                        else
                            return true;
                    } else if (curr == -1 || curr == 0 ) {
                        valid = true;
                    }
                }
            }
        }
        if (move && legal) { board[y][x] = c; }
        return legal;
    }

    public int[] get_scores(int[][] board) {
        // counts the number of black and white pieces on the board
        // returns the two values as the two players' scores
        int[] scores = new int[2];
        int black = 0;
        int white = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 1) {
                    black++;
                } else if (board[i][j] == 2) {
                    white++;
                }
            }
        }
        scores[0] = black;
        scores[1] = white;
        return scores;
    }

    public boolean game_over(int[][] board) {
        // counts total number of pieces each player has
        // if either player has 0 pieces, then the game is over, return true
        // black total plus white total = # spaces on board, return true
        int[] scores = get_scores(board);
        if (scores[0] == 0 || scores[1] == 0 || scores[0]+scores[1] == 64) { return true; }
        return false;
    }
}
