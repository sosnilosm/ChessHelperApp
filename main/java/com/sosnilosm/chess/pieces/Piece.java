package com.sosnilosm.chess.pieces;

/**
 * @author Sergei Sosnilo
 */
public interface Piece {
    enum Types {
        Bishop,
        King,
        Knight,
        Pawn,
        Queen,
        Rook,
        empty
    }
    enum Colour {
        White,
        Black,
        empty
    }
    AbstractPiece.Types getType();

    AbstractPiece.Colour getColour();

    Move[] getValidMoves();

    void doMove();

    boolean isFirstMove();

    boolean isRepeatableMoves();
}
