package com.greg;

public enum PixelMoveType {
    LEFT_UP(pixel -> new Pixel(pixel.getX() -1, pixel.getY() + 1), null),
    LEFT(pixel -> new Pixel(pixel.getX() - 1, pixel.getY()), LEFT_UP),
    DOWN_LEFT(pixel -> new Pixel(pixel.getX() - 1, pixel.getY() - 1), LEFT),
    DOWN(pixel -> new Pixel(pixel.getX(), pixel.getY() - 1), DOWN_LEFT),
    RIGHT_DOWN(pixel -> new Pixel(pixel.getX() + 1, pixel.getY() - 1), DOWN),
    RIGHT(pixel -> new Pixel(pixel.getX() + 1, pixel.getY()), RIGHT_DOWN),
    UP_RIGHT(pixel -> new Pixel(pixel.getX() + 1, pixel.getY() + 1), RIGHT),
    UP(pixel -> new Pixel(pixel.getX(), pixel.getY() + 1), UP_RIGHT);

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
