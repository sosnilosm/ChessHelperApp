package com.sosnilosm.chess.pieces.definite;

import com.sosnilosm.chess.pieces.AbstractPiece;
import com.sosnilosm.chess.pieces.Move;

/**
 * @author Sergei Sosnilo
 */
public class EmptyCell extends AbstractPiece {
    public EmptyCell() {
        super(Types.empty, 0, Colours.empty, new Move[]{}, false);
    }
}
