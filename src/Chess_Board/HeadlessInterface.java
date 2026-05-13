package Chess_Board;

import Chess_Board.Chess_Set.Board;
import Engine.Player_Engine;
import java.io.IOException;

public class HeadlessInterface {

    private boolean white_active;
    private boolean isPieceHeld;
    private int[] pieceHeld;//pretty sure this is a coordinate of the form {x,y} with x and y being ints between 0 and 7 inclusively
    private Player Wplayer;
    private Player Bplayer;
    private Board board;
    private boolean isCheckmate;
    private int[] promotionSquare={};
    private boolean white_cpu=false;
    private boolean black_cpu=false;
    private Player_Engine engine;
    private Player_Engine engine2;
    private int depth=2;
    private String id="";

    private boolean ready=false;
    public void play(Board board, boolean isCpuOpponent, boolean playerIsWhite) throws IOException {
        white_active=true;
        isCheckmate=false;
        Bplayer=new Player('B');
        Wplayer=new Player('W');
        if(playerIsWhite||!isCpuOpponent){
            if(isCpuOpponent)black_cpu=true;
        }
        if(!playerIsWhite||!isCpuOpponent){
            if(isCpuOpponent)white_cpu=true;
        }
        this.board=board;
        if(black_cpu&&white_cpu){
            engine=new Player_Engine('W',depth);
            engine2=new Player_Engine('B',depth);
            while(!isCheckmate){

                int[][] move=engine.getNextMove(board);
                pieceHeld=move[0];
                white_move(move[1][0],move[1][1]);

                move=engine2.getNextMove(board);
                pieceHeld=move[0];
                black_move(move[1][0],move[1][1]);
            }
            printMessage("game over");
            return;
        }
        else if(white_cpu){
            engine=new Player_Engine('W',depth);
            int[][] move=engine.getNextMove(board);
            pieceHeld=move[0];
            white_move(move[1][0],move[1][1]);
        }
        else if(black_cpu){
            engine=new Player_Engine('B',depth);
        }
        ready=true;
    }


    public void squareClicked(int x, int y){
        if (!isPieceHeld){isPieceHeld=true;pieceHeld=new int[]{x,y};highlightSquare();}
        else if (white_active){
            isPieceHeld = false;
            printMessage("Piece deselected");
            boolean moved=white_move(x,y);
            if (moved) {
                if (board.at(new int[]{x, y}).getName().charAt(1) == 'P' && y == 7) {
                    //displayPromotionOptions(x, y);
                    return;
                }
            }
        }
        else {
            isPieceHeld = false;
            printMessage("Piece deselected");
            boolean moved=black_move(x,y);
            if (moved) {
                if (board.at(new int[]{x, y}).getName().charAt(1) == 'P' && y == 0) {
                    //displayPromotionOptions(x, y);
                    return;
                }

            }
        }
    }


    private boolean white_move(int x, int y){
        int moveResult = Wplayer.move(board, pieceHeld,new int[]{x,y});
        if(moveResult>0){//move is legal
            isPieceHeld=false;white_active=false;
            sendMove();
            if (board.isCheckmate(board.findKing('B'))){
                for (int i=0;i<8;i++) {
                    for (int j = 0; j < 8; j++) {
                        printMessage("at: "+i+","+j+"there is: "+board.at(i,j).getName());
                    }
                }
                isCheckmate=true;
                endGame("White");
            }

        }
        else{
            printMessage("illegal move, move was "+pieceHeld[0]+","+pieceHeld[1]+" to "+x+","+y);
            return false;
        }
        return true;
    }

    private boolean black_move(int x, int y){
        int moveResult = Bplayer.move(board, pieceHeld,new int[]{x,y});
        if(moveResult>0){//move is legal
            isPieceHeld=false;white_active=true;
            sendMove();
            if (board.isCheckmate(board.findKing('W'))){
                for (int i=0;i<8;i++) {
                    for (int j = 0; j < 8; j++) {
                        printMessage("at: "+i+","+j+"there is: "+board.at(i,j).getName());
                    }
                }
                isCheckmate=true;
                endGame("Black");
            }
        }
        else{
            printMessage("illegal move, move was "+pieceHeld[0]+","+pieceHeld[1]+" to "+x+","+y);
            return false;
        }
        return true;
    }

    private void sendMove(){
        printMessage("move success;highlight:null"+";board:"+board.toString());
        return;
    }

    private void highlightSquare(){
        printMessage("selection success;highlight:"+pieceHeld[0]+","+pieceHeld[1]);
    }

    private void endGame(String winner){
        printMessage("Checkmate! "+winner+" wins!");
    }

    public void getCPUMove(){
        if(white_cpu) {
            int[][] move = engine.getNextMove(board);
            pieceHeld = move[0];
            white_move(move[1][0], move[1][1]);
        }else if (black_cpu) {
            int[][] move = engine.getNextMove(board);
            pieceHeld = move[0];
            black_move(move[1][0], move[1][1]);
        }
    }

    private void printMessage(String message){
        System.out.println(message+"@"+id);
    }


    private void activatePromotion(){

    }

    private void confirmPromotion(){

    }

    public boolean ready(String idT){
        id=idT;
        return ready&&!isCheckmate;
    }


}
