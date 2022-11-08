package com.sosnilosm.chess.pieces;

/**
 * @author Sergei Sosnilo
 */
public record Move(int x, int y, boolean firstStepOnly, boolean canTake, boolean onTakeOnly) {
    public Move(int x, int y, boolean firstStepOnly, boolean canTake, boolean onTakeOnly) {
        this.x = x;
        this.y = y;
        this.firstStepOnly = firstStepOnly;
        this.canTake = canTake;
        this.onTakeOnly = onTakeOnly;
    }

    public Move(int x, int y) {
        this(x, y, false, true, false);
    }
}
