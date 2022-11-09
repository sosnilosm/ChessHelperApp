package com.sosnilosm.chess.pieces.definite;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;

/**
 * @author Sergei Sosnilo
 */
public class Bishop extends AbstractPiece {
    public Bishop(Colours colours) {
        super(
                Types.Bishop,
                3,
                colours,
                new Move[]{
                        new Move(1, 1), new Move(1, -1),
                        new Move(-1, 1), new Move(-1, -1)},
                true
        );
    }
}
