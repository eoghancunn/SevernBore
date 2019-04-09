
import java.util.Hashtable;

class AlphaBeta {

    private static String[][] killerMoves = new String[5][3];
    private static Hashtable<String, Integer> history = new Hashtable<>();
    static int numEvaluations = 0;

    static AlphaBetaMove alphabeta(int p1, int p2, int charges, String prevMove, int alpha, int beta, boolean p1toMove, int depth, boolean killer, boolean history){
        int bestScore;
        if (depth == 0){
            bestScore = Board.evaluate(p1,p2,charges);
            numEvaluations++;
            return new AlphaBetaMove(prevMove, bestScore);
        }
        String moves;
        if (p1toMove){
            moves = Board.possibleMovesPlayer1(p1,p2,charges);
        } else {
            moves = Board.possibleMovesPlayer2(p1,p2,charges);
        }
        if (moves.length() == 0) {
            if (p1toMove){
                bestScore = -Integer.MAX_VALUE;
            } else {
                bestScore = Integer.MAX_VALUE;
            }
            return new AlphaBetaMove(prevMove, bestScore);
        } if(killer){
            for(int pos = 0; pos<killerMoves[depth].length; pos++){
                String killerMove = killerMoves[depth][pos];
                int i = 0;
                boolean found = false;
                while(i < moves.length()-6 && !found){
                    if (moves.substring(i, i+=6).equals(killerMove)){
                        moves = promoteMove(moves, i);
                        found = true;
                    }
                    i += 6;
                }
            }
        }
        if (history){
            moves = sortMoves(moves);
        }
        String bestMove = moves.substring(0,6);
        if(p1toMove){
          int value = Integer.MIN_VALUE;
            for (int i = 0; i < moves.length(); i += 6){
                String move = moves.substring(i,i+6);
                int p1t = Board.makeMove(p1, move);
                int p2t= p2;
                int chargest = Board.makeMove(charges, move);
                int score = alphabeta(p1t, p2t, chargest, move, alpha, beta,false, depth - 1, killer, history).value;
                if (score > value){value = score;}
                if (value > alpha){alpha = value; bestMove = move;}
                if (alpha >= beta){
                    if(killer){
                        addKillerMove(move, depth);
                    }
                    if(history){
                        addHistoryMove(move);
                    }
                    return new AlphaBetaMove(bestMove, value);
                }
            }
            return new AlphaBetaMove(bestMove, value);
        } else{
            int value = Integer.MAX_VALUE;
            for (int i = 0; i < moves.length(); i += 6){
                String move = moves.substring(i,i+6);
                int p1t = p1;
                int p2t= Board.makeMove(p2, move);
                int chargest = Board.makeMove(charges, move);
                int score = alphabeta(p1t, p2t, chargest, move, alpha, beta, true, depth - 1, killer, history).value;
                if (score < value){value = score;}
                if (value < beta){beta = value; bestMove = move;}
                if (alpha >= beta){
                    if(killer){
                        addKillerMove(move, depth);
                    }
                    if(history){
                        addHistoryMove(move);
                    }
                    return new AlphaBetaMove(bestMove, value);
                }
            }
            return new AlphaBetaMove(bestMove, value);
        }
    }

    private static String promoteMove(String moves, int moveIndex){
        String move, start, end;
        start = moves.substring(0, moveIndex);
        move = moves.substring(moveIndex, moveIndex+6);
        end = moves.substring(moveIndex+6);
        return move + start + end;
    }

    private static void addKillerMove(String move, int depth){
        for(int j = killerMoves.length - 2; j<=0; j++){
            killerMoves[depth][j + 1] = killerMoves[depth][j];
        }
        killerMoves[depth][0] = move;
    }

    private static void addHistoryMove(String move){
        if(history.containsKey(move)){
            history.put(move, history.get(move)+1);
        } else {
            history.put(move, 1);
        }
    }

    private static String swap(String moves, int i, int j){
        String move1, move2, start, end, middle;
        start = moves.substring(0,i);
        move1 = moves.substring(i, i+6);
        middle = moves.substring(i+6, j);
        move2 = moves.substring(j, j+6);
        end = moves.substring(j+6);
        return start+move2+middle+move1+end;
    }

    private static String sortMoves(String moves){
        String move;
        int promotions = 0;
        for(int i = 0; i < moves.length()-6; i+=6){
            move = moves.substring(i, i+6);
            if(history.containsKey(move)){
                moves = promoteMove(moves, i);
                promotions++;
            }
        }
        String historyMoves = moves.substring(0,promotions*6);
        String rest = moves.substring(promotions*6);
        String move1, move2;
        for(int i = 0; i < historyMoves.length()-6; i+=6 ){
            for(int j = i+6; j < historyMoves.length()-6; j+=6){
                move1 = historyMoves.substring(i, i + 6);
                move2 = historyMoves.substring(j, j + 6);
                if(history.get(move1) < history.get(move2)){
                    historyMoves = swap(historyMoves, i, j);
                }
            }
        }
        return historyMoves+rest;
    }

    static void resetEvaluations(){
        numEvaluations = 0;
    }

}


