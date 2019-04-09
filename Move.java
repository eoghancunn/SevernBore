/*
class for managing user input of moves.
 */

class Move {

    private String move = "";

    Move(String userInput) {
        userInput = userInput.toUpperCase();
        String positions[] = userInput.split(" ");
        if(positions.length != 3){throw new IllegalArgumentException("oops");}
        for(int i = 0; i < 3; i++){
            move += moveString(positions[i]);
        }
    }

    private static String moveString(String pos){
        String output = "";
        switch (pos.charAt(1)){
        case '1': output += 0;
            break;
        case '2': output += 1;
            break;
        case '3': output += 2;
            break;
        case '4': output += 3;
            break;
        case '5': output += 4;
            break;
        default: throw new IllegalArgumentException("oops");
    }
        switch (pos.charAt(0)) {
        case 'A': output += 0;
            break;
        case 'B': output += 1;
            break;
        case 'C': output += 2;
            break;
        case 'D': output += 3;
            break;
        case 'E': output += 4;
            break;
        default: throw new IllegalArgumentException("oops");

    }

        return output;
}

    String getMove(){
        return move;
    }

}
