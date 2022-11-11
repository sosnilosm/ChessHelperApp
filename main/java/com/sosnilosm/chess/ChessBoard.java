package com.sosnilosm.chess;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;
import com.sosnilosm.chess.pieces.Piece;
import com.sosnilosm.chess.pieces.definite.*;

import java.util.*;

/**
 * @author Sergei Sosnilo
 */
// TODO: fix nextMove() for roque and pawnSwap using PosStkData.MoveType
public class ChessBoard {
    private final AbstractPiece[][] board;
    private Piece.Colours currentTurn = Piece.Colours.White;

    private final Stack<PosStkData> previousPosStk = new Stack<>();
    private final Stack<PosStkData> nextPosStk = new Stack<>();

    private final List<AbstractPiece> allTakenPieces = new ArrayList<>();

    public ChessBoard() {
        board = new AbstractPiece[8][8];
        setBoard();
    }

    private void setBoard(){
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(Piece.Colours.White);
            board[2][i] = new EmptyCell();
            board[3][i] = new EmptyCell();
            board[4][i] = new EmptyCell();
            board[5][i] = new EmptyCell();
            board[6][i] = new Pawn(Piece.Colours.Black);
        }

        //rooks
        board[0][0] = new Rook(Piece.Colours.White);
        board[0][7] = new Rook(Piece.Colours.White);
        board[7][0] = new Rook(Piece.Colours.Black);
        board[7][7] = new Rook(Piece.Colours.Black);

        //knight
        board[0][1] = new Knight(Piece.Colours.White);
        board[0][6] = new Knight(Piece.Colours.White);
        board[7][1] = new Knight(Piece.Colours.Black);
        board[7][6] = new Knight(Piece.Colours.Black);

        //bishop
        board[0][2] = new Bishop(Piece.Colours.White);
        board[0][5] = new Bishop(Piece.Colours.White);
        board[7][2] = new Bishop(Piece.Colours.Black);
        board[7][5] = new Bishop(Piece.Colours.Black);

        //queens
        board[0][3] = new Queen(Piece.Colours.White);
        board[7][3] = new Queen(Piece.Colours.Black);

        //kings
        board[0][4] = new King(Piece.Colours.White);
        board[7][4] = new King(Piece.Colours.Black);
    }

    public AbstractPiece[][] getBoard(){
        return board;
    }

    private boolean isMoveValidInValues(int fromX, int fromY, Move move) {
        return (fromX + move.x() >= 0) && (fromY + move.y() >= 0) && (fromX + move.x() < 8) && (fromY + move.y() < 8);
    }

    private boolean isEmptyCell(int x, int y) {
        return (board[y][x].getType() == Piece.Types.empty);
    }

    public Piece.Colours getCurrentTurn() {
        return this.currentTurn;
    }

    private Piece.Colours swapRealColour(Piece.Colours currentColour) {
        if (currentColour == Piece.Colours.White) {
            return Piece.Colours.Black;
        }
        else if (currentColour == Piece.Colours.Black) {
            return Piece.Colours.White;
        }
        return null;
    }

    private boolean swapTurn() {
        currentTurn = swapRealColour(currentTurn);
        return true;
    }

    private boolean addPosInPreviousStk(int fromX, int fromY, int toX, int toY,
                                        AbstractPiece fromPiece, AbstractPiece toPiece) {
        previousPosStk.push(new PosStkData(fromX, fromY, toX, toY, fromPiece, toPiece));
        return swapTurn();
    }

    private boolean addPosInPreviousStk(int fromX, int fromY, int toX, int toY,
                                        AbstractPiece fromPiece, AbstractPiece toPiece,
                                        PosStkData.MoveType moveType) {
        previousPosStk.push(new PosStkData(fromX, fromY, toX, toY, fromPiece, toPiece, moveType));
        return swapTurn();
    }

    private boolean addPosInNextStk(int fromX, int fromY, int toX, int toY,
                                    AbstractPiece fromPiece, AbstractPiece toPiece) {
        nextPosStk.push(new PosStkData(fromX, fromY, toX, toY, fromPiece, toPiece));
        return swapTurn();
    }

    private boolean addPosInNextStk(int fromX, int fromY, int toX, int toY,
                                    AbstractPiece fromPiece, AbstractPiece toPiece,
                                    PosStkData.MoveType moveType) {
        nextPosStk.push(new PosStkData(fromX, fromY, toX, toY, fromPiece, toPiece, moveType));
        return swapTurn();
    }

    public boolean previousPos() {
        if (!previousPosStk.empty()) {
            PosStkData previousMove = previousPosStk.pop();

            board[previousMove.toY()][previousMove.toX()] = previousMove.toPiece();
            board[previousMove.fromY()][previousMove.fromX()] = previousMove.fromPiece();

            allTakenPieces.remove(previousMove.toPiece());

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

            if (nextMove.toPiece().getType() != Piece.Types.empty) {
                allTakenPieces.add(nextMove.toPiece());
                if (nextMove.fromPiece().getColour() != nextMove.toPiece().getColour()) {
                    board[nextMove.fromY()][nextMove.fromX()] = new EmptyCell();
                }
            }
            else {
                board[nextMove.fromY()][nextMove.fromX()] = nextMove.toPiece();
            }

            board[nextMove.toY()][nextMove.toX()] = nextMove.fromPiece();

            return addPosInPreviousStk(nextMove.fromX(), nextMove.fromY(), nextMove.toX(), nextMove.toY(),
                    nextMove.fromPiece(), nextMove.toPiece());
        }
        else {
            return false;
        }
    }

    public boolean doMove(int fromX, int fromY, int toX, int toY) {
        if (moveCheckForLegal(fromX, fromY, toX, toY)) {
            AbstractPiece fromPieceStk = board[fromY][fromX];
            AbstractPiece toPieceStk = board[toY][toX];

            if (board[toY][toX].getType() != Piece.Types.empty) {
                allTakenPieces.add(board[toY][toX]);
            }

            if (board[fromY][fromX].getType() == Piece.Types.Pawn
                    && ((toY == 0 && board[fromY][fromX].getColour() == Piece.Colours.Black)
                    || (toY == 7 && board[fromY][fromX].getColour() == Piece.Colours.White))
            ) {
                board[toY][toX] = pawnSwap();
            } else {
                board[toY][toX] = board[fromY][fromX];
            }
            board[toY][toX].doMove();
            board[fromY][fromX] = new EmptyCell();

            nextPosStk.clear();
            return addPosInPreviousStk(fromX, fromY, toX, toY, fromPieceStk, toPieceStk);

        }
        return false;
    }

    public boolean doRoque(int fromX, int fromY, int toX, int toY) { // int x, y of Rook to roque
        if (board[fromY][fromX].getColour() == board[toY][toX].getColour()
                && board[fromY][fromX].getType() == Piece.Types.King
                && board[toY][toX].getType() == Piece.Types.Rook
                && board[fromY][fromX].getColour() == currentTurn) {
            if (board[fromY][fromX].isFirstMove() && board[toY][toX].isFirstMove()
                    && !isKingOnCheck(board[fromY][fromX].getColour())) {
                if (fromX < toX) {
                    for (int i = fromX + 1; i < toX; i++) {
                        if (board[fromY][i].getType() != Piece.Types.empty
                                || isCellOnCheck(i, fromY, board[fromY][fromX].getColour())) {
                            return false;
                        }
                    }
                    return shortRoque(fromX, fromY, toX, toY);
                } else if (fromX > toX) {
                    for (int i = toX + 1; i < fromX; i++) {
                        if (board[fromY][i].getType() != Piece.Types.empty
                                || isCellOnCheck(i, fromY, board[fromY][fromX].getColour())) {
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
        AbstractPiece king = board[fromY][fromX];
        AbstractPiece rook = board[toY][toX];

        board[fromY][fromX] = new EmptyCell();
        board[toY][toX] = new EmptyCell();

        board[fromY][fromX + 2] = king;
        board[toY][toX - 2] = rook;

        board[fromY][fromX + 2].doMove();
        board[toY][toX - 2].doMove();

        nextPosStk.clear();
        return addPosInPreviousStk(fromX, fromY, toX, toY, king, rook, PosStkData.MoveType.ShortRoque);
    }

    private boolean longRoque(int fromX, int fromY, int toX, int toY) {
        AbstractPiece king = board[fromY][fromX];
        AbstractPiece rook = board[toY][toX];

        board[fromY][fromX] = new EmptyCell();
        board[toY][toX] = new EmptyCell();

        board[fromY][fromX - 2] = king;
        board[toY][toX + 3] = rook;

        board[fromY][fromX - 2].doMove();
        board[toY][toX + 3].doMove();

        nextPosStk.clear();
        return addPosInPreviousStk(fromX, fromY, toX, toY, king, rook, PosStkData.MoveType.LongRoque);
    }

    // TODO: refactor and delete sout n sin
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

    private boolean moveCheckForLegal(int fromX, int fromY, int toX, int toY) {
        if (board[fromY][fromX].getColour() == currentTurn) {
            if (board[toY][toX].getType() == Piece.Types.empty
                    || (board[toY][toX].getType() != Piece.Types.empty
                    && board[toY][toX].getColour() != board[fromY][fromX].getColour())) {
                for (int[] el : getLegalMoves(fromX, fromY)) {
                    if (Arrays.equals(el, new int[]{toX, toY})) {
                        if (isNotMoveCauseCheck(fromX, fromY, toX, toY)) {
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
        Piece.Colours currentColours = board[fromY][fromX].getColour();

        // while we have valid values for x, y (from where we try to do move)
        while (isMoveValidInValues(fromX, fromY, move)) {
            if (board[fromY + move.y()][fromX + move.x()].getType() == Piece.Types.empty) {
                fromX += move.x();
                fromY += move.y();
                legalMoves.addAll(getLegalNonRepeatable(fromX, fromY, move));
            } else if (board[fromY + move.y()][fromX + move.x()].getColour() != currentColours) {
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

    private List<int[]> getAllLegalMovesAgainstColour(Piece.Colours currentColours) {
        List<int[]> allLegalMovesAgainstColour = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getColour() != Piece.Colours.empty && board[i][j].getColour() != currentColours) {
                    allLegalMovesAgainstColour.addAll(getLegalMoves(j, i));
                }
            }
        }
        return allLegalMovesAgainstColour;
    }

    private int[] findKing(Piece.Colours currentColours) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].getType() == Piece.Types.King && board[i][j].getColour() == currentColours) {
                    return new int[]{j, i};
                }
            }
        }
        return new int[]{};
    }

    private boolean isCellOnCheck(int x, int y, Piece.Colours againstColor) {
        int[] cell = new int[]{x, y};
        for (int[] legalMove : getAllLegalMovesAgainstColour(againstColor)) {
            if (Arrays.equals(cell, legalMove)) {
                return true;
            }
        }
        return false;
    }

    public boolean isKingOnCheck(Piece.Colours colours) {
        if (findKing(colours).length > 0) {
            int[] king = findKing(colours);
            return isCellOnCheck(king[0], king[1], colours);
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

    public boolean isKingOnCheckmate(Piece.Colours currentColours) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getColour() == currentColours) {
                    for (int[] move : getLegalMoves(j, i)) {
                        if (isNotMoveCauseCheck(j, i, move[0], move[1]) &&
                                board[i][j].getColour() != board[move[1]][move[0]].getColour()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isDraw() {
        return (allTakenPieces.size() == 30
                || getAllLegalMovesAgainstColour(swapRealColour(getCurrentTurn())).isEmpty());
    }

    public List<Piece.Types> getTakenPiecesTypesByColour(Piece.Colours currentColour) {
        List<Piece.Types> takenPiecesFromColour = new ArrayList<>();
        currentColour = swapRealColour(currentColour);

        for(AbstractPiece piece : allTakenPieces) {
            if (piece.getColour() == currentColour) {
                takenPiecesFromColour.add(piece.getType());
            }
        }

        return takenPiecesFromColour;
    }

    public int getSuperiorityOfColour(Piece.Colours currentColour) {
        int whiteValue = 0;
        int blackValue = 0;

        for (AbstractPiece[] lineY : board) {
            for(AbstractPiece piece : lineY) {
                if (piece.getColour() == Piece.Colours.White) {
                    whiteValue += piece.getValue();
                } else if (piece.getColour() == Piece.Colours.Black) {
                    blackValue += piece.getValue();
                }
            }
        }

        if (currentColour == Piece.Colours.White) {
            return (whiteValue - blackValue);
        }
        else {
            return  (blackValue - whiteValue);
        }
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                res.append(this.board[i][j].getType()).append(this.board[i][j].getColour()).append("([x,y][").append(j).append(",").append(i).append("] ");
            }
            res.append("\n");
        }
        return res.toString();
    }
}
