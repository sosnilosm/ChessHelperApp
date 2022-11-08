package com.sosnilosm.chess.pieces.definite;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;

/**
 * @author Sergei Sosnilo
 */
public class Pawn extends AbstractPiece {
    public Pawn(Colour colour) {
        super(
                Types.Pawn,
                1,
                colour,
                (colour == Colour.Black)
                        ?
                        // if Pawn is White
                        new Move[]{
                                new Move(0, -1, false, false, false),
                                new Move(0, -2, true, false, false),
                                new Move(1, -1, false, true, true),
                                new Move(-1, -1, false, true, true)
                        }
                        :
                        // if Pawn is Black
                        new Move[]{
                                new Move(0, 1, false, false, false),
                                new Move(0, 2, true, false, false),
                                new Move(1, 1, false, true, true),
                                new Move(-1, 1, false, true, true)
                        },
                false
        );
    }
}
