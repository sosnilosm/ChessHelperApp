package com.sosnilosm.chess.pieces;

/**
 * @author Sergei Sosnilo
 */
public abstract class AbstractPiece implements Piece{
    private final Types type;
    private final int value;
    private final Colour colour;
    private final Move[] validMoves;
    private final boolean repeatableMoves;
    private boolean firstMove;

    public AbstractPiece(Piece.Types type, int value, Piece.Colour colour, Move[] validMoves, boolean repeatableMoves) {
        this.type = type;
        this.value = value;
        this.colour = colour;
        this.validMoves = validMoves;
        this.repeatableMoves = repeatableMoves;
        this.firstMove = true;
    }

    public Piece.Types getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public Piece.Colour getColour() {
        return colour;
    }

    public Move[] getValidMoves() {
        return validMoves;
    }

    public void doMove() {
        this.firstMove = false;
    }

    public boolean isFirstMove() {
        return firstMove;
    }

    public boolean isRepeatableMoves() {
        return repeatableMoves;
    }
}
