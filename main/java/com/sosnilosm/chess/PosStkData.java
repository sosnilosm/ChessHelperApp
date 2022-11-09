package com.sosnilosm.chess;

import com.sosnilosm.chess.pieces.AbstractPiece;

/**
 * @author Sergei Sosnilo
 */
public record PosStkData(int fromX, int fromY, int toX, int toY, AbstractPiece fromPiece, AbstractPiece toPiece) {

    public PosStkData(int fromX, int fromY, int toX, int toY, AbstractPiece fromPiece, AbstractPiece toPiece) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.fromPiece = fromPiece;
        this.toPiece = toPiece;
    }
}
