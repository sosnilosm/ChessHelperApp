package com.sosnilosm.chess;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;
import com.sosnilosm.chess.pieces.Piece;
import com.sosnilosm.chess.pieces.definite.*;

import java.util.*;

/**
 * @author Sergei Sosnilo
 */
// Exceptions to Check:
    /*
    Done/ Not    |   Exception
    _________________________________________________
    DONE check   |   Null or Empty for CURRENT_PIECE
    DONE check   |   FirstMoveOnly
    DONE check   |   Repeatable/ NonRepeatable
    DONE check   |   Null or Empty for OTHER_PIECES
    DONE check   |   Values toX, toY
    DONE check   |   On take, OnlyOnTake
    */

public class ChessBoard {
    private final AbstractPiece[][] board;
    private Piece.Colour currentTurn = Piece.Colour.White;

    private final Stack<PosStkData> previousPosStk = new Stack<>();
    private final Stack<PosStkData> nextPosStk = new Stack<>();

    public ChessBoard() {
        board = new AbstractPiece[8][8];
        setBoard();
    }

    private void setBoard(){
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(Piece.Colour.White);
            board[2][i] = new EmptyCell();
            board[3][i] = new EmptyCell();
            board[4][i] = new EmptyCell();
            board[5][i] = new EmptyCell();
            board[6][i] = new Pawn(Piece.Colour.Black);
        }

        //rooks
        board[0][0] = new Rook(Piece.Colour.White);
        board[0][7] = new Rook(Piece.Colour.White);
        board[7][0] = new Rook(Piece.Colour.Black);
        board[7][7] = new Rook(Piece.Colour.Black);

        //knight
        board[0][1] = new Knight(Piece.Colour.White);
        board[0][6] = new Knight(Piece.Colour.White);
        board[7][1] = new Knight(Piece.Colour.Black);
        board[7][6] = new Knight(Piece.Colour.Black);

        //bishop
        board[0][2] = new Bishop(Piece.Colour.White);
        board[0][5] = new Bishop(Piece.Colour.White);
        board[7][2] = new Bishop(Piece.Colour.Black);
        board[7][5] = new Bishop(Piece.Colour.Black);

        //queens
        board[0][3] = new Queen(Piece.Colour.White);
        board[7][3] = new Queen(Piece.Colour.Black);

        //kings
        board[0][4] = new King(Piece.Colour.White);
        board[7][4] = new King(Piece.Colour.Black);
    }

    private boolean isMoveValidInValues(int fromX, int fromY, Move move) {
        return (fromX + move.x() >= 0) && (fromY + move.y() >= 0) && (fromX + move.x() < 8) && (fromY + move.y() < 8);
    }

    private boolean isEmptyCell(int x, int y) {
        return (board[y][x].getType() == Piece.Types.empty);
    }

    public AbstractPiece[][] getBoard(){
        return board;
    }

    public Piece.Colour getCurrentTurn() {
        return this.currentTurn;
    }

    private boolean swapTurn() {
        if (currentTurn == Piece.Colour.White) {
            currentTurn = Piece.Colour.Black;
        }
        else if (currentTurn == Piece.Colour.Black) {
            currentTurn = Piece.Colour.White;
        }
        return true;
    }

    public boolean previousPos() {
        if (!previousPosStk.empty()) {
            PosStkData previousMove = previousPosStk.pop();

            board[previousMove.toY()][previousMove.toX()] = previousMove.toPiece();
            board[previousMove.fromY()][previousMove.fromX()] = previousMove.fromPiece();

            return addPosInNextStk(previousMove.fromX(), previousMove.fromY(), previousMove.toX(), previousMove.toY(),
                    previousMove.fromPiece(), previousMove.toPiece());
        }
        else {
            return false;
        }
    }

    public boolean nextPos() {
        if (!nextPosStk.empty()) {
            PosStkData nextMove = nextPosStk.pop();

            board[nextMove.fromY()][nextMove.fromX()] = nextMove.toPiece();
            board[nextMove.toY()][nextMove.toX()] = nextMove.fromPiece();

            return addPosInPreviousStk(nextMove.fromX(), nextMove.fromY(), nextMove.toX(), nextMove.toY(),
                    nextMove.fromPiece(), nextMove.toPiece());
        }
        else {
            return false;
        }
    }



    private boolean addPosInPreviousStk(int fromX, int fromY, int toX, int toY,
                                        AbstractPiece fromPiece, AbstractPiece toPiece) {
        previousPosStk.push(new PosStkData(fromX, fromY, toX, toY, fromPiece, toPiece));
        return swapTurn();
    }

    private boolean addPosInNextStk(int fromX, int fromY, int toX, int toY,
                                    AbstractPiece fromPiece, AbstractPiece toPiece) {
        nextPosStk.push(new PosStkData(fromX, fromY, toX, toY, fromPiece, toPiece));
        return swapTurn();
    }

    public boolean doMove(int fromX, int fromY, int toX, int toY) {
        if (moveCheckForLegal(fromX, fromY, toX, toY)) {
            AbstractPiece fromPiece = board[fromY][fromX], toPiece = board[toY][toX];
            if (board[fromY][fromX].getType() == Piece.Types.Pawn
                    && ((toY == 0 && board[fromY][fromX].getColour() == Piece.Colour.Black)
                                    || (toY == 7 && board[fromY][fromX].getColour() == Piece.Colour.White))
            ) {
                board[toY][toX] = pawnSwap();
            } else {
                board[toY][toX] = board[fromY][fromX];
            }
            board[toY][toX].doMove();
            board[fromY][fromX] = new EmptyCell();

            nextPosStk.clear();
            return addPosInPreviousStk(fromX, fromY, toX, toY, fromPiece, toPiece);

        }
        return false;
    }

    private boolean moveCheckForLegal(int fromX, int fromY, int toX, int toY) {
        if (board[fromY][fromX].getColour() == currentTurn) {
            System.out.println("moveCheckForLegal.if1");
            if (board[toY][toX].getType() == Piece.Types.empty
                    || (board[toY][toX].getType() != Piece.Types.empty
                    && board[toY][toX].getColour() != board[fromY][fromX].getColour())) {
                System.out.println("moveCheckForLegal.if2");
                for (int[] el : getLegalMoves(fromX, fromY)) {
                    System.out.println(Arrays.toString(el) + "?=" + Arrays.toString(new int[]{toX, toY}));
                    if (Arrays.equals(el, new int[]{toX, toY})) {
                        System.out.println("moveCheckForLegal.if3");
                        if (isNotMoveCauseCheck(fromX, fromY, toX, toY)) {
                            System.out.println("moveCheckForLegal.if4");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private List<int[]> getLegalMoves(int x, int y) {
        List<int[]> legalMoves = new ArrayList<>();
        // Empty cell check
        if (!isEmptyCell(x, y)) {
            for (Move move : board[y][x].getValidMoves()) {
                // First move check
                if (!move.firstStepOnly() || board[y][x].isFirstMove()) {
                    // Repeatable or NonRepeatable
                    if (board[y][x].isRepeatableMoves()) {
                        legalMoves.addAll(getLegalRepeatable(x, y, move));
                    } else {
                        legalMoves.addAll(getLegalNonRepeatable(x, y, move));
                    }
                }
            }
        }

        return legalMoves;
    }

    private List<int[]> getLegalRepeatable(int fromX, int fromY, Move move) {
        List<int[]> legalMoves = new ArrayList<>(getLegalNonRepeatable(fromX, fromY, move));
        Piece.Colour currentColour = board[fromY][fromX].getColour();

        // while we have valid values for x, y (from where we try to do move)
        while (isMoveValidInValues(fromX, fromY, move)) {
            if (board[fromY + move.y()][fromX + move.x()].getType() == Piece.Types.empty) {
                fromX += move.x();
                fromY += move.y();
                legalMoves.addAll(getLegalNonRepeatable(fromX, fromY, move));
            } else if (board[fromY + move.y()][fromX + move.x()].getColour() != currentColour) {
                legalMoves.addAll(getLegalNonRepeatable(fromX, fromY, move));
                break;
            }
            // else end while cycle
            else {
                break;
            }
        }

        return legalMoves;
    }

    private List<int[]> getLegalNonRepeatable(int fromX, int fromY, Move move) {
        List<int[]> legalMoves = new ArrayList<>();
        int toX;
        int toY;

        // 0 <= x, y < 8
        if (isMoveValidInValues(fromX, fromY, move)) {
            toX = fromX + move.x();
            toY = fromY + move.y();
            // if cell is empty and onTakeOption check
            if (!isEmptyCell(toX, toY) &&
                    (move.canTake() || move.onTakeOnly()) &&
                    board[fromY][fromX].getColour() != board[toY][toX].getColour()) {
                legalMoves.add(new int[]{toX, toY});
            }
            else if (isEmptyCell(toX, toY) && !move.onTakeOnly()) {
                legalMoves.add(new int[]{toX, toY});
            }
        }
        return legalMoves;
    }

    public List<int[]> getAllLegalMovesAgainstColour(Piece.Colour currentColour) {
        List<int[]> allLegalMovesAgainstColour = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getColour() != Piece.Colour.empty && board[i][j].getColour() != currentColour) {
                    allLegalMovesAgainstColour.addAll(getLegalMoves(j, i));
                }
            }
        }
        return allLegalMovesAgainstColour;
    }

    private int[] findKing(Piece.Colour currentColour) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getType() == Piece.Types.King && board[i][j].getColour() == currentColour) {
                    return new int[]{j, i};
                }
            }
        }
        return new int[]{};
    }

    private boolean isCellOnCheck(int x, int y, Piece.Colour againstColor) {
        int[] cell = new int[]{x, y};
        for (int[] legalMove : getAllLegalMovesAgainstColour(againstColor)) {
            if (Arrays.equals(cell, legalMove)) {
                return true;
            }
        }
        return false;
    }

    public boolean isKingOnCheck(Piece.Colour colour) {
        if (findKing(colour).length > 0) {
            int[] king = findKing(colour);
            return isCellOnCheck(king[0], king[1], colour);
        }
        return false;
    }

    private boolean isNotMoveCauseCheck(int fromX, int fromY, int toX, int toY) {
        boolean answer = true;
        AbstractPiece pieceToBeMoved = board[fromY][fromX];
        AbstractPiece pieceWhereMoved = board[toY][toX];

        board[fromY][fromX] = new EmptyCell();
        board[toY][toX] = pieceToBeMoved;

        if (isKingOnCheck(pieceToBeMoved.getColour())) {
            answer = false;
        }

        board[fromY][fromX] = pieceToBeMoved;
        board[toY][toX] = pieceWhereMoved;
        return answer;
    }

    public boolean isKingOnCheckmate(Piece.Colour currentColour) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getColour() == currentColour) {
                    for (int[] move : getLegalMoves(j, i)) {
                        if (isNotMoveCauseCheck(j, i, move[0], move[1]) &&
                                board[i][j].getColour() != board[move[1]][move[0]].getColour()) {
                            System.out.println("safe move -> from[" + j + "," + i + "], to[" + (move[0]) + "," + (move[1]) + "]");
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean doRoque(int fromX, int fromY, int toX, int toY) { // int x, y of Rook to roque
        System.out.println("roque()");
        if (board[fromY][fromX].getColour() == board[toY][toX].getColour()
                && board[fromY][fromX].getType() == Piece.Types.King
                && board[toY][toX].getType() == Piece.Types.Rook
                && board[fromY][fromX].getColour() == currentTurn) {
            System.out.println("roque().if1");
            if (board[fromY][fromX].isFirstMove() && board[toY][toX].isFirstMove()
                    && !isKingOnCheck(board[fromY][fromX].getColour())) {
                System.out.println("roque().if2");
                if (fromX < toX) {
                    System.out.println("roque().if3");
                    for (int i = fromX + 1; i < toX; i++) {
                        if (board[fromY][i].getType() != Piece.Types.empty
                                || isCellOnCheck(i, fromY, board[fromY][fromX].getColour())) {
                            System.out.println("roque().if4");
                            return false;
                        }
                    }
                    return shortRoque(fromX, fromY, toX, toY);
                } else if (fromX > toX) {
                    System.out.println("roque().else-if3");
                    for (int i = toX + 1; i < fromX; i++) {
                        if (board[fromY][i].getType() != Piece.Types.empty
                                || isCellOnCheck(i, fromY, board[fromY][fromX].getColour())) {
                            System.out.println("roque().else-if4");
                            return false;
                        }
                    }
                    return longRoque(fromX, fromY, toX, toY);
                }
            }
        }
        return false;
    }

    private boolean shortRoque(int fromX, int fromY, int toX, int toY) {
        System.out.println("roque() roque!!!!");
        AbstractPiece king = board[fromY][fromX];
        AbstractPiece rook = board[toY][toX];

        board[fromY][fromX] = new EmptyCell();
        board[toY][toX] = new EmptyCell();

        board[fromY][fromX + 2] = king;
        board[toY][toX - 2] = rook;

        board[fromY][fromX + 2].doMove();
        board[toY][toX - 2].doMove();

        nextPosStk.clear();
        return swapTurn();
    }

    private boolean longRoque(int fromX, int fromY, int toX, int toY) {
        System.out.println("roque() roque!!!!");
        AbstractPiece king = board[fromY][fromX];
        AbstractPiece rook = board[toY][toX];

        board[fromY][fromX] = new EmptyCell();
        board[toY][toX] = new EmptyCell();

        board[fromY][fromX - 2] = king;
        board[toY][toX + 3] = rook;

        board[fromY][fromX - 2].doMove();
        board[toY][toX + 3].doMove();

        nextPosStk.clear();
        return swapTurn();
    }

    private AbstractPiece pawnSwap() {
        System.out.print("Enter piece to swap with pawn: ");
        String input = new Scanner(System.in).nextLine();
        return switch (input) {
            case "K" -> new Knight(currentTurn);
            case "B" -> new Bishop(currentTurn);
            case "R" -> new Rook(currentTurn);
            default -> new Queen(currentTurn);
        };
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                res.append(this.board[i][j].getType()).append("([x,y][").append(j).append(",").append(i).append("] ");
            }
            res.append("\n");
        }
        return res.toString();
    }
}
