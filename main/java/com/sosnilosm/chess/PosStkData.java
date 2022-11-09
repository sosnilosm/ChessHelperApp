package com.sosnilosm.chess;

import com.sosnilosm.chess.pieces.AbstractPiece;

/**
 * @author Sergei Sosnilo
 */
public record PosStkData(int fromX, int fromY, int toX, int toY, AbstractPiece fromPiece, AbstractPiece toPiece) {

}
