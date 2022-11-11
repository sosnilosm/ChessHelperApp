package com.sosnilosm.chess;

import com.sosnilosm.chess.pieces.Piece;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Sergei Sosnilo
 */
public class ChessLogic {
    public void start() {
        Scanner sc = new Scanner(System.in);
        ChessBoard chessBoard = new ChessBoard();
        System.out.println(getTakenPiecesInfo(chessBoard));
        System.out.println(chessBoard);

        int movesCounter = 0;
        while (!chessBoard.isKingOnCheckmate(chessBoard.getCurrentTurn()) && !chessBoard.isDraw()) {
            System.out.print("Enter command(" + chessBoard.getCurrentTurn() +"): ");
            String cmd = sc.nextLine();

            if (doMove(cmd, chessBoard)) {
                System.out.println(getTakenPiecesInfo(chessBoard));
                System.out.println(chessBoard);
                movesCounter++;
            }

            if (chessBoard.isKingOnCheck(chessBoard.getCurrentTurn())) {
                System.out.println("CHECK");
            }
        }
        if (chessBoard.isKingOnCheckmate(chessBoard.getCurrentTurn())) {
            System.out.println("CHECKMATE!");
            if (chessBoard.getCurrentTurn() == Piece.Colours.White) {
                System.out.println("BLACK WON in " + ((movesCounter / 2) + 1) + " moves!!! End of the game.");
            } else if (chessBoard.getCurrentTurn() == Piece.Colours.Black) {
                System.out.println("WHITE WON in " + ((movesCounter / 2) + 1) + " moves!!! End of the game.");
            }
        }
        else if (chessBoard.isDraw()) {
            System.out.println("PATE");
        }
    }

    private boolean doMove(String cmd, ChessBoard chessBoard) {
        if (cmd.matches("[a-hA-H][1-8] [a-hA-ZH][1-8]")) {
            return ordinaryMove(cmd, chessBoard);
        }
        else if (cmd.matches("0-0")) {
            return shortRoque(cmd, chessBoard);
        } else if (cmd.matches("0-0-0")) {
            return longRoque(cmd, chessBoard);
        }
        else if (cmd.matches("undo")) {
            return undoPos(chessBoard);
        }
        else if (cmd.matches("next")) {
            return nextPos(chessBoard);
        }
        System.out.println("<INPUT ERROR>");
        return false;
    }

    private boolean ordinaryMove(String cmd, ChessBoard chessBoard) {
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

    private boolean shortRoque(String cmd, ChessBoard chessBoard) {
        if (chessBoard.getCurrentTurn() == Piece.Colours.White) {
            System.out.println("OK " + cmd);
            return chessBoard.doRoque(4, 0, 7, 0);
        } else if (chessBoard.getCurrentTurn() == Piece.Colours.Black) {
            System.out.println("OK " + cmd);
            return chessBoard.doRoque(4, 7, 7, 7);
        }
        else {
            System.out.println("<ROQUE ERROR>");
            return false;
        }
    }

    private boolean longRoque(String cmd, ChessBoard chessBoard) {
        if (chessBoard.getCurrentTurn() == Piece.Colours.White) {
            System.out.println("OK " + cmd);
            return chessBoard.doRoque(4, 0, 0, 0);
        } else if (chessBoard.getCurrentTurn() == Piece.Colours.Black) {
            System.out.println("OK " + cmd);
            return chessBoard.doRoque(4, 7, 0, 7);
        }
        else {
            System.out.println("<ROQUE ERROR>");
            return false;
        }
    }

    private boolean undoPos(ChessBoard chessBoard) {
        if (chessBoard.previousPos()) {
            System.out.println("OK");
            return true;
        }
        else {
            System.out.println("<ERROR No moves to be UNDER>");
            return false;
        }
    }

    private boolean nextPos(ChessBoard chessBoard) {
        if (chessBoard.nextPos()) {
            System.out.println("OK");
            return true;
        }
        else {
            System.out.println("<ERROR No moves to be NEXT>");
            return false;
        }
    }

    private int[] getIntXY(String cmd) {
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

    private String getTakenPiecesInfo(ChessBoard chessBoard) {
        StringBuilder takenPiecesInfo = new StringBuilder();
        takenPiecesInfo.append("White took: ").append(chessBoard.getTakenPiecesTypesByColour(Piece.Colours.White));
        if (chessBoard.getSuperiorityOfColour(Piece.Colours.White) > 0) {
            takenPiecesInfo.append("+").append(chessBoard.getSuperiorityOfColour(Piece.Colours.White));
        }
        takenPiecesInfo.append("\nBlack took: ").append(chessBoard.getTakenPiecesTypesByColour(Piece.Colours.Black));
        if (chessBoard.getSuperiorityOfColour(Piece.Colours.Black) > 0) {
            takenPiecesInfo.append("+").append(chessBoard.getSuperiorityOfColour(Piece.Colours.Black));
        }
        return takenPiecesInfo.toString();
    }
}
