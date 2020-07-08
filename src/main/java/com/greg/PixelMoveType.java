package com.greg;

import java.util.stream.Stream;

public enum PixelMoveType {
    LEFT(pixel -> new Pixel(pixel.getX() - 1, pixel.getY()), null),
    DOWN(pixel -> new Pixel(pixel.getX(), pixel.getY() - 1), LEFT),
    RIGHT(pixel -> new Pixel(pixel.getX() + 1, pixel.getY()), DOWN),
    UP(pixel -> new Pixel(pixel.getX(), pixel.getY() + 1), RIGHT);

    private final PixelMove move;
    private final PixelMoveType nextMove;

    PixelMoveType(PixelMove move, PixelMoveType nextMove) {
        this.move = move;
        this.nextMove = nextMove;
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
}
