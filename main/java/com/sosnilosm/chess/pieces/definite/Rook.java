package com.sosnilosm.chess.pieces.definite;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;

/**
 * @author Sergei Sosnilo
 */
public class Rook extends AbstractPiece {
    public Rook(Colours colours) {
        super(
                Types.Rook,
                5,
                colours,
                new Move[]{
                        new Move(1, 0), new Move(0, 1),
                        new Move(-1, 0), new Move(0, -1)
                },
                true
        );
    }
}
