//Author: Eoghan Cunningham - 16441162.

public class Main {

    private static boolean livePayer = false; //set false to play against computer, set true for pass and play.
    private static boolean killerMoveOn = false; //set true to enable the killer move heuristic.
    private static boolean historyOn = true;
    private static int alphaBetaDepth = 4; //choose depth limit for alpha beta search.

    public static void main(String[] args) {

        Player player_one =  new Player(0b10, 0b1000000000000000); //
        Player player_two = new Player(0b0100000000000000000000000, 0b1000000000);
        Board.drawBoard(player_one.getPosition(), player_two.getPosition(), Board.CHARGES);


        while(inPlay(player_one,player_two)){
            if (inPlay(player_one, player_two)) {
                System.out.println("Player 1: ");
                movePlayer(player_one, 0,true );
                Board.PLAYER_1 = player_one.getPosition();
                Board.drawBoard(player_one.getPosition(),player_two.getPosition(),Board.CHARGES);
            }
            if (inPlay(player_one,player_two)){
                System.out.println("Player 2: ");
                movePlayer(player_two, player_one.getPosition(),livePayer);
                Board.PLAYER_2 = player_two.getPosition();
                Board.drawBoard(player_one.getPosition(),player_two.getPosition(),Board.CHARGES);
            }
        }

    }

    private static boolean inPlay(Player p1, Player p2){
        if(p1.cantMove()){
            System.out.println("*** PLAYER TWO IS THE WINNER! ***");
            return false;
        }
        if(p2.cantMove()){
            System.out.println("*** PLAYER ONE IS THE WINNER! ***");
            return false;
        }
        return true;
    }


    private static void movePlayer(Player p, int opponentPos, boolean livePlayer){
        if(livePlayer){
            boolean legalMove;
            do {
                try {
                    legalMove = true;
                    p.getMove();
                } catch (IllegalArgumentException e ){
                    System.out.println("this is not a legal move!");
                    legalMove = false;
                }
            }while(!legalMove);
        } else{
            long startTime=System.currentTimeMillis();
            AlphaBetaMove temp = AlphaBeta.alphabeta(opponentPos, p.getPosition(), Board.CHARGES, "", Integer.MIN_VALUE, Integer.MAX_VALUE,false, alphaBetaDepth, killerMoveOn, historyOn);
            System.out.println(temp);
            long endTime=System.currentTimeMillis();
            System.out.println("That took "+(endTime-startTime)+" milliseconds");
            System.out.println("AlphaBeta made "+AlphaBeta.numEvaluations+" evaluations.");
            //AlphaBeta.resetEvaluations();
            String move = temp.move;
            p.setPosition(Board.makeMove(p.getPosition(), move));
            Board.CHARGES = Board.makeMove(Board.CHARGES, move);
        }
    }

}
