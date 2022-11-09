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
    enum Colours {
        White,
        Black,
        empty
    }
    AbstractPiece.Types getType();

    Colours getColour();

    Move[] getValidMoves();

    void doMove();

    boolean isFirstMove();

    boolean isRepeatableMoves();
}
