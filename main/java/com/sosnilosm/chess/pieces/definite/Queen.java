package com.sosnilosm.chess.pieces.definite;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;

/**
 * @author Sergei Sosnilo
 */
public class Queen extends AbstractPiece {
    public Queen(Colours colours) {
        super(
                Types.Queen,
                9,
                colours,
                new Move[]{
                        new Move(1, 0), new Move(0, 1),
                        new Move(-1, 0), new Move(0, -1),
                        new Move(1, 1), new Move(1, -1),
                        new Move(-1, 1), new Move(-1, -1)
                },
                true
        );
    }
}
