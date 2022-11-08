package com.sosnilosm.chess.pieces.definite;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;

/**
 * @author Sergei Sosnilo
 */
public class King extends AbstractPiece {
    public King(Colour colour) {
        super(
                Types.King,
                Integer.MAX_VALUE,
                colour,
                new Move[]{
                        new Move(1, 0), new Move(0, 1),
                        new Move(-1, 0), new Move(0, -1),
                        new Move(1, 1), new Move(1, -1),
                        new Move(-1, 1), new Move(-1, -1)
                },
                false
        );
    }
}
