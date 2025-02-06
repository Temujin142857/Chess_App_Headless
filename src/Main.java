import Chess_Board.Chess_Set.Board;
import Chess_Board.HeadlessInterface;
import DatabaseHandler.Streamer;
import Engine.Player_Engine;

import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main { //main for the project, work in progress; play not finish.

    public static void main(String[] args) {
        boolean playerIsWhite=true;
        if(args.length>0){
            playerIsWhite=args[0].equals("true");
        }
        play(true, playerIsWhite);
        //dataTest();
        //translatePGN("master_games.pgn","datafile");
        /**
        Player_Engine engine=new Player_Engine('W');
        int[][] move=engine.getNextMove(new Board());
        System.out.println(move[0][0]+","+move[0][1]+" goes to "+move[1][0]+","+move[1][1]);
        System.out.println("final answer, lock it in");
         */

    }

    //runs the game
    private static void play(boolean isCpuOpponent, boolean playerIsWhite){
        HeadlessInterface HInterface= new HeadlessInterface();
        try {
            HInterface.play(new Board(), isCpuOpponent, playerIsWhite );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scanner stdin = new Scanner(System.in);
        while(true){
            String signal=stdin.nextLine();
            System.out.println("signal recieved: "+signal);
            if(signal.equals("engine ready?")){
                if(HInterface.ready()){
                    System.out.println("ready");
                }
            } else if (signal.contains("coordinates")) {
                String temp=signal.split(":")[1];
                String[] temp2 =temp.split(",");
                int x=Integer.parseInt(temp2[0]);
                int y=Integer.parseInt(temp2[1]);
                HInterface.squareClicked(x,y);
            } else if(signal.contains("perform engine move")){
                System.out.println("active");
                HInterface.getCPUMove();
            } else if (signal.contains("reset")) {
                System.out.println("broke");
                break;
            }
        }


    }



    private static void translatePGN(String filename1, String filename2){
        Streamer streamer=new Streamer();
        streamer.pgnTodata(filename1,filename2);
    }

}
