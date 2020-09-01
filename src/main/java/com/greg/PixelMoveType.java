package com.greg;

import javax.annotation.CheckForNull;
import java.util.stream.Stream;

public enum PixelMoveType {
    LEFT(pixel -> new Pixel(pixel.getX() - 1, pixel.getY())),
    DOWN(pixel -> new Pixel(pixel.getX(), pixel.getY() + 1)),
    RIGHT(pixel -> new Pixel(pixel.getX() + 1, pixel.getY())),
    UP(pixel -> new Pixel(pixel.getX(), pixel.getY() - 1));

    static {
        UP.firstAttemptMove = LEFT;
        UP.nextMove = RIGHT;
        UP.oppositeMove= DOWN;
        RIGHT.firstAttemptMove = UP;
        RIGHT.nextMove = DOWN;
        RIGHT.nextMove = LEFT;
        DOWN.firstAttemptMove = RIGHT;
        DOWN.nextMove = LEFT;
        DOWN.nextMove = UP;
        LEFT.firstAttemptMove = DOWN;
        LEFT.nextMove = UP;
        LEFT.nextMove = RIGHT;
    }

    private final PixelMove move;
    private PixelMoveType nextMove;
    private PixelMoveType firstAttemptMove;
    private PixelMoveType oppositeMove;

    PixelMoveType(PixelMove move) {
        this.move = move;
    }

    public Pixel movePixel(Pixel pixel) {
        return move.movePixel(pixel);
    }

    public PixelMoveType getNextMoveType() {
        //Necessary as cannot forward reference
        if(nextMove == null) {
            return UP;
        }

        return nextMove;
    }

    public boolean isBacktracking(@CheckForNull PixelMoveType moveType) {
        if(moveType == null) return false;

        return this.oppositeMove == moveType;
    }

    public PixelMoveType getFirstAttemptMove() {
        return firstAttemptMove;
    }
}
