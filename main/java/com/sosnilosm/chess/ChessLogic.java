package com.sosnilosm.chess;

import com.sosnilosm.chess.pieces.Piece;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Sergei Sosnilo
 */
// TODO: add undo move method, counter of taken pieces (Piece.Value)
public class ChessLogic {
    public void start() {
        Scanner sc = new Scanner(System.in);
        ChessBoard chessBoard = new ChessBoard();
        System.out.println(chessBoard);

        int movesCounter = 0;
        while (!chessBoard.isKingOnCheckmate(chessBoard.getCurrentTurn())) {
            System.out.print("Enter command(" + chessBoard.getCurrentTurn() +"): ");
            String cmd = sc.nextLine();

            if (doMove(cmd, chessBoard)) {
                System.out.println(chessBoard);
                movesCounter++;
            }

            if (chessBoard.isKingOnCheck(chessBoard.getCurrentTurn())) {
                System.out.println("CHECK");
            }
        }
        System.out.println("CHECKMATE!");
        if (chessBoard.getCurrentTurn() == Piece.Colour.White) {
            System.out.println("BLACK WON in " + ((movesCounter / 2) + 1) + " moves!!! End of the game.");
        } else if (chessBoard.getCurrentTurn() == Piece.Colour.Black) {
            System.out.println("WHITE WON in " + ((movesCounter / 2) + 1) + " moves!!! End of the game.");
        }
    }

    private boolean doMove(String cmd, ChessBoard chessBoard) {
        if (cmd.matches("[a-hA-H][1-8] [a-hA-ZH][1-8]")) {
            String fromCmd = cmd.toLowerCase().split(" ")[0];
            String toCmd = cmd.toLowerCase().split(" ")[1];

            int[] from = getIntXY(fromCmd);
            int[] to = getIntXY(toCmd);
            System.out.println("from = " + Arrays.toString(from));
            System.out.println("to = " + Arrays.toString(to));
            if (chessBoard.doMove(from[0], from[1], to[0], to[1])) {
                System.out.println("OK " + cmd);
                return true;
            } else {
                System.out.println("<MOVE ERROR>");
                return false;
            }
        }
        else if (cmd.matches("0-0")) {
            if (chessBoard.getCurrentTurn() == Piece.Colour.White) {
                return chessBoard.doRoque(4, 0, 7, 0);
            } else if (chessBoard.getCurrentTurn() == Piece.Colour.Black) {
                System.out.println("OK " + cmd);
                return chessBoard.doRoque(4, 7, 7, 7);
            }
            else {
                System.out.println("<ROQUE ERROR>");
                return false;
            }
        } else if (cmd.matches("0-0-0")) {
            if (chessBoard.getCurrentTurn() == Piece.Colour.White) {
                return chessBoard.doRoque(4, 0, 0, 0);
            } else if (chessBoard.getCurrentTurn() == Piece.Colour.Black) {
                System.out.println("OK " + cmd);
                return chessBoard.doRoque(4, 7, 0, 7);
            }
            else {
                System.out.println("<ROQUE ERROR>");
                return false;
            }
        }
        System.out.println("<INPUT ERROR>");
        return false;
    }

    private static int[] getIntXY(String cmd) {
        cmd = cmd.toLowerCase().split(" ")[0];

        char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

        int x = 0;
        for (int i = 0; i < alphabet.length; i++) {
            if (alphabet[i] == cmd.charAt(0)) {
                x = i;
                break;
            }
        }
        int y = Character.getNumericValue(cmd.charAt(1)) - 1;
        return new int[]{x, y};
    }
}

