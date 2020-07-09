package com.greg;

import java.util.stream.Stream;

public enum PixelMoveType {
    LEFT(pixel -> new Pixel(pixel.getX() - 1, pixel.getY())),
    DOWN(pixel -> new Pixel(pixel.getX(), pixel.getY() + 1)),
    RIGHT(pixel -> new Pixel(pixel.getX() + 1, pixel.getY())),
    UP(pixel -> new Pixel(pixel.getX(), pixel.getY() - 1));

    static {
        UP.nextMove = RIGHT;
        UP.firstAttemptMove = LEFT;
        RIGHT.nextMove = DOWN;
        RIGHT.firstAttemptMove = UP;
        DOWN.nextMove = LEFT;
        DOWN.firstAttemptMove = RIGHT;
        LEFT.nextMove = UP;
        LEFT.firstAttemptMove = DOWN;
    }

    private final PixelMove move;
    private PixelMoveType nextMove;
    private PixelMoveType firstAttemptMove;

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

    public PixelMoveType getFirstAttemptMove() {
        return firstAttemptMove;
    }
}
