package com.sosnilosm.chess;

import com.sosnilosm.chess.pieces.AbstractPiece;

/**
 * @author Sergei Sosnilo
 */
public record MovesStkData(int fromX, int fromY, int toX, int toY, AbstractPiece fromPiece, AbstractPiece toPiece) {

    public MovesStkData(int fromX, int fromY, int toX, int toY, AbstractPiece fromPiece, AbstractPiece toPiece) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.fromPiece = fromPiece;
        this.toPiece = toPiece;
    }
}
