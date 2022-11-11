package com.sosnilosm.chess;

import com.sosnilosm.chess.pieces.AbstractPiece;

/**
 * @author Sergei Sosnilo
 */
public record PosStkData(int fromX, int fromY, int toX, int toY, AbstractPiece fromPiece, AbstractPiece toPiece, MoveType moveType) {
    public enum MoveType {
        OrdinaryMove,
        ShortRoque,
        LongRoque,
        pawnSwap
    }

    public PosStkData(int fromX, int fromY, int toX, int toY, AbstractPiece fromPiece, AbstractPiece toPiece) {
        this(fromX, fromY, toX, toY, fromPiece, toPiece, MoveType.OrdinaryMove);
    }
}
