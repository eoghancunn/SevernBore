class AlphaBetaMove {

    String move;
    int value;

    AlphaBetaMove(String m, int v){
        move = m;
        value = v;
    }


    public String toString(){
        return correctNotation(move) + " " +value;
    }

    private static String correctNotation(String move){
        if (move.length() < 6 ) {
            return "";
        }
        String[] positions = new String[3];
        for(int i = 0; i < 6; i+=2) {
            positions[i/2] = move.substring(i, i+2);
        }
        String output = "";
        for (int i = 0; i < 3; i++){
            switch (positions[i].charAt(1)){
                case '0': output += 'A'; break;
                case '1': output += 'B'; break;
                case '2': output += 'C'; break;
                case '3': output += 'D'; break;
                case '4': output += 'E'; break;
                default: throw new IllegalArgumentException("oops");
            }
            switch (positions[i].charAt(0)) {
                case '0': output += 1; break;
                case '1': output += 2; break;
                case '2': output += 3; break;
                case '3': output += 4; break;
                case '4': output += 5; break;
                default: throw new IllegalArgumentException("oops");

            }
        output += " ";
        }
        return output;
    }


}

