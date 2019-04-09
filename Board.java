/*
  Class for all operations relative to the game board
  Contains methods for producing valid moves and for making static evaluations
 */


import java.util.Arrays;

class Board {

    private static int OCCUPIED;
    static int CHARGES = 0b0;
    static int PLAYER_1 = 0b1000000000000010;
    static int PLAYER_2 = 0b100000000000001000000000;


    final private static int RowMasks[] = {
            0b11111,
            0b1111100000,
            0b111110000000000,
            0b11111000000000000000,
            0b1111100000000000000000000,
    };

    final private static int ColMasks[] = {
            0b0000100001000010000100001,
            0b0001000010000100001000010,
            0b0010000100001000010000100,
            0b0100001000010000100001000,
            0b1000010000100001000010000,
    };

    final private static int DiagMasks[] = {
            0b1,
            0b100010,
            0b10001000100,
            0b1000100010001000,
            0b100010001000100010000,
            0b1000100010001000000000,
            0b10001000100000000000000,
            0b100010000000000000000000,
            0b1000000000000000000000000
    };

    final private static int AntiDiagMasks[] = {
            0b10000,
            0b1000001000,
            0b100000100000100,
            0b10000010000010000010,
            0b1000001000001000001000001,
            0b100000100000100000100000,
            0b10000010000010000000000,
            0b1000001000000000000000,
            0b100000000000000000000,
    };

    final private static int CornerMask = 0b1000100000000000000010001;

    final private static int EdgeMask = 0b111111000110001100011000111111;

    static void drawBoard(int player1, int player2, int charges) {

        String board[][] = new String[5][5];
        for(int i = 0; i < 25; i++){
            board[i/5][i%5] = " ";
        }

        for(int i = 0; i < 25; i++){
            if (((player1 >> i) & 1 ) == 1) { board[i/5][i%5] = "#";}
            if (((player2 >> i) & 1 ) == 1) { board[i/5][i%5] = "O";}
            if (((charges >> i) & 1 ) == 1) { board[i/5][i%5] = "x";}
        }
        System.out.println("  A  B  C  D  E");
        for(int i = 0; i < 5; i++){
            System.out.println(i+1 + Arrays.toString(board[i]));
        }
        System.out.println("\n");

    }

    private static int validMoves(int OCCUPIED, int s){
        int binS = 1<<s;
        int horizontal = (OCCUPIED - 2*binS) ^ Integer.reverse(Integer.reverse(OCCUPIED) - 2 * Integer.reverse(binS));
        int vertical = ((OCCUPIED&ColMasks[s % 5]) - (2 * binS)) ^ Integer.reverse(Integer.reverse(OCCUPIED&ColMasks[s % 5]) - (2 * Integer.reverse(binS)));
        horizontal = horizontal&(~OCCUPIED);
        vertical = vertical&~(OCCUPIED&ColMasks[s % 5]);
        int diagonal = ((OCCUPIED&DiagMasks[(s / 5) + (s % 5)]) - (2 * binS)) ^ Integer.reverse(Integer.reverse(OCCUPIED&DiagMasks[(s / 5) + (s % 5)]) - (2 * Integer.reverse(binS)));
        int antidiagonal = ((OCCUPIED&AntiDiagMasks[(s / 5) + 4 - (s % 5)]) - (2 * binS)) ^ Integer.reverse(Integer.reverse(OCCUPIED&AntiDiagMasks[(s / 5) + 4 - (s % 5)]) - (2 * Integer.reverse(binS)));
        diagonal = diagonal&~(OCCUPIED&DiagMasks[(s / 5) + (s % 5)]);
        antidiagonal = antidiagonal&~(OCCUPIED&AntiDiagMasks[(s / 5) + 4 - (s % 5)]);
        return (horizontal&RowMasks[s/5]) | (vertical&ColMasks[s%5]) | (diagonal&(DiagMasks[(s / 5) + (s % 5)])) | antidiagonal&(AntiDiagMasks[(s / 5) + 4 - (s % 5)]);

    }

    static String possibleMoves(int OCCUPIED, int Q) {
        String list="";
        int i=Q&~(Q-1);
        int possibility;
        int possible_throws;
        while(i != 0) {
            int iLocation=Integer.numberOfTrailingZeros(i);
            possibility=validMoves(OCCUPIED, iLocation);
            int j=possibility&~(possibility-1);
            while (j != 0) {
                int index=Integer.numberOfTrailingZeros(j);
                possible_throws = validMoves(OCCUPIED, index);
                int k = possible_throws&~(possible_throws-1);
                while(k != 0){
                    int chrge = Integer.numberOfTrailingZeros(k);
                    list+=""+(iLocation/5)+(iLocation%5)+(index/5)+(index%5)+(chrge/5)+(chrge%5);
                    possible_throws&=~k;
                    k = possible_throws&~(possible_throws-1);
                }
                list+=""+(iLocation/5)+(iLocation%5)+(index/5)+(index%5)+(iLocation/5)+(iLocation%5);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            Q&=~i;
            i=Q&~(Q-1);
        }
        return list;
    }

    static int makeMove(int board, String move){
        int start = (Character.getNumericValue(move.charAt(0))*5)+(Character.getNumericValue(move.charAt(1)));
        int end = (Character.getNumericValue(move.charAt(2))*5)+(Character.getNumericValue(move.charAt(3)));
        if (((board>>>start)&1)==1) {
            board&=~(1<<start);
        } else {
            end = (Character.getNumericValue(move.charAt(4))*5)+(Character.getNumericValue(move.charAt(5)));
        }
        board|=(1L<<end);
        return board;
    }

    static int evaluate(int p1, int p2, int charges ){
        int i = p1;
        int p1s1 = Integer.numberOfTrailingZeros(i);
        int p1s2 = Integer.numberOfTrailingZeros((i&~p1s1));
        i = p2;
        int p2s1 = Integer.numberOfTrailingZeros(i);
        int p2s2 = Integer.numberOfTrailingZeros((i&~p2s1));
        int val = 0;
        val -= inCorner(p1);
        val -= onEdge(p1);
        val -= nextToCharge(p1s1,p1s2,charges);
        val -= nextToSurfer(p1s1,p1s2,p1,p2);
        if(canMoveNextToPartner(p1s1,p1s2,charges)){val += 1;}
        val += inCorner(p2);
        val += onEdge(p2);
        val += nextToCharge(p2s1,p2s2,charges);
        val += nextToSurfer(p2s1,p2s2,p1,p2);
        val += possibleMoves(p1|p2|charges, p1).length();
        val -= possibleMoves(p1|p2|charges,p2).length();
        return val;
    }

    static String possibleMovesPlayer1(int player1, int player2, int charges){
        OCCUPIED = player1 | player2 | charges;
        return possibleMoves(OCCUPIED, player1);
    }

    static String possibleMovesPlayer2(int player1, int player2, int charges){
        OCCUPIED = player1 | player2 | charges;
        return possibleMoves(OCCUPIED, player2);
    }

    //returns the numnber of the players surfers that ar in a corner
    private static int inCorner(int position){
        return Integer.bitCount(position&CornerMask);
    }


    //returns the number of surfers on the edge of the board.
    private static int onEdge(int position){
        return Integer.bitCount(position&EdgeMask);
    }

    //returns the total number of charges next to surfers.
    private static int nextToCharge(int p1, int p2, int charges){
        return Integer.bitCount(adjacency(p1,p2)&charges);
    }
    //returns the total number of surfers adjacent to this players surfers.
    private static int nextToSurfer(int p1, int p2, int player1, int player2){
        return Integer.bitCount( adjacency(p1,p2)& (player1 | player2));
    }
    //indicates whether or not one surfer can move to a cell adjacent to its partner
    private static boolean canMoveNextToPartner(int p1, int p2,int charges){
        return((Board.validMoves(p1|p2|charges, p1) & adjacentTo(p2)) != 0);
    }

    //returns a bitboard indicating all of the cells adjacent to the cell indicated in the input
    private static int adjacentTo(int pos){
        int adjacency = 0;
        adjacency = pos>>1|pos<<1|pos>>5|pos>>4|pos>>6|pos<<5|pos<<4|pos<<6;
        if((pos & Board.RowMasks[0])!= 0){
            adjacency = adjacency&~(pos>>5|pos>>4|pos>>6);
        }
        if((pos & Board.RowMasks[4]) != 0){
            adjacency = adjacency&~(pos<<5|pos<<4|pos<<6);
        }
        if((pos & Board.ColMasks[0]) != 0){
            adjacency = adjacency&~(pos<<4|pos>>1|pos>>6);
        }
        if((pos & Board.ColMasks[4]) != 0){
            adjacency = adjacency&~(pos>>4|pos<<1|pos<<6);
        }
        return adjacency;
    }

    //returns a bitboard indicating all cells adjacent to the player
    private static int adjacency(int p1,int p2){
        return adjacentTo(p1) | adjacentTo(p2);
    }


}
