package com.sosnilosm.chess.pieces.definite;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;

/**
 * @author Sergei Sosnilo
 */
public class Rook extends AbstractPiece {
    public Rook(Colour colour) {
        super(
                Types.Rook,
                5,
                colour,
                new Move[]{
                        new Move(1, 0), new Move(0, 1),
                        new Move(-1, 0), new Move(0, -1)
                },
                true
        );
    }
}
