/*
Class for maintaining and updating the position/moves made by players

 */

import java.util.Scanner;

class Player {

    private Scanner scanner = new Scanner(System.in);
    private int position;

    Player(int p_one, int p_two){
        position = p_one | p_two;
    }

    void getMove(){
        boolean validMove;
        Move temp = null;
        do {
            validMove = true;
            System.out.println("input your move : ");
            try{ temp = new Move(scanner.nextLine()); }
            catch (IllegalArgumentException e){
                System.out.println("invalid move!");
                validMove = false;
            }
        }while(!validMove);
        String move = temp.getMove();
        if(isLegalMove(move)){
            position = Board.makeMove(position, move);
            Board.CHARGES = Board.makeMove(Board.CHARGES, move);
        } else {throw new IllegalArgumentException();}
    }

    private boolean isLegalMove(String move) {
        String possibilities = Board.possibleMoves(Board.CHARGES|Board.PLAYER_1|Board.PLAYER_2, position);
        for (int i = 0; i < possibilities.length(); i+=6){
            if(possibilities.substring(i, i+6).equals(move)){
                return true;
            }
        }
        return false;
    }

    int getPosition() {
        return position;
    }

    void setPosition(int pos){
        position = pos;
    }

    boolean cantMove(){
        return Board.possibleMoves(Board.PLAYER_1|Board.PLAYER_2|Board.CHARGES, position).length() == 0;
    }

}
