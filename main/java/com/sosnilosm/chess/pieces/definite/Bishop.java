package com.sosnilosm.chess.pieces.definite;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;

/**
 * @author Sergei Sosnilo
 */
public class Bishop extends AbstractPiece {
    public Bishop(Colour colour) {
        super(
                Types.Bishop,
                3,
                colour,
                new Move[]{
                        new Move(1, 1), new Move(1, -1),
                        new Move(-1, 1), new Move(-1, -1)},
                true
        );
    }
}
