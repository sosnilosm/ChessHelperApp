package com.sosnilosm.chess.pieces.definite;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;

/**
 * @author Sergei Sosnilo
 */
public class Knight extends AbstractPiece {
    public Knight(Colours colours) {
        super(
                Types.Knight,
                3,
                colours,
                new Move[]{
                        new Move(2, 1), new Move(1, 2),
                        new Move(2, -1), new Move(-1, 2),
                        new Move(2, -1), new Move(-1, 2),
                        new Move(-2, 1), new Move(1, -2),
                        new Move(-2, -1), new Move(-1, -2),
                        new Move(-2, -1), new Move(-1, -2)
                },
                false
        );
    }
}
